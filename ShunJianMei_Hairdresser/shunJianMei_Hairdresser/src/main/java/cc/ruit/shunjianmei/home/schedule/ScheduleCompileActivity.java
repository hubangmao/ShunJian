package cc.ruit.shunjianmei.home.schedule;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.title.TitleUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.OrderApi;
import cc.ruit.shunjianmei.net.request.H_DeleteSelfWorkingRequest;
import cc.ruit.shunjianmei.net.request.H_SaveSelfWorkingRequest;
import cc.ruit.shunjianmei.net.response.H_DeleteSelfWorkingResponse;
import cc.ruit.shunjianmei.net.response.H_SaveSelfWorkingResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.Util;
import cc.ruit.utils.sdk.http.NetWorkUtils;

public class ScheduleCompileActivity extends BaseActivity
		implements OnClickListener, OnValueChangeListener, OnScrollListener, Formatter {
	@ViewInject(R.id.et_schedule_compile_username)
	private EditText etUserName;// 客户姓名
	@ViewInject(R.id.et_schedule_compile_phone)
	private EditText etPhone;// 客户电话
	@ViewInject(R.id.et_schedule_compile_work_content)
	private EditText etWorkContent;// 工作内容
	@ViewInject(R.id.tv_schedule_compile_time)
	private TextView dayTime;// 日期
	@ViewInject(R.id.et_schedule_compile_start_time)
	private TextView setStartTime;// 开始时间
	@ViewInject(R.id.et_schedule_compile_predict_time)
	private TextView etEndTime;// 预计时长
	@ViewInject(R.id.iv_schedule_compile_call)
	private ImageView ivCall;// 电话
	@ViewInject(R.id.btn_schedule_compile_delete)
	private Button btnDelete;// 删除
	String Year, Month, Day;// 年，月，日
	String orderId, Name, Mobile, StartHour, EndHour, Type, Content;
	String Date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_compile_activity);
		ViewUtils.inject(this);
		Date = ScheduleFragment.DATE;
		initTitle();// 初始化标题
		initData();// 初始化数据
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		dayTime.setText(ScheduleFragment.DATE);
		Intent intent = getIntent();
		if (intent.getStringExtra("OrderID") != null && !intent.getStringExtra("OrderID").equals("")) {
			orderId = intent.getStringExtra("OrderID");
			Name = intent.getStringExtra("Name");
			Mobile = intent.getStringExtra("Mobile");
			StartHour = intent.getStringExtra("StartHour");
			EndHour = String.valueOf(Integer.valueOf(intent.getStringExtra("EndHour")) - Integer.valueOf(StartHour));
			Type = intent.getStringExtra("Type");
			Content = intent.getStringExtra("Content");
			ivCall.setVisibility(View.VISIBLE);
			showData();
		}
	}

	/**
	 * 显示数据
	 */
	private void showData() {
		dayTime.setText(ScheduleFragment.DATE);
		setStartTime.setText(StartHour + ":00  >");
		setStartTime.setTextSize(17);
		setStartTime.setTextColor(Color.BLACK);
		etUserName.setText(Name);
		etPhone.setText(Mobile);
		etWorkContent.setText(Content);
		etEndTime.setText(EndHour +"小时  >");
		etEndTime.setTextSize(17);
		etEndTime.setTextColor(Color.BLACK);
	}

	private String startTime;
	private String endTime;

	@Override
	@OnClick({ R.id.ll_schedule_satrt_time, R.id.fl_schedule_compile_end_time, R.id.btn_schedule_compile_delete,
			R.id.btn_schedule_compile_save, R.id.iv_schedule_compile_call })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_schedule_satrt_time:// 设置开始时间
			closeInputMethod();
			startTimeSelect("60");
			break;
		case R.id.btn_schedule_compile_delete:// 删除
			if(TextUtils.isEmpty(etUserName.getText())){
				ToastUtils.showLong("操作失误，请返回");
			}else{
				new AlertDialog.Builder(this).setMessage("你确定要删除吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DeleteSelfWorking();
					}
				}).setNegativeButton("取消", null).show();
			}
			break;
		case R.id.btn_schedule_compile_save:// 保存
			checkSaveParames();
			break;
		case R.id.fl_schedule_compile_end_time:// 预计时长
			closeInputMethod();
			startTimeSelect("1");
			break;
		case R.id.iv_schedule_compile_call:
			Util.openTelDall(this, Mobile);
		}
	}
	/**
	 * 校验需要保存的数据据
	 */
	private void checkSaveParames(){
		if (setStartTime.getText().equals("点击设置开始时间")) {
			ToastUtils.showLong("请设置开始时间");
		}else if (etEndTime.getText().equals("点击设置预计时长")) {
			ToastUtils.showLong("请输入预计时长");
		}else if (etUserName == null || TextUtils.isEmpty(etUserName.getText())) {
			ToastUtils.showLong("请输入客户姓名");
		}else{
			SaveSelfWorking();
		}
	}

	/**
	 * 删除自编辑内容
	 */
	private void DeleteSelfWorking() {
		if (!NetWorkUtils.checkNetworkAvailable1(this)) {
			ToastUtils.showShort(this.getResources().getString(R.string.no_networks_found));
			return;
		}
		H_DeleteSelfWorkingRequest request = new H_DeleteSelfWorkingRequest("" + UserManager.getUserID(), orderId);
		OrderApi.DeleteSelfWorking(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				ToastUtils.showShort("删除成功");
				try {
					Thread.sleep(1000);
					finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showShort("删除失败");
			}
		});
	}

	/**
	 * 保存自编辑内容
	 */
	private void SaveSelfWorking() {
		Year = Date.substring(0, 4);
		Month = Date.substring(5, 7);
		Day = Date.substring(8);
		String userName = etUserName.getText().toString();
		String phoneNumber = etPhone.getText().toString();
		String workContent = etWorkContent.getText().toString();
		endTime = etEndTime.getText().toString().substring(0, 1);
		startTime = setStartTime.getText().toString().substring(0, 2);
		if (!NetWorkUtils.checkNetworkAvailable1(this)) {
			ToastUtils.showShort(this.getResources().getString(R.string.no_networks_found));
			return;
		}
		LoadingDailog.show(this, "正在保存中，请稍候。。。");
		H_SaveSelfWorkingRequest request = new H_SaveSelfWorkingRequest("" + UserManager.getUserID(), startTime, Year,
				Month, Day, userName, phoneNumber, workContent, endTime, orderId);
		OrderApi.SaveSelfWorking(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				BaseResponse result = H_SaveSelfWorkingResponse.getBaseResponse(responseInfo.result);
				if (result.getCode() == 1000) {
					LoadingDailog.dismiss();
					ToastUtils.showShort("保存成功");
					 List<H_SaveSelfWorkingResponse> selfWorkList = H_SaveSelfWorkingResponse.getclazz2(result.getData());
					 orderId = selfWorkList.get(0).getOrderID();
				} else if (result.getCode() == 2000) {
					LoadingDailog.dismiss();
					ToastUtils.showShort("保存失败，该时间段内已经包含了预约信息");
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LoadingDailog.dismiss();
				ToastUtils.showShort("保存失败");

			}
		});
	}

	private void initTitle() {
		TitleUtil title = new TitleUtil(this);
		title.tv_title.setText("编辑工作内容");
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title.iv_left.setImageResource(R.drawable.arrow_left_white);
		title.iv_left.setVisibility(View.VISIBLE);

	}
	private void closeInputMethod() {
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    boolean isOpen = imm.isActive();
	    if (isOpen) {
	    	imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	AlertDialog dialog;
	NumberPicker hour;// 选小时
	NumberPicker minute;// 选分钟
	View colon, H, zero;// 用于隐藏":","小时","00"

	/**
	 * 开始时间选择器
	 * 
	 * @param times
	 */
	public void startTimeSelect(final String times) {
		if (times.equals("60")) {
			View TimeView = LayoutInflater.from(this).inflate(R.layout.dailog_time_select_schedule_compile, null);
			hour = (NumberPicker) TimeView.findViewById(R.id.num_time_select_hourpicker_compile);
			hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
			minute = (NumberPicker) TimeView.findViewById(R.id.num_time_select_minuteicker_compile);
			colon = TimeView.findViewById(R.id.num_time_select_hourpicker_colon);
			zero = TimeView.findViewById(R.id.tv_num_time_select_00);
			AlertDialog.Builder Timebuilder = new AlertDialog.Builder(this);// 弹窗
			Timebuilder.setView(TimeView);// 设置弹窗显示内容
			Timebuilder.setCancelable(true);// 设置对话框是否可撤销
			hour.getChildAt(0).setFocusable(false);
			minute.setVisibility(View.GONE);
			colon.setVisibility(View.GONE);
			zero.setVisibility(View.VISIBLE);
			initNumberPicker(hour, minute, times);
			hour.setOnValueChangedListener(this);// 加监听
			minute.setOnValueChangedListener(this);// 加监听
			Timebuilder.setTitle("请选择开始时间：").setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}

			});
			dialog = Timebuilder.show();
		} else {
			View TimeView = LayoutInflater.from(this).inflate(R.layout.dailog_time_select_schedule_compile, null);
			hour = (NumberPicker) TimeView.findViewById(R.id.num_time_select_hourpicker_compile);
			hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
			minute = (NumberPicker) TimeView.findViewById(R.id.num_time_select_minuteicker_compile);
			colon = TimeView.findViewById(R.id.num_time_select_hourpicker_colon);
			H = TimeView.findViewById(R.id.num_time_select_hourpicker_hour);
			AlertDialog.Builder Timebuilder = new AlertDialog.Builder(this);// 弹窗
			Timebuilder.setView(TimeView);// 设置弹窗显示内容
			Timebuilder.setCancelable(true);// 设置对话框是否可撤销
			minute.setVisibility(View.GONE);
			colon.setVisibility(View.GONE);
			H.setVisibility(View.VISIBLE);
			initNumberPicker(hour, times);
			hour.setOnValueChangedListener(this);// 加监听

			Timebuilder.setTitle("预计时长：").setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}

			});
			dialog = Timebuilder.show();
		}
	}

	private void initNumberPicker(NumberPicker hour, String times) {
		hour.setMaxValue(6);
		hour.setMinValue(1);
		etEndTime.setText("1小时  >");
		etEndTime.setTextColor(Color.BLACK);
		hour.setValue(1);
		hour.setWrapSelectorWheel(false);
		hour.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChange(NumberPicker view, int scrollState) {

			}
		});
		hour.setFormatter(new Formatter() {

			@Override
			public String format(int value) {
				return "" + value;
			}
		});
	}

	/**
	 * 初始化数字选择器
	 * 
	 * @param hour
	 * @param minute
	 * @param times
	 */
	private void initNumberPicker(NumberPicker hour, NumberPicker minute, String times) {
		hour.setMaxValue(21);// 设置小时最大值
		hour.setMinValue(10);// 设置小时最小值
		setStartTime.setText("10:00  >");
		setStartTime.setTextColor(Color.BLACK);
		hour.setValue(10);
		hour.setWrapSelectorWheel(false);
		minute.setMaxValue(0);
		minute.setMinValue(0);
		minute.setWrapSelectorWheel(false);
		hour.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChange(NumberPicker view, int scrollState) {

			}
		});
		hour.setFormatter(new Formatter() {

			@Override
			public String format(int value) {
				return "" + value;
			}
		});
		minute.setFormatter(new Formatter() {

			@Override
			public String format(int value) {
				return "" + value;
			}
		});

	}

	@Override
	public String format(int value) {
		return null;
	}

	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {

	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		if (newVal == 1 || newVal == 2 || newVal == 3 || newVal == 4 || newVal == 5 || newVal == 6) {
			etEndTime.setText(newVal + "小时  >");
			etEndTime.setTextColor(Color.BLACK);
		} else {
			setStartTime.setText(newVal + ":00  >");
			setStartTime.setTextColor(Color.BLACK);
		}
	}
}
