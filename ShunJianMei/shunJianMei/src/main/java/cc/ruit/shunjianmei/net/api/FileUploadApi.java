package cc.ruit.shunjianmei.net.api;

import java.io.File;
import java.util.List;

import cc.ruit.shunjianmei.constants.Constant;
import cc.ruit.utils.sdk.http.FileUploadHttpHelper;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;
import com.lidroid.xutils.util.LogUtils;

public class FileUploadApi {

	public FileUploadApi() {
		
	}

	/* ===========以下是接口部分，注：方法名保持和文档中接口名一致，方便查询========== */
	/**
	 * 单文件上传
	 * @param filePath 文件路径
	 * @param callBack 结果回调
	 */
	public void upload(String filePath,String paramsJson, RequestCallBack<String> callback) {
		try {
			paramsJson = paramsJson.substring(1, paramsJson.length() - 1);
			paramsJson = paramsJson.replaceAll("\"", "\'");
			LogUtils.i("paramjson=="+paramsJson);
			RequestParams params = new RequestParams("utf-8");
			
			File file = new File(filePath);
//			params.setHeader("uploadFileName", file.getName());
//			params.setHeader("ua", paramsJson);
			FileUploadEntity body = new FileUploadEntity(file, "image/jpeg");
//			FileUploadEntity body = new FileUploadEntity(file, "image/jpeg");
//			params.setBodyEntity(body);				
//			params.setContentType("text/plain");
			params.addBodyParameter("ua", paramsJson);
			if (!(file.getName().contains(".jpg"))&&!(file.getName().contains(".png"))) {
				params.addBodyParameter("file", file, file.getName()+".jpg",
						"image/jpeg", "utf-8");
			}else{
				params.addBodyParameter("file", file, file.getName(),
						"image/jpeg", "utf-8");
			}
			
			LogUtils.i("fileSize: " + file.length());
			LogUtils.i("uploadFileName: " + file.getName());
			LogUtils.i("params: " + params.toString());
			FileUploadHttpHelper.send(HttpMethod.POST, Constant.FILEURL,
					params, callback);
			
		} catch (Exception e) {
			
		}
	}
	/**
	 * 多文件上传
	 * @param filePaths
	 * @param paramsJson
	 * @param callback
	 */
	public void uploads(List<String> filePaths,String paramsJson, RequestCallBack<String> callback) {
		try {
			paramsJson = paramsJson.substring(1, paramsJson.length() - 1);
			paramsJson = paramsJson.replaceAll("\"", "\'");
			LogUtils.i("paramjson=="+paramsJson);
			RequestParams params = new RequestParams("utf-8");
			params.addBodyParameter("ua", paramsJson);
			
			for (int i = 0; i < filePaths.size(); i++) {
				File file = new File(filePaths.get(i));
				FileUploadEntity body = new FileUploadEntity(file, "image/jpeg");
				params.addBodyParameter("file"+i, file, file.getName(),
						"image/jpeg", "utf-8");
				LogUtils.i("fileSize: " + file.length());
				LogUtils.i("uploadFileName: " + file.getName());
			}
			
			LogUtils.i("params: " + params.toString());
			
			FileUploadHttpHelper.send(HttpMethod.POST, Constant.FILEURL,
					params, callback);
			
		} catch (Exception e) {
			
		}
	}
	
	
	/**
	 * 多文件上传，此项目中此方法暂不适用
	 * @param filePath 文件路径
	 * @param callBack 结果回调
	 */
	public void upload(RequestCallBack<String> callback, String... filePath) { 
		if (null != filePath && filePath.length > 0) {
			RequestParams params = new RequestParams("utf-8");
			for (int i = 0; i < filePath.length; i++) {
				File file = new File(filePath[i]);
				params.addBodyParameter("file" + i, file, file.getName(),
						"image/jpeg", "utf-8");
				LogUtils.i("fileSize: " + file.length());
				LogUtils.i("uploadFileName: " + file.getName());
			}
			// params.addBodyParameter("username","彭欣");
			// params.addBodyParameter("password","000000");
			FileUploadHttpHelper.send(HttpMethod.POST, Constant.FILEURL,
					params, callback);
		}
	}
	
	// public void uploadFile() {
		// HttpURLConnection conn = null;
		// OutputStream dos = null;
		// int bytesRead;
		// byte[] buffer;
		// File sourceFile = new File(path);
		// String fileName = sourceFile.getName();
		//
		// try {
		//
		// // open a URL connection to the Servlet
		// FileInputStream fileInputStream = new FileInputStream(sourceFile);
		// URL url = new URL(SPConstants.Net.UPLOAD);
		//
		// // Open a HTTP connection to the URL
		// conn = (HttpURLConnection) url.openConnection();
		// conn.setDoInput(true); // Allow Inputs
		// conn.setDoOutput(true); // Allow Outputs
		// conn.setUseCaches(false); // Don't use a Cached Copy
		// conn.setRequestMethod("POST");
		// conn.setRequestProperty("Connection", "Keep-Alive");
		// conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		// conn.setRequestProperty("uploadFileName", fileName);
		// conn.setRequestProperty("Content-Type", "image/jpeg");
		//
		// conn.connect();
		// dos = conn.getOutputStream();
		//
		// // dos.writeBytes(twoHyphens + boundary + lineEnd);
		// // dos.writeBytes("Content-Disposition: form-data; name="
		// // + fileName + ";filename=" + fileName + "" + lineEnd);
		// //
		// // dos.writeBytes(lineEnd);
		//
		// buffer = new byte[4096*10];
		//
		// // read file and write it into form...
		// bytesRead = fileInputStream.read(buffer, 0, buffer.length);
		// int leng = 0;
		// while (bytesRead > 0) {
		// dos.write(buffer, 0, bytesRead);
		// bytesRead = fileInputStream.read(buffer, 0, buffer.length);
		// LogUtils.i("length" + (leng += bytesRead));
		// }
		//
		// // send multipart form data necesssary after file data...
		// // dos.writeBytes(lineEnd);
		// // dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		//
		// // Responses from the server (code and message)
		//
		// // close the streams //
		// fileInputStream.close();
		// dos.flush();
		// dos.close();
		// LogUtils.i("--------------close---------------");
		// BufferedReader reader = new BufferedReader(new InputStreamReader(
		// conn.getInputStream()));
		// String line = null;
		// String result = "";
		// while ((line = reader.readLine()) != null) {
		// result += line;
		// }
		// reader.close();
		// conn.disconnect();
		// LogUtils.i("result" + new String(result.getBytes(), "utf-8"));
		// uploadMethod();
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		//
		// }
}
