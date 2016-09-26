package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: Intro
 * @Description: 简介
 * @author: 欧阳
 * @date: 2015年10月23日 下午4:37:12
 */
public class Intro implements Serializable{
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	public List<Hair> Hair;// 擅长发型
	public List<Hobbies> Hobbies;// 爱好
	public String Content;// 简介内容

	public static class Hair implements Serializable{
		public String Name;// 擅长发型名称

		@Override
		public String toString() {
			return "Hair [Name=" + Name + "]";
		}
		
	}

	public static class Hobbies implements Serializable{
		public String Name;// 爱好内容

		@Override
		public String toString() {
			return "Hobbies [Name=" + Name + "]";
		}
		
	}

	@Override
	public String toString() {
		return "Intro [Hair=" + Hair + ", Hobbies=" + Hobbies + ", Content="
				+ Content + "]";
	}
	
}
