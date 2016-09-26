package cc.ruit.shunjianmei.home.order;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.schedule.MyScheduleActivity;
import cc.ruit.shunjianmei.net.api.H_OrderDetailApi;
import cc.ruit.shunjianmei.net.api.H_UpdateOrderStateApi;
import cc.ruit.shunjianmei.net.request.H_OrderDetailRequest;
import cc.ruit.shunjianmei.net.request.H_UpdateOrderStateRequest;
import cc.ruit.shunjianmei.net.response.H_OrderDetailResponse;
import cc.ruit.shunjianmei.net.response.H_OrderDetailResponse.Comment;
import cc.ruit.shunjianmei.net.response.OrderListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.ForLargeImageActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.view.MyListView;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.Util;
import cc.ruit.utils.sdk.http.NetWorkUtils;

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

import de.greenrobot.event.EventBus;

/**
 * @ClassName: OrderDetailFragment
 * @Description: 订单详情
 * @author: 欧阳
 * @date: 2015年10月19日 上午9:27:17
 */
public class OrderDetailFragment extends BaseFragment implements View.OnTouchListener {
    @ViewInject(R.id.tv_orderdetail_state)
    TextView tv_state;// 状态
    @ViewInject(R.id.tv_orderdetail_ordercode)
    TextView tv_code;// 订单号
    @ViewInject(R.id.tv_orderdetail_nextstep)
    TextView tv_nextstep;// 下一步
    @ViewInject(R.id.tv_orderdetail_cancel)
    Button tv_cancel;// 签到
    @ViewInject(R.id.tv_orderdetails_ordertime)
    TextView tv_ordertime;// 预约时间
    @ViewInject(R.id.image_hairstylist_photo)
    ImageView hairstylist_photo;// 发型师头像
    @ViewInject(R.id.tv_hairstylist_master_name)
    TextView hairstylist_Name;// 发型师名称
    @ViewInject(R.id.image_hairstylist_master_Mobile)
    ImageView hairstylist_Mobile;// 发型师电话
    @ViewInject(R.id.ratingbar_all_evaluate)
    RatingBar hairstylist_evaluate;// 发型师评论
    @ViewInject(R.id.tv_order_quantity)
    TextView order_quantity;// 接单量
    @ViewInject(R.id.image_orderdetail_StorePhoto)
    ImageView StorePhoto;// 店铺图片
    @ViewInject(R.id.tv_orderdetail_StoreName)
    TextView StoreName;// 店铺名称
    @ViewInject(R.id.tv_orderdetails_StoreAddress)
    TextView StoreAddress;// 店铺地址
    @ViewInject(R.id.ratingbar_StoreScore)
    RatingBar StoreScore;// 店铺评分
    @ViewInject(R.id.image_orderdetails_StoreTel)
    ImageView imageStoreTel;// 店铺电话
    @ViewInject(R.id.tv_orderdetails_OrderNum)
    TextView OrderNum;// 店铺接单量
    @ViewInject(R.id.tv_orderdetails_CarNum)
    TextView CarNum;// 停车位
    @ViewInject(R.id.tv_orderdetail_StyleName)
    TextView StyleName;// 发型名称
    @ViewInject(R.id.tv_orderdetail_Styleprice)
    TextView Styleprice;// 发型价格
    @ViewInject(R.id.tv_orderdetail_Item)
    TextView Item;// 发型项目
    @ViewInject(R.id.tv_orderdetail_AdditionalContent)
    TextView AddContent;// 加单内容
    @ViewInject(R.id.tv_orderdetail_AdditionalAmount)
    TextView AddAmount;// 加单金额
    @ViewInject(R.id.tv_base_way_price)
    TextView base_price;// 基本支付的价格
    @ViewInject(R.id.tv_addorder_way_price)
    TextView addorder_price;// 加单价格
    @ViewInject(R.id.tv_have_discount_price)
    TextView discount_price;// 已优惠价格
    @ViewInject(R.id.pulltorefreshlistview_CommentListview)
    MyListView CommentListview;// 评论列表
    @ViewInject(R.id.tv_orderdetails_Comment)
    TextView tv_Comment;// 用户评论
    @ViewInject(R.id.tv_line_Comment)
    TextView line_Comment;// 用户评论下划线
    @ViewInject(R.id.tv_orderdetails_StoreComment)
    TextView tv_StoreComment;// 店铺评论
    @ViewInject(R.id.tv_line_StoreComment)
    TextView line_StoreComment;// 店铺评论下划线
    @ViewInject(R.id.btn_expence_dealwith)
    Button dealwith;// 处理异常
    @ViewInject(R.id.btn_addorder)
    Button addorder;// 加单
    @ViewInject(R.id.rl_btn_order_dealwith)
    RelativeLayout order_dealwith;// 订单处理
    CommentAdapter adapter;// 评论适配器
    List<Comment> list;// 评论接口
    @ViewInject(R.id.rl_order_detail_comment)
    RelativeLayout tv_emptyComment;
    // @ViewInject(R.id.tv_empty_ordercomment)
    // TextView tv_emptyComment;
    String Mobile;// 发型师电话
    String StoreTel;// 店铺电话
    AlertDialog dialog;
    String state;// 状态处理
    String ContentState;// 状态内容显示
    String orderId;// 订单id
    int AdditionalState = -1;// 订单状态
    Float masterscore;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.order_detail_layout, null);
        ViewUtils.inject(this, view);
        initTitle(view);
        LoadingDailog.show(activity, "正在加载....");
        initMyListView();
        initData();
        initEventBus();
        initOnTouch();
        return view;
    }

    private void initOnTouch() {
        hairstylist_Name.setOnTouchListener(this);
        StoreName.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (view.getId()) {
            case R.id.tv_hairstylist_master_name:
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //多行显示
                        hairstylist_Name.setSingleLine(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        //单显示
                        hairstylist_Name.setSingleLine(true);
                        break;

                }
                break;

            case R.id.tv_orderdetail_StoreName:
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //多行显示
                        StoreName.setSingleLine(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        //单显示
                        StoreName.setSingleLine(true);
                        break;

                }
                break;


        }
        return true;
    }

    /**
     * @param view
     * @Title: initTitle
     * @Description: 初始化标题
     * @author: 欧阳
     * @return: void
     */
    private void initTitle(View view) {
        TitleUtil titleUtil = new TitleUtil(view);
        // 设置标题栏中间的文字
        titleUtil.tv_title.setText("订单详情");
        // 设置标题栏左边的图片
        titleUtil.iv_left.setVisibility(View.VISIBLE);
        titleUtil.iv_left.setImageResource(R.drawable.back);
        // 设置左边的标题
        titleUtil.iv_right.setVisibility(View.VISIBLE);
        titleUtil.iv_right.setImageResource(R.drawable.store_service_icon);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11,
                getResources().getDisplayMetrics());
        titleUtil.iv_right.setPadding(padding, padding, padding, padding);
        titleUtil.iv_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Uri uri = Uri.parse("tel:" + Constant.serverPhone);
                // Intent intent = new Intent();
                // intent.setAction(Intent.ACTION_CALL);
                // intent.setData(uri);
                // startActivity(intent);
                Util.openTelDall(activity, Constant.serverPhone);
                // UserManager.showCommentDailog(activity,
                // Constant.serverPhone);
            }
        });
        // 设置标题栏背景为白色
        titleUtil.rl_container.setBackgroundColor(this.getResources().getColor(R.color.red));
        titleUtil.iv_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!FragmentManagerUtils.back(activity, R.id.content_frame)) {
                    activity.finish();
                }
            }
        });
    }

    /**
     * @Title: initPullToRefreshListView
     * @Description: 初始化刷新
     * @author: 欧阳
     * @return: void
     */
    private void initMyListView() {
        CommentListview.setFocusable(false);
        CommentListview.setFocusableInTouchMode(false);
        list = new ArrayList<Comment>();
        adapter = new CommentAdapter(activity, list);
        CommentListview.setAdapter(adapter);
    }

    /**
     * @Title: initData
     * @Description: 请求数据
     * @author: 欧阳
     * @return: void
     */
    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            OrderListResponse bean = (OrderListResponse) bundle.getSerializable("bean");
            if (bean != null) {
                orderId = bean.getOrderID();
            } else if (MyScheduleActivity.ORDER_ID != null) {
                orderId = MyScheduleActivity.ORDER_ID;
            }
        }
        getData();
    }

    /**
     * @Title: initEventBus
     * @Description: 注册EventBus
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
        if (!TextUtils.isEmpty(bus.getmMsg()) && bus.getmMsg().equals("已经加单了")) {
            initData();
        }
    }

    Comment storeComment;// 店铺评论
    Comment comment;

    /**
     * @Title: getData
     * @Description: 网络请求
     * @author: 欧阳
     * @return: void
     */
    protected void getData() {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            LoadingDailog.dismiss();
            new LoadingViewUtil(view).hint();
            return;
        }
        H_OrderDetailRequest request = new H_OrderDetailRequest(UserManager.getUserID() + "", orderId);
        H_OrderDetailApi.H_OrderDetail(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                new LoadingViewUtil(view).hint();
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
                    List<H_OrderDetailResponse> templist = H_OrderDetailResponse.getclazz2(result.getData());
                    bundweidgt(templist);
                    comment = templist.get(0).Comment;
                    storeComment = templist.get(0).StoreComment;
                    if (comment.ID != null) {
                        list.clear();
                        list.add(comment);
                        tv_emptyComment.setVisibility(View.VISIBLE);

                    } else {
                        tv_emptyComment.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                    LoadingDailog.dismiss();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort("网络请求失败");
            }
        });

    }

    /**
     * @param templist
     * @Title: bundweidgt
     * @Description: 绑定数据
     * @author: 欧阳
     * @return: void
     */
    protected void bundweidgt(List<H_OrderDetailResponse> templist) {
        tv_code.setText(templist.get(0).OrderCode);
        tv_nextstep.setText(templist.get(0).NextStep);
        tv_ordertime.setText(templist.get(0).AppointmentTime);
        RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
        RoundImageLoaderUtil.getInstance(activity, 360).loadImage(templist.get(0).Photo, hairstylist_photo);
        hairstylist_Name.setText(templist.get(0).Name);
        Mobile = templist.get(0).Mobile.toString();
        if (TextUtils.isEmpty(templist.get(0).Comment.Score)) {
            masterscore = Float.valueOf("0.0");
        } else {
            masterscore = Float.valueOf(templist.get(0).Comment.Score);
        }
        hairstylist_evaluate.setRating(masterscore);
        order_quantity.setText("接单次" + templist.get(0).OrderNum + "次");
        ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
        ImageLoaderUtils.getInstance(activity).loadImage(templist.get(0).StorePhoto, StorePhoto);
        StoreName.setText(templist.get(0).StoreName);
        StoreAddress.setText(templist.get(0).Address);
        if (!TextUtils.isEmpty(templist.get(0).StoreScore)) {
            Float storescore = Float.valueOf(templist.get(0).StoreScore);
            StoreScore.setRating(storescore);
        } else {
            StoreScore.setRating(0);
        }
        StoreTel = templist.get(0).StoreTel.toString();
        OrderNum.setText("接单" + templist.get(0).StoreOrderNum + "次");
        CarNum.setText("停车位" + templist.get(0).CarNum + "个");
        StyleName.setText(templist.get(0).StyleName);
        Styleprice.setText("¥" + templist.get(0).Amount);
        base_price.setText("¥" + templist.get(0).Amount);
        try {
            if (!TextUtils.isEmpty(templist.get(0).AdditionalState) && templist.get(0).AdditionalState != null) {
                AdditionalState = Integer.parseInt(templist.get(0).AdditionalState.toString());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < templist.get(0).Item.size(); i++) {
            sb.append(templist.get(0).Item.get(i).Name + "");
        }
        Item.setText(sb.toString());
        if (TextUtils.isEmpty(templist.get(0).AdditionalContent)) {
            AddContent.setText("无");
            AddAmount.setText("¥0");
            addorder_price.setText("¥0");
        } else {
            AddContent.setText(templist.get(0).AdditionalContent);
            AddAmount.setText("¥" + templist.get(0).AdditionalAmount);
            addorder_price.setText("¥" + templist.get(0).AdditionalAmount);
        }
        discount_price.setText("¥" + templist.get(0).Discount);
        String StateName = templist.get(0).StateName.toString();
        state = templist.get(0).State;
        switch (Integer.parseInt(templist.get(0).State)) {// 状态1待支付2待确认3已预约4已签到5服务中6服务完成7评论完成8订单完成9已取消10异常处理
            case 1:
                tv_state.setText(StateName);
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.GONE);
                order_dealwith.setVisibility(View.GONE);
                break;
            case 2:
                tv_state.setText(StateName);
                tv_cancel.setText("确认预约");
                tv_cancel.setVisibility(View.VISIBLE);
                dealwith.setVisibility(View.VISIBLE);
                ContentState = "是否确认预约？";
                break;
            case 3:
                tv_state.setText(StateName);
                tv_cancel.setText("签到");
                tv_cancel.setVisibility(View.VISIBLE);
                dealwith.setVisibility(View.VISIBLE);
                ContentState = "是否进行签到？";
                break;
            case 4:
                tv_state.setText(StateName);
                tv_cancel.setText("开始服务");
                tv_cancel.setVisibility(View.VISIBLE);
                dealwith.setVisibility(View.VISIBLE);
                ContentState = "是否开始服务项目？";
                break;
            case 5:
                tv_state.setText(StateName);
                dealwith.setVisibility(View.INVISIBLE);
                ContentState = "服务是否结束？";
                if (AdditionalState == 0) {
                    addorder.setVisibility(View.VISIBLE);
                    tv_cancel.setText("结束服务");
                    tv_cancel.setVisibility(View.VISIBLE);
                } else if (AdditionalState == 1) {
                    tv_nextstep.setText("等待用户改单支付");
                    tv_cancel.setVisibility(View.INVISIBLE);
                    addorder.setVisibility(View.INVISIBLE);
                } else if (AdditionalState == 2) {
                    addorder.setVisibility(View.INVISIBLE);
                    tv_cancel.setText("结束服务");
                    tv_cancel.setVisibility(View.VISIBLE);
                }
                break;
            case 6:
                tv_state.setText(StateName);
                dealwith.setVisibility(View.VISIBLE);
                addorder.setVisibility(View.INVISIBLE);
                tv_cancel.setVisibility(View.INVISIBLE);
                break;
            case 7:
                tv_state.setText(StateName);
                dealwith.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.INVISIBLE);
                break;
            case 8:
                tv_state.setText(StateName);
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.GONE);
                order_dealwith.setVisibility(View.GONE);
                break;
            case 9:
                tv_state.setText(StateName);
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.GONE);
                order_dealwith.setVisibility(View.GONE);
                break;
            case 10:
                tv_state.setText(StateName);
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.GONE);
                order_dealwith.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * @param v
     * @Title: onClick
     * @Description: 点击事件
     * @author: 欧阳
     * @return: void
     */
    @OnClick({R.id.tv_orderdetail_cancel, R.id.image_hairstylist_master_Mobile, R.id.image_orderdetails_StoreTel,
            R.id.tv_orderdetails_Comment, R.id.tv_orderdetails_StoreComment, R.id.btn_expence_dealwith,
            R.id.btn_addorder})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_orderdetail_cancel:// 签到
                int intstate = Integer.parseInt(state);
                if (intstate >= 2 && intstate <= 5) {
                    tv_cancel.setEnabled(false);
                    alertDialog(ContentState, (intstate + 1) + "");// 状态//
                    // 3确认预约4签到5开始服务6结束服务9已取消10异常处理

                }
                break;
            case R.id.image_hairstylist_master_Mobile:// 发型师电话
                if (TextUtils.isEmpty(Mobile)) {
                    ToastUtils.showShort("不好意思暂时没有发型师电话", Gravity.CENTER);
                    return;
                } else {
                    Util.openTelDall(activity, Mobile);
                    // Uri uri = Uri.parse("tel:" + Mobile);
                    // Intent intent = new Intent();
                    // intent.setAction(Intent.ACTION_CALL);
                    // intent.setData(uri);
                    // startActivity(intent);
                    // UserManager.showCommentDailog(activity, Mobile);
                }

                break;
            case R.id.image_orderdetails_StoreTel:// 店铺电话
                if (TextUtils.isEmpty(StoreTel)) {
                    ToastUtils.showShort("不好意思暂时没有店铺电话", Gravity.CENTER);
                    return;
                } else {
                    Util.openTelDall(activity, StoreTel);
                    // Uri Storeuri = Uri.parse("tel:" + StoreTel);
                    // Intent Storeintent = new Intent();
                    // Storeintent.setAction(Intent.ACTION_CALL);
                    // Storeintent.setData(Storeuri);
                    // startActivity(Storeintent);
                    // UserManager.showCommentDailog(activity, StoreTel);
                }

                break;
            case R.id.tv_orderdetails_Comment:// 发型师评论
                controlTab(R.id.tv_line_Comment, R.id.tv_orderdetails_Comment);
                if (comment.ID != null) {
                    list.clear();
                    list.add(comment);
                    adapter.notifyDataSetChanged();
                    tv_emptyComment.setVisibility(View.VISIBLE);
                } else {
                    // ToastUtils.showShort("没有评论");
                    // tv_emptyComment.setVisibility(View.VISIBLE);
                    // showCommentDailog();
                    tv_emptyComment.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_orderdetails_StoreComment:// 店铺评论
                controlTab(R.id.tv_line_StoreComment, R.id.tv_orderdetails_StoreComment);
                if (storeComment.ID != null) {
                    list.clear();
                    list.add(storeComment);
                    adapter.notifyDataSetChanged();
                    tv_emptyComment.setVisibility(View.VISIBLE);
                } else {
                    // ToastUtils.showShort("没有评论");
                    // tv_emptyComment.setVisibility(View.VISIBLE);
                    tv_emptyComment.setVisibility(View.GONE);
                    // showCommentDailog();
                }
                break;
            case R.id.btn_expence_dealwith:// 异常处理
                if (Integer.parseInt(state) > 1 && Integer.parseInt(state) < 8) {// 订单异常处理
                    alertDialog("是否进行异常处理？", 10 + "");
                }
                break;
            case R.id.btn_addorder:// 改单
                if (AdditionalState == 0 && Integer.parseInt(state) == 5) {
                    // 跳转到改单界面
                    Fragment fragment = FragmentManagerUtils.getFragment(activity, ModifyOrderFragment.class.getName());
                    Bundle bundle = new Bundle();
                    bundle.putString("OrderID", orderId);
                    fragment.setArguments(bundle);
                    FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
                }
                break;
            default:
                break;
        }
    }

    private void showCommentDailog() {

        final MessageDialog dialog = new MessageDialog(activity);
        dialog.setMessage("没有评论");
        dialog.setCancelButtonGone(true);
        dialog.getOkButton().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
        tv_emptyComment.setVisibility(View.VISIBLE);
    }

    /**
     * @Title: controlTab
     * @Description: 控件按钮设置
     * @author: 欧阳
     * @return: void
     */
    public void controlTab(int checked_iv, int checked_tv) {
        int red = getResources().getColor(R.color.red);
        int gray = getResources().getColor(R.color.black33);
        tv_Comment.setTextColor(checked_tv == R.id.tv_orderdetails_Comment ? red : gray);
        tv_StoreComment.setTextColor(checked_tv == R.id.tv_orderdetails_StoreComment ? red : gray);
        line_Comment.setVisibility(checked_iv == R.id.tv_line_Comment ? View.VISIBLE : View.INVISIBLE);
        line_StoreComment.setVisibility(checked_iv == R.id.tv_line_StoreComment ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * @ClassName: CommentAdapter
     * @Description: 评论适配器
     * @author: 欧阳
     * @date: 2015年10月19日 上午10:33:21
     */
    class CommentAdapter extends BaseAdapter {

        private Context context;
        private List<Comment> list;

        public CommentAdapter(Context context, List<Comment> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Comment getItem(int position) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_order_comment_layout, null);
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
            final Comment comment = list.get(position);
            vh.commentName.setText(comment.Name);
            RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
            RoundImageLoaderUtil.getInstance(activity, 360).loadImage(comment.Photo, vh.commentPhoto);
            vh.commmentTime.setText(comment.Time.substring(0, comment.Time.length() - 3));
            if (!TextUtils.isEmpty(comment.Score)) {
                Float Score = Float.valueOf(comment.Score);
                vh.commentScore.setVisibility(View.VISIBLE);
                vh.commentScore.setRating(Score);
            } else {
                vh.commentScore.setVisibility(View.VISIBLE);
                vh.commentScore.setRating(0);
            }
            vh.tv_commentcontent.setText(comment.Content);
            ImageView[] image = {vh.style1, vh.style2, vh.style3};
            if (comment.Item != null && comment.Item.size() > 0) {
                if (comment.Item.size() == 1) {
                    image[0].setVisibility(View.VISIBLE);
                    ImageLoaderUtils.getInstance(activity).loadImage(comment.Item.get(0).Photo, image[0]);
//					RoundImageLoaderUtil.getInstance(activity, 0).loadImage(comment.Item.get(0).Photo, image[0]);
                } else {
                    image[1].setVisibility(View.INVISIBLE);
                    image[2].setVisibility(View.INVISIBLE);
                }
                if (comment.Item.size() == 2) {
                    image[0].setVisibility(View.VISIBLE);
                    image[1].setVisibility(View.VISIBLE);
                    ImageLoaderUtils.getInstance(activity).loadImage(comment.Item.get(0).Photo, image[0]);
                    ImageLoaderUtils.getInstance(activity).loadImage(comment.Item.get(1).Photo, image[1]);
//					RoundImageLoaderUtil.getInstance(activity, 0).loadImage(comment.Item.get(0).Photo, image[0]);
//					RoundImageLoaderUtil.getInstance(activity, 0).loadImage(comment.Item.get(1).Photo, image[1]);
                } else {
                    image[2].setVisibility(View.INVISIBLE);
                }
                if (comment.Item.size() == 3) {
                    for (int i = 0; i < image.length; i++) {
                        image[i].setVisibility(View.VISIBLE);
                        ImageLoaderUtils.getInstance(activity).loadImage(comment.Item.get(i).Photo, image[i]);
//						RoundImageLoaderUtil.getInstance(activity, 0).loadImage(comment.Item.get(i).Photo, image[i]);
                    }
                }

            } else {
                for (int i = 0; i < image.length; i++) {
                    image[i].setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < comment.Item.size(); i++) {
                final int index = i;
                image[i].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> images = new ArrayList<String>();
                        for (int j = 0; j < comment.Item.size(); j++) {
                            images.add(comment.Item.get(j).Photo);
                        }
                        startActivity(ForLargeImageActivity.getIntent(activity, index, images));
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
        @ViewInject(R.id.image_comment_style1)
        ImageView style1;// 发型1
        @ViewInject(R.id.image_comment_style2)
        ImageView style2;// 发型2
        @ViewInject(R.id.image_comment_style3)
        ImageView style3;// 发型3
        @ViewInject(R.id.image_commentPhoto)
        ImageView commentPhoto;// 评论时间
        @ViewInject(R.id.ratingbar_commentScore)
        RatingBar commentScore;// 评论分数

        void findView(View convertView) {
            ViewUtils.inject(this, convertView);
        }
    }

    /**
     * @Title: alertDialog
     * @Description: 询问对话框
     * @author: 欧阳
     * @return: void
     */
    protected void alertDialog(String content, final String changestate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(content);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 网络请求更新UI
                LoadingDailog.show(activity, "正在更改订单状态...");
                sendNetDetailRequest(changestate);

            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // 最重要的代码
        dialog = builder.show();

    }

    /**
     * @Title: sendNetDetailRequest
     * @Description: 发送网络更新订单状态请求
     * @author: 欧阳
     * @return: void
     */
    protected void sendNetDetailRequest(final String changestate) {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            LoadingDailog.dismiss();
            tv_cancel.setEnabled(true);
            return;
        }
        H_UpdateOrderStateRequest request = new H_UpdateOrderStateRequest(UserManager.getUserID() + "", orderId,
                changestate);
        H_UpdateOrderStateApi.H_UpdateOrderState(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
                if (result == null) {
                    return;
                }
                String[] split = result.getMsg().split("\\|");
                if ("1".equals(split[0])) {
                    ToastUtils.showShort(split[1] + "");
                }
                if (result.getCode() == 1000) {
                    // 发送消息
                    EventBus.getDefault().post(new MyEventBus("刷新订单状态"));
                    ToastUtils.showShort("修改成功");
                    changeWidget(changestate);
                    state = changestate;
                    LoadingDailog.dismiss();
                    tv_cancel.setEnabled(true);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort("网络请求失败");
                LoadingDailog.dismiss();
                tv_cancel.setEnabled(true);
            }
        });

    }

    /**
     * @Title: changeWidget
     * @Description:改变控件的text
     * @author: 欧阳
     * @return: void
     */
    protected void changeWidget(String state) {
        switch (Integer.parseInt(state)) {
            case 3:
                tv_state.setText("已预约");
                tv_cancel.setText("签到");
                tv_nextstep.setText("等待美发师签到");
                ContentState = "是否进行签到？";
                break;
            case 4:
                tv_state.setText("已签到");
                tv_cancel.setText("开始服务");
                tv_nextstep.setText("美发师开始服务");
                ContentState = "是否开始服务项目？";
                break;
            case 5:
                tv_state.setText("服务中");
                tv_cancel.setText("结束服务");
                tv_nextstep.setText("结束服务");
                ContentState = "是否服务已经结束";
                dealwith.setVisibility(View.INVISIBLE);
                if (AdditionalState == 0) {
                    addorder.setVisibility(View.VISIBLE);
                }
                break;
            case 6:
                tv_state.setText("服务完成");
                tv_nextstep.setText("等待用户完成评论");
                tv_cancel.setVisibility(View.INVISIBLE);
                addorder.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.VISIBLE);
                break;
            case 10:
                tv_state.setText("异常处理中");
                tv_nextstep.setText("");
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

}
