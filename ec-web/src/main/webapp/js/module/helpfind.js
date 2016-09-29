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
		timeLong1=60,
		timeoutFlag = false;
	var helpfind = {
		
		init:function(){
			/* 获得焦点清空输入框 */
			$("#nsortName").inputFocus();
			$("#steelName").inputFocus();
			$("#csortName").inputFocus();

			//点击表单清空文本
			helpfind.xqEmptyFormText($("#focastextarea"));
			
			/* 点击清空按钮清空表单内容 */ 
			$(".focas-input-empty").click(function(){
				$(this).closest(".focas-item-text").find("input").val("");
			})
			
	        //首屏切换
	        $(".menuul li").each(function(i){
	        	var $ = require("jquery");
	        	$(this).click(function(){
	        		timeoutFlag = false;
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
	        			$("#banner05 .focaserr-msg-span").hide().html("");
	        			$("#userPhonetellogin").inputFocus();
	        			$("#smsCodeNumber").inputFocus();
	        		}else if(num =="5"){
	        			$("#banner05 .smsCodeNumber").attr("id","smsCodeNumber");
	        			$("#banner05 .userPhonetellogin").attr("id","userPhonetellogin");
	        			$("#banner03 .smsCodeNumber").attr("id","");
	        			$("#banner03 .userPhonetellogin").attr("id","");
	        			$("#banner03 .focaserr-msg-span").hide().html("");
	        			$("#userPhonetellogin").inputFocus();
	        			$("#smsCodeNumber").inputFocus();
	        		}
	        	})
	        })
	        	        
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
						
			//点击上传按钮上传文件
			if($.support.style){
				$(document).on("change","#uploadFile1,#uploadFile2",function(){
					var filetypeTrue = helpfind.fileChange($(this));
					var btnID = $(this).attr("id"),isSizeTrue = helpfind.getFileSize($(this));
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
				        helpfind.uploadFile("uploadFile1");
			        }else{
				        $("#uploadFile1").attr("name","");
				        $("#uploadFile2").attr("name","uploadFile");
				        helpfind.uploadFile("uploadFile2");
			        }
			        $(".markbtn").hide();
				})
			}else{
				$("#uploadFile1,#uploadFile2").bind("change",function(){
					var filetypeTrue = helpfind.fileChange($(this));
					var btnID = $(this).attr("id"),isSizeTrue = helpfind.getFileSize($(this));
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
				        helpfind.uploadFile("uploadFile1");
			        }else{
				        $("#uploadFile1").attr("name","");
				        $("#uploadFile2").attr("name","uploadFile");
				        helpfind.uploadFile("uploadFile2");
			        }
			        $(".markbtn").hide();
				})
			}

			//删除需求列表
			 helpfind.closeli();
			 
			//提交需求
			$(document).on('click', ".input_btn", function () {
				cache.targetBtn = ".input_btn";
				var textareaVal = $("#focastextarea").val();
				if(textareaVal == FORMTEXT){
		            $("#focastextarea").val("")
		        }
		        var isTrue = helpfind.checkPhone($("#userPhonetellogin")) ;
	        	if(!isTrue){
	        		return;
	        	}
	        	
	        	var rtn = cas.remoteValid($("#userPhonetellogin").val());
				if(rtn == "3012"){
	    	 		return ;
	    	 	}
	        	if($(this).closest("ul").children("li").eq(1).is(":visible")){
	        		helpfind.userLogin();
	        	}else{
	        		helpfind.submitdata(); //提交需求
	        	}
	        	//helpfind.submitdata(); //提交需求
	        });
	        
	        //关闭灰色弹层
	        $(document).on("click",".markclose",function(){
	        	$(".markapcity").slideUp();
	        	$(".gwzginfo").slideUp();
	        	$(".marktop").slideUp();
	        })
	        //钢为掌柜弹出
	        $(document).on("click","#gwzgjs",function(e){
	        	util.stopF(e); //阻止冒泡
	        	$(".markapcity").hide();
	        	$(".zgmarkbg").slideDown();
	        	$(".yp-introduce").hide();
	        	$(".zg-introduce").slideDown().css("top","");
	        	$(".marktop").slideDown().css("top","");
	        })
	        //银票介绍1弹出
	        $(document).on("click",".infotext",function(e){
 	        	var even = $(e.target) || $(e.srcElement);
 	        	util.stopF(e); //阻止冒泡
	        	if($(even).hasClass("ypjs")){
		        	$(".markapcity").hide();
		        	$(".ypmarkbg").slideDown().css("top","");
		        	$(".zg-introduce").hide();
		        	$(".yp-introduce").slideDown().css("top","");
		        	$(".marktop").slideDown().css("top","");
	        	}else{
		        	$(".markapcity").hide();
		        	$(".ypmarkbg").slideDown().css("top","859px");
		        	$(".zg-introduce").hide();
		        	$(".yp-introduce").slideDown().css("top","859px");
		        	$(".marktop").slideDown().css("top","859px");
		        	$(".gwzginfo").css("top","909px");
	        	}
	         })
	        
	        //点击遮罩层以外的任何区域关闭遮罩层
        	$(document).on("click",function(e){
	        	var even =  $(e.target) || $(e.srcElement); //兼容IE的even
	        	if(!even.parents("#markd").hasClass('#markd')){
		        	$(".markapcity").slideUp();
		        	$(".gwzginfo").slideUp();
		        	$(".marktop").slideUp();
	        	}
	        })

	        //验证手机
	        $(document).on("change","#userPhonetellogin",function(){
	        	var isTrue = helpfind.checkPhone($(this));
	        	if(!isTrue){
	        		return;
	        	}
			})
	                
	        //发送验证码
	        $(document).on('click', ".sendsms", function () {
	           var $ = require("jquery"), phone =$(this).closest("ul").find("input[name='phone']"), 
	           v = helpfind.checkPhone(phone);
	           var temp = ($(this).attr("data-attr") == "timeLong" ? timeLong : timeLong1)
	            if (!v || (temp > 0 && temp != 60) || $(this).hasClass("not-allowed")) {
	                return;
	            }
	            var el = $(this).closest("ul").find(".err-msg-span");
	            $.post(cache.base_url + "/api/code/send/registerorlogin", {phone: phone.val()}, function (response, status) {
	            	el.show().html(constant.getCodeMsg(response.code));
	
	            });
	            helpfind.countDown($(this),($(this).attr("data-attr") == "timeLong" ? timeLong : timeLong1));
	            $(this).attr("disabled", "disabled").addClass("not-allowed");
	        });
	        
	        //qq客服
	        $(".qqlink").bind("click",function(){
	        	util.setqq();
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
		        			$(".markbtn").show();
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
						if(data.code == "7000"){
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
			            }else if (data.code == "0001"){
			            	$(".err-msg-span").hide();
			            	$("#telCheckLogin li").eq(1).show();
			               // helpfind.userLogin();
			            }
			            else{
			                $(".err-msg-span").show().html("").html(constant.getCodeMsg(data.code));
			                $("#focastextarea").val(FORMTEXT).css("color","#999");
			                return false;
			            }
					}
				}
			})
	    },
	    //短信验证码发送
	    countDown:function(btn,timeLong){
	        //var btn = $(".cart-login-valid-btn");
	        var intervalEvent = setInterval(function(){
	            if(timeLong>=0){
	                btn.text(timeLong--+"秒");
	            }else{
	                btn.text ("重新获取");
	                timeLong = 60;
	                btn.removeClass("not-allowed");
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
						helpfind.checkUserRegCallback(data,2);
					}
				}
				util.ajax({config:config});
    	 		return ;
    	 	}
            
            $(".input_btn").prop("disabled","disabled").addClass("not-allowed");
            cas.setCallback(function(){
            	helpfind.submitdata(1);
            });
			cas.casLogin(phone,code);
            
//	       	var config = {
//				url : "/api/user/phone/check",
//				data:{"phone":phone},
//				success:function(data){
//					helpfind.checkUserCallback(data);
//				}
//			}
//			util.ajax({config:config});
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
					func:function(){helpfind.submitdata(1)}
				}
				caslogin.casLogin(obj);
			}else if(data.code == "1004"){ //未注册用户
				var config = {
					url : "/api/user/registnopwd",
					data:{"mobile":phone,"code":code},
					success:function(data){
						helpfind.checkUserRegCallback(data,2);
					}
				}
				util.ajax({config:config});
			}else if(data.code == "3012"){ //用户锁定
				helpfind.checkUserRegCallback(data,2);
			}
		},
		checkUserRegCallback:function(data,style){
			var phone = $("#userPhonetellogin").val(),code = $("#smsCodeNumber").val(),obj,
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
            	helpfind.submitdata(1);
            });
			cas.casLogin(phone,code);
//			loginobj = {
//				username :phone,
//				userpassword : data.data,
//				type : 3,
//				logintab : 1,
//				func:function(){helpfind.submitdata(1)}
//			}
//			caslogin.casLogin(loginobj);

		}

	}
	module.exports = {
		init : helpfind.init
	}	
})
