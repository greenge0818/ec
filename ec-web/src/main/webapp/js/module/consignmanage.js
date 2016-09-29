define("consign",function(require, exports, module){
	"use strict";
	var $ = require("jquery"),cache=require("cache"),util=require("util"),tips = require("jquery-tips");
	 require("prcsteel");	
	 require("jqueryMigrate");
	 var jqPrint =require("jqprint");
	var consign = {
		init:function(){
			var el = $(".left-content").children("div[name='list']");
  			$(function(){
  				$("li[name='purchase-tab']").css("background-color","#9e2324");
  				consign.isFirisLoad = true;
  				consign.getData(consign.pageIndex);
  				consign.eventBind();
  				$('#keyword').inputFocus();
  			})
		},
		time:"",
		pageIndex:1,
		cacheList:[],
		currentStatus:"",
		eventBind:function(){
			$(".sch-btn").bind("click",function(){
			    consign.isFirisLoad = true;
				consign.getData(consign.pageIndex);
				
			});
			$(".item").bind("click",function(){
				var pty = $(this).attr("data-pty");
				consign.currentStatus = pty;
				consign.isFirisLoad = true;
				$(".item").removeClass("item-color");
				$(this).addClass("item-color");
				$("#startDate").val("");
				$("#endDate").val("");
				consign.getData(consign.pageIndex);
				
			});
			$(document).on("click", ".close-btn",function () {
	            util.closeDialog();
	        });
			$(document).on("click", ".viewcontract",function () {
	            //consign.shwoDialog();
	        });
				        
	        $(document).on("click", "#print",function () {
	            $(".print-c").jqprint({
	            	debug:false,
	            	importCSS:true,
	            	operaSupport:true
	            });
	           //$(".item-c").jqprint(); 
	        });
		},
		shwoDialog:function(){
			var str = "";
			str +="<div style='width:888px;height:1080px'>";
			str +="<div style='height:1000px' class='print-c'><iframe width='100%' height='1000' frameborder='none' src='https://www.baidu.com'></iframe></div>";
			str +="<div style='height:63px;padding-top:15px' align='center'>";
			str +="<a class='contract-btn' id='print'>打印合同</a><a class='contract-btn'>下载合同</a> <a style='color:#1d7ad9;display:inline-block;vertical-align:bottom' class='close-btn'>关闭</a>";
			str +="</div>";
			str +="</div>";
			util.getDialog(false,str,false,null,null,null);
		},
		isFirisLoad : false,
		total:0,
		initPage:function(total){
			util.initPage(total,function(pageIndex){
				consign.getData(pageIndex);
			});
		},
			
		getStatus:function(status){
			var map = {
				"RELATED":"待付款",
				"SECONDSETTLE":"待结算",
				"FINISH":"交易完成",
				"CLOSED" : "交易关闭"
			}
			return map[status];
		},
		showMore:function(){
			$("span[name='morebtn']").bind("click",function(){
				var elArr = $(this).closest(".item-bottom").prev().find("div[name='hide-c']");
				var flag = elArr.eq(0).is(":visible"),arr = $(this).closest(".item-bottom").prev().find(".item-detail ");
				if(flag){
					$(this).removeClass("point-up").addClass("point-down");
					arr.eq(2).removeClass("borderbottom");
					elArr.hide();
				}else{
					$(this).removeClass("point-down").addClass("point-up");
					elArr.show();
					arr.eq(2).addClass("borderbottom");
					arr.last().addClass("borderbottom-none");
				}
				
			})
		},
		callback :function(response){
			
			if(response.code == "8003" ){
				consign.total = response.recordsTotal == null ? 0 :response.recordsTotal;
				if(consign.isFirisLoad){
					consign.initPage(consign.total);
					consign.isFirisLoad = false;
					$("#m-page").show();
				}
				var el = $("#list-c");
				el.empty();
				if(response.data && response.data.length>0){
					for(var i=0;i<response.data.length;i++){
						el.append(consign.concatHtml(response.data[i]));
					}
					$(".tips").tips("bottom");
				}
			}
		},
		getDatebyLongTime:function(time,format){
			var d = new Date(time);
			return d.getFullYear()+(format ? format :"年")+(((d.getMonth()+1)+"").length == 1 ? ("0"+(d.getMonth()+1)): (d.getMonth()+1) )+(format ? format :"月")+(d.getDate() > 9 ? d.getDate() : ("0"+d.getDate()))+(format ? " " :"日 ") + ((d.getHours()+"").length == 1 ? ("0"+d.getHours()) : (d.getHours()))+":"+((d.getMinutes()+"").length == 1 ? ("0"+d.getMinutes()) : (d.getMinutes())) ;
		},
		getAmountTitle:function(status){
			var maps = {
				"RELATED":"待付金额",
				"SECONDSETTLE":"已付金额",
				"FINISH":"实付金额",
				"CLOSED":"订单金额"
			};
			return maps[status];
		},
		getCloseReason:function(ele,status){
			var str="";
			if(ele["status"] == "CLOSED"){
				str +=	'<span class="status tips" style="text-decoration:underline" data-tip="'+ele.closeReason+'">'+this.getStatus(status)+'</span>';
			}else{
				str +=	'<span class="status">'+this.getStatus(status)+'</span>';
			}
			return str;
		},
		concatHtml:function(data){
			var html="";
			html += '<div class="item-c">';
			html += '<div class="item-head bg-color3">';
			html += '<ul class="d-list-ul f-clrfix" >';
			html += '<li class="d-list-bar list02"><dl class="prod-name-dl">';
			html += '<dt class="dt"><span class="s-gray">订单号：</span><em>'+data.code+'</em></dt>';
			html += '<dd class="dd"><span class="s-gray">下单时间：</span>'+consign.getDatebyLongTime(data.time)+'</dd>';
			html += '</dl></li>';
			html += '<li class="d-list-bar list03">';
			html += '<dl><dt><span class="s-gray">卖家：</span>'+data.accountName+'</dt>';
			html += '<dd><span class="s-gray">联系人：</span>'+data.contactName+" "+data.contactTel+'</dd>';
			html += '</dl></li>';
			html += '<li class="d-list-bar list04">';
			html += '<dl><dt><span class="s-gray">订单状态：</span>'+consign.getCloseReason(data,data.status)+'</dt>'
			html += '<dd><span class="s-gray">支付方式：</span>'+data.payType+'</dd>';
			html += '</dl>';
			html += '</li>';
			html += '<li class="d-list-bar list05">';
			html += '<dl><dt><span class="viewcontract color-1d7ad9" style="">&nbsp;</span></dt>';//查看合同
			html += '<dd><span class="s-gray">'+consign.getAmountTitle(data.status)+'：<span class="'+ ((data.status == "CLOSED") ? "s-gray" : "color-1")+' bold font-size-16">￥'+util.setFormat(data.totalAmount)+'</span></span></dd>'
			html += '</dl>';
			html += '</li>';
			html += '</ul>';
			html += '</div>';
			html += '<div class="item-body f-clrfix">';
			html += '<div class="item-detail-content f-fl">';
			var count = 0;
			if(data.items){
				var arr =data.items,count = arr.length;
				for(var i =0;i<count;i++){
					html +=(this.getDetailList(arr[i],i,count));
				}
			}
			html += '</div>';
			html += '<div class="f-fr item-body-right">';
			html += '<dl><dt><span class="s-gray">订单重量：'+util.setFormat(data.totalWeight,true)+' 吨</span></dt>';
			html += '<dd><span class="s-gray">实提重量：'+(data.actualPickWeight == null || data.actualPickWeight == 0 ? "-" : (util.setFormat(data.actualPickWeight,true))+" 吨")+'</span></dd>';
			html += '</dl>';
			html += '</div>';
			html += '</div>';
			html += '<div class="item-bottom bg-color3">';
			html += '<div class="oneMoreContent f-clrfix"><span class="f-fl">共<span>'+count+'</span>条</span>'+(count >3 ? "<span name='morebtn' class='in-block bgurl icon-16 point-down hand'></span>" :"");
			html += '</div>';
			html += '</div>';
			return html;
		},
		getDetailList:function(data,index,total){
			var str = "";
			if(total > 3){
				if(index == 2){
					str+= '<div class="item-detail borderbottom-none">';
				}else if(index >=3){
					str+= '<div class="item-detail " name="hide-c" style="display:none">';
				}else{
					str+= '<div class="item-detail ">';
				}
			}else{
				if(index == total-1 ){
					str+= '<div class="item-detail borderbottom-none">';
				}else{
					str+= '<div class="item-detail">';
				}
			}
			str += '<ul class="d-list-ul f-clrfix" >';
			str += '<li class="d-list-bar list02">';
			str += '<dl class="prod-name-dl">';
			str += '<dt class="dt"><span class="name">'+data.nsortName+'</span> <em>'+data.material+'</em></dt>';
			str += '<dd class="dd"><span class="s-gray">规格：</span>'+data.spec+'</dd>';
			str += '</dl>';
			str += '</li>';
			str += '<li class="d-list-bar list03">';
			str += '<dl><dt><span class="s-gray">厂家：</span>'+data.factory+'</dt>';
			str += '<dd><span class="s-gray">仓库：</span>'+data.city+" "+data.warehouse+'</dd>';
			str += '</dl>';
			str += '</li>';
			str += '<li class="d-list-bar list04">';
			str += '<dl><dt><span class="s-gray">重量：<span style="color:#333">'+util.setFormat(data.weight,true)+' 吨('+data.weightConcept+')'+'</span></span></dt>';
			str += '<dd><span class="s-gray">实提重量：</span>'+(data.actualPickWeight == null || data.actualPickWeight == 0 ? "-" : (util.setFormat(data.actualPickWeight,true))+" 吨")+'</dd>';
			str += '</dl>';
			str += '</li>';
			str += '<li class="d-list-bar list05">';
			str += '<dl><dt><span>￥'+util.setFormat(data.dealPrice)+'/吨</span></dt>';
			str += '</dl>';
			str += '</li>';
			str += '</ul>';
			str += '</div>';
			return str;
		},
		getData:function(pageIndex){
			var timeFrom = $("#startDate").val(),timeTo = $("#endDate").val(),status =  $(".item-color").attr("data-pty"),
			keyWord = $("#keyword").val();
			var param = {
				pageIndex:pageIndex
			}
			if(keyWord !=""){
				param.keyWord = keyWord;
			}
			if(status!=""){
				param.status = status;
			}
			if(timeFrom!=""){
				param.timeFrom = timeFrom;
			}
			if(timeTo!=""){
				param.timeTo = timeTo;
			}
			util.post("/api/member/getconsigninfo", param, function (response, status) {
				var timeout =  setTimeout(function(){
					//$("#loading_c").remove();
					consign.callback(response);
					consign.showMore();
				},300);
            },"1");
		}
		
   
	}
	module.exports = {
		init : consign.init
	}
})
