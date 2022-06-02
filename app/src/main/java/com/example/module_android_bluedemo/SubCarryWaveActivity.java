package com.example.module_android_bluedemo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.communication.inf.CommunicationException;
import com.function.commfun;
import com.silionmodule.Functional;
import com.silionmodule.ParamNames;
import com.silionmodule.R2000_calibration;
import com.silionmodule.ReaderException;
import com.silionmodule.Region;
import com.silionmodule.R2000_calibration.VSWRReturnloss_DATA;
import com.silionmodule.ReaderException.ERROR;
import com.silionmodule.Region.RegionE;
import com.tool.log.LogD;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SubCarryWaveActivity extends Activity {
	MyApplication myapp;
	Button button_cwset, button_on, button_off,button_allfreturnloss;
	TextView tv_ibpow, tv_ibrpow, tv_ipow, tv_iapow, tv_iarpow, tv_cnt, tv_tep;

	short oemcfgaddr_rfcal_fwdpwr_A2 = 0x00000091;
	short oemcfgaddr_rfcal_fwdpwr_A1 = 0x00000092;
	short oemcfgaddr_rfcal_fwdpwr_A0 = 0x00000093;

	short oemcfgaddr_rfcal_revpwr_A2 = 0x000003C6;
	short oemcfgaddr_rfcal_revpwr_A1 = 0x000003C7;
	short oemcfgaddr_rfcal_revpwr_A0 = 0x000003C8;
	double fa2;
	double fa1;
	double fa0;

	double ra2;
	double ra1;
	double ra0;
	R2000_calibration.MAC_DATA r2000macp = new R2000_calibration().new MAC_DATA(
			(short) 0x0B00);
	R2000_calibration.MAC_DATA r2000macfp = new R2000_calibration().new MAC_DATA(
			(short) 0x0B04);
	R2000_calibration.R2000cmd rcmdo2 = R2000_calibration.R2000cmd.OEMread;
	R2000_calibration.R2000cmd rcmdo = R2000_calibration.R2000cmd.readMAC;
	R2000_calibration r2000pcmd = new R2000_calibration();
	int ant =1, power = 3000, fre = 915250;
	 
	int x_w = 55, x_wadd = 30;// x段宽度
	int Height = 600, Width = x_w * 50 + x_wadd;// 坐标轴长宽
	int spit = 5;// 分几段
	int x_st = 60, y_st = 200;// 坐标轴中心
	int x_wbl = 20, x_wsl = 10;// x大段长，x小段长
	int xy_x_char = 8, xy_y_char = 20;// 坐标轴 点 文字位置
	int x_char = 15;// x轴上 文字位置

	

	public class MsgObj {
		public byte[] soh = new byte[1];
		public byte[] dataLen = new byte[1];
		public byte[] opCode = new byte[1];
		public byte[] status = new byte[2];
		public byte[] crc = new byte[2];
		public byte[] data = new byte[250];

		public byte[] getcheckcrcdata() {
			int l=dataLen[0]&0xff;
			byte[] crcb = new byte[l + 4];
			int p = 0;
			crcb[p] = dataLen[0];
			p++;
			crcb[p++] = opCode[0];
			crcb[p++] = status[0];
			crcb[p++] = status[1];
			for (int i = 0; i < l; i++)
				crcb[p++] = data[i];
			return crcb;
		}
	}

	void FlushDummyData2Mod() throws CommunicationException {
		/* if (m_stream->isOpen) */
		{
			byte[] zerobuf = new byte[255];
			zerobuf[0] = (byte) 0xff;
			zerobuf[1] = (byte) 250;
			zerobuf[2] = 0x0;
			for (int i = 3; i < 255; ++i)
				zerobuf[i] = 0;

			 myapp.CommBth.Comm_Write(zerobuf, 0, 255);
		}

	}

	void TestModLive() throws CommunicationException {

		byte[] cmd = new byte[] { (byte) 0xff, 0x00, 0x03, 0x1d, 0x0c };
		byte[] resp = new byte[50];
		byte[] resp2 = new byte[50];
		myapp.CommBth.Comm_Write(cmd, 0, cmd.length);
		 
		if(myapp.CommBth.Comm_Read(resp, 0, 5)>=5)
		{
		
		if (myapp.CommBth.Comm_Read(resp2, 0,resp[1] + 2)==resp[1]+2)
			return;
		}
		
		throw new CommunicationException(ERROR.PORT_OP_EORROR, "read less");
	}

	private void SendandRev(byte[] data, MsgObj hMsg) throws CommunicationException {
		int COMM_NON_FATAL_ERR = 0xfefd;
		int MODULE_NEED_RESTART = 0xfefe;
		short MSG_CRC_INIT = (short) 0xFFFF;
        
		LogD.LOGD("send:" + Functional.bytes_Hexstr(data));

		int re = myapp.CommBth.Comm_Write(data, 0, data.length);
 
		short scrc;
	 
		int ret = 0;

		System.out.print("revd:");
		try{
		ret = myapp.CommBth.Comm_Read(hMsg.soh,0, 1);
		 
		}catch(CommunicationException cex)
		{
			throw new CommunicationException(ERROR.PORT_OP_EORROR, String.valueOf(ret));
		}
 
		if ((hMsg.soh[0] & 0xff) != 0xff) {
		   FlushDummyData2Mod();
		   throw new CommunicationException(ERROR.PORT_OP_EORROR, String.valueOf(ret));
		}

		scrc = MSG_CRC_INIT;

		 myapp.CommBth.Comm_Read(hMsg.dataLen, 0,1);
		LogD.LOGD("datalen:"+String.valueOf(hMsg.dataLen[0]&0xff));

		 myapp.CommBth.Comm_Read(hMsg.opCode, 0,1);
		 

		myapp.CommBth.Comm_Read(hMsg.status, 0,2); 
	    int l=hMsg.dataLen[0]&0xff;
		if (l > 0) {
		     myapp.CommBth.Comm_Read(hMsg.data,0, l);
			byte[] fdata = new byte[l];
			System.arraycopy(hMsg.data, 0, fdata, 0, l);
			LogD.LOGD(Functional.bytes_Hexstr(fdata));
			 
		}

	    myapp.CommBth.Comm_Read(hMsg.crc, 0,2);
		LogD.LOGD(Functional.bytes_Hexstr(hMsg.crc));

		scrc = (short) (((hMsg.crc[0] & 0xFF) << 8) | (hMsg.crc[1] & 0xFF));

		if (R2000_calibration.calcCrc_short(hMsg.getcheckcrcdata()) != scrc) {
			/* SLOS_Sleep(1500); */
			 FlushDummyData2Mod();
			   throw new CommunicationException(ERROR.PORT_OP_EORROR, String.valueOf(ret));
		}
 
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carrywavelayout);
		Application app = getApplication();
		myapp = (MyApplication) app;
	 
		button_on = (Button) findViewById(R.id.button_on);
		button_off = (Button) findViewById(R.id.button_off);
		button_off.setEnabled(false);
		 

		button_cwset = (Button) findViewById(R.id.button_cwset);
		button_cwset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				try {
					ant = Integer
							.parseInt(((EditText) findViewById(R.id.editText_cwant))
									.getText().toString());
					power = Integer
							.parseInt(((EditText) findViewById(R.id.editText_cwpow))
									.getText().toString());
					fre = Integer
							.parseInt(((EditText) findViewById(R.id.editText__cwfre))
									.getText().toString());
				} catch (Exception ex) {
					Toast.makeText(SubCarryWaveActivity.this,
							MyApplication.Constr_SetFaill + ex.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

				try {
					R2000_calibration.ENGTest_DATA et = r2000pcmd.new ENGTest_DATA(
							(byte) R2000_calibration.SubCmd.SetTestAntPow
									.Value(), ant, power);
					byte[] senddata = r2000pcmd.GetSendCmd(
							R2000_calibration.R2000cmd.ENGTEST, et.ToByteData());

					MsgObj hMsg = new MsgObj();

					SendandRev(senddata, hMsg);
					if (hMsg.opCode[0] == 0) {
						Toast.makeText(SubCarryWaveActivity.this,
								MyApplication.Constr_SetFaill, Toast.LENGTH_SHORT)
								.show();
					}
				} catch (Exception ex) {
					Toast.makeText(SubCarryWaveActivity.this,
							MyApplication.Constr_SetFaill + ex.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					List<Byte> lb = new ArrayList<Byte>();
					lb.add((byte) 0);
					lb.add((byte) 0);
					lb.add((byte) 0);
					lb.add((byte) 0);

					byte[] bdata = R2000_calibration.intTobytes(fre);
					lb.addAll(R2000_calibration.bytesTolistbytes(bdata));

					byte[] senddata = r2000pcmd.GetSendCmd(
							R2000_calibration.R2000cmd.SetTestFre,
							R2000_calibration.ListBtobytes(lb));
					MsgObj hMsg = new MsgObj();
					SendandRev(senddata, hMsg);
					if (hMsg.opCode[0] == 0) {
						Toast.makeText(SubCarryWaveActivity.this,
								MyApplication.Constr_SetFaill, Toast.LENGTH_SHORT)
								.show();
					}
				} catch (Exception ex) {
					Toast.makeText(SubCarryWaveActivity.this,
							MyApplication.Constr_SetFaill + ex.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

				Toast.makeText(SubCarryWaveActivity.this,
						MyApplication.Constr_SetOk, Toast.LENGTH_SHORT).show();
			}

		});

		button_on.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				try {
					MsgObj hMsg = new MsgObj();
					byte[] senddata = r2000pcmd.GetSendCmd(
							R2000_calibration.R2000cmd.carrier,
							new byte[] { 0x01 });
					SendandRev(senddata, hMsg);
					if (hMsg.opCode[0] == 0) {
						Toast.makeText(SubCarryWaveActivity.this,
								MyApplication.Constr_SetFaill, Toast.LENGTH_SHORT)
								.show();
					}
					button_on.setEnabled(false);
					button_off.setEnabled(true);
				} catch (Exception ex) {
					Toast.makeText(SubCarryWaveActivity.this,
							MyApplication.Constr_SetFaill + ex.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		button_off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					MsgObj hMsg = new MsgObj();
					byte[] senddata = r2000pcmd.GetSendCmd(
							R2000_calibration.R2000cmd.carrier,
							new byte[] { 0x00 });
					SendandRev(senddata, hMsg);
					
					if (hMsg.opCode[0] == 0) {
						Toast.makeText(SubCarryWaveActivity.this,
								MyApplication.Constr_SetFaill, Toast.LENGTH_SHORT)
								.show();
					}
					button_on.setEnabled(true);
					button_off.setEnabled(false);
				} catch (Exception ex) {
					Toast.makeText(SubCarryWaveActivity.this,
							MyApplication.Constr_SetFaill + ex.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}

		});

button_allfreturnloss=(Button) findViewById(R.id.button_api_getRL);
		
		button_allfreturnloss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				try {
					ant = Integer
							.parseInt(((EditText) findViewById(R.id.editText_cwant))
									.getText().toString());
					power = Integer
							.parseInt(((EditText) findViewById(R.id.editText_cwpow))
									.getText().toString());
					 
				} catch (Exception ex) {
					
					Toast.makeText(SubCarryWaveActivity.this,
							MyApplication.Constr_GetFaill + ex.getMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				frequency_Returnloss3();
			}

		});
	}
    
	int[] tablefre;
	int htbindex = 0;
	List<String> Lires = new ArrayList<String>();

	public String PadLeft(String strval, int len, char adtn) {
		return String.format("%" + len + "s" + adtn, strval);
	}

	public String PadRight(String strval, int len, char adtn) {
		return String.format("%-" + len + "s" + adtn, strval);
	}

	public String HexstrToQ724(int val) throws Exception {
		int valuepint = val;
		double value_q714 = valuepint * 1.0
				/ (new BigDecimal(2)).pow(24).intValue();// Math.Pow(2, 24);

		String q714 = String.valueOf(value_q714);
		if (value_q714 > 0)
			return String.valueOf(String.valueOf(q714).substring(0, 6));
		else
			return q714.length() >= 7 ? String.valueOf(q714).substring(0, 7)
					: q714;
	}

	public boolean CheckDatarule(String data, int numk) {
		boolean returnvalue = true;
		if (data.isEmpty() || data == null) {
			return false;
		}
		if (numk == 2) {
			for (int i = 0; i < data.length(); i++) {
				char single = data.charAt(i);
				if (single != '0' && single != '1')
					return false;
			}
		} else if (numk == 10) {

			for (int i = 0; i < data.length(); i++) {
				char single = data.charAt(i);
				switch (single) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					break;
				default: {
					returnvalue = false;
					break;
				}

				}
				if (!returnvalue) {
					break;
				}
			}
		} else {
			for (int i = 0; i < data.length(); i++) {
				char single = data.charAt(i);
				// String temp=String.valueOf(single).toUpperCase();
				switch (single) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F':
					break;
				default: {
					returnvalue = false;
					break;
				}
				}
				if (!returnvalue) {
					break;
				}
			}
		}

		if (data.substring(0, 1) == "."
				|| data.substring(data.length() - 1, data.length()) == ".")
			returnvalue = false;
		return returnvalue;
	}

	public void setantpow(int ant, int pow) throws CommunicationException {

		R2000_calibration.ENGTest_DATA et = new R2000_calibration().new ENGTest_DATA(
				(byte) (R2000_calibration.SubCmd.SetTestAntPow.Value()), ant,
				pow);
		byte[] senddata = r2000pcmd.GetSendCmd(
				R2000_calibration.R2000cmd.ENGTEST, et.ToByteData());

		int re = 0;
		MsgObj hMsg = new MsgObj();
		SendandRev(senddata, hMsg);

	}

	private void CW_On() {
		try {

			byte[] senddata = r2000pcmd.GetSendCmd(
					R2000_calibration.R2000cmd.carrier, new byte[] { 0x01 });
			int re = 0;
			MsgObj hMsg = new MsgObj();
			SendandRev(senddata, hMsg);

		} catch (Exception ex) {
			Toast.makeText(SubCarryWaveActivity.this,
					MyApplication.Constr_SetFaill + ex.getMessage(),
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	private void CW_Off() {
		try {
			byte[] senddata = r2000pcmd.GetSendCmd(
					R2000_calibration.R2000cmd.carrier, new byte[] { 0x00 });

			int re = 0;
			MsgObj hMsg = new MsgObj();
			SendandRev(senddata, hMsg);
		} catch (Exception ex) {
			Toast.makeText(SubCarryWaveActivity.this,
					MyApplication.Constr_SetFaill + ex.getMessage(),
					Toast.LENGTH_SHORT).show();
			return;
		}

	}

	public void setpll(int fre) throws Exception {
		if (fre == 0) {
			throw new Exception("no frequency");
		}

		if (fre < 840000 || fre > 960000)
			throw new Exception("840000-960000");

		List<Byte> lb = new ArrayList<Byte>();
		lb.add((byte) 0);
		lb.add((byte) 0);
		lb.add((byte) 0);
		lb.add((byte) 0);

		byte[] bdata = new byte[4];
		bdata[0] = (byte) ((fre & 0xff000000) >> 24);
		bdata[1] = (byte) ((fre & 0x00ff0000) >> 16);
		bdata[2] = (byte) ((fre & 0x0000ff00) >> 8);
		bdata[3] = (byte) (fre & 0x000000ff);

		for (int i = 0; i < 4; i++)
			lb.add(bdata[i]);

		Byte[] by = new Byte[lb.size()];
		byte[] by2 = new byte[lb.size()];
		lb.toArray(by);
		for (int i = 0; i < by2.length; i++)
			by2[i] = by[i];

		byte[] senddata = r2000pcmd.GetSendCmd(
				R2000_calibration.R2000cmd.SetTestFre, by2);

		int re = 0;
		MsgObj hMsg = new MsgObj();
		SendandRev(senddata, hMsg);
	}

	private void frequency_Returnloss3() {
		try{
						Region.RegionE rre = (RegionE) myapp.Mreader
					.paramGet(ParamNames.Reader_Region_Id);
		 
			com.silionmodule.R2000_calibration.Region r2reg = null;
			if(rre==Region.RegionE.NA)
	        	r2reg=com.silionmodule.R2000_calibration.Region.NA;
			else if(rre==Region.RegionE.China)
	        	r2reg=com.silionmodule.R2000_calibration.Region.PRC;
	        else  if(rre==Region.RegionE.China2)
	        	r2reg=com.silionmodule.R2000_calibration.Region.PRC2;
	        else  if(rre==Region.RegionE.Europe1)
	        	r2reg=com.silionmodule.R2000_calibration.Region.EU;
	        else  if(rre==Region.RegionE.Europe2)
	        	r2reg=com.silionmodule.R2000_calibration.Region.EU2;
	        else  if(rre==Region.RegionE.Europe3)
	        	r2reg=com.silionmodule.R2000_calibration.Region.EU3;
	        else  if(rre==Region.RegionE.IN)
	        	r2reg=com.silionmodule.R2000_calibration.Region.IN;
	        else  if(rre==Region.RegionE.Korea)
	        	r2reg=com.silionmodule.R2000_calibration.Region.KR;
	        else  if(rre==Region.RegionE.OPEN)
	        	r2reg=com.silionmodule.R2000_calibration.Region.OPEN;
	       
	        	
			VSWRReturnloss_DATA apvr=new com.silionmodule.R2000_calibration().new VSWRReturnloss_DATA
					(Sub4TabActivity.nowpower,new int[]{},new int[]{ant},r2reg);
		 
			byte[] senddata = r2000pcmd.GetSendCmd(
					R2000_calibration.R2000cmd.ReturnLossTest, apvr.ToByteData());
			 
			MsgObj hMsg = new MsgObj();
			 SendandRev(senddata, hMsg);
			
			if (hMsg.status[0] + hMsg.status[1] != 0) {
				throw new ReaderException( Functional.bytes_Hexstr(hMsg.status));
			}
			if (hMsg.opCode[0] == 0) {
				Toast.makeText(SubCarryWaveActivity.this,
						MyApplication.Constr_GetFaill, Toast.LENGTH_SHORT).show();
			}

			byte[] data = new byte[hMsg.dataLen[0]&0xff - 12];
			System.arraycopy(hMsg.data, 12, data, 0, data.length);
			R2000_calibration.VSWRReturnloss_DATA et2 = new R2000_calibration().new VSWRReturnloss_DATA(
					data);

		String msg="频率              RL(dB)          VSWR          天线\r\n";
		TextView tv=(TextView)this.findViewById(R.id.textView_frelostlist);
		tablefre = (int[]) myapp.Mreader
				.paramGet(ParamNames.Reader_Region_HopTable);
		tablefre = Sub4TabActivity.Sort(tablefre);

		  
			for(int j=0;j<tablefre.length;j++)
			{	
				 float ft1=0;
			     float ft2=et2.LfVSWR().get(j);
			     msg+=String.valueOf(tablefre[j])+"             -"+String.valueOf(ft1)+"          "+
			     String.valueOf(ft2)+"               "+String.valueOf(ant)+"\r\n";
			     
			     String[] ss=new String[4];
			     ss[0]=String.valueOf(tablefre[j]);
			     ss[1]=String.valueOf(ft1);
			     ss[2]=String.valueOf(ft2);
			     ss[3]=String.valueOf(ant);
			}
		tv.setText(msg);
 
		}catch (Exception ex) {
			Toast.makeText(SubCarryWaveActivity.this,
					MyApplication.Constr_GetFaill + ex.getMessage(),
					Toast.LENGTH_SHORT).show();
			return;
		}
	}
}
