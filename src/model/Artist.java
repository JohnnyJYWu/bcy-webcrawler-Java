package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @version V1.0
 * @author JohnnyJYWu
 * @description 作者类，用于储存作者信息
 */

public class Artist implements Serializable {
	private long id;
	private String name;
	private String headImgUrl;
	private String url;
	private String selfIntro;
	
	//作品
	private ArrayList<String> notes;//图片
	private ArrayList<String> articles;//文字
	private ArrayList<String> ganswers;//回答
	private ArrayList<String> videos;//视频
	private ArrayList<String> sets;//连载
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSelfIntro() {
		return selfIntro;
	}
	public void setSelfIntro(String selfIntro) {
		this.selfIntro = selfIntro;
	}
	//作品
	public ArrayList<String> getNotes() {
		return notes;
	}
	public void setNotes(ArrayList<String> notes) {
		this.notes = notes;
	}
	public ArrayList<String> getArticles() {
		return articles;
	}
	public void setArticles(ArrayList<String> articles) {
		this.articles = articles;
	}
	public ArrayList<String> getGanswers() {
		return ganswers;
	}
	public void setGanswers(ArrayList<String> ganswers) {
		this.ganswers = ganswers;
	}
	public ArrayList<String> getVideos() {
		return videos;
	}
	public void setVideos(ArrayList<String> videos) {
		this.videos = videos;
	}
	public ArrayList<String> getSets() {
		return sets;
	}
	public void setSets(ArrayList<String> sets) {
		this.sets = sets;
	}
	public String toString() {//windows换行\r\n mac换行\r linux、unix换行\n
		return "作者：" + name
				+ "\r\n作者id：" + id
				+ "\r\n作者简介：" + selfIntro
				+ "\r\n作者主页：" + url
				+ "\r\n作者头像链接：" + headImgUrl + "\r\n"
				+ "\r\nTA的作品：共 " + 
				+ (notes.size() + articles.size() + ganswers.size() + videos.size() + sets.size()) + " 篇"
				+ "\r\n作品链接：\r\n"
				+ "图片：\r\n" + listToString(notes)
				+ "文字：\r\n" + listToString(articles)
				+ "问答：\r\n" + listToString(ganswers)
				+ "视频：\r\n" + listToString(videos)
				+ "连载：\r\n" + listToString(sets);
	}
	public String listToString(ArrayList<String> albums) {
		String str = "";
		if(albums.size() > 0) {
			for(String s: albums) {
				str += s + "\r\n";
			}
		} else {
			str += "还没有发布过任何作品呐～\r\n";
		}
		return str;
	}
}
