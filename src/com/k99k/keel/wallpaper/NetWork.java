/**
 * 
 */
package com.k99k.keel.wallpaper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

/**
 * 用于网络相关的操作
 * @author keel
 *
 */
public final class NetWork {

	public NetWork() {
	}
	
	private static final String TAG = "NetWork";
	
	//---------------服务器组管理
	private static String[] servers;
	
	/**
	 * 设置服务器组
	 * @param servs
	 */
	public static final void setServers(String[] servs){
		servers = servs;
	}
	
	private static int serverFailCount = 0;
	private static int serverNum = 0;
	
	/**
	 * 当前服务器
	 * @return
	 */
	public static final String getServer(){
		return servers[serverNum];
	}
	
	/**
	 * 服务器连接失败,同一服务器次数达到3次则发生切换
	 * @param autoChange 是否自动切换
	 */
	public static final void serverFail(boolean autoChange){
		serverFailCount++;
		if (autoChange && serverFailCount>3) {
			changeServer();
		}
	}
	
	/**
	 * 切换服务器
	 */
	public static final void changeServer(){
		if (servers != null && servers.length>1) {
			if ((serverNum + 1) < servers.length) {
				serverNum++;
			}else if((serverNum - 1) >= 0){
				serverNum--;
			}
		}
	}
	
	/**
	 * 切换到下一个服务器
	 * @return 是否切换过,false表示无下一个服务器或servers长度为1
	 */
	public static final boolean nextServer(){
		if (servers != null && servers.length>1) {
			if ((serverNum + 1) < servers.length) {
				serverNum++;
				return true;
			}
			return false;
		}else{
			return false;
		}
	}
	
	/**
	 * 服务器状态正常
	 */
	public static final void serverOK(){
		serverFailCount = 0;
	}
	
	
	//多服务器组自动切换
	
	//服务器组保存
	
	//服务器组读取
	
	
	
	
	
	
	

	
	private static final int IO_BUFFER_SIZE = 1024 * 4;

	private static final void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	public static final Bitmap getRemotePicWithWallProp(String url,String[] headers) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			//加入默认的参数
			conn.setRequestProperty("wall", ID.getSmallJsonEnc());
			String sortby = (K99KWall.orderby.equals("random") || K99KWall.orderby
					.equals("shuffle")) ? "time" : K99KWall.orderby;
			conn.setRequestProperty("sortBy", sortby);
			conn.setRequestProperty("sortType", K99KWall.orderAsc + "");
			conn.connect();
			// 获取图片id,判断是否已加星(根据同步后的本地加星列表)
			String oid = conn.getHeaderField("pic_oid");
			if (headers != null && oid != null && oid.length() > 0) {
//				headers.put("pic_oid", oid);
				headers[0] = oid;
			}
			final BufferedInputStream in = new BufferedInputStream(conn
					.getInputStream(), IO_BUFFER_SIZE);

			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			final BufferedOutputStream out = new BufferedOutputStream(
					dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();

			final byte[] data = dataStream.toByteArray();
			bm = BitmapFactory.decodeByteArray(data, 0, data.length);
		} catch (MalformedURLException e) {
			Log.e(TAG, "getRemotePic Error!" + url, e);
		} catch (IOException e) {
			Log.e(TAG, "getRemotePic Error!" + url, e);
		} catch (Exception e) {
			Log.e(TAG, "getRemotePic Error!" + url, e);
		}
		if (bm == null) {
			Log.e(TAG, "getRemotePic Error!" + url);
		} else {
			Log.d(TAG, "getRemotePic OK:" + url);
		}
		return bm;

	}

	/**
	 * 默认timeout为3000，换行为false
	 * @param url
	 * @return
	 */
	public final static String getUrlContent(String url){
		return NetWork.getUrlContent(url, 3000, false);
	}
	
	/**
	 * get方式获 取url的文本内容,所有数据均使用utf-8编码,默认3秒超时
	 * @param url
	 * @param timeOut 超时毫秒数
	 * @param breakLine 是否加入换行符
	 * @return 返回内容String
	 */
	public final static String getUrlContent(String url,int timeOut,boolean breakLine){
		String str = "";
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
//			conn.setRequestProperty("appVersion", appver+"");
//			conn.setRequestProperty("imei", imei);
//			conn.setRequestProperty("imsi", imsi);
//			conn.setRequestProperty("lang", lang);
//			conn.setRequestProperty("width", screenWidth+"");
//			conn.setRequestProperty("height", screenHeight+"");
//			conn.setRequestProperty("dpi", screenDpi+"");
//			
//			conn.setRequestProperty("DISPLAY", android.os.Build.DISPLAY);
//			conn.setRequestProperty("BOARD", android.os.Build.BOARD);
//			conn.setRequestProperty("BRAND", android.os.Build.BRAND);
//			conn.setRequestProperty("FINGERPRINT", android.os.Build.FINGERPRINT);
//			conn.setRequestProperty("DEVICE", android.os.Build.DEVICE);
//			conn.setRequestProperty("HOST", android.os.Build.HOST);
//			conn.setRequestProperty("ID", android.os.Build.ID);
//			conn.setRequestProperty("MODEL", android.os.Build.MODEL);
//			conn.setRequestProperty("PRODUCT", android.os.Build.PRODUCT);
//			conn.setRequestProperty("TAGS", android.os.Build.TAGS);
//			conn.setRequestProperty("TYPE", android.os.Build.TYPE);
//			conn.setRequestProperty("USER", android.os.Build.USER);
			conn.setConnectTimeout(timeOut);
			conn.connect();
			StringBuilder b = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		
			String line;
			if (breakLine) {
				while ((line = reader.readLine()) != null) {
					b.append(line);
					b.append("\r\n"); // 添加换行
				}
			}else{
				while ((line = reader.readLine()) != null) {
					b.append(line);
				}
			}
			
			reader.close();
			str = b.toString();
			Log.d(TAG, "getUrlContent OK:" + url);
		} catch (IOException e) {
			Log.e(TAG, "getUrlContent Error!"+url, e);
			return "";
		} catch (Exception e) {
			Log.e(TAG, "getUrlContent unknown Error!"+url, e);
			return "";
		}
		return str;
	}
	
	/**
	 * 使用wall参数发起一个post，timeout为3000，无换行
	 * @param url
	 * @param postValue post参数值,自动进行加密
	 * @return
	 */
	public final static String postUrlByEncrypt(String url,String postValue){
		String v = ID.encrypt(postValue);
		return NetWork.postUrl(url, v);
	}
	
	/**
	 * 使用wall参数发起一个post，timeout为3000，无换行
	 * @param url
	 * @param postValue post参数值,无加密
	 * @return
	 */
	public final static String postUrl(String url,String postValue){
		return NetWork.postUrl(url, "wall", postValue, 3000, false);
	}
	
	/*
	 * post数据到一个url,并获取反回的String,所有数据均使用utf-8编码,本应用用不到
	 * @param url Url地址
	 * @param paras Map形式的参数集
	 * @param timeOut 超时毫秒数
	 * @param breakLine 是否加入换行符
	 * @return 返回的结果页内容
	
	public final static String postUrl(String url,HashMap<String,String> paras,int timeOut,boolean breakLine){
		 // Construct data
		StringBuilder sb;
		String data = "";
		try {
			sb = new StringBuilder();
			for (Iterator<String> it = paras.keySet().iterator(); it.hasNext();) {
				String key = it.next();
				String value = paras.get(key);
				sb.append(URLEncoder.encode(key, "UTF-8")).append("=");
				sb.append(URLEncoder.encode(value, "UTF-8")).append("&");
			}
			sb.deleteCharAt(sb.length()-1);
			data = sb.toString();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG,"postUrl error!" ,e);
		}
	    return NetWork.postUrl(url, data, timeOut, breakLine);
	}
	 */
	
	
	/**
	 * post数据到一个url,并获取反回的String,此方法仅适用于单个参数,所有数据均使用utf-8编码
	 * @param url Url
	 * @param key 参数名,注意不带urlEncode功能,使用改进过的加密算法后无需URLencode
	 * @param value 参数值,注意不带urlEncode功能,使用改进过的加密算法后无需URLencode
	 * @param timeOut 超时毫秒数
	 * @param breakLine 是否加入换行符
	 * @return 返回的结果页内容
	 */
	private final static String postUrl(String url,String key,String value,int timeOut,boolean breakLine){
		  // Construct data
		StringBuilder sb = new StringBuilder();
		String data = "";
//		try {
//			sb.append(URLEncoder.encode(key, "UTF-8")).append("=");
//			sb.append(URLEncoder.encode(value, "UTF-8"));
			sb.append(key).append("=");
			sb.append(value).append("&");
			data = sb.toString();
//		} catch (UnsupportedEncodingException e) {
//			Log.e(TAG,"postUrl error!" ,e);
//		}
	    return NetWork.postUrl(url, data, timeOut, breakLine);
	}
	
	/**
	 * post数据到一个url,并获取反回的String,此方法仅适用于单个参数,所有数据均使用utf-8编码
	 * @param url Url
	 * @param key 参数名,注意不带urlEncode功能,使用改进过的加密算法后无需URLencode
	 * @param value 参数值,注意不带urlEncode功能,使用改进过的加密算法后无需URLencode
	 * @param timeOut 超时毫秒数
	 * @param breakLine 是否加入换行符
	 * @return 返回的结果页内容
	 */
	public final static String postUrl(String url,String[] key,String[] value,int timeOut,boolean breakLine){
		  // Construct data
		StringBuilder sb = new StringBuilder();
		String data = "";
		for (int i = 0; i < key.length; i++) {
			sb.append(key).append("=");
			sb.append(value).append("&");
		}
		sb.deleteCharAt(sb.length()-1);
		data = sb.toString();
	    return NetWork.postUrl(url, data, timeOut, breakLine);
	}
	
	/**
	 * post数据到一个url,并获取反回的String,此方法仅适用于单个参数,所有数据均使用utf-8编码
	 * @param url Url
	 * @param data 参数合成后的String
	 * @param timeOut 超时毫秒数
	 * @param breakLine 是否加入换行符
	 * @return 返回的结果页内容
	 */
	private final static String postUrl(String url,String data,int timeOut,boolean breakLine){
		try {
		    // Send data
		    URL aUrl = new URL(url);
		    URLConnection conn = aUrl.openConnection();
		    conn.setConnectTimeout(timeOut);
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
		    String line;
		    StringBuilder sb = new StringBuilder();
		    if (breakLine) {
				while ((line = rd.readLine()) != null) {
					sb.append(line);
					sb.append("\r\n"); // 添加换行
				}
			}else{
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
			}
		    wr.close();
		    rd.close();
		    return sb.toString();
		} catch (Exception e) {
			Log.e(TAG,"postUrl error!" ,e);
			return "";
		}
	}
	
	
	/**
	 * 实现用系统自带浏览器打开网页
	 * @param url
	 */
	public final static void openUrl(Context context,String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}
	
	/*
	private final String getRemoteFileByString(String url){
		String re = "";
		try {
			URL aURL = new URL(url);
			InetAddress addr = InetAddress.getByName(aURL.getHost());
			int port = 80;
			SocketAddress sockaddr = new InetSocketAddress(addr, port);

			// Create an unbound socket
			Socket sock = new Socket();

			// This method will block no more than timeoutMs.
			// If the timeout occurs, SocketTimeoutException is thrown.
			int timeoutMs = 2000; // 2 seconds
			sock.connect(sockaddr, timeoutMs);
			
			OutputStream output = sock.getOutputStream();
			StringBuilder sb = new StringBuilder();
			sb.append("GET " + url + " HTTP/1.1\r\n");
			sb.append("Host:" + addr.getHostAddress() + "\r\n");
			sb.append("Connection:Close\r\n");
			sb.append("\r\n");

			output.write(sb.toString().getBytes("utf8"));
			output.flush();

			//BufferedReader rd = new BufferedReader(new InputStreamReader(sock.getInputStream(),"utf8"));
			URLConnection conn = aURL.openConnection();
			conn.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final BufferedInputStream in = new BufferedInputStream(conn.getInputStream(),
					IO_BUFFER_SIZE);
			
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			final BufferedOutputStream  out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();
			//final byte[] data = dataStream.toByteArray();
			re = dataStream.toString("utf-8");//new String(data);
	        String str;
	        boolean contentStart = false;
	        while ((str = rd.readLine()) != null) {
	        	if (str.trim().length() == 0) {
	        		contentStart = true;
				}
	        	if (contentStart) {
	        		re+=str;
				}
	            
	        }
	        rd.close();
	        //sock.close();
		} catch (UnknownHostException e) {
			Log.e(TAG, "getRemoteTxt Error!"+url, e);
		} catch (SocketTimeoutException e) {
			Log.e(TAG, "getRemoteTxt Error!"+url, e);
		} catch (IOException e) {
			Log.e(TAG, "getRemoteTxt Error!"+url, e);
		}
		
		return re;
	}*/
}
