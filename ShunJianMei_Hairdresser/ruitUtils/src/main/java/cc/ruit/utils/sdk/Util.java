package cc.ruit.utils.sdk;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera.Size;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import cc.ruit.utils.sdk.file.FileConstant;
import cc.ruit.utils.sdk.file.FileHelper;

/******************************************************************
 * 文件名称 : Util.java 文件描述 : 通用工具类
 ******************************************************************/
public class Util {

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < (listAdapter.getCount()); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			Log.e("", "-i--" + i + "--totalHeight--" + totalHeight);
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		Log.e("", "--params.height--" + params.height);
		listView.setLayoutParams(params);
	}

	/**
	 * 得到二个日期间的间隔天数
	 * 
	 * @param sj1
	 * @param sj2
	 * @return
	 */
	public static String getTwoDay(String sj1, String sj2) {
		// SimpleDateFormat myFormatter = new SimpleDateFormat(
		// "yyyy-MM-dd");
		long day = 0;
		try {
			Date date = new Date(Long.parseLong(sj1) * 1000L);
			Date mydate = new Date(Long.parseLong(sj2) * 1000L);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	/**
	 * 是否是同一天
	 * 
	 * @param dateTime
	 * @param dateTime
	 * @return
	 */
	public static boolean isTheSameDay(String now, String server_time) {
		if (server_time.length() == 10) {
			server_time = server_time + "000";
		}
		if (now.length() == 10) {
			now = now + "000";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String temp1 = df.format(new Date(Long.parseLong(now)));
		String temp2 = df.format(new Date(Long.parseLong(server_time)));
		return temp1.equals(temp2);
	}

	/**
	 * 获取两点之间距离 返回double 类型
	 * 
	 * @param lat1
	 *            位置1纬度
	 * @param lon1
	 *            位置1经度
	 * @param lat2
	 *            位置2纬度
	 * @param lon2
	 *            位置2经度
	 * @return
	 */
	public static double getDistance_double(double lat1, double lon1,
			double lat2, double lon2) {
		float[] results = new float[1];
		Location.distanceBetween(lat1, lon1, lat2, lon2, results);
		return results[0];
	}

	/**
	 * 震动
	 * 
	 * @param activity
	 * @param milliseconds
	 */
	public static void vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	
	/**
	 * 版本号转换
	 * 
	 * @param version
	 * @return
	 */
	public static int getVersion(String version) {
		String[] tempStrings = Util.split2(version, ".");
		version = "";
		for (int i = 0; i < tempStrings.length; i++) {
			version = version + tempStrings[i];
		}
		return Integer.parseInt(version);
	}

	public static boolean isCurrentWIFI(Context cx) {
		ConnectivityManager mag = (ConnectivityManager) cx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 此处输出当前可用网络
		NetworkInfo info = mag.getActiveNetworkInfo();
		if (info == null || info.getTypeName() == null) {
			return false;
		}

		if (info.getTypeName().equalsIgnoreCase("WIFI")) {
			// return WIFI;
			return true;
		}
		return false;
	}

	/**
	 * @param isDetail
	 * @param name
	 * @return
	 */
	public static List<String> exeLsNoRoot(boolean isDetail, String name) {
		List<String> list = new ArrayList<String>();
		try {
			Process p = null;

			if (!isDetail) {
				if (name == null) {
					p = Runtime.getRuntime().exec("ls -a \n");
				} else {
					p = Runtime.getRuntime().exec("ls -a " + name + "\n");
				}
			} else {
				if (name == null) {
					p = Runtime.getRuntime().exec("ls -l -a \n");
				} else {
					p = Runtime.getRuntime().exec("ls -l -a " + name + "\n");
				}
			}

			DataInputStream is = new DataInputStream(p.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is),
					8192);
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				list.add(line);
			}
			p.waitFor();
			return list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param data
	 * @return
	 */
	public static boolean isTime(String data) {
		if (data.contains("-") && (data.indexOf("-") != data.lastIndexOf("-"))) {
			String[] datas = data.split("-");
			int i = 0;
			int count = 0;
			int len = datas.length;
			for (; i < len; i++) {
				if (datas[i].equals("")) {
					break;
				}
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(datas[i]);
				if (isNum.matches()) {
					count++;
				}
				if (count == 3) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 该变量是从sd卡读取文件时默认的字符缓冲区的大小
	 */
	private final static int MAX_LENTH = 512;

	public static OnKeyListener onKeyListener = new OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					|| keyCode == KeyEvent.KEYCODE_SEARCH) {
				return true;
			}
			return false;
		}
	};

	// 检查network
	public static boolean isNetworkAvailable(Context cx) {
		ConnectivityManager cm = (ConnectivityManager) cx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		return (info != null && info.isConnected());
	}

	/**
	 * 解压zip格式的压缩包
	 * 
	 * @param inPath
	 *            输入路径
	 * @param outPath
	 *            输出路径
	 * @return 解压成功或失败标志
	 */
	public static Boolean openZip(String inPath, String outPath) {
		String unzipfile = inPath; // 解压缩的文件名
		try {
			ZipInputStream zin = new ZipInputStream(new FileInputStream(
					unzipfile));
			unzipfile = null;

			ZipEntry entry;
			// 创建文件夹
			while ((entry = zin.getNextEntry()) != null) {
				if (entry.isDirectory()) {
					File directory = new File(outPath, entry.getName());
					if (!directory.exists()) {
						if (!directory.mkdirs()) {
							return false;
						}
					}
					directory = null;
					zin.closeEntry();
				} else {
					// File myFile = new File(entry.getName());
					File myFile = FileHelper.createFile(outPath
							+ entry.getName());
					entry = null;

					FileOutputStream fout = new FileOutputStream(
							myFile.getPath());
					myFile = null;
					DataOutputStream dout = new DataOutputStream(fout);
					byte[] b = new byte[1024];
					int len = 0;
					while ((len = zin.read(b)) != -1) {
						dout.write(b, 0, len);
					}
					dout.close();
					fout.close();
					b = null;
					fout = null;
					dout = null;
					zin.closeEntry();
				}
			}
			zin = null;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			// Log.e("unzip", "error");
			return false;
		}
	}

	/**
	 * Bitmap对象转化byte数组
	 * 
	 * @param bitmap
	 *            Bitmap对象
	 * @return byte数组
	 */
	public static byte[] bitmapToBytes(Bitmap bitmap, boolean needCompress,
			boolean isJPEG) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (isJPEG) {
			if (needCompress) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			}
		} else {
			if (needCompress) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			}
		}

		return baos.toByteArray();
	}

	/**
	 * 将字符串str按子字符串separatorChars 分割成数组
	 * 
	 * @param str
	 *            要拆分的字符串
	 * @param separatorChars
	 *            用来拆分的分割字符
	 * @return 拆分后的字符串
	 */
	public static String[] split2(String str, String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	/**
	 * 拆分字符串
	 * 
	 * @param str
	 *            要拆分的字符串
	 * @param separatorChars
	 *            用来拆分的分割字符
	 * @param max
	 *            要拆分字符串的最大长度
	 * @param preserveAllTokens
	 * @return 拆分后的字符串
	 */
	private static String[] splitWorker(String str, String separatorChars,
			int max, boolean preserveAllTokens) {
		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return new String[] { "" };
		}
		Vector<String> vector = new Vector<String>();
		int sizePlus1 = 1;
		int i = 0;
		int start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			while (i < len) {
				if (str.charAt(i) == '\r' || str.charAt(i) == '\n'
						|| str.charAt(i) == '\t') {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						vector.addElement(str.substring(start, i));
						match = false;
					}
					start = ++i;
				} else {
					lastMatch = false;
					match = true;
					i++;
				}
			}
		} else if (separatorChars.length() == 1) {
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						vector.addElement(str.substring(start, i));
						match = false;
					}
					start = ++i;
				} else {
					lastMatch = false;
					match = true;
					i++;
				}
			}
		} else {
			while (i < len) {
				int id = i + separatorChars.length() < len ? i
						+ separatorChars.length() : len;
				if (separatorChars.indexOf(str.charAt(i)) >= 0
						&& separatorChars.equals(str.substring(i, id))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						vector.addElement(str.substring(start, i));
						match = false;
					}
					i += separatorChars.length();
					start = i;
				} else {
					lastMatch = false;
					match = true;
					i++;
				}
			}
		}

		if (match || preserveAllTokens && lastMatch) {
			vector.addElement(str.substring(start, i));
		}
		String[] ret = new String[vector.size()];
		vector.copyInto(ret);
		vector = null;
		return ret;
	}

	/**
	 * 从SD卡中读取文本文件
	 * 
	 * @param fileName
	 *            文件的名称
	 * @return 文件中的信息
	 */
	public static String readTextFile(String fileName) {
		ByteArrayOutputStream bos = null;
		InputStream is = null;
		byte[] data = null;
		File file = null;
		try {
			bos = new ByteArrayOutputStream();
			file = new File(fileName);
			is = new FileInputStream(file);
			int ch = 0;
			byte[] buffer = new byte[MAX_LENTH];

			while ((ch = is.read(buffer)) != -1) {
				bos.write(buffer, 0, ch);
			}
			data = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bos = null;
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is = null;
			}
		}
		if (data != null) {
			try {
				return new String(data, HTTP.UTF_8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return "";
		}
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	private static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > 2147483647L)
			return -1;
		else
			return (int) count;
	}

	private static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte buffer[] = new byte[4096];
		long count = 0L;
		for (int n = 0; -1 != (n = input.read(buffer));) {
			output.write(buffer, 0, n);
			count += n;
		}
		input.close();

		return count;
	}

	/**
	 * 缩放图片
	 * 
	 * @param sourceBitmap
	 * @return
	 */
	public static Bitmap MatrixBitmap2(Bitmap sourceBitmap, float targetW,
			float targetH) {
		float sourceBitmapW = sourceBitmap.getWidth();
		float sourceBitmapH = sourceBitmap.getHeight();

		float scaleW = 0;
		float scaleH = 0;
		if (targetH == 0) {
			scaleH = 1;
		} else {
			scaleH = targetH / sourceBitmapH;
		}
		if (targetW == 0) {
			scaleW = 1;
		} else {
			scaleW = targetW / sourceBitmapW;
		}
		Matrix matrix = new Matrix();

		if (sourceBitmapW == sourceBitmapH) {
			matrix.postScale(scaleH, scaleH);
		} else {
			matrix.postScale(scaleW, scaleH);
		}

		sourceBitmap = Bitmap
				.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight(), matrix, true);
		matrix = null;
		return sourceBitmap;
	}

	/**
	 * email格式判断
	 * 
	 * @param email
	 * @return
	 * 
	 *         合法E-mail地址： 1. 必须包含一个并且只有一个符号“@” 2. 第一个字符不得是“@”或者“.” 3.
	 *         不允许出现“@.”或者.@ 4. 结尾不得是字符“@”或者“.” 5. 允许“@”前的字符中出现“＋” 6.
	 *         不允许“＋”在最前面，或者“＋@”
	 * 
	 *         字符描述： ^ ：匹配输入的开始位置。 \：将下一个字符标记为特殊字符或字面值。 ：匹配前一个字符零次或几次。 +
	 *         ：匹配前一个字符一次或多次。 (pattern) 与模式匹配并记住匹配。 x|y：匹配 x 或 y。 [a-z]
	 *         ：表示某个范围内的字符。与指定区间内的任何字符匹配。 \w ：与任何单词字符匹配，包括下划线。 $ ：匹配输入的结尾。
	 */
	public static boolean checkEmail(String email) {
		Pattern pattern = Pattern
				.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public static Bitmap takeScreenShot(View view, int width, int height) {
		// View是你需要截图的View
		Bitmap map = Bitmap
				.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(map);
		view.draw(c);
		return map;
	}

	/**
	 * 换算 强制按东八区计算
	 * 
	 * @param miao
	 * @return
	 */
	public static String getDetailTime(String miao) {
		if (miao.length() == 10) {
			miao = miao + "000";
		}

		long temp = Long.parseLong(miao);
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		Locale loc = new Locale("zh", "CN");
		Date d = new Date(temp);

		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", loc);
		df.setTimeZone(tz);
		// 格式化输出
		return df.format(d);
	}

	/**
	 * 获取评论时间
	 * 
	 * @param miao
	 * @return
	 */
	public static String getDetailHoursTime(String miao) {
		try {
			if (miao.length() <= 10) {
				miao = miao + "000";
			}
			long temp = Long.parseLong(miao);
			SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
			String time = df.format(new Date(temp));

			// 格式化输出
			return time;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return miao;
	}

	/**
	 * 换算 强制按东八区计算
	 * 
	 * @param miao
	 * @return
	 */
	public static String getBirthTime(String miao) {
		try {
			if (miao.length() <= 10) {
				miao = miao + "000";
			}
			long temp = Long.parseLong(miao);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String time = df.format(new Date(temp));

			// 格式化输出
			return time;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return miao;
	}

	/**
	 * 换算 强制按东八区计算
	 * 
	 * @param miao
	 * @return
	 */
	public static String getBirthTime_StrongGo(String miao) {
		try {
			long temp = Long.parseLong((miao.substring(0, 10) + "000"));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String time = df.format(new Date(temp));

			// 格式化输出
			return time;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return miao;
	}

	/**
	 * 换算 强制按东八区计算
	 * 
	 * @param miao
	 * @return
	 */
	public static String getNowTime() {
		String miao = System.currentTimeMillis() + "";

		long temp = Long.parseLong(miao);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String time = df.format(new Date(temp));

		// 格式化输出
		return time;
	}

	/**
	 * 换算 强制按东八区计算
	 * 
	 * @param miao
	 * @return
	 */
	public static String getBirthTime_month(String miao) {
		if (miao.length() <= 10) {
			miao = miao + "000";
		}

		long temp = Long.parseLong(miao);
		SimpleDateFormat df = new SimpleDateFormat("MM-dd");
		String time = df.format(new Date(temp));

		// 格式化输出
		return time;
	}

	/**
	 * 
	 * @param act
	 * @param value
	 */
	public static void startBrowser(Activity act, String value) {
		if (value.startsWith("http")) {
			try {
				Uri uri = Uri.parse(value);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				act.startActivity(intent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param act
	 * @param value
	 */
	public static void startBrowserByExit(Activity act, String value) {
		Uri uri = Uri.parse(value);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		act.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

//	/**
//	 * 发送邮件
//	 * 
//	 * @param act
//	 */
//	public static void startEmail(Activity act, String emailReciver,
//			String emailSubject, String emailBody) {
//		Intent intent = new Intent(Intent.ACTION_SEND); // 建立Intent对象
//		intent.setType("plain/text");
//		intent.putExtra(Intent.EXTRA_EMAIL, emailReciver);// 设置对方的EMAIL地址
//		intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);// 设置标题
//		intent.putExtra(Intent.EXTRA_TEXT, emailBody);// 设置邮件文本内容
//		act.startActivity(Intent.createChooser(intent, act.getResources()
//				.getString(R.string.trans_choose_email)));
//	}

	/**
	 * 是否为正实数
	 * 
	 * @param money
	 * @return
	 */
	public static boolean checkMoney(String money) {
		try {
			Float.parseFloat(money);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 按目标宽度缩放图片
	 * 
	 * @param bitmap
	 * @param targetW
	 * @return
	 */
	public static Bitmap scaleBitmapByW(Bitmap bitmap, float targetW) {

		float scaleW = (float) (targetW / bitmap.getWidth());

		Matrix matrix = new Matrix();
		matrix.postScale(scaleW, scaleW);

		return bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	/**
	 * 按目标高度缩放图片
	 * 
	 * @param bitmap
	 * @param targetH
	 * @return
	 */
	public static Bitmap scaleBitmapByH(Bitmap bitmap, float targetH)
			throws OutOfMemoryError {

		float scaleH = (float) (targetH / bitmap.getHeight());

		Matrix matrix = new Matrix();
		matrix.postScale(scaleH, scaleH);

		return bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	/**
	 * 
	 */
	public static byte[] getBytes(String inputS) {
		StringBuilder inputString = new StringBuilder(inputS);

		// 分段数
		int count = 10;
		// 字符串长度
		int lengthString = inputString.length();
		// 平均分段的字符串长度
		int perLenthString = lengthString / count;

		// 字符串byte长度
		int length = 0;
		String tempString;

		// 计算字符串byte长度s
		int k = 0;
		for (; k < count; k++) {
			tempString = inputString.substring(k * perLenthString,
					(k + 1) * perLenthString).intern();
			length += tempString.getBytes().length;
			tempString = null;

		}
		tempString = inputString.substring(k * perLenthString, lengthString)
				.intern();
		length += tempString.getBytes().length;
		tempString = null;
		// 字符串byte数组
		byte[] totalByte = new byte[length];

		int start = 0;
		int i = 0;
		for (; i < count; i++) {
			tempString = inputString.substring(i * perLenthString,
					(i + 1) * perLenthString).intern();

			byte[] tempByte = tempString.getBytes();
			int x = 0;
			int tempL = tempByte.length;
			for (int j = start; j < start + tempL; j++) {
				totalByte[j] = tempByte[x];
				x++;
			}

			start = start + tempByte.length;
			tempString = null;

		}
		tempString = inputString.substring(i * perLenthString, lengthString)
				.intern();

		byte[] tempByte = tempString.getBytes();
		int x = 0;
		for (int j = start; j < start + tempByte.length; j++) {
			totalByte[j] = tempByte[x];
			x++;
		}
		tempString = null;
		return totalByte;
	}

	/**
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
		if (bitmap == null) {
			return null;
		}
		Bitmap output = null;
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawRoundRect(rectF, pixels, pixels, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (Error e) {
			// TODO: handle exception
		}

		return output;
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 代理地址
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getProxy(Context con) {
		ConnectivityManager cm = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			return "";
		}
		System.out.println("type :" + info.getType());
		if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			return "";
		}
		try {
			Cursor cusor = con.getContentResolver().query(
					FileConstant.PREFERRED_APN_URI, null, null, null, null);
			if (cusor != null && cusor.getCount() > 0) {
				cusor.moveToFirst();
				String proxy = cusor.getString(cusor.getColumnIndex("proxy"));
				cusor.close();
				return proxy != null ? proxy : getAPNType(info);
			} else {
				return getAPNType(info);
			}
		} catch (SecurityException e) {
			// 出现异常判断是4.0以后 apn访问受限
			return getAPNType(info);
		}
	}

	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络 2：wap网络 3：net网络
	 * 
	 * @param context
	 * @return
	 */
	public static String getAPNType(NetworkInfo info) {
		if (info == null)
			return "";

		String ss = info.getExtraInfo().toLowerCase();
		System.out.println("ss : " + ss);
		if (ss.equalsIgnoreCase("cmwap") || ss.equalsIgnoreCase("3gwap")
				|| ss.equalsIgnoreCase("uniwap")) {
			return "10.0.0.172";
		}
		if (ss.equalsIgnoreCase("ctwap") || ss.startsWith("ctwap")) {
			return "10.0.0.200";
		}
		return "";

	}

	/**
	 * 获取文件创建时间 <功能详细描述>
	 * 
	 * @param file_path
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static long getFileTime(String file_path) {
		File f = new File(file_path);
		return f.lastModified();
	}

	/**
	 * <通过命令删除文件> <功能详细描述>
	 * 
	 * @param path
	 * @see [类、类#方法、类#成员]
	 */
	public static void deleteAllByLinux(String path) {

		Process p = null;
		try {
			p = Runtime.getRuntime().exec("rm -r " + path);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取字符串的md5
	 * 
	 * @param data
	 * @return
	 */
	public static String getDigest(String data) {
		byte[] btInput = data.getBytes();
		MessageDigest mdInst = null;
		try {
			mdInst = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mdInst.update(btInput);
		byte[] md = mdInst.digest();
		StringBuffer sb = new StringBuffer();
		for (byte i = 0; i < md.length; i++) {
			int val = ((int) md[i]) & 0xff;
			if (val < 16)
				sb.append("0");
			sb.append(Integer.toHexString(val));
		}
		return sb.toString();
	}

	/**
	 * 图片名称md5
	 * 
	 * @param url
	 * @return
	 */
	public static String getImgFileName(String url) {
		return getDigest(url);
	}

	/**
	 * 通过url 地址 获取 文件名
	 * 
	 * @return
	 */
	public static String getHttpFileName(String url) {
		try {
			URL temp = new URL(url);
			return temp.getFile().substring(1);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 搜索历史记录
	 * 
	 * @param obj
	 * @return
	 */
	public static List<String> getJsonArrayToList(String obj) {
		if (obj == null || obj.equals("")) {
			return null;
		}
		List<String> lists = null;
		try {
			JSONArray array = new JSONArray(obj);
			lists = new ArrayList<String>();
			for (int i = 0; i < array.length(); i++) {
				lists.add(array.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lists;
	}

	/**
	 * 搜索历史记录
	 * 
	 * @param obj
	 * @return
	 */
	public static String setListToJsonArray(List<String> obj) {
		if (null == obj) {
			return "";
		}
		JSONArray array = new JSONArray();
		for (int i = 0; i < obj.size(); i++) {
			array.put(obj.get(i));
		}
		return array.toString();
	}

	/**
	 * 存储数据
	 * 
	 * @param flag
	 * @param context
	 */
	public static void saveDataToXml(String key, Context context, String value) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = context.getSharedPreferences("setting",
				0);
		Editor edit = preferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	/**
	 * 读取数据
	 * 
	 * @param flag
	 * @param context
	 * @return
	 */
	public static String readDataFromXml(String flag, Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = context.getSharedPreferences("setting",
				0);
		if (flag.equals("last_time") || flag.equals("themenum")) {
			return preferences.getString(flag, "0");
		} else if (flag.equals("family_head")) {
			return preferences.getString(flag, "");
		} else {
			return preferences.getString(flag, "true");
		}
	}

	/**
	 * 调用手机自带的拨打电话功能
	 * 
	 * @param icbcSmsNum
	 * @param bankSortActivity
	 */
	public static void dialPhone(String smsNum, Context context) {
		// TODO Auto-generated method stub
		Intent phoneIntent = new Intent("android.intent.action.CALL",
				Uri.parse("tel:" + smsNum));
		try {
			context.startActivity(phoneIntent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 判断微信APP是否存在 <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isWeixinExist(Context context) {
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> pkgList = manager.getInstalledPackages(0);
		for (int i = 0; i < pkgList.size(); i++) {
			PackageInfo pI = pkgList.get(i);
			if (pI.packageName.equalsIgnoreCase("com.tencent.mm"))
				return true;
		}
		return false;
	}

	/**
	 * 隐藏软键盘
	 */
	@SuppressWarnings("static-access")
	public static void hidekeyboard(Activity act) {
		InputMethodManager imm = ((InputMethodManager) act
				.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (imm != null) {
			try {
				imm.hideSoftInputFromWindow(act.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 操作键盘
	 * 
	 * @param act
	 */
	public static void operateKeyboard(Activity act) {
		InputMethodManager imm = ((InputMethodManager) act
				.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (imm != null) {
			try {
				imm.toggleSoftInput(0, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 定时打开软键盘
	 */
	public static void openKeyboard(Activity act) {
		final InputMethodManager immr = (InputMethodManager) act
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (immr != null) {
			try {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						immr.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}, 200);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static Size getOptimalPreviewSize(Activity currentActivity,
			List<Size> sizes, double targetRatio) {
		// Use a very small tolerance because we want an exact match.
		final double ASPECT_TOLERANCE = 0.001;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		// Because of bugs of overlay and layout, we sometimes will try to
		// layout the viewfinder in the portrait orientation and thus get the
		// wrong size of mSurfaceView. When we change the preview size, the
		// new overlay will be created before the old one closed, which causes
		// an exception. For now, just get the screen size

		Display display = currentActivity.getWindowManager()
				.getDefaultDisplay();
		int targetHeight = Math.min(display.getHeight(), display.getWidth());

		if (targetHeight <= 0) {
			// We don't know the size of SurfaceView, use screen height
			targetHeight = display.getHeight();
		}

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio. This should not happen.
		// Ignore the requirement.
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	/**
	 * 字节转化成16进制
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer(b.length);
		String stmp = "";
		int len = b.length;
		for (int n = 0; n < len; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs = hs.append("0").append(stmp);
			else {
				hs = hs.append(stmp);
			}
		}
		return String.valueOf(hs);
	}

	/**
	 * 计算星座
	 * 
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getConstellation(int month, int day) {
		String s = "魔羯宝瓶双鱼白羊金牛双子巨蟹狮子室女天秤天蝎人马魔羯";
		Integer[] arr = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		Integer num = month * 2 - (day < arr[month - 1] ? 2 : 0);
		return s.substring(num, num + 2) + "座";
	}

	/**
	 * 将年月日转化成时间戳
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String convertTimeToTimeStamp(String dateTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = df.parse(dateTime);
			return date.getTime() / 1000 + "";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将年月日小时分钟转化成时间戳
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String convertTimeHoursToTimeStamp(String dateTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		try {
			date = df.parse(dateTime);
			return date.getTime() / 1000 + "";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 发送SMS
	 * 
	 * @param cx
	 *            上下文
	 * @param body
	 *            信息体
	 * @param path
	 */
	public static void sendSMS(Context cx, String body, String number) {
		// TODO Auto-generated method stub
		Uri uri = Uri.parse("smsto:" + number);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body", body);
		cx.startActivity(it);

	}

	/**
	 * @param view
	 * @return
	 */
	public static byte[] getScreenShot(View view, int width, int height) {
		Bitmap bitmap = takeScreenShot(view, width, height);
		return bitmapToBytes(bitmap, false, false);
	}

	// 加载图片时防止内存溢出
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	public static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 128 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 打开系统相册
	 * 
	 * @param act
	 */
	public static void openSystemImage(Activity act) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		act.startActivityForResult(intent, FileConstant.PHOTOALBUM);
	}

	/**
	 * 打开系统视频
	 * 
	 * @param act
	 */
	public static void openSystemVideo(Activity act) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				"video/*");
		act.startActivityForResult(intent, FileConstant.VIDEOSHOW);
	}

	/**
	 * 使用系统缩放图片
	 * 
	 * @param act
	 */
	public static void openSystemZoomImage(Activity act, Uri uri,
			Uri output_uri, int width, int height) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 发送裁剪信号
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		// 是否返回图片数组
		intent.putExtra("return-data", false);
		// 定义输出路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, output_uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection

		act.startActivityForResult(intent, FileConstant.PHOTORESOULT);
	}

	/**
	 * 使用系统缩放图片
	 * 
	 * @param act
	 */
	public static void openSystemZoomImage(Activity act, Uri uri, Uri output_uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 发送裁剪信号
		// intent.putExtra("crop", "false");
		// aspectX aspectY 是宽高的比例
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		// intent.putExtra("outputX", width);
		// intent.putExtra("outputY", height);
		// 是否返回图片数组
		// intent.putExtra("return-data", false);
		// 定义输出路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, output_uri);
		// intent.putExtra("outputFormat",
		// Bitmap.CompressFormat.JPEG.toString());
		// intent.putExtra("noFaceDetection", true); // no face detection

		act.startActivityForResult(intent, FileConstant.PHOTORESOULT);
	}

	/**
	 * 打开照相机
	 * 
	 * @param act
	 */
	public static void openSystemCapture(Activity act) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), "temp.jpg")));
		act.startActivityForResult(intent, FileConstant.PHOTOCAPTURE);
	}

	/**
	 * 打开照相机
	 * 
	 * @param act
	 */
	public static void openCustomSystemCapture(Activity act, Uri uri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		act.startActivityForResult(intent, FileConstant.PHOTOCAPTURE);
	}

	/**
	 * 
	 * 获取发布时间差
	 */
	public static String getPublishTime(String miao) {
		int time = Integer.parseInt(miao);
		int fen = time / 60;
		int hour;
		if (fen == 0) {
			return miao + "秒前";
		}
		if (fen < 60) {
			return fen + "分钟前";
		} else {
			hour = fen / 60;
			if (hour < 24) {
				return hour + "小时前";
			} else if (hour >= 24 && hour < 48) {
				return "1天前";
			} else if (hour >= 48 && hour < 72) {
				return "2天前";
			} else {
				return "3天前";
			}
		}
	}

	/**
	 * 是否显示图片
	 * 
	 * @return
	 */
	public static boolean needshowPhoto(Context con, boolean showPhoto) {
		ConnectivityManager cm = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else {
			if (showPhoto) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 
	 */
	final static String[] PHONENUMBER_PREFIX = { "130", "131", "132", "145",
			"155", "156", "185", "186", "134", "135", "136", "137", "138",
			"139", "147", "150", "151", "152", "157", "158", "159", "182",
			"183", "187", "188", "133", "153", "189", "180" };

	/**
	 * 电话
	 * 
	 * @param number
	 * @return
	 */
	public static boolean patternPhoneNumber(String number) {
		int len = PHONENUMBER_PREFIX.length;
		if (number != null) {
			for (int i = 0; i < len; i++) {
				Pattern p = Pattern.compile(PHONENUMBER_PREFIX[i] + "\\d{8}");
				if (p.matcher(number).matches()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获得距离
	 * 
	 * @return
	 */
	public static String getDistance(String str) {
		if (str.equals("未知")) {
			return "距离未知";
		}
		String distance = str;// 米
		DecimalFormat formater = new DecimalFormat("#.#");

		try {
			int length = Integer.parseInt(str);
			float i = length / 1000;
			if (i > 100) {
				// 显示大于100KM
				distance = "大于100KM";
			} else if (i < 0.1) {
				// "小于100M";
				distance = "附近" + length + "M";
			} else {
				if (length < 1000) {
					distance = "距离你" + length + "M";
				} else {
					distance = "距离你"
							+ formater.format(Float.parseFloat(str) / 1000)
							+ "KM";
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			distance = "距离你" + str + "M";
		}
		return distance;
	}

	/**
	 * @param content
	 * @param paint
	 * @param width
	 * @return
	 */
	public static int getTextLines(String content, Paint paint, float width) {
		int i = 0;
		int num = 0;
		while (content.length() > 0) {
			num = paint.breakText(content, true, width, null);
			content = content.substring(num, content.length());
			i++;
		}
		return i;
	}

	/**
	 * @param paint
	 * @return
	 */
	public static int getTextHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.ascent);
	}

	/**
	 * 打开系统 视频录制
	 * 
	 * @param act
	 */
	public static void openSystemVideoRecord(Activity act, int durationLimit,
			boolean isHighQuality) {
		try {
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			if (isHighQuality) {
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			} else {
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			}
			intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit);
			act.startActivityForResult(intent, FileConstant.VIDEORECORD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void openSystemVideoPlayer(Activity act, String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		String type = "video/*";
		intent.setDataAndType(Uri.fromFile(new File(path)), type);
		act.startActivity(intent);
	}

	/**
	 * 打开系统 视频播放器
	 * 
	 * @param act
	 */
	public static void openSystemVideo(Activity act, Uri uri) {
		try {
			Intent innerIntent = new Intent(Intent.ACTION_VIEW);
			innerIntent.setDataAndType(uri, "video/*");
			Intent wrapperIntent = Intent.createChooser(innerIntent, null);
			act.startActivityForResult(wrapperIntent, FileConstant.VIDEOSHOW);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width,
			int height, int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * @param contentUri
	 * @return
	 */
	public static String getVideoRealPathFromURI(Context con, Uri contentUri) {
		String path = null;
		String[] proj = { MediaStore.Video.Media.DATA };
		CursorLoader loader = new CursorLoader(con, contentUri, proj, null,
				null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		cursor.moveToFirst();
		path = cursor.getString(column_index);
		cursor.close();
		return path;
	}

	/**
	 * @param contentUri
	 * @return
	 */
	public static String getPhotoRealPathFromURI(Context con, Uri contentUri) {
		String path = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(con, contentUri, proj, null,
				null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		path = cursor.getString(column_index);
		cursor.close();
		return path;
	}

	/**
	 * @return
	 */
	public static boolean isJpeg(InputStream is) {
		byte[] b = new byte[4];
		try {
			is.read(b, 0, b.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String type = byte2hex(b).toUpperCase();
		if (type.contains("FFD8FF")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 用于同一年
	 * 
	 * @return
	 */
	public static int getDayIntervalFromNow(String startTime) {
		if (startTime.length() <= 10) {
			startTime = startTime + "000";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(startTime));

		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());

		int start_day = cal.get(Calendar.DAY_OF_YEAR);
		int end_day = now.get(Calendar.DAY_OF_YEAR);
		return end_day - start_day;
	}

	/**
	 * 用于同一年
	 * 
	 * @return
	 */
	public static int getWeekIndex(String startTime) {
		int days = getDayIntervalFromNow(startTime);

		if (startTime.length() <= 10) {
			startTime = startTime + "000";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(startTime));
		int weekday = cal.get(Calendar.DAY_OF_WEEK);

		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());
		int now_weekday = now.get(Calendar.DAY_OF_WEEK);

		int fir_Interval = weekday == 1 ? 1 : 9 - weekday;
		int sec_Interval = now_weekday == 1 ? 7 : now_weekday - 1;

		// 判断2 天是不是在同一周
		if (cal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)) {
			return 1;
		} else {
			int num = (days + 1 - fir_Interval - sec_Interval) / 7;
			return num + 2;
		}
	}

	/**
	 * @return
	 */
	public static int getDayOfWeek() {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());
		return now.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 使用拨号盘
	 */
	public static void openTelDall(FragmentActivity activity,String phone){
		Uri Storeuri = Uri.parse("tel:" + phone);
		Intent Storeintent = new Intent();
		Storeintent.setAction(Intent.ACTION_DIAL);
		Storeintent.setData(Storeuri);
		activity.startActivity(Storeintent);
	}
}
