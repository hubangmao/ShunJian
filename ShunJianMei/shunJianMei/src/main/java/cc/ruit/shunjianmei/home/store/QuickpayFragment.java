package cc.ruit.shunjianmei.home.store;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.sourceforge.simcpux.MD5;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cc.ruit.shunjianmei.MainApplication;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.R.color;
import cc.ruit.shunjianmei.alipay.PayResult;
import cc.ruit.shunjianmei.alipay.SignUtils;
import cc.ruit.shunjianmei.baidumap.LocationActivity;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.login.LoginFragment;
import cc.ruit.shunjianmei.net.api.QuickpayWithAlipayApi;
import cc.ruit.shunjianmei.net.api.QuickpayWithWechatApi;
import cc.ruit.shunjianmei.net.api.SaveFavoriteApi;
import cc.ruit.shunjianmei.net.api.StartQuickPayApi;
import cc.ruit.shunjianmei.net.api.QuickpayWithWalletApi;
import cc.ruit.shunjianmei.net.request.QuickpayWithAlipayRequest;
import cc.ruit.shunjianmei.net.request.QuickpayWithWechatRequest;
import cc.ruit.shunjianmei.net.request.SaveFavoriteRequest;
import cc.ruit.shunjianmei.net.request.StartQuickPayRequest;
import cc.ruit.shunjianmei.net.request.WalletPayRequest;
import cc.ruit.shunjianmei.net.response.StartQuickPayResponse;
import cc.ruit.shunjianmei.net.request.QuickpayWithWalletRequest;
import cc.ruit.shunjianmei.net.response.QuickpayWithAlipayResponse;
import cc.ruit.shunjianmei.net.response.QuickpayWithWalletResponse;
import cc.ruit.shunjianmei.net.response.QuickpayWithWechatResponse;
import cc.ruit.shunjianmei.net.response.StoreDetailResponse.Images;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.Util;
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
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.oruit.widget.messagedialog.MessageDialog;
import com.oruit.widget.title.TitleUtil;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: QuickpayFragment
 * @Description: 闪惠界面
 * @author: jiazhaohui
 */
public class QuickpayFragment extends BaseFragment implements
		OnClickListener {

	@ViewInject(R.id.quickpay_store_name)
	private TextView quickpay_store_name;		// 美发店名称
	@ViewInject(R.id.quickpay_input_money)
	private EditText quickpay_input_money;		// 应付金额的输入框
	@ViewInject(R.id.quickpay_tips)
	private TextView quickpay_tips;		// 支付提示的输入框
	@ViewInject(R.id.quickpay_realpay)
	private TextView quickpay_realpay;		// 实付款
	@ViewInject(R.id.quickpay_minus)
	private TextView quickpay_minus;		// 优惠额
	@ViewInject(R.id.btn_balance_quickpay)
	private Button btn_balance_quickpay;		// 余额支付按钮
	@ViewInject(R.id.quickpay_master)
	private LinearLayout quickpay_master;		// 显示美发师的区域
	@ViewInject(R.id.select_quickpay_master)
	private TextView select_quickpay_master;	// 显示选择的美发师的控件
	
	AlertDialog dialog;			// 弹出的对话框
	
	QuickpayWithAlipayResponse alipayResponse;		// 点击支付宝支付，返回值
	private boolean isAlipaying = false;			// 支付宝正在支付
	
	QuickpayWithWechatResponse wechatResponse;		// 点击微信支付，返回值
	
	
	public int selectPosition = -1;
	public int tmpPosition = -1;
	

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	
	private StartQuickPayResponse obj;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Quickpay"); // 统计页面
		
		
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Quickpay");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.quickpay_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		initData();
		return view;
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: Johnny
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(view);
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isBack = FragmentManagerUtils.back(getActivity(),
						R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
		title.iv_right.setVisibility(View.GONE);
		title.tv_title.setText("优惠支付");
	}

	/**
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {
		EventBus.getDefault().register(this);//注册
		getData();
		// 设置编辑框的响应函数
		final EditText editText = (EditText) view.findViewById(R.id.quickpay_input_money);
		editText.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				setAmount(s.toString());
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	// add by jiazhaohui
	// 设置金额变化
	public void setAmount(String origin)
	{
		if (obj != null && origin != null)
		{
			try 
			{
				float fo = Float.parseFloat(origin);
				if (fo > 0)
				{
					float enough = Float.parseFloat(obj.getEnoughMoney());
					float minus = Float.parseFloat(obj.getCouponPayCustom());
					int times = (int) (fo / enough);
					float allminus = times * minus;
					float shouldpay = fo - allminus;
					// 设置钱数
					quickpay_realpay.setText("¥" + String.valueOf(shouldpay));
					quickpay_minus.setText("¥" + String.valueOf(allminus));
				}	
			}
			catch(Exception e)
			{
				quickpay_realpay.setText("¥0");
				quickpay_minus.setText("¥0");
			}
		}
	}

	/**
	 * @Title: getData
	 * @Description: 获取数据
	 * @author: Johnny
	 * @return: void
	 */
	public void getData() {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}

		LoadingDailog.show(activity, "数据加载中，请稍后");
		int UserId = UserManager.getUserID();
		String StoreID = getArguments().getString("ID");
		
		StartQuickPayRequest request = new StartQuickPayRequest("" + UserId, StoreID);
		StartQuickPayApi.startQuickPay(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				new LoadingViewUtil(view).hint();
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

					List<StartQuickPayResponse> response = StartQuickPayResponse
							.getclazz2(result.getData());
					resultHanlder(response);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				new LoadingViewUtil(view).hint();
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(
						R.string.request_failure));
			}
		});
	}

	/**
	 * @Title: resultHanlder
	 * @Description: 结果处理
	 * @author: Johnny
	 * @param listcontrolSignUp
	 * @return: void
	 */
	void resultHanlder(List<StartQuickPayResponse> info) {
		if (info == null) {
			LogUtils.e("StartQuickPayResponse err");
			return;
		}

		obj = info.get(0);
		// 设置美发店的名字
		String name = getArguments().getString("Name");
		quickpay_store_name.setText(name);
		String tips = obj.getCouponPayCusDesc();
		quickpay_tips.setText(tips);
		// 设置我的余额
		btn_balance_quickpay.setText("余额支付(¥"
				+ String.format("%.2f",
						Float.parseFloat(obj.getWallet())) + ")");
		btn_balance_quickpay.setEnabled(true);
		selectPosition = -1;
	}

	// 判断目前的输入是否可以提交支付
	private boolean inputIsRight()
	{
		if (selectPosition < 0)
		{
			// 提示请选择美发师
			ToastUtils.showShort("请选择美发师");
			return false;
		}
		String content = quickpay_input_money.getText().toString();
		try
		{
			float payment = Float.parseFloat(content);
			if (payment > 0)
			{
				return true;
			}
			else
			{
				// 提示请输入正确的支付金额
				ToastUtils.showShort("请输入正确的支付金额");
				return false;
			}
		}
		catch(Exception e)
		{
			// 提示请输入正确的支付金额
			ToastUtils.showShort("请输入正确的支付金额");
			return false;
		}
	}
	
	// add by jiazhaohui
	// 得到应该支付的钱
	private float getShouldPayAmount()
	{
		String content = quickpay_realpay.getText().toString();
		try
		{
			content = content.substring(1);
			float payment = Float.parseFloat(content);
			return payment;
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	// add by jiazhaohui
	// 钱包支付
	private void payWithWallet()
	{
		if (!inputIsRight())
		{
			return;
		}
		if (obj != null && Float.parseFloat(obj.getWallet()) < getShouldPayAmount())
		{
			ToastUtils.showShort("钱包金额不足");
			return;
		}
		
		// 调用钱包支付接口
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}
		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "正在支付");
		String StoreID = getArguments().getString("ID");
		String masterName = select_quickpay_master.getText().toString();
		String originalPrice = quickpay_input_money.getText().toString();
		
		QuickpayWithWalletRequest request = new QuickpayWithWalletRequest("" + userId, StoreID, masterName, originalPrice);
		QuickpayWithWalletApi.QuickpayWithWallet(request, new RequestCallBack<String>() {

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
					// 支付成功
					List<QuickpayWithWalletResponse> response = QuickpayWithWalletResponse
							.getclazz2(result.getData());
					processWalletSuc(response);
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
	
	private void processWechatSuc(List<QuickpayWithWechatResponse> response)
	{
		if (!response.isEmpty())
		{
			wechatResponse = response.get(0);
			if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
				ToastUtils.showShort(activity.getResources().getString(
						R.string.no_networks_found));
				return;
			}
			
			startWechatPay();
		}
	}
	
	///////////////////////////////////////////////////////////////
	// 微信支付相关
	private static final String TAG = "MicroMsg.SDKSample.PayActivity";// Log日志的tag标签
	IWXAPI msgApi;
	PayReq req;
	private StringBuffer sb;
	private String prepay_id;

	private void startWechatPay()
	{
		prepay_id = wechatResponse.getWechatPayID();
		req = new PayReq();
		sb = new StringBuffer();
		genPayReq();
		sendPayReq();
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
	 * @param bean
	 * @return: void
	 */
	public void onEventMainThread(BaseResp resp){
		if (resp!=null) {
//			String str = "返回码：" + resp.errCode;
			//ToastUtils.showLong(str);
			switch (resp.errCode) {
			case 0://成功 展示成功页面
				gotoResult(wechatResponse.getOrderID(), wechatResponse.getStoreName(), wechatResponse.getMasterName(),
						wechatResponse.getPayAmount(), wechatResponse.getReduceAmount());
				ToastUtils.showLong("支付成功 ");
				break;
			case -1://错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
				ToastUtils.showLong("支付发生异常~");
				break;
			case -2://用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
				ToastUtils.showLong("支付取消");
				break;
			default:
				ToastUtils.showLong("未知错误");
				break;
			}
		}
	}
	
	private void showSuccessDialog()
	{
		final MessageDialog alertDialog = new MessageDialog(activity);
		alertDialog.setMessage("微信支付成功！");
		alertDialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoResult(wechatResponse.getOrderID(), wechatResponse.getStoreName(), wechatResponse.getMasterName(),
						wechatResponse.getPayAmount(), wechatResponse.getReduceAmount());
			}
		});
		alertDialog.setCancelButtonGone(true);
		alertDialog.setCancelable(false);
		alertDialog.show();
	}
	
	/*------------------------------------------------微信支付 end--------------------------------------------------*/
	
	///////////////////////////////////////////////////////////////
	
	
	// 点击支付宝支付，收到了服务器的回调，预处理
	private void processAlipaySuc(List<QuickpayWithAlipayResponse> response)
	{
		if (!response.isEmpty())
		{
			alipayResponse = response.get(0);
			if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
				ToastUtils.showShort(activity.getResources().getString(
						R.string.no_networks_found));
				return;
			}
			
			startAlipay();
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			LoadingDailog.dismiss();
			isAlipaying = false;
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				// String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// ToastUtils.showShort("支付宝支付成功");
					// 跳转到支付完成结果的界面
					gotoResult(alipayResponse.getOrderID(), alipayResponse.getStoreName(),
							alipayResponse.getMasterName(), alipayResponse.getPayAmount(), alipayResponse.getReduceAmount());
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
	
	// 真正的支付宝支付
	private void startAlipay()
	{
		if (isAlipaying) {
			return;
		}
		isAlipaying = true;
		// 订单
		String orderInfo = Constant.getOrderInfo(alipayResponse.getOrderID(), 
				"顺间", "订单", alipayResponse.getPayAmount(), Constant.quickpayNotifyUrl);
		// 对订单做RSA 签名
		String sign = SignUtils.sign(orderInfo, Constant.RSA_PRIVATE);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ Constant.getSignType();

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
	
	private void processWalletSuc(List<QuickpayWithWalletResponse> response)
	{
		if (!response.isEmpty())
		{
			// 将数据塞入，跳转界面
			QuickpayWithWalletResponse resp = response.get(0);
			gotoResult(resp.getOrderID(), resp.getStoreName(), resp.getMasterName(), resp.getPayAmount(),
					resp.getReduceAmount());
		}
	}
	
	private void gotoResult(String orderID, String storeName, String masterName, String payAmount, String reduceAmount)
	{
		Fragment fragment = FragmentManagerUtils.getFragment(activity,
				QuickpayResultFragment.class.getName());
		Fragment origin = FragmentManagerUtils.getFragment(activity,
				QuickpayFragment.class.getName());
		
		Bundle bundle = new Bundle();
		bundle.putString("OrderID", orderID);
		bundle.putString("StoreName", storeName);
		bundle.putString("MasterName", masterName);
		bundle.putString("PayAmount", payAmount);
		bundle.putString("ReduceAmount", reduceAmount);
		String StoreID = getArguments().getString("ID");
		bundle.putString("StoreID", StoreID);
		fragment.setArguments(bundle);
//		FragmentManagerUtils.replaceFramgment(activity, R.id.content_frame, fragment, origin);
		FragmentManagerUtils.add(activity, R.id.content_frame, fragment, false);
	}
	
	// 支付宝支付
	private void payWithAlipay()
	{
		if (!inputIsRight())
		{
			return;
		}
		
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}
		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "正在支付");
		String StoreID = getArguments().getString("ID");
		String masterName = select_quickpay_master.getText().toString();
		String originalPrice = quickpay_input_money.getText().toString();
		
		QuickpayWithAlipayRequest request = new QuickpayWithAlipayRequest("" + userId, StoreID, masterName, originalPrice);
		QuickpayWithAlipayApi.QuickpayWithAlipay(request, new RequestCallBack<String>() {

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
					// 支付成功
					List<QuickpayWithAlipayResponse> response = QuickpayWithAlipayResponse
							.getclazz2(result.getData());
					processAlipaySuc(response);
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
	
	// 微信支付
	private void payWithWechat()
	{
		if (!inputIsRight())
		{
			return;
		}
		
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}
		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "正在支付");
		String StoreID = getArguments().getString("ID");
		String masterName = select_quickpay_master.getText().toString();
		String originalPrice = quickpay_input_money.getText().toString();
		
		QuickpayWithWechatRequest request = new QuickpayWithWechatRequest("" + userId, StoreID, masterName, originalPrice);
		QuickpayWithWechatApi.QuickpayWithWechat(request, new RequestCallBack<String>() {

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
					// 支付成功
					List<QuickpayWithWechatResponse> response = QuickpayWithWechatResponse
							.getclazz2(result.getData());
					processWechatSuc(response);
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
	
	// 点击，确定选中了美发师
	private void selectConfirmMaster()
	{
		// 选中了selectPosition位置
		if (selectPosition >= 0)
		{
			if (obj != null)
			{
				String[] masters = obj.getMasterList().split("\\|");
				String masterName = masters[selectPosition];
				select_quickpay_master.setText(masterName);
				int black = activity.getResources().getColor(R.color.black33);
				select_quickpay_master.setTextColor(black);
			}
		}
	}
	
	// 点中了选择美发师
	private void selectMaster()
	{
		if (obj != null)
		{
			LayoutInflater inflater = LayoutInflater.from(activity);
			View MasterView = inflater.inflate(R.layout.quickpay_master_select_layout, null);
			Button cancel = (Button) MasterView
					.findViewById(R.id.btn_master_select_cancel);
			Button sumbit = (Button) MasterView
					.findViewById(R.id.btn_master_select_sumbit);
			
			// 设置美发师列表List
			ListView masterList = (ListView)MasterView.findViewById(R.id.master_select_list);
			if (obj.getMasterList() != null)
			{
				String[] masters = obj.getMasterList().split("\\|");
				masterList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				final MasterListAdapter mlAdapter = new MasterListAdapter(activity.getApplicationContext(), masters);
				masterList.setAdapter(mlAdapter);
			}
			
			AlertDialog.Builder Masterbuilder = new AlertDialog.Builder(activity);
			Masterbuilder.setView(MasterView);
			Masterbuilder.setCancelable(true);
			
			cancel.setOnClickListener(new OnClickListener() {// 取消

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			sumbit.setOnClickListener(new OnClickListener() {// 确认

				@Override
				public void onClick(View v) {
					// 选中了某一个美发师
					if (tmpPosition >= 0)
					{
						selectPosition = tmpPosition;
					}
					dialog.dismiss();
					selectConfirmMaster();
				}
			});
			
			dialog = Masterbuilder.show();
		}
	}
	
	
	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.btn_balance_quickpay, R.id.btn_zhifubao_quickpay,
			R.id.btn_weixin_quickpay, R.id.quickpay_master})
	public void onClick(View v) {
		if (obj==null) {
			return;
		}
		FragmentManager fm = getChildFragmentManager();
		android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
		switch (v.getId())
		{
			// 点击了钱包支付
			case R.id.btn_balance_quickpay:
				payWithWallet();
				break;
			// 点击了支付宝支付
			case R.id.btn_zhifubao_quickpay:
				payWithAlipay();
				break;
			// 点击了微信支付
			case R.id.btn_weixin_quickpay:
				payWithWechat();
				break;
			// 点中了选择美发师
			case R.id.quickpay_master:
				selectMaster();
				break;
		}		
	}
	
	// add by jiazhaohui
	// 美发师选择列表的Adapter
	class MasterListAdapter extends BaseAdapter
	{
		private Context context;
		private String[] masterList;
		private List<View> viewList = new ArrayList<View>();
		
		public MasterListAdapter(Context context,
				String[] masterlist) {
			this.context = context;
			this.masterList = masterlist;
			tmpPosition = -1;
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return masterList == null ? 0 : masterList.length;
		}

		@Override
		public String getItem(int position) {
			return masterList[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewitemHolder vh = null;
			String name = getItem(position);
			if (convertView == null) 
			{
				vh = new ViewitemHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.master_listview_item, null);
				vh.findView(convertView);
				convertView.setTag(vh);
				viewList.add(convertView);
			} 
			else 
			{
				vh = (ViewitemHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			final int gray = activity.getResources().getColor(R.color.gray_ab);
			final int red = activity.getResources().getColor(R.color.red_e5);
			vh.master_name.setText(name);
			if (position == selectPosition)
			{
				vh.master_name.setTextColor(red);
			}
			else
			{
				vh.master_name.setTextColor(gray);
			}
			
			//////
			final TextView nameTxt = vh.master_name;
			final int ifPos = position; 
			
			vh.master_item.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// 重置，确保最多只有一项被选中
					clearAll();
					nameTxt.setTextColor(red);
					tmpPosition = ifPos;
				}
			});

			return convertView;
		}
		
		// 重置所有字体
		private void clearAll()
		{
			for (int i = 0; i < viewList.size(); i++)
			{
				View fv = viewList.get(i);
				final TextView name = (TextView)fv.findViewById(R.id.master_name);
				if (name != null)
				{
					int gray = activity.getResources().getColor(R.color.gray_ab);
					name.setTextColor(gray);
				}
			}
		}		
	}
	
	//////////////////////////////////
	// add by jiazhaohui
	// item布局类
	static class ViewitemHolder {

		@ViewInject(R.id.master_item)
		LinearLayout master_item;
		@ViewInject(R.id.master_name)
		TextView master_name;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}
}
