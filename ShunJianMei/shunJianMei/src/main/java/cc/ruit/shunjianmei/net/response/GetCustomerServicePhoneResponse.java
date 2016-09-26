package cc.ruit.shunjianmei.net.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: GetCustomerServicePhoneResponse
 * @Description: 得到客服电话
 * @author: 欧阳
 * @date: 2015年10月29日 下午2:55:19
 */
public class GetCustomerServicePhoneResponse implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	private String CustomerServicePhone;// 客服电话

	public static GetCustomerServicePhoneResponse getclazz(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			GetCustomerServicePhoneResponse response = gson.fromJson(json,
					GetCustomerServicePhoneResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<GetCustomerServicePhoneResponse> getclazz2(String json) {
		if (json == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			List<GetCustomerServicePhoneResponse> lists = new ArrayList<GetCustomerServicePhoneResponse>();
			lists = gson.fromJson(json,
					new TypeToken<List<GetCustomerServicePhoneResponse>>() {
					}.getType());
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "GetCustomerServicePhoneResponse [CustomerServicePhone="
				+ CustomerServicePhone + "]";
	}

	public String getCustomerServicePhone() {
		return CustomerServicePhone;
	}

	public void setCustomerServicePhone(String customerServicePhone) {
		CustomerServicePhone = customerServicePhone;
	}

}
