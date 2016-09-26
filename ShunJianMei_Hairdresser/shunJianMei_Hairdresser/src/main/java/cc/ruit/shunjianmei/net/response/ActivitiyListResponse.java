package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: WithdrawalsInfoResponse
 * @Description: 提现信息返回
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class ActivitiyListResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Title;// 活动标题
	private String Id;// 编号
	private String StartTime;// 开始时间
	private String EndTime;// 结束时间
	private String Case;// 限制条件
	private String Content;// 活动内容
//	 "Title": "活动标题",
//     "Id": "1",
//     "StartTime": "2015-08-01 0:12:12",
//     "EndTime": "2015-08-10 0:12:12",
//"Case": "限制条件说明",
//"Content": "活动内容"

	@Override
	public String toString() {
		return "ActivitiyListResponse [Title=" + Title + ", Id=" + Id
				+ ", StartTime=" + StartTime + ", EndTime=" + EndTime
				+ ", Case=" + Case + ", Content=" + Content + "]";
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getCase() {
		return Case;
	}

	public void setCase(String case1) {
		Case = case1;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static ActivitiyListResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			ActivitiyListResponse response = gson.fromJson(json,ActivitiyListResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<ActivitiyListResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<ActivitiyListResponse> lists = new ArrayList<ActivitiyListResponse>();
			lists = gson.fromJson(json, new TypeToken<List<ActivitiyListResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
