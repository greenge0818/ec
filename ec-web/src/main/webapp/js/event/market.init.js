define(function(require,exports,module){
	"use strict";
	var $ = require("jquery"),
 	search = require("search"),
 	markethelpfind = require("markethelpfind");
 	require('event.shoppingcart');
 	markethelpfind.init();//底部钢为购
	search.init();

 	$(document).on("click", ".m-data #data-area button", function () {
        var $li = $(this).closest("li"),
        price = $li.find('input[name="price"]').val(),
        weightConcept = $li.find('input[name="weightConcept"]').val(),
        cityId = $li.find('input[name="cityId"]').val(),
        cityName = $li.find('input[name="cityName"]').val(),
		warehouseId = $li.find('input[name="warehouseId"]').val(),
		warehouseName = $li.find('input[name="warehouseName"]').val(),
        factoryId = $li.find('input[name="factoryId"]').val(),
        factoryName = $li.find('input[name="factoryName"]').val(),
        spec = $li.find('input[name="spec"]').val(),
        spec1 = $li.find('input[name="spec1"]').val(),
        spec2 = $li.find('input[name="spec2"]').val(),
        spec3 = $li.find('input[name="spec3"]').val(),
        materialUuid = $li.find('input[name="materialUuid"]').val(),
        materialName = $li.find('input[name="materialName"]').val(),
        categoryUuid = $li.find('input[name="categoryUuid"]').val(),
        categoryName = $li.find('input[name="categoryName"]').val();
		var des = categoryName+materialName+" "+spec;
		var param ={
			price:price,
			weightConcept:weightConcept,
			cityId:cityId,
			cityName:cityName,
			warehouseId:warehouseId,
			warehouseName:warehouseName,
			factoryId:factoryId,
			factoryName:factoryName,
			spec1:spec1,
			spec2:spec2,
			spec3:spec3,
			materialUuid:materialUuid,
			materialName:materialName,
			categoryUuid:categoryUuid,
			categoryName:categoryName,
		}
        $("#shopcart").shoppingcart('dialog',  des, param);
    });
    
    function st(){
        

        var h = $(window).scrollTop();
        if(h > 239){
           // $(".m-s").removeAttr("style");
           $(".m-s").css({"position":"fixed","top":"0px","z-index":"100","left":"0"}).slideDown(300);
       	   $("#m-search").show(50);
        }else{
        	$(".m-s").removeAttr("style");
        }
    }
    $(window).scroll(function(){
        setTimeout(st, 200);
    });
    
    
    
    
    
//	$(document).ready(function(){
//		
//		$(".sc_link ul").append('<li><a id="showGuide" class="show_Guide" href="javascript:;">新版引导</a></li>');
//		$("#showGuide").click(function(){
//			scrollTo(0,0);
//			changeGuidePosition();
//			$("#guide").removeClass("none").find("div:eq(0)").show();
//			$("#fade01").show();
//		});
//		$("#guide #guide05 .guide05").css("margin-top", 0.25 * $(window).height());
//		changeGuidePosition();
//	});
//	function changeGuidePosition(){
//		if($(".prefer-resoult dl:visible").length === 0){
//			$("#guide #guide02 .guide02").css("margin-top", 240);
//			$("#guide #guide03 .guide03").css("margin-top", 303);
//		}else if($(".prefer-resoult dl:visible").length === 1){
//			$("#guide #guide02 .guide02").css("margin-top", 240+35);
//			$("#guide #guide03 .guide03").css("margin-top", 303+35);
//		}else if($(".prefer-resoult dl:visible").length === 2){
//			$("#guide #guide02 .guide02").css("margin-top", 240+70);
//			$("#guide #guide03 .guide03").css("margin-top", 303+70);
//		} 
//	};
//	//esc退出引导
//	$(document).keydown(function(event){
//		if($("#guide .guide_box").length > 0 && event.keyCode == 27){
//			$("#guide").addClass("none");
//			$("#guide .guide_box,#fade01").hide();
//		}
//	});
});
