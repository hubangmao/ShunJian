package cc.ruit.shunjianmei.home.me;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.messagedialog.MessageDialog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.BrowserActivity;
import cc.ruit.shunjianmei.home.me.feedback.FeedbackActivity;
import cc.ruit.shunjianmei.home.me.feedback.FeedbackFragment;
import cc.ruit.shunjianmei.home.order.OrderActivity;
import cc.ruit.shunjianmei.home.order.OrderFragment;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.net.api.MeApi;
import cc.ruit.shunjianmei.net.request.MeRequest;
import cc.ruit.shunjianmei.net.response.MeResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

/**
 * @ClassName: MeFragment
 * @Description: 用户中心
 * @author: lee
 * @date: 2015年10月9日 下午8:38:26
 */
public class MeFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.rl_dresserinfo_me)
	private RelativeLayout rl_dresserinfo;// 美发师信息的布局
	@ViewInject(R.id.rl_balance_me)
	private RelativeLayout rl_balance;// 余额的布局
	@ViewInject(R.id.rl_jiedan_me)
	private RelativeLayout rl_jiedan;// 接单的布局
	@ViewInject(R.id.rl_evaluate_me)
	private RelativeLayout rl_evaluate;// 评论的布局
	@ViewInject(R.id.rl_basicinfo_me)
	private RelativeLayout rl_basicinfo;// 我的基本信息的布局
	@ViewInject(R.id.rl_levelinfo_me)
	private RelativeLayout rl_levelinfo;// 我的定级信息的布局
	@ViewInject(R.id.rl_feedback_me)
	private RelativeLayout rl_feedback;// 用户反馈的布局
	@ViewInject(R.id.rl_aboutus_me)
	private RelativeLayout rl_aboutus;// 关于我们的布局
	@ViewInject(R.id.rl_update_me)
	private RelativeLayout rl_update;// 检测更新的布局

	@ViewInject(R.id.iv_dresserphoto_me)
	private ImageView iv_userPhoto;// 美发师姓名
	@ViewInject(R.id.tv_dressername_me)
	private TextView tv_dressername;// 美发师姓名
	@ViewInject(R.id.tv_gender_me)
	private TextView tv_gender;// 美发师性别
	@ViewInject(R.id.tv_phone_me)
	private TextView tv_phone;// 美发师电话
	@ViewInject(R.id.tv_autherize_me)
	private TextView tv_autherize;// 已认证标签
	@ViewInject(R.id.tv_id_me)
	private TextView tv_id;// 美发师ID
	@ViewInject(R.id.tv_balance_me)
	private TextView tv_balance;// 余额
	@ViewInject(R.id.tv_jiedan_me)
	private TextView tv_jiedan;// 接单数量
	@ViewInject(R.id.tv_evaluate_me)
	private TextView tv_evaluate;// 评论数量

	EmptyView ev;// 空载页
	boolean isFirstLoading = true;

	public void onResume() {
		super.onResume();
		getData();

		MobclickAgent.onPageStart("MeFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MeFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.me_layout, null);
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
		TitleUtil title = new TitleUtil(view);
		// title.iv_left.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		// title.iv_left.setImageResource(R.drawable.back);
		// title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText("我的");
		// 将美发师名字设置为粗体
		TextPaint tp = tv_dressername.getPaint();
		tp.setFakeBoldText(true);
	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 请求接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		if (isFirstLoading) {
			LoadingDailog.show(activity, "数据加载中，请稍后");
			isFirstLoading = false;
		}
		MeRequest request = new MeRequest("" + userId);
		MeApi.homePage(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");

				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<MeResponse> data = MeResponse.getclazz2(result.getData());
					if (data != null) {
						// 设置网络请求的数据
						setData(data);
					} else {
						ToastUtils.showShort("请求数据异常");
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "login_failure");
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));

			}
		});

	}

	/**
	 * 
	 * @Title: setData
	 * @Description: 设置网络请求的数据
	 * @author: Johnny
	 * @return: void
	 */
	private void setData(List<MeResponse> data) {
		MeResponse obj = data.get(0);
		tv_dressername.setText(obj.getNickName());
		// tv_gender.setText("2".equals(obj.getSex())?"女":"男");
		tv_phone.setText(obj.getBindPhone());
		// tv_id.setText(obj.getUserID());
		tv_balance.setText("¥" + (TextUtils.isEmpty(obj.getBalance()) ? "0" : obj.getBalance()));
		tv_jiedan.setText(TextUtils.isEmpty(obj.getOrderNum()) ? "0" : obj.getOrderNum());
		tv_evaluate.setText(TextUtils.isEmpty(obj.getEvaluateNum()) ? "0" : obj.getEvaluateNum());

		tv_balance.setVisibility(View.VISIBLE);
		tv_jiedan.setVisibility(View.VISIBLE);
		tv_evaluate.setVisibility(View.VISIBLE);

		tv_autherize.setBackgroundResource(
				obj.getCheckState().equals("2") ? R.drawable.me_autherized_btn_bg : R.drawable.mybalance_charge_btn_bg);
		tv_autherize.setText(obj.getCheckState().equals("2") ? "已认证" : "未认证");
		tv_autherize.setVisibility(View.VISIBLE);

		if ("1".equals(UserManager.getUserInfo(activity).getSex())) {
			RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
		} else {
			RoundImageLoaderUtil.setErrImage(R.drawable.tx_woman, R.drawable.tx_woman, R.drawable.tx_woman);
		}
		RoundImageLoaderUtil.getInstance(activity, 500).loadImage(obj.getPhoto(), iv_userPhoto);
	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.rl_balance_me, R.id.rl_jiedan_me, R.id.rl_pricelist_me, R.id.rl_feedback_me, R.id.rl_aboutus_me,
			R.id.rl_update_me, R.id.rl_logout_me, R.id.rl_evaluate_me, R.id.rl_liucheng_me, R.id.rl_dresserinfo_me })
	public void onClick(View v) {
		Intent in;
		switch (v.getId()) {
		// 余额
		case R.id.rl_balance_me:
			in = MyBalanceActivity.getIntent(getActivity(), MyBalanceFragment.class.getName());
			startActivity(in);
			break;
		// 接单
		case R.id.rl_jiedan_me:
			OrderFragment.requestMethod = Constant.ORDER_LIST_FINISH_MEHTOD;
			in = OrderActivity.getIntent(getActivity(), OrderFragment.class.getName());
			in.putExtra("fromeclassname", MeFragment.class.getSimpleName());
			startActivity(in);
			break;
		// 价目表
		case R.id.rl_pricelist_me:
			in = BrowserActivity.getIntent(activity, Constant.ME_PRICELIST, "价目表");
			startActivity(in);
			break;
		// 接单流程
		case R.id.rl_liucheng_me:
			in = BrowserActivity.getIntent(activity, Constant.ME_JIEDAN, "接单流程");
			startActivity(in);
			break;
		// 评论
		case R.id.rl_evaluate_me:
			in = EvaluateActivity.getIntent(activity, EvaluateFragment.class.getName());
			startActivity(in);
			break;
		// 用户反馈
		case R.id.rl_feedback_me:
			in = FeedbackActivity.getIntent(getActivity(), FeedbackFragment.class.getName());
			startActivity(in);
			break;
		// 关于我们
		case R.id.rl_aboutus_me:
			in = BrowserActivity.getIntent(activity, Constant.ABOUTME_NET,
					"关于我们");
			startActivity(in);
			break;
		// 检测更新
		case R.id.rl_update_me:

			break;
		// 个人信息界面
		case R.id.rl_dresserinfo_me:
			in = FeedbackActivity.getIntent(getActivity(), MyInformationFragment.class.getName());
			startActivity(in);
			break;
		// 退出登录
		case R.id.rl_logout_me:
			final MessageDialog builder = new MessageDialog(getActivity());
			builder.setMessage("确定要退出吗？");
			builder.setCancelButtonGone(false);
			builder.setOkButtonInfo("确定");
			builder.getOkButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					UserManager.logout(activity);
					Intent in = LoginActivity.getIntent(activity);
					startActivity(in);
				}
			});
			builder.setCancelButtonInfo("取消");
			builder.getCancelButton().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					builder.dismiss();
				}
			});
			builder.show();
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
		// String mobile = et_name.getText().toString();
		// if (TextUtils.isEmpty(mobile)) {
		// ToastUtils.showShort("手机号码不能为空!");
		// return false;
		// }
		// if (!ParmsUtil.checkPhone_2(mobile)) {
		// ToastUtils.showShort("手机号码格式错误!");
		// return false;
		// }
		return true;
	}

}
