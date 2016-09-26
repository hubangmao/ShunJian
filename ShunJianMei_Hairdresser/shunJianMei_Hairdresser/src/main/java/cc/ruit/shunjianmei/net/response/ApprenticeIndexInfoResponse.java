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
public class ApprenticeIndexInfoResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Money;// 师徒累计奖励
	private String MoneyDay;// 师徒当日奖励
	private String Reward1;// 徒弟收入比例
	private String Reward1Num;// 徒弟收入金额
	private String Reward1Count;// 徒弟人数
	private String Reward2;// 徒孙收入比例
	private String Reward2Num;// 徒孙收入金额
	private String Reward2Count;// 徒孙人数
	private String FeedRate;// 换算比例
	private String GGC;// 呱呱号
	private String SharingUrl;//分享地址（此地址包含动态域名部分和固定页面部分，地址需要自行添加参数ggc=呱呱号）（配置表取）
					   		//例如：http://ggc.ruitei.com/user/receivebonus?ggc=123456
	private String Bonus;//红包金额（配置表取）
	private String SharingTitle;//分享的标题（配置表取）
	private String SharingContent;//分享的内容（配置表取）
	private String SharingComment;//分享的评论
	private String SharingImage;//分享的图片地址（配置表取）

	public String getMoney() {
		return Money;
	}

	public void setMoney(String money) {
		Money = money;
	}

	public String getMoneyDay() {
		return MoneyDay;
	}

	public void setMoneyDay(String moneyDay) {
		MoneyDay = moneyDay;
	}

	public String getReward1() {
		return Reward1;
	}

	public void setReward1(String reward1) {
		Reward1 = reward1;
	}

	public String getReward1Num() {
		return Reward1Num;
	}

	public void setReward1Num(String reward1Num) {
		Reward1Num = reward1Num;
	}

	public String getReward1Count() {
		return Reward1Count;
	}

	public void setReward1Count(String reward1Count) {
		Reward1Count = reward1Count;
	}

	public String getReward2() {
		return Reward2;
	}

	public void setReward2(String reward2) {
		Reward2 = reward2;
	}

	public String getReward2Num() {
		return Reward2Num;
	}

	public void setReward2Num(String reward2Num) {
		Reward2Num = reward2Num;
	}

	public String getReward2Count() {
		return Reward2Count;
	}

	public void setReward2Count(String reward2Count) {
		Reward2Count = reward2Count;
	}

	public String getFeedRate() {
		return FeedRate;
	}

	public void setFeedRate(String feedRate) {
		FeedRate = feedRate;
	}

	public String getGGC() {
		return GGC;
	}

	public void setGGC(String gGC) {
		GGC = gGC;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSharingUrl() {
		return SharingUrl;
	}

	public void setSharingUrl(String sharingUrl) {
		SharingUrl = sharingUrl;
	}

	public String getBonus() {
		return Bonus;
	}

	public void setBonus(String bonus) {
		Bonus = bonus;
	}

	public String getSharingTitle() {
		return SharingTitle;
	}

	public void setSharingTitle(String sharingTitle) {
		SharingTitle = sharingTitle;
	}

	public String getSharingContent() {
		return SharingContent;
	}

	public void setSharingContent(String sharingContent) {
		SharingContent = sharingContent;
	}

	public String getSharingComment() {
		return SharingComment;
	}

	public void setSharingComment(String sharingComment) {
		SharingComment = sharingComment;
	}

	public String getSharingImage() {
		return SharingImage;
	}

	public void setSharingImage(String sharingImage) {
		SharingImage = sharingImage;
	}

	@Override
	public String toString() {
		return "ApprenticeIndexInfoResponse [Money=" + Money + ", MoneyDay="
				+ MoneyDay + ", Reward1=" + Reward1 + ", Reward1Num="
				+ Reward1Num + ", Reward1Count=" + Reward1Count + ", Reward2="
				+ Reward2 + ", Reward2Num=" + Reward2Num + ", Reward2Count="
				+ Reward2Count + ", FeedRate=" + FeedRate + ", GGC=" + GGC
				+ ", SharingUrl=" + SharingUrl + ", Bonus=" + Bonus
				+ ", SharingTitle=" + SharingTitle + ", SharingContent="
				+ SharingContent + ", SharingComment=" + SharingComment
				+ ", SharingImage=" + SharingImage + "]";
	}

	public static ApprenticeIndexInfoResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			ApprenticeIndexInfoResponse response = gson.fromJson(json, ApprenticeIndexInfoResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<ApprenticeIndexInfoResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<ApprenticeIndexInfoResponse> lists = new ArrayList<ApprenticeIndexInfoResponse>();
			lists = gson.fromJson(json, new TypeToken<List<ApprenticeIndexInfoResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
