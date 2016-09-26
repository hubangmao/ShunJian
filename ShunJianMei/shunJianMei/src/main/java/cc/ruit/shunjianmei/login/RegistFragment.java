package cc.ruit.shunjianmei.login;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.jpush.JPushUtil;
import cc.ruit.shunjianmei.jpush.MyTagAliasCallback;
import cc.ruit.shunjianmei.net.api.UserInfoApi;
import cc.ruit.shunjianmei.net.request.CommonSendVCodeRequest;
import cc.ruit.shunjianmei.net.request.UserRegisterRequest;
import cc.ruit.shunjianmei.usermanager.UserInfo;
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
import com.oruit.oruitkey.OruitMD5;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: RegistFragment
 * @Description: 注册
 * @author: lee
 * @date: 2015年10月9日 下午8:44:49
 */
public class RegistFragment extends BaseFragment implements
        OnClickListener {
    @ViewInject(R.id.et_username)
    EditText et_name;// 用户名
    @ViewInject(R.id.et_vcode)
    EditText et_vcode;//验证码
    @ViewInject(R.id.et_password)
    EditText et_password;// 密码
    @ViewInject(R.id.et_password2)
    EditText et_password2;// 密码
    @ViewInject(R.id.et_referee)
    EditText et_referee;// 推荐渠道
    @ViewInject(R.id.btn_getvcode)
    Button btn_getvcode;// 获取验证码
    @ViewInject(R.id.btn_commit)
    Button btn_commit;//提交
    @ViewInject(R.id.tv_regist)
    TextView tv_regist;// 注册
    @ViewInject(R.id.tv_use)
    TextView tv_use;// 立即使用

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RegistFragment"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RegistFragment");
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.login_regist_layout, null);
        ViewUtils.inject(this, view);
        initTitle();
        return view;
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
                ((LoginActivity) activity).hideSoftInput();
                FragmentManagerUtils.back(activity, R.id.content_frame);
            }
        });
        title.iv_left.setImageResource(R.drawable.back);
        title.iv_left.setVisibility(View.VISIBLE);
        title.tv_title.setText("注册");
    }

    @OnClick({R.id.btn_getvcode, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_commit:
                if (checkUserLoginParems()) {
                    UserRegister(et_name.getText().toString(), et_password.getText().toString(), et_vcode.getText().toString());
                }
                break;
            case R.id.btn_getvcode:
                if (checkParems()) {
                    commonSendVCode(et_name.getText().toString(), "1");
                }
                break;

            default:
                break;
        }
    }


    /**
     * @Title: checkParems
     * @Description: 检测参数
     * @author: lee
     * @return: boolean true表示所有参数无异常，false则反之
     */
    boolean checkParems() {
        // 获取验证码
        String mobile = et_name.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showShort("手机号码不能为空!");
            return false;
        }
        if (!ParmsUtil.checkPhone_2(mobile)) {
            ToastUtils.showShort("手机号码格式错误!");
            return false;
        }
        return true;
    }

    /**
     * @param mobile (手机号,正则验证)
     * @param type   (1注册2找回密码3发型师找回密码)
     * @Title: commonSendVCode
     * @Description: 获取验证码
     * @author: lee
     * @return: void
     */
    public void commonSendVCode(String mobile, String type) {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(
                    R.string.no_networks_found));
            return;
        }
        CommonSendVCodeRequest request = new CommonSendVCodeRequest(mobile, type);
        timerControler(1);
        LoadingDailog.show(activity, "正在获取验证码");
        UserInfoApi.commonSendVCode(request, new RequestCallBack<String>() {
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
                if (result.getCode() != 1000) {
                    timerControler(2);
                } else {
                    et_vcode.requestFocus();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                LoadingDailog.dismiss();
                timerControler(2);
                ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
            }
        });
    }

    /**
     * @Title: checkParems
     * @Description: 检测参数
     * @author: lee
     * @return: boolean true表示所有参数无异常，false则反之
     */
    boolean checkUserLoginParems() {
        String username = et_name.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showShort("手机号码不能为空!");
            return false;
        }
        if (username.length() != 11) {
            ToastUtils.showShort("手机号码格式错误!");
            return false;
        }
        String vcode = et_vcode.getText().toString();
        if (TextUtils.isEmpty(vcode)) {
            ToastUtils.showShort("验证码不能为空!");
            return false;
        }
        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("密码不能为空!");
            return false;
        }
        if (password.length() < 6 || password.length() > 12) {
            ToastUtils.showShort("密码长度必须是6~12位");
            return false;
        }
        String rppassword = et_password2.getText().toString();
        if (!password.equals(rppassword)) {
            ToastUtils.showShort("两次密码不一样!");
            return false;
        }
        return true;
    }

    /**
     * @param userName
     * @param passWord
     * @param vCode
     * @Title: findPassWordRequest
     * @Description: TODO
     * @author: lee
     * @return: void
     */
    public void UserRegister(String userName, String passWord,
                             String vCode) {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(getResources().getString(
                    R.string.no_networks_found));
            return;
        }
        String referee = et_referee.getText().toString();
        UserRegisterRequest request = new UserRegisterRequest(userName,
                OruitMD5.getMD5UpperCaseStr(passWord), vCode, referee, "android");
        LoadingDailog.show(activity, "正在注册...");
        UserInfoApi.UserRegister(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LoadingDailog.dismiss();
                BaseResponse result = BaseResponse
                        .getBaseResponse(responseInfo.result);
                String[] split = result.getMsg().split("\\|");
                if ("1".equals(split[0])) {
                    ToastUtils.showShort(split[1] + "");
                }
                if (result.getCode() == 1000) {
                    ((LoginActivity) activity).hideSoftInput();
                    resultHandle(result.getData());

                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                LoadingDailog.dismiss();
                ToastUtils.showShort(getResources().getString(
                        R.string.request_failure));
            }
        });
    }

    /**
     * @param data
     * @Title: resultHandle
     * @Description: 结果处理
     * @author: lee
     * @return: void
     */
    protected void resultHandle(String data) {
        List<UserInfo> userinfo = UserInfo.getclazz2(data);
        if (userinfo != null && userinfo.size() > 0) {
            JPushUtil.setAlias("" + userinfo.get(0).getUserID(),
                    new MyTagAliasCallback());
            UserManager.updateUserinfo(activity, userinfo.get(0));
            UserManager.setUserID(userinfo.get(0).getUserID());
//			startActivity(MainActivity.getIntent(getActivity()));
//			activity.finish();
//			Intent intent = MainActivity.getIntent(activity);
//			startActivity(intent);
//			activity.finish();
            Fragment fragment = FragmentManagerUtils.getFragment(activity, MeEditFragment.class.getName());
            Bundle bundle = new Bundle();
            bundle.putString("Form", RegistFragment.class.getSimpleName());
            fragment.setArguments(bundle);
            FragmentManagerUtils.add(activity, R.id.content_frame, fragment, false);
        }

    }

    private Timer timer;

    /**
     * @param state 1代表开始，2代表结束倒计时
     * @Title: timerControler
     * @Description: 获取验证码倒计时
     * @author: lee
     * @return: void
     */
    private void timerControler(int state) {
        if (1 == state) {
            timer = new Timer();
            // 创建一个TimerTask
            TimerTask timerTask = new TimerTask() {
                // 倒数60秒
                int i = 60;

                @Override
                public void run() {
                    // 定义一个消息传过去
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = i--;
                    timerHandler.sendMessage(msg);
                }
            };
            // TimerTask是个抽象类,实现了Runnable接口，所以TimerTask就是一个子线程
            timer.schedule(timerTask, 0, 1000);
            btn_getvcode.setClickable(false);
        } else if (2 == state) {
            if (timer != null) {
                timer.cancel();
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = 0;
                timerHandler.sendMessage(msg);
            }
        }
    }

    // 自定义Handler
    Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                // Handler处理消息
                if (msg.arg1 > 0) {
                    btn_getvcode.setText("重新获取(" + msg.arg1 + "s)");
                    btn_getvcode
                            .setBackgroundColor(activity.getResources().getColor(R.color.gray_ab));
                    ;
                } else if (msg.arg1 == 0) {
                    // 结束Timer计时器
                    timer.cancel();
                    btn_getvcode.setText("获取验证码");
                    btn_getvcode.setClickable(true);
                    btn_getvcode
                            .setBackgroundResource(R.drawable.btn_red);
                }
            }
        }
    };
}
