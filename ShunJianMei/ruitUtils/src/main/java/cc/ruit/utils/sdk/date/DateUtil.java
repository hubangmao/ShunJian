package cc.ruit.utils.sdk.date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @ClassName: DateUtil
 * @Description: 时间工具类
 * @author: zhq_pc
 * @date: 2015年2月3日 下午2:40:15
 */
public class DateUtil {
	private static SimpleDateFormat sf;

	/**
	 * 
	 * @Title: getMillisecond
	 * @Description: 获得当前时间的毫秒值
	 * @return 毫秒值
	 * @return: String
	 */
	public static String getCurrentTime_Millisecond() {
		return String.valueOf(System.currentTimeMillis());
	}

	/**
	 * 
	 * @Title: getCurrentDate
	 * @Description: 获得当前时间的Date类型
	 * @return Date类型
	 * @return: Date
	 */
	public static Date getCurrentTime_Date() {
		return new Date();
	}

	/**
	 * 
	 * @Title: getCurrentTime_String
	 * @Description: 获得当前时间的String类型
	 * @return yyyy-MM-dd HH:mm:ss 字符串类型
	 * @return: String
	 */
	public static String getCurrentTime_String() {
		return getCurrentTime_Assignformat("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 
	 * @Title: getCurrentTime_Assignformat
	 * @Description: 获得指定格式的当前时间
	 * @param format
	 *            指定格式
	 * @return String类型
	 * @return: String
	 */
	public static String getCurrentTime_Assignformat(String format) {
		sf = new SimpleDateFormat(format);
		return sf.format(new Date());
	}

	/**
	 * 
	 * @Title: parseDateToString
	 * @Description: Date类型转String
	 * @param date
	 *            格式为yyyy-MM-dd HH:mm:ss
	 * @return String类型
	 * @return: String
	 */
	public static String parseDateToString(Date date) {
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}

	/**
	 * 
	 * @Title: parseDateToString
	 * @Description: Date转为指定格式的String类型
	 * @param date
	 * @param format
	 *            指定格式
	 * @return
	 * @return: String
	 */
	public static String parseDateToString(Date date, String format) {
		sf = new SimpleDateFormat(format);
		return sf.format(date);
	}

	/**
	 * 
	 * @Title: parseDateToMillisecond
	 * @Description: Date类型转毫秒
	 * @param date
	 * @return 毫秒值
	 * @return: long
	 */
	public static long parseDateToMillisecond(Date date) {
		return date.getTime();
	}

	/**
	 * 
	 * @Title: parseStringToDate
	 * @Description: 格式为的String类型转为Date
	 * @param time
	 *            为yyyy-MM-dd HH:mm 格式
	 * @return Date类型
	 * @throws ParseException
	 * @return: Date
	 */
	public static Date parseStringToDate(String time) throws ParseException {
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sf.parse(time);
	}

	/**
	 * 
	 * @Title: parseStringToDate_Assignformat
	 * @Description: 将指定格式的String类型转为Date
	 * @param time
	 *            String类型
	 * @param format
	 *            指定格式
	 * @return
	 * @throws ParseException
	 * @return: Date
	 */
	public static Date parseStringToDate(String time, String format)
			throws ParseException {
		sf = new SimpleDateFormat(format);
		return sf.parse(time);
	}

	/**
	 * 
	 * @Title: parseMillisecondToDate
	 * @Description: 将毫秒值转为Date
	 * @param millisecond
	 *            long类型的毫秒值
	 * @return Date类型
	 * @return: Date
	 */
	public static Date parseMillisecondToDate(long millisecond) {
		return new Date(millisecond);
	}

	/**
	 * 
	 * @Title: parseMillisecondToString
	 * @Description: 将毫秒值转为String类型
	 * @param millisecond
	 *            long类型毫秒值
	 * @return 格式为yyyy-MM-dd HH:mm:ss的String类型
	 * @return: String
	 */
	public static String parseMillisecondToString(long millisecond) {
		Date date = new Date(millisecond);
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}

	/**
	 * 
	 * @Title: parseMillisecondToString
	 * @Description: 将毫秒值转为指定格式的String类型
	 * @param millisecond
	 * @param format
	 *            格式
	 * @return 指定格式的String类型
	 * @return: String
	 */
	public static String parseMillisecondToString(long millisecond,
			String format) {
		Date date = new Date(millisecond);
		sf = new SimpleDateFormat(format);
		return sf.format(date);
	}

	/**
	 * 
	 * @Title: parseStringToString
	 * @Description: String类型不同格式间相互转换
	 * @param time
	 * @param timeFormat
	 *            当前String类型格式
	 * @param newFormat
	 *            要转换的String类型格式
	 * @return
	 * @throws ParseException
	 * @return: String
	 */
	public static String parseStringToString(String time, String timeFormat,
			String newFormat) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(timeFormat);
		Date parse = format.parse(time);
		SimpleDateFormat nformat = new SimpleDateFormat(newFormat);
		return nformat.format(parse);
	}

	/**
	 * 
	 * @Title: getCurrentMouthLastDay
	 * @Description: 获得当月最后一天
	 * @return
	 * @return: int
	 */
	public static int getCurrentMouthLastDay_1() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * @Title: getCurrentMonthLastDay_2
	 * @Description: 获得当月的最后一天
	 * @return
	 * @return: int
	 */
	public static int getCurrentMonthLastDay_2() {
		Calendar calendar = Calendar.getInstance();
		// calendar月份默认是从0开始的
		calendar.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		calendar.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = calendar.get(Calendar.DATE);
		// calendar.clear();
		return maxDate;
	}

	/**
	 * 
	 * @Title: getAssignMouthLastDay
	 * @Description: 获得指定月份的最后一天
	 * @param mouth
	 * @return
	 * @return: int
	 */
	public static int getAssignMouthLastDay(int mouth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, mouth - 1);
		int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// calendar.clear();
		return actualMaximum;
	}

	/**
	 * 
	 * @Title: getCurrentMonthFirstDay_1
	 * @Description: 获得当月的第一天
	 * @return
	 * @return: int
	 */
	public static int getCurrentMonthFirstDay_1() {
		Calendar calendar = Calendar.getInstance();

		return calendar.getMinimum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * @Title: getCurrentMonthFirstDay_2
	 * @Description: 获得当月的第一天
	 * @return
	 * @return: int
	 */

	public static int getCurrentMonthFirstDay_2() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		int firstDate = calendar.get(Calendar.DATE);
		// calendar.clear();
		return firstDate;
	}

	/**
	 * 
	 * @Title: inSometimeAfter
	 * @Description: 在time以后manyDays的时间
	 * @param time
	 * @param format
	 *            time格式
	 * @param manyDays
	 *            相差天数
	 * @return
	 * @throws ParseException
	 * @return: String
	 */
	public static String inSometimeAfter(String time, String format,
			int manyDays) throws ParseException {
		sf = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sf.parse(time));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				+ manyDays);
		String format2 = sf.format(calendar.getTime());
		return sf.format(calendar.getTime());
	}

	/**
	 * 
	 * @Title: inSometimebefre
	 * @Description: 在time之前manyDays的时间
	 * @param time
	 * @param format
	 * @param manyDays
	 *            相差天数
	 * @return
	 * @throws ParseException
	 * @return: String
	 */
	public static String inSometimebefore(String time, String format,
			int manyDays) throws ParseException {
		sf = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sf.parse(time));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				- manyDays);
		String format2 = sf.format(calendar.getTime());
		return sf.format(calendar.getTime());
	}

	/**
	 * 
	 * @Title: getYesterday
	 * @Description: 获得当前时间的昨天
	 * @return 按yyyy-MM-dd HH:mm:ss格式返回
	 * @return: String
	 */
	public static String getYesterday() {
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		return sf.format(calendar.getTime());
	}

	/**
	 * 
	 * @Title: getYesterday
	 * @Description: 获得当前时间的昨天，按指定格式返回
	 * @param format 指定格式
	 * @return
	 * @return: String
	 */
	
	public static String getYesterday(String format) {
		sf = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		return sf.format(calendar.getTime());
	}
	/**
	 * 
	 * @Title: getTomorrow
	 * @Description: 获得当前时间的明天
	 * @return 按yyyy-MM-dd HH:mm:ss格式返回
	 * @return: String
	 */
	public static String getTomorrow() {
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) + 1);
		return sf.format(calendar.getTime());

	}
	/**
	 * 
	 * @Title: getTomorrow
	 * @Description: 获得当前时间的明天，按指定格式返回
	 * @param format 指定格式
	 * @return
	 * @return: String
	 */
	public static String getTomorrow(String format) {
		sf = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) + 1);
		return sf.format(calendar.getTime());
	}
	
	/**
	 * 
	 * @Title: getCurrent_ZeroTime
	 * @Description: 获取当天零点时间
	 * @return
	 * @return: String
	 */
	public static String getCurrent_ZeroTime(){
		sf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return sf.format(new Date());
	}
	/**
	 * 
	 * @Title: getAssignTime_ZeroTime
	 * @Description: 获得指定时间的零点
	 * @param time 指定时间
	 * @param format 指定时间的格式
	 * @return String类型
	 * @throws ParseException
	 * @return: String
	 */
	public static String getAssignTime_ZeroTime(String time,String format) throws ParseException{
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sf2 = new SimpleDateFormat(format);
		Date date = sf2.parse(time);
		return sf1.format(date);
	}
	
	/**
	 * 
	 * @Title: getAssignTime_ZeroTime_Date
	 * @Description: 获得指定时间的零点
	 * @param time 指定时间
	 * @param format 指定时间的格式
	 * @return Date类型
	 * @throws ParseException
	 * @return: Date
	 */
	public static Date getAssignTime_ZeroTime_Date(String time,String format) throws ParseException{
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sf2 = new SimpleDateFormat(format);
		Date date = sf2.parse(time);
		sf1.parse(sf1.format(date));
		return sf1.parse(sf1.format(date));
	}
	
	/**
	 * 
	 * @Title: getAssignTime_ZeroTime_Mill
	 * @Description: 获得指定时间的零点
	 * @param time 指定时间
	 * @param format 指定时间的格式
	 * @return long类型
	 * @throws ParseException
	 * @return: long
	 */
	public static long getAssignTime_ZeroTime_Mill(String time,String format) throws ParseException{
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sf2 = new SimpleDateFormat(format);
		Date date = sf2.parse(time);
		sf1.parse(sf1.format(date));
		return sf1.parse(sf1.format(date)).getTime();
	}
	
	/**
	 * 
	 * @Title: isSameDay
	 * @Description: 判断time2是否在time1这天里
	 * @param time1 
	 * @param time2 
	 * @param format
	 * @return  true为在time1这天里，false为不在time1这天里
	 * @throws ParseException
	 * @return: boolean
	 */
	public static boolean isSameDay(String time1,String time2,String format) throws ParseException{
		sf = new SimpleDateFormat(format);
		String assignTime_ZeroTime = getAssignTime_ZeroTime(time1, format);
		long millisecond1 = sf.parse(assignTime_ZeroTime).getTime();
		long after24Millisecond = millisecond1+24*60*60*1000;
		long millisecond2 = sf.parse(time2).getTime();
		
		return millisecond1<=millisecond2&&millisecond2<after24Millisecond;
	}
	
	/**   
	 * 得到本月的第一天   
	 * @return   
	 */    
	public static String getMonthFirstDay() {     
	    Calendar calendar = Calendar.getInstance();     
	    calendar.set(Calendar.DAY_OF_MONTH, calendar     
	            .getActualMinimum(Calendar.DAY_OF_MONTH));     
	    
	    return dateFormat("yyyy-MM-dd 00:00:00", calendar.getTime());     
	}     
	    
	/**   
	 * 得到本月的最后一天   
	 *    
	 * @return   
	 */    
	public static String getMonthLastDay() {     
	    Calendar calendar = Calendar.getInstance();     
	    calendar.set(Calendar.DAY_OF_MONTH, calendar     
	            .getActualMaximum(Calendar.DAY_OF_MONTH));     
	    return dateFormat("yyyy-MM-dd 23:59:59", calendar.getTime());     
	}
	/**
	 * 把日期转换为指定格式
	 * @param format  指定要转化的格式
	 * @param date  日期
	 * @return
	 */
	public static String dateFormat(String format,Date date){
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
		
	}
	/**
	 * 把某种格式的时间转化为星期几
	 * @param format
	 * @param time
	 * @return
	 */
	public static String getXIngQi(String format,String time){
		SimpleDateFormat sf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sf2 = new SimpleDateFormat("EEEE");
		return sf2.format(date);
	}
	/**
	 * 时间格式转化
	 * @param time
	 * @param oldformat
	 * @param newformat
	 * @return
	 */
	public static String formatToformat(String time,String oldformat,String newformat){
		
		try {
			SimpleDateFormat sf = new SimpleDateFormat(oldformat);
			SimpleDateFormat sf1 = new SimpleDateFormat(newformat);
			return sf1.format(sf.parse(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @Title: getNextDay
	 * @Description: 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	 * @author: leezw
	 * @param delay
	 *            为前移或后延的天数，负值为前移
	 * @return: 例如：2015-05-17
	 */
	public static String getNextDay(int delay,String fromat) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(fromat,
					Locale.getDefault());
			String mdate = "";
			Date d = new Date();
			long myTime = (d.getTime() / 1000) + (delay) * 24 * 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}
}
