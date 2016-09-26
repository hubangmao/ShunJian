package cc.ruit.shunjianmei.home.order;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.MainActivity;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertDetailsFragment;
import cc.ruit.shunjianmei.home.store.StoreDetailFragment;
import cc.ruit.shunjianmei.net.api.OrderDetailApi;
import cc.ruit.shunjianmei.net.api.UpdateOrderStateApi;
import cc.ruit.shunjianmei.net.request.OrderDetailRequest;
import cc.ruit.shunjianmei.net.request.UpdateOrderStateRequest;
import cc.ruit.shunjianmei.net.response.OrderDetailResponse;
import cc.ruit.shunjianmei.net.response.OrderListResponse;
import cc.ruit.shunjianmei.pay.OrderPayActivity;
import cc.ruit.shunjianmei.pay.OrderPayFragment;
import cc.ruit.shunjianmei.reserve.EventOrderBean;
import cc.ruit.shunjianmei.reserve.ReserveActivity;
import cc.ruit.shunjianmei.reserve.ReserveFragment;
import cc.ruit.shunjianmei.reserve.ReserveFreeFragment;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.ForLargeImageActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmei.util.view.MyListView;
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

import de.greenrobot.event.EventBus;

/**
 * @ClassName: OrderDetailFragment
 * @Description: 订单详情
 * @author: 欧阳
 * @date: 2015年10月19日 上午9:27:17
 */
public class OrderDetailFragment extends BaseFragment {
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
    @ViewInject(R.id.tv_hairstylist_level)
    TextView levelName;
    // @ViewInject(R.id.tv_empty_ordercomment)
    // TextView tv_comment;
    @ViewInject(R.id.rl_order_detail_comment)
    RelativeLayout rl_comment;
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
    @ViewInject(R.id.tv_OrderDetails_title)
    TextView tv_title;// 标题
    @ViewInject(R.id.iv_OrderDetails_right_image)
    ImageView iv_right;
    CommentAdapter adapter;// 评论适配器
    List<OrderDetailResponse.Comment> list;// 评论接口
    cc.ruit.shunjianmei.net.response.OrderDetailResponse.Comment storeComment;// 店铺评论
    cc.ruit.shunjianmei.net.response.OrderDetailResponse.Comment comment;
    List<OrderDetailResponse> templist;
    String Mobile;// 发型师电话
    String StoreTel;// 店铺电话
    AlertDialog dialog;
    int state = 0;// 状态处理
    String ContentState;// 状态内容显示
    String orderId;// 订单id
    Float masterscore;
    int Additional_State = 0;// 加单状态
    public static String EvalID = "";
    private String mistype;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.order_detail_layout, null);
        ViewUtils.inject(this, view);
        initTitle();
        initMyListView();
        initData();
        initEventBus();
        return view;
    }

    /**
     * @Title: initTitle
     * @Description: 初始化标题
     * @author: 欧阳
     * @return: void
     */
    private void initTitle() {
        tv_title.setText("订单详情");
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11,
                getResources().getDisplayMetrics());
        iv_right.setPadding(padding, padding, padding, padding);
        iv_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // UserManager.showCommentDailog(activity,UserManager.getPhone());
                Util.openTelDall(activity, UserManager.getPhone());
                // Uri uri = Uri.parse("tel:" +UserManager.getPhone());
                // Intent intent = new Intent();
                // intent.setAction(Intent.ACTION_CALL);
                // intent.setData(uri);
                // startActivity(intent);
            }
        });
        //店铺名超长触摸事件
        StoreName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent evente) {
                switch (evente.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        StoreName.setSingleLine(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        StoreName.setSingleLine(true);
                        break;
                }
                return true;
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
        list = new ArrayList<OrderDetailResponse.Comment>();
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
            }
        }
        LoadingDailog.show(activity, "正在加载....");
        getData();
    }

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

    public void onEventMainThread(EventOrderBean bean) {
        if (bean.isPay()) {
            initData();
        }
    }

    /**
     * @param bus
     * @Title: onEventMainThread
     * @Description: 这个方法不能进行耗时操作
     * @author: 欧阳
     * @return: void
     */
    public void onEventMainThread(MyEventBus bus) {
        if (!TextUtils.isEmpty(bus.getmMsg()) && bus.getmMsg().equals("请刷新订单状态")) {
            initData();
        }

    }

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
        OrderDetailRequest request = new OrderDetailRequest(UserManager.getUserID() + "", orderId);
        Log.i("Tag", "request=" + request.toJsonString(request));
        OrderDetailApi.OrderDetail(request, new RequestCallBack<String>() {

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
                    templist = OrderDetailResponse.getclazz2(result.getData());
                    bundweidgt(templist);
                    comment = templist.get(0).Comment;
                    storeComment = templist.get(0).StoreComment;
                    EvalID = templist.get(0).Comment.ID;
                    if (comment != null && comment.ID != null) {
                        list.clear();
                        list.add(comment);
                        adapter.notifyDataSetChanged();
                        rl_comment.setVisibility(View.VISIBLE);
                        // tv_comment.setVisibility(View.GONE);
                    } else {
                        // tv_comment.setVisibility(View.VISIBLE);
                        rl_comment.setVisibility(View.GONE);
                    }
                    LoadingDailog.dismiss();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort("网络请求失败");
                LoadingDailog.dismiss();
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
    protected void bundweidgt(List<OrderDetailResponse> templist) {
        if (templist == null) {
            return;
        }
        orderId = templist.get(0).OrderID;
        tv_code.setText(templist.get(0).OrderCode);
        tv_nextstep.setText(templist.get(0).NextStep);
        tv_ordertime.setText(templist.get(0).AppointmentTime);
        RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
        RoundImageLoaderUtil.getInstance(activity, 360).loadImage(templist.get(0).Photo, hairstylist_photo);
        hairstylist_Name.setText(templist.get(0).Name);
        Mobile = templist.get(0).Mobile.toString();
        if (TextUtils.isEmpty(templist.get(0).Score)) {
            masterscore = Float.valueOf("0.0");
        } else {
            masterscore = Float.valueOf(templist.get(0).Score);
        }
        hairstylist_evaluate.setRating(masterscore);
        order_quantity.setText("接单" + templist.get(0).OrderNum + "次");
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
        levelName.setText(templist.get(0).LevelName);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < templist.get(0).Item.size(); i++) {
            sb.append(templist.get(0).Item.get(i).Name + " ");
        }
        Item.setText(sb.toString());
        if (TextUtils.isEmpty(templist.get(0).AdditionalContent)) {
            AddContent.setText("无");
            AddAmount.setText("¥0.0");
            addorder_price.setText("¥0.0");
        } else {
            AddContent.setText(templist.get(0).AdditionalContent);
            AddAmount.setText("¥" + templist.get(0).AdditionalAmount);
            addorder_price.setText("¥" + templist.get(0).AdditionalAmount);
        }
        discount_price.setText("¥" + templist.get(0).Discount);
        try {
            state = Integer.parseInt(templist.get(0).State);
            Additional_State = Integer.parseInt(templist.get(0).AdditionalState);
        } catch (Exception e) {
        }
        switch (state) {// 状态1待支付2待确认3已预约4已签到5服务中6服务完成7评论完成8订单完成9已取消10异常处理
            case 1:
                tv_state.setText(templist.get(0).StateName + "(请在30分钟内支付)");
                tv_cancel.setVisibility(View.VISIBLE);
                addorder.setVisibility(View.VISIBLE);
                tv_nextstep.setText("预约成功");
                tv_cancel.setText("支付");
                ContentState = "是否确认支付？";
                addorder.setText("取消");
                break;
            case 2:
                tv_state.setText(templist.get(0).StateName);
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.INVISIBLE);
                addorder.setVisibility(View.VISIBLE);
                addorder.setText("取消");
                break;
            case 3:
                tv_state.setText("预约成功");
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.INVISIBLE);
                addorder.setVisibility(View.VISIBLE);
                order_dealwith.setVisibility(View.VISIBLE);
                addorder.setText("退款");
                break;
            case 4:
                tv_state.setText(templist.get(0).StateName);
                tv_cancel.setVisibility(View.INVISIBLE);
                addorder.setVisibility(View.INVISIBLE);
                addorder.setVisibility(View.GONE);
                order_dealwith.setVisibility(View.GONE);
                break;
            case 5:
                tv_state.setText(templist.get(0).StateName);
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.INVISIBLE);
                if (Additional_State == 1 && state == 5) {// 改单支付
                    addorder.setText("加单支付");
                    addorder.setVisibility(View.VISIBLE);
                    order_dealwith.setVisibility(View.VISIBLE);
                } else {
                    addorder.setVisibility(View.GONE);
                    order_dealwith.setVisibility(View.GONE);
                }

                break;
            case 6:
                tv_state.setText(templist.get(0).StateName);
                tv_cancel.setVisibility(View.VISIBLE);
                tv_cancel.setText("评论");
                dealwith.setVisibility(View.INVISIBLE);
                addorder.setVisibility(View.GONE);
                order_dealwith.setVisibility(View.GONE);
                break;
            case Constant.COMMENT_OK:
                tv_state.setText(templist.get(0).StateName);
                tv_cancel.setVisibility(View.VISIBLE);
                addorder.setVisibility(View.GONE);
                dealwith.setVisibility(View.INVISIBLE);
                order_dealwith.setVisibility(View.GONE);
                tv_cancel.setText("分享");
                break;
            case Constant.ORDER_OK:
                if (templist.get(0).Comment.Time == null) {
                    tv_state.setText(templist.get(0).StateName);
                    tv_cancel.setVisibility(View.VISIBLE);
                    tv_cancel.setText("评论");
                } else {
                    tv_state.setText(templist.get(0).StateName);
                    tv_cancel.setVisibility(View.VISIBLE);
                    tv_cancel.setText("分享");
                    addorder.setVisibility(View.GONE);
                    dealwith.setVisibility(View.GONE);
                    order_dealwith.setVisibility(View.GONE);
                }
                break;
            case 9:
                tv_state.setText(templist.get(0).StateName);
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.GONE);
                addorder.setText("再次预约");
                addorder.setVisibility(View.VISIBLE);
                break;
            case 10:
                tv_state.setText(templist.get(0).StateName);
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.GONE);
                addorder.setVisibility(View.INVISIBLE);
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
            R.id.btn_addorder, R.id.iv_OrderDetails_back, R.id.iv_OrderDetails_right_image,
            R.id.image_hairstylist_photo, R.id.image_orderdetail_StorePhoto})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_orderdetail_cancel:// 支付或者评论
                if (state == 1) {
                    openPayFragment();
                } else if (state == 6) {
                    openEvaluationFragment();
                } else if (state == Constant.COMMENT_OK) {// 分享
                    openOrderShareFragment();
                } else if (state == Constant.ORDER_OK) {
                    if (tv_cancel.getText().equals("评论")) {
                        openEvaluationFragment();
                    } else {
                        openOrderShareFragment();
                    }
                }
                break;
            case R.id.image_hairstylist_master_Mobile:// 发型师电话
                if (TextUtils.isEmpty(Mobile)) {
                    ToastUtils.showShort("不好意思暂时没有发型师电话", Gravity.CENTER);
                    return;
                }
                // UserManager.showCommentDailog(activity,Mobile);
                Util.openTelDall(activity, Mobile);
                // openDIAL(Mobile);
                break;
            case R.id.image_orderdetails_StoreTel:// 店铺电话
                if (TextUtils.isEmpty(StoreTel)) {
                    ToastUtils.showShort("不好意思暂时没有店铺电话", Gravity.CENTER);
                    return;
                }
                // UserManager.showCommentDailog(activity,StoreTel);
                Util.openTelDall(activity, StoreTel);
                // openDIAL(StoreTel);
                break;
            case R.id.tv_orderdetails_Comment:// 发型师评论
                controlTab(R.id.tv_line_Comment, R.id.tv_orderdetails_Comment);
                if (comment != null && comment.ID != null) {
                    list.clear();
                    list.add(comment);
                    adapter.notifyDataSetChanged();
                    rl_comment.setVisibility(View.VISIBLE);
                    // tv_comment.setVisibility(View.GONE);
                } else {
                    // ToastUtils.showShort("没有评论");
                    // tv_comment.setVisibility(View.VISIBLE);
                    rl_comment.setVisibility(View.GONE);
                    // showCommentDailog();
                }
                break;
            case R.id.tv_orderdetails_StoreComment:// 店铺评论
                controlTab(R.id.tv_line_StoreComment, R.id.tv_orderdetails_StoreComment);
                if (storeComment != null && storeComment.ID != null) {
                    list.clear();
                    list.add(storeComment);
                    adapter.notifyDataSetChanged();
                    rl_comment.setVisibility(View.VISIBLE);
                    // tv_comment.setVisibility(View.GONE);
                } else {
                    // ToastUtils.showShort("没有评论");
                    // tv_comment.setVisibility(View.VISIBLE);
                    // showCommentDailog();
                    rl_comment.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_expence_dealwith:// 异常处理
                if (state > 1 && state < 8) {// 订单异常处理
                    alertDialog("是否进行异常处理？", 10 + "");
                }
                break;
            case R.id.btn_addorder:// 改单支付
                if (Additional_State == 1 && state == 5) {
                    payAddToFragment();
                } else if (Additional_State != 1 && state < 4) {// 取消订单
                    alertDialog("您是否取消订单？", 9 + "");
                } else if (state == 9) {// 订单预约
                    // qqq
                    mistype = templist.get(0).istype;
                    openReserveFragment(templist.get(0).OrderID);
                }
                break;
            case R.id.iv_OrderDetails_back:// 返回键
                boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
                if (!isBack) {
                    getActivity().finish();
                    if (MainActivity.getInstance() == null) {
                        startActivity(MainActivity.getIntent(activity, OrderFragment.class.getName()));
                    }
                }
                break;
            case R.id.iv_OrderDetails_right_image:// 打电话
                openDIAL(UserManager.getPhone());
                break;
            case R.id.image_hairstylist_photo:
                openhairstylistDetails();
                break;
            case R.id.image_orderdetail_StorePhoto:
                openHairStoreDetails();
                break;
            default:
                break;
        }
    }

    /**
     * @Title: openOrderShareFragment
     * @Description: 打开分享界面
     * @return: void
     */
    private void openOrderShareFragment() {
        Fragment fragment = FragmentManagerUtils.getFragment(activity, OrderShareFragment.class.getName());
        FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);

    }

    /**
     * @Title: openHairStoreDetails
     * @Description: 打开美发师店铺
     * @author: 欧阳
     * @return: void
     */
    private void openHairStoreDetails() {
        Fragment fragment = FragmentManagerUtils.getFragment(activity, StoreDetailFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putString("ID", templist.get(0).StoreID);
        fragment.setArguments(bundle);
        FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
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
        bundle.putString("ID", templist.get(0).ID);
        bundle.putString("Name", templist.get(0).Name);
        if (templist.get(0).istype.equals("1")) {
            bundle.putString("Type", "自由定价");
        } else {
            bundle.putString("Type", "资深合作");
        }
        fragment.setArguments(bundle);
        FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
    }

    /**
     * @Title: openPayFragment
     * @Description: 打开支付界面
     * @author: 欧阳
     * @return: void
     */
    public void openPayFragment() {
        Intent intent = OrderPayActivity.getIntent(activity, OrderPayFragment.class.getName());
        intent.putExtra("OrderID", orderId);
        intent.putExtra("From", OrderDetailFragment.class.getName());
        startActivity(intent);
        activity.finish();
    }

    /**
     * @Title: openEvaluationFragment
     * @Description: 打开评论界面
     * @author: 欧阳
     * @return: void
     */
    public void openEvaluationFragment() {
        Fragment fragment = FragmentManagerUtils.getFragment(activity, OrderEvaluationFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putString("ID", templist.get(0).OrderID);
        fragment.setArguments(bundle);
        FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
    }

    /**
     * @Title: openReserveFragment
     * @Description: 打开预约界面
     * @author: 欧阳
     * @return: void
     */
    public void openReserveFragment(String OrderID) {
        if (mistype.equals("1")) {
            Intent intent = ReserveActivity.getIntent(activity, ReserveFreeFragment.class.getName());
            Bundle bundle = new Bundle();
            bundle.putString("OrderID", OrderID);
            bundle.putString("From", OrderDetailFragment.class.getSimpleName());
            intent.putExtras(bundle);
            startActivity(intent);
            activity.finish();
        } else {
            Intent intent = ReserveActivity.getIntent(activity, ReserveFragment.class.getName());
            Bundle bundle = new Bundle();
            bundle.putString("OrderID", OrderID);
            bundle.putString("From", OrderDetailFragment.class.getSimpleName());
            intent.putExtras(bundle);
            startActivity(intent);
            activity.finish();
        }

        // Fragment fragment = FragmentManagerUtils.getFragment(activity,
        // ReserveFragment.class.getName());
        // Bundle bundle = new Bundle();
        // bundle.putString("ID", templist.get(0).OrderID);
        // bundle.putString("From", OrderDetailFragment.class.getSimpleName());
        // fragment.setArguments(bundle);
        // FragmentManagerUtils.add(activity, R.id.content_frame,
        // new ReserveFragment(), true);
    }

    /**
     * @Title: payAddToFragment
     * @Description: 打开加单支付界面
     * @author: 欧阳
     * @return: void
     */
    public void payAddToFragment() {
        Fragment fragment = FragmentManagerUtils.getFragment(activity, OrderModifyFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putString("OrderID", orderId);
        bundle.putString("AdditionalCode", templist.get(0).AdditionalCode);
        bundle.putString("AdditionalContent", templist.get(0).AdditionalContent);
        bundle.putString("AdditionalAmount", templist.get(0).AdditionalAmount);
        fragment.setArguments(bundle);
        FragmentManagerUtils.add(activity, R.id.content_frame, fragment, true);
    }

    /**
     * @Title: open
     * @Description: 打开拨号盘
     * @author: 欧阳
     * @return: void
     */
    protected void openDIAL(String phone) {
        Uri Storeuri = Uri.parse("tel:" + phone);
        Intent Storeintent = new Intent();
        Storeintent.setAction(Intent.ACTION_CALL);
        Storeintent.setData(Storeuri);
        startActivity(Storeintent);
    }

    /**
     * @Title: controlTab
     * @Description: 控件按钮设置
     * @author: 欧阳
     * @return: void
     */
    public void controlTab(int checked_iv, int checked_tv) {
        int red = getResources().getColor(R.color.red_e5);
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
        private List<OrderDetailResponse.Comment> list;

        public CommentAdapter(Context context, List<OrderDetailResponse.Comment> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public OrderDetailResponse.Comment getItem(int position) {
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
            OrderDetailResponse.Comment comment = list.get(position);
            vh.commentName.setText(comment.Name);
            vh.tv_commentcontent.setText(comment.Content);
            RoundImageLoaderUtil.setErrImage(R.drawable.tx_man, R.drawable.tx_man, R.drawable.tx_man);
            RoundImageLoaderUtil.getInstance(activity, 360).loadImage(comment.Photo, vh.commentPhoto);
            vh.commmentTime.setText(comment.Time.substring(0, comment.Time.length() - 3));
            if (!TextUtils.isEmpty(comment.Score)) {
                Float Score = Float.valueOf(comment.Score);
                vh.commentScore.setRating(Score);
            } else {
                vh.commentScore.setRating(0);
            }
            addImages(comment.Item, vh.ll_image);
        }

        /**
         * @Title: addImages
         * @Description:
         * @author: Johnny
         * @return: void
         */
        private void addImages(final List<OrderDetailResponse.Comment.Item> images, LinearLayout ll) {
            ll.removeAllViews();
            ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
            for (int i = 0; i < images.size(); i++) {
                ImageView iv = new ImageView(activity);
                ImageLoaderUtils.getInstance(activity).loadImage(images.get(i).Photo, iv);
                int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                        activity.getResources().getDisplayMetrics());
                iv.setPadding(padding, padding, padding, padding);
                int width = ScreenUtils.getScreenWidth(activity) / 3;
                ll.addView(iv, new LayoutParams(width, width));
                final int index = i;
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> image = new ArrayList<String>();
                        for (int j = 0; j < images.size(); j++) {
                            image.add(images.get(j).Photo);
                        }
                        startActivity(ForLargeImageActivity.getIntent(activity, index, image));
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

    /**
     * @Title: alertDialog
     * @Description: 询问对话框
     * @author: 欧阳
     * @return: void
     */
    protected void alertDialog(String content, final String state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(content);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 网络请求更新UI
                sendNetDetailRequest(state);
                EventBus.getDefault().post(new MyEventBus("请刷新状态"));

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
    protected void sendNetDetailRequest(final String state) {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            return;
        }
        UpdateOrderStateRequest request = new UpdateOrderStateRequest(UserManager.getUserID() + "", orderId, state);
        UpdateOrderStateApi.UpdateOrderState(request, new RequestCallBack<String>() {

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
                    ToastUtils.showShort("修改成功");
                    EventBus.getDefault().post("请刷新订单状态");
                    changeWidget(state);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort("网络请求失败");
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
            case 9:
                tv_state.setText("已取消");
                tv_nextstep.setText(" ");
                tv_cancel.setVisibility(View.INVISIBLE);
                addorder.setVisibility(View.VISIBLE);
                addorder.setText("再次预约");
                this.state = 9;
                break;
            case 10:
                tv_state.setText("异常处理中");
                tv_nextstep.setText("");
                tv_cancel.setVisibility(View.INVISIBLE);
                dealwith.setVisibility(View.GONE);
                order_dealwith.setVisibility(View.GONE);
                this.state = 10;
                break;
            default:
                break;
        }

    }

}
