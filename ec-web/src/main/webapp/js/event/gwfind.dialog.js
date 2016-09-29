define(function(require,exports,module){
	var $ = require("jquery"),util = require("util");

	var init = {

	    successDialog:function(reload, orderCode, status) {
	        var el = "";
	        el += '<div class="indent-succ-layer_n">';
	        el += '<div class="indent-succ">';
	        el += '<div class="indent-succ-info"><em class="icon success-icon"></em><br><span style="display:inline-block;margin-top:20px">提交成功！</span></div>';
	        if(status == "1"){
				el += '<div>请保持您的手机畅通，钢为掌柜会尽快联系您</div>';
			}else{
				el += '<div>尊敬的客户，我们将会在下个工作日及时联系您！</div>';
			}

	        el += '<div class="succ-link">';
	        el += '<a href="javascript:;" class="dias_continue">我知道了</a>&nbsp;';
	        el += '</div>';
	        //el += '<div><span >温馨提示：</span>钢为会尽快联系您；若超过30分钟仍未和您联系，请拨打我们的官方热线0571-8971&nbsp;8799。</div>';
	        el += '</div>';
	        el += '</div>';
	        util.getDialog(false, el);
			if(orderCode != undefined && orderCode != null){
				_utaq.push(['setCustomVariable',1, 'salesleads-tijiao', orderCode,'page']);
				_utaq.push(['trackPageView','/virtual/salesleads-tijiao']);
			}
	        $(".dias_continue,#dialogClose").on("click",function(){
	            util.closeDialog();
	            $(".file-err-msg-span").html("");
	            if(reload!=0){
	            	window.location.reload();
	            }
	        })
	
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
	        el +='<input type="text" id="userPhonetellogin" autocomplete="off"  name="phone_number" must="1" class="input_txt ipt-text" maxlength="11">';
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
	        el +='<label class="ipt-label" style="color: rgb(153, 153, 153);">验证码</label><input type="text" must="1" maxlength="4" msg="验证码错误" verify="number" value="" name="code" class="ipt-text wd-90" id="smsCodeNumber">';
	        el +='</div>';
	        el +='<em class="input-empty"></em>';
	        el +='</div>';
	        el +='</div>';
	        el +='</div>';
	        el +='<span class="sms_btn login-valid-btn-find">获取验证码</span>';
	        el +='<p class="form-err"><em class="error-icon"></em><span></span></p>';
	        el +='</li>';
	        el +='</ul>';
	        el +='</form>';
	        el +='</div>'
	        el +='<div style="height:20px;margin:3px auto;"><span class="red-c err-msg-span" ></span></div>';
	        el +='<div class="cart-login-bottom"><a class="cart-login-submit">提交</a></div>';
	        el +='</div>';
	        util.getDialog(false, el);
	        
	        $("input[name='phone_number']").inputFocus();
	        $("#smsCodeNumber").inputFocus();
	        $(".input-empty").click(function(){
	            $(this).closest(".item-text").find("input").val("");
	        })
	        $("#userRegister").verifyForm(3,blur);
	        $("#gwfindform").verifyForm(3,blur);
	    }
   }
   module.exports = {
		successDialog: init.successDialog,
		loginDialog: init.loginDialog
	}
})