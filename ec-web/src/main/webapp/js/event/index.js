/*utf-8*/

define(function (require, exports, module) {
    "use strict";
     var   $ = require("jquery"),
       // cache = require("js/cache"),
      //  search = require("js/search"),
        shoppingcart = require('event.shoppingcart');
	//require("js/plugin/header");
	require("prcsteel");
    require("jquery-tips");
	
	
	//初始化搜索
   // search.init();
	
	//首次打开引导功能
//  if($.cookie('guide') === undefined){
//		$("#guide").show();
//		$.cookie('guide', false ,{expires: cache.search_global_cookie_expire_day});
//	}

    $(document).on("click", ".m-data button", function () {
        var $li = $(this).closest("li"), price = "", resId = "", maxWeight = 0, mobile = "", category="";
        price = $li.find('input[name="price"]').val();
        resId = $li.find('input[name="resourceID"]').val();
        maxWeight = $li.find('input[name="maxWeight"]').val();
        mobile = $li.find('input[name="SellerMobileNumber"]').val(),
        category = $li.find('dl').eq(0).find('strong').html();

        $("#shopcart").shoppingcart('dialog', resId, price, maxWeight, mobile, category);
    });

    function st(){
        $(".m-s").css({"position":"fixed","top":"0px","z-index":"100","left":"0"}).slideDown(300);
        $("#m-search").show(50);

        var h = $(window).scrollTop();
        if(h <= 320){
            $(".m-s").removeAttr("style");
        }
    }
    $(window).scroll(function(){
        setTimeout(st, 200);
    });
	
	$(document).ready(function(){
		
		$(".sc_link ul").append('<li><a id="showGuide" class="show_Guide" href="javascript:;">新版引导</a></li>');
		$("#showGuide").click(function(){
			scrollTo(0,0);
			changeGuidePosition();
			$("#guide").removeClass("none").find("div:eq(0)").show();
			$("#fade01").show();
		});
		$("#guide #guide05 .guide05").css("margin-top", 0.25 * $(window).height());
		changeGuidePosition();
	});
	
	function changeGuidePosition(){
		if($(".prefer-resoult dl:visible").length === 0){
			$("#guide #guide02 .guide02").css("margin-top", 240);
			$("#guide #guide03 .guide03").css("margin-top", 303);
		}else if($(".prefer-resoult dl:visible").length === 1){
			$("#guide #guide02 .guide02").css("margin-top", 240+35);
			$("#guide #guide03 .guide03").css("margin-top", 303+35);
		}else if($(".prefer-resoult dl:visible").length === 2){
			$("#guide #guide02 .guide02").css("margin-top", 240+70);
			$("#guide #guide03 .guide03").css("margin-top", 303+70);
		} 
	}
	//esc退出引导
	$(document).keydown(function(event){
		if($("#guide .guide_box").length > 0 && event.keyCode == 27){
			$("#guide").addClass("none");
			$("#guide .guide_box,#fade01").hide();
		}
	});
	
	$(window).resize(function() {
	  $("#guide #guide05 .guide05").css("margin-top", 0.25 * $(window).height());
	});
});
