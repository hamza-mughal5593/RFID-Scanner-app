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

	//常量
	//*
	public static String Constr_READ = "读";
	public static String Constr_CONNECT = "连接";
	public static String Constr_INVENTORY = "盘点";
	public static String Constr_RWLOP = "读写锁";
	public static String Constr_PASSVICE = "被动设置";
	public static String Constr_ACTIVE = "主动设置";
	public static String Constr_SetFaill = "设置失败：";
	public static String Constr_GetFaill = "获取失败：";
	public static String Constr_SetOk="设置成功";
	public static String Constr_unsupport="不支持";
	public static String Constr_Putandexit = "再按一次退出程序";
	public static String[] Coname = new String[] { "序号", "EPC ID", "次数", "天线",
			"协议", "RSSI", "频率", "附加数据 " };
	public static String Constr_stopscan = "请先停止扫描";
	public static String Constr_scanasetconnecto = "请扫描并选中一个蓝牙读写器,并且完成连接";
	public static String Constr_scanselectabluereader = "请扫描并选中一个蓝牙读写器";
	public static String Constr_scanselectabluereaderandconnect = "请扫描并选中一个蓝牙读写器,并且完成连接";
	public static String Constr_hadconnected = "已经连接";
	public static String Constr_plsetuuid = "请设置好UUID:";
	public static String Constr_pwderror = "密码错误";
	public static String Constr_search = "搜索";
	public static String Constr_stop = "停止";
	public static String Constr_plselectsearchblueset = "请选择要搜索的蓝牙设备";
	public static String Constr_startsearchblueok = "开始搜索蓝牙 成功";
	public static String Constr_startsearchbluefail1 = "开始搜索蓝牙:1 失败";
	public static String Constr_startsearchbluefail2 = "开始搜索蓝牙:2 失败";
	public static String Constr_startsearchbluefail12 = "开始搜索蓝牙:1,2 失败，将重启蓝牙，请等待重启完成";
	public static String Constr_canclebluematch = "取消蓝牙设备匹配:";
	public static String Constr_connectbluesetfail = "连接蓝牙设备失败:";
	public static String Constr_matchbluefail = "匹配蓝牙设备失败";
	public static String Constr_pwdmatchfail = "密码匹配失败";
	public static String Constr_connectblueokthentoreader = "连接蓝牙设备成功,将连接读写器";
	public static String Constr_connectblueserfail = "连接蓝牙设备服务失败";
	public static String Constr_connectbluesetok = "连接蓝牙设备成功";
	public static String Constr_createreaderok = "读写器创建失败";
	static String[] pdaatpot = { "一天线", "双天线", "三天线", "四天线" };
	static String[] strconectway = { "被动式", "主动式" };
	    
	static String[] spibank={"保留区","EPC区","TID区","用户区"};
	static String[] spifbank={"EPC区","TID区","用户区"}; 
	public static String[] regtype;
	static String[] spilockbank={"访问密码","销毁密码","EPCbank","TIDbank","USERbank"}; 
	static String[] spilocktype={"解锁定","暂时锁定","永久锁定"};
	public static String Constr_sub3readmem = "读标签";
	public static String Constr_sub3writemem = "写标签";
	public static String Constr_sub3lockkill = "锁与销毁";
	public static String Constr_sub3readfail = "读失败:";
	public static String Constr_sub3nodata = "无数据";
	public static String Constr_sub3wrtieok = "写成功";
	public static String Constr_sub3writefail = "写失败:";
	public static String Constr_sub3lockok = "锁成功";
	public static String Constr_sub3lockfail = "锁失败:";
	public static String Constr_sub3killok = "销毁成功";
	public static String Constr_sub3killfial = "销毁失败:";
	
	static String[] spireg={"中国","北美","日本","韩国","欧洲","印度","加拿大","全频段"
			,"中国2"};
	static String[] spinvmo={"普通模式","高速模式"};
	static String[] spitari={"25微秒","12.5微秒","6.25微秒"};
	static String[] spiwmod={"字写","块写"};
	public static String[] gpodemo;
	
    static String Auto="自动";
    
    public static String Constr_sub4invenpra="盘点参数";
    public static String Constr_sub4antpow="天线功率";
      		public static String Constr_sub4regionfre="区域频率";
      		public static String Constr_sub4gen2opt="Gen2项";
      		public static String Constr_sub4invenfil="盘点过滤";
      		public static String Constr_sub4addidata="附加数据";
      		public static String Constr_sub4others="其他参数";
      		public static String Constr_sub4quickly = "快速模式";
      		public static String Constr_sub4setmodefail="配置模式失败";
      		public static String Constr_sub4hadactivemo="已经为主动模式";
      		public static String Constr_sub4setokresettoab="设置成功，重启读写器生效";
      		public static String Constr_sub4hadpasstivemo="已经为被动模式";
      		public static String Constr_sub4ndsapow="该设备需要功率一致";
      		public static String Constr_sub4unspreg="不支持的区域";

	String[] spiregbs = { "北美", "中国", "欧频", "中国2" };
	public static String Constr_subblmode = "模式";
	public static String Constr_subblinven = "盘点";
	public static String Constr_subblfil = "过滤";
	public static String Constr_subblfre = "频率";
	public static String Constr_subbl = "蓝牙";
	public static String Constr_subblnofre = "没有选择频点";
	
	static String[] cusreadwrite={"读操作","写操作"};
	static String[] cuslockunlock={"锁","解锁"};
	static String[] straryuploadm;
	static String[] straryuploads;
	static String[] strarytagend;
	
	public static String Constr_subcsalterpwd="改密码";
	public static String Constr_subcslockwpwd="带密码锁";
	public static String Constr_subcslockwoutpwd="不带密码锁";
	public static String Constr_subcsplsetimeou="请设置超时时间";
	public static String Constr_subcsputcnpwd="填入当前密码与新密码";
	public static String Constr_subcsplselreg="请选择区域";
	public static String Constr_subcsopfail="操作失败:";
	public static String Constr_subcsputcurpwd="填入当前密码";
	
	public static String Constr_subdbhaddisconnerecon = "已经断开,正在重新连接";
	public static String Constr_subdbdisconnreconn = "已经断开,正在重新连接";
	public static String Constr_subdbhadconnected = "已经连接";
	public static String Constr_subdbconnecting = "正在连接......";
	public static String Constr_subdbrev = "接收";
	public static String Constr_subdbstop = "停止";
	public static String Constr_subdbdalennot = "数据长度不对";
	public static String Constr_subdbplpuhexchar = "请输入16进制字符";
	
	public static String Constr_setpwd = "设置功率:";
	public static String Constr_set = "设置";
	    /*
	 * 公共变量   
	 */
	 public Map<String,TAGINFO> TagsMap=new LinkedHashMap<String,TAGINFO>();//有序

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
	int Vtestforcount;//测试循环次数
	int Vtestforsleep;//测试循环间隔
	boolean VisTestFor=false;//是否循环测试
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
