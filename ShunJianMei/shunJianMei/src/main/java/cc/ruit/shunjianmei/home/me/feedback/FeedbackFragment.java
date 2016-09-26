package cc.ruit.shunjianmei.home.me.feedback;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.FeedbackApi;
import cc.ruit.shunjianmei.net.request.FeedbackRequest;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.contentcheck.ParmsUtil;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.oruitkey.OruitBase64;
import com.oruit.widget.messagedialog.MessageDialog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: FeedbackFragment
 * @Description: 用户反馈
 * @author: Johnny
 * @date: 2015年10月9日 下午8:38:26
 */
public class FeedbackFragment extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.et_phone_feedback)
	private EditText et_phone;// 用户手机号或Email
	@ViewInject(R.id.et_content_feedback)
	private EditText et_content;// 反馈信息

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FeedbackFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("FeedbackFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.feedback_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		initEditTextListener();
		return view;
	}

	private void initEditTextListener() {
		final EditText editText = (EditText) view.findViewById(R.id.et_content_feedback);
		editText.addTextChangedListener(new TextWatcher() {
			
			private CharSequence temp;//监听前的文本  
			private final int charMaxNum = 500; 
			
			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				temp = s;
			}
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				TextView textView = (TextView) view.findViewById(R.id.tv_last_count);
				textView.setText("" + (charMaxNum-s.length()) + "/" + charMaxNum);	
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
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
				boolean isBack = FragmentManagerUtils.back(getActivity(),
						R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText("用户反馈");
		
		et_phone.clearFocus();
	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: TODO
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@OnClick({ R.id.btn_commit_feedback })
	@Override
	public void onClick(View v) {
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
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			return;
		}
		String contact = et_phone.getText().toString().trim();
		if (!ParmsUtil.checkPhone_2(contact)&&!ParmsUtil.emailFormat(contact)) {
			ToastUtils.showShort("请输入正确的手机号或邮箱地址");
			return;
		}
		
		int userId = UserManager.getUserID();
		String content = et_content.getText().toString().trim();
		byte[] bytes = content.getBytes();
		String base64Content = OruitBase64.encode(bytes);
		if (checkParems(contact, content)) {
			FeedbackRequest request = new FeedbackRequest("" + userId,
					contact,base64Content);
			FeedbackApi.feedback(request, new RequestCallBack<String>() {

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
						showSuccessDialog();
					} else {
						showErrDialog();
					}

				}

				@Override
				public void onFailure(HttpException error, String msg) {
					MobclickAgent.onEvent(activity, "feedback_failure");
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
	boolean checkParems(String contact, String content) {

		if (TextUtils.isEmpty(contact)) {
			ToastUtils.showShort("联系方式不能为空!");
			return false;
		}
		if (TextUtils.isEmpty(content)) {
			ToastUtils.showShort("请输入反馈内容");
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
		dialog.setMessage("反馈成功！");
		dialog.getOkButton().setText("确定");
		dialog.setCancelButtonGone(true);
		dialog.getOkButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				activity.finish();
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
		dialog.setMessage("反馈失败，请联系客服！");
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
