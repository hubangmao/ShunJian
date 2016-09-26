package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.Html;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * @ClassName: HomeInfoResponse
 * @Description: 首页信息返回
 * @author: lee
 * @date: 2015年9月5日 下午5:41:17
 */
public class HomeInfoResponse implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String GGC;// 呱呱号
	private String Gold;// 金币数量
	private String Money;// 金额
	private String FeedRate;// 换算比例
	private String Minmum;// 最小提现金额
	private String MoneyDay;// 当日奖励
	private String RewardNum;// 今日收徒数量
	private String GGCQQ;// 可选	呱呱官方Q群
	private String GGCBG;// 呱呱广告（活动）图
	private String NewMsgNum;// 新消息数量
	private String Continuous;// 连续签到天数
	private String Attendance;// 签到标识（0未签到，1已签到）
	private String Exp;// 当前经验值
	private String ExpContent;// 经验值说明
	private String ShortMsg;// 积分下边的提示信息
	List<HomeInfoMessageBean> Items;
	public String getGold() {
		return Gold;
	}

	public void setGold(String gold) {
		Gold = gold;
	}

	public String getMoney() {
		return Money;
	}

	public void setMoney(String money) {
		Money = money;
	}

	public String getFeedRate() {
		return FeedRate;
	}

	public void setFeedRate(String feedRate) {
		FeedRate = feedRate;
	}

	public String getMinmum() {
		return Minmum;
	}

	public void setMinmum(String minmum) {
		Minmum = minmum;
	}

	public String getMoneyDay() {
		return MoneyDay;
	}

	public void setMoneyDay(String moneyDay) {
		MoneyDay = moneyDay;
	}

	public String getRewardNum() {
		return RewardNum;
	}

	public void setRewardNum(String rewardNum) {
		RewardNum = rewardNum;
	}

	public String getGGCQQ() {
		return GGCQQ;
	}

	public void setGGCQQ(String gGCQQ) {
		GGCQQ = gGCQQ;
	}

	public String getGGCBG() {
		return GGCBG;
	}

	public void setGGCBG(String gGCBG) {
		GGCBG = gGCBG;
	}

	public String getNewMsgNum() {
		return NewMsgNum;
	}

	public void setNewMsgNum(String newMsgNum) {
		NewMsgNum = newMsgNum;
	}

	public String getContinuous() {
		return Continuous;
	}

	public void setContinuous(String continuous) {
		Continuous = continuous;
	}

	public String getAttendance() {
		return Attendance;
	}

	public void setAttendance(String attendance) {
		Attendance = attendance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getGGC() {
		return GGC;
	}

	public void setGGC(String gGC) {
		GGC = gGC;
	}

	public String getShortMsg() {
		return ShortMsg;
	}

	public void setShortMsg(String shortMsg) {
		ShortMsg = shortMsg;
	}

	public List<HomeInfoMessageBean> getItems() {
		return Items;
	}

	public void setItems(List<HomeInfoMessageBean> items) {
		Items = items;
	}

	public String getExp() {
		return Exp;
	}

	public void setExp(String exp) {
		Exp = exp;
	}

	public String getExpContent() {
		return ExpContent;
	}

	public void setExpContent(String expContent) {
		ExpContent = expContent;
	}

	@Override
	public String toString() {
		return "HomeInfoResponse [GGC=" + GGC + ", Gold=" + Gold + ", Money="
				+ Money + ", FeedRate=" + FeedRate + ", Minmum=" + Minmum
				+ ", MoneyDay=" + MoneyDay + ", RewardNum=" + RewardNum
				+ ", GGCQQ=" + GGCQQ + ", GGCBG=" + GGCBG + ", NewMsgNum="
				+ NewMsgNum + ", Continuous=" + Continuous + ", Attendance="
				+ Attendance + ", Exp=" + Exp + ", ExpContent=" + ExpContent
				+ ", ShortMsg=" + ShortMsg + ", Items=" + Items + "]";
	}

	public static HomeInfoResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			HomeInfoResponse response = gson.fromJson(json,HomeInfoResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<HomeInfoResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<HomeInfoResponse> lists = new ArrayList<HomeInfoResponse>();
			lists = gson.fromJson(json, new TypeToken<List<HomeInfoResponse>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
