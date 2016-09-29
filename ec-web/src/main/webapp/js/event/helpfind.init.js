define(function(require,exports,module){
	var helpfind = require("helpfind"),
		indexsearch = require("indexsearch");
	
	//钢为购模块初始化
	helpfind.init();
	//品名材质搜索初始化
	indexsearch.init();
	//侧边栏模块初始化
	require('event.shoppingcart');
})