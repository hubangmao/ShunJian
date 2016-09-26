package cc.ruit.utils.zq;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.NumberPicker.OnValueChangeListener;
import cc.ruit.utils.R;
import cc.ruit.utils.zq.DateTimePicker.OnDateTimeChangedListener;

/**
 * 
 * @ClassName: DateTimePickerDialog
 * @Description: 时间选择器，包括日期和小时分钟
 * @author: Johnny
 * @date: 2015年12月12日 下午2:25:50
 */
public class DateTimePickerDialog extends AlertDialog {
	// private DateTimePicker mDateTimePicker;
	private Calendar mDate = Calendar.getInstance();
//	private OnDateTimeSetListener mOnDateTimeSetListener;
	private long date;
	private final NumberPicker mDateSpinner;
	private final NumberPicker mHourSpinner;
	private final NumberPicker mMinuteSpinner;
	private Button okButton, cancelButton;
	private int mHour, mMinute;
	private TextView title;
	private String[] mDateDisplayValues = new String[7];

	@SuppressWarnings("deprecation")
	public DateTimePickerDialog(Context context, long date, Calendar c) {
		super(context);
		this.date = date;

		// mDateTimePicker = new DateTimePicker(context, c);
		LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.datetimepickdialog, null);
		// 设置点击屏幕Dialog不消失
		setCanceledOnTouchOutside(false);
		setView(linearLayout);
		/**
		 * 初始化组件
		 */
		mDateSpinner = (NumberPicker) linearLayout.findViewById(R.id.np_date);
		mDateSpinner.setMinValue(0);
		mDateSpinner.setMaxValue(6);

		mHourSpinner = (NumberPicker) linearLayout.findViewById(R.id.np_hour);
		mHourSpinner.setMaxValue(23);
		mHourSpinner.setMinValue(0);

		mMinuteSpinner = (NumberPicker) linearLayout
				.findViewById(R.id.np_minute);
		mMinuteSpinner.setMaxValue(59);
		mMinuteSpinner.setMinValue(0);
		/**
		 * 屏蔽NumberPicker的点击事件
		 */
		mDateSpinner
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		mHourSpinner
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		mMinuteSpinner
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		/**
		 * 为NumberPicker添加监听事件
		 */
		mDateSpinner.setOnValueChangedListener(mOnDateChangedListener);
		mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);
		mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);

		okButton = (Button) linearLayout.findViewById(R.id.okButton_datedialog);
		cancelButton = (Button) linearLayout
				.findViewById(R.id.cancelButton_datedialog);

		title = (TextView) linearLayout.findViewById(R.id.title);

		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long nDate = mDate.getTimeInMillis();
				if (nDate - DateTimePickerDialog.this.date > 70 * 60 * 1000) {
					dismiss();
				} else {
					title.setText("请选择当前时间之后70分钟!");
					dismiss();
				}
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		updateDateControl();
		updateTitle(mDate.getTimeInMillis());

		// mDateTimePicker
		// .setOnDateTimeChangedListener(new OnDateTimeChangedListener() {
		// @Override
		// public void onDateTimeChanged(DateTimePicker view,
		// int year, int month, int day, int hour, int minute) {
		// mDate.set(Calendar.YEAR, year);
		// mDate.set(Calendar.MONTH, month);
		// mDate.set(Calendar.DAY_OF_MONTH, day);
		// mDate.set(Calendar.HOUR_OF_DAY, hour);
		// mDate.set(Calendar.MINUTE, minute);
		// mDate.set(Calendar.SECOND, 0);
		// /**
		// * 更新日期
		// */
		// updateTitle(mDate.getTimeInMillis());
		// }
		// });
		// setButton("确定", this);
		// setButton2("取消", (OnClickListener) null);
		// setOkButton();
		// setCancelButton();
		// mDate.setTimeInMillis(date);
		// updateTitle(mDate.getTimeInMillis());
	}

	private void updateDateControl() {
		/**
		 * 星期几算法
		 */
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(mDate.getTimeInMillis());
		cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);
		mDateSpinner.setDisplayedValues(null);
		for (int i = 0; i < 7; ++i) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			mDateDisplayValues[i] = (String) DateFormat.format("MM.dd EEEE",
					cal);
		}
		mDateSpinner.setDisplayedValues(mDateDisplayValues);
		mDateSpinner.setValue(7 / 2);
		mDateSpinner.invalidate();
	}
	
	/**
	 * 
	 * 控件监听器
	 */
	private NumberPicker.OnValueChangeListener mOnDateChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			mDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
			/**
			 * 更新日期
			 */
			updateDateControl();
			updateTitle(mDate.getTimeInMillis());
		}
	};

	private NumberPicker.OnValueChangeListener mOnHourChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			mHour = mHourSpinner.getValue();
			mDate.set(Calendar.HOUR_OF_DAY, mHour);
			updateTitle(mDate.getTimeInMillis());
		}
	};

	private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new OnValueChangeListener() {
		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			mMinute = mMinuteSpinner.getValue();
			mDate.set(Calendar.MINUTE, mMinute);
			updateTitle(mDate.getTimeInMillis());
		}
	};

//	/**
//	 * 接口回調控件 秒数
//	 */
//	public interface OnDateTimeSetListener {
//		void OnDateTimeSet(AlertDialog dialog, long date);
//	}

	/**
	 * 更新对话框日期
	 * 
	 * @param date
	 */
	private void updateTitle(long date) {
		int flag = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE
				| DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_TIME;
		title.setText(DateUtils.formatDateTime(this.getContext(), date, flag));
	}

//	/**
//	 * 对外公开方法让Activity实现
//	 */
//	public void setOnDateTimeSetListener(OnDateTimeSetListener callBack) {
//		mOnDateTimeSetListener = callBack;
//	}

	// public void onClick(DialogInterface arg0, int arg1) {
	// long nDate = mDate.getTimeInMillis();
	// if (mOnDateTimeSetListener != null && nDate - date > 0) {
	// mOnDateTimeSetListener.OnDateTimeSet(this, mDate.getTimeInMillis());
	// }
	// }
//
//	public void setOkButton() {
//		if (mDateTimePicker.getOkButton() != null) {
//			mDateTimePicker.getOkButton().setOnClickListener(this);
//		}
//	}
//
//	public void setCancelButton() {
//		if (mDateTimePicker.getCancelButton() != null) {
//			mDateTimePicker.getCancelButton().setOnClickListener(this);
//		}
//	}

}
