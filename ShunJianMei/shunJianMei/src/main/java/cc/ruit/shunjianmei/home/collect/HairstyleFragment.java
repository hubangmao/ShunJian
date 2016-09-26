package cc.ruit.shunjianmei.home.collect;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.home.hairstyle.HairStyleAdapter;
import cc.ruit.shunjianmei.home.hairstyle.HairStyleDetailsFragment;
import cc.ruit.shunjianmei.home.me.MyBalanceActivity;
import cc.ruit.shunjianmei.home.me.MyBalanceFragment;
import cc.ruit.shunjianmei.home.me.coupon.CouponActivity;
import cc.ruit.shunjianmei.home.me.coupon.CouponFragment;
import cc.ruit.shunjianmei.net.api.FavoriteHairStyleListApi;
import cc.ruit.shunjianmei.net.request.FavoriteHairStyleListRequest;
import cc.ruit.shunjianmei.net.response.HairStyleListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.ScreenUtils;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.umeng.analytics.MobclickAgent;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshGridView;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: HairstyleFragment
 * @Description: 收藏发型界面
 * @author: HBM
 * @date: 2015年12月9日 下午8:38:26
 */
public class HairstyleFragment extends BaseFragment implements OnClickListener {
    public static final String TAG = HairstyleFragment.class.getSimpleName();
    @ViewInject(R.id.pulltorefreshgridview)
    private PullToRefreshGridView gv_refresh;

    private List<HairStyleListResponse> msgList = new ArrayList<HairStyleListResponse>();
    private HairStyleAdapter myAdapter;

    private String pageSize = "10";// 加载页数目
    private int pageIndex = 1;// 加载页
    private EmptyView ev;// 空载页

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HairstyleFragment"); // 统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HairstyleFragment");
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.hairstyle_layout, null);
        ViewUtils.inject(this, view);
        initData();
        initEventBus();
        return view;
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

    /**
     * @param bus
     * @Title: onEventMainThread
     * @Description: 这个方法不能进行耗时操作
     * @author: 欧阳
     * @return: void
     */
    public void onEventMainThread(MyEventBus bus) {
        if (!TextUtils.isEmpty(bus.getmMsg()) && bus.getmMsg().equals("请刷新发型收藏列表")) {
            initData();
        }
    }

    /**
     * @Title: initData
     * @Description: 数据初始化
     * @author: Johnny
     * @return: void
     */
    private void initData() {
        myAdapter = new HairStyleAdapter(activity, msgList);
        gv_refresh.setAdapter(myAdapter);
        ev = new EmptyView(activity, new OnClickListener() {

            @Override
            public void onClick(View v) {
                pageIndex = 1;
                ev.setLoadingState();
                getData("" + pageIndex, pageSize);
            }
        });
        gv_refresh.setEmptyView(ev.getView());
        gv_refresh.getRefreshableView().setNumColumns(2);// 设置GridView列数
        gv_refresh.getRefreshableView().setVerticalSpacing(ScreenUtils.dip2px(activity, 10));
        gv_refresh.getRefreshableView().setHorizontalSpacing(ScreenUtils.dip2px(activity, 10));

        gv_refresh.setMode(Mode.BOTH);
        gv_refresh.setOnRefreshListener(new OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                pageIndex = 1;
                getData("" + pageIndex, pageSize);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                getData("" + pageIndex, pageSize);
            }

        });

        gv_refresh.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 打开发型详情列表
                Fragment fragment = FragmentManagerUtils.getFragment(
                        activity, HairStyleDetailsFragment.class.getName());
                Bundle bundle = new Bundle();
                bundle.putString("ID", msgList.get(position).ID);
                bundle.putString("Name", msgList.get(position).Name);
                fragment.setArguments(bundle);
                FragmentManagerUtils.add(getActivity(), R.id.content_frame,
                        fragment, true);

            }
        });

        ev.setState(State.Loading);
        getData("" + pageIndex, pageSize);
    }

    /**
     * @Title: getData
     * @Description: 请求接口
     * @author: Johnny
     * @return: void
     */
    private void getData(final String pageIndex, String pageSize) {
        if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
            ToastUtils.showShort(activity.getResources().getString(
                    R.string.no_networks_found));
            gv_refresh.onRefreshComplete();
            return;
        }

        int userId = UserManager.getUserID();
        FavoriteHairStyleListRequest request = new FavoriteHairStyleListRequest(
                "" + userId, pageIndex, pageSize);
        FavoriteHairStyleListApi.favoriteHairStyleList(request,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoadingDailog.dismiss();
                        gv_refresh.onRefreshComplete();
                        BaseResponse result = BaseResponse
                                .getBaseResponse(responseInfo.result);
                        if (result == null) {
                            return;
                        }
                        String[] split = result.getMsg().split("\\|");

                        if ("1".equals(split[0])) {
                            ToastUtils.showShort(split[1] + "");
                        }
                        if (result.getCode() == 1000) {
                            List<HairStyleListResponse> data = HairStyleListResponse
                                    .getclazz2(result.getData());
                            if (data != null) {
                                // 设置网络请求的数据
                                resultHanlder(data, pageIndex);
                            } else {
                                ToastUtils.showShort("请求数据异常");
                            }
                        } else if (result.getCode() == 2100) {
                            if ("1".equals(pageIndex)) {
                                ev.setNullState();
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MobclickAgent.onEvent(activity, "login_failure");
//						LoadingDailog.dismiss();
                        ev.setErrState();
                        ToastUtils.showShort(activity.getResources().getString(
                                R.string.request_failure));

                    }
                });

    }

    /**
     * @param listcontrolSignUp
     * @Title: resultHanlder
     * @Description: 结果处理
     * @author: Johnny
     * @return: void
     */
    void resultHanlder(List<HairStyleListResponse> info,
                       String pageIndex) {
        if (info == null) {
            LogUtils.e("MineResponse err");
            return;
        }

        if (!"1".equals(pageIndex) && info.size() <= 0) {
            return;
        }
        if ("1".equals(pageIndex)) {
            msgList.clear();
        }
        msgList.addAll(info);
        if (msgList.size() > 0) {
            ev.setDataState();
        } else {
            ev.setNullState();
        }
        this.pageIndex++;
        myAdapter.notifyDataSetChanged();

    }

    /**
     * @param v
     * @Title: onClick
     * @Description: 定义点击事件
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    @OnClick({R.id.rl_balance_me, R.id.rl_coupon_me, R.id.rl_pricelist_me,
            R.id.rl_favorite_me})
    public void onClick(View v) {
        Intent in;
        switch (v.getId()) {
            // 余额
            case R.id.rl_balance_me:
                in = MyBalanceActivity.getIntent(getActivity(),
                        MyBalanceFragment.class.getName());
                startActivity(in);
                break;
            // 我的优惠劵
            case R.id.rl_coupon_me:
                in = CouponActivity.getIntent(getActivity(),
                        CouponFragment.class.getName());
                startActivity(in);
                break;

            default:
                break;
        }
    }


//	/**
//	 * @ClassName: HairStyleListAdapter
//	 * @Description: 收藏发型列表
//	 * @author: Johnny
//	 * @date: 2015年9月11日 下午3:09:35
//	 */
//	class HairStyleListAdapter extends BaseAdapter {
//
//		private Context context;
//		private List<FavoriteHairStyleListResponse> list;
//
//		public HairStyleListAdapter(Context context,
//				List<FavoriteHairStyleListResponse> list) {
//			this.context = context;
//			this.list = list;
//		}
//
//		@Override
//		public void notifyDataSetChanged() {
//			super.notifyDataSetChanged();
//			if (list == null || list.size() <= 0) {
//				ev.setNullState();
//			}
//		}
//
//		@Override
//		public int getCount() {
//			return list == null ? 0 : list.size();
//		}
//
//		@Override
//		public FavoriteHairStyleListResponse getItem(int position) {
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder vh = null;
//			if (convertView == null) {
//				vh = new ViewHolder();
//				convertView = LayoutInflater.from(context).inflate(
//						R.layout.hairstyle_gridview_item_layout, null);
//				vh.findView(convertView);
//				convertView.setTag(vh);
//			} else {
//				vh = (ViewHolder) convertView.getTag();
//			}
//			// 得到数据和绑定数据
//			setLabel(position, vh);
//			return convertView;
//		}
//
//		/**
//		 * @Title: setLable
//		 * @Description: TODO
//		 * @author: Johnny
//		 * @param position
//		 * @param vh
//		 * @return: void
//		 */
//		private void setLabel(int position, ViewHolder vh) {
//			final FavoriteHairStyleListResponse obj = getItem(position);
//			vh.tv_name.setText(obj.getName());
//			vh.tv_money.setText(obj.getPrice());
//			vh.tv_soldnum.setText(obj.getUsedNum());
//			vh.collect_hairstyle.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					Fragment fragment = FragmentManagerUtils.getFragment(
//							activity, HairStyleDetailsFragment.class.getName());
//					Bundle bundle = new Bundle();
//					bundle.putString("ID", obj.getID());
//					bundle.putString("Name", obj.getName());
//					fragment.setArguments(bundle);
//					FragmentManagerUtils.add(getActivity(), R.id.content_frame,
//							fragment, true);
//				}
//			});
//			ImageLoaderUtils.getInstance(activity).loadImage(obj.getPhoto(),
//					vh.iv_photo);
//
//		}
//	}
//
//	static class ViewHolder {
//		@ViewInject(R.id.tv_name_hairstyle)
//		TextView tv_name;// 发型名称
//		@ViewInject(R.id.tv_money_hairstyle)
//		TextView tv_money;// 金额
//		@ViewInject(R.id.tv_soldnum_hairstyle)
//		TextView tv_soldnum;// 已售数量
//
//		@ViewInject(R.id.iv_photo_hairstyle)
//		ImageView iv_photo;// 发型照片
//		@ViewInject(R.id.ll_collect_hairstyle)
//		LinearLayout collect_hairstyle;
//
//		void findView(View convertView) {
//			ViewUtils.inject(this, convertView);
//		}
//	}
}
