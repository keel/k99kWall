package com.k99k.keel.wallpaper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.admob.android.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import com.k99k.keel.wallpaper.R;
import com.wooboo.adlib_android.WoobooAdView;
/**
 * 主界面Activity
 * @author keel
 *
 */
public class K99KWall extends Activity {
	
	private static final String TAG  ="K99KWall";

	
	private Button b_new;
	private Button b_cate;
	private Button b_random;
	private Button b_next;
	private Button b_pre;
	private Button b_star;
	
	//private View topView;
	private View bottomView;
	private TextView pagenView;
	//private View bottom2;
	private ScrollView centerScrollView;
	private ListView listView;
//	private static ImageView[] imgs = new ImageView[4];
	private static ImageView[] loader = new ImageView[4];
	private static Bitmap[] loadedBmp = new Bitmap[4];
	static String[] cateArr;
	
	
	private static final int MSG_LOAD_SMALL = 10;
	private static final int MSG_LOAD_ERR = 20;
	private static final int MSG_JSON_ERR = 8;
	private static final int MSG_EXIT = 9;
	private static final int MSG_LOAD_COMPLET = 7;
	private static final int MSG_SYN = 101;
	private static final int MSG_SYNOK = 102;
	private static final int MSG_SYNERR = 103;
	
	private static final int MENU_HELP = 1;
	//private static final int MENU_INIT = 2;
	private static final int MENU_ABOUT = 3;
	//private static final int MENU_MORE = 4;
	private static final int MENU_EXIT = 5;
	
	static String adkey = "game";
	
	/**
	 * json根节点String
	 */
	private static String jsonStr;
	/**
	 * json根节点
	 */
	private static JSONObject json;

	/**
	 * 用于显示星星菜单的json
	 */
	static JSONObject btjson;
	
	/**
	 * btjson中处于ison为true的位置,-1为未选择状态,1为第一个选项,0为title,仅用于排序,与加星菜单无关(与ShowPic无关)
	 */
	static int starBtOn = -1;
	
	private SharedPreferences settings;
	
	
	/**
	 * 四个图片路径数组，可为远程路径或本地图片名
	 */
	private static String[] imgPaths = new String[4];
//	/**
//	 * 当前页码
//	 */
//	private static int imgPage = 1;
//	/**
//	 * 目前就定为"remote"常量! 
//	 * json数据中的tag名，是一个String类型的key
//	 */
//	private static final String jsonTag = "remote";
	/**
	 * 标识当前的类别
	 */
	static int jsonCate = 0;
	
	/**
	 * 特殊的标识，用于在显示某类的随机图时，保持上一页下一页的可用性
	 */
	static boolean isCateRandom = false;
	/**
	 * json数据中的页码标识，是一个int类型的key
	 */
	static int jsonPage = 1;
	/**
	 * 每页显示图片数
	 */
	private static final int perPageCount = 4;
	
	/**
	 * 是否已同步加星的情况
	 */
	private static boolean hasSynStar = false;
	
	/**
	 * 进度条显示状态
	 */
	private static boolean showProc = false;
	
	/**
	 * 同步锁
	 */
	private static boolean synRunning = false;
	
	
	/**
	 * 排序字段
	 */
	public static String orderby = "time";
	
	/**
	 * 个人加星列表,内容为图片ID
	 */
	static final ArrayList<String> myStarList = new ArrayList<String>(50);
	
	/**
	 * 0为顺序，非0为倒序
	 */
	static int orderAsc = 0;
	
	static final int RESULT_OK = 10; 
	
	private ProgressDialog dialog ;
//	/**
//	 * 远程图片路径的前缀
//	 */
//	private static String remotePrePath = "http://202.102.113.204/orion/";
	
	/**
	 * 同步星星的URL
	 */
	private static String synStarPath = "getstars.htm";
	
	static boolean isInited = false;
	static boolean isShuffle = false;
	static final String settings_inited = "init";
	static final String settings_cate = "cate";
	static final String settings_page = "page";
	static final String settings_asc = "asc";
	static final String settings_orderby = "orderby";
	static final String settings_shuffle = "shuffle";
	
	/**
	 * 屏高
	 */
	private static int screenH;
	
	/**
	 * 消息处理Handler,用于更新界面
	 */
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			/*
			switch (msg.what) {
			case MSG_LOAD_SMALL:
				for (int i = 0; i < 4; i++) {
					imgs[i].setImageBitmap(loadedBmp[i]);
					imgs[i].setScaleType(ImageView.ScaleType.CENTER);
					loader[i].setVisibility(View.GONE);
					imgs[i].setVisibility(View.VISIBLE);
				}
				break;
			case MSG_LOAD_ERR:
				Log.e(TAG, "load small error!");
				break;

			default:
				break;
			}
	*/
			if (msg.what >= 10 && msg.what <20) {
				int posi = msg.what - 10;
//				imgs[posi].setImageBitmap(loadedBmp[posi]);
//				imgs[posi].setScaleType(ImageView.ScaleType.CENTER);
//				loader[posi].setVisibility(View.GONE);
//				imgs[posi].setVisibility(View.VISIBLE);
				loader[posi].setImageBitmap(loadedBmp[posi]);
				loader[posi].setScaleType(ImageView.ScaleType.CENTER);
				return;
			}
			
			/*
			 * 在使用多线程载入四图时应将此注释打开
			else if (msg.what >= 20) {
				int posi = msg.what - 20;
				Log.e(TAG, "load small error:"+posi);
				return;
			}
			*/
			
			switch (msg.what) {
			case MSG_EXIT:
				showDialog(MSG_EXIT);
				break;
			case MSG_LOAD_ERR:
				
				Toast.makeText(K99KWall.this,getString(R.string.loadPicError),Toast.LENGTH_SHORT).show();
				break;
			case MSG_LOAD_COMPLET:
				initBtJson();
				loadAdMob();
				break;
			case MSG_SYN:
				new SynMyStars(synStarPath).start();
				break;
			case MSG_SYNOK:
				//转到我的星星第一页
				updateStarPage(1);
				break;
			case MSG_SYNERR:
				Toast.makeText(K99KWall.this, R.string.err_synstar, Toast.LENGTH_SHORT);
			default:
				break;
			}
			
			
		}
	};

	/**
	 * 初始化btjson
	 */
	private final void initBtJson(){
		try {
			JSONArray arr =  btjson.getJSONArray("show2");
			int len = arr.length();
			for (int i = 0; i < len; i++) {
				JSONObject o = arr.getJSONObject(i);
				if (o.getString("type").equals("order") && o.getString("sort").equals(orderby)) {
					o.put("ison", true);
					o.put("asc", orderAsc);
					Log.e(TAG, "orderAsc:"+orderAsc);
//					if (orderAsc == 0) {
//						o.put("asc", 1);
//					}else{
//						o.put("asc", 0);
//					}
					
				}
				
			}
		} catch (JSONException e) {
			Log.e(TAG, "btjson error.",e);
		}
	}
	private void showIndexList(){
		listView.setVisibility(View.VISIBLE);
		//bottom2.setVisibility(View.VISIBLE);
		centerScrollView.setVisibility(View.GONE);
		bottomView.setVisibility(View.GONE);
	}
	private void showPicList(){
		listView.setVisibility(View.GONE);
		//bottom2.setVisibility(View.GONE);
		centerScrollView.setVisibility(View.VISIBLE);
		//centerScrollView.scrollTo(0, 0);
		bottomView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 读取配置
	 */
	final void readSettings(){
    	settings = PreferenceManager.getDefaultSharedPreferences(this);
    	isInited = settings.getBoolean(settings_inited, false);
    	jsonCate = settings.getInt(settings_cate, 0);
    	jsonPage = settings.getInt(settings_page, 1);
    	orderby = settings.getString(settings_orderby, "time");
    	orderAsc =  settings.getInt(settings_asc, 0);
    	isShuffle = settings.getBoolean(settings_shuffle, false);
    	if (orderby.equals("random")) {
			isCateRandom = true;
		}
		Log.d(TAG, ".....[readSettings]....jsonCate:"+jsonCate+" jsonPage:"+jsonPage+" orderAsc:"+orderAsc+" orderby:"+orderby);

	}
	
	/**
	 * 保存配置
	 */
	 final void saveSettings(){
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(settings_inited, true);
		isInited = true;
		editor.putInt(settings_cate, jsonCate);
		editor.putInt(settings_page, jsonPage);
		editor.putString(settings_orderby, orderby);
		editor.putInt(settings_asc,orderAsc);
		editor.putBoolean(settings_shuffle, (jsonCate<0)?true:false);
		editor.commit();
	 }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.setTitle(R.string.title);
     	jsonStr = this.getIntent().getStringExtra("jsonRoot");
 		if (jsonStr == null || jsonStr.length() < 1) {
 			//参数错误,提示
 			Log.e(TAG, "no para data.");
 			showDialog(MSG_JSON_ERR);
 		}
 		try {
 			json = new JSONObject(jsonStr);
 			//Log.e(TAG, "JSONSTR:"+jsonStr);
 			//这里使用index.json中的server组
 			JSONArray jarr =  json.getJSONArray("servers");
 			int len = jarr.length();
 			String[] servers = new String[len];
 			for (int i = 0; i < len; i++) {
 				servers[i] = jarr.getString(i);
			}
 			NetWork.setServers(servers);
 			
 			//remotePrePath = servers[0];//json.getString("server");
 			//星星菜单
 			btjson = json.getJSONObject("starmenu");
 			//synStarPath = remotePrePath+"getstars.htm";
 		} catch (JSONException e) {
 			Log.e(TAG, "json parse error.",e);
 			showDialog(MSG_JSON_ERR);
 		}
 		
        b_new = (Button)this.findViewById(R.id.b_new);
        b_cate = (Button)this.findViewById(R.id.b_cate);
        b_next = (Button)this.findViewById(R.id.b_next);
        b_random = (Button)this.findViewById(R.id.b_random);
        b_pre = (Button)this.findViewById(R.id.b_pre);
        b_star = (Button)this.findViewById(R.id.b_star);
        loader[0] = (ImageView)this.findViewById(R.id.loader1);
        loader[1] = (ImageView)this.findViewById(R.id.loader2);
        loader[2] = (ImageView)this.findViewById(R.id.loader3);
        loader[3] = (ImageView)this.findViewById(R.id.loader4);
        bottomView = this.findViewById(R.id.Bottom);
        centerScrollView = (ScrollView)this.findViewById(R.id.CenterScroll);
        listView = (ListView)this.findViewById(R.id.indexList);
        pagenView =  (TextView) this.findViewById(R.id.page_num);
        b_new.setText(getString(R.string.bt_new));
        b_cate.setText(getString(R.string.bt_cate));
        b_next.setText(getString(R.string.bt_next));
        b_pre.setText(getString(R.string.bt_pre));
        b_random.setText(getString(R.string.bt_random));
        
        pagenView.setText("["+jsonPage+"]");
		try {
			//获取索引分类数组,去掉new,所以-1
			JSONArray cateJsonArr = json.getJSONArray("index");
			int len = cateJsonArr.length();
			cateArr = new String[len-1];
			for (int i = 1; i < len; i++) {
				cateArr[i-1] = cateJsonArr.getJSONObject(i).getString("cate"+ID.getLANG());
			}
			String ad = json.getString("adkey");
			if (ad!=null && ad.length()>2) {
				adkey = ad;
			}
		} catch (JSONException e) {
			Log.e(TAG, "json error!",e);
		} catch (Exception e) {
			Log.e(TAG, "json error!",e);
		}
		for (int i = 0; i < 4; i++) {
        	loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading));
        	loader[i].setScaleType(ImageView.ScaleType.CENTER);
		}
		listView.setAdapter(new ArrayAdapter<String>(this,R.layout.showindex,cateArr));
        
        listView.setOnItemClickListener(new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0,  View v, int position, long id) {
                //list项目点击
            	showPicList();
            	if (isCateRandom) {
					updatePageCateRandom(position+1);
				}else{
					updatePage(position+1,1);
				}
				
            }  
        }); 
        
        //最新
        b_new.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				showPicList();
//				jsonCate = 1;
				orderAsc = 0;
				jsonPage = 1;
				updatePageNew(1);
				//Log.d(TAG, "=====The new's page:" + jsonPage);
			}
        });
        //随机
        b_random.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				showPicList();
				updatePageRandom();
			}
        });        
        //下一个
        b_next.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if (jsonCate == 0) {
					updatePageNew(jsonPage + 1);
				}else if(jsonCate == -1){
					updatePageRandom();
				}else if(jsonCate == -2){
					updateStarPage(jsonPage + 1);
				}else{
					if (isCateRandom) {
						updatePageCateRandom(jsonCate);
					}else{
						if (updatePage(jsonCate,jsonPage + 1)) {
							//jsonPage = jsonPage + 1;
						}
					}
				}
				
				
			}
        });
        //前一个
        b_pre.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if (jsonCate == 0) {
					updatePageNew(jsonPage - 1);
				}else if(jsonCate == -1){
					updatePageRandom();
				}else if(jsonCate == -2){
					updateStarPage(jsonPage - 1);
				}else{
					if (isCateRandom) {
						updatePageCateRandom(jsonCate);
					}else{
						if (updatePage(jsonCate,jsonPage - 1)) {
							//jsonPage = jsonPage - 1;
						}
					}
					
				}
				
			}
        });
        
        b_cate.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				showIndexList();
			}
        });
        b_star.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Intent intent = new Intent();
				//1为显示类别的状态，2为显示四图的状态
				int s = 1;
				if (K99KWall.this.listView.getVisibility() == View.GONE && jsonCate >0) {
					s = 2;
				}
				intent.putExtra("star", s);
				intent.putExtra("isShowOrder", true);
				//intent.putExtra("btjson", btjson.toString());
				intent.setClass(K99KWall.this, StarActivity.class);
				//startActivity(intent);
				startActivityForResult(intent,RESULT_OK);
			}
        });
        DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenH = dm.heightPixels;
		if (screenH < 500) {
			LinearLayout bottom = (LinearLayout) this.findViewById(R.id.pageCtrl);
			bottom.setLayoutParams(BOTTOM_40);
		}
		//this.updatePage(jsonPage);
		mHandler.sendEmptyMessageDelayed(MSG_LOAD_COMPLET, 500);
		mHandler.sendEmptyMessageDelayed(MSG_SYN, 1000);
		readSettings();
		jsonCate = 0;
		//如果是第一次启动，则保存设置
		if (!isInited) {
			saveSettings();
		}
	
		
    }
    
    
    /* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			String type = data.getStringExtra("type");
			if (type.equals("order")) {
				
//				starBtOn = data.getIntExtra("btn",starBtOn);
//				orderby = data.getStringExtra("orderby");
//				orderAsc = data.getIntExtra("orderAsc",0);
				showPicList();
				isCateRandom = false;
				if (orderby.equals("random")) {
					isCateRandom = true;
					this.updatePageCateRandom(jsonCate);
					updateBtJson("show2","random",orderAsc);
				}else if(orderby.equals("shuffle")){
					this.updatePageRandom();
					//更新btjson
					updateBtJson("show1","shuffle",orderAsc);
				}else if(orderby.equals("last")){
					readSettings();
					this.show4Pics();
					//更新btjson
					updateBtJson("show1","last",orderAsc);
				}else{
					jsonPage = 1;
					this.show4Pics();
					//更新btjson
					updateBtJson("show2",orderby,orderAsc);
				}
				
			}else if(type.equals("mystars")){
				if (hasSynStar) {
					Log.d(TAG, "hasSynStar:"+hasSynStar);
					this.updateStarPage(1);
				}else{
					Log.d(TAG, "hasSynStar:"+hasSynStar+" showProc:"+showProc);
					//显示我的星星
					if (!showProc) {
						dialog = ProgressDialog.show(this, getString(R.string.proc_diag_title), getString(R.string.proc_diag_text));
						showProc = true;
						if (!hasSynStar) {
							new SynMyStars(synStarPath).start();
						}
					}
				}
				if (dialog!=null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
			
			break;

		default:
			break;
		}
		
	}
	
	static boolean orderbyChanged = true;
	
	private final void updateBtJson(String showType,String sort,int asc){
		//更新btjson
		try {
			//Log.d(TAG, "updateBtJson showType:"+showType+" sort:"+sort+" asc:"+asc+" orderby:"+orderby);
			JSONArray arr = btjson.getJSONArray(showType);
			for (int i = 0; i <arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				//只对type为order的节点处理
				if (o.getString("type").equals("order") ) {
					if (o.getString("sort").equals(sort)) {
						o.put("ison", true);
						Log.e(TAG, "");
						if (!orderbyChanged) {
							//反置ascz
							if (asc == 0) {
								asc = 1;
							}else{
								asc = 0;
							}
						}
						orderAsc = asc;
						o.put("asc", asc);
					}else{
						o.put("ison", false);
					}
				}
			}
			//Log.d(TAG, "updateBtJson done! showType:"+showType+" sort:"+sort+" asc:"+asc+" orderby:"+orderby);
			
		} catch (JSONException e) {
			Log.e(TAG, "btjson:check ison position error:"+showType+" sort:"+sort, e);
		}
	}
	/**
     * 显示四张图
     */
    public final void show4Pics(){
    	if (isCateRandom) {
			updatePageCateRandom(jsonCate);
			return;
		}
    	if (jsonCate == 0) {
			updatePageNew(jsonPage);
		}else if(jsonCate == -1){
			updatePageRandom();
		}else if(jsonCate == -2){
			updateStarPage(jsonPage);
		}else{
			updatePage(jsonCate,jsonPage);
		}
    	Log.d(TAG, "orderby:"+orderby+" orderAsc:"+orderAsc+" jsonPage:"+jsonPage+" jsonCate:"+jsonCate);
    }
   
    private void loadAdMob(){
        //=======================ADMOB====================
    	if (ID.getLANG().equals("CN")) {
    		WoobooAdView ad = new WoobooAdView(this,Color.argb(255, 61, 31, 51),
    				Color.argb(255, 204, 204, 204), false, 28);
    		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
    				LayoutParams.WRAP_CONTENT);
    		ad.setLayoutParams(params);
            ((LinearLayout)this.findViewById(R.id.admob1)).addView(ad);
		}else{
			com.admob.android.ads.AdView admob2 = new AdView(K99KWall.this);
	        admob2.setBackgroundColor(Color.argb(255, 61, 31, 51));
	        admob2.setPrimaryTextColor(Color.argb(255, 204, 204, 204));
	        admob2.setRequestInterval(30);
	        admob2.setKeywords(adkey);
	        admob2.setLayoutParams(LP_FW);
	        ((LinearLayout)this.findViewById(R.id.admob1)).addView(admob2);
		}
    }
  
    //private static final RelativeLayout.LayoutParams LP_FF = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);  
    private static final RelativeLayout.LayoutParams LP_FW = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);  
    private static final LinearLayout.LayoutParams BOTTOM_40 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 38);  
    //private RelativeLayout.LayoutParams LP_WW = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(0,MENU_INIT,0,"刷新索引");  
        menu.add(0,MENU_ABOUT,0,getString(R.string.menu_about));  
        menu.add(0,MENU_HELP,1,getString(R.string.menu_help));  
        menu.add(0,MENU_EXIT,2,getString(R.string.menu_exit));  
        return super.onCreateOptionsMenu(menu);
        
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& e.getAction() == KeyEvent.ACTION_UP) {
			if (this.listView.getVisibility() == View.GONE) {
				this.showIndexList();
			}else{
				showDialog(MSG_EXIT);
			}
			return true;
		}
		return false;
    	//return super.dispatchKeyEvent(event);
    }
    
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
    	switch (item.getItemId()) {
		case MENU_ABOUT:
			showDialog(MENU_ABOUT);
			break;
		case MENU_HELP:
			showDialog(MENU_HELP);
			break;
		case MENU_EXIT:
			finish();
			break;

		default:
			break;
		}
    	return super.onOptionsItemSelected(item); 
    }

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case MSG_JSON_ERR:
			 return new AlertDialog.Builder(K99KWall.this)
	         .setMessage(getString(R.string.netError))
	         //.setTitle("载入图片失败,请检查网络连接.")
	         .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	 finish();
	             }
	         })
	         .create();
		case MENU_ABOUT:
			 return new AlertDialog.Builder(K99KWall.this)
	         .setMessage(R.string.about)
	         .setTitle(getString(R.string.menu_about))
	         .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	 
	             }
	         })
	         .create();
		case MENU_HELP:
			return new AlertDialog.Builder(K99KWall.this)
	         .setMessage(R.string.help)
	         .setTitle(getString(R.string.menu_help))
	         .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	 
	             }
	         })
	         .create();
		case MSG_EXIT:
			return new AlertDialog.Builder(K99KWall.this)
	         .setMessage(getString(R.string.msg_exit_ask))
	         .setPositiveButton(getString(R.string.msg_exit), new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	 finish();
	             }
	         })
	         .setNegativeButton(getString(R.string.msg_cancel),new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	             }
	         })
	         .create();

		default:
			break;
		}
		
		return null;//super.onCreateDialog(id);
	}
	
	/**
	 * 产生不为0的随机ini
	 * @param maxIni 最大值
	 * @return
	 */
	private static final int randomIni(int maxIni){
		if (maxIni == 0) {
			maxIni = 100;
		}
		int re = ((int) (Math.random()*100))%maxIni;
		//不能为0,因为json数据格式不一样
		if (re <= 0) {
			re = 1;
		}
		return re;
	}
	
	/**
	 * 显示随机列表
	 * @return 是否产生页变化
	 */
	private boolean updatePageRandom(){
		jsonCate = -1;
		centerScrollView.scrollTo(0, 0);
		pagenView.setText("[R]");
		try {
			int len = json.getJSONArray("index").length();
			
			String[] pres = new String[4];
			Set<String> hasChoose = new HashSet<String>();
			for (int i = 0; i < 4; i++) {
				int cate = randomIni(len);
				int maxPic = json.getJSONArray("index").getJSONObject(cate).getInt("maxPic");
				String picPre = json.getJSONArray("index").getJSONObject(cate).getString("picPre");
				int randomPic = randomIni(maxPic);
				//防止重复
				int maxTry = 3;
				while (hasChoose.contains(picPre+randomPic)) {
					randomPic = randomIni(maxPic);
					if (maxTry <= 0) {
						break;
					}
					maxTry--;
				}
				hasChoose.add(picPre+randomPic);
				Log.d(TAG, "random cate:"+cate+" randomPic:"+randomPic+" picPre:"+picPre +" len:"+len);
				pres[i] = picPre;
				imgPaths[i] = picPre+randomPic+".jpg";
				if (screenH < 500) {
					loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading_l));
				}else{
					loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading));
				}
	        	loader[i].setScaleType(ImageView.ScaleType.CENTER);
			}
			loadNewImgs(pres,imgPaths);
			return true;
			
		} catch (JSONException e) {
			Log.e(TAG, "json error!",e);
		} catch (Exception e) {
			Log.e(TAG, "json error!",e);
		}
		return false;
	}
	
	/**
	 * 按分类显示随机列表
	 * @return 是否产生页变化
	 */
	private boolean updatePageCateRandom(int cate){
		centerScrollView.scrollTo(0, 0);
		pagenView.setText("[R]");
		jsonCate = cate;
		try {
			int len = json.getJSONArray("index").length();
			
			String[] pres = new String[4];
			Set<String> hasChoose = new HashSet<String>();
			for (int i = 0; i < 4; i++) {
				int maxPic = json.getJSONArray("index").getJSONObject(cate).getInt("maxPic");
				String picPre = json.getJSONArray("index").getJSONObject(cate).getString("picPre");
				int randomPic = randomIni(maxPic);
				//防止重复
				int maxTry = 3;
				while (hasChoose.contains(picPre+randomPic)) {
					randomPic = randomIni(maxPic);
					if (maxTry <= 0) {
						break;
					}
					maxTry--;
				}
				hasChoose.add(picPre+randomPic);
				Log.d(TAG, "random cate:"+cate+" randomPic:"+randomPic+" picPre:"+picPre +" len:"+len);
				pres[i] = picPre;
				imgPaths[i] = picPre+randomPic+".jpg";
				if (screenH < 500) {
					loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading_l));
				}else{
					loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading));
				}
	        	loader[i].setScaleType(ImageView.ScaleType.CENTER);
			}
			loadNewImgs(pres,imgPaths);
			return true;
			
		} catch (JSONException e) {
			Log.e(TAG, "json error!",e);
		} catch (Exception e) {
			Log.e(TAG, "json error!",e);
		}
		return false;
	}
	/**
	 * 显示最新列表
	 * @param pageNum 页码
	 * @return 是否产生页变化
	 */
	private boolean updatePageNew(int pageNum){
		jsonCate = 0;
		orderby = "time";
		centerScrollView.scrollTo(0, 0);
		try {
			JSONArray pageArr = json.getJSONArray("index").getJSONObject(jsonCate).getJSONArray("pics");
			
			int maxPage = pageArr.length();
			//只有页码在正确范围内才发生变化
			Log.d(TAG, "maxPage:"+maxPage+" currentPageNum:"+pageNum+" jsonPage:"+jsonPage);
			if (pageNum > maxPage) {
				Toast.makeText(K99KWall.this,getString(R.string.msg_last_page),Toast.LENGTH_SHORT).show();
				return false;
			}else if(pageNum <= 0){
				Toast.makeText(K99KWall.this,getString(R.string.msg_first_page),Toast.LENGTH_SHORT).show();
				return false;
			}else{
				jsonPage = pageNum;
				pagenView.setText("["+jsonPage+"]");
				String[] pres = new String[4];
				JSONArray picArr = pageArr.getJSONArray(pageNum - 1);
				for (int i = 0; i < picArr.length(); i++) {
					String[] picOfNew = picArr.getString(i).split("#");
					pres[i] = picOfNew[0];
					imgPaths[i] = picOfNew[0]+picOfNew[1]+".jpg";
					if (screenH < 500) {
						loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading_l));
					}else{
						loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading));
					}
		        	loader[i].setScaleType(ImageView.ScaleType.CENTER);
				}
				loadNewImgs(pres,imgPaths);
				return true;
			}
		} catch (JSONException e) {
			Log.e(TAG, "json error!",e);
		} catch (Exception e) {
			Log.e(TAG, "json error!",e);
		}
		
		return false;
	}
	
	/**
	 * 显示我的星星
	 * @param pageNum 页码
	 * @param cate 类别
	 * @return 是否产生页变化
	 */
	private boolean updateStarPage(int pageNum){
		showPicList();
		jsonCate = -2;//-2为star类别
		orderby  = "time";
		orderAsc = 0;
		centerScrollView.scrollTo(0, 0);
		
		try {
			int maxPic = myStarList.size();
			//String picPre = json.getJSONArray("index").getJSONObject(jsonCate).getString("picPre");
			if (maxPic == 0) {
				String[] pres = new String[perPageCount];
				for (int i = 0; i < perPageCount; i++) {
					pres[i] = "end";
					imgPaths[i] = "end";
				}
				loadNewImgs(pres,imgPaths);
				Toast.makeText(K99KWall.this,getString(R.string.msg_last_page),Toast.LENGTH_SHORT).show();
				return true;
			}
			//最大页数
			int maxPage = maxPic/perPageCount;
			if (maxPic%perPageCount != 0) {
				maxPage++;
			}
			//只有页码在正确范围内才发生变化
			Log.d(TAG, " maxPic:"+maxPic+" maxPage:"+maxPage+" currentPageNum:"+pageNum+" jsonPage:"+jsonPage);
			if (pageNum > maxPage) {
				Toast.makeText(K99KWall.this,getString(R.string.msg_last_page),Toast.LENGTH_SHORT).show();
				return false;
			}else if(pageNum <= 0){
				Toast.makeText(K99KWall.this,getString(R.string.msg_first_page),Toast.LENGTH_SHORT).show();
				return false;
			}else{
				jsonPage = pageNum;
				pagenView.setText("["+jsonPage+"]");
				int currentPicId = maxPic-(pageNum-1)*perPageCount-1;
				Log.d(TAG, "currentPicId:"+currentPicId);
				String[] pres = new String[perPageCount];
				for (int i = 0; i < perPageCount; i++) {
					if (currentPicId-i < 0) {
						//已经到最后一张，无图可显示了
						pres[i] = "end";
						imgPaths[i] = "end";
					}else{
//						Log.d(TAG, "-------myStarList:"+myStarList.get(currentPicId-i));
						//String[] strarr = myStarList.get(currentPicId-i).split("#");
						//imgPaths[i] = strarr[0]+strarr[1]+".jpg";
						//pres[i] = strarr[0];
						imgPaths[i] = "_"+myStarList.get(currentPicId-i)+".jpg";
						pres[i] = "oid";
					}
					//String s = myStarList.get(currentPicId-i);//cate/cate_id
					//picPre+(currentPicId-i)+".jpg";
					if (screenH < 500) {
						loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading_l));
					}else{
						loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading));
					}
					loader[i].setScaleType(ImageView.ScaleType.CENTER);
				}
				loadNewImgs(pres,imgPaths);
				return true;
			}
		} catch (Exception e) {
			Log.e(TAG, "json error!",e);
		}
		return false;
		
	}
	
	
	/**
	 * 显示某一页
	 * @param pageNum 页码
	 * @param cate 类别
	 * @return 是否产生页变化
	 */
	private boolean updatePage(int cate,int pageNum){
		isCateRandom = false;
		showPicList();
		jsonCate = cate;
		centerScrollView.scrollTo(0, 0);
		
		try {
			int maxPic = json.getJSONArray("index").getJSONObject(jsonCate).getInt("maxPic");
			String picPre = json.getJSONArray("index").getJSONObject(jsonCate).getString("picPre");
			//最大页数
			int maxPage = maxPic/perPageCount;
			if (maxPage < 1) {
				maxPage = 1;
			}
			//只有页码在正确范围内才发生变化
			Log.d(TAG, "maxPage:"+maxPage+" currentPageNum:"+pageNum+" jsonPage:"+jsonPage +" picPre:"+picPre+" orderAsc:"+orderAsc);
			if (pageNum > maxPage) {
				Toast.makeText(K99KWall.this,getString(R.string.msg_last_page),Toast.LENGTH_SHORT).show();
				return false;
			}else if(pageNum <= 0){
				Toast.makeText(K99KWall.this,getString(R.string.msg_first_page),Toast.LENGTH_SHORT).show();
				return false;
			}else{
				jsonPage = pageNum;
				pagenView.setText("["+jsonPage+"]");
				int currentPicId = maxPic-(pageNum-1)*perPageCount;
				for (int i = 0; i < perPageCount; i++) {
					imgPaths[i] = picPre+(currentPicId-i)+".jpg";
					if (screenH < 500) {
						loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading_l));
					}else{
						loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loading));
					}
					loader[i].setScaleType(ImageView.ScaleType.CENTER);
				}
				loadImgs(picPre,imgPaths);
				return true;
			}
		} catch (JSONException e) {
			Log.e(TAG, "json error!",e);
		} catch (Exception e) {
			Log.e(TAG, "json error!",e);
		}
		return false;
		
	}

	/**
	 * 载入四张图片
	 * @param picPre 图片路径
	 * @param paths 图片名
	 */
	private final void loadImgs(String picPre,String[] paths){
		String[] remotePaths = new String[4];
		String pre = picPre+"/";
		for (int i = 0; i < paths.length; i++) {
			//结束图片直接显示
			if (paths[i].equals("end")) {
				if (screenH < 500) {
					loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loadingend_l));
				}else{
					loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loadingend));
				}
				remotePaths[i] = "end";
				continue;
			}else{
				remotePaths[i] = pre + paths[i];
				this.setImgLink(loader[i], pre,"b_" + paths[i]);
			}
			
		}
		this.saveSettings();
		//单线程载入四张图
		new LoadPicsOnce(remotePaths).start();
		
	}
	
	/**
	 * 载入"最新"四张图片
	 * @param pres 图片路径
	 * @param paths 图片名
	 */
	private final void loadNewImgs(String[] pres,String[] paths){
		String[] remotePaths = new String[4];
		for (int i = 0; i < 4; i++) {
			//结束图片直接显示
			if (paths[i].equals("end")) {
				if (screenH < 500) {
					loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loadingend_l));
				}else{
					loader[i].setImageDrawable(getResources().getDrawable(R.drawable.loadingend));
				}
				remotePaths[i] = "end";
				continue;
			}else{
				remotePaths[i] = pres[i]+"/" + paths[i];
				this.setImgLink(loader[i], pres[i]+"/","b_" + paths[i]);
			}
			
		}
		this.saveSettings();
		//单线程载入四张图
		new LoadPicsOnce(remotePaths).start();
	}

	/**
	     * 设置图片的链接传送参数
	     * @param iv ImageView
	     * @param imgId int
	     */
	private final void setImgLink(ImageView iv,final String server,final String bigImg){
	    	iv.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		Intent toPic = new Intent();
        		toPic.putExtra("bigImg", bigImg);
        		toPic.putExtra("server", server);
        		Log.d(TAG, "bigImg:"+bigImg+" server:"+server);
        		try {
					toPic.putExtra("saveSdPath", json.getString("saveSdPath"));
				} catch (JSONException e) {
					Log.e(TAG, "json saveSdPath parse error.",e);
				}
        		Log.d(TAG, "setImgLink -- bigImg:"+bigImg);
        		toPic.setClass(K99KWall.this, ShowPic.class);
        		startActivity(toPic);
        	}
        });
	}
	
	class SynMyStars extends Thread{

		public SynMyStars(String url) {
			this.url = url;
		}
		private String url;
		
		boolean syn(){
			synRunning = true;
			
			try {
				/*URL aURL = new URL(url);
				URLConnection conn = aURL.openConnection();
				conn.setRequestProperty("appVersion", appver+"");
				conn.setRequestProperty("imei", imei);
				conn.setRequestProperty("lang", lang);
				conn.connect();
				StringBuilder b = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
				String line;
				while ((line = reader.readLine()) != null) {
					b.append(line);
				}
				reader.close();*/
				String str = NetWork.postUrl(url, ID.getSmallJsonEnc());
				if (str.length() == 0) {
					Log.d(TAG, "load syn get empty." + url);
					str = "[]";
				}
				Log.d(TAG, "load syn string OK:" + url);
				//Log.d(TAG, "str:" + str);
				JSONArray arr = new JSONArray(str);
				myStarList.clear();
				for (int i = 0; i < arr.length(); i++) {
					myStarList.add(arr.getString(i));
				}
				Log.d(TAG, "syn star item:" + myStarList.size());
				
			}catch (Exception e) {
				Log.e(TAG, "syn star unknown Error!"+url, e);
				return false;
			}
			
			return true;
			
		}
		
		@Override
		public void run() {
			if (!synRunning) {
				boolean re =  this.syn();
				synRunning = false;
				if (re) {
					if ((!hasSynStar) && showProc) {
						K99KWall.this.dialog.dismiss();
						showProc = false;
					}
					hasSynStar = true;
					Log.d(TAG, "syn star OK!"+url);
					if (jsonCate == -2) {
						mHandler.sendEmptyMessage(MSG_SYNOK);
					}
					
				}else{
					mHandler.sendEmptyMessage(MSG_SYNERR);
				}
			}
			
		}
		
	}

	/**
	 * 单线程载入四张图
	 * @author keel
	 *
	 */
	private class LoadPicsOnce extends Thread{
		public LoadPicsOnce(String[] path) {
			this.paths = path;
		}
		private String[] paths;
		@Override
    	public void run() {
			boolean loadOK = true;
			for (int i = 0; i < 4; i++) {
				//结束图不处理
				if (this.paths[i].equals("end")) {
					continue;
				}
				//获取远程图片
				Bitmap b = NetWork.getRemotePicWithWallProp(this.paths[i],null);//K99KWall.loadedBmp[i];
				if (b!=null && b.getWidth()>0) {
				
					K99KWall.loadedBmp[i] = (screenH<500) ? b : IO.resizePic(b,337);
					//Log.d(TAG, "screenH:"+screenH+"small:"+i+" w:"+K99KWall.loadedBmp[i].getWidth()+" h:"+K99KWall.loadedBmp[i].getHeight());
					mHandler.sendEmptyMessage(MSG_LOAD_SMALL+i);
				}else{
					loadOK = false;
				}
			}
			
			Log.d(TAG, "-------jsonCate:"+jsonCate+" jsonPage:"+jsonPage+" orderAsc:"+orderAsc+" orderby:"+orderby+" isCateRandom:"+isCateRandom);
//			for (int i = 0; i < 4; i++) {
//				Bitmap b = K99KWall.loadedBmp[i];
//				if (b!=null && b.getWidth()>0) {
//					Log.d(TAG, "small:"+i+" w:"+K99KWall.loadedBmp[i].getWidth()+" h:"+K99KWall.loadedBmp[i].getHeight());
//					mHandler.sendEmptyMessage(MSG_LOAD_SMALL+i);
//				}else{
//					loadOK = false;
//				}
//			}
			
			if (!loadOK) {
				mHandler.sendEmptyMessage(MSG_LOAD_ERR);
			}
		}
	}
	
//	private static final Bitmap resizePic(Bitmap b,int maxHeight){
//		int orgWidth = b.getWidth();
//		int orgHeight = b.getHeight();
////		Log.d(TAG, "maxHeight:"+maxHeight+" orgHeight:"+orgHeight);
//		float scale = ((float)maxHeight)/((float)orgHeight);
////		float toWidth = (orgWidth*scale);
////		float toHeight = (orgHeight*scale);
////		Log.d(TAG, "scale:"+scale);
////		Log.d(TAG, "toWidth:"+toWidth);
////		Log.d(TAG, "toHeight:"+toHeight);
//		
//		Matrix matrix = new Matrix();
//		matrix.postScale(scale, scale);
//		Bitmap newBmp = Bitmap.createBitmap(b,0,0,orgWidth,orgHeight,matrix,true);
////		Log.d(TAG, "newHeight:"+newBmp.getHeight());
////		Log.d(TAG, "newWidth:"+newBmp.getWidth());
//		return newBmp;
//	}
	
 
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		// 解决横竖屏切换导致重载的问题
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){  
		    //横向  
		    //setContentView(R.layout.file_list_landscape); 
			//Log.i(TAG,"to land...");
			//fitSize();
		}else{  
		    //竖向  
		    //setContentView(R.layout.file_list);  
			//Log.i(TAG,"to port...");
			//fitSize();
		}  
	}
	
	
}