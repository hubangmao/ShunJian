package cc.ruit.shunjianmei.login;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.FindFragment;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.home.me.MeFragment;
import cc.ruit.shunjianmei.net.api.FileUploadApi;
import cc.ruit.shunjianmei.net.api.SaveUserInfoApi;
import cc.ruit.shunjianmei.net.api.SaveUserPhotoApi;
import cc.ruit.shunjianmei.net.request.SaveUserInfoRequest;
import cc.ruit.shunjianmei.net.request.SaveUserPhotoRequest;
import cc.ruit.shunjianmei.net.request.UploadImageRequest;
import cc.ruit.shunjianmei.net.response.SaveUserInfoResponse;
import cc.ruit.shunjianmei.net.response.UploadImageResponce;
import cc.ruit.shunjianmei.usermanager.UserInfo;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.CutPicDialogUtils;
import cc.ruit.shunjianmei.util.CutPicDialogUtils.OnChoosPicResultListener;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.title.TitleUtil;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: MeEditFragment
 * @Description: 信息编辑界面
 * @author: 欧阳
 * @date: 2015年10月31日 下午4:59:30
 */
public class MeEditFragment extends BaseFragment {
    @ViewInject(R.id.image_photo)
    ImageView iv_photo;// 头像
    @ViewInject(R.id.ed_nickname)
    EditText ed_nickname;// 昵称
    @ViewInject(R.id.tv_sex_boy)
    TextView tv_boy;// 男孩
    @ViewInject(R.id.tv_sex_girl)
    TextView tv_girl;// 女孩
    @ViewInject(R.id.btn_back)
    Button btn_back;// 女孩
    UserInfo user;// 用户
    private String photo_name;// 图片名称
    String imgUrl;// 图片地址
    String sex = "1";
    String nickname;

    CutPicDialogUtils cutPic;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.me_edit_fragment, null);
        ViewUtils.inject(this, view);
        initTitle();
        initData();
        return view;
    }

    /**
     * @Title: initTitle
     * @Description: 标题
     * @author: 欧阳
     * @return: void
     */
    private void initTitle() {
        TitleUtil title = new TitleUtil(view);
        title.iv_left.setVisibility(View.VISIBLE);
        title.iv_left.setImageResource(R.drawable.arrow_back_white);
        title.iv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        title.tv_title.setText("个人信息编辑");
    }

    /**
     * @Title: back
     * @Description: 返回的判断
     * @author: 欧阳
     * @return: void
     */
    protected void back() {
        if (getArguments().getString("Form") != null) {
            Intent intent = MainActivity.getIntent(activity, FindFragment.class.getName());
            startActivity(intent);
            activity.finish();
        } else {
            Intent intent = MainActivity.getIntent(activity, MeFragment.class.getName());
            startActivity(intent);
            activity.finish();
        }
    }

    /**
     * @Title: initData
     * @Description: 初始化数据
     * @author: 欧阳
     * @return: void
     *
     */
    private void initData() {
        user = UserManager.getUserInfo(activity);
        if (getArguments().getString("Form") != null) {
        } else {
            btn_back.setVisibility(View.GONE);
        }
        if (user != null) {
            if ("1".equals(user.getSex()) || "男".equals(user.getSex())) {
                setLabel(R.id.tv_sex_boy);
                sex = "1";
            } else {
                setLabel(R.id.tv_sex_girl);
                sex = "2";
            }
            if (!TextUtils.isEmpty(user.getNickName())) {
                ed_nickname.setText(user.getNickName());
                ed_nickname.setSelection(ed_nickname.getText().length());
            }
            if (!TextUtils.isEmpty(user.getPhoto())) {
                RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
                RoundImageLoaderUtil.getInstance(activity, 360).loadImage(
                        user.getPhoto(), iv_photo);
            }

        }

    }

    @OnClick({R.id.btn_sumbit, R.id.btn_back, R.id.tv_sex_boy,
            R.id.tv_sex_girl, R.id.image_photo})
    public void methodClick(View v) {
        switch (v.getId()) {
            case R.id.image_photo:// 选择头像
                showDialog_tupian();
                break;
            case R.id.btn_sumbit:// 提交
                LoadingDailog.show(activity, "正在上传用户信息....");
                SumbitUserinfo();
                break;
            case R.id.btn_back:// 返回
                back();
                break;
            case R.id.tv_sex_boy:// 选择男孩
                setLabel(R.id.tv_sex_boy);
                sex = "1";
                break;
            case R.id.tv_sex_girl:// 选择女孩
                setLabel(R.id.tv_sex_girl);
                sex = "2";
                break;
            default:
                break;
        }
    }

    /**
     * @Title: showDialog_tupian
     * @Description: 保存图片
     * @author: 欧阳
     * @return: void
     */
    private void showDialog_tupian() {
//		Intent intent = new Intent(activity, CutPicDialogActivity.class);
//		startActivityForResult(intent, 1);
        cutPic = new CutPicDialogUtils(activity, this);
        cutPic.shwoChooseDialog();
        cutPic.setOnChoosPicResultListener(new OnChoosPicResultListener() {

            @Override
            public void onChoosPicResult(int result, String photo) {
                if (result == Activity.RESULT_OK) {
                    imgUrl = photo;
                    //在头像请求回来之前将默认的图片隐藏，避免出现图片闪一下的效果
                    iv_photo.setVisibility(View.INVISIBLE);
                    UpLoadPhoto();
                }
            }
        });
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * @Title: onActivityResult
     * @Description: 选取图片的结果返回
     * @see android.support.v4.app.Fragment#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cutPic.onActivityResult(requestCode, resultCode, data);

    }

//	/**
//	 * 
//	 * @Title: openSystemZoomImage
//	 * @Description: 系统裁剪图片
//	 * @author: 欧阳
//	 * @param uri
//	 * @param output_uri
//	 * @return: void
//	 */
//	public void openSystemZoomImage(Uri uri, Uri output_uri) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		// 发送裁剪信号
//		intent.putExtra("crop", "true");
//		// aspectX aspectY 是宽高的比例
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputX", ScreenUtils.dip2px(activity, 80));// 还要判断一下是从发型图片还是店铺图片
//		intent.putExtra("outputY", ScreenUtils.dip2px(activity, 80));
//		// 定义输出路径
//		intent.putExtra("outputFormat", "JPEG");
//		intent.putExtra("noFaceDetection", true);
//		intent.putExtra("return-data", true);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, output_uri);
//		startActivityForResult(intent, FileConstant.PHOTORESOULT);
//	}

    /**
     * @Title: UpLoadPhoto
     * @Description: 上传图片
     * @author: 欧阳
     * @return: void
     */
    private void UpLoadPhoto() {
        if (!NetWorkUtils.isConnectInternet(getActivity())) {
            ToastUtils.showShort("网络未链接，请检查网络设置");
            LoadingDailog.dismiss();
            return;
        }
        LoadingDailog.show(activity, "正在上传,请稍后~");
        UploadImageRequest sr = new UploadImageRequest(UserManager.getUserID()
                + "");
        new FileUploadApi().upload(imgUrl, sr.toJsonString(sr),
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        BaseResponse result = BaseResponse
                                .getBaseResponse(responseInfo.result);
                        String[] split = result.getMsg().split("\\|");
                        if ("1".equals(split[0])) {
                            ToastUtils.showShort(split[1] + "");
                        }
                        if (result.getCode() == 1000) {
                            List<UploadImageResponce> Responce = UploadImageResponce
                                    .getclazz(result.getData());
                            if (Responce != null) {

                                SaveUserPhoto(Responce);
                            }
                            LoadingDailog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        ToastUtils.showLong(R.string.request_failure);
                        LoadingDailog.dismiss();
                    }
                });

    }

    /**
     * @Title: SaveUserPhoto
     * @Description: 再调用保存图片的信息
     * @author: 欧阳
     * @return: void
     */
    protected void SaveUserPhoto(final List<UploadImageResponce> Responce) {
        SaveUserPhotoRequest request = new SaveUserPhotoRequest(UserManager.getUserID() + "", Responce.get(0).ID);
        SaveUserPhotoApi.SaveUserPhoto(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BaseResponse result = BaseResponse
                        .getBaseResponse(responseInfo.result);
                LoadingDailog.dismiss();
                String[] split = result.getMsg().split("\\|");
                if ("1".equals(split[0])) {
                    ToastUtils.showShort(split[1] + "");
                }
                if (result.getCode() == 1000) {
                    RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
                    RoundImageLoaderUtil.getInstance(activity, 360).loadImage(Responce.get(0).Image, iv_photo);
                    iv_photo.setVisibility(View.VISIBLE);
                    user.setPhoto(Responce.get(0).Image);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showLong(R.string.no_networks_found);
                iv_photo.setVisibility(View.VISIBLE);
                LoadingDailog.dismiss();
            }
        });

    }

    /**
     * @Title: SumbitUserinfo
     * @Description: 提交用户信息
     * @author: 欧阳
     * @return: void
     */
    private void SumbitUserinfo() {
        if (TextUtils.isEmpty(ed_nickname.getText().toString())) {
            nickname = "";
        } else {
            nickname = ed_nickname.getText().toString();
        }
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(
                    R.string.no_networks_found));
            return;
        }
        SaveUserInfoRequest request = new SaveUserInfoRequest(
                UserManager.getUserID() + "", nickname, sex);
        SaveUserInfoApi.SaveUserInfo(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BaseResponse response = BaseResponse
                        .getBaseResponse(responseInfo.result);
                if (response == null) {
                    return;
                }
                String[] split = response.getMsg().split("\\|");
                if ("1".equals(split[0])) {
                    ToastUtils.showShort(split[1] + "");
                }
                if (response.getCode() == 1000) {
                    List<SaveUserInfoResponse> temList = SaveUserInfoResponse
                            .getclazz2(response.getData());
                    if (temList != null && temList.size() > 0) {
                        // 发送消息
                        EventBus.getDefault().post(
                                new MyEventBus("请刷新数据"));
                        SaveUserinfo(temList);
                        LoadingDailog.dismiss();
                    }

                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort(activity.getResources().getString(
                        R.string.request_failure));
                LoadingDailog.dismiss();
            }
        });
    }

    /**
     * @param temList
     * @Title: SaveUserinfo
     * @Description: 保存用户信息
     * @author: 欧阳
     * @return: void
     */
    protected void SaveUserinfo(List<SaveUserInfoResponse> temList) {
        Log.i("aa", temList.get(0).getPhoto());
        user.setNickName(temList.get(0).getNickName());
        user.setSex(temList.get(0).getSex());
        user.setPhoto(temList.get(0).getPhoto());
        // 将数据保存在本地数据库里面
        UserManager.updateUserinfo(activity, user);
        back();
    }

    /**
     * @param Checked
     * @Title: setLabel
     * @Description: 根据性别控件设置背景
     * @author: 欧阳
     * @return: void
     */
    public void setLabel(int Checked) {
        Drawable gray = getResources().getDrawable(R.drawable.btn_bg_gray);
        Drawable red = getResources().getDrawable(R.drawable.btn_bg_red);
        tv_boy.setBackgroundDrawable(Checked == R.id.tv_sex_boy ? red : gray);
        tv_girl.setBackgroundDrawable(Checked == R.id.tv_sex_girl ? red : gray);
        if (TextUtils.isEmpty(user.getPhoto())) {
            iv_photo.setImageResource(Checked == R.id.tv_sex_boy ? R.drawable.tx_man : R.drawable.tx_woman);
        }
    }
}
