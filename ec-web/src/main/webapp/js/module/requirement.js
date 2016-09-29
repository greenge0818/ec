/*
 * author：chengf
 * date:2016-5-12
 * des:询价管理JS交互，
 */
define("requirement",function(require, exports, module){
	"use strict";
	var $ = require("jquery"),cache=require("cache"),util=require("util"),tips = require("jquery-tips");
	var requirement = {
		init:function(){
			var el = $(".left-content").children("div[name='list']");
  			$(function(){
  				$("li[name='requirement-tab']").css("background-color","#9e2324");
			   // $("#endDate").val(requirement.parseLong2Date(new Date().getTime()));
  				var d = new Date().getTime();
  				requirement.isFirisLoad = true;
  				requirement.getData(requirement.pageIndex);
  				requirement.eventBind();
  				$(document).on("click", ".tomycart",function () {
		          	util.closeDialog();
		        });
  			})
		},
		time:"",
		pageIndex:1,
		cacheList:[],
		currentStatus:"",
		eventBind:function(){
			$(".sch-btn").bind("click",function(){
			    requirement.isFirisLoad = true;
				requirement.getData(requirement.pageIndex);
				
			});
			//tab页签切换事件处理
			$(".item").bind("click",function(){
				var pty = $(this).attr("data-pty");
				requirement.currentStatus = pty;
				requirement.isFirisLoad = true;
				$("#startDate").val("");
				$("#endDate").val("");
				requirement.getData(requirement.pageIndex);
				$(".item").removeClass("item-color");
				$(this).addClass("item-color")
			});
			//关闭弹层
			$(document).on("click", ".close-btn",function () {
	            util.closeDialog();
	        });
	        //付款方式
	        $(document).on("click", ".show-pay-way",function () {
	          	requirement.getPayWayDialog();
	        });
	        
		},
		//付款方式UI以及弹层
		getPayWayDialog:function(){
			var el="";
			 var el = "";
	       	el +="<div class='payway' >";
	       	el +="<div class='p-head'>";
	       	el +="<div style='float:left'>付款方式</div>";
	       	el +="</div>";
	       	el +="<div class='p-body'>";
	       	el +="<div class='p-list'>";
	       	el +="<ul class='d-list-ul f-clrfix' >";
	       	el +="<li class='list02 tl borderbottom'>公司名称</li>";
	       	el +="<li class='list03 tl borderbottom'>行号</li>";
            el +="<li class='list05 tl borderbottom'>开户银行</li>";
            el +="<li class='list06  borderbottom'>账号</li>";
            el +="<li class='list02 tl pad12 borderbottom'>杭州钢为网络科技有限公司</li>";
	       	el +="<li class='list03 tl pad12 borderbottom'>310331000068</li>";
            el +="<li class='list05 tl pad12 borderbottom'>上海浦东发展银行杭州钱塘支行</li>";
            el +="<li class='list06 tr pad12 borderbottom'>95090154800004792</li>";
       	    el +="<li class='list02 tl pad12'>杭州钢为网络科技有限公司</li>";
	       	el +="<li class='list03 tl pad12'>102331002069</li>";
            el +="<li class='list05 tl pad12'>中国工商银行杭州湖墅支行</li>";
            el +="<li class='list06 tr pad12'>1202020619900121479</li>";
	       	el +="</ul>";
	       	el +="</div>";
	       	el +="</div>";
	       	el +="<div class='detail-c'>";
	       	el +="</div>";
	        el +="</div>";
			util.getDialog(false, el);
		},
		//根据判断city是否存在，来控制是否需要显示
		getDetailCity : function(city){
			if(city == null || city == "null" || city ==""){
				return '<span> | </span><span>交货地：<span>'+city+'</span></span>';
			}else{
				return "";
			}
		},
		/*获取明细弹层中的已报价/已完成/关闭（已报价）的列表UI
		 */
		getDetailQuotedList:function(item){
			var str = "";
			str+= '<div class="paddud">';
			str+= '<ul class="d-list-ul f-clrfix" >';
			str+= '<li class="d-list-bar list02">';
			str+= '<dl class="prod-name-dl"><dt class="dt"><span class="name">'+item.categoryName+ ' <em>'+item.materialName+'</em></span></dt>';
			str+= '<dd class="dd"><span class="s-gray">规格：</span>'+this.getSpec(item)+'</dd>';
			str+= '</dl>';
			str+= '</li>';
			str+= '<li class="d-list-bar list03 ">';
			str+= '<dl><dt class="ellipsis"><span class="s-gray">厂家：</span><span class="tips" data-tip="'+item.factoryName+'">'+item.factoryName+'</span></dt>';
			str+= '<dd><span class="s-gray">重量：</span>'+util.setFormat(item.weight,true)+' 吨('+item.weightConcept+')</dd>';
			str+= '</dl>';
			str+= '</li>';
			str+= '<li class="d-list-bar list04">';
			str+= '<dl><dt><span class="s-gray"></span>￥'+util.setFormat(item.price)+'/吨</dt>';
			str+= '<dd class="ellipsis"><span class="s-gray">仓库：</span><span class="tips" data-tip="'+item.cityName+" "+item.warehouseName+'">'+item.cityName+" "+item.warehouseName+'</span></dd>';
			str+= '</dl>';
			str+= '</li>';
			str+= '</ul>';
			str+= '</div>';
			return str;
		},
		//获取明细弹层中的已报价/已完成/关闭（已报价）的UI
		getDetailQuoted:function(ele,des){
			var el = "";
			el +="<div class='item-c'>";
	       	el +="<span class='left-logo circle-sm bgurl icon-16'></span>";
	       	el +="<div class='title f-clrfix'>";
	       	el += "<div class='f-fl'><span class='status'>"+des+"</span> <span>"+this.getDatebyLongTime(ele.created,"-")+"</span></div>";
	       	el += "<div class='f-fr'>钢为掌柜-<span>"+ele["operator"]+"</span> ( <span>"+ele["mobile"]+" )</span></div>";
	       	el +="</div>";
	       	el +="<div class='detail-list no-border-bottom'>";
	       	var list = ele.items;
			if(list){
				for(var i=0;i<list.length;i++){
					el +=this.getDetailQuotedList(list[i]);
				}
			}
	       	el +="</div>";
	       	el +="</div>";
	       	return el ;
		},
		getDetailInquotedList:function(ele){
			var str="";
			str +='<div  class="paddud" >';
			str += '<ul class="d-list-ul f-clrfix" >';
			str += '<li class="d-list-bar list02">';
			str += '<dl class="prod-name-dl"><dt class="dt ellipsis width200 " ><span class="name">'+ele.categoryName+ ' <em class="tips" data-tip="'+ele.materialName+'">'+ele.materialName+'</em></span></dt>';
			str += '<dd class="dd ellipsis width200 " ><span class="s-gray">规格：</span><span class="tips" data-tip="'+(this.getSpec(ele))+'">'+(this.getSpec(ele))+'</span></dd>';
			str += '</dl>';
			str += '</li>';
			str += '<li class="d-list-bar list03">';
			str +='<dl><dt class="ellipsis " ><span class="s-gray">厂家：</span><span class="tips" data-tip="'+ele.factoryName+'">'+ele.factoryName+'</span></dt>';
			str += '<dd><span class="s-gray">重量：</span>'+util.setFormat(ele.weight,true) +"吨("+ele.weightConcept+')</dd>';
			str +='</dl>';
			str += '</li>';
			str +='<li class="d-list-bar list04">';
			str += '待报价';
			str +='</li>';
			str +='</ul>';
			if(util.isNotBlank(ele.remark)){
				str +='<div class="request-c" >';
				str +='<span ><span class="s-gray">要求：</span>'+ele.remark+'</span>';
				str +='</div>';
			}
			str +='</div>';
			return str;
		},
		getDetailInquoted:function(ele,status){
			var el = "";
			el +="<div class='item-c'>";
	       	el +="<span class='left-logo circle-sm bgurl icon-16'></span>";
	       	el +="<div class='title f-clrfix'>";
	       	el += "<div class='f-fl'><span class='status'>"+status+"</span> <span>"+this.getDatebyLongTime(ele.created,"-")+"</span></div>";
	       	el +="</div>";
	       	el +="<div class='detail-list no-border-bottom'>";
	       	var list = ele.items;
			if(list){
				for(var i=0;i<list.length;i++){
					el+=this.getDetailInquotedList(list[i]);
				}
			}
	       	el +="</div>";
	       	el +="</div>";
	       	return el ;
		},
		getDetailByCartList:function(item,isLast){
			var str="";
			if(isLast){
				str +='<div  class="paddud noborder-bottom">';
			}else{
				str +='<div  class="paddud">';
			}
			str += '<ul class="d-list-ul f-clrfix" >';
			str +='<li class="d-list-bar list10">'
			str +='<span class="name">'+item.categoryName+' <em>'+item.materialName+'</em></span>';
			str +='</li>'
			str +='<li class="d-list-bar list10">'
			str +='<span class="dd"><span class="s-gray">规格：</span>'+(this.getSpec(item))+'</span>';
			str +='</li>';
			str +='<li class="d-list-bar list10">';
			str +='<span class="dd"><span class="s-gray">采购量：</span>'+ (item.weight !=null ? (util.setFormat(item.weight,true)+' 吨') : ("-"))+'</span>';
			str +='</li>';
			str +='<li class="d-list-bar list08 ellipsis">';
			str +='<span class="dd"><span class="s-gray">厂家：</span><span class="tips" data-tip="'+(item.factoryName)+'">'+(item.factoryName)+'</span></span>';
			str +='</li>';
			str +='</ul>';
			str +='</div>';
			return str;
		},
		getDetailByCart:function(ele,status){
			var el = "";
			el +="<div class='item-c'>";
	       	el +="<span class='left-logo circle-sm bgurl icon-16'></span>";
	       	el +="<div class='title f-clrfix'>";
	       	el += "<div class='f-fl'><span class='status'>"+status+"</span> <span>"+this.getDatebyLongTime(ele.created,"-")+"</span></div>";
	       	el +="</div>";
	       	el +="<div class='detail-list'>";
	       	var list = ele.items;
			if(list){
				for(var i=0;i<list.length;i++){
					if(i==list.length-1){
						el +=this.getDetailByCartList(list[i],true);
					}else{
						el +=this.getDetailByCartList(list[i]);
					}
					
				}
			}
	       	el +="</div>";
	       	el +="</div>";
	       	return el ;
		},
		getDetailByReceipt:function(ele,status){
			var el = "";
			el +="<div class='item-c'>";
	       	el +="<span class='left-logo circle-sm bgurl icon-16'></span>";
	       	el +="<div class='title f-clrfix'>";
	       	el += "<div class='f-fl'><span class='status'>"+status+"</span> <span>"+this.getDatebyLongTime(ele.created,"-")+"</span></div>";
	       	el +="</div>";
	       	el +="<div class='detail-list' style='padding:16px;'>";
	       	el +="您发布了一次采购需求";
	       	el +="</div>";
	       	el +="</div>";
	       	return el ;
		},
		
		getDetailByImg:function(ele,status){
			var el = "";
			el +="<div class='item-c'>";
	       	el +="<span class='left-logo circle-sm bgurl icon-16'></span>";
	       	el +="<div class='title f-clrfix'>";
	       	el += "<div class='f-fl'><span class='status'>"+status+"</span> <span>"+this.getDatebyLongTime(ele.created,"-")+"</span></div>";
	       	el +="</div>";
	       	el +="<div class='detail-list' style='padding:16px;'>";
	       	var urlArr = ele.fileUrl.split(",");
			for(var i=0;i<urlArr.length;i++){
				el +=this.getImgHtml(urlArr[i]);
			}
	       	el +="</div>";
	       	el +="</div>";
	       	return el ;
		},
		getDetailByFile :function(ele,status){
			var el = "";
			el +="<div class='item-c'>";
	       	el +="<span class='left-logo circle-sm bgurl icon-16'></span>";
	       	el +="<div class='title f-clrfix'>";
	       	el += "<div class='f-fl'><span class='status'>"+status+"</span> <span>"+this.getDatebyLongTime(ele.created,"-")+"</span></div>";
	       	el +="</div>";
	       	el +="<div class='detail-list' "+(((ele.request == null || ele.request=="null" || ele.request == "") && !(ele.fileUrl && ele.fileUrl.length>0 )) ? "style='padding:16px'" :"")+">";
	       	if(!(ele.request == null || ele.request=="null" || ele.request == "") ){
				el+='<div class="detail-myreq bold '+((ele.fileUrl && ele.fileUrl.length>0) ? "" : "noborder-bottom")+'">';
				el+=ele.request;
				el+='</div>';
			}
	       	
	       	if(ele.fileUrl && ele.fileUrl.length>0 ){
	       		el +="<div  class='paddud noborder-bottom' >";
	       		el+='<ul class="d-list-ul f-clrfix" >';
				var urlArr = ele.fileUrl.split(",");
				if(urlArr.length>0){
					for(var i=0;i<urlArr.length;i++){
						el+=this.getFileList(urlArr[i]);
					}
				}
				el+='</ul>';
				el +="</div>";
			}
			
			
	       	el +="</div>";
	       	el +="</div>";
	       	return el ;
			
		},
		getDetailDialog:function(data){
			var first  = data[0];
			 var el = "";
	       	el +="<div style='width:877px;text-align:center'>";
	       	el +="<div style='height:20px;line-height:20px;padding:26px 65px;background-color:#f3f8ff'>";
	       	el +="<div style='float:left;font-size:14px;'><span>询价单号：</span><span style='color:#1d7ad9' id='codeNumber'>"+first["code"]+"</span> | 来源：<span id='source'>"+this.getSource(first["source"])+"</span>"+(first['city'] == null ? "" :" | 交货地：<span id='place'>"+first['city']+"</span>")+"</div><div style='float:right"+(first['amount'] == null ? ";display:none" :"")+"'>订单金额：<span style='color:#ff5a00' id='amount'>￥"+util.setFormat(first['amount'])+"</span></div>";
	       	el +="</div>";
	       	el +="<div class='detail-c'>";
	       	el +="</div>";
	       	el +="<div style='margin-bottom:29px;'><a style='color:#1d7ad9' class='close-btn'>关闭</a></div>";
	        el +="</div>";
	        util.getDialog(false, el,false,null,null,null);
	        this.getDetailCallback(data);
	        
		},
		getDetail :function(code){
			util.ajax({
				config:{
					data:{requirementCode:code},
					url:"/api/member/viewdetails",
					success:function(response, status){
						if(response.code == "9001"){
							requirement.getDetailDialog(response.data);
						}
						
					}
				}
			})
//			util.post("/api/requirement/pickup/getDetailsByCode.html", {"code":code}, function (response, status) {
//				requirement.getDetailDialog(data);
//          },"1");
		},
		showMore:function(){
			$("span[name='morebtn']").bind("click",function(){
				var elArr = $(this).closest(".detail-offer-bottom").children("div[name='hide-c']"),
				temp = $(this).closest(".detail-offer-bottom").children(".paddud");
				var flag = elArr.eq(0).is(":visible");
				if(flag){
					$(this).removeClass("point-up").addClass("point-down");
					elArr.hide();
					temp.eq(2).removeClass("bottomborder");
				}else{
					$(this).removeClass("point-down").addClass("point-up");
					elArr.show();
					if($(this).attr("data-border") != "0"){
						temp.eq(2).addClass("bottomborder");
					}
					temp.last().addClass("nobottomborder");
				}
				
			})
		},
		getStatusBycode:function(status){
			var map = {
				"FINISHED":"已付款",
				"QUOTED":"已报价",
				"NEW":"待确认",
				"CLOSED" : "已关闭",
				"PICKED" :"报价中"
			}
			return map[status];
		},
		//分页函数
		initPage:function(total){
			util.initPage(total,function(pageIndex){
				requirement.getData(pageIndex);
			});
		},
		isFirisLoad : false,
		total:0,
		//获取列表后的回调函数，负责UI渲染等
		callback :function(response){
			requirement.total = response.recordsFiltered == null ? 0 :response.recordsFiltered;
			if(requirement.isFirisLoad){
				requirement.initPage(requirement.total);
				requirement.isFirisLoad = false;
				$("#m-page").show();
			}
			if(response.data && response.data.length>0){
				var data = response.data,i=0,ele, el = $(".left-content").children("div[name='list']");
				//requirement.pageIndex++;
				requirement.cacheList =data;
				el.empty()
				for(;i<data.length;i++){
					ele = data[i];
					var des =requirement.getStatusBycode( ele["stageStatus"]);
					if(ele["stageStatus"] == "NEW" || (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'PICKUP')){
						var type= ele["type"];
						if(type == "RECEIPT"){
							el.append(requirement.getDemandHtml(ele,des));
						}else if(type == "IMAGE"){
							el.append(requirement.getDemadByImg(ele,des));
						}else if(type == "HELP"){
							el.append(requirement.getDemandByFile(ele,des));
						}else if(type=="CART" || type=="ONEMORE"){
							el.append(requirement.getDemandByList(ele,des));
						}
					}else if(ele["stageStatus"] == "FINISHED" || ele["stageStatus"] == "QUOTED" ||  (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'QUOTED') ||  (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'BILL')){
						el.append(requirement.getQuotedHtml(ele,des,i,ele["stageStatus"] == "CLOSED"));
						if(requirement.quoteId>=0){
							$("#quoteIndex_"+i).bind("click",function(){
								requirement.oneMore(this);
							})
						}
					}else if(ele["stageStatus"] == "PICKED" || (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'INQUIRY')){
						el.append(requirement.getForQuoteHtml(ele,des));
						
					}
				}
			}else if(response.code == 8001){
				//requirement.dataIsNull = true;
				
			}
			//requirement.initPage();
			$(".codenumber").bind("click",function(){
				var id = $(this).text();
				requirement.getDetail(id);
				
			})
			$(".tips").tips("bottom");
		},
		//弹层回调函数
		getDetailCallback :function(data){
			
			var i=0,ele, el = $(".detail-c");
			el.empty();
			for(;i<data.length;i++){
				ele = data[i];
				var des =requirement.getStatusBycode( ele["stageStatus"]);
				if(ele["stageStatus"] == "NEW" || (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'PICKUP')){
					var type= ele["type"];
					if(type == "RECEIPT"){
						el.append(requirement.getDetailByReceipt(ele,des));
					}else if(type == "IMAGE"){
						el.append(requirement.getDetailByImg(ele,des));
					}else if(type == "HELP"){
						el.append(requirement.getDetailByFile(ele,des));
					}else if(type=="CART" || type=="ONEMORE"){
						el.append(requirement.getDetailByCart(ele,des));
					}
				}else if(ele["stageStatus"] == "FINISHED" || ele["stageStatus"] == "QUOTED" ||  (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'QUOTED') ||  (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'BILL')){
					el.append(requirement.getDetailQuoted(ele,des));
				}else if(ele["stageStatus"] == "PICKED" || (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'INQUIRY')){
					el.append(requirement.getDetailInquoted(ele,des));
					
				}
			}
			$(".item-c").last().css("borderLeft","0px solid #ccc");
			setTimeout(function(){
				$(".tips").tips("bottom");
			},300)
			
		},
		getData:function(pageIndex){
			var startDate = $("#startDate").val(),endDate = $("#endDate").val(),status =  requirement.currentStatus;
			var param = {
				start:pageIndex
			}
			if(status!=""){
				param.stageStatus = status;
			}
			if(startDate!=""){
				param.startTime = startDate;
			}
			if(endDate!=""){
				param.endTime = endDate;
			}
			util.post("/api/member/searchrequirement", param, function (response, status) {
				$(".sub-content").replaceWith("");
				var timeout =  setTimeout(function(){
					//$("#loading_c").remove();
					requirement.callback(response);
					requirement.showMore();
				},300);
            },"1");
		},
		oneMore : function(el){
			var obj = requirement.cacheList[el.id.split("_")[1]];
			var arr = obj.items;
			var config = {
				url : "/api/member/oncemore",
				data:JSON.stringify(arr),
				contentType:"application/json",
				success:function(data){
					 if(data.code == "4008"){
                    	requirement.getToMycartDialog();
                    	$(".circle").text(data.data);
                    }
				}
			}
			util.ajax({config:config});
		},
		getSource:function(source){
			return source == "APP" ? "APP" : (source =="PICK" ? "分检" : "超市");
		},
		getTitle:function(e){
			if(e["stageStatus"] == "NEW"){
				return "钢为掌柜收到了您的采购需求，我们将用 <span style='color:#c81623' >0571-8971&nbsp;8799</span> 给您回电，请保持手机畅通"
			}
			return  "" ;
		},
		getToMycartDialog:function(){
			 var el = "";
	       	el +="<div style='width:335px;height:160px;text-align:center'>";
	       	el +="<div style='height:20px;margin-top:45px;margin-bottom:34px;line-height:20px;'>已加入购物车</div>";
	       	el +="<div style='margin-bottom:29px;'><a class='tomycart' style='display:inline-block;width:129px;height:40px;font-size:14px;background-color:#c81623;color:#fff;line-height:40px' target='_blank' href='"+cache.base_url+"/cart/mycart'>去购物车查看</a></div>";
	        el +="</div>";
	        util.getDialog(false, el);
		},
		getCloseReason:function(ele,status){
			var str="";
			if(ele["stageStatus"] == "CLOSED"){
				str +=	'<span class="status tips" data-tip="'+ele["closeReason"]+'">'+status+'</span>';
			}else{
				str +=	'<span class="status">'+status+'</span>';
			}
			return str;
		},
		getDemandHtml :function(ele,status){
			var htmlStr = "";
				htmlStr +=	'<div class="sub-content no-border-left">';
				htmlStr +=	'<div class="detail-frame detail-frame-wth ht">';
				htmlStr +=	'<div class="detail-mid bottomnone">';
				htmlStr +=	'<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span class="codenumber">'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span>';
				htmlStr +=this.getCloseReason(ele,status);
				htmlStr +=	'</div>';
                if (this.getTitle(ele) != "") {
                    htmlStr += '<div class="detail-head background-white">';
                    htmlStr += '<h2 class="font-color-999">' + this.getTitle(ele) + '</h2>';
                    htmlStr += '</div>';
                }
				htmlStr +=	'</div>';
				htmlStr +=	'</div>';
			return htmlStr
		},
		getDatebyLongTime:function(time,format){
			var d = new Date(time);
			return d.getFullYear()+(format ? format :"年")+(((d.getMonth()+1)+"").length == 1 ? ("0"+(d.getMonth()+1)): (d.getMonth()+1) )+(format ? format :"月")+(d.getDate() > 9 ? d.getDate() : ("0"+d.getDate()))+(format ? " " :"日 ") + ((d.getHours()+"").length == 1 ? ("0"+d.getHours()) : (d.getHours()))+":"+((d.getMinutes()+"").length == 1 ? ("0"+d.getMinutes()) : (d.getMinutes())) ;
		},
		parseLong2Date:function(long){
			var d = new Date(long);
			return d.getFullYear()+"-"+(((d.getMonth()+1)+"").length == 1 ? ("0"+(d.getMonth()+1)): (d.getMonth()+1) )+"-"+(d.getDate() > 9 ? d.getDate() : ("0"+d.getDate()));
		},
		parseDate2Long:function(y,m,d,h,min,sec,ms){
			return new Date(y,m,d,h,min,sec,ms).getTime();
		},
		getDemadByImg : function(ele,status){
			var htmlStr ="";
			htmlStr +=  '<div class="sub-content no-border-left">';
			htmlStr +=	'<div class="detail-frame detail-frame-wth ht">';
			htmlStr +=	'<div class="detail-mid ">';
			htmlStr +=	'<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span class="codenumber">'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span>';
			htmlStr +=this.getCloseReason(ele,status);
			htmlStr +=	'</div>';
            if (this.getTitle(ele) != "") {
                htmlStr += '<div class="detail-head background-white">';
                htmlStr += '<h2 class="font-color-999">' + this.getTitle(ele) + '</h2>';
                htmlStr += '</div>';
            }
			htmlStr +=	'<div class="detail-img-bottom">';
			var urlArr = ele.fileUrl.split(",");
			for(var i=0;i<urlArr.length;i++){
				htmlStr +=this.getImgHtml(urlArr[i]);
			}
			htmlStr +=	'</div>';
			htmlStr +=	'</div>';
			htmlStr +=	'</div>';
			return htmlStr;
		},
		getImgHtml :function(src){
			var str = "";
			str += '<div class="img-content" style="width:90px;height:90px;">';
			str += '<img style="width:90px;height:90px;" src="'+src+'">';
			str += '</div>';
			return str;
		},
		quoteId:-1,
		getQuotedHtml : function(ele,des,index,closed){
			var htmlStr = "";
			htmlStr += '<div class="sub-content no-border-left">';
			htmlStr += '<div class="detail-frame detail-frame-wth ht">';
			htmlStr += '<div class="detail-mid bottomnone">';
			htmlStr += '<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span class="codenumber">'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span><span> | </span><span>交货地：<span>'+ele.city+'</span></span>';
			htmlStr +=this.getCloseReason(ele,des);
			htmlStr += '</div>';
			htmlStr += '<div class="detail-offer-bottom">';
			var list = ele.items,weight=0,amount=0,total=0;
			if(list){
				total = list.length;
				for(var i=0;i<total;i++){
					weight +=list[i].weight;
					amount +=(list[i].weight*100 * list[i].price)/100;
					htmlStr +=this.getQuotedList(list[i],i,total);
				}
			}
			var show = false;//|| ele["stageStatus"] =="FINISHED"
			if(ele["stageStatus"] =="QUOTED"  ||((ele["stageStatus"] =="CLOSED") && ele["closeStage"] == "QUOTED")){
				show = true;
				this.quoteId = index;
			}else{
				this.quoteId = -1;
			}
			htmlStr += this.showOneMoreAndCount(total,util.setFormat(weight,true),util.setFormat(amount),this.quoteId,closed);
			htmlStr += '<div class="info-content relative" >';
			htmlStr += '<span class="info-content-span">'+this.getBottomTitle(ele)+'</span>';
			htmlStr += '</div>';
			htmlStr += '</div>';
			htmlStr += '</div>';
			htmlStr += '</div>';
			return htmlStr;
		},
		getSpec:function(item){
			var str = "";
			if(item.spec1 !=null && item.spec1 !="null" && item.spec1 !=""){
				str +=item.spec1;
			}
			if(item.spec2 !=null && item.spec2 !="null" && item.spec2 !=""){
				str +="*"+item.spec2;
			}
			if(item.spec3 !=null && item.spec3 !="null" && item.spec3 !=""){
				str +="*"+item.spec3;
			}
			return str;
		},
		getBottomTitle:function(ele){
			if(ele["stageStatus"] =="QUOTED"){
				return '如价格满意，请及时付款，以免报价失效；如需帮助，请联系钢为掌柜-'+ele.operator+'('+ele.mobile+')<span class="show-pay-way">查看付款方式</span>';
			}else if(ele["stageStatus"] =="FINISHED"){
				return "如需帮助，请联系钢为掌柜-"+ele.operator+'('+ele.mobile+')<span class="paddWay"></span>';
			}else if(ele["stageStatus"] =="CLOSED"){
				return "如需帮助，请联系钢为掌柜-"+ele.operator+'('+ele.mobile+')<span class="paddWay"></span>';
			}
		},
		getQuotedList:function(item,index,total){
			var str = "";
			if(total > 3){
				if(index == 2){
					str+= '<div class="paddud">';
				}else if(index >=3){
					str+= '<div class="paddud " name="hide-c" style="display:none">';
				}else{
					str+= '<div class="paddud bottomborder">';
				}
			}else{
				if(index == total-1 ){
					str+= '<div class="paddud">';
				}else{
					str+= '<div class="paddud bottomborder">';
				}
			}
			
			str+= '<ul class="d-list-ul f-clrfix" >';
			str+= '<li class="d-list-bar list02">';
			str+= '<dl class="prod-name-dl"><dt class="dt"><span class="name">'+item.categoryName+ ' <em>'+item.materialName+'</em></span></dt>';
			str+= '<dd class="dd"><span class="s-gray">规格：</span>'+this.getSpec(item)+'</dd>';
			str+= '</dl>';
			str+= '</li>';
			str+= '<li class="d-list-bar list03 ">';
			str+= '<dl><dt class="ellipsis"><span class="s-gray">厂家：</span><span class="tips" data-tip="'+item.factoryName+'">'+item.factoryName+'</span></dt>';
			str+= '<dd><span class="s-gray">重量：</span>'+util.setFormat(item.weight,true)+' 吨('+item.weightConcept+')</dd>';
			str+= '</dl>';
			str+= '</li>';
			str+= '<li class="d-list-bar list04">';
			str+= '<dl><dt><span class="s-gray"></span>￥'+util.setFormat(item.price)+'/吨</dt>';
			str+= '<dd class="ellipsis" ><span class="s-gray">仓库：</span><span class="tips" data-tip="'+item.cityName+" "+item.warehouseName+'">'+item.cityName+" "+item.warehouseName+'</span></dd>';
			str+= '</dl>';
			str+= '</li>';
			str+= '</ul>';
			str+= '</div>';
			return str;
		},
		showOneMoreAndCount:function(count,ton,amount,index,closed){
			var str = "";
			str+= '<div  class="oneMoreContent f-clrfix">';
			str+= '<span class="f-fl">共<span>'+count+'</span>条</span>'+(count >3 ? "<span name='morebtn' class='in-block bgurl icon-16 point-down hand'></span>" :"");
			if(index>=0){
				str+= '<div class="f-fr">';
				str+= '<span class="oneMore" id="quoteIndex_'+index+'">';
				str+= '再来一单';
				str+= '</span>';
				str+= '</div>';
			}
			str+= '<div class="amount-c f-fr" >';
			str+= '共计：<span class="'+(closed ? "color-999" : "color")+'">'+ton+'</span> 吨 合计：<span class="'+ (closed ? "color-999" :"color")+' bold">￥'+amount+'</span>';
			str+= '</div>';
			str+= '</div>';
			return str;
		},
		getForQuoteHtml : function(ele,status){
			var htmlStr ="";
			htmlStr += '<div class="sub-content no-border-left">';
			htmlStr += '<div class="detail-frame detail-frame-wth ht">';
			htmlStr += '<div class="detail-mid bottomnone">';
			htmlStr +=	'<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span class="codenumber">'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span> | <span>交货地：<span>'+(ele["city"] ? ele["city"] :"")+'</span></span>';
			htmlStr +=this.getCloseReason(ele,status);
			htmlStr += '</div>';
			htmlStr += '<div class="detail-offer-bottom">';
			var list = ele.items,weight=0,total = 0;
			if(list){
				total = list.length;
				for(var i=0;i<list.length;i++){
					htmlStr +=this.getForQuoteList(list[i],i,list.length);
					weight +=list[i]["weight"];
				}
			}
			htmlStr += '<div  class="oneMoreContent f-clrfix">';
			htmlStr += '<span class="f-fl">共<span>' +total+ '</span>条</span>'+(total >3 ? "<span name='morebtn'  class='in-block bgurl  icon-16 point-down hand'></span>" :"");	
			htmlStr += '<div class="amount-c f-fr pddr-16" >';
			htmlStr += '共计：<span class="color">'+util.setFormat(weight,true)+'</span> 吨';
			htmlStr +='</div>';
			htmlStr +='</div></div></div></div>';
			return htmlStr;
		
		},
		getForQuoteList : function(ele,index,total){
			var str="";
			
			if(total > 3){
				if(index == 2){
					str+= '<div class="paddud">';
				}else if(index >=3){
					str+= '<div class="paddud bottomborder" name="hide-c" style="display:none">';
				}else{
					str+= '<div class="paddud bottomborder">';
				}
			}else{
				if(index == total-1 ){
					str+= '<div class="paddud">';
				}else{
					str+= '<div class="paddud bottomborder">';
				}
			}
			
			
			
			
			//str +='<div  class="paddud" '+((total >3 && i>2) ? 'style="display:none" name="hide-c" ' : '')+'>';
			str += '<ul class="d-list-ul f-clrfix" >';
			str += '<li class="d-list-bar list02">';
			str += '<dl class="prod-name-dl"><dt class="dt ellipsis width200"><span class="name">'+ele.categoryName+ '<em class="tips" data-tip="'+ele.materialName+'"> '+ele.materialName+'</em></span></dt>';
			str += '<dd class="dd  ellipsis width200"><span class="s-gray">规格：</span><span class="tips" data-tip="'+(this.getSpec(ele))+'">'+(this.getSpec(ele))+'</span></dd>';
			str += '</dl>';
			str += '</li>';
			str += '<li class="d-list-bar list03">';
			str +='<dl><dt class="ellipsis "><span class="s-gray">厂家：</span><span class="tips" data-tip="'+ele.factoryName+'">'+ele.factoryName+'</span></dt>';
			str += '<dd><span class="s-gray">重量：</span>'+util.setFormat(ele.weight,true) +"吨("+ele.weightConcept+')</dd>';
			str +='</dl>';
			str += '</li>';
			str +='<li class="d-list-bar list04">';
			str += '待报价';
			str +='</li>';
			str +='</ul>';
			if(util.isNotBlank(ele.remark)){
				str +='<div class="request-c" '+((total >3 && index>2) ? "style='display:none' name='hide-c' " : "")+'>';
				str +='<span ><span class="s-gray">要求：</span>'+ele.remark+'</span>';
				str +='</div>';
			}
			str +='</div>';
			
			return str;
		},
		getDemandByList:function(ele,status){
			var htmlStr ="";
			htmlStr +='<div class="sub-content no-border-left">';
			htmlStr +='<div class="detail-frame detail-frame-wth ht">';		
			htmlStr +='<div class="detail-mid bottomnone">';
			htmlStr +=	'<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span class="codenumber">'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span>';
			htmlStr +=this.getCloseReason(ele,status);
			htmlStr +='</div>';
            if (this.getTitle(ele) != "") {
                htmlStr += '<div class="detail-head background-white">';
                htmlStr += '<h2 class="font-color-999">' + this.getTitle(ele) + '</h2>';
                htmlStr += '</div>';
            }
			htmlStr +='<div class="detail-offer-bottom">';
			var list = ele.items,total = 0;
			if(list){
				total = list.length;
				for(var i=0;i<list.length;i++){
					htmlStr +=this.getListForDemand(list[i],i,total);
//					if(i==list.length-1){
//						htmlStr +=this.getListForDemand(list[i],triue);
//					}else{
//						htmlStr +=this.getListForDemand(list[i]);
//					}
					
				}
			}
			htmlStr += '<div  class="oneMoreContent f-clrfix">';
			htmlStr += '<span class="f-fl">共<span>' +total+ '</span>条</span>'+(total >3 ? "<span name='morebtn' class='in-block bgurl  icon-16 point-down hand'></span>" :"");
			htmlStr += '<div class="amount-c f-fr pddr-16" >';
			htmlStr += '<span class="color"></span>';
			htmlStr +='</div>';
			htmlStr +='</div>';
			htmlStr +='</div>';
			return htmlStr;
		},
		getListForDemand:function(item,index,total){
			
			var str="";
			
			if(total > 3){
				if(index == 2){
					str+= '<div class="paddud">';
				}else if(index >=3){
					str+= '<div class="paddud bottomborder" name="hide-c" style="display:none">';
				}else{
					str+= '<div class="paddud bottomborder">';
				}
			}else{
				if(index == total-1 ){
					str+= '<div class="paddud">';
				}else{
					str+= '<div class="paddud bottomborder">';
				}
			}

			str +='<ul class="d-list-ul f-clrfix" >';
			str +='<li class="d-list-bar list05">'
			str +='<span class="name">'+item.categoryName+' <em>'+item.materialName+'</em></span>';
			str +='</li>'
			str +='<li class="d-list-bar list06">'
			str +='<span class="dd"><span class="s-gray">规格：</span>'+(this.getSpec(item))+'</span>';
			str +='</li>';
			str +='<li class="d-list-bar list07">';
			str +='<span class="dd"><span class="s-gray">重量：</span>'+(item.weight != null ? (util.setFormat(item.weight,true)+' 吨('+item.weightConcept+')') : ("-"))+'</span>';
			str +='</li>';
			str +='<li class="d-list-bar list08 ellipsis">';
			str +='<span class="dd "><span class="s-gray">厂家：</span><span class="tips" data-tip="'+(item.factoryName)+'">'+(item.factoryName)+'</span></span>';
			str +='</li>';
			str +='</ul>';
			str +='</div>';
			return str;
		},
		getDemandByFile:function(ele,status){
			var htmlStr="";;
			htmlStr+='<div class="sub-content no-border-left">';
			htmlStr+='<div class="detail-frame detail-frame-wth ht">';
			htmlStr+='<div class="detail-mid bottomnone">';
			htmlStr+='<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span class="codenumber">'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span>';
			htmlStr +=this.getCloseReason(ele,status);
			htmlStr+='</div>';
			if(this.getTitle(ele) !=""){
				htmlStr+='<div class="detail-head background-white">';
				htmlStr+='<h2 class="font-color-999">'+this.getTitle(ele)+'</h2>';
				htmlStr+='</div>';
			}
			if(!(ele.request == null || ele.request=="null" || ele.request == "") ){
				htmlStr+='<div class="detail-myreq bold" '+(ele.fileUrl && ele.fileUrl.length>0 ? '' :'style="border-bottom:0px"')+' title="'+ele.request+'">';
				htmlStr+=ele.request;
				htmlStr+='</div>';
			}
			if(ele.fileUrl && ele.fileUrl.length>0 ){
				var urlArr = ele.fileUrl.split(",");
				if(urlArr.length>0){
					htmlStr+='<div class="detail-offer-bottom">';
					htmlStr+='<div  class="paddud">';
					htmlStr+='<ul class="d-list-ul f-clrfix" >';
					for(var i=0;i<urlArr.length;i++){
						htmlStr+=this.getFileList(urlArr[i]);
					}
					htmlStr+='</ul>';
					htmlStr+='</div>';
					htmlStr+='</div>';
				}
			}
			
			htmlStr+='</div>';
			htmlStr+='</div>';
			return htmlStr;
		},
		getFileList :function(url){
			//var urlArr = requirement.ReplaceAll(url,"\\","/").split("/");
			var urlArr=url.replace(/\\/g,"/").split("/");
			var str="";
			str+='<li class="d-list-bar list09">';
			str+='<span class="color-1D7AD9"><span class="bgurl file icon-26" >&nbsp;</span>'+urlArr[urlArr.length-1]+'</span>';
			str+='</li>'
			return str;
		}
   
	}
	module.exports = {
		init : requirement.init
	}
})
