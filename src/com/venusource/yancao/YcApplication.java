package com.venusource.yancao;

import java.util.HashMap;
import java.util.Map;

import com.venusource.yancao.javabean.Goods;

import android.app.Application;

public class YcApplication extends Application{
	private Map<String,Goods> gs;

	public Map<String,Goods> getGs() {
		return gs;
	}

	public void setGs(Map<String,Goods> gs) {
		this.gs = gs;
	}
	
	public int goodsNum = 0;
	
	public double goodsPrice = 0d;
	
	
	
	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	@Override
    public void onCreate() {
        super.onCreate();     
        this.setGs(new HashMap<String,Goods>());
    }
}
