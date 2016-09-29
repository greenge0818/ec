 /**
      *@date:2016-07-27
      *@author :qianxinzi
      *@describe：广告位页面弹窗
      *
      */
define(function(require,exports,module){
	 "use strict";
	 var $ = require("jquery"),util = require("util");
	 //付款方式UI以及弹层
	var getPayWayDialog = function(){
		var el = "";
       	el +="<div class='payway' >";
       	el +="<div class='p-head'>";
       	el +="<div style='float:left'>付款方式</div>";
       	el +="</div>";
       	el +="<div class='p-body'>";
       	el +="<div class='p-list'>";
       	el +="<ul class='d-list-ul f-clrfix' >";
       	el +="<li class='list02 tl borderbottom'>公司名称</li>";
        el +="<li class='list05 tl borderbottom'>开户银行</li>";
        el +="<li class='list06  borderbottom'>账号</li>";
   	    el +="<li class='list02 tl pad12'>浙江钢为网络科技有限公司</li>";
        el +="<li class='list05 tl pad12'>浦发银行杭州钱塘支行</li>";
        el +="<li class='list06 tr pad12'>95090154800003503</li>";
       	el +="</ul>";
       	el +="</div>";
       	el +="</div>";
       	el +="<div class='detail-c'>";
       	el +="</div>";
        el +="</div>";
		util.getDialog(false, el);
	};
	var ht = $(window).height();
	var init = function(){
		$(function(){
		    //点击弹出详细
		   $(document).on('click','.adsBox',function(){
		   	 	var el = $(this),layout = el.find('.layerOut');
		   		var h = layout.show().find("img").height()
		   		layout.hide();
				var html = el.find('.layerOut').html();
				if(html == null || html == ""){
					return 
				}
		   		if(h>ht){
		   			util.getDialog(false, html,false,(100+$(window).scrollTop()));
		   		}else{
		   			util.getDialog(false, html,null,(ht-h)/2);
		   		}
//		        var el = $(this);
//		        var html = el.find('.layerOut').html();
//		        html = "<div class='showImg'>"+html+"</div>";
//		        var ht = el.find('.layerOut').find("img").height();
//		        util.getDialog(false, html,null,"15%");
		        
//		        el.closest('.ads').find('.layerOut').addClass('none');
//		        el.find('.layerOut').removeClass('none'); 
		
		   });   
		   $(document).on('click','.closeIcon',function(event){
		       
		       $(this).closest('div').addClass('none');
		       event.stopPropagation();
		   });
		   
		   //悬浮滚动
		   $('.combo,.webPage,.weChat').click(function(){
		        var cl = $(this).attr('class');
		        $('html,body').animate({scrollTop: $('.'+cl+'_content').offset().top}, 1500);
		    });
		    
		    //返回顶部
		    $('.backTop').click(function(){
		        $('html,body').animate({scrollTop: 0}, 1500);
		    });
		    
		    //返回底部
		    $('.backDown').click(function(){
		         $('html,body').animate({ scrollTop: document.body.clientHeight }, 1500);
		    });
		    
		    $(window).bind("scroll",function(){
		        
		        var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
		        if(scrollTop > 300){
		            $('.floatTip').removeClass('none');
		        }
		        else{
		            $('.floatTip').addClass('none');
		        }
		    });
		    
		     //在线客服链接
			$(".qqlink").bind("click",function(){
	        	util.setqq();
	        });
		    $('#payInfo').bind("click",function(){
		    	getPayWayDialog();
		    })
		    
		});
	}
	init();
})



