package cc.ruit.shunjianmei.home.hairdressert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.oruit.widget.messagedialog.MessageDialog;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnLastItemVisibleListener;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;
import com.zachary.hodge.uicomp.widget.refreshable.internal.LoadingLayout;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.login.LoginActivity;
import cc.ruit.shunjianmei.login.LoginFragment;
import cc.ruit.shunjianmei.net.api.BusinessAreaApi;
import cc.ruit.shunjianmei.net.api.FavoriteHairdresserListApi;
import cc.ruit.shunjianmei.net.api.HairdresserListApi;
import cc.ruit.shunjianmei.net.request.BusinessAreaRequest;
import cc.ruit.shunjianmei.net.request.FavoriteHairdresserListRequest;
import cc.ruit.shunjianmei.net.request.HairdresserListRequest;
import cc.ruit.shunjianmei.net.response.HairdresserListResponse;
import cc.ruit.shunjianmei.reserve.ReserveActivity;
import cc.ruit.shunjianmei.reserve.ReserveFragment;
import cc.ruit.shunjianmei.reserve.ReserveFreeFragment;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.view.BusinessAreaBean;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
import cc.ruit.shunjianmei.util.view.HairDressertPopupwindow;
import cc.ruit.shunjianmei.util.view.HairDressertPopupwindow.OnItemSelecedListener;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

/**
 * @ClassName: HairDressertFragment
 * @Description: 发型师列表
 * @author: HBM
 * @date: 2015年10月20日 下午8:15:17
 */
public class HairDressertFragment extends BaseFragment implements OnItemSelecedListener {
    private final String TAG = HairDressertFragment.class.getSimpleName();
    @ViewInject(R.id.pulltorefreshlistview_hairdressert)
    private PullToRefreshListView lv_refresh;// 列表
    @ViewInject(R.id.ll_filter)
    private LinearLayout ll_filter;
    @ViewInject(R.id.fl_container)
    private FrameLayout fl;
    @ViewInject(R.id.tv_hair_time)
    private TextView tv_time;// 时间
    @ViewInject(R.id.tv_shop_circle)
    private TextView tv_shop;// 商圈
    @ViewInject(R.id.tv_intelligent_sorting)
    private TextView tv_sorting;// 智能排序
    @ViewInject(R.id.iv_hair_time)
    private ImageView iv_time;// 时间
    @ViewInject(R.id.iv_shop_circle)
    private ImageView iv_shop;// 商圈
    @ViewInject(R.id.iv_intelligent_sorting)
    private ImageView iv_sorting;// 智能排序
    private EmptyView ev;// 空载页
    HairDressertPopupwindow popupWindow;// 下拉菜单
    HairdresserListAdapter listAdapter;// 发型列表
    List<HairdresserListResponse> list;
    private List<BusinessAreaBean> timeList = new ArrayList<BusinessAreaBean>();// 时间选项
    private List<BusinessAreaBean> shopList = new ArrayList<BusinessAreaBean>();// 商圈选项
    private List<BusinessAreaBean> sortList = new ArrayList<BusinessAreaBean>();// 智能排序选项
    private Map<String, String> filterTexts = new HashMap<String, String>();// 储存筛选项文本
    private Map<String, String> filterIds = new HashMap<String, String>();// 储存筛选项Id

    private String pageSize = "10";// 加载页数目
    private int pageIndex = 1;// 加载页
    private static final int STAR_TAG = 0;// 时间选项标识
    private static final int ORDER_TAG = 1;// 商圈选项标识
    private static final int DISTANCE_TAG = 2;// 智能排序标识

    PageType type = PageType.meifashi;
    protected boolean isLast = false;
    protected boolean isLoading = false;// 接口请求中
    protected boolean isFavorite = false;// 标记是否有收藏美发师

    TitleUtil title;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.hair_dressert_fragment_layout, null);
        ViewUtils.inject(this, view);
        initPageType();
        initTitle();
        initListView();
        initData();
        initPullToRefreshListView();
        return view;
    }

    private void initPageType() {
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("title"))) {
            type = PageType.meifashi;
        } else {
            type = PageType.yuyue;
        }
    }

    /**
     * @Title: getBusinessArea
     * @Description: 得到商圈
     * @author: 欧阳
     * @return: void
     */
    private void getBusinessArea() {
        ll_filter.setVisibility(View.VISIBLE);
        if (!NetWorkUtils.isConnectInternet(activity)) {
            ToastUtils.showShort("网络未链接，请检查网络设置");
            return;
        }
        BusinessAreaRequest request = new BusinessAreaRequest(UserManager.getUserID() + "",
                UserManager.getCityID() + "");
        BusinessAreaApi.BusinessArea(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
                if (response == null) {
                    return;
                }

                String[] split = response.getMsg().split("\\|");
                if ("1".equals(split[0])) {
                    ToastUtils.showShort(split[1] + "");
                }
                if (response.getCode() == 1000) {
                    List<BusinessAreaBean> templist = BusinessAreaBean.getclazz2(response.getData());
                    if (templist.size() > 0 && templist != null) {
                        shopList.clear();
                        shopList.addAll(templist);
                        shopList.add(0, new BusinessAreaBean("0", "全部"));
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort("请求数据失败");
            }
        });

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("StoreManagement"); // 统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("StoreManagement");
    }

    /**
     * @Title: initTitle
     * @Description: 初始化标题
     * @author: 欧阳
     * @return: void
     */
    private void initTitle() {
        title = new TitleUtil(view);
        title.iv_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
                if (!isBack) {
                    getActivity().finish();
                }
            }
        });
        title.iv_left.setImageResource(R.drawable.arrow_back_white);
        title.iv_left.setVisibility(type == PageType.yuyue ? View.INVISIBLE : View.VISIBLE);
        title.iv_right.setImageResource(R.drawable.cut);
        title.iv_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent Hairdresserintent = HairDresserActivity.getIntent(activity,
                        HairDressertFragment.class.getName());
                Hairdresserintent.putExtra("title", "美发师");
                startActivity(Hairdresserintent);
            }
        });
        title.iv_right.setVisibility(View.INVISIBLE);
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("title"))) {
            title.tv_title.setText(getArguments().getString("title"));
        } else {
            title.tv_title.setText("预约");
        }
    }

    /**
     * @Title: initData
     * @Description: 初始化数据
     * @author: Johnny
     * @return: void
     */
    private void initData() {
        timeList.add(new BusinessAreaBean("0", "全部"));
        timeList.add(new BusinessAreaBean("1", getNextDay(0)));
        timeList.add(new BusinessAreaBean("2", getNextDay(1)));
        timeList.add(new BusinessAreaBean("3", getNextDay(2)));
        timeList.add(new BusinessAreaBean("4", getNextDay(3)));
        timeList.add(new BusinessAreaBean("5", getNextDay(4)));
        timeList.add(new BusinessAreaBean("6", getNextDay(5)));
        timeList.add(new BusinessAreaBean("7", getNextDay(6)));
        timeList.add(new BusinessAreaBean("8", getNextDay(7)));

        // sortList.add(new BusinessAreaBean("0", "智能排序"));
        sortList.add(new BusinessAreaBean("1", "级别"));
        sortList.add(new BusinessAreaBean("2", "接单量"));
        sortList.add(new BusinessAreaBean("3", "服务评论"));

        filterTexts.put("时间", "全部");
        filterTexts.put("商圈", "全部");
        filterTexts.put("智能排序", "智能排序");

        filterIds.put("时间", "0");
        filterIds.put("商圈", "0");
        filterIds.put("智能排序", "1");
        if (type == PageType.yuyue) {
            getData("" + pageIndex, "20");
        } else {
            getBusinessArea();
            getData(filterIds.get("时间"), filterIds.get("商圈"), filterIds.get("智能排序"), "" + pageIndex);
        }
    }

    /**
     * @Title: initPullToRefreshListView
     * @Description: 初始化
     * @author: 欧阳
     * @return: void
     */
    private void initListView() {
        list = new ArrayList<HairdresserListResponse>();
        listAdapter = new HairdresserListAdapter(activity, list);
        lv_refresh.setAdapter(listAdapter);
        lv_refresh.getRefreshableView().setDividerHeight(0);
        ev = new EmptyView(activity, new OnClickListener() {

            @Override
            public void onClick(View v) {
                LoadingDailog.show(activity, "数据加载中，请稍后");
                pageIndex = 1;
                getData(filterIds.get("时间"), filterIds.get("商圈"), filterIds.get("智能排序"), "" + pageIndex);
            }
        });
        lv_refresh.setEmptyView(ev.getView());
        ev.setState(State.Loading);

    }

    /**
     * @Title: initPullToRefreshListView
     * @Description: 初始化下拉刷新
     * @author: 欧阳
     * @return: void
     */
    private void initPullToRefreshListView() {
        lv_refresh.setMode(Mode.BOTH);
        lv_refresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (ll_filter.getVisibility() == View.VISIBLE) {
                    pageIndex = 1;
                    getData(filterIds.get("时间"), filterIds.get("商圈"), filterIds.get("智能排序"), "" + pageIndex);
                } else {
                    getData("" + pageIndex, "20");
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (ll_filter.getVisibility() == View.VISIBLE) {
                    getData(filterIds.get("时间"), filterIds.get("商圈"), filterIds.get("智能排序"), "" + pageIndex);
                } else {
                    getData("" + pageIndex, "20");
                }
            }
        });
        lv_refresh.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                // ToastUtils.showLong("last");
                if (isLast) {
                    return;
                }
                isLast = true;
                if (ll_filter.getVisibility() == View.VISIBLE) {
                    getData(filterIds.get("时间"), filterIds.get("商圈"), filterIds.get("智能排序"), "" + pageIndex);
                } else {
                    getData("" + pageIndex, "20");
                }
            }
        });
    }

    /**
     * @Title: getData
     * @Description: 获取收藏的美发师
     * @author: Johnny
     * @return: void
     */
    private void getData(final String pageIndex, String pageSize) {
        ll_filter.setVisibility(View.GONE);
        if (isLoading)
            return;
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            return;
        }

        int userId = UserManager.getUserID();

        FavoriteHairdresserListRequest request = new FavoriteHairdresserListRequest("" + userId, pageIndex, pageSize);
        Log.i("Tag", "request=" + request.toJsonString(request));
        FavoriteHairdresserListApi.favoriteHairdresserList(request, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                isLoading = true;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LoadingDailog.dismiss();
                lv_refresh.onRefreshComplete();
                isLoading = false;
                BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
                if (result == null) {
                    return;
                }
                String[] split = result.getMsg().split("\\|");

                if ("1".equals(split[0])) {
                    ToastUtils.showShort(split[1] + "");
                }

                if (result.getCode() == 1000) {
                    isFavorite = true;
                    List<HairdresserListResponse> data = HairdresserListResponse.getclazz2(result.getData());
                    if (data != null) {
                        // 设置网络请求的数据
                        resultHanlder(data, pageIndex);
                        title.iv_right.setVisibility(View.VISIBLE);
                    } else {
                        ToastUtils.showShort("请求数据异常");
                    }
                } else if (result.getCode() == 2100) {
                    isFavorite = false;

                    if ("1".equals(pageIndex)) {
                        if (type == PageType.yuyue) {
                            getBusinessArea();
                            getData(filterIds.get("时间"), filterIds.get("商圈"), filterIds.get("智能排序"), "" + pageIndex);
                        }
                    }
                }
            }


            @Override
            public void onFailure(HttpException error, String msg) {
                MobclickAgent.onEvent(activity, "login_failure");
                LoadingDailog.dismiss();
                ev.setErrState();
                isLast = false;
                isLoading = false;
                ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));

            }
        });

    }

    /**
     * @param Days
     * @param AreaID
     * @param Sort
     * @param pageIndex
     * @Title: getData
     * @Description: 网络请求
     * @author: 欧阳
     * @return: void
     */
    protected void getData(String Days, String AreaID, String Sort, final String pageIndex) {
        if (isLoading)
            return;
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            LoadingDailog.dismiss();
            lv_refresh.onRefreshComplete();
            ev.setErrState();
            new LoadingViewUtil(view).hint();
            isLast = false;
            return;
        }

        final HairdresserListRequest request = new HairdresserListRequest(UserManager.getUserID() + "", Days, AreaID,
                UserManager.getCityID() + "", Sort, pageSize, pageIndex);

        HairdresserListApi.HairdresserList(request, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                isLoading = true;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                new LoadingViewUtil(view).hint();
                LoadingDailog.dismiss();
                isLoading = false;
                BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);

                if (result == null) {
                    return;
                }
                lv_refresh.onRefreshComplete();
                String[] split = result.getMsg().split("\\|");
                if ("1".equals(split[0])) {
                    ToastUtils.showShort(split[1] + "");
                }
                if (result.getCode() == 1000) {
                    List<HairdresserListResponse> templist = HairdresserListResponse.getclazz2(result.getData());

                    resultHanlder(templist, pageIndex);
                } else if (result.getCode() == 2100) {
                    //保存值
                    setSharedPreferences();
                    if ("1".equals(pageIndex)) {
                        ev.setNullState();
                    }
                } else {
                    ev.setErrState();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                new LoadingViewUtil(view).hint();
                LoadingDailog.dismiss();
                ev.setErrState();
                isLast = false;
                isLoading = false;
                lv_refresh.onRefreshComplete();
                ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
            }
        });

    }

    boolean b = false;

    private void setSharedPreferences() {
        if (b = true) {
            return;
        }
        b = true;
        Context context = getContext();
        SharedPreferences sp = context.getSharedPreferences("a", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isEnd", true);
        editor.commit();
    }

    /**
     * @param templist
     * @param pageIndex
     * @Title: resultHanlder
     * @Description: 数据处理
     * @author: 欧阳
     * @return: void
     */
    protected void resultHanlder(List<HairdresserListResponse> templist, String pageIndex) {
        if (templist == null) {
            return;
        }

        if ("1".equals(pageIndex)) {
            list.clear();
        }
        if (!"0".equals(pageIndex) && templist.size() <= 0) {
            return;
        }
        isLast = false;
        list.addAll(templist);
        if (list.size() > 0) {
            ev.setVisible(false);
        } else {
            ev.setNullState();
        }

        this.pageIndex++;
        listAdapter.notifyDataSetChanged();

    }

    /**
     * @param v
     * @Title: onClick
     * @Description: 定义点击事件
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @OnClick({R.id.ll_hair_time, R.id.ll_shop_circle, R.id.ll_intelligent_sorting})
    public void onClick(View v) {
        // ----------初始化筛选框-------------
        if (popupWindow == null) {
            popupWindow = new HairDressertPopupwindow(activity, fl);
            popupWindow.setOnItemClickListener(this);
        }
        switch (v.getId()) {
            // 时间
            case R.id.ll_hair_time:
                // controlTab(R.id.ll_hair_time);
                if (popupWindow.isCurrentShowing(STAR_TAG)) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.setSelectedPosition(sortSelectId);
                    popupWindow.show(timeList, STAR_TAG);
                }
                break;

            // 商圈
            case R.id.ll_shop_circle:
                // controlTab(R.id.ll_shop_circle);
                if (popupWindow.isCurrentShowing(ORDER_TAG)) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.setSelectedPosition(shopSelectId);
                    popupWindow.show(shopList, ORDER_TAG);
                }

                break;
            // 智能排序
            case R.id.ll_intelligent_sorting:
                // controlTab(R.id.ll_intelligent_sorting);
                if (popupWindow.isCurrentShowing(DISTANCE_TAG)) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.setSelectedPosition(timeSelectId);
                    popupWindow.show(sortList, DISTANCE_TAG);
                }
                break;
            default:
                break;
        }
    }

    /**
     * @Title: controlTab
     * @Description: 控件按钮设置
     * @author: 欧阳
     * @return: void
     */
    public void controlTab(int checkedview, String title) {
        // iv_time.setImageResource(checkedview == R.id.ll_hair_time ?
        // R.drawable.arrow_up_gray
        // : R.drawable.arrow_down_gray);
        // iv_shop.setImageResource(checkedview == R.id.ll_shop_circle ?
        // R.drawable.arrow_up_gray
        // : R.drawable.arrow_down_gray);
        // iv_sorting
        // .setImageResource(checkedview == R.id.ll_intelligent_sorting ?
        // R.drawable.arrow_up_gray
        // : R.drawable.arrow_down_gray);
        // int red = getResources().getColor(R.color.red_e5);
        // int gray = getResources().getColor(R.color.gray_ab);
        // tv_time.setTextColor(checkedview == R.id.ll_hair_time ? red : gray);
        // tv_shop.setTextColor(checkedview == R.id.ll_shop_circle ? red :
        // gray);
        // tv_sorting.setTextColor(checkedview == R.id.ll_intelligent_sorting ?
        // red
        // : gray);

        TextView textView = null;

        switch (checkedview) {
            case R.id.ll_hair_time:
                textView = tv_time;
                break;
            case R.id.ll_shop_circle:
                textView = tv_shop;
                break;
            case R.id.ll_intelligent_sorting:
                textView = tv_sorting;
                break;
            default:
                break;
        }

        if (textView != null) {
            textView.setText(title);
        }
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

    /**
     * @ClassName: HairdresserListAdapter
     * @Description: 发型师适配器
     * @author: 欧阳
     * @date: 2015年10月20日 下午9:36:13
     */
    class HairdresserListAdapter extends BaseAdapter {

        private Context context;
        private List<HairdresserListResponse> list;

        public HairdresserListAdapter(Context context, List<HairdresserListResponse> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (list == null || list.size() <= 0) {
                ev.setNullState();
            }
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public HairdresserListResponse getItem(int position) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_hair_dressert_fragment_layout, null);
                vh.findView(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            // 得到数据和绑定数据
            setLabel(position, vh);
            return convertView;
        }

        /**
         * @param position
         * @param vh
         * @Title: setLabel
         * @Description: 绑定控件
         * @author: 欧阳
         * @return: void
         */
        private void setLabel(final int position, final ViewHolder vh) {
            HairdresserListResponse obj = getItem(position);
            Log.i("main", TAG + "美发师列表页面图片链接= " + obj.getImage());
            vh.tv_name.setText(obj.getName());
            vh.tv_LevelName.setText(obj.getLevelName());
            vh.tv_Star.setText(obj.getStar());
            // vh.tv_type.setText(obj.getIstypeString());
            if (obj.getIstypeString().equals("自由定价")) {
                vh.tv_type.setImageResource(R.drawable.master_free);
            } else {
                vh.tv_type.setImageResource(R.drawable.master_deep);
            }
            if (list.get(position).getIstypeString().toString().equals("资深合作")) {
                vh.tv_Star.setVisibility(View.VISIBLE);
                vh.tv_price.setVisibility(View.VISIBLE);
            } else {
                vh.tv_price.setVisibility(View.GONE);
            }

            String price = TextUtils.isEmpty(obj.getPrice()) ? "0.00" : obj.getPrice();

            // 如果是收藏预约结口返回的数据
            if (type == PageType.yuyue && isFavorite == true) {
                String tip = "";
                vh.tv_price.setText(Html.fromHtml(tip));
            } else {
                String tip = "<font color='#ababab'>设计剪发 </font><font color='#e53123'>" + "¥" + price + "</font>";
                vh.tv_price.setText(Html.fromHtml(tip));
            }

            vh.tv_ordernum.setText("接单" + obj.getOrderNum() + "次");
            float start = Float.parseFloat(obj.getScore());
            vh.tv_evaluate.setRating(start);
            ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
            ImageLoaderUtils.getInstance(activity).loadImage(obj.getImage(), vh.iv_Image);
            // add by jiazhaohui
            String flag = obj.getFlagpng();
            if (flag == null || flag == "") {
                vh.iv_flag_Image.setVisibility(View.GONE);
            } else {
                vh.iv_flag_Image.setVisibility(View.VISIBLE);
                String flagstr = Constant.IMAGESURL + flag;
                ImageLoaderUtils.getInstance(context).loadImage(flagstr, vh.iv_flag_Image);
            }

            RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
            RoundImageLoaderUtil.getInstance(activity, 360).loadImage(obj.getPhoto(), vh.iv_photo);
            vh.iv_Image.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = ReserveActivity.getIntent(context, HairDressertDetailsFragment.class.getName());
                    intent.putExtra("ID", list.get(position).getID());
                    intent.putExtra("Name", list.get(position).getName());
                    intent.putExtra("Type", list.get(position).getIstypeString());
                    startActivity(intent);
                }
            });
            vh.ll_detail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = ReserveActivity.getIntent(context, HairDressertDetailsFragment.class.getName());
                    intent.putExtra("ID", list.get(position).getID());
                    intent.putExtra("Name", list.get(position).getName());
                    intent.putExtra("Type", list.get(position).getIstypeString());
                    startActivity(intent);
                }
            });
            vh.ll_time.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (UserManager.getUserID() <= 0) {
                        showUnLoginDialog();
                        return;
                    }

                    if (list.get(position).getIstypeString().toString().equals("资深合作")) {
                        Intent intent = ReserveActivity.getIntent(context, ReserveFragment.class.getName());
                        intent.putExtra("ID", list.get(position).getID());

                        intent.putExtra("From", HairDressertFragment.class.getSimpleName());
                        startActivity(intent);
                    } else {
                        if (list.get(position).getHasPacked().equals("0")) {
                            ToastUtils.showShort("无此套餐");
                        } else {
                            Intent intent = ReserveActivity.getIntent(context, ReserveFreeFragment.class.getName());
                            intent.putExtra("ID", list.get(position).getID());
                            intent.putExtra("From", HairDressertFragment.class.getSimpleName());
                            startActivity(intent);
                        }

                    }

                }
            });
        }
    }

    static class ViewHolder {
        @ViewInject(R.id.tv_hairstylist_master_name)
        TextView tv_name;// 发型师名称
        @ViewInject(R.id.tv_hairstylist_master_LevelName)
        TextView tv_LevelName;// 级别名称
        @ViewInject(R.id.tv_hairstylist_master_Star)
        TextView tv_Star;// 星座
        @ViewInject(R.id.tv_order_receiving_num)
        TextView tv_ordernum;// 接单量
        @ViewInject(R.id.ratingbar_all_evaluate)
        RatingBar tv_evaluate;// 评论
        @ViewInject(R.id.iv_hairstylist_Image)
        ImageView iv_Image;// 发型图片
        // add by jiazhaohui
        @ViewInject(R.id.iv_flag_Image)
        ImageView iv_flag_Image;// 发型图
        @ViewInject(R.id.image_hairstylist_photo)
        ImageView iv_photo;// 头像
        @ViewInject(R.id.ll_hairdressert_detail)
        LinearLayout ll_detail;// 详情
        @ViewInject(R.id.ll_hairdressert_time)
        LinearLayout ll_time;// 预约
        @ViewInject(R.id.iv_hairdressert_detail)
        ImageView iv_detail;
        @ViewInject(R.id.iv_hairdressert_time)
        ImageView iv_time;
        @ViewInject(R.id.tv_hairdressert_detail)
        TextView tv_detail;
        @ViewInject(R.id.tv_hairdressert_time)
        TextView tv_time;
        @ViewInject(R.id.tv_order_price)
        TextView tv_price;
        @ViewInject(R.id.image_find_type)
        ImageView tv_type;

        @ViewInject(R.id.lin_gone_vis)
        LinearLayout lin;

        void findView(View convertView) {
            ViewUtils.inject(this, convertView);
        }
    }

    /**
     * @param delay 为前移或后延的天数，负值为前移
     * @Title: getNextDay
     * @Description: 得到一个时间延后或前移几天的时间, nowdate为时间, delay为前移或后延的天数
     * @author: leezw
     * @return: 例如：2015-05-17
     */
    public static String getNextDay(int delay) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("M.d E", Locale.getDefault());
            String mdate = "";
            Date d = new Date();
            long myTime = (d.getTime() / 1000) + (delay) * 24 * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }

    String sortID = "0";
    String shopid = "0";
    String timeID = "0";

    int sortSelectId = 0;
    int shopSelectId = 0;
    int timeSelectId = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, int tag) {
        switch (tag) {
            // 时间
            case 0:
                timeID = timeList.get(position).getID();
                String time = timeList.get(position).getName();
                filterTexts.put("时间", time);
                filterIds.put("时间", timeID);
                sortSelectId = position;
                // setTabText(tag);
                // lv_refresh.setEmptyView(ev.getView());
                ev.setState(State.Loading);
                pageIndex = 1;
                controlTab(R.id.ll_hair_time, time);
                getData(timeID, filterIds.get("商圈"), filterIds.get("智能排序"), "" + pageIndex);
                break;
            // 商圈选项
            case 1:
                shopid = shopList.get(position).getID();
                String shop = shopList.get(position).getName();
                filterTexts.put("商圈", shop);
                filterIds.put("商圈", shopid);
                shopSelectId = position;
                // setTabText(tag);
                pageIndex = 1;
                // lv_refresh.setEmptyView(ev.getView());
                ev.setState(State.Loading);
                controlTab(R.id.ll_shop_circle, shop);
                getData(filterIds.get("时间"), shopid, filterIds.get("智能排序"), "" + pageIndex);
                break;
            // 智能排序
            case 2:
                sortID = sortList.get(position).getID();
                String sort = sortList.get(position).getName();
                filterTexts.put("智能排序", sort);
                filterIds.put("智能排序", sortID);
                timeSelectId = position;
                pageIndex = 1;
                // setTabText(tag);
                // lv_refresh.setEmptyView(ev.getView());
                ev.setState(State.Loading);
                controlTab(R.id.ll_intelligent_sorting, sort);
                getData(filterIds.get("时间"), filterIds.get("商圈"), sortID, "" + pageIndex);
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
        if (filterTexts.get("时间") != null && tag == 0) {
            tv_time.setText(filterTexts.get("时间"));
            tv_time.setTextColor(activity.getResources().getColor(R.color.red_e5));
            // iv_time.setImageResource(R.drawable.arrow_up_gray);
        } else {
            tv_time.setTextColor(activity.getResources().getColor(R.color.gray_ab));
        }
        iv_time.setImageResource(R.drawable.arrow_down_gray);
        if (filterTexts.get("商圈") != null && tag == 1) {
            tv_shop.setText(filterTexts.get("商圈"));
            tv_shop.setTextColor(activity.getResources().getColor(R.color.red_e5));
            // iv_shop.setImageResource(R.drawable.arrow_up_gray);
        } else {
            tv_shop.setTextColor(activity.getResources().getColor(R.color.gray_ab));
        }
        iv_shop.setImageResource(R.drawable.arrow_down_gray);

        if (filterTexts.get("智能排序") != null && tag == 2) {
            tv_sorting.setText(filterTexts.get("智能排序"));
            tv_sorting.setTextColor(activity.getResources().getColor(R.color.red_e5));
            // iv_sorting.setImageResource(R.drawable.arrow_up_gray);
        } else {
            tv_sorting.setTextColor(activity.getResources().getColor(R.color.gray_ab));
        }
        iv_sorting.setImageResource(R.drawable.arrow_down_gray);

    }

    public enum PageType {
        meifashi, yuyue
    }
}
