package bcy;

import utils.UrlUtils;

public class Main {
	public static String phantom = UrlUtils.phantom;
	public static String ajaxhtmljs = UrlUtils.ajaxhtmljs;
	public static String codejs = UrlUtils.codejs;
	
	public static String savePath = UrlUtils.savePath;

	public static String[] urls = {
//			"https://bcy.net/item/detail/[作品id]",//这里填写作品地址
	};
	
	public static String[] strs = {
//			"https://bcy.net/u/[作者id]",//这里填写作者主页地址
	};
	
	public static void main(String[] args) {
//		downloadAlbums();//此方法直接下载作品页图片
		
		downloadArtists();//解析作者页

		System.out.println("All Finished");
	}

	public static void downloadAlbums() {
		//下载note作品页
		AlbumDownloader albumDownloader = new AlbumDownloader(phantom, codejs, savePath);
		for(int i = 0; i < urls.length; i ++) {
			albumDownloader.downloadNote(urls[i]);
		}		
	}
	
	public static void downloadArtists() {
		//下载作者主页并爬取作品页
		AjaxHtmlUtils ajaxHtmlUtils = new AjaxHtmlUtils(phantom, ajaxhtmljs, codejs, savePath);
		for(int i = 0; i < strs.length; i ++) {
			ajaxHtmlUtils.bcyWebCrawler(strs[i]);
		}
	}
}
