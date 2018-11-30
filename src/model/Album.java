package model;

import java.io.Serializable;

/**
 * @version V1.0
 * @author JohnnyJYWu
 * @description 作品类，用于储存作品信息
 */

public class Album implements Serializable {
	private String url;
	private String title;
	private String artist;
	//info
	private String[] imgUrls;
	private int imgNum;
	private String date;
	private String intro;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String[] getImgUrls() {
		return imgUrls;
	}
	public void setImgUrls(String[] imgUrls) {
		this.imgUrls = imgUrls;
	}
	public int getImgNum() {
		return imgNum;
	}
	public void setImgNum(int imgNum) {
		this.imgNum = imgNum;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String toString() {
		return "标题：" + title
				+ "\r\n作者：" + artist
				+ "\r\n作品链接：" + url
				+ "\r\n发布时间：" + date
				+ "\r\n照片数：共 " + imgNum + " P" + "\r\n"
				+ "\r\n" + intro + "\r\n"
				+ "\r\n照片原图链接：\r\n"
				+ stringsToString(imgUrls);//windows换行\r\n mac换行\r linux、unix换行\n
	}
	public String stringsToString(String[] strings) {
		String s = "";
		for(int i = 0; i < strings.length; i ++) {
			s += strings[i] + "\r\n";
		}
		return s;
	}
}
