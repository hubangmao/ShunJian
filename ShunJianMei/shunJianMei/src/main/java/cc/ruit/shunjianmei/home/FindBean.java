package cc.ruit.shunjianmei.home;
/**
 * @ClassName: FindBean
 * @Description: 首页列表 名字bean
 * @author: lee
 * @date: 2015年10月29日 下午9:50:34
 */
public class FindBean {
	private String cnName;
	private String enName;
	public FindBean(String cnName, String enName) {
		super();
		this.cnName = cnName;
		this.enName = enName;
	}
	
	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	@Override
	public String toString() {
		return "FindBean [cnName=" + cnName + ", enName=" + enName + "]";
	}
	
}
