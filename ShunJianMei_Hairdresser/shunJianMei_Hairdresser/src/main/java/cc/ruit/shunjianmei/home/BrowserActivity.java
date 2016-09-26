package cc.ruit.shunjianmei.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmeihairdresser.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: BrowserActivity
 * @Description: 网页展示
 * @author: lee
 * @date: 2015年9月9日 下午7:15:58
 */
public class BrowserActivity extends BaseActivity {

	@ViewInject(R.id.webview)
	WebView webview;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.browser_activity_layout);
		ViewUtils.inject(this);
		init();
		initTitle();
	}
	/**
	 * @Title: initTitle
	 * @Description: 初始化标题
	 * @author: lee
	 * @return: void
	 */
	private void initTitle() {
		String titleName = getIntent().getStringExtra("TitleName");
		TitleUtil title = new TitleUtil(this);
		title.iv_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText(titleName);
	}
	/**
	 * @Title: init
	 * @Description: 初始化页面数据
	 * @author: lee
	 * @return: void
	 */
	private void init() {
		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				webview.loadUrl(Constant.HELP_NET
//						+ UserManager.getUserHomeInfo(context).getUserGuid());
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				LoadingDailog.show(BrowserActivity.this, "正在努力加载~");
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				LoadingDailog.dismiss();
			}

		});
		String url = getIntent().getStringExtra("url");
		webview.loadUrl(url);
	}


	/**
	 * @Title: getIntent
	 * @Description: 获取Intent
	 * @author: lee
	 * @param context
	 * @param url
	 * @param titleName
	 * @return
	 * @return: Intent
	 */
	public static Intent getIntent(Context context, String url,String titleName) {
		Intent intent = new Intent(context, BrowserActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("TitleName", titleName);
		return intent;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		 MobclickAgent.onPause(this); 
	}
}
