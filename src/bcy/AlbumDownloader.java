package bcy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

import model.Album;
import utils.FileUtils;

/**
 * @version V1.0
 * @author JohnnyJYWu
 * @description 作品页面解析 https://bcy.net/item/detail/[作品id]，下载图片至本地
 */

public class AlbumDownloader {
	public enum Type {//type为作者作品类型，如note图片，article文字，ganswer回答，video视频，set连载
		note, article, ganswer, video, set
	}

	private String phantom;
	private String codejs;
	private String dirPath;
	
	public AlbumDownloader(String phantom, String codejs, String dirPath) {
		this.phantom = phantom;
		this.codejs = codejs;
		this.dirPath = dirPath;
	}
	
	/**
	 * 执行, download Note图片作品集
	 */
	public void downloadNote(String url) {
		System.out.println("正在加载页面：" + url);
		String savePath = dirPath + Type.note + "//";//note图片类型储存文件夹
		File file = new File(savePath);
        if(!file.exists()) file.mkdirs();

        //获取ajax页面
		String html = getAjaxCotnent(url, savePath);
		
		Album album = new Album();
		//获取基础信息
		album.setUrl(url);
		album.setTitle("id：" + url.substring(url.lastIndexOf("/")) + " " + getTitle(html));
		album.setArtist(getArtist(html));
		//获取具体信息
		album = getAlbumNoteInfo(album, html);
		
		//去除非法字符
		String name = stringFilter(album.getTitle());
		//创建文件夹
		File files = new File(savePath + name);
        if(!files.exists()) files.mkdirs();
        //下载图片
		downloadImgs(album.getImgUrls(), savePath + name);
		//移动截图
		moveFile(savePath + "webscreenshot.png", savePath + name + "//webscreenshot.png");
		//存作品信息对象
		FileUtils.writeAlbumsToFile(album, savePath + name + "//" + name +".info");
		//存作品信息展示txt
		FileUtils.writeStringToFile(album.toString(), savePath + name + "//" + name +".txt");
	
		System.out.println("Note作品下载完成：" + name + "\n");
		System.out.println("------------Note作品信息------------\n");
		System.out.println(album.toString());
	}
	
	/**
	 * 获取ajax页面内容
	 */
	public String getAjaxCotnent(String url, String path) {
		String exec = phantom + " " + codejs + " " + url + " " + path;
		
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
	 * 提取专辑标题title
	 */
	public String getTitle(String html) {
		String title = StringUtils.substringBetween(html, "<title>", "| 半次元-二次元爱好者社区</title>");
		System.out.println("作品标题：" + title);
		
		return title;
	}
	
	/**
	 * 提取作者名name
	 */
	public String getArtist(String html) {
		String str = StringUtils.substringBetween(html, "class=\"user-name\"", "class=\"follow-followed\"");
		String artist = StringUtils.substringBetween(str, "title=\"", "\"");
		System.out.println("作者：" + artist);
		
		return artist;
	}

	/**
	 * 提取作品发布时间、图片数
	 * 提取作品大图集url链接
	 */
	public Album getAlbumNoteInfo(Album album, String html) {
		String article = StringUtils.substringBetween(html, "<article>", "</article>");
		String content = StringUtils.substringBetween(article, "class=\"inner-container\"", "class=\"declaration\"");
//		System.out.println(album+ "\n");
		String[] strings = StringUtils.substringsBetween(content, "src=\"","/w650\"");
		
		if(strings == null) {
			strings = new String[0];
			System.out.println("【作者设置了仅粉丝可见，请手动关注作者后重试！】");
		} else {
			String header = StringUtils.substringBetween(article, "<header>", "</header>");
			String info = StringUtils.substringBetween(header, "class=\"meta-info mb20\"", "class=\"actions\"");
			String date = StringUtils.substringBetween(info, "<span>", "</span>");//提取发布时间
			String imgNum = StringUtils.substringBetween(info, "<span>共 ", " P</span>");//提取照片数
			album.setDate(date);
			album.setImgNum(Integer.parseInt(imgNum));
			
			System.out.println("发布时间：" + album.getDate());
			System.out.println("照片数：共 " + album.getImgNum() + " P");
			
			String intro = StringUtils.substringBetween(article, 
					"<div style=\"margin-bottom:20px\">", "</div><div class=\"album\">");//提取作者文字介绍
			intro = intro.replaceAll("<br>", "\r\n");//替换html标签内容
			album.setIntro(intro);
			System.out.println(intro);
		}
		
		for(int i = 0; i < strings.length; i ++) {
			System.out.println(strings[i]);
		}

		album.setImgUrls(strings);
		
		return album;
	}
	
	/**
	 * 下载图片组
	 * 线程池实现
	 */
	public void downloadImgs(String[] imgUrls, String savePath) {
		System.out.println("创建下载任务！");
		System.out.println("共" + imgUrls.length + "项任务");
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
		for(int i = 0; i < imgUrls.length; i ++) {
			final String imgUrl = imgUrls[i];
			fixedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					downloadImg(imgUrl, savePath);
				}
			});
		}
		
		//等待下载完成
		fixedThreadPool.shutdown();
		while(true) {
			if(fixedThreadPool.isTerminated()) {
				fixedThreadPool.shutdownNow();
				break;
			}
		}
	}
	
	/**
	 * 下载图片
	 */
	public void downloadImg(String imgUrl, String filePath) {
		String fileName = imgUrl.substring(imgUrl.lastIndexOf("/"));
		
		//创建文件的目录结构
        File files = new File(filePath);
        if(!files.exists()){// 判断文件夹是否存在，如果不存在就创建一个文件夹
            files.mkdirs();
        }
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            // 创建文件
            File file = new File(filePath + "//" + fileName);
            FileOutputStream out = new FileOutputStream(file);
            int i = 0;
            while((i = is.read()) != -1){
                out.write(i);
            }
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("下载完成：" + fileName);
	}
	
	/**
	 * 移动文件（网页截图）
	 */
	public void moveFile(String filePath, String newPath) {
		File file = new File(filePath);
		File newFile = new File(newPath);
		if(newFile.exists() && newFile.isFile()) newFile.delete();
		if (file.renameTo(new File(newPath))) {
            System.out.println("File is moved successful!");
        } else {
            System.out.println("File is failed to move!");
        }
	}
	
	/**
	 * 正则，过滤非法字符（创建文件夹）
	 */
	public String stringFilter(String str) {
		String regEx="[\\:*?\"<>|\\/\\\\]";//windows文件名不允许包含（\/:*?"<>|），使用正则消除
		Pattern p = Pattern.compile(regEx);     
		Matcher m = p.matcher(str);     
		return m.replaceAll("").trim();     
	}

}
