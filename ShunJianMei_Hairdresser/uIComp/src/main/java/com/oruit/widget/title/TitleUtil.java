package com.oruit.widget.title;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zachary.hodge.uicomp.R;

public class TitleUtil {
	public ImageView iv_left,iv_right;
	public TextView tv_left,tv_right,tv_title;
	public RelativeLayout rl_container;
	private Activity activity;
	private Fragment fragment;
	private View view;
	public TitleUtil(Activity activity) {
		super();
		this.activity = activity;
		initTitleView(activity);
	}
	public TitleUtil(Fragment fragment) {
		super();
		this.fragment = fragment;
		initTitleView(fragment);
	}
	public TitleUtil(View view) {
		super();
		this.view = view;
		initTitleView(view);
	}
	private void initTitleView(Activity activity) {
		iv_left =(ImageView) activity.findViewById(R.id.title_iv_left_image);
		iv_right =(ImageView) activity.findViewById(R.id.title_iv_right_image);
		tv_left =(TextView) activity.findViewById(R.id.title_tv_left_text);
		tv_right =(TextView) activity.findViewById(R.id.title_tv_right_text);
		tv_title = (TextView) activity.findViewById(R.id.title_tv_title);
		rl_container = (RelativeLayout) activity.findViewById(R.id.title_rl_container);
	}
	private void initTitleView(Fragment fragment) {
		iv_left =(ImageView) fragment.getView().findViewById(R.id.title_iv_left_image);
		iv_right =(ImageView) fragment.getView().findViewById(R.id.title_iv_right_image);
		tv_left =(TextView) fragment.getView().findViewById(R.id.title_tv_left_text);
		tv_right =(TextView) fragment.getView().findViewById(R.id.title_tv_right_text);
		tv_title = (TextView) fragment.getView().findViewById(R.id.title_tv_title);
		rl_container = (RelativeLayout) fragment.getView().findViewById(R.id.title_rl_container);
	}
	private void initTitleView(View view) {
		iv_left =(ImageView) view.findViewById(R.id.title_iv_left_image);
		iv_right =(ImageView) view.findViewById(R.id.title_iv_right_image);
		tv_left =(TextView) view.findViewById(R.id.title_tv_left_text);
		tv_right =(TextView) view.findViewById(R.id.title_tv_right_text);
		tv_title = (TextView) view.findViewById(R.id.title_tv_title);
		rl_container = (RelativeLayout) view.findViewById(R.id.title_rl_container);
	}
}
