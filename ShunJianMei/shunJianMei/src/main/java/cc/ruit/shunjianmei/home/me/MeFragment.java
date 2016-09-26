package cc.ruit.shunjianmei.home.me;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.messagedialog.MessageDialog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.collect.CollectActivity;
import cc.ruit.shunjianmei.home.collect.CollectFragment;
import cc.ruit.shunjianmei.home.me.coupon.CouponActivity;
import cc.ruit.shunjianmei.home.me.coupon.CouponFragment;
import cc.ruit.shunjianmei.home.me.feedback.FeedbackActivity;
import cc.ruit.shunjianmei.home.me.feedback.FeedbackFragment;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.login.LoginFragment;
import cc.ruit.shunjianmei.login.MeEditActivity;
import cc.ruit.shunjianmei.login.MeEditFragment;
import cc.ruit.shunjianmei.login.WelcomeFragment;
import cc.ruit.shunjianmei.net.api.MineApi;
import cc.ruit.shunjianmei.net.api.SettingApi;
import cc.ruit.shunjianmei.net.request.CheckVersionRequest;
import cc.ruit.shunjianmei.net.request.MineRequest;
import cc.ruit.shunjianmei.net.response.CheckVersionResponse;
import cc.ruit.shunjianmei.usermanager.UserInfo;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;
import de.greenrobot.event.EventBus;

/**
 * @ClassName: MeFragment
 * @Description: 用户中心
 * @author: lee
 * @date: 2015年10月9日 下午8:38:26
 */
public class MeFragment extends BaseFragment implements OnClickListener {

    @ViewInject(R.id.ll_container)
    private LinearLayout ll_container;
    @ViewInject(R.id.rl_dresserinfo_me)
    private RelativeLayout rl_dresserinfo;// 美发师信息的布局
    @ViewInject(R.id.rl_balance_me)
    private RelativeLayout rl_balance;// 余额的布局
    @ViewInject(R.id.rl_coupon_me)
    private RelativeLayout rl_coupon;// 优惠劵的布局
    @ViewInject(R.id.rl_favorite_me)
    private RelativeLayout rl_favorite;// 收藏的布局
    @ViewInject(R.id.rl_basicinfo_me)
    private RelativeLayout rl_basicinfo;// 我的基本信息的布局
    @ViewInject(R.id.rl_levelinfo_me)
    private RelativeLayout rl_levelinfo;// 我的定级信息的布局
    @ViewInject(R.id.rl_feedback_me)
    private RelativeLayout rl_feedback;// 用户反馈的布局
    @ViewInject(R.id.rl_aboutus_me)
    private RelativeLayout rl_aboutus;// 关于我们的布局
    @ViewInject(R.id.rl_update_me)
    private RelativeLayout rl_update;// 检测更新的布局

    @ViewInject(R.id.iv_dresserphoto_me)
    ImageView iv_me_photo;// 本人头像
    @ViewInject(R.id.tv_dressername_me)
    private TextView tv_dressername;// 本人姓名
    @ViewInject(R.id.tv_gender_me)
    private TextView tv_gender;// 本人电话
    @ViewInject(R.id.tv_phone_me)
    private TextView tv_phone;// 美发师电话
    @ViewInject(R.id.tv_autherize_me)
    private TextView tv_autherize;// 已认证标签
    @ViewInject(R.id.tv_id_me)
    private TextView tv_id;// 美发师ID
    @ViewInject(R.id.tv_balance_me)
    private TextView tv_balance;// 余额
    @ViewInject(R.id.tv_coupon_me)
    private TextView tv_coupon;// 接单数量
    @ViewInject(R.id.tv_favorite_me)
    private TextView tv_favorite;// 评论数量

    EmptyView ev;// 空载页

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MeFragment"); // 统计页面
        getData();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MeFragment");
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.me_layout, null);
        ViewUtils.inject(this, view);
        initTitle();
        LoadingDailog.show(activity, "数据加载中，请稍后");
        getData();
        initEventBus();
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
        title.tv_title.setText("我的");
    }

    /**
     * @Title: getData
     * @Description: 请求接口
     * @author: Johnny
     * @return: void
     */
    private void getData() {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            LoadingDailog.dismiss();
            return;
        }

        int userId = UserManager.getUserID();

        MineRequest request = new MineRequest("" + userId);
        MineApi.mine(request, new RequestCallBack<String>() {

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
                if (result.getCode() == 1000) {
                    List<UserInfo> data = UserInfo.getclazz2(result.getData());
                    if (data != null) {
                        // 设置网络请求的数据
                        resultHanlder(data);
                    } else {
                        ToastUtils.showShort("请求数据异常");
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MobclickAgent.onEvent(activity, "login_failure");
                LoadingDailog.dismiss();
                // ev.setErrState();
                ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));

            }
        });

    }

    /**
     * @Title: resultHanlder
     * @Description: 结果处理
     * @author: Johnny
     * @return: void
     */
    void resultHanlder(List<UserInfo> info) {
        if (info == null) {
            LogUtils.e("MineResponse err");
            return;
        }

        UserInfo obj = info.get(0);
        if (UserManager.getUserID() <= 0) {
            tv_dressername.setText("未登录");
        } else {
            tv_dressername.setText(obj.getNickName());
        }
        tv_gender.setText(obj.getSex());
        if (obj.getSex().equals("男")) {
            RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
        } else {
            RoundImageLoaderUtil.setErrImage(R.drawable.tx_woman, R.drawable.tx_woman, R.drawable.tx_woman);
        }
        RoundImageLoaderUtil.getInstance(activity, 360).loadImage(obj.getPhoto(), iv_me_photo);
        tv_phone.setText(obj.getMobile());
        tv_balance.setText("¥" + obj.getBalance());
        tv_coupon.setText(obj.getCoupons());
        tv_favorite.setText(obj.getFavoriteNum());
        UserManager.updateUserinfo(activity, obj);
    }

    /**
     * @param v
     * @Title: onClick
     * @Description: 定义点击事件
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    @OnClick({R.id.rl_balance_me, R.id.rl_coupon_me, R.id.rl_pricelist_me, R.id.rl_favorite_me, R.id.rl_feedback_me,
            R.id.rl_update_me, R.id.rl_logout_me, R.id.rl_aboutus_me, R.id.rl_dresserinfo_me})
    public void onClick(View v) {
        Intent in;
        switch (v.getId()) {
            // 头像
            case R.id.rl_dresserinfo_me:
                in = MeEditActivity.getIntent(activity, MeEditFragment.class.getName());
                startActivity(in);
                break;
            // case R.id.image_arrow_right:
            // in = MeEditActivity.getIntent(getActivity(),
            // MeEditFragment.class.getName());
            // startActivity(in);
            // break;
            // 余额
            case R.id.rl_balance_me:
                in = MyBalanceActivity.getIntent(activity, MyBalanceFragment.class.getName());
                startActivity(in);
                break;
            // 我的优惠劵
            case R.id.rl_coupon_me:
                in = CouponActivity.getIntent(activity, CouponFragment.class.getName());
                startActivity(in);
                break;
            // 收藏
            case R.id.rl_favorite_me:
                in = CollectActivity.getIntent(activity, CollectFragment.class.getName());
                startActivity(in);
                break;
            // 用户反馈
            case R.id.rl_feedback_me:
                in = FeedbackActivity.getIntent(activity, FeedbackFragment.class.getName());
                startActivity(in);
                break;
            // 关于我们
            case R.id.rl_aboutus_me:
                in = BrowserActivity.getIntent(activity, Constant.ABOUTME_NET, "关于我们");
                startActivity(in);
                break;
            // 检测更新
            case R.id.rl_update_me:
                LoadingDailog.show(activity, "正在检测,请稍等~");
                getData(true);
                break;
            // 退出登录
            case R.id.rl_logout_me:
                // UserManager.setUserID(-1);;
                final MessageDialog builder = new MessageDialog(getActivity());
                builder.setMessage("确定要退出吗？");
                builder.setCancelButtonGone(false);
                builder.setOkButtonInfo("确定");
                builder.getOkButton().setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        UserManager.logout(activity);
                        Intent in = LoginActivity.getIntent(activity, LoginFragment.class.getName());
                        in.putExtra("from", WelcomeFragment.class.getSimpleName());
                        startActivity(in);
                    }
                });
                builder.setCancelButtonInfo("取消");
                builder.getCancelButton().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                builder.show();
                break;

            default:
                break;
        }
    }

    /**
     * @Title: initEventBus
     * @Description: TODO
     * @author: 欧阳
     * @return: void
     */
    private void initEventBus() {
        EventBus.getDefault().register(this);

    }

    /**
     * @Title: onDestroy
     * @Description: 注销EventBus
     * @see cc.ruit.shunjianmei.base.BaseFragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * @param bus
     * @Title: onEventMainThread
     * @Description: 这个方法不能进行耗时操作
     * @author: 欧阳
     * @return: void
     */
    public void onEventMainThread(MyEventBus bus) {
        if (bus.getmMsg().equals("请刷新数据") || bus.getmMsg().equals("请刷新美发师收藏列表") || bus.getmMsg().equals("请刷新美发店收藏列表")
                || bus.getmMsg().equals("请刷新发型收藏列表")) {
            getData();
        }
    }

	/*-----------------检测更新逻辑部分--begin-------------------*/

    /**
     * @Title: getData
     * @Description: 版本更新
     * @author: lee
     * @return: void
     */
    public void getData(final boolean isUser) {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            LoadingDailog.dismiss();
            return;
        }

        CheckVersionRequest request = new CheckVersionRequest(getVersionName(activity), "2");
        SettingApi.CheckVersion(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LoadingDailog.dismiss();
                BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
                if (result == null) {
                    return;
                }
                String[] split = result.getMsg().split("\\|");
                if ("1".equals(split[0]) && isUser) {
                    ToastUtils.showShort(split[1] + "");
                }
                if (result.getCode() == 1000) {
                    CheckVersionResponse checkVersionResponse = CheckVersionResponse.getclazz(result.getData());
                    // 当返回版本大于本地版本号提示下载
                    if (checkVersionResponse != null && Double.parseDouble(checkVersionResponse.Code) > Double
                            .parseDouble(getVersionName(activity))) {
                        if (isUser) {
                            showDialog(checkVersionResponse);
                        }
                        // tv_version.setText("发现新版本 " +
                        // checkVersionResponse.Name);
                    } else {
                        // tv_version.setText("已是最新版本");
                        if (isUser) {
                            ToastUtils.showLong("已经是最新版本了！", Gravity.CENTER);
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                LoadingDailog.dismiss();
                ToastUtils.showLong("网络可能不太好，请您再试试吧");
            }
        });
    }

    /**
     * @param checkVersionResponse
     * @Title: showDialog
     * @Description: 提示下载
     * @author: lee
     * @return: void
     */
    void showDialog(final CheckVersionResponse checkVersionResponse) {
        final MessageDialog messageDialog = new MessageDialog(activity);
        messageDialog.setTitle("是否下载" + checkVersionResponse.Name);
        messageDialog.setMessage(checkVersionResponse.Intro);
        messageDialog.getOkButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动浏览器
                startBrowser(checkVersionResponse.URL);
            }
        });
        messageDialog.getCancelButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialog.dismiss();
            }
        });
        messageDialog.show();
    }

    /**
     * @param context
     * @return
     * @Title: getVersionName
     * @Description: 获取本地VersionName
     * @return: String
     */
    public String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * @param url
     * @Title: startBrowser
     * @Description: 启动浏览器
     * @return: void
     */
    public void startBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

	/*----------------版本检测更新------end-----------------------------*/

}
