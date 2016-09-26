package cc.ruit.shunjianmei.usermanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import cc.ruit.shunjianmei.MainApplication;
import cc.ruit.shunjianmei.db.DbUtil;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.jpush.JPushUtil;
import cc.ruit.shunjianmei.net.response.MessageListResponse;
import cc.ruit.utils.sdk.SPUtils;

import com.lidroid.xutils.util.LogUtils;
import com.oruit.widget.messagedialog.MessageDialog;

public class UserManager {
	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	public static int getUserID() {
		return SPUtils.getInt("UserID", 0);
	}

	/**
	 * 存储详情图片ID
	 * 
	 * @param id
	 */
	public static void setdetailphotoID(int detail) {
		SPUtils.putInt("detail", detail);
	}
	/**
	 * 获取详情图片ID
	 * 
	 * @return
	 */
	public static int getdetailphotoID() {
		return SPUtils.getInt("detail", 0);
	}
	/**
	 * 获取城市ID
	 * 
	 * @return
	 */
	public static String getCityID() {
		return SPUtils.getString("CityID", "1");
	}

	/**
	 * 存储城市id
	 * 
	 * @param id
	 */
	public static void setCityID(String id) {
		SPUtils.putString("CityID", id);
	}

	/**
	 * 存储简介图片ID
	 * 
	 * @param id
	 */
	public static void setjianjiephotoID(int jianjie) {
		SPUtils.putInt("jianjie", jianjie);
	}

	/**
	 * 获取简洁图片ID
	 * 
	 * @return
	 */
	public static int getjianjiephotoID() {
		return SPUtils.getInt("jianjie", 0);
	}

	/**
	 * 获取城市经度
	 * 
	 * @return
	 */
	public static float getLatitude() {
		return SPUtils.getFloat("Latitude", 39.8414970000f);
	}

	/**
	 * 存储城市纬度
	 * 
	 * @param id
	 */
	public static void setLatitude(float Latitude) {
		SPUtils.putFloat("Latitude", Latitude);
	}

	/**
	 * 获取城市经度
	 * 
	 * @return
	 */
	public static float getlongitude() {
		return SPUtils.getFloat("longitude", 116.2909490000f);
	}

	/**
	 * 存储城市纬度
	 * 
	 * @param id
	 */
	public static void setlongitude(float Longitude) {
		SPUtils.putFloat("longitude", Longitude);
	}

	/**
	 * 存储用户id
	 * 
	 * @param id
	 */
	public static void setUserID(int id) {
		SPUtils.putInt("UserID", id);
	}

	/**
	 * 
	 * @Title: getHairStyle
	 * @Description: 获取发型类型的json数据
	 * @author: Johnny
	 * @return: void
	 */
	// public static String getHairStyle(){
	// return SPUtils.getString("HairStyle", null);
	// }

	/**
	 * 
	 * @Title: setHairStyle
	 * @Description: 存储发型类型的json数据
	 * @author: Johnny
	 * @param json
	 * @return: void
	 */
	// public static void setHairStyle(String json){
	// SPUtils.putString("HairStyle", json);
	// }

	/**
	 * 获取用户信息
	 * 
	 * @param ctx
	 * @return
	 */
	public static UserInfo getUserInfo(Context ctx) {
		Object userInfo = DbUtil.findById(ctx, UserInfo.class, getUserID());
		if (userInfo != null) {
			return (UserInfo) userInfo;
		}
		LogUtils.i("userinfo is null");
		return null;
	}

	/**
	 * 保存用户信息
	 * 
	 * @param ctx
	 * @param info
	 */
	public static void updateUserinfo(Context ctx, UserInfo info) {
		if (info == null) {
			return;
		}
		UserInfo oldInfo = getUserInfo(ctx);
		if (oldInfo != null) {
			if (info.getUserID() != 0)
				oldInfo.setUserID(info.getUserID());
			if (!TextUtils.isEmpty(info.getPhoto()))
				oldInfo.setPhoto(info.getPhoto());
			if (!TextUtils.isEmpty(info.getState()))
				oldInfo.setState(info.getState());
			if (!TextUtils.isEmpty(info.getNickName()))
				oldInfo.setNickName(info.getNickName());
			if (!TextUtils.isEmpty(info.getBindphone()))
				oldInfo.setBindphone(info.getBindphone());
			if (!TextUtils.isEmpty(info.getSex()))
				oldInfo.setSex(info.getSex());
			DbUtil.saveOrUpdate(ctx, oldInfo);
		} else {
			DbUtil.saveOrUpdate(ctx, info);
		}
	}

	/**
	 * 清空用户id
	 */
	public static void clearUserID() {
		SPUtils.putInt("UserID", 0);
	}

	/**
	 * 退出登录
	 */
	public static void logout(Context ctx) {
		clearUserID();
		JPushUtil.setlogout();
		SPUtils.putBoolean("LogInState", false);
		SPUtils.putInt("Toggle", 1);
		SPUtils.putString("userphone", "");
		SPUtils.putBoolean("isOpen", true);
		SPUtils.putString("close", "");
		DbUtil.deleteAllObj(ctx, UserInfo.class);
		DbUtil.deleteAllObj(ctx, MessageListResponse.class);
		// SharedPreferencesUtils.setData(ctx, "");
		// EMChatManager.getInstance().logout();
		// DbUtil.deleteAllObj(ctx, ThridInfo.class);
		try {
			if (MainActivity.getInstance() != null) {
				MainActivity.getInstance().finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		MainApplication.closeAllActivity();
	}

	/**
	 * 
	 * @Title: updataResetData
	 * @Description: 升级后开启引导页，退出登录
	 * @return: void
	 */
	public static void updataResetData(Context context) {
		// 退出登录
		logout(context);
		SPUtils.putInt("first_enter", 0);
	}

	/**
	 * 
	 * @Title: showCommentDailog
	 * @Description: 没有评论对话框显示
	 * @author: 欧阳
	 * @return: void
	 */
	public static void showCommentDailog(final FragmentActivity activity, final String phone) {
		final MessageDialog dialog = new MessageDialog(activity);
		dialog.setMessage(phone);
		dialog.getCancelButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.setOkButtonInfo("呼叫");
		dialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri Storeuri = Uri.parse("tel:" + phone);
				Intent Storeintent = new Intent();
				Storeintent.setAction(Intent.ACTION_CALL);
				Storeintent.setData(Storeuri);
				activity.startActivity(Storeintent);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 存储个人信息
	 * 
	 * @param picID
	 *            头像
	 */
	public static void setPicID(int picID) {
		SPUtils.putInt("picID", picID);
	}

	public static int getPicID() {
		return SPUtils.getInt("picID", 0);
	}

	/**
	 * 存储个人信息
	 * 
	 * @param picID1
	 *            第一张形象照片
	 * 
	 */
	public static void setPicID1(int picID1) {
		SPUtils.putInt("picID1", picID1);
	}

	public static int getPicID1() {
		return SPUtils.getInt("picID1", 0);
	}

	/**
	 * 存储个人信息
	 * 
	 * @param picID2
	 *            第二张形象照片
	 */
	public static void setPicID2(int picID2) {
		SPUtils.putInt("picID2", picID2);
	}

	public static int getPicID2() {
		return SPUtils.getInt("picID2", 0);
	}

	/**
	 * 存储个人信息
	 * 
	 * @param picID3
	 *            第三张形象照片
	 */
	public static void setPicID3(int picID3) {
		SPUtils.putInt("picID3", picID3);
	}

	public static int getPicID3() {
		return SPUtils.getInt("picID3", 0);
	}

}
