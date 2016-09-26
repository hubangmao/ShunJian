package cc.ruit.shunjianmei.usermanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "UserInfo")
public class UserInfo implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	@Id(column = "UserID")
	@NoAutoIncrement
	public int UserID;// 用户id
	@Column(column = "NickName")
	private String NickName;// 名字
	@Column(column = "Photo")
	private String Photo;// 头像
	@Column(column = "Bindphone")
	private String Bindphone;//手机
	@Column(column = "Sex")
	private String Sex;//性别
	@Column(column = "State")
	private String State= "1";// 状态 1正常，0停用


	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}


	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getBindphone() {
		return Bindphone;
	}

	public void setBindphone(String bindphone) {
		Bindphone = bindphone;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserInfo [UserID=" + UserID + ", NickName=" + NickName
				+ ", Photo=" + Photo + ", Bindphone=" + Bindphone + ", Sex="
				+ Sex + ", State=" + State + "]";
	}

	public static List<UserInfo> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<UserInfo> lists = new ArrayList<UserInfo>();
			lists = gson.fromJson(json, new TypeToken<List<UserInfo>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static UserInfo getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			UserInfo info = gson.fromJson(json, UserInfo.class);
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
