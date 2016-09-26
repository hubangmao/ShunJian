package cc.ruit.shunjianmei.net.request;

/**
 * 
 * @ClassName: Item
 * @Description: 服务型项
 * @author: 欧阳
 * @date: 2015年10月17日 下午3:57:05
 */
public class ServiceItem {
	private String Code;// 服务代码
	private String Times;// 时长

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getTimes() {
		return Times;
	}

	public void setTimes(String times) {
		Times = times;
	}

	@Override
	public String toString() {
		return "Item [Code=" + Code + ", Times=" + Times + "]";
	}

}
