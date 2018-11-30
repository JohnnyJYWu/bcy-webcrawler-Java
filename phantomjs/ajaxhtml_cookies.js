system = require('system')
address = system.args[1];

var page = require('webpage').create();
var url = address;

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
		window.setTimeout(function () {
			console.log(page.content);
			phantom.exit();
		}, 5000);
	});
} else {
	console.log("cookies error")
}
