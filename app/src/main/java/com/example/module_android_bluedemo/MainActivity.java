package com.example.module_android_bluedemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.bth.api.cls.Comm_Bluetooth;
import com.communication.inf.CommunicationType;
import com.function.AndroidWakeLock;
import com.function.ScreenListener;
import com.function.ScreenListener.ScreenStateListener;

import com.silionmodule.DataListener;
import com.silionmodule.Functional;
import com.silionmodule.GPioPin;
import com.silionmodule.Gen2.Gen2TagFilter;
import com.silionmodule.Gen2.MemBankE;
import com.silionmodule.ParamNames;
import com.silionmodule.ReaderException;
import com.silionmodule.SimpleReadPlan;
import com.silionmodule.StatusEventListener;
import com.silionmodule.TAGINFO;
import com.silionmodule.TagReadData;
import com.silionmodule.TagProtocol.TagProtocolE;
import com.tool.log.LogD;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.app.Application;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class MainActivity extends TabActivity { // ActionBarActivity

	// 读线程：
	private Thread runThread;
	boolean isrun, issound = true;
	TextView tv_once, tv_state, tv_tags, tv_cost, tv_timcost, tv_statics;
	ExpandableListView tab4_left, tab4_right;

	Button button_read, button_stop, button_clear;
	private ListView listView;

	Map<String, TAGINFO> TagsMap = new LinkedHashMap<String, TAGINFO>();// 有序
	private MyApplication myapp;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	RadioGroup gr_match;
	public static TabHost tabHost;
	public static TabSpec tab1, tab2, tab3, tab4_1, tab4_2, tab5;
	ScreenListener l;
	String[] Coname;

	List<Map<String, ?>> ListMs = new ArrayList<Map<String, ?>>();
	MyAdapter Adapter;

	AndroidWakeLock Awl;
	long statenvtick;

	private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

	// 权限
	boolean right_reg = true;
	// 是否统计
	List<String> VstaticTags = new ArrayList<String>();
	public int Scount = 0;
	Lock lockobj = new ReentrantLock();// 锁
	private Handler vstatichandler = new Handler();
	long vstaticstarttick;
	int vmaxstaticcount;
	int Testcount;
	float Vallcount;
	long Valltime;
	int allrdcnt, avgcnt;
	String strlog;
	

	List<String> Gpodemoauthortags = new ArrayList<String>();
	
	boolean isreadtid=false;//read tid by filter epcid

	public enum Region_Conf {
		RG_NONE(0x0), RG_NA(0x01), RG_EU(0x02), RG_EU2(0X07), RG_EU3(0x08), RG_KR(
				0x03), RG_PRC(0x06), RG_PRC2(0x0A), RG_OPEN(0xFF);

		int p_v;

		Region_Conf(int v) {
			p_v = v;
		}

		public int value() {
			return this.p_v;
		}

		public static Region_Conf valueOf(int value) { // 手写的从int到enum的转换函数
			switch (value) {
			case 0:
				return RG_NONE;
			case 1:
				return RG_NA;
			case 2:
				return RG_EU;
			case 7:
				return RG_EU2;
			case 8:
				return RG_EU3;
			case 3:
				return RG_KR;
			case 6:
				return RG_PRC;
			case 0x0A:
				return RG_PRC2;
			case 0xff:
				return RG_OPEN;
			}
			return null;
		}
	}

	private void fcastatic(String epcid) {

		if (lockobj.tryLock()) {
			try {
				// 处理任务
				Scount++;
				if (!VstaticTags.contains(epcid)) {
					VstaticTags.add(epcid);
				}

			} catch (Exception ex) {

			} finally {
				lockobj.unlock(); // 释放锁
			}
		} else {
			// 如果不能获取锁，则直接做其他事情
		}
	}

	public Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {

				tv_timcost.setText(String.valueOf(System.currentTimeMillis()
						- statenvtick)
						+ "  ");
				Bundle bd = msg.getData();
				TagReadDataParcel[] trds = (TagReadDataParcel[]) bd
						.getParcelableArray("TAGS");
				if (trds != null && trds.length > 0) {
					// soundPool.play(1, 1, 1, 0, 0, 1);
					for (int i = 0; i < trds.length; i++) {
						if (myapp.VisStatics)
							fcastatic(trds[i].EPCHexstr());
						if (!TagsMap.containsKey(trds[i].EPCHexstr())) {
							TAGINFO Ti = new TAGINFO();
							Ti.AntennaID = (byte) trds[i].Antenna();
							Ti.CRC = trds[i].CRC();
							Ti.EmbededData = trds[i].AData();
							Ti.EmbededDatalen = (short) trds[i].AData().length;
							Ti.EpcId = trds[i].EPCbytes();
							Ti.Epclen = (short) trds[i].EPCbytes().length;
							Ti.Frequency = trds[i].Frequency();
							Ti.PC = trds[i].PC();
							Ti.protocol = -1;
							Ti.ReadCnt = trds[i].ReadCount();
							Ti.RSSI = trds[i].RSSI();
							Ti.TimeStamp = (int) trds[i].Time().getTime();
							// LogD.LOGD("ti:timestamp2:"+Ti.TimeStamp);
							TagsMap.put(trds[i].EPCHexstr(), Ti);

							// list
							Map<String, String> m = new HashMap<String, String>();
							m.put(Coname[0], String.valueOf(TagsMap.size()));

							String epcstr = Functional.bytes_Hexstr(Ti.EpcId);
							if (epcstr.length() < 24)
								epcstr = String.format("%-24s", epcstr);

							m.put(Coname[1], epcstr);
							String cs = m.get("次数");
							if (cs == null)
								cs = "0";
							int isc = Integer.parseInt(cs) + Ti.ReadCnt;

							m.put(Coname[2], String.valueOf(isc));
							m.put(Coname[3], String.valueOf(Ti.AntennaID));
							m.put(Coname[4], "");
							m.put(Coname[5], String.valueOf(Ti.RSSI));
							m.put(Coname[6], String.valueOf(Ti.Frequency));

							if (Ti.EmbededDatalen > 0) {
								byte[] out = new byte[Ti.EmbededDatalen];
								System.arraycopy(Ti.EmbededData, 0, out, 0,
										Ti.EmbededDatalen);

								m.put(Coname[7], Functional.bytes_Hexstr(out));
							} else
								m.put(Coname[7], "                 ");

							ListMs.add(m);

						} else {
							// LogD.LOGD("ti:timestamp2:"+(int)trds[i].Time().getTime());
							TAGINFO tf = TagsMap.get(trds[i].EPCHexstr());
							tf.ReadCnt += trds[i].ReadCount();
							tf.RSSI = trds[i].RSSI();
							tf.Frequency = trds[i].Frequency();
							if (trds[i].AData() != null) {
								tf.EmbededDatalen = (short) trds[i].AData().length;
								tf.EmbededData = trds[i].AData();
							}
							String epcstr = trds[i].EPCHexstr();
							if (epcstr.length() < 24)
								epcstr = String.format("%-24s", epcstr);

							for (int k = 0; k < ListMs.size(); k++) {
								@SuppressWarnings("unchecked")
								Map<String, String> m = (Map<String, String>) ListMs
										.get(k);
								if (m.get(Coname[1]).equals(epcstr)) {

									m.put(Coname[2], String.valueOf(tf.ReadCnt));
									m.put(Coname[5], String.valueOf(tf.RSSI));
									m.put(Coname[6],
											String.valueOf(tf.Frequency));
									if (tf.EmbededDatalen > 0) {
										byte[] out = new byte[tf.EmbededDatalen];
										System.arraycopy(tf.EmbededData, 0,
												out, 0, tf.EmbededDatalen);

										m.put(Coname[7],
												Functional.bytes_Hexstr(out));
									}
									break;
								}
							}
						}
					}
				}

				Adapter.notifyDataSetChanged();

				TextView et = (TextView) findViewById(R.id.textView_readoncecnt);
				et.setText(String.valueOf(bd.get("OnceCount")));
				TextView et2 = (TextView) findViewById(R.id.textView_readallcnt);
				et2.setText(String.valueOf(TagsMap.size()));
				break;
			}
			case 1: {
				Bundle bd = msg.getData();
				String mes = (String) bd.get("Msg");
				TextView et = (TextView) findViewById(R.id.textView_invstate);
				if (myapp.isquicklymode)
					button_stop.performClick();

				if (myapp.CommBth.ConnectState() != Comm_Bluetooth.CONNECTED) {
					if (et != null)
						et.setText(" disconnect...reconnect...");
					myapp.CommBth.ReConnect();

				} else {
					if (et != null)
						et.setText(mes);
				}

				break;
			}
			case 2: {
				Bundle bd = msg.getData();
				String mes = (String) bd.get("Msg");
				tv_statics.setText("   " + mes);
			}
			}
		}
	};

	public Handler handler3 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bd = msg.getData();
			switch (msg.what) {
			case 0: {
				String count = bd.get("Msg_cnt").toString();
				tv_once.setText(count);
				tv_tags.setText(bd.get("Msg_all").toString());
				tv_cost.setText(bd.get("Msg_time").toString());
				tv_timcost.setText(bd.get("Msg_run").toString());
				Adapter.notifyDataSetChanged();
				break;

			}
			case 1: {
				button_read.setText(MyApplication.Constr_READ);
				tv_state.setText(bd.get("Msg_error_1").toString());
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				button_stop.performClick();
				break;
			}
			case 2: {
				tv_state.setText(bd.get("Msg_error_2").toString());
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				break;
			}

			}
		}
	};

	StatusEventListener SL = new StatusEventListener() {

		@Override
		public void StatusCatch(Object t) {
			// TODO Auto-generated method stub

			Message msg = new Message();
			msg.what = 1;
			Bundle bundle = new Bundle();
			bundle.putString("Msg", (String) t);
			msg.setData(bundle);
			// 发送消息到Handler
			handler2.sendMessage(msg);
		}

	};

	DataListener DL = new DataListener() {

		@Override
		public void ReadData(TagReadData[] t) {
			// TODO Auto-generated method stub
			TagReadData[] trds = t;
			TagReadDataParcel[] trdsp = new TagReadDataParcel[trds.length];

			if (trds != null && trds.length > 0) {
				soundPool.play(1, 1, 1, 0, 0, 1);
				for (int i = 0; i < trds.length; i++) {
					TagReadDataParcel trdpl = null;
					try {
						trdpl = new TagReadDataParcel(trds[i].EPCbytes(),
								trds[i].Antenna(), trds[i].ReadCount(),
								trds[i].RSSI(), trds[i].Frequency(), trds[i]
										.Time().getTime(), trds[i].AData(),
								trds[i].PC(), trds[i].CRC());
					} catch (ReaderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					trdsp[i] = trdpl;
				}

			}

			Message msg = new Message();
			msg.what = 0;
			Bundle bundle = new Bundle();
			bundle.putInt("OnceCount", trds.length);
			bundle.putParcelableArray("TAGS", trdsp);
			msg.setData(bundle);
			// 发送消息到Handler
			handler2.sendMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int permissionCheck1 = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
		int permissionCheck2 = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int permissionCheck3 = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
		int permissionCheck4 = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
		if (permissionCheck1 != PackageManager.PERMISSION_GRANTED
				|| permissionCheck2 != PackageManager.PERMISSION_GRANTED
				|| permissionCheck3 != PackageManager.PERMISSION_GRANTED
				|| permissionCheck4 != PackageManager.PERMISSION_GRANTED
		)
		{
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
							Manifest.permission.WRITE_EXTERNAL_STORAGE,
							Manifest.permission.ACCESS_COARSE_LOCATION,
							Manifest.permission.ACCESS_FINE_LOCATION},
					124);
		}

		Application app = getApplication();
		myapp = (MyApplication) app;

		setLange();

		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.beep, 1);

		soundPoolerr = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPoolerr.load(this, R.raw.alarm, 1);

		Awl = new AndroidWakeLock(
				(PowerManager) getSystemService(Context.POWER_SERVICE));
		Awl.WakeLock();

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tab1 = tabHost
				.newTabSpec("tab1")
				.setIndicator(MyApplication.Constr_CONNECT,
						getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(new Intent(this, Sub1TabActivity.class));
		tabHost.addTab(tab1);
		tab2 = tabHost.newTabSpec("tab2")
				.setIndicator(MyApplication.Constr_INVENTORY)
				.setContent(R.id.tab2);
		tabHost.addTab(tab2);
		tab3 = tabHost
				.newTabSpec("tab3")
				.setIndicator(
						MyApplication.Constr_RWLOP,
						getResources().getDrawable(
								android.R.drawable.arrow_down_float))
				.setContent(new Intent(this, Sub3TabActivity.class));

		tab4_1 = tabHost
				.newTabSpec("tab4")
				.setIndicator(
						MyApplication.Constr_PASSVICE,
						getResources().getDrawable(
								android.R.drawable.arrow_down_float))
				.setContent(new Intent(this, Sub4TabActivity.class));
		tab4_2 = tabHost
				.newTabSpec("tab5")
				.setIndicator(
						MyApplication.Constr_ACTIVE,
						getResources().getDrawable(
								android.R.drawable.arrow_down_float))
				.setContent(new Intent(this, SubBlueSetTabActivity.class));

		tabHost.setCurrentTab(0);
		TabWidget tw = tabHost.getTabWidget();
		tw.getChildAt(1).setVisibility(View.INVISIBLE);

		/*
		 * Region_Conf rcf1=Region_Conf.valueOf(Integer.valueOf("8")); byte[]
		 * data=new byte[1]; data[0]=(byte)((Region_Conf)rcf1).value();
		 * System.out.println(String.valueOf(data[0]));
		 */

		// myapp.CommBth = new Comm_Bluetooth(this);
		myapp.Mact = this;
		l = new ScreenListener(this);
		l.begin(new ScreenStateListener() {

			@Override
			public void onScreenOn() {
				if (myapp.CommBth != null) {

					// LogD.LOGD("init bluetooth");
					// myapp.CommBth = new Comm_Bluetooth(myapp.Mact);

				}
			}

			@Override
			public void onScreenOff() {

				Log.d("MYINFO", "onScreenoff");
				/*
				 * if(button_stop.isEnabled()) { button_stop.performClick();}
				 * 
				 * if(myapp.Mreader!=null) myapp.Mreader.DisConnect();
				 * 
				 * if(myapp.CommBth!=null) { myapp.CommBth.Comm_Close();
				 * 
				 * } StopHandleUI(); TextView et = (TextView)
				 * findViewById(R.id.textView_invstate); if (et != null)
				 * et.setText("disconnect...Please to reconnect...");
				 */
			}
		});

		Coname = MyApplication.Coname;
		myapp.Rparams = myapp.new ReaderParams();
		myapp.tabHost = tabHost;
		/*
		 * spinner_opbank= (Spinner)findViewById(R.id.spinner_opfbank);
		 * arradp_opbank = new
		 * ArrayAdapter<String>(this,android.R.layout.simple_spinner_item
		 * ,spibank); arradp_opbank.setDropDownViewResource(android.R.layout.
		 * simple_spinner_dropdown_item);
		 * spinner_opbank.setAdapter(arradp_opbank);
		 */

		button_read = (Button) findViewById(R.id.button_start);
		button_stop = (Button) findViewById(R.id.button_stop);
		button_stop.setEnabled(false);
		button_clear = (Button) findViewById(R.id.button_readclear);

		listView = (ListView) findViewById(R.id.listView_epclist);
		gr_match = (RadioGroup) findViewById(R.id.radioGroup_opmatch);

		tv_once = (TextView) findViewById(R.id.textView_readoncecnt);
		tv_state = (TextView) findViewById(R.id.textView_invstate);
		tv_tags = (TextView) findViewById(R.id.textView_readallcnt);
		tv_cost = (TextView) findViewById(R.id.textView_cost);
		tv_timcost = (TextView) findViewById(R.id.textView_costaltime);
		tv_statics = (TextView) findViewById(R.id.textView_cavs);

		for (int i = 0; i < Coname.length; i++)
			h.put(Coname[i], Coname[i]);

		button_read.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				try {
					if (myapp.islatetime) {
						Thread.sleep(myapp.latetimems);
					}

					strlog = "";
					Testcount = 0;
					allrdcnt = 0;
					avgcnt = 0;
					Vallcount = 0;
					Valltime = 0;
					

					myapp.CommBth.Comm_SetParam("Test_log", 0);

					TextView et = (TextView) findViewById(R.id.textView_invstate);
					if (et != null)
						et.setText("...");

					if (Adapter == null) {
						Map<String, String> h = new HashMap<String, String>();
						for (int i = 0; i < Coname.length; i++)
							h.put(Coname[i], Coname[i]);
						ListMs.add(h);
						Adapter = new MyAdapter(getApplicationContext(),
								ListMs, R.layout.listitemview_inv, Coname,
								new int[] { R.id.textView_readsort,
										R.id.textView_readepc,
										R.id.textView_readcnt,
										R.id.textView_readant,
										R.id.textView_readpro,
										R.id.textView_readrssi,
										R.id.textView_readfre,
										R.id.textView_reademd });

						listView.setAdapter(Adapter);
					}
					myapp.Mreader.addStatusListener(SL);

					if (myapp.Mode == CommunicationType.EMode.Passivity.value()) {
						SimpleReadPlan srp = new SimpleReadPlan(
								myapp.Rparams.uants);

						try {

							if (myapp.Rparams.To != null
									|| myapp.Rparams.Tf != null) {
								srp = new SimpleReadPlan(myapp.Rparams.uants,
										TagProtocolE.Gen2, myapp.Rparams.Tf,
										myapp.Rparams.To, 10);
							}

							myapp.Mreader.paramSet(ParamNames.Reader_Read_Plan,
									srp);

							/* 取消检测天线 */
							/*
							 * new Thread(new Runnable(){
							 * 
							 * public void run() { try { //
							 */
							myapp.Mreader.paramSet(
									ParamNames.Reader_Antenna_CheckPort, false);
							/*
							 * } catch (ReaderException e) { // TODO
							 * Auto-generated catch block e.printStackTrace(); }
							 * }}).start(); //
							 */

							if (myapp.isquicklymode) {
								myapp.Mreader.addDataListener(DL);
								myapp.Mreader
										.AsyncStartReading(myapp.Rparams.option);

								myapp.isread = true;
								isreading = true;
								ReadHandleUI();
							} else {
								isrun = true;
								runThread = new Thread(runnable);
								runThread.start();

								isreading = true;
								myapp.isread = true;
							}
							ReadHandleUI();

						} catch (ReaderException e) {
							Toast.makeText(
									MainActivity.this,
									MyApplication.Constr_SetFaill
											+ e.GetMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
					} else if (myapp.Mode == CommunicationType.EMode.Init
							.value()) {
						try {
							myapp.Mreader.addDataListener(DL);
							myapp.Mreader.StartTagEvent();
							myapp.isread = true;
							isreading = true;
							ReadHandleUI();

						} catch (ReaderException e) {
							// TODO Auto-generated catch block
							Toast.makeText(
									MainActivity.this,
									MyApplication.Constr_SetFaill
											+ e.GetMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
					}

					statenvtick = System.currentTimeMillis();
					vstaticstarttick = System.currentTimeMillis();
					if (myapp.VisStatics)
						vstatichandler.post(runnable_statics);

				} catch (Exception ex) {
					Toast.makeText(MainActivity.this,
							MyApplication.Constr_SetFaill + ex.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		button_stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (myapp.VisStatics)
					vstatichandler.removeCallbacks(runnable_statics);

				StopHandle();
				myapp.isread = false;
				myapp.TagsMap.putAll(TagsMap);
			}
		});

		button_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Adapter != null) {
					TagsMap.clear();
					ListMs.clear();
					myapp.TagsMap.clear();
					// showlist();

					ListMs.add(h);
					Adapter.notifyDataSetChanged();
				}

				TextView et = (TextView) findViewById(R.id.textView_readoncecnt);
				et.setText("0");

				TextView et2 = (TextView) findViewById(R.id.textView_readallcnt);
				et2.setText("0");

				TextView et3 = (TextView) findViewById(R.id.textView_invstate);
				et3.setText("...");

				TextView et4 = (TextView) findViewById(R.id.textView_cost);
				et4.setText("0");

				tv_timcost.setText("0");
				tv_statics.setText("   ");
				myapp.Rparams.Curepc = "";

				// Test_paramlist();
			}
		});
		this.listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				arg1.setBackgroundColor(Color.YELLOW);

				EditText et = (EditText) findViewById(R.id.editText_opfilterdata);
				EditText et2 = (EditText) findViewById(R.id.editText_opfilsadr);
				HashMap<String, String> hm = (HashMap<String, String>) listView
						.getItemAtPosition(arg2);
				String epc = hm.get("EPC ID");
				myapp.Rparams.Curepc = epc.trim();
				
				if(myapp.gpodemomode!=0)
				showPopupMenu(arg1);
				// et.setText(epc);
				// et2.setText("32");
				// gr_match.check(gr_match.getChildAt(0).getId());
				// spinner_opbank.setSelection(1);

				for (int i = 0; i < listView.getCount(); i++) {
					if (i != arg2) {
						View v = listView.getChildAt(i);
						if (v != null) {
							ColorDrawable cd = (ColorDrawable) v
									.getBackground();
							if (Color.YELLOW == cd.getColor()) {
								int[] colors = { Color.WHITE,
										Color.rgb(219, 238, 244) };// RGB颜色
								v.setBackgroundColor(colors[i % 2]);// 每隔item之间颜色不同
							}
						} else {
							break;
						}
					}
				}
			}

		});

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				int j = tabHost.getCurrentTab();
				if (tabHost.getTabWidget().getChildCount() == 4) {
					if (j == 2) {
						Sub3TabActivity.EditText_sub3fildata
								.setText(myapp.Rparams.Curepc);
						Sub3TabActivity.EditText_sub3wdata
								.setText(myapp.Rparams.Curepc);
					} else if (j == 1) {
						if (myapp.connectok) {
							myapp.connectok = false;
							tv_state.setText("connect sucessful");
						}
					}
				}

			}
		});
		setLange();
		/*
		 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android M
		 * Permission check
		 * 
		 * if
		 * (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
		 * != PackageManager.PERMISSION_GRANTED) { requestPermissions(new
		 * String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
		 * PERMISSION_REQUEST_COARSE_LOCATION); }
		 * 
		 * 
		 * } //
		 * 
		 * private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
		 * 
		 * private void setLocationService() { Intent locationIntent = new
		 * Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		 * this.startActivityForResult(locationIntent,
		 * REQUEST_CODE_LOCATION_SETTINGS); }
		 */

		startActivity(new Intent(this, Sub3TabActivity.class));


	}

	public void onRequestPermissionsResult(int requestCode,
			String permissions[], int[] grantResults) {
		switch (requestCode) {
		case PERMISSION_REQUEST_COARSE_LOCATION:
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// TODO request success
			}
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - myapp.exittime) > 2000) {
				Toast.makeText(getApplicationContext(),
						MyApplication.Constr_Putandexit, Toast.LENGTH_SHORT)
						.show();
				myapp.exittime = System.currentTimeMillis();
			} else {
				finish();
				// System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	void StopHandle() {
		isreading = false;
		if (myapp.Mreader != null)
			myapp.Mreader.removeStatusListener(SL);
		if (myapp.Mode == CommunicationType.EMode.Passivity.value()) {

			isrun = false;
			if (myapp.isquicklymode) {
				// new Thread(new Runnable(){
				// public void run() {

				int re = myapp.Mreader.AsyncStopReading();

				// }}).start();

				myapp.Mreader.removeDataListener(DL);
			} else {

				try {
					if (runThread != null)
						runThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			StopHandleUI();
		} else {
			try {
				if (myapp.Mreader != null) {
					myapp.Mreader.EndTagEvent();
					myapp.Mreader.removeDataListener(DL);
				}
				StopHandleUI();
			} catch (ReaderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	Map<String, String> h = new HashMap<String, String>();

	private void triggerGPO() {
		try {
			GPioPin[] gps = new GPioPin[1];

			if (myapp.gpodemo1) {
				gps[0] = new GPioPin(1, true);
				myapp.Mreader.GPOSet(gps);
			}
			if (myapp.gpodemo2) {
				gps[0] = new GPioPin(2, true);
				myapp.Mreader.GPOSet(gps);
			}
			if (myapp.gpodemo3) {
				gps[0] = new GPioPin(3, true);
				myapp.Mreader.GPOSet(gps);
			}
			if (myapp.gpodemo4) {
				gps[0] = new GPioPin(4, true);
				myapp.Mreader.GPOSet(gps);
			}

			try {
				Thread.sleep(myapp.gpodemotime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (myapp.gpodemo1) {
				gps[0] = new GPioPin(1, false);
				myapp.Mreader.GPOSet(gps);
			}
			if (myapp.gpodemo2) {
				gps[0] = new GPioPin(2, false);
				myapp.Mreader.GPOSet(gps);
			}
			if (myapp.gpodemo3) {
				gps[0] = new GPioPin(3, false);
				myapp.Mreader.GPOSet(gps);
			}
			if (myapp.gpodemo4) {
				gps[0] = new GPioPin(4, false);
				myapp.Mreader.GPOSet(gps);
			}
		} catch (ReaderException rex) {

		}
	}

	private Runnable runnable = new Runnable() {
		public void run() {

			while (isrun) {
				synchronized (this) {
					if (isreading) {
						if (myapp.connectok
								|| myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED) {
							TagReadData[] trds = null;
							long st = System.currentTimeMillis();
							try {
								trds = myapp.Mreader
										.Read((int) (myapp.Rparams.readtime));
								/*
								 * if(st%2==0) { trds = myapp.Mreader
								 * .Read((int) (myapp.Rparams.readtime+st%100));
								 * } else {
								 * myapp.CommBth.Comm_SetParam("Test_log", 0);
								 * LogD.LOGD("asyn starting");
								 * myapp.Mreader.addDataListener(DL);
								 * myapp.Mreader
								 * .AsyncStartReading(myapp.Rparams.option);
								 * Thread.sleep(st%5000+myapp.Rparams.readtime);
								 * LogD
								 * .LOGD("asyn stoping:"+(st%5000+myapp.Rparams
								 * .readtime));
								 * myapp.Mreader.AsyncStopReading();
								 * myapp.Mreader.removeDataListener(DL);
								 * LogD.LOGD("asyn had stop"); }
								 * /*myapp.CommBth.Comm_SetParam("Test_log", 1);
								 * Object
								 * obj2=myapp.Mreader.paramGet(ParamNames.
								 * Reader_Version_Hardware); Object
								 * obj3=myapp.Mreader
								 * .paramGet(ParamNames.Reader_Version_Software
								 * );
								 * 
								 * //test get param null Object
								 * obj1=myapp.Mreader
								 * .paramGet(ParamNames.Reader_Radio_Temperature
								 * ); int[] tablefre = (int[]) myapp.Mreader
								 * .paramGet(ParamNames.Reader_Region_HopTable);
								 * //obj1=null;
								 * if(obj1==null||tablefre==null||obj2
								 * ==null||obj3==null) { Message msg = new
								 * Message(); msg.what = 2; Bundle bundle = new
								 * Bundle(); bundle.putString( "Msg_error_2",
								 * "error:null"); msg.setData(bundle);
								 * 
								 * handler3.sendMessage(msg); }
								 */

								if (trds != null && trds.length > 0) {

									TagReadDataParcel[] trdsp = new TagReadDataParcel[trds.length];
									soundPool.play(1, 1, 1, 0, 0, 1);
									for (int i = 0; i < trds.length; i++) {
										TagReadDataParcel trdpl = null;
										
										if(isreadtid)
										{
											// 过滤读tid----------------filter read tid
											
											int count = 0;
											while (true) {
												try {
													Gen2TagFilter g2tf = null;
													byte[] fdata = Functional.hexstr_Bytes(trds[i].EPCHexstr());

													g2tf = new Gen2TagFilter(MemBankE.EPC, 32,fdata,fdata.length * 8);
													myapp.Mreader.paramSet(ParamNames.Reader_Tagop_Antenna,trds[i].Antenna());
													short[] epddata = myapp.Mreader.ReadTagMemWords(g2tf,MemBankE.TID,0, 6);
												 
													trdpl = new TagReadDataParcel(
															trds[i].EPCbytes(),
															trds[i].Antenna(),
															trds[i].ReadCount(),
															trds[i].RSSI(),
															trds[i].Frequency(),
															trds[i].Time().getTime(),
															Functional.hexstr_Bytes(Functional.shorts_HexStr(epddata)),
															trds[i].PC(), trds[i].CRC());
													
													break;
													 
												} catch (ReaderException ex) {
													LogD.LOGD(ex.GetMessage());
												}
												if (count++ > 1)
												{
													trdpl = new TagReadDataParcel(
															trds[i].EPCbytes(),
															trds[i].Antenna(),
															trds[i].ReadCount(),
															trds[i].RSSI(),
															trds[i].Frequency(),
															trds[i].Time().getTime(),
															new byte[0],
															trds[i].PC(), trds[i].CRC());
													break;
												}
											} 
											 
										}
										else{
										try {
											trdpl = new TagReadDataParcel(
													trds[i].EPCbytes(),
													trds[i].Antenna(),
													trds[i].ReadCount(),
													trds[i].RSSI(),
													trds[i].Frequency(),
													trds[i].Time().getTime(),
													trds[i].AData(),
													trds[i].PC(), trds[i].CRC());
										} catch (ReaderException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										}
										trdsp[i] = trdpl;
									}
									Message msg = new Message();
									msg.what = 0;
									Bundle bundle = new Bundle();
									bundle.putInt("OnceCount", trds.length);
									bundle.putParcelableArray("TAGS", trdsp);
									msg.setData(bundle);
									// 发送消息到Handler
									handler2.sendMessage(msg);

									// gpodemo
									if (myapp.gpodemomode == 1) {

										for (int i = 0; i < trds.length; i++) {
											if (Gpodemoauthortags
													.contains(trds[i]
															.EPCHexstr())) {
												GPioPin[] gps = new GPioPin[1];
												gps[0] = new GPioPin(1, true);
												myapp.Mreader.GPOSet(gps);

												gps[0] = new GPioPin(2, true);
												myapp.Mreader.GPOSet(gps);

												Thread.sleep(1000);

												gps[0] = new GPioPin(1, false);
												myapp.Mreader.GPOSet(gps);

												gps[0] = new GPioPin(2, false);
												myapp.Mreader.GPOSet(gps);
											} else {
												for (int s = 0; s < 3; s++) {
													GPioPin[] gps = new GPioPin[1];
													gps[0] = new GPioPin(1,
															true);
													myapp.Mreader.GPOSet(gps);

													Thread.sleep(20);

													gps[0] = new GPioPin(1,
															false);
													myapp.Mreader.GPOSet(gps);
												}
											}

										}
									} else if (myapp.gpodemomode == 2) {
										for (int i = 0; i < trds.length; i++) {
											if (Gpodemoauthortags
													.contains(trds[i]
															.EPCHexstr())) {
												GPioPin[] gps = new GPioPin[1];
												gps[0] = new GPioPin(1, true);
												myapp.Mreader.GPOSet(gps);

												Thread.sleep(100);

												gps[0] = new GPioPin(1, false);
												myapp.Mreader.GPOSet(gps);

											} else {
												GPioPin[] gps = new GPioPin[1];
												gps[0] = new GPioPin(2, true);
												myapp.Mreader.GPOSet(gps);

												Thread.sleep(300);

												gps[0] = new GPioPin(2, false);
												myapp.Mreader.GPOSet(gps);
											}

										}
									} else if (myapp.gpodemomode == 3) {

										for (int i = 0; i < trds.length; i++) {
											if (Gpodemoauthortags
													.contains(trds[i]
															.EPCHexstr())) {
												triggerGPO();
											}
										}

									} else if (myapp.gpodemomode == 4) {

										for (int i = 0; i < trds.length; i++) {
											if (!Gpodemoauthortags
													.contains(trds[i]
															.EPCHexstr())) {
												triggerGPO();
											}
										}
									}
								}

							} catch (ReaderException rex) {
								Message msg2 = new Message();
								msg2.what = 1;
								Bundle bundle2 = new Bundle();
								bundle2.putString("Msg_error_1",
										"error:" + rex.GetMessage());
								msg2.setData(bundle2);
								handler3.sendMessage(msg2);
								// isrun = false;
								LogD.LOGD(rex.GetMessage());

							} catch (Exception ex) {

								Message msg = new Message();
								msg.what = 2;
								Bundle bundle = new Bundle();
								bundle.putString(
										"Msg_error_2",
										"error:" + ex.toString()
												+ ex.getMessage());
								msg.setData(bundle);
								LogD.LOGD(ex.toString() + ex.getMessage());
								handler3.sendMessage(msg);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								continue;

							}

							if (trds != null && trds.length > 0) {
								Message msg = new Message();
								Bundle bundle = new Bundle();
								msg.what = 0;
								bundle.putString("Msg_cnt",
										(String.valueOf(trds.length)));
								int vl = (int) (System.currentTimeMillis() - st);
								int v2 = (int) (System.currentTimeMillis() - statenvtick);
								bundle.putString("Msg_time",
										(String.valueOf(vl)));
								bundle.putString("Msg_run",
										(String.valueOf(v2)));

								synchronized (this) {
									bundle.putString("Msg_all",
											(String.valueOf(TagsMap.size())));
								}
								msg.setData(bundle);
								handler3.sendMessage(msg);

							}

							try {
								Thread.sleep(myapp.Rparams.sleep);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
			}
		}
	};

	private Runnable runnable_statics = new Runnable() {
		public void run() {

			long readtime = System.currentTimeMillis() - statenvtick;
			// 每秒统计
			// *
			long evetime = System.currentTimeMillis() - vstaticstarttick;
			if (evetime >= 1000) {
				int tagcountunit = 0;
				if (lockobj.tryLock()) {
					try {
						// 处理任务
						tagcountunit = VstaticTags.size();
						VstaticTags.clear();

						strlog += "  	    ,";
						String temp = Scount + "/" + tagcountunit + "/"
								+ String.valueOf(TagsMap.size());
						temp = String.format("%-12s", temp);
						strlog += "  	" + temp + "   ,";
						strlog += "   " + String.valueOf(readtime) + "   ,\r\n";
						Scount = 0;

						// myapp.fs.write(strlog.getBytes());
					} catch (Exception ex) {

					} finally {
						lockobj.unlock(); // 释放锁

						// 标签总次数 平均次数
						Iterator<Entry<String, TAGINFO>> iesb;
						iesb = TagsMap.entrySet().iterator();

						while (iesb.hasNext()) {
							TAGINFO bd = iesb.next().getValue();
							allrdcnt += bd.ReadCnt;
						}
						if (TagsMap.size() > 0)
							avgcnt = allrdcnt / TagsMap.size();

						if (tagcountunit > vmaxstaticcount)
							vmaxstaticcount = tagcountunit;
						String text = " a:" + String.valueOf(tagcountunit)
								+ "/s m:" + String.valueOf(vmaxstaticcount)
								+ "/s " + " 总次数:" + String.valueOf(allrdcnt)
								+ " 平均次数:" + String.valueOf(avgcnt);
						// LogD.LOGD(" 总次数:"+String.valueOf(allrdcnt)+" 平均次数:"+String.valueOf(avgcnt));
						Message msg = new Message();
						msg.what = 2;
						Bundle bundle = new Bundle();
						bundle.putString("Msg", text);

						msg.setData(bundle);
						avgcnt = allrdcnt = 0;
						// 发送消息到Handler
						handler2.sendMessage(msg);
						vstaticstarttick = System.currentTimeMillis();
					}
				} else {
					// 如果不能获取锁，则直接做其他事情
				}
			}

			// */

			if (myapp.isstoptime && readtime > myapp.stoptimems) {

				if (myapp.VisTestFor) {
					Testcount++;

					Vallcount += TagsMap.size();
					Valltime += readtime;

					strlog += "  	" + String.valueOf(Testcount) + "   ,";
					String temp = Scount + "/" + VstaticTags.size() + "/"
							+ String.valueOf(TagsMap.size());
					temp = String.format("%-12s", temp);
					strlog += "  	" + temp + "   ,";

					strlog += "   " + String.valueOf(readtime) + "   ,\r\n\r\n";
					VstaticTags.clear();
					Scount = 0;

					/*
					 * try { myapp.fs.write(strlog.getBytes()); } catch
					 * (IOException e1) { // TODO Auto-generated catch block
					 * e1.printStackTrace(); } //
					 */

					if (Testcount >= myapp.Vtestforcount) {
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy年MM月dd日   HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());

						String linestr = "SLR1200  ";

						int cnp = Sub4TabActivity.nowpower;

						linestr += String.valueOf(cnp);
						linestr += "  平均标签数量："
								+ String.valueOf((int) Vallcount
										/ myapp.Vtestforcount)
								+ "\r\n平均耗时："
								+ String.valueOf((int) Valltime
										/ myapp.Vtestforcount) + "  \r\n日期："
								+ formatter.format(curDate) + "\r\n\r\n\r\n";
						try {
							strlog += linestr;
							myapp.fs.write(strlog.getBytes());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						Vallcount = 0;
						Valltime = 0;
						button_stop.performClick();
						return;
					} else {
						StopHandle();
						myapp.isread = false;

						if (myapp.VisStatics)
							vstatichandler.removeCallbacks(runnable_statics);

						// 标签总次数 平均次数
						Iterator<Entry<String, TAGINFO>> iesb;
						iesb = TagsMap.entrySet().iterator();

						while (iesb.hasNext()) {
							TAGINFO bd = iesb.next().getValue();
							allrdcnt += bd.ReadCnt;
						}

						if (TagsMap.size() > 0)
							avgcnt = allrdcnt / TagsMap.size();

						TagsMap.clear();
						ListMs.clear();
						ListMs.add(h);

						Adapter.notifyDataSetChanged();

						try {
							Thread.sleep(myapp.Vtestforsleep);
						} catch (InterruptedException e) {
						}

						if (myapp.isquicklymode) {
							myapp.Mreader.addStatusListener(SL);
							myapp.Mreader.addDataListener(DL);
							myapp.Mreader
									.AsyncStartReading(myapp.Rparams.option);
							myapp.isread = true;
							isreading = true;

						} else {
							isrun = true;
							runThread = new Thread(runnable);
							runThread.start();

						}

						isreading = true;
						myapp.isread = true;

						statenvtick = System.currentTimeMillis();

						vstaticstarttick = System.currentTimeMillis();
						if (myapp.VisStatics)
							vstatichandler.post(runnable_statics);

						ReadHandleUI();
					}
				} else {
					button_stop.performClick();
					handler3.removeCallbacks(runnable);
				}
			} else
				vstatichandler.postDelayed(this, myapp.Rparams.sleep);

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (myapp.isread) {
			Toast.makeText(MainActivity.this, MyApplication.Constr_stopscan,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		int id = item.getItemId();

		if (id == R.id.action_debug) {

			if (myapp.m != null
					&& myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED) {
				Intent intent = new Intent(MainActivity.this,
						SubDebugActivity.class);
				startActivityForResult(intent, 0);
				return true;
			}
			Toast.makeText(MainActivity.this,
					MyApplication.Constr_scanselectabluereaderandconnect,
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (id == R.id.action_system) {
			if (myapp.m != null) {
				Intent intent = new Intent(MainActivity.this,
						SubSystemActivity.class);
				startActivityForResult(intent, 0);
				return true;
			}

			Toast.makeText(MainActivity.this,
					MyApplication.Constr_scanselectabluereader,
					Toast.LENGTH_SHORT).show();
			return true;
		} else if (id == R.id.action_cw) {
			if (myapp.m != null) {
				Intent intent = new Intent(MainActivity.this,
						SubCarryWaveActivity.class);
				startActivityForResult(intent, 0);
			}

		} else if (id == R.id.action_reg) {

			if (myapp.m != null
					&& myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED) {

				if (right_reg == false) {
					Toast.makeText(MainActivity.this, "no permissions",
							Toast.LENGTH_SHORT).show();
					return false;
				}

				Intent intent = new Intent(MainActivity.this,
						SubRegopActivity.class);
				startActivityForResult(intent, 0);
				return true;
			}
			Toast.makeText(MainActivity.this,
					MyApplication.Constr_scanselectabluereader,
					Toast.LENGTH_SHORT).show();
			return true;
		} else if (id == R.id.action_custom) {
			if (myapp.m != null
					&& myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED) {
				Intent intent = new Intent(MainActivity.this,
						SubCustomActivity.class);
				startActivityForResult(intent, 0);
				return true;
			}
			Toast.makeText(MainActivity.this,
					MyApplication.Constr_scanselectabluereaderandconnect,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		/*
		 * else if (id == R.id.action_update) { if (myapp.m != null &&
		 * myapp.CommBth.ConnectState() == Comm_Bluetooth.CONNECTED) { Intent
		 * intent = new Intent(MainActivity.this, UpdateActivity.class);
		 * startActivityForResult(intent, 0); return true; }
		 * Toast.makeText(MainActivity.this,
		 * MyApplication.Constr_scanselectabluereaderandconnect,
		 * Toast.LENGTH_SHORT).show(); return false; }
		 */
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	private void ReadHandleUI() {
		this.button_read.setEnabled(false);
		this.button_stop.setEnabled(true);
		TabWidget tw = myapp.tabHost.getTabWidget();
		tw.getChildAt(0).setEnabled(false);
		tw.getChildAt(2).setEnabled(false);
		if (myapp.Mode == CommunicationType.EMode.Passivity.value())
			tw.getChildAt(3).setEnabled(false);

	}

	private void StopHandleUI() {
		button_read.setEnabled(true);
		button_stop.setEnabled(false);
		TabWidget tw = myapp.tabHost.getTabWidget();
		tw.getChildAt(0).setEnabled(true);
		if (tw.getChildCount() > 2)
			tw.getChildAt(2).setEnabled(true);
		if (tw.getChildCount() > 3
				&& myapp.Mode == CommunicationType.EMode.Passivity.value())
			tw.getChildAt(3).setEnabled(true);
	}

	/*
	 * protected void onPause() {
	 * 
	 * long now=System.currentTimeMillis();
	 * if(!(myapp.exittime<now&&now-myapp.exittime<2000)) { myapp.exittime=now;
	 * Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
	 * return; }
	 * 
	 * super.onPause(); }
	 * 
	 * protected void onResume() { //this.setVisible(true);
	 * 
	 * super.onResume(); }
	 */

	protected void onDestroy() {

		if (button_read.isEnabled())
			StopHandle();

		if (myapp.Mreader != null)
			myapp.Mreader.DisConnect();
		// /*
		if (myapp.CommBth.getRemoveType() == 4
				&& myapp.CommBth.ConnectState() != Comm_Bluetooth.DISCONNECTED)
			myapp.CommBth.DisConnect();
		// */
		Awl.ReleaseWakeLock();

		if (myapp.VisStatics) {
			try {
				if (myapp.fs != null)
					myapp.fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.exit(0);
		super.onDestroy();
	}

	/**
	 * 根据android 平台设置显示语言
	 */
	private void setLange() {
		// Toast.makeText(getApplicationContext(), "setl1",
		// Toast.LENGTH_SHORT).show();
		Locale locale = getApplicationContext().getResources()
				.getConfiguration().locale;
		String language = locale.getLanguage();
		MyApplication.Constr_READ = this.getString(R.string.Constr_READ);
		MyApplication.Constr_CONNECT = this.getString(R.string.Constr_CONNECT);
		MyApplication.Constr_INVENTORY = this
				.getString(R.string.Constr_INVENTORY);
		MyApplication.Constr_RWLOP = this.getString(R.string.Constr_RWLOP);

		MyApplication.Constr_PASSVICE = this
				.getString(R.string.Constr_PASSVICE);
		MyApplication.Constr_ACTIVE = this.getString(R.string.Constr_ACTIVE);

		MyApplication.Constr_SetFaill = this
				.getString(R.string.Constr_SetFaill);
		MyApplication.Constr_GetFaill = this
				.getString(R.string.Constr_GetFaill);
		MyApplication.Constr_SetOk = this.getString(R.string.Constr_SetOk);
		MyApplication.Constr_unsupport = this
				.getString(R.string.Constr_unsupport);
		MyApplication.Constr_Putandexit = this
				.getString(R.string.Constr_Putandexit);

		MyApplication.Constr_stopscan = this
				.getString(R.string.Constr_stopscan);
		MyApplication.Constr_scanasetconnecto = this
				.getString(R.string.Constr_scanasetconnecto);
		MyApplication.Constr_scanselectabluereader = this
				.getString(R.string.Constr_scanselectabluereader);
		MyApplication.Constr_scanselectabluereaderandconnect = this
				.getString(R.string.Constr_scanselectabluereaderandconnect);
		MyApplication.Constr_hadconnected = this
				.getString(R.string.Constr_hadconnected);
		MyApplication.Constr_plsetuuid = this
				.getString(R.string.Constr_plsetuuid);
		MyApplication.Constr_pwderror = this
				.getString(R.string.Constr_pwderror);
		MyApplication.Constr_search = this.getString(R.string.Constr_search);
		MyApplication.Constr_stop = this.getString(R.string.Constr_stop);
		MyApplication.Constr_plselectsearchblueset = this
				.getString(R.string.Constr_plselectsearchblueset);
		MyApplication.Constr_startsearchblueok = this
				.getString(R.string.Constr_startsearchblueok);
		MyApplication.Constr_startsearchbluefail1 = this
				.getString(R.string.Constr_startsearchbluefail1);
		MyApplication.Constr_startsearchbluefail2 = this
				.getString(R.string.Constr_startsearchbluefail2);
		MyApplication.Constr_startsearchbluefail12 = this
				.getString(R.string.Constr_startsearchbluefail12);
		MyApplication.Constr_canclebluematch = this
				.getString(R.string.Constr_canclebluematch);
		MyApplication.Constr_connectbluesetfail = this
				.getString(R.string.Constr_connectbluesetfail);
		MyApplication.Constr_matchbluefail = this
				.getString(R.string.Constr_matchbluefail);
		MyApplication.Constr_pwdmatchfail = this
				.getString(R.string.Constr_pwdmatchfail);
		MyApplication.Constr_connectblueokthentoreader = this
				.getString(R.string.Constr_connectblueokthentoreader);
		MyApplication.Constr_connectblueserfail = this
				.getString(R.string.Constr_connectblueserfail);
		MyApplication.Constr_createreaderok = this
				.getString(R.string.Constr_createreaderok);

		MyApplication.Constr_sub3readmem = this
				.getString(R.string.Constr_sub3readmem);
		MyApplication.Constr_sub3writemem = this
				.getString(R.string.Constr_sub3writemem);
		MyApplication.Constr_sub3lockkill = this
				.getString(R.string.Constr_sub3lockkill);
		MyApplication.Constr_sub3readfail = this
				.getString(R.string.Constr_sub3readfail);
		MyApplication.Constr_sub3nodata = this
				.getString(R.string.Constr_sub3nodata);
		MyApplication.Constr_sub3wrtieok = this
				.getString(R.string.Constr_sub3wrtieok);
		MyApplication.Constr_sub3writefail = this
				.getString(R.string.Constr_sub3writefail);
		MyApplication.Constr_sub3lockok = this
				.getString(R.string.Constr_sub3lockok);
		MyApplication.Constr_sub3lockfail = this
				.getString(R.string.Constr_sub3lockfail);
		MyApplication.Constr_sub3killok = this
				.getString(R.string.Constr_sub3killok);
		MyApplication.Constr_sub3killfial = this
				.getString(R.string.Constr_sub3killfial);

		MyApplication.Auto = this.getString(R.string.Auto);

		MyApplication.Constr_sub4invenpra = this
				.getString(R.string.Constr_sub4invenpra);
		MyApplication.Constr_sub4antpow = this
				.getString(R.string.Constr_sub4antpow);
		MyApplication.Constr_sub4regionfre = this
				.getString(R.string.Constr_sub4regionfre);
		MyApplication.Constr_sub4gen2opt = this
				.getString(R.string.Constr_sub4gen2opt);
		MyApplication.Constr_sub4invenfil = this
				.getString(R.string.Constr_sub4invenfil);
		MyApplication.Constr_sub4addidata = this
				.getString(R.string.Constr_sub4addidata);
		MyApplication.Constr_sub4others = this
				.getString(R.string.Constr_sub4others);

		MyApplication.Constr_sub4setmodefail = this
				.getString(R.string.Constr_sub4setmodefail);
		MyApplication.Constr_sub4setokresettoab = this
				.getString(R.string.Constr_sub4setokresettoab);
		MyApplication.Constr_sub4ndsapow = this
				.getString(R.string.Constr_sub4ndsapow);
		MyApplication.Constr_sub4unspreg = this
				.getString(R.string.Constr_sub4unspreg);

		MyApplication.Constr_subblmode = this
				.getString(R.string.Constr_subblmode);
		MyApplication.Constr_subblinven = this
				.getString(R.string.Constr_subblinven);
		MyApplication.Constr_subblfil = this
				.getString(R.string.Constr_subblfil);
		MyApplication.Constr_subblfre = this
				.getString(R.string.Constr_subblfre);
		MyApplication.Constr_subblnofre = this
				.getString(R.string.Constr_subblnofre);
		MyApplication.Constr_subbl = this.getString(R.string.Constr_subbl);

		MyApplication.Constr_subcsalterpwd = this
				.getString(R.string.Constr_subcsalterpwd);
		MyApplication.Constr_subcslockwpwd = this
				.getString(R.string.Constr_subcslockwpwd);
		MyApplication.Constr_subcslockwoutpwd = this
				.getString(R.string.Constr_subcslockwoutpwd);
		MyApplication.Constr_subcsplsetimeou = this
				.getString(R.string.Constr_subcsplsetimeou);
		MyApplication.Constr_subcsputcnpwd = this
				.getString(R.string.Constr_subcsputcnpwd);
		MyApplication.Constr_subcsplselreg = this
				.getString(R.string.Constr_subcsplselreg);
		MyApplication.Constr_subcsopfail = this
				.getString(R.string.Constr_subcsopfail);
		MyApplication.Constr_subcsputcurpwd = this
				.getString(R.string.Constr_subcsputcurpwd);

		MyApplication.Constr_subdbdisconnreconn = this
				.getString(R.string.Constr_subdbdisconnreconn);
		MyApplication.Constr_subdbhadconnected = this
				.getString(R.string.Constr_subdbhadconnected);
		MyApplication.Constr_subdbconnecting = this
				.getString(R.string.Constr_subdbconnecting);
		MyApplication.Constr_subdbrev = this
				.getString(R.string.Constr_subdbrev);
		MyApplication.Constr_subdbstop = this
				.getString(R.string.Constr_subdbstop);
		MyApplication.Constr_subdbdalennot = this
				.getString(R.string.Constr_subdbdalennot);
		MyApplication.Constr_subdbplpuhexchar = this
				.getString(R.string.Constr_subdbplpuhexchar);

		MyApplication.Coname = this.getResources().getStringArray(
				R.array.Coname);

		MyApplication.pdaatpot = this.getResources().getStringArray(
				R.array.pdaatpot);

		MyApplication.spibank = this.getResources().getStringArray(
				R.array.spibank);
		MyApplication.spifbank = this.getResources().getStringArray(
				R.array.spifbank);
		MyApplication.spilockbank = this.getResources().getStringArray(
				R.array.spilockbank);
		MyApplication.spilocktype = this.getResources().getStringArray(
				R.array.spilocktype);

		MyApplication.spireg = this.getResources().getStringArray(
				R.array.spireg);
		MyApplication.spinvmo = this.getResources().getStringArray(
				R.array.spinvmo);
		MyApplication.spitari = this.getResources().getStringArray(
				R.array.spitari);
		MyApplication.spiwmod = this.getResources().getStringArray(
				R.array.spiwmod);

		MyApplication.cusreadwrite = this.getResources().getStringArray(
				R.array.cusreadwrite);
		MyApplication.cuslockunlock = this.getResources().getStringArray(
				R.array.cuslockunlock);
		MyApplication.straryuploadm = this.getResources().getStringArray(
				R.array.struploadm);
		MyApplication.straryuploads = this.getResources().getStringArray(
				R.array.struploads);

		MyApplication.strconectway = this.getResources().getStringArray(
				R.array.strconectway);
		MyApplication.strarytagend = this.getResources().getStringArray(
				R.array.tagend);
		if (language.contains("en"))
			myapp.spiregbs = new String[] { "NA", "China", "Europe", "China2" };

		MyApplication.regtype = this.getResources().getStringArray(
				R.array.regtype);

		MyApplication.gpodemo = this.getResources().getStringArray(
				R.array.gpodemo);
	}

	public void Test_Permaparamlist() {
		/*
		 * int[] fre1= (int[])
		 * myapp.Mreader.paramGet(ParamNames.Reader_Perma_Region_HopTable);
		 * int[] fre2= (int[])
		 * myapp.Mreader.paramGet(ParamNames.Reader_Region_HopTable);
		 * LogD.LOGD("get save fres:"
		 * +fre1.length+"  after connect get fres"+fre2.length);
		 */

		// 测试永久保存
		/*
		 * AntPower[] ap = (AntPower[])
		 * myapp.Mreader.paramGet(ParamNames.Reader_Perma_Radio_PortPowerList);
		 * AntPower[] ap2 = (AntPower[]) myapp.Mreader
		 * .paramGet(ParamNames.Reader_Radio_PortPowerList);
		 * LogD.LOGD("get save rpow:"
		 * +ap[0].Readpower()+" wpow:"+ap[0].Writepower
		 * ()+" after connect get rpower:"
		 * +ap2[0].Readpower()+" wpower:"+ap2[0].Writepower());
		 * 
		 * 
		 * com.silionmodule.Region.RegionE re=(RegionE)
		 * myapp.Mreader.paramGet(ParamNames.Reader_Perma_Region_Id);
		 * com.silionmodule.Region.RegionE re2= (RegionE)
		 * myapp.Mreader.paramGet(ParamNames.Reader_Region_Id);
		 * LogD.LOGD("get save region:"+re+"  after connect get region:"+re2);
		 * 
		 * int[] fre1= (int[])
		 * myapp.Mreader.paramGet(ParamNames.Reader_Perma_Region_HopTable);
		 * int[] fre2= (int[])
		 * myapp.Mreader.paramGet(ParamNames.Reader_Region_HopTable);
		 * LogD.LOGD("get save fres:"
		 * +fre1.length+"  after connect get fres"+fre2.length);
		 * 
		 * 
		 * int fret= (Integer)
		 * myapp.Mreader.paramGet(ParamNames.Reader_Perma_Region_HopTime);
		 * LogD.LOGD("get save fretime:"+fret);
		 * 
		 * boolean checkval=(Boolean)
		 * myapp.Mreader.paramGet(ParamNames.Reader_Perma_Antenna_CheckPort);
		 * boolean checkval2 =
		 * (Boolean)myapp.Mreader.paramGet(ParamNames.Reader_Antenna_CheckPort);
		 * LogD
		 * .LOGD("get save checkant:"+checkval+" after connect checkant:"+checkval2
		 * );
		 * 
		 * com.silionmodule.Gen2.SessionE se1=(SessionE)
		 * myapp.Mreader.paramGet(ParamNames.Reader_Perma_Gen2_Session);
		 * com.silionmodule.Gen2.SessionE se2=(SessionE)
		 * myapp.Mreader.paramGet(ParamNames.Reader_Gen2_Session);
		 * LogD.LOGD("get save ses:"+se1+" after connect ses:"+se2);
		 * 
		 * com.silionmodule.Gen2.TargetE tar1=(TargetE)
		 * myapp.Mreader.paramGet(ParamNames.Reader_Perma_Gen2_Target);
		 * com.silionmodule.Gen2.TargetE tar2=(TargetE)
		 * myapp.Mreader.paramGet(ParamNames.Reader_Gen2_Target);
		 * LogD.LOGD("get save tar:"+tar1+" after connect tar:"+tar2);
		 * 
		 * Gen2.Q g2q1=(Q)
		 * myapp.Mreader.paramGet(ParamNames.Reader_Perma_Gen2_Q); Gen2.Q
		 * g2q2=(Q) myapp.Mreader.paramGet(ParamNames.Reader_Gen2_Q); String
		 * q1,q2;
		 * 
		 * if(g2q1 instanceof Gen2.DynamicQ) q1="D"; else
		 * q1=String.valueOf(((Gen2.StaticQ) g2q1).initialQ);
		 * 
		 * if(g2q2 instanceof Gen2.DynamicQ) q2="D"; else
		 * q2=String.valueOf(((Gen2.StaticQ) g2q2).initialQ);
		 * LogD.LOGD("get save q:"+q1+" after connect q2:"+q2);
		 */
	}

	public void Test_paramlist() {
		Object obj;
		try {

			// 过滤读tid----------------
			/*
			 * int count=0; while(true) { try { byte[] rdata = new byte[12];
			 * Gen2TagFilter g2tf=null; byte[] rpaswd = new byte[4]; byte[]
			 * fdata = Functional .hexstr_Bytes(trds[i].EPCHexstr());
			 * 
			 * g2tf = new Gen2TagFilter(MemBankE.EPC, 32, fdata,
			 * fdata.length*8); myapp.Mreader.
			 * paramSet(ParamNames.Reader_Tagop_Antenna ,trds[i].Antenna());
			 * short[] epddata=myapp .Mreader.ReadTagMemWords(g2tf,
			 * MemBankE.TID, 0,4);
			 * 
			 * TAGINFO tf2 = TagsMap.get(trds[i] .EPCHexstr());
			 * tf2.EmbededData=Functional .hexstr_Bytes
			 * (Functional.shorts_HexStr(epddata)); tf2.EmbededDatalen=(short)
			 * tf2.EmbededData.length; } catch (ReaderException ex) {
			 * LogD.LOGD(ex.GetMessage()); } if(count++>3) break; } //
			 */

			// 暂不支持
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_BaudRate);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Ipinfo);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_UserMode);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Antenna_SettlingTimeList);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Gpio_InputList);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Gpio_OutputList);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Radio_Power_On);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Read_AsyncOffTime);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Version_Model);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_PowerMode);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Read_AsyncOnTime);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Version_Serial);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Region_SupportedRegions);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Region_HopTime);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_TagReadData_ReportRssilnDbm);
			// obj=myapp.Mreader.paramGet(ParamNames.Reader_Flash);
			myapp.Mreader.paramSet(ParamNames.Reader_CommandTimeout, 1200);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_CommandTimeout);
			myapp.Mreader.paramSet(ParamNames.Reader_TransportTimeout, 200);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_TransportTimeout);

			obj = myapp.Mreader
					.paramGet(ParamNames.Reader_Antenna_ConnectedPortList);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_Antenna_PortList);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_Radio_PowerMax);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_Radio_PowerMin);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_Radio_Temperature);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_Tagop_Protocol);
			obj = myapp.Mreader
					.paramGet(ParamNames.Reader_Version_SupportedProtocols);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_Version_Hardware);
			LogD.LOGD("hardware:" + obj);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_Version_Software);
			LogD.LOGD("software:" + obj);
			// 不建议使用的参数
			myapp.Mreader.paramSet(ParamNames.Reader_Radio_PortReadPowerList,
					new int[][] { { 1, 2100 }, { 2, 2100 }, { 3, 2100 },
							{ 4, 2100 } });
			obj = myapp.Mreader
					.paramGet(ParamNames.Reader_Radio_PortReadPowerList);
			myapp.Mreader.paramSet(ParamNames.Reader_Radio_PortWritePowerList,
					new int[][] { { 1, 2200 }, { 2, 2200 }, { 3, 2200 },
							{ 4, 2200 } });
			obj = myapp.Mreader
					.paramGet(ParamNames.Reader_Radio_PortWritePowerList);
			myapp.Mreader.paramSet(ParamNames.Reader_Radio_ReadPower, 2300);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_Radio_ReadPower);
			myapp.Mreader.paramSet(ParamNames.Reader_Radio_WritePower, 2400);
			obj = myapp.Mreader.paramGet(ParamNames.Reader_Radio_WritePower);
			myapp.Mreader.paramSet(ParamNames.Reader_Radio_EnablePowerSave,
					true);
			obj = myapp.Mreader
					.paramGet(ParamNames.Reader_Radio_EnablePowerSave);

		} catch (Exception ex) {
			LogD.LOGD(ex.getMessage());
		}
	}

	private void showPopupMenu(View view) {
		// View当前PopupMenu显示的相对View的位置
		PopupMenu popupMenu = new PopupMenu(this, view);
		 
		// menu布局
		popupMenu.getMenuInflater()
				.inflate(R.menu.tagspop, popupMenu.getMenu());
		// menu的item点击事件
		popupMenu
				.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
					 
						if(item.getItemId()==R.id.menu_tagop_add)
						{
							if(!Gpodemoauthortags.contains(myapp.Rparams.Curepc))
							Gpodemoauthortags.add(myapp.Rparams.Curepc);
						
						}
						else if(item.getItemId()==R.id.menu_tagop_rem)
						{
							if(Gpodemoauthortags.contains(myapp.Rparams.Curepc))
								Gpodemoauthortags.remove(myapp.Rparams.Curepc);
						}
							
						Toast.makeText(getApplicationContext(),
								item.getTitle(), Toast.LENGTH_SHORT).show();
						return false;
					}
				});
		// PopupMenu关闭事件
		popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
			@Override
			public void onDismiss(PopupMenu menu) {

			}
		});

		popupMenu.show();
	}
}
