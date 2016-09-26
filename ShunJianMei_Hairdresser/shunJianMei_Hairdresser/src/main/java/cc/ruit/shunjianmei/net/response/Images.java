package cc.ruit.shunjianmei.net.response;

public class Images {
	private String Image;//评论配图
	private String Photo;//评论配图

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	@Override
	public String toString() {
		return "Images [Image=" + Image + ", Photo=" + Photo + "]";
	}
	
}
