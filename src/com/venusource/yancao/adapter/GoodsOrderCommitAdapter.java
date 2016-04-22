package com.venusource.yancao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.venusource.yancao.MainActivity;
import com.venusource.yancao.R;
import com.venusource.yancao.javabean.Goods;


public class GoodsOrderCommitAdapter extends BaseAdapter {
	
	private List<Goods> list;
	private Context context;	
	
	public GoodsOrderCommitAdapter(Context context,List<Goods> list) {	
		int count = 0;
		int price = 0;
		this.context = context;
		this.list = new ArrayList<Goods>();
		Goods good = new Goods();
		if (list!=null && list.size() > 0) {
			for (Goods gd : list) {
				this.list.add(gd);
				count += gd.getCount();
				price += (Integer.valueOf(gd.getPrice()) * gd.getCount());
			}
		}
		good = new Goods();
		good.setGood_name("总计:");
		good.setCount(count);
		good.setPrice(price + "");
		this.list.add(good);
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

	@SuppressLint("NewApi") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {		
		View view = convertView;
		final ViewOrderCommitGoods viewholder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.order_goods_commit_listview, null);
			viewholder = new ViewOrderCommitGoods();
			viewholder.goods_name = (TextView) view.findViewById(R.id.order_goods_commit_des);
			viewholder.goods_acount = (TextView) view.findViewById(R.id.order_goods_commit_num);
			viewholder.goods_price = (TextView) view.findViewById(R.id.order_goods_commit_price);
		
			view.setTag(viewholder);
			
			
		} else {
			viewholder = (ViewOrderCommitGoods) view.getTag();
		}
		
		int w = MainActivity.screenWidth/6;
		
		viewholder.goods_name.setWidth(w*2);
		viewholder.goods_acount.setWidth(80);
		viewholder.goods_price.setWidth(w*1);
		
		viewholder.goods_name.setText(list.get(position).getGood_name());		
		viewholder.goods_acount.setText(list.get(position).getCount() + "");		
		viewholder.goods_price.setText(list.get(position).getPrice() + "元");		
		
		
		return view;
	}
	
	
	
	class ViewOrderCommitGoods {
		TextView goods_name;		
		TextView goods_acount;
		TextView goods_price;
	}

}
