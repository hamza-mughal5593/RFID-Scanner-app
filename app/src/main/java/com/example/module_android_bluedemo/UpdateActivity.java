package com.example.module_android_bluedemo;

import java.util.HashMap;
import java.util.Map;

import com.function.CallbackBundle;
import com.silionmodule.ReaderException;
import com.silionmodule.StatusEventListener;
import com.tool.log.LogD;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;

public class UpdateActivity extends Activity {
	
	private MyApplication myapp;
	Button button_selectfile,button_stupdate;
	Button button_setblueframe;
	EditText et_fc,et_ft,et_swt;
	 static private int openfileDialogId = 0; 
	 private final int HandlerorPlatBoard=0;//0手机1平板
	 ProgressBar prb;
	 String filename;
	 public Message amsg;
	 private Thread runthread;
	 Dialog dialog;
	 
	 Runnable Run=new Runnable(){
       
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				myapp.Mreader.FirmwareLoad(filename);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				    Message msg = new Message(); 
      		    
	                msg.what = 1;  
	                Bundle bundle = new Bundle();  
	                if(e.getClass().getName().contains("ReaderException"))
	                	 bundle.putCharSequence("ps",((ReaderException)e).GetMessage());
	                else
	                bundle.putCharSequence("ps", e.toString());
	               
	                msg.setData(bundle);  
				    handler.sendMessage(msg);
			}
		}
		 
	 };
	 
	 public  Handler handler = new Handler()  
	    {  
			
	        public void handleMessage(Message msg)  
	        {  
	            switch (msg.what)  
	            {  
	            case 0:  
	                {  
	                    //取出参数更新控件  
	                	Bundle bd=msg.getData();
	                	float v=bd.getFloat("ps");
	                	float vps=v*100;
	            		TextView et= (TextView)findViewById(R.id.textView_ps);
	            		String vstr=String.valueOf(vps);
	            		vstr=vstr.substring(0,vstr.indexOf(".")+2)+"%";
	            		et.setText(vstr);
	            		et.refreshDrawableState();
	            		prb.setProgress((int)(v*100));
	                }  
	                break;  
	            case 1:  
	            {
	            	Bundle bd=msg.getData();
                	String v=(String) bd.getCharSequence("ps");
                	TextView et= (TextView)findViewById(R.id.textView_ps);
            		 
            		et.setText(v);
            		prb.setProgress(0);
	            }
	            break;
	            case 2:
	            {
	            	Bundle bd=msg.getData();
                	String v=(String) bd.getCharSequence("ps");
					Toast.makeText(UpdateActivity.this, v,
							Toast.LENGTH_SHORT).show();
	            }
	            default:  
	                break;  
	            }  
	            super.handleMessage(msg);  
	        }  
	          
	    };  
	      
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);

		myapp=(MyApplication) getApplication(); 
		
		 et_fc=(EditText)findViewById(R.id.udeditText_framecount);
		 et_ft=(EditText)findViewById(R.id.udeditText_frametime);
		 et_swt=(EditText)findViewById(R.id.udeditText_frameswtime);
		 et_fc.setText(String.valueOf(myapp.framecount));
		 et_ft.setText(String.valueOf(myapp.frametime));
		 et_swt.setText(String.valueOf(myapp.frameswtime));
		button_setblueframe=(Button) findViewById(R.id.udbutton_frameset);
		button_setblueframe.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int fcount=Integer.parseInt(et_fc.getText().toString());
				int ftime=Integer.parseInt(et_ft.getText().toString());
				int fswtime=Integer.parseInt(et_swt.getText().toString());
				myapp.CommBth.SetFrameParams(fcount, ftime,fswtime);
				
			}
			
		});
		button_selectfile=(Button)this.findViewById(R.id.button_selectfile);
		button_selectfile.setOnClickListener(new View.OnClickListener(){

			@SuppressWarnings("deprecation")
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(openfileDialogId);  
				
			}
 	 
		});
		button_stupdate=(Button)this.findViewById(R.id.button_startupdate);
		button_stupdate.setOnClickListener(new View.OnClickListener() {
		 
			@Override
			public void onClick(View arg0) {
				filename="";
				// TODO Auto-generated method stub
				TextView et=(TextView)findViewById(R.id.textView_path);
				 filename=(String) et.getText();
				if(filename.isEmpty()||filename.endsWith("path"))
				{
					 Toast.makeText(UpdateActivity.this, "选择升级文件/*.bin",
								Toast.LENGTH_SHORT).show();
					 return ;
				}
				//for xb update
				//myapp.CommBth.SetFrameParams(20, 100, 10);
                //for honeywell
				//myapp.CommBth.SetFrameParams(160, 100, 0);
				try {
					//myapp.Breader.FirmwareLoad(filename);
					runthread=new Thread(Run);
					runthread.start();
	                 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(UpdateActivity.this, "升级失败",
							Toast.LENGTH_SHORT).show();
				 return ;
				}
				
				Toast.makeText(UpdateActivity.this, "升级开始",
						Toast.LENGTH_SHORT).show();
			}
		});
		
		prb=(ProgressBar) findViewById(R.id.progressBar1);
 
		 
		//注册监听器  
	 
		myapp.Mreader.Eobject().addStatusEventListener(new StatusEventListener(){ 
           
			@Override
			public void StatusCatch(Object e) {
				// TODO Auto-generated method stub
				if(e.getClass()==Float.class)
            	{
            		    Message msg = new Message(); 
		                msg.what = 0;  
		                Bundle bundle = new Bundle();  
		                bundle.putFloat("ps", Float.parseFloat(e.toString()));
		               
		                msg.setData(bundle);  
		                //发送消息到Handler  
		                handler.sendMessage(msg);
		                
            	}
            	else if(e.getClass()==String.class)
            	{
            		if(e.toString().contains("Update finish"))
            		{
						//for xb update
						//myapp.CommBth.SetFrameParams(20, 100, 0);

						Message msg = new Message();
     	                msg.what = 2;  
     	                Bundle bundle = new Bundle();  
     	                bundle.putCharSequence("ps","升级完毕，请重启读写器");
     	               
     	                msg.setData(bundle);  
     				    handler.sendMessage(msg);
            		}
            	}
			}
		});
		 
		//dialog2();
		//dialog1();
		/*
		int permissionCheck1 = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
		int permissionCheck2 = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
							Manifest.permission.WRITE_EXTERNAL_STORAGE},
					124);
		}
		*/
	}
	
	protected void dialog1() {
		  AlertDialog.Builder builder = new Builder(UpdateActivity.this);
		  builder.setMessage("请确认蓝牙读写器是否处于刚上电配置状态？");

		  builder.setTitle("提示");

		  builder.setPositiveButton("否", new OnClickListener() {

		   @Override
		   public void onClick(android.content.DialogInterface dialog, int which) {
		    dialog.dismiss();

		    UpdateActivity.this.finish();
		   }
 
		  });

		  builder.setNegativeButton("是", new OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
		 }

	    protected void dialog2() {
		  AlertDialog.Builder builder = new Builder(UpdateActivity.this);
		  builder.setMessage("是否清除掉原先对蓝牙读写器的配置？");

		  builder.setTitle("提示");

		  builder.setPositiveButton("否", new OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    
		   boolean bl = false;
		  try {
			bl = myapp.Mreader.ResetSettings(false);
			 
		   } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
		   if(bl)
		   {
			   Toast.makeText(UpdateActivity.this, "操作成功",
						Toast.LENGTH_SHORT).show();
		   }
		   else
		   {
			   Toast.makeText(UpdateActivity.this, "操作失败",
						Toast.LENGTH_SHORT).show();
		   }
		   }
		    

		  });

		  builder.setNegativeButton("是", new OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    
		    boolean  bl = myapp.Mreader.ResetSettings(true);
		   if(bl)
		    {
			   Toast.makeText(UpdateActivity.this, "操作成功",
						Toast.LENGTH_SHORT).show();
		   }
		   else
		   {
			   Toast.makeText(UpdateActivity.this, "操作失败",
						Toast.LENGTH_SHORT).show();
		   }
		   }
		  });

		  builder.create().show();
		 }
	    
	   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onDestroy()
	{
		
		if(myapp.Mreader!=null)
		{
			myapp.CommBth.SetFrameParams(myapp.framecount, 
					myapp.frametime, myapp.frameswtime);
			myapp.Mreader.DisConnect();
		}
	    setResult(RESULT_OK);
		super.onDestroy();
	}
	
	@Override  
	   protected Dialog onCreateDialog(int id) {  
	        if(id==openfileDialogId){  
	            Map<String, Integer> images = new HashMap<String, Integer>();  
	           // 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹   
	           images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);   // 根目录图标   
               images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);    //返回上一层的图标   
	           images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);   //文件夹图标   
	           images.put("bin", R.drawable.filedialog_file);  
	          //images.put("wav", R.drawable.filedialog_wavfile);   //wav文件图标   //wav文件图标   
	            images.put(OpenFileDialog.sEmpty, R.drawable.filedialog_root);  
	           dialog = OpenFileDialog.createDialog(id, this, "打开文件", new CallbackBundle() {  
	                @Override  
	              public void callback(Bundle bundle) {  
	                  String filepath = bundle.getString("path");  
	                 // setTitle(filepath); // 把文件路径显示在标题上   
	                  TextView et=(TextView)findViewById(R.id.textView_path);
	  				  et.setText(filepath);
	                }  
                },   
	           ".bin;",  
	          images);  
	           dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
             	   @Override
             	   public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
             	   {
             	   if (keyCode == KeyEvent.KEYCODE_BACK)
             	    {
             		    removeDialog(openfileDialogId);
             	      
             	    }
             	    return true;
             	   }
             	  });
	        return dialog;  
	      }  
	     return null;  
	   }  
	
}
