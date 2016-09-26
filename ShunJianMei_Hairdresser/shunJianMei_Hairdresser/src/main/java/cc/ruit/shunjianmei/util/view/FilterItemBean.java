package cc.ruit.shunjianmei.util.view;

public class FilterItemBean {
	private String id;
	private String name;
	
	public FilterItemBean() {
		super();
	}
	
	public FilterItemBean(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "FilterItemBean [id=" + id + ", name=" + name + "]";
	}

}
