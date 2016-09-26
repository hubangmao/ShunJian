package cc.ruit.shunjianmei.home.me;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.title.TitleUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.net.api.FileUploadApi;
import cc.ruit.shunjianmei.net.api.H_GetInformationApi;
import cc.ruit.shunjianmei.net.api.H_SetInformationApi;
import cc.ruit.shunjianmei.net.request.H_GetInformationRequest;
import cc.ruit.shunjianmei.net.request.H_SetInformationRequest;
import cc.ruit.shunjianmei.net.request.UploadImageRequest;
import cc.ruit.shunjianmei.net.response.H_GetInformationResponse;
import cc.ruit.shunjianmei.net.response.UploadImageResponce;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.HairdresserCutPicDialogUtils;
import cc.ruit.shunjianmei.util.HairdresserCutPicDialogUtils.OnChoosPicResultListener;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

public class MyInformationFragment extends BaseFragment {

    private final String TAG = MyInformationFragment.class.getSimpleName();
    private Bitmap finish_bit;
    private int type = -1;
    private static String str_path;
    private String imgUrlimgUrl;
    @ViewInject(R.id.iv_me_information_hairdress_photo)
    private ImageView ivPhoto;
    @ViewInject(R.id.iv_me_information_picid_bg1)
    private ImageView bgPicId1;
    @ViewInject(R.id.iv_me_information_picid_bg2)
    private ImageView bgPicId2;
    @ViewInject(R.id.iv_me_information_picid_bg3)
    private ImageView bgPicId3;

    @ViewInject(R.id.iv_me_information_picid1)
    private ImageView picId1;
    @ViewInject(R.id.iv_me_information_picid2)
    private ImageView picId2;
    @ViewInject(R.id.iv_me_information_picid3)
    private ImageView picId3;
    public static String postion = "";

    @ViewInject(R.id.et_my_information_nickname)
    private EditText etNickName;// 昵称
    @ViewInject(R.id.et_me_information_intro)
    private EditText etIntro;// 个人简介
    @ViewInject(R.id.et_my_information_nationality)
    private EditText etNationallity;// 国籍
    @ViewInject(R.id.et_my_information_language)
    private EditText etLanguage;// 语言
    @ViewInject(R.id.et_my_information_hairstyle)
    private EditText etHairStyle;// 擅长
    @ViewInject(R.id.et_my_information_hobbies)
    private EditText etHobbies;// 爱好
    @ViewInject(R.id.btn_my_information_hairstyle_save)
    private Button btnHairStyle;// 保存擅长
    @ViewInject(R.id.btn_my_information_Hobbie_save)
    private Button btnHobbies;// 保存爱好
    @ViewInject(R.id.tv_my_information_hairstyle1)
    private TextView tvHairStyle1;// 擅长
    @ViewInject(R.id.tv_my_information_hairstyle2)
    private TextView tvHairStyle2;// 擅长
    @ViewInject(R.id.tv_my_information_hairstyle3)
    private TextView tvHairStyle3;// 擅长
    @ViewInject(R.id.tv_my_information_hairstyle4)
    private TextView tvHairStyle4;// 擅长
    @ViewInject(R.id.tv_my_information_hobbies1)
    private TextView tvHobbies1;// 爱好
    @ViewInject(R.id.tv_my_information_hobbies2)
    private TextView tvHobbies2;// 爱好
    @ViewInject(R.id.tv_my_information_hobbies3)
    private TextView tvHobbies3;// 爱好
    @ViewInject(R.id.tv_my_information_hobbies4)
    private TextView tvHobbies4;// 爱好
    @ViewInject(R.id.tv_me_information_null_hairstyle)
    TextView tvNullHairsttyle;
    @ViewInject(R.id.tv_me_information_null_hobbies)
    TextView tvNullHobbies;
    String imgUrl;// 路径
    private int screenWidth;
    StringBuffer sbHairStyle = new StringBuffer();// 拼接 擅长 字符串
    StringBuffer sbHobbies = new StringBuffer();// 拼接 爱好 字符串

    HairdresserCutPicDialogUtils cpdu;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.my_information_layout, null);
        ViewUtils.inject(this, view);
        initImageViewHeight();
        initTitle();
        getDataMsg();
        return view;
    }

    private void initImageViewHeight() {
        screenWidth = ScreenUtils.getScreenWidth(activity);
        LayoutParams lp1 = (LayoutParams) bgPicId1.getLayoutParams();
        LayoutParams lp2 = (LayoutParams) bgPicId2.getLayoutParams();
        LayoutParams lp3 = (LayoutParams) bgPicId3.getLayoutParams();

        lp1.height = screenWidth / 16 * 7;
        lp2.height = screenWidth / 4 * 3;
        lp3.height = screenWidth / 4 * 3;

    }

    @OnClick({R.id.iv_me_information_hairdress_photo, R.id.iv_me_information_picid1, R.id.iv_me_information_picid2,
            R.id.iv_me_information_picid3, R.id.schedule_setting_button_save, R.id.btn_my_information_hairstyle_save,
            R.id.btn_my_information_Hobbie_save, R.id.tv_my_information_hairstyle1, R.id.tv_my_information_hairstyle2,
            R.id.tv_my_information_hairstyle3, R.id.tv_my_information_hairstyle4, R.id.tv_my_information_hobbies1,
            R.id.tv_my_information_hobbies2, R.id.tv_my_information_hobbies3, R.id.tv_my_information_hobbies4,
            R.id.iv_me_information_picid_bg1, R.id.iv_me_information_picid_bg2, R.id.iv_me_information_picid_bg3})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_me_information_hairdress_photo:// 头像
                selectPhoto();
                type = -1;
                postion = "hairdressPhoto";
                break;
            case R.id.iv_me_information_picid1:// 第一张形象照片
            case R.id.iv_me_information_picid_bg1:// 第一张形象照片
                Log.i("main", TAG + "启动图库剪切图片开始");
                selectPhoto();
                postion = "picID1";
                type = 1;
                break;
            case R.id.iv_me_information_picid2:// 第二张形象照片
            case R.id.iv_me_information_picid_bg2:// 第二张形象照片
                selectPhoto();
                type = -2;
                postion = "picID2";
                break;
            case R.id.iv_me_information_picid3:// 第三张形象照片
            case R.id.iv_me_information_picid_bg3:// 第三张形象照片
                type = -2;
                selectPhoto();
                postion = "picID3";
                break;
            case R.id.schedule_setting_button_save:// 保存
                checkSaveParames();
                if (sbHairStyle.length() > 0) {
                    sbHairStyle.delete(0, sbHairStyle.length());
                }
                if (sbHobbies.length() > 0) {
                    sbHobbies.delete(0, sbHobbies.length());
                }

                break;
            case R.id.btn_my_information_hairstyle_save:// 保存擅长
                SaveHairtyle();
                break;
            case R.id.btn_my_information_Hobbie_save:// 保存爱好
                SaveHobbies();
                break;
            case R.id.tv_my_information_hairstyle1:// 擅长 标签1
                new AlertDialog.Builder(activity).setMessage("你确定删除“" + tvHairStyle1.getText() + "”这个标签吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvHairStyle1.setText("");
                                tvHairStyle1.setVisibility(View.INVISIBLE);
                                etHairStyle.setEnabled(true);
                                visibleHairstyle();
                            }

                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_my_information_hairstyle2:// 擅长 标签2
                new AlertDialog.Builder(activity).setMessage("你确定删除“" + tvHairStyle2.getText() + "”这个标签吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvHairStyle2.setText("");
                                tvHairStyle2.setVisibility(View.INVISIBLE);
                                etHairStyle.setEnabled(true);
                                visibleHairstyle();
                            }

                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_my_information_hairstyle3:// 擅长 标签3
                new AlertDialog.Builder(activity).setMessage("你确定删除“" + tvHairStyle3.getText() + "”这个标签吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvHairStyle3.setText("");
                                tvHairStyle3.setVisibility(View.INVISIBLE);
                                etHairStyle.setEnabled(true);
                                visibleHairstyle();
                            }

                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_my_information_hairstyle4:// 擅长 标签4
                new AlertDialog.Builder(activity).setMessage("你确定删除“" + tvHairStyle4.getText() + "”这个标签吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvHairStyle4.setText("");
                                tvHairStyle4.setVisibility(View.INVISIBLE);
                                etHairStyle.setEnabled(true);
                                visibleHairstyle();
                            }

                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_my_information_hobbies1:
                new AlertDialog.Builder(activity).setMessage("你确定删除“" + tvHobbies1.getText() + "”这个标签吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvHobbies1.setText("");
                                tvHobbies1.setVisibility(View.INVISIBLE);
                                etHobbies.setEnabled(true);
                                visibleHobbies();
                            }

                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_my_information_hobbies2:
                new AlertDialog.Builder(activity).setMessage("你确定删除“" + tvHobbies2.getText() + "”这个标签吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvHobbies2.setText("");
                                tvHobbies2.setVisibility(View.INVISIBLE);
                                etHobbies.setEnabled(true);
                                visibleHobbies();
                            }

                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_my_information_hobbies3:
                new AlertDialog.Builder(activity).setMessage("你确定删除“" + tvHobbies3.getText() + "”这个标签吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvHobbies3.setText("");
                                tvHobbies3.setVisibility(View.INVISIBLE);
                                etHobbies.setEnabled(true);
                                visibleHobbies();
                            }

                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_my_information_hobbies4:
                new AlertDialog.Builder(activity).setMessage("你确定删除“" + tvHobbies4.getText() + "”这个标签吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvHobbies4.setText("");
                                tvHobbies4.setVisibility(View.INVISIBLE);
                                etHobbies.setEnabled(true);
                                visibleHobbies();
                            }

                        }).setNegativeButton("取消", null).show();
                break;
        }
    }

    private void visibleHobbies() {
        if (TextUtils.isEmpty(tvHobbies1.getText()) && TextUtils.isEmpty(tvHobbies2.getText())
                && TextUtils.isEmpty(tvHobbies3.getText()) && TextUtils.isEmpty(tvHobbies4.getText())) {
            tvNullHobbies.setVisibility(View.VISIBLE);
        }
    }

    private void visibleHairstyle() {
        if (TextUtils.isEmpty(tvHairStyle1.getText()) && TextUtils.isEmpty(tvHairStyle2.getText())
                && TextUtils.isEmpty(tvHairStyle3.getText()) && TextUtils.isEmpty(tvHairStyle4.getText())) {
            tvNullHairsttyle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 保存爱好
     */
    private void SaveHobbies() {
        if (!TextUtils.isEmpty(etHobbies.getText())) {
            if (TextUtils.isEmpty(tvHobbies1.getText())) {
                tvHobbies1.setText(etHobbies.getText());
                tvNullHobbies.setVisibility(View.GONE);
                tvHobbies1.setVisibility(View.VISIBLE);
                etHobbies.setText("");
            } else {
                if (TextUtils.isEmpty(tvHobbies2.getText())) {
                    tvHobbies2.setText(etHobbies.getText());
                    tvNullHobbies.setVisibility(View.GONE);
                    tvHobbies2.setVisibility(View.VISIBLE);
                    etHobbies.setText("");
                } else {
                    if (TextUtils.isEmpty(tvHobbies3.getText())) {
                        tvHobbies3.setText(etHobbies.getText());
                        tvNullHobbies.setVisibility(View.GONE);
                        tvHobbies3.setVisibility(View.VISIBLE);
                        etHobbies.setText("");
                    } else {
                        if (TextUtils.isEmpty(tvHobbies4.getText())) {
                            tvHobbies4.setText(etHobbies.getText());
                            tvNullHobbies.setVisibility(View.GONE);
                            tvHobbies4.setVisibility(View.VISIBLE);
                            sbHobbies.append(tvHobbies4.getText().toString());
                            etHobbies.setText("");
                            etHobbies.setEnabled(false);
                        }
                    }
                }
            }
        } else {
            ToastUtils.showShort("请输入爱好");
        }
    }

    /**
     * 保存擅长
     */
    private void SaveHairtyle() {
        if (!TextUtils.isEmpty(etHairStyle.getText())) {
            if (TextUtils.isEmpty(tvHairStyle1.getText())) {
                tvHairStyle1.setText(etHairStyle.getText());
                tvNullHairsttyle.setVisibility(View.GONE);
                tvHairStyle1.setVisibility(View.VISIBLE);
                etHairStyle.setText("");
            } else {
                if (TextUtils.isEmpty(tvHairStyle2.getText())) {
                    tvHairStyle2.setText(etHairStyle.getText());
                    tvNullHairsttyle.setVisibility(View.GONE);
                    tvHairStyle2.setVisibility(View.VISIBLE);
                    etHairStyle.setText("");
                } else {
                    if (TextUtils.isEmpty(tvHairStyle3.getText())) {
                        tvHairStyle3.setText(etHairStyle.getText());
                        tvNullHairsttyle.setVisibility(View.GONE);
                        tvHairStyle3.setVisibility(View.VISIBLE);
                        etHairStyle.setText("");
                    } else {
                        if (TextUtils.isEmpty(tvHairStyle4.getText())) {
                            tvHairStyle4.setText(etHairStyle.getText());
                            tvHairStyle4.setVisibility(View.VISIBLE);
                            tvNullHairsttyle.setVisibility(View.GONE);
                            sbHairStyle.append(tvHairStyle4.getText().toString());
                            etHairStyle.setText("");
                            etHairStyle.setEnabled(false);
                        }
                    }
                }
            }
        } else {
            ToastUtils.showShort("请输入擅长");
        }
    }

    /**
     * 选取照片
     */
    private void selectPhoto() {
        cpdu = new HairdresserCutPicDialogUtils(activity, this);
        cpdu.shwoChooseDialog();
        cpdu.setOnChoosPicResultListener(new OnChoosPicResultListener() {

            @Override
            public void onChoosPicResult(int result, String photo) {
                if (result == Activity.RESULT_OK) {
                    imgUrl = photo;
                    Log.i("Tag", "imgUrl=" + imgUrl);
                    Bitmap bm = BitmapFactory.decodeFile(imgUrl);
                    if (type == -1) {
                        finish_bit = zoomImg(bm, 128, 128);
                    } else if (type == 1) {
                        finish_bit = zoomImg(bm, 480, 220);
                    } else {
                        finish_bit = zoomImg(bm, 480, 320);
                    }
                    Boolean yn = saveBitmap2file(finish_bit, System.currentTimeMillis() + ".jpg");
                    UpLoadPhoto();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cpdu.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传照片
     */
    private void UpLoadPhoto() {
        if (!NetWorkUtils.isConnectInternet(getActivity())) {
            ToastUtils.showShort("网络未链接，请检查网络设置");
            return;
        }

        UploadImageRequest sr = new UploadImageRequest(UserManager.getUserID() + "");
        new FileUploadApi().upload(str_path, sr.toJsonString(sr), new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
                        String[] split = result.getMsg().split("\\|");
                        if ("1".equals(split[0])) {
                            ToastUtils.showShort(split[1] + "");
                            LoadingDailog.dismiss();
                        }
                        if (result.getCode() == 1000) {
                            List<UploadImageResponce> Responce = UploadImageResponce.getclazz(result.getData());
                            if (Responce != null) {
                                if (postion == "hairdressPhoto") {
                                    UserManager.setPicID(Integer.parseInt(Responce.get(0).ID));
                                } else if (postion == "picID1") {
                                    UserManager.setPicID1(Integer.parseInt(Responce.get(0).ID));
                                } else if (postion == "picID2") {
                                    UserManager.setPicID2(Integer.parseInt(Responce.get(0).ID));
                                } else if (postion == "picID3") {
                                    UserManager.setPicID3(Integer.parseInt(Responce.get(0).ID));
                                }
                                setHeadPhoto(Responce.get(0).Image);
                            }
                            LoadingDailog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        ToastUtils.showLong(R.string.request_failure);
                        LoadingDailog.dismiss();
                    }
                }

        );

    }

    /**
     * 设置图片显示
     *
     * @param image
     */
    private void setHeadPhoto(String image) {
        ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
        if (postion == "hairdressPhoto") {
            RoundImageLoaderUtil.getInstance(activity, 360).loadImage(image, ivPhoto);
        }
        if (postion == "picID1") {
            picId1.setAlpha(0f);
            ImageLoaderUtils.getInstance(activity).loadImage(image, bgPicId1);
        }
        if (postion == "picID2") {
            picId2.setAlpha(0f);
            ImageLoaderUtils.getInstance(activity).loadImage(image, bgPicId2);
        }
        if (postion == "picID3") {
            picId3.setAlpha(0f);
            ImageLoaderUtils.getInstance(activity).loadImage(image, bgPicId3);
        }
    }

    /**
     * 获取数据
     */
    private void getDataMsg() {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            return;
        }
        H_GetInformationRequest request = new H_GetInformationRequest("" + UserManager.getUserID());
        H_GetInformationApi.GetInformation(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
                if (result.getCode() == 1000) {
                    List<H_GetInformationResponse> response = H_GetInformationResponse.getclazz2(result.getData());
                    if (response.size() > 0 && response != null) {
                        setData(response.get(0));
                    }

                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showLong("数据获取失败");

            }
        });
    }

    /**
     * 设置获取的数据
     *
     * @param response
     */
    private void setData(H_GetInformationResponse response) {
        ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
        int picID = 0;
        int picID1 = 0;
        int picID2 = 0;
        int picID3 = 0;
        if (!response.getPicID().equals("")) {
            picID = Integer.parseInt(response.getPicID());
        }
        if (!response.getPicID1().equals("")) {
            picID1 = Integer.parseInt(response.getPicID1());
        }
        if (!response.getPicID2().equals("")) {
            picID2 = Integer.parseInt(response.getPicID2());
        }
        if (!response.getPicID3().equals("")) {
            picID3 = Integer.parseInt(response.getPicID3());
        }
        etNickName.setText(response.getNickName());
        etNationallity.setText(response.getNationality());
        etLanguage.setText(response.getLanguage());
        etIntro.setText(response.getIntro());
        String[] hairstyle = response.getHairstyle().split("\\|");
        if (hairstyle.length == 4) {
            tvHairStyle1.setText(hairstyle[0]);
            tvHairStyle2.setText(hairstyle[1]);
            tvHairStyle3.setText(hairstyle[2]);
            tvHairStyle4.setText(hairstyle[3]);
            tvNullHairsttyle.setVisibility(View.GONE);
            tvHairStyle1.setVisibility(View.VISIBLE);
            tvHairStyle2.setVisibility(View.VISIBLE);
            tvHairStyle3.setVisibility(View.VISIBLE);
            tvHairStyle4.setVisibility(View.VISIBLE);
            etHairStyle.setEnabled(false);
            btnHairStyle.setOnClickListener(null);
        } else if (hairstyle.length == 3) {
            tvHairStyle1.setText(hairstyle[0]);
            tvHairStyle2.setText(hairstyle[1]);
            tvHairStyle3.setText(hairstyle[2]);
            tvNullHairsttyle.setVisibility(View.GONE);
            tvHairStyle1.setVisibility(View.VISIBLE);
            tvHairStyle2.setVisibility(View.VISIBLE);
            tvHairStyle3.setVisibility(View.VISIBLE);
            tvHairStyle4.setVisibility(View.INVISIBLE);
        } else if (hairstyle.length == 2) {
            tvHairStyle1.setText(hairstyle[0]);
            tvHairStyle2.setText(hairstyle[1]);
            tvNullHairsttyle.setVisibility(View.GONE);
            tvHairStyle1.setVisibility(View.VISIBLE);
            tvHairStyle2.setVisibility(View.VISIBLE);
            tvHairStyle3.setVisibility(View.INVISIBLE);
            tvHairStyle4.setVisibility(View.INVISIBLE);
        } else if (hairstyle.length == 1 && !hairstyle[0].equals("")) {
            tvHairStyle1.setText(hairstyle[0]);
            tvNullHairsttyle.setVisibility(View.GONE);
            tvHairStyle1.setVisibility(View.VISIBLE);
            tvHairStyle2.setVisibility(View.INVISIBLE);
            tvHairStyle3.setVisibility(View.INVISIBLE);
            tvHairStyle4.setVisibility(View.INVISIBLE);
        } else {
            tvNullHairsttyle.setVisibility(View.VISIBLE);
            tvHairStyle1.setVisibility(View.INVISIBLE);
            tvHairStyle2.setVisibility(View.INVISIBLE);
            tvHairStyle3.setVisibility(View.INVISIBLE);
            tvHairStyle4.setVisibility(View.INVISIBLE);
        }

        String[] hobbies = response.getHobbies().split("\\|");
        if (hobbies.length == 4) {
            tvHobbies1.setText(hobbies[0]);
            tvHobbies2.setText(hobbies[1]);
            tvHobbies3.setText(hobbies[2]);
            tvHobbies4.setText(hobbies[3]);
            tvNullHobbies.setVisibility(View.GONE);
            tvHobbies1.setVisibility(View.VISIBLE);
            tvHobbies2.setVisibility(View.VISIBLE);
            tvHobbies3.setVisibility(View.VISIBLE);
            tvHobbies4.setVisibility(View.VISIBLE);
            etHobbies.setEnabled(false);
            btnHobbies.setOnClickListener(null);
        } else if (hobbies.length == 3) {
            tvHobbies1.setText(hobbies[0]);
            tvHobbies2.setText(hobbies[1]);
            tvHobbies3.setText(hobbies[2]);
            tvNullHobbies.setVisibility(View.GONE);
            tvHobbies1.setVisibility(View.VISIBLE);
            tvHobbies2.setVisibility(View.VISIBLE);
            tvHobbies3.setVisibility(View.VISIBLE);
            tvHobbies4.setVisibility(View.INVISIBLE);
        } else if (hobbies.length == 2) {
            tvHobbies1.setText(hobbies[0]);
            tvHobbies2.setText(hobbies[1]);
            tvNullHobbies.setVisibility(View.GONE);
            tvHobbies1.setVisibility(View.VISIBLE);
            tvHobbies2.setVisibility(View.VISIBLE);
            tvHobbies3.setVisibility(View.INVISIBLE);
            tvHobbies4.setVisibility(View.INVISIBLE);
        } else if (hobbies.length == 1 && !hobbies[0].equals("")) {
            tvHobbies1.setText(hobbies[0]);
            tvNullHobbies.setVisibility(View.GONE);
            tvHobbies1.setVisibility(View.VISIBLE);
            tvHobbies2.setVisibility(View.INVISIBLE);
            tvHobbies3.setVisibility(View.INVISIBLE);
            tvHobbies4.setVisibility(View.INVISIBLE);
        } else {
            tvHobbies1.setVisibility(View.INVISIBLE);
            tvHobbies2.setVisibility(View.INVISIBLE);
            tvHobbies3.setVisibility(View.INVISIBLE);
            tvHobbies4.setVisibility(View.INVISIBLE);
        }
        UserManager.setPicID(picID);
        if (!TextUtils.isEmpty(response.getPicturePath())) {
            RoundImageLoaderUtil.getInstance(activity, 360).loadImage(response.getPicturePath(), ivPhoto);
        }
        UserManager.setPicID1(picID1);
        if (!TextUtils.isEmpty(response.getPicturePath1())) {
            ImageLoaderUtils.getInstance(activity).loadImage(response.getPicturePath1(), bgPicId1);
            picId1.setAlpha(0f);
        }
        UserManager.setPicID2(picID2);
        if (!TextUtils.isEmpty(response.getPicturePath2())) {
            ImageLoaderUtils.getInstance(activity).loadImage(response.getPicturePath2(), bgPicId2);
            picId2.setAlpha(0f);
        }
        UserManager.setPicID3(picID3);
        if (!TextUtils.isEmpty(response.getPicturePath3())) {
            ImageLoaderUtils.getInstance(activity).loadImage(response.getPicturePath3(), bgPicId3);
            picId3.setAlpha(0f);
        }

    }

    private String nickName;
    private String language;
    private String Nationallity;
    private String intro;

    /**
     * 发送数据，保存个人信息到服务器
     */
    private void SaveMyInformation() {
        nickName = etNickName.getText().toString();// 获取用户输入的昵称
        language = etLanguage.getText().toString();// 获取用户输入的语言
        Nationallity = etNationallity.getText().toString();// 获取用户输入的国家
        intro = etIntro.getText().toString();// 获取用户输入的个人简介
        // 擅长
        if (!TextUtils.isEmpty(tvHairStyle1.getText()) && !tvHairStyle1.getText().equals("")) {
            sbHairStyle.append(tvHairStyle1.getText().toString() + "|");
        }
        if (!TextUtils.isEmpty(tvHairStyle2.getText()) && !tvHairStyle2.getText().equals("")) {
            sbHairStyle.append(tvHairStyle2.getText().toString() + "|");
        }
        if (!TextUtils.isEmpty(tvHairStyle3.getText())) {
            sbHairStyle.append(tvHairStyle3.getText().toString() + "|");
        }
        if (!TextUtils.isEmpty(tvHairStyle4.getText())) {
            sbHairStyle.append(tvHairStyle4.getText().toString());
        }
        // 爱好
        if (!TextUtils.isEmpty(tvHobbies1.getText())) {
            sbHobbies.append(tvHobbies1.getText().toString() + "|");
        }
        if (!TextUtils.isEmpty(tvHobbies2.getText())) {
            sbHobbies.append(tvHobbies2.getText().toString() + "|");
        }
        if (!TextUtils.isEmpty(tvHobbies3.getText())) {
            sbHobbies.append(tvHobbies3.getText().toString() + "|");
        }
        if (!TextUtils.isEmpty(tvHobbies4.getText())) {
            sbHobbies.append(tvHobbies4.getText().toString());
        }
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            return;
        }
        H_SetInformationRequest request = new H_SetInformationRequest("" + UserManager.getUserID(),
                "" + UserManager.getPicID(), "" + UserManager.getPicID1(), "" + UserManager.getPicID2(),
                "" + UserManager.getPicID3(), nickName, intro, sbHairStyle.toString(), sbHobbies.toString(),
                Nationallity, language);
        H_SetInformationApi.SetInformation(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
                if (result.getCode() == 1000) {
                    LoadingDailog.dismiss();
                    ToastUtils.showShort("保存成功！");
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
                LoadingDailog.dismiss();

            }
        });
    }

    /**
     * 校验保存参数
     *
     * @return
     */
    private void checkSaveParames() {
        if (TextUtils.isEmpty(etNickName.getText())) {
            ToastUtils.showShort("请输入昵称");
        } else if (TextUtils.isEmpty(etIntro.getText())) {
            ToastUtils.showShort("请输入个人简介");
        } else if (TextUtils.isEmpty(tvHairStyle1.getText())) {
            ToastUtils.showShort("请输入至少一个擅长发型");
        } else if (TextUtils.isEmpty(tvHobbies1.getText())) {
            ToastUtils.showShort("请输入至少一个爱好");
        } else if (UserManager.getPicID1() == 0) {
            ToastUtils.showShort("请添加形象照片用于封面");
        } else if (UserManager.getPicID2() == 0) {
            ToastUtils.showShort("请添加形象照片");
        } else if (UserManager.getPicID3() == 0) {
            ToastUtils.showShort("请添加形象照片");
        } else {
            LoadingDailog.show(activity, "正在上传用户信息....");
            SaveMyInformation();
        }
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        TitleUtil title = new TitleUtil(view);
        title.tv_title.setText("个人信息");
        title.iv_left.setVisibility(View.VISIBLE);
        title.iv_left.setImageResource(R.drawable.arrow_left_white);
        title.iv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.getIntent(activity, MeFragment.class.getName());
                startActivity(intent);
                activity.finish();
            }

        });

        title.tv_right.setText("保存");
        title.tv_right.setVisibility(View.VISIBLE);
        title.tv_right.setTextColor(Color.WHITE);
        title.tv_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                checkSaveParames();
                if (sbHairStyle.length() > 0) {
                    sbHairStyle.delete(0, sbHairStyle.length());
                }
                if (sbHobbies.length() > 0) {
                    sbHobbies.delete(0, sbHobbies.length());
                }
            }
        });
    }

    /**
     * 处理图片
     *
     * @param bm        所要转换的bitmap
     * @param newWidth  新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // 得到新的图片 www.2cto.com
        Bitmap newBm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newBm;
    }

    static boolean saveBitmap2file(Bitmap bmp, String filename) {
        CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        str_path = "/sdcard/" + filename;
        // String str = "/storage/emulated/0/.chache/cache/image/" +
        // System.currentTimeMillis();
        try {
            stream = new FileOutputStream(str_path);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

}
