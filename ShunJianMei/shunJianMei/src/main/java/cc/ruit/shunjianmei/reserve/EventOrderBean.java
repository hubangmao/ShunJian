package cc.ruit.shunjianmei.reserve;
/**
 * @ClassName: EventOrderBean
 * @Description: EventBus使用的信息传递内容
 * @author: lee
 * @date: 2015年11月10日 下午3:44:35
 */
public class EventOrderBean {
	boolean isPay = false;

	public EventOrderBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventOrderBean(boolean isPay) {
		super();
		this.isPay = isPay;
	}

	public boolean isPay() {
		return isPay;
	}

	public void setPay(boolean isPay) {
		this.isPay = isPay;
	}

	@Override
	public String toString() {
		return "EventOrderBean [isPay=" + isPay + "]";
	}
	
}
