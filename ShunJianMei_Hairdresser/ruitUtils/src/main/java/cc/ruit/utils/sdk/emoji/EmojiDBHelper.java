package cc.ruit.utils.sdk.emoji;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * emoji数据库
 */
public class EmojiDBHelper {

	private static final String TAG = "EmojiDBHelper";

	// private static final String ID = "id";
	private static final String UNICODE = "unicode";
	private static final String UTF8 = "utf8";
	private static final String UTF16 = "utf16";
	private static final String SBUNICODE = "sbunicode";
	private static final String FILENAME = "filename";

	/**
	 * 同步锁
	 */
	private final Object synObj = new Object();

	/**
	 * 用来操作的数据库的实例
	 */
	private SQLiteDatabase db = null;

	private Context context;

	public EmojiDBHelper(Context context) {
		this.context = context;
	}

	private SQLiteDatabase openDatabase() {
		// 获得db文件的绝对路径
		String emoji_db = null;
//		String emoji_db = Constant.DATABASE_PATH + Constant.DATABASE_EMOJI;
//		File dir = new File(Constant.DATABASE_PATH);
		File dir = null;
		if (!dir.exists()) { // 目录不存在，创建
			dir.mkdir();
		}
		// 如果在data目录中不存在
		// db文件，则从res\raw目录中复制这个文件到data
		if (!(new File(emoji_db)).exists()) {
			// 获得封装dictionary.db文件的InputStream对象
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				//读取存储在raw或者assests中的表情文件
//				is = context.getResources().openRawResource(R.raw.emoji);
				fos = new FileOutputStream(emoji_db);
				byte[] buffer = new byte[1024 * 10];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// 打开data目录中的db文件
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(emoji_db,
				null);
		return database;
	}

	/**
	 * 对数据进行的查询操作
	 * 
	 * @param tableName
	 *            需要操作的表名
	 * @param whereArgs
	 *            要查询的数据中提供的条件参数的名称
	 * @param whereArgsValues
	 *            要查询的数据中提供的条件参数的值
	 * @param column
	 *            控制哪些字段返回结果（传null是返回所有字段的结果集）
	 * @param orderBy
	 *            是否对某一字段进行排序（传null不进行排序）
	 * @return 查找的数据集的指针
	 */
	public Cursor query() {
		Cursor cursor = null;
		db = openDatabase();
		if (db == null) {
			throw new NullPointerException();
		}
		db.beginTransaction();
		try {
			cursor = db.query("ios_emoji", null, null, null, null, null, null);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return cursor;
	}

	/**
	 * 获取所有
	 * 
	 * @return
	 */
	public List<ChatEmoji> getEmojiList() {

		// 数据读取的同步操作
		synchronized (synObj) {
			List<ChatEmoji> emojiList = new ArrayList<ChatEmoji>();

			Cursor cursor = query();
			// 判空操作
			if (cursor == null) {
				return emojiList;
			}
			ChatEmoji item = null;
			try {
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					do {
						item = new ChatEmoji();
						// item.setCharacter("["
						// + cursor.getString(cursor
						// .getColumnIndex(UNICODE)) + "]");
						item.setCharacter(cursor.getString(
								cursor.getColumnIndex(FILENAME)).replace(
								".png", ""));
						item.setUtf8(cursor.getString(cursor
								.getColumnIndex(UTF8)));
						item.setUtf16(cursor.getString(cursor
								.getColumnIndex(UTF16)));
						item.setSbunicode(cursor.getString(cursor
								.getColumnIndex(SBUNICODE)));
						item.setFaceName(cursor
								.getString(cursor.getColumnIndex(FILENAME))
								.replace(".png", "").replace("[", "")
								.replace("]", ""));
						emojiList.add(item);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// 关闭游标
				cursor.close();
				cursor = null;
				db.close();// 注：数据库关闭要在游标后
			}
			return emojiList;
		}
	}
}