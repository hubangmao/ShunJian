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
public class WithdrawalsInfoItemBean implements Serializable{
	/**@fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: 
	 */
	private static final long serialVersionUID = 1L;
	private String Id;// 商品编号
	private String Title;// 标题
	private String Gold;// 需要金币数
	private String Num;// 库存数量
	private String Explain;// 说明
	private String Photo;// 图片
	private String Identification;// 运营商标识（1移动，2电信，3联通，6三网通）
	private String Type;// 类别（1手机流量）


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

	public String getGold() {
		return Gold;
	}

	public void setGold(String gold) {
		Gold = gold;
	}

	public String getNum() {
		return Num;
	}

	public void setNum(String num) {
		Num = num;
	}

	public String getExplain() {
		return Explain;
	}

	public void setExplain(String explain) {
		Explain = explain;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getIdentification() {
		return Identification;
	}

	public void setIdentification(String identification) {
		Identification = identification;
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
		return "WithdrawalsInfoItemBean [Id=" + Id + ", Title=" + Title
				+ ", Gold=" + Gold + ", Num=" + Num + ", Explain=" + Explain
				+ ", Photo=" + Photo + ", Identification=" + Identification
				+ ", Type=" + Type + "]";
	}

	public static List<WithdrawalsInfoItemBean> getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<WithdrawalsInfoItemBean> lists = new ArrayList<WithdrawalsInfoItemBean>();
			lists = gson.fromJson(json, new TypeToken<List<WithdrawalsInfoItemBean>>() {
			}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
