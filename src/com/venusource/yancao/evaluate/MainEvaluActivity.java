package com.venusource.yancao.evaluate;

import java.util.ArrayList;
import com.venusource.yancao.javabean.Goods;

import com.venusource.yancao.MainActivity;
import com.venusource.yancao.R;
import com.venusource.yancao.YcApplication;
import com.venusource.yancao.adapter.GoodsOrderEvaluAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainEvaluActivity extends Activity{
	 	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_evalu_main);
	        final YcApplication yc = (YcApplication)this.getApplication();
	        ((TextView)this.findViewById(R.id.evalu_name)).setText("中百仓储");
	        ((TextView)this.findViewById(R.id.evalu_address)).setText("地址：解放大道宝丰路");
	        GoodsOrderEvaluAdapter goodsAdapter = new GoodsOrderEvaluAdapter(this, yc.getCountGsList());		
    		ListView lw = (ListView)this.findViewById(R.id.evalu_list);
    		lw.setAdapter(goodsAdapter);
    		lw.addHeaderView(createListHeard());
    		int totalHeight = 0;
    		for (int i = 0; i < goodsAdapter.getCount(); i++) {
    			View listItem = goodsAdapter.getView(i, null, lw);
    		    listItem.measure(0, 0);
    		    totalHeight += listItem.getMeasuredHeight();
    		}
    		ViewGroup.LayoutParams params = lw.getLayoutParams();
    		params.height = totalHeight + 80;
    		lw.setLayoutParams(params);
    		
    		Button bt = (Button)findViewById(R.id.evalu_commit);
    		bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {	
					yc.setGs(new ArrayList<Goods>());
					finish();
				}	
		    });

    		
	    }
	 	
	 	private View createListHeard() {
	 		View headerView = getLayoutInflater().inflate(R.layout.order_goods_heardview, null);
			int w = MainActivity.screenWidth/4;
			((TextView)headerView.findViewById(R.id.heard_goods_des)).setWidth(w*2);
			((TextView)headerView.findViewById(R.id.heard_goods_num)).setWidth(w*1);
			((TextView)headerView.findViewById(R.id.heard_goods_price)).setWidth(w*1);
	
			return headerView;
		}
	 	
	 	@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   	        	
	        	((YcApplication)this.getApplication()).setGs(new ArrayList<Goods>());
	        }
	        return super.onKeyDown(keyCode, event);
	    }
}
