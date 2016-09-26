package cc.ruit.utils.sdk.contentcheck;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParmsUtil {
	public static boolean checkIDCard(String idCard) {
		Pattern p = Pattern
				.compile("^(\\d{15}|\\d{17}[\\dxX])$");
		Matcher m = p.matcher(idCard);
		return m.matches();
	}

	/**
	 * 
	 * @Title: checkPhone
	 * @Description: 手机号码校验
	 * @param phone
	 * @return
	 * @return: boolean
	 */
	public static boolean checkPhone(String phone) {
		Pattern p = Pattern
				.compile("^(((13[0-9])|(14[5,7])|(17[0|5-8])|(15([0-3]|[5-9]))|(18([0-9])))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 
	 * @Title: checkPhone_2
	 * @Description: 手机号码校验
	 * @param phone
	 * @return
	 * @return: boolean
	 */
	public static boolean checkPhone_2(String phone) {
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 
	 * @Title: passwordRegExp
	 * @Description: 字母和数字组合，至少8位以上
	 * @param password
	 * @return
	 * @return: boolean
	 */
	public static boolean passwordRegExp_8(String password) {
		Pattern p = Pattern
				.compile("^(?![A-Z]*$)(?![a-z]*$)(?![0-9]*$)(?![^a-zA-Z0-9]*$)\\S{8,}");
		Matcher m = p.matcher(password);
		return m.matches();
	}

	/**
	 * 
	 * @Title: passwordRegExp
	 * @Description: 字母和数字组合，至少6位以上
	 * @param password
	 * @return
	 * @return: boolean
	 */
	public static boolean passwordRegExp_6(String password) {
		Pattern p = Pattern
				.compile("^(?![A-Z]*$)(?![a-z]*$)(?![0-9]*$)(?![^a-zA-Z0-9]*$)\\S{6,}");
		Matcher m = p.matcher(password);
		return m.matches();
	}

	/**
	 * 
	 * @Title: emailFormat
	 * @Description: 校验邮箱
	 * @param email
	 * @return
	 * @return: boolean
	 */
	public static boolean emailFormat(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	public static boolean idCardFormat(String idcard) {
		boolean tag = true;
		final String pattern1 = "^[xX0-9]{18}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(idcard);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}
}
