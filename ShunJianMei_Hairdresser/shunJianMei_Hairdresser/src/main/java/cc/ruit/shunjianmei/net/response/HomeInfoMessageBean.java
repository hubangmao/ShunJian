package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: HomeInfoResponse
 * @Description: 首页信息返回
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class HomeInfoMessageBean implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Id;// 编号id
	private String Times;// 时间（类：8小时前）
	private String NickName;// 昵称
	private String Content;// 消息体
	private String CreateTime;// 消息时间
	private String Photo;// 头像地址
	

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

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

	public String getTimes() {
		return Times;
	}

	public void setTimes(String times) {
		Times = times;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "HomeInfoMessageBean [Content=" + Content + ", CreateTime="
				+ CreateTime + ", Id=" + Id + ", NickName=" + NickName
				+ ", Photo=" + Photo + ", Times=" + Times + "]";
	}

	public static List<HomeInfoMessageBean> getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HomeInfoMessageBean> lists = new ArrayList<HomeInfoMessageBean>();
			lists = gson.fromJson(json, new TypeToken<List<HomeInfoMessageBean>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
