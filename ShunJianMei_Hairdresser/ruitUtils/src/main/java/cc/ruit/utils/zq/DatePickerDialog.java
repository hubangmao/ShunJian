package cc.ruit.utils.zq;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.ruit.utils.R;

/**
 * 
 * @ClassName: DatePickerDialog
 * @Description: 时间选择器，选择日期
 * @author: Johnny
 * @date: 2015年12月12日 下午2:28:43
 */
public class DatePickerDialog extends AlertDialog {

	private TextView title;
	private Button okButton, cancelButton;
	private DatePicker datePicker;
	private Calendar mDate = Calendar.getInstance();
	private long pDate = mDate.getTimeInMillis();
	private OnDataChangedListener mOnDataChangedListener;

	public DatePickerDialog(Context context) {
		super(context);

		LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.datepickdialog, null);
		// 设置点击屏幕Dialog不消失
		setCanceledOnTouchOutside(false);
		setView(linearLayout);
		/*
		 * 初始化控件
		 */
		title = (TextView) linearLayout.findViewById(R.id.title);
		okButton = (Button) linearLayout.findViewById(R.id.okButton);
		cancelButton = (Button) linearLayout.findViewById(R.id.cancelButton);
		datePicker = (DatePicker) linearLayout.findViewById(R.id.datePicker);
		/*
		 * 设置title
		 */
		setTitle(mDate);
		/*
		 * 初始化DatePicker数据
		 */
		datePicker.init(mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH),
				mDate.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfYear) {
						mDate.set(Calendar.YEAR, year);
						mDate.set(Calendar.MONTH, monthOfYear);
						mDate.set(Calendar.DAY_OF_MONTH, dayOfYear);
						checkDate(mDate);
						setTitle(mDate);
					}
				});
		/*
		 * 设置按钮的监听事件
		 */
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onDataChanged();
				dismiss();
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void setTitle(Calendar c) {
		String str = getStringDate(c.getTimeInMillis());
		title.setText(str);
	}

	private String getStringDate(Long date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	private void checkDate(Calendar c) {
		if (c.getTimeInMillis() - pDate < 0) {
			c.setTimeInMillis(pDate);
			datePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			setTitle(c);
		}
	}

	public interface OnDataChangedListener {
		void onDataChanged(String data);
	}

	public void setOnDataChangedListener(OnDataChangedListener callback) {
		mOnDataChangedListener = callback;
	}

	private void onDataChanged() {
		if (mOnDataChangedListener != null) {
			mOnDataChangedListener.onDataChanged(getStringDate(mDate
					.getTimeInMillis()));
		}
	}

}
