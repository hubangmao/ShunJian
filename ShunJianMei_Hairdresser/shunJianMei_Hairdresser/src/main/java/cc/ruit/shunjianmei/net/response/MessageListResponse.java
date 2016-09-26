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
 * @ClassName: MessageListResponse
 * @Description: 消息列表
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
@Table(name = "message")
public class MessageListResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	@Id(column = "MsgId")
	@NoAutoIncrement
	private String Id;// 编号id
	@Column(column = "Title")
	private String Title;// 标题
	@Column(column = "Times")
	private String Times;// 时间（类：1小时前）
	@Column(column = "Content")
	private String Content;//内容
	@Column(column = "CreateTime")
	private String CreateTime;//记录时间
	@Column(column = "comeFrom")
	private String From;//来源（0系统，非0即用户）
	@Column(column = "IsRead")
	private String IsRead;//阅读标记（0红点，1消除）
	@Column(column = "Photo")
	private String Photo;//图片
	@Column(column = "Type")
	private String Type;//消息类型
	

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


	public String getCreateTime() {
		return CreateTime;
	}


	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}


	public String getFrom() {
		return From;
	}


	public void setFrom(String from) {
		From = from;
	}


	public String getIsRead() {
		return IsRead;
	}


	public void setIsRead(String isRead) {
		IsRead = isRead;
	}


	public String getPhoto() {
		return Photo;
	}


	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "MessageListResponse [Id=" + Id + ", Title=" + Title
				+ ", Times=" + Times + ", Content=" + Content + ", CreateTime="
				+ CreateTime + ", From=" + From + ", IsRead=" + IsRead
				+ ", Photo=" + Photo + ", Type=" + Type + "]";
	}


	public static MessageListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			MessageListResponse lists = gson.fromJson(json, MessageListResponse.class);
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<MessageListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<MessageListResponse> lists = new ArrayList<MessageListResponse>();
			lists = gson.fromJson(json, new TypeToken<List<MessageListResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
