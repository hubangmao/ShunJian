package cc.ruit.shunjianmei.base;
/**
 * 
 * @ClassName: BaseRequest
 * @Description: TODO http请求时参数格式统一，所以写此基类做统一参数的管理
 * @author: lee
 * @date: 2015年7月22日 上午10:45:51
 */
public class BaseRequest {
	public String Method;//加密内容
	public String Infversion;//接口版本号
	public String Key;//加密后的内容
	public String UID;//密钥，内容为随机数
	public BaseRequest(String method, String infversion) {
		super();
		Method = method;
		Infversion = infversion;
	}

	public void setUid(String UID, String Key){
		this.UID = UID;
		this.Key = Key;
	}

	@Override
	public String toString() {
		return "BaseRequest [Method=" + Method + ", Infversion=" + Infversion
				+ ", Key=" + Key + ", UID=" + UID + "]";
	}
	
}
