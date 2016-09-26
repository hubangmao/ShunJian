package cc.ruit.shunjianmei.home.manager.material;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import cc.ruit.shunjianmei.MainApplication;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.MaterialApi;
import cc.ruit.shunjianmei.net.request.MaterialInfoRequest;
import cc.ruit.shunjianmei.net.request.SaveMaterialDeliveryRequest;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.view.EmptyView;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;
import cc.ruit.utils.sdk.http.NetWorkUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oruit.widget.loadingdialog.LoadingDailog;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: MaterialGetFragment
 * @Description: 物料提取页面
 * @author: leezw
 * @date: 2015-10-13 14:55:29
 */
@SuppressLint("InflateParams")
public class MaterialGetFragment extends BaseFragment implements
		OnClickListener {
	private MaterialGetProductInstance mGetProductInstance;
	@ViewInject(R.id.materialget_spinner_address)
	private Spinner chooseAddressSpinner;
	@ViewInject(R.id.materialget_radiobutton_choosetime1)
	private RadioButton choosetime1;
	@ViewInject(R.id.materialget_radiobutton_choosetime2)
	private RadioButton choosetime2;
	@ViewInject(R.id.materialget_radiobutton_choosetime3)
	private RadioButton choosetime3;
	@ViewInject(R.id.materialget_radiobutton_choosetime4)
	private RadioButton choosetime4;
	@ViewInject(R.id.materialget_radiobutton_choosetime5)
	private RadioButton choosetime5;
	@ViewInject(R.id.materialget_radiobutton_choosetime6)
	private RadioButton choosetime6;
	@ViewInject(R.id.materialget_radiobutton_choosetime7)
	private RadioButton choosetime7;
	@ViewInject(R.id.materialget_listview)
	private ListView mListViwe;
	@ViewInject(R.id.materialget_spinner_product_name)
	private Spinner productNameSpinner;
	@ViewInject(R.id.materialget_spinner_product_faction)
	private Spinner productFactionSpinner;
	@ViewInject(R.id.materialget_spinner_product_color)
	private Spinner productColorSpinner;
	@ViewInject(R.id.materialget_edittext_product_num)
	private EditText mEditText;
	@ViewInject(R.id.materialget_button_add)
	private TextView addButton;
	@ViewInject(R.id.materialget_button_ok)
	private TextView delButton;
	private ArrayAdapter<String> addressAdapter, proNameAdapter,
			proFactionAdapter, proColorAdapter;
	private ArrayList<ProductInstance> listProduct = new ArrayList<ProductInstance>();
	private ArrayList<InnerProductInstance> innerlistProduct = new ArrayList<InnerProductInstance>();

	private AdapterMaterialGetList adapterMaterialGetList;

	private int position_choosename = 0, position_choosefaction = 0;

	// 提交部分
	private String address, time, proName, proFaction, proColor, proNum;
	private String addressId, proNameId, proFactionId, proColorId;

	@SuppressLint("InflateParams")
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.materialget_fragment_layout, null);
		ViewUtils.inject(this, view);
		initData();
		return view;
	}

	private void initData() {
		addressAdapter = new ArrayAdapter<String>(
				MainApplication.getInstance(),
				R.layout.materialget_spinneradapter);
		proNameAdapter = new ArrayAdapter<String>(
				MainApplication.getInstance(),
				R.layout.materialget_spinneradapter);
		proFactionAdapter = new ArrayAdapter<String>(
				MainApplication.getInstance(),
				R.layout.materialget_spinneradapter);
		proColorAdapter = new ArrayAdapter<String>(
				MainApplication.getInstance(),
				R.layout.materialget_spinneradapter);
		adapterMaterialGetList = new AdapterMaterialGetList(
				MainApplication.getInstance(), innerlistProduct);
		mListViwe.setAdapter(adapterMaterialGetList);
		choosetime1.setText(getNextDay(1));
		choosetime2.setText(getNextDay(2));
		choosetime3.setText(getNextDay(3));
		choosetime4.setText(getNextDay(4));
		choosetime5.setText(getNextDay(5));
		choosetime6.setText(getNextDay(6));
		choosetime7.setText(getNextDay(7));
		getMaterialInfo();
	}

	private void getMaterialInfo() {
		LoadingDailog.show(activity, "数据加载中，请稍候。。。");
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			LoadingDailog.dismiss();
			return;
		}
		MaterialInfoRequest request = new MaterialInfoRequest(
				UserManager.getUserID() + "");
		MaterialApi.getMaterialInfo(request, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LoadingDailog.dismiss();
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
					mGetProductInstance = MaterialGetProductInstance
							.getBaseResponse(result.getData());
					setAddressAdapter(mGetProductInstance.address);
					setProductNameAdapter(mGetProductInstance.products);
					if (mGetProductInstance.products.size() > 0) {
						setProductFactionAdapter(mGetProductInstance.items
								.get(0).factions);
						if (mGetProductInstance.items.get(0).factions.size() > 0) {
							setProductColorAdapter(mGetProductInstance.items
									.get(0).items.get(0).colors);
						}
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MobclickAgent.onEvent(activity, "login_failure");
				LoadingDailog.dismiss();
				ToastUtils.showShort(activity.getResources().getString(
						R.string.request_failure));
			}
		});
	}

	/**
	 * 
	 * @Title: onClick
	 * @Description: 定义点击事件
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	@OnClick({ R.id.materialget_radiobutton_choosetime1,
			R.id.materialget_radiobutton_choosetime2,
			R.id.materialget_radiobutton_choosetime3,
			R.id.materialget_radiobutton_choosetime4,
			R.id.materialget_radiobutton_choosetime5,
			R.id.materialget_radiobutton_choosetime6,
			R.id.materialget_radiobutton_choosetime7,
			R.id.materialget_linearlayout_add, R.id.materialget_button_ok, })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.materialget_radiobutton_choosetime1:
			time = getDateNextDay(1);
			break;
		case R.id.materialget_radiobutton_choosetime2:
			time = getDateNextDay(2);
			break;
		case R.id.materialget_radiobutton_choosetime3:
			time = getDateNextDay(3);
			break;
		case R.id.materialget_radiobutton_choosetime4:
			time = getDateNextDay(4);
			break;
		case R.id.materialget_radiobutton_choosetime5:
			time = getDateNextDay(5);
			break;
		case R.id.materialget_radiobutton_choosetime6:
			time = getDateNextDay(6);
			break;
		case R.id.materialget_radiobutton_choosetime7:
			time = getDateNextDay(7);
			break;
		case R.id.materialget_linearlayout_add:
			if (chickMsg()) {
				ProductInstance itempro = new ProductInstance();
				itempro.MaterialID = proColorId;
				itempro.Num = proNum;
				InnerProductInstance instance = new InnerProductInstance();
				instance.name = proName;
				instance.num = proNum;
				instance.faction = proFaction;
				instance.color = proColor;
				
				listProduct.add(itempro);
				innerlistProduct.add(instance);
				adapterMaterialGetList.setDeviceList(innerlistProduct);
			}
			break;
		case R.id.materialget_button_ok:
			if (listProduct.size() > 0) {
				postToNet();
			} else {
				ToastUtils.showLong("请先添加商品");
			}
			break;
		default:
			time = getNextDay(1);
			break;
		}
	}

	/**
	 * @Title: chickMsg
	 * @Description: 检查选择信息是否遗漏
	 * @author: leezw
	 * @return: 没有异常返回true，继续接下来操作
	 */
	private boolean chickMsg() {
		// private String address, time, proName, proFaction, proColor, proNum;
		if (null == time || "".endsWith(time)) {
			ToastUtils.showLong("请选择提取时间");
			return false;
		}
		proNum = mEditText.getText().toString();
		if (null == proNum || "".endsWith(proNum)) {
			ToastUtils.showLong("请选输入提取物品数量");
			return false;
		} else {
			try {
				proNum = Integer.parseInt(mEditText.getText().toString()) + "";
			} catch (NumberFormatException e) {
				e.printStackTrace();
				proNum = "";
				ToastUtils.showLong("输入提取物品数量有误");
				return false;
			}
		}
		if (null == address || "".equals(address)) {
			return false;
		}
		return true;
	}

	/**
	 * @Title: postToNet
	 * @Description: 上传数据到网络
	 * @author: leezw
	 * @return: void
	 */
	private void postToNet() {
		// TODO Auto-generated method stub
		LoadingDailog.show(activity, "请稍候。。。");
		if (!NetWorkUtils.checkNetworkAvailable1(activity)) {
			ToastUtils.showShort(activity.getResources().getString(
					R.string.no_networks_found));
			LoadingDailog.dismiss();
			return;
		}
		SaveMaterialDeliveryRequest request = new SaveMaterialDeliveryRequest(
				UserManager.getUserID() + "", time, addressId, listProduct);
		MaterialApi.saveMaterialDelivery(request,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LoadingDailog.dismiss();
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
							ToastUtils.showShort("提交成功！");
							listProduct.clear();
							innerlistProduct.clear();
							adapterMaterialGetList.setDeviceList(innerlistProduct);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						MobclickAgent.onEvent(activity, "login_failure");
						LoadingDailog.dismiss();
						ToastUtils.showShort(activity.getResources().getString(
								R.string.request_failure));
					}
				});
	}

	/**
	 * @Title: getNextDay
	 * @Description: 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	 * @author: leezw
	 * @param delay
	 *            为前移或后延的天数，负值为前移
	 * @return: 例如：2015-05-17
	 */
	public static String getNextDay(int delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("M.d",
					Locale.getDefault());
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

	/**
	 * @Title: getNextDay
	 * @Description: 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	 * @author: leezw
	 * @param delay
	 *            为前移或后延的天数，负值为前移
	 * @return: 例如：2015-05-17
	 */
	public static String getDateNextDay(int delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
					Locale.getDefault());
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

	/**
	 * @ClassName: setAddressAdapter
	 * @Description: 设置取货地址spinnerAdapter
	 * @author: leezw
	 * @date: 2015-10-12 23:06:04
	 */
	private void setAddressAdapter(final ArrayList<Map<String, String>> list) {
		addressAdapter.clear();
		for (int i = 0; i < list.size(); i++) {
			addressAdapter.add(list.get(i).get("Address"));
		}
		addressAdapter
				.setDropDownViewResource(R.layout.materialget_spinner_dropdown_item);
		chooseAddressSpinner.setAdapter(addressAdapter);
		chooseAddressSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						address = list.get(position).get("Address");
						addressId = list.get(position).get("ID");
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						if (list.size() > 0) {
							address = list.get(0).get("Address");
							addressId = list.get(0).get("ID");
						}
					}
				});
	}

	/**
	 * @ClassName: setProductNameAdapter
	 * @Description: 设置货物名称 （多级联动）
	 * @author: leezw
	 * @date: 2015-10-12 23:06:04
	 */
	private void setProductNameAdapter(final ArrayList<Map<String, String>> list) {
		proNameAdapter.clear();
		for (int i = 0; i < list.size(); i++) {
			proNameAdapter.add(list.get(i).get("Name"));
		}
		proNameAdapter
				.setDropDownViewResource(R.layout.materialget_spinner_dropdown_item);
		productNameSpinner.setAdapter(proNameAdapter);
		productNameSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						proName = list.get(position).get("Name");
						proNameId = list.get(position).get("ID");
						position_choosename = position;
						setProductFactionAdapter(mGetProductInstance.items
								.get(position).factions);
						setProductColorAdapter(mGetProductInstance.items
								.get(position).items.get(0).colors);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						if (list.size() > 0) {
							proName = list.get(0).get("Name");
							proNameId = list.get(0).get("ID");
						}
					}
				});
	}

	/**
	 * @ClassName: setProductFactionAdapter
	 * @Description: 设置货物功能（多级联动）
	 * @author: leezw
	 * @date: 2015-10-12 23:06:04
	 */
	private void setProductFactionAdapter(
			final ArrayList<Map<String, String>> list) {
		proFactionAdapter.clear();
		for (int i = 0; i < list.size(); i++) {
			proFactionAdapter.add(list.get(i).get("Name"));
		}
		proFactionAdapter
				.setDropDownViewResource(R.layout.materialget_spinner_dropdown_item);
		productFactionSpinner.setAdapter(proFactionAdapter);
		productFactionSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						proFaction = list.get(position).get("Name");
						proFactionId = list.get(position).get("ID");
						position_choosefaction = position;
						setProductColorAdapter(mGetProductInstance.items
								.get(position_choosename).items.get(position).colors);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						if (list.size() > 0) {
							proFaction = list.get(0).get("Name");
							proFactionId = list.get(0).get("ID");
						}
					}
				});
	}

	/**
	 * @ClassName: setProductColorAdapter
	 * @Description: 设置货物颜色信息（多级联动）
	 * @author: leezw
	 * @date: 2015-10-12 23:06:04
	 */
	private void setProductColorAdapter(
			final ArrayList<Map<String, String>> list) {
		proColorAdapter.clear();
		for (int i = 0; i < list.size(); i++) {
			proColorAdapter.add(list.get(i).get("Name"));
		}
		proColorAdapter
				.setDropDownViewResource(R.layout.materialget_spinner_dropdown_item);
		productColorSpinner.setAdapter(proColorAdapter);
		productColorSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						proColor = list.get(position).get("Name");
						proColorId = list.get(position).get("ID");
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						if (list.size() > 0) {
							proColor = list.get(0).get("Name");
							proColorId = list.get(0).get("ID");
						}
					}
				});
	}

	class AdapterMaterialGetList extends BaseAdapter {
		// 此处的Object最好是Map<String, Object>,可以方便记清楚数据
		private ArrayList<InnerProductInstance> obgList;
		// private Context context;
		private LayoutInflater mInflater;

		public AdapterMaterialGetList(Context context,
				ArrayList<InnerProductInstance> list) {
			super(context, list);
			// this.context = context;
			this.mInflater = LayoutInflater.from(context);
			this.obgList = list;
		}

		/**
		 * 数据发生改变由adapter自己去处理，外部调用时注意在主线程中去调用： a.调用Activity.runOnUIThread()方法；
		 * b.使用Handler（其实这并不非常准确，因为Handler也可以运行在非UI线程）； 　　c.使用AsyncTask。
		 * 
		 * @param list
		 */
		@SuppressWarnings("unchecked")
		public void setDeviceList(ArrayList<InnerProductInstance> list) {
			if (list != null) {
				obgList = (ArrayList<InnerProductInstance>) list.clone();
				notifyDataSetChanged();
			}
		}

		public void clearDeviceList() {
			if (obgList != null) {
				obgList.clear();
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return null == obgList ? 0 : obgList.size();
		}

		@Override
		public Object getItem(int position) {
			return null == obgList ? null : obgList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			HoldView hold;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.materialget_fragment_list_item, null);
				hold = new HoldView();
				hold.initView(convertView);
				convertView.setTag(hold);
			} else {
				hold = (HoldView) convertView.getTag();
			}
			hold.name.setText(obgList.get(position).name);
			hold.faction.setText(obgList.get(position).faction);
			hold.color.setText(obgList.get(position).color);
			hold.num.setText(obgList.get(position).num);
			hold.del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listProduct.remove(position);
					obgList.remove(position);
					innerlistProduct.remove(position);
					notifyDataSetChanged();
				}
			});
			// 对UI内的组件进行操作控制

			return convertView;
		}

	}

	// 内部类，操作要填充的view部件内容
	static class HoldView {
		TextView name;
		TextView faction;
		TextView color;
		TextView num;
		TextView del;

		public void initView(View convertView) {
			name = (TextView) convertView
					.findViewById(R.id.materialget_listitem_textview_name);
			faction = (TextView) convertView
					.findViewById(R.id.materialget_listitem_textview_faction);
			color = (TextView) convertView
					.findViewById(R.id.materialget_listitem_textview_color);
			num = (TextView) convertView
					.findViewById(R.id.materialget_listitem_textview_num);
			del = (TextView) convertView
					.findViewById(R.id.materialget_listitem_textview_del);
		}
	}
}

class InnerProductInstance {
	public String name = "";
	public String faction = "";
	public String color = "";
	public String num = "";
}
