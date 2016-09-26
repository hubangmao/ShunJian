package cc.ruit.shunjianmei.net.request;

import cc.ruit.shunjianmei.base.BaseRequest;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.oruitkey.OruitKey;

/**
 * @ClassName: HairdresserListRequest
 * @Description: 发型列表
 * @author: 欧阳
 * @date: 2015年10月29日 下午9:31:44
 */
public class HairdresserListRequest extends BaseRequest {
    private String UserID;// 用户ID
    private String Days;// 1~7 未来7天 0代表全部
    private String AreaID;// 商圈ID 0代表全部商圈
    private String CityID;// 城市ID
    private String Sort;
    private String PageSize;
    private String PageIndex;

    /**
     * @param UserId
     * @Title:ActivityListRequest
     * @Description:活动
     */
    public HairdresserListRequest(String UserID, String Days, String AreaID, String CityID, String Sort,
                                  String PageSize, String PageIndex) {
        super("HairdresserList2", "2.0");
        this.UserID = UserID;
        this.CityID = CityID;
        this.Days = Days;
        this.Sort = Sort;
        this.PageSize = PageSize;
        this.PageIndex = PageIndex;
        this.AreaID = AreaID;
        String uid = System.currentTimeMillis() + "";
        setUid(uid, OruitKey.encrypt(uid, "HairdresserList2"));
    }

    /**
     * @param obj
     * @Title: toJsonString
     * @Description: 把对象转成json格式的字符串
     * @author: lee
     * @return: String
     */
    public String toJsonString(HairdresserListRequest obj) {
        GsonBuilder gson = new GsonBuilder();
        gson.disableHtmlEscaping();
        String json = gson.create().toJson(obj);
        LogUtils.i("cord==" + json);
        return json;
    }

    @Override
    public String toString() {
        return "BusinessAreaRequest [UserID=" + UserID + ", Days=" + Days + ", AreaID=" + AreaID + ", CityID=" + CityID
                + ", Sort=" + Sort + ", PageSize=" + PageSize + ", PageIndex=" + PageIndex + "]";
    }

    public String getPageSize() {
        return PageSize;
    }

    public String getPageIndex() {
        return PageIndex;
    }
}
