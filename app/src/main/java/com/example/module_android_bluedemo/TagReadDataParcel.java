package com.example.module_android_bluedemo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.silionmodule.ReaderException;

public class TagReadDataParcel implements Parcelable {
  private TagDataParcel ptdata;
  private int pantenna;
  private int preadcount;
  private int prssi;
  private int pfrequency;
  private long ptime;
  private int pstartreadoffset;
  private byte[] addata=null;
  TagReadDataParcel(TagDataParcel tdata)
  {
	  ptdata=tdata;
  }
  
  TagReadDataParcel(byte[] epc,byte[] crc,byte[] pc)
  {
	  ptdata=new TagDataParcel(pc.clone(),epc.clone(),crc.clone());
  }
  
  TagReadDataParcel(byte[] epc,byte[] crc,byte[] pc,int ant,int count,int rssi,int fre,long time,int readoff)
  {
	  ptdata=new TagDataParcel(pc.clone(),epc.clone(),crc.clone());
	  pantenna=ant;
	  preadcount=count;
	  prssi=rssi;
	  ptime=time;
	  pfrequency=fre;
	  pstartreadoffset=readoff;
  }
  TagReadDataParcel(byte[] epc,int ant,int count,int rssi,int fre,long time) throws ReaderException
  {
	  ptdata=new TagDataParcel(epc.clone());
	  pantenna=ant;
	  preadcount=count;
	  prssi=rssi;
	  ptime=time;
	  pfrequency=fre;
	  pstartreadoffset=0;
  }
  TagReadDataParcel(byte[] epc,int ant,int count,int rssi,int fre,long time,byte[] data) throws ReaderException
  {
	  ptdata=new TagDataParcel(epc.clone());
	  pantenna=ant;
	  preadcount=count;
	  prssi=rssi;
	  ptime=time;
	  pfrequency=fre;
	  pstartreadoffset=0;
	  if(data!=null)
	  addata=data.clone();
  }
  TagReadDataParcel(byte[] epc,int ant,int count,int rssi,int fre,long time,
		  byte[] data,byte[] pc,byte[] crc) throws ReaderException
  {
	  ptdata=new TagDataParcel(pc!=null?pc.clone():null,
			  epc!=null?epc.clone():null,crc!=null?crc.clone():null);
	  pantenna=ant;
	  preadcount=count;
	  prssi=rssi;
	  ptime=time;
	  pfrequency=fre;
	  pstartreadoffset=0;
	  if(data!=null)
	  addata=data.clone();
	  
  }
  public TagReadDataParcel() {
	// TODO Auto-generated constructor stub
}

public byte[] AData()
  {
     return addata;
  }
  public TagDataParcel TData()
  {
	  return ptdata;
  }
  public String EPCHexstr()
  {
	  return ptdata.HexStr();
  }
  public byte[] EPCbytes()
  {
	  return ptdata.Epc().clone();
  }
  
  public byte[] CRC()
  {
	  return ptdata.CRC();
  }
  
  public byte[] PC()
  {
	  return ptdata.PC();
  }
  
  public int Antenna()
  {
	  return pantenna;
  }
  
  public int ReadCount()
  {
	  return preadcount;
  }
  
  public int RSSI()
  {
	  return prssi;
  }
  
  public int Frequency()
  {
	  return pfrequency;
  }
  
  public Date Time()
  {
	  return new Date(ptime);
  }
   
  public int ReadOffTime()
  {
	  return pstartreadoffset;
  }
  
  public String toString() 
  {
    return String.format("EPC:%s ant:%d count:%d rssi:%d",
                         (ptdata == null) ? "none" : ptdata.HexStr(),
                         pantenna,
                         preadcount,
                         prssi);
  }

@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public void writeToParcel(Parcel arg0, int arg1) {
	// TODO Auto-generated method stub
	arg0.writeParcelable(ptdata, PARCELABLE_WRITE_RETURN_VALUE);
	arg0.writeInt(pantenna);
	arg0.writeInt(preadcount);
	arg0.writeInt(prssi);
	arg0.writeInt(pfrequency);
	arg0.writeLong(ptime);
	arg0.writeInt(pstartreadoffset);
	arg0.writeByteArray(addata);
}

public static final Parcelable.Creator<TagReadDataParcel> CREATOR  = new Creator<TagReadDataParcel>(){
	@Override
	public TagReadDataParcel createFromParcel(Parcel source) {
		TagReadDataParcel object=  new TagReadDataParcel();
		 
		object.pantenna=source.readInt();
		object.preadcount=source.readInt();
		object.prssi =source.readInt();
		object.pfrequency =source.readInt();
		object.ptime =source.readLong();
		object.pstartreadoffset =source.readInt();
		source.readByteArray(object.addata);
		return object;
	}
	@Override
	public TagReadDataParcel[] newArray(int size) {
		return new TagReadDataParcel[size];
	}
	
};
}
