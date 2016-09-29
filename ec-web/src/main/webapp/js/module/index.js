define(function(require,exports,module){
	var $ = require("jquery"),
		util = require("util"),
		ajaxFileUpload = require("ajaxfileupload"),
		caslogin = require("caslogin"),
		constant = require("constant"),
		prcsteel=require("prcsteel"),
		cache = require("cache"),
		tips = require("checkformtips"),
		cas = require("cas"),
		gwfinddialog = require("gwfind.dialog");
		window.cas = cas;
	var FORMTEXT = "如：中天18的四级抗震螺纹，40吨，杭州交货",
		imgUrlstr = [],
		timeLong=60,
		timeLong1 = 60,
		timeoutFlag = false;
	var index = {
		init:function(){
			/* 热门行情资讯删除最后一行底边 */
			$(".hqnews li").last().css("border","0px");

			/* 获得焦点清空输入框 */
			$("#nsortName").inputFocus();
			$("#steelName").inputFocus();
			$("#csortName").inputFocus();
			
			//左侧悬挂行情跟随页面跑动
//			$(window).on("scroll",function(){
//	          var navH = $("#quote").offset().top;
//	          var scroH = $(document).scrollTop();
//	          if(scroH>="200" && scroH<="1190"){
//	             $("#quote").css({"position":"fixed", "top" :0});        
//	          }else{
//	          	 $("#quote").css({"position":"static"});
//	          }
//	        });
			//点击表单清空文本
			index.xqEmptyFormText($("#focastextarea"));
			
			/* 点击清空按钮清空表单内容 */ 
			$(".focas-input-empty").click(function(){
				$(this).closest(".focas-item-text").find("input").val("");
			})
			
	        //首屏切换
	        $(".menuul li").each(function(i){
	        	$(this).click(function(){
	        		timeoutFlag = false;
	        		$ = require("jquery");
	        		var num = i+1;
	        		$(".menuul li").removeClass("redfont");
	        		$(".menuslide").hide();
	        		$(".err-msg-span").hide().html("");
	        		//$(".focaserr-msg-span").hide().html("");
	        		$(this).addClass("redfont");
	        		$(".menu-content-mark").show();
	        		$(".menu-content").show();
	        		$(".bannermark").show();
	        		$("#banner0"+num).show();
	        		if(num == "3"){
	        			$("#banner03 .smsCodeNumber").attr("id","smsCodeNumber");
	        			$("#banner03 .userPhonetellogin").attr("id","userPhonetellogin");
	        			$("#banner05 .smsCodeNumber").attr("id","");
	        			$("#banner05 .userPhonetellogin").attr("id","");
	        			$("#userPhonetellogin").inputFocus();
	        			$("#smsCodeNumber").inputFocus();
	        		}else if(num =="5"){
	        			$("#banner05 .smsCodeNumber").attr("id","smsCodeNumber");
	        			$("#banner05 .userPhonetellogin").attr("id","userPhonetellogin");
	        			$("#banner03 .smsCodeNumber").attr("id","");
	        			$("#banner03 .userPhonetellogin").attr("id","");
	        			$("#userPhonetellogin").inputFocus();
	        			$("#smsCodeNumber").inputFocus();
	        		}
	        	})
	        })

	        
	        //初始化热门资源上部轮播图
	        var obj = {
				main:"#banner",
				child:"#banner_id",
				width:"998",
				height:"148",
				url:cache.base_url+"/common/getad",
				status:0
			};
			index.bannerRollingData(obj,"ad");
			
			//初始化会员活动轮播图
			var vipobj = {
				main:"#vipbanner",
				child:"#vipbanner_id",
				width:"622",
				height:"193",
				url:cache.base_url+"/common/getactivities",
				status:0
			};
			index.bannerRollingData(vipobj,"vip");
	        
			//焦点大图鼠标移除，关闭所有弹层
			$(".bannerbox").mouseleave(function(){
				timeoutFlag = true;
				setTimeout(function(){
					if(timeoutFlag){
						$(".menu-content-mark").hide();
			        	$(".menu-content").hide();
			        	$(".bannermark").hide();
					}
				},1000)
				
			});
			
			//初始化行情中心
			index.loadTreasurerIndex();
			
			//初始化热门资源
			index.hotResourcesData();
			
			//点击上传按钮上传文件
			if($.support.style){
				$(document).on("change","#uploadFile1,#uploadFile2",function(){
					var filetypeTrue = index.fileChange($(this));
					var btnID = $(this).attr("id"),isSizeTrue = index.getFileSize($(this));
			        if(!filetypeTrue){
			            return false;
			        };
			        if(imgUrlstr.length >=5){
			            $(".focaserr-msg-span").show().html("").html("最多上传5个文件");
			            return false;
			        };
		            if(!isSizeTrue){
                        $(".focaserr-msg-span").show().html("").html("文件超过2M");
                        return false;
                    };
			        $(".focaserr-msg-span").hide();
			        if(btnID == "uploadFile1"){ //判断file
				        $("#uploadFile2").attr("name","");
				        $("#uploadFile1").attr("name","uploadFile");        
				        index.uploadFile("uploadFile1");
			        }else{
				        $("#uploadFile1").attr("name","");
				        $("#uploadFile2").attr("name","uploadFile");
				        index.uploadFile("uploadFile2");
			        }
			        $(".markbtn").hide();
				})
			}else{
				$("#uploadFile1,#uploadFile2").bind("change",function(){
					var filetypeTrue = index.fileChange($(this));
					var btnID = $(this).attr("id"),isSizeTrue = index.getFileSize($(this));
			        if(!filetypeTrue){
			            return false;
			        };
			        if(imgUrlstr.length >=5){
			            $(".focaserr-msg-span").show().html("").html("最多上传5个文件");
			            return false;
			        };
			        if(!isSizeTrue){
                        $(".focaserr-msg-span").show().html("").html("文件超过2M");
                        return false;
                    };
			        $(".focaserr-msg-span").hide();
			        if(btnID == "uploadFile1"){ //判断file
				        $("#uploadFile2").attr("name","");
				        $("#uploadFile1").attr("name","uploadFile");        
				        index.uploadFile("uploadFile1");
			        }else{
				        $("#uploadFile1").attr("name","");
				        $("#uploadFile2").attr("name","uploadFile");
				        index.uploadFile("uploadFile2");
			        }
			        $(".markbtn").hide();
				})
			};
				
			//删除需求列表
			 index.closeli();
			 
			//提交需求
			$(document).on('click', ".input_btn", function () {
				cache.targetBtn = $(this);
				var textareaVal = $("#focastextarea").val();
				if(textareaVal == FORMTEXT){
		            $("#focastextarea").val("")
		        }
		        var isTrue = index.checkPhone($("#userPhonetellogin"));
	        	if(!isTrue){
	        		return;
	        	}
	        	var rtn = cas.remoteValid($("#userPhonetellogin").val());
				if(rtn == "3012"){
	    	 		return ;
	    	 	}
	        	//if($("#telCheckLogin li").eq(1).is(":visible")){
	        	if($(this).closest("ul").children("li").eq(1).is(":visible")){
	        		index.userLogin();
	        	}else{
	        		index.submitdata(); //提交需求
	        	}
	        	
	        });

	        //验证手机
	        $(document).on("change","#userPhonetellogin",function(){
	        	var isTrue = index.checkPhone($(this));
	        	if(!isTrue){
	        		return;
	        	}
			});
	        
	        //发送验证码
	        $(document).on('click', ".sendsms", function () {
	        	var $ = require("jquery"), phone =$(this).closest("ul").find("input[name='phone']"), 
	            v = index.checkPhone(phone);
	            var temp = ($(this).attr("data-attr") == "timeLong" ? timeLong : timeLong1);
	            if (!v || (temp > 0 && temp != 60) || $(this).hasClass("not-allowed")) {
	                return;
	            }
	            var el = $(this).closest("ul").find(".err-msg-span");
	            $.post(cache.base_url + "/api/code/send/registerorlogin", {phone: phone.val()}, function (response, status) {
	            	el.show().html("").html(constant.getCodeMsg(response.code));

	            });
	            index.countDown($(this),($(this).attr("data-attr") == "timeLong" ? timeLong : timeLong1));
	            $(this).attr("disabled", "disabled").addClass("not-allowed");
	        });
	        
	        $(".qqlink").bind("click",function(){
	        	util.setqq();
	        });
	        index.getAD();
		},
		getAD:function(){
			util.ajax({
				config:{
					url:"/common/getfloatad",//getfloatad
					dataType: "json",
					type:"GET",
					success:function(response, status){
						if(response.status == 0){
							if(response.data !=null && response.data.length >0){
								var data = response.data;
								$("body").append('<div class="d-mask-ad"  style="display: none;"></div><div class="alert-dialog"><div class="closeLabel"></div><a target="_blank"><img id="adImg" /></a></div>');
								$("#adImg").attr("src",data.image).load(function(){
									var imgWTH = $(this).width();
									$(".alert-dialog").css("left",($(window).width()-imgWTH)/2).fadeTo("fast", 1);
									$(".d-mask-ad").show();
								})
								.closest("a").attr("href",data.url).bind("click",function(){
									$(".closeLabel").click()
								});
								
								$(".closeLabel").bind("click",function(){
							    	$(".d-mask-ad").remove();
							    	$(".alert-dialog").remove();
							    })
								setTimeout(function(){
									$(".closeLabel").click();
								},5000)
							}
						}
						
					}
				}
			})
			
		},
		//检查文件大小
        getFileSize:function(eleId) {
            try {
                var size = 0;
                if ($.support.style) {//非IE
                    size = eleId[0].files[0].size;//byte
                    size = size / 1024;//kb
                    size = size / 1024;//mb
                } else {//IE
                    var fileMgr = new ActiveXObject("Scripting.FileSystemObject");
                    var filePath = eleId[0].value;
                    var fileObj = fileMgr.getFile(filePath);
                    size = fileObj.size; //byte
                    size = size / 1024;//kb
                    size = size / 1024;//mb
                }
                if(size >2){
                    return false;
                }else{
                    return true;
                }
            } catch (e) {
                return true;
            } 
        },
		//验证手机格式与长度
		checkPhone:function(elmid){
			var val = elmid.val();
			if(val == ""){
				$(".err-msg-span").show().html("").html("手机号码不能为空");
				elmid.focus();
				return false;
			}else if(!(/^1[3|4|5|7|8][0-9]\d{8}$/.test(val))) {
				$(".err-msg-span").show().html("").html("手机号码格式不正确");
				elmid.focus();
				return false;
			}
			$(".err-msg-span").show().html("");
			return true;
		},

		//上传采购需求
		 uploadFile:function(elmid){
            $ = require("jquery1.9");
            $.ajaxFileUpload({
                url: Context.PATH+"/common/uploadfile",
                secureuri:false,
                dataType: "json",
                fileElementId:elmid,
                success: function(data){
                    
	                if(data.code!="6000"){
		            	$(".focaserr-msg-span").show().html("").html(constant.getCodeMsg(data.code));
		                if(imgUrlstr.length <1){
		        			$(".filelist").hide();
						}
		                return false;
		            }
		            $(".focaserr-msg-span").hide();
                    var fileUrlarr = data.data,
                        fileUrlstrs= new Array(),
                        imgtype = "",
                        filename = "",
                        icon = "";
                        fileUrlstrs=fileUrlarr.replace(/\\/g,"/").split("/"); 
                        imgtype =fileUrlstrs[fileUrlstrs.length-1].split(".");
                        filename = fileUrlstrs[fileUrlstrs.length-1];
                    if(imgtype[1] == "png" || imgtype[1] == "jpg" || imgtype[1] == "gif" || imgtype[1] == "bmp"){
                        icon = "image-icon";
                    }else{
                        icon = "file-icon";
                    }

                    //文件列表
                    $("#file_upload-queue1").append('<div class="uploadify-queue-item1" data-string="'+fileUrlarr+'"><span class='+icon+'></span><span class="filenametxt">'+filename+'</span><em></em></div>')
                    //隐藏表单赋值
                    imgUrlstr.push(data.data);
                    $("#fileUrl1").val(imgUrlstr);

                }
            });

        },
        
        //删除上传文件条目    
	    closeli:function(){
	        $(document).on('click', ".uploadify-queue-item1 em", function() {
	           var item = $(this).closest(".uploadify-queue-item1");
			   item.remove();
			   if(imgUrlstr.length <=1){
			        $(".markbtn").show();
			   }
	           var replaceText = item.attr("data-string");
	           var fileUrlstring = $("#fileUrl1").val();
	           $(".a-upload input").val("");
	           var arry = util.remove(imgUrlstr,replaceText);
	           $("#fileUrl1").val(arry.join(','));
	           $(".focaserr-msg-span").show().html("")
	        })
	    },
        
        //验证上传文件是否合法
	     fileChange:function(target) {
	        var filepath=target.val(),
	            extStart=filepath.lastIndexOf("."),
	            ext=filepath.substring(extStart,filepath.length),
	            filetypes =[".jpg",".png",".gif",".txt",".jped",".bmp",".wps",".doc",".ppt",".xls",".docx",".xlsx",".et"],
	            isNext = false;
	        for(var i =0; i<filetypes.length; i++){
	            if(filetypes[i] == ext){
	                isNext = true;
	            }
	        }
	        if(!isNext){
	             $(".focaserr-msg-span").show().html("文件格式有误");
	             return false;
	        }
	        return true;
	    },
	    
	   //需求提交表单
	   submitdata : function(getreload){
	   		var isreload = getreload ==undefined || getreload ==null || getreload =="" ? 0 : getreload;
		    util.ajax({
				config:{
					url:"/api/requirement/submit",
					data:{ request:$("#focastextarea").val(),fileUrl:$("#fileUrl1").val()},
					dataType: "json",
					beforeSend:function(response){
	    				$(".input_btn").attr("disabled", "disabled");
					},
					complete:function(response, textStatus){
					    $(".input_btn").removeAttr("disabled");
					},
					success:function(data, status){
						
                        if(data.code=="7000"){
			                gwfinddialog.successDialog(isreload, data.data, data.status);
			                var phonev = $("#userPhonetellogin").val();
			                $("#file_upload-queue1").html("");
			                $("#focastextarea").val(FORMTEXT).css("color","#999");
			                $("#fileUrl1").val("");
			                $(".markbtn").show();
							imgUrlstr = [];
			                $("#smsCodeNumber").val("");
			                $(".userPhonetellogin").attr("readonly",true);
			                $(".userPhonetellogin").val(phonev);
			                $(".smsCodeNumber").closest("li").css("display","none");
			                $(".err-msg-span").hide().html("");
			            }
                        else if(data.code == "0001"){
			            	$(".err-msg-span").hide();
			            	$("#telCheckLogin li").eq(1).show();
			            	//$(".err-msg-span").show().html("验证码错误");
			            	//用户登录
			            	//index.userLogin();
			            }
                        else{
			                $(".err-msg-span").show().html("").html(constant.getCodeMsg(data.code));
			                return false;
			            }
					}
				}
			})
	    },
	    //短信验证码发送
	    countDown:function(btn,timeLong){
	        var intervalEvent = setInterval(function(){
	            if(timeLong>=0){
	                btn.text(timeLong--+"秒");
	            }else{
	                btn.text ("重新获取");
	                timeLong = 60;
	                btn.removeClass("not-allowed");
	                btn.removeAttr("disabled");
	                clearInterval(intervalEvent);
	            }
	
	        },1000);
	    },
	    //验证用户是否是登录状态
	    userLogin:function(){
    	var $ = require("jquery"),v = $.verifyElement($("#userPhonetellogin")[0], 3) && $.verifyElement($("#smsCodeNumber")[0], 3),
            phone = $("#userPhonetellogin").val(),
            code = $("#smsCodeNumber").val();
            if (!v) {
                return;
            }
			var rtn = cas.remoteValid($("#userPhonetellogin").val());
			if(rtn == "3012"){
    	 		return ;
    	 	}else if(rtn == "1004"){
    	 		var config = {
					url : "/api/user/registnopwd",
					data:{"mobile":phone,"code":code},
					success:function(data){
						index.checkUserRegCallback(data,2);
					}
				}
				util.ajax({config:config});
    	 		return ;
    	 	}
            $(".cart-login-submit").prop("disabled","disabled").addClass("not-allowed");
            cas.setCallback(function(){
            	index.submitdata(1);
            });
			cas.casLogin(phone,code);
//	       	var config = {
//				url : "/api/user/phone/check",
//				data:{"phone":phone},
//				success:function(data){
//					index.checkUserCallback(data);
//				}
//			}
//			util.ajax({config:config});
	    },
   
		//轮播图插件
		banner:function(obj){	
			var bn_id = 0,bn_id2= 1,speed33=3000,qhjg = 1,MyMar33,obj,ulwidth;
			$(obj.main+" .d1").hide();
			$(obj.main+" .d1").eq(0).fadeIn("slow");
			if($(obj.main+" .d1").length>1)
			{
				$(obj.child+" li").eq(0).addClass("nuw");
				function Marquee33(){
					bn_id2 = bn_id+1;
					if(bn_id2>$(obj.main+" .d1").length-1)
					{
						bn_id2 = 0;
					}
					ulwidth = $(obj.child+" ul").width();
					$(obj.child+" ul").css("margin-left",-ulwidth/2);
					$(obj.main+" .d1").eq(bn_id).css("z-index","2");
					$(obj.main+" .d1").eq(bn_id2).css("z-index","1");
					$(obj.main+" .d1").eq(bn_id2).show();
					$(obj.main+" .d1").eq(bn_id).fadeOut("slow");
					$(obj.child+" li").removeClass("nuw");
					$(obj.child+" li").eq(bn_id2).addClass("nuw");
					bn_id=bn_id2;
				};
			
				MyMar33=setInterval(Marquee33,speed33);
				
				$(obj.child+" li").mouseover(function(){
					var bn_id3 = $(obj.child+" li").index(this);
					if(bn_id3!=bn_id&&qhjg==1)
					{
						qhjg = 0;
						$(obj.main+" .d1").eq(bn_id).css("z-index","2");
						$(obj.main+" .d1").eq(bn_id3).css("z-index","1");
						$(obj.main+" .d1").eq(bn_id3).show();
						$(obj.main+" .d1").eq(bn_id).fadeOut("slow",function(){qhjg = 1;});
						$(obj.child+" li").removeClass("nuw");
						$(obj.child+" li").eq(bn_id3).addClass("nuw");
						bn_id=bn_id3;
					}
				})
				$(obj.child).hover(
					function(){
						clearInterval(MyMar33);
					}
					,
					function(){
						MyMar33=setInterval(Marquee33,speed33);
					}
				)	
			}
			else
			{
				$(obj.child).hide();
			}
		},
		//需求表单，指定默认内容
		xqEmptyFormText:function(elm){
			//兼容IEtextarea控制长度
			elm.css("color","#999");
		    elm.keyup(function(){
			    var area=$(this);
			    var max=parseInt(area.attr("maxlength"),10);
			    if(max>0){
				    if(area.val().length>max){ 
				    	area.val(area.val().substr(0,max));
				    }
			    }
		    });
			elm.focus(function(){
		        if($(this).val() != FORMTEXT){
		            return true;
		            $(this).css("color","#FFF");
		        }else{
		            $(this).val("");
		            $(this).css("color","#999");
		        }
		    });
		    elm.blur(function(){
		    	//复制的字符处理问题
		    	var area=$(this);
			    var max=parseInt(area.attr("maxlength"),10);
			    if(max>0){
				    if(area.val().length>max){
				    	area.val(area.val().substr(0,max));
				    }
			    }
		        if($(this).val() == ""){
		            $(this).val(FORMTEXT);
		            $(this).css("color","#999");
		        }else{
		            $(this).css("color","#FFF");
		            return true;
		
		        }
		    });

		},
		//轮播广告数据调用
		bannerRollingData:function(obj,type){
			util.esbajax({
				config:{
					url:obj.url,
					dataType: "json",
					type:"GET",
					success:function(response, status){
						if(response.status == obj.status){
							index.getbannerDataCallback(response.data,obj,type);
						}else if(type == "ad"){
							$(".scrollbanner").addClass("none");
						}else if(type == "vip"){
							$(".viphd").addClass("none");
						}
					}
				}
			})
		},
		
		//热门资源数据调用
		hotResourcesData:function(){
			//获得当前城市名称
				util.ajax({
				config:{
					url:"/api/search/getHotResourceList",
					dataType: "json",
					beforeSend:function(response){
	    				index.publicLodding(".hot_res_list"); 
					},
					complete:function(response, textStatus){
					    index.emptyLodding(".hot_res_list");
					},
					success:function(response, status){
						if(response.code == "11001"){
							index.getHotResourcesDataCallback(response.data);
						} else{
							$(".hot_resource").addClass("none");
						}
					}
				}
			})
		},
		
		// 加载中弹层
		publicLodding:function(elm){
			var html = "";
			html+='<p class="publicmark"><img src="'+Context.PATH+'/css/default/images/loading.gif"></p>';
			$(elm).html(html);
		},
		emptyLodding:function(elm){
			$(elm+" .publicmark").remove();
		},
		
		// 掌柜指数数据调用
		loadTreasurerIndex : function() {
		    util.esbajax({
				config:{
					dataType: "json",
					type:"GET",
					data:{city:0,pagesize:6},
					url:cache.base_url+"/common/getpriceindexes",
					success:function(response, status){
						if(response.status == 0){
							index.getLoadTreasurerCallback(response.data);
						}
					}
				}
			})
		},
		// 查看掌柜指数日期 月/日 星期
		showTime:function(){
		  //var dateArr = date.replace(/\-/g,"/");
		  var show_day=new Array('周日','周一','周二','周三','周四','周五','周六');
		  var time=new Date();
		  var year=time.getYear(); 
		  var month=time.getMonth(); 
		  var date=time.getDate(); 
		  var day=time.getDay();

		  month=month+1;
		  month<10?month='0'+month:month; 
		  var now_time= month+'/'+date+'/'+' '+show_day[day];
		  return now_time; 
		},
		//广告轮播回调
		getbannerDataCallback:function(data,obj,type){
			if((!data || data.length<=0) && type=="ad"){
				$(".scrollbanner").addClass("none");
			}else if((!data || data.length<=0) && type=="vip"){
				$(".viphd").addClass("none");
			}else{
				if(type=="ad"){
					$(".scrollbanner").removeClass("none");
				}else if(type=="vip"){
					$(".viphd").removeClass("none");
				}
				var bannerlist = "",bannerbtn = "";
				$.each(data,function(n, value){
				var url = value.url =="" || value.url ==undefined || value.url == null || value.url == "http://" ? "javascript:void(0)" :value.url;

					bannerlist += '<a href="'+url+'" target="_blank" class="d1"><img width="'+obj.width+'" height="'+obj.height+'" src="'+value.image+'" alt="'+value.title+'"></a>';
					bannerbtn +='<li></li>';
				});

			 	$(obj.main+" .bannerimgbox").html(bannerlist);
				$(obj.main+" .d2 ul").html(bannerbtn);
				index.banner(obj);
			}
		},
		//掌柜指数回调
		getLoadTreasurerCallback:function(data){

			var html = "",iconclass = "",icontext = "",fontcolor = "",updownPrice = "",price = "";
			$("#lastTime").html(index.showTime());//掌柜指数显示日期
			$.each(data,function(n, value){
				if(n == "0"){
					$("#areaCity").html(value.CityName);
				};
				if (value.Price == 0) {
                  price = "待更新";
                }else {
                  price = value.Price;
                };
				if(value.HighLows == "0"){
					iconclass = "kong";
					icontext = "-";
					fontcolor = "gray";
					updownPrice = value.HighLows;
				}else if(value.HighLows > "0"){
					fontcolor = "red";
					iconclass = "up";
					icontext = "";
					updownPrice = value.HighLows;
				}else{
					fontcolor = "green";
					iconclass = "down";
					icontext = "";
					updownPrice = (value.HighLows).toString().replace("-", '');
				};
				html += '<li>'
					//+'<a target="_blank" href="">'
					+'<dl class="f-fl">'
					+'<dd class="f-clrfix">'
					+'<span class="z-tit f-fl" key="nsortName" title="'+value.NsortName+'">'+value.NsortName+'</span>'
					+'<span class="price f-fr '+fontcolor+'">'+price+'</span>'
					+'</dd>'
					+'<dd class="f-clrfix">'
					+'<span class="s-gray1 f-fl" key="materials">'+value.Materials+'</span>'
					+'<span key="yieldly" class="s-gray2 s-textcenter f-fl" title="'+value.Yieldly+'">'+value.Yieldly+'</span>'
					+'<span key="specs" class="s-gray3 '+fontcolor+' s-textright f-fr">'+updownPrice+'</span>'  
					+'</dd>'
					+'</dl>'
					+'<p class="highlows f-fr"><label><em class="'+iconclass+'">'+icontext+'</em></label></p>'
					//+'</a>'
					+'</li>';
				$(".m-quote ul").html(html);
                $("#mmain").addClass("f-fr");
                $("#quote").show();
//              var docuHeight = $(document).scrollTop();
//              if(docuHeight>=228){
//              	 $("#quote").css({"position":"fixed", "top" :0});
//              }
//              // 屏幕分辨率小于等于1024	        
		        var windowWidth = $(window).width();
		        if(windowWidth <="1280"){
		        	$("#quote").hide();
		        	$("#mmain").removeClass('f-fr');
		        	//$("#idxMain").css("width","1000px");
		        }
           });
		},
		//热门资源回调
		getHotResourcesDataCallback:function(data){
			if(!data || data.length<=0){
				$(".hot_resource").addClass("none");
			}else{
				$(".hot_resource").removeClass("none");
				var html="",footer = "",elmfloat = "",fen ='<p class="hot_fen"></p>';
				$.each(data,function(n, value){
					if((n+1)%2==0){
						elmfloat = "f-fr";
					}else{
						elmfloat = "f-fl";
					}
					var url=cache.base_url+"/market/categoryuuid_"+value.categoryUuid+"_categoryname_"+encodeURIComponent(encodeURIComponent(value.categoryName))+"_material_"+value.materialUuid+"_factory_"+value.factoryId ;
					if(value.spec1){
						url +="_spec1_"+value.spec1;
					}
					if(value.spec2){
						url +="_spec2_"+value.spec2;
					}
					if(value.spec3){
						url +="_spec3_"+value.spec3;
					}
					url+="_city_"+value.cityId;

					html +='<a href="'+url+'" target="_blank"><table class="'+elmfloat+'"><tr>'
						+'<td class="sort">'+value.categoryName+'</td>'
						+'<td class="store">'+value.materialName+'</td>'
						+'<td class="company">'+value.factoryName+'</td>'
						+'<td class="nsort">'+value.spec1+(value.spec2?'*'+value.spec2+(value.spec3?'*'+value.spec3:""):"")+'</td>'
						+'<td class="city">'+value.cityName+'</td>';
					if(value.expired){
						html +='<td class="price">议价</td>';
					}else{
						html +='<td class="price">￥'+value.price+'</td>';
					}
					html +='</tr></table></a>';
					$(".hot_res_list").html(html);
				})
				$(".hot_res_list").append(fen);
			}
		},
		//验证用户是否存在回调   	
    	checkUserCallback:function(data){
    	 	var phone = $("#userPhonetellogin").val(),
            code = $("#smsCodeNumber").val();
			if(data.code == "1002"){ //已存在用户直接登录
				obj = {
					username : phone,
					userpassword : code,
					type : 3,
					logintab : 2,
					func:function(){index.submitdata(1)}
				}
				caslogin.casLogin(obj);
			}else if(data.code == "1004"){ //未注册用户
				var config = {
					url : "/api/user/registnopwd",
					data:{"mobile":phone,"code":code},
					success:function(data){
						index.checkUserRegCallback(data,2);
					}
				}
				util.ajax({config:config});
			}else if(data.code == "3012"){ //用户锁定
				index.checkUserRegCallback(data,2);
			}
		},
		checkUserRegCallback:function(data,style){
			var phone = $("#userPhonetellogin").val(),code = $("#smsCodeNumber").val(),obj,loginobj,
				style = style == null ? 1 : style,
				logintab = logintab ==null ? 1:logintab;

			if(data.code != "3005"){
				obj = {
					type : style,
				}
				tips.checkFormTips(obj,constant.getCodeMsg(data.code));
				return false;
			}
			cas.setCallback(function(){
            	index.submitdata(1);
            });
			cas.casLogin(phone,code);
//			loginobj = {
//				username :phone,
//				userpassword : data.data,
//				type : 3,
//				logintab : 1,
//				func:function(){index.submitdata(1)}
//			}
//			caslogin.casLogin(loginobj);
		}

	}
	module.exports = {
		init : index.init
	}	
})
