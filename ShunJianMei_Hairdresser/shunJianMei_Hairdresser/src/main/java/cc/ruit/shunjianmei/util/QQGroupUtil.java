package cc.ruit.shunjianmei.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/**
 * @ClassName: QQGroupUtil
 * @Description: TODO
 * @author: lee
 * @date: 2015年8月30日 下午6:11:04
 */
public class QQGroupUtil {
	/****************
	*
	* 发起添加群流程。群号：呱呱赚官方QQ群 （479172601） 的 key 为： h_RP0aDUQPZYcc2_CWxT7JNjbJ2r4fFT
	* 调用 joinQQGroup(h_RP0aDUQPZYcc2_CWxT7JNjbJ2r4fFT) 即可发起手Q客户端申请加群 呱呱赚官方QQ群 （479172601）
	*
	* @param key 由官网生成的key
	* @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	******************/
	public boolean joinQQGroup(Context context,String key) {
//		key = "9akIcm8EsiJHL7lTnbc8VJ5naaJyNFew";
	    Intent intent = new Intent();
	    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	    try {
	    	context.startActivity(intent);
	        return true;
	    } catch (Exception e) {
	        // 未安装手Q或安装的版本不支持
	        return false;
	    }
	}
}
