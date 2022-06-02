package com.example.module_android_bluedemo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bth.api.cls.Comm_Bluetooth;
import com.communication.imp.NetPort;
import com.communication.inf.Communication;
import com.function.SPconfig;
import com.silionmodule.*;

import android.app.Activity;
import android.app.Application;
import android.widget.TabHost;

public class MyApplication extends Application{

	//����
	//*
	public static String Constr_READ = "��";
	public static String Constr_CONNECT = "����";
	public static String Constr_INVENTORY = "�̵�";
	public static String Constr_RWLOP = "��д��";
	public static String Constr_PASSVICE = "��������";
	public static String Constr_ACTIVE = "��������";
	public static String Constr_SetFaill = "����ʧ�ܣ�";
	public static String Constr_GetFaill = "��ȡʧ�ܣ�";
	public static String Constr_SetOk="���óɹ�";
	public static String Constr_unsupport="��֧��";
	public static String Constr_Putandexit = "�ٰ�һ���˳�����";
	public static String[] Coname = new String[] { "���", "EPC ID", "����", "����",
			"Э��", "RSSI", "Ƶ��", "�������� " };
	public static String Constr_stopscan = "����ֹͣɨ��";
	public static String Constr_scanasetconnecto = "��ɨ�貢ѡ��һ��������д��,�����������";
	public static String Constr_scanselectabluereader = "��ɨ�貢ѡ��һ��������д��";
	public static String Constr_scanselectabluereaderandconnect = "��ɨ�貢ѡ��һ��������д��,�����������";
	public static String Constr_hadconnected = "�Ѿ�����";
	public static String Constr_plsetuuid = "�����ú�UUID:";
	public static String Constr_pwderror = "�������";
	public static String Constr_search = "����";
	public static String Constr_stop = "ֹͣ";
	public static String Constr_plselectsearchblueset = "��ѡ��Ҫ�����������豸";
	public static String Constr_startsearchblueok = "��ʼ�������� �ɹ�";
	public static String Constr_startsearchbluefail1 = "��ʼ��������:1 ʧ��";
	public static String Constr_startsearchbluefail2 = "��ʼ��������:2 ʧ��";
	public static String Constr_startsearchbluefail12 = "��ʼ��������:1,2 ʧ�ܣ���������������ȴ��������";
	public static String Constr_canclebluematch = "ȡ�������豸ƥ��:";
	public static String Constr_connectbluesetfail = "���������豸ʧ��:";
	public static String Constr_matchbluefail = "ƥ�������豸ʧ��";
	public static String Constr_pwdmatchfail = "����ƥ��ʧ��";
	public static String Constr_connectblueokthentoreader = "���������豸�ɹ�,�����Ӷ�д��";
	public static String Constr_connectblueserfail = "���������豸����ʧ��";
	public static String Constr_connectbluesetok = "���������豸�ɹ�";
	public static String Constr_createreaderok = "��д������ʧ��";
	static String[] pdaatpot = { "һ����", "˫����", "������", "������" };
	static String[] strconectway = { "����ʽ", "����ʽ" };
	    
	static String[] spibank={"������","EPC��","TID��","�û���"};
	static String[] spifbank={"EPC��","TID��","�û���"}; 
	public static String[] regtype;
	static String[] spilockbank={"��������","��������","EPCbank","TIDbank","USERbank"}; 
	static String[] spilocktype={"������","��ʱ����","��������"};
	public static String Constr_sub3readmem = "����ǩ";
	public static String Constr_sub3writemem = "д��ǩ";
	public static String Constr_sub3lockkill = "��������";
	public static String Constr_sub3readfail = "��ʧ��:";
	public static String Constr_sub3nodata = "������";
	public static String Constr_sub3wrtieok = "д�ɹ�";
	public static String Constr_sub3writefail = "дʧ��:";
	public static String Constr_sub3lockok = "���ɹ�";
	public static String Constr_sub3lockfail = "��ʧ��:";
	public static String Constr_sub3killok = "���ٳɹ�";
	public static String Constr_sub3killfial = "����ʧ��:";
	
	static String[] spireg={"�й�","����","�ձ�","����","ŷ��","ӡ��","���ô�","ȫƵ��"
			,"�й�2"};
	static String[] spinvmo={"��ͨģʽ","����ģʽ"};
	static String[] spitari={"25΢��","12.5΢��","6.25΢��"};
	static String[] spiwmod={"��д","��д"};
	public static String[] gpodemo;
	
    static String Auto="�Զ�";
    
    public static String Constr_sub4invenpra="�̵����";
    public static String Constr_sub4antpow="���߹���";
      		public static String Constr_sub4regionfre="����Ƶ��";
      		public static String Constr_sub4gen2opt="Gen2��";
      		public static String Constr_sub4invenfil="�̵����";
      		public static String Constr_sub4addidata="��������";
      		public static String Constr_sub4others="��������";
      		public static String Constr_sub4quickly = "����ģʽ";
      		public static String Constr_sub4setmodefail="����ģʽʧ��";
      		public static String Constr_sub4hadactivemo="�Ѿ�Ϊ����ģʽ";
      		public static String Constr_sub4setokresettoab="���óɹ���������д����Ч";
      		public static String Constr_sub4hadpasstivemo="�Ѿ�Ϊ����ģʽ";
      		public static String Constr_sub4ndsapow="���豸��Ҫ����һ��";
      		public static String Constr_sub4unspreg="��֧�ֵ�����";

	String[] spiregbs = { "����", "�й�", "ŷƵ", "�й�2" };
	public static String Constr_subblmode = "ģʽ";
	public static String Constr_subblinven = "�̵�";
	public static String Constr_subblfil = "����";
	public static String Constr_subblfre = "Ƶ��";
	public static String Constr_subbl = "����";
	public static String Constr_subblnofre = "û��ѡ��Ƶ��";
	
	static String[] cusreadwrite={"������","д����"};
	static String[] cuslockunlock={"��","����"};
	static String[] straryuploadm;
	static String[] straryuploads;
	static String[] strarytagend;
	
	public static String Constr_subcsalterpwd="������";
	public static String Constr_subcslockwpwd="��������";
	public static String Constr_subcslockwoutpwd="����������";
	public static String Constr_subcsplsetimeou="�����ó�ʱʱ��";
	public static String Constr_subcsputcnpwd="���뵱ǰ������������";
	public static String Constr_subcsplselreg="��ѡ������";
	public static String Constr_subcsopfail="����ʧ��:";
	public static String Constr_subcsputcurpwd="���뵱ǰ����";
	
	public static String Constr_subdbhaddisconnerecon = "�Ѿ��Ͽ�,������������";
	public static String Constr_subdbdisconnreconn = "�Ѿ��Ͽ�,������������";
	public static String Constr_subdbhadconnected = "�Ѿ�����";
	public static String Constr_subdbconnecting = "��������......";
	public static String Constr_subdbrev = "����";
	public static String Constr_subdbstop = "ֹͣ";
	public static String Constr_subdbdalennot = "���ݳ��Ȳ���";
	public static String Constr_subdbplpuhexchar = "������16�����ַ�";
	
	public static String Constr_setpwd = "���ù���:";
	public static String Constr_set = "����";
	    /*
	 * ��������   
	 */
	 public Map<String,TAGINFO> TagsMap=new LinkedHashMap<String,TAGINFO>();//����

	 public Comm_Bluetooth CommBth;
	 public Communication CommNet;
	 public int framecount=20;
	 public int frametime=100;
	 public int frameswtime=0;
	 public Activity Mact;
	 public int Mode;
	 public Map<String, String> m;

	 public  int gpodemomode=0;
	 public int gpodemotime=20;
	 public boolean gpodemo1 = false;
	 public boolean gpodemo2 = false;
	 public boolean gpodemo3 = false;
	 public boolean gpodemo4 = false;
	public Reader Mreader;
	public String Address;
	public SPconfig spf;
	public ReaderParams Rparams;
	public long exittime;
	public TabHost tabHost;
	public boolean isread;
	public String bluepassword;
	public int BackResult;
	//public TagMetaFlags TMFlags=new TagMetaFlags();
	public QuickModeOption qmioption=new QuickModeOption();
	public boolean connectok;
	
	public boolean isquicklymode = false;
	
	public long stoptimems;
	public boolean isstoptime;
	public long latetimems;
	public boolean islatetime;
	int Vtestforcount;//����ѭ������
	int Vtestforsleep;//����ѭ�����
	boolean VisTestFor=false;//�Ƿ�ѭ������
	public boolean VisStatics=false;
	
	File file;
	FileOutputStream fs = null;
	
	public class ReaderParams {
		public int antportc;
		public int sleep;
		public int[] uants;
		public int readtime;
		public String Curepc;
		public int Bank;
       
		public TagOp To;
		public TagFilter Tf;
		
		public int option;

		public ReaderParams() {
			sleep = 0;
			readtime = 50;
			uants = new int[1];
			uants[0] = 1;
			 
		}
	}
}
