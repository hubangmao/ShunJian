package cc.ruit.shunjianmei.util;

/**
 * 
 * @ClassName: MyEventBus
 * @Description: 构造时传进去一个字符串 ，然后可以通过getMsg()获取出来。
 * @author: 欧阳
 * @date: 2015年10月18日 下午1:37:01
 */
public class MyEventBus {
	private String mMsg;

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

}
