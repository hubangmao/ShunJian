package cc.ruit.shunjianmei.constants;

/**
 * 接口常量
 * 
 * @author tianjm
 *
 */
public class Constant {
	/**
	 * 统一根地址
	 */
	public static final String NET = "http://101.251.196.90:8000/shunjianmeiweb";
//	public static final String NET = "http://10.2.11.233:8080/shunjianmeiweb/"; // 测试
	// public static final String NET = "http://shunjianonline.com";
	public static final String NET_WEB = NET;

	/**
	 * 统一接口调用地址
	 */
	public static final String HOSTURL = NET + "/appInterface";
	/**
	 * 接口统一文件上传地址
	 */
	public static final String FILEURL = NET + "/UploadImage";

	/**
	 * 初始化分享图片的默认路径
	 */
	public static String TEST_IMAGE = null;
	/**
	 * 关于我们页面
	 */
	public static final String ABOUTME_NET = NET_WEB + "/html/about/index.html";
	/**
	 * 规则说明
	 */
	public static final String HOME_RULEDESC = NET_WEB + "/app-h5/user/ruleapp";
	/**
	 * 使用协议
	 */
	public static final String HOME_USEDOC = NET_WEB + "/app-h5/user/term";
	/**
	 * 价目表
	 */
	public static final String ME_PRICELIST = NET_WEB + "/pricelist";
	/**
	 * 接单流程
	 */
	public static final String ME_JIEDAN = NET_WEB + "/goJiedanliucheng";
	public static final String serverPhone = "13439553792";
	
	// add by jiazhaohui
	public static final String ORDER_LIST_ALL_METHOD = "H_OrderList";			// 选择所有订单
	public static final String ORDER_LIST_FINISH_MEHTOD = "H_FinishOrderList";		// 选择已经完结的订
	
	public static final String SCHEDULE_STATE_OVERDUE = "-2"; //过期
	public static final String SCHEDULE_STATE_REST = "-1"; //休息
	public static final String SCHEDULE_STATE_CAN_ORDER = "0"; //可约
	public static final String SCHEDULE_STATE_LOCK = "1"; //锁定
	public static final String SCHEDULE_STATE_YET_ORDER = "2"; //已预约
	
}
