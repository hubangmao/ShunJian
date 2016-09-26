package cc.ruit.shunjianmei.home.me;

import java.util.List;

import android.text.TextUtils;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.CashRecordApi;
import cc.ruit.shunjianmei.net.api.MyBalanceApi;
import cc.ruit.shunjianmei.net.request.CashRecordRequest;
import cc.ruit.shunjianmei.net.request.MyBalanceRequest;
import cc.ruit.shunjianmei.net.response.MyBalanceResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.messagedialog.MessageDialog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: MeFragment
 * @Description: 用户中心
 * @author: lee
 * @date: 2015年10月9日 下午8:38:26
 */
public class WithdrawFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.tv_moneynum_mybalance)
	private TextView tv_moneynum;// 账户余额
	@ViewInject(R.id.tv_canwithdraw_mybalance)
	private TextView tv_canwithdraw;// 可提取余额
	@ViewInject(R.id.et_money_withdraw)
	private EditText et_money;// 提现金额
	@ViewInject(R.id.et_account_withdraw)
	private EditText et_account;// 银行账户
	@ViewInject(R.id.et_name_withdraw)
	private EditText et_name;// 银行账户姓名
	@ViewInject(R.id.et_bank_withdraw)
	private EditText et_bank;// 开户行

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WithdrawFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WithdrawFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.withdraw_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		getMyBalanceData();
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
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isBack = FragmentManagerUtils.back(activity,
						R.id.content_frame);
				if (!isBack) {
					activity.finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText("提现");

	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 提交按钮的点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@OnClick({ R.id.btn_commit })
	@Override
	public void onClick(View v) {
		if (Float.parseFloat(tv_canwithdraw.getText().toString()
				.replace("¥", "").trim()) <= 0) {
			showAlertDialog();
		} else {
			getData();
		}
	}

	private void showAlertDialog() {
		final MessageDialog messageDialog = new MessageDialog(activity);
		messageDialog.setMessage("余额不足");
		messageDialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				messageDialog.dismiss();

			}
		});
		messageDialog.setCancelButtonGone(true);
		messageDialog.show();
	}

	/**
	 * 
	 * @Title: getMyBalanceData
	 * @Description: 请求接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getMyBalanceData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "数据加载中，请稍后");
		MyBalanceRequest request = new MyBalanceRequest("" + userId);
		MyBalanceApi.myBalance(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
				BaseResponse result = BaseResponse
						.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<MyBalanceResponse> response = MyBalanceResponse
							.getclazz2(result.getData());
					resultMyBalanceHanlder(response);
				} else {
					tv_moneynum.setText("0.0");
					tv_canwithdraw.setText("¥" + 0.00);
					// myBalance = "0.0";
					// canWithDraw = "0.0";
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "mybalance_failure");
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(
						R.string.request_failure));

			}
		});

	}

	/**
	 * @Title: resultMyBalanceHanlder
	 * @Description: 结果处理
	 * @author: lee
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultMyBalanceHanlder(List<MyBalanceResponse> info) {
		if (info == null) {
			return;
		}
		if (info == null||info.size()<=0) {
			LogUtils.e("MyBalanceResponse err");
			tv_moneynum.setText("0.0");
			tv_canwithdraw.setText("¥" + 0.00);
			return;
		}
		// myBalance = info.getTotal();
		// canWithDraw = info.getAmount();
		tv_moneynum.setText("¥"+info.get(0).getTotal());
		tv_canwithdraw.setText("¥"+info.get(0).getAmount());

	}

	/**
	 * 
	 * @Title: getData
	 * @Description: 请求接口
	 * @author: Johnny
	 * @return: void
	 */
	private void getData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		String cardID = et_account.getText().toString().trim();
		String name = et_name.getText().toString().trim();
		String bank = et_bank.getText().toString().trim();
		String amount = et_money.getText().toString().trim();

		if (checkParems(cardID, name, bank, amount)) {
			LoadingDailog.show(activity, "申请提交中，请稍后~");
			CashRecordRequest request = new CashRecordRequest("" + userId,
					name, cardID, bank, amount);
			CashRecordApi.cashRecord(request, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					LoadingDailog.dismiss();
					BaseResponse result = BaseResponse
							.getBaseResponse(responseInfo.result);
					if (result == null) {
						return;
					}
					String[] split = result.getMsg().split("\\|");
					if ("1".equals(split[0])) {
						ToastUtils.showShort(split[1] + "");
					}
					if (result.getCode() == 1000) {
//						showSuccessDialog();
//						getMyBalanceData();
						boolean isBack = FragmentManagerUtils.back(activity,
								R.id.content_frame);
						if (isBack) {
							EventBus.getDefault().post(new MyEventBus("MyBalance"));
//							activity.finish();
						}
					}

				}

				@Override
				public void onFailure(HttpException error, String msg) {
					MobclickAgent.onEvent(activity, "login_failure");
					LoadingDailog.dismiss();
					showErrDialog();
				}
			});
		}
	}

	/**
	 * @Title: checkParems
	 * @Description: 检测参数
	 * @author: Johnny
	 * @return: boolean true表示所有参数无异常，false则反之
	 */
	boolean checkParems(String cardID, String name, String bank, String amount) {

		if (TextUtils.isEmpty(cardID)) {
			ToastUtils.showShort("银行卡账户不能为空!");
			return false;
		}
		if (TextUtils.isEmpty(name)) {
			ToastUtils.showShort("银行卡户名不能为空!");
			return false;
		}
		if (TextUtils.isEmpty(bank)) {
			ToastUtils.showShort("开户行不能为空!");
			return false;
		}
		if (TextUtils.isEmpty(amount)) {
			ToastUtils.showShort("提现金额不能为空!");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @Title: showSuccessDialog
	 * @Description: 当用户点击联系主厨的按钮时，弹出此对话框
	 * @author: Johnny
	 * @return: void
	 */
	private void showSuccessDialog() {
		final MessageDialog dialog = new MessageDialog(activity);
		dialog.setMessage("提现申请提交成功！");
		dialog.getOkButton().setText("确定");
		dialog.setCancelButtonGone(true);
		dialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}

		});
		dialog.show();
	}

	/**
	 * 
	 * @Title: showErrDialog
	 * @Description: 当用户点击联系主厨的按钮时，弹出此对话框
	 * @author: Johnny
	 * @return: void
	 */
	private void showErrDialog() {
		final MessageDialog dialog = new MessageDialog(activity);
		dialog.setMessage("提现失败，请联系客服！");
		dialog.getOkButton().setText("确定");
		dialog.setCancelButtonGone(true);
		dialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		dialog.show();
	}

}
