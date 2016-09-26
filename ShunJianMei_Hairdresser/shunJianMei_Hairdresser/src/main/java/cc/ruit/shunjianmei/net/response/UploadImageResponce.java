package cc.ruit.shunjianmei.net.response;

import java.util.ArrayList;
import java.util.List;

import cc.ruit.shunjianmei.base.BaseResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: UserPhotoResponce
 * @Description: 图片上传
 * @author: 欧阳
 * @date: 2015年9月14日 上午8:56:19
 */
public class UploadImageResponce extends BaseResponse {
	public String ID;// 图片
	public String Image;// id

	/**
	 * 
	 * @Title: getclazz
	 * @Description: 将解析json
	 * @author: 欧阳
	 * @param json
	 * @return
	 * @return: List<UserPhotoResponce>
	 */
	public static List<UploadImageResponce> getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<UploadImageResponce> lists = new ArrayList<UploadImageResponce>();
			lists = gson.fromJson(json,
					new TypeToken<List<UploadImageResponce>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "UploadImageResponce [ID=" + ID + ", Image=" + Image + "]";
	}

}
