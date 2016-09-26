package cc.ruit.shunjianmei.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.home.hairdressert.HairDresserActivity;
import cc.ruit.shunjianmei.home.hairdressert.HairDressertFragment;
import cc.ruit.shunjianmei.home.hairstyle.HairStyeActivity;
import cc.ruit.shunjianmei.home.hairstyle.HairStyleFragment;
import cc.ruit.shunjianmei.home.me.BrowserActivity;
import cc.ruit.shunjianmei.home.store.StoreActivity;
import cc.ruit.shunjianmei.home.store.StoreFragment;
import cc.ruit.shunjianmei.net.api.CustomerServicePhoneApi;
import cc.ruit.shunjianmei.net.api.HomePageApi;
import cc.ruit.shunjianmei.net.api.OpenCityListApi;
import cc.ruit.shunjianmei.net.request.GetCustomerServicePhoneRequest;
import cc.ruit.shunjianmei.net.request.HomePageRequest;
import cc.ruit.shunjianmei.net.request.OpenCityListRequest;
import cc.ruit.shunjianmei.net.response.GetCustomerServicePhoneResponse;
import cc.ruit.shunjianmei.net.response.HomePageResponse;
import cc.ruit.shunjianmei.net.response.OpenCityListResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.view.CityPopupwindow;
import cc.ruit.shunjianmei.util.view.CityPopupwindow.OnItemSelecedListener;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmei.util.view.EmptyView.State;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.Util;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.oruit.widget.loadingdialog.LoadingViewUtil;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.Mode;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshBase.OnRefreshListener2;
import com.zachary.hodge.uicomp.widget.refreshable.PullToRefreshListView;

/**
 * @ClassName: FindFragment
 * @Description: 发现界面
 * @author: 欧阳
 * @date: 2015年10月20日 上午7:32:55
 */
public class FindFragment extends BaseFragment implements OnItemSelecedListener, OnRefreshListener2<ListView> {
    private static final String TAG = FindFragment.class.getSimpleName();
    @ViewInject(R.id.mylistview_find)
    PullToRefreshListView listviewfind;// 发现列表
    @ViewInject(R.id.tv_city)
    TextView CityText;// 城市选择框
    @ViewInject(R.id.tv_title)
    TextView title;// 中间的标题
    @ViewInject(R.id.iv_right_image)
    ImageView servicetel;// 服务电话
    Findadapter adapter;// 适配器
    @ViewInject(R.id.content_frame_find)
    FrameLayout frame;
    @ViewInject(R.id.ll_city)
    LinearLayout ll_city;// 城市布局
    private EmptyView ev;// 空载页
    CityPopupwindow popupWindow;// 下拉菜单
    private static final int CITY_TAG = 0;// 城市星级标识
    private LocationClient mLocClient;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListenner();
    private double latitude;// 用户所在位置的经度
    private double longitude;// 用户所在位置的纬度
    private String city = "北京市";// 定位的城市
    List<OpenCityListResponse> mlist = new ArrayList<OpenCityListResponse>();
    List<HomePageResponse> list;// 商品详情列表
    List<GetCustomerServicePhoneResponse> phone;// 得到客服电话

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.home_find_layout, null);
        ViewUtils.inject(this, view);
        getCustomerServicePhone();
        initCityList();
        initPopupWindow();
        initTitle();
        initLocation();
        initMyListview();
        return view;
    }

    /**
     * @Title: getCustomerServicePhone
     * @Description: 得到客服电话
     * @author: 欧阳
     * @return: void
     */
    private void getCustomerServicePhone() {
        if (!NetWorkUtils.isConnectInternet(activity)) {
            ToastUtils.showShort("网络未链接，请检查网络设置");
            return;
        }
        GetCustomerServicePhoneRequest request = new GetCustomerServicePhoneRequest(UserManager.getUserID() + "");
        CustomerServicePhoneApi.CustomerServicePhone(request, new RequestCallBack<String>() {

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
                    phone = GetCustomerServicePhoneResponse.getclazz2(response.getData());
                    UserManager.setPhone(phone.get(0).getCustomerServicePhone());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort("请求数据失败");
            }
        });

    }

    /**
     * @Title: initLocation
     * @Description: 初始化百度定位
     * @author: Johnny
     * @return: void
     */
    private void initLocation() {
        mLocationClient = new LocationClient(activity.getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        mLocClient = new LocationClient(activity);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);
        option.setIsNeedAddress(true);// 获取地址信息
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * @Title: initPopupWindow
     * @Description: 初始化下来刷新
     * @author: 欧阳
     * @return: void
     */
    private void initPopupWindow() {
        // ----------初始化筛选框-------------
        if (popupWindow == null) {
            popupWindow = new CityPopupwindow(activity, frame);
            popupWindow.setOnItemClickListener(this);
        }
    }

    /**
     * @Title: initData
     * @Description: 网络请求动态添加
     * @author: 欧阳
     * @return: void
     */
    private void getData() {
        if (!NetWorkUtils.isConnectInternet(getActivity())) {
            ToastUtils.showShort(activity.getResources().getString(R.string.no_networks_found));
            LoadingDailog.dismiss();
            ev.setErrState();
            listviewfind.onRefreshComplete();
            return;
        }
        HomePageRequest request = new HomePageRequest(UserManager.getUserID() + "");
        HomePageApi.HomePage(request, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BaseResponse response = BaseResponse.getBaseResponse(responseInfo.result);
                listviewfind.onRefreshComplete();
                LoadingDailog.dismiss();
                if (response == null) {
                    return;
                }

                String[] split = response.getMsg().split("\\|");
                if ("1".equals(split[0])) {
                    ToastUtils.showShort(split[1] + "");
                }
                if (response.getCode() == 1000) {
                    List<HomePageResponse> templist = HomePageResponse.getclazz2(response.getData());
                    if (templist != null && templist.size() > 0) {
                        list.clear();
                        list.addAll(templist);
                    } else {
                        ev.setNullState();
                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                new LoadingViewUtil(view).hint();
                listviewfind.onRefreshComplete();
                ev.setErrState();
                ToastUtils.showShort(activity.getResources().getString(R.string.request_failure));
            }
        });

    }

    /**
     * @Title: getCityList
     * @Description: 得到城市列表
     * @author: 欧阳
     * @return: void
     */
    private void initCityList() {
      /*  try {
            System.loadLibrary("liblocnaviSDK");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.i("main",TAG+"错误="+e.getMessage());
        }*/
        if (!NetWorkUtils.isConnectInternet(activity)) {
            ToastUtils.showShort("网络未链接，请检查网络设置");
            return;
        }
        OpenCityListRequest request = new OpenCityListRequest(UserManager.getUserID() + "");
        OpenCityListApi.OpenCityList(request, new RequestCallBack<String>() {

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
                    List<OpenCityListResponse> templist = OpenCityListResponse.getclazz2(response.getData());
                    if (templist.size() > 0 && templist != null) {
                        mlist.clear();
                        mlist.addAll(templist);
                    }
                }

                initPopupWindowSelectedPosition();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showShort("请求数据失败");
            }
        });
    }

    /**
     * @Title: initTitle
     * @Description: 初始化标题
     * @author: 欧阳
     * @return: void
     */
    private void initTitle() {
        CityText.setText("北京市");
        title.setText("顺间");
        servicetel.setImageResource(R.drawable.store_service_icon);
        ll_city.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popupWindow.isCurrentShowing(CITY_TAG)) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.show(mlist, CITY_TAG);
                }

            }
        });
        servicetel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 打开拨号盘
                Util.openTelDall(activity, UserManager.getPhone());
                // UserManager.showCommentDailog(activity,UserManager.getPhone());
            }
        });

        initPopupWindowSelectedPosition();
    }

    /**
     * @Title: open
     * @Description: 打开拨号盘
     * @author: 欧阳
     * @return: void
     */
    protected void openDIAL() {
        Uri Storeuri = Uri.parse("tel:" + phone.get(0).getCustomerServicePhone());
        Intent Storeintent = new Intent();
        Storeintent.setAction(Intent.ACTION_DIAL);
        Storeintent.setData(Storeuri);
        startActivity(Storeintent);
    }

    /**
     * @Title: initMyListview
     * @Description: 初始化listview
     * @author: 欧阳
     * @return: void
     */
    private void initMyListview() {
        list = new ArrayList<HomePageResponse>();
        adapter = new Findadapter(activity, list, activity);
        listviewfind.setAdapter(adapter);
        listviewfind.setMode(Mode.PULL_FROM_START);
        ev = new EmptyView(activity, new OnClickListener() {

            @Override
            public void onClick(View v) {
                ev.setLoadingState();
                getData();
            }
        });
        listviewfind.setEmptyView(ev.getView());
        ev.setState(State.Loading);
        getData();
        listviewfind.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int Id = 0;
                try {
                    Id = Integer.parseInt(list.get(position - 1).getID());
                } catch (Exception e) {
                    // TODO: handle exception
                }
                switch (Id) {
                    case 1:// 打开发型
                        // TODO
                        Intent HairStyeintent = HairStyeActivity.getIntent(activity, HairStyleFragment.class.getName());
                        startActivity(HairStyeintent);
                        break;
                    case 2:// 发型师
                        // TODO
                        Intent Hairdresserintent = HairDresserActivity.getIntent(activity,
                                HairDressertFragment.class.getName());
                        Hairdresserintent.putExtra("title", "美发师");
                        startActivity(Hairdresserintent);
                        break;
                    case 3:// 美发店
                        Intent Storeintent = StoreActivity.getIntent(activity, StoreFragment.class.getName());
                        startActivity(Storeintent);
                        break;
                    case 4:// 预约
                        // TODO
                        Intent intent = HairDresserActivity.getIntent(activity, HairDressertFragment.class.getName());
                        startActivity(intent);
                        break;
                    case 5:// 价格表
                        Intent in = BrowserActivity.getIntent(activity, Constant.ME_PRICELIST, "价目表");
                        startActivity(in);
                        break;
                    default:
                        break;
                }
            }
        });
        listviewfind.setOnRefreshListener(this);
    }

    public class Findadapter extends BaseAdapter {
        List<HomePageResponse> list;
        FragmentActivity activity;

        // String[] name = new String[] { "发型", "美发师", "美发店", "预约", "价目表" } ;
        // String[] enname = new String[] { "HairDo", "HairDresser", "The Barber
        // Shop",
        // "Appointment", "Priced List" };
        Context context;
        Map<String, FindBean> map = new HashMap<String, FindBean>();

        public Findadapter(Context context, List<HomePageResponse> list, FragmentActivity activity) {
            super(context, list);
            this.context = context;
            this.activity = activity;
            this.list = list;
            // 目录 1发型2美发师3美发店4预约5价目表
            map.put("1", new FindBean("发型", "Hairdo"));
            map.put("2", new FindBean("美发师", "Hairdresser"));
            map.put("3", new FindBean("美发店", "The Barber Shop"));
            map.put("4", new FindBean("预约", "Appointment"));
            map.put("5", new FindBean("价目表", "Price List"));
        }

        // @Override
        // public int getCount() {
        // return map == null||list!=null||list.size()==0 ? 0 : map.size();
        // }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder sv = null;
            if (convertView == null) {
                sv = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.home_item_find_layout, null);
                sv.findView(convertView);
                convertView.setTag(sv);
            } else {
                sv = (ViewHolder) convertView.getTag();
            }
            // 得到数据和绑定数据
            setLable(position, sv);
            return convertView;
        }

        /**
         * @param position
         * @param sv
         * @Title: setLable
         * @Description: 绑定数据
         * @author: 欧阳
         * @return: void
         */
        public void setLable(int position, ViewHolder sv) {
            HomePageResponse response = list.get(position);
            if (map.get(response.getID()) != null) {
                sv.name.setText(map.get(response.getID()).getCnName());
                sv.enname.setText(map.get(response.getID()).getEnName());
                ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
                ImageLoaderUtils.getInstance(activity).loadImage(response.getPhoto(), sv.finditem);
            }
            if (position == 4) {

            }
        }
    }

    static class ViewHolder {
        @ViewInject(R.id.image_find_item)
        ImageView finditem;// 图片展示
        @ViewInject(R.id.tv_find_name)
        TextView name;// 中文名
        @ViewInject(R.id.tv_find_enname)
        TextView enname;// 英文名

        void findView(View convertView) {
            ViewUtils.inject(this, convertView);
        }
    }

    /**
     * @param parent
     * @param view
     * @param position
     * @param id
     * @param tag
     * @Title: onItemClick
     * @Description: 选择事件
     * @see cc.ruit.shunjianmei.util.view.BusinessAreaPopupwindow.OnItemSelecedListener#onItemClick(android.widget.AdapterView,
     * android.view.View, int, long, int)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, int tag) {
        String ID = mlist.get(position).getID();
        String City = mlist.get(position).getName();
        // 保存在本地
        UserManager.setCityID(Integer.parseInt(ID));
        popupWindow.setSelectedPosition(position);
        CityText.setText(City);
    }

	/*------------------------------百度地图定位 begin---------------------------------*/

    /**
     * @ClassName: MyLocationListenner
     * @Description: 定位SDK监听函数
     * @author: Johnny
     * @date: 2015年9月29日 下午3:14:57
     */

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

//            Log.i(" main", sb.toString());
            //=====================================================
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            city = location.getCity();
            if (city != null) {
                CityText.setText(city);
            }
            UserManager.setLatitude((float) 104.06);
            UserManager.setlongitude((float) 30.67);
            Log.i("main", TAG + "当前城市=" + city + latitude);
            for (int i = 0; i < mlist.size(); i++) {
                if (mlist.get(i).getName().equals(city)) {
                    UserManager.setCityID(Integer.parseInt(mlist.get(i).getID()));
                    UserManager.setLatitude((float) latitude);
                    UserManager.setlongitude((float) longitude);
                    return;
                } else {
                    ToastUtils.showShort("当前城市没有开通");
                    UserManager.setCityID(1);
                }
            }

            initPopupWindowSelectedPosition();
        }
    }

    /*------------------------------百度地图定位 end---------------------------------*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    private void initPopupWindowSelectedPosition() {

        if (mlist == null || mlist.size() == 0) {
            return;
        }

        for (int i = 0; i < mlist.size(); i++) {
            if (mlist.get(i).getName().equals(city)) {
                popupWindow.setSelectedPosition(i);
            }
        }

    }
}
