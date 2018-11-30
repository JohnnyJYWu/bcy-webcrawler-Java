system = require('system')
address = system.args[1];
dir = system.args[2]

var page = require('webpage').create();
var url = address;
var savedir = dir;

var flag = phantom.addCookie({
  'name'     : 'sessionid',
  'value'    : '',//在这里写你自己的cookie值
  'domain'   : '.bcy.net',
  'path'     : '/',
  'httponly' : false,
  'secure'   : false,
  'expires'  : 'Fri, 01 Jan 2038 00:00:00 GMT'
});
console.log(flag);


if(flag) {
	page.open(url, function (status) { 
		//Page is loaded!
		if (status !== 'success') {
			console.log('Unable to post!');
		} else {
		window.setTimeout(function () {
			page.render(savedir + "webscreenshot.png");

			console.log(page.content);
			phantom.exit();

			}, 5000);
		}
	});
} else {
	console.log('cookies error')
}