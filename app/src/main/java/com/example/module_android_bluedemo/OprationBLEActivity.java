package com.example.module_android_bluedemo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bth.api.cls.BlueTooth4_C.BLECharater;
import com.bth.api.cls.BlueTooth4_C.BLEServices;
import com.function.SPconfig;
import com.function.*;

public class OprationBLEActivity extends Activity{
	MyApplication myapp;
	ExpandableListView mGattServicesList;
	RadioGroup rg;
	TextView uuidtv,readuuid,writeuuid,passuuid;
	EditText ed_pwd;
	Button intoopra,setuuid,clearuuid,refreshservice;
	Xml xmlf;
	SPconfig spf;
	//uuid
		String Uuid;
		String Uuid_read;
		String Uuid_write;
		String Uuid_pass;
		String Val_pass;
		
	private ArrayList<ArrayList<BLECharater>> mGattCharacteristics =
            new ArrayList<ArrayList<BLECharater>>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bulesetopration_ble);
		
		Application app=getApplication();
		myapp=(MyApplication) app;
		xmlf=new Xml();
		xmlf.init();
		rg=(RadioGroup)this.findViewById(R.id.radioGroup_servieces);
		uuidtv=(TextView)this.findViewById(R.id.textView_uuid);
		readuuid=(TextView)this.findViewById(R.id.textView_read);
		writeuuid=(TextView)this.findViewById(R.id.textView_write);
		passuuid=(TextView)this.findViewById(R.id.textView_pwd);
		ed_pwd=(EditText)this.findViewById(R.id.editText_pwd);
		
		intoopra=(Button)this.findViewById(R.id.button_into);
		//setuuid=(Button)this.findViewById(R.id.button_setuuid);
		clearuuid=(Button)this.findViewById(R.id.button_clearuuid);
		refreshservice=(Button)this.findViewById(R.id.button_refreshservie);
		
        mGattServicesList = (ExpandableListView) findViewById(R.id.expandableListView_servies);
        mGattServicesList.setOnChildClickListener(servicesListClickListner);

		TextView et1=(TextView)findViewById(R.id.textView_bleset);
		et1.setText(myapp.CommBth.GetConnectAddr());
		TextView et2=(TextView)findViewById(R.id.textView_blestate);
		et2.setText(MyApplication.Constr_hadconnected);
		 
		displayGattServices(myapp.CommBth.FindServices(6000));
 
		clearuuid.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clearuuid();
			}
			
		});
		refreshservice.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clearuuid();
          		 ArrayList<LinkedHashMap<String, String>> gattServiceData = new ArrayList<LinkedHashMap<String, String>>();
                 ArrayList<ArrayList<LinkedHashMap<String, String>>> gattCharacteristicData
                         = new ArrayList<ArrayList<LinkedHashMap<String, String>>>();
          		SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                        getApplicationContext(),
                        gattServiceData,
                        android.R.layout.simple_expandable_list_item_2,
                        new String[] {"NAME", "UUID"},
                        new int[] { android.R.id.text1, android.R.id.text2 },
                        gattCharacteristicData,
                        android.R.layout.simple_expandable_list_item_2,
                        new String[] {"NAME", "UUID"},
                        new int[] { android.R.id.text1, android.R.id.text2 }
                );
                mGattServicesList.setAdapter(gattServiceAdapter);
          		displayGattServices(myapp.CommBth.FindServices(1000*20)); 
			}
			
		});
		 
		intoopra.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(uuidtv.getText().toString()==""|| readuuid.getText().toString()=="")
				{
					Toast.makeText(OprationBLEActivity.this, MyApplication.Constr_plsetuuid,
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(!passuuid.equals(""))
					myapp.CommBth.SetServiceUUIDs(uuidtv.getText().toString(),passuuid.getText().toString());
				
				//判断是否需要密码验证
				Val_pass=ed_pwd.getText().toString();
				if(myapp.CommBth.ToMatch(Val_pass)!=0)
				{
					Toast.makeText(OprationBLEActivity.this, MyApplication.Constr_pwderror,
							Toast.LENGTH_SHORT).show();
					return;
				}
				myapp.CommBth.SetServiceUUIDs(uuidtv.getText().toString(), readuuid.getText().toString(),
						writeuuid.getText().toString());
				setResult(1);
				if(myapp.CommBth.IssetUUID())
				{
					myapp.BackResult=1;
					myapp.bluepassword=Val_pass;
					
					finish();
				}
				else
					Toast.makeText(OprationBLEActivity.this, MyApplication.Constr_plsetuuid,
							Toast.LENGTH_SHORT).show();
			}
			
		}
		);
	}
	private void clearuuid()
	{
		 myapp.CommBth.ResetUUID();
		 uuidtv.setText("");
  		 readuuid.setText("");
  		 writeuuid.setText("");
  		 passuuid.setText("");
  		 
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
    
	private void displayGattServices(List<BLEServices> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = "Unknown service";
        String unknownCharaString = "Unknown characteristic";
        ArrayList<LinkedHashMap<String, String>> gattServiceData = new ArrayList<LinkedHashMap<String, String>>();
        ArrayList<ArrayList<LinkedHashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<LinkedHashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BLECharater>>();
        spf=myapp.spf;
        Uuid=spf.GetString("Uuid");
        Uuid_read=spf.GetString("Uuid_read");
        Uuid_write=spf.GetString("Uuid_write");
        Uuid_pass=spf.GetString("Uuid_pass");
        Val_pass=spf.GetString("Val_pass");
        // Loops through available GATT Services.
        for (BLEServices gattService : gattServices) {
        	LinkedHashMap<String, String> currentServiceData = new LinkedHashMap<String, String>();
            uuid = gattService.UUID();
            currentServiceData.put("NAME", unknownServiceString);
            currentServiceData.put("UUID", uuid);
            gattServiceData.add(currentServiceData);

            if(uuid.endsWith(Uuid)&&uuid!="")
            {
            	  uuidtv.setText(Uuid);
           	    
           		   readuuid.setText(Uuid_read);
           	    
           		   writeuuid.setText(Uuid_write);
           		   passuuid.setText(Uuid_pass);
           		   if(!passuuid.equals(""))
            	   myapp.CommBth.SetServiceUUIDs(uuidtv.getText().toString(), readuuid.getText().toString(),
						writeuuid.getText().toString());
           		   else
           		   {
           			 myapp.CommBth.SetServiceUUIDs(uuidtv.getText().toString(), readuuid.getText().toString(),
     						writeuuid.getText().toString());
           		   }
            }
            ArrayList<LinkedHashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<LinkedHashMap<String, String>>();
            List<BLECharater> gattCharacteristics =gattService.BLElist();
            ArrayList<BLECharater> charas =new ArrayList<BLECharater>();

            // Loops through available Characteristics.
            for (BLECharater gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                LinkedHashMap<String, String> currentCharaData = new LinkedHashMap<String, String>();
                uuid = gattCharacteristic.UUID();
                currentCharaData.put("NAME"	, unknownCharaString);
                currentCharaData.put("UUID", uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {"NAME", "UUID"},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {"NAME", "UUID"},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
    }
	
	private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    
                	  SimpleExpandableListAdapter ela=(SimpleExpandableListAdapter) mGattServicesList.getExpandableListAdapter();
                	  
                	  @SuppressWarnings("unchecked")
					  LinkedHashMap<String, String> bles=(LinkedHashMap<String, String>) ela.getGroup(groupPosition);
                	  int ign=SortGroup(rg);
                	  // mGattServicesList
                	   
                	   uuidtv.setText(bles.get("UUID"));
                	   BLECharater blec=mGattCharacteristics.get(groupPosition).get(childPosition);
                	 
                	   if(ign==0)
                		   readuuid.setText(blec.UUID());
                	   else if(ign==1)
                		   writeuuid.setText(blec.UUID());
                	   else
                		   passuuid.setText(blec.UUID());
                		   
                	   return true;
                }
    };  
}
