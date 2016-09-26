package cc.ruit.shunjianmei.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.utils.sdk.ToastUtils;
import cn.jpush.android.api.JPushInterface;

/**
 * @ClassName: LoginActivity
 * @Description: 登录相关
 * @author: HBM
 * @date: 2015年8月31日 下午3:39:01
 */
public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_base_layout);
        MobclickAgent.updateOnlineConfig(this);
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        String className = in.getStringExtra("className");
        try {
            if (className == null) {
                className = WelcomeFragment.class.getName();//默认进入欢迎页
            }
            Fragment fragment = FragmentManagerUtils.getFragment(this,
                    className);
            fragment.setArguments(bundle);
            FragmentManagerUtils.add(this, R.id.content_frame, fragment, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(LoginActivity.this, "再点击一次退出", Toast.LENGTH_LONG).show();
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * @param context
     * @return
     * @Title: getIntent
     * @Description: 获取跳转到当前Activity的intent对象
     * @author: lee
     * @return: Intent
     */
    public static Intent getIntent(Context context) {
        Intent in = new Intent(context, LoginActivity.class);
        return in;
    }

    /**
     * @param context
     * @return
     * @Title: getIntent
     * @Description: 获取跳转到当前Activity的intent对象
     * @author: lee
     * @return: Intent
     */
    public static Intent getIntent(Context context, String fragmentName) {
        Intent in = new Intent(context, LoginActivity.class);
        in.putExtra("className", fragmentName);
        return in;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    /**
     * @Title: hideSoftInput
     * @Description: 隐藏软键盘
     * @author: lee
     * @return: void
     */
    public void hideSoftInput() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
