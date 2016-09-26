package cc.ruit.shunjianmei.constants;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.lidroid.xutils.util.LogUtils;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * 接口常量
 *
 * @author tianjm
 */
public class Constant {
    /**
     * 统一根地址
     */
    public static final String NET = "http://101.251.196.90:8000/shunjianmeiweb";
//    public static final String NET = "http://192.168.1.244:8080/shunjianmeiweb";
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

    // add by jiazhaohui
    // 静态图片路径
    public static final String IMAGESURL = NET + "/images/";

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
     * 支付宝相关参数
     */
    public static final String PARTNER = "2088021882749328";// 商户PID
    public static final String SELLER = "2088021882749328";// 商户收款账号
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK6YRZtXs/bHAL/0J314Qcsg1uOC2Eu9PSzn6lZpCVoLzDBoWJvt+e8g8fVtzn5wcgmya3dIOwDAvbQhCAHE9aOM8uPMKmrBdP/L5Ki8FWVlbFarjVcO7enTnr7g1SRqbq2Q/uB0hQ30WaFZ6Kt55cG++8jMoiY/wBOxnXVBvNIlAgMBAAECgYB4DE9gHo+pUO/dDH3SsctbZfsbeC5M9ZOIuvXP8Gzzs0T0y+qRXLILkDWOMbBYE+z1ae6ml82qGNzXbBbbUJgsd8nLhZaEuENq2G8QtJSTMxVd1DiRPhdF+DmwoDeszT+US5gAcnWaDbXEv/vLQmcSSS479IxNEhRIOA6o1gAXCQJBANbrieGfoEoZ+nBxn+LVYpoIlDK5SkXMRJTL+TQQT598OdBAyTiaChEGRybCfMMo/ryjygPDO/QnX+zXcOXI56cCQQDP94oBErEFoZuFF5AgykFPksstfsTWQ1tH5GmnUDdJJ8b2YaX2Ya2kW5NupkWhTnc47KacMYUf8meMskmM/3FTAkEApc7n/DKLlj++j/uJT5Yy0QVnWE7NN1YlXuqSoxEmbLOJYtpoo4tr0sCWKhgH6wT4qo5ub0KzQWUTQ4Te69h4pwJATFvIWRKC2t12m8yGB/QbPNtaLI8yOLWsi14LiPpEAmFPhWFfLXK57fIN1P/J6ytDTSxfFFR+W7eGW7uOk+2qrwJAaoANaTDAlebavZjCW93sKD9pAwzC3fN554N5fOtjylq+sEcAGW5OpL2o8Xh/i+Px4rLDk03+C4bCyab5pMwZUg==";// 商户私钥，pkcs8格式
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";// 支付宝公钥
    public static final String notify_url = NET + "/notify_url";// 支付回调地址
    public static final String additionalNotifyUrl = NET + "/additionalNotifyUrl";// 加单支付回调地址
    // add by jiazhaohui
    public static final String quickpayNotifyUrl = NET + "/quickpayNotifyUrl";

    /**
     * 微信支付相关参数
     */
    // 微信分配的公众账号ID,请同时修改 androidmanifest.xml里面的<data
    // android:scheme=""/>为新设置的appid
    public static final String APP_ID = "wxe99c393389decb57";

    public static final String MCH_ID = "1279859301"; // 微信支付分配的商户号
    public static final String API_KEY = "7ce9b9a25197308edf37e3248368046d";// API密钥，在商户平台设置
    public static final int COMMENT_OK = 7;// 评论完成
    public static final int ORDER_OK = 8; // 订单完成

    // add by jiazhaohui
    public static final String SERVICE_TF_CODE = "tf"; // 烫发编码
    public static final String SERVICE_RF_CODE = "rf"; // 染发编码
    public static final String SERVICE_HL_CODE = "hl"; // 护理编码

    /**
     * create the order info. 创建订单信息
     */
    public static String getOrderInfo(String orderID, String subject, String body, String price, String url) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Constant.PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Constant.SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderID + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + url + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        LogUtils.i(orderInfo);
        return orderInfo;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public static final String SCHEDULE_STATE_OVERDUE = "-2"; //过期
    public static final String SCHEDULE_STATE_REST = "-1"; //休息
    public static final String SCHEDULE_STATE_CAN_ORDER = "0"; //可约
    public static final String SCHEDULE_STATE_LOCK = "1"; //锁定
    public static final String SCHEDULE_STATE_YET_ORDER = "2"; //已预约
}
