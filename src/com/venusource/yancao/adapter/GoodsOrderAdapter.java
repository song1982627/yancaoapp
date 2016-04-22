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


public class GoodsOrderAdapter extends BaseAdapter {
	

	private List<Goods> list;
	private Context context;	
	private TextView orderGoodsTotal;
	private int buyNum = 0;// 购买数量
	private int totalPrice=0;//总价

	public List<Goods> getList() {
		return list;
	}

	public void setList(List<Goods> list) {
		this.list = list;
	}

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

	public GoodsOrderAdapter(Context context,List<Goods> list,TextView orderGoodsTotal) {	
		this.context = context;
		this.list = list;	
		this.orderGoodsTotal = orderGoodsTotal;
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
		final ViewOrderGoods viewholder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.order_goods_listview, null);
			viewholder = new ViewOrderGoods();
			viewholder.tv_name = (TextView) view.findViewById(R.id.order_goods_name);
			viewholder.iv_add = (ImageView) view.findViewById(R.id.order_goods_add);
			viewholder.iv_remove = (ImageView) view.findViewById(R.id.order_goods_remove);
			viewholder.et_acount = (EditText) view.findViewById(R.id.order_goods_count);
			view.setTag(viewholder);
			
		} else {
			viewholder = (ViewOrderGoods) view.getTag();
		}
		viewholder.tv_name.setText(list.get(position).getGood_name());
		viewholder.iv_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = list.get(position).getCount();
				count++;
				totalPrice += Integer.valueOf(list.get(position).getPrice());
				orderGoodsTotal.setText("总价:" + totalPrice + "元");
				list.get(position).setCount(count);
				viewholder.et_acount.setVisibility(View.VISIBLE);
				viewholder.iv_remove.setVisibility(View.VISIBLE);
				viewholder.et_acount
						.setText(list.get(position).getCount() + "");
			}
		});

		viewholder.iv_remove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = list.get(position).getCount();
				count--;
				totalPrice -= Integer.valueOf(list.get(position).getPrice());
				orderGoodsTotal.setText("总价:" + totalPrice + "元");
				list.get(position).setCount(count);
				viewholder.et_acount
						.setText(list.get(position).getCount() + "");
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

	class ViewOrderGoods {
		TextView tv_name;
		ImageView iv_add, iv_remove;
		EditText et_acount;
	}

}
