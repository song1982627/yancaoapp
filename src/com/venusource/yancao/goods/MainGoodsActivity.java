package com.venusource.yancao.goods;



import com.venusource.yancao.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class MainGoodsActivity extends Activity implements OnClickListener{
	
	private GoodsTab01 goodsTab01;

	private LinearLayout mTabBtn01;
	private LinearLayout mTabBtn02;
	private LinearLayout mTabBtn03;
	private LinearLayout mTabBtn04;

	private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_goods);
		initViews();
		fragmentManager = getFragmentManager();
		setTabSelection(0);

		
        
    }

    private void initViews()
	{

    	mTabBtn01 = (LinearLayout) findViewById(R.id.id_btn_tab_goods_01);
    	mTabBtn02 = (LinearLayout) findViewById(R.id.id_btn_tab_goods_02);
    	mTabBtn03 = (LinearLayout) findViewById(R.id.id_btn_tab_goods_03);
    	mTabBtn04 = (LinearLayout) findViewById(R.id.id_btn_tab_goods_04);

    	mTabBtn01.setOnClickListener(this);
    	mTabBtn02.setOnClickListener(this);
    	mTabBtn03.setOnClickListener(this);
    	mTabBtn04.setOnClickListener(this);
	}
    
    public void onClick(View v)
	{
//		switch (v.getId())
//		{
//		case R.id.btn_tab_goods_01:
//			setTabSelection(0);
//			break;
//		case R.id.btn_tab_goods_02:
//			setTabSelection(1);
//			break;
//		case R.id.btn_tab_goods_03:
//			setTabSelection(2);
//			break;
//		case R.id.btn_tab_goods_04:
//			setTabSelection(3);
//			break;
//
//		default:
//			break;
//		}
	}
    
  
	@SuppressLint("NewApi")
	private void setTabSelection(int index)
	{
		
		resetBtn();
	
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		hideFragments(transaction);
		switch (index)
		{
		case 0:
			
			if (goodsTab01 == null)
			{
				
				goodsTab01 = new GoodsTab01();
				transaction.add(R.id.id_goods_content, goodsTab01);
			} else
			{
				
				transaction.show(goodsTab01);
			}
			break;
//		case 1:
//			
//			if (mTab02 == null)
//			{
//				// ���MessageFragmentΪ�գ��򴴽�һ������ӵ�������
//				mTab02 = new MainTab02();
//				transaction.add(R.id.id_content, mTab02);
//			} else
//			{
//				// ���MessageFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
//				transaction.show(mTab02);
//			}
//			break;
//		case 2:
//			// ������˶�̬tabʱ���ı�ؼ���ͼƬ��������ɫ
//			((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
//					.setImageResource(R.drawable.tab_address_pressed);
//			if (mTab03 == null)
//			{
//				// ���NewsFragmentΪ�գ��򴴽�һ������ӵ�������
//				mTab03 = new MainTab03();
//				transaction.add(R.id.id_content, mTab03);
//			} else
//			{
//				// ���NewsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
//				transaction.show(mTab03);
//			}
//			break;
//		case 3:
//			// �����������tabʱ���ı�ؼ���ͼƬ��������ɫ
//			((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting))
//					.setImageResource(R.drawable.tab_settings_pressed);
//			if (mTab04 == null)
//			{
//				// ���SettingFragmentΪ�գ��򴴽�һ������ӵ�������
//				mTab04 = new MainTab04();
//				transaction.add(R.id.id_content, mTab04);
//			} else
//			{
//				// ���SettingFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
//				transaction.show(mTab04);
//			}
//			break;
		}
		transaction.commit();
	}


	private void resetBtn()
	{
//		((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin))
//				.setImageResource(R.drawable.tab_weixin_normal);
//		((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
//				.setImageResource(R.drawable.tab_find_frd_normal);
//		((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
//				.setImageResource(R.drawable.tab_address_normal);
//		((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting))
//				.setImageResource(R.drawable.tab_settings_normal);
	}

	
	@SuppressLint("NewApi")
	private void hideFragments(FragmentTransaction transaction)
	{
		if (goodsTab01 != null)
		{
			transaction.hide(goodsTab01);
		}
//		if (mTab02 != null)
//		{
//			transaction.hide(mTab02);
//		}
//		if (mTab03 != null)
//		{
//			transaction.hide(mTab03);
//		}
//		if (mTab04 != null)
//		{
//			transaction.hide(mTab04);
//		}
		
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
