package cc.ruit.shunjianmei.util;

import java.util.List;

import cc.ruit.shunjianmei.net.response.HairdresserDetailResponse;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse;

/**
 * 
 * @ClassName: MyEventBus
 * @Description: 构造时传进去一个字符串 ，然后可以通过getMsg()获取出来。
 * @author: 欧阳
 * @date: 2015年10月18日 下午1:37:01
 */
public class MyEventBus {
	private String mMsg;
	private StoreDetailResponse obj;// 店铺详情页向商户介绍页传值
	private List<HairdresserDetailResponse> templist;

	public MyEventBus(String mMsg) {
		super();
		this.mMsg = mMsg;
	}

	public MyEventBus() {
		super();
	}

	public String getmMsg() {
		return mMsg;
	}

	public void setmMsg(String mMsg) {
		this.mMsg = mMsg;
	}

	public StoreDetailResponse getObj() {
		return obj;
	}

	public void setObj(StoreDetailResponse obj) {
		this.obj = obj;
	}

	public List<HairdresserDetailResponse> getTemplist() {
		return templist;
	}

	public void setTemplist(List<HairdresserDetailResponse> templist) {
		this.templist = templist;
	}

}
