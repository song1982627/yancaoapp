package com.venusource.yancao.goods;

import com.venusource.yancao.BadgeView;
import com.venusource.yancao.R;
import com.venusource.yancao.YcApplication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.venusource.yancao.adapter.GoodsAdapter;
import com.venusource.yancao.javabean.Goods;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GoodsTab01 extends Fragment {

	private ListView listView2;

	// private List<Catogray> list;

	
	private GoodsAdapter goodsAdapter;
	private View view;
	private ViewGroup rootView;
	private ImageView shopCart;// 购物车
	private BadgeView buyNumBt;
	private BadgeView buyNumView;// 购物车上的数量标签
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = container;
		view = inflater.inflate(R.layout.main_tab_goods_01, container, false);
		listView2 = (ListView) view.findViewById(R.id.listview_2);
		shopCart = (ImageView) view.findViewById(R.id.iv_add_cart);
		buyNumView = (BadgeView) view.findViewById(R.id.tv_count_price);
		buyNumBt = (BadgeView)view.findViewById(R.id.iv_add_num);
		YcApplication yc = (YcApplication)this.getActivity().getApplication();
		initListData(yc);	
		if (yc.getGoodsNum() > 0) {
			buyNumView.setText("总价:" + yc.getGoodsPrice() + "元");;//
			buyNumView.setBadgePosition(BadgeView.POSITION_CENTER);
			buyNumView.show();
			buyNumBt.setText(yc.getGoodsNum() + "");
		}
		return view;

	}

	private void initListData(YcApplication yc) {
		Map<String,Goods> gs = yc.getGs();
		
		List<Goods> list = new ArrayList<Goods>();
		for (int j = 0; j < 25; j++) {
			String id = "ID-" + j;
			Goods goods = new Goods();
			if (gs.containsKey(id)) {
				goods.setCount(gs.get(id).getCount());
			}
			
			goods.setId(id);
			goods.setGood_name("货品" + j);
			goods.setDescrible("描述" + j);
			goods.setPrice(20 + j + "");
			list.add(goods);
		}
		
		
		goodsAdapter = new GoodsAdapter(view.getContext(), list,rootView,shopCart,buyNumView,buyNumBt,yc);
		
		listView2.setAdapter(goodsAdapter);

	}	
}
