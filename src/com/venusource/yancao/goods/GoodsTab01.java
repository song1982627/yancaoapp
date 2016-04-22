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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
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

	private ListView listView;

	private View view;
	private ViewGroup rootView;
	private ImageView shopCart;
	private BadgeView buyNumBt;
	private BadgeView buyNumView;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = container;
	
		view = inflater.inflate(R.layout.main_tab_goods_01, container, false);
		listView = (ListView) view.findViewById(R.id.listview_2);
		shopCart = (ImageView) view.findViewById(R.id.iv_add_cart);
		buyNumView = (BadgeView) view.findViewById(R.id.tv_count_price);
		buyNumBt = (BadgeView)view.findViewById(R.id.iv_add_num);
		YcApplication yc = (YcApplication)this.getActivity().getApplication();
		initListData(yc);	
		
		
		Button bt = (Button)view.findViewById(R.id.goods_ok_01);
        
        bt.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				 Intent intent=new Intent();   
                 intent.putExtra("GOODSBACK", "1");                 
                 getActivity().setResult(1, intent);   
                 getActivity().finish();   
 			}	
 		});  
		
		return view;

	}

	private void initListData(YcApplication yc) {
		
		GoodsAdapter goodsAdapter = new GoodsAdapter(view.getContext(), yc.getGs(),rootView,shopCart,buyNumView,buyNumBt,yc);
		
		listView.setAdapter(goodsAdapter);
		
		if (yc.getGs() != null && yc.getGs().size() > 0) {
			int[] total = yc.getTotal();
			buyNumView.setText("总价:" + total[1] + "元");;//
			buyNumView.setBadgePosition(BadgeView.POSITION_CENTER);
			buyNumView.show();
			buyNumBt.setText(total[0] + "");
			goodsAdapter.setBuyNum(total[0]);
			goodsAdapter.setTotalPrice(total[1]);
		}

	}	
}
