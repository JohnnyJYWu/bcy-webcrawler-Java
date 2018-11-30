package utils;

/**
 * @version V1.0
 * @author JohnnyJYWu
 */

public class UrlUtils {
	public static final String projectPath = System.getProperty("user.dir");//项目工程目录路径
	public static final String phantomPath = projectPath + "\\phantomjs";//phantomjs路径
	public static final String phantom = phantomPath + "\\phantomjs.exe";//phantomjs.exe
	public static final String codejs = phantomPath + "\\code_cookies.js";//访问页面并截图的js
	public static final String ajaxhtmljs = phantomPath + "\\ajaxhtml_cookies.js";//仅访问页面的js
	
	//图片存储地址
	public static final String savePath = "E:\\bcy\\bcyWebCrawler\\";//!!!!这里换成自己本地的储存地址

	//网址url
	public static final String album = "https://bcy.net/item/detail/";// +id为专辑url 例如https://bcy.net/item/detail/[作品id]
	
	public static final String artistPre = "https://bcy.net/u/";// +id为作者主页 例如https://bcy.net/u/[作者id]
	public static final String artistSuf = "/post";// +?p=[页数] 访问主页作品的后缀

}
