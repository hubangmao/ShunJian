package cc.ruit.shunjianmei.net.api;

import java.io.UnsupportedEncodingException;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.shunjianmei.net.request.CheckVersionRequest;
import cc.ruit.shunjianmei.net.request.CommonSendVCodeRequest;
import cc.ruit.shunjianmei.net.request.FindPassWordRequest;
import cc.ruit.shunjianmei.net.request.HomeInfoRequest;
import cc.ruit.shunjianmei.net.request.IncomeDetailsRequest;
import cc.ruit.shunjianmei.net.request.MyInfoRequest;
import cc.ruit.shunjianmei.net.request.UserLoginRequest;
import cc.ruit.shunjianmei.net.request.UserRegisterRequest;
import cc.ruit.shunjianmei.net.request.WxAuthoriorLoginRequest;
import cc.ruit.utils.sdk.http.HttpHelper;

import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @ClassName: UserInfoApi
 * @Description: 用户信息相关结构
 * @author: lee
 * @date: 2015年9月5日 下午5:35:41
 */
public class UserInfoApi {
    /**
     * @param request
     * @param callBack
     * @Title: WxAuthoriorLogin
     * @Description: 用户登录
     * @return: void
     */
    public static void WxAuthoriorLogin(WxAuthoriorLoginRequest request,
                                        RequestCallBack<String> callBack) {
        try {
            String json = request.toJsonString(request);
            HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param callBack
     * @Title: UserLogin
     * @Description: 用户登录
     * @return: void
     */
    public static void userLogin(UserLoginRequest request,
                                 RequestCallBack<String> callBack) {

        try {
            String json = request.toJsonString(request);
            HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param callBack
     * @Title: commonSendVCode
     * @Description: 获取验证码
     * @return: void
     */
    public static void commonSendVCode(CommonSendVCodeRequest request, RequestCallBack<String> callBack) {
        try {
            String json = request.toJsonString(request);
            HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param request
     * @param callBack
     * @Title: userRegister
     * @Description: 用户注册
     * @return: void
     */
    public static void UserRegister(UserRegisterRequest request, RequestCallBack<String> callBack) {
        try {
            String json = request.toJsonString(request);
            HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param callBack
     * @Title: CheckVersion
     * @Description: 检测版本
     * @return: void
     */
    public static void checkVersion(CheckVersionRequest request,
                                    RequestCallBack<String> callBack) {

        try {
            String json = request.toJsonString(request);
            HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param callBack
     * @Title: AppIndexInfo
     * @Description: 主页信息
     * @author: lee
     * @return: void
     */
    public static void AppIndexInfo(HomeInfoRequest request,
                                    RequestCallBack<String> callBack) {
        try {
            String json = request.toJsonString(request);
            HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param callBack
     * @Title: AppIndexInfo
     * @Description: 主页信息
     * @author: lee
     * @return: void
     */
    public static void MyInfo(MyInfoRequest request,
                              RequestCallBack<String> callBack) {
        try {
            String json = request.toJsonString(request);
            HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param callBack
     * @Title: IncomeDetails
     * @Description: 收入明细
     * @author: lee
     * @return: void
     */
    public static void IncomeDetails(IncomeDetailsRequest request,
                                     RequestCallBack<String> callBack) {
        try {
            String json = request.toJsonString(request);
            HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param callBack
     * @Title: findPassWord
     * @Description: 找回密码
     * @return: void
     */
    public static void findPassWord(FindPassWordRequest request,
                                    RequestCallBack<String> callBack) {

        try {
            String json = request.toJsonString(request);
            HttpHelper.postJSON(Constant.HOSTURL, json, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
