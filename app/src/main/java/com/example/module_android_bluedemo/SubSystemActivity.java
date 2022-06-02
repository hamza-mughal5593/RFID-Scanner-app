package com.example.module_android_bluedemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.silionmodule.Functional;
import com.silionmodule.TAGINFO;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubSystemActivity extends Activity {
	
	Button button_setblueframe,button_path,button_out;
	static EditText et_path;
	EditText et_fc,et_ft,et_swt;
	MyApplication myapp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemlayout);
		
		Application app=getApplication();
		myapp=(MyApplication) app;  
		
		et_path=(EditText) findViewById(R.id.editText_path);
		 et_fc=(EditText)findViewById(R.id.editText_framecount);
		 et_ft=(EditText)findViewById(R.id.editText_frametime);
		 et_swt=(EditText)findViewById(R.id.editText_frameswtime);
		 et_fc.setText(String.valueOf(myapp.framecount));
		 et_ft.setText(String.valueOf(myapp.frametime));
		 et_swt.setText(String.valueOf(myapp.frameswtime));
		button_path=(Button) findViewById(R.id.button_locate);
		button_out=(Button) findViewById(R.id.button_outfile);
		button_setblueframe=(Button) findViewById(R.id.button_frameset);
		button_path.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SubSystemActivity.this, SubPathActivity.class); 
				startActivityForResult(intent, 0); 
			}
			
		});
		button_setblueframe.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int fcount=Integer.parseInt(et_fc.getText().toString());
				int ftime=Integer.parseInt(et_ft.getText().toString());
				int fswtime=Integer.parseInt(et_swt.getText().toString());
				myapp.CommBth.SetFrameParams(fcount, ftime,fswtime);
				myapp.framecount=fcount;
				myapp.frametime=ftime;
				myapp.frameswtime=fswtime;
			}
			
		});
		button_out.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!et_path.getText().toString().contains("txt")
						&& !et_path.getText().toString().contains("csv")) {
					Toast.makeText(SubSystemActivity.this,
							" ‰»ÎtxtªÚ’ﬂcsv", Toast.LENGTH_SHORT)
							.show();
				}
				File file = new File(et_path.getText().toString());
				FileOutputStream fs=null;
				try {
					 
					 fs = new FileOutputStream(file);
					String wstr = "EPC,Count,\r\n";
					fs.write(wstr.getBytes());

					Iterator<Entry<String, TAGINFO>> iesb;
					iesb = myapp.TagsMap.entrySet().iterator();

					while (iesb.hasNext()) {
						TAGINFO bd = iesb.next().getValue();
						String linestr = Functional.bytes_Hexstr(bd.EpcId) + ",";
						linestr += String.valueOf(bd.ReadCnt) + ",";
						linestr += "\r\n";
						fs.write(linestr.getBytes());
					}

					Toast.makeText(SubSystemActivity.this,
							"save ok",
							Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                finally
                {
                	try {
                		if(fs!=null)
    					fs.close();

    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                }
				 
			}
			
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		 
	}
}
