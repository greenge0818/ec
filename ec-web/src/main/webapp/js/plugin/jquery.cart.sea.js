/**
 *
 * This plugin depends on store.js now.
 * https://github.com/marcuswestin/store.js
 *
 */
define('cart', function (require, exports, module) {
	var jQuery = require("jquery"),
		cache = require("cache"), 
		constant = require("constant"),
        tips  = require("checkformtips"),
        cas = require("cas"),
        util=require("util");
    	window.cas = cas;
       require("jquery-tips");
	   require('prcsteel');
(function( $ ){
  
   // require('json');
    var em, hasEnoughWeight = true, timeLong=60;
        
        //prcsteel = require("prcsteel"),
       
       
	//当HTML高度小于视口高度提交订单按钮固定在底部
	 function setHeight() {
        $(".m-b").removeAttr("style");
		var browser_h = util.getTotalHeight(),sH = $(window).scrollTop(), html_h = $('body').innerHeight();
        var ro = $(".list_return")[0].getBoundingClientRect();
         
        if( ro.top >= (browser_h)){
            $(".m-b").css({"position":"fixed","bottom":"0px","left":"0"});
        } else{
            $(".m-b").removeAttr("style");
        }

     }
    function successDialog(status) {
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

        $(".dias_continue,#dialogClose").on("click",function(){
            util.closeDialog();
        })

    }
    function loginDialog(){
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
		el +='<span class="sms_btn cart-login-valid-btn">获取验证码</span>';
		el +='<p class="form-err"><em class="error-icon"></em><span></span></p>';
		el +='</li>';
		el +='</ul>';
		el +='</form>';
		el +='</div>'
		el +='<div style="height:20px;margin:3px auto;"><span class="red-c err-msg-span" ></span></div>';
    	el +='<div class="cart-login-bottom"><input type="button" class="cart-login-submit" value="提交"/></div>';
    	el +='</div>';
    	util.getDialog(false, el);
    	$("input[name='phone_number']").inputFocus();
		$("#smsCodeNumber").inputFocus();
		$(".input-empty").click(function(){
			$(this).closest(".item-text").find("input").val("");
		})
		$("#userRegister").verifyForm(3,blur);
    };
    
    function countDown(){
    	var btn = $(".cart-login-valid-btn");
		var intervalEvent = setInterval(function(){
			if(timeLong>=0){
				btn.text(timeLong--+"秒");
			}else{
				btn.text ("重新获取");
				timeLong = 60;
				btn.removeAttr("disabled").removeClass("not-allowed");
				clearInterval(intervalEvent);
			}
			
		},1000);
	};
    function diaBatchRemoveConfirm() {
        var el = "";
        
        el += '<div class="confirm-layer">';
        el += '<div class="confirm-bar">';
        el += '<div class="confirm-info">确定要删除所选资源吗！</div>';
        el += '<div class="confirm-btn-bar">';
        el += '<button class="red-btn dia_cart_brmi_yes">确定</button>&nbsp;&nbsp;';
        el += '<button class="gray-btn dia_cart_rmi_no">取消</button>';
        el += '</div>';
        el += '</div>';
        el += '</div>';
        
        util.getDialog(false, el);
    }
    
    function diaRemoveRemoveConfirm(id) {
    	
        var el = "";
        
        el += '<div class="confirm-layer">';
        el += '<div class="confirm-bar">';
        el += '<div class="confirm-info">确定要删除资源吗！</div>';
        el += '<div class="confirm-btn-bar">';
        el += '<button class="gray-btn dia_cart_rmi_no" data-id="'+id+'">取消</button>&nbsp;&nbsp;';
        el += '<button class="red-btn dia_cart_rmi_yes" data-id="'+id+'">确定</button>';
        el += '</div>';
        el += '</div>';
        el += '</div>';
        
        util.getDialog(false, el);
    }
    
    function diaRemoveExpiredConfirm(id) {
        var el = "";
        
        el += '<div class="confirm-layer">';
        el += '<div class="confirm-bar">';
        el += '<div class="confirm-info">确定要删除资源吗！</div>';
        el += '<div class="confirm-btn-bar">';
        el += '<button class="red-btn dia_cart_rmei_yes" data-id="'+id+'">确定</button>&nbsp;&nbsp;';
        el += '<button class="gray-btn dia_cart_rmei_no" data-id="'+id+'">取消</button>';
        el += '</div>';
        el += '</div>';
        el += '</div>';
        
        util.getDialog(false, el);
    }
    
    function showMsg(msg) {
        var el = "";
        
        el += msg;
        
        util.getDialog(false, el);
    }

    function isSpecialCategory(str) {
        if(str.indexOf(cache.messages.cart.special) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    function rounding (num) {
        return Math.round(num*1000)/1000;
    }
    var ajaxCallback = {
	//验证用户是否存在   	
//		checkUserCallback:function(data){
//		 	var phone = $("#userPhonetellogin").val(),
//	        code = $("#smsCodeNumber").val();
//			if(data.code == "1001"){ //已存在用户直接登录
//				login(phone,code);
//			}else if(data.code == "1002"){ //未注册用户
//				var config = {
//					url : "/api/user/registnopwd",
//					data:{"mobile":phone,"code":code},
//					success:function(data){
//						ajaxCallback.checkUserRegCallback(data);
//					}
//				}
//				util.ajax({config:config});
//			}else if(data.code == "3012"){ //用户锁定
//	            ajaxCallback.checkUserRegCallback(data);
//			}
//		},
		checkUserRegCallback:function(data){
			var phone = $("#userPhonetellogin").val(),code = $("#smsCodeNumber").val(),obj,
				style = 2;
				if(data.code !="3005"){
					obj = {
						type : style
					}
					tips.checkFormTips(obj,constant.getCodeMsg(data.code));
					$(".cart-login-submit").removeAttr("disabled").removeClass("not-allowed");
					return false;
				}
				cas.setCallback(loginAfter);
				cas.casLogin(phone,code);
			
		}
  	};
//  function login(phone,pwd){
//  	var config = {
//      	type : "POST",
//			url : "/api/passport/verify",
//			data : {
//              username:phone,
//              password:pwd
//          },
//			dataType: "json",
//          success : function(res, status, xhr) {
//          	loginCallback(res)
//
//          }
//		}
//		util.ajax({config:config});
//  };
//  function verify(url){
//		$.ajax({
//          type : "GET",
//          url : url,
//          success : function(text, textStatus, xhr) {
//              fetchAccount();
//             
//          }
//      });
//  };
    function loginAfter(){
        var config = {
			url : "/api/user/login/after",
			asnyc:false,
            success : function(data) {
            	$.fn.shoppingcart('submit');
            }
		}
		util.ajax({config:config});
    };
	//验证手机格式与长度
    function checkPhone(elmid){
        var val = elmid.val();
        if(val == ""){
            $(".err-msg-span").show().html("手机号码不能为空");
            elmid.focus();
            return false;
        }else if(!(/^1[3|4|5|7|8][0-9]\d{8}$/.test(val))) {
            $(".err-msg-span").show().html("手机号码格式不正确");
            elmid.focus();
            return false;
        }
        $(".err-msg-span").show().html("");
        return true;
    };
    function setEvents() {
    	
    	 $(document).on('click', ".cart-login-submit", function() {
    	 	var v = checkPhone($("#userPhonetellogin")) &&　util.checkverifyCode($("#smsCodeNumber")) ;
    	 	if(!v){
    	 		return ;
    	 	}
    	 	var phone = $("#userPhonetellogin").val(),
            code = $("#smsCodeNumber").val();
    	 	var rtn = cas.remoteValid(phone);
    	 	if(rtn == "3012"){
    	 		return ;
    	 	}else if(rtn == "1004"){
    	 		var config = {
					url : "/api/user/registnopwd",
					data:{"mobile":phone,"code":code},
					success:function(data){
						ajaxCallback.checkUserRegCallback(data);
					}
				}
				util.ajax({config:config});
    	 		return ;
    	 	}
    	 	$(".cart-login-submit").prop("disabled","disabled").addClass("not-allowed");
//	 		var config = {
//				url : "/api/user/phone/check",
//				data:{"phone":$("#userPhonetellogin").val()},
//				success:function(data){
//					ajaxCallback.checkUserCallback(data);
//				}
//			}
//			util.ajax({config:config});
            cas.setCallback(function(){$.fn.shoppingcart('submit');});
			cas.casLogin(phone,code);
        });
    	 $(document).on('click', ".cart-login-valid-btn", function() {
    	 	var v = checkPhone($("#userPhonetellogin"));
    	 	if(!v || (timeLong>0 && timeLong!=60)){
    	 		return ;
    	 	}
			$.post(cache.base_url + "/api/code/send/registerorlogin", { phone : $("#userPhonetellogin").val()}, function (response, status) {
                $(".err-msg-span").show().html(constant.getCodeMsg(response.code));
                //if(response.code == "2000"){
                //	$(".err-msg-span").show().html("").html("验证码发送成功");
                //}else if(response.code == "2001"){
                //	$(".err-msg-span").show().html("").html("验证码发送失败");
                //}else if(response.code == "2003"){
                //	$(".err-msg-span").show().html("").html("验证码发送太频繁");
                //}else if (response.code == "3012") {
                //    $(".err-msg-span").show().html("").html("用户账号已被锁定");
                //}
                
            });
			countDown();
    		$(this).attr("disabled","disabled").addClass("not-allowed");
        });
        $(document).on('click', ".cart_remove_item", function() {

            var e = $(this), id = e.attr('id').replace('cart_remove_item_id_', '');
            diaRemoveRemoveConfirm(id);

        });
        
        $(document).on('click', ".deleteSelected", function() {
            var b = false;
            $('.cart_items').find('.d-list-m').each(function() {
                $(this).find('.cart_itme_checkbox').each(function(){
                    var id, checked = $(this).is(':checked'), ul = $(this).closest('ul');
                    if(!b && checked){
                        b = true;
                    }
                });
            });
            
            if(b) {
                diaBatchRemoveConfirm();
            }
           setTimeout(setHeight, 200);
        });
        
//      $(document).on("click", ".cart_edit",function () {
//          var e = $(this), action = e.attr('data-action');
//          var sname = $(this).closest('li').parent().find('li').eq(1).find('.name').html();
//          modifyWeight(e, action, sname);
//      });
        
        $(document).on("change", ".buy_num",function () {
            var e = $(this);

          //  var sname = $(this).closest('li').parent().find('li').eq(1).find('.name').html();

            modifyWeight(e, 'modify');
        });
        
        $(document).on("change", "input[type='checkbox']",function () {
            $.fn.shoppingcart('refresh');
        });
        
        $(document).on("click", " .d-list-ul .list01 input[type='checkbox']",function () {
            var isT = true,
                checked = $(this).is(":checked"), 
                checkItem = $(this).closest('.cart_items'),
                coms = $(this).closest('.company-name'), 
                dl = coms.next('.d-list-m')
                id = $(this).closest('.d-list-ul').attr("data-id");
            	
            dl.find('input[type="checkbox"]').prop('checked', checked);
            
            checkItem.find('.cart_itme_checkbox').each(function(){
                var checked = $(this).is(':checked');
                if( isT && !checked) {
                    isT = false;
                }
            });

            $(".checked-all").prop('checked', isT);
            var checkFlag = checked == true ? "Y" : "N";	
            $.fn.shoppingcart("check",id,checkFlag);
        });
        
        $(document).on("change", ".checked-all", function(){
        	var isChecked = $(this).prop("checked");
        	var ids=[];
            $(".cart_items").find('input[type="checkbox"]').prop("checked", isChecked);
            $(".checked-all").prop("checked", isChecked);
            $(".cart_items").find('input[type="checkbox"]').each(function(){
             	ids.push($(this).closest('.d-list-ul').attr("data-id"));
            })
           $.fn.shoppingcart("check",ids.join(","),isChecked ? "Y" : "N");
        });
        
        $(document).on('click', ".btn_submit", function() {
        	
        	$.fn.shoppingcart('submit');
        });
        
        $(document).on("click", ".dia_continue",function () {
            util.closeDialog();
        });
        
        $(document).on("click", ".dia_cart_rmi_yes",function () {
            
            var id = $(this).attr('data-id'), e = $('#cart_remove_item_id_'+id);
            function callback(){
//          	showMsg('删除成功！');
//         		setTimeout(setHeight, 200);
            }
            $.fn.shoppingcart('remove', e,callback);
            util.closeDialog();
            
        });
        
        $(document).on("click", ".dia_cart_brmi_yes",function () {
            var ids = [];
            $('.cart_items').find('.d-list-m').each(function() {
                $(this).find('.cart_itme_checkbox').each(function(){
                    var id, checked = $(this).is(':checked'), ul = $(this).closest('ul');
                    if(checked){
                        id = ul.attr('data-sid');

                        ids.push(id);
                    }
                });
            });
           function callback(){
//          	showMsg('删除成功！');
//         		setTimeout(setHeight, 200);
            }
            $.fn.shoppingcart('batchRemove', ids.join(","),callback);
            
            util.closeDialog();
            
            
        });
        
        $(document).on("click", ".dia_cart_rmi_no",function () {            
            util.closeDialog();
        }); 
        
        $(document).on("click", ".cart_remove_expire_item",function () {
            
            var e = $(this), id = e.attr('data-sid');
            diaRemoveExpiredConfirm(id);
            
//            var e = $(this);
//            $.fn.shoppingcart('removeExpiresItme', e);
//            
//            util.closeDialog();
//            
//            showMsg('删除成功！');
        });
        
//      $(document).on("click", ".cart_online",function () {
//          window.open ("http://live800.gtxh.cn/live800/chatClient/chatbox.jsp?companyID=8909&amp;configID=9&amp;skillId=2&amp;enterurl=http%3A%2F%2Fwww%2Egtxh%2Ecom%2F&amp;k=1", "newwindow", "height=600, width=900, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
//      });
        
        $(document).on("change", ".cart_itme_checkbox", function(){
            var c = true, isT = true, dl = $(this).closest('.d-list-m'), checkItem = $(this).closest('.cart_items');
            
            dl.find('.cart_itme_checkbox').each(function(){
                var checked = $(this).is(':checked');
                if(!checked) {
                    c = false;
                }
            });

            checkItem.find('.cart_itme_checkbox').each(function(){
                var checked = $(this).is(':checked');
                if(!checked) {
                    isT = false;
                }
            });

            var comCheckBox = dl.closest('.d-list-m').prev('.company-name').find('input[type="checkbox"]');
                comCheckBox.prop('checked', c);
            $(".checked-all").prop('checked', isT);
            
            $.fn.shoppingcart('refresh');

        });
        
        $(document).on("click", ".dia_cart_rmei_yes",function () {
            
            var id = $(this).attr('data-id'), e = $('#cart_remove_item_id_'+id);
            $.fn.shoppingcart('removeExpiresItme', e);
            
            util.closeDialog();
            
           // showMsg('删除成功！');
        });
        
        $(document).on("click", ".dia_cart_rmei_no",function () {            
            util.closeDialog();
        });
        
        
    }
    
    function modifyWeight(e, action) {
        var el, w, weight, id = e.closest('.d-list-ul').attr('data-id');
        if(action=="add") {
            el = e.prev('.buy_num');
            w = util.clearFormat(el.val());
            
            weight = rounding(parseFloat(w)+1);
        } else if (action == "cut") {
            el = e.next('.buy_num');
           w = util.clearFormat(el.val());
            
            weight = rounding(parseFloat(w)-1);
        } else if (action == "modify") {
            weight = util.clearFormat(e.val());
            
            el = e;
        }
        
        var minWeight = 1;

        weight = weight<minWeight || !verifyWeight(weight)?minWeight:weight;
		e.val(util.setFormat(weight,true));
        $.fn.shoppingcart('edit', el, id, util.setFormat(weight,true));
    }
    
    function verifyWeight(str) {
        str = str + "";
        if(str.match(/^\+{0,1}\d+(\.\d{1,})?$/)!=null){
             return true;
        } else {
            return false;
        }       

    }
	function getSpec(item){
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
	};
    // Add one item into shopping cart.
    function addItem(item,isfirst){
    		if(item.isChecked == "N"){
    			$(".checked-all").removeAttr("checked");
    		}
        var com_container = em;
            com_container.append('<div class="d-list-m" '+(isfirst ? 'style="border-top:1px solid #d2d2d2;"':"")+' id="list_'+item.id+'"></div>');
            com_container.find('#list_'+item.id).append('<div class="d-list" >' +
                    '<ul class="d-list-ul f-clrfix" data-sid="'+item.id+'" data-id="'+item.id+'">' +
                        '<li class="list01">' +
                            '<input type="checkbox" class="cart_itme_checkbox" '+(item.isChecked=="Y" ? 'checked="checked">' :"") +
                        '</li>' +
                        '<li class="d-list-bar list02">' +
                            '<dl class="prod-name-dl">' +
                                '<dt class="dt"><em class="recommend-zw relative" ></em><span class="name">'+item.categoryName+' <em>'+item.materialName+'</em></span></dt>' +
                                '<dd class="dd"><span class="s-gray">规格：</span>'+getSpec(item)+' </dd>' +
                            '</dl>' +
                        '</li>' +
                        '<li class="d-list-bar list03">' +
                            '<dl>' +
                                '<dt><span class="s-gray">厂家：</span>'+item.factoryName+'</dt>' +
                                '<dd class="ellipsis"><span class="s-gray">仓库：</span><span class="tips" data-tip="'+item.cityName+" "+item.warehouseName+'">'+item.cityName+" "+item.warehouseName+'</span></dd>' +
                            '</dl>' +
                        '</li>' +
                        '<li class="d-list-bar list04 t-r ">' +
                            '<p data-price="'+(item.price == null ? 0 :item.price) +'"> '+ (item.price == null ? "议价" : ("￥"+util.setFormat(item.price))+"/吨")+' </p>' +
                        '</li>' +
                        '<li class="d-list-bar list05">' +
                        	'<dl class="prod-name-dl">' +
                                '<dt class="dt"><input name="" type="text" class="buy_num" value="'+util.setFormat(item.weight,true)+'" >吨</dt>' +
                                '<dd class="dd" style="margin-left:0px;padding-left:0px"><span>'+item.weightConcept+'</span></dd>' +
                            '</dl>' +
                        '</li>' +
                        '<li class="d-list-bar pay-bar list06  t-r">' +
                            '<p class="rmb ">'+(item.amount == null ? "-" : ("￥"+util.setFormat(item.amount)))+'</p>' +
                        '</li>' +
                        '<li class="d-list-bar  list07">' +
                            '<p><a href="javascript:;" id="cart_remove_item_id_'+item.id+'" data-sid="'+item.id+'" class="cart_remove_item" >删除</a>' +
                            '</p>' +
                        '</li>' +
                    '</ul>' +
                '</div>');
    }

    var methods = {
        load : function() {
        	$('.cart_items').empty();
            $.post(cache.base_url + "/api/cart/getcartinfo", {}, function (response, status) {
                if(response.code == "4013") {
                    $('.cart_items').empty();
                    
                    var items = response.data;
                    
                    
                    if(items == null || items.length==0) {
                        $('.empty-indent').show();
                        
                        $('.d-list-tit').hide();
                        $('.list-total').hide();
                        $('.buy_step').hide();
                        $('.list_return').hide();
                        
//                      if(res.isLogin) {
//                          $('.indent-login-btn').hide();
//                          $('.info-text').html('购物车内暂时没有商品~');
//                      }
                    } else {
                        for(var i in items[0]["resourceList"]) {
	                        addItem(items[0]["resourceList"][i],(i==0 ? true:false));
	                    }
                        $(".tips").tips("bottom");
                        $('.empty-indent').hide();
                        
                        $('.d-list-tit').show();
                        $('.list-total').show();
                        $('.buy_step').show();  
                        $('.list_return').show();
                    }
                    
                    $.fn.shoppingcart('refresh');
                  //  showOverweightWarning();
                }
            });

        },
        
        init : function () {
            em = $(this);
            setEvents();
            $.fn.shoppingcart('load');
            setTimeout(setHeight, 200);
            $(window).scroll(function() {
                setTimeout(setHeight, 200);
            });
        },
        check:function(id,check){
			$.post(cache.base_url + "/api/cart/updatecart", {
				ids: id,
				isChecked:check
			}, function(response, status) {
				if (response.code == "4010") {
					$.fn.shoppingcart('refresh');
				}
			});
		},
        edit : function(e, id, weight) {
        	weight = util.clearFormat(weight);
            var min = 1;
            var b = weight>=min ? true :false;
            if(b) {
                $.post(cache.base_url + "/api/cart/updatecart", { ids : id, weight : weight }, function (response, status) {
                    if(response.code == "4010") {
                        e.val(util.setFormat(weight));
                        $.fn.shoppingcart('refresh');

                        var sp = 0, tp = 0, p = 0;
                        sp = e.closest('li').prev('li').find('p').attr("data-price");
                        tp = e.closest('li').next('li').find('.rmb');//.html().replace('￥','');
                        if(sp > 0){
                        	p = rounding(weight*sp);
                        	tp.html("￥"+util.setFormat(p));
                        }else{
                        	tp.html("-");
                        }
						e.val(util.setFormat(weight,true));
                        $.fn.shoppingcart('refresh');
                    } 
                });
            } else {

                alert("采购重量不能小于"+min+"吨");
            }
        },
        
        removeExpiresItme : function(e) {
            var mid = e.attr('data-mid'),
                id = e.attr('data-sid'),
                dlist = e.closest('.d-list'),
                parent = dlist.closest('.d-list-m'),
                com_name = parent.prev('.company-name');

            $.post(cache.base_url + "/Home/DelOrder", { shopID : id }, function (response, status) {
                var res = $.parseJSON(response);
                if(res.statusCode == "0") {
                    if(parent.find('.d-list').length==1) {
                        com_name.next('.d-list-m').remove();
                        com_name.remove();
                    }

                    dlist.remove();
                    
                    $.fn.shoppingcart('refresh');
                }

            });
        },
        
        batchRemove : function(ids,callback) {
            
            $.post(cache.base_url + "/api/cart/delcartbyids", { ids : ids }, function (response, status) {
                if(response.code == "4012") {
                	callback();
                    $.fn.shoppingcart('load');
                }

            });
 
        },
        
        remove : function(e,callback) {
            var id = e.attr('data-sid'),
                dlist = e.closest('.d-list'),
                parent = dlist.closest('.d-list-m'),
                com_name = parent.prev('.company-name');
            $.post(cache.base_url + "/api/cart/delcartbyids", { ids : id }, function (response, status) {
                if(response.code == "4012") {
                    
                    if(parent.find('.d-list').length==1) {
                        dlist.remove();
                        parent.remove();
                        com_name.remove();
                    } else {
                        dlist.remove();
                    }
                    $(".d-list-m").eq(0).attr("style","border-top:1px solid #d2d2d2;");
                    callback();
                   // setTimeout(setHeight, 200);
                    $.fn.shoppingcart('refresh');
                }

            });
        },
        
        // Get items count
        getCount : function( ) {
            var count = 0
            em.find('.cart_itme_checkbox').each(function() {
                if($(this).is(':checked')) {
                    count = count+1;
                }
            });

            return count;
        },

        getWeight : function( ) {
            var count = 0;
            em.find('.cart_itme_checkbox').each(function() {
                if($(this).is(':checked')) {
                    var w = util.clearFormat($(this).closest('.d-list').find('.buy_num').val());
                    count = parseFloat(w)+parseFloat(count);
                    
                }
            });

            return rounding(count);
        },

        // Get cart price
        getPrice : function( ) {
            var price = 0;

            em.find('.cart_itme_checkbox').each(function() {
                if($(this).is(':checked')) {
                    var p = util.clearFormat($(this).closest('.d-list').find('.rmb').html().replace('￥',''));
                    price = parseFloat(p)+parseFloat(price);
                }
            });

            return rounding(price);
        },
        
        refresh : function () {
              var  m = $('.list-total').find('.middle'); 
                //l = $('.list-total').find('.right');
           
           
            
            var icount = $.fn.shoppingcart('getCount');
            m.html('已选资源 <span>'+icount+'</span> 条 &nbsp; &nbsp; &nbsp; 重量总计： <span>'+util.setFormat($.fn.shoppingcart('getWeight'),true)+'</span> 吨');

           // l.find('.font_total').html('<em>￥</em>'+util.setFormat($.fn.shoppingcart('getPrice')));
            
            var link = $('.list-total').find('.right').find('a');
            if(icount>0) {
                link.addClass('hover');
            } else {
                link.removeClass("hover");
            }
            
            em.find('.buy_num').each(function() {
                var w = parseFloat($(this).val()), s = parseFloat($(this).attr('data-stock'));
                if(w<=s) {
                    $(this).closest('.d-list').css('border', '');
                }
            });
        },
        //Not yet open
        notOpenDeal : function (){
            var el = "";

            el += '<div class="indent-succ-layer">';
            el += '<div class="indent-succ">';
            el += '<div class="indent-succ-info"><em class="icon text-icon"></em>您未开通交易功能！</div>';
            el += '<div class="succ-link">';
            el += '<a href="http://market.prcsteel.com/Manage/Account/OpenTransaction" class="dia_continue">开通交易, 您将享有更多的服务^_^</a>';
            el += '</div>';
            el += '</div>';
            el += '</div>';

            util.getDialog(false, el);
        },
        // Submit
        submit : function() {
        	if($(".list01").children('input:checked').length == 0){
        		return ;
        	}
           	util.post("/api/cart/insertrequirement?t="+Math.random(), {}, function (response, status) {
           		if(response.code == "4006" ){
           			successDialog(response.status);
                    var orderCode = response.data;
                    if(orderCode != undefined && orderCode != null){
                        _utaq.push(['setCustomVariable',1, 'salesleads-tijiao', orderCode,'page']);
                        _utaq.push(['trackPageView','/virtual/salesleads-tijiao']);
                    }
           			$.fn.shoppingcart('load');
           		}else if(response.code == "0001"){
           			loginDialog();
           		}
	        });
        }
    };

    $.fn.shoppingcart = function( method ) {
        // Method call logic
        if ( methods[method] ) {
            return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof method === 'object' || ! method ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  method + ' not exist in jQuery.shoppingcart' );
        }

    };

    $(window).resize(function() {
        setTimeout(setHeight, 200);
    });
})( jQuery );
})
