package cc.ruit.shunjianmei.home.manager;

public class HomeBean {
	private String name;
	private String desc;
	private int rid;
	
	public HomeBean(String name, String desc, int rid) {
		super();
		this.name = name;
		this.desc = desc;
		this.rid = rid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	@Override
	public String toString() {
		return "HomeBean [name=" + name + ", desc=" + desc + ", rid=" + rid
				+ "]";
	}
	
}
