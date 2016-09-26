package cc.ruit.shunjianmei.home.manager;
/**
 * @ClassName: HairStyle
 * @Description: 发型
 * @author: gaoj
 * @date: 2015年10月15日 下午1:02:04
 */
public class HairStyle {
	private String ID;//发型ID
	private String Name; //发型
	private String Checked;//选择
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getChecked() {
		return Checked;
	}
	public void setChecked(String checked) {
		Checked = checked;
	}
	@Override
	public String toString() {
		return "HairStyle [ID=" + ID + ", Name=" + Name + ", Checked="
				+ Checked + "]";
	}
	

}
