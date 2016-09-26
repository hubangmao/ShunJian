package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: RewardListResponse
 * @Description: 徒弟徒孙返回
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class RewardListResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Id;// 编号id
	private String NickName;// 昵称
	private String Photo;// 头像地址
	private String sortLetters;//简写字母

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
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

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	@Override
	public String toString() {
		return "RewardListResponse [Id=" + Id + ", NickName=" + NickName
				+ ", Photo=" + Photo + ", sortLetters=" + sortLetters + "]";
	}

	public static List<RewardListResponse> getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<RewardListResponse> lists = new ArrayList<RewardListResponse>();
			lists = gson.fromJson(json, new TypeToken<List<RewardListResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
