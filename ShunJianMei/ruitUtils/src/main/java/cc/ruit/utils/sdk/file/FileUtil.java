package cc.ruit.utils.sdk.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

@SuppressLint("NewApi")
public class FileUtil {

	public static boolean saveInputStreamToFile(InputStream inStream,
			String destFileName) {

		FileOutputStream outPuts = null;
		BufferedInputStream brAtt = null;
		File attFile = new File(destFileName);
		try {
			outPuts = new FileOutputStream(attFile);
			byte[] byteData = new byte[1024];
			brAtt = new BufferedInputStream(inStream);
			int len = 0;
			while ((len = brAtt.read(byteData)) != -1) {
				outPuts.write(byteData, 0, len);
			}
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outPuts.close();
			} catch (IOException e) {
			}
		}
		return false;
	}

	public static void saveTextToFile(String fileName, String text,
			String charset) throws FileNotFoundException, IOException {

		String path = extractFilePath(fileName);
		String name = extractFileName(fileName);
		java.io.File file = new java.io.File(path, name);
		FileOutputStream out = new FileOutputStream(file);
		addUTF8Head(charset, out);
		OutputStreamWriter writer = new OutputStreamWriter(out, charset);
		writer.write(text);
		writer.flush();
		writer.close();
		out.close();
	}

	public static void saveDataToFile(String fileName, byte[] data)
			throws FileNotFoundException, IOException {
		String path = extractFilePath(fileName);
		String name = extractFileName(fileName);
		java.io.File file = new java.io.File(path, name);
		FileOutputStream out = new FileOutputStream(file);
		out.write(data);
		out.flush();
		out.close();
	}

	public static boolean writeDataToFile(File file, String data, boolean append) {
		FileWriter filerWriter = null;
		BufferedWriter bufWriter = null;
		try {
			// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			filerWriter = new FileWriter(file, append);
			bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(data);
			bufWriter.newLine();
		} catch (NotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (bufWriter != null) {
					bufWriter.close();
				}
				if (filerWriter != null) {
					filerWriter.close();
				}
			} catch (Exception e) {
			}
		}
		return true;

	}

	public static boolean updateDate2File(String fileName, String data,
			boolean superaddition) {
		FileWriter filerWriter = null;
		BufferedWriter bufWriter = null;
		try {
			File file = new File(fileName);
			// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			filerWriter = new FileWriter(file, superaddition);
			bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(data);
			bufWriter.newLine();
		} catch (NotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (bufWriter != null) {
					bufWriter.close();
				}
				if (filerWriter != null) {
					filerWriter.close();
				}
			} catch (Exception e) {
			}
		}
		return true;

	}

	static public String loadTextFromFile(String file) {
		try {
			if (isFileUTF16(file))
				return loadTextFromFile(file, "UTF-16");
			else if (isFileUTF8(file))
				return loadTextFromFile(file, "UTF-8");
			else {
				return loadTextFromFile(file, "UTF-8-nobom");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";

	}

	// 读取指定text文件
	public static String loadTextFromFile(String fileName, String charset)
			throws FileNotFoundException, IOException {
		//
		int offset = 0;
		if (charset.equalsIgnoreCase("utf-8")) {
			offset = 3;
		} else if (charset.equalsIgnoreCase("utf8")) {
			charset = "utf-8";
			offset = 3;
		} else if (charset.equalsIgnoreCase("unicode")) {
			charset = "UTF-16LE";
			offset = 2;
		} else if (charset.equalsIgnoreCase("utf-16")) {
			charset = "UTF-16LE";
			offset = 2;
		} else if (charset.equalsIgnoreCase("UTF-16LE")) {
			offset = 2;
		}
		if (charset.equalsIgnoreCase("utf-8-nobom")) {
			offset = 0;
			charset = "utf-8";
		} else if (charset.equalsIgnoreCase("utf8-nobom")) {
			offset = 0;
			charset = "utf-8";
		}

		//
		byte[] arr = loadByteFromFile(fileName, offset);
		if (charset == null || charset.length() == 0) {
			return new String(arr);
		} else {
			return new String(arr, charset);
		}
	}

	// 读取字节文件
	public static byte[] loadByteFromFile(String fileName, int offset)
			throws FileNotFoundException, IOException {
		byte[] ret = null;
		String path = extractFilePath(fileName);
		String name = extractFileName(fileName);
		File file = null;
		FileInputStream stream = null;
		try {
			file = new File(path, name);
			stream = new FileInputStream(file);
			int length = stream.available();
			ret = new byte[length - offset];
			stream.skip(offset);

			stream.read(ret, 0, length - offset);
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
			}
		}
		return ret;
	}

	public static InputStream getInputStreamFromAssets(Context ctx,
			String fileName) {
		try {
			AssetManager am = ctx.getAssets();
			return am.open(fileName);
		} catch (Exception e) {
			return null;
		}
	}

	public static String loadStringFromAssetsFile(Context ctx, String fileName) {
		String text = "";
		InputStream is = null;
		try {
			is = getInputStreamFromAssets(ctx, fileName);
			text = loadTextFromStream(is);
		} catch (Exception e) {
			text = "";
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
			}
		}
		return text;
	}

	public static boolean copyDirectory(String srcDir, String destDir) {
		if (!fileExists(srcDir))
			return false;

		if (!ensurePathExists(destDir))
			return false;
		try {
			File srcFile = new File(srcDir);
			File[] files = srcFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file == null)
					continue;
				if (file.isDirectory()) {
					String destpath = pathAddBackslash(destDir)
							+ file.getName();
					copyDirectory(file.getAbsolutePath(), destpath);
				} else if (file.isFile()) {
					File destFile = new File(destDir, file.getName());
					copyFile(file, destFile);
				}
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean copyAssetsFile(Context ctx, String srcFile,
			String destFile) {
		InputStream is = getInputStreamFromAssets(ctx, srcFile);
		OutputStream out = null;
		try {
			out = new java.io.FileOutputStream(destFile);
			copyFile(is, out);
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
			try {
				out.close();
			} catch (Exception e) {
			}

		}
	}

	// 拷贝file
	public static boolean copyFile(String srcFileName, String destFileName)
			throws FileNotFoundException, IOException {
		File srcFile = new File(srcFileName);
		File destFile = new File(destFileName);
		return copyFile(srcFile, destFile);
	}

	public static boolean copyFile(File srcFile, File destFile)
			throws FileNotFoundException, IOException {

		java.io.FileInputStream stream = null;
		java.io.FileOutputStream out = null;
		try {
			stream = new java.io.FileInputStream(srcFile);
			out = new java.io.FileOutputStream(destFile);
			return copyFile(stream, out);
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public static boolean copyFile(InputStream is, OutputStream out)
			throws FileNotFoundException, IOException {
		BufferedInputStream brAtt = null;
		try {
			byte[] byteData = new byte[1024];
			brAtt = new BufferedInputStream(is);
			int len = 0;
			while ((len = brAtt.read(byteData)) != -1) {
				out.write(byteData, 0, len);
			}
			return true;
		} finally {
			brAtt.close();
		}
	}

	static public boolean copyImageFile(String srcPath, String destPath,
			String fileName) {
		if (isImageFile(fileName)) {
			srcPath = pathAddBackslash(srcPath);
			destPath = pathAddBackslash(destPath);
			String srcFile = srcPath + fileName;
			String destFile = destPath + fileName;
			try {
				if (!fileExists(destFile))
					copyFile(srcFile, destFile);

				return true;
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}

	public static boolean moveDirectory(String srcPath, String destPath) {
		if (FileUtil.copyDirectory(srcPath, destPath))
			return FileUtil.deleteDirectory(srcPath);

		return false;
	}

	// 转移指定文件
	public static boolean moveSpecificFiles(String oldFile, String newFile)
			throws FileNotFoundException, IOException {

		File oldfile = new File(oldFile);
		File newfile = new File(newFile);
		return moveFile(oldfile, newfile);
	}

	// 转移指定文件
	public static boolean moveFile(String srcFileName, String destFileName)
			throws FileNotFoundException, IOException {
		File srcFile = new File(srcFileName);
		File destFile = new File(destFileName);
		return moveFile(srcFile, destFile);
	}

	public static boolean moveFile(File srcFileName, File destFileName)
			throws FileNotFoundException, IOException {
		return srcFileName.renameTo(destFileName);
	}

	// 删除文件
	public static boolean deleteFile(String filename) {
		java.io.File file = new java.io.File(filename);
		return deleteFile(file);
	}

	// 删除文件
	public static boolean deleteFile(File file) {
		try {
			if (fileExists(file))
				file.delete();

			return true;
		} catch (NotFoundException e) {
			return false;
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param dir
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true,否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		if (!pathExists(dir))
			return true;
		//
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(java.io.File.separator)) {
			dir = dir + java.io.File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		// 删除文件夹下的所有文件(包括子目录)
		java.io.File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				deleteFile(files[i].getAbsolutePath());
			} else {
				// 删除子目录
				deleteDirectory(files[i].getAbsolutePath());
			}
		}

		// 删除当前目录
		return dirFile.delete();
	}

	/**
	 * : 删除目录下的文件 不删除目录下的目录
	 * 
	 * @param dir
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true,否则返回false
	 */
	public static boolean clearDirectory(String dir) {
		if (!pathExists(dir))
			return true;
		//
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(java.io.File.separator)) {
			dir = dir + java.io.File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		// 删除文件夹下的所有文件(包括子目录)
		java.io.File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				deleteFile(files[i].getAbsolutePath());
			} else {
				// 删除子目录
				clearDirectory(files[i].getAbsolutePath());
			}
		}

		return true;
	}

	// 文件查找
	public static boolean fileExists(String fileName) {
		try {
			String path = extractFilePath(fileName);// 获取文件路径
			String name = extractFileName(fileName);// 获取文件名
			java.io.File file = new java.io.File(path, name);
			boolean bExists = file.exists();// 判断是否能在path中找到name文件，如果找到返回true否则返回false
			return bExists;
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
	}

	// 文件查找
	public static boolean fileExists(File file) {
		try {
			return file.exists();
		} catch (Exception err) {
			return false;
		}
	}

	// 文件查找
	public static boolean fileExists(android.content.Context ctx, String name) {
		String[] files = ctx.fileList();
		for (int i = 0; i < files.length; i++) {
			if (files[i].equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	// 判断目录
	public static boolean pathExists(String path) {
		try {
			java.io.File myFilePath = new java.io.File(path);
			return myFilePath.exists();
		} catch (Exception err) {
			return false;
		}
	}

	// 从流中 读取文件
	public static String loadTextFromStream(InputStream stream) {
		try {
			ByteArrayOutputStream streamNew = null;
			try {
				streamNew = new ByteArrayOutputStream();
				byte[] buf = new byte[4096];
				while (true) {
					int read = stream.read(buf);
					if (read <= 0)
						break;
					streamNew.write(buf, 0, read);
				}
				//
				String charset;
				int offset = 0;
				//
				byte[] all = streamNew.toByteArray();
				if (all.length >= 2 && all[0] == (byte) 0xFF
						&& all[1] == (byte) 0xFE) {
					charset = "UTF-16LE";
					offset = 2;
				} else if (all.length >= 3 && all[0] == (byte) 0xEF
						&& all[1] == (byte) 0xBB && all[2] == (byte) 0xBF) {
					charset = "UTF-8";
					offset = 3;
				} else {
					charset = "UTF-8";
					offset = 0;
				}
				//
				return new String(all, offset, all.length - offset, charset);
			} finally {
				if (streamNew != null) {
					streamNew.close();
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	public static void addUTF8Head(String charset, FileOutputStream out) {
		if (charset.equals("utf-8") || charset.equals("UTF-8")) {
			byte[] b = { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
			try {
				out.write(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 获取文件类型
	public static String extractFileExt(String filename) {
		return extractFileExt(filename, "");
	}

	public static String extractFileExt(String filename, String defExt) {
		if (!TextUtils.isEmpty(filename)) {
			int i = filename.lastIndexOf('.');

			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(i);
			}
		}
		return defExt;
	}

	// 获取文件的Title部分(不包含.后面的内容也即文件类型)
	public static String extractFileTitle(String filename) {
		String title = extractFileName(filename);
		//
		if ((title != null) && (title.length() > 0)) {
			int i = title.lastIndexOf('.');
			if ((i > -1) && (i < (title.length()))) {
				return title.substring(0, i);
			}
		}
		return title;
	}

	// 获取文件路径
	public static String extractFilePath(String filename) {
		filename = pathRemoveBackslash(filename);
		//
		int pos = filename.lastIndexOf('/');
		if (-1 == pos)
			return "";
		return filename.substring(0, pos);
	}

	// 获取目录
	public static String[] getFilePath(String filename) {

		String[] paths = null;

		if (getPathSlash(filename)) {
			paths = filename.split("/");
			if (paths.length == 0) {
				paths = filename.split("\\");
			}
		} else {
			filename = filename + "/";
			paths = filename.split("/");
		}

		return paths;
	}

	// 判断字符段中是否存在反斜线
	static boolean getPathSlash(String path) {
		if (TextUtils.isEmpty(path))
			return false;
		//
		if (path.indexOf("/") == -1 && path.indexOf("\\") == -1) {
			return false;
		}
		return true;
	}

	// 获取文件名
	public static String extractFileName(String filename) {
		if (!TextUtils.isEmpty(filename) && getPathSlash(filename)) {
			int i = filename.lastIndexOf('/');
			if (i < 0)
				i = filename.lastIndexOf("\\");
			//
			if ((i > -1) && (i < (filename.length()))) {
				return filename.substring(i + 1);
			}
		}
		return filename;
	}

	public static long getFileSize(String fileName) {
		try {
			java.io.File file = new java.io.File(fileName);
			return file.length();
		} catch (Exception e) {
			return 0;
		}
	}

	public static boolean isFileUTF16(String str) {
		java.io.InputStream is = null;
		byte[] b = new byte[2];
		try {
			is = new FileInputStream(new File(str));
			is.read(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
			}
		}
		if (b[0] == (byte) 0xff && b[1] == (byte) 0xfe)
			return true;
		return false;
	}

	public static boolean isFileUTF8(String str) {
		java.io.InputStream is = null;
		byte[] b = new byte[3];
		try {
			is = new FileInputStream(new File(str));
			is.read(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (b[0] == (byte) 0xef && b[1] == (byte) 0xbb && b[2] == (byte) 0xbf)
			return true;
		return false;
	}

	// static public String getJpegFile(String name) {
	// String file = null;
	// String type = getTypeForFile(name);
	// if (!type.equals(".gif") && getFileType(type).equals(ATTTYPE_JPG))
	// file = name;
	//
	// return file;
	// }

	// // 取得较大的文件
	// public static File getLargeJpeg(File[] files) {
	// File file = null;
	// for (int i = 0; i < files.length; i++) {
	// String name = files[i].getName();
	// if (TextUtils.isEmpty(getJpegFile(name)))
	// continue;
	//
	// if (file == null || file.length() < files[i].length())
	// file = files[i];
	//
	// }
	// return file;
	// }

	// // icon img type
	// public static final int iconOfAudio = 3001;// R.drawable.icon_img_audio
	// public static final int iconOfVideo = 3002;// R.drawable.icon_img_video
	// public static final int iconOfWord = 3002;// R.drawable.icon_img_word
	// public static final int iconOfExcel = 3003;// R.drawable.icon_img_excel
	// public static final int iconOfPPt = 3004;// R.drawable.icon_img_ppt
	// public static final int iconOfFile = 3005;// R.drawable.icon_img_file
	//
	// public static int iniAttIconId(String nameType) {
	// String fileType = getFileType(nameType);
	// if (TextUtils.equals(fileType, ATTTYPE_JPG)) {
	// return 0;
	// } else if (TextUtils.equals(fileType, ATTTYPE_MP3)) {
	// return iconOfAudio;
	// } else if (TextUtils.equals(fileType, ATTTYPE_RMVB)) {
	// return iconOfVideo;
	// } else if (TextUtils.equals(fileType, ATTTYPE_WORD)) {
	// return iconOfWord;
	// } else if (TextUtils.equals(fileType, ATTTYPE_EXECL)) {
	// return iconOfExcel;
	// } else if (TextUtils.equals(fileType, ATTTYPE_PPT)) {
	// return iconOfPPt;
	// } else {
	// return iconOfFile;
	// }
	// }
	public enum FileType {
		AUDIO, IMAGE, VIDEO, WEB_PAGE, WEB_STYLE, WORD, PPT, EXCEL, TXT, PDF, OTHERS
	}

	@SuppressLint("DefaultLocale")
	private static FileType getFileTypeByFileExtension(String fileExtension) {

		if (TextUtils.isEmpty(fileExtension))
			return FileType.OTHERS;

		fileExtension = fileExtension.toLowerCase();
		if (fileExtension.equals(".jpg") || fileExtension.equals(".png")
				|| fileExtension.equals(".jpeg")
				|| fileExtension.equals(".gif") || fileExtension.equals(".bmp")) {
			return FileType.IMAGE;
		} else if (fileExtension.equals(".amr") || fileExtension.equals(".mp3")
				|| fileExtension.equals(".wav")) {
			return FileType.AUDIO;
		} else if (fileExtension.equals(".avi")
				|| fileExtension.equals(".rmvb")
				|| fileExtension.equals(".mp4")) {
			return FileType.VIDEO;
		} else if (fileExtension.equals(".doc")
				|| fileExtension.equals(".docx")) {
			return FileType.WORD;
		} else if (fileExtension.equals(".xls")
				|| fileExtension.equals(".xlsx")) {
			return FileType.EXCEL;
		} else if (fileExtension.equals(".ppt")
				|| fileExtension.equals(".pptx")) {
			return FileType.PPT;
		} else if (fileExtension.equals(".html")
				|| fileExtension.equals(".xhtml")
				|| fileExtension.equals(".mht")) {
			return FileType.WEB_PAGE;
		} else if (fileExtension.equals(".css")) {
			return FileType.WEB_STYLE;
		} else if (fileExtension.equals(".txt")) {
			return FileType.TXT;
		} else if (fileExtension.equals(".pdf")) {
			return FileType.PDF;
		} else {
			return FileType.OTHERS;
		}

	}

	public static boolean isImageFile(String filePath) {
		return FileType.IMAGE == getFileTypeByFilePath(filePath);
	}

	public static boolean isWebPage(String filePath) {
		return FileType.WEB_PAGE == getFileTypeByFilePath(filePath);
	}

	public static boolean isWebStyle(String filePath) {
		return FileType.WEB_STYLE == getFileTypeByFilePath(filePath);
	}

	// public static int getIntTypeFile(String fileName) {
	// String nameType = getTypeForFile(fileName);
	// return iniAttIconId(nameType);
	// }

	/**
	 * 通过文件获取文件名类型
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileExtensionByFilePath(String filePath) {
		String name = extractFileName(filePath);
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf < 0)
			return "";
		return name.substring(lastIndexOf);
	}

	public static FileType getFileTypeByFilePath(String filePath) {
		return getFileTypeByFileExtension(getFileExtensionByFilePath(filePath));
	}

	/**
	 * wiz mime类型匹配 优先匹配此规则，没有的话使用系统带有的mime类型。
	 */
	private static HashMap<String, String> mWizMimeType = new HashMap<String, String>();
	static {
		mWizMimeType.put("epub", "application/epub+zip");
		mWizMimeType.put("mobi", "application/x-mobipocket-ebook");
		mWizMimeType.put("umd", "application/umd");
		mWizMimeType.put("flv", "video/x-flv");
	}

	private static String getOpenFileType(String file_CanonicalPath) {
		String extension = MimeTypeMap
				.getFileExtensionFromUrl(file_CanonicalPath);
		String fileType = mWizMimeType.get(extension);
		if (fileType != null) {
			return fileType;
		}
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
	}

	public static boolean reSaveFileToUTF8(String fileName) {
		try {
			if (isFileUTF16(fileName)) {
				String str = loadTextFromFile(fileName, "utf-16");
				saveTextToFile(fileName, str, "utf-8");
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	static public String getFileMIMEType(File info) {
		String filePant = info.getPath();
		String type = "";
		try {
			String file_CanonicalPath = info.getCanonicalPath();
			type = FileUtil.getOpenFileType(file_CanonicalPath);
			if (TextUtils.isEmpty(type)) {
				int lastIndex = filePant.lastIndexOf(".");
				String infoName = "wiz" + filePant.substring(lastIndex);

				File new_file = new File(infoName);
				String new_CanonicalPath = new_file.getCanonicalPath();

				type = FileUtil.getOpenFileType(new_CanonicalPath);
			}

		} catch (Exception e) {
		}
		return type;

	}

	/***
	 * create new Directory,
	 * 
	 * return true Said create directory success, return false Said create
	 * directory faild
	 * 
	 * @param path
	 * @return
	 */
	public static boolean ensurePathExists(String path) {
		String dir = pathAddBackslash(path);
		File myFilePath = new File(dir);
		if (myFilePath.exists())
			return true;
		//
		synchronized (FileUtil.class) {

			int count = 0;
			while (count < 10) {
				if (!myFilePath.mkdirs()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
					myFilePath.delete();
					continue;
				}

				return true;
			}
			return false;
		}
	}

	public static boolean isSdCardAvailable(Context context, String path) {
		try {
			return pathExists(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 私有数据目录
	public static String getRamRootPath(Context ctx) {
		File file = ctx.getFilesDir();
		String path = file.getAbsolutePath();
		return pathAddBackslash(path);
	}

	// sd card cache
	public static String getAppCacheFilePath(Context ctx) {
		File file = null;
		try {
			file = ctx.getExternalCacheDir();
		} catch (NoSuchMethodError e) {
			e.printStackTrace();
			file = getExternalCacheDir(ctx);
		}

		return file.getPath();
	}

	/**
	 * 为2.2以下版本提供兼容
	 * 
	 * @param context
	 * @return
	 */
	private static File getExternalCacheDir(final Context context) {
		// return context.getExternalCacheDir(); API level 8

		// e.g. "<sdcard>/Android/data/<package_name>/cache/"
		final File extCacheDir = new File(
				Environment.getExternalStorageDirectory(), "/Android/data/"
						+ context.getApplicationInfo().packageName + "/cache/");
		extCacheDir.mkdirs();
		return extCacheDir;
	}

	// 返回临时文件夹路径
	// 用于浏览笔记，编辑笔记，程序退出的时候会清空。
	public static String getCacheRootPath(Context ctx) {
		try {
			String basePath = getAppCacheFilePath(ctx);
			basePath = pathAddBackslash(basePath);
			FileUtil.ensurePathExists(basePath);
			//
			try {
				String noMediaFileName = pathAddBackslash(basePath)
						+ ".nomedia";
				if (!FileUtil.fileExists(noMediaFileName)) {
					FileUtil.saveTextToFile(noMediaFileName, "", "utf-8");
				}
			} catch (Exception err) {
			}
			return basePath;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getRamResourcePath(Context ctx) {
		String basePath = getRamRootPath(ctx);
		basePath = pathAddBackslash(basePath);
		String path = basePath + "res";
		path = pathAddBackslash(path);
		ensurePathExists(path);
		return path;
	}

	public static String getRamJavaScriptPath(Context ctx) {
		String basePath = getRamResourcePath(ctx);
		basePath = pathAddBackslash(basePath);
		String path = basePath + "javascript";
		path = pathAddBackslash(path);
		ensurePathExists(path);
		return path;
	}

	public static String getRamImagePath(Context ctx) {
		String basePath = getRamResourcePath(ctx);
		basePath = pathAddBackslash(basePath);
		String path = basePath + "img";
		path = pathAddBackslash(path);
		ensurePathExists(path);
		return path;
	}

	public static String getRamDBPath(Context ctx) {
		String basePath = getRamResourcePath(ctx);
		basePath = pathAddBackslash(basePath);
		String path = basePath + "db";
		path = pathAddBackslash(path);
		ensurePathExists(path);
		return path;
	}

	public static String getRamCssPath(Context ctx) {
		String basePath = getRamResourcePath(ctx);
		basePath = pathAddBackslash(basePath);
		String path = basePath + "css";
		path = pathAddBackslash(path);
		ensurePathExists(path);
		return path;
	}

	public static String getRamRecordPath(Context ctx) {
		String basePath = getRamResourcePath(ctx);
		basePath = pathAddBackslash(basePath);
		String path = basePath + "record";
		path = pathAddBackslash(path);
		ensurePathExists(path);
		return path;
	}

	//
	// 去掉path中的反斜线
	public static String pathRemoveBackslash(String path) {
		if (path == null)
			return path;
		if (path.length() == 0)
			return path;
		//
		char ch = path.charAt(path.length() - 1);
		if (ch == '/' || ch == '\\')
			return path.substring(0, path.length() - 1);
		return path;

	}

	// 在path中添加反斜线
	public static String pathAddBackslash(String path) {
		if (path == null)
			return java.io.File.separator;
		if (path.length() == 0)
			return java.io.File.separator;
		//
		char ch = path.charAt(path.length() - 1);
		if (ch == '/' || ch == '\\')
			return path;
		return path + java.io.File.separator;
	}

	public static ArrayList<String> loadas(Context ctx, String fileName)
			throws IOException {
		InputStream is = getInputStreamFromAssets(ctx, fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String tempString = null;
		ArrayList<String> list = new ArrayList<String>();
		while ((tempString = reader.readLine()) != null) {
			if (TextUtils.isEmpty(tempString))
				continue;
			list.add(tempString);
		}
		reader.close();
		return list;
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return double 单位为M
	 * @throws Exception
	 */
	public static double getFolderSize(java.io.File file) throws Exception {
		double size = 0;
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return Math.round((size / 1048576)*100)/100;
	}
	
	
	/** 把Uri转化成文件路径 */
	 public static String uri2filePath(Context context,Uri uri){
		 if (Build.VERSION.SDK_INT >= 19) {
			String res = null;
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
			if (cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				res = cursor.getString(index);
			}
			cursor.close();
			return res;
		}else {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
			int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(index);
			return getPath(context,uri);
		}
	}

	/** 把Uri转化成文件路径 */
	public String getRealPathFromURI(Context context, Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj,
				null, null, null);
		if (cursor.moveToFirst()) {
			;
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}
	 	@TargetApi(Build.VERSION_CODES.KITKAT)
		@SuppressLint("NewApi")
		public static String getPath(final Context context, final Uri uri) {

			final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

			// DocumentProvider
			if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
				// ExternalStorageProvider
				if (isExternalStorageDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];

					if ("primary".equalsIgnoreCase(type)) {
						return Environment.getExternalStorageDirectory() + "/"
								+ split[1];
					}

					// TODO handle non-primary volumes
				}
				// DownloadsProvider
				else if (isDownloadsDocument(uri)) {

					final String id = DocumentsContract.getDocumentId(uri);
					final Uri contentUri = ContentUris.withAppendedId(
							Uri.parse("content://downloads/public_downloads"),
							Long.valueOf(id));

					return getDataColumn(context, contentUri, null, null);
				}
				// MediaProvider
				else if (isMediaDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];

					Uri contentUri = null;
					if ("image".equals(type)) {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					} else if ("video".equals(type)) {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					} else if ("audio".equals(type)) {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					}

					final String selection = "_id=?";
					final String[] selectionArgs = new String[] { split[1] };

					return getDataColumn(context, contentUri, selection,
							selectionArgs);
				}
			}
			// MediaStore (and general)
			else if ("content".equalsIgnoreCase(uri.getScheme())) {

				// Return the remote address
				if (isGooglePhotosUri(uri))
					return uri.getLastPathSegment();

				return getDataColumn(context, uri, null, null);
			}
			// File
			else if ("file".equalsIgnoreCase(uri.getScheme())) {
				return uri.getPath();
			}

			return null;
		}

		/**
		 * Get the value of the data column for this Uri. This is useful for
		 * MediaStore Uris, and other file-based ContentProviders.
		 *
		 * @param context
		 *            The context.
		 * @param uri
		 *            The Uri to query.
		 * @param selection
		 *            (Optional) Filter used in the query.
		 * @param selectionArgs
		 *            (Optional) Selection arguments used in the query.
		 * @return The value of the _data column, which is typically a file path.
		 */
		public static String getDataColumn(Context context, Uri uri,
				String selection, String[] selectionArgs) {

			Cursor cursor = null;
			final String column = "_data";
			final String[] projection = { column };

			try {
				cursor = context.getContentResolver().query(uri, projection,
						selection, selectionArgs, null);
				if (cursor != null && cursor.moveToFirst()) {
					final int index = cursor.getColumnIndexOrThrow(column);
					return cursor.getString(index);
				}
			} finally {
				if (cursor != null)
					cursor.close();
			}
			return null;
		}

		/**
		 * @param uri
		 *            The Uri to check.
		 * @return Whether the Uri authority is ExternalStorageProvider.
		 */
		public static boolean isExternalStorageDocument(Uri uri) {
			return "com.android.externalstorage.documents".equals(uri
					.getAuthority());
		}

		/**
		 * @param uri
		 *            The Uri to check.
		 * @return Whether the Uri authority is DownloadsProvider.
		 */
		public static boolean isDownloadsDocument(Uri uri) {
			return "com.android.providers.downloads.documents".equals(uri
					.getAuthority());
		}

		/**
		 * @param uri
		 *            The Uri to check.
		 * @return Whether the Uri authority is MediaProvider.
		 */
		public static boolean isMediaDocument(Uri uri) {
			return "com.android.providers.media.documents".equals(uri
					.getAuthority());
		}

		/**
		 * @param uri
		 *            The Uri to check.
		 * @return Whether the Uri authority is Google Photos.
		 */
		public static boolean isGooglePhotosUri(Uri uri) {
			return "com.google.android.apps.photos.content".equals(uri
					.getAuthority());
		}
}
