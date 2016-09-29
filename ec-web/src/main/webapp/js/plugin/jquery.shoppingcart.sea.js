/*jslint sloppy: false, vars: false*/
/**
 *
 * This plugin depends on store.js now.
 * https://github.com/marcuswestin/store.js
 *
 */
define('shoppingcart', function(require, exports, module) {
	var $ = require("jquery"),
	cache = require("cache"),
	util = require("util"),
	constant = require("constant");
	require('prcsteel');
	require('json');
	(function($) {
		//	var tips = require("jquery-tips");
			var em = "";

			function setHeader(isHidden) {
				var html = '<div class="sc_link">' +
					'<ul>' +
					'<li><a id="toggleCart" class="toggle-cart" style="display:none;" href="javascript:;"><em title="折叠">&raquo;</em></a></li>' +
					'<li class="findLink"><a href="javascript:;" class="cart i_cart"><span class="logo">&nbsp;</span>钢为购</a></li>' +
					'<li class="onlineLink"><a class="online"><span class="logo icons">&nbsp;</span>在线客服</a>' +
					'</li>' +
					'<li class="qrcode"><a class="qrcode_a"><span class="logo icons">&nbsp;</span>掌柜APP</a>' +
					'<div class="qrcode_sh"><i class="i_qrcode_sh"></i>' +
					'</div>' +
					'</li>' ;
					if(!isHidden){
						html +='<li class="succ-info-r relative"><div class="addCar-succ-layer absolute none"><em class="addcar-layer-arr">◆</em><a class="succ-del">×</a><dl class="succ-info-bar f-clrfix"><dt class="fl"><span class="icon addcar-succ-icon"></span></dt><dd class="fr"><p class="bold">加入购物车成功</p><p>您可以去<a class="link" target="_blank" href="'+cache.base_url+'/cart/mycart">购物车结算</a></p></dd></dl><p class="s-gray close-info-p"><em>3</em>秒后自动关闭</p></div></li>' ;
						html +='<li class="cartLink"><a href="javascript:;" class="cart i_cart"><span class="logo">&nbsp;</span>购物车<span class="cart_num">0</span></a></li>' ;
					}
//					'<li class="qrcode"> <a href="javascript:;" class="qr_code icons"></a>' +
//					'<div class="qrcode_sh"><i class="i_qrcode_sh"></i>' +
//					'</div>' +
//					'</li>' +
					html +='</ul>' +
					'<div class="returntop" id="returntop">' +
					'<a class="rtn_a"><span class="logo icons">&nbsp;</span>回到顶部</a>'+
					'</div>' +
					'</div>' ;
					if(!isHidden){
						html +='<div class="sc_list">' +
						'<div class="set_tit f-clrfix">' +
						'<label class="f-fl"><input name="checkedall" type="checkbox" checked="checked" /> 全选</label><a target="_blank" href="'+cache.base_url+'/cart/mycart">全屏查看</a>' +
						'</div>' +
						'<div class="cart_list relative">' +
						'</div>' +
						'<div class="cartfter relative"><div class="cart_choose f-clrfix"><span class="fr">已选 <span class="count">0</span> 条，共<span class="count">0</span>吨</span>' +
						'</div>' +
						'<div class="cart_btn"><a href="JavaScript:;" class="cart_btn_submit">去购物车</a>' +
						'</div></div>' +
						'</div>';
					}
					html +='<div class="find-content"></div>';

				em.append(html);
			}

			function rounding(num) {
				return Math.round(num * 1000) / 1000;
			}

			function setHide() {
				// em.find(".cart_remove_item").hide(); // hide delete link.
				em.find(".cart_edit").hide();
				em.find(".cart_remove_by_company").hide();
			}

			function setEvents() {
				$("#returntop").bind("click", function() {
						//为了兼容IE8下滚动条事件
						if(document.documentElement && document.documentElement.scrollTop){
							document.documentElement.scrollTop = 0;
						}
						$("body").scrollTop(0)
					})
					// bind delete event
					//      $(document).on("mouseenter", ".st_name",function () {
					//          $(this).find(".cart_remove_item").show();
					//
					//      });

				//      $(document).on("mouseleave", ".st_name",function () {
				//          $(this).find(".cart_remove_item").hide();
				//      });

				$(document).on("click", ".cart_item_weight", function() {
					$(this).css("border", "1px solid #cacaca");
					$('.cart_item_weight').css({
						"border": "1px solid rgba(255,255,255,0)",
						"background": "rgba(255,255,255,0)"
					}).attr("readonly", "readonly");
					$(this).removeAttr("readonly").css({
						"border": "1px solid #cacaca",
						"background": "#fff"
					});
					$(this)[0].focus();
					$(this).focus();
				});
				$(document).on("focus", ".cart_item_weight", function() {
					$(this).removeAttr("readonly");
				});
				$(document).on("blur", ".cart_item_weight", function() {
					$('.cart_item_weight').css({
						"border": "1px solid rgba(255,255,255,0)",
						"background": "rgba(255,255,255,0)"
					}).attr("readonly", "readonly");
				});

				$(document).on("mousemove", ".ul_content", function() {
					$(this).find(".cart_remove_by_company").show();
				});

				$(document).on("mouseout", ".ul_content", function() {
					$(this).find(".cart_remove_by_company").hide();
				});

			      $(document).on("click", ".cart_remove_by_company",function (ent) {
			          var id  = $(this).attr('data-id');
			          $.fn.shoppingcart('remove', id);
			      });

//				$(document).on("click", ".cart_remove_by_company", function(ent) {
//					var ids = [],
//						u = $(this).closest('ul');
//
//					u.find('.st_name').each(function() {
//						var id = $(this).attr('data-sid');
//						ids.push(id);
//					});
//
//					$.fn.shoppingcart('batchRemove', ids);
//				});

				//        $(document).on("click", ".cart_edit",function () {
				//            var e = $(this), action = e.attr('data-action');
				//            modifyWeight(e, action);
				//        });

				$(document).on("change", ".cart_item_weight", function() {
					var e = $(this);
					var sname = $(this).closest('li').find('.sname').html();
					modifyWeight(e, 'modify', sname);
				});


				$(document).on("click", ".shop_name input[type='checkbox']", function() {
					var checked = $(this).is(":checked");
					
					if (!checked) {
						em.find('.set_tit').find('input[name="checkedall"]').prop("checked", false);
					} else {
						var c = true;
						em.find('.cart_list').find('input[type="checkbox"]').each(function() {
							if (c && !$(this).is(':checked')) {
								c = false;
							}
						});
						em.find('.set_tit').find('input[name="checkedall"]').prop("checked", c);
					}

					id = $(this).closest('ul').attr('data-id');
					//alert(checked)
					var checkFlag = checked == true ? "Y" : "N";					
					$.fn.shoppingcart("check",id,checkFlag);
					
				});


				$(document).on("click", "input[name='checkedall']", function() {
					var c = $(this).is(":checked"),id="";
					em.find('.cart_list').find('input[type="checkbox"]').prop("checked", c);
					em.find('.cart_list').find('ul[class="ul_content"]').each(function(){
						id += $(this).attr("data-id")+",";
					})
					id = id.substr(0,id.length-1);
					var checkFlag = c == true ? "Y" : "N";
					$.fn.shoppingcart("check",id,checkFlag);
				});

				$(document).on("click", ".cart_btn_submit", function() {
					$.fn.shoppingcart('submit');
				});

				$(document).on("click", ".dia_continue", function() {
					util.closeDialog();
				});

				$(document).on("click", ".add-car-btn", function() {
					var weight = $('.m-purchase').find('.weight-ipt').val();
					$("#shopcart").shoppingcart('add', weight,cache.toCartParam);

				});

//				$(document).on("click", ".sub-indent-btn", function() {
//					var resId = $('.m-purchase').attr("data-id"),
//						weight = $('.m-purchase').find('.weight-ipt').val(),
//						maxWeight = $('#dialogContBox').find('.m-purchase').attr('data-maxw'),
//						mobile = $('#dialogContBox').find('.m-purchase').attr('data-mobile'),
//						category = $('#dialogContBox').find('.m-purchase').attr('data-category');
//					$("#shopcart").shoppingcart('singleSubmit', resId, weight, maxWeight, mobile, category);
//				});

				$(".addCar-succ-layer").on("click", ".succ-del", function(event) {
					$(this).closest(".addCar-succ-layer").hide("300");
				});

			}

			function modifyWeight(e, action, sname) {
				var w, weight, id = e.closest('ul').attr('data-id');
				if (action == "add") {
					w =util.clearFormat(e.prev('.cart_item_weight').val());

					weight = rounding(parseFloat(w) + 1);
				} else if (action == "cut") {
					w = util.clearFormat(e.next('.cart_item_weight').val());
					weight = rounding(parseFloat(w) - 1);
				} else if (action == "modify") {
					weight = util.clearFormat(e.val());
				}
				var minWeight = 1;
				weight = weight < minWeight || !verifyWeightFormat(weight) ? minWeight : weight;
				$.fn.shoppingcart('edit', id,util.setFormat(weight,true,true),e);
			}

			function verifyWeightFormat(str) {
				str = str + "";
				if (str.match(/^\+{0,1}\d+(\.\d*)?$/) != null) {
					if (str.match(/^\+{0,1}\d+(\.\d{1,})?$/) != null) {
						return true;
					} else {
						$.fn.shoppingcart('message', '.form-err em', cache.messages.cart.invalidWeightDecimal);
						return false;
					}
				} else {
					$.fn.shoppingcart('message', '.form-err em', cache.messages.cart.invalidWeightFormat);
					return false;
				}
			}

			function verifyWeightValue(weight, stock, category) {
				stock = stock != null && stock != undefined ? stock : 100;
				var max = stock < cache.max_weight ? stock : cache.max_weight;

				//var min = isSpecialCategory(category) ? 0 : cache.mix_weight;
				var min = cache.mix_weight;
				if (weight < min) {
					$.fn.shoppingcart('message', '.form-err em', cache.messages.cart.invalidWeightNotEnough.replace('X', min));
					return false;
				} 
//				else if (weight > max) {
//					$.fn.shoppingcart('message', '.form-err em', cache.messages.cart.invalidWeightOverweight.replace('X', max));
//					return false;
//				} 
				else {
					return true;
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
			function addItem(item) {
				if(item.isChecked=="N"){
					$("input[name='checkedall']").removeAttr("checked")
				}
				var container = em.find('.cart_list');
				container.append('<ul class="ul_content" data-id="'+item.id+'">' +
					'<li class="shop_name f-clrfix relative">' +
					'<label class="fl">' +
					'<input name="" class="fl" type="checkbox" value=""'+(item.isChecked=="Y" ? 'checked="checked"' :"") +'><span class="shop" >'+item.categoryName+'</span><span title="'+item.factoryName+'" class="tips company">'+item.factoryName+'</span></label><span class="amount-p price fr">'+ (item.amount == null ? "议价" : "￥"+util.setFormat(item.amount))+'</span><a class="cart_remove_by_company" data-id="'+item.id+'"style="display: none;">×</a></li>' +
					'<li class="st_name" >' +
					'<p class="f-clrfix">' +
					'<label class="fl">' +
					'<span class="sname">'+item.materialName+'<br>'+getSpec(item)+'</span></label> <span class="price fr relative"><span class="cart_item_price" data-price="'+item.price+'">'+(item.price==null ? "-" : ("￥"+util.setFormat(item.price)+"/吨"))+'</span>' +
					'<br><a class="cart_edit" data-action="cut" style="display: none;">-</a>' +
					'<input type="text" class="cart_item_weight" value="'+util.setFormat(item.weight,true,true)+'">吨 <a class="cart_edit" data-action="add" style="display: none;">+</a></span>' +
					'</p>' +
					'</li>' +
					'</ul>');
			}

			function showMsg(msg) {
				var el = "";

				el += msg;

				util.getDialog(false, el);
			}

			function isSpecialCategory(str) {
				if (str.indexOf(cache.messages.cart.special) >= 0) {
					return true;
				} else {
					return false;
				}
			}

			var methods = {
				isHidden:false,
				setCartHide:function(){
					methods.isHidden = true;
				},
				setSuccHide: function() {

					$('.addCar-succ-layer').hide("100");

				},
				setSucc: function() {
					$(".addCar-succ-layer").show();
					setTimeout(methods.setSuccHide, 3000);
				},
				// Load data
				load: function() {
					em.find('.cart_list').empty();
					//需要做请求去获取列表
					          $.post(cache.base_url + "/api/cart/getcartinfo", {}, function (response, status) {
					              if(response.code == "4013") {
					                  var items = response.data;
					                  if(items.length>0) {
					                      em.find('.set_tit').html('<label class="f-fl"><input name="checkedall" type="checkbox" checked="checked"> 全选</label> <a target="_blank"  href="'+cache.base_url+'/cart/mycart">全屏查看</a>');
					                      em.find('.cart_choose').show();
					                      em.find('.cart_btn').show();
					                      
					                      for(var i in items[0]['resourceList']) {
					                          addItem(items[0]['resourceList'][i]);
					                      }
					                  } else {
					                      em.find('.set_tit').html('<a target="_blank"  href="'+cache.base_url+'/cart/mycart">全屏查看</a>');
					                      em.find('.cart_choose').hide();
					                      em.find('.cart_btn').hide();
					                      
					                     // em.find('.cart_list').html('<span class="block t-c">您没购买任何资源，快去逛逛吧～</span>');
					                      em.find('.cart_list').html('<span class="block t-c no-shop"><span class="cart-logo"></span>您没购买任何资源，快去逛逛吧～</span>');
					                  }
					
					                  setHide();
					                  
					                  $.fn.shoppingcart('refresh');
					              }
					          });
				},
				// Init cart
				init: function() {
					em = $(this);
					setHeader(methods.isHidden);
					setEvents();
					if(!methods.isHidden){
						if($(".cartLink").is(":visible")){
							$.fn.shoppingcart('load');
						}
					}
					
					//$(".tips").tips("bottom");
				},
				// Add item
				add: function(weight, param) {
					if (verifyWeightFormat(weight)) {
						//weight = rounding(parseFloat(weight));
						cache.mix_weight = 1;
						if (verifyWeightValue(weight)) {
							param.weight = weight;
							var config = {
								url : "/api/cart/addcart",
								data:param,
								dataType: "json",
								success:function(data){
									if(data.code == "4008"){
										$.fn.shoppingcart('load');
									 	util.closeDialog();
									 	methods.setSucc();
									}else{
										showMsg(constant.getCodeMsg(data.code));
									}
									 
								}
							}
							util.ajax({config:config});
//							$.post(cache.base_url + "/Home/AddToCart", {
//								resourceID: id,
//								weight: weight
//							}, function(response, status) {
//								var res = $.parseJSON(response);
//								if (res.statusCode == "0") {
//									$.fn.shoppingcart('load');
//									util.closeDialog();
//									methods.setSucc();
//								} else {
//									if (res.message != null && res.message != undefined) {
//										showMsg(res.message);
//									}
//								}
//							});
						}
					}
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
				// Edit weight
				edit: function(id, value,el) {
					if (verifyWeightFormat(value)) {
							$.post(cache.base_url + "/api/cart/updatecart", {
								ids: id,
								weight:value
							}, function(response, status) {
								if (response.code == "4010") {
									var ul = el.closest("ul"), p = ul.find(".cart_item_price").attr("data-price"),v="";
									if(p == null || p =="null"){
										v = "议价";
									}else{
										v = "￥"+util.setFormat(p*value);
									}
									ul.find(".amount-p").html(v);
									el.val(util.setFormat(value,true,true));
									$.fn.shoppingcart('refresh');
								}
							});
					}
				},
				batchRemove: function(ids) {
					var b = true;
					for (var i in ids) {
						$.ajax({
							type: "post",
							url: cache.base_url + "/Home/DelOrder",
							data: {
								shopID: ids[i]
							},
							async: false,
							success: function(response) {
								var res = $.parseJSON(response);

								if (res.statusCode != "0") {
									b = false;
								}
							}
						});
					}

					if (b) {
						$.fn.shoppingcart('load');
					}
				},
				// Remove item
				remove: function(id) {
					$.post(cache.base_url + "/api/cart/delcartbyids", {
						ids: id
					}, function(response, status) {
						//console.log(response)
						//var res = $.parseJSON(response);

//						if (res.statusCode == "0") {
//							$.fn.shoppingcart('load');
//						}
						if(response.code == "4012"){
							$.fn.shoppingcart('load');
						}
					});
				},
				// Get items count
				getCount: function() {
					var count = 0;
					em.find('.shop_name').each(function() {
						if ($(this).find('input[type="checkbox"]').is(':checked')) {
							count = count + 1;
						}
					});

					return count;
				},
				// Get total
				getWeight: function() {
					var count = 0;
					$('.cart_item_weight').each(function(index) {
						var parent = $(this).closest('ul');
						if (parent.find('input[type="checkbox"]').is(':checked')) {
							count = parseFloat($(this).val()) + parseFloat(count);
						}
					});

					return rounding(count);
				},
				// Get cart price
				getPrice: function() {
					var price = 0;

					em.find('.cart_item_price').each(function() {
						var checked = $(this).closest('.ul').find('input').is(':checked');

						if (checked) {
							var w = $(this).parent().find('.cart_item_weight').val(),
								p = $(this).html();
							var count = parseFloat(w) * parseFloat(p);
							price = count + parseFloat(price);
						}
					});

					return rounding(price);
				},
				// refresh
				refresh: function() {
					em.find(".cart_choose").html('<span class="fr">已选 <span class="count">' + util.setFormat($.fn.shoppingcart('getCount')) + '</span> 条，共<span class="count">' + util.setFormat($("#cart").shoppingcart('getWeight'),true,true) + '</span>吨</span> ');

					var count = $.fn.shoppingcart('getCount');
					em.find('.cart_num').css('display', 'block').html(count);
//					if (count > 0) {
//						em.find('.cart_num').css('display', 'block').html(count);
//
//					} else {
//						em.find('.cart_num').css('display', 'none').html(0);
//					}

					
				},
				// show dialog after submit 
				success: function(mobile) {
					var el = "";

					el += '<div class="indent-succ-layer">';
					el += '<div class="indent-succ">';
					el += '<div class="indent-succ-info"><em class="icon text-icon"></em>恭喜您，订单提交成功！</div>';
					el += '<div class="succ-link">';
					el += '<a href="javascript:;" class="dia_continue">继续购买</a>';
					el += '<a target="_blank" href="' + cache.base_url + '/Manage/Buy/Index/Book" class="dia_goto_mycart">查看我的订单</a>';
					el += '</div>';
					el += '<div><span class="red-c">温馨提示：</span>卖家<span class="red-c">' + (mobile != null ? '(' + mobile + ')' : '') + '</span>将在<span class="red-c">30</span>分钟内确认订单，请等待。</div>';
					el += '</div>';
					el += '</div>';

					util.getDialog(false, el);
				},
				// show action dialog
				dialog: function(des, param) {
					cache.toCartParam  = null;
					cache.toCartParam = param;
					var el = "";

					el += '<div class="m-purchase" >';
					el += '       <div class="price-bar">';
					el += '           您要采购的：<span>' + des + '</span>';
					el += '       </div>';
					el += '   <div class="pur-bar">';
					el += '        <div class="item-text weight-form">';
					el += '            采购重量：<input class="weight-ipt" type="text" name="weight" value="1" autocomplete="off"  /><font> 吨</font>';
					el += '            <p class="form-err"><em>  *采购重量不能小于1吨</em></p>';
					el += '        </div>';
					el += '        <div class="pur-btn-bar">';
					//el += '            <button class="sub-indent-btn">提交订单</button>';
					el += '            <button class="add-car-btn">加入购物车</button>';
					el += '        </div>';
					el += '     </div>';
					el += '  </div>';

					util.getDialog(false, el);
				},
				// show warning message
				message: function(e, msg) {
					$(e).html(msg);
				},
				//Not yet open
				notOpenDeal: function() {
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
				// submit a single order
//				singleSubmit: function(resId, weight, maxWeight, mobile, category) {
//					if (verifyWeightFormat(weight)) {
//						weight = rounding(parseFloat(weight));
//						if (verifyWeightValue(weight, maxWeight, category)) {
//							var min = isSpecialCategory(category) ? 0 : cache.mix_weight;
//							var b = min == 0 ? (weight > min) : (weight >= min);
//
//							if (b) {
//								var data = {
//									"resourceID": resId,
//									"weight": weight
//								};
//								$.post(cache.base_url + "/Home/sumbitIndexOrders", data, function(response, status) {
//									var res = $.parseJSON(response);
//
//									if (res.statusCode == 0) {
//										util.closeDialog();
//										$.fn.shoppingcart('success', mobile);
//									} else if (res.statusCode == -3) { /*-3未登录*/
//										util.closeDialog();
//										util.login();
//									} else if (res.statusCode == -6) {
//										$.fn.shoppingcart('notOpenDeal');
//									} else {
//
//										if (res.message != null && res.message != undefined) {
//											showMsg(res.message);
//										}
//									}
//								});
//							}
//						}
//					}
//				},
				// goto my cart page
				submit: function() {
					//location.href = cache.base_url + "/mycart.html";
					window.open(cache.base_url + "/cart/mycart")
				}
			};

			$.fn.shoppingcart = function(method) {
				// Method call logic
				if (methods[method]) {
					return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
				} else if (typeof method === 'object' || !method) {
					return methods.init.apply(this, arguments);
				} else {
					$.error('Method ' + method + ' not exist in jQuery.shoppingcart');
				}
			};
		})($);
});