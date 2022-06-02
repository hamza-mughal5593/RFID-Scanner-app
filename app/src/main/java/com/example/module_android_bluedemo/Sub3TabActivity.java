package com.example.module_android_bluedemo;

import com.silionmodule.Functional;
import com.silionmodule.Gen2;
import com.silionmodule.Gen2.Gen2LockAction;
import com.silionmodule.Gen2.Gen2Password;
import com.silionmodule.Gen2.Gen2TagFilter;
import com.silionmodule.Gen2.MemBankE;
import com.silionmodule.ParamNames;
import com.silionmodule.ReaderException;
import com.silionmodule.TagData;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class Sub3TabActivity extends Activity {

	String[] spibank;
	String[] spifbank; 
	String[] spilockbank; 
	String[] spilocktype;
	
	Button button_readop,button_ic,button_writeop,button_writepc,button_lockop,button_kill;
	Spinner spinner_opbank,spinner_bankr,spinner_bankw,
	spinner_lockbank,spinner_locktype; 
	TabHost tabHost_op;
	 RadioGroup gr_opant,gr_match,gr_enablefil;
	 CheckBox cb_pwd;
	// Switch swt_fil;
	private ArrayAdapter arradp_bank,arradp_fbank,arradp_lockbank,arradp_locktype;
	MyApplication myapp;
	Gen2TagFilter g2tf=null;
	public static EditText EditText_sub3fildata,EditText_sub3wdata;
	
	private View createIndicatorView(Context context, TabHost tabHost, String title) {
		  LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		  View tabIndicator = inflater.inflate(R.layout.tab_indicator_vertical, tabHost.getTabWidget(), false);
		  final TextView titleView = (TextView) tabIndicator.findViewById(R.id.tv_indicator);
		  titleView.setText(title);
		  return tabIndicator;
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
	 
	 private void SetOpant() throws ReaderException
	 {
		 
			int opa=SortGroup(gr_opant)+1;
			myapp.Mreader.paramSet(ParamNames.Reader_Tagop_Antenna,opa);
		 
	 }
	 
	 private void SetFiler() throws ReaderException
	 { 
		 if(SortGroup(gr_enablefil)==1)
		 {  
			 byte[] fdata;
			 EditText et=(EditText)findViewById(R.id.editText_opfilterdata);
			 fdata = Functional.hexstr_Bytes(et.getText().toString());
			 int it=spinner_opbank.getSelectedItemPosition();
			 MemBankE mb=null;
			 switch(it)
				{
				case 0: 
					mb=MemBankE.RESERVED;
					break;
				case 1:
					mb=MemBankE.EPC;
					break;
				case 2:
					mb=MemBankE.TID;
					break;
				case 3:
					mb=MemBankE.USER;
					break;
				}
			 EditText etadr=(EditText)findViewById(R.id.editText_opfilsadr);
			 
			 g2tf = new Gen2TagFilter(mb, 
					 Integer.valueOf(etadr.getText().toString()), 
					 fdata,  Integer.valueOf(et.getText().toString().length())*4);
			 int ma=SortGroup(gr_match);
			 if(ma==1)
				 g2tf.SetInvert(true);
		 }
		 else
			 g2tf=null;
	 }
	 
	 private void SetPassword() throws ReaderException
	 {
		    EditText et=(EditText)findViewById(R.id.editText_password);
		   // String temp=et.getText().toString();
		    if(cb_pwd.isChecked())
		    {
		    	Gen2Password g2pw = new Gen2Password(et.getText().toString());
				myapp.Mreader.paramSet(ParamNames.Reader_Gen2_AccessPassword, g2pw);
		    	
		    }
		    else
		    { 
		    	Gen2Password g2pw = new Gen2Password("00000000");
		    	myapp.Mreader.paramSet(ParamNames.Reader_Gen2_AccessPassword, g2pw);
		    }
	 }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab3_tablelayout);
		Application app=getApplication();
		myapp=(MyApplication) app;  
		
		 spibank=myapp.spibank;
		 spifbank=myapp.spifbank; 
		 spilockbank=myapp.spilockbank;
		 spilocktype=myapp.spilocktype;
		 
				// 获取TabHost对象          
				 // 得到TabActivity中的TabHost对象
				tabHost_op = (TabHost) findViewById(R.id.tabhost3); 
				// 如果没有继承TabActivity时，通过该种方法加载启动tabHost 
				tabHost_op.setup(); 
				tabHost_op.getTabWidget().setOrientation(LinearLayout.VERTICAL);
				//tabHost2.addTab(tabHost2.newTabSpec("tab1").setIndicator("初始化",  
				//getResources().getDrawable(R.drawable.ic_launcher)).setContent(R.id.tab11)); 
			    //tabHost2.addTab(tabHost2.newTabSpec("tab1").setIndicator(createIndicatorView(this, tabHost2, "1111"))
			    	//	.setContent(R.id.tab11)); 
			   
				tabHost_op.addTab(tabHost_op.newTabSpec("tab1").setIndicator(createIndicatorView(this, tabHost_op, MyApplication.Constr_sub3readmem))
						.setContent(R.id.tab3_sub_read)); 
				tabHost_op.addTab(tabHost_op.newTabSpec("tab2").setIndicator(createIndicatorView(this, tabHost_op, MyApplication.Constr_sub3writemem)) 
						.setContent(R.id.tab3_sub_write)); 
				tabHost_op.addTab(tabHost_op.newTabSpec("tab3").setIndicator(createIndicatorView(this, tabHost_op, MyApplication.Constr_sub3lockkill))
						.setContent(R.id.tab3_sub_lockkill)); 
				
				TabWidget tw=tabHost_op.getTabWidget();
				tw.getChildAt(0).setBackgroundColor(Color.BLUE);
				 
				spinner_bankr = (Spinner) findViewById(R.id.spinner_bankr); 
				// View layout = getLayoutInflater().inflate(R.layout.tab3_tablelayout_write, null);
				spinner_bankw= (Spinner)findViewById(R.id.spinner_bankw);
				spinner_opbank= (Spinner)findViewById(R.id.spinner_opfbank);	 
				arradp_bank = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spibank); 
				arradp_bank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
				spinner_bankr.setAdapter(arradp_bank);
				spinner_bankw.setAdapter(arradp_bank);
				spinner_opbank.setAdapter(arradp_bank);
				
				spinner_lockbank = (Spinner) findViewById(R.id.spinner_lockbank); 
				 //将可选内容与ArrayAdapter连接起来         	 
				arradp_lockbank = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spilockbank); 
				 //设置下拉列表的风格       
				arradp_lockbank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				 //将adapter 添加到spinner中       
				spinner_lockbank.setAdapter(arradp_lockbank);
				
				spinner_locktype = (Spinner) findViewById(R.id.spinner_locktype);   	 
				arradp_locktype = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spilocktype); 
				arradp_locktype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner_locktype.setAdapter(arradp_locktype);
			 
				button_readop = (Button) findViewById(R.id.button_read);
				button_ic=(Button)findViewById(R.id.button_ic);
				button_writeop = (Button) findViewById(R.id.button_write);
				button_writepc = (Button) findViewById(R.id.button_wepc);
				button_lockop=(Button)findViewById(R.id.button_lock);
				button_kill=(Button)findViewById(R.id.button_kill);
				gr_opant=(RadioGroup)findViewById(R.id.radioGroup_opant);
				gr_match=(RadioGroup)findViewById(R.id.radioGroup_opmatch);
				gr_enablefil=(RadioGroup)findViewById(R.id.radioGroup_enableopfil);
				cb_pwd=(CheckBox)findViewById(R.id.checkBox_opacepwd);
				// EditText et=(EditText)findViewById(R.id.editText_opfilterdata);
				// et.setText(myapp.Rparams.Curepc);
				 
				 EditText_sub3fildata=(EditText)findViewById(R.id.editText_opfilterdata);
				 EditText_sub3wdata=(EditText)findViewById(R.id.editText_dataw);
				
				 spinner_opbank.setSelection(1);
				 spinner_bankr.setSelection(1);
				 spinner_bankw.setSelection(1);
				 spinner_lockbank.setSelection(2);
				 spinner_locktype.setSelection(1);
	 
				button_readop.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							EditText etdr=(EditText)findViewById(R.id.editText_datar);
							etdr.setText("");
							SetOpant();
							SetPassword();
							SetFiler();
							
							EditText etcount=(EditText)findViewById(R.id.editText_opcountr);
							EditText etadr=(EditText)findViewById(R.id.editText_startaddr);
							EditText etdrw=(EditText)findViewById(R.id.editText_dataw);
							short[] epddata =null; 
	 
							int trycount=5;
							do{
								try{
									epddata=myapp.Mreader.ReadTagMemWords(g2tf, 
										MemBankE.values()[spinner_bankr.getSelectedItemPosition()], 
										Integer.valueOf(etadr.getText().toString()),
										Integer.valueOf(etcount.getText().toString()));
									break;
								}catch(ReaderException ex)
								{ trycount--;
								
								if(trycount<1)
									throw ex;
								 continue;
								}
 
							}while(epddata==null);
							
							if(epddata!=null)
							{etdr.setText(Functional.shorts_HexStr(epddata));
							etdrw.setText(Functional.shorts_HexStr(epddata));
							}
							else
							{
								etdr.setText("");
								etdrw.setText("");
							}
						} catch (ReaderException e) {
							// TODO Auto-generated catch block
							Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3readfail+e.GetMessage(),
									Toast.LENGTH_SHORT).show();
						}
						catch (Exception e) {
							// TODO Auto-generated catch block
							Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3readfail+e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
						
					}
					
				});
				
				
				button_ic.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						
						try {
							EditText etdr=(EditText)findViewById(R.id.editText_datar);
							etdr.setText("");
							SetOpant();
							short[] epddata =myapp.Mreader.ReadTagMemWords(null, MemBankE.TID, 0,4);
							String hstr=Functional.shorts_HexStr(epddata);
							 //11+5,4+12
							 String fac="厂家:";
							 String nic2="";
							 for(int i=0;i<5;i++)
							 {
								 if(((epddata[0]>>(5-i))&0x0001)!=0)
								   nic2+="1";
								 else
									 nic2+="0";
							 }
							 for(int i=0;i<4;i++)
							 {
								 if(((epddata[1]>>(16-i))&0x0001)!=0)
									   nic2+="1";
									 else
										 nic2+="0";
							 }
							 
								 if(nic2.equals("000000001"))
								   fac = fac + "Impinj";
								 else if(nic2.equals("000000010"))
								            fac = fac + "Texas Instruments";
								 else if(nic2.equals("000000011"))
								            fac = fac +"Alien Technology";
								 else if(nic2.equals("000000100"))
								            fac = fac + "Intelleflex";
								 else if(nic2.equals("000000101"))
								            fac = fac + "Atmel";
								 else if(nic2.equals("000000110"))
								            fac = fac + "NXP Semiconductors";
								 else if(nic2.equals("000000111"))
								            fac = fac + "ST Microelectronics";
								 else if(nic2.equals("000001000"))
								            fac = fac + "EP Microelectronics";
								 else if(nic2.equals("000001001"))
								            fac = fac + "Motorola(formerly Symbol Technologies)";
								 else if(nic2.equals("000001010"))
								            fac = fac + "Sentech Snd Bhd";
								 else if(nic2.equals("000001011"))
								            fac = fac + "EM Microelectronics";
								 else if(nic2.equals("000001100"))
								            fac = fac + "Renesas Technology Corp.";
								 else if(nic2.equals("000001101"))
								            fac = fac + "Mstar";
								 else if(nic2.equals("000001110"))
								            fac = fac + "Tyco International";
								 else if(nic2.equals("000001111"))
								            fac = fac + "Quanray Electronics";
								 else if(nic2.equals("000010000"))
								            fac = fac + "Fujitsu";
								 else if(nic2.equals("000010001"))
								            fac = fac + "LSIS";
								 else if(nic2.equals("000010010"))
								            fac = fac + "CAEN RFID srl";
								 else if(nic2.equals("000010011"))
								            fac = fac + "Productivity Engineering GmbH";
								 else if(nic2.equals("000010100"))
								            fac = fac + " Federal Electric Corp.";
								 else if(nic2.equals("000010101"))
								            fac = fac + "ON Semiconductor";
								 else if(nic2.equals("000010110"))
								            fac = fac + "Ramtron";
								 else if(nic2.equals("000010111"))
								            fac = fac + "Tego";
								 else if(nic2.equals("000011000"))
								            fac = fac + "Ceitec S.A.";
								 else if(nic2.equals("000011001"))
								            fac = fac + "CPA Wernher von Braun";
								 else if(nic2.equals("000011010"))
								            fac = fac + "TransCore";
								 else if(nic2.equals("000011011"))
								            fac = fac + "Nationz";
								 else if(nic2.equals("000011100"))
								            fac = fac + "Invengo";
								 else if(nic2.equals("000011101"))
								            fac = fac + "Kiloway";
								 else if(nic2.equals("000011110"))
								            fac = fac + "Longjing Microelectronics Co.Ltd.";
								 else if(nic2.equals("000011111"))
								            fac = fac + "Chipus Microelectronics";
								 else if(nic2.equals("000100000"))
								            fac = fac + "ORIDAO";
								 else if(nic2.equals("000100001"))
								            fac = fac + "Maintag";
								 else if(nic2.equals("000100010"))
								            fac = fac + "Yangzhou Daoyuan Microelectronics Co.Ltd";
								 else if(nic2.equals("000100011"))
								            fac = fac + " Gate Elektronik";
								 else if(nic2.equals("000100100"))
								            fac = fac + "RFMicron, Inc.";
								 else if(nic2.equals("000100101"))
								            fac = fac + "RST - Invent LLC";
								 else if(nic2.equals("000100110"))
								            fac = fac + "Crystone Technology";
								 else if(nic2.equals("000100111"))
								            fac = fac + "Shanghai Fudan Microelectronics Group";
								 else if(nic2.equals("000101000"))
								            fac = fac + "Farsens";
								 else if(nic2.equals("000101001"))
								            fac = fac + "Giesecke & Devrient GmbH";
								 else if(nic2.equals("000101010"))
								            fac = fac + "AWID";
								 else if(nic2.equals("000101011"))
								            fac = fac + "Unitec Semicondutores S / A";
								 else if(nic2.equals("000101100"))
								            fac = fac + " Q - Free ASA";
								 else if(nic2.equals("000101101"))
								            fac = fac + "Valid S.A.";
								 else if(nic2.equals("000101110"))
								            fac = fac + "Fraunhofer IPMS";
								 else if(nic2.equals("000101111"))
								            fac = fac + "ams AG";
								 else if(nic2.equals("000110000"))
								            fac = fac + "Angstrem JSC";
								 else if(nic2.equals("000110001"))
								            fac = fac + "Honeywell";
								 else if(nic2.equals("000110010"))
								            fac = fac + "Huada Semiconductor Co.Ltd(HDSC)";
								 else if(nic2.equals("000110011"))
								            fac = fac + "Lapis Semiconductor Co., Ltd.";
								 else if(nic2.equals("000110100"))
								            fac = fac + "PJSC Mikron";
								 else if(nic2.equals("000110101"))
								            fac = fac + "Hangzhou Landa Microelectronics Co., Ltd.";
								 else if(nic2.equals("000110110"))
								            fac = fac + "Nanjing NARI Micro - Electronic Technology Co., Ltd.";
								 else if(nic2.equals("000110111"))
								            fac = fac + "Southwest Integrated Circuit Design Co., Ltd.";
								 else if(nic2.equals("000111000"))
								            fac = fac + "Silictec";
								 else if(nic2.equals("000111001"))
								            fac = fac + " Nation RFID";
								 else if(nic2.equals("000111010"))
								            fac = fac + "Asygn";
								 else if(nic2.equals("000111011"))
								            fac = fac + "Suzhou HCTech Technology Co., Ltd.";
								 else if(nic2.equals("000111100"))
								            fac = fac + "AXEM Technology";
								 
								 String verp=hstr.substring(0,8);
									       // console.log('verp:', verp)
								 if(verp.equals("E2801130"))
									            fac = fac +" Monza 5";
								 else if(verp.equals("E2801100"))
									            fac = fac + " Monza 4D";
								 else if(verp.equals("E280110C"))
									            fac = fac + " Monza 4E";
								 else if(verp.equals("E2801105"))
									            fac = fac + " Monza 4QT";
								 else if(verp.equals("E2801170"))
									            fac = fac + " Monza R6P";
								 else if(verp.equals("E2801171"))
									          {  
									            String verp2 = hstr.substring(8, 5);
									              if (verp2 =="20000")
									                fac = fac + " Monza R6A";
									              else if (verp2 =="20001")
									                fac = fac + " Monza R6B";
									               
									          }
								 else if(verp.equals("E2801173"))
									            fac = fac + " Monza S6C";
								 else if(verp.equals("E2801160"))
									            fac = fac + " Monza R6";
								 else if(verp.equals("E2801130"))
									            fac = fac + " Monza 5";
								 else if(verp.equals("E2801114"))
									            fac = fac + " Monza 4i";
								 else if(verp.equals("E2801140"))
									            fac = fac + " Monza X-2K";
								 else if(verp.equals("E2801150"))
									            fac = fac + " Monza X-8K";
								 else if(verp.equals("E2801190"))
									            fac = fac + " Monza M700";
								 else if(verp.equals("E2003412"))
									            fac = fac + " Alien Higg3";
								 else if(verp.equals("E2003414"))
									            fac = fac + " Alien Higg4";
								 else if(verp.equals("E2003811"))
									            fac = fac + " Alien Higg";
								 else if(verp.equals("E20A1161"))
									            fac = fac + " 智芯微";
								 else if(verp.equals("E2806890"))
									            fac = fac + " NXP Ucode 7";
								 else if(verp.equals("E2808894"))
									            fac = fac + " NXP Ucode  8";
									         
								 
								 etdr.setText(fac);       
						} catch (ReaderException e) {
							// TODO Auto-generated catch block
							Toast.makeText(Sub3TabActivity.this, e.GetMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				button_writeop.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							SetOpant();
							SetPassword();
							SetFiler();
							
							//EditText etcount=(EditText)findViewById(R.id.editText_opcountw);
							EditText etadr=(EditText)findViewById(R.id.editText_startaddrw);
							EditText etdr=(EditText)findViewById(R.id.editText_dataw);
							if(etdr.getText().toString().equals(""))
							{
								Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3nodata,
										Toast.LENGTH_SHORT).show();
							}
							int trycount=5;
							do{
								try{
									//String ascsttr="abcdefghijkl";
									//myapp.Mreader.WriteTagMemWords(null, MemBankE.USER, 0,Functional.bytes_Short16s(ascsttr.getBytes()));
									myapp.Mreader.WriteTagMemWords(g2tf, 
											MemBankE.values()[spinner_bankw.getSelectedItemPosition()], 
											Integer.valueOf(etadr.getText().toString()),
											Functional.hexstr_Short16s(etdr.getText().toString()));
									
									Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3wrtieok,
											Toast.LENGTH_SHORT).show();
									break;
								}catch(ReaderException ex)
								{ trycount--;
								
								if(trycount<1)
									throw ex;
								 continue;
								}

							}while(true);
 
						} catch (ReaderException re) {
							// TODO Auto-generated catch block
							Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3writefail+re.GetMessage(),
									Toast.LENGTH_SHORT).show();
						}
						catch (Exception e) {
							// TODO Auto-generated catch block
							Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3writefail+e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
						
					
					}
					
				});
				
				button_writepc.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						try {
							SetOpant();
							SetPassword();
							SetFiler();
							
							EditText etdr=(EditText)findViewById(R.id.editText_dataw);

							int trycount=5;
							do{
								try{

									myapp.Mreader.InitTag(g2tf,new TagData(etdr.getText().toString()));
									Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3wrtieok,
											Toast.LENGTH_SHORT).show();
									break;
								}catch(ReaderException ex)
								{ trycount--;
								
								if(trycount<1)
									throw ex;
								 continue;
								}

							}while(true);
							
						} catch (ReaderException e) {
							// TODO Auto-generated catch block
							Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3writefail+e.GetMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}catch (Exception e) {
							// TODO Auto-generated catch block
							Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3writefail+e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}
						
					
					}
					
				});
				
				button_lockop.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							SetOpant();
							SetPassword();
							SetFiler();
							
							/*定义锁类型，epc临时锁*/
							Gen2LockAction g2la = null;
							int lbank=spinner_lockbank.getSelectedItemPosition();
							int ltype=spinner_locktype.getSelectedItemPosition();
							if(lbank==0)
							{
								if(ltype==0)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.ACCESS_UNLOCK);
								else if(ltype==1)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.ACCESS_LOCK);
								else if(ltype==2)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.ACCESS_PERMALOCK);
									 
							}
							else if(lbank==1)
							{
								if(ltype==0)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.KILL_UNLOCK);
								else if(ltype==1)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.KILL_LOCK);
								else if(ltype==2)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.KILL_PERMALOCK);
							}
							else if(lbank==2)
							{
								if(ltype==0)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.EPC_UNLOCK);
								else if(ltype==1)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.EPC_LOCK);
								else if(ltype==2)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.EPC_PERMALOCK);
							}
							else if(lbank==3)
							{
								if(ltype==0)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.TID_UNLOCK);
								else if(ltype==1)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.TID_LOCK);
								else if(ltype==2)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.TID_PERMALOCK);
							}
							else if(lbank==4)
							{
								if(ltype==0)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.USER_UNLOCK);
								else if(ltype==1)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.USER_LOCK);
								else if(ltype==2)
									g2la = new Gen2LockAction(Gen2.Gen2LockAction.USER_PERMALOCK);
							}
							 
							int trycount=5;
							do{
								try{
									myapp.Mreader.LockTag(g2tf, g2la);
									Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3lockok,
											Toast.LENGTH_SHORT).show();
									break;
								}catch(ReaderException ex)
								{ trycount--;
								
								if(trycount<1)
									throw ex;
								 continue;
								}

							}while(true);
							
						} catch (ReaderException e) {
							// TODO Auto-generated catch block
							Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3lockfail+e.GetMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				button_kill.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						try {
							 SetOpant();
							 SetFiler();
							 EditText et=(EditText)findViewById(R.id.editText_password);
                             if(et.getText().toString().isEmpty())
                             {
                            	 Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3killfial,
     									Toast.LENGTH_SHORT).show();
                            	 return;
                             }
							 int trycount=5;
							 byte[] kbd = Functional.hexstr_Bytes(et.getText().toString());
								int kint = (kbd[0] & 0xff) << 24 | (kbd[1] & 0xff) << 16
										| (kbd[2] & 0xff) << 8 | (kbd[3] & 0xff);
							do{
								try{
									myapp.Mreader.KillTag(g2tf, kint);
									Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3killok,
											Toast.LENGTH_SHORT).show();
									break;
								}catch(ReaderException ex)
								{ trycount--;
								
								if(trycount<1)
									throw ex;
								 continue;
								}

							}while(true);

						} catch (ReaderException e) {
							// TODO Auto-generated catch block
							Toast.makeText(Sub3TabActivity.this, MyApplication.Constr_sub3killfial+e.GetMessage(),
									Toast.LENGTH_SHORT).show();
						}
						
					}
					
				});
				
				tabHost_op.setOnTabChangedListener(new OnTabChangeListener(){

					@Override
					public void onTabChanged(String arg0) {
						// TODO Auto-generated method stub
						int j=tabHost_op.getCurrentTab();
						TabWidget tabIndicator=tabHost_op.getTabWidget();
						View vw=tabIndicator.getChildAt(j);
						vw.setBackgroundColor(Color.BLUE);
						int tc=tabHost_op.getTabContentView().getChildCount();
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
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-myapp.exittime) > 2000){  
	            Toast.makeText(getApplicationContext(), MyApplication.Constr_Putandexit, Toast.LENGTH_SHORT).show();                                
	            myapp.exittime = System.currentTimeMillis();   
	        } else {
	            finish();
	           
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	protected void onResume()
	{
		gr_opant.getChildAt(1).setEnabled(false);
		gr_opant.getChildAt(2).setEnabled(false);
		gr_opant.getChildAt(3).setEnabled(false);
		 if(myapp.Rparams.antportc>=2)
			 gr_opant.getChildAt(1).setEnabled(true);
		 if(myapp.Rparams.antportc>=3)
			 gr_opant.getChildAt(2).setEnabled(true);
		 if(myapp.Rparams.antportc>=4)
			 gr_opant.getChildAt(3).setEnabled(true);
		 
		 super.onResume();
	}
	protected void onStop()
	{
		 super.onStop();
	}
}
