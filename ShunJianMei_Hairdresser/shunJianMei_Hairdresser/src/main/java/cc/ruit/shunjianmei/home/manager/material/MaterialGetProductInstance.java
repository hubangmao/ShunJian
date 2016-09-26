package cc.ruit.shunjianmei.home.manager.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.lidroid.xutils.util.LogUtils;

public class MaterialGetProductInstance {
	public ArrayList<Map<String, String>> address = new ArrayList<Map<String, String>>();
	public ArrayList<Map<String, String>> products = new ArrayList<Map<String, String>>();
	public ArrayList<ProductFactionItemInstance> items = new ArrayList<ProductFactionItemInstance>();

	@SuppressLint("NewApi")
	public static MaterialGetProductInstance getBaseResponse(String json) {
		LogUtils.i("onSuccess return:" + json);
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		try {
			MaterialGetProductInstance instance = new MaterialGetProductInstance();
			JSONArray addressArray1 = new JSONArray(json);

			JSONObject jsonObject = (JSONObject) addressArray1.opt(0);
			;

			JSONArray addressArray = new JSONArray(
					jsonObject.optString("Address"));
			for (int i = 0; i < addressArray.length(); i++) {
				JSONObject item = (JSONObject) addressArray.opt(i);
				if (null == item) {
					return null;
				}
				Map<String, String> addressMap = new HashMap<String, String>();
				addressMap.put("ID", item.getString("ID"));
				addressMap.put("Address", item.getString("Address"));
				instance.address.add(addressMap);
			}

			JSONArray praductArray = new JSONArray(
					jsonObject.optString("Product"));
			for (int i = 0; i < praductArray.length(); i++) {
				JSONObject item = (JSONObject) praductArray.opt(i);
				if (null == item) {
					return null;
				}
				Map<String, String> productItem = new HashMap<String, String>();
				productItem.put("ID", item.getString("ID"));
				productItem.put("Name", item.getString("Name"));
				instance.products.add(productItem);
				instance.items.add(ProductFactionItemInstance
						.getBaseResponse(item.getString("Item")));
			}
			return instance;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

class ProductFactionItemInstance {
	public ArrayList<Map<String, String>> factions = new ArrayList<Map<String, String>>();
	public ArrayList<ProductColorItemInstance> items = new ArrayList<ProductColorItemInstance>();

	public static ProductFactionItemInstance getBaseResponse(String json) {
		LogUtils.i("onSuccess return:" + json);
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		try {
			ProductFactionItemInstance instance = new ProductFactionItemInstance();
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = (JSONObject) array.opt(i);
				if (null == item) {
					return null;
				}
				Map<String, String> productItem = new HashMap<String, String>();
				productItem.put("ID", item.getString("ID"));
				productItem.put("Name", item.getString("Name"));
				instance.factions.add(productItem);
				instance.items.add(ProductColorItemInstance
						.getBaseResponse(item.getString("Item")));
			}
			return instance;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}

class ProductColorItemInstance {
	public ArrayList<Map<String, String>> colors = new ArrayList<Map<String, String>>();

	public static ProductColorItemInstance getBaseResponse(String json) {
		LogUtils.i("onSuccess return:" + json);
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		try {
			ProductColorItemInstance instance = new ProductColorItemInstance();
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = (JSONObject) array.opt(i);
				if (null == item) {
					return null;
				}
				Map<String, String> productItem = new HashMap<String, String>();
				productItem.put("ID", item.getString("ID"));
				productItem.put("Name", item.getString("Name"));
				instance.colors.add(productItem);
			}
			return instance;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}

