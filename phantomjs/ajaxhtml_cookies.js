system = require('system')
address = system.args[1];

var page = require('webpage').create();
var url = address;

var flag = phantom.addCookie({
  'name'     : 'sessionid',
  'value'    : '换成你自己的value',
  'domain'   : '.bcy.net',
  'path'     : '/',
  'httponly' : false,
  'secure'   : false,
  'expires'  : 'Fri, 01 Jan 2038 00:00:00 GMT'
});
console.log(flag);

if(flag) {
	page.open(url, function (status) {
		console.log("Status: " + status);
		if (status === 'success') {
			window.setTimeout(function () {
				console.log(page.content);
				phantom.exit();
			}, 5000);
		} else {
			console.log('Failed to post!');
			phantom.exit();
		}
	});
} else {
	console.log("cookies error")
}
