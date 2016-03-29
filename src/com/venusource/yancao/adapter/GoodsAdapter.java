package com.venusource.yancao.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.venusource.yancao.BadgeView;
import com.venusource.yancao.R;
import com.venusource.yancao.YcApplication;
import com.venusource.yancao.javabean.Goods;


public class GoodsAdapter extends BaseAdapter {

	private List<Goods> list;
	private Context context;
	private ImageView shopCart;// 购物车
	private ViewGroup anim_mask_layout;// 动画层	
	private BadgeView buyNumView;// 购物车上的数量标签
	private BadgeView buyNumBt;
	private ViewGroup rootView;
	private YcApplication yc;
	
	private int buyNum = 0;// 购买数量
	private int totalPrice=0;//总价

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public GoodsAdapter(Context context, List<Goods> list, ViewGroup rootView,
			ImageView shopCart, BadgeView buyNumView,BadgeView buyNumBt,YcApplication yc) {
		this.context = context;
		this.list = list;
		this.rootView = rootView;
		this.shopCart = shopCart;
		this.buyNumView = buyNumView;
		this.buyNumBt = buyNumBt;
		this.yc = yc;
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final Viewholder viewholder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.right_listview, null);
			viewholder = new Viewholder();
			viewholder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewholder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
			viewholder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewholder.iv_add = (ImageView) view.findViewById(R.id.iv_add);
			viewholder.iv_remove = (ImageView) view.findViewById(R.id.iv_remove);
			viewholder.et_acount = (EditText) view.findViewById(R.id.et_count);
			view.setTag(viewholder);
			
		} else {
			viewholder = (Viewholder) view.getTag();
		}
		viewholder.tv_name.setText(list.get(position).getGood_name());
		viewholder.tv_desc.setText(list.get(position).getDescrible());
		viewholder.tv_price.setText(list.get(position).getPrice());

		viewholder.iv_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = list.get(position).getCount();
				count++;
				list.get(position).setCount(count);
				yc.getGs().get(position).setCount(count);
				viewholder.et_acount.setVisibility(View.VISIBLE);
				viewholder.iv_remove.setVisibility(View.VISIBLE);
				viewholder.et_acount
						.setText(list.get(position).getCount() + "");
				int[] startLocation = new int[2];
				v.getLocationInWindow(startLocation);
				ImageView ball = new ImageView(context);
				ball.setImageResource(R.drawable.ic_launcher);
				setAnim(ball, startLocation,
						Integer.valueOf(list.get(position).getPrice()));
			}
		});

		viewholder.iv_remove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = list.get(position).getCount();
				count--;
				list.get(position).setCount(count);
				yc.getGs().get(position).setCount(count);
				viewholder.et_acount.setText(list.get(position).getCount() + "");
				buyNum--;// 让购买数量加1
				totalPrice -= Integer.valueOf(list.get(position).getPrice());
				buyNumView.setText("总价:" + totalPrice + "元");
				buyNumView.setBadgePosition(BadgeView.POSITION_CENTER);
				buyNumView.show();
				buyNumBt.setText(buyNum + "");

				if (list.get(position).getCount() <= 0) {
					viewholder.et_acount.setVisibility(View.INVISIBLE);
					viewholder.iv_remove.setVisibility(View.INVISIBLE);
				} else {
					viewholder.et_acount.setVisibility(View.VISIBLE);
					viewholder.iv_remove.setVisibility(View.VISIBLE);
				}

			}
		});
		if (list.get(position).getCount() <= 0) {
			viewholder.et_acount.setVisibility(View.INVISIBLE);
			viewholder.iv_remove.setVisibility(View.INVISIBLE);
		} else {
			viewholder.et_acount.setText(list.get(position).getCount() + "");
			viewholder.et_acount.setVisibility(View.VISIBLE);
			viewholder.iv_remove.setVisibility(View.VISIBLE);
		}
		return view;
	}

	/**
	 * @Description: 创建动画层
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() {

		LinearLayout animLayout = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE - 1);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final ViewGroup parent, final View view,
			int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	public void setAnim(final View v, int[] startLocation,final int price) {

		anim_mask_layout = null;
		anim_mask_layout = createAnimLayout();
		anim_mask_layout.addView(v);// 把动画小球添加到动画层
		final View view = addViewToAnimLayout(anim_mask_layout, v,
				startLocation);
		int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
		shopCart.getLocationInWindow(endLocation);// shopCart是那个购物车

		// 计算位移
		int endX = 0 - startLocation[0] ;// 动画位移的X坐标
		int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
				0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(800);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new Animation.AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				v.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
				buyNum++;// 让购买数量加1
				totalPrice += price;			
				buyNumView.setBadgePosition(BadgeView.POSITION_CENTER);
				buyNumView.show();
				buyNumBt.setText(buyNum+"");
				buyNumView.setText("总价:" + totalPrice + "元");
				
			}
		});

	}

	class Viewholder {
		TextView tv_name;
		TextView tv_desc;
		TextView tv_price;
		ImageView iv_add, iv_remove;
		EditText et_acount;
	}

}
