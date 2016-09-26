package cc.ruit.shunjianmei.pay;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sourceforge.simcpux.MD5;
import net.sourceforge.simcpux.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.alipay.PayResult;
import cc.ruit.shunjianmei.alipay.SignUtils;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.order.OrderActivity;
import cc.ruit.shunjianmei.home.order.OrderDetailFragment;
import cc.ruit.shunjianmei.net.api.MyBalanceApi;
import cc.ruit.shunjianmei.net.api.OrderPayApi;
import cc.ruit.shunjianmei.net.api.WalletPayApi;
import cc.ruit.shunjianmei.net.request.MyBalanceRequest;
import cc.ruit.shunjianmei.net.request.OrderPayRequest;
import cc.ruit.shunjianmei.net.request.WalletPayRequest;
import cc.ruit.shunjianmei.net.request.WechatPayRequest;
import cc.ruit.shunjianmei.net.response.MyBalanceResponse;
import cc.ruit.shunjianmei.net.response.OrderListResponse;
import cc.ruit.shunjianmei.net.response.OrderPayResponse;
import cc.ruit.shunjianmei.net.response.WXpayResponse;
import cc.ruit.shunjianmei.net.response.OrderPayResponse.Item;
import cc.ruit.shunjianmei.net.response.WechatPayResponse;
import cc.ruit.shunjianmei.reserve.EventOrderBean;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.view.MyListView;
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
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: OrderPayFragment
 * @Description: 支付订单界面
 * @author: Johnny
 * @date: 2015年10月9日 下午8:38:26
 */
public class OrderPayFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.rl_discount_pay)
	private RelativeLayout rl_discount;// 优惠减免
	@ViewInject(R.id.lv_pay)
	private MyListView lv_pay;

	@ViewInject(R.id.tv_money_pay)
	private TextView tv_money;// 支付金额
	@ViewInject(R.id.tv_totle_pay)
	private TextView tv_total;// 订单总额
	@ViewInject(R.id.tv_discount_pay)
	private TextView tv_discount;// 优惠减免
	@ViewInject(R.id.btn_balance_pay)
	private Button btn_balance;// 余额支付

	private ListViewAdapter orderInfoAdapter;
	private List<Item> orderInfoList = new ArrayList<OrderPayResponse.Item>();
	private String orderID;// 订单ID
	private String amount;// 实付金额

	/*--------------------------- 支付宝支付参数  begin-------------------------------*/

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	/*--------------------------- 支付宝支付参数  end-------------------------------*/

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OrderPayFragment"); // 支付订单
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderPayFragment");
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
		view = inflater.inflate(R.layout.pay_layout, null);
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
				boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.arrow_left_white);
		title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText("支付");
	}

	/**
	 * 
	 * @Title: initData
	 * @Description:
	 * @author: Johnny
	 * @return: void
	 */
	private void initData() {

		if (getArguments() != null) {
			orderID = getArguments().getString("OrderID");// 订单ID
		}
		EventBus.getDefault().register(this);// 注册
		orderInfoAdapter = new ListViewAdapter(activity, orderInfoList);
		lv_pay.setAdapter(orderInfoAdapter);
		getData();

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
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}

		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "数据加载中，请稍后");
		OrderPayRequest request = new OrderPayRequest("" + userId, orderID);
		OrderPayApi.orderPay(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<OrderPayResponse> response = OrderPayResponse.getclazz2(result.getData());
					resultHanlder(response);
				} else {
					LoadingDailog.dismiss();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "mybalance_failure");
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));

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
	void resultHanlder(List<OrderPayResponse> info) {
		if (info == null || info.size() <= 0) {
			LogUtils.e("OrderPayResponse err");
			LoadingDailog.dismiss();
			return;
		}

		OrderPayResponse obj = info.get(0);
		try {
			tv_money.setText(String.format("%.2f", Float.parseFloat(obj.getAmount())));
			amount = obj.getAmount();
			tv_total.setText("¥" + String.format("%.2f", Float.parseFloat(obj.getTotal())));
			tv_discount.setText("-¥" + String.format("%.2f", Float.parseFloat(obj.getDiscount())));
		} catch (Exception e) {
		}
		if (obj.getWallet().equals("1")) {
			getMyBalanceData();
		} else {
			LoadingDailog.dismiss();
			btn_balance.setText("余额支付(余额不足)");
			btn_balance.setEnabled(false);
		}
		orderInfoList.clear();
		orderInfoList.addAll(obj.getItem());
		orderInfoAdapter.notifyDataSetChanged();
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
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			LoadingDailog.dismiss();
			return;
		}
		int userId = UserManager.getUserID();
		MyBalanceRequest request = new MyBalanceRequest("" + userId);
		MyBalanceApi.myBalance(request, new RequestCallBack<String>() {

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
					List<MyBalanceResponse> response = MyBalanceResponse.getclazz2(result.getData());
					// MyBalanceResponse response = MyBalanceResponse
					// .getclazz(result.getData());
					resultMyBalanceHanlder(response);
				} else {
					btn_balance.setText("余额支付(余额不足)");
					btn_balance.setEnabled(false);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "mybalance_failure");
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));

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
		if (response == null || response.size() <= 0) {
			LogUtils.e("MyBalanceResponse err");
			return;
		}
		btn_balance.setText("余额支付(¥" + String.format("%.2f", Float.parseFloat(response.get(0).getTotal())) + ")");
		btn_balance.setEnabled(true);
	}

	/**
	 * 
	 * @ClassName: ListViewAdapter
	 * @Description: ListView的Adapter
	 * @author: Johnny
	 * @date: 2015年10月24日 上午11:03:43
	 */
	class ListViewAdapter extends BaseAdapter {

		private Context context;
		private Map<Integer, OrderPayResponse.Item> map = new HashMap<Integer, OrderPayResponse.Item>();
		private List<Item> list;

		public ListViewAdapter(Context context, List<Item> list) {
			this.context = context;
			this.list = list;
			map.put(0, new OrderPayResponse.Item("xjc", "设计剪发", "0.00", "0.00"));
			map.put(1, new OrderPayResponse.Item("tf", "烫发", "0.00", "0.00"));
			map.put(2, new OrderPayResponse.Item("rf", "染发", "0.00", "0.00"));
			map.put(3, new OrderPayResponse.Item("hl", "护理", "0.00", "0.00"));
		}

		@Override
		public void notifyDataSetChanged() {
			for (int i = 0; list != null && i < list.size(); i++) {
				String code = list.get(i).Code;
				if ("xjc".equals(code)) {
					map.put(0, list.get(i));
				} else if ("xc".equals(code)) {
					map.put(0, list.get(i));
				} else if ("tf".equals(code)) {
					map.put(1, list.get(i));
				} else if ("rf".equals(code)) {
					map.put(2, list.get(i));
				} else if ("hl".equals(code)) {
					map.put(3, list.get(i));
				}
			}
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Item getItem(int position) {
			return map.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.pay_listview_item, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLabel(position, vh);
			return convertView;
		}

		/**
		 * @Title: setLable
		 * @Description: TODO
		 * @author: Johnny
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLabel(int position, ViewHolder vh) {
			try {
				Item obj = getItem(position);
				vh.tv_left.setText(obj.Name);
				vh.tv_right.setText("¥" + String.format("%.2f", Float.parseFloat(obj.Amount)));
				vh.tv_middle.setText("优惠¥" + String.format("%.2f", Float.parseFloat(obj.Coupon)));
			} catch (Exception e) {
				LogUtils.e(e.getMessage());
			}
		}
	}

	/**
	 * 
	 * @ClassName: ViewHolder
	 * @Description: TODO
	 * @author: Johnny
	 * @date: 2015年11月2日 下午8:50:59
	 */
	static class ViewHolder {

		@ViewInject(R.id.tv_left_lv_pay)
		TextView tv_left;
		@ViewInject(R.id.tv_middle_lv_pay)
		TextView tv_middle;
		@ViewInject(R.id.tv_right_lv_pay)
		TextView tv_right;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
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
	@OnClick({ R.id.btn_zhifubao_pay, R.id.btn_weixin_pay, R.id.btn_balance_pay })
	public void onClick(View v) {

		switch (v.getId()) {
		// 支付宝支付
		case R.id.btn_zhifubao_pay:
			zhifubaoPay(v);
			break;

		// 微信支付
		case R.id.btn_weixin_pay:
			// ToastUtils.showLong("开发中...");
			// msgApi.registerApp(Constant.APP_ID);
			req = new PayReq();
			sb = new StringBuffer();
			weixinPay();

			// GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
			// getPrepayId.execute();
			break;
		// 余额支付
		case R.id.btn_balance_pay:
			balancePay();
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
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}
		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "正在支付");
		WalletPayRequest request = new WalletPayRequest("" + userId, orderID,
				tv_money.getText().toString().replace("¥", ""));
		WalletPayApi.walletPay(request, new RequestCallBack<String>() {

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
					showSuccessDialog();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "mybalance_failure");
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));

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
		EventBus.getDefault().post(new EventOrderBean(true));
		OrderListResponse bean = new OrderListResponse();
		bean.setOrderID(orderID);
		startActivity(OrderActivity.getIntent(activity, OrderDetailFragment.class.getName(), bean));
		activity.finish();
		/*
		 * final MessageDialog alertDialog = new MessageDialog(activity);
		 * alertDialog.setMessage("支付成功！");
		 * alertDialog.getOkButton().setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * EventBus.getDefault().post(new EventOrderBean(true));
		 * OrderListResponse bean = new OrderListResponse();
		 * bean.setOrderID(orderID);
		 * startActivity(OrderActivity.getIntent(activity,
		 * OrderDetailFragment.class.getName(), bean)); activity.finish(); } });
		 * alertDialog.setCancelButtonGone(true);
		 * alertDialog.setCancelable(false); alertDialog.show();
		 */
	}

	/*--------------------------------------支付宝支付 begin------------------------------------*/
	/**
	 * 
	 * @Title: zhifubaoPay
	 * @Description: 支付宝支付
	 * @author: Johnny
	 * @param v
	 * @return: void
	 */
	private void zhifubaoPay(View v) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}
		pay(v);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			LoadingDailog.dismiss();
			isPaying = false;
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				// String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// Toast.makeText(activity, "支付成功",
					// Toast.LENGTH_SHORT).show();
					showSuccessDialog();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(activity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
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
		String orderInfo = Constant.getOrderInfo(orderID, "顺间", "订单", amount, Constant.notify_url);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + Constant.getSignType();

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
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	// public String getOutTradeNo() {
	// SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
	// Locale.getDefault());
	// Date date = new Date();
	// String key = format.format(date);
	//
	// Random r = new Random();
	// key = key + r.nextInt();
	// key = key.substring(0, 15);
	// return key;
	// }

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, Constant.RSA_PRIVATE);
	}

	/*------------------------------------------------支付宝支付 end--------------------------------------------------*/

	/*------------------------------------------------微信支付 begin--------------------------------------------------*/
	private static final String TAG = "MicroMsg.SDKSample.PayActivity";// Log日志的tag标签
	IWXAPI msgApi;
	PayReq req;
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
			ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
			return;
		}
		int userId = UserManager.getUserID();
		LoadingDailog.show(activity, "支付准备中~");
		WechatPayRequest request = new WechatPayRequest("" + userId, orderID);
		OrderPayApi.WechatPay(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
				if (result == null) {
					LoadingDailog.dismiss();
					return;
				}
				String[] split = result.getMsg().split("\\|");
				if ("1".equals(split[0])) {
					ToastUtils.showShort(split[1] + "");
				}
				if (result.getCode() == 1000) {
					List<WechatPayResponse> response = WechatPayResponse.getclazz2(result.getData());
					resultWechatPayHanlder(response);
				} else {
					LoadingDailog.dismiss();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LoadingDailog.dismiss();
				LogUtils.e(msg, error);
			}
		});
	}

	protected void resultWechatPayHanlder(List<WechatPayResponse> response) {
		if (response != null && response.size() > 0) {
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
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
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
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
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
	public void onEventMainThread(BaseResp resp) {
		if (resp != null) {
			switch (resp.errCode) {
			case 0:// 成功 展示成功页面
				showSuccessDialog();
				break;
			case -1:// 错误
					// 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
				ToastUtils.showLong("支付发生异常~");
				break;
			case -2:// 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
				break;
			default:
				break;
			}
		}
	}

	/*------------------------------------------------微信支付 end--------------------------------------------------*/
}
