define(function(require,exports,module){
	var index = require("index"),
		$ = require("jquery"),
		indexsearch = require("indexsearch");
		
	//首页模块初始化
	index.init();
	
	//品名材质搜索初始化
	indexsearch.init();
	
	//侧边栏模块初始化
	 require('event.shoppingcart');
	//var shoppingcart = require('event.shoppingcart');
	//side.init(function(){$(".cartLink").hide();});
	//shoppingcart.init();
})