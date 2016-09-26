package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetAllusersubinfoPackedBysubidResponse implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private String id;
	private String createtime;
	private String storeid;
	private String style;
	private String times;
	private String name;
	private String state;
	private String intro;
	private List<GetAllusersubinfoPackedBysubid> uhiList;

	private String inhair;
	private String longhair;
	private String shorthair;

	public String getInhair() {
		return inhair;
	}

	public void setInhair(String inhair) {
		this.inhair = inhair;
	}

	public String getLonghair() {
		return longhair;
	}

	public void setLonghair(String longhair) {
		this.longhair = longhair;
	}

	public String getShorthair() {
		return shorthair;
	}

	public void setShorthair(String shorthair) {
		this.shorthair = shorthair;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getStoreid() {
		return storeid;
	}

	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public List<GetAllusersubinfoPackedBysubid> getUhiList() {
		return uhiList;
	}

	public void setUhiList(List<GetAllusersubinfoPackedBysubid> uhiList) {
		this.uhiList = uhiList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static class GetAllusersubinfoPackedBysubid {
		public String id;
		public String state;
		public String createtime;
		public String hairpackedid;
		public String price;
		public String style;

		public GetAllusersubinfoPackedBysubid(String id, String state, String createtime, String hairpackedid,
				String price, String style) {
			super();
			this.id = id;
			this.state = state;
			this.createtime = createtime;
			this.hairpackedid = hairpackedid;
			this.price = price;
			this.style = style;
		}

	}

	public static List<GetAllusersubinfoPackedBysubidResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<GetAllusersubinfoPackedBysubidResponse> lists = new ArrayList<GetAllusersubinfoPackedBysubidResponse>();
			lists = gson.fromJson(json, new TypeToken<List<GetAllusersubinfoPackedBysubidResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
