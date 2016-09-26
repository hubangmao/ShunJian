package cc.ruit.shunjianmei.db;

import java.util.List;

import android.content.Context;
import cc.ruit.shunjianmei.net.response.MessageListResponse;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
/**
 * @ClassName: MessageDbUtil
 * @Description: 消息db工具类
 * @author: lee
 * @date: 2015年9月16日 下午5:38:10
 */
public class MessageDbUtil {
	
	public long getUnReadCount(Context ctx){
		try {
			long count = DbUtil.getDefaultDbUtils(ctx).count(Selector.from(MessageListResponse.class)
					.where("IsRead", "=", "0"));
			List<MessageListResponse> list = DbUtil.getDefaultDbUtils(ctx).findAll(MessageListResponse.class);
			LogUtils.i("count:"+count);
			return count;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
