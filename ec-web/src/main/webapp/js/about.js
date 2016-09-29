define(function(require,exports,module){
	 /**
      *@date:2016-07-27
      *@author :qianxinzi
      *@describe：关于我们，导航切换功能
      *
      */
	"use strict";
	var $ = require("jquery"),
	util = require("util");
	$(function(){
	    
	   $(document).on('click','.about-ul li',function(){
	
	        var el = $(this),
	        dataAttr = el.attr('data-attr');  
	        el.addClass('active');
	        el.siblings('li').removeClass('active');     
	        $("div[data-attr='"+dataAttr+"']").show().siblings(".about-eq").hide();
	        document.title = el.text();
	        
	   }); 
	   
	    var href = window.location.href;
	    var arr = href.split("?");
	    if(arr.length>1){
	    	var text = $("li[data-attr='"+arr[1]+"']").click();
	    }else{
	    	$("li[data-attr='prc']").click();
	    }
	   
	    //qq客服
        $(".qqlink").bind("click",function(){
        	util.setqq();
        })
	});




})