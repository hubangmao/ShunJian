package cc.ruit.shunjianmei.home.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.AdditionalOrderApi;
import cc.ruit.shunjianmei.net.request.AdditionalOrderRequest;
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
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.title.TitleUtil;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: ModifyOrderFragment
 * @Description: 确认修改订单
 * @author: 欧阳
 * @date: 2015年10月22日 下午1:50:52
 */
public class ModifyOrderFragment extends BaseFragment {
	@ViewInject(R.id.ed_modifyorder_price)
	EditText price;// 改单价格
	@ViewInject(R.id.ed_modifyorder_result)
	EditText result;// 原因
	@ViewInject(R.id.ll_keybord)
	LinearLayout ll_keybord;//关闭键盘

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.modify_order_fragment, null);
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
		titleUtil.tv_title.setText("改单");
		// 设置标题栏左边的图片
		titleUtil.iv_left.setVisibility(View.VISIBLE);
		titleUtil.iv_left.setImageResource(R.drawable.back);
		titleUtil.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog();
			}
		});
		setPricePoint(price);
	}

	AlertDialog dialog;

	/**
	 * 
	 * @Title: alertDialog
	 * @Description: 询问对话框
	 * @author: 欧阳
	 * @return: void
	 */
	protected void alertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("是否放弃对改单信息编辑");
		builder.setPositiveButton("继续编辑",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				((OrderActivity)activity).hideSoftInput();
				if (!FragmentManagerUtils.back(activity, R.id.content_frame)) {
					activity.finish();
				}
			}
		});
		// 最重要的代码
		dialog = builder.show();

	}

	String orderId;

	/**
	 * 
	 * @Title: initData
	 * @Description: 得到id
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		if (this.getArguments() != null) {
			orderId = this.getArguments().getString("OrderID").toString();
		}

	}

	@OnClick(R.id.btn_modifyorder_submit)
	public void submit(View v) {
		String orderprice = price.getText().toString();
		String orderresult = result.getText().toString();
		switch (v.getId()) {
		case R.id.btn_modifyorder_submit:
			if (TextUtils.isEmpty(orderprice)) {
				ToastUtils.showShort("改单价格不能为空");
				return;
			} else if (TextUtils.isEmpty(orderresult)) {
				ToastUtils.showShort("改单的原因不能为空");
				return;
			} else {
				sendNetRequest(orderprice, orderresult);
			}
			break;
//		case R.id.ll_keybord:
//			InputMethodManager imm = (InputMethodManager)activity.getSystemService(activity.INPUT_METHOD_SERVICE);  
//		      imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
//			break;
		default:
			break;
		}
	}
	/**
	 * 
	 * @Title: setPricePoint
	 * @Description: 金额输入框中控制用户输入小数2位
	 * @author: 
	 * @param editText
	 * @return: void
	 */
	public static void setPricePoint(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					editText.setText(s);
					editText.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText(s.subSequence(0, 1));
						editText.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

		});

	}
	/**
	 * 
	 * @Title: sendNetRequest
	 * @Description: 提交改单的接口
	 * @author: 欧阳
	 * @param orderprice
	 * @param orderresult
	 * @return: void
	 */
	private void sendNetRequest(String orderprice, String orderresult) {
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		} else {
			AdditionalOrderRequest request = new AdditionalOrderRequest(
					UserManager.getUserID() + "", orderId, orderresult,
					orderprice);
			AdditionalOrderApi.AdditionalOrder(request,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
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
								EventBus.getDefault().post(new MyEventBus("已经加单了"));
								ToastUtils.showShort("提交成功");
								((OrderActivity)activity).hideSoftInput();
								FragmentManagerUtils.back(activity,
										R.id.content_frame);

							}
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							ToastUtils.showShort(activity.getResources()
									.getString(R.string.request_failure));
						}
					});
		}
	}
}
