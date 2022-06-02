package com.example.module_android_bluedemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bth.api.cls.Comm_Bluetooth;
import com.communication.inf.CommunicationException;
import com.function.SPconfig;
import com.tool.log.LogD;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SubDebugActivity extends Activity {

	MyApplication myapp;
	Button button_clear, button_send1, button_send2, button_send3, button_revd,
			button_app;
	CheckBox checkhex1, checkhex2, checkhex3, checkrhex;
	private Handler handler = new Handler();
	private Handler handler2 = new Handler();
	private boolean isrun2 = false;
    long costime;
	TextView tv_send, tv_revd,tv_cost;

	private Runnable runnable2 = new Runnable() {
		public void run() {

			// check connect
			TextView et1 = (TextView) findViewById(R.id.textView_state);
			int bl = myapp.CommBth.ConnectState();
			if (bl == Comm_Bluetooth.DISCONNECTED) {
				et1.setText(MyApplication.Constr_subdbdisconnreconn);
				if (isrun2)
					myapp.CommBth.Connect(myapp.CommBth.GetConnectAddr(),
							myapp.CommBth.getRemoveType());

				handler2.postDelayed(this, 1000);
			} else if (bl == Comm_Bluetooth.CONNECTED) {
				if (et1.getText().toString().equals(MyApplication.Constr_subdbdisconnreconn))
					et1.setText(MyApplication.Constr_hadconnected);
			} else if (bl == Comm_Bluetooth.CONNECTING) {
				et1.setText(MyApplication.Constr_subdbconnecting);
			}
			handler2.postDelayed(this, 1000);
			return;
		}
	};

	private Runnable runnable = new Runnable() {
		public void run() {

			// while(isrun)
			// {
			byte[] revdata = new byte[256];
			int offset = 0;

			int re=0;
			try {
				 
				re = myapp.CommBth.Comm_Read(revdata, offset, 0);
			 
			} catch (CommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (re > 0) {
				TextView et2 = (TextView) findViewById(R.id.textView_revddata);
				String btext = et2.getText().toString();
				String rdata = "";
				if (checkrhex.isChecked())
					rdata = Comm_Bluetooth.bytes_Hexstr(revdata, re);
				else
					rdata = String.valueOf(revdata);
				et2.setText(btext + rdata + "\n");
				
				int ct=(int) (System.currentTimeMillis()-costime);
				tv_cost.setText(String.valueOf(ct));
			}

			showrevdc(re);
			// }
			handler.postDelayed(this, 0);
			return;
		}
	};

	private void showsendc(int l) {
		int c = Integer.parseInt(tv_send.getText().toString());
		c += l;
		tv_send.setText(String.valueOf(c));
	}

	private void showrevdc(int l) {
		int c = Integer.parseInt(tv_revd.getText().toString());
		c += l;
		tv_revd.setText(String.valueOf(c));
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1_tablelayout_debug);

		Application app = getApplication();
		myapp = (MyApplication) app;

		myapp.spf = new SPconfig(this);

		button_clear = (Button) this.findViewById(R.id.button_clear);
		button_send1 = (Button) this.findViewById(R.id.button_send1);
		button_send2 = (Button) this.findViewById(R.id.button_send2);
		button_send3 = (Button) this.findViewById(R.id.button_send3);
		button_revd = (Button) this.findViewById(R.id.button_revd);

		checkrhex = (CheckBox) this.findViewById(R.id.checkBox_rhex);
		checkhex1 = (CheckBox) this.findViewById(R.id.checkBox_hex1);
		checkhex2 = (CheckBox) this.findViewById(R.id.checkBox_hex2);
		checkhex3 = (CheckBox) this.findViewById(R.id.checkBox_hex3);
		TextView et3 = (TextView) findViewById(R.id.textView_revddata);
		et3.setText("");

		tv_send = (TextView) findViewById(R.id.textView_debug_sendcal);
		tv_revd = (TextView) findViewById(R.id.textView_debug_revdcal);
		tv_cost= (TextView) findViewById(R.id.textView_costtime);
		// revThread=new Thread(runnable);
		isrun2 = true;
		handler2.postDelayed(runnable2, 0);
		button_revd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String txt = button_revd.getText().toString().trim();
				if (txt.equals(MyApplication.Constr_subdbrev)) {
					handler.postDelayed(runnable, 0);
					// isrun=true;
					// revThread.start();
					button_revd.setText(MyApplication.Constr_subdbstop);
				} else {
					handler.removeCallbacks(runnable);
					button_revd.setText(MyApplication.Constr_subdbrev);
				}
			}
		}

		);

		button_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TextView et2 = (TextView) findViewById(R.id.textView_revddata);
				et2.setText("");
				tv_cost.setText("0");
				tv_send.setText("0");
				tv_revd.setText("0");
				
			}
		}

		);
		button_send1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				byte[] buffer = null;
				if (checkhex1.isChecked()) {
					// CommBth.
					EditText ets1 = (EditText) findViewById(R.id.editText_s1);
					String data = ets1.getText().toString();
					buffer = new byte[data.length() / 2];
					int re = Comm_Bluetooth.Str2Hex(data, data.length(), buffer);
					if (re == -1)
						Toast.makeText(SubDebugActivity.this, MyApplication.Constr_subdbdalennot,
								Toast.LENGTH_SHORT).show();
					if (re == 1)
						Toast.makeText(SubDebugActivity.this, MyApplication.Constr_subdbplpuhexchar,
								Toast.LENGTH_SHORT).show();

				} else {
					EditText ets1 = (EditText) findViewById(R.id.editText_s1);
					String data = ets1.getText().toString();
					buffer = data.getBytes();

				}

				try {
					costime=System.currentTimeMillis();
					if (buffer != null)
						myapp.CommBth.Comm_Write(buffer, 0, buffer.length);
					LogD.LOGD("send1 cost:"+String.valueOf(System.currentTimeMillis()-costime));
					showsendc(buffer.length);

				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		);
		button_send2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				byte[] buffer = null;
				if (checkhex2.isChecked()) {
					// CommBth.
					EditText ets1 = (EditText) findViewById(R.id.editText_s2);
					String data = ets1.getText().toString();
					buffer = new byte[data.length() / 2];
					int re = Comm_Bluetooth.Str2Hex(data, data.length(), buffer);
					if (re == -1)
						Toast.makeText(SubDebugActivity.this, MyApplication.Constr_subdbdalennot,
								Toast.LENGTH_SHORT).show();
					if (re == 1)
						Toast.makeText(SubDebugActivity.this, MyApplication.Constr_subdbplpuhexchar,
								Toast.LENGTH_SHORT).show();
				} else {
					EditText ets1 = (EditText) findViewById(R.id.editText_s2);
					String data = ets1.getText().toString();
					buffer = data.getBytes();

				}

				try {
					costime=System.currentTimeMillis();
					if (buffer != null)
						myapp.CommBth.Comm_Write(buffer, 0, buffer.length);
					LogD.LOGD("send2 cost:"+String.valueOf(System.currentTimeMillis()-costime));
					showsendc(buffer.length);
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		);
		button_send3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				byte[] buffer = null;
				List<byte[]> lbuffer=new ArrayList<byte[]>();
				if (checkhex3.isChecked()) {
					// CommBth.
					EditText ets1 = (EditText) findViewById(R.id.editText_s3);
					String[] data = ets1.getText().toString().split(" ");
					for(int i=0;i<data.length;i++)
					{
						buffer = new byte[data[i].length() / 2];
					    int re = Comm_Bluetooth.Str2Hex(data[i], data[i].length(), buffer);
					    if (re == -1)
						Toast.makeText(SubDebugActivity.this, MyApplication.Constr_subdbdalennot,
								Toast.LENGTH_SHORT).show();
					    if (re == 1)
						Toast.makeText(SubDebugActivity.this, MyApplication.Constr_subdbplpuhexchar,
								Toast.LENGTH_SHORT).show();
					    lbuffer.add(buffer);
					}
				} else {
					EditText ets1 = (EditText) findViewById(R.id.editText_s3);
					String[] data = ets1.getText().toString().split(" ");
					for(int i=0;i<data.length;i++)
					{
						buffer = data[i].getBytes();
						lbuffer.add(buffer);
					}
				}

				
				costime=System.currentTimeMillis();

				byte[] revdata = new byte[256];
				int offset = 0;

				int re=0,all=0;
				try {
 
					for(int i=0;i<lbuffer.size();i++)
					{
						buffer=lbuffer.get(i);
						re=0;all=0;
						if (buffer == null||buffer.length==0)
							continue;
						
						Arrays.fill(revdata, re, 256,(byte) 0);
					 myapp.CommBth.Comm_Write(buffer, 0, buffer.length);
						
					re = myapp.CommBth.Comm_Read(revdata, offset, 0);
					all+=re;

					if((revdata[0]&0xff)==0xff&&revdata[1]+7>re)
					{	re=myapp.CommBth.Comm_Read(revdata, re, revdata[1]+7-re);
						all+=re;
					}
					if (all > 0) {
						TextView et2 = (TextView) findViewById(R.id.textView_revddata);
						String btext = et2.getText().toString();
						String rdata = "";
						if (checkrhex.isChecked())
							rdata = Comm_Bluetooth.bytes_Hexstr(revdata, all);
						else
							rdata = String.valueOf(revdata);
						et2.setText(btext + rdata + "\n");

					}
					}
					int ct=(int) (System.currentTimeMillis()-costime);
					tv_cost.setText(String.valueOf(ct));
					showrevdc(re);
					
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//LogD.LOGD("send3 cost:"+String.valueOf(System.currentTimeMillis()-costime));
				showsendc(buffer.length);
			}
		}

		);

		TextView et1 = (TextView) findViewById(R.id.textView_blueset);
		et1.setText(myapp.CommBth.GetConnectAddr());
		TextView et2 = (TextView) findViewById(R.id.textView_state);
		et2.setText(MyApplication.Constr_hadconnected);

	}

	protected void onStop() {
		isrun2 = false;
		handler2.removeCallbacks(runnable2);
		if (runnable != null && handler != null) {
			handler.removeCallbacks(runnable);
		}
		super.onStop();
	}

}
