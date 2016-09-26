package cc.ruit.shunjianmei.baidumap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.platform.comapi.map.C;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.title.TitleUtil;

public class LocationActivity extends BaseActivity {

	@ViewInject(R.id.bmapView)
	private MapView mMapView;// 显示地图的视图层

	private BaiduMap mBaiduMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.location_activity_layout);
		ViewUtils.inject(this);
		initTitle();
		setMap();
	}

	/**
	 * 
	 * @Title: setMap
	 * @Description: 设置地图
	 * @author: Johnny
	 * @return: void
	 */
	private void setMap() {
		mBaiduMap = mMapView.getMap();
		//获取商铺坐标值
		Float latitude = Float.parseFloat(getIntent().getStringExtra("latitude"));
		Float longitude = Float.parseFloat(getIntent().getStringExtra("longitude"));
//		mBaiduMap.
		//定义Maker坐标点 
		LatLng p = new LatLng(latitude, longitude);
		//转换高德坐标为百度坐标
		LatLng p_convert = new CoordinateConverter().from(CoordType.COMMON).coord(p).convert();
		//设置中心点为指定点
		MapStatus mMapStatus = new MapStatus.Builder().target(p_convert).zoom(17).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(mMapStatusUpdate);
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icon_marka);  
		//设置 Marker 覆盖物的位置，图标，是否可拖曳
		MarkerOptions option = new MarkerOptions().position(p_convert).icon(bitmap)
				.draggable(false);

		//在地图上添加Marker，并显示 
		mBaiduMap.addOverlay(option);
	}
	
	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: Johnny
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(this);
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					finish();
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText(getIntent().getStringExtra("title"));
	}
	
	/**
	 * @Title: getIntent
	 * @Description: 获取跳转到当前Activity的intent对象
	 * @author: Johnny
	 * @param context
	 * @return
	 * @return: Intent
	 */
	public static Intent getIntent(Context context,String className) {
		Intent in = new Intent(context, LocationActivity.class);
		in.putExtra("className", className);
		return in;
	}
	/**
	 * @Title: getIntent
	 * @Description: 获取跳转到当前Activity的intent对象
	 * @author: Johnny
	 * @param context
	 * @return
	 * @return: Intent
	 */
	public static Intent getIntent(Context context,String className,String type) {
		Intent in = new Intent(context, LocationActivity.class);
		in.putExtra("className", className);
		in.putExtra("type", type);
		return in;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN) {
			boolean isBack = FragmentManagerUtils.back(this, R.id.content_frame);
			if (!isBack) {
				finish();
			}
		}
		return true;
	}
}
