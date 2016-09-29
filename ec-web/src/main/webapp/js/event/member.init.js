define(function(require,exports,module){
	var member = require("member");
	var find = require("find.init");
	find.uploadfileFch();
	member.init();
	require("ajaxfileupload");
});
