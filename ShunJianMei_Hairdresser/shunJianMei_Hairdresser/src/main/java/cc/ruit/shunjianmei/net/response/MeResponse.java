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
 * @ClassName: MeResponse
 * @Description: 我的出参
 * @author: Johnny
 * @date: 2015年10月12日 下午10:26:45
 */
@Table(name = "UserInfo")
public class MeResponse implements Serializable{
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
	@Column(column = "Content")
	private String Content;//内容
	@Column(column = "Photo")
	private String Photo;//头像
	@Column(column = "BindPhone")
	private String BindPhone;//绑定手机
	@Column(column = "CheckState")
	private String CheckState;//审核状态 1审核中2审核通过3审核失败
	@Column(column = "Balance")
	private String Balance;//余额
	@Column(column = "OrderNum")
	private String OrderNum;//订单数
	@Column(column = "EvaluateNum")
	private String EvaluateNum;//评论数
	
	public String getUserID() {
		return UserID;
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


	public String getContent() {
		return Content;
	}


	public void setContent(String content) {
		Content = content;
	}


	public String getPhoto() {
		return Photo;
	}


	public void setPhoto(String photo) {
		Photo = photo;
	}


	public String getBindPhone() {
		return BindPhone;
	}


	public void setBindPhone(String bindPhone) {
		BindPhone = bindPhone;
	}


	public String getCheckState() {
		return CheckState;
	}


	public void setCheckState(String checkState) {
		CheckState = checkState;
	}


	public String getBalance() {
		return Balance;
	}


	public void setBalance(String balance) {
		Balance = balance;
	}


	public String getOrderNum() {
		return OrderNum;
	}


	public void setOrderNum(String orderNum) {
		OrderNum = orderNum;
	}


	public String getEvaluateNum() {
		return EvaluateNum;
	}


	public void setEvaluateNum(String evaluateNum) {
		EvaluateNum = evaluateNum;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MeResponse [UserID=" + UserID + ", NickName=" + NickName
				+ ", Sex=" + Sex + ", Content=" + Content + ", Photo=" + Photo
				+ ", BindPhone=" + BindPhone + ", CheckState=" + CheckState
				+ ", Balance=" + Balance + ", OrderNum=" + OrderNum
				+ ", EvaluateNum=" + EvaluateNum + "]";
	}

	public static MeResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			MeResponse lists = gson.fromJson(json, MeResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<MeResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<MeResponse> lists = new ArrayList<MeResponse>();
			lists = gson.fromJson(json, new TypeToken<List<MeResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
