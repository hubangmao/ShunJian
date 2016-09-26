package cc.ruit.shunjianmei.home.hairdressert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.ruit.shunjianmei.R;
import cc.ruit.shunjianmei.base.BaseFragment;
import cc.ruit.shunjianmei.net.response.HairdresserDetailResponse;
import cc.ruit.shunjianmei.util.ForLargeImageActivity;
import cc.ruit.shunjianmei.util.FragmentManagerUtils;
import cc.ruit.shunjianmei.util.ImageLoaderUtils;
import cc.ruit.shunjianmei.util.MyEventBus;
import cc.ruit.shunjianmei.util.RoundImageLoaderUtil;
import cc.ruit.utils.sdk.date.DateUtil;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: HairDressertDetailsIntroFragment
 * @Description: 展示发型详情简介界面
 * @author: 欧阳
 * @date: 2015年10月23日 下午9:18:32
 */
public class HairDressertDetailsIntroFragment extends BaseFragment implements
		OnClickListener {
	private final String TAG = HairDressertDetailsIntroFragment.class.getSimpleName();
	@ViewInject(R.id.tv_nationality)
	TextView tv_nationality;// 国籍语言
	@ViewInject(R.id.tv_years)
	TextView tv_years;// 擅长发型
	@ViewInject(R.id.tv_good_at_content)
	TextView tv_good;// 擅长发型
	@ViewInject(R.id.tv_preson_hobby_content)
	TextView tv_hobby;// 爱好
	@ViewInject(R.id.tv_preson_intro_content)
	TextView tv_intro;// 简介
	@ViewInject(R.id.rl_hairdessert_Comment)
	RelativeLayout LookComment;// 查看更多评论
	@ViewInject(R.id.tv_commentName)
	TextView tv_commentName;// 评论姓名
	@ViewInject(R.id.tv_hairdessert_Comment_num)
	TextView tv_CommentNum;// 评论数
	@ViewInject(R.id.tv_commmentTime)
	TextView tv_commmentTime;// 评论时间
	@ViewInject(R.id.tv_commentcontent)
	TextView tv_commentcontent;// 评论内容
	@ViewInject(R.id.image_comment_style1)
	ImageView style1;// 发型1
	@ViewInject(R.id.image_comment_style2)
	ImageView style2;// 发型2
	@ViewInject(R.id.image_comment_style3)
	ImageView style3;// 发型3
	@ViewInject(R.id.image_commentPhoto)
	ImageView tv_commentPhoto;// 评论时间
	@ViewInject(R.id.ratingbar_commentScore)
	RatingBar tv_commentScore;// 评论分数
	@ViewInject(R.id.btn_intro_appointment)
	Button intro_appointment;
	@ViewInject(R.id.rl_hairdessert_comment)
	RelativeLayout rl_hairdessert_comment;
	@ViewInject(R.id.tv_empty_comment)
	TextView emptyComment;
	String ID;// 发型师ID
	String name;
	List<HairdresserDetailResponse> templist;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.hair_dressert_details_intro_fragment,
				null);
		ViewUtils.inject(this, view);
		LookComment.setOnClickListener(this);
		//intro_appointment.setOnClickListener(this);
		initData();
		return view;
	}
	HairDressertDetailsIntroFragment(List<HairdresserDetailResponse> templist){
		this.templist = templist;
		EventBus.getDefault().register(this);
	}
	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	/**
	 * 
	 * @Title: onEventMainThread
	 * @Description: 接收EventBus消息
	 * @author: Johnny
	 * @param event
	 * @return: void
	 */
	public void onEventMainThread(MyEventBus event){
		if(event.getmMsg().equals("HairdresserDetail")){
			this.templist= event.getTemplist();
			initData();
		}
	}
	int CommentNum=-1;//评论数
	/**
	 * 
	 * @Title: initData
	 * @Description: 初始化数据
	 * @author: 欧阳
	 * @return: void
	 */
	private void initData() {
		if (templist==null||templist.size()<0) {
			return;
		}
		final HairdresserDetailResponse Response = templist.get(0);
		if (Response.getIntro()!= null) {
			ID = Response.getUserID();
			name = Response.getName();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < Response.getIntro().Hair.size(); i++) {
				sb.append(Response.getIntro().Hair.get(i).Name + " ");
			}
			StringBuffer sb1 = new StringBuffer();
			for (int i = 0; i < Response.getIntro().Hobbies.size(); i++) {
				sb1.append(Response.getIntro().Hobbies.get(i).Name + " ");
			}
			tv_years.setText(Response.getWorkingLife());
			tv_good.setText(sb.toString());
			tv_hobby.setText(sb1.toString());
			tv_intro.setText(Response.getIntro().Content.toString());
			// add by jiazhaohui
			// 国籍和语言
			String nationality = Response.getNationality();
			if (nationality == null || nationality.equals(""))
			{
				nationality = "中国大陆";
			}
			String language = Response.getLanguage();
			if (language == null || language.equals(""))
			{
				language = "普通话";
			}
			String txt = nationality + "/" + language;
			tv_nationality.setText(txt);

		} 
		if (Response.getComment()!= null) {
			tv_commentScore.setVisibility(View.VISIBLE);
			tv_commentName.setText(Response.getComment().Name);
			RoundImageLoaderUtil.setErrImage(R.drawable.default_prc,
					R.drawable.default_prc, R.drawable.default_prc);
			RoundImageLoaderUtil.getInstance(activity, 360).loadImage(
					Response.getComment().Photo, tv_commentPhoto);
//			tv_commmentTime.setText((Response.getComment().Time).substring(0, Response.getComment().Time.length()-3));
			if (!TextUtils.isEmpty(Response.getComment().Time)) {
				try {
					Date date = DateUtil.parseStringToDate(Response.getComment().Time);
					SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd hh:mm");
					String format = sd.format(date);
					tv_commmentTime.setText(format);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			
			if (!TextUtils.isEmpty(Response.getComment().Score)) {
				Float Score = Float.valueOf(Response.getComment().Score);
				tv_commentScore.setRating(Score);
			} else {
				tv_commentScore.setRating(0);	
			}
			tv_commentcontent.setText(Response.getComment().Content);
			ImageLoaderUtils.setErrImage(R.drawable.default_prc, R.drawable.default_prc, R.drawable.default_prc);
			if (Response.getComment().Item != null && Response.getComment().Item.size() > 0) {
				ImageView[] image = { style1, style2, style3 };
				for (int i = 0; i < Response.getComment().Item.size(); i++) {
					image[i].setVisibility(View.VISIBLE);
					Log.i("main",TAG+"评论图片链接="+Response.getComment().Item.get(i).Photo);
					ImageLoaderUtils.getInstance(activity).loadImage(
							Response.getComment().Item.get(i).Photo, image[i]);
					
					final int index = i;
					image[i].setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ArrayList<String> images = new ArrayList<String>();
							for (int j = 0; j < Response.getComment().Item.size(); j++) {
								images.add(Response.getComment().Item.get(j).Photo);
							}
							startActivity(ForLargeImageActivity.getIntent(activity, index, images));
						}
					});
					
				}
			}
		} 
		try {
			CommentNum=Integer.parseInt(Response.getCommentNum());
		} catch (Exception e) {
		}
		if (Response.getCommentNum() != null) {
			tv_CommentNum.setText("("+Response.getCommentNum()+")");
			if (CommentNum==0) {
				rl_hairdessert_comment.setVisibility(View.INVISIBLE);
				emptyComment.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_hairdessert_Comment:// 打开评论
			if (CommentNum>0) {
				Fragment fragment = FragmentManagerUtils.getFragment(activity,
						HairDressertCommentFragment.class.getName());
				Bundle bundle = new Bundle();
				bundle.putString("ID", ID);
				bundle.putString("Name", name);
				fragment.setArguments(bundle);
				FragmentManagerUtils.add(activity, R.id.content_frame, fragment,
						true);
			}
			break;
//		case R.id.btn_intro_appointment:
//			Fragment fragment = FragmentManagerUtils.getFragment(activity, ReserveFragment.class.getName());
//			Bundle bundle=new Bundle();
//			bundle.putString("ID", ID);
//			bundle.putString("From", HairDressertDetailsIntroFragment.class.getSimpleName());
//			fragment.setArguments(bundle);
//			FragmentManagerUtils.add(activity, R.id.content_frame, new ReserveFragment(), true);
//			break;
		default:
			break;
		}
	}

}
