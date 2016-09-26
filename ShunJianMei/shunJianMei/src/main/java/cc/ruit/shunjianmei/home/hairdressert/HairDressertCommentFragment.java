package cc.ruit.shunjianmei.home.hairdressert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.EvaluationListApi;
import cc.ruit.shunjianmei.net.api.HairStyleApi;
import cc.ruit.shunjianmei.net.request.EvaluationListRequest;
import cc.ruit.shunjianmei.net.request.HairStyleRequest;
import cc.ruit.shunjianmei.net.response.EvaluationListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.ForLargeImageActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmei.util.view.BusinessAreaBean;
import cc.ruit.shunjianmei.util.view.BusinessAreaPopupwindow;
import cc.ruit.shunjianmei.util.view.BusinessAreaPopupwindow.OnItemSelecedListener;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.oruit.widget.title.TitleUtil;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;

/**
 * @ClassName: HairDressertCommentFragment
 * @Description: 发型师评论
 * @author: HBM
 * @date: 2015年10月24日 上午10:57:16
 */
public class HairDressertCommentFragment extends BaseFragment implements
        OnClickListener, OnItemSelecedListener {
    private final String TAG = HairDressertCommentFragment.class.getSimpleName()+"测试";
    @ViewInject(R.id.rl_Style_select)
    RelativeLayout rl_Style_select;// 选择发型
    @ViewInject(R.id.tv_StyleID)
    TextView tv_StyleID;// 发型
    @ViewInject(R.id.iv_arrow_down)
    ImageView arrow_down;// 发型
    @ViewInject(R.id.listview_hairdressertcomment)
    PullToRefreshListView hairdressertcomment;// 发型评论
    @ViewInject(R.id.fl_hairstyle)
    FrameLayout fl_hairstyle;// 发型
    BusinessAreaPopupwindow popupWindow;// 下拉菜单
    List<EvaluationListResponse> list;// 评论列表
    CommentAdapter Adapter;
    private EmptyView ev;// 空载页
    int ID = 0;// 首次传入0
    private List<BusinessAreaBean> HairStyle = new ArrayList<BusinessAreaBean>();// 发型分类
    private Map<String, String> filterTexts = new HashMap<String, String>();// 储存筛选项文本
    private Map<String, String> filterIds = new HashMap<String, String>();// 储存筛选项Id
    private static final int HairStyle_TAG = 0;// 时间选项标识

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.hair_dressert_comment_fragment, null);
        ViewUtils.inject(this, view);
        initTitle();
        getHairStyle();
        initData();
        rl_Style_select.setOnClickListener(this);
        fl_hairstyle.setOnClickListener(this);
        initPullToRefreshListView();
        return view;
    }

    /**
     * @Title: initTitle
     * @Description: 初始化标题
     * @author: 欧阳
     * @return: void
     */
    private void initTitle() {
        TitleUtil title = new TitleUtil(view);
        title.tv_title.setText("评论");
        title.iv_left.setImageResource(R.drawable.arrow_back_white);
        title.iv_left.setVisibility(View.VISIBLE);
        title.iv_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isBack = FragmentManagerUtils.back(getActivity(),
                        R.id.content_frame);
                if (!isBack) {
                    getActivity().finish();
                }
            }
        });

    }

    private void initData() {
        filterTexts.put("发型", "全部");
        filterIds.put("发型", "0");
    }

    /**
     * @Title: getHairStyle
     * @Description: 得到发型
     * @author: 欧阳
     * @return: void
     */
    private void getHairStyle() {
        if (!NetWorkUtils.isConnectInternet(activity)) {
            ToastUtils.showShort("网络未链接，请检查网络设置");
            return;
        }
        HairStyleRequest request = new HairStyleRequest(UserManager.getUserID()
                + "");
        HairStyleApi.hairStyle(request, new RequestCallBack<String>() {

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
                    List<BusinessAreaBean> templist = BusinessAreaBean
                            .getclazz2(response.getData());
                    if (templist.size() > 0 && templist != null) {
                        HairStyle.clear();
                        HairStyle.addAll(templist);
                        HairStyle.add(0, new BusinessAreaBean("0", "全部"));
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort("请求数据失败");
            }
        });

    }

    String Hairdresser;// 发型师

    /**
     * @Title: initPullToRefreshListView
     * @Description: 初始化下拉刷新
     * @author: 欧阳
     * @return: void
     */
    private void initPullToRefreshListView() {
        if (this.getArguments() != null) {
            Hairdresser = this.getArguments().getString("ID").toString();
            list = new ArrayList<EvaluationListResponse>();
            Adapter = new CommentAdapter(activity, list);

            hairdressertcomment.setAdapter(Adapter);
            hairdressertcomment.getRefreshableView().setDividerHeight(
                    BaseActivity.dip2px(activity, 5));
            ev = new EmptyView(activity, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    LoadingDailog.show(activity, "数据加载中，请稍后");
                    ID = 0;
                    getData(Hairdresser, filterIds.get("发型"), ID + "");
                }
            });
            hairdressertcomment.setEmptyView(ev.getView());
            ev.setState(State.Loading);
            getData(Hairdresser, filterIds.get("发型"), ID + "");
            hairdressertcomment.setMode(Mode.BOTH);
            hairdressertcomment
                    .setOnRefreshListener(new OnRefreshListener2<ListView>() {

                        @Override
                        public void onPullDownToRefresh(
                                PullToRefreshBase<ListView> refreshView) {
                            ID = 0;
                            getData(Hairdresser, filterIds.get("发型"), ID + "");
                        }

                        @Override
                        public void onPullUpToRefresh(
                                PullToRefreshBase<ListView> refreshView) {
                            ID = Integer.parseInt(list.get(list.size() - 1).ID);
                            getData(Hairdresser, filterIds.get("发型"), ID + "");
                        }
                    });
        } else {
            return;
        }
    }

    /**
     * @param hairdresser
     * @param hairstyleID
     * @param commentID
     * @Title: getData
     * @Description: 得到数据
     * @author: 欧阳
     * @return: void
     */
    protected void getData(String hairdresser, String hairstyleID,
                           String commentID) {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(
                    R.string.no_networks_found));
            LoadingDailog.dismiss();
            hairdressertcomment.onRefreshComplete();
            ev.setErrState();
            new LoadingViewUtil(view).hint();
            return;
        }
        EvaluationListRequest request = new EvaluationListRequest(
                UserManager.getUserID() + "", hairdresser, hairstyleID,
                commentID);
        EvaluationListApi.EvaluationList(request,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        new LoadingViewUtil(view).hint();
                        LoadingDailog.dismiss();

                        BaseResponse result = BaseResponse
                                .getBaseResponse(responseInfo.result);
                        if (result == null) {
                            return;
                        }
                        hairdressertcomment.onRefreshComplete();
                        String[] split = result.getMsg().split("\\|");
                        if ("1".equals(split[0])) {
                            ToastUtils.showShort(split[1] + "");
                        }
                        List<EvaluationListResponse> templist = EvaluationListResponse
                                .getclazz2(result.getData());
                        if (result.getCode() == 1000) {
                            resultHanlder(templist, ID);
                        } else if (result.getCode() == 2100) {
                            if (ID == 0) {
                                ev.setNullState();
                            } else {
                                ToastUtils.showShort("没有更多评论");
                            }
                        } else {
                            ev.setNullState();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        new LoadingViewUtil(view).hint();
                        LoadingDailog.dismiss();
                        ev.setErrState();
                        hairdressertcomment.onRefreshComplete();
                        ToastUtils.showShort(activity.getResources().getString(
                                R.string.request_failure));
                    }
                });

    }

    /**
     * @param templist
     * @Title: resultHanlder
     * @Description: 绑定数据
     * @author: 欧阳
     * @return: void
     */
    protected void resultHanlder(List<EvaluationListResponse> templist, int id) {
        if (id == 0) {
            list.clear();
            list.addAll(templist);

            Collections.sort(list, new Comparator<EvaluationListResponse>() {
                @Override
                public int compare(EvaluationListResponse evaluationListResponse, EvaluationListResponse t1) {

                    return t1.Time.compareTo(evaluationListResponse.Time);
                }
            });

            if (list == null || list.size() <= 0) {
                ev.setNullState();
            } else {
                ev.setVisible(false);
            }
        } else {
            if (templist == null || templist.size() <= 0) {
                ToastUtils.showShort("没有更多评论");
            } else {
                list.addAll(templist);
            }
        }

        // if (templist == null) {
        // return;
        // }
        //
        // if (id == 0) {
        // list.clear();
        // }
        // if (id != 0 && templist.size() <= 0) {
        // return;
        // }
        // list.addAll(templist);
        // if (list.size() > 0) {
        // ev.setVisible(false);
        // } else {
        // ev.setNullState();
        // }
        Adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (popupWindow == null) {
            popupWindow = new BusinessAreaPopupwindow(activity, fl_hairstyle);
            popupWindow.setOnItemClickListener(this);
            View view2 = popupWindow.getView();
            view2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    arrow_down.setImageResource(R.drawable.arrow_down_gray);
                }
            });
        }
        switch (v.getId()) {// 打开图片
            case R.id.rl_Style_select:
                if (popupWindow.isCurrentShowing(HairStyle_TAG)) {
                    arrow_down.setImageResource(R.drawable.arrow_down_gray);
                    popupWindow.dismiss();
                } else {
                    popupWindow.show(HairStyle, HairStyle_TAG);
                    arrow_down.setImageResource(R.drawable.arrow_up_gray);
                }
                break;
            // case R.id.fl_hairstyle:
            // if (popupWindow.isFlag()) {
            // arrow_down.setImageResource(R.drawable.arrow_down_gray);
            // }else {
            // arrow_down.setImageResource(R.drawable.arrow_up_gray);
            // }
            // break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id, int tag) {
        switch (tag) {
            case HairStyle_TAG:
                String StyleID = HairStyle.get(position).getID();
                String Hair = HairStyle.get(position).getName();
                filterTexts.put("发型", Hair);
                filterIds.put("发型", StyleID);
                popupWindow.setSelectedPosition(position);
                arrow_down.setImageResource(R.drawable.arrow_down_gray);
                setTabText(tag);
                ev.setState(State.Loading);
                ID = 0;
                getData(Hairdresser, StyleID, "" + ID);
                break;

            default:
                break;
        }
    }

    /**
     * @param tag
     * @Title: setTabText
     * @Description: 设置文本属性
     * @author: 欧阳
     * @return: void
     */
    private void setTabText(int tag) {
        if (filterTexts.get("发型") != null && tag == 0) {
            tv_StyleID.setText(filterTexts.get("发型"));
            tv_StyleID.setTextColor(activity.getResources().getColor(
                    R.color.red_e5));
        } else {
            tv_StyleID.setTextColor(activity.getResources().getColor(
                    R.color.gray_ab));
        }
    }

    /**
     * @ClassName: CommentAdapter
     * @Description: 评论适配器
     * @author: 欧阳
     * @date: 2015年10月24日 上午11:48:44
     */
    class CommentAdapter extends BaseAdapter {

        private Context context;
        private List<EvaluationListResponse> list;

        public CommentAdapter(Context context, List<EvaluationListResponse> list) {
            this.context = context;
            this.list = list;


        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public EvaluationListResponse getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_order_comment_layout, null);
                vh.findView(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            // 得到数据和绑定数据
            setLable(position, vh);
            return convertView;
        }

        /**
         * @param position
         * @param vh
         * @Title: setLable
         * @Description: 得到数据和绑定数据
         * @author: 欧阳
         * @return: void
         */
        public void setLable(int position, ViewHolder vh) {
            Log.i("main", TAG + this.list.get(position).toString());
            final EvaluationListResponse comment = list.get(position);
            vh.commentName.setText(comment.Name);
            RoundImageLoaderUtil.setErrImage(R.drawable.tx_man,
                    R.drawable.tx_man, R.drawable.tx_man);
            RoundImageLoaderUtil.getInstance(activity, 360).loadImage(
                    comment.Photo, vh.commentPhoto);
            vh.commmentTime.setText(comment.Time.substring(0, comment.Time.length() - 3));
            if (!TextUtils.isEmpty(comment.Score)) {
                Float Score = Float.valueOf(comment.Score);
                vh.commentScore.setRating(Score);
            } else {
                vh.commentScore.setRating(0);
            }
            vh.tv_commentcontent.setText(comment.Content);
            addImages(comment.Images, vh.ll_image);
        }

        /**
         * @Title: addImages
         * @Description:
         * @author: Johnny
         * @return: void
         */
        private void addImages(
                final List<EvaluationListResponse.Images> images,
                LinearLayout ll) {
            ll.removeAllViews();
            ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
            for (int i = 0; i < images.size(); i++) {
                ImageView iv = new ImageView(activity);
                ImageLoaderUtils.getInstance(activity).loadImage(
                        images.get(i).Image, iv);
                int padding = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources()
                                .getDisplayMetrics());
                iv.setPadding(padding, padding, padding, padding);
                int width = ScreenUtils.getScreenWidth(activity) / 3;
                ll.addView(iv, new LayoutParams(width, width));
                final int index = i;
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> bigimages = new ArrayList<String>();
                        for (int j = 0; j < images.size(); j++) {
                            bigimages.add(images.get(j).Image);
                        }
                        startActivity(ForLargeImageActivity.getIntent(activity,
                                index, bigimages));
                    }
                });
            }
        }
    }

    static class ViewHolder {
        @ViewInject(R.id.tv_commentName)
        TextView commentName;// 评论姓名
        @ViewInject(R.id.tv_commmentTime)
        TextView commmentTime;// 评论时间
        @ViewInject(R.id.tv_commentcontent)
        TextView tv_commentcontent;// 评论内容
        // @ViewInject(R.id.image_comment_style1)
        // ImageView style1;// 发型1
        // @ViewInject(R.id.image_comment_style2)
        // ImageView style2;// 发型2
        // @ViewInject(R.id.image_comment_style3)
        // ImageView style3;// 发型3
        @ViewInject(R.id.image_commentPhoto)
        ImageView commentPhoto;// 评论时间
        @ViewInject(R.id.ratingbar_commentScore)
        RatingBar commentScore;// 评论分数
        @ViewInject(R.id.ll_image)
        LinearLayout ll_image;

        void findView(View convertView) {
            ViewUtils.inject(this, convertView);
        }
    }

}
