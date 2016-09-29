// JavaScript Document
define(function (require, exports, module) {
    //初始化
    var $ = require("jquery"), util = require("util"),
        screenH = window.screen.height,
        shoppingcart = require("shoppingcart");
        
    $(function () {

        //当HTML高度小于视口高度时将侧边栏提交订单按钮固定在底部
         function setHeight() {

            var browser_h = util.getTotalHeight(), html_h = $('body').innerHeight(), sh = $(window).scrollTop();
            //alert(browser_h+'....'+html_h);
            if (browser_h < html_h) {
                var h = browser_h <= 0 ? "100%" : browser_h;
                $("#shopcart").height(h);
                $(".sc_link").height(h);
                $(".cart_list").height(h - 150);

            } else {
                $("#shopcart").height(browser_h);
                $(".sc_link").height(browser_h);
                $(".cart_list").height(browser_h - 135);
            }

            new function (w, b, c, o) {
               b = document.body; o = b.childNodes; c = "className";
               if(o[0][c] == "shopcart") {$(b).append("shopcart")};
            }
        };
        // init shopping cart
        $("#shopcart").shoppingcart();
		//$(".find-content").gwfind();
        // set sider tool
        $("#shopcart").height("100%");
        $(".find-content").height("100%");
        $(".sc_link").height("100%");
        $(".sc_list").height(screenH);

        setHeight();
        //购物列表显示
        $(document).on("click", ".cartLink",function () {
            var w = $("#shopcart").width();
            if(w!=360) {
                $("#shopcart").width(360);
                $(".sc_list").show();
                $(".cartLink").addClass("current");
                $(".sc_link").height("100%");
                $(".sc_list").height("100%");
                $(".toggle-cart").show();
            } else{
            	if($(".find-content").is(":visible")){
            		$(".find-content").hide();
            		$(".findLink").removeClass("current");
            		$(".cartLink").addClass("current");
            		$(".sc_list").show();
            	}else{
            		$('.sc_list').hide();
	                $("#shopcart").width(60);
	                $(".cartLink").removeClass("current");
	                $(".toggle-cart").hide();
            	}
                
            }
        });
		//显示钢为帮你找
		$(document).on("click",".findLink",function(){
            gwfind = require("gwfind");
			var w = $("#shopcart").width();
            if(w!=360) {
                $("#shopcart").width(360);
                $(".find-content").show();
                $(".findLink").addClass("current");
                $(".cartLink").removeClass("current");
                $(".sc_link").height("100%");
                $(".sc_list").height("100%");
                $(".toggle-cart").show();
            } else {
            	if( $(".sc_list").is(":visible")){
            		$(".sc_list").hide();
            		$(".cartLink").removeClass("current");
            		$(".findLink").addClass("current");
            		$(".find-content").show();
            	}else{
            		 $('.find-content').hide();
	                $("#shopcart").width(60);
	                $(".findLink").removeClass("current");
	                $(".toggle-cart").hide();
            	}
               
            }
		});
        //点击其他位置时隐藏
        $(document).on("click", "#toggleCart",function (evt) {
            $('.sc_list').hide();
            $("#shopcart").width(60);
            $(".cartLink").removeClass("current");
            $(".findLink").removeClass("current");
            $(".toggle-cart").hide();
        });

        $(document).on("click", "body", function (evt) {
        	if($(evt.target).closest(".d-border").length > 0 || $(evt.target).closest(".d-mask").length > 0){
        		return ;
        	}
            if ($(evt.target).closest("#shopcart").length == 0) {
                $('.sc_list').hide();
                $("#shopcart").width(60);
                $(".cartLink").removeClass("current");
                 $(".findLink").removeClass("current");
                $(".toggle-cart").hide();
            }
        });

        //钢为知道二维码显示
        $(document).on("mouseover", ".qrcode",function () {
        //$(".").mouseover(function () {
            $(".qrcode_sh").show();
        });
        $(document).on("mouseleave", ".qrcode",function () {
            $(".qrcode_sh").hide();
        });

        //返回顶部
//      $(window).scroll(function () {
//
//          var isIE=!!window.ActiveXObject, isIE6=isIE&&!window.XMLHttpRequest;
//          if(isIE6){
//              $("#shopcart").addClass("top0");
//          }
//
//          if ($(this).scrollTop() > 400) {
//              $("#returntop").show();
//          } else {
//              $("#returntop").hide();
//          }
//
//          setHeight();
//      });

        $(window).resize(function () {
            setHeight();
        });

        $('.addNew').click(function() {
            var id = $('#rid').val();
            $("#shopcart").shoppingcart('add', id, 1.22);
        });
        
        $('.online').click(function() {
			util.setqq();
        });
    })
    

});
