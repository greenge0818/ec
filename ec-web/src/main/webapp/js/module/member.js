/*author:chengf;
 * date:2016-05-05;
 * description:会员中心交互
 */
define("member",function(require, exports, module){
	"use strict";
	var $ = require("jquery"),cache=require("cache"),util=require("util"),tips = require("jquery-tips"),globalId=0;
	var member = {
		/**
		 * des:初始化函数，对外暴露的唯一接口。用于初始化会员中心的UI以及页面交互；
		 * 
		 * */
		init:function(){
			var el = $(".left-content");
			//滚动条滚动事件
			$(window).scroll(function() {  
		      //当内容滚动到底部时加载新的内容  
			      if ($(this).scrollTop() + $(window).height()>= $(document).height() && $(this).scrollTop() > 0) {
				      var loading = '<div id="loading_c" style="position:relative;margin:0px auto;bottom:0px;left:50%"><img src="'+cache.base_url+'/css/default/images/loading.gif"></div>';
				      if($("#loading_c").length == 0){
				      	 el.append(loading);
				      }
			          //当前要加载的页码  
			          if(!member.dataIsNull && !member.inRequest && member.cacheList.length>0){
			          	member.inRequest = true;
			          	var d = member.cacheList[member.cacheList.length-1]["created"];	
					 	 member.getData(d);
			          }else{
			          	setTimeout(function(){
							$("#loading_c").remove();
						},300)
			          }
			          
			    }  
			});  
  			$(function(){
  				//会员中心为当前模块，TAB中设置选中状态
  				$("li[name='member-tab']").css("background-color","#9e2324");
  				//页面初次加载时，增加loading 效果；
  				var loading = '<div id="loading_c" style="position:relative;margin:0px auto;bottom:0px;left:50%"><img src="'+cache.base_url+'/css/default/images/loading.gif"></div>';
			      if($("#loading_c").length == 0){
			      	 el.append(loading);
			      }
  				var d = new Date().getTime();
  				//获取数据，默认每次最多获取5条
  				member.getData(d);
  				
  				//绑定付款方式事件，显示付款方式的弹层
  				 $(document).on("click", ".show-pay-way",function () {
		          	member.getPayWayDialog();
		        });
		        //绑定再来一单事件
		         $(document).on("click", ".oneMore",function () {
		          	member.oneMore(this);
		        });
		         $(document).on("click", ".tomycart",function () {
		          	util.closeDialog();
		        });
  			})
  			
  			
		},
		time:"",
		//设置初始化页面
		pageIndex:0,
		//缓存后台获取到的数据，用于后期处理，比如再来一单。
		cacheList:[],
		//缓存数据的长度，
		cacheListLength:0,
		//用于判断前一次滚动条加载后数据是否已经获取完毕，如果获取完毕，则再次滚动时，不再下发请求获取数据，以免造成无谓的服务器压力
		dataIsNull : false,
		//用于阻止当数据还没返回时，继续滚动后，继续下发请求
		inRequest:false,
		//根据status获取对应的文案描述
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
		//获取付款方式弹层函数，
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
		/*
		 * 获取数据，回调函数处理：首先会根据返回数据，判断后台数据已经获取完毕，如果获取完毕，则不再下发请求，同时
		 * 将获取请求前的数据长度保留，将数据与原先缓存数据拼接（为了再来一单）。最后根据不同状态渲染不同的UI视图
		 */
		getData:function(d){
			util.post("/api/member/todolist", { lastTime:d,pageIndex:member.pageIndex,maxId:globalId}, function (response, status) {
				
				var timeout =  setTimeout(function(){
					$("#loading_c").remove();
					call();
				},300);
				function call(){
					if(response.code == 8000 && response.data.length>0){
						var data = response.data,i=0,ele, el = $(".left-content"),len;
						member.pageIndex++;
						member.cacheListLength = member.cacheList.length;
						member.cacheList = member.cacheList.concat(data);
						if(data.length < 5){
							member.dataIsNull = true;
						}
						for(;i<data.length;i++){
							len = member.cacheListLength;
							member.cacheListLength++;
							ele = data[i];
							var des =member.getStatusBycode( ele["stageStatus"]);
							if(ele["stageStatus"] == "NEW" || (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'PICKUP')){
								var type= ele["type"];
								if(type == "RECEIPT"){
									el.append(member.getDemandHtml(ele,des));
								}else if(type == "IMAGE"){
									el.append(member.getDemadByImg(ele,des));
								}else if(type == "HELP"){
									el.append(member.getDemandByFile(ele,des));
								}else if(type=="CART" || type=="ONEMORE"){
									el.append(member.getDemandByList(ele,des));
								}
							}else if(ele["stageStatus"] == null || ele["stageStatus"] == "null"){
								el.append(member.getHQZXHtml(ele));
								if(globalId == 0 || globalId * 1 > ele["id"] * 1) {
									globalId = ele["id"];
								}
							}else if(ele["stageStatus"] == "FINISHED" || ele["stageStatus"] == "QUOTED" ||  (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'QUOTED') || (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'BILL')){
								el.append(member.getQuotedHtml(ele,des,len));
							}else if(ele["stageStatus"] == "PICKED" || (ele["stageStatus"] == "CLOSED" && ele["closeStage"] == 'INQUIRY')){
								el.append(member.getForQuoteHtml(ele,des));
								
							}
						}
					}else if(response.code == 8001){
						//member.dataIsNull = true;
						
					}
					member.inRequest = false;
					$(".tips").tips("bottom");
					$(".sub-content").css("borderLeft","2px solid #e8e8e8").last().css("borderLeft","2px solid #fff");
				}
				
//              if(response.code == "3005" || response.code == "5002"){
//              	global.closeDialog();
//              	$.fn.shoppingcart('submit');
//              }else if(response.code == "2004"){
//              	$(".err-msg-span").show().html("").html("验证码错误");
//              }else if(response.code == "2002"){
//              	$(".err-msg-span").show().html("").html("验证码已失效");
//              }
            });
		},
		/*
		 *再来一单
		 * */
		oneMore : function(el){
			var obj = member.cacheList[el.id.split("_")[1]-0];
			var arr = obj.items;
			var config = {
				url : "/api/member/oncemore",
				data:JSON.stringify(arr),
				contentType:"application/json",
				success:function(data){
					 if(data.code == "4008"){
                    	member.getToMycartDialog();
                    	$(".circle").text(data.data);
                    }
				}
			}
			util.ajax({config:config});
//			$.ajax({
//				type:"post",
//				url:cache.base_url+"/api/member/oncemore",
//				dataType:"json",
//				data:JSON.stringify(arr),
//              contentType:"application/json",
//              success: function(data){
//                  if(data.code == "4008"){
//                  	member.getToMycartDialog();
//                  	$(".circle").text(data.data);
//                  }
//              }
//
//			});
		},
		getSource:function(source){
			return source == "APP" ? "APP" : (source =="PICK" ? "分检" : "超市");
		},
		getTitle:function(ele){
			if(ele["stageStatus"] == "NEW"){
				return "钢为掌柜收到了您的采购需求，我们将用<span class='color-c81623'> 0571-8971&nbsp;8799 </span>给您回电，请保持手机畅通";
			}else if(ele["stageStatus"] == "PICKED"){
				return "钢为掌柜正在为您找货，我们将用<span class='color-c81623'> 0571-8971&nbsp;8799 </span>给您回电，请保持手机畅通";
			}else if(ele["stageStatus"] == "QUOTED"){
				return "钢为掌柜-"+ele["operator"]+"（"+ele["mobile"]+"）给您的报价，请及时付款"
			}else if(ele["stageStatus"] == "FINISHED"){
				return "请联系钢为掌柜-"+ele["operator"]+"（"+ele["mobile"]+"）安排提货";
			}else if(ele["stageStatus"] == "CLOSED"){
				if(ele["closeStage"] == "BILL"){
					return "您的订单已取消";
				}
				return "您的询价已取消";
				
			}
			//return source == "APP" ? "通过掌柜APP发布了一个回执需求" : "通过超市发布了一个回执需求";
		},
		getToMycartDialog:function(){
			 var el = "";
	       	el +="<div style='width:335px;height:160px;text-align:center'>";
	       	el +="<div style='height:20px;margin-top:45px;margin-bottom:34px;line-height:20px;'>已加入购物车</div>";
	       	el +="<div style='margin-bottom:29px;'><a class='tomycart' style='display:inline-block;width:129px;height:40px;font-size:14px;background-color:#c81623;color:#fff;line-height:40px' target='_blank' href='"+cache.base_url+"/cart/mycart'>去购物车查看</a></div>";
	        el +="</div>";
	        util.getDialog(false, el);
		},
		//展现行情中心的UI
		getHQZXHtml:function(ele){
			var htmlStr = "";
			htmlStr += '<div class="sub-content">';
			htmlStr += '<span class="left-logo left-logo-z bgurl"></span>';
			htmlStr += '<div class="detail-frame">';
			htmlStr += '<div class="detail-head">';
			htmlStr += '<h2>'+"钢为网发布了新的行情分析"+'</h2>';
			htmlStr += '</div>';
			htmlStr += '<div class="detail-body">';
			htmlStr += '<div class="img-content"><a href="'+ele.url+'" target="_blank">';
			htmlStr += '<img src="'+ele.thumbnail+'" onerror="javascript:this.src=\''+cache.base_url+"/css/default/images/error.jpg"+'\';"  style="width:131px;height:102px;"/>';
			htmlStr += '</a></div>';
			htmlStr += '<div align="left" class="desribe-content in-block f-fr">';
			htmlStr += '<h2><a href="'+ele.url+'"  target="_blank">'+ele.title+'</a></h2>';
			htmlStr += '<a href="'+ele.url+'" target="_blank"><h4><span>作者&nbsp;</span><span>'+ele.author+'</span><span>&nbsp;|</span><span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>'+"来源：钢为网"+'</span></h4></a>';
			htmlStr += '<div class="des-abs">';
			htmlStr += '<a href="'+ele.url+'"  target="_blank">'+ele.text+'</a>';
			htmlStr += "</div></div></div></div></div>";
			return htmlStr;
		},
		getDemandHtml :function(ele,title){
			var htmlStr = "";
				htmlStr +=	'<div class="sub-content">';
				htmlStr +=	'<span class="left-logo left-logo-x bgurl"></span>';
				htmlStr +=	'<div class="detail-frame ht">';
				htmlStr +=	'<div class="detail-head">';
				htmlStr +=	'<h2>'+this.getTitle(ele)+'</h2>';
				htmlStr +=	'</div>';
				htmlStr +=	'<div class="detail-mid">';
				htmlStr +=	'<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span>'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span>';
				htmlStr +=	'<span class="status">'+title+'</span>';
				htmlStr +=	'</div>';
				htmlStr +=	'<div class="detail-bottom">';
				htmlStr +=	'<div><em class="bgurl tea in-block">&nbsp;</em><span style="color:#ffa352">请稍后片刻，钢为掌柜会尽快联系您</span></div>';
				htmlStr +=	'</div>';
				htmlStr +=	'</div>';
				htmlStr +=	'</div>';
			return htmlStr
		},
		getDatebyLongTime:function(time){
			var d = new Date(time);
			return d.getFullYear()+"年"+(((d.getMonth()+1)+"").length == 1 ? ("0"+(d.getMonth()+1)): (d.getMonth()+1) )+"月"+(d.getDate() > 9 ? d.getDate() : ("0"+d.getDate()))+"日 " + ((d.getHours()+"").length == 1 ? ("0"+d.getHours()) : (d.getHours()))+":"+((d.getMinutes()+"").length == 1 ? ("0"+d.getMinutes()) : (d.getMinutes())) ;
		},
		getDemadByImg : function(ele,title){
			var htmlStr ="";
			htmlStr +=  '<div class="sub-content">';
			htmlStr +=	'<span class="left-logo left-logo-x bgurl"></span>';
			htmlStr +=	'<div class="detail-frame ht">';
			htmlStr +=	'<div class="detail-head">';
			htmlStr +=	'<h2>'+this.getTitle(ele)+'</h2>';
			htmlStr +=	'</div>';
			htmlStr +=	'<div class="detail-mid">';
			htmlStr +=	'<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span>'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span>';
			htmlStr +=	'<span class="status">'+title+'</span>';
			htmlStr +=	'</div>';
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
		getQuotedHtml : function(ele,des,index){
			var htmlStr = "";
			htmlStr += '<div class="sub-content">';
			if(ele["stageStatus"] == "FINISHED" || ele["closeStage"] == "BILL"){
				htmlStr += '<span class="left-logo left-logo-c bgurl"></span>';
			}else{
				htmlStr += '<span class="left-logo left-logo-x bgurl"></span>';
			}
			
			htmlStr += '<div class="detail-frame ht">';
			htmlStr += '<div class="detail-head">';
			htmlStr += '<h2>'+this.getTitle(ele)+'</h2>';
			htmlStr += '</div>';
			htmlStr += '<div class="detail-mid">';
			htmlStr += '<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span>'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span><span> | </span><span>交货地：<span>'+ele.city+'</span></span>';
			htmlStr += '<span class="status">'+des+'</span>';
			htmlStr += '</div>';
			htmlStr += '<div class="detail-offer-bottom">';
			var list = ele.items,weight=0,amount=0,total=0;
			if(list){
				for(var i=0;i<list.length;i++){
					total = list.length;
					weight +=list[i].weight;
					amount +=(list[i].weight*100 * list[i].price)/100;
					if(i==list.length-1){
						htmlStr +=this.getQuotedList(list[i],true);
					}else{
						htmlStr +=this.getQuotedList(list[i]);
					}
					
				}
			}
			//???????
			var show = false;
			if(ele["stageStatus"] =="QUOTED" ||((ele["stageStatus"] =="CLOSED") && ele["closeStage"] == "QUOTED") ){
				show = true;
				this.quoteId = index;
			}else{
				this.quoteId = -1;
			}
			htmlStr += this.showOneMoreAndCount(total,util.setFormat(weight,true),util.setFormat(amount),this.quoteId);
			htmlStr += '<div class="info-content relative" >';
			htmlStr += '<span class="info-content-span">'+this.getBottomTitle(ele)+'</span>';
			htmlStr += '</div>';
			htmlStr += '</div>';
			htmlStr += '</div>';
			htmlStr += '</div>';
			return htmlStr;
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
		getQuotedList:function(item,isLast){
			var str = "";
			if(!isLast){
				str+= '<div class="paddud bottomborder">';
			}else{
				str+= '<div class="paddud">';
			}
			str+= '<ul class="d-list-ul f-clrfix" >';
			str+= '<li class="d-list-bar list02">';
			str+= '<dl class="prod-name-dl"><dt class="dt ellipsis"><span class="name tips" data-tip="'+item.categoryName+ ' '+item.materialName+'">'+item.categoryName+ ' <em>'+item.materialName+'</em></span></dt>';
			str+= '<dd class="dd ellipsis"><span class="s-gray">规格：</span><span class="tips" data-tip="'+this.getSpec(item)+'">'+this.getSpec(item)+'</span></dd>';
			str+= '</dl>';
			str+= '</li>';
			str+= '<li class="d-list-bar list03">';
			str+= '<dl><dt class="ellipsis"><span class="s-gray">厂家：</span><span class="tips" data-tip="'+item.factoryName+'">'+item.factoryName+'</span></dt>';
			str+= '<dd><span class="s-gray">重量：</span>'+util.setFormat(item.weight,true)+' 吨('+item.weightConcept+')</dd>';
			str+= '</dl>';
			str+= '</li>';
			str+= '<li class="d-list-bar list04">';
			str+= '<dl><dt><span class="s-gray"></span>￥'+util.setFormat(item.price)+'/吨</dt>';
			str+= '<dd class="dd ellipsis"><span class="s-gray">仓库：</span><span class="tips" data-tip="'+item.cityName+" "+item.warehouseName+'">'+item.cityName+" "+item.warehouseName+'</span></dd>';
			str+= '</dl>';
			str+= '</li>';
			str+= '</ul>';
			str+= '</div>';
			return str;
		},
		showOneMoreAndCount:function(count,ton,amount,index){
			var str = "";
			str+= '<div  class="oneMoreContent f-clrfix">';
			str+= '<span class="f-fl">共<span>'+count+'</span>条</span>';
			if(index>=0){
				str+= '<div class="f-fr">';
				str+= '<span class="oneMore" id="quoteIndex_'+(index)+'">';
				str+= '再来一单';
				str+= '</span>';
				str+= '</div>';
			}
			str+= '<div class="amount-c f-fr"  >';
			str+= '共计：<span class="color">'+ton+'</span> 吨 合计：<span class="color bold">￥'+amount+'</span>';
			str+= '</div>';
			str+='<div style="clear:both"> </div>'
			str+= '</div>';
			return str;
		},
		getForQuoteHtml : function(ele,title){
			var htmlStr ="";
			htmlStr += '<div class="sub-content">';
			htmlStr += '<span class="left-logo left-logo-x bgurl"></span>';
			htmlStr += '<div class="detail-frame ht">';
			htmlStr += '<div class="detail-head">';
			htmlStr +=	'<h2>'+this.getTitle(ele)+'</h2>';
			htmlStr += '</div>';
			htmlStr += '<div class="detail-mid">';
			htmlStr +=	'<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span>'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span>';
			htmlStr += '<span class="status">'+title+'</span>';
			htmlStr += '</div>';
			htmlStr += '<div class="detail-offer-bottom">';
			var list = ele.items,weight=0,total= 0;
			if(list){
				for(var i=0;i<list.length;i++){
					total = list.length;
					htmlStr +=this.getForQuoteList(list[i],(i == total-1));
					weight +=list[i]["weight"];
				}
			}
			
			htmlStr += '<div  class="oneMoreContent f-clrfix">';
			htmlStr += '<span class="f-fl">共<span>' +total+ '</span>条</span>';	
			htmlStr += '<div class="amount-c f-fr pddr-16" >';
			htmlStr += '共计：<span class="color">'+util.setFormat(weight,true)+'</span> 吨';
			htmlStr +='</div>';
			htmlStr +='</div></div></div></div>';
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
		getForQuoteList : function(ele,isLast){
			var str="";
			if(!isLast){
				str+= '<div class="paddud bottomborder">';
			}else{
				str+= '<div class="paddud">';
			}
			str += '<ul class="d-list-ul f-clrfix" >';
			str += '<li class="d-list-bar list02 ">';
			str += '<dl class="prod-name-dl"><dt class="dt ellipsis"><span class="name tips" data-tip="'+ele.categoryName+ ' '+ele.materialName+'">'+ele.categoryName+ ' <em>'+ele.materialName+'</em></span></dt>';
			str += '<dd class="dd ellipsis"><span class="s-gray">规格：</span><span class="tips" data-tip="'+this.getSpec(ele)+'">'+this.getSpec(ele)+'</span></dd>';
			str += '</dl>';
			str += '</li>';
			str += '<li class="d-list-bar list03">';
			str +='<dl><dt class="ellipsis"><span class="s-gray">厂家：</span><span class="tips" data-tip="'+ele.factoryName+'">'+ele.factoryName+'</span></dt>';
			str += '<dd><span class="s-gray">重量：</span>'+util.setFormat(ele.weight,true) +" 吨("+ele.weightConcept+')</dd>';
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
		getDemandByList:function(ele,title){
			var htmlStr ="";
			htmlStr +='<div class="sub-content">';
			htmlStr +='<span class="left-logo left-logo-x bgurl"></span>';
			htmlStr +='<div class="detail-frame ht">';		
			htmlStr +='<div class="detail-head">';
			htmlStr +=	'<h2>'+this.getTitle(ele)+'</h2>';
			htmlStr +='</div>';
			htmlStr +='<div class="detail-mid">';
			htmlStr +=	'<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span>'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span>';
			htmlStr +='<span class="status">'+title+'</span>';
			htmlStr +='</div>';
			htmlStr +='<div class="detail-offer-bottom">';
			var list = ele.items,total=0;
			if(list){
				total = list.length;
				for(var i=0;i<list.length;i++){
					if(i==list.length-1){
						htmlStr +=this.getListForDemand(list[i],true);
					}else{
						htmlStr +=this.getListForDemand(list[i]);
					}
					
				}
			}
			htmlStr += '<div  class="oneMoreContent f-clrfix">';
			htmlStr += '<span class="f-fl">共<span>' +total+ '</span>条</span>';	
			htmlStr += '<div class="amount-c f-fr pddr-16" >';
			htmlStr += '<span class="color"></span>';
			htmlStr +='</div>';
			htmlStr +='</div>';
			htmlStr +='</div>';
			htmlStr +='</div>';
			return htmlStr;
		},
		getListForDemand:function(item,isLast){
			var str="";
			if(!isLast){
				str +='<div  class="paddud bottomborder">';
			}else{
				str +='<div  class="paddud">';
			}
			str +='<ul class="d-list-ul f-clrfix" >';
			str +='<li class="d-list-bar list05 ellipsis">'
			str +='<span class="name tips" data-tip="'+item.categoryName+' '+item.materialName+'" >'+item.categoryName+' <em>'+item.materialName+'</em></span>';
			str +='</li>'
			str +='<li class="d-list-bar list06 ellipsis">'
			str +='<span class="dd"><span class="s-gray">规格：</span><span class="tips" data-tip="'+this.getSpec(item)+'">'+this.getSpec(item)+'</span></span>';
			str +='</li>';
			str +='<li class="d-list-bar list07">';
			str +='<span class="dd"><span class="s-gray">重量：</span>'+(item.weight !=null ?( util.setFormat(item.weight,true)+' 吨('+item.weightConcept+')') :("-"))+'</span>';
			str +='</li>';
			str +='<li class="d-list-bar list08 ellipsis">';
			str +='<span class="dd"><span class="s-gray">厂家：</span><span class="tips" data-tip="'+item.factoryName+'">'+(item.factoryName)+'</span></span>';
			str +='</li>';
			str +='</ul>';
			str +='</div>';
			return str;
		},
		getDemandByFile:function(ele,title){
			var htmlStr="";;
			htmlStr+='<div class="sub-content">';
			htmlStr+='<span class="left-logo left-logo-x bgurl"></span>';
			htmlStr+='<div class="detail-frame ht">';
			htmlStr+='<div class="detail-head">';
			htmlStr+='<h2>'+this.getTitle(ele)+'</h2>';
			htmlStr+='</div>';
			htmlStr+='<div class="detail-mid">';
			htmlStr+='<span>'+this.getDatebyLongTime(ele.created)+'</span><span> | </span><span>询价单号：<span>'+ele.code+'</span></span><span> | </span><span>来源：<span>'+this.getSource(ele.source)+'</span></span>';
			htmlStr+='<span class="status">'+title+'</span>';
			htmlStr+='</div>';
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
			//var urlArr = member.ReplaceAll(url,"\\","/").split("/");
			var urlArr=url.replace(/\\/g,"/").split("/");
			var str="";
			str+='<li class="d-list-bar list09">';
			str+='<span class="color-1D7AD9" title="'+urlArr[urlArr.length-1]+'"><span class="bgurl file icon-26" >&nbsp;</span>'+urlArr[urlArr.length-1]+'</span>';
			str+='</li>'
			return str;
		}
   
	}
	module.exports = {
		init : member.init
	}
})
