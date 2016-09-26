package cc.ruit.shunjianmei.util.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cc.ruit.shunjianmeihairdresser.R;
/**
 * @ClassName: EmptyView
 * @Description: 空页面
 * @author: lee
 * @date: 2015年9月15日 下午4:34:36
 */
public class EmptyView {

	private Context context;
	private View view;
	private TextView tv_msg;
	private ImageView iv_null;
	private ProgressBar pb_loading;

	public EmptyView(Context context,OnClickListener listener) {
		super();
		this.context = context;
		initView();
		view.setOnClickListener(listener);
	}

	private void initView() {
		view = LayoutInflater.from(context).inflate(R.layout.empty_view, null);
		tv_msg = (TextView) view.findViewById(R.id.tv_null);
		iv_null = (ImageView) view.findViewById(R.id.iv_null);
		pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
	}
	/**
	 * @Title: getView
	 * @Description: 获取view
	 * @author: lee
	 * @return
	 * @return: View
	 */
	public View getView(){
		return view;
	}
	/**
	 * @Title: setMessage
	 * @Description: 设置消息
	 * @author: lee
	 * @param msg
	 * @return: void
	 */
	public void setMessage(String msg){
		if (tv_msg!=null) {
			tv_msg.setText(msg);
		}
	}
	/**
	 * @Title: setMsgIcon
	 * @Description: 设置icon
	 * @author: lee
	 * @param iconRid
	 * @return: void
	 */
	public void setMsgIcon(int iconRid){
		if (iv_null!=null) {
			iv_null.setImageResource(iconRid);
		}
	}
	/**
	 * @Title: setNullState
	 * @Description: 空数据页
	 * @author: lee
	 * @return: void
	 */
	public void setNullState(){
		setVisible(true);
		iv_null.setVisibility(View.VISIBLE);
		pb_loading.setVisibility(View.GONE);
		setMessage("喔噢~当前没有数据");
	}
	/**
	 * @Title: setErrState
	 * @Description: 错误页
	 * @author: lee
	 * @return: void
	 */
	public void setErrState(){
		setVisible(true);
		iv_null.setVisibility(View.VISIBLE);
		pb_loading.setVisibility(View.GONE);
		setMessage("点击屏幕,重新加载");
	}
	/**
	 * @Title: setLoadingState
	 * @Description: 加载页
	 * @author: lee
	 * @return: void
	 */
	public void setLoadingState(){
		setVisible(true);
		iv_null.setVisibility(View.GONE);
		pb_loading.setVisibility(View.VISIBLE);
		setMessage("正在努力加载...");
	}
	/**
	 * @Title: setLoadingState
	 * @Description: 加载页
	 * @author: lee
	 * @return: void
	 */
	public void setDataState(){
		setVisible(false);
	}
	/**
	 * @Title: setVisible
	 * @Description: 设置空载页是否出现
	 * @author: lee
	 * @param isVisible
	 * @return: void
	 */
	public void setVisible(boolean isVisible){
		if (view!=null) {
			view.setVisibility(isVisible?View.VISIBLE:View.GONE);
		}
	}
	/**
	 * @Title: setState
	 * @Description: 设置空载页状态
	 * @author: lee
	 * @param state
	 * @return: void
	 */
	public void setState(State state){
		if (state==State.Null) {
			setNullState();
		}else if (state==State.Err) {
			setErrState();
		}else if (state==State.Loading) {
			setLoadingState();
		}
	}
	public enum State{
		Null,Err,Loading
	}
}
