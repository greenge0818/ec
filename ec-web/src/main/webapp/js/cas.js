define(function(require,exports,module){
	var $ = require("jquery"),cache = require("cache"),util = require("util"),index=0,hasLoad = false,interval = null;
	var casLogin = function(username,pwd){
		$("#casIframe")[0].contentWindow.setInfo(username,pwd);
	}
   var loginAfter =  function (){
        var config = {
			url : "/api/user/login/after",
			asnyc:false,
            success : function(data) {
            	if(callback!=null){
            		callback();
            	}
            }
		}
		util.ajax({config:config});
    };
    var wrongAfter = function(msg){
    	if(wrongTip !=null){
			wrongTip();
		}else{
			$(".err-msg-span").show().html(msg);
			$(cache.targetBtn).removeAttr("disabled").removeClass("not-allowed");
		}
    }
    var callback = null;
	function setCallback(cb){
		callback = function(){
			cb();
		};
	};
	var wrongTip = null;
	function setWrongTip(cb){
		wrongTip = function(){
			cb();
		}
	};
    /**
     * 时间戳
     * @returns {number}
     */
    var timestamp = function() {
        return new Date().getTime();
    };
    var remoteValid = function(username){
    	var rtn ="0000";
    	var config = {
	        	type : "POST",
				url : "/api/passport/verify",
				data : {
	                username:username,
	                password:"*****"
	            },
	            async:false,
				dataType: "json",
	            success : function(res, status, xhr) {
	            	if(res.code == "3012"){
	            		$(".err-msg-span").show().html("用户账号已被锁定");
	            		rtn = res.code 
	            	}else{
	            		$(".err-msg-span").show().html("");
	            		rtn = res.code;
	            	}
					            	
	            }
			}
		util.ajax({config:config});
		return rtn;
    };
    
    module.exports = {
		setCallback : setCallback,
		casLogin : casLogin,
		remoteValid : remoteValid,
		setWrongTip : setWrongTip,
		wrongAfter:wrongAfter,
		loginAfter:loginAfter
	}
});
