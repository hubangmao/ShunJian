/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.ruit.shunjianmei.util;

import java.net.URLEncoder;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
/**
 * @ClassName: YouMiAES
 * @Description: 有米加密工具类
 * @author: lee
 * @date: 2015年9月2日 下午3:26:24
 */
public class YouMiAES {

	public static String encode(String appid, String app_secret, String userId,
			String feedback) throws Exception {
		if (feedback == null) {
			feedback = "";
		}
		String salt = Base64.encode(userId.getBytes(),Base64.DEFAULT) + "&"
				+ Base64.encode(feedback.getBytes(),Base64.DEFAULT);
		return URLEncoder.encode(
				appid + Base64.encode(Encrypt(salt, app_secret),Base64.DEFAULT), "UTF-8");
	}
	// 加密
	public static byte[] Encrypt(String content, String key) throws Exception {
		if (key == null) {
			System.out.print("Key 为空 null");
			return null;
		}
		// 判断 Key 是否为 16 位
		if (key.length() != 16) {

			System.out.print("Key 长度不是 16 位");
			return null;
		}
		Random random = new Random();
		byte[] buff = new byte[16];
		random.nextBytes(buff);
		byte[] raw = key.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		IvParameterSpec iv = new IvParameterSpec(buff);// 使用 CBC 模式，需要一个向量
														// iv，可增加加
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(content.getBytes());
		int totalLength = iv.getIV().length + +encrypted.length;
		;
		byte[] combine = new byte[totalLength];
		System.arraycopy(iv.getIV(), 0, combine, 0, iv.getIV().length);
		System.arraycopy(encrypted, 0, combine, iv.getIV().length,
				encrypted.length);
		return combine;
	}

	public static void main(String[] args) throws Exception {
		// 对应公众号用户的 openid
		String openid = "aaaabbbbb";
		// 预留参数,如果不需要请留空
		String feedback = "ccccddddd";
		// 在有米官网获取的应用 ID
		String appid = "20bc607d983a540c";
		// 在有米官网获取的应用密钥
		String app_secret = "f27c36e5ab7ef02d";
		// 加密结果
		String encryptResult = encode(appid, app_secret, openid, feedback);
		System.out.println("encryptText: " + encryptResult);
	}
}
