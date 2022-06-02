package com.example.module_android_bluedemo;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.module_android_bluedemo.MyAdapter2.ViewHolder;
import com.silionmodule.ParamNames;
import com.silionmodule.ReaderException;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

public class SubBlueSetTabActivity extends Activity {

	 TabHost tabHost_blue;
	 MyApplication myapp;
	 Button button_bluegetinvtime,button_bluesetinvtime,
	 		button_bluegetframeinva,button_bluesetframeinva,
	 		button_bluegetpower,button_bluesetpower,
	 		button_bluegetrssi,button_bluesetrssi,
	 		button_bluegetmode,button_bluesetmode,
	 		button_bluetoinit,button_bluetopass,
	 		button_bluegetagopn,button_bluesetagopn,
	 		button_bluegetagt,button_bluesetagt,
	 		button_bluegetq,button_bluesetq,
	 		button_bluegetses,button_bluesetses,
	 		button_bluegetfil,button_bluesetfil,
	 		button_bluegetreg,button_bluesetreg,
	 		button_bluegetfre,button_bluesetfre,
	 		button_bluegetname,button_bluesetname,
	 		button_blueboot,buttonbluereset;

	 CheckBox cb_hrssi,
	 cbmf_readcount,cbmf_rssi,cbmf_ant,cbmf_fre,cbmf_time,cbmf_rfu,cbmf_pro,cbmf_dl,cb_fre;
	 
	 RadioGroup gr_mode,gr_readmode,gr_blue_fbank,gr_bluefil;
	 
	 //Switch swt_bluefil;
	 
	 ListView elist;
	 
	 private Spinner spinner_blue_target,spinner_blue_q,spinner_blue_ses,
	 				 spinner_blue_fil,spinner_blue_reg;
	 private ArrayAdapter arradp_blue_target,arradp_blue_q,arradp_blue_ses,
	 				 arradp_blue_fil,arradp_blue_reg;
	  String[] spises={"S0","S1","S2","S3"}; 
	  String[] spiq={"自动","0","1","2","3","4","5","6","7","8","9","10","11","12",
				"13","14","15"};
	  String[] spitget={"A","B","A-B","B-A"};
	  String[] spibluefbank={"EPC bank","TID bank","User bank"};
	  String[] spireg;
	  
	  String[] NAfrelist= new String[]{"915750","915250","903250","926750","926250","904250","927250","920250","919250","909250",
				"918750","917750","905250","904750","925250","921750","914750","906750","913750","922250",
				"911250","911750","903750","908750","905750","912250","906250","917250","914250","907250",
				"918250","916250","910250","910750","907750","924750","909750","919750","916750","913250",
				"923750","908250","925750","912750","924250","921250","920750","922750","902750","923250"};
		//中国1跳频表：
		String[] Chinafrelist1= new String[]{"921375","922625","920875","923625","921125","920625","923125","921625",
		           "922125","923875","921875","922875","924125","923375","924375","922375"};

		//欧频跳频表：
		String[] Eu3frelist= new String[]{"865700","866300","866900","867500"};

		//中国2跳频表：
		String[] Chinafrelist2= new String[]{"841375","842625","840875","843625","841125","840625","843125","841625",
		           "842125","843875","841875","842875","844125","843375","844375","842375"};

		 public Integer[] Sort(Object[] array)
	        {
	            int tmpIntValue = 0;
	            for (int xIndex = 0; xIndex < array.length; xIndex++)
	            {
	                for (int yIndex = 0; yIndex < array.length; yIndex++)
	                {
	                    if ((Integer)array[xIndex] < (Integer)array[yIndex])
	                    {
	                        tmpIntValue = (Integer)array[xIndex];
	                        array[xIndex] = array[yIndex];
	                        array[yIndex] = tmpIntValue;
	                    }
	                }
	            }
	            
	            return (Integer[])array;
	        }
	 private int SortGroup(RadioGroup rg)
		{
			 int check1=rg.getCheckedRadioButtonId();
			    if(check1!=-1)
			    {
			    	for(int i=0;i<rg.getChildCount();i++)
			    	{ 
			    	  View vi=rg.getChildAt(i);
			    	  int vv=vi.getId();
			    	  if(check1==vv)
			    	  {
			    		  return i;
			    	  }
			    	}
			    	
			    	return -1;
			    }
			    else
			    	return check1;
		}
	 
	 private void showlist(String[] fres)
		{
			ArrayList<String> list = new ArrayList<String>();  
			list.addAll(Arrays.asList(fres));
			MyAdapter2 mAdapter = new MyAdapter2(list,this);
			 // 绑定Adapter   
			 
		     elist.setAdapter(mAdapter); 
		}
	 
	 private View createIndicatorView(Context context, TabHost tabHost, String title) {
		  LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		  View tabIndicator = inflater.inflate(R.layout.tab_indicator_vertical, tabHost.getTabWidget(), false);
		  final TextView titleView = (TextView) tabIndicator.findViewById(R.id.tv_indicator);
		  titleView.setText(title);
		  return tabIndicator;
		 }
 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_bluesetlayout);
		
		Application app=getApplication();
		myapp=(MyApplication) app;  
		
		spireg=myapp.spiregbs;
		spiq[0]=myapp.Auto;
		
		tabHost_blue = (TabHost) findViewById(R.id.tabhost2); 
	       
		tabHost_blue.setup(); 
		  
		tabHost_blue.getTabWidget().setOrientation(LinearLayout.VERTICAL);
			 
		tabHost_blue.addTab(tabHost_blue.newTabSpec("tab1").setIndicator(createIndicatorView(this, tabHost_blue, MyApplication.Constr_subblmode))
					.setContent(R.id.tab_blue_mode)); 
		tabHost_blue.addTab(tabHost_blue.newTabSpec("tab2").setIndicator(createIndicatorView(this, tabHost_blue, MyApplication.Constr_subblinven)) 
					.setContent(R.id.tab_blue_read)); 
		tabHost_blue.addTab(tabHost_blue.newTabSpec("tab3").setIndicator(createIndicatorView(this, tabHost_blue, "Gen2"))
					.setContent(R.id.tab_blue_gen2)); 
		tabHost_blue.addTab(tabHost_blue.newTabSpec("tab4").setIndicator(createIndicatorView(this, tabHost_blue, MyApplication.Constr_subblfil)) 
					.setContent(R.id.tab_blue_fil));
		tabHost_blue.addTab(tabHost_blue.newTabSpec("tab5").setIndicator(createIndicatorView(this, tabHost_blue, MyApplication.Constr_subblfre))
					.setContent(R.id.tab_blue_fre)); 
		tabHost_blue.addTab(tabHost_blue.newTabSpec("tab5").setIndicator(createIndicatorView(this, tabHost_blue, MyApplication.Constr_subbl))
				.setContent(R.id.tab_blue_info)); 
		    
		TabWidget tw=tabHost_blue.getTabWidget();
		tw.getChildAt(0).setBackgroundColor(Color.BLUE);
		
		button_bluegetinvtime=(Button)findViewById(R.id.button_blue_getinvtime);
		button_bluesetinvtime=(Button)findViewById(R.id.button_blue_setinvtime);
 		button_bluegetframeinva=(Button)findViewById(R.id.button_blue_getframe); 		
 		button_bluesetframeinva=(Button)findViewById(R.id.button_blue_setframe);
 		button_bluegetpower=(Button)findViewById(R.id.button_blue_getpower);
 		button_bluesetpower=(Button)findViewById(R.id.button_blue_setpower);
 		button_bluegetrssi=(Button)findViewById(R.id.button_blue_gethrssi);
 		button_bluesetrssi=(Button)findViewById(R.id.button_blue_sethrssi);
 		cb_hrssi=(CheckBox)findViewById(R.id.checkBox_bluehrssi);
 		
 		button_bluegetmode=(Button)findViewById(R.id.button_blgetworkmode);
 		button_bluesetmode=(Button)findViewById(R.id.button_blue_setworkmode);
 		button_bluetoinit=(Button)findViewById(R.id.button_bltoinit);
 		button_bluetopass=(Button)findViewById(R.id.button_bltopass);
 		button_bluegetagopn=(Button)findViewById(R.id.button_blue_getreadoption);
 		button_bluesetagopn=(Button)findViewById(R.id.button_blue_setreadoption);
 		buttonbluereset=(Button)findViewById(R.id.button_resetc);
 		gr_mode=(RadioGroup)findViewById(R.id.radioGroup_blue_mode);
 		gr_readmode=(RadioGroup)findViewById(R.id.radioGroup_blue_readbank);
 		
 		 cbmf_readcount=(CheckBox)this.findViewById(R.id.checkBox_bluereadcount);
         cbmf_rssi=(CheckBox)this.findViewById(R.id.checkBox_bluerssi);
         cbmf_ant=(CheckBox)this.findViewById(R.id.checkBox_blueant);
         cbmf_fre=(CheckBox)this.findViewById(R.id.checkBox_bluefrequency);
         cbmf_time=(CheckBox)this.findViewById(R.id.checkBox_bluetime);
         cbmf_rfu=(CheckBox)this.findViewById(R.id.checkBox_bluerfu);
         cbmf_pro=(CheckBox)this.findViewById(R.id.checkBox_bluepro);
         cbmf_dl=(CheckBox)this.findViewById(R.id.checkBox_bluetagdatalen);
         
         
         button_bluegetagt=(Button)findViewById(R.id.button_blue_target);
         button_bluesetagt=(Button)findViewById(R.id.button_blue_targetset);
         button_bluegetq=(Button)findViewById(R.id.button_blue_gen2qget);
         button_bluesetq=(Button)findViewById(R.id.button_blue_gen2qset);
         button_bluegetses=(Button)findViewById(R.id.button_blue_gen2sesget);
         button_bluesetses=(Button)findViewById(R.id.button_blue_gen2sesset);
         
           spinner_blue_target = (Spinner) findViewById(R.id.spinner_bluetarget);       	 
           arradp_blue_target = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spitget); 
           arradp_blue_target.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		   spinner_blue_target.setAdapter(arradp_blue_target);
			
			spinner_blue_q = (Spinner) findViewById(R.id.spinner_bluegen2q);       	 
			arradp_blue_q = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spiq); 
			arradp_blue_q.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
			spinner_blue_q.setAdapter(arradp_blue_q);
			
			spinner_blue_ses = (Spinner) findViewById(R.id.spinner_bluegen2session);       	 
			arradp_blue_ses = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spises); 
			arradp_blue_ses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
			spinner_blue_ses.setAdapter(arradp_blue_ses);
			
			button_bluegetfil=(Button)findViewById(R.id.button_blgetfil);
			button_bluesetfil=(Button)findViewById(R.id.button_blsetfil);
			//swt_bluefil=(Switch)findViewById(R.id.switch_bluenablefil);
			gr_bluefil=(RadioGroup)findViewById(R.id.radioGroup_blue_fil);
			spinner_blue_fil = (Spinner) findViewById(R.id.spinner_blueinvfbank);       	 
	        arradp_blue_fil = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spibluefbank); 
	        arradp_blue_fil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	        spinner_blue_fil.setAdapter(arradp_blue_fil);
	        gr_blue_fbank=(RadioGroup)findViewById(R.id.radioGroup_blueinvmatch);
	        
	        button_bluegetreg=(Button)findViewById(R.id.button_blgetregion);
			button_bluesetreg=(Button)findViewById(R.id.button_blsetregion);
			button_bluegetfre=(Button)findViewById(R.id.button_blgetfre);
			button_bluesetfre=(Button)findViewById(R.id.button_blsetfre);
			button_bluegetname=(Button)findViewById(R.id.button_blue_getname);
			button_bluesetname=(Button)findViewById(R.id.button_blue_setname);
			button_blueboot=(Button)findViewById(R.id.button_boot);
			
			spinner_blue_reg = (Spinner) findViewById(R.id.spinner_blueregion);       	 
			arradp_blue_reg = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spireg); 
			arradp_blue_reg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
			spinner_blue_reg.setAdapter(arradp_blue_reg);
			elist=(ListView)this.findViewById(R.id.listView_bluefrequency);
			 
			button_bluetoinit.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try {
						Integer ieg = (Integer) myapp.Mreader
								.paramGet(ParamNames.InitMode_READ_MODE);
						 
							if (ieg != 0) {
								try {
									myapp.Mreader.paramSet(
											ParamNames.InitMode_READ_MODE, "0");
								} catch (ReaderException e) {
									// TODO Auto-generated catch block
									Toast.makeText(getApplicationContext(),  MyApplication.Constr_sub4setmodefail,
											Toast.LENGTH_SHORT).show();
									return;
								}
							}
							else
							{
								Toast.makeText(getApplicationContext(),  MyApplication.Constr_sub4hadactivemo,
										Toast.LENGTH_SHORT).show();
								return;
							}
					}catch (ReaderException e) {
						// TODO Auto-generated catch block
						Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
								Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(getApplicationContext(), MyApplication.Constr_sub4setokresettoab,
							Toast.LENGTH_SHORT).show();
				}
			});
			button_bluetopass.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try {
						Integer ieg = (Integer) myapp.Mreader
								.paramGet(ParamNames.InitMode_READ_MODE);
						 
						if (ieg == 0) {
							try {
								int val = 0xa5a55a5a;
								myapp.Mreader.paramSet(
										ParamNames.InitMode_READ_MODE,
										String.valueOf(val));
							} catch (ReaderException e) {
								// TODO Auto-generated catch block
								Toast.makeText(getApplicationContext(),  MyApplication.Constr_sub4setmodefail,
										Toast.LENGTH_SHORT).show();
							}
						}
							else
							{
								Toast.makeText(getApplicationContext(), MyApplication.Constr_sub4hadpasstivemo,
										Toast.LENGTH_SHORT).show();
								return;
							}
					}catch (ReaderException e) {
						// TODO Auto-generated catch block
						Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
								Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(getApplicationContext(), MyApplication.Constr_sub4setokresettoab,
							Toast.LENGTH_SHORT).show();
				
				}
			});
			
		button_bluegetinvtime.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et=(EditText)findViewById(R.id.editText_bluetimeout);
					Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_TIMEOUT);
					et.setText(String.valueOf(ieg));
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetinvtime.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et=(EditText)findViewById(R.id.editText_bluetimeout);
					myapp.Mreader.paramSet(ParamNames.InitMode_TIMEOUT,et.getText().toString());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_bluegetframeinva.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et=(EditText)findViewById(R.id.editText_sendblueinven);
					Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_SENDTIME);
					et.setText(String.valueOf(ieg));
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetframeinva.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et=(EditText)findViewById(R.id.editText_sendblueinven);
					myapp.Mreader.paramSet(ParamNames.InitMode_SENDTIME,et.getText().toString());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_bluegetpower.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et=(EditText)findViewById(R.id.editText_bluepower);
					Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_POWER);
					et.setText(String.valueOf(ieg));
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetpower.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et=(EditText)findViewById(R.id.editText_bluepower);
					myapp.Mreader.paramSet(ParamNames.InitMode_POWER,et.getText().toString());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_bluegetrssi.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_HIGH_RSSI);
					if(ieg==1)
						cb_hrssi.setChecked(true);
					else
						cb_hrssi.setChecked(false);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetrssi.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					String et="";
					if(cb_hrssi.isChecked())
						et="1";
					else
						et="0";
					myapp.Mreader.paramSet(ParamNames.InitMode_HIGH_RSSI,et);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_bluegetagopn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText stadr=(EditText)findViewById(R.id.editText_blue_staddr);
					EditText len=(EditText)findViewById(R.id.editText_blue_length);
					EditText pwd=(EditText)findViewById(R.id.editText_blue_pwd);
					Integer adress=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_READ_ADDRESS);
					Integer bank=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_READ_BANK);
					Integer length=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_READ_LENGTH);
					Integer psd=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_READ_PASSWORD);
					
					stadr.setText(String.valueOf(adress));
					len.setText(String.valueOf(length));
					pwd.setText(String.valueOf(psd));
					
					if(bank!=-1)
					gr_readmode.check(gr_readmode.getChildAt(bank).getId());
					 
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetagopn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText stadr=(EditText)findViewById(R.id.editText_blue_staddr);
					EditText len=(EditText)findViewById(R.id.editText_blue_length);
					EditText pwd=(EditText)findViewById(R.id.editText_blue_pwd);
				 
					myapp.Mreader.paramSet(ParamNames.InitMode_READ_ADDRESS,stadr.getText().toString());
					myapp.Mreader.paramSet(ParamNames.InitMode_READ_BANK,String.valueOf(SortGroup(gr_readmode)));
					myapp.Mreader.paramSet(ParamNames.InitMode_READ_LENGTH,len.getText().toString());
					if(pwd.getText().toString().equals(""))
						pwd.setText("0");
					myapp.Mreader.paramSet(ParamNames.InitMode_READ_PASSWORD,pwd.getText().toString());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_bluegetmode.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_WORK_MODE);
					int mode=((ieg&0xff000000)>>24)&0xff;
					if(mode==0xA5)
						gr_mode.check(gr_mode.getChildAt(0).getId());
					else if(mode==0xB5)
						gr_mode.check(gr_mode.getChildAt(1).getId());
					else  
						gr_mode.check(gr_mode.getChildAt(2).getId());
					
					 int metaflag=0;
					 
					 metaflag=ieg&0x0000ffff;
					 cbmf_readcount.setChecked(false);
					 cbmf_rssi.setChecked(false);
					 cbmf_ant.setChecked(false);
					 cbmf_fre.setChecked(false);
					 cbmf_time.setChecked(false);
					 cbmf_rfu.setChecked(false);
					 cbmf_pro.setChecked(false);
					 cbmf_dl.setEnabled(false);
					 cbmf_dl.setChecked(false);
					 if((metaflag&0x0001)==0x0001)
						 cbmf_readcount.setChecked(true);
					 if((metaflag&0x0002)==0x0002)
						 cbmf_rssi.setChecked(true);
					 if((metaflag&0x0004)==0x0004)
						 cbmf_ant.setChecked(true);
					 if((metaflag&0x0008)==0x0008)
						 cbmf_fre.setChecked(true);
					 if((metaflag&0x0010)==0x0010)
						 cbmf_time.setChecked(true);
					 if((metaflag&0x0020)==0x0020)
						 cbmf_rfu.setChecked(true);
					 if((metaflag&0x0040)==0x0040)
						 cbmf_pro.setChecked(true);
					 if((metaflag&0x0080)==0x0080)
						 cbmf_dl.setChecked(true);
				 
					
					
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetmode.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					  byte mo=0;
					    
					    int check_mode=SortGroup(gr_mode);
					    
					    if(check_mode==0)
					    	mo=(byte) 0xA5;
					    else if(check_mode==1)
					    	mo=(byte) 0xB5;
					    else
					    	mo=0;

					    int metaflag=0;
					    if(cbmf_readcount.isChecked())
					        metaflag|=0X0001;
				        if(cbmf_rssi.isChecked())
				        	metaflag|=0X0002;
				        if(cbmf_ant.isChecked())
				        	metaflag|=0X0004;
				       if(cbmf_fre.isChecked())
				    	   metaflag|=0X0008;
				       if(cbmf_time.isChecked())
				    	   metaflag|=0X0010;
				       if(cbmf_rfu.isChecked())
				    	   metaflag|=0X0020;
				       if(cbmf_pro.isChecked())
				    	   metaflag|=0X0040;
				       if(cbmf_dl.isChecked())
				    	   metaflag|=0X0080;
				       
				       int val=mo<<24|metaflag;
					myapp.Mreader.paramSet(ParamNames.InitMode_WORK_MODE,String.valueOf(val));
					
					 if(check_mode!=1)
					 {
						 	EditText stadr=(EditText)findViewById(R.id.editText_blue_staddr);
							EditText len=(EditText)findViewById(R.id.editText_blue_length);
							EditText pwd=(EditText)findViewById(R.id.editText_blue_pwd);
							 
							stadr.setText("0");
							len.setText("0");
							pwd.setText("0");
							gr_readmode.clearCheck();
							
							button_bluesetagopn.performClick();
					 }
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
 
		button_bluegetagt.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_TARGET);
					spinner_blue_target.setSelection(ieg);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetagt.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					myapp.Mreader.paramSet(ParamNames.InitMode_TARGET,String.valueOf(spinner_blue_target.getSelectedItemPosition()));
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_bluegetq.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_Q_VALUE);
					if(ieg==-1)
						ieg=0;
					else 
						ieg++;
					spinner_blue_q.setSelection(ieg);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetq.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					myapp.Mreader.paramSet(ParamNames.InitMode_Q_VALUE,String.valueOf(spinner_blue_q.getSelectedItemPosition()-1));
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_bluegetses.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_SESSION);
					spinner_blue_ses.setSelection(ieg);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetses.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					myapp.Mreader.paramSet(ParamNames.InitMode_SESSION,String.valueOf(spinner_blue_ses.getSelectedItemPosition()));
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		
		button_bluegetfil.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Integer filop=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_SELECT_OPTION);
					EditText et1=(EditText)findViewById(R.id.editText_bluefilterdata);
					EditText et2=(EditText)findViewById(R.id.editText_blueinvfilsadr);
					 
					if(filop!=0)
					{
						gr_bluefil.check(gr_bluefil.getChildAt(1).getId());
						//swt_bluefil.setChecked(true);
						Integer filen=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_SELECT_LENGTH);

						String fdata=(String)myapp.Mreader.paramGet(ParamNames.InitMode_SELECT_FILTER_DATA);
						et1.setText(fdata.subSequence(0, filen));
						Integer filadr=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_SELECT_ADDRESS);
						et2.setText(String.valueOf(filadr));
						 
						if((filop&0x08)==0x08)
							gr_blue_fbank.setSelected(true);
						else
							gr_blue_fbank.setSelected(false);
						  spinner_blue_fil.setSelection((filop&0x03)-1);
						return ;
					}
					else
					{
						et1.setText("");
						et2.setText("");
						gr_blue_fbank.setSelected(false);
						spinner_blue_fil.setSelection(-1);
						//swt_bluefil.setChecked(false);
						gr_bluefil.check(gr_bluefil.getChildAt(0).getId());
					}
 
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetfil.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					
					if(SortGroup(gr_bluefil)==1)
					{ byte ismatch=0;
					 if(SortGroup(gr_blue_fbank)==0)
						 ismatch=0x08;

	                 int ipo=spinner_blue_fil.getSelectedItemPosition()+1;
	                 byte fbank=(byte) ipo;
	                 int option=ismatch|fbank;
	                
					 myapp.Mreader.paramSet(ParamNames.InitMode_SELECT_OPTION, String.valueOf(option));
					 EditText et1=(EditText)findViewById(R.id.editText_bluefilterdata);
						EditText et2=(EditText)findViewById(R.id.editText_blueinvfilsadr);
						 
					 
						myapp.Mreader.paramSet(ParamNames.InitMode_SELECT_FILTER_DATA,et1.getText().toString());
						myapp.Mreader.paramSet(ParamNames.InitMode_SELECT_ADDRESS,et2.getText().toString());
						myapp.Mreader.paramSet(ParamNames.InitMode_SELECT_LENGTH,String.valueOf(et1.length()));
					}
					else
					{
						EditText et1=(EditText)findViewById(R.id.editText_bluefilterdata);
						EditText et2=(EditText)findViewById(R.id.editText_blueinvfilsadr);
						 
						myapp.Mreader.paramSet(ParamNames.InitMode_SELECT_OPTION, "0");
						myapp.Mreader.paramSet(ParamNames.InitMode_SELECT_FILTER_DATA,"0");
						myapp.Mreader.paramSet(ParamNames.InitMode_SELECT_ADDRESS,"0");
						myapp.Mreader.paramSet(ParamNames.InitMode_SELECT_LENGTH,"0");
						et1.setText("");
						et2.setText("");
					 
						gr_blue_fbank.setSelected(false);
						spinner_blue_fil.setSelection(0);
						//swt_bluefil.setChecked(false);
					}
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_bluegetreg.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_REGION);
					int region=0;
					switch(ieg)
		    		  {
		    		  case 0x01:
		    			  region=0;
		    			  break;
		    		  case 0x06:
		    			  region=1;
		    			  break;
		    		  case 0x08:
		    			  region=2;
		    			  break;
		    		  case 0x0A:
		    			  region=3;
		    			  break;
		    		  }
					spinner_blue_reg.setSelection(region);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetreg.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int vl=spinner_blue_reg.getSelectedItemPosition();
					int region=0;
					switch(vl)
		    		  {
		    		  case 0:
		    			  region=0x01;
		    			  break;
		    		  case 1:
		    			  region=0x06;
		    			  break;
		    		  case 2:
		    			  region=0x08;
		    			  break;
		    		  case 3:
		    			  region=0x0A;
		    			  break;
		    		  }
					myapp.Mreader.paramSet(ParamNames.InitMode_REGION,String.valueOf(region));
					myapp.Mreader.paramSet(ParamNames.InitMode_FREQUENCY,"0");
					showlist(new String[0]);
					
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		
		button_bluegetfre.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					Integer[] re=(Integer[])myapp.Mreader.paramGet(ParamNames.InitMode_FREQUENCY);
					int all=0;
					for(int i=0;i<re.length;i++)
						all+=re[i];
					
					if(all!=0)
					{
						re=Sort(re);
						String[] ssf=new String[re.length];
						for(int i=0;i<re.length;i++)
						{
							ssf[i]=String.valueOf(re[i]);
						}
						 
						//显示
						showlist(ssf);
					}
					else
					{
						Integer ieg=(Integer)myapp.Mreader.paramGet(ParamNames.InitMode_REGION);
						Integer[] frei;
						String[] ssf = null;
						 switch(ieg)
			    		  {
			    		  case 0x01:
			    			  frei=new Integer[NAfrelist.length];
			    			  for(int i=0;i<NAfrelist.length;i++)
			    				  frei[i]=Integer.valueOf(NAfrelist[i]);
			    			  frei=Sort(frei);
			    			   ssf=new String[frei.length];
								for(int i=0;i<frei.length;i++)
								{
									ssf[i]=String.valueOf(frei[i]);
								}
			    			 
			    			  break;
			    		  case 0x06:
			    			  frei=new Integer[Chinafrelist1.length];
			    			  for(int i=0;i<Chinafrelist1.length;i++)
			    				  frei[i]=Integer.valueOf(Chinafrelist1[i]);
			    			  frei=Sort(frei);
			    			   ssf=new String[frei.length];
								for(int i=0;i<frei.length;i++)
								{
									ssf[i]=String.valueOf(frei[i]);
								}
			    			 
			    			  break;
			    		  case 0x08:
			    			  frei=new Integer[Eu3frelist.length];
			    			  for(int i=0;i<Eu3frelist.length;i++)
			    				  frei[i]=Integer.valueOf(Eu3frelist[i]);
			    			  frei=Sort(frei);
			    			   ssf=new String[frei.length];
								for(int i=0;i<frei.length;i++)
								{
									ssf[i]=String.valueOf(frei[i]);
								}
			    			 
			    			  break;
			    		  case 0x0A:
			    			  frei=new Integer[Chinafrelist2.length];
			    			  for(int i=0;i<Chinafrelist2.length;i++)
			    				  frei[i]=Integer.valueOf(Chinafrelist2[i]);
			    			  frei=Sort(frei);
			    			   ssf=new String[frei.length];
								for(int i=0;i<frei.length;i++)
								{
									ssf[i]=String.valueOf(frei[i]);
								}
			    			  break;
			    		  }
						 showlist(ssf);
					}
					 
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetfre.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					String frestr = "";
					
					for(int i=0;i<elist.getCount();i++)
					{
						String temp=(String) elist.getItemAtPosition(i);
						 if( MyAdapter2.getIsSelected().get(i))
					    {
					    	if(frestr=="")
					    	frestr+=temp;
					    	else
					    		frestr+=","+temp;
					    }
					     
					}
					if(frestr=="")
					{
						Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_subblnofre,
								Toast.LENGTH_SHORT).show();
					}
					myapp.Mreader.paramSet(ParamNames.InitMode_FREQUENCY,frestr);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_bluegetname.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et1=(EditText)findViewById(R.id.editText_bluename);
					et1.getText().clear();
					byte[] ieg=(byte[])myapp.Mreader.paramGet(ParamNames.InitMode_BLUE_NAME);

					 et1.setText(new String(ieg));
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_GetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			 
			}
		});
		button_bluesetname.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et1=(EditText)findViewById(R.id.editText_bluename);
					myapp.Mreader.paramSet(ParamNames.InitMode_BLUE_NAME,et1.getText().toString());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		button_blueboot.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					myapp.Mreader.BootApp();
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
			
		});
		
		buttonbluereset.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					 
					myapp.Mreader.paramSet(ParamNames.Reader_Perma_reset, true);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetFaill+e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(SubBlueSetTabActivity.this, MyApplication.Constr_SetOk,
						Toast.LENGTH_SHORT).show();
			}
			
		});
		
		tabHost_blue.setOnTabChangedListener(new OnTabChangeListener(){

			@Override
			public void onTabChanged(String arg0) {
				// TODO Auto-generated method stub
				int j=tabHost_blue.getCurrentTab();
				TabWidget tabIndicator=tabHost_blue.getTabWidget();
				View vw=tabIndicator.getChildAt(j);
				vw.setBackgroundColor(Color.BLUE);
				int tc=tabHost_blue.getTabContentView().getChildCount();
				for(int i=0;i<tc;i++)
				{
					if(i!=j)
					{
						View vw2=tabIndicator.getChildAt(i);
					  vw2.setBackgroundColor(Color.TRANSPARENT);
					}
				}
				 
			}
			
		});
		
		// 绑定listView的监听器   
				 elist.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						 // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤   
					 ViewHolder holder = (ViewHolder) arg1.getTag();  
					 // 改变CheckBox的状态   
					 holder.cb.toggle();  
					 // 将CheckBox的选中状况记录下来   
					 MyAdapter2.getIsSelected().put(arg2, holder.cb.isChecked());  
					}  
					 
				 });
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-myapp.exittime) > 2000){  
	            Toast.makeText(getApplicationContext(), MyApplication.Constr_Putandexit, Toast.LENGTH_SHORT).show();                                
	            myapp.exittime = System.currentTimeMillis();   
	        } else {
	            finish();
	           // System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
