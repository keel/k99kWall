/**
 * 
 */
package com.k99k.keel.wallpaper;


import org.json.JSONException;
import org.json.JSONObject;

import com.k99k.tools.encrypter.Encrypter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 用户数据获取与处理
 * @author keel
 *
 */
public final class ID {

	public ID() {
	}
	private static final String TAG = "ID";
	
	
//	private static JSONObject idJson;
//	private static JSONObject osJson;
	private static JSONObject smallIdJson;
	private static JSONObject fullIdJson;
	
	//缓存json加密后的String
//	private static String idJsonEnc;
//	private static String osJsonEnc;
//	private static String smallIdJsonEnc;
//	private static String fullIdJsonEnc;
	/**
	 * imei
	 */
	private static String imei = "";
	/**
	 * tel号码
	 */
	private static String tel = "";
	/**
	 * imsi
	 */
	private static String imsi = "";
	
	/**
	 * 语言
	 */
	private static String lang = "";
	
	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static float screenDpi = 0;
	
	/**
	 * 包名
	 */
	public static String PACKAGE = "com.k99k.keel.wallpaper";
	
	/**
	 * 获取广告参数的地址
	 */
	public static final String remoteAdOrder = "getadtype";
	
	/**
	 * app版本
	 */
	private static int appVer = 8;
	
	public static String DISPLAY;
	public static String BOARD ;
	public static String BRAND ;
	public static String FINGERPRINT ;
	public static String DEVICE ;
	public static String HOST ;
	public static String ID ;
	public static String MODEL;
	public static String PRODUCT ;
	public static String TAGS;
	public static String TYPE;
	public static String USER;
	
	public static final int getScreenWidth(){
		return screenWidth;
	}
	public static final int getScreenHeight(){
		return screenHeight;
	}
	public static final float getScreenDpi(){
		return screenDpi;
	}
	
	public static final int getAppVer(){
		return appVer;
	}
	
	public static final String getIMEI(){
		return imei;
	}
	public static final String getIMSI(){
		return imsi;
	}
	public static final String getTEL(){
		return tel;
	}
	public static final String getLANG(){
		return lang;
	}

	public static final void setLANG(String newLang){
		lang  = newLang;
	}
	
//	/**
//	 * 广告的分配,wooboo/youmi
//	 */
//	public static int[] ads = new int[]{70,30};
	
	public static String adkey = "game movie";
	
	/**
	 * 初始化,务必保证此方法先执行 
	 * @param activity Activity
	 */
	public static final void init(Activity activity,String packageStr){
		PACKAGE = packageStr;
		//这里通过在strings.xml的不同语言参数实现,所以需要在不同的语言文件夹中配置对应的lang参数
		lang = activity.getString(R.string.lang);
		//这里有另一种方法，未确认
		//String coutry = activity.getResources().getConfiguration().locale.getCountry();
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		imei = (tm.getDeviceId() == null) ? "" : tm.getDeviceId();
		tel = (tm.getLine1Number() == null) ? "" : tm.getLine1Number();
		// String snumber = tm.getSimSerialNumber();
		imsi = (tm.getSubscriberId() == null) ? "" : tm.getSubscriberId();

		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.heightPixels;
		screenHeight = dm.widthPixels;
		screenDpi = dm.density;
		try {
			appVer = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_ACTIVITIES).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "appver error",e);
		}
		DISPLAY = (android.os.Build.DISPLAY == null)?"":android.os.Build.DISPLAY;
		BOARD = (android.os.Build.BOARD== null)?"":android.os.Build.BOARD;
		BRAND = (android.os.Build.BRAND== null)?"":android.os.Build.BRAND;
		FINGERPRINT = (android.os.Build.FINGERPRINT== null)?"":android.os.Build.FINGERPRINT;
		DEVICE = (android.os.Build.DEVICE== null)?"":android.os.Build.DEVICE;
		HOST = (android.os.Build.HOST== null)?"":android.os.Build.HOST;
		ID = (android.os.Build.ID== null)?"":android.os.Build.ID;
		MODEL = (android.os.Build.MODEL== null)?"":android.os.Build.MODEL;
		PRODUCT = (android.os.Build.PRODUCT== null)?"":android.os.Build.PRODUCT;
		TAGS = (android.os.Build.TAGS== null)?"":android.os.Build.TAGS;
		TYPE = (android.os.Build.TYPE== null)?"":android.os.Build.TYPE;
		USER = (android.os.Build.USER== null)?"":android.os.Build.USER;
		//最后形成属性json
		initIDJson();
	}
	
	/**
	 * 生成属性的json集合,分三种集合
	 */
	private static final void initIDJson(){
//		idJson = new JSONObject();
//		osJson = new JSONObject();
		smallIdJson = new JSONObject();
		fullIdJson = new JSONObject();
		try {
			//smallIdJson
			smallIdJson.put("pk", PACKAGE);
			smallIdJson.put("lang", lang);
			smallIdJson.put("imei", imei);
			smallIdJson.put("appVersion", appVer);
//			//idJson
//			idJson.put("lang", lang);
//			idJson.put("imei", imei);
//			idJson.put("imsi", imsi);
//			idJson.put("tel", tel);
//			idJson.put("width", screenWidth);
//			idJson.put("height", screenHeight);
//			idJson.put("dpi", screenDpi);
//			idJson.put("appVersion", appVer);
//			//osJson
//			osJson.put("DISPLAY", DISPLAY);
//			osJson.put("BOARD", BOARD);
//			osJson.put("BRAND", BRAND);
//			osJson.put("FINGERPRINT", FINGERPRINT);
//			osJson.put("DEVICE", DEVICE);
//			osJson.put("HOST", HOST);
//			osJson.put("ID", ID);
//			osJson.put("MODEL", MODEL);
//			osJson.put("PRODUCT", PRODUCT);
//			osJson.put("TAGS", TAGS);
//			osJson.put("TYPE", TYPE);
//			osJson.put("USER", USER);
			//fullIdJson
			fullIdJson.put("pk", PACKAGE);
			fullIdJson.put("lang", lang);
			fullIdJson.put("imei", imei);
			fullIdJson.put("imsi", imsi);
			fullIdJson.put("tel", tel);
			fullIdJson.put("width", screenWidth);
			fullIdJson.put("height", screenHeight);
			fullIdJson.put("dpi", screenDpi);
			fullIdJson.put("appVersion", appVer);
			fullIdJson.put("display", DISPLAY);
			fullIdJson.put("board", BOARD);
			fullIdJson.put("brand", BRAND);
			fullIdJson.put("fingerprint", FINGERPRINT);
			fullIdJson.put("device", DEVICE);
			fullIdJson.put("host", HOST);
			fullIdJson.put("id", ID);
			fullIdJson.put("model", MODEL);
			fullIdJson.put("product", PRODUCT);
			fullIdJson.put("tags", TAGS);
			fullIdJson.put("type", TYPE);
			fullIdJson.put("user", USER);
			//Log.e(TAG, "display:"+DISPLAY);
			smallIdJson.put("userName", "");
			fullIdJson.put("userName", "");
			
			//缓存加密后的String
//			idJsonEnc = encrypt(idJson.toString());
//			osJsonEnc = encrypt(osJson.toString());
			//Log.e(TAG, fullIdJson.toString());
			//fullIdJsonEnc = encrypt(fullIdJson.toString());
			//Log.e(TAG, fullIdJsonEnc);
			//smallIdJsonEnc = encrypt(smallIdJson.toString());
//			try {
//				Log.e(TAG, encrypter.decrypt(fullIdJsonEnc));
//			} catch (Exception e) {
//			}
		} catch (JSONException e) {
			Log.e(TAG, "initIDJson error",e);
		}
	}
	
//	public static final JSONObject getIDJson(){
//		return idJson;
//	}
//	
//	public static final JSONObject getOSJson(){
//		return osJson;
//	}
	
	public void setUserToJson(String userNanme){
		try {
			smallIdJson.put("userName", userNanme);
			fullIdJson.put("userName", userNanme);
		} catch (JSONException e) {
		}
	}
	
	public static final JSONObject getSmallJson(){
		return smallIdJson;
	}
	
	public static final JSONObject getFullJson(){
		return fullIdJson;
	}
	
	
//	public static final String getIDJsonEnc(){
//		return idJsonEnc;
//	}
//	
//	public static final String getOSJsonEnc(){
//		return osJsonEnc;
//	}
	
	/**
	 * 直接从缓存中取密文,加入时间变量每次新生成密文
	 * @return String
	 */
	public static final String getSmallJsonEnc(){
		try {
			smallIdJson.put("time", System.currentTimeMillis());
			} catch (JSONException e) {
		}
		return encrypt(smallIdJson.toString());
	}
	
	public static final String getFullJsonEnc(){
		
		try {
			fullIdJson.put("time",System.currentTimeMillis());
			//String s = encrypt(fullIdJson.toString());
			//Log.e(TAG, " getFullJsonEnc:"+decrypt(s));
			//return s;
		} catch (JSONException e) {
			//return "";
		}
		return encrypt(fullIdJson.toString());
		//return fullIdJsonEnc;
	}
	
	

	//-------------------加密
//	/**
//	 * 加密用的key
//	 * TODO 未实现密钥网络更新机制
//	 */
//	static final String encryptKey = "htk";
	
	/**
	 * 加密器
	 */
	private static final Encrypter encrypter = createDesEncrypt();
	
	private final static Encrypter createDesEncrypt(){
		try {
			return new Encrypter();
		} catch (Exception e) {
			Log.e(TAG, "createDesEncrypt Error!",e);
		}
		return null;
	}
	
	public static final Encrypter getDesEncrypt(){
		return encrypter;
	}
	
	public static final String encrypt(String content){
		try {
			return encrypter.encrypt(content);
		} catch (Exception e) {
			Log.e(TAG, "encrypt Error!",e);
			return "";
		}
	}
	public static final String decrypt(String content){
		try {
			return encrypter.decrypt(content);
		} catch (Exception e) {
			Log.e(TAG, "decrypt Error!",e);
			return "";
		}
	}
}
