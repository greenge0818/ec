define(function(require,exports,module){
		var $ = require("jquery"),
		tips = require("checkformtips");
		
		var callback = {
		/*
		 *  发送短信回调函数
		 *  验证手机号码是否可用，提示短信验证码是否发送成功
		 */
		sendsmsCallback:function(data){
        var obj;
        if(data.code ==1002){
            obj = {
                elm : $("#userPhonetellogin"),
                type : 1,
                errortip :2
            }
            tips.checkFormTips(obj,"手机号码可用");
            return false;
        }else if(data.code ==1003){
            obj = {
                elm : $("#userPhonetellogin"),
                type : 1,
                errortip :1
            }
            tips.checkFormTips(obj,"手机号码不正确");
            return false;
        }else if(data.code ==1004){
            obj = {
                elm : $("#userPhonetellogin"),
                type : 1,
                errortip :1
            }
            tips.checkFormTips(obj,"手机号码未注册");
            return false;
        }else if(data.code ==2000){
            obj = {
                elm : $("#smsCodeNumber"),
                type : 1,
                errortip :2
            }
            tips.checkFormTips(obj,"验证码发送成功");
            return false;
        }else if(data.code ==2001){
            obj = {
                elm : $("#smsCodeNumber"),
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
        }else if(data.code == "15002"){
            obj = {
                elm : $("#vcode"),
                type : 1,
                errortip :1
            }
            tips.checkFormTips(obj,"验证码错误");
            return false;
        }else if(data.code ==2013){
            obj = {
                elm : $("#smsCodeNumber"),
                type : 1,
                errortip :1
            }
            tips.checkFormTips(obj,"注册验证码发送受限");
            return false;
        }

    }
		
		}
	module.exports = {
		sendsmsCallback: callback.sendsmsCallback
	}
				
		
})