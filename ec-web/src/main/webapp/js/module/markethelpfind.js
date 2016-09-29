define(function(require,exports,module){
	"use strict";
	var $ = require("jquery"),
	    util = require("util"),
        cache = require("cache"),
        constant = require("constant"),
        tips = require("checkformtips"),
        prcsteel = require("prcsteel"),
        cas = require("cas"),
        gwfinddialog = require("gwfind.dialog");
        window.cas = cas;
	var TEXTAREA = "输入想要查找 品名",
		timeLong=60;
	var markethelpfind = {
		
		init:function(){
			//提交需求
			$(document).on('click', "#helpfind", function () {
				var textareaVal = $("#requirement_textarea").val();
				if(textareaVal == TEXTAREA){
		            $("#requirement_textarea").val("");
		        }
	        	markethelpfind.submitdata(); //提交需求
	        });
	        
	        //登陆或者注册用户
	        markethelpfind.setEvents();
	        
	        //点击表单清空文本
			markethelpfind.xqEmptyFormText($("#requirement_textarea"));
		},
		
		//需求表单，指定默认内容
		xqEmptyFormText:function(elm){
			//兼容IEtextarea控制长度
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
		        if($(this).val() != TEXTAREA){
		            return true;
		            $(this).css("color","#999");
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
		            $(this).val(TEXTAREA);
		            $(this).css("color","#999");
		        }else{
		            $(this).css("color","#999");
		            return true;
		
		        }
		    });

		},
		//需求提交表单
	   submitdata : function(getreload){
	   	var isreload = getreload ==undefined || getreload ==null || getreload =="" ? 0 : getreload;
		    util.ajax({
				config:{
					url:"/api/requirement/submit",
					data:{ request:$("#requirement_textarea").val()},
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
			                $(".marketerr-msg-span").hide();
			                $("#requirement_textarea").val("").val(TEXTAREA).css("color","#999");
			            }else if (data.code == "0001"){
			                markethelpfind.loginDialog();
			            }else{
			                $(".marketerr-msg-span").show().html("").html(constant.getCodeMsg(data.code));
			                $("#requirement_textarea").val(TEXTAREA).css("color","#999");
			                return false;
			            }
			            
					}
				}
			})
	    },
	   	countDown:function(){
	        var btn = $(".login-valid-btn-marketfind");
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
	    setEvents:function () {
	         $(document).on('click', ".cart-helpfind-login-submit", function () {
	            var v = $.verifyElement($("#userPhonetellogin")[0], 3) && $.verifyElement($("#smsCodeNumber")[0], 3),
	            phone = $("#userPhonetellogin").val(),
	            code = $("#smsCodeNumber").val();
	            if (!v) {
	                return;
	            }
	            var rtn = cas.remoteValid(phone);
	    	 	if(rtn == "3012"){
	    	 		return ;
	    	 	}else if(rtn == "1004"){
	    	 		var config = {
						url : "/api/user/registnopwd",
						data:{"mobile":phone,"code":code},
						success:function(data){
							markethelpfind.checkUserRegCallback(data);
						}
					}
					util.ajax({config:config});
	    	 		return ;
	    	 	}
	            $(".cart-helpfind-login-submit").prop("disabled","disabled").addClass("not-allowed");
	            cas.setCallback(function(){
	            	markethelpfind.submitdata(1);
	            });
				cas.casLogin(phone,code);
//		       	var config = {
//					url : "/api/user/phone/check",
//					data:{"phone":phone},
//					success:function(data){
//						markethelpfind.checkUserCallback(data);
//					}
//				}
//				util.ajax({config:config});
	        });
	        $(document).on('click', ".login-valid-btn-marketfind", function () {
	            var v = $.verifyElement($("#userPhonetellogin")[0], 3);
	            if (!v || (timeLong > 0 && timeLong != 60)) {
	                return;
	            }
	            $.post(cache.base_url + "/api/code/send/registerorlogin", {phone: $("#userPhonetellogin").val()}, function (response, status) {
                    //if (response.code == "2000") {
	                 //   $(".err-msg-span").show().html("").html("验证码发送成功");
                    //} else if (response.code == "2001") {
	                 //   $(".err-msg-span").show().html("").html("验证码发送失败");
                    //} else if (response.code == "2003") {
	                 //   $(".err-msg-span").show().html("").html("验证码发送太频繁");
                    //}else if (response.code == "3012") {
					//	$(".err-msg-span").show().html("").html("用户账号已被锁定!");
					//}
					$(".err-msg-span").show().html(constant.getCodeMsg(response.code));
	            });
	            markethelpfind.countDown();
	            $(this).attr("disabled", "disabled").addClass("not-allowed");
	        });
	    },
	    //验证用户是否存在   	
    	checkUserCallback:function(data){
    	 	var phone = $("#userPhonetellogin").val(),
	    	 	obj = {},
	            code = $("#smsCodeNumber").val();
			if(data.code == "1002"){ //已存在用户直接登录
				login(phone,code);
			}else if(data.code == "1004"){ //未注册用户
				var config = {
					url : "/api/user/registnopwd",
					data:{"mobile":phone,"code":code},
					success:function(data){
						markethelpfind.checkUserRegCallback(data);
					}
				}
				util.ajax({config:config});
			}else if(data.code == "3012"){ //用户锁定
				markethelpfind.checkUserRegCallback(data);
			}
		},
		loginDialog:function(){
	        var el ="";
	        el +='<div class="cart-login">';
	        el +='<div class="cart-login-head">';
	        el +='<span class="red-c">请填写手机号码并保持通信畅通，钢为掌柜会及时与您确认采购需求</span>';
	        el +='</div>';
	        el +='<div id="loginbox" class="loginbox login-form f-clrfix">';
	        el +='<form id="userRegister" method="post">';
	        el +='<ul id="telCheckLogin">';
	        el +='<li class="f-clrfix">';
	        el +='<div class="form-item">';
	        el +='<div class="border-style">';
	        el +='<div class="item-text">';
	        el +='<span class="icon login-iphone-icon"></span>';
	        el +='<div class="item-text-wrap">';
	        el +='<label class="ipt-label" style="color: rgb(153, 153, 153);">手机号码</label>';
	        el +='<input type="text" id="userPhonetellogin" name="phone_number" must="1" verify="mobile" class="input_txt ipt-text" maxlength="11">';
	        el +='</div>';
	        el +='<em class="input-empty"></em>';
	        el +='</div>';
	        el +='</div>';
	        el +='</div>';
	        el +='<p class="form-err"><em class="success-icon"></em><span></span></p>';
	        el +='</li>';
	        el +='<li class="f-clrfix" style="margin-bottom:0px">';
	        el +='<div class="form-item wd-150">';
	        el +='<div class="border-style">';
	        el +='<div class="item-text wd-150">';
	        el +='<span class="icon login-smscode-icon"></span>';
	        el +='<div class="item-text-wrap wd-90">';
	        el +='<label class="ipt-label" style="color: rgb(153, 153, 153);">验证码</label><input type="text" must="1" maxlength="4" verify="number" value="" name="code" class="ipt-text wd-90" id="smsCodeNumber">';
	        el +='</div>';
	        el +='<em class="input-empty"></em>';
	        el +='</div>';
	        el +='</div>';
	        el +='</div>';
	        el +='<span class="sms_btn login-valid-btn-marketfind">获取验证码</span>';
	        el +='<p class="form-err"><em class="error-icon"></em><span></span></p>';
	        el +='</li>';
	        el +='</ul>';
	        el +='</form>';
	        el +='</div>';
	        el +='<div style="height:20px;margin:3px auto;"><span class="red-c err-msg-span" ></span></div>';
	        el +='<div class="cart-login-bottom"><a class="cart-helpfind-login-submit">提交</a></div>';
	        el +='</div>';
	        util.getDialog(false, el);
	        
	        $("input[name='phone_number']").inputFocus();
	        $("#smsCodeNumber").inputFocus();
	        $(".input-empty").click(function(){
	            $(this).closest(".item-text").find("input").val("");
	        })
	        $("#userRegister").verifyForm(3,blur);
	        $("#gwfindform").verifyForm(3,blur);
	    },
		checkUserRegCallback:function(data){
			var phone = $("#userPhonetellogin").val(),obj,
				style = 2;
				if(data.code !="3005"){
					obj = {
						type : style
					}
					tips.checkFormTips(obj,constant.getCodeMsg(data.code));
					$(".cart-helpfind-login-submit").removeAttr("disabled").removeClass("not-allowed");;
					return false;
				}
				login(phone,data.data);
		}
	}
	function login(phone,pwd){
    	var config = {
        	type : "POST",
			url : "/api/passport/verify",
			data : {
                username:phone,
                password:pwd
            },
			dataType: "json",
            success : function(res, status, xhr) {
            	loginCallback(res)

            }
		}
		util.ajax({config:config});
    };
    function verify(url){
		$.ajax({
            type : "GET",
            url : url,
            success : function(text, textStatus, xhr) {
                fetchAccount();
               
            }
        });
    };
    function loginAfter(){
        var config = {
			url : "/api/user/login/after",
			asnyc:false,
            success : function(data) {
            	markethelpfind.submitdata(1);
            }
		}
		util.ajax({config:config});
    };
    function fetchAccount(){
    	var config = {
    		type : "GET",
			url : "/api/passport/verify",
            dataType: "text",
            success : function(res, status, xhr) {
               	 loginAfter();
            }
		}
		util.ajax({config:config});
    };
    function loginCallback(data,logintab,style){
    	var obj,style = 2;
    	if(data.code !="5002"){
			obj = {
				type : style
			}
			tips.checkFormTips(obj,constant.getCodeMsg(data.code =="5004" ? "2004": data.code));
			$(".cart-helpfind-login-submit").removeAttr("disabled").removeClass("not-allowed");
			return false;
		}
		verify(data.data);
    };
    
	module.exports = {
		init : markethelpfind.init
	}	

});
