package com.oruit.widget.messagedialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zachary.hodge.uicomp.R;

/**
 * 消息弹框工具类
 * 
 * @author lilw
 * @CrateTime 2012-11-27
 */
public class MessageDialog {
	private Dialog mDialog;
	private Context context;
	private String message;
	private String okButtonInfo;
	private String cancelButtonInfo;
	private boolean isShowing;
	private boolean isCancelable = false;
	private Button okButton;
	private Button cancelButton;
	private View view;
	private String title;
	private String color;
	private float size;
	public MessageDialog(Context context) {
		super();
		this.context = context;
		view = LayoutInflater.from(context).inflate(
				R.layout.uicomp_message_dialog, null);
		init();
	}

	public MessageDialog(Context context, View view) {
		super();
		this.context = context;
		this.view = view;
		init();
	}

	/**
	 * 初始化
	 * 
	 * @author lilw
	 * @CrateTime 2012-11-27
	 */
	private void init() {
		mDialog = new Dialog(context, R.style.uicomp_message_dialog);
		WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.horizontalWeight = 1;
		// mDialog.
		// mDialog.getWindow().getAttributes().horizontalWeight =
		// 1;//锟斤拷锟斤拷权锟斤拷为1
		mDialog.addContentView(view, params);
		isShowing = false;
	}

	public View getView() {
		return view;
	}

	/**
	 * 设置自定义的view内容
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @param view
	 *            要自定义的内容 警告： 设置自定义的View后，getButton（）、setButton（）
	 *            等原有布局里的view不能使用否则可能会报NnullPointException
	 */
	public void setView(View view) {
		WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
		params.width = ViewGroup.LayoutParams.FILL_PARENT;
		params.horizontalWeight = 1;
		// params.
		getDialog().getWindow().getAttributes().horizontalWeight = 1;
		// getDialog().addContentView(view, params);
		getDialog().setContentView(view, params);
		this.view = view;
		isShowing = false;
	}

	/**
	 * 获取弹框实例
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @return Dialog实例
	 */
	public Dialog getDialog() {
		return mDialog;
	}
	/**
	 * @Title: setDialogBg
	 * @Description: 设置dialog背景
	 * @author: lee
	 * @param bgres
	 * @return: void
	 */
	public void setDialogBg(int bgres){
		View ll = view.findViewById(R.id.uicomp_ll_dialog_bg);
		if (ll!=null) {
			ll.setBackgroundResource(bgres);
		}
	}
	/**
	 * 获取消息内容
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @return Messag内容
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 设置消息内容
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @param message
	 *            要显示的内容
	 */
	public void setMessage(String message) {
		this.message = message;
		TextView tv_message = (TextView) view
				.findViewById(R.id.textView_messagedialog_message);
		tv_message.setText(message);
	}
	public void setMessage(String message,int gravity) {
		this.message = message;
		TextView tv_message = (TextView) view
				.findViewById(R.id.textView_messagedialog_message);
		tv_message.setGravity(gravity);
		tv_message.setText(message);
	}
	public void setMessage(String message, String color) {
		this.message = message;
		this.color = color;
		TextView tv_message = (TextView) view
				.findViewById(R.id.textView_messagedialog_message);
		tv_message.setText(message);
		tv_message.setTextColor(Color.parseColor(color));
	}

	public void setMessage(String message, String color, float size) {
		this.message = message;
		this.color = color;
		this.size = size;
		TextView tv_message = (TextView) view
				.findViewById(R.id.textView_messagedialog_message);
		tv_message.setText(message);
		tv_message.setTextColor(Color.parseColor(color));
		tv_message.setTextSize(size);
		tv_message.setGravity(Gravity.LEFT);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
		TextView tv_title = (TextView) view
				.findViewById(R.id.textView_messagedialog_title);
		tv_title.setText(title);
		if (TextUtils.isEmpty(title)) {
			tv_title.setVisibility(View.GONE);
		} else {
			tv_title.setVisibility(View.VISIBLE);
		}
	}

	public void setTitle(String title, String color) {
		this.title = title;
		this.color = color;
		TextView tv_title = (TextView) view
				.findViewById(R.id.textView_messagedialog_title);
		tv_title.setText(title);
		tv_title.setTextColor(Color.parseColor(color));
		if (TextUtils.isEmpty(title)) {
			tv_title.setVisibility(View.GONE);
		} else {
			tv_title.setVisibility(View.VISIBLE);
		}
	}

	public void setTitle(String title, String color, float size) {
		this.title = title;
		this.color = color;
		this.size = size;
		TextView tv_title = (TextView) view
				.findViewById(R.id.textView_messagedialog_title);
		tv_title.setGravity(Gravity.CENTER);
		tv_title.setText(title);
		tv_title.setTextColor(Color.parseColor(color));
		tv_title.setTextSize(size);
		if (TextUtils.isEmpty(title)) {
			tv_title.setVisibility(View.GONE);
		} else {
			tv_title.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 获取确定按钮
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @return button 确定按钮
	 */
	public Button getOkButton() {
		if (okButton == null) {
			okButton = (Button) view.findViewById(R.id.button_meesagedialog_ok);
		}
		return okButton;
	}

	/**
	 * 设置确定按钮文字
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @param okButtonInfo
	 *            确定按钮
	 */
	public void setOkButtonInfo(String okButtonInfo) {
		this.okButtonInfo = okButtonInfo;
		getOkButton().setText(okButtonInfo);
		// TextView tv_okButton = (TextView) view
		// .findViewById(R.id.textView_meesagedialog_okInfo);
		// tv_okButton.setText(okButtonInfo);
	}

	public void setOkButtonInfo(String okButtonInfo, String color) {
		this.okButtonInfo = okButtonInfo;
		this.color = color;
		getOkButton().setText(okButtonInfo);
		getOkButton().setTextColor(Color.parseColor(color));
	}

	public void setOkButtonInfo(String okButtonInfo, String color, float size) {
		this.okButtonInfo = okButtonInfo;
		this.color = color;
		this.size = size;
		getOkButton().setText(okButtonInfo);
		getOkButton().setTextColor(Color.parseColor(color));
		getOkButton().setTextSize(size);
	}

	/**
	 * 获取取消按钮
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @return button 取消按钮
	 */
	public Button getCancelButton() {
		if (cancelButton == null) {
			cancelButton = (Button) view
					.findViewById(R.id.button_meesagedialog_cancel);
		}
		return cancelButton;
	}

	public void setCancelButtonGone(boolean isGone) {
		getCancelButton().setVisibility(isGone ? View.GONE : View.VISIBLE);
		view.findViewById(R.id.meesagedialog_btn_line).setVisibility(
				isGone ? View.GONE : View.VISIBLE);
	}

	/**
	 * 设置取消按钮文字
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @param cancelButtonInfo
	 *            要显示的文字
	 */
	public void setCancelButtonInfo(String cancelButtonInfo) {
		this.cancelButtonInfo = cancelButtonInfo;
		getCancelButton().setText(cancelButtonInfo);
		// TextView tv_okButton = (TextView) view
		// .findViewById(R.id.textView_meesagedialog_cancelInfo);
		// tv_okButton.setText(cancelButtonInfo);
	}

	public void setCancelButtonInfo(String cancelButtonInfo, String color) {
		this.cancelButtonInfo = cancelButtonInfo;
		this.color = color;
		getCancelButton().setText(cancelButtonInfo);
		getCancelButton().setTextColor(Color.parseColor(color));
	}

	public void setCancelButtonInfo(String cancelButtonInfo, String color,
			float size) {
		this.cancelButtonInfo = cancelButtonInfo;
		this.color = color;
		this.size = size;
		getCancelButton().setText(cancelButtonInfo);
		getCancelButton().setTextColor(Color.parseColor(color));
		getCancelButton().setTextSize(size);
	}

	/**
	 * 显示弹框
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 */
	public void show() {
		try {
			getDialog().show();
			isShowing = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消弹框
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 */
	public void dismiss() {
		if (isShowing) {
			getDialog().dismiss();
		}
		isShowing = false;
	}

	/**
	 * 判断弹框是否正在显示
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @return
	 */
	public boolean isShowing() {
		return isShowing;
	}

	/**
	 * 设置弹框是否可以取消（点击弹框外区域和按back按钮取消）
	 * 
	 * @author lilw
	 * @CrateTime 2013-1-7
	 * @param flag
	 *            true为可以取消 false为不可取消
	 */
	public void setCancelable(boolean flag) {
		getDialog().setCancelable(flag);
	}
}
