package com.example.module_android_bluedemo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.example.module_android_bluedemo.MyAdapter2.ViewHolder;

import com.silionmodule.AntPower;
import com.silionmodule.GPioPin;
import com.silionmodule.Gen2;
import com.silionmodule.Gen2.Gen2Password;
import com.silionmodule.Gen2.Gen2TagFilter;
import com.silionmodule.Gen2.MemBankE;
import com.silionmodule.Gen2.ReadData;
import com.silionmodule.Gen2.SessionE;
import com.silionmodule.Gen2.TagEncodingE;
import com.silionmodule.Gen2.TargetE;
import com.silionmodule.Gen2.TariE;
import com.silionmodule.Gen2.WriteModeE;
import com.silionmodule.Functional;
import com.silionmodule.HardWareDetector.Module_Type;
import com.silionmodule.ParamNames;
import com.silionmodule.ReaderException;
import com.silionmodule.Region;
import com.silionmodule.Region.RegionE;
import com.silionmodule.TagOp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

public class Sub4TabActivity extends Activity {

	String[] spireg;
	String[] spibank;
	String[] spifbank;
	String[] spises = { "S0", "S1", "S2", "S3" };
	String[] spipow = { "500", "600", "700", "800", "900", "1000", "1100",
			"1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900",
			"2000", "2100", "2200", "2300", "2400", "2500", "2600", "2700",
			"2800", "2900", "3000"};// 运行时添加
	String[] spiq = { "自动", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15" };
	String[] spinvmo;
	String[] spiblf = { "40", "250", "400", "640" };
	String[] spimlen = { "96", "496" };
	String[] spitget = { "A", "B", "A-B", "B-A" };
	String[] spigcod = { "FM0", "M2", "M4", "M8","PROFILE0","PROFILE1","PROFILE2","PROFILE3","PROFILE4","PROFILE5" };
	String[] spitari;
	String[] spiwmod;
	String[] spi6btzsd = { "99percent", "11percent" };
	String[] spidelm = { "Delimiter1", "Delimiter4" };
	String[] spiperst = { "0%", "5%", "10%", "15%", "20%", "25%", "30%", "35%",
			"40%", "45%", "50%" };
	String[] spigpodemo;

	CheckBox cb_gpo1, cb_gpo2, cb_gpo3, cb_gpo4, cb_gpi1, cb_gpi2, cb_gpi3,
			cb_gpi4, cb_oant, cb_odata, cb_hrssi, cb_allsel, cb_uant1,
			cb_uant2, cb_uant3, cb_uant4, cb_pwdm,cb_nostop, cbmf_readcount, cbmf_rssi,
			cbmf_ant, cbmf_fre, cbmf_time, cbmf_rfu, cbmf_pro, cbmf_dl, cb_fre,
			cb_showuinemd, cb_showuinant, cb_1200handset,cb_HB;

	RadioGroup rg_emdenable, rg_antcheckenable, rg_invfilenable,
			rg_invfilmatch;

	private ArrayAdapter arrdp_bank, arrdp_fbank, arrdp_ses, arradp_pow,
			arrdp_q, arrdp_invmo, arrdp_blf, arrdp_mlen, arrdp_tget,
			arrdp_g2cod, arrdp_tari, arrdp_wmod, arrdp_6btzsd, arrdp_delm,
			arradp_reg, arrdp_per,arrdp_gpodemo;
	Spinner spinner_ant1rpow, spinner_ant1wpow, spinner_ant2rpow,
			spinner_ant2wpow, spinner_ant3rpow, spinner_ant3wpow,
			spinner_ant4rpow, spinner_ant4wpow, spinner_sesion, spinner_q,
			spinner_wmode, spinner_blf, spinner_maxlen, spinner_target,
			spinner_g2code, spinner_tari, spinner_emdbank, spinner_filbank,
			spinner_region, spinner_invmode, spinner_6btzsd, spinner_delmi,
			spinner_persen,spinner_gpodemo;

	TabHost tabHost_set;

	Button button_getantpower, button_setantpower, button_getantcheck,
			button_setantcheck, button_getgen2ses, button_setgen2ses,
			button_getgen2q, button_setgen2q, button_getwmode, button_setwmode,
			button_getgen2blf, button_setgenblf, button_getgen2maxl,
			button_setgen2maxl, button_getgen2targ, button_setgen2targ,
			button_getgen2code, button_setgen2code, button_getgen2tari,
			button_setgen2tari, button_setgpo, button_getgpi, button_getemd,
			button_setemd, button_getfil, button_setfil, button_getreg,
			button_setreg, button_getfre, button_setfre, button_getusl,
			button_setusl, button_oantuqget, button_oantuqset,
			button_odatauqget, button_odatauqset, button_hrssiget,
			button_hrssiset, button_invmodeget, button_invmodeset,
			button_6bdpget, button_6bdpset, button_6bdltget, button_6bdltset,
			button_6bblfget, button_6bblfset, button_invantsget,
			button_invantset, button_bluetoinit_2, button_bluetopass_2,
			button_quicklyread, button_q11200, button_q21200,button_forstop,
			button_latetime,button_stoptime,button_isstatics,button_allpwset,
			button_gpodemo;
	ListView elist;
	MyApplication myapp;
	public static int nowpower;

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

	private void showlist(String[] fres) {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(fres));
		MyAdapter2 mAdapter = new MyAdapter2(list, this);
		// 绑定Adapter

		elist.setAdapter(mAdapter);
	}

	public static int[] Sort(int[] array) {
		int tmpIntValue = 0;
		for (int xIndex = 0; xIndex < array.length; xIndex++) {
			for (int yIndex = 0; yIndex < array.length; yIndex++) {
				if (array[xIndex] < array[yIndex]) {
					tmpIntValue = (Integer) array[xIndex];
					array[xIndex] = array[yIndex];
					array[yIndex] = tmpIntValue;
				}
			}
		}

		return array;
	}

	public static int[] CollectionTointArray(List list) {
		Iterator itor = list.iterator();
		int[] backdata = new int[list.size()];
		int i = 0;
		while (itor.hasNext()) {
			backdata[i++] = (int) (Integer) itor.next();
		}
		return backdata;
	}

	private void SetPassword() throws ReaderException
	 {
		    EditText et=(EditText)findViewById(R.id.editText_emdacspwd);
		   // String temp=et.getText().toString();
		    if(cb_pwdm.isChecked())
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab4_tablelayout);

		Application app = getApplication();
		myapp = (MyApplication) app;

		spireg = myapp.spireg;
		spibank = myapp.spibank;
		spifbank = myapp.spifbank;
		spiq[0] = myapp.Auto;
		spinvmo = myapp.spinvmo;
		spitari = myapp.spitari;
		spiwmod = myapp.spiwmod;
		spigpodemo=myapp.gpodemo;
		// 获取TabHost对象
		// 得到TabActivity中的TabHost对象
		tabHost_set = (TabHost) findViewById(R.id.tabhost4);
		// 如果没有继承TabActivity时，通过该种方法加载启动tabHost
		tabHost_set.setup();
		tabHost_set.getTabWidget().setOrientation(LinearLayout.VERTICAL);
		// tabHost2.addTab(tabHost2.newTabSpec("tab1").setIndicator("初始化",
		// getResources().getDrawable(R.drawable.ic_launcher)).setContent(R.id.tab11));
		// tabHost2.addTab(tabHost2.newTabSpec("tab1").setIndicator(createIndicatorView(this,
		// tabHost2, "1111"))
		// .setContent(R.id.tab11));
//		tabHost_set.addTab(tabHost_set
//				.newTabSpec("tab1")
//				.setIndicator(
//						createIndicatorView(this, tabHost_set,
//								MyApplication.Constr_sub4invenpra))
//				.setContent(R.id.tab4_sub1_invusl));
		tabHost_set.addTab(tabHost_set
				.newTabSpec("tab2")
				.setIndicator(
						createIndicatorView(this, tabHost_set,
								MyApplication.Constr_sub4antpow))
				.setContent(R.id.tab4_sub2_antpow));
		tabHost_set.addTab(tabHost_set
				.newTabSpec("tab3")
				.setIndicator(
						createIndicatorView(this, tabHost_set,
								MyApplication.Constr_sub4regionfre))
				.setContent(R.id.tab4_sub3_invfre));
//		tabHost_set.addTab(tabHost_set
//				.newTabSpec("tab4")
//				.setIndicator(
//						createIndicatorView(this, tabHost_set,
//								MyApplication.Constr_sub4gen2opt))
//				.setContent(R.id.tab4_sub4_gen2));
//		tabHost_set.addTab(tabHost_set
//				.newTabSpec("tab5")
//				.setIndicator(
//						createIndicatorView(this, tabHost_set,
//								MyApplication.Constr_sub4invenfil))
//				.setContent(R.id.tab4_sub5_invfil));
//		tabHost_set.addTab(tabHost_set
//				.newTabSpec("tab6")
//				.setIndicator(
//						createIndicatorView(this, tabHost_set,
//								MyApplication.Constr_sub4addidata))
//				.setContent(R.id.tab4_sub6_emd));
//		tabHost_set.addTab(tabHost_set.newTabSpec("tab7")
//				.setIndicator(createIndicatorView(this, tabHost_set, "GPIO"))
//				.setContent(R.id.tab4_sub7_gpio));
//		tabHost_set.addTab(tabHost_set
//				.newTabSpec("tab8")
//				.setIndicator(
//						createIndicatorView(this, tabHost_set,
//								MyApplication.Constr_sub4others))`
//				.setContent(R.id.tab4_sub8_others));
//		tabHost_set.addTab(tabHost_set
//				.newTabSpec("tab9")
//				.setIndicator(
//						createIndicatorView(this, tabHost_set,
//								MyApplication.Constr_sub4quickly))
//				.setContent(R.id.tab4_sub9_quickly));

		TabWidget tw = tabHost_set.getTabWidget();
		tw.getChildAt(0).setBackgroundColor(Color.BLUE);
		// tabHost2.setCurrentTab(2);

		spinner_ant1rpow = (Spinner) findViewById(R.id.spinner_ant1rpow);
		spinner_ant1wpow = (Spinner) findViewById(R.id.spinner_ant1wpow);
		spinner_ant2rpow = (Spinner) findViewById(R.id.spinner_ant2rpow);
		spinner_ant2wpow = (Spinner) findViewById(R.id.spinner_ant2wpow);
		spinner_ant3rpow = (Spinner) findViewById(R.id.spinner_ant3rpow);
		spinner_ant3wpow = (Spinner) findViewById(R.id.spinner_ant3wpow);
		spinner_ant4rpow = (Spinner) findViewById(R.id.spinner_ant4rpow);
		spinner_ant4wpow = (Spinner) findViewById(R.id.spinner_ant4wpow);

		// /*
		arradp_pow = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spipow);
		arradp_pow
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_ant1rpow.setAdapter(arradp_pow);
		spinner_ant1wpow.setAdapter(arradp_pow);
		spinner_ant2rpow.setAdapter(arradp_pow);
		spinner_ant2wpow.setAdapter(arradp_pow);
		spinner_ant3rpow.setAdapter(arradp_pow);
		spinner_ant3wpow.setAdapter(arradp_pow);
		spinner_ant4rpow.setAdapter(arradp_pow);
		spinner_ant4wpow.setAdapter(arradp_pow);

		spinner_sesion = (Spinner) findViewById(R.id.spinner_gen2session);
		arrdp_ses = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spises);
		arrdp_ses
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_sesion.setAdapter(arrdp_ses);

		spinner_q = (Spinner) findViewById(R.id.spinner_gen2q);
		arrdp_q = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spiq);
		arrdp_q.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_q.setAdapter(arrdp_q);

		spinner_wmode = (Spinner) findViewById(R.id.spinner_gen2wmode);
		arrdp_wmod = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spiwmod);
		arrdp_wmod
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_wmode.setAdapter(arrdp_wmod);

		spinner_blf = (Spinner) findViewById(R.id.spinner_gen2blf);
		arrdp_blf = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spiblf);
		arrdp_blf
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_blf.setAdapter(arrdp_blf);

		spinner_maxlen = (Spinner) findViewById(R.id.spinner_gen2maxl);
		arrdp_mlen = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spimlen);
		arrdp_mlen
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_maxlen.setAdapter(arrdp_mlen);

		spinner_target = (Spinner) findViewById(R.id.spinner_target);
		arrdp_tget = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spitget);
		arrdp_tget
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_target.setAdapter(arrdp_tget);

		spinner_g2code = (Spinner) findViewById(R.id.spinner_gen2code);
		arrdp_g2cod = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spigcod);
		arrdp_g2cod
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_g2code.setAdapter(arrdp_g2cod);

		spinner_tari = (Spinner) findViewById(R.id.spinner_gen2tari);
		arrdp_tari = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spitari);
		arrdp_tari
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_tari.setAdapter(arrdp_tari);

		spinner_emdbank = (Spinner) findViewById(R.id.spinner_emdbank);
		arrdp_bank = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spibank);
		arrdp_bank
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_emdbank.setAdapter(arrdp_bank);

		spinner_filbank = (Spinner) findViewById(R.id.spinner_invfbank);
		arrdp_fbank = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spifbank);
		arrdp_fbank
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_filbank.setAdapter(arrdp_fbank);

		spinner_invmode = (Spinner) findViewById(R.id.spinner_invmode);
		arrdp_invmo = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinvmo);
		arrdp_invmo
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_invmode.setAdapter(arrdp_invmo);

		spinner_6btzsd = (Spinner) findViewById(R.id.spinner_6bdlt);
		arrdp_6btzsd = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spi6btzsd);
		arrdp_6btzsd
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_6btzsd.setAdapter(arrdp_6btzsd);

		spinner_delmi = (Spinner) findViewById(R.id.spinner_6bdp);
		arrdp_delm = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spidelm);
		arrdp_delm
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_delmi.setAdapter(arrdp_delm);

		spinner_region = (Spinner) findViewById(R.id.spinner_region);
		arradp_reg = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spireg);
		arradp_reg
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_region.setAdapter(arradp_reg);

		spinner_persen = (Spinner) findViewById(R.id.spinner_per);
		arrdp_per = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spiperst);
		arrdp_per
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_persen.setAdapter(arrdp_per);
		
		spinner_gpodemo = (Spinner) findViewById(R.id.spinner_gpodemo);
		arrdp_gpodemo = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spigpodemo);
		arrdp_gpodemo
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_gpodemo.setAdapter(arrdp_gpodemo);

		rg_antcheckenable = (RadioGroup) findViewById(R.id.radioGroup_antcheck);
		button_getantpower = (Button) findViewById(R.id.button_antpowget);
		button_setantpower = (Button) findViewById(R.id.button_antpowset);
		button_getantcheck = (Button) findViewById(R.id.button_checkantget);
		button_setantcheck = (Button) findViewById(R.id.button_checkantset);

		button_getgen2ses = (Button) findViewById(R.id.button_gen2sesget);
		button_setgen2ses = (Button) findViewById(R.id.button_gen2sesset);
		button_getgen2q = (Button) findViewById(R.id.button_gen2qget);
		button_setgen2q = (Button) findViewById(R.id.button_gen2qset);
		button_getwmode = (Button) findViewById(R.id.button_gen2wmodeget);
		button_setwmode = (Button) findViewById(R.id.button_gen2wmodeset);
		button_getgen2blf = (Button) findViewById(R.id.button_gen2blfget);
		button_setgenblf = (Button) findViewById(R.id.button_gen2blfset);
		button_getgen2maxl = (Button) findViewById(R.id.button_gen2mlget);
		button_setgen2maxl = (Button) findViewById(R.id.button_gen2mlset);
		button_getgen2targ = (Button) findViewById(R.id.button_target);
		button_setgen2targ = (Button) findViewById(R.id.button_targetset);
		button_getgen2code = (Button) findViewById(R.id.button_codeget);
		button_setgen2code = (Button) findViewById(R.id.button_codeset);
		button_getgen2tari = (Button) findViewById(R.id.button_gen2tariget);
		button_setgen2tari = (Button) findViewById(R.id.button_gen2tariset);
		button_setgpo = (Button) findViewById(R.id.button_gposet);
		button_getgpi = (Button) findViewById(R.id.button_gpiget);
		
		button_forstop = (Button) findViewById(R.id.button_forstop);
		button_latetime = (Button) findViewById(R.id.button_latetime);
		button_stoptime = (Button) findViewById(R.id.button_stoptime);
		button_isstatics = (Button) findViewById(R.id.button_isstatics);
		button_allpwset = (Button) findViewById(R.id.button_setallsamepw);
		button_gpodemo = (Button) findViewById(R.id.button_gpodemoset);
		
		cb_gpo1 = (CheckBox) findViewById(R.id.checkBox_gpo1);
		cb_gpo2 = (CheckBox) findViewById(R.id.checkBox_gpo2);
		cb_gpo3 = (CheckBox) findViewById(R.id.checkBox_gpo3);
		cb_gpo4 = (CheckBox) findViewById(R.id.checkBox_gpo4);
		cb_gpi1 = (CheckBox) findViewById(R.id.checkBox_gpi1);
		cb_gpi2 = (CheckBox) findViewById(R.id.checkBox_gpi2);
		cb_gpi3 = (CheckBox) findViewById(R.id.checkBox_gpi3);
		cb_gpi4 = (CheckBox) findViewById(R.id.checkBox_gpi4);
		cb_allsel = (CheckBox) findViewById(R.id.checkBox_allselect);
		button_getemd = (Button) findViewById(R.id.button_getemd);
		button_setemd = (Button) findViewById(R.id.button_setemd);
		rg_emdenable = (RadioGroup) findViewById(R.id.radioGroup_emdenable);
		button_getfil = (Button) findViewById(R.id.button_getfil);
		button_setfil = (Button) findViewById(R.id.button_setfil);
		button_getreg = (Button) findViewById(R.id.button_getregion);
		button_setreg = (Button) findViewById(R.id.button_setregion);
		button_getfre = (Button) findViewById(R.id.button_getfre);
		button_setfre = (Button) findViewById(R.id.button_setfre);
		button_getusl = (Button) findViewById(R.id.button_invuslget);
		button_setusl = (Button) findViewById(R.id.button_invuslset);
		rg_invfilenable = (RadioGroup) findViewById(R.id.radioGroup_enablefil);
		rg_invfilmatch = (RadioGroup) findViewById(R.id.radioGroup_invmatch);
		elist = (ListView) this.findViewById(R.id.listView_frequency);

		button_oantuqget = (Button) findViewById(R.id.button_oantuqget);
		button_oantuqset = (Button) findViewById(R.id.button_oantuqset);
		button_odatauqget = (Button) findViewById(R.id.button_odatauqget);
		button_odatauqset = (Button) findViewById(R.id.button_odatauqset);
		button_hrssiget = (Button) findViewById(R.id.button_hrssiget);
		button_hrssiset = (Button) findViewById(R.id.button_hrssiset);
		button_invmodeget = (Button) findViewById(R.id.button_invmodeget);
		button_invmodeset = (Button) findViewById(R.id.button_invmodeset);
		button_6bdpget = (Button) findViewById(R.id.button_6bdpget);
		button_6bdpset = (Button) findViewById(R.id.button_6bdpset);
		button_6bdltget = (Button) findViewById(R.id.button_6bdltget);
		button_6bdltset = (Button) findViewById(R.id.button_6bdltset);
		button_6bblfget = (Button) findViewById(R.id.button_6bblfget);
		button_6bblfset = (Button) findViewById(R.id.button_6bblfset);
		cb_oant = (CheckBox) findViewById(R.id.checkBox_oantuq);
		cb_odata = (CheckBox) findViewById(R.id.checkBox_odatauq);
		cb_hrssi = (CheckBox) findViewById(R.id.checkBox_hrssi);
		button_invantsget = (Button) findViewById(R.id.button_invantsget);
		button_invantset = (Button) findViewById(R.id.button_invantsset);
		cb_uant1 = (CheckBox) findViewById(R.id.checkBox_ant1);
		cb_uant2 = (CheckBox) findViewById(R.id.checkBox_ant2);
		cb_uant3 = (CheckBox) findViewById(R.id.checkBox_ant3);
		cb_uant4 = (CheckBox) findViewById(R.id.checkBox_ant4);
		
		cbmf_readcount = (CheckBox) this.findViewById(R.id.checkBox_readcount);
		cbmf_rssi = (CheckBox) this.findViewById(R.id.checkBox_rssi);
		cbmf_ant = (CheckBox) this.findViewById(R.id.checkBox_ant);
		cbmf_fre = (CheckBox) this.findViewById(R.id.checkBox_frequency);
		cbmf_time = (CheckBox) this.findViewById(R.id.checkBox_time);
		cbmf_rfu = (CheckBox) this.findViewById(R.id.checkBox_rfu);
		cbmf_pro = (CheckBox) this.findViewById(R.id.checkBox_pro);
		cbmf_dl = (CheckBox) this.findViewById(R.id.checkBox_tagdatalen);
		cb_nostop = (CheckBox) findViewById(R.id.checkBox_nostop);
		cb_1200handset= (CheckBox) findViewById(R.id.checkBox_1200handle);
		cb_HB= (CheckBox) findViewById(R.id.checkBox_HB);
		cb_pwdm=(CheckBox)findViewById(R.id.checkBox_emdmatch);
		
		button_bluetoinit_2 = (Button) findViewById(R.id.button_toinit_pass);
		button_bluetopass_2 = (Button) findViewById(R.id.button_topass_pass);

		button_quicklyread = (Button) findViewById(R.id.button_nostop);
		button_q11200 = (Button) findViewById(R.id.button_q1enable1200);
		button_q21200 = (Button) findViewById(R.id.button_q2enable1200);

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

		button_bluetoinit_2.setOnClickListener(new OnClickListener() {

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
							Toast.makeText(getApplicationContext(),
									MyApplication.Constr_sub4setmodefail,
									Toast.LENGTH_SHORT).show();
							return;
						}
					} else {
						Toast.makeText(getApplicationContext(),
								MyApplication.Constr_sub4hadactivemo,
								Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(getApplicationContext(),
						MyApplication.Constr_sub4setokresettoab,
						Toast.LENGTH_SHORT).show();
			}
		});
		button_bluetopass_2.setOnClickListener(new OnClickListener() {

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
							Toast.makeText(getApplicationContext(),
									MyApplication.Constr_sub4setmodefail,
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								MyApplication.Constr_sub4hadpasstivemo,
								Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(getApplicationContext(),
						MyApplication.Constr_sub4setokresettoab,
						Toast.LENGTH_SHORT).show();

			}
		});
		button_invantsget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cb_uant1.setChecked(false);
				cb_uant2.setChecked(false);
				cb_uant3.setChecked(false);
				cb_uant4.setChecked(false);
				int[] conants = null;
				try {
					conants = (int[]) myapp.Mreader
							.paramGet(ParamNames.Reader_Antenna_ConnectedPortList);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (conants != null && conants.length != 0) {
					for (int i = 0; i < conants.length; i++) {
						if (conants[i] == 1)
							cb_uant1.setChecked(true);
						else if (conants[i] == 2)
							cb_uant2.setChecked(true);
						else if (conants[i] == 3)
							cb_uant3.setChecked(true);
						else if (conants[i] == 4)
							cb_uant4.setChecked(true);
					}
				} else
					for (int i = 0; i < myapp.Rparams.uants.length; i++) {
						if (myapp.Rparams.uants[i] == 1)
							cb_uant1.setChecked(true);
						else if (myapp.Rparams.uants[i] == 2)
							cb_uant2.setChecked(true);
						else if (myapp.Rparams.uants[i] == 3)
							cb_uant3.setChecked(true);
						else if (myapp.Rparams.uants[i] == 4)
							cb_uant4.setChecked(true);
					}
			}
		});
		button_invantset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				List<Integer> lb = new ArrayList<Integer>();
				if (cb_uant1.isChecked())
					lb.add(1);
				if (cb_uant2.isChecked())
					lb.add(2);
				if (cb_uant3.isChecked())
					lb.add(3);
				if (cb_uant4.isChecked())
					lb.add(4);
				Integer[] iv = new Integer[lb.size()];
				myapp.Rparams.uants = new int[lb.size()];
				lb.toArray(iv);
				for (int i = 0; i < iv.length; i++)
					myapp.Rparams.uants[i] = iv[i];
			}
		});

		button_getantcheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					boolean bl = (Boolean) myapp.Mreader
							.paramGet(ParamNames.Reader_Antenna_CheckPort);
					if (!bl)
						rg_antcheckenable.check(rg_antcheckenable.getChildAt(0)
								.getId());
					else
						rg_antcheckenable.check(rg_antcheckenable.getChildAt(1)
								.getId());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setantcheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {

					if (SortGroup(rg_antcheckenable) == 0)
						myapp.Mreader.paramSet(
								ParamNames.Reader_Antenna_CheckPort, false);
					else
						myapp.Mreader.paramSet(
								ParamNames.Reader_Antenna_CheckPort, true);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});
		button_getantpower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					AntPower[] ap = (AntPower[]) myapp.Mreader
							.paramGet(ParamNames.Reader_Radio_PortPowerList);
					for (int i = 0; i < ap.length; i++) {
						if (i == 0) {
							spinner_ant1rpow.setSelection((ap[0].Readpower() - 500) / 100);
							spinner_ant1wpow.setSelection(((ap[0].Writepower() - 500) / 100));
						} else if (i == 1) {
							spinner_ant2rpow.setSelection((ap[1].Readpower() - 500) / 100);
							spinner_ant2wpow.setSelection(((ap[1].Writepower() - 500) / 100));
						} else if (i == 2) {
							spinner_ant3rpow.setSelection((ap[2].Readpower() - 500) / 100);
							spinner_ant3wpow.setSelection(((ap[2].Writepower() - 500) / 100));
						} else if (i == 3) {
							spinner_ant4rpow.setSelection((ap[3].Readpower() - 500) / 100);
							spinner_ant4wpow.setSelection(((ap[3].Writepower() - 500) / 100));
						}
					}

					/*
					 * int[][] powr=(int[][])myapp.Mreader.paramGet(ParamNames.
					 * Reader_Radio_PortReadPowerList); int[][]
					 * poww=(int[][])myapp
					 * .Mreader.paramGet(ParamNames.Reader_Radio_PortWritePowerList
					 * );
					 * 
					 * for(int i=0;i<powr.length;i++) { if(i==0)
					 * spinner_ant1rpow.setSelection((powr[i][1]-500)/100); else
					 * if(i==1)
					 * spinner_ant2rpow.setSelection((powr[i][1]-500)/100); else
					 * if(i==2)
					 * spinner_ant3rpow.setSelection((powr[i][1]-500)/100); else
					 * if(i==3)
					 * spinner_ant4rpow.setSelection((powr[i][1]-500)/100); }
					 * 
					 * for(int i=0;i<poww.length;i++) { if(i==0)
					 * spinner_ant1wpow.setSelection((poww[i][1]-500)/100); else
					 * if(i==1)
					 * spinner_ant2wpow.setSelection((poww[i][1]-500)/100); else
					 * if(i==2)
					 * spinner_ant3wpow.setSelection((poww[i][1]-500)/100); else
					 * if(i==3)
					 * spinner_ant4wpow.setSelection((poww[i][1]-500)/100); }
					 */

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setantpower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] rp = new int[4];
				int[] wp = new int[4];

				rp[0] = spinner_ant1rpow.getSelectedItemPosition();
				rp[1] = spinner_ant2rpow.getSelectedItemPosition();
				rp[2] = spinner_ant3rpow.getSelectedItemPosition();
				rp[3] = spinner_ant4rpow.getSelectedItemPosition();

				wp[0] = spinner_ant1wpow.getSelectedItemPosition();
				wp[1] = spinner_ant2wpow.getSelectedItemPosition();
				wp[2] = spinner_ant3wpow.getSelectedItemPosition();
				wp[3] = spinner_ant4wpow.getSelectedItemPosition();
				if (myapp.Rparams.antportc == 2
						&& myapp.Mreader.HardWare().module == Module_Type.MODOULE_SLR5300) {
					if (rp[0] != rp[1] || rp[0] != wp[0] || rp[0] != wp[1]) {
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_sub4ndsapow,
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				int[][] powr = new int[myapp.Rparams.antportc][2];
				for (int i = 0; i < myapp.Rparams.antportc; i++) {
					powr[i][0] = i + 1;
					powr[i][1] = 500 + rp[i] * 100;
				}

				int[][] poww = new int[myapp.Rparams.antportc][2];
				for (int i = 0; i < myapp.Rparams.antportc; i++) {
					poww[i][0] = i + 1;
					poww[i][1] = 500 + wp[i] * 100;
				}

				AntPower[] ap = new AntPower[myapp.Rparams.antportc];
				for (int i = 0; i < myapp.Rparams.antportc; i++) {
					ap[i] = new AntPower(i + 1, powr[i][1], poww[i][1]);
				}

				try {
					myapp.Mreader.paramSet(
							ParamNames.Reader_Radio_PortPowerList, ap);
					Sub4TabActivity.nowpower=ap[0].Readpower();
					// myapp.Mreader.paramSet(ParamNames.Reader_Radio_PortReadPowerList,
					// powr);
					// myapp.Mreader.paramSet(ParamNames.Reader_Radio_PortWritePowerList,
					// poww);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}

				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});

		button_getgen2ses.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.SessionE g2se = (Gen2.SessionE) myapp.Mreader
							.paramGet(ParamNames.Reader_Gen2_Session);
					switch (g2se) {
					case Session0:
						spinner_sesion.setSelection(0);
						break;
					case Session1:
						spinner_sesion.setSelection(1);
						break;
					case Session2:
						spinner_sesion.setSelection(2);
						break;
					case Session3:
						spinner_sesion.setSelection(3);
						break;
					}
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setgen2ses.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.SessionE g2se = null;

					switch (spinner_sesion.getSelectedItemPosition()) {
					case 0:
						g2se = SessionE.Session0;
						break;
					case 1:
						g2se = SessionE.Session1;
						break;
					case 2:
						g2se = SessionE.Session2;
						break;
					case 3:
						g2se = SessionE.Session3;
						break;
					}
					myapp.Mreader
							.paramSet(ParamNames.Reader_Gen2_Session, g2se);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});
		button_getgen2q.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.Q g2q = (Gen2.Q) myapp.Mreader
							.paramGet(ParamNames.Reader_Gen2_Q);
					if (g2q instanceof Gen2.DynamicQ)
						spinner_q.setSelection(0);
					else {
						Gen2.StaticQ g2sq = (Gen2.StaticQ) g2q;
						spinner_q.setSelection(g2sq.initialQ + 1);
					}

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setgen2q.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.Q g2q = null;

					switch (spinner_q.getSelectedItemPosition()) {
					case 0:
						g2q = new Gen2.DynamicQ();
						break;
					default:
						g2q = new Gen2.StaticQ(spinner_q
								.getSelectedItemPosition() - 1);
					}
					myapp.Mreader.paramSet(ParamNames.Reader_Gen2_Q, g2q);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});

		button_getwmode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.WriteModeE g2we = (Gen2.WriteModeE) myapp.Mreader
							.paramGet(ParamNames.Reader_Gen2_WriteMode);
					if (g2we == WriteModeE.WORD_ONLY)
						spinner_wmode.setSelection(0);
					else if (g2we == WriteModeE.BLOCK_ONLY)
						spinner_wmode.setSelection(1);

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setwmode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.WriteModeE g2we = null;

					switch (spinner_wmode.getSelectedItemPosition()) {
					case 0:
						g2we = WriteModeE.WORD_ONLY;
						break;
					default:
						g2we = WriteModeE.BLOCK_ONLY;
					}
					myapp.Mreader.paramSet(ParamNames.Reader_Gen2_WriteMode,
							g2we);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();

			}

		});
		button_getgen2blf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int it = (Integer) myapp.Mreader
							.paramGet(ParamNames.Reader_Gen2_BLF);

					switch (it) {
					case 40:
						spinner_blf.setSelection(0);
						break;
					case 250:
						spinner_blf.setSelection(1);
						break;
					case 400:
						spinner_blf.setSelection(2);
						break;
					case 640:
						spinner_blf.setSelection(3);
						break;
					}
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setgenblf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int vl = -1;

					switch (spinner_blf.getSelectedItemPosition()) {
					case 0:
						vl = 40;
						break;
					case 1:
						vl = 250;
						break;
					case 2:
						vl = 400;
						break;
					case 3:
						vl = 640;
						break;
					}
					myapp.Mreader.paramSet(ParamNames.Reader_Gen2_BLF, vl);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();

			}

		});
		button_getgen2maxl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();

			}

		});
		button_setgen2maxl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();

			}

		});
		button_getgen2targ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.TargetE g2te = (Gen2.TargetE) myapp.Mreader
							.paramGet(ParamNames.Reader_Gen2_Target);
					switch (g2te) {
					case A:
						spinner_target.setSelection(0);
						break;
					case B:
						spinner_target.setSelection(1);
						break;
					case AB:
						spinner_target.setSelection(2);
						break;
					case BA:
						spinner_target.setSelection(3);
						break;
					}
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setgen2targ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.TargetE g2se = null;

					switch (spinner_target.getSelectedItemPosition()) {
					case 0:
						g2se = TargetE.A;
						break;
					case 1:
						g2se = TargetE.B;
						break;
					case 2:
						g2se = TargetE.AB;
						break;
					case 3:
						g2se = TargetE.BA;
						break;
					}
					myapp.Mreader.paramSet(ParamNames.Reader_Gen2_Target, g2se);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();

			}

		});
		button_getgen2code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.TagEncodingE g2te = (Gen2.TagEncodingE) myapp.Mreader
							.paramGet(ParamNames.Reader_Gen2_TagEncoding);
					switch (g2te) {
					case FM0:
						spinner_g2code.setSelection(0);
						break;
					case M2:
						spinner_g2code.setSelection(1);
						break;
					case M4:
						spinner_g2code.setSelection(2);
						break;
					case M8:
						spinner_g2code.setSelection(3);
						break;
					case PROFILE0:
						spinner_g2code.setSelection(4);
						break;
					case PROFILE1:
						spinner_g2code.setSelection(5);
						break;
					case PROFILE2:
						spinner_g2code.setSelection(6);
						break;
					case PROFILE3:
						spinner_g2code.setSelection(7);
						break;
					case PROFILE4:
						spinner_g2code.setSelection(8);
						break;
					case PROFILE5:
						spinner_g2code.setSelection(9);
						break;
					}
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

		});
		button_setgen2code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.TagEncodingE g2se = null;

					switch (spinner_g2code.getSelectedItemPosition()) {
					case 0:
						g2se = TagEncodingE.FM0;
						break;
					case 1:
						g2se = TagEncodingE.M2;
						break;
					case 2:
						g2se = TagEncodingE.M4;
						break;
					case 3:
						g2se = TagEncodingE.M8;
					case 4:
						g2se = TagEncodingE.PROFILE0;
						break;
					case 5:
						g2se = TagEncodingE.PROFILE1;
						break;
					case 6:
						g2se = TagEncodingE.PROFILE2;
						break;
					case 7:
						g2se = TagEncodingE.PROFILE3;
						break;
					case 8:
						g2se = TagEncodingE.PROFILE4;
						break;
					case 9:
						g2se = TagEncodingE.PROFILE5;
						break;
					}
					myapp.Mreader.paramSet(ParamNames.Reader_Gen2_TagEncoding,
							g2se);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();

			}

		});
		button_getgen2tari.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.TariE g2te = (Gen2.TariE) myapp.Mreader
							.paramGet(ParamNames.Reader_Gen2_Tari);
					switch (g2te) {
					case TARI_25US:
						spinner_tari.setSelection(0);
						break;
					case TARI_12_5US:
						spinner_tari.setSelection(1);
						break;
					case TARI_6_25US:
						spinner_tari.setSelection(2);
						break;

					}
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setgen2tari.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2.TariE g2se = null;

					switch (spinner_tari.getSelectedItemPosition()) {
					case 0:
						g2se = TariE.TARI_25US;
						break;
					case 1:
						g2se = TariE.TARI_12_5US;
						break;
					case 2:
						g2se = TariE.TARI_6_25US;
						break;

					}
					myapp.Mreader.paramSet(ParamNames.Reader_Gen2_Tari, g2se);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();

			}

		});

		button_getgpi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					GPioPin[] gpp = (GPioPin[]) myapp.Mreader.GPIGet();
					for (int i = 0; i < gpp.length; i++) {
						if (gpp[i].ID() == 1) {
							if (gpp[i].High())
								cb_gpi1.setChecked(true);
							else
								cb_gpi1.setChecked(false);
						} else if (gpp[i].ID() == 2) {
							if (gpp[i].High())
								cb_gpi2.setChecked(true);
							else
								cb_gpi2.setChecked(false);
						} else if (gpp[i].ID() == 3) {
							if (gpp[i].High())
								cb_gpi3.setChecked(true);
							else
								cb_gpi3.setChecked(false);
						} else if (gpp[i].ID() == 4) {
							if (gpp[i].High())
								cb_gpi4.setChecked(true);
							else
								cb_gpi4.setChecked(false);
						}
					}

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setgpo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					GPioPin[] state = new GPioPin[2];
					GPioPin gpp1 = new GPioPin(1, cb_gpo1.isChecked());
					state[0] = gpp1;

					GPioPin gpp2 = new GPioPin(2, cb_gpo2.isChecked());
					state[1] = gpp2;

					/*
					 * GPioPin gpp3=new GPioPin(3,cb_gpo3.isChecked());
					 * state[2]=gpp3;
					 * 
					 * GPioPin gpp4=new GPioPin(4,cb_gpo4.isChecked());
					 * state[3]=gpp4;
					 */

					myapp.Mreader.GPOSet(state);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});
		button_getemd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				EditText etst = (EditText) findViewById(R.id.editText_emdsadr);
				EditText etapwd = (EditText) findViewById(R.id.editText_emdacspwd);
				EditText etct = (EditText) findViewById(R.id.editText_emdcount);
				if (myapp.Rparams.To == null) {
					etst.setText("");
					etapwd.setText("");
					etct.setText("");
					rg_emdenable.check(rg_emdenable.getChildAt(0).getId());
					spinner_emdbank.setSelection(0);
				} else {
					Gen2.ReadData g2rd = (ReadData) myapp.Rparams.To;
					etst.setText(String.valueOf(g2rd.WordAddress));
					etapwd.setText("");
					etct.setText(String.valueOf(g2rd.Len));
					rg_emdenable.check(rg_emdenable.getChildAt(1).getId());
					switch (g2rd.Bank) {
					case RESERVED:
						spinner_emdbank.setSelection(0);
						break;
					case EPC:
						spinner_emdbank.setSelection(1);
						break;
					case TID:
						spinner_emdbank.setSelection(2);
						break;
					case USER:
						spinner_emdbank.setSelection(3);
						break;
					}
				}
			}

		});
		button_setemd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (SortGroup(rg_emdenable) == 1) {
					EditText etst = (EditText) findViewById(R.id.editText_emdsadr);
					// EditText
					// etapwd=(EditText)findViewById(R.id.editText_emdacspwd);
					EditText etct = (EditText) findViewById(R.id.editText_emdcount);
					Gen2.MemBankE mb = null;
					switch (spinner_emdbank.getSelectedItemPosition()) {
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
					TagOp tp = new Gen2.ReadData(mb, Integer.valueOf(etst
							.getText().toString()), (byte)(Byte.parseByte(etct
							.getText().toString())/2));
					myapp.Rparams.To = tp;
				} else
					myapp.Rparams.To = null;

				try {
					SetPassword();
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill, Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});

		button_getfil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Gen2TagFilter g2tf = (Gen2TagFilter) myapp.Rparams.Tf;
				EditText et = (EditText) findViewById(R.id.editText_filterdata);
				EditText etadr = (EditText) findViewById(R.id.editText_invfilsadr);
				if (g2tf == null) {
					rg_invfilenable
							.check(rg_invfilenable.getChildAt(0).getId());
					et.setText("");
					etadr.setText("");
					spinner_filbank.setSelection(0);
				} else {
					rg_invfilenable
							.check(rg_invfilenable.getChildAt(1).getId());
					et.setText(Functional.bytes_Hexstr(g2tf.FilterData()));
					etadr.setText(String.valueOf(g2tf.Filteraddress()));
					MemBankE mb = (MemBankE) g2tf.GetAttribe();
					if (mb == MemBankE.EPC)
						spinner_filbank.setSelection(0);
					else if (mb == MemBankE.TID)
						spinner_filbank.setSelection(1);
					else if (mb == MemBankE.USER)
						spinner_filbank.setSelection(2);
					if (g2tf.Invert())
						rg_invfilmatch.check(rg_invfilmatch.getChildAt(1)
								.getId());
					else
						rg_invfilmatch.check(rg_invfilmatch.getChildAt(0)
								.getId());
				}

			}

		});
		button_setfil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Gen2TagFilter g2tf = null;
					if (SortGroup(rg_invfilenable) == 1) {
						byte[] fdata;
						EditText et = (EditText) findViewById(R.id.editText_filterdata);
						fdata = Functional
								.hexstr_Bytes(et.getText().toString());
						int it = spinner_filbank.getSelectedItemPosition() + 1;
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
						EditText etadr = (EditText) findViewById(R.id.editText_invfilsadr);

						g2tf = new Gen2TagFilter(mb, Integer.valueOf(etadr
								.getText().toString()), fdata, Integer.valueOf(et
										.getText().toString().length())*4);
						int ma = SortGroup(rg_invfilmatch);
						if (ma == 1)
							g2tf.SetInvert(true);
					} else
						g2tf = null;

					// myapp.Mreader.paramSet(ParamNames.Reader_Read_Filter,
					// g2tf);
					myapp.Rparams.Tf = g2tf;

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});
		button_getreg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Region.RegionE rre = (RegionE) myapp.Mreader
							.paramGet(ParamNames.Reader_Region_Id);

					switch (rre) {
					case China:
						spinner_region.setSelection(0);
						break;
					case NA:
						spinner_region.setSelection(1);
						break;
					case Korea:
						spinner_region.setSelection(3);
						break;
					case Europe3:
						spinner_region.setSelection(6);
						break;
					case IN:
						spinner_region.setSelection(7);
						break;
					case OPEN:
						spinner_region.setSelection(9);
						break;
					case China2:
						spinner_region.setSelection(10);
						break;
					default:
						spinner_region.setSelection(1);
						break;
					}
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setreg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Region.RegionE rre = RegionE.Unknown;
				switch (spinner_region.getSelectedItemPosition()) {
				case 0:
					rre = RegionE.China;
					break;
				case 1:
					rre = RegionE.NA;
					break;
				case 2:
					rre = RegionE.Unknown;
					break;
				case 3:
					rre = RegionE.Korea;
					break;
				case 4:
					rre = RegionE.Europe1;
					break;
				case 5:
					rre = RegionE.Europe2;
					break;
				case 6:
					rre = RegionE.Europe3;
					break;
				case 7:
					rre = RegionE.IN;
					break;
				case 8:
					rre = RegionE.Unknown;
					break;
				case 9:
					rre = RegionE.OPEN;
					break;
				case 10:
					rre = RegionE.China2;
					break;
				default:
					rre = RegionE.Unknown;
					break;
				}
				if (rre == RegionE.Unknown) {
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_sub4unspreg,
							Toast.LENGTH_SHORT).show();
					return;
				}

				try {
					myapp.Mreader.paramSet(ParamNames.Reader_Region_Id, rre);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}

				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});

		button_getfre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] tablefre = (int[]) myapp.Mreader
							.paramGet(ParamNames.Reader_Region_HopTable);
					tablefre = Sort(tablefre);
					String[] ssf = new String[tablefre.length];
					for (int i = 0; i < tablefre.length; i++) {
						ssf[i] = String.valueOf(tablefre[i]);
					}
					showlist(ssf);
					cb_allsel.setChecked(false);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setfre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					ArrayList<Integer> lit = new ArrayList<Integer>();
					for (int i = 0; i < elist.getCount(); i++) {
						String temp = (String) elist.getItemAtPosition(i);
						if (MyAdapter2.getIsSelected().get(i)) {
							lit.add(Integer.valueOf(temp));
						}

					}
					if (lit.size() > 0) {
						int[] vls = CollectionTointArray(lit);

						myapp.Mreader.paramSet(
								ParamNames.Reader_Region_HopTable, vls);

					}

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});

		button_getusl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EditText ettime = (EditText) findViewById(R.id.editText_invtime);
				EditText etsleep = (EditText) findViewById(R.id.editText_invsleep);

				ettime.setText(String.valueOf((myapp.Rparams.readtime)));
				etsleep.setText(String.valueOf((myapp.Rparams.sleep)));
			}

		});
		button_setusl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText ettime = (EditText) findViewById(R.id.editText_invtime);
					EditText etsleep = (EditText) findViewById(R.id.editText_invsleep);
					myapp.Rparams.readtime = Integer.valueOf(ettime.getText()
							.toString());
					myapp.Rparams.sleep = Integer.valueOf(etsleep.getText()
							.toString());

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});
		button_oantuqget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					boolean bl = (Boolean) myapp.Mreader
							.paramGet(ParamNames.Reader_TagReadData_UniqueByAntenna);

					cb_oant.setChecked(bl);

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_oantuqset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {

					myapp.Mreader.paramSet(
							ParamNames.Reader_TagReadData_UniqueByAntenna,
							cb_oant.isChecked());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});
		button_odatauqget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					boolean bl = (Boolean) myapp.Mreader
							.paramGet(ParamNames.Reader_TagReadData_UniqueByData);

					cb_odata.setChecked(bl);

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_odatauqset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {

					myapp.Mreader.paramSet(
							ParamNames.Reader_TagReadData_UniqueByData,
							cb_odata.isChecked());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});

		button_hrssiget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					boolean bl = (Boolean) myapp.Mreader
							.paramGet(ParamNames.Reader_TagReadData_RecordHighestRssi);

					cb_hrssi.setChecked(bl);

				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_hrssiset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {

					myapp.Mreader.paramSet(
							ParamNames.Reader_TagReadData_RecordHighestRssi,
							cb_hrssi.isChecked());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});
		button_invmodeget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();

			}

		});
		button_invmodeset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();
			}

		});

		button_6bdpget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();

			}

		});
		button_6bdpset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();
			}

		});

		button_6bdltget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();

			}

		});
		button_6bdltset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();
			}

		});

		button_6bblfget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();

			}

		});
		button_6bblfget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this,
						MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();
			}

		});

		/**
		 * 配置快速模式
		 */
		button_quicklyread.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				myapp.qmioption.tmf.IsEmdData = false;
				myapp.qmioption.tmf.IsReadCnt = false;
				myapp.qmioption.tmf.IsRSSI = false;
				myapp.qmioption.tmf.IsAntennaID = false;
				myapp.qmioption.tmf.IsFrequency = false;
				myapp.qmioption.tmf.IsTimestamp = false;
				myapp.qmioption.tmf.IsRFU = false;
				myapp.qmioption.tmf.IsPro = false;
				myapp.qmioption.tmf.IsEmdData = false;
				if (cbmf_readcount.isChecked()) {

					myapp.qmioption.tmf.IsReadCnt = true;
				}
				if (cbmf_rssi.isChecked()) {

					myapp.qmioption.tmf.IsRSSI = true;
				}
				if (cbmf_ant.isChecked()) {

					myapp.qmioption.tmf.IsAntennaID = true;
				}
				if (cbmf_fre.isChecked()) {

					myapp.qmioption.tmf.IsFrequency = true;
				}
				if (cbmf_time.isChecked()) {

					myapp.qmioption.tmf.IsTimestamp = true;
				}
				if (cbmf_rfu.isChecked()) {

					myapp.qmioption.tmf.IsRFU = true;
				}
				if (cbmf_pro.isChecked()) {
					myapp.qmioption.tmf.IsPro = true;
				}
				if (cbmf_dl.isChecked()) {
					myapp.qmioption.tmf.IsEmdData = true;
				}
				myapp.Rparams.option = 0;
				myapp.qmioption.stoppercent=spinner_persen.getSelectedItemPosition();
				// 0-10 0%-50%
				myapp.qmioption.ishandlmodefor1200=cb_1200handset.isChecked();
				myapp.qmioption.isHeartbeat=cb_HB.isChecked();
				 
				myapp.Rparams.option=myapp.qmioption.getcmdotpion();

				if (cb_nostop.isChecked())
					myapp.isquicklymode = true;
				else
					myapp.isquicklymode = false;
			}
		});

		/**
		 * 快速模式并且最大功率以及session1
		 */
		button_q11200.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				myapp.Rparams.option = 0;
				myapp.isquicklymode = true;
				try {
					int mp;
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
							ParamNames.Reader_Radio_PortPowerList, ap);
					Gen2.SessionE g2se = SessionE.Session1;
					myapp.Mreader
							.paramSet(ParamNames.Reader_Gen2_Session, g2se);

					/*
					 * Toast.makeText(Sub4TabActivity.this,
					 * MyApplication.Constr_set + "Session 1",
					 * Toast.LENGTH_SHORT).show();
					 */
					myapp.Rparams.sleep = 0;
				} catch (ReaderException e) {

				}

				cb_nostop.setChecked(true);
			}
		});

		/**
		 * 快速模式并且最大功率以及session0
		 */
		button_q21200.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				myapp.Rparams.option = 0;
				myapp.isquicklymode = true;
				try {
					int mp;
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
							ParamNames.Reader_Radio_PortPowerList, ap);

					Gen2.SessionE g2se = SessionE.Session0;
					myapp.Mreader
							.paramSet(ParamNames.Reader_Gen2_Session, g2se);

					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_set + "Session 0",
							Toast.LENGTH_SHORT).show();
					myapp.Rparams.sleep = 0;
				} catch (ReaderException e) {

				}
				// if (myapp.myhd.module==Module_Type.MODOULE_SLR1200)
				{
					myapp.Rparams.option |= 0x10;
				}
				cb_nostop.setChecked(true);
			}
		});
		
		
		button_stoptime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					CheckBox cb = (CheckBox) findViewById(R.id.checkBox_chkstoptime);
					myapp.isstoptime = cb.isChecked();
					if (myapp.isstoptime) {
						EditText et_autostoptime = (EditText) findViewById(R.id.editText_stoptime);
						myapp.stoptimems = Integer.parseInt(et_autostoptime
								.getText().toString());
					}
				} catch (Exception ex) {

				}
			}
			
		});
		button_latetime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					CheckBox cb = (CheckBox) findViewById(R.id.checkBox_chklatetime);
					myapp.islatetime=cb.isChecked();
					if (myapp.islatetime) {
						EditText et_latetime = (EditText) findViewById(R.id.editText_latetime);
						myapp.latetimems = Integer.parseInt(et_latetime
								.getText().toString());
					}
				} catch (Exception ex) {

				}
			}
			
		});
		button_isstatics.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
					CheckBox cb = (CheckBox) findViewById(R.id.checkBox_chkstatics);
					myapp.VisStatics=cb.isChecked();
					
					 if(myapp.VisStatics){
							try{
								//"/sdcard/testrecord.txt");
					           myapp.file = new File(Environment.getExternalStorageDirectory().getPath()+"/testrecord.txt");
					            if(myapp.file.exists())
					            	myapp.fs=new FileOutputStream(myapp.file,true);
					            else
					            {
					            	myapp.fs = new FileOutputStream(myapp.file);
					            String wstr = "Test count,per sec/add/total count,cost time\r\n";
					            myapp.fs.write(wstr.getBytes());}
					          }catch (Exception ex)
					         {
					            Toast.makeText(Sub4TabActivity.this, ex.getMessage(), Toast.LENGTH_SHORT)
					                    .show();
					         }
							 }
				 
			}
		});
		
		button_forstop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					CheckBox cb = (CheckBox) findViewById(R.id.checkBox_autostopcount);
					myapp.VisTestFor=cb.isChecked();
					if (myapp.VisTestFor) {
						EditText et_autostoptimecount=(EditText)findViewById(R.id.editText_autostopcount);
						EditText et_testforsleep=(EditText)findViewById(R.id.editText_testforsleep);
						myapp.Vtestforcount = Integer.parseInt(et_autostoptimecount
								.getText().toString());
						myapp.Vtestforsleep = Integer.parseInt(et_testforsleep
								.getText().toString());
					}
				} catch (Exception ex) {

				}

			}
			
		});
		
		/**
		 * 统一功率设置
		 */
		button_allpwset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				int[] rp = new int[4];
				int[] wp = new int[4];
				EditText allp = (EditText) findViewById(R.id.editText_allpw);
				for (int i = 0; i < 4; i++) {
					rp[i] = Integer.parseInt(allp.getText().toString());
					wp[i] = rp[i];
				}
 
				AntPower[] ap = new AntPower[myapp.Rparams.antportc];
				for (int i = 0; i < myapp.Rparams.antportc; i++) {
					ap[i] = new AntPower(i + 1, rp[i], wp[i]);
				}

				try {
					myapp.Mreader.paramSet(
							ParamNames.Reader_Radio_PortPowerList, ap);
					Sub4TabActivity.nowpower=ap[0].Readpower();
					// myapp.Mreader.paramSet(ParamNames.Reader_Radio_PortReadPowerList,
					// powr);
					// myapp.Mreader.paramSet(ParamNames.Reader_Radio_PortWritePowerList,
					// poww);
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.GetMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
		
		button_gpodemo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				myapp.gpodemomode=spinner_gpodemo.getSelectedItemPosition();
				if(myapp.gpodemomode>2)
				{
					
					if(cb_gpo1.isChecked())
						myapp.gpodemo1=true;
					else
						myapp.gpodemo1=false;
					if(cb_gpo2.isChecked())
						myapp.gpodemo2=true;
					else
						myapp.gpodemo2=false;
					
					if(cb_gpo3.isChecked())
						myapp.gpodemo3=true;
					else
						myapp.gpodemo3=false;
					if(cb_gpo4.isChecked())
						myapp.gpodemo4=true;
					else
						myapp.gpodemo4=false;
				}
				try{
					EditText et=(EditText)findViewById(R.id.editText_gpodemo);
					String valstr=et.getText().toString();
					myapp.gpodemotime=Integer.parseInt(valstr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			}

		});
		
		cb_allsel.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				MyAdapter2 m2 = (MyAdapter2) elist.getAdapter();
				if (m2 == null)
					return;
				if (arg1 == true) {
					HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();

					for (int m = 0; m < m2.getCount(); m++)
						isSelected.put(m, true);
					m2.setIsSelected(isSelected);

				} else {
					HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();

					for (int m = 0; m < m2.getCount(); m++)
						isSelected.put(m, false);
					m2.setIsSelected(isSelected);
				}
				elist.setAdapter(m2);
			}

		});

		tabHost_set.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				// TODO Auto-generated method stub
				int j = tabHost_set.getCurrentTab();
				TabWidget tabIndicator = tabHost_set.getTabWidget();
				View vw = tabIndicator.getChildAt(j);
				vw.setBackgroundColor(Color.BLUE);
				int tc = tabHost_set.getTabContentView().getChildCount();
				for (int i = 0; i < tc; i++) {
					if (i != j) {
						View vw2 = tabIndicator.getChildAt(i);
						vw2.setBackgroundColor(Color.TRANSPARENT);
					} else {
						/*
						 * switch(i) { case 0: button_getusl.performClick();
						 * break; case 1: button_getantcheck.performClick();
						 * button_getantpower.performClick(); break; case 2:
						 * button_getreg.performClick();
						 * button_getfre.performClick(); break; case 3:
						 * button_getgen2ses.performClick();
						 * button_getgen2q.performClick();
						 * button_getwmode.performClick();
						 * button_getgen2blf.performClick();
						 * button_getgen2maxl.performClick();
						 * button_getgen2targ.performClick();
						 * button_getgen2code.performClick();
						 * button_getgen2tari.performClick(); break; case 4:
						 * button_getfil.performClick(); break; case 5:
						 * button_getemd.performClick(); break; case 7:
						 * button_oantuqget.performClick();
						 * button_odatauqget.performClick();
						 * button_hrssiget.performClick(); break;
						 * 
						 * } //
						 */
					}
				}
			}
		});

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

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onResume() {
		spinner_ant2rpow.setEnabled(false);
		spinner_ant2wpow.setEnabled(false);
		spinner_ant3rpow.setEnabled(false);
		spinner_ant3wpow.setEnabled(false);
		spinner_ant4rpow.setEnabled(false);
		spinner_ant4wpow.setEnabled(false);
		// */
		if (myapp.Rparams.antportc >= 2) {
			spinner_ant2rpow.setEnabled(true);
			spinner_ant2wpow.setEnabled(true);
		}
		if (myapp.Rparams.antportc >= 3) {
			spinner_ant3rpow.setEnabled(true);
			spinner_ant3wpow.setEnabled(true);
		}
		if (myapp.Rparams.antportc >= 4) {
			spinner_ant4rpow.setEnabled(true);
			spinner_ant4wpow.setEnabled(true);
		}
		if (myapp.Rparams.antportc > 1) {
			button_invantsget.setEnabled(true);
			button_invantset.setEnabled(true);
		}
		cb_uant2.setEnabled(false);
		cb_uant3.setEnabled(false);
		cb_uant4.setEnabled(false);
		switch (myapp.Rparams.antportc) {
		case 2:
			cb_uant1.setEnabled(true);
			cb_uant2.setEnabled(true);
			break;
		case 3:
			cb_uant1.setEnabled(true);
			cb_uant2.setEnabled(true);
			cb_uant3.setEnabled(true);
			break;
		case 4:
			cb_uant1.setEnabled(true);
			cb_uant2.setEnabled(true);
			cb_uant3.setEnabled(true);
			cb_uant4.setEnabled(true);
			break;
		}

		super.onResume();
	}

	protected void onStop() {
		super.onStop();
	}
}
