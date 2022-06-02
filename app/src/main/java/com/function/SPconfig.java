package com.function;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPconfig {
	SharedPreferences sp;
    public SPconfig(Context cth)
    {
    	  Context ctx = cth;       
          sp = ctx.getSharedPreferences("SP", android.content.Context.MODE_PRIVATE);
    }
    
    public String GetString(String key)
    {
    	return sp.getString(key, "");
    }
    
    public boolean SaveString(String key,String val)
    {
    	try{
    	 Editor editor = sp.edit();
         editor.putString(key, val);
         editor.commit();
    	}catch(Exception ex)
    	{
    		return false;
    	}
         return true;
    }
}
