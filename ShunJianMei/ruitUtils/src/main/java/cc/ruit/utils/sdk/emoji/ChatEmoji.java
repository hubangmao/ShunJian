package cc.ruit.utils.sdk.emoji;

/**
 * 表情对象实体
 */
public class ChatEmoji {

	/** 表情资源图片对应生成的资源ID */
	private int id;

	/** 表情资源对应的文字描述 */
	private String character;

	/** 表情资源的文件名 */
	private String faceName;

	/** 表情资源的unicode */
	private String unicode;

	/** 表情资源的utf8 */
	private String utf8;

	/** 表情资源的utf16 */
	private String utf16;

	/** 表情资源的sbunicode */
	private String sbunicode;

	/** 表情资源图片对应的ID */
	public int getId() {
		return id;
	}

	/** 表情资源图片对应的ID */
	public void setId(int id) {
		this.id = id;
	}

	/** 表情资源对应的文字描述 */
	public String getCharacter() {
		return character;
	}

	/** 表情资源对应的文字描述 */
	public void setCharacter(String character) {
		this.character = character;
	}

	/** 表情资源的文件名 */
	public String getFaceName() {
		return faceName;
	}

	/** 表情资源的文件名 */
	public void setFaceName(String faceName) {
		this.faceName = faceName;
	}

	/** 表情资源的unicode */
	public String getUnicode() {
		return unicode;
	}

	/** 表情资源的unicode */
	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}

	/** 表情资源的utf8 */
	public String getUtf8() {
		return utf8;
	}

	/** 表情资源的utf8 */
	public void setUtf8(String utf8) {
		this.utf8 = utf8;
	}

	/** 表情资源的utf16 */
	public String getUtf16() {
		return utf16;
	}

	/** 表情资源的utf16 */
	public void setUtf16(String utf16) {
		this.utf16 = utf16;
	}

	/** 表情资源的sbunicode */
	public String getSbunicode() {
		return sbunicode;
	}

	/** 表情资源的sbunicode */
	public void setSbunicode(String sbunicode) {
		this.sbunicode = sbunicode;
	}

}
