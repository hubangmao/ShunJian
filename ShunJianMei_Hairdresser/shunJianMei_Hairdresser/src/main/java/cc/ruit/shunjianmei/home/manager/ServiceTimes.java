package cc.ruit.shunjianmei.home.manager;
/**
 * @ClassName: HairStyle
 * @Description: 发型
 * @author: gaoj
 * @date: 2015年10月15日 下午1:02:04
 */
public class ServiceTimes {
	private String Code;//服务类型
	private String Name; //服务名称
	private String Times;//服务时间
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getTimes() {
		return Times;
	}
	public void setTimes(String times) {
		Times = times;
	}
	@Override
	public String toString() {
		return "ServiceTimes [Code=" + Code + ", Name=" + Name + ", Times="
				+ Times + "]";
	}
	

}
