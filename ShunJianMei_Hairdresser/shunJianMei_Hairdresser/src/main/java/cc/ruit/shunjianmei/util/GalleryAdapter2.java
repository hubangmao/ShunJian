package cc.ruit.shunjianmei.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView.ScaleType;
import cc.ruit.shunjianmei.util.view.MyImageView;
import cc.ruit.shunjianmeihairdresser.R;

/**
 * 图片浏览适配器
 * 
 * @author Administrator
 * 
 */
public class GalleryAdapter2 extends BaseAdapter {
	
	private Context context;

	private ArrayList<MyImageView> imageViews = new ArrayList<MyImageView>();

	// private ImageCacheManager imageCache;
	private List<String> detailImageList;

	private LayoutInflater inflater;

	// private List<String> mItems;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bitmap bitmap = (Bitmap) msg.obj;
			Bundle bundle = msg.getData();
			String url = bundle.getString("url");
			for (int i = 0; i < imageViews.size(); i++) {
				if (imageViews.get(i).getTag().equals(url)) {
					imageViews.get(i).setImageBitmap(bitmap);
				}
			}
		}
	};

	// public void setData(List<String> data) {
	// this.mItems = data;
	// notifyDataSetChanged();
	// }

	public GalleryAdapter2(Context context, List<String> detailImageList) {
		this.context = context;
		this.detailImageList = null;
		this.detailImageList = detailImageList;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// imageCache = ImageCacheManager.getInstance(context);
	}

	@Override
	public int getCount() {
		return detailImageList != null ? detailImageList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return detailImageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = new MyImageView(context);
			holder = new ViewHolder();
			holder.miv_pic = (MyImageView) convertView;
			holder.miv_pic.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			holder.miv_pic.setScaleType(ScaleType.FIT_CENTER);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
		ImageLoaderUtils.getInstance(context).loadImage(
				detailImageList.get(position), holder.miv_pic);
		if (!this.imageViews.contains(holder.miv_pic)) {
			imageViews.add(holder.miv_pic);
		}
		return convertView;
	}

	class ViewHolder {
		MyImageView miv_pic;
	}

}
