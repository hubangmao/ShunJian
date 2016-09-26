package cc.ruit.shunjianmei.usermanager;

import java.io.Serializable;

import com.google.gson.Gson;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "UserInfo")
public class UserInfoOfWeixin implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	@Id(column = "UserID")
	@NoAutoIncrement
	public int UserID;// 用户id
	private String city;//城市
	private String country;//"CN"国家
	private String expiresIn;//7200,授权有效时间
	private String expiresTime;//1441083268290,授权时间
	private String gender;//0性别
	private String icon;//头像
	private String nickname;//昵称
	private String openid;//openid
	private String province;//省
	private String refresh_token;//刷新用的token
	private String token;//token
	private String unionid;//联合id
	private String weibo;//腾讯微博标示
	
	
	
	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getExpiresTime() {
		return expiresTime;
	}

	public void setExpiresTime(String expiresTime) {
		this.expiresTime = expiresTime;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	

	@Override
	public String toString() {
		return "UserInfoOfWeixin [UserID=" + UserID + ", city=" + city
				+ ", country=" + country + ", expiresIn=" + expiresIn
				+ ", expiresTime=" + expiresTime + ", gender=" + gender
				+ ", icon=" + icon + ", nickname=" + nickname + ", openid="
				+ openid + ", province=" + province + ", refresh_token="
				+ refresh_token + ", token=" + token + ", unionid=" + unionid
				+ ", weibo=" + weibo + "]";
	}

	public static UserInfoOfWeixin getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			UserInfoOfWeixin info = gson.fromJson(json, UserInfoOfWeixin.class);
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
