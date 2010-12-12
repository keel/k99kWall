/**
 * 
 */
package com.k99k.keel.wallpaper;


import org.json.JSONException;
import org.json.JSONObject;

import com.k99k.tools.desEncrypt.DesEncrypt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	
	
	private static JSONObject idJson;
	private static JSONObject smallIdJson;
	private static JSONObject fullIdJson;
	private static JSONObject osJson;
	
	//缓存json加密后的String
	private static String idJsonEnc;
	private static String smallIdJsonEnc;
	private static String fullIdJsonEnc;
	private static String osJsonEnc;
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
	 * app版本
	 */
	private static int appVer = 8;
	
	public static final String DISPLAY = android.os.Build.DISPLAY;
	public static final String BOARD = android.os.Build.BOARD;
	public static final String BRAND = android.os.Build.BRAND;
	public static final String FINGERPRINT = android.os.Build.FINGERPRINT;
	public static final String DEVICE = android.os.Build.DEVICE;
	public static final String HOST = android.os.Build.HOST;
	public static final String ID = android.os.Build.ID;
	public static final String MODEL = android.os.Build.MODEL;
	public static final String PRODUCT = android.os.Build.PRODUCT;
	public static final String TAGS = android.os.Build.TAGS;
	public static final String TYPE = android.os.Build.TYPE;
	public static final String USER = android.os.Build.USER;
	
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
	
	
	
	
	/**
	 * 初始化,务必保证此方法先执行 
	 * @param activity Activity
	 */
	public static final void init(Activity activity){
		//这里通过在strings.xml的不同语言参数实现,所以需要在不同的语言文件夹中配置对应的lang参数
		lang = activity.getString(R.string.lang);
		//这里有另一种方法，未确认
		//String coutry = activity.getResources().getConfiguration().locale.getCountry();
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		imei = (tm.getDeviceId() == null) ? "" : tm.getDeviceId();
		tel = (tm.getLine1Number() == null) ? "" : tm.getDeviceId();
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
		//最后形成属性json
		initIDJson();
	}
	
	/**
	 * 生成属性的json集合,分三种集合
	 */
	private static final void initIDJson(){
		idJson = new JSONObject();
		osJson = new JSONObject();
		smallIdJson = new JSONObject();
		fullIdJson = new JSONObject();
		try {
			//smallIdJson
			smallIdJson.put("lang", lang);
			smallIdJson.put("imei", imei);
			smallIdJson.put("appVer", appVer);
			//idJson
			idJson.put("lang", lang);
			idJson.put("imei", imei);
			idJson.put("imsi", imsi);
			idJson.put("tel", tel);
			idJson.put("screenWidth", screenWidth);
			idJson.put("screenHeight", screenHeight);
			idJson.put("screenDpi", screenDpi);
			idJson.put("appVer", appVer);
			//osJson
			osJson.put("DISPLAY", DISPLAY);
			osJson.put("BOARD", BOARD);
			osJson.put("BRAND", BRAND);
			osJson.put("FINGERPRINT", FINGERPRINT);
			osJson.put("DEVICE", DEVICE);
			osJson.put("HOST", HOST);
			osJson.put("ID", ID);
			osJson.put("MODEL", MODEL);
			osJson.put("PRODUCT", PRODUCT);
			osJson.put("TAGS", TAGS);
			osJson.put("TYPE", TYPE);
			osJson.put("USER", USER);
			//fullIdJson
			fullIdJson.put("idJson", idJson);
			fullIdJson.put("osJson", osJson);
			
			//缓存加密后的String
			idJsonEnc = encrypt(idJson.toString());
			osJsonEnc = encrypt(osJson.toString());
			fullIdJsonEnc = encrypt(fullIdJson.toString());
			smallIdJsonEnc = encrypt(smallIdJson.toString());
			
		} catch (JSONException e) {
			Log.e(TAG, "initIDJson error",e);
		}
	}
	
	public static final JSONObject getIDJson(){
		return idJson;
	}
	
	public static final JSONObject getOSJson(){
		return osJson;
	}
	
	public static final JSONObject getSmallJson(){
		return smallIdJson;
	}
	
	public static final JSONObject getFullJson(){
		return fullIdJson;
	}
	
	/**
	 * 直接从缓存中取密文,如果用于高安全项目应该加入时间变量每次新生成密文
	 * @return String
	 */
	public static final String getIDJsonEnc(){
		return idJsonEnc;
	}
	
	public static final String getOSJsonEnc(){
		return osJsonEnc;
	}
	
	public static final String getSmallJsonEnc(){
		return smallIdJsonEnc;
	}
	
	public static final String getFullJsonEnc(){
		return fullIdJsonEnc;
	}
	
	

	//-------------------加密
	/**
	 * 加密用的key
	 * TODO 未实现密钥网络更新机制
	 */
	static final String encryptKey = "htHunter01_!(!)";
	
	/**
	 * 加密器
	 */
	private static final DesEncrypt desEncrypt = createDesEncrypt(encryptKey);
	
	private final static DesEncrypt createDesEncrypt(String enKey){
		try {
			return new DesEncrypt(encryptKey);
		} catch (Exception e) {
			Log.e(TAG, "createDesEncrypt Error!",e);
		}
		return null;
	}
	
	public static final DesEncrypt getDesEncrypt(){
		return desEncrypt;
	}
	
	public static final String encrypt(String content){
		try {
			return desEncrypt.encrypt(content);
		} catch (Exception e) {
			Log.e(TAG, "encrypt Error!",e);
			return "";
		}
	}
	
}
