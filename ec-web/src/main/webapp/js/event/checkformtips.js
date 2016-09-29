define(function(require,exports,module){
	var $ = require("jquery");

    exports.checkFormTips = function(obj,text){
        var objlink = obj.link == "undefined" ? "" : obj.link;
      if(obj.type == "1"){
        var msgp = obj.elm.closest("li").find(".form-err"),
            arr = obj.elm.closest("li").find(".form-err span"),
            em = obj.elm.closest("li").find(".form-err em")
        msgp.show();
        if(obj.errortip == "1"){
          em.removeClass("success-icon");
          em.addClass("error-icon");
          arr.text(text);
            if (objlink){
                msgp.find(".form-err-link").html("&nbsp;&nbsp;"+objlink);
            }else{
                msgp.find(".form-err-link").html("");
            }
        }else if(obj.errortip == "2"){
          em.removeClass("error-icon");
          em.addClass("success-icon");
          arr.text(text);
            if (objlink){
                msgp.find(".form-err-link").html("&nbsp;&nbsp;"+objlink);
            }else{
                msgp.find(".form-err-link").empty();
            }
        }else if(obj.errortip == "3"){
        	em.removeClass("error-icon");
        	arr.text(text);
        }
      }else if(obj.type == "2"){
      	$(".err-msg-span").show().html("").html(text);
      }

    }
})