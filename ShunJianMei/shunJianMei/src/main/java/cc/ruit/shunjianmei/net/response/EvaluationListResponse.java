package cc.ruit.shunjianmei.net.response;

import android.util.Log;

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
public class EvaluationListResponse implements Serializable {
    /**
     * @fieldName: serialVersionUID
     * @fieldType: long
     * @Description: TODO
     */
    private static final long serialVersionUID = 3L;
    public String ID;// ID 评论ID
    public String Name;// Name 评论者
    public String Photo;// Photo 头像
    public String Time;// 评论时间
    public String Content;// 评论内容
    public String Score;// 评论分数
    public List<Images> Images;//

    public static EvaluationListResponse getclazz(String json) {
        if (json == null) {
            return null;
        }
        try {
            Gson gson = new Gson();
            EvaluationListResponse response = gson.fromJson(json, EvaluationListResponse.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<EvaluationListResponse> getclazz2(String json) {
        if (json == null) {
            return null;
        }
        try {
            Gson gson = new Gson();
            List<EvaluationListResponse> lists = new ArrayList<EvaluationListResponse>();
            lists = gson.fromJson(json, new TypeToken<List<EvaluationListResponse>>() {
            }.getType());
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Images {
        public String Image;// 照片

        @Override
        public String toString() {
            return "Images{" +
                    "Image='" + Image + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EvaluationListResponse{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", Photo='" + Photo + '\'' +
                ", Time='" + Time + '\'' +
                ", Content='" + Content + '\'' +
                ", Score='" + Score + '\'' +
                ", Images=" + Images +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvaluationListResponse that = (EvaluationListResponse) o;

        return Time != null ? Time.equals(that.Time) : that.Time == null;

    }

    @Override
    public int hashCode() {
        return Time != null ? Time.hashCode() : 0;
    }
}
