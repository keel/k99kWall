/**
 * 
 */
package com.k99k.keel.wallpaper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

/**
 * IO操作
 * @author keel
 *
 */
public final class IO {

	public IO() {
	}

	private static final String TAG = "IO";
	/**
	 * 读取raw下的文件
	 * @param context
	 * @param file raw下的文件
	 * @return
	 */
	public static final String readRaw(Context context, int resId) {
		String data = "";
		try {
			//InputStream stream = context.getAssets().open(file);
			//InputStream stream = context.getResources().openRawResource(resId);
			BufferedReader in = new BufferedReader(
		            new InputStreamReader(context.getResources().openRawResource(resId), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			int c;
			while ((c = in.read()) != -1) {
				sb.append((char) c);
			}
			in.close();
			if (sb.length() > 0) {
				data = sb.toString();
			}

		} catch (FileNotFoundException e) {
			Log.e(TAG, "readIndex - FileNotFound:" + resId,e);
			return "";
		} catch (IOException e) {
			Log.e(TAG, "readIndex - File read error:" + resId,e);
			return "";
		}
		return data;
	} 
	
	/**
	 * 读取文本文件
	 * @param context
	 * @param file
	 * @return 失败则返回空字符串
	 */
	public static final String readTxt(Context context, String file) { 
		String data = "";
		try {
			//FileInputStream stream = context.openFileInput(file);
			BufferedReader in = new BufferedReader(
		            new InputStreamReader(context.openFileInput(file), "UTF-8"));
			StringBuffer sb = new StringBuffer();
			int c;
			while ((c = in.read()) != -1) {
				sb.append((char) c);
			}
			in.close();
			data = sb.toString();

		} catch (FileNotFoundException e) {
			Log.w(TAG, "FileNotFound:" + file,e);
			return "";
		} catch (IOException e) {
			Log.e(TAG, "File read error:" + file,e);
			return "";
		}
		return data;
	} 
	

	
	/**
	 * 写入文件到本地files目录,utf-8方式
	 * @param context Context
	 * @param file 本地文件名
	 * @param msg 需要写入的字符串
	 */
	public static final void writeTxt(Context context, String file, String msg) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					context.openFileOutput(file, Context.MODE_PRIVATE),"utf-8"));

//			FileOutputStream stream = context.openFileOutput(file, Context.MODE_PRIVATE);
//			stream.write(msg.getBytes());
//			stream.flush();
			out.write(msg);
			out.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFound:" + file,e);
			return;
		} catch (IOException e) {
			Log.e(TAG, "File write error:" + file,e);
			return;
		}
	}
	
	/**
	 * 保存图片到SD卡
	 * @param fileName 文件名
	 * @param savePath 文件路径(需要保证它存在)
	 * @param pic 图片
	 * @return 是否成功保存
	 */
	public static final boolean savePic(String fileName,String savePath,Bitmap pic){
		if (pic == null) {
			return false;
		}
		try {

				//如果SD卡未挂载
//				if (android.os.Environment.getExternalStorageState() != android.os.Environment.MEDIA_MOUNTED) {
//					showDialog(DIALOG_ERR_NOSDCARD);
//					return;
//				}
				//FileOutputStream out = this.openFileOutput(fileName, MODE_WORLD_READABLE);//这是保存到/data/package下面，不能定义位置
			File f = new File(savePath);
			if (!f.exists()) {
				f.mkdir();
			}
			FileOutputStream out = new FileOutputStream(fileName);
			pic.compress(CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			return true;
		} catch (IOException e) {
			Log.e(TAG, "savePic error:" + fileName,e);
			return false;
		}

	}

}
