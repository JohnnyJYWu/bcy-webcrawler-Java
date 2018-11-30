system = require('system')
address = system.args[1];

var page = require('webpage').create();
var url = address;
page.open(url, function (status) { 
	window.setTimeout(function () {
		console.log(page.content);
		phantom.exit();
		}, 5000);
});