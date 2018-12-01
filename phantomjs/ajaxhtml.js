system = require('system')
address = system.args[1];

var page = require('webpage').create();
var url = address;

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