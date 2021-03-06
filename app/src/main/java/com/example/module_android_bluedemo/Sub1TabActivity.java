package com.example.module_android_bluedemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.bth.api.cls.BlueTooth4_C.BLEServices;
import com.bth.api.cls.CommBlueDev;
import com.bth.api.cls.Comm_Bluetooth;
import com.communication.imp.NetPort;
import com.communication.inf.CommunicationType;
import com.function.SPconfig;
import com.function.myAdapter;
import com.silionmodule.AntPower;
import com.silionmodule.Gen2;
import com.silionmodule.Gen2.Q;
import com.silionmodule.Gen2.SessionE;
import com.silionmodule.Gen2.TargetE;
import com.silionmodule.HardWareDetector;
import com.silionmodule.ParamNames;
import com.silionmodule.Reader;
import com.silionmodule.ReaderException;
import com.silionmodule.ReaderType.AntTypeE;
import com.silionmodule.Region.RegionE;
import com.tool.log.LogD;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Sub1TabActivity extends Activity {

	Button button_search, button_connect, button_disconnect,
			button_setblueframe,button_reset,button_test2,button_test3;
	CheckBox cb_blue, cb_ble, cb_rebo;
	ListView listview;
	TextView grid_1, grid_2, grid_3, grid_4,ev,textView_SN;
	TabHost tabHost_connect;

	Map<String, CommBlueDev> Devaddrs = new LinkedHashMap<String, CommBlueDev>();

	private Spinner spinner_conectway, spinner_antports, spinner_module,
			spinner_bluetype;
	private ArrayAdapter arradp_cetway, arradp_antports, arradp_module,
			arradp_bluetype;

	String[] strbltp = { "CJJRJG", "HMSoft", "DXYBT021", "CWBLUE", "IH25","no special" };
	String[] pdaatpot;
	String[] strconectway;
	String[] strmodule = { "slr????????","slr5104","MT100","5304" };
	MyApplication myapp;
	ListAdapter adapter;
	List<Map<String, ?>> list;
	final int UpdateActivity_req=9;
	String filterblset="";

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				// ????????????????
				//showlist();
				Bundle bd=msg.getData();
				boolean isnew =bd.getBoolean("isnew");
				String name=bd.getString("name");
				String address=bd.getString("address");
				String rssi=bd.getString("rssi");
				if(isnew) {
					String count=bd.getString("count");
					
					String type=bd.getString("type");
					
					
					Map<String, String> m = new HashMap<String, String>();
					m.put(Coname[0],count);

					m.put(Coname[1], name+ "\r\n" + address);
					 
					m.put(Coname[2], type);
					m.put(Coname[3], rssi);
					list.add(m);
				}
				else
                for(int i=0;i<list.size();i++)
				{
					Map<String,String> m=(Map<String,String>)list.get(i);
					if(m.get(Coname[1]).endsWith(name + "\r\n" + address))
					{
						m.put(Coname[3], rssi);
					}
				}
				
				((BaseAdapter) adapter).notifyDataSetChanged();
				 
				break;
			}
			case 1:{
				
				if(myapp.CommBth.ConnectState()==Comm_Bluetooth.CONNECTED)
				{
					LogD.LOGD("to Create reader");
					ConnectEvent();
				}
				break;
			}
			}
		}
	};

	private Comm_Bluetooth.SearchCallback Cbscallback = new Comm_Bluetooth.SearchCallback() {

		@Override
		public void OnSearch(CommBlueDev device) {
			// TODO Auto-generated method stub
			Log.d("MYINFO","search:" + device.getName() + " "
					+ device.getAddress()+" "+device.RssiValue());
			
			if(!filterblset.equals("no special"))
			{
				if(!filterblset.equals(device.getName()))
					{  
					   Log.d("MYINFO","filter pass");
					     return;
					}
			}
		 
			boolean isfresh=false,isnew=false;
			synchronized (MainActivity.class) {
				if(Devaddrs.containsKey(device.getAddress()))
				{
					if(Devaddrs.get(device.getAddress()).RssiValue()==device.RssiValue())
					{isfresh=false;
					//Log.d("MYINFO","the same");
					}
					else
					{
						isfresh=true;
					//Log.d("MYINFO","value diff");
					}
				}
				else
				{ isfresh=true;isnew=true;
				//Log.d("MYINFO","no contain");
				}

				if(isfresh) {
					Devaddrs.put(device.getAddress(), device);
					
					//
					/*
					Iterator<Entry<String, CommBlueDev>> iesb = Devaddrs.entrySet()
							.iterator();
					int i = 1;
					while (iesb.hasNext()) {
						CommBlueDev bd = iesb.next().getValue();
						Log.d("MYINFO", "Devaddrs " + String.valueOf(i++) + " "
					 			+ bd.getName() + " " + bd.getAddress()+" "+bd.RssiValue());

					}
					
					*/
				 
					//*
					Bundle bd=new Bundle();
					bd.putBoolean("isnew", isnew);
					bd.putString("name",device.getName());
					bd.putString("address",device.getAddress());
					bd.putString("rssi",String.valueOf(device.RssiValue()));
					bd.putString("count",String.valueOf(Devaddrs.size()));
					bd.putString("type",device.getType() == 1 ? "2.0" : "BLE");
					
					Message msg=new Message();
					msg.what=0;
					msg.setData(bd);
					handler.sendMessage(msg);
					//*/ 
					//((BaseAdapter) adapter).notifyDataSetChanged();
				}
			}
			 
		}
		};

	String[] Coname = new String[] { "sort", "blue", "type", "rssi" };

	private synchronized void showlist() {

		//List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		synchronized (MainActivity.class) {
			list.clear();
			Iterator<Entry<String, CommBlueDev>> iesb = Devaddrs.entrySet()
					.iterator();
			int j = 1;
			while (iesb.hasNext()) {
				CommBlueDev bd = iesb.next().getValue();
				Map<String, String> m = new HashMap<String, String>();
				m.put(Coname[0], String.valueOf(j));
				j++;
				Log.d("MYINFO","List:"+bd.getName()+" "+bd.getAddress()+" "+bd.getType());
				m.put(Coname[1], bd.getName() + "\r\n" + bd.getAddress());
				String typen = bd.getType() == 1 ? "2.0" : "BLE";
				m.put(Coname[2], typen);
				m.put(Coname[3], String.valueOf(bd.RssiValue()));
				list.add(m);
			}
		}
		((BaseAdapter) adapter).notifyDataSetChanged();
		/*
		ListAdapter adapter = new myAdapter(this, list,
				R.layout.listitemview_blue, Coname, new int[] { R.id.View_sort,
						R.id.View_bluetooth, R.id.View_type, R.id.View_rssi });

		// layout??listView????????????????????TextView????????????????????????????
		// ColumnNames??????????????????
		// ??????????????int[]??????????view??????id??????????ColumnNames????????????????view????????TextView
		listview.setAdapter(adapter);
		*/
		//System.out.println("listcount:" + listview.getCount());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1_tablelayout_connect);

		Application app = getApplication();
		myapp = (MyApplication) app; 
		myapp.CommBth = new Comm_Bluetooth(this);
		myapp.Mact=this;
		myapp.spf = new SPconfig(this);
		
		pdaatpot=myapp.pdaatpot;
		strconectway= myapp.strconectway;
		
		strmodule[0]=this
					.getString(R.string.Constr_slrserise);
		
		grid_1 = (TextView) this.findViewById(R.id.textView_sort);
		grid_2 = (TextView) this.findViewById(R.id.textView_bluetooth);
		grid_3 = (TextView) this.findViewById(R.id.textView_type);
		grid_4 = (TextView) this.findViewById(R.id.textView_rssi);
		ev = (TextView) findViewById(R.id.TextView_adr);
		textView_SN=(TextView) this.findViewById(R.id.textView_SN);

		grid_1.setBackgroundColor(Color.BLACK);
		grid_2.setBackgroundColor(Color.BLACK);
		grid_3.setBackgroundColor(Color.BLACK);
		grid_4.setBackgroundColor(Color.BLACK);

		grid_1.setTextColor(Color.WHITE);
		grid_2.setTextColor(Color.WHITE);
		grid_3.setTextColor(Color.WHITE);
		grid_4.setTextColor(Color.WHITE);

		cb_blue = (CheckBox) this.findViewById(R.id.checkBox_bth2);
		cb_ble = (CheckBox) this.findViewById(R.id.checkBox_bthble);
		 
		cb_rebo = (CheckBox) this.findViewById(R.id.checkBox_rematch);
		listview = (ListView) this.findViewById(R.id.listView_params);

		spinner_antports = (Spinner) findViewById(R.id.spinner_antports);
		arradp_antports = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, pdaatpot);
		arradp_antports
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_antports.setAdapter(arradp_antports);

		spinner_bluetype = (Spinner) findViewById(R.id.spinner_bluetype);
		arradp_bluetype = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strbltp);
		arradp_bluetype
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_bluetype.setAdapter(arradp_bluetype);

		spinner_conectway = (Spinner) findViewById(R.id.spinner_connectway);
		arradp_cetway = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strconectway);
		arradp_cetway
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_conectway.setAdapter(arradp_cetway);

		spinner_module = (Spinner) findViewById(R.id.spinner_module);
		arradp_module = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strmodule);
		arradp_module
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_module.setAdapter(arradp_module);
		spinner_module.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				arradp_cetway = new ArrayAdapter<String>(getParent(),
						android.R.layout.simple_spinner_item, strconectway);
				arradp_cetway
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner_conectway.setAdapter(arradp_cetway);
				spinner_antports.setEnabled(true);
				if(arg2==1)
				{
					spinner_conectway.setSelection(0);
					spinner_conectway.setEnabled(true);
				}
				else if(arg2==0)
				{
					arradp_cetway = new ArrayAdapter<String>(getParent(),
							android.R.layout.simple_spinner_item, new String[]{
							strconectway[0],strconectway[2]	});
					arradp_cetway
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        spinner_conectway.setAdapter(arradp_cetway);
					spinner_conectway.setSelection(0);
					spinner_conectway.setEnabled(true);
				}
				else if(arg2==3)
				{
					spinner_antports.setEnabled(false);
				}
				else
					{
					   spinner_conectway.setSelection(0);
					   spinner_conectway.setEnabled(false);
					}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		spinner_bluetype.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				 
					filterblset=strbltp[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		button_search = (Button) this.findViewById(R.id.button_search);
		button_connect = (Button) this.findViewById(R.id.button_connect);
		button_disconnect = (Button) this.findViewById(R.id.button_disconnect);
		button_disconnect.setEnabled(false);
		
		//*
		button_reset = (Button) this.findViewById(R.id.button_reset);
		//button_test2 = (Button) this.findViewById(R.id.button_test2);
		//button_test3 = (Button) this.findViewById(R.id.button_test3);
		 //*/
		
//		String classsearch = myapp.spf.GetString("CLASSSEARCH");
//		if(classsearch!=null&&classsearch.equals("1"))
//			cb_blue.setChecked(true);
//		else
//			cb_blue.setChecked(false);
//		String blesearch = myapp.spf.GetString("BLESEARCH");
//		if(blesearch!=null&&blesearch.equals("1"))
			cb_ble.setChecked(true);
//		else
//			cb_blue.setChecked(false);
		
		String modulestr = myapp.spf.GetString("MODULE");

		String conwaystr = myapp.spf.GetString("CONWAY");

		String bluetype = myapp.spf.GetString("BLTYPE");
		
		String anttype = myapp.spf.GetString("ANTYPE");
		 
		if (!anttype.equals("")) {
			spinner_antports.setSelection(Integer.valueOf(anttype));
		}
		
		if (!modulestr.equals("")) {
			spinner_module.setSelection(Integer.valueOf(modulestr));
		}
		if (!conwaystr.equals("")) {
			spinner_conectway.setSelection(Integer.valueOf(conwaystr));
		}
		if (!bluetype.equals("")) {
			spinner_bluetype.setSelection(Integer.valueOf(bluetype));
		}
		button_search.setOnClickListener(new OnClickListener() {
			@Override
			public synchronized void onClick(View arg0) {
				// TODO Auto-generated method stub
				String txt = button_search.getText().toString().trim();
				if (txt.equals(MyApplication.Constr_search)) {
					synchronized (MainActivity.class) {
						Devaddrs.clear();
					}
					//showlist();
					int selectoption = 0;
					if (cb_blue.isChecked())
						selectoption |= Comm_Bluetooth.SearchOption.Blue
								.value();
					if (cb_ble.isChecked())
						selectoption |= Comm_Bluetooth.SearchOption.BLE.value();
					if (selectoption == 0) {
						Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_plselectsearchblueset,
								Toast.LENGTH_SHORT).show();
						return;
					}

					myapp.spf.SaveString("CLASSSEARCH",cb_blue.isChecked()?"1":"0");

					myapp.spf.SaveString("BLESEARCH",cb_ble.isChecked()?"1":"0");
 
					if(adapter==null)
					{ list = new ArrayList<Map<String, ?>>();
					 
					adapter = new myAdapter(getApplicationContext(), list,
							R.layout.listitemview_blue, Coname, new int[] { R.id.View_sort,
									R.id.View_bluetooth, R.id.View_type, R.id.View_rssi });

					// layout??listView????????????????????TextView????????????????????????????
					// ColumnNames??????????????????
					// ??????????????int[]??????????view??????id??????????ColumnNames????????????????view????????TextView
					listview.setAdapter(adapter);
					}
					else
					{
						list.clear();
						((BaseAdapter) adapter).notifyDataSetChanged();
					}
						
					
					int re = myapp.CommBth.StartSearch(selectoption,
							Cbscallback);

					if (re == 0) {
						Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_startsearchblueok,
								Toast.LENGTH_SHORT).show();

					}else
                    {
						if (re == 1) {
							Toast.makeText(Sub1TabActivity.this,
									MyApplication.Constr_startsearchbluefail1,
									Toast.LENGTH_SHORT).show();

						} else if (re == 2) {
							Toast.makeText(Sub1TabActivity.this,
									MyApplication.Constr_startsearchbluefail2,
									Toast.LENGTH_SHORT).show();

						} else if (re == 3) {
							Toast.makeText(Sub1TabActivity.this,
									MyApplication.Constr_startsearchbluefail12,
									Toast.LENGTH_SHORT).show();

						}
						myapp.CommBth.ResetBlueTooth();
						return;
					}
					button_search.setText(MyApplication.Constr_stop);

				} else {
					myapp.CommBth.StopSearch();
					button_search.setText(MyApplication.Constr_search);

				}

			}
		});

		button_connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				/*
				 
				new Thread(new Runnable(){

						@Override
						public void run() {
							try {
							// TODO Auto-generated method stub
							myapp.CommNet=new NetPort("192.168.1.169");
							myapp.CommNet.Comm_SetAddr("192.168.1.169");
							myapp.CommNet.Comm_SetParam(ParamNames.Communication_mode,
									0);
							myapp.Mreader = Reader.Create(AntTypeE.ONE_ANT,
									myapp.CommNet);
							 
							myapp.connectok=true;
							 
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_connectbluesetfail,
										Toast.LENGTH_SHORT).show();
								myapp.connectok=false;
								return;
							}
							 
						}
						
					
					}).start();
				 
				ConnectHandleUI();
					if(true)
						return;
				//*/
				
				myapp.CommBth.StopSearch();
				button_search.setText(MyApplication.Constr_search);
				
				if (myapp.m == null) {
					Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_scanselectabluereader,
							Toast.LENGTH_SHORT).show();
					return;
				}
				String mes = myapp.m.get("blue");
				String[] messit = mes.split("\r\n");

				String typestr = myapp.m.get("type");
				myapp.Address = messit[1];
				LogD.LOGD("connect:"+messit[0]+" "+messit[1]);
				if (typestr == "2.0") {
					if (cb_rebo.isChecked()) {
						boolean re = myapp.CommBth.CancleMatch(messit[1]);
						Toast.makeText(Sub1TabActivity.this,
								MyApplication.Constr_canclebluematch + String.valueOf(re),
								Toast.LENGTH_SHORT).show();
					}

					if (myapp.CommBth.ToMatch(messit[1]) == BluetoothDevice.BOND_BONDED) {
						// /*
						int re = myapp.CommBth.Connect(messit[1], 2);
                        
						// ??????????????????????????????????
						if (re == 0) {
							Message msg = new Message();
							msg.what = 1;
							Bundle bundle = new Bundle();
							msg.setData(bundle);
							handler.sendMessage(msg);
							//ConnectEvent();
						} else
							Toast.makeText(Sub1TabActivity.this,
									MyApplication.Constr_connectbluesetfail + String.valueOf(re),
									Toast.LENGTH_SHORT).show();
						// */
					} else
						Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_matchbluefail,
								Toast.LENGTH_SHORT).show();
				} else {
					 
					int re = -1;
                                     
					if (myapp.CommBth.ConnectState() != Comm_Bluetooth.CONNECTED)
						re = myapp.CommBth.Connect(messit[1], 4);
					else
						re = 0;
					 
					// ????????
					if (re == 0) {
						String Uuid = "", Uuid_read = "", Uuid_write = "", Uuid_pass = "", Val_pass = "";
						if (spinner_bluetype.getSelectedItemPosition() == spinner_bluetype.getCount()-1) {
							Uuid = myapp.spf.GetString("Uuid");
							Uuid_read = myapp.spf.GetString("Uuid_read");
							Uuid_write = myapp.spf.GetString("Uuid_write");
							Uuid_pass = myapp.spf.GetString("Uuid_pass");
							Val_pass = myapp.spf.GetString("Val_pass");
						} else {
							if (spinner_bluetype.getSelectedItemPosition() == 0
									|| spinner_bluetype
											.getSelectedItemPosition() == 1) {
								Uuid = "0000ffe0-0000-1000-8000-00805f9b34fb";
								Uuid_read = "0000ffe1-0000-1000-8000-00805f9b34fb";
								Uuid_write = "0000ffe1-0000-1000-8000-00805f9b34fb";
							} else if (spinner_bluetype
									.getSelectedItemPosition() == 2) {
								Uuid = "0000fea0-0000-1000-8000-00805f9b34fb";
								Uuid_read = "0000fea2-0000-1000-8000-00805f9b34fb";
								Uuid_write = "0000fea1-0000-1000-8000-00805f9b34fb";
								Uuid_pass = "0000ffa0-0000-1000-8000-00805f9b34fb";
								Val_pass = "0800313131313131";
							} else if (spinner_bluetype
									.getSelectedItemPosition() == 3) {
								Uuid = "00001C00-D102-11E1-9B23-000efB0000A5";
								Uuid_read = "00001C0f-D102-11E1-9B23-000efB0000A5";
								Uuid_write = "00001C01-D102-11E1-9B23-000efB0000A5";
							}
							else if (spinner_bluetype
									.getSelectedItemPosition() == 4) {
								Uuid = "e093f3b5-00a3-a9e5-9eca-40016e0edc24";
								Uuid_read = "e093f3b5-00a3-a9e5-9eca-40026e0edc24";
								Uuid_write = "e093f3b5-00a3-a9e5-9eca-40036e0edc24";
							}
						}

						/*
						 * if(spinner_bluetype.getSelectedItemPosition()==4&&!
						 * Uuid_pass.equals("")&&Val_pass.equals(""))
						 * needpwd=true;
						 * 
						 * boolean issame=false;
						 * 
						 * if(spinner_bluetype.getSelectedItemPosition()==4&&
						 * Addr.equals(myapp.Address)) issame=true;
						 */

						if (Uuid.equals("")
								&& Uuid_read.equals("")
								&& Uuid_write.equals("")
								|| spinner_bluetype.getSelectedItemPosition() == spinner_bluetype.getCount()-1) {

							myapp.BackResult = 0;
							Intent intent = new Intent(Sub1TabActivity.this,
									OprationBLEActivity.class);
							if(myapp.Mode==CommunicationType.EMode.Update.value())
							startActivityForResult(intent, UpdateActivity_req);
							else
								startActivityForResult(intent, 0);
						} else {
							 
							List<BLEServices> lbe = myapp.CommBth
									.FindServices(6000);
							if (lbe != null && lbe.size() > 0) {
								if (!Uuid_pass.equals(""))
									{
									myapp.CommBth.SetServiceUUIDs(Uuid,Uuid_pass);

									if (myapp.CommBth.ToMatch(Val_pass) != 0) {
										Toast.makeText(Sub1TabActivity.this,
												MyApplication.Constr_pwdmatchfail, Toast.LENGTH_SHORT)
												.show();
										return;
									}
								    }
								myapp.CommBth.SetServiceUUIDs(Uuid,
										Uuid_read, Uuid_write);
								
								Toast.makeText(Sub1TabActivity.this,
										MyApplication.Constr_connectblueokthentoreader, Toast.LENGTH_SHORT)
										.show();
								myapp.BackResult = 1;
								ConnectEvent();
								return;
							} else {
								 
								myapp.CommBth.DisConnect();
								
								Toast.makeText(Sub1TabActivity.this,
										MyApplication.Constr_connectblueserfail, Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}

						Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_connectbluesetok,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub1TabActivity.this,
								MyApplication.Constr_connectbluesetfail + String.valueOf(re),
								Toast.LENGTH_SHORT).show();
				}
			}
		});
		button_disconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LogD.LOGD("disconnect_1");
				myapp.CommBth.StopSearch();
				button_search.setText(MyApplication.Constr_search);
				 
				if (myapp.Mreader != null) {
				
					boolean isreset=true;
					 
					/*com.silionmodule.Region.RegionE re = com.silionmodule.Region.RegionE.China;
					
					int[] fre2;
					try {
						myapp.Mreader.paramSet(ParamNames.Reader_Perma_Region_Id, re);
						fre2 = (int[]) myapp.Mreader.paramGet(ParamNames.Reader_Region_HopTable);
						fre2=Sub4TabActivity.Sort(fre2);
						int[] fre3=new int[3];
						for(int i=0;i<fre3.length;i++)
							fre3[i]=fre2[i];
						
						myapp.Mreader.paramSet(ParamNames.Reader_Perma_Region_HopTable,fre3);
					} catch (ReaderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				
					
				    /*
					try {
						if (isreset)
							myapp.Mreader.paramSet(
									ParamNames.Reader_Perma_reset, true);
						else {

							AntPower[] ap=new AntPower[1];
							ap[0]=new AntPower(1,2460,2480);
						 
							myapp.Mreader.paramSet(ParamNames.Reader_Perma_Radio_PortPowerList,null);
							myapp.Mreader.paramSet(ParamNames.Reader_Perma_Radio_PortPowerList, ap);

							com.silionmodule.Region.RegionE re = com.silionmodule.Region.RegionE.NA;
							//myapp.Mreader.paramSet(ParamNames.Reader_Perma_Region_Id, re);

							int[] fre2 = (int[]) myapp.Mreader.paramGet(ParamNames.Reader_Region_HopTable);
							// myapp.Mreader.paramSet(ParamNames.Reader_Region_HopTable,null);
							// myapp.Mreader.paramSet(ParamNames.Reader_Region_HopTable,new int[] { fre2[0], fre2[1], fre2[2],
							// 		fre2[3],fre2[4] });
							fre2=Sub4TabActivity.Sort(fre2);
							int[] fre3=new int[fre2.length-1];
							for(int i=0;i<fre3.length;i++)
								fre3[i]=fre2[i];
							
							//myapp.Mreader.paramSet(ParamNames.Reader_Perma_Region_HopTable,fre3);

							// myapp.Mreader.paramSet(ParamNames.Reader_Perma_Region_HopTime,-1);
							// myapp.Mreader.paramSet(ParamNames.Reader_Perma_Region_HopTime,null);
							//myapp.Mreader.paramSet(ParamNames.Reader_Perma_Region_HopTime, 200);

							// true,false,null
							  myapp.Mreader.paramSet(ParamNames.Reader_Perma_Antenna_CheckPort,true);
							  myapp.Mreader.paramSet(ParamNames.Reader_Perma_Antenna_CheckPort,false);
							 myapp.Mreader.paramSet(ParamNames.Reader_Perma_Antenna_CheckPort,null);

							//myapp.Mreader.paramSet(ParamNames.Reader_Perma_Gen2_Session,com.silionmodule.Gen2.SessionE.Session1);

							//myapp.Mreader.paramSet(ParamNames.Reader_Perma_Gen2_Target,TargetE.B);

							// D,staticQ,null
							myapp.Mreader.paramSet(ParamNames.Reader_Perma_Gen2_Q,new Gen2.DynamicQ());
							myapp.Mreader.paramSet(ParamNames.Reader_Perma_Gen2_Q, new Gen2.StaticQ(7));
							myapp.Mreader.paramSet(ParamNames.Reader_Perma_Gen2_Q,null);
						}
					} catch (ReaderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					//*/

					LogD.LOGD("disconnect_2");
					myapp.Mreader.DisConnect();
				}
				DisConnectHandleUI();
			}
		});
		
		
		button_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			   myapp.CommBth.ResetBlueTooth();
			}
		});
		/*
		button_test2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				myapp.CommBth.DisConnect();
			}
		});
		button_test3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				myapp.CommBth=new Comm_Bluetooth(myapp.Mact);
			}
		});
		//*/
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				@SuppressWarnings({ "unchecked" })
				Map<String, String> m = (Map<String, String>) listview
						.getItemAtPosition(arg2);
				myapp.CommBth.StopSearch();
				button_search.setText(MyApplication.Constr_search);
				myapp.m = m;

				String mes = m.get("blue");
				String[] messit = mes.split("\r\n");
				
				ev.setText(messit[1]);
				// /*
				arg1.setBackgroundColor(Color.GREEN);
				int fv=listview.getFirstVisiblePosition();
				int gc=listview.getChildCount();
				for (int i = 0; i < gc; i++) {
					if (i != arg3-fv) {
						View v = listview.getChildAt(i);
						 
						ColorDrawable cd = (ColorDrawable) v.getBackground();
						 
						if (Color.YELLOW == cd.getColor()) {
							int[] colors = { Color.WHITE,
									Color.rgb(219, 238, 244) };// RGB????
							v.setBackgroundColor(colors[i % 2]);// ????item????????????
						}
					}
				}
				// */
			}
		});

		/*
		 * tabHost_connect.setOnTabChangedListener(new OnTabChangeListener(){
		 * 
		 * @Override public void onTabChanged(String arg0) { // TODO
		 * Auto-generated method stub int j=tabHost_connect.getCurrentTab();
		 * TabWidget tabIndicator=tabHost_connect.getTabWidget(); View
		 * vw=tabIndicator.getChildAt(j); vw.setBackgroundColor(Color.BLUE); int
		 * tc=tabHost_connect.getTabContentView().getChildCount(); for(int
		 * i=0;i<tc;i++) { if(i!=j) { View vw2=tabIndicator.getChildAt(i);
		 * vw2.setBackgroundColor(Color.TRANSPARENT); } }
		 * 
		 * }
		 * 
		 * });
		 */
	}

	private void ConnectEvent() {
		// /*
		boolean isok = false;
		if (myapp.CommBth.getRemoveType() == 4) {
			isok = myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED
					&& myapp.CommBth.IssetUUID() == true
					&& myapp.BackResult == 1;
		} else
			isok = true;

		if (isok) {

			try {
				myapp.Mode =spinner_conectway.getSelectedItemPosition();
				if(spinner_conectway.getCount()==2&&myapp.Mode==1)
					myapp.Mode=2;//????????
				 
				myapp.CommBth.Comm_SetParam(ParamNames.Communication_mode,
						myapp.Mode);
 
				myapp.CommBth.SetFrameParams(20, 100);
				if (myapp.Mode == CommunicationType.EMode.Init.value()) {
					if (spinner_module.getSelectedItemPosition() == 1||
							spinner_module.getSelectedItemPosition() == 0)
						myapp.CommBth.Comm_SetParam(
								ParamNames.Communication_module,
								HardWareDetector.Module_Type.MODOULE_SLR5100);
					else if(spinner_module.getSelectedItemPosition() == 2)
						myapp.CommBth.Comm_SetParam(
								ParamNames.Communication_module,
								HardWareDetector.Module_Type.MODOULE_R902_M1S);
				}
				
				int antc = spinner_antports.getSelectedItemPosition() + 1;
				if(spinner_module.getSelectedItemPosition()==3)
				{
					myapp.CommBth.Comm_SetParam(ParamNames.Communication_Gpioctrants,4);
					myapp.CommBth.Comm_SetParam(ParamNames.Communication_isGpioCtrant, true);
					antc=4;
				}

				myapp.Rparams.antportc = antc;
                long stconnect=System.currentTimeMillis();
				myapp.Mreader = Reader.Create(AntTypeE.valueOf(antc),
						myapp.CommBth);
				LogD.LOGD("connect cost:"+(System.currentTimeMillis()-stconnect));
				
				if(myapp.Mode==0)
				{	AntPower[] ap = (AntPower[]) myapp.Mreader.paramGet(ParamNames.Reader_Radio_PortPowerList);
				Sub4TabActivity.nowpower=ap[0].Readpower();
				
			    String  moduelesn=(String)myapp.Mreader.paramGet(ParamNames.Reader_Version_Hardware);
			    String[] infos=moduelesn.split("-");
			    StringBuilder sb = new StringBuilder();
			    for(int i=0;i<infos[1].length();i+=2)
			    {
			    	String temp=(String) infos[1].subSequence(i, i+2);
			    	sb.append(temp.subSequence(1, 2));
			    }
			    textView_SN.setText(sb.toString());
				}
				 
				//??????????????????
				/*if(myapp.Mode== CommunicationType.EMode.Passivity.value())
				{ int mp;
				mp = (Integer) myapp.Mreader
						.paramGet(ParamNames.Reader_Radio_PowerMax);
				int[][] powr = new int[myapp.Rparams.antportc][2];
				for (int i = 0; i < myapp.Rparams.antportc; i++) {
					powr[i][0] = i + 1;
					powr[i][1] = mp;
				}

				int[][] poww = new int[myapp.Rparams.antportc][2];
				for (int i = 0; i < myapp.Rparams.antportc; i++) {
					poww[i][0] = i + 1;
					poww[i][1] = mp;
				}

				AntPower[] ap = new AntPower[myapp.Rparams.antportc];
				for (int i = 0; i < myapp.Rparams.antportc; i++) {
					ap[i] = new AntPower(i + 1, powr[i][1], poww[i][1]);
				}

				myapp.Mreader.paramSet(
						ParamNames.Reader_Radio_PortPowerList, ap);}*/
				 
				ConnectHandleUI();

			} catch (Exception ex) {
				myapp.CommBth.DisConnect();
				Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_createreaderok,
						Toast.LENGTH_SHORT).show();

			}

		} else {
			myapp.CommBth.DisConnect();
			Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_connectblueserfail,
					Toast.LENGTH_SHORT).show();
		}
		// */
	}

	private void ConnectHandleUI() {
		this.button_search.setEnabled(false);
		this.button_connect.setEnabled(false);
		this.button_disconnect.setEnabled(true);
		if(myapp.CommBth.UUID_SERVICE.equals("0000fea0-0000-1000-8000-00805f9b34fb"))
		{
		myapp.CommBth.SetUUID("0000fea0-0000-1000-8000-00805f9b34fb",
				"0000fea3-0000-1000-8000-00805f9b34fb", false);
		myapp.CommBth.SetUUID("0000fea0-0000-1000-8000-00805f9b34fb",
				"0000fea4-0000-1000-8000-00805f9b34fb", false);
		myapp.CommBth.SetUUID("0000fea0-0000-1000-8000-00805f9b34fb",
				"0000fea5-0000-1000-8000-00805f9b34fb", false);
		myapp.CommBth.SetUUID("0000fea0-0000-1000-8000-00805f9b34fb",
				"0000fea6-0000-1000-8000-00805f9b34fb", false);
		myapp.CommBth.SetUUID("0000fea0-0000-1000-8000-00805f9b34fb",
				"0000fea7-0000-1000-8000-00805f9b34fb", false);
		myapp.spf.SaveString("Blue_Address", myapp.Address);
		}
		UUID uuid;
		uuid = myapp.CommBth.GetUUID(myapp.CommBth.UUID_SERVICE);
		if (uuid != null)
			myapp.spf.SaveString("Uuid", uuid.toString());
		else
			myapp.spf.SaveString("Uuid", "");

		uuid = myapp.CommBth.GetUUID(myapp.CommBth.UUID_READ);
		if (uuid != null)
			myapp.spf.SaveString("Uuid_read", uuid.toString());
		else
			myapp.spf.SaveString("Uuid_read", "");

		uuid = myapp.CommBth.GetUUID(myapp.CommBth.UUID_WRITE);
		if (uuid != null)
			myapp.spf.SaveString("Uuid_write", uuid.toString());
		else
			myapp.spf.SaveString("Uuid_write", "");

		uuid = myapp.CommBth.GetUUID(myapp.CommBth.UUID_PASS);
		if (uuid != null)
			myapp.spf.SaveString("Uuid_pass", uuid.toString());
		else
			myapp.spf.SaveString("Uuid_pass", "");

		myapp.spf.SaveString("Val_pass", myapp.bluepassword);

		myapp.spf.SaveString("MODULE",
				String.valueOf(spinner_module.getSelectedItemPosition()));
		myapp.spf.SaveString("CONWAY",
				String.valueOf(spinner_conectway.getSelectedItemPosition()));
		myapp.spf.SaveString("BLTYPE",
				String.valueOf(spinner_bluetype.getSelectedItemPosition()));
		myapp.spf.SaveString("ANTYPE",
				String.valueOf(spinner_antports.getSelectedItemPosition()));

		TabWidget tw = myapp.tabHost.getTabWidget();
		if (myapp.Mode == CommunicationType.EMode.Passivity.value()) {
			myapp.tabHost.addTab(MainActivity.tab3);
			myapp.tabHost.addTab(MainActivity.tab4_1);
			// tw.getChildAt(3).setEnabled(true);
			// tw.getChildAt(4).setEnabled(false);
		} else if (myapp.Mode == CommunicationType.EMode.Init.value()){
			// myapp.tabHost.addTab(MainActivity.tab3);
			myapp.tabHost.addTab(MainActivity.tab4_2);
			// tw.getChildAt(3).setEnabled(false);
			// tw.getChildAt(4).setEnabled(true);
		}
		 
		if(myapp.Mode==CommunicationType.EMode.Update.value())
		{
			if (myapp.m != null
					&& myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED) {
				Intent intent = new Intent(Sub1TabActivity.this,
						UpdateActivity.class);
				startActivityForResult(intent, UpdateActivity_req);
				 
			}
		}
		else{
		tw.getChildAt(1).setVisibility(View.VISIBLE);
		myapp.connectok=true;
		myapp.tabHost.setCurrentTab(1);
		}
	}

	private void DisConnectHandleUI() {
		this.button_search.setEnabled(true);
		button_disconnect.setEnabled(false);
		button_connect.setEnabled(true);
		 textView_SN.setText("");
		myapp.tabHost.clearAllTabs();
		myapp.tabHost.setCurrentTab(0);
		myapp.tabHost.addTab(MainActivity.tab1);
		myapp.tabHost.addTab(MainActivity.tab2);
		TabWidget tw = myapp.tabHost.getTabWidget();
		tw.getChildAt(1).setVisibility(View.INVISIBLE);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==1)
		ConnectEvent();
		else
			button_disconnect.performClick();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - myapp.exittime) > 2000) {
				Toast.makeText(getApplicationContext(), MyApplication.Constr_Putandexit,
						Toast.LENGTH_SHORT).show();
				myapp.exittime = System.currentTimeMillis();
			} else {
				myapp.CommBth.StopSearch();
				finish();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onStop() {
		super.onStop();
	}
	 
}
