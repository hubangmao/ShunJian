package cc.ruit.shunjianmei.reserve;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.util.LogUtils;

import cc.ruit.shunjianmei.net.request.CommitOrderRequest;
import android.R.string;
import android.text.TextUtils;
/**
 * @ClassName: ServiceBean
 * @Description: 预约服务相关
 * @author: lee
 * @date: 2015年11月8日 下午6:00:38
 */
public class ServiceBean {
	//服务码
	public String codeTypeName;
	public String codeTf;
	public String codeRf;
	public String codeHl;
	
	// 钱
	public String priceType;
	public String priceTf;
	public String priceRf;
	public String priceHl;
	
	// 时长
	public String timeType;
	public String timeTf;
	public String timeRf;
	public String timeHl;
	
	// 时长
	public String materialidType = "0";
	public String materialidTf;
	public String materialidRf;
	public String materialidHl;
	
	
	
	/**
	 * @Title: getCode
	 * @Description: 获取服务编码串
	 * @author: lee
	 * @return
	 * @return: String
	 */
	public String getCodes(){
		StringBuffer sb = new StringBuffer();
		if (!TextUtils.isEmpty(codeTypeName)) {
			sb.append(codeTypeName).append("|");
		}
		if (!TextUtils.isEmpty(codeTf)) {
			sb.append(codeTf).append("|");
		}
		if (!TextUtils.isEmpty(codeRf)) {
			sb.append(codeRf).append("|");
		}
		if (!TextUtils.isEmpty(codeHl)) {
			sb.append(codeHl).append("|");
		}
		String str = "";
		try {
			str = sb.toString().substring(0, sb.toString().lastIndexOf("|"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}
	
	/**
	 * @Title: getTotalPrice
	 * @Description: 获取服务总价
	 * @author: lee
	 * @return
	 * @return: String
	 */
	public String getTotalPrice(){
		float price = 0f;
		try {
			price+=Float.parseFloat(priceType);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			price+=Float.parseFloat(priceTf);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			price+=Float.parseFloat(priceRf);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			price+=Float.parseFloat(priceHl);
		} catch (Exception e) {
			// TODO: handle exception
		}
		String str = "0.00";
		try {
			str = String.format("%.2f" ,price);
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return str;
	}
	/**
	 * @Title: getTotalTimes
	 * @Description: 总时长
	 * @author: lee
	 * @return
	 * @return: String
	 */
	public String getTotalTimes(){
		int times = 0;
		try {
			times+=Integer.parseInt(timeType);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			times+=Integer.parseInt(timeTf);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			times+=Integer.parseInt(timeRf);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			times+=Integer.parseInt(timeHl);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return times+"";
	}
	/**
	 * @Title: getMaterialId
	 * @Description:获取服务项信息
	 * @author: lee
	 * @return
	 * @return: List<CommitOrderRequest.Item>
	 */
	public List<CommitOrderRequest.Item> getMaterialId(){
		List<CommitOrderRequest.Item> items = new ArrayList<CommitOrderRequest.Item>();
		if (!TextUtils.isEmpty(materialidType)) {
			items.add(new CommitOrderRequest.Item(codeTypeName, timeType, priceType, materialidType));
		}
		if (!TextUtils.isEmpty(materialidTf)) {
			items.add(new CommitOrderRequest.Item(codeTf, timeTf, priceTf, materialidTf));
		}
		if (!TextUtils.isEmpty(materialidRf)) {
			items.add(new CommitOrderRequest.Item(codeRf, timeRf, priceRf, materialidRf));
		}
		if (!TextUtils.isEmpty(materialidHl)) {
			items.add(new CommitOrderRequest.Item(codeHl, timeHl, priceHl, materialidHl));
		}
		return items;
	}
	/**
	 * @Title: strPriceSum
	 * @Description: 价格计算
	 * @author: lee
	 * @param price1
	 * @param price2
	 * @return
	 * @return: String
	 */
	public static String strPriceSum(String price1,String price2){
		String sum = "0.00";
		try {
			sum = Float.parseFloat(price1)+Float.parseFloat(price2)+"";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sum;
	}
	
}
