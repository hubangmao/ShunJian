package cc.ruit.utils.sdk.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
/**
 * @ClassName: UUIDUtil 
 * @Description: UUID的相关操作工具类 
 * @author liliwei 
 * @create 2013-12-2 上午10:38:42
 */
public class UUIDUtil {

    private static final String TAG = UUIDUtil.class.getSimpleName();

    private static String uuid = null;

    private static String path;
    
    public static final String UUID_KEY = "uuid_key";
    public static final String UUID_VERSION = "uuid_version";
    public static void init(Context context) {
//		// TODO Auto-generated method stub
//         readFromFile();
	}
    static {
    	path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/.android/system/";
    }
    public static void saveInFile(String uuid) {
        if (Environment.getExternalStorageState() != null)
            try {
                byte[] msgs = uuid.getBytes();

                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream out = new FileOutputStream(new File(path + "uuid"));
                out.write(msgs);
                out.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
    }

    public static void readFromFile() {
        try {
            File file = new File(path + "uuid");
            if (file.exists()) {
                FileInputStream in = new FileInputStream(file);
                int len = -1;
                byte[] buffer = new byte[1024];
                StringBuffer sb = new StringBuffer();
                while ((len = in.read(buffer)) != -1) {
                    String msg = new String(buffer, 0, len);
                    sb.append(msg);
                }
                uuid = sb.toString();
                in.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
//    
//
//    public static void readFromPublicFile() {
//    	try {
//    		File file = new File("/data/local/tmp/" + "jdpush.jd");
//    		if (file.exists()) {
//    			FileInputStream in = new FileInputStream(file);
//    			int len = -1;
//    			byte[] buffer = new byte[1024];
//    			StringBuffer sb = new StringBuffer();
//    			while ((len = in.read(buffer)) != -1) {
//    				String msg = new String(buffer, 0, len);
//    				sb.append(msg);
//    			}
//    			uuid = sb.toString();
//    			in.close();
//    		}
//    	} catch (FileNotFoundException e) {
//    	} catch (IOException e) {
//    	}
//    }
    public static void saveInSP(Context context, String uuid, long uuidVersion) {
        if (TextUtils.isEmpty(uuid) || uuidVersion == 0) {
            return;
        }
        SharedPreferencesUtil.putString(context, UUID_KEY, uuid);
        SharedPreferencesUtil.putLong(context, UUID_VERSION, uuidVersion);
    }
//    public static void syncUUID(Context context) {
//    	if (null!=CommonUtil.getDeviceID(context)) {
//			return;
//		}
//        String uuid = SPUtils.getString(JD_CLOUD_UUID_KEY,
//                getUUID(context));
//        long uuidVersion = SPUtils.getLong(JD_CLOUD_UUID_VERSION,
//                new Date().getTime());
//        Log.d(TAG, TAG+" uuid = "+uuid+" uuidVersion = "+uuidVersion);
//        syncUUID(context, uuid, uuidVersion);
//    }

//    public static void syncUUID(Context context, String uuid, long uuidVersion) {
//        if (TextUtils.isEmpty(uuid)) {
//            return;
//        }
//        if (null!=CommonUtil.getDeviceID(context)) {
//			return;
//		}
//        Intent in = new Intent(Constants.DataKey.JD_CLOUD_SYNCSDK);
//        in.putExtra("uuid", uuid);
//        in.putExtra("uuidVersion", uuidVersion);
//        context.sendBroadcast(in);
//        Log.d(TAG, "syncsdk send uuidVersion = " + uuidVersion + " uuid = " + uuid);
//    }

//    public static void uuidSyncControl(Context context, Intent intent) {
//    	if (null!=CommonUtil.getDeviceID(context)) {
//			return;
//		}
//        String uuid = intent.getStringExtra("uuid");
//        long uuidVersion = intent.getLongExtra("uuidVersion", 0);
//        // 如果UUID是空或者版本为零，证明发送来的是错误的，不进行同步
//        if (TextUtils.isEmpty(uuid) || uuidVersion == 0) {
//            return;
//        }
//        String olduuid = SPUtils.getString(JD_CLOUD_UUID_KEY,
//                getUUID(context));
//        long oldUuidVersion = SPUtils.getLong(JD_CLOUD_UUID_VERSION,
//                new Date().getTime());
//
//        if (uuidVersion < oldUuidVersion) {// 如果接受到的uuid版本小于当前sdk保存的版本，则替换当前保存
//            if ("JDPushnull".equals(uuid)) {
//                // 如果uuid为JDPushnull证明为1.3.0的bug，设置一个版本时间为2005.10.10的版本同步；
//            	Log.e(TAG, "uuid is JDPushnull syncuuid uuid：other uuidVersion = " + uuidVersion + "other uuid = " + uuid);
//            	Date date = CommonUtil.getDateFromString("yyyy-MM-dd", "2005-10-10");
//            	if (date!=null) {
//            		oldUuidVersion = date.getTime();
////            		syncUUID(context, olduuid, oldUuidVersion);
//				}
//            } else {
//                Log.d(TAG, "save uuid：other uuidVersion = " + uuidVersion + "other uuid = " + uuid);
//                saveInSP(context, uuid, uuidVersion);
//                saveInFile(uuid);
//                
//            }
//        } else if (uuidVersion > oldUuidVersion) {// 如果收到的版本大于当前保存的版本，则将当前的版本发送广播通知其他应用；
//            if (uuid != null && !olduuid.equals(uuid)) {
//                // 发送uuid给其他应用
////                syncUUID(context, olduuid, oldUuidVersion);
//            }
//        }
//    }

   
    /**
     * 获取UUID
     * 
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
    	//获取设备码
        String uuid = getDeviceID(context);
        // 1、如果为空先从SharedPreference中取UUID
        if (TextUtils.isEmpty(uuid)||TextUtils.isEmpty(uuid.replaceAll("0", ""))) {
        	uuid = getUUIDFromSP(context);
		}
        // 2.如果为空从SDCard中取UUID
        if (TextUtils.isEmpty(uuid)) {
            uuid = getUUIDFromSDCard();
        }
        // 3.依然为空时生成UUID
        if (TextUtils.isEmpty(uuid)) {
            uuid = createUUID(context);
        }
        //如果所得UUID和原有存储的UUID不同时，保存新的UUID；
        if (uuid!=null&&!uuid.equals(getUUIDFromSP(context))) {
            UUIDUtil.saveInFile(uuid);
            saveInSP(context, uuid, new Date().getTime());
        }
        Log.d(TAG, "uuid =="+uuid);
        return uuid;
    }
    /**
	 * @Description: 获取设备ID
	 * @author liliwei
	 * @create 2013-12-2 上午10:10:20
	 * @updateTime 2013-12-2 上午10:10:20
	 * @param context
	 * @return
	 */
	public static String getDeviceID(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
    //从SharedPreference中取出UUID
    private static String getUUIDFromSP(Context context){
        String deviceID = CommonUtil.getDeviceID(context);
        String uuid = SharedPreferencesUtil.getString( context, UUID_KEY,deviceID);
        if (!TextUtils.isEmpty(uuid)) {
            if ("JDPushnull".equals(uuid)) {
                SharedPreferencesUtil.putString(context, UUID_KEY, "");
                getUUIDFromSP(context);
            }
        }
        return uuid;
    }
    //从SDCard中取出UUID
    private static String getUUIDFromSDCard(){
    	readFromFile();
        if (!TextUtils.isEmpty(uuid)) {
            if ("JDPushnull".equals(uuid)) {
                UUIDUtil.saveInFile("");
                getUUIDFromSDCard();
            }
        }
        return uuid;
    }
//    public static String getUUIDFromPublicFile(){
//    	readFromPublicFile();
//    	if (!TextUtils.isEmpty(uuid)) {
//    		if ("JDPushnull".equals(uuid)) {
//    			UUIDUtil.saveInFile("");
//    			getUUIDFromPublicFile();
//    		}
//    	}
//    	return uuid;
//    }
    //按照规则生成UUID
    private static String createUUID(Context context){
        String packageName = CommonUtil.getPackageName(context);
        String ip = CommonUtil.getLocalIpAddress();
        //生成UUID，规则为：UUID工具类随机出UUID+包名+ip所得字符串进行MD5编码；
        String uuid = MD5.getMD5Str(UUID.randomUUID().toString() + packageName + ip);
        return uuid;
    }
}
