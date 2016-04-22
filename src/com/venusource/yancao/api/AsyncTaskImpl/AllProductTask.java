package com.venusource.yancao.api.AsyncTaskImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.venusource.yancao.YcApplication;
import com.venusource.yancao.adapter.GoodsOrderAdapter;
import com.venusource.yancao.api.HttpsClient;
import com.venusource.yancao.javabean.Goods;

import android.os.AsyncTask;
import android.widget.ListView;

public class AllProductTask extends AsyncTask<Object, Object, Object> {
	
	private static String apiUrl = "api/products";
	
	private YcApplication yc;
	private GoodsOrderAdapter goodsAdapter;
	
	public AllProductTask(YcApplication yc,GoodsOrderAdapter goodsAdapter){
        this.yc=yc;
        this.goodsAdapter = goodsAdapter;
    }


	@Override
	protected Object doInBackground(Object... arg) {
		// TODO Auto-generated method stub
		Map<String,String> params = (Map<String,String>)arg[0];	
		Map<String,String> authParams = (Map<String,String>)arg[1];
		return HttpsClient.get(params, authParams, apiUrl, yc);			
	}
	
	@Override
    protected void onPostExecute(Object result) { 
		super.onPostExecute(result);
		List<Goods> list = new ArrayList<Goods>();	
		if (result != null && result instanceof JSONArray) {
			JSONArray ja = (JSONArray)result;
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo;
				try {
					jo = (JSONObject) ja.get(i);
					Goods goods = new Goods();
					goods.setId(jo.getString("code"));
					goods.setGood_name(jo.getString("name"));
					goods.setImgUrl(jo.getString("avatar"));
					JSONArray pja = jo.getJSONArray("packings");
					for (int j=0; j<pja.length(); j++) {
						JSONObject pjo = (JSONObject) pja.get(j);
						if ("BAR".equalsIgnoreCase(pjo.getString("type"))) {
							int price = (int)pjo.getDouble("price");
							goods.setPrice(price + "");
						}						
					}
	
					list.add(goods);						
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}		
		} 
		
		yc.getNewGS(list);	
		List<Goods> nlist = new ArrayList<Goods>();
    	if (yc.getGs() != null && yc.getGs().size() > 2) {
    		nlist.add(yc.getGs().get(0));
    		nlist.add(yc.getGs().get(1));
		}
		goodsAdapter.setList(nlist);
		goodsAdapter.notifyDataSetChanged();
       
    }

}
