define(function(require,exports,module){
	var $ = require("jquery"),
	    cache=require("cache");
		// 10秒倒计时跳转至个人中心
    var time = 10;
    var code = $(".reg-msg span") 
    var t=setInterval(function() {
          time--;
          code.html(time);
          if (time==0) {
              clearInterval(t);
              location.href =  cache.base_url+"/member/"; 
          }
        },1000);

})