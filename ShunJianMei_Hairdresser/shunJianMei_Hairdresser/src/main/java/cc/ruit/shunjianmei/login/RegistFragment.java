package cc.ruit.shunjianmei.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.jpush.JPushUtil;
import cc.ruit.shunjianmei.jpush.MyTagAliasCallback;
import cc.ruit.shunjianmei.net.api.UserInfoApi;
import cc.ruit.shunjianmei.net.request.UserLoginRequest;
import cc.ruit.shunjianmei.usermanager.UserInfo;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.contentcheck.ParmsUtil;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: LoginFragment
 * @Description: 登录页面
 * @author: lee
 * @date: 2015年10月9日 下午8:38:26
 */
public class RegistFragment extends BaseFragment implements
		OnClickListener {
	@ViewInject(R.id.iv_title_back)
	ImageView iv_back;// 返回按钮
	@ViewInject(R.id.et_username)
	EditText et_name;// 用户名
	@ViewInject(R.id.et_password)
	EditText et_password;// 密码
	@ViewInject(R.id.btn_login)
	EditText btn_login;// 登录
	@ViewInject(R.id.tv_forgetpassword)
	TextView tv_figetpassword;// 忘记密码
	@ViewInject(R.id.tv_regist)
	TextView tv_regist;// 注册
	@ViewInject(R.id.tv_use)
	TextView tv_use;// 立即使用

	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("LoginFragment"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("LoginFragment"); 
	}
	
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.login_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		return view;
	}
	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: lee
	 * @return: void
	 */
	private void initTitle() {
//		TitleUtil title = new TitleUtil(view);
//		title.iv_left.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});
////		title.iv_left.setImageResource(R.drawable.back);
//		title.iv_left.setVisibility(View.VISIBLE);
//		title.tv_title.setText("手机登录");
		Bundle bundle = getArguments();
		String from = bundle.getString("from", "");
		if (WelcomeFragment.class.getSimpleName().equals(from)) {
			iv_back.setVisibility(View.INVISIBLE);
		}else {
			iv_back.setVisibility(View.VISIBLE);
		}
	}
	
	@OnClick({  R.id.btn_login ,R.id.iv_title_back,R.id.tv_forgetpassword,R.id.tv_regist,R.id.tv_use})
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_login:
			if (checkUserLoginParems()) {
				userLogin(et_name.getText().toString(),et_password.getText().toString());
			}
			break;
		case R.id.iv_title_back:
			((LoginActivity)activity).hideSoftInput();
			FragmentManagerUtils.back(getActivity(), R.id.content_frame);
			break;
		case R.id.tv_forgetpassword:
			FragmentManagerUtils.back(getActivity(), R.id.content_frame);
			break;
		case R.id.tv_regist:
			FragmentManagerUtils.back(getActivity(), R.id.content_frame);
			break;
		case R.id.tv_use:
			FragmentManagerUtils.back(getActivity(), R.id.content_frame);
			break;

		default:
			break;
		}
	}

	
	/**
	 * @Title: checkParems
	 * @Description: 检测参数
	 * @author: lee
	 * @return: boolean true表示所有参数无异常，false则反之
	 */
	boolean checkParems() {
		// 获取验证码
		String mobile = et_name.getText().toString();
		if (TextUtils.isEmpty(mobile)) {
			ToastUtils.showShort("手机号码不能为空!");
			return false;
		}
		if (!ParmsUtil.checkPhone_2(mobile)) {
			ToastUtils.showShort("手机号码格式错误!");
			return false;
		}
		return true;
	}
	/**
	 * @Title: checkParems
	 * @Description: 检测参数
	 * @author: lee
	 * @return: boolean true表示所有参数无异常，false则反之
	 */
	boolean checkUserLoginParems() {
		// 获取手机号
		String mobile = et_name.getText().toString();
		if (TextUtils.isEmpty(mobile)) {
			ToastUtils.showShort("手机号码不能为空!");
			return false;
		}
		if (!ParmsUtil.checkPhone_2(mobile)) {
			ToastUtils.showShort("手机号码格式错误!");
			return false;
		}
		//获取密码
		String code = et_password.getText().toString();
		if (TextUtils.isEmpty(code)) {
			ToastUtils.showShort("密码不能为空!");
			return false;
		}
		return true;
	}
	/**
	 * @Title: userLogin
	 * @Description: 用户登录
	 * @author: lee
	 * @param mobile 手机号
	 * @param code 验证码
	 * @return: void
	 */
	private void userLogin(String mobile, String code) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}
		LoadingDailog.show(activity, "正在登录，请稍后");
		UserLoginRequest request = new UserLoginRequest(mobile,code);
		UserInfoApi.userLogin(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
				BaseResponse result = BaseResponse
						.getBaseResponse(responseInfo.result);
				if (result==null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1]+"");
				}
				if (result.getCode() == 1000) {
					MobclickAgent.onEvent(activity,"login_success");
					UserInfo info = UserInfo.getclazz(result.getData());
					if (info != null && "1".equals(info.getState())) {
						JPushUtil.setAlias("" + info.getUserID(),
								new MyTagAliasCallback());
						UserManager.updateUserinfo(activity, info);
						UserManager.setUserID(info.getUserID());
						startActivity(MainActivity.getIntent(activity));
						activity.finish();
					} else {
						ToastUtils.showShort("账号异常，请联系客服");
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity,"login_failure");
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(
						R.string.request_failure));
				
			}
		});
	}
}
