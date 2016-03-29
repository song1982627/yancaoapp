package com.venusource.yancao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.venusource.yancao.javabean.Goods;

import android.app.Application;

public class YcApplication extends Application{
	private List<Goods> gs;
	
	public List<Goods> getGs() {
		return gs;
	}


	public void setGs(List<Goods> gs) {
		this.gs = gs;
	}
	
	public int[] getTotal() {
		int[] total = {0,0};
		if (gs != null && gs.size() > 0) {		
			for (Goods good : gs) {
				total[0] += good.getCount();
				total[1] += Integer.parseInt(good.getPrice() ) * good.getCount();
			}
		}
		return total;
	}
	
	public List<Goods> getNewGS(List<Goods> newgs) {
		if (gs == null || gs.size() < 1) {
			setGs(newgs);
		} else if (newgs != null && newgs.size() > 0) {
			Map<String ,Goods> gsMap = new HashMap<String ,Goods>();
			for (Goods gd : gs) {
				gsMap.put(gd.getId(), gd);
			}
			for (Goods gd : newgs) {
				if (!gsMap.containsKey(gd.getId())) {
					gs.add(gd);
				}
			}			
		}
		return gs;
	}
	
	public List<Goods> getCountGsList() {
		List<Goods> countGsList = new ArrayList<Goods>();
		if (gs != null && gs.size() > 0) {
			for (Goods gd : gs) {
				if (gd.getCount() > 0) {
					countGsList.add(gd);
				}
			}
		}
		return countGsList;
	}

	@Override
    public void onCreate() {
        super.onCreate();     
        this.setGs(new ArrayList<Goods>());
    }
}
