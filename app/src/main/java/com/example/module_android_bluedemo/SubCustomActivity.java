package com.example.module_android_bluedemo;

import java.util.HashMap;
import java.util.Map;

import com.silionmodule.Custom;
import com.silionmodule.Custom.CustomCmdType;
import com.silionmodule.Custom.KUNRUI_CustomParam;
import com.silionmodule.Custom.KUNRUI_CustomResult;
import com.silionmodule.Functional;
import com.silionmodule.ParamNames;
import com.silionmodule.R2000_calibration;
import com.silionmodule.ReaderException;
import com.silionmodule.Gen2.Gen2Password;
import com.silionmodule.Gen2.Gen2TagFilter;
import com.silionmodule.Gen2.MemBankE;
import com.silionmodule.R2000_calibration.OEM_DATA;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

public class SubCustomActivity extends Activity {
	MyApplication myapp;
	String[] UserArea = { "0", "1", "2", "3" };
	String[] cusfbank;
	String[] cusreadwrite;
	String[] cuslockunlock;
	String[] straryuploadm;
	String[] straryuploads;
	String[] strarytagend;
	Button button_chanageareapwd, button_changearealock, button_uploadget,
			button_uploadset, button_changearealocknopwd;
	Spinner spinner_userarea, spinner_cusfbank, spinner_readwrite_nopwd,
			spinner_lockunlock_nopwd, spinner_uploadm, spinner_uploads,
			spinner_readwrite, spinner_lockunlock, spn_tagend;
	private ArrayAdapter arradp_uarea, arradp_fbank, arradp_readwrite,
			arradp_lockunlock, arradp_uploadm, arradp_uploads, arr_tagend;
	Gen2TagFilter g2tf = null;
	RadioGroup gr_customatch, gr_customenablefil;
	CheckBox cb_maskread, cb_maskwirte, cb_actionread, cb_actionwrite,
			cb_xbfilter;
	EditText et_xbstadr, et_xblen;
	TabHost tabHost_custom_lunrui;

	private View createIndicatorView(Context context, TabHost tabHost,
			String title) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View tabIndicator = inflater.inflate(R.layout.tab_indicator_vertical,
				tabHost.getTabWidget(), false);
		final TextView titleView = (TextView) tabIndicator
				.findViewById(R.id.tv_indicator);
		titleView.setText(title);
		return tabIndicator;
	}

	private int SortGroup(RadioGroup rg) {
		int check1 = rg.getCheckedRadioButtonId();
		if (check1 != -1) {
			for (int i = 0; i < rg.getChildCount(); i++) {
				View vi = rg.getChildAt(i);
				int vv = vi.getId();
				if (check1 == vv) {
					return i;
				}
			}

			return -1;
		} else
			return check1;
	}

	private void SetFiler() throws ReaderException {
		if (SortGroup(gr_customenablefil) == 1) {
			byte[] fdata;
			EditText et = (EditText) findViewById(R.id.editText_customfilterdata);
			fdata = Functional.binarystr_Bytes(et.getText().toString());
			int it = spinner_cusfbank.getSelectedItemPosition() + 1;
			MemBankE mb = null;
			switch (it) {
			case 0:
				mb = MemBankE.RESERVED;
				break;
			case 1:
				mb = MemBankE.EPC;
				break;
			case 2:
				mb = MemBankE.TID;
				break;
			case 3:
				mb = MemBankE.USER;
				break;
			}
			EditText etadr = (EditText) findViewById(R.id.editText_customfilsadr);

			g2tf = new Gen2TagFilter(mb, Integer.valueOf(etadr.getText()
					.toString()), fdata, et.getText().toString().length());// 按二进制数据过滤
			int ma = SortGroup(gr_customatch);
			if (ma == 1)
				g2tf.SetInvert(true);
		} else
			g2tf = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab5_tablelayout);

		Application app = getApplication();
		myapp = (MyApplication) app;

		cusfbank = myapp.spifbank;
		cusreadwrite = myapp.cusreadwrite;
		cuslockunlock = myapp.cuslockunlock;
		straryuploadm = myapp.straryuploadm;
		straryuploads = myapp.straryuploads;
		strarytagend = myapp.strarytagend;

		tabHost_custom_lunrui = (TabHost) findViewById(R.id.tabhost4);
		// 如果没有继承TabActivity时，通过该种方法加载启动tabHost
		tabHost_custom_lunrui.setup();
		tabHost_custom_lunrui.getTabWidget().setOrientation(
				LinearLayout.VERTICAL);
		// tabHost2.addTab(tabHost2.newTabSpec("tab1").setIndicator("初始化",
		// getResources().getDrawable(R.drawable.ic_launcher)).setContent(R.id.tab11));
		// tabHost2.addTab(tabHost2.newTabSpec("tab1").setIndicator(createIndicatorView(this,
		// tabHost2, "1111"))
		// .setContent(R.id.tab11));

		tabHost_custom_lunrui.addTab(tabHost_custom_lunrui
				.newTabSpec("tab1")
				.setIndicator(
						createIndicatorView(this, tabHost_custom_lunrui,
								MyApplication.Constr_subcsalterpwd))
				.setContent(R.id.tab5_1));
		tabHost_custom_lunrui.addTab(tabHost_custom_lunrui
				.newTabSpec("tab2")
				.setIndicator(
						createIndicatorView(this, tabHost_custom_lunrui,
								MyApplication.Constr_subcslockwpwd))
				.setContent(R.id.tab5_2));
		tabHost_custom_lunrui.addTab(tabHost_custom_lunrui
				.newTabSpec("tab3")
				.setIndicator(
						createIndicatorView(this, tabHost_custom_lunrui,
								MyApplication.Constr_subcslockwoutpwd))
				.setContent(R.id.tab5_3));

		TabWidget tw = tabHost_custom_lunrui.getTabWidget();
		tw.getChildAt(0).setBackgroundColor(Color.BLUE);

		et_xbstadr = (EditText) findViewById(R.id.editText_uploadadr);
		et_xblen = (EditText) findViewById(R.id.editText_uploadmlen);

		gr_customenablefil = (RadioGroup) findViewById(R.id.radioGroup_enablecustomfil);
		gr_customatch = (RadioGroup) findViewById(R.id.radioGroup_customatch);
		// cb_maskread=(CheckBox) findViewById(R.id.checkBox_customaskread);
		// cb_maskwirte=(CheckBox) findViewById(R.id.checkBox_customaskwrite);
		// cb_actionread=(CheckBox)
		// findViewById(R.id.checkBox_customactionread);
		// cb_actionwrite=(CheckBox)
		// findViewById(R.id.checkBox_customactionwrite);

		cb_xbfilter = (CheckBox) findViewById(R.id.checkBox_xbfilter);
		spinner_userarea = (Spinner) findViewById(R.id.spinner_customuserarea);
		// View layout =
		// getLayoutInflater().inflate(R.layout.tab3_tablelayout_write, null);
		arradp_uarea = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, UserArea);
		arradp_uarea
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_userarea.setAdapter(arradp_uarea);

		spinner_cusfbank = (Spinner) findViewById(R.id.spinner_customfbank);
		arradp_fbank = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cusfbank);
		arradp_fbank
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_cusfbank.setAdapter(arradp_fbank);

		spinner_lockunlock_nopwd = (Spinner) findViewById(R.id.spinner_lockunlock_nopwd);
		spinner_lockunlock = (Spinner) findViewById(R.id.spinner_lockunlock_pwd);
		arradp_lockunlock = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cuslockunlock);
		arradp_lockunlock
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_lockunlock.setAdapter(arradp_lockunlock);
		spinner_lockunlock_nopwd.setAdapter(arradp_lockunlock);

		spinner_readwrite = (Spinner) findViewById(R.id.spinner_readwrite_pwd);
		spinner_readwrite_nopwd = (Spinner) findViewById(R.id.spinner_readwrite_nopwd);
		arradp_readwrite = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cusreadwrite);
		arradp_readwrite
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_readwrite.setAdapter(arradp_readwrite);
		spinner_readwrite_nopwd.setAdapter(arradp_readwrite);

		spinner_uploadm = (Spinner) findViewById(R.id.Spinner_uploadm);
		spinner_uploads = (Spinner) findViewById(R.id.Spinner_uploads);
		spn_tagend = (Spinner) findViewById(R.id.Spinner_tagend);

		arradp_uploadm = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, straryuploadm);
		arradp_uploads = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, straryuploads);
		arr_tagend = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strarytagend);
		spinner_uploadm.setAdapter(arradp_uploadm);
		spinner_uploads.setAdapter(arradp_uploads);
		spn_tagend.setAdapter(arr_tagend);

		button_chanageareapwd = (Button) findViewById(R.id.button_changegareapwd);
		button_changearealock = (Button) findViewById(R.id.button_changearealock);
		button_changearealocknopwd = (Button) findViewById(R.id.button_changearealocknopwd);
		button_uploadget = (Button) findViewById(R.id.button_xbuploadget);
		button_uploadset = (Button) findViewById(R.id.button_xbuploadset);
		button_chanageareapwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {

					SetFiler();
					KUNRUI_CustomParam kuricp = new Custom().new KUNRUI_CustomParam();
					EditText timestr = (EditText) findViewById(R.id.editText_customtime);
					if (timestr.getText().toString().equals("")) {
						Toast.makeText(SubCustomActivity.this,
								MyApplication.Constr_subcsplsetimeou,
								Toast.LENGTH_SHORT).show();
						return;
					}
					EditText et = (EditText) findViewById(R.id.editText_customapwd);
					EditText et2 = (EditText) findViewById(R.id.editText_customnowpwd);
					EditText et3 = (EditText) findViewById(R.id.editText_customnewpwd);
					if (et2.getText().toString().equals("")
							|| et3.getText().toString().equals("")) {
						Toast.makeText(SubCustomActivity.this,
								MyApplication.Constr_subcsputcnpwd,
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (spinner_userarea.getSelectedItemPosition() == -1) {
						Toast.makeText(SubCustomActivity.this,
								MyApplication.Constr_subcsplselreg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					// String temp=et.getText().toString();

					Gen2Password g2pw = new Gen2Password(et.getText()
							.toString());
					Gen2Password nowpw = new Gen2Password(et2.getText()
							.toString());
					Gen2Password newpw = new Gen2Password(et3.getText()
							.toString());

					kuricp.Set_ChangeAreaPwd(
							Short.parseShort(timestr.getText().toString()),
							g2pw.GetPasswordBytes(),
							(byte) spinner_userarea.getSelectedItemPosition(),
							newpw.GetPasswordBytes(), nowpw.GetPasswordBytes(),
							g2tf);

					KUNRUI_CustomResult cr = (KUNRUI_CustomResult) myapp.Mreader
							.CustomCmd(g2tf,
									CustomCmdType.KUNRUI_ChangeAreaPwd, kuricp);
					if (cr != null)
						Toast.makeText(SubCustomActivity.this,
								"epc:" + Functional.bytes_Hexstr(cr.epcid),
								Toast.LENGTH_SHORT).show();
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubCustomActivity.this,
							MyApplication.Constr_subcsopfail + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		button_changearealock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					SetFiler();
					KUNRUI_CustomParam kuricp = new Custom().new KUNRUI_CustomParam();
					EditText timestr = (EditText) findViewById(R.id.editText_customtime);
					if (timestr.getText().toString().equals("")) {
						Toast.makeText(SubCustomActivity.this,
								MyApplication.Constr_subcsplsetimeou,
								Toast.LENGTH_SHORT).show();
						return;
					}
					EditText et = (EditText) findViewById(R.id.editText_customapwd);
					EditText et2 = (EditText) findViewById(R.id.editText_customnowpwd_lock);
					if (et2.getText().toString().equals("")) {
						Toast.makeText(SubCustomActivity.this,
								MyApplication.Constr_subcsputcurpwd,
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (spinner_userarea.getSelectedItemPosition() == -1) {
						Toast.makeText(SubCustomActivity.this,
								MyApplication.Constr_subcsplselreg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					// String temp=et.getText().toString();
					Gen2Password g2pw = new Gen2Password(et.getText()
							.toString());
					Gen2Password nowpw = new Gen2Password(et2.getText()
							.toString());

					byte mask = 0, action = 0;
					short payload = 0;

					/*
					 * if(cb_maskread.isChecked()) mask|=0x02;
					 * if(cb_maskwirte.isChecked()) mask|=0x01;
					 * 
					 * 
					 * if(cb_actionread.isChecked()) action|=0x02;
					 * if(cb_actionwrite.isChecked()) action|=0x01;
					 */
					if (spinner_readwrite.getSelectedItemPosition() == 0)
						mask |= 0x02;
					else if (spinner_readwrite.getSelectedItemPosition() == 1)
						mask |= 0x01;

					if (spinner_readwrite.getSelectedItemPosition() == 0
							&& spinner_lockunlock.getSelectedItemPosition() == 0)
						action |= 0x02;
					else if (spinner_readwrite.getSelectedItemPosition() == 0
							&& spinner_lockunlock.getSelectedItemPosition() == 1)
						action |= 0x00;
					else if (spinner_readwrite.getSelectedItemPosition() == 1
							&& spinner_lockunlock.getSelectedItemPosition() == 0)
						action |= 0x01;
					else if (spinner_readwrite.getSelectedItemPosition() == 1
							&& spinner_lockunlock.getSelectedItemPosition() == 1)
						action |= 0x00;

					payload = (short) (mask << 8 | action);
					kuricp.Set_ChangeAreaLock(
							Short.parseShort(timestr.getText().toString()),
							g2pw.GetPasswordBytes(),
							(byte) spinner_userarea.getSelectedItemPosition(),
							payload, nowpw.GetPasswordBytes(), g2tf);

					KUNRUI_CustomResult cr = (KUNRUI_CustomResult) myapp.Mreader
							.CustomCmd(g2tf,
									CustomCmdType.KUNRUI_ChangeAreaLock, kuricp);
					Toast.makeText(SubCustomActivity.this,
							"epc:" + Functional.bytes_Hexstr(cr.epcid),
							Toast.LENGTH_SHORT).show();

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubCustomActivity.this,
							MyApplication.Constr_subcsopfail + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		button_changearealocknopwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					SetFiler();
					KUNRUI_CustomParam kuricp = new Custom().new KUNRUI_CustomParam();
					EditText timestr = (EditText) findViewById(R.id.editText_customtime);
					if (timestr.getText().toString().equals("")) {
						Toast.makeText(SubCustomActivity.this,
								MyApplication.Constr_subcsplsetimeou,
								Toast.LENGTH_SHORT).show();
						return;
					}
					EditText et = (EditText) findViewById(R.id.editText_customapwd);
					if (spinner_userarea.getSelectedItemPosition() == -1) {
						Toast.makeText(SubCustomActivity.this,
								MyApplication.Constr_subcsplselreg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					// String temp=et.getText().toString();
					Gen2Password g2pw = new Gen2Password(et.getText()
							.toString());

					byte mask = 0, action = 0;
					// cb_maskread.setChecked(false);
					// cb_maskwirte.setChecked(false);

					/*
					 * if(cb_actionread.isChecked()) action|=0x02;
					 * if(cb_actionwrite.isChecked()) action|=0x01;
					 */
					if (spinner_readwrite.getSelectedItemPosition() == 0
							&& spinner_lockunlock.getSelectedItemPosition() == 0)
						action |= 0x02;
					else if (spinner_readwrite.getSelectedItemPosition() == 0
							&& spinner_lockunlock.getSelectedItemPosition() == 1)
						action |= 0x00;
					else if (spinner_readwrite.getSelectedItemPosition() == 1
							&& spinner_lockunlock.getSelectedItemPosition() == 0)
						action |= 0x01;
					else if (spinner_readwrite.getSelectedItemPosition() == 1
							&& spinner_lockunlock.getSelectedItemPosition() == 1)
						action |= 0x00;

					kuricp.Set_ChangeAreaLockwithoutPwd(
							Short.parseShort(timestr.getText().toString()),
							g2pw.GetPasswordBytes(),
							(byte) spinner_userarea.getSelectedItemPosition(),
							action, g2tf);

					KUNRUI_CustomResult cr = (KUNRUI_CustomResult) myapp.Mreader
							.CustomCmd(
									g2tf,
									CustomCmdType.KUNRUI_ChangeAreaLockWithoutPwd,
									kuricp);
					Toast.makeText(SubCustomActivity.this,
							"epc:" + Functional.bytes_Hexstr(cr.epcid),
							Toast.LENGTH_SHORT).show();

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubCustomActivity.this,
							MyApplication.Constr_subcsopfail + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		button_uploadget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					int vali = (Integer) myapp.Mreader
							.paramGet(ParamNames.InitMode_XBCUSTOM);
					int mval = ((vali & 0xff000000) >> 24) & 0xff;
					if (mval == 0xa5 | mval == 0x5a) {
						if (mval == 0xa5)
							spinner_uploadm.setSelection(0);
						else
							spinner_uploadm.setSelection(1);

						int sval = (vali & 0x00ff0000) >> 16;
						spinner_uploads.setSelection(sval);

						if (sval == 4 | sval == 5) {
							et_xblen.setEnabled(true);
							et_xbstadr.setEnabled(true);
							int aval = (vali & 0x0000ff00) >> 8;
							et_xbstadr.setText(String.valueOf(aval));

							int lval = (vali & 0x000000ff);
							et_xblen.setText(String.valueOf(lval));
						} else {
							et_xblen.setEnabled(false);
							et_xbstadr.setEnabled(false);
						}

					} else {
						spinner_uploadm.setSelection(2);
						spinner_uploads.setEnabled(false);
						et_xblen.setEnabled(false);
						et_xbstadr.setEnabled(false);
					}
					int vali2 = (Integer) myapp.Mreader
							.paramGet(ParamNames.InitMode_RFU);
					if (vali2 == 0xA5000000)
						cb_xbfilter.setChecked(true);
					else
						cb_xbfilter.setChecked(false);
					 
					 R2000_calibration.R2000cmd rcmdo=null;
					 R2000_calibration r2000pcmd = new R2000_calibration();
			            R2000_calibration.OEM_DATA r2000oem = null;
			            R2000_calibration.MAC_DATA r2000mac = null;
 
			                rcmdo = R2000_calibration.R2000cmd.OEMread;
			                r2000oem = r2000pcmd.new OEM_DATA((short) 0x0460);
			               
			                    byte[] senddata = null;
 
			                        senddata = r2000pcmd.GetSendCmd(rcmdo, r2000oem.ToByteData());
			                    
			                    myapp.CommBth.Comm_Write(senddata, 0,senddata.length);
			                    
			    				byte[] part1 = new byte[3];
			    				byte[] part2=null;
			    				byte[] revdata=null;
			    				int re = myapp.CommBth.Comm_Read(part1,0,part1.length);

			    				if (re > 0 && part1[2] != 0x00) {
			    					
			    					int l = (part1[1] & 0xff) + 4;
			    					part2 = new byte[l];
			    					revdata= new byte[l+3];
			    					System.arraycopy(part1, 0, revdata, 0, 3);
			    					 
			    					re = myapp.CommBth.Comm_Read(part2,0, part2.length);
			    					if (re > 0&&((part2[0]+part2[1])==0))
			    						System.arraycopy(part2, 0, revdata, 3, part2.length);
			    					else
			    					{
			    						 Toast.makeText(SubCustomActivity.this, "收发指令失败"+
			    								 String.format("%02x", part2[0])+String.format("%02x", part2[1]),
			    								 Toast.LENGTH_SHORT)
				    	    				.show();
				    		                return;
			    					}
			    				}
			    				else
			    				 {
			    		                Toast.makeText(SubCustomActivity.this, "收发指令失败", Toast.LENGTH_SHORT)
			    	    				.show();
			    		                return;
			    		            }
			    			 
			                    byte[] data = new byte[revdata.length - 19];
			                    System.arraycopy(revdata, 17, data, 0, data.length);
			                    R2000_calibration.OEM_DATA r2000data = r2000pcmd.new OEM_DATA(data);

			                    R2000_calibration.OEM_DATA.Adpair[] adp = r2000data.GetAddr();
			                    spn_tagend.setSelection(adp[0].val);
			                
					
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubCustomActivity.this,
							MyApplication.Constr_subcsopfail + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		button_uploadset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				int setval = 0;
				int mval = spinner_uploadm.getSelectedItemPosition();
				if (mval == 0)
					setval |= 0xa5 << 24;
				else if (mval == 1)
					setval |= 0x5a << 24;

				if (mval == 0 | mval == 1) {
					byte sval = (byte) spinner_uploads
							.getSelectedItemPosition();

					setval |= sval << 16;

					if (sval == 4 || sval == 5) {
						byte st = Byte.valueOf(et_xbstadr.getText().toString());
						byte len = Byte.valueOf(et_xblen.getText().toString());

						setval |= st << 8;
						setval |= len;
					}
				}

				try {
					myapp.Mreader.paramSet(ParamNames.InitMode_XBCUSTOM,
							String.valueOf(setval));
					int sval = 0;
					if (cb_xbfilter.isChecked())
						sval = 0xA5000000;

					myapp.Mreader.paramSet(ParamNames.InitMode_RFU,
							String.valueOf(sval));

					R2000_calibration r2000pcmd = new R2000_calibration();
					R2000_calibration.R2000cmd rcmdo = null;

					R2000_calibration.OEM_DATA r2000oem = null;

					rcmdo = R2000_calibration.R2000cmd.OEMwrite;
					r2000oem = r2000pcmd.new OEM_DATA((short) 0x0460,
							spn_tagend.getSelectedItemPosition());

					byte[] senddata = null;
					senddata = r2000pcmd.GetSendCmd(rcmdo,
							r2000oem.ToByteData());
					myapp.CommBth.Comm_Write(senddata, 0, senddata.length);
					byte[] part1 = new byte[3];
					byte[] part2 = null;
					byte[] revdata = null;
					int re = myapp.CommBth.Comm_Read(part1, 0,part1.length);

					if (re > 0 && part1[2] != 0x00) {
						int l = (part1[1] & 0xff) + 4;
						part2 = new byte[l];
						revdata = new byte[l + 3];
						System.arraycopy(part1, 0, revdata, 0, 3);

						re = myapp.CommBth.Comm_Read(part2, 0, part2.length);
						if (re > 0)
							System.arraycopy(part2, 0, revdata, 3, part2.length);

					} else {
						Toast.makeText(SubCustomActivity.this, "配置标签结束符号失败",
								Toast.LENGTH_SHORT).show();
						return;
					}
					
					Toast.makeText(SubCustomActivity.this,
							MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(SubCustomActivity.this,
							MyApplication.Constr_subcsopfail + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		spinner_uploadm.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				et_xblen.setEnabled(false);
				et_xbstadr.setEnabled(false);
				if (arg2 == 0 | arg2 == 1) {
					spinner_uploads.setEnabled(true);
					// spinner_uploads.setSelection(0);
				} else {
					spinner_uploads.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		spinner_uploads.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				et_xblen.setEnabled(false);
				et_xbstadr.setEnabled(false);
				if (arg2 == 4 | arg2 == 5) {
					et_xblen.setEnabled(true);
					et_xbstadr.setEnabled(true);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		tabHost_custom_lunrui
				.setOnTabChangedListener(new OnTabChangeListener() {

					@Override
					public void onTabChanged(String arg0) {
						// TODO Auto-generated method stub
						int j = tabHost_custom_lunrui.getCurrentTab();
						TabWidget tabIndicator = tabHost_custom_lunrui
								.getTabWidget();
						View vw = tabIndicator.getChildAt(j);
						vw.setBackgroundColor(Color.BLUE);
						int tc = tabHost_custom_lunrui.getTabContentView()
								.getChildCount();
						for (int i = 0; i < tc; i++) {
							if (i != j) {
								View vw2 = tabIndicator.getChildAt(i);
								vw2.setBackgroundColor(Color.TRANSPARENT);
							}
						}

					}

				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
