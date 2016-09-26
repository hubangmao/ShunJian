package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
/**
 * 
 * @ClassName: MineResponse
 * @Description: 我的出参
 * @author: Johnny
 * @date: 2015年10月12日 下午10:26:45
 */
@Table(name = "UserInfo")
public class MineResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	@Id(column = "UserID")
	@NoAutoIncrement
	private String UserID;// 用户ID
	@Column(column = "NickName")
	private String NickName;// 昵称
	@Column(column = "Sex")
	private String Sex;// 性别 1男2女
	@Column(column = "Photo")
	private String Photo;//头像
	@Column(column = "Mobile")
	private String Mobile;//绑定手机
	@Column(column = "Balance")
	private String Balance;//余额
	@Column(column = "Coupons")
	private String Coupons;//优惠券数
	@Column(column = "FavoriteNum")
	private String FavoriteNum;//收藏数
	
	public String getUserID() {
		return UserID;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getCoupons() {
		return Coupons;
	}

	public void setCoupons(String coupons) {
		Coupons = coupons;
	}

	public String getFavoriteNum() {
		return FavoriteNum;
	}

	public void setFavoriteNum(String favoriteNum) {
		FavoriteNum = favoriteNum;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getBalance() {
		return Balance;
	}

	public void setBalance(String balance) {
		Balance = balance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MineResponse [UserID=" + UserID + ", NickName=" + NickName
				+ ", Sex=" + Sex + ", Photo=" + Photo + ", Mobile=" + Mobile
				+ ", Balance=" + Balance + ", Coupons=" + Coupons
				+ ", FavoriteNum=" + FavoriteNum + "]";
	}

	public static MineResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			MineResponse lists = gson.fromJson(json, MineResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<MineResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<MineResponse> lists = new ArrayList<MineResponse>();
			lists = gson.fromJson(json, new TypeToken<List<MineResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
