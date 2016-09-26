package cc.ruit.shunjianmei.home.hairstyle;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertCommentFragment;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertDetailsFragment;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.login.LoginFragment;
import cc.ruit.shunjianmei.net.api.HairStyleDetailApi;
import cc.ruit.shunjianmei.net.api.SaveFavoriteApi;
import cc.ruit.shunjianmei.net.request.HairStyleDetailRequest;
import cc.ruit.shunjianmei.net.request.SaveFavoriteRequest;
import cc.ruit.shunjianmei.net.response.Comment;
import cc.ruit.shunjianmei.net.response.HairStyleDetailResponse;
import cc.ruit.shunjianmei.net.response.HairStyleDetailResponse.ServiceTimes;
import cc.ruit.shunjianmei.reserve.ReserveActivity;
import cc.ruit.shunjianmei.reserve.ReserveFragment;
import cc.ruit.shunjianmei.reserve.ReserveFreeFragment;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.ForLargeImageActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.android.volley.Response;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.messagedialog.MessageDialog;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: HairStyleDetailsFragment
 * @Description: 发型订单详情
 * @author: 欧阳
 * @date: 2015年10月25日 上午10:11:44
 */
public class HairStyleDetailsFragment extends BaseFragment {
    @ViewInject(R.id.tv_hairstyle_details_name)
    TextView details_name;// 详情发型名字
    @ViewInject(R.id.tv_HairStyleDetail_price)
    TextView Detail_price;// 发型价格
    @ViewInject(R.id.tv_HairStyleDetail_service)
    TextView Detail_service;// 发型服务
    @ViewInject(R.id.tv_HairStyleDetail_time)
    TextView Detail_time;// 发型服务时间
    @ViewInject(R.id.iv_hairstyle_details_photo)
    ImageView details_big_photo;// 发型大图
    @ViewInject(R.id.iv_HairStyleDetail_photo)
    ImageView Detail_photo;// 发型师头像
    @ViewInject(R.id.tv_HairStyleDetail_mastername)
    TextView mastername;// 发型名字
    @ViewInject(R.id.tv_HairStyleDetail_levename)
    TextView levename;// 职业水平名字
    @ViewInject(R.id.tv_HairStyleDetail_star)
    TextView star;// 星座
    @ViewInject(R.id.ratingbar_all_masterevaluate)
    RatingBar masterevaluate;// 发型评分
    @ViewInject(R.id.tv_HairStyleDetail_OrderNum)
    TextView OrderNum;// 订单量
    @ViewInject(R.id.tv_HairStyleDetail_Content)
    TextView tv_Content;// 发型简介内容
    @ViewInject(R.id.tv_hairStryleDetails_Comment_num)
    TextView Comment_num;// 评论数据
    @ViewInject(R.id.tv_hairStryleDetails_title)
    TextView tv_title;// 标题
    @ViewInject(R.id.iv_hairStryleDetails_right_image)
    ImageView right_image;// 点赞
    @ViewInject(R.id.tv_hairStryleDetails_commentName)
    TextView tv_commentName;// 评论姓名
    @ViewInject(R.id.tv_hairStryleDetails_commmentTime)
    TextView tv_commmentTime;// 评论时间
    @ViewInject(R.id.tv_hairStryleDetails_commentcontent)
    TextView tv_commentcontent;// 评论内容
    @ViewInject(R.id.image_hairStryleDetails_comment_style1)
    ImageView style1;// 发型1
    @ViewInject(R.id.image_hairStryleDetails_comment_style2)
    ImageView style2;// 发型2
    @ViewInject(R.id.image_hairStryleDetails_comment_style3)
    ImageView style3;// 发型3
    @ViewInject(R.id.image_hairStryleDetails_commentPhoto)
    ImageView tv_commentPhoto;// 评论头像
    @ViewInject(R.id.ratingbar_hairStryleDetails_commentScore)
    RatingBar tv_commentScore;// 评论分数
    @ViewInject(R.id.tv_HairStyleDetail_service)
    TextView service;// 服务
    @ViewInject(R.id.tv_HairStyleDetail_ps)
    TextView ps;// 服务
    @ViewInject(R.id.tv_empty_hairStylecomment)
    TextView hairStylecomment;// 显示暂时没有评论
    @ViewInject(R.id.rl_hairStryleDetail_comment)
    RelativeLayout rl_hairStryleDetails_Comment;// 评论选择项
    @ViewInject(R.id.tv_HairStyleDetail_xinxi)
    TextView xinxi;// 备注
    List<HairStyleDetailResponse> templist;
    int commentNum = 0;// 评论数

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.hair_style_details_fragment, null);
        ViewUtils.inject(this, view);
        LoadingDailog.show(activity, "正在加载中...");
        initData();
        return view;
    }

    String ID;// 订单ID
    String Name;

    /**
     * @Title: initData
     * @Description: 请求数据网络请求
     * @author: 欧阳
     * @return: void
     */
    private void initData() {
        if (this.getArguments() != null) {
            ID = this.getArguments().getString("ID");
            Name = this.getArguments().getString("Name");
            if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
                ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
                LoadingDailog.dismiss();
                return;
            }
            LoadingDailog.show(activity, "正在加载.....");
            HairStyleDetailRequest request = new HairStyleDetailRequest(UserManager.getUserID() + "", ID);
            HairStyleDetailApi.HairStyleDetail(request, new RequestCallBack<String>() {

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
                        templist = HairStyleDetailResponse.getclazz2(result.getData());
                        if (templist != null && templist.size() > 0) {
                            resultHanlder();
                        } else {
                            return;
                        }

                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    LoadingDailog.dismiss();
                    ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
                }
            });
        } else {
            LoadingDailog.dismiss();
        }
    }

    /**
     * 图片仅供参考，到店后发型师会为您量身定做合适的发型
     *
     * @Title: resultHanlder
     * @Description: 绑定控件
     * @author: 欧阳
     * @return: void
     */
    protected void resultHanlder() {
        ps.setVisibility(View.VISIBLE);
        if (templist != null && templist.size() > 0) {
            if (Integer.parseInt(templist.get(0).getIsFavorite()) == 0) {
                right_image.setImageResource(R.drawable.star_collect_solid);
            } else {
                right_image.setImageResource(R.drawable.star_collect_stroke);
            }
            ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
            ImageLoaderUtils.getInstance(activity).loadImage(templist.get(0).getPhoto(), details_big_photo);
            RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
            RoundImageLoaderUtil.getInstance(activity, 360).loadImage(templist.get(0).getUser().Photo, Detail_photo);
            details_name.setText(templist.get(0).getName());
            Detail_price.setText("¥" + templist.get(0).getPrice());
            mastername.setText(templist.get(0).getUser().Name);
            levename.setText(templist.get(0).getUser().LevelName);
            star.setText(templist.get(0).getUser().Star);
            ServiceTimes serviceTimes = templist.get(0).getServiceTimes();
            Detail_time.setText(serviceTimes.toString(serviceTimes.染发, serviceTimes.烫发));
            service.setText(serviceTimes.toString2(serviceTimes.设计剪发));
            if (!TextUtils.isEmpty(templist.get(0).getUser().Score)) {
                Float score = Float.valueOf(templist.get(0).getUser().Score);
                masterevaluate.setVisibility(View.VISIBLE);
                masterevaluate.setRating(score);
            }
            tv_title.setText(templist.get(0).getUser().Name + "的发型");
            OrderNum.setText("接单" + templist.get(0).getUser().OrderNum + "次");
            tv_Content.setText(templist.get(0).getIntro());
            xinxi.setVisibility(View.VISIBLE);
            try {
                commentNum = Integer.parseInt(templist.get(0).getCommentNum());
            } catch (Exception e) {
            }
            if (commentNum > 0) {
                Comment_num.setText("(" + templist.get(0).getCommentNum() + ")");
                final Comment comment = templist.get(0).getComment();
                tv_commentName.setText(comment.Name);

                RoundImageLoaderUtil.getInstance(activity, 360).loadImage(comment.Photo, tv_commentPhoto);
                tv_commmentTime.setText(comment.Time.substring(0, comment.Time.length() - 3));
                if (!TextUtils.isEmpty(comment.Score)) {
                    Float Score = Float.valueOf(comment.Score);
                    tv_commentScore.setRating(Score);
                } else {
                    tv_commentScore.setRating(0);
                }
                tv_commentcontent.setText(comment.Content);
                // ImageView[] image = { style1, style2, style3 };
                // if (comment.Item != null && comment.Item.size() > 0) {
                // for (int i = 0; i < comment.Item.size(); i++) {
                // image[i].setVisibility(View.VISIBLE);
                // RoundImageLoaderUtil.getInstance(activity, 10)
                // .loadImage(comment.Item.get(i).Photo, image[i]);
                //
                // final int index = i;
                // image[i].setOnClickListener(new OnClickListener() {
                // @Override
                // public void onClick(View v) {
                // ArrayList<String> images = new ArrayList<String>();
                // for (int j = 0; j < comment.Item.size(); j++) {
                // images.add(comment.Item.get(j).Photo);
                // }
                // startActivity(ForLargeImageActivity.getIntent(activity,
                // index, images));
                // }
                // });
                // }
                // }
                final ImageView[] image = {style1, style2, style3};
                Log.i("main", "ImageUrl1=" + comment.Item.get(0));
                if (comment.Item != null && comment.Item.size() > 0) {
                    if (comment.Item.size() == 1) {
                        image[0].setVisibility(View.VISIBLE);
                        RoundImageLoaderUtil.getInstance(activity, 10).loadImage(comment.Item.get(0).Photo, image[0]);

                    } else {
                        image[1].setVisibility(View.INVISIBLE);
                        image[2].setVisibility(View.INVISIBLE);
                    }
                    if (comment.Item.size() == 2) {
                        image[0].setVisibility(View.VISIBLE);
                        image[1].setVisibility(View.VISIBLE);
                        RoundImageLoaderUtil.getInstance(activity, 10).loadImage(comment.Item.get(0).Photo, image[0]);
                        RoundImageLoaderUtil.getInstance(activity, 10).loadImage(comment.Item.get(1).Photo, image[1]);
                    } else {
                        image[2].setVisibility(View.INVISIBLE);
                    }
                    if (comment.Item.size() == 3) {
                        for (int i = 0; i < image.length; i++) {
                            image[i].setVisibility(View.VISIBLE);
                            RoundImageLoaderUtil.getInstance(activity, 10).loadImage(comment.Item.get(i).Photo,
                                    image[i]);

                        }
                    }
                    for (int i = 0; i < comment.Item.size(); i++) {
                        final int index = i;
                        image[i].setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<String> images = new ArrayList<String>();
                                for (int j = 0; j < comment.Item.size(); j++) {
                                    ImageLoaderUtils.getInstance(activity).loadImage(
                                            comment.Item.get(j).Photo, image[j]);
                                    images.add(comment.Item.get(j).Photo);
                                }
                                startActivity(ForLargeImageActivity.getIntent(activity, index, images));
                            }
                        });
                    }
                }
            } else {
                rl_hairStryleDetails_Comment.setVisibility(View.GONE);
                hairStylecomment.setVisibility(View.VISIBLE);
            }

        }
    }

    @OnClick({R.id.rl_hairStryleDetails_Comment, R.id.btn_hairStyleDetailappointment,
            R.id.iv_hairStryleDetails_right_image, R.id.iv_hairStryleDetails_back, R.id.iv_HairStyleDetail_photo})
    public void methodClick(View v) {
        switch (v.getId()) {
            case R.id.rl_hairStryleDetails_Comment:// 打开评论
                if (commentNum > 0) {
                    Fragment fragment = FragmentManagerUtils.getFragment(activity,
                            HairDressertCommentFragment.class.getName());
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", templist.get(0).getUser().UserID);
                    bundle.putString("Name", Name);
                    fragment.setArguments(bundle);
                    FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
                }
                break;
            case R.id.btn_hairStyleDetailappointment:// 预约
                if (UserManager.getUserID() <= 0) {
                    showUnLoginDialog();
                    break;
                }
                if (templist.get(0).getUser().istype.equals("1")) {
                    if (templist.get(0).getUser().hasPacked.equals("0")) {
                        ToastUtils.showShort("无此套餐");
                    } else {
                        Intent intent = ReserveActivity.getIntent(activity, ReserveFreeFragment.class.getName());
                        Bundle bundle = new Bundle();
                        bundle.putString("ID", templist.get(0).getUser().UserID);
                        bundle.putString("HairStyleId", ID);
                        bundle.putString("HairStyleName", Name);
                        bundle.putString("From", HairStyleDetailsFragment.class.getSimpleName());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = ReserveActivity.getIntent(activity, ReserveFragment.class.getName());
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", templist.get(0).getUser().UserID);
                    bundle.putString("HairStyleId", ID);
                    bundle.putString("HairStyleName", Name);
                    bundle.putString("From", HairStyleDetailsFragment.class.getSimpleName());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                break;
            case R.id.iv_hairStryleDetails_right_image:// 收藏
                if (UserManager.getUserID() <= 0) {
                    showUnLoginDialog();
                    break;
                }
                LoadingDailog.show(activity, "正在收藏...");
                if (Integer.parseInt(templist.get(0).getIsFavorite()) == 0) {
                    SaveFoodDiariesFavorite(ID, "1");
                } else if (Integer.parseInt(templist.get(0).getIsFavorite()) == 1) {
                    SaveFoodDiariesFavorite(ID, "0");
                }
                break;
            case R.id.iv_hairStryleDetails_back:// 返回
                boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
                if (!isBack) {
                    getActivity().finish();
                }
                break;
            case R.id.iv_HairStyleDetail_photo:
                openhairstylistDetails();
                break;
            default:
                break;
        }
    }

    /**
     * @Title: openhairstylistDetails
     * @Description: 打开美发师详情
     * @author: 欧阳
     * @return: void
     */
    private void openhairstylistDetails() {
        Fragment fragment = FragmentManagerUtils.getFragment(activity, HairDressertDetailsFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putString("ID", templist.get(0).getUser().UserID);
        bundle.putString("Name", templist.get(0).getUser().Name);
        if (templist.get(0).getUser().istype.equals("1")) {
            bundle.putString("Type", "自由定价");
        } else {
            bundle.putString("Type", "资深合作");
        }
        fragment.setArguments(bundle);
        FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
    }

    /**
     * @param iD
     * @param State
     * @Title: SaveFoodDiariesFavorite
     * @Description: 网络请求
     * @author: 欧阳
     * @return: void
     */
    protected void SaveFoodDiariesFavorite(String iD, final String State) {
        SaveFavoriteRequest request = new SaveFavoriteRequest(UserManager.getUserID() + "", "1", iD, State);
        SaveFavoriteApi.SaveFavorite(request, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LoadingDailog.dismiss();
                BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
                if (response.getCode() == 1000) {
                    if (Integer.parseInt(State) == 1) {
                        right_image.setImageResource(R.drawable.star_collect_stroke);
                        ToastUtils.showShort("收藏成功");
                        EventBus.getDefault().post(new MyEventBus("请刷新发型收藏列表"));
                        templist.get(0).setIsFavorite("1");
                    } else {
                        ToastUtils.showShort("取消收藏");
                        right_image.setImageResource(R.drawable.star_collect_solid);
                        templist.get(0).setIsFavorite("0");
                        EventBus.getDefault().post(new MyEventBus("请刷新发型收藏列表"));
                    }
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort("收藏关注失败");
                LoadingDailog.dismiss();
            }
        });

    }

    /**
     * @Title: showDialog
     * @Description: 提示登录
     * @author: lee
     * @return: void
     */
    void showUnLoginDialog() {
        final MessageDialog messageDialog = new MessageDialog(activity);
        messageDialog.setMessage("请先登录");
        messageDialog.getOkButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动登录
                Bundle bundle = new Bundle();
                bundle.putString("from", HairDressertDetailsFragment.class.getSimpleName());
                Intent in = LoginActivity.getIntent(activity, LoginFragment.class.getName());
                in.putExtras(bundle);
                startActivity(in);
                messageDialog.dismiss();
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

}
