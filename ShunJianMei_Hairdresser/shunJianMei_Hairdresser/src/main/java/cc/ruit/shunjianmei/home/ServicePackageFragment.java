package cc.ruit.shunjianmei.home;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.oruit.widget.title.TitleUtil;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.base.BaseResponse;
import cc.ruit.shunjianmei.net.api.GetAllusersubinfoPackedBysubid;
import cc.ruit.shunjianmei.net.request.GetAllusersubinfoPackedBysubidRequest;
import cc.ruit.shunjianmei.net.response.GetAllusersubinfoPackedBysubidResponse;
import cc.ruit.shunjianmei.usermanager.UserManager;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmeihairdresser.R;
import cc.ruit.utils.sdk.ToastUtils;

public class ServicePackageFragment extends BaseFragment {

	@ViewInject(R.id.hair_lv)
	private ListView lv;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.servicepackagefragment, null);
		ViewUtils.inject(this, view);
		initTitle();
		initData();
		return view;
	}

	/**
	 * @Title: initTitle
	 * @Description: 标题初始化
	 * @author: Johnny
	 * @return: void
	 */
	private void initTitle() {
		TitleUtil title = new TitleUtil(view);
		title.iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isBack = FragmentManagerUtils.back(getActivity(), R.id.content_frame);
				if (!isBack) {
					getActivity().finish();
				}
			}
		});
		title.iv_left.setImageResource(R.drawable.back);
		title.iv_left.setVisibility(View.VISIBLE);

		title.tv_title.setText("服务套餐");
	}

	private void initData() {

		int id = UserManager.getUserID();
		String uid = "" + id;
		GetAllusersubinfoPackedBysubidRequest mGetAllusersubinfoPackedBysubidRequest = new GetAllusersubinfoPackedBysubidRequest(
				uid);
		GetAllusersubinfoPackedBysubid.canGetAllusersubinfoPackedBysubid(mGetAllusersubinfoPackedBysubidRequest,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub

						BaseResponse result = BaseResponse.getBaseResponse(responseInfo.result);
						if (result == null) {
							return;
						}
						String[] split = result.getMsg().split("\\|");
						if ("1".equals(split[0])) {
							ToastUtils.showShort(split[1] + "");
						}
						if (result.getCode() == 1000) {
							List<GetAllusersubinfoPackedBysubidResponse> alllist = GetAllusersubinfoPackedBysubidResponse
									.getclazz2(result.getData());

							if (alllist.size() != 0) {
								lv.setAdapter(
										new HairStyleAdapters(ServicePackageFragment.this.getActivity(), alllist));
							} else {
								ToastUtils.showShort("没有数据");
							}

						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						ToastUtils.showShort("请求数据失败");
					}
				});

	}

	/**
	 * 
	 * @ClassName: HairStyleAdapter
	 * @Description: dialog内发型ListView的adapter
	 * @author: Johnny
	 * @date: 2015年10月24日 上午11:03:43
	 */
	class HairStyleAdapters extends BaseAdapter {

		private Context context;
		private List<GetAllusersubinfoPackedBysubidResponse> list;

		public HairStyleAdapters(Context context, List<GetAllusersubinfoPackedBysubidResponse> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public GetAllusersubinfoPackedBysubidResponse getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getCount() {
			// return 5;
			return list == null ? 0 : list.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.reserve_listview_item_free_dialog, null);
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
		 * @Title: setLable
		 * @Description: 绑定数据
		 * @author: Johnny
		 * @param position
		 * @param vh
		 * @return: void
		 */
		private void setLabel(int position, ViewHolder vh) {
			GetAllusersubinfoPackedBysubidResponse obj = getItem(position);

			String Longhair = "<font color='#333333'>长发  :</font><font color='#f34c28'>" + " ￥" + obj.getLonghair()
					+ "</font>";
			vh.tv_long_hair_price.setText(Html.fromHtml(Longhair));
			String Inhair = "<font color='#333333'>中发  :</font><font color='#f34c28'>" + " ￥" + obj.getInhair()
					+ "</font>";
			vh.tv_in_hair_price.setText(Html.fromHtml(Inhair));
			String Shorthair = "<font color='#333333'>短发  :</font><font color='#f34c28'>" + " ￥" + obj.getShorthair()
					+ "</font>";
			vh.tv_short_hair_price.setText(Html.fromHtml(Shorthair));

			vh.tv_left_reserve_dialog_name.setText(obj.getName());
			vh.tv_right_lv_reserve_dialog_time.setText(obj.getTimes() + "分钟");
			if (vh.tv_show_package_details__dialog_content.getLineCount() > 1) {
				vh.tv_show_package_details__dialog_content.setText(obj.getIntro() + ".........");
			} else {
				vh.tv_show_package_details__dialog_content.setText(obj.getIntro());
			}

		}

	}

	static class ViewHolder {

		@ViewInject(R.id.tv_left_reserve_dialog_name)
		TextView tv_left_reserve_dialog_name;
		@ViewInject(R.id.tv_right_lv_reserve_dialog_time)
		TextView tv_right_lv_reserve_dialog_time;
		@ViewInject(R.id.tv_show_package_details__dialog_content)
		TextView tv_show_package_details__dialog_content;
		@ViewInject(R.id.tv_long_hair_price)
		TextView tv_long_hair_price;
		@ViewInject(R.id.tv_in_hair_price)
		TextView tv_in_hair_price;
		@ViewInject(R.id.tv_short_hair_price)
		TextView tv_short_hair_price;

		void findView(View convertView) {
			ViewUtils.inject(this, convertView);
		}
	}
}
