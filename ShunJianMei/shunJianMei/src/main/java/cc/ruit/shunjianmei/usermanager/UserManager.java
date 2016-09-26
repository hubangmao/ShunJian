package cc.ruit.shunjianmei.usermanager;

import java.util.Set;

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
import cn.jpush.android.api.TagAliasCallback;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.util.LogUtils;
import com.oruit.widget.messagedialog.MessageDialog;

public class UserManager {
	/**
	 * 获取用户ID
	 * 
	 * @return 用户初次使用且未登录且非游客时userid为-1 ，用户为游客模式时为0，正常登录显示Userid
	 */
	public static int getUserID() {
		return SPUtils.getInt("UserID", -1);
	}

	/**
	 * 存储用户id
	 * 
	 * @param id
	 */
	public static void setUserID(int id) {
		SPUtils.putInt("UserID", id);
		if (id>0) {
			JPushUtil.setAlias(""+id, new TagAliasCallback() {
				
				@Override
				public void gotResult(int arg0, String arg1, Set<String> arg2) {
					LogUtils.i(arg0+"   "+arg1+"    "+arg2);
				}
			});
		}
	}
	/**
	 * 获取城市ID
	 * 
	 * @return
	 */
	public static int getCityID() {
		return SPUtils.getInt("CityID", 1);
	}

	/**
	 * 存储城市id
	 * 
	 * @param id
	 */
	public static void setCityID(int id) {
		SPUtils.putInt("CityID", id);
	}
	/**
	 * 获取城市经度
	 * 
	 * @return
	 */
	public static float getLatitude(){
		return SPUtils.getFloat("Latitude",39.8414970000f);
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
	public static float getlongitude(){
		return SPUtils.getFloat("longitude", 116.2909490000f);
	}

	/**
	 * 存储城市纬度
	 * 
	 * @param id
	 */
	public static void setlongitude(float Longitude) {
		SPUtils.putFloat("longitude",  Longitude);
	}
	/**
	 * 
	 * @Title: getPhone
	 * @Description: 获取存储的客服电话号码
	 * @return
	 * @return: String
	 */
	public static String getPhone() {
		return SPUtils.getString("Phone", "");
	}
	/**
	 * 
	 * @Title: setUserID
	 * @Description: 存储客服电话号码
	 * @param id
	 * @return: void
	 */
	public static void setPhone(String phone) {
		SPUtils.putString("Phone", phone);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param ctx
	 * @return
	 */
	public static UserInfo getUserInfo(Context ctx) {
		Object userInfo = DbUtil.findById(ctx,UserInfo.class, getUserID());
		if (userInfo!=null) {
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
		if (info==null) {
			return;
		}
		UserInfo oldInfo = getUserInfo(ctx);
		if (oldInfo!=null) {
			if(info.getUserID()!=0)oldInfo.setUserID(info.getUserID());
			if(!TextUtils.isEmpty(info.getNickName()))oldInfo.setNickName(info.getNickName());
			if(!TextUtils.isEmpty(info.getSex()))oldInfo.setSex(info.getSex());
			if(!TextUtils.isEmpty(info.getPhoto()))oldInfo.setPhoto(info.getPhoto());
			if(!TextUtils.isEmpty(info.getMobile()))oldInfo.setMobile(info.getMobile());
			if(!TextUtils.isEmpty(info.getBalance()))oldInfo.setBalance(info.getBalance());
			if(!TextUtils.isEmpty(info.getCoupons()))oldInfo.setCoupons(info.getCoupons());
			if(!TextUtils.isEmpty(info.getFavoriteNum()))oldInfo.setFavoriteNum(info.getFavoriteNum());
			DbUtil.saveOrUpdate(ctx, oldInfo);
		}else {
			DbUtil.saveOrUpdate(ctx, info);
		}
	}
	/**
	 * 清空用户id
	 */
	public static void clearUserID() {
		SPUtils.putInt("UserID", -1);
	}

	/**
	 * 保存登录状态
	 */
	public static void saveLoginIn() {
		SPUtils.putBoolean("LogInState", true);
	}

	/**
	 * 判断是否登录
	 */
	public static boolean isLogin() {

		return SPUtils.getBoolean("LogInState", false);
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
		DbUtil.deleteAllObj(ctx,UserInfo.class);
		DbUtil.deleteAllObj(ctx, MessageListResponse.class);
//		SharedPreferencesUtils.setData(ctx, "");
//		EMChatManager.getInstance().logout();
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
	public static void showCommentDailog(final FragmentActivity activity,final String phone) {
		final MessageDialog dialog=new MessageDialog(activity);
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
		//tv_comment.setVisibility(View.VISIBLE);
	}
}
