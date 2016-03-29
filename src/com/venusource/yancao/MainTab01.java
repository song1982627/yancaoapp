package com.venusource.yancao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.venusource.yancao.adapter.GoodsAdapter;
import com.venusource.yancao.adapter.GoodsOrderAdapter;
import com.venusource.yancao.adapter.GoodsOrderCommitAdapter;
import com.venusource.yancao.goods.MainGoodsActivity;
import com.venusource.yancao.javabean.Goods;
import com.venusource.yancao.zxing.CreateQRImage;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.RelativeLayout.LayoutParams;




@SuppressLint("NewApi")
public class MainTab01 extends Fragment 
{
	View view;
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	MapView mMapView;
	BaiduMap mBaiduMap;
	boolean isFirstLoc = true;
	boolean show = true;
	TextView order_bar_num;
	private double latitude;
	private double longitude;
	
	
	private static final int paddingLeft = 100;
	private static final int paddingTop = 220;
	private static final int paddingRight = 100;
	private static final int paddingBottom = 350;
	private int recLen = 0;

	private GestureDetector mGestureDetector;
	private View ordeGoodsView; //商户选烟图层
	private ViewGroup vg;
	private View orderview;//等待图层
	private View ordeGoodsCommitView;//订单图层
	private ScrollView sw;
	private boolean isClick = true;
	private YcApplication yc;
	
	private Handler handler = new Handler();    
	private Runnable runnable = new Runnable() {    
        @Override    
        public void run() {    
            recLen++;    
            if (recLen == 10) {
            	TextView vm = (TextView)orderview.findViewById(R.id.order_bar_t1);
            	vm.setVisibility(View.VISIBLE);
            	LinearLayout ly = (LinearLayout)orderview.findViewById(R.id.order_bar_t2);
            	ly.setVisibility(View.VISIBLE);
            	
            }
            
            if (recLen == 15) {            	
            	mMapView.removeView(orderview);           	
            	ordeGoodsCommitView = LayoutInflater.from(view.getContext()).inflate(
        				R.layout.order_goods_commit, null);	
            	((TextView)ordeGoodsCommitView.findViewById(R.id.order_goods_commit_name)).setText("中百仓储");
            	((TextView)ordeGoodsCommitView.findViewById(R.id.order_goods_commit_address)).setText("地址：解放大道宝丰路");
            		
        		GoodsOrderCommitAdapter goodsAdapter = new GoodsOrderCommitAdapter(ordeGoodsCommitView.getContext(), yc.getCountGsList());		
        		ListView lw = (ListView)ordeGoodsCommitView.findViewById(R.id.order_goods_commit_list);
        		lw.setAdapter(goodsAdapter);
        		
        		ImageView sweepIV = (ImageView)ordeGoodsCommitView.findViewById(R.id.order_goods_commit_pic);
        		sweepIV.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {	
						handler.removeCallbacks(runnable);
						recLen = 0;
						mMapView.removeView(ordeGoodsCommitView);	
						isClick = true;
						yc.setGs(new ArrayList<Goods>());
					}	
			    });
        		CreateQRImage cr = new CreateQRImage();
        		cr.createQRImage("11111111", sweepIV);   		
        		
        		MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
                builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
                builder.width(mMapView.getWidth());
                builder.height(400);
                builder.point(new Point(0, mMapView.getHeight()));
                builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);
                mMapView.addView(ordeGoodsCommitView, builder.build());
                
                
            }
            
            order_bar_num.setText("" + recLen);    
            handler.postDelayed(this, 1000); 
           
        }    
    };  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		yc = (YcApplication)getActivity().getApplication();
		
		vg = container;
		view = inflater.inflate(R.layout.main_tab_01, container, false);
	
		mMapView = (MapView) view.findViewById(R.id.bmapView);

		View child = mMapView.getChildAt(1);
		if (child != null
				&& (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		
		mBaiduMap.setMyLocationEnabled(true);
	
		mLocClient = new LocationClient(view.getContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); 
		option.setCoorType("bd09ll"); 
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		return view;

	}

	//定位产生的商户
	private void setMapView() {
		LinearLayout ly = new LinearLayout(view.getContext());

		RelativeLayout rl = new RelativeLayout(view.getContext());

		TextView image = new TextView(view.getContext());
		image.setId(1);
		image.setWidth(70);
		image.setHeight(62);
		image.setGravity(Gravity.CENTER);
		image.setText("4.7");
		image.setTextColor(Color.parseColor("#FFFFFF"));
		rl.addView(image);

		LayoutParams param1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		param1.addRule(RelativeLayout.RIGHT_OF, 1);

		TextView location1 = new TextView(view.getContext());
		location1.setWidth(200);
		location1.setId(2);
		location1.setTextSize(10);
		location1.setText("中百仓储");
		location1.setGravity(Gravity.CENTER);
		rl.addView(location1, param1);

		LayoutParams param2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		param2.addRule(RelativeLayout.RIGHT_OF, 1);
		param2.addRule(RelativeLayout.BELOW, 2);

		TextView location2 = new TextView(view.getContext());
		location2.setWidth(200);
		location2.setText("45m");
		location2.setGravity(Gravity.CENTER);
		location2.setTextSize(10);
		rl.addView(location2, param2);
		rl.setBackgroundResource(R.drawable.pop_1);
		ly.addView(rl);
		
		BitmapDescriptor bd = BitmapDescriptorFactory.fromView(ly);
		
		//LatLng llA = new LatLng(30.583970, 114.259781);	
		LatLng llA = new LatLng(latitude,longitude);	
		
		MarkerOptions ooA = new MarkerOptions().position(llA).icon(bd)
				.anchor(0.2f, 1.0f).zIndex(9);
		mBaiduMap.addOverlay(ooA);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				//mBaiduMap.setPadding(paddingLeft, paddingTop, paddingRight,paddingBottom);
				addView();
				return true;
			}
		});
	}
	
	 /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
         
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())                          
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                
                latitude = location.getLatitude() + 0.0005;
                longitude = location.getLongitude() + 0.0005;
                setMapView();
            }
          
           
            
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    
    
    //获取商户默认选择烟列表数据
    private void initListData(ListView listView,Context context,TextView orderGoodsTotal) {	
    	isClick = true;
		List<Goods> list = new ArrayList<Goods>();
		Goods goods = new Goods();
		goods.setId("ID-1");
		goods.setGood_name("中南海(软包)/条" );
		goods.setDescrible("商户1");
		goods.setPrice(120 + "");
		list.add(goods);
		goods = new Goods();
		goods.setId("ID-2");
		goods.setGood_name("长白山(软包)/条" );
		goods.setDescrible("商户1");
		goods.setPrice(160 + "");	
		list.add(goods);		
		GoodsOrderAdapter goodsAdapter = new GoodsOrderAdapter(context, yc.getNewGS(list),orderGoodsTotal);		
		listView.setAdapter(goodsAdapter);
	}	
    
    //选择商品列表返回数据
    private void choseListData(ListView listView,Context context,TextView orderGoodsTotal) {	    			
		GoodsOrderAdapter goodsAdapter = new GoodsOrderAdapter(context, yc.getCountGsList(),orderGoodsTotal);		
		listView.setAdapter(goodsAdapter);
		if (yc.getGs() != null && yc.getGs().size() > 0) {
			int[] total = yc.getTotal();
			if (total[1] > 0) {
				orderGoodsTotal.setText("总价:" + total[1] + "元");
			} else {
				orderGoodsTotal.setText("");
			}
			goodsAdapter.setBuyNum(total[0]);
			goodsAdapter.setTotalPrice(total[1]);
		}
		
	}	
   
    
    //点击商户弹出图层
    public void addView() {
    	if (!isClick) {
    		return;
    	} 	   	
        if (ordeGoodsView != null) {
        	mMapView.removeView(ordeGoodsView);      	
        }
             
    	ordeGoodsView = LayoutInflater.from(view.getContext()).inflate(
				R.layout.order_goods, null);	
    	((TextView)ordeGoodsView.findViewById(R.id.order_goods_name)).setText("中百仓储");
    	((TextView)ordeGoodsView.findViewById(R.id.order_goods_address)).setText("地址：解放大道宝丰路");
    	initListData(((ListView)ordeGoodsView.findViewById(R.id.order_goods_list)),ordeGoodsView.getContext(),(TextView)ordeGoodsView.findViewById(R.id.order_goods_total));
    	
    	MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
        builder.width(mMapView.getWidth());
        builder.height(paddingBottom);
        builder.point(new Point(0, mMapView.getHeight()));
        builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);
        mMapView.addView(ordeGoodsView, builder.build());
        
	    Animation translatAnimation=new TranslateAnimation(0, 0, paddingBottom, 0);
	    translatAnimation.setDuration(500);
	    translatAnimation.setRepeatCount(0);
	    translatAnimation.setFillAfter(true);
	    ordeGoodsView.startAnimation(translatAnimation);
        
	    Button bc = (Button)ordeGoodsView.findViewById(R.id.order_goods_commit);
	    bc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				orderClick();				
			}	
	    });
	    
        
        Button bt = (Button)ordeGoodsView.findViewById(R.id.order_goods_button);
        
        bt.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				animationClick(ordeGoodsView);
 			}	
 		});  
        
        Button be = (Button)ordeGoodsView.findViewById(R.id.order_goods_choose);
        
        be.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				Intent in = new Intent();  
                in.setClassName( view.getContext(), "com.venusource.yancao.goods.MainGoodsActivity" );                          
                startActivityForResult(in ,1);  
 			}	
 		});  
    }
    
    
    //下单事件
    private void orderClick() {
    	isClick = false;
		mMapView.removeView(ordeGoodsView);
		orderview = LayoutInflater.from(view.getContext()).inflate(
		R.layout.order_bar, null);
		MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
	    builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
	    builder.width(mMapView.getWidth());
	    builder.height(paddingBottom);
	    builder.point(new Point(0, 0));
	    builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_TOP);
		mMapView.addView(orderview,builder.build());
		order_bar_num = (TextView)orderview.findViewById(R.id.order_bar_num);
		Button cancel = (Button)orderview.findViewById(R.id.order_goods_cancel);
		cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					isClick = true;
					mMapView.removeView(orderview);			
					handler.removeCallbacks(runnable);
					recLen = 0;
					yc.setGs(new ArrayList<Goods>());
					
				}	
		    });
		runnable.run();
	}
    
    //动画效果
	private void animationClick(final View ordeGoodsView) {
		if (show) {
			Animation translatAnimation = new TranslateAnimation(0, 0, 0,
					paddingBottom);
			translatAnimation.setDuration(500);
			translatAnimation.setRepeatCount(0);
			translatAnimation.setFillAfter(false);
			ordeGoodsView.startAnimation(translatAnimation);
			translatAnimation
					.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationEnd(Animation arg0) {
							MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
							builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
							builder.width(mMapView.getWidth());
							builder.height(40);
							builder.point(new Point(0, mMapView.getHeight()));
							builder.align(MapViewLayoutParams.ALIGN_LEFT,
									MapViewLayoutParams.ALIGN_BOTTOM);
							mMapView.updateViewLayout(ordeGoodsView,
									builder.build());

						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
						}
					});
			show = false;
		} else {
			Animation translatAnimation = new TranslateAnimation(0, 0,
					paddingBottom, 0);
			translatAnimation.setDuration(500);
			translatAnimation.setRepeatCount(0);
			translatAnimation.setFillAfter(false);
			ordeGoodsView.startAnimation(translatAnimation);
			MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
			builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
			
			builder.width(mMapView.getWidth());
			builder.height(paddingBottom);
			builder.point(new Point(0, mMapView.getHeight()));
			builder.align(MapViewLayoutParams.ALIGN_LEFT,
					MapViewLayoutParams.ALIGN_BOTTOM);
			mMapView.updateViewLayout(ordeGoodsView, builder.build());
			show = true;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && data.hasExtra("GOODSBACK")) {
			choseListData(((ListView)ordeGoodsView.findViewById(R.id.order_goods_list)),ordeGoodsView.getContext(),(TextView)ordeGoodsView.findViewById(R.id.order_goods_total));
	    	
		}
		
		
	}
	
}
