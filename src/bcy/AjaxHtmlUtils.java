package bcy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import model.Artist;
import utils.FileUtils;
import utils.UrlUtils;

/**
 * @version V1.0
 * @author JohnnyJYWu
 * @description 作者页面解析 https://bcy.net/u/[作者id]/post，本地文件读写与对比
 */

public class AjaxHtmlUtils {
	private String phantom;
	private String ajaxhtmljs;	
	private String codejs;	
	
	private String savePath;
	
	public AjaxHtmlUtils(String phantom, String ajaxhtmljs, String codejs, String savePath) {
		this.phantom = phantom;
		this.ajaxhtmljs = ajaxhtmljs;
		this.codejs = codejs;
		this.savePath = savePath;
	}
	
	private Artist artist;
	private Artist localArtist;

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	
	
	/**
	 * 爬虫流程
	 * 爬取作者作品页url
	 */
	public void bcyWebCrawler(String url) {
		if(getArtistAlbums(url)) {
			Artist task = compareLocaldata();
			
			//下载
			System.out.println("目前仅支持下载note类型作品");
			downloadArtistAlbums(task);
		} else {
			System.out.println("phantomjs访问失败");
		}
	}
	
	/**
	 * 获取ajax页面内容
	 */
	public String getAjaxCotnent(String url) {
		String exec = phantom + " " + ajaxhtmljs + " " + url;
		
		try {
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(exec);
			
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sbf = new StringBuffer();
			String tmp = "";
			while((tmp =br.readLine())!=null){  
				sbf.append(tmp);  
			}
//			System.out.println(sbf.toString());  
			
			return sbf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 加载分析作者作品页面https://bcy.net/u/[作者id]/post[?p=n]
	 * 根据作者获取专辑集
	 */
	public boolean getArtistAlbums(String artistUrl) {
		artist = new Artist();
		artist.setUrl(artistUrl);
		
		int page = 1;
		
		String url = artistUrl + UrlUtils.artistSuf + "?p=" + page;
		
		System.out.println("正在加载页面：" + url);
		
		String html = getAjaxCotnent(url);
		
		if(html.length() <= 0 || html == null) {
			return false;
		}
		
		String str;
		//获取作者姓名
		str = StringUtils.substringBetween(html, "<title>", "的个人主页");
		artist.setName(str);
		
		String info;
		info = StringUtils.substringBetween(html, "class=\"userInfo\"", "class=\"mainNav js-resize-mainNav\"");
		//获取作者头像图片地址
		str = StringUtils.substringBetween(info, "<a class=\"avatar-user\"", "</a>");
		str = StringUtils.substringBetween(str, "<img src=\"", "/abig\"");
		artist.setHeadImgUrl(str);
		
		//获取作者id
		str = StringUtils.substringBetween(info, "class=\"uname\" href=\"/u/", "\" title=\""+ artist.getName());
		artist.setId(Long.parseLong(str));
		
		//获取作者个人介绍
		str = StringUtils.substringBetween(info, "<p class=\"selfIntro\">", "</p>");
		artist.setSelfIntro(str);

		//Ta的作品-图片，文字，问答，视频，连载
		ArrayList<String> notes = new ArrayList<>();
		ArrayList<String> articles = new ArrayList<>();
		ArrayList<String> ganswers = new ArrayList<>();
		ArrayList<String> videos = new ArrayList<>();
		ArrayList<String> sets = new ArrayList<>();
		while(true) {
			str = StringUtils.substringBetween(html, "class=\"gridList mhn10 smallCards\"", "class=\"split-line\"");
			if(str == null) {
				System.out.println("共" + (page - 1) + "页作品\n");
				break;
			}
			
			String[] note = StringUtils.substringsBetween(str, "<li class=\"_box note\"><a href=\"/item/detail/", "\" class=\"db posr\"");
			String[] article = StringUtils.substringsBetween(str, "<li class=\"_box article\"><a href=\"/item/detail/", "\" class=\"db posr\"");
			String[] ganswer = StringUtils.substringsBetween(str, "<li class=\"_box ganswer\"><a href=\"/item/detail/", "\" class=\"db posr\"");
			String[] video = StringUtils.substringsBetween(str, "<li class=\"_box video\"><a href=\"/item/detail/", "\" class=\"db posr\"");
			String[] set = StringUtils.substringsBetween(str, "<li class=\"_box set\"><a href=\"/item/detail/", "\" class=\"db posr\"");
			
			//添加进相应数组
			if(note != null) for(int i = 0; i < note.length; i ++) notes.add(UrlUtils.album + note[i]);
			if(article != null) for(int i = 0; i < article.length; i ++) articles.add(UrlUtils.album + article[i]);
			if(ganswer != null) for(int i = 0; i < ganswer.length; i ++) ganswers.add(UrlUtils.album + ganswer[i]);
			if(video != null) for(int i = 0; i < video.length; i ++) videos.add(UrlUtils.album + video[i]);
			if(set != null) for(int i = 0; i < set.length; i ++) sets.add(UrlUtils.album + set[i]);
			
			page ++;
			url = artistUrl + UrlUtils.artistSuf + "?p=" + page;
			System.out.println("正在加载页面：" + url);
			html = getAjaxCotnent(url);
		}
		
		artist.setNotes(notes);
		artist.setArticles(articles);
		artist.setGanswers(ganswers);
		artist.setVideos(videos);
		artist.setSets(sets);
		
		System.out.println(artist.toString());
		return true;
	}
	
	/**
	 * 判断本地文件中artist的作品列表
	 * 去除albums中重复的专辑作品，返回需要下载的专辑作品
	 * 更新本地文件artist
	 */
	public Artist compareLocaldata() {
		Artist task = null;
		String path = savePath + artist.getName() + "\\";

		String localfilePath = path + artist.getName() + ".info";//存artist对象信息
		String txtPath = path + artist.getName() + ".txt";//toString展示txt
		
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
			FileUtils.writeArtistToFile(artist, localfilePath);
			FileUtils.writeStringToFile(artist.toString(), txtPath);
			return artist;
		} else {
			File localfile = new File(localfilePath);
			if(!localfile.exists()) {
				System.out.println("本地作者信息文件丢失，即将根据最新信息重新下载专辑...");
				FileUtils.writeArtistToFile(artist, localfilePath);
				FileUtils.writeStringToFile(artist.toString(), txtPath);
				return artist;
			} else {
				localArtist = FileUtils.readArtistFromFile(localfilePath);
				task = getTask(localfilePath, txtPath);
			}
		}
		return task;
	}
	@SuppressWarnings("unchecked")
	private Artist getTask(String localfilePath, String txtPath) {
		//clone task 
		Artist task = new Artist();
		task.setId(artist.getId());
		task.setName(artist.getName());
		task.setHeadImgUrl(artist.getHeadImgUrl());
		task.setUrl(artist.getUrl());
		task.setSelfIntro(artist.getSelfIntro());
		
		Map<String, Object> map;
		boolean flag;
		boolean upgrade = false;// 是否出现更新
		ArrayList<String> list;
		//note
		map = compareAlbums(artist.getNotes(), localArtist.getNotes());
		flag = (boolean) map.get("flag");
		if(flag) {
			list = (ArrayList<String>) map.get("all");
			artist.setNotes(list);
			System.out.println("以上更新类型为：note图片");
			if(!upgrade) upgrade = true;
		}
		list = (ArrayList<String>) map.get("task");
		task.setNotes(list);
		//article
		map = compareAlbums(artist.getArticles(), localArtist.getArticles());
		flag = (boolean) map.get("flag");
		if(flag) {
			list = (ArrayList<String>) map.get("all");
			artist.setArticles(list);
			System.out.println("以上更新类型为：article文字");
			if(!upgrade) upgrade = true;
		}
		list = (ArrayList<String>) map.get("task");
		task.setArticles(list);
		//ganswer
		map = compareAlbums(artist.getGanswers(), localArtist.getGanswers());
		flag = (boolean) map.get("flag");
		if(flag) {
			list = (ArrayList<String>) map.get("all");
			artist.setGanswers(list);
			System.out.println("以上更新类型为：ganswer问答");
			if(!upgrade) upgrade = true;
		}
		list = (ArrayList<String>) map.get("task");
		task.setGanswers(list);
		//video
		map = compareAlbums(artist.getVideos(), localArtist.getVideos());
		flag = (boolean) map.get("flag");
		if(flag) {
			list = (ArrayList<String>) map.get("all");
			artist.setVideos(list);
			System.out.println("以上更新类型为：video视频");
			if(!upgrade) upgrade = true;
		}
		list = (ArrayList<String>) map.get("task");
		task.setVideos(list);
		//set
		map = compareAlbums(artist.getSets(), localArtist.getSets());
		flag = (boolean) map.get("flag");
		if(flag) {
			list = (ArrayList<String>) map.get("all");
			artist.setSets(list);
			System.out.println("以上更新类型为：set连载");
			if(!upgrade) upgrade = true;
		}
		list = (ArrayList<String>) map.get("task");
		task.setSets(list);
		
		if(upgrade) {
			FileUtils.writeArtistToFile(artist, localfilePath);
			FileUtils.writeStringToFile(artist.toString(), txtPath);
		}
		return task;
	}
	private Map<String, Object> compareAlbums(ArrayList<String> currentAlbums, ArrayList<String> localAlbums) {
		Map<String, Object> map = new HashMap<>();
		//比较两个albums中链接的异同
		Set<String> set = new HashSet<>();
		Set<String> newset = new HashSet<>();
		for(String s: localAlbums) {
			set.add(s);
		}
		for(String s: currentAlbums) {
			if(set.add(s)) {//如果返回true则说明本地albums不包含此条链接
				newset.add(s);
				System.out.println("作者有作品更新：" + s);
			} else {
//				System.out.println("已存在此专辑：" + s);
			}
		}

		currentAlbums = new ArrayList<>();
		if(newset.size() > 0) {
//			System.out.println("作者有新的专辑发布，即将下载更新");
			map.put("flag", true);
			localAlbums = new ArrayList<>();
			for(String s: set) localAlbums.add(s);
			map.put("all", localAlbums);

			for(String s: newset) currentAlbums.add(s);
			map.put("task", currentAlbums);
		} else {
//			System.out.println("作者暂无专辑更新");
			map.put("flag", false);
			map.put("task", currentAlbums);
		}
		
		return map;
	}
	
	/**
	 * 根据Artist下载Note图集
	 */
	public void downloadArtistAlbums(Artist artist) {
		if((artist.getNotes() == null || artist.getNotes().size() <= 0)) {
			System.out.println("暂无更新的Note图集页");
			return;
		}
		
		System.out.println("正在下载【" + artist.getName() + "】的作品集");
		
		String path = savePath + artist.getName() + "\\";
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		AlbumDownloader albumDownloader = new AlbumDownloader(phantom, codejs, path);
		if(artist.getHeadImgUrl() != null) {
			albumDownloader.downloadImg(artist.getHeadImgUrl(), path);//下载作者头像
		}
		ArrayList<String> albumsUrls = artist.getNotes();
		for(int i = 0; i < albumsUrls.size(); i ++) {
			albumDownloader.downloadNote(albumsUrls.get(i));
		}
		
		System.out.println("该作者作品下载完成");
	}
}
