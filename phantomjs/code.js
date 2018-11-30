system = require('system')
address = system.args[1];
dir = system.args[2]

var page = require('webpage').create();
var url = address;
var savedir = dir;
page.open(url, function (status) { 
	//Page is loaded!
	if (status !== 'success') {
		console.log('Unable to post!');
	} else {
	window.setTimeout(function () {

		page.render(savedir + "webscreenshot.png");
		//╫ьм╪
		console.log(page.content);
		phantom.exit();

		}, 5000);
	}
});