/**
 * 
 */
package com.k99k.keel.wallpaper;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.k99k.keel.wallpaper.R;

/**
 * @author keel
 *
 */
public class StarActivity extends Activity {

	/**
	 * 
	 */
	public StarActivity() {
	}

	private static final String TAG = "StarActivity";
	
	/**
	 * 显示的状态,1为显示类别的状态，2为显示四图的状态,3为显示大图的状态,4为加上按星排序的类别状态
	 */
	private int showType = 1;
	
	
	private LinearLayout bglayout;
	
//	private JSONObject btJson;
	
//private String jsonString;
	
//	private LayoutParams params = new LayoutParams(200,LayoutParams.WRAP_CONTENT);

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.stardialog);
		bglayout = ((LinearLayout)this.findViewById(R.id.starlayout));
		showType = getIntent().getIntExtra("star",1);
		boolean isShowOrder = this.getIntent().getBooleanExtra("isShowOrder", true);
		//根据JSON显示
		String type = "show"+this.showType;
		try {
			JSONArray arr = K99KWall.btjson.getJSONArray(type);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = (JSONObject) arr.get(i);
				//如有noshow元素，表示此按钮不显示
				if (o.has("noshow")) {
					continue;
				}
				String t = o.getString("type");
				if (t.equals("title")) {
					if ((!isShowOrder)&&o.getString("txt").equals("Order by:")) {
					}else{
						this.createTitle(o.getString("txt"+ID.getLANG()));
					}
					continue;
				}
				if (t.equals("order")) {
					
					if (isShowOrder ) {
						boolean ison = o.getBoolean("ison");
						String orderby = o.getString("sort");
						int orderAsc = o.getInt("asc");//K99KWall.orderAsc;
						String asc = "";
						String c = "";
						//K99KWall.starBtOn == i
						if (K99KWall.jsonCate > 0 && type.equals("show2")) {
							if (ison) {
								//显示选中状态
								if (!orderby.equals("random")) {
									//Log.e(TAG, "asc:"+orderAsc);
									asc = (orderAsc == 0)?" ↓":" ↑";
								}
							}
							c =" ("+K99KWall.cateArr[K99KWall.jsonCate-1]+") ";
						}
						this.createRadioButton(o.getString("txt"+ID.getLANG())+asc+c, ison,orderby,orderAsc,i);
					}
					continue;
				}
				if (t.equals("mystars")) {
					this.createArrowButton(o.getString("txt"+ID.getLANG()),t);
					continue;
				}
				if (t.equals("addstar")) {
					String isAdd = this.getIntent().getStringExtra("isAdd");
					String[] txarr = o.getString("txt"+ID.getLANG()).split("#");
					if (isAdd!= null && isAdd.equals("n")) {
						//显示减星
						this.createArrowButton(txarr[1],"del");
					}else{
						//显示加星
						this.createArrowButton(txarr[0],"add");
					}
				}
			}
			this.createCloseButton(K99KWall.btjson.getJSONObject("close").getString("txt"+ID.getLANG()));
		} catch (Exception e) {
			Log.e(TAG, "StarActivity show bts error!",e);
		}
	}

	private void createRadioButton(String btTxt,boolean isChoosen,final String orderby,final int orderAsc,final int position){
		final LayoutInflater layoutInflater =
            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout bt_star = (LinearLayout) layoutInflater.inflate(R.layout.starbtview, null);
		TextView text = (TextView) bt_star.findViewById(R.id.bttxt);
		ImageView img = (ImageView)bt_star.findViewById(R.id.btdot);
		if (!isChoosen) {
			img.setImageResource(R.drawable.walldotb);
		}
		text.setText(btTxt);
		bt_star.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				if (K99KWall.orderby.equals(orderby)) {
					K99KWall.orderbyChanged = false;
				}else{
					K99KWall.orderbyChanged = true;
				}
				K99KWall.orderby = orderby;
				K99KWall.orderAsc = orderAsc;
				Log.d(TAG, "orderby:"+orderby+" orderAsc:"+orderAsc);
				Intent i = new Intent();
//				i.putExtra("orderby", orderby);
//				i.putExtra("orderAsc", orderAsc);
				//i.putExtra("btn", position);
				i.putExtra("type", "order");
				setResult(K99KWall.RESULT_OK, i);
				finish();
			}
		});
		
		bglayout.addView(bt_star);
		setMargin(bt_star);
	}
	
	/**
	 * 设置margin,必须的addView之后执行方有效
	 * @param view
	 */
	private final void setMargin(View view){
		MarginLayoutParams margin = new MarginLayoutParams(view.getLayoutParams());
		margin.setMargins(0, 0,0, 2);
		view.setLayoutParams(new LinearLayout.LayoutParams(margin));
	}
	
	private void createArrowButton(String btTxt,final String type){
		final LayoutInflater layoutInflater =
            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View bt_star = layoutInflater.inflate(R.layout.starbtview, null);
		TextView text = (TextView) bt_star.findViewById(R.id.bttxt);
		ImageView img = (ImageView)bt_star.findViewById(R.id.btdot);
		img.setImageResource(R.drawable.walldotr);
		text.setText(btTxt);
		bt_star.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				//type:浏览星星,加星或减星
				Log.d(TAG, "type:"+type);
				Intent i = new Intent();
				i.putExtra("type", type);
				setResult(K99KWall.RESULT_OK, i);
				finish();
			}
		});
		bglayout.addView(bt_star);
		setMargin(bt_star);
	}
	
	private void createTitle(String titleTxt){
		final LayoutInflater layoutInflater =
            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View bt_star = layoutInflater.inflate(R.layout.startitle, null);
		TextView text = (TextView) bt_star.findViewById(R.id.title1);
		text.setText(titleTxt);
		bglayout.addView(bt_star);
		setMargin(bt_star);
	}
	
	private void createCloseButton(String btTxt){
		final LayoutInflater layoutInflater =
            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View bt_star = layoutInflater.inflate(R.layout.starbtclose, null);
		TextView text = (TextView) bt_star.findViewById(R.id.bttxt);
		text.setText(btTxt);
		bt_star.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				finish();
			}
		});
		
		bglayout.addView(bt_star);
		setMargin(bt_star);
	}
	
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& e.getAction() == KeyEvent.ACTION_UP) {
			finish();
			return true;
		}
		return false;
    	//return super.dispatchKeyEvent(event);
    }

}
