define(function(require,exports,module){
    var $ = require("jquery"),
        tips = require("checkformtips"),
        util = require("util"),
        cache=require("cache"),
       	prcsteel=require("prcsteel"),
       	userCallback = require("usercallback"),
        sendsms = require("sendsms");
    var resetpassword = {
    	
    	init:function(){
    		/* 获得焦点清空输入框 */
			$("#userPhonetellogin").inputFocus();
		    $("#smsCodeNumber").inputFocus();
		    $("#userPwd").inputFocus();
		    $("#afterPwd").inputFocus();
		
		    /* 验证表单 */
		    $("#oldcheck").verifyForm(2,blur);
		    $("#newphone").verifyForm(2,blur);
		    
		    /* 点击清空按钮清空表单内容 */
		    $(".input-empty").click(function(){
		        $(this).closest(".item-text").find("input").val("");
		    })
			//判断密码是否为空
	        $("#userPwd").change(function(){
		        var rpwd = resetpassword.replacePasswoldNbsp($(this).val());
		        $(this).val(rpwd);
		        var thisval = $(this).val();
		        if(thisval.length < 6){
		            var obj = {
		                elm : $("#userPwd"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"用户密码不能少于6位");
		            return false;
		        }else{
		            var obj = {
		                elm : $("#userPwd"),
		                type : 1,
		                errortip :3
		            }
		            tips.checkFormTips(obj,"");
		        }
		    });
		    
		    $("#afterPwd").change(function(){
		        var rpwd = resetpassword.replacePasswoldNbsp($(this).val());
		        $(this).val(rpwd);
		    })
		    
		  	/* 确认密码 */
		    $("#Stepgo").click(function(){
		        var oldCode = $("#smsCodeNumber").val(),phone = $("#userPhonetellogin").val(),obj = {},isTrue = resetpassword.checkPhone($("#userPhonetellogin")) && resetpassword.checkverifyCode($("#smsCodeNumber"));
	        	if(!isTrue){
	        		return;
	        	}else if(oldCode == ""){
		            var obj = {
		                elm : $("#smsCodeNumber"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"验证码错误");
		            return false;
		        }
		        obj = {
		        	phone : phone,
		        	code : oldCode
		        };
		        resetpassword.checkOldCode(obj);
		        
		    })
		    
		    //qq客服
		    $(".qqlink").bind("click",function(){
		    	util.setqq();
		    })
		    
		   //点击绑定按钮校验表单是否为空，绑定短信发送
		   $("#bindNewPhone").click(function(){
		        var userPwd = $("#userPwd").val(),
		        	afterPwd = $("#afterPwd").val(),
		        	oldCode = $("#smsCodeNumber").val(),
		        	userphone = $("#userPhonetellogin").val();
		        config = {
		        	phone:userphone,
		        	pwd : userPwd,
		        	code : oldCode
		        };
		        if(userPwd == ""){
		            var obj = {
		                elm : $("#userPwd"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"用户密码不能为空");
		            $("#userPwd").focus();
		            return false;
		        }else if(userPwd.length < 6){
		            var obj = {
		                elm : $("#userPwd"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"用户密码不能少于6位");
		            $("#userPwd").focus();
		            return false;
		        }else if(afterPwd != userPwd){
		            var obj = {
		                elm : $("#afterPwd"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"两次密码不一致");
		            $("#afterPwd").focus();
		            return false;
		        }else{
		            var obj = {
		                elm : $("#afterPwd"),
		                type : 1,
		                errortip :2
		            }
		            tips.checkFormTips(obj,"");
					resetpassword.confirmEditPwd(config);//确定修改密码
		        }
		   })
		   
		/* 绑定发送短信事件和显示第一区块*/	
	    resetpassword.showstep(1);
	    resetpassword.phoneChange();

		   
    	},
    	checkverifyCode :function(elmid){
    		var val = elmid.val();
			if(val == ""){
				var obj = {
					elm : elmid,
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"验证码错误");
				elmid.focus();
				return false;
			}else if(!(/^\d{4}$/.test(val))) {
				var obj = {
					elm : elmid,
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"验证码错误");
				elmid.focus();
				return false;
			}
			var obj = {
				elm : elmid,
				type : 1,
				errortip :2
			}
			tips.checkFormTips(obj,"");
			return true;
    	},
    	//验证手机格式与长度
		checkPhone:function(elmid){
			var val = elmid.val();
			if(val == ""){
				var obj = {
					elm : elmid,
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"手机号码不能为空");
				elmid.focus();
				return false;
			}else if(!(/^1[3|4|5|7|8][0-9]\d{8}$/.test(val))) {
				var obj = {
					elm : elmid,
					type : 1,
					errortip :1
				}
				tips.checkFormTips(obj,"手机号码格式不正确");
				elmid.focus();
				return false;
			}
			var obj = {
				elm : elmid,
				type : 1,
				errortip :2
			}
			tips.checkFormTips(obj,"");
			return true;
		},
    	phoneChange:function(){
			var userphone = $("#userPhonetellogin");
			userphone.change(function(){
				var val = $(this).val(),isTrue = resetpassword.checkPhone(userphone);
	        	if(!isTrue){
	        		return;
	        	}
				resetpassword.loginCheck(val);
			});
		},
		
		//验证手机号码
		loginCheck:function(phone){
			var config = {
				url : "/api/user/phone/check",
				data:{"phone":phone},
				success:function(data){
					userCallback.resetCheckCallback(data,function(){sendsms.sendSms("/api/code/send/resetpassword",phone)});
				}
			}
			util.ajax({config:config});
		},
		
		//去除字段头尾空格
	    replacePasswoldNbsp:function(str){
	        return str.replace(/(^\s*)|(\s*$)/g, '');
	    },
	    
	    //显示对应步骤
	    showstep:function(num){
	        $(".showstep").addClass("hide");
	        $("#step"+num).removeClass("hide");
    	},
    	
    	//校验原来手机验证码是否正确
	    checkOldCode:function(data){
	        var config = {
	        	type: "POST",
				url : "/api/user/pwd/code/check_unlogin",
				dataType: "json",
				data:{"phone":data.phone,"code":data.code},
				success:function(data){
					resetpassword.checkOldCodeCallback(data);
				}
			}
			util.ajax({config:config});
	    },
	    
	    //5秒倒计时结束登出
	    setIntervalTimeOut:function(){
	    	var time = 5;
		    var code = $(".reg-msg span");
		    var t=setInterval(function() {
		          time--;
		          code.html(time);
		          if (time==0) {
		              clearInterval(t);
		              window.location.href = cache.base_url+"/logout";
		          }
		        },1000);
	    },
	    
		//确定修改密码
	   confirmEditPwd:function(obj){
	   	   var config = {
	        	type: "POST",
				url : "/api/user/pwd/resetpassword_unlogin",
				dataType: "json",
				data : {phone:obj.phone,pwd:obj.pwd,code:obj.code},
				success:function(data){
					resetpassword.confirmEditPwdCallback(data);
				}
			}
			util.ajax({config:config});
	   },
	   
	   //验证手机号码是否正确回调
	   checkOldCodeCallback:function(data){
	        var obj;
	        if(data.code ==2001){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码发送失败");
	            return false;
	        }else if(data.code ==2002){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else if(data.code ==2003){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码发送太频繁");
	            return false;
	        }else if(data.code ==2004){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else if(data.code ==2007){
	            resetpassword.showstep(2);
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"");
	            
	        }else if(data.code ==2008){
	            obj = {
	                elm : $("#smsCodeNumber"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            
	        }else if(data.code ==3010){
	            obj = {
	                elm : $("#userPhonetellogin"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"手机号与当前登录手机号不一致");
	            return false;
	        }
	    },
		
		//确定重置密码回调
	    confirmEditPwdCallback:function(data){
	    	var userPwd = $("#userPwd");
	    	if(data.code ==3007){
	            obj = {
	                elm : userPwd,
	                type : 1,
	                errortip :2
	            }
	            tips.checkFormTips(obj,"用户密码重置成功");
	            resetpassword.showstep(3);
	            resetpassword.setIntervalTimeOut();
	            return false;
	        }else if(data.code ==3008){
	            obj = {
	                elm : userPwd,
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"用户密码重置失败");
	            return false;
	        }else if(data.code ==3009){
	            obj = {
	                elm : userPwd,
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"用户密码重置操作异常");
	            return false;
	        }
	    }
    }

	resetpassword.init();

})