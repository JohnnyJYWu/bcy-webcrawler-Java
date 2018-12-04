***
### 此项目旨在学习交流，希望所有使用的小伙伴们清楚以下几点：
**1. 所有图片都是各位作者付出辛苦劳动得来的，请尊重coser版权。**

**2. 图片自己下载使用可以，请勿用于商业用途，转载请先取得作者的同意并注明cn及链接。**

**3. 代码不可直接运行，我留了几个空白需要小伙伴们自己操作填写，拒绝伸手党。**

***

# bcy-webcrawler-Java v1.0
**半次元爬虫 版本1.0**

更新时间：`2018-12-4`

作者：JohnnyJYWu

描述：此项目实现了在[半次元](https://bcy.net)网站批量下载高清原图，并保存在自定义路径下。

技术博客：[机盐](https://www.jianshu.com/u/723ed1883cb5)

简书：https://www.jianshu.com/p/779aa738bce4

CSDN：https://blog.csdn.net/qq_34907122/article/details/84792125

## 实现功能

* [x] 根据```作者主页url```批量下载某个coser发布的```note（图片类型）```作品下的所有高清原图。**作者主页url格式：```https://bcy.net/u/[作者id]```**
* [x] 目前仅支持下载作者```note（图片类型）```作品，作品储存在作者目录的note文件夹下
* [x] 生成以 **作者名称** 命名的文件夹，同时在文件夹中保存该作者主页相关信息，包括：作者头像```fat.jpg```，作者展示信息```[name].txt```，作者信息对象储存文件```[name].info```
* [x] 作者每次发布的作品以 **作品专辑** 的概念建立文件夹，下载的图片分别储存在相应文件夹下，为防止命名重复，文件夹命名规则为```id：[作品id] [作品title]```
* [x] 每个 **作品专辑** 文件夹包含：该作品的所有图片```[imgname].jpg```*n，作品页截图```webscreenshot.jpg```作品展示信息```[name].txt```，作品信息对象储存文件```[name].info```
* [x] 当下载某作者的作品时，会根据```.info```文件进行对比筛选，仅下载**最新**发布的作品，本地已有作品不会重复下载。
* [x] 可选择仅根据某页作品专辑url下载**指定作品页面**的图片。**作品页url格式：```https://bcy.net/item/detail/[作品id]```**
* [x] **多线程**下载图片，采用线程池管理
* [x] 支持下载**仅粉丝可见**作品，需手动登录账号关注作者，并在PhantomJs所用的js脚本中添加cookie
* [ ] 不支持智能下载未下载的图片。下载前筛选信息是基于```.info```文件，请勿随意更改或删除
* [ ] 目前仅支持下载note图片类型作品，article文字、ganswer回答、video视频及set连载类型作品已预留可后续扩展

## 运行环境&软件包
- Windows 10

- Java 10.0.1

- [commons-lang3-3.8.1](http://commons.apache.org/proper/commons-lang/download_lang.cgi)

- [phantomjs-2.1.1-windows](http://phantomjs.org/download.html)

## 安装
```
git clone https://github.com/JohnnyJYWu/bcy-webcrawler-Java.git
```

## 使用说明
1. 更改```/src/utils/UrlUtils.java```中的```savePath```为自己的本地存储目录路径。
```java
//图片存储地址
public static final String savePath = "E:\\bcy\\bcyWebCrawler\\";//!!!!这里换成自己本地的储存地址
```

2. 更改```/phantomjs/ajaxhtml_cookies.js```及```/phantomjs/code_cookies.js```中addCookie()方法的```value```值为自己的value值。
value值获取方法在技术博客中有提到：https://www.jianshu.com/p/792bf78adbd1
```
var flag = phantom.addCookie({
  'name'     : 'sessionid',
  'value'    : '换成你自己的value',
  'domain'   : '.bcy.net',
  'path'     : '/',
  'httponly' : false,
  'secure'   : false,
  'expires'  : 'Fri, 01 Jan 2038 00:00:00 GMT'
});
```

3. 根据提示在```/src/bcy/Main.java```中填写相关链接后运行。其中```downloadAlbums()```方法为下载某作品页的图片，```downloadArtists()```方法为下载某作者的所有note图片类型作品图片。
```
	public static void main(String[] args) {
		downloadAlbums();//此方法直接下载作品页图片
		
		downloadArtists();//解析作者页

		System.out.println("All Finished");
	}
```


## 效果展示
以下以[Misa贞喵](https://bcy.net/u/207603)的作品图片为例进行展示

#### 她的主页：[Misa贞喵](https://bcy.net/u/207603)
![](https://github.com/JohnnyJYWu/bcy-webcrawler-Java/blob/master/README/artist.png)

#### 下载页面：[#cos正片# #Fate/GrandOrder# #远坂凛# 远坂凛情人节礼装ver.](https://bcy.net/item/detail/6406332008502419214)
![](https://github.com/JohnnyJYWu/bcy-webcrawler-Java/blob/master/README/album.png)

#### 下载成功后的目录结构
![](https://github.com/JohnnyJYWu/bcy-webcrawler-Java/blob/master/README/folder.png)

#### 下载中控制台信息
![](https://github.com/JohnnyJYWu/bcy-webcrawler-Java/blob/master/README/console.png)

#### 展示信息```*.txt```
![](https://github.com/JohnnyJYWu/bcy-webcrawler-Java/blob/master/README/textInfo.png)

## FAQ
如果有任何问题可发布[issue](https://github.com/JohnnyJYWu/bcy-webcrawler-Java/issues)，或在我的 [简书](https://www.jianshu.com/p/779aa738bce4) & [CSDN博客](https://blog.csdn.net/qq_34907122/article/details/84792125) 评论留言，我会尽量及时查看回复。
