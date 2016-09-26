package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: WithdrawalsInfoResponse
 * @Description: 提现记录返回
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class IncomeDetailsResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Id;// 记录id
	private String Title;// 标题
	private String Times;// 时间（类：1小时前）
	private String Content;// 内容
	private String Gold;//金币数
	private String CreateTime;// 记录时间
	private String Photo;// 图片

	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getTimes() {
		return Times;
	}
	public void setTimes(String times) {
		Times = times;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	public String getGold() {
		return Gold;
	}
	public void setGold(String gold) {
		Gold = gold;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getPhoto() {
		return Photo;
	}
	public void setPhoto(String photo) {
		Photo = photo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return "IncomeDetailsResponse [Id=" + Id + ", Title=" + Title
				+ ", Times=" + Times + ", Content=" + Content + ", Gold="
				+ Gold + ", CreateTime=" + CreateTime + ", Photo=" + Photo
				+ "]";
	}
	public static IncomeDetailsResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			IncomeDetailsResponse response = gson.fromJson(json,IncomeDetailsResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<IncomeDetailsResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<IncomeDetailsResponse> lists = new ArrayList<IncomeDetailsResponse>();
			lists = gson.fromJson(json, new TypeToken<List<IncomeDetailsResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
