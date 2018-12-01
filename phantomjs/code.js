system = require('system')
address = system.args[1];
path = system.args[2]

var page = require('webpage').create();
var url = address; //访问网址
var savePath = path; //截图保存路径，不写默认保存的调用phantomjs的目录

page.open(url, function (status) { 
	console.log("Status: " + status);
	if (status === 'success') {
		window.setTimeout(function () { //设置等待延迟，保证phantom能完整加载出页面
			page.render(savePath + "webscreenshot.png"); //网页截图
			console.log(page.content); //网页html文本
			phantom.exit();
		}, 5000);
	} else {
		console.log('Failed to post!');
		phantom.exit();
	}
});