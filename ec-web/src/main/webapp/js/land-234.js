define(function(require,exports,module){
	var $ = require("jquery"),cache=require("cache");
	$(function(){
		$(".page-num-btn").bind("click",function(){
			
			if($(".page-num-ipt").val() == ""){
				return ;
			}
			var dataAttr = $(this).attr("data-attr"),
			url = window.location.href,
			pageIndex = $(".page-num-ipt").val();
			pageIndex =  parseInt(pageIndex,10);
			if(pageIndex == NaN){
				pageIndex = 1;
			}
			if(pageIndex<=0){
				pageIndex = 1;
			}
			if(url.indexOf(dataAttr)>0){
				url = url.split(dataAttr)[0];
			}else{
				url = cache.base_url+"/guide/";
			}
			window.location.href = url+dataAttr+pageIndex+".html";
			
		})
	})
})