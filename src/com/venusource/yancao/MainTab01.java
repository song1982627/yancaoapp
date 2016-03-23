package com.venusource.yancao;

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
import com.venusource.yancao.goods.MainGoodsActivity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Fragment;
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
	boolean isFirstLoc = true; // 是否首次定位
	boolean show = true;
	TextView textView1;
	
	
	private static final int paddingLeft = 100;
	private static final int paddingTop = 220;
	private static final int paddingRight = 100;
	private static final int paddingBottom = 380;
	private int recLen = 0;

	private GestureDetector mGestureDetector;
	private ViewGroup vg;
	private ScrollView sw;
	
	Handler handler = new Handler();    
    Runnable runnable = new Runnable() {    
        @Override    
        public void run() {    
            recLen++;    
            textView1.setText("" + recLen);    
            handler.postDelayed(this, 1000);    
        }    
    };  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		YcApplication yc = (YcApplication)this.getActivity().getApplication();
		vg = container;
		view = inflater.inflate(R.layout.main_tab_01, container, false);
		
		
		// 地图初始化
		mMapView = (MapView) view.findViewById(R.id.bmapView);

		View child = mMapView.getChildAt(1);
		if (child != null
				&& (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(view.getContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		

		RelativeLayout rl = new RelativeLayout(view.getContext());

		TextView image = new TextView(view.getContext());
		image.setId(1);
		image.setWidth(92);
		image.setHeight(92);
		image.setGravity(Gravity.CENTER);
		image.setText("4.7");
		image.setTextColor(Color.parseColor("#FFFFFF"));
		rl.addView(image);

		LayoutParams param1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		param1.addRule(RelativeLayout.RIGHT_OF, 1);

		TextView location1 = new TextView(view.getContext());
		location1.setWidth(220);
		location1.setId(2);
		location1.setText("中百仓储");
		location1.setGravity(Gravity.CENTER);
		rl.addView(location1, param1);

		LayoutParams param2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		param2.addRule(RelativeLayout.RIGHT_OF, 1);
		param2.addRule(RelativeLayout.BELOW, 2);

		TextView location2 = new TextView(view.getContext());
		location2.setWidth(220);
		location2.setText("456m");
		location2.setGravity(Gravity.CENTER);
		rl.addView(location2, param2);
		rl.setBackgroundResource(R.drawable.pop);
		BitmapDescriptor bd = BitmapDescriptorFactory.fromView(rl);
		
		LatLng llA = new LatLng(30.583970, 114.259781);	
		MarkerOptions ooA = new MarkerOptions().position(llA).icon(bd)
				.anchor(0.2f, 1.0f).zIndex(9);
		mBaiduMap.addOverlay(ooA);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				mBaiduMap.setPadding(paddingLeft, paddingTop, paddingRight,paddingBottom);
				addView();
				return true;
			}
		});
		return view;

	}
	
	 /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
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
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    
    public void addView() {
    	show = true;
    
    	sw = (ScrollView)mMapView.findViewById(1001);
    	if (sw != null){ 
    		mMapView.removeView(sw);
    	}
    		
    	sw = new ScrollView(mMapView.getContext());
    	sw.setId(1001);
    	sw.setVerticalScrollBarEnabled(false);
    	
    	RelativeLayout rl = new RelativeLayout(view.getContext());
    	
    	Button bt0 = new Button(view.getContext());
    	
    	bt0.setOnClickListener(
    		new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (show) {
						Animation translatAnimation=new TranslateAnimation(0, 0, 0, 330);
		    	        translatAnimation.setDuration(500);//设置动画持续时间为1秒
		    	        translatAnimation.setRepeatCount(0);//设置重复次数 
		   	     
		    	        translatAnimation.setFillAfter(true);    	        
		    	        translatAnimation  
		                .setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationEnd(Animation arg0) {
								mMapView.removeView(sw);
								Button bt2 = new Button(view.getContext());
								bt2.setId(19);
						        bt2.setWidth(mMapView.getWidth());
						        bt2.setText("三");
						        bt2.setGravity(Gravity.CENTER);
						        bt2.setBackgroundColor(Color.parseColor("white"));
						        bt2.setHeight(50);
						        bt2.setOnClickListener(
						        		new OnClickListener() {
						        			public void onClick(View v) {
						        				addView();
						        			}
						        		}
						        
								);
						    	//rl.addView(bt0, LayoutParams.WRAP_CONTENT, 50);
						        MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
						        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
						        builder.width(mMapView.getWidth());
						        builder.height(50);
						        builder.point(new Point(0,mMapView.getHeight()));
						        builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);
						        
						        mMapView.addView(bt2, builder.build());
//				    			MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
//					   		    builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
//					   		    builder.width(mMapView.getWidth());
//					   		    builder.height(paddingBottom);
//					   		    builder.point(new Point(0, mMapView.getHeight()*2));
//					   		    builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);
//					   		    mMapView.updateViewLayout(mMapView.findViewById(1001), builder.build());			   		    
					   		    show = false;
							}
							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub
								
							}  });
		    	      
		    			
		    	        sw.startAnimation(translatAnimation);	
					} 
					
		   		    
				}
		});	
    	
    	
    	
    	
    	bt0.setId(9);
        bt0.setWidth(mMapView.getWidth());
        bt0.setText("三");
        bt0.setGravity(Gravity.CENTER);
        bt0.setBackgroundColor(Color.parseColor("white"));
        bt0.setHeight(50);
    	rl.addView(bt0, LayoutParams.WRAP_CONTENT, 50);
    	
    	textView1 = new TextView(view.getContext());
    	textView1.setId(1);
    	textView1.setText("中百仓储");
    	textView1.setTextSize(15.0f);
    	textView1.setGravity(Gravity.LEFT);
    	textView1.setTextColor(Color.BLACK);
    	textView1.setBackgroundColor(Color.parseColor("#FFFFFF"));
    	LayoutParams param0 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		param0.addRule(RelativeLayout.BELOW,9);
        rl.addView(textView1,param0);
        
        
        
        
        TextView textView2 = new TextView(view.getContext());
        textView2.setId(2);
    	textView2.setText("地址：解放大道宝丰路");
    	textView2.setTextSize(15.0f);
    	textView2.setGravity(Gravity.LEFT);
    	textView2.setTextColor(Color.BLACK);
    	textView2.setBackgroundColor(Color.parseColor("#FFFFFF"));
    	LayoutParams param1 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		param1.addRule(RelativeLayout.BELOW, 1);
        rl.addView(textView2,param1);
        
        TextView textView3 = new TextView(view.getContext());
        textView3.setId(3);
    	textView3.setText("长白山(软包)/包");
    	textView3.setTextSize(15.0f);
    	textView3.setGravity(Gravity.LEFT);
    	textView3.setTextColor(Color.BLACK);
    	textView3.setBackgroundColor(Color.parseColor("#FFFFFF"));
    	LayoutParams param2 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		param2.addRule(RelativeLayout.BELOW, 2);
        rl.addView(textView3,param2);
        
        TextView textView4 = new TextView(view.getContext());
        textView4.setId(4);
    	textView4.setText("中南海(软包)/条");
    	textView4.setTextSize(15.0f);
    	textView4.setGravity(Gravity.LEFT);
    	textView4.setTextColor(Color.BLACK);
    	textView4.setBackgroundColor(Color.parseColor("#FFFFFF"));
    	LayoutParams param3 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		param3.addRule(RelativeLayout.BELOW, 3);
        rl.addView(textView4,param3);
        
        TextView textView5 = new TextView(view.getContext());
        textView5.setId(5);
    	textView5.setText("共：90元");
    	textView5.setTextSize(15.0f);
    	textView5.setGravity(Gravity.LEFT);
    	textView5.setTextColor(Color.BLACK);
    	textView5.setBackgroundColor(Color.parseColor("#FFFFFF"));
    	LayoutParams param4 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		param4.addRule(RelativeLayout.BELOW,4);
        rl.addView(textView5,param4);
        
        Button bt = new Button(view.getContext());
        bt.setOnClickListener(
        		new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					startActivity(new Intent(view.getContext(),MainGoodsActivity.class));
    				}
        		}
        );
        
        
        bt.setWidth(mMapView.getWidth());
        bt.setText("下单");
        bt.setGravity(Gravity.CENTER);
        bt.setBackgroundColor(Color.parseColor("blue"));
        LayoutParams param5 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		param5.addRule(RelativeLayout.BELOW,5);
        rl.addView(bt,param5);
        
        rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
        builder.width(mMapView.getWidth());
        builder.height(paddingBottom);
        builder.point(new Point(0, mMapView.getHeight()));
        builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);
       
        sw.addView(rl);
        
        mMapView.addView(sw, builder.build());
       
        
        Animation translatAnimation=new TranslateAnimation(0, 0, 330, 0);
        translatAnimation.setDuration(500);//设置动画持续时间为1秒
        translatAnimation.setRepeatCount(0);//设置重复次数 
     
        translatAnimation.setFillAfter(true);
        sw.startAnimation(translatAnimation);
        runnable.run(); 
        
//        mGestureDetector = new GestureDetector(view.getContext(), new DefaultGestureListener());
//        sw.setOnTouchListener(new OnTouchListener(){
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				return mGestureDetector.onTouchEvent(arg1);
//			}
//        });
       
    }
    
    private class DefaultGestureListener extends SimpleOnGestureListener {
    	@Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
    		if (e2.getY()-e1.getY() > 20) {       			
    			Animation translatAnimation=new TranslateAnimation(0, 0, 0, 330);
    	        translatAnimation.setDuration(500);//设置动画持续时间为1秒
    	        translatAnimation.setRepeatCount(0);//设置重复次数 
   	     
    	        //translatAnimation.setFillAfter(true);    	        
    	        translatAnimation  
                .setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationEnd(Animation arg0) {
		    			MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
			   		    builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
			   		    builder.width(mMapView.getWidth());
			   		    builder.height(paddingBottom);
			   		    builder.point(new Point(0, mMapView.getHeight() + 330));
			   		    builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);
			   		    mMapView.updateViewLayout(mMapView.findViewById(1001), builder.build());
			   		    show = false;
					}
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						
					}  });
    	      
    			
    	        sw.startAnimation(translatAnimation);			
    		}     	
    		else  {    			
    			MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
	   		    builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
	   		    builder.width(mMapView.getWidth());
	   		    builder.height(paddingBottom);
	   		    builder.point(new Point(0, mMapView.getHeight()));
	   		    builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);
	   		    mMapView.updateViewLayout(mMapView.findViewById(1001), builder.build());   	   		
    		}
    		//Toast.makeText(view.getContext(), "e1.getY():"+e1.getY()+",e2.getY():"+e2.getY(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

	
}
