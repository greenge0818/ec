define(function(require,exports,module){
    var $ = require("jquery"),
        tips = require("checkformtips"),
        util = require("util"),
        cache=require("cache"),
       	prcsteel=require("prcsteel");
   	var modifypassword = {
   		
   		init:function(){
   			/* 获得焦点清空输入框 */
		    $("#oldCode").inputFocus();
		    $("#userPwd").inputFocus();
		    $("#afterPwd").inputFocus();
		
		    /* 验证表单 */
		    $("#oldcheck").verifyForm(2,blur);
		    $("#newphone").verifyForm(2,blur);
		    
		    /* 点击清空按钮清空表单内容 */
		    $(".input-empty").click(function(){
		        $(this).closest(".item-text").find("input").val("");
		    })
		
			//清除密码字段头尾空格，验证表单内容长度
	        $("#userPwd").change(function(){
		        var rpwd = modifypassword.replacePasswoldNbsp($(this).val());
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
		                errortip :2
		            }
		            tips.checkFormTips(obj,"");
		        }
		    })
		    
		    //去除确认密码头尾空格
		    $("#afterPwd").change(function(){
		        var rpwd = modifypassword.replacePasswoldNbsp($(this).val());
		        $(this).val(rpwd);
		    })
		    
		    /* 确认密码 */
		    $("#Stepgo").click(function(){
		        var oldCode = $("#oldCode").val()
		        if(oldCode == ""){
		            var obj = {
		                elm : $("#oldCode"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"验证码错误");
		            return false;
		        }
		        modifypassword.checkOldCode(oldCode);
		    })
		    
		    //qq客服
		    $(".qqlink").bind("click",function(){
		    	util.setqq();
		    })
		    
		    //返回首页
		    $("#gobackindex").click(function(){
			    var config = {
		        	type: "POST",
		        	dataType: "json",
					url : "/ajaxlogout",
					success:function(data){
						console.log(data);
						if(data.code == "0"){
							window.location.href = cache.base_url+"/";
						}
					}
				}
				util.ajax({config:config});
		    })
	    
	       //点击绑定按钮校验表单是否为空，绑定短信发送
		   $("#bindNewPhone").click(function(){
		        var userPwd = $("#userPwd").val(),afterPwd = $("#afterPwd").val(),oldCode = $("#oldCode").val();
		        config = {
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
		            return false;
		        }else if(afterPwd != userPwd){
		            var obj = {
		                elm : $("#afterPwd"),
		                type : 1,
		                errortip :1
		            }
		            tips.checkFormTips(obj,"两次密码不一致");
		            return false;
		        }else{
		            var obj = {
		                elm : $("#afterPwd"),
		                type : 1,
		                errortip :2
		            }
		            tips.checkFormTips(obj,"");
					modifypassword.confirmEditPwd(config);//确定修改密码
		        }
		        
		   })
		   
			/* 绑定发送短信事件和显示第一区块*/	
		    modifypassword.resetPhonesendSms();
		    modifypassword.showstep(1);
		    
   		},
   		//去除表单头尾空格函数
   		replacePasswoldNbsp:function(str){
	        return str.replace(/(^\s*)|(\s*$)/g, '');
	    },
	    
	    //显示对应的步骤
	    showstep:function(num){
	        $(".showstep").addClass("hide");
	        $("#step"+num).removeClass("hide");
    	},
    	
    	//校验旧验证码是否正确
    	checkOldCode:function(data){
	        var config = {
	        	type: "POST",
				url : "/api/user/pwd/code/check_afterlogin",
				dataType: "json",
				data:{"code":data},
				success:function(data){
					modifypassword.checkOldCodeCallback(data);
				}
			}
			util.ajax({config:config});
	    },
	    
	    //密码修改成功后5秒倒计时登出
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
	    
	   	//验证旧手机号是否正确
	    resetPhonesendSms:function(url,phone){
	    	var validCode;
            
            $("#getCode").click (function  () {
            	if($(this).hasClass("msg")){
		    		validCode=false;
		    		return;
		    	}
		    	validCode=true;
                var time=60;
                var code=$(this);
                if (validCode) {
                    validCode=false;
                    code.text("60秒");
                    code.addClass("msg");
                    code.addClass("msgnum");
                    var t=setInterval(function  () {
                        time--;
                        code.html(time+"秒");
                        if (time==0) {
                            clearInterval(t);
                            code.html("重新获取");
                            validCode=true;
                            code.removeClass("msg");
                        }
                    },1000)
                }
                    modifypassword.smsSend(); //Ajax发送给短信服务端

            })
        },
        
        //发送短信
	   smsSend:function(){
	        var config = {
	        	type: "POST",
				url : "/api/code/send/resetpassword_afterlogin",
				dataType: "json",
				success:function(data){
					modifypassword.sendsmsCallback(data);
				}
			}
			util.ajax({config:config});
	   },
	   
	   //确定修改密码
	   confirmEditPwd:function(obj){
	   	   var config = {
	        	type: "POST",
				url : "/api/user/pwd/resetpassword_afterlogin",
				dataType: "json",
				data : {pwd:obj.pwd,code:obj.code},
				success:function(data){
					modifypassword.confirmEditPwdCallback(data);
				}
			}
			util.ajax({config:config});
	   },
	   
	   //验证原来手机号码回调函数
	   checkOldCodeCallback:function(data){
	        var obj;
	        if(data.code ==2001){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码发送失败");
	            return false;
	        }else if(data.code ==2002){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else if(data.code ==2003){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码发送太频繁");
	            return false;
	        }else if(data.code ==2004){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else if(data.code ==2007){
	            modifypassword.showstep(2);
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"");
	            
	        }else if(data.code ==2008){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            
	        }
	    },
	    
	   	//发送端信回调函数
	    sendsmsCallback:function(data){
	      var obj;
	        if(data.code ==2000){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :2
	            }
	            tips.checkFormTips(obj,"验证码发送成功");
	            return false;
	        }else if(data.code ==2001){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码发送失败");
	            return false;
	        }else if(data.code ==2002){
	            obj = {
	                elm : $("#userPhonetellogin"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else if(data.code ==2003){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码发送太频繁");
	            return false;
	        }else if(data.code ==2004){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else if(data.code ==2008){
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"验证码错误");
	            return false;
	        }else{
	            obj = {
	                elm : $("#oldCode"),
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"发送失败");
	            return false;
	        }
	    },
	    
	    //确定重置密码回调函数
		confirmEditPwdCallback:function(data){
	    	var userPwd = $("#userPwd");
	    	if(data.code ==3007){
	            obj = {
	                elm : userPwd,
	                type : 1,
	                errortip :2
	            }
	            tips.checkFormTips(obj,"用户密码重置成功");
	            modifypassword.setIntervalTimeOut();
	            modifypassword.showstep(3);
	            return false;
	        }else if(data.code ==3003){
	            obj = {
	                elm : userPwd,
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"用户密码为空");
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
	        }else if(data.code ==3010){
	            obj = {
	                elm : userPwd,
	                type : 1,
	                errortip :1
	            }
	            tips.checkFormTips(obj,"被操作的手机号与当前用户的手机号不一致，请刷新后再试!");
	            return false;
	        }
	    }  
	    
   	}

	modifypassword.init();

})