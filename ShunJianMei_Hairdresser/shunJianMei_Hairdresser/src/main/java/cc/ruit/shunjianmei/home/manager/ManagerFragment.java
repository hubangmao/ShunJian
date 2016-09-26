package cc.ruit.shunjianmei.home.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseAdapter;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.home.ServicePackageFragment;
import cc.ruit.shunjianmei.home.manager.material.MaterialManagementActivity;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.title.TitleUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: ManagerFragment
 * @Description: TODO
 * @author: lee
 * @date: 2015年10月12日 下午3:49:45
 */
public class ManagerFragment extends BaseFragment implements OnItemClickListener {
	@ViewInject(R.id.gv_list)
	GridView gv_list;// 管理排版

	List<HomeBean> list = new ArrayList<HomeBean>();

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ManagerFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ManagerFragment");
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.manager_layout, null);
		ViewUtils.inject(this, view);
		initTitle();
		initData();
		return view;
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: lee
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(view);
		// title.iv_left.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		// title.iv_left.setImageResource(R.drawable.back);
		// title.iv_left.setVisibility(View.VISIBLE);
		title.tv_title.setText("管理");
	}

	private void initData() {
		list.add(new HomeBean("服务套餐", "Service package", R.drawable.tu05));
		// list.add(new HomeBean("客户管理", "Customer management",
		// R.drawable.tu01));
		list.add(new HomeBean("发型管理", "Hair management", R.drawable.tu02));
		list.add(new HomeBean("店面管理", "Store management", R.drawable.tu03));
		list.add(new HomeBean("物料管理", "Material management", R.drawable.tu04));
		// list.add(new HomeBean("推广工具", "Promotion", R.drawable.tu05));
		// list.add(new HomeBean("服务套餐", "Promotion", R.drawable.tu05));
		gv_list.setAdapter(new ManagerAdapter(activity, list));
		gv_list.setVerticalScrollBarEnabled(false);
		gv_list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:
			activity.startActivity(ManagerActivity.getIntent(activity, ServicePackageFragment.class.getName()));

			break;
		case 1:
			activity.startActivity(ManagerActivity.getIntent(activity, HairManagement.class.getName()));
			break;
		case 2:
			activity.startActivity(ManagerActivity.getIntent(activity, StoreFragment.class.getName()));
			break;
		case 3:
			ToastUtils.showShort("暂未开放");
			// activity.startActivity(new Intent(activity,
			// MaterialManagementActivity.class));
			break;
		case 4:
			ToastUtils.showShort("暂未开放");
			break;
		case 5:
			ToastUtils.showShort("暂未开放");
			// activity.startActivity(ManagerActivity.getIntent(activity,
			// ServicePackageFragment.class.getName()));
			break;

		default:
			break;
		}
	}
	// @Override
	// @OnClick({ R.id.btn_login, R.id.iv_title_back, R.id.tv_forgetpassword })
	// public void o {
	// switch (v.getId()) {
	// case R.id.btn_login:
	//// if (checkUserLoginParems()) {
	//// userLogin(et_name.getText().toString(), et_password.getText()
	//// .toString());
	//// }
	// break;
	// case R.id.iv_title_back:
	// FragmentManagerUtils.back(getActivity(), R.id.content_frame);
	// break;
	// case R.id.tv_forgetpassword:
	// Fragment fragment = FragmentManagerUtils.getFragment(activity,
	// ForgetFragment.class.getName());
	// FragmentManagerUtils.add(activity, R.id.content_frame, fragment,
	// true);
	// break;
	//
	// default:
	// break;
	// }
	// }

	/**
	 * @Title: checkParems
	 * @Description: 检测参数
	 * @author: lee
	 * @return: boolean true表示所有参数无异常，false则反之
	 */
	boolean checkParems() {
		// 获取验证码
		// String mobile = et_name.getText().toString();
		// if (TextUtils.isEmpty(mobile)) {
		// ToastUtils.showShort("手机号码不能为空!");
		// return false;
		// }
		// if (!ParmsUtil.checkPhone_2(mobile)) {
		// ToastUtils.showShort("手机号码格式错误!");
		// return false;
		// }
		return true;
	}

	/**
	 * @Title: checkParems
	 * @Description: 检测参数
	 * @author: lee
	 * @return: boolean true表示所有参数无异常，false则反之
	 */
	boolean checkUserLoginParems() {
		// 获取手机号
		// String mobile = et_name.getText().toString();
		// if (TextUtils.isEmpty(mobile)) {
		// ToastUtils.showShort("手机号码不能为空!");
		// return false;
		// }
		// if (!ParmsUtil.checkPhone_2(mobile)) {
		// ToastUtils.showShort("手机号码格式错误!");
		// return false;
		// }
		// //获取密码
		// String code = et_password.getText().toString();
		// if (TextUtils.isEmpty(code)) {
		// ToastUtils.showShort("密码不能为空!");
		// return false;
		// }
		return true;
	}

	class ManagerAdapter extends BaseAdapter {
		List<HomeBean> list;
		int width = 0;

		public ManagerAdapter(Context context, List<HomeBean> list) {
			super(context, list);
			this.list = list;
			width = getScreenWidth();
		}

		@Override
		public HomeBean getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(activity).inflate(R.layout.item_manager_list_layout, null);
				vh.findView(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// 得到数据和绑定数据
			setLable(position, vh);
			int num = (width - 30) / 2 - BaseFragment.dip2px(activity, 10);
			LayoutParams p = new LayoutParams(num, num);
			convertView.setLayoutParams(p);
			return convertView;
		}

		/**
		 * @Title: setLable
		 * @Description: TODO
		 * @author: lee
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLable(int position, ViewHolder vh) {
			HomeBean obj = getItem(position);
			// ---------提示文字颜色不一样，特殊处理一下-----------
			vh.tv_name.setText(obj.getName());
			vh.tv_enname.setText(obj.getDesc());
			vh.iv_bg.setImageResource(obj.getRid());
		}

	}

	static class ViewHolder {
		@ViewInject(R.id.tv_name)
		TextView tv_name;
		@ViewInject(R.id.tv_enname)
		TextView tv_enname;
		@ViewInject(R.id.iv_bg)
		ImageView iv_bg;
		@ViewInject(R.id.ll_content)
		LinearLayout ll_content;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}

	public int getScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		return width;
	}

}
