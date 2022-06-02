package com.example.module_android_bluedemo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.silionmodule.R2000_calibration;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SubRegopActivity extends Activity {
	MyApplication myapp;
	Spinner spinner_regtype;
	ArrayAdapter<String> arradp_regtype;
	Button button_regr, button_regw;
	ListView elist;
	EditText et_regadr,et_regc,et_regwadr,et_regwv;
	R2000_calibration r2000pcmd = new R2000_calibration();
	
	public String bytes_Hexstr(byte[] bArray, int length) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerop);
		Application app = getApplication();
		myapp = (MyApplication) app;
		
		spinner_regtype = (Spinner) findViewById(R.id.spinner_regtype);
		arradp_regtype = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MyApplication.regtype);
		spinner_regtype.setAdapter(arradp_regtype);
		
		button_regr = (Button) findViewById(R.id.button_regread);
		button_regw = (Button) findViewById(R.id.button_regwrite);
		elist = (ListView) this.findViewById(R.id.listView_reglist);
		
		et_regadr=(EditText)findViewById(R.id.editText_regadr);
		et_regc=(EditText)findViewById(R.id.editText_regrc);
		et_regwadr=(EditText)findViewById(R.id.editText_regwadr);
		et_regwv=(EditText)findViewById(R.id.editText_regwv);
		
		button_regr.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (spinner_regtype.getSelectedItemPosition() == -1)
	            {
	                Toast.makeText(SubRegopActivity.this, "��ѡ������", Toast.LENGTH_SHORT)
    				.show();
	                return;
	            }

	            if (et_regadr.getText().toString().isEmpty())
	            {
	                Toast.makeText(SubRegopActivity.this, "���������ַ", Toast.LENGTH_SHORT)
    				.show();
	                return;
	            }

	            int count;
	            short addr;
	            

	            try
	            {
	                //Convert.ToInt32(hex, 16);
	                //addr == Convert.ToUInt16(raddresstextBox.Text);
	                if (et_regadr.getText().toString().length() > 4)
	                    throw new Exception("��ַ���");

	                addr = Short.parseShort(et_regadr.getText().toString(),16);
	                		 
	                count = Integer.parseInt(et_regc.getText().toString());
	                
	            }
	            catch (Exception ex)
	            {
	                Toast.makeText(SubRegopActivity.this, "����ȷ�������Ͷ���ַ:" + ex.getMessage(), Toast.LENGTH_SHORT)
    				.show();
	                return;
	            }
 
	            R2000_calibration.R2000cmd rcmdo=null;

	            R2000_calibration.OEM_DATA r2000oem = null;
	            R2000_calibration.MAC_DATA r2000mac = null;

	            if (spinner_regtype.getSelectedItemPosition() == 0)
	            {
	                rcmdo = R2000_calibration.R2000cmd.OEMread;
	                r2000oem = r2000pcmd.new OEM_DATA(addr);
	                for (int i = 1; i < count; i++)
	                {
	                    r2000oem.AddTo(++addr, 0);
	                }
	            }
	            else if (spinner_regtype.getSelectedItemPosition() == 1)
	            {
	                rcmdo = R2000_calibration.R2000cmd.readMAC; r2000mac =r2000pcmd.new MAC_DATA(addr);
	                for (int i = 1; i < count; i++)
	                {
	                    r2000mac.AddTo(++addr, 0);
	                }
	            }
	            
	            
	            List<Map<String, Object>> list = new  ArrayList<Map<String, Object>>();
	            Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("number","number");
				map1.put("address", "address");
				map1.put("value",  "value");
				list.add(map1);
	            if (spinner_regtype.getSelectedItemPosition() != 2)
	            {

	                try
	                {
	                    byte[] senddata = null;

	                    if (spinner_regtype.getSelectedItemPosition() == 0)
	                        senddata = r2000pcmd.GetSendCmd(rcmdo, r2000oem.ToByteData());
	                    else if (spinner_regtype.getSelectedItemPosition() == 1)
	                        senddata = r2000pcmd.GetSendCmd(rcmdo, r2000mac.ToByteData());
 
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
	    						 Toast.makeText(SubRegopActivity.this, "�շ�ָ��ʧ��"+
	    								 String.format("%02x", part2[0])+String.format("%02x", part2[1]),
	    								 Toast.LENGTH_SHORT)
		    	    				.show();
		    		                return;
	    					}
	    				}
	    				else
	    				 {
	    		                Toast.makeText(SubRegopActivity.this, "�շ�ָ��ʧ��", Toast.LENGTH_SHORT)
	    	    				.show();
	    		                return;
	    		            }
	    			 
	                    byte[] data = new byte[revdata.length - 19];
	                    System.arraycopy(revdata, 17, data, 0, data.length);
	                    R2000_calibration.OEM_DATA r2000data = r2000pcmd.new OEM_DATA(data);

	                    R2000_calibration.OEM_DATA.Adpair[] adp = r2000data.GetAddr();
	                    for (int i = 0; i < adp.length; i++)
	                    {
	                        Map<String, Object> map = new HashMap<String, Object>();
	        				map.put("number",String.valueOf(i+1));
	        				map.put("address", String.format("%04x", adp[i].addr));
	        				map.put("value",  String.format("%04x", adp[i].val));
	        				list.add(map);
	                    }
	                     
	                }
	                catch (Exception ex)
	                {
	                	  Toast.makeText(SubRegopActivity.this, "����ʧ��:" + ex.getMessage(), Toast.LENGTH_SHORT)
		    				.show();
	                    
	                    return;
	                }
	            }
	            else
	            {
	                try
	                {
	                    for (int i = 0; i < count;i++ )
	                    {
	                        short val = (short)(addr+i);
	                        byte[] data = new byte[3];
	                        data[0] = 0x07;
	                        data[1] = (byte)((val & 0xff00) >> 8);
	                        data[2] = (byte)(val & 0x00ff);
	                        byte[] senddata = r2000pcmd.GetSendCmd(R2000_calibration.R2000cmd.Regop, data);
	                        
	                        myapp.CommBth.Comm_Write(senddata,0, senddata.length);
		                    
		    				byte[] part1 = new byte[3];
		    				byte[] part2=null;
		    				byte[] revdata=null;
		    				int re = myapp.CommBth.Comm_Read(part1,0, part1.length);

		    				if (re == 0 && part1[2] != 0x00) {
		    					int l = (part1[1] & 0xff) + 4;
		    					part2 = new byte[l];
		    					revdata= new byte[l+3];
		    					System.arraycopy(part1, 0, revdata, 0, 3);
		    					
		    					re = myapp.CommBth.Comm_Read(part2,0, part2.length);
		    					 
		    					if (re == 0&&((part2[0]+part2[1])==0))
		    						System.arraycopy(part2, 0, revdata, 3, part2.length);
		    					else
		    					{
		    						 Toast.makeText(SubRegopActivity.this, "�շ�ָ��ʧ��"+
		    								 String.format("%02x", part2[0])+String.format("%02x", part2[1]),
		    								 Toast.LENGTH_SHORT)
			    	    				.show();
			    		                return;
		    					}
		    					
		    					 Map<String, Object> map = new HashMap<String, Object>();
			        				map.put("number",String.valueOf(i+1));
			        				map.put("address", String.format("%04x", addr+i));
			        				map.put("value",  String.format("%02x", revdata[revdata.length - 4])+String.format("%02x", revdata[revdata.length - 3]));
			        				list.add(map);
		    				}
		    				else
		    				 {
		    		                Toast.makeText(SubRegopActivity.this, "�շ�ָ��ʧ��", Toast.LENGTH_SHORT)
		    	    				.show();
		    		                return;
		    		          }
	                    }
	                }
	                catch (Exception ex)
	                {
	                    Toast.makeText(SubRegopActivity.this, "����ʧ��:" + ex.getMessage(), Toast.LENGTH_SHORT)
	    				.show();
	                    return;
	                }
	 
	            }

	            Toast.makeText(SubRegopActivity.this, "�����ɹ�", Toast.LENGTH_SHORT)
				.show();
	          
				SimpleAdapter adapter = new SimpleAdapter(SubRegopActivity.this,list,
						R.layout.listitemview_reg,
						new String[] { "number", "address", "value" }, new int[] {
								R.id.textView_regnumber, R.id.textView_regaddress,
								R.id.textView_regval });
				elist.setAdapter(adapter);
			}
			
		});
		
		button_regw.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (spinner_regtype.getSelectedItemPosition() == -1)
	            {
	                Toast.makeText(SubRegopActivity.this, "��ѡ������", Toast.LENGTH_SHORT)
    				.show();
	                return;
	            }

	            if (et_regwadr.getText().toString().isEmpty())
	            {
	                Toast.makeText(SubRegopActivity.this, "������д��ַ", Toast.LENGTH_SHORT)
    				.show();
	                return;
	            }


		            int val=0;
		            short addr;
		            long val2;
				try {
					if (et_regwadr.getText().toString().length() > 4)
						throw new Exception("��ַ���");

					addr = Short
							.parseShort(et_regwadr.getText().toString(), 16);

					val2 = (Long.parseLong(et_regwv.getText().toString(), 16));
					val=((int)val2)&0xffffffff;

				} catch (Exception ex) {
					Toast.makeText(SubRegopActivity.this,
							"����ȷд���ݺ͵�ַ:" + ex.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;

				}

				R2000_calibration.R2000cmd rcmdo = null;

		            R2000_calibration.OEM_DATA r2000oem = null;
		            R2000_calibration.MAC_DATA r2000mac = null;

		            if (spinner_regtype.getSelectedItemPosition() == 0)
		            {
		                rcmdo = R2000_calibration.R2000cmd.OEMwrite;
		                r2000oem =r2000pcmd.new OEM_DATA(addr, val);

		            }
		            else if(spinner_regtype.getSelectedItemPosition()==1)
		            {
		                rcmdo = R2000_calibration.R2000cmd.writeMAC; r2000mac =r2000pcmd.new MAC_DATA(addr, val);

		            }

		            if(spinner_regtype.getSelectedItemPosition()==2)
		            {
		                try
		                {
		                    
		                    byte[] data = new byte[5];
		                    data[0] = 0x07;
		                    data[1] = (byte)((addr & 0xff00) >> 8);
		                    data[2] = (byte)(addr & 0x00ff);
		                    data[3] = (byte)((val & 0xff00) >> 8);
		                    data[4] = (byte)(val & 0x00ff);
		                    byte[] senddata = r2000pcmd.GetSendCmd(R2000_calibration.R2000cmd.Regop, data);
		                    
		                    myapp.CommBth.Comm_Write(senddata,0, senddata.length);
		                    
		    				byte[] part1 = new byte[3];
		    				byte[] part2=null;
		    				byte[] revdata=null;
		    				int re = myapp.CommBth.Comm_Read(part1,0, part1.length);

		    				if (re > 0 && part1[2] != 0x00) {
		    					int l = (part1[1] & 0xff) + 4;
		    					part2 = new byte[l];
		    					revdata= new byte[l+3];
		    					System.arraycopy(part1, 0, revdata, 0, 3);
		    					 
		    					re = myapp.CommBth.Comm_Read(part2,0, part2.length);
		    					if (re > 0)
		    						System.arraycopy(part2, 0, revdata, 3, part2.length);

		    				}
		    				else
		    				 {
		    		                Toast.makeText(SubRegopActivity.this, "�շ�ָ��ʧ��", Toast.LENGTH_SHORT)
		    	    				.show();
		    		                return;
		    		          }
		                }
		                catch (Exception ex)
		                {
		                	 Toast.makeText(SubRegopActivity.this, "����ʧ��:" + ex.getMessage(), Toast.LENGTH_SHORT)
			    				.show();
			                    return;
		                }
		            }
		            else
		            {
		                try
		                {
		                    byte[] senddata = null;

		                    if (spinner_regtype.getSelectedItemPosition() == 0)
		                        senddata = r2000pcmd.GetSendCmd(rcmdo, r2000oem.ToByteData());
		                    else
		                        senddata = r2000pcmd.GetSendCmd(rcmdo, r2000mac.ToByteData());

		                    myapp.CommBth.Comm_Write(senddata,0, senddata.length);
		                    
		    				byte[] part1 = new byte[3];
		    				byte[] part2=null;
		    				byte[] revdata=null;
		    				int re = myapp.CommBth.Comm_Read(part1, part1.length,
		    						1000);

		    				if (re > 0 && part1[2] != 0x00) {
		    					int l = (part1[1] & 0xff) + 4;
		    					part2 = new byte[l];
		    					revdata= new byte[l+3];
		    					System.arraycopy(part1, 0, revdata, 0, 3);
		    					 
		    					re = myapp.CommBth.Comm_Read(part2,0, part2.length);
		    					if (re > 0)
		    						System.arraycopy(part2, 0, revdata, 3, part2.length);

		    				}
		    				else
		    				 {
		    		                Toast.makeText(SubRegopActivity.this, "�շ�ָ��ʧ��", Toast.LENGTH_SHORT)
		    	    				.show();
		    		                return;
		    		          }
		                }
		                catch (Exception ex)
		                {
		                	 Toast.makeText(SubRegopActivity.this, "����ʧ��:" + ex.getMessage(), Toast.LENGTH_SHORT)
			    				.show();
			                    return;
		                }

		            }
		            Toast.makeText(SubRegopActivity.this, "�����ɹ�", Toast.LENGTH_SHORT)
					.show();
			}
			
		});
	}
}
