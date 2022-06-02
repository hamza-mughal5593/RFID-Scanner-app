package com.example.module_android_bluedemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.silionmodule.Functional;
import com.silionmodule.ReaderException;

public class TagDataParcel implements Parcelable{
   private byte[] data;
   private String hexstr;
   private byte[] ppc;
   private byte[] pcrc;
   
   public byte[] Epc()
   {
	   
	   return data.clone();
   }
   public byte[] CRC()
   {
	   if(pcrc!=null)
	   return pcrc.clone();
	   else
		   return null;
   }
   public byte[] PC()
   {
	   if(ppc!=null)
	   return ppc.clone();
	   else
		   return null;
   }
   public String HexStr()
   {
	   return hexstr;
   }
   public TagDataParcel(byte[] pc,byte[] epc,byte[] crc)
   {
	   ppc=pc!=null?pc.clone():null;
	   data=epc!=null?epc.clone():null;
	   pcrc=crc!=null?crc.clone():null;
	   hexstr=Functional.bytes_Hexstr(epc);
	   
   }
   public TagDataParcel(byte[] epcdata,byte[] crc)
   {
	   data=epcdata.clone();
	   hexstr=Functional.bytes_Hexstr(data);
	   ppc=null;
	   pcrc=crc.clone();
   }
   public TagDataParcel(String hexstrdata) throws ReaderException
   {
	   hexstr=hexstrdata;
	   data=Functional.hexstr_Bytes(hexstr);
	   ppc=null;
	   pcrc=null;
   }
   public TagDataParcel(byte[] epcdata) throws ReaderException
   {
	   hexstr=Functional.bytes_Hexstr(epcdata);
	   data=epcdata.clone();
	   ppc=null;
	   pcrc=null;
   }
   public TagDataParcel(String hexstrdata,String hexstrcrc) throws ReaderException
   {
	   hexstr=hexstrdata;
	   data=Functional.hexstr_Bytes(hexstr);
	   ppc=null;
	   pcrc=Functional.hexstr_Bytes(hexstrcrc);
   }
public TagDataParcel() {
	// TODO Auto-generated constructor stub
}
@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}
@Override
public void writeToParcel(Parcel arg0, int arg1) {
	// TODO Auto-generated method stub
		 arg0.writeByteArray(data);
		 arg0.writeString(hexstr);
		 arg0.writeByteArray(ppc);
		 arg0.writeByteArray(pcrc);
}

//必须实现这个接口，它的作用是从 percel中读出来数据，顺序必须按照声明顺序
	public static final Parcelable.Creator<TagDataParcel> CREATOR  = new Creator<TagDataParcel>(){
		@Override
		public TagDataParcel createFromParcel(Parcel source) {
			TagDataParcel object=  new TagDataParcel();
			 source.readByteArray(object.data);
			object.hexstr = source.readString();
			 source.readByteArray(object.ppc);
			 source.readByteArray(object.pcrc);
			return object;
		}
		@Override
		public TagDataParcel[] newArray(int size) {
			return new TagDataParcel[size];
		}
		
	};

 
}
