package cc.ruit.shunjianmei.home.order;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.sourceforge.simcpux.MD5;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.alipay.PayResult;
import cc.ruit.shunjianmei.alipay.SignUtils;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.api.MyBalanceApi;
import cc.ruit.shunjianmei.net.api.OrderPayApi;
import cc.ruit.shunjianmei.net.api.WalletPayApi;
import cc.ruit.shunjianmei.net.request.MyBalanceRequest;
import cc.ruit.shunjianmei.net.request.WalletPayRequest;
import cc.ruit.shunjianmei.net.request.WechatPayRequest;
import cc.ruit.shunjianmei.net.response.MyBalanceResponse;
import cc.ruit.shunjianmei.net.response.WechatPayResponse;
import cc.ruit.shunjianmei.reserve.EventOrderBean;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.alipay.sdk.app.PayTask;
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
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: OrderModifyFragment
 * @Description: 确认修改订单
 * @author: 欧阳
 * @date: 2015年10月22日 下午1:50:52
 */
public class OrderModifyFragment extends BaseFragment {
	@ViewInject(R.id.tv_modifyorder_ordercode)
	TextView ordercode;//订单编号
	@ViewInject(R.id.ed_modifyorder_price)
	TextView tv_price;// 改单价格
	@ViewInject(R.id.ed_modifyorder_result)
	TextView result;// 原因
	String orderId;//订单号
	@ViewInject(R.id.btn_modify_balance_pay)
	Button btn_balance;// 余额支付
	private String price;//价格
	
	/*-----支付宝支付参数------*/
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onStop() {
		super.onStop();
		LoadingDailog.dismiss();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.order_modify_fragment, null);
		ViewUtils.inject(this, view);
		initTitle();
		initData();
		return view;
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题
	 * @author: 欧阳
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil titleUtil = new TitleUtil(view);
		// 设置标题栏中间的文字
		titleUtil.tv_title.setText("改单支付");
		// 设置标题栏左边的图片
		titleUtil.iv_left.setVisibility(View.VISIBLE);
		titleUtil.iv_left.setImageResource(R.drawable.back);
		titleUtil.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!FragmentManagerUtils.back(activity, R.id.content_frame)) {
					activity.finish();
				}
			}
		});
	}
	
	/**
	 * 
	 * @Title: initData
	 * @Description: 得到id
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		EventBus.getDefault().register(this);//注册
		if (this.getArguments() != null) {
			orderId = this.getArguments().getString("AdditionalCode").toString();
			ordercode.setText("订单号:"+this.getArguments().getString("AdditionalCode").toString());
			tv_price.setText("¥"+this.getArguments().getString("AdditionalAmount").toString());
			price = this.getArguments().getString("AdditionalAmount").toString();
			result.setText(this.getArguments().getString("AdditionalContent").toString());
		}
		getMyBalanceData();
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
			LoadingDailog.dismiss();
			return;
		}
		int userId = UserManager.getUserID();
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
					 List<MyBalanceResponse> response = MyBalanceResponse.getclazz2(result.getData());
//					MyBalanceResponse response = MyBalanceResponse
//							.getclazz(result.getData());
					resultMyBalanceHanlder(response);
				} else {
					btn_balance.setText("余额支付(余额不足)");
					btn_balance.setEnabled(false);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "mybalance_failure");
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
	void resultMyBalanceHanlder(List<MyBalanceResponse> response) {
		if (response == null||response.size()<=0) {
			LogUtils.e("MyBalanceResponse err");
			return;
		}
		try {
			String price = tv_price.getText().toString().replace("¥", "");
			String total = response.get(0).getTotal();
			if (Float.parseFloat(total)-Float.parseFloat(price)>=0) {
				btn_balance.setText("余额支付(¥"+String.format("%.2f" ,Float.parseFloat(response.get(0).getTotal()))+")");
				btn_balance.setEnabled(true);
			}else {
				btn_balance.setText("余额支付(余额不足)");
				btn_balance.setEnabled(false);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * 
	 * @Title: submit
	 * @Description: 进行支付
	 * @author: 欧阳
	 * @param v
	 * @return: void
	 */
	@OnClick({R.id.btn_modify_balance_pay,R.id.btn_modify_zhifubao_pay,R.id.btn_modify_weixin_pay})
	public void onClick(View v) {
//		String orderprice = tv_price.getText().toString();
//		String AdditionalCode = ordercode.getText().toString();
		switch (v.getId()) {
		case R.id.btn_modify_balance_pay://余额支付
			balancePay();
			break;
		case R.id.btn_modify_zhifubao_pay://支付宝支付
			pay(v);
			break;
		case R.id.btn_modify_weixin_pay://微信支付
			req = new PayReq();
			sb = new StringBuffer();
			weixinPay();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title: balancePay
	 * @Description: 余额支付
	 * @author: Johnny
	 * @return: void
	 */
	private void balancePay() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "正在支付");
		WalletPayRequest request = new WalletPayRequest("" + userId, orderId,
				tv_price.getText().toString().replace("¥", ""));
		WalletPayApi.walletPay(request, new RequestCallBack<String>() {

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
					showSuccessDialog();
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
	 * @Title: showAlertDialog
	 * @Description: 支付成功
	 * @author: Johnny
	 * @return: void
	 */
	private void showSuccessDialog() {
		final MessageDialog alertDialog = new MessageDialog(activity);
		alertDialog.setMessage("支付成功！");
		alertDialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new EventOrderBean(true));
				if (!FragmentManagerUtils.back(activity, R.id.content_frame)) {
					activity.finish();
				}
				alertDialog.dismiss();
			}
		});
		alertDialog.setCancelButtonGone(true);
		alertDialog.setCancelable(false);
		alertDialog.show();
	}
	/* TODO ------------------------------------------------支付宝支付 begin--------------------------------------------------*/
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			LoadingDailog.dismiss();
			isPaying = false;
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
//					Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
					showSuccessDialog();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT)
								.show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT)
								.show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(activity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
						.show();
				break;
			}
			default:
				break;
			}
		};
	};
	private boolean isPaying = false;

	public void pay(View v) {
		if (isPaying) {
			return;
		}
		isPaying = true; 
		// 订单
		String orderInfo = getOrderInfo("顺间", "订单", price);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		LoadingDailog.show(activity, "正在前往支付宝");
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(activity);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(activity);
		String version = payTask.getVersion();
		Toast.makeText(activity, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" +  Constant.PARTNER + "\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" +  Constant.SELLER + "\"";
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";
		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" +Constant.additionalNotifyUrl + "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		LogUtils.i(orderInfo);
		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content,  Constant.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/*------------------------------------------------支付宝支付 end--------------------------------------------------*/
	/*------------------------------------------------微信支付 begin--------------------------------------------------*/
	private static final String TAG = "MicroMsg.SDKSample.PayActivity";// Log日志的tag标签
	PayReq req;
	IWXAPI msgApi;
	private StringBuffer sb;
	private String prepay_id;

	/**
	 * 
	 * @Title: weixinPay
	 * @Description: 微信支付
	 * @author: Johnny
	 * @return: void
	 */
	private void weixinPay() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}
		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "支付准备中~");
		WechatPayRequest request = new WechatPayRequest("" + userId, orderId);
		OrderPayApi.WechatPay(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = BaseResponse
						.getBaseResponse(responseInfo.result);
				if (result == null) {
					LoadingDailog.dismiss();
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<WechatPayResponse> response = WechatPayResponse
							.getclazz2(result.getData());
					resultWechatPayHanlder(response);
				}else {
					LoadingDailog.dismiss();
				}
			}	

			@Override
			public void onFailure(HttpException error, String msg) {
				LoadingDailog.dismiss();
				LogUtils.e(msg,error);
			}
		});
	}

	protected void resultWechatPayHanlder(List<WechatPayResponse> response) {
		if (response!=null&&response.size()>0) {
			prepay_id = response.get(0).getWechatPayID();
			genPayReq();
			sendPayReq();
		}
	}

	/**
	 * 
	 * @Title: genNonceStr
	 * @Description: 生成随机字符串，不长于32位
	 * @author: Johnny
	 * @return
	 * @return: String
	 */
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}


	/**
	 * 
	 * @Title: genPayReq
	 * @Description: 生成签名参数
	 * @author: Johnny
	 * @return: void
	 */
	private void genPayReq() {

		req.appId = Constant.APP_ID;
		req.partnerId = Constant.MCH_ID;
		req.prepayId = prepay_id;
		// 扩展字段,暂填写固定值Sign=WXPay
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		// 生成时间戳
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n" + req.sign + "\n\n");


	}

	/**
	 * 
	 * @Title: genTimeStamp
	 * @Description: 得到当前时间
	 * @author: Johnny
	 * @return
	 * @return: long
	 */
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 
	 * @Title: genAppSign
	 * @Description: 生成签名
	 * @author: Johnny
	 * @param params
	 * @return
	 * @return: String
	 */
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constant.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return appSign;
	}

	/**
	 * 
	 * @Title: sendPayReq
	 * @Description: TODO
	 * @author: Johnny
	 * @return: void
	 */
	private void sendPayReq() {
		msgApi = WXAPIFactory.createWXAPI(activity, null);
		msgApi.registerApp(Constant.APP_ID);
		msgApi.sendReq(req);
	}
	/**
	 * @Title: onEventMainThread
	 * @Description: eventbus内容处理
	 * @author: lee
	 * @return: void
	 */
	public void onEventMainThread(BaseResp resp){
		if (resp!=null) {
			switch (resp.errCode) {
			case 0://成功 展示成功页面
				showSuccessDialog();
				break;
			case -1://错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
				ToastUtils.showLong("支付发生异常~");
				break;
			case -2://用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
				break;
			default:
				break;
			}
		}
	}
	
	/*------------------------------------------------微信支付 end--------------------------------------------------*/
	
}
