/*jslint devel: true, plusplus: false, vars: false*/
/*utf-8*/
define(function (require, exports, module) {
    "use strict";
    var $ = require("jquery"), prcsteel = require("prcsteel"),util=require("util"),  cache = require("cache");
	
	if (typeof window.JSON === 'undefined') {
		require("json2");
	}
    var isRefresh = false,autoClick = false,flag = false,isInit = false;
    /**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：根据输入的品名首字母或中文，从品名列表中查询匹配的值，显示在弹出层上
      *@param: 输入参数直接从页面品名输入框获取，不传递参数
      *@return：null
      *
      */
    function showPYMatchList() {
        $("#proComp .product-complete-ul").empty();
        var matchStr = $("#nsortName").val();
		var count = 0;
        $(cache.search_sorts).each(function (i, e) {
            $(e.classInfo).each(function (j, f) {
                $(f.nsort).each(function (k, g) {
                    if (prcsteel.filter(g.nsortName, matchStr)) {
                        $("#proComp .product-complete-ul").append('<li sortId="' + e.sortID + '" nsortId="' + g.nsortID + '"><a href="javascript:;">' + g.nsortName + '</a></li>');
						count++;
                    }
					if (count === 10) {
						return false;
					}
                });
				if (count === 10) {
					return false;
				}
            });
			if (count === 10) {
				return false;
			}
        });
        $("#showLayer_nsortName").hide();
        if ($("#proComp .product-complete-ul li").length > 0) {
			$("#proComp").show();
		} else {
			$("#proComp").hide();
		}
        
        $("#proComp li").click(function () {
            var nsortId = $(this).attr("nsortId");
            var a = $("#showLayer_nsortName div[name='nsort'] a[nsortId='" + nsortId + "']");
			cache.nsort_trigger_flag = false;
			$(a).click();

			var index = $(a).closest("div[name='nsort']").index() - 1;
			
			$("#showLayer_nsortName .product-t-ul li:eq(" + index + ")").trigger('mouseenter');
			$("#proComp .product-complete-ul").empty();
            $("#proComp").hide();
            return false;
        });

    }
	/**
	 *@date:2016-05-28
	 *@Modify-lastdate :2016-06-02
	 *@author :Chengf
	 *@describe：根据输入的材质首字母或中文，从材质列表中查询匹配的值，显示在弹出层上
	 *@param: 输入参数直接从页面品名输入框获取，不传递参数
	 *@return：null
	 *
	 */
	function showMaterialMatchList() {
		$("#materialsHeadMatch .product-complete-ul").empty();
		var matchStr = $("#material").val();
		var count = 0;
		$(cache.temp_material).each(function (j, f) {
			$(f.material).each(function (k, g) {
				if (prcsteel.filter(g.name, matchStr)) {
					$("#materialsHeadMatch .product-complete-ul").append('<li nsortId="' + g.uuid + '"><a href="javascript:;">' + g.name + '</a></li>');
					count++;
				}
				if (count === 10) {
					return false;
				}
			});
			if (count === 10) {
				return false;
			}
		});
		$("#showLayer_material").hide();
		if ($("#materialsHeadMatch .product-complete-ul li").length > 0) {
			$("#materialsHeadMatch").show();
		} else {
			$("#materialsHeadMatch").hide();
		}

		$("#materialsHeadMatch li").click(function () {
			var nsortId = $(this).attr("nsortId");
			var a = $("#showLayer_material").find("a[uuid='" + nsortId + "']");
			cache.nsort_trigger_flag = false;
			$(a).click();
			var index = $("#materialsHeadMatch ul li").has("a.hover").index();
			$("#showLayer_material button.confirm-btn").click();
			$("#materialsHeadMatch .product-complete-ul").empty();
			$("#materialsHeadMatch").hide();
			return false;
			
		});

	}
	/**
	 *@date:2016-05-28
	 *@Modify-lastdate :2016-06-02
	 *@author :Chengf
	 *@describe：根据输入的厂家首字母或中文，从厂家列表中查询匹配的值，显示在弹出层上
	 *@param: 输入参数直接从页面品名输入框获取，不传递参数
	 *@return：null
	 *
	 */
	function showFactoryMatchList() {
		$("#factoryMatch .product-complete-ul").empty();
		var matchStr = $("#factory").val();
		var count = 0;
		cache.factory_cache_arr = [];
		for(var k in cache.factory_cache){
			cache.factory_cache_arr = cache.factory_cache_arr.concat(cache.factory_cache[k]);
		}
		$(cache.factory_cache_arr).each(function (j, f) {
			$(cache.factory_cache_arr[j]).each(function (k, g) {

				if (prcsteel.filter(g.name, matchStr)) {
					$("#factoryMatch .product-complete-ul").append('<li nsortId="' + g.uuid + '"><a href="javascript:;">' + g.name + '</a></li>');
					count++;
				}
				if (count === 10) {
					return false;
				}
			});
			if (count === 10) {
				return false;
			}
		});
		$("#showLayer_factory").hide();
		if ($("#factoryMatch .product-complete-ul li").length > 0) {
			$("#factoryMatch").show();
		} else {
			$("#factoryMatch").hide();
		}

		$("#factoryMatch li").click(function () {
			var nsortId = $(this).attr("nsortid");
			var a = $("#showLayer_factory div[name='fsort'] a[uuid='" + nsortId + "']");
			cache.nsort_trigger_flag = false;
			$(a).click();

			var index = $(a).closest("div[name='fsort']").index() - 1;

			$("#showLayer_factory .product-t-ul li:eq(" + index + ")").trigger('mouseenter');
			$("#showLayer_factory button.confirm-btn").click();
			$("#factoryMatch .product-complete-ul").empty();
			$("#factoryMatch").hide();
			return false;
		});

	}
	/**
	 * load all category materials info (contains name and uuid) for autocomplete search resource
	 *
	 * @author peanut
	 * @date 2015/08/18
	 */
	function loadAllCategoryMaterials(){
		// $.get("api/market/getallcategorymaterials",{},function(data){
		// 	cache.search_sorts = data;
		// });
	}

	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：设置材质的事件。
      *@param: null
      *@return：null
      *
      */
	function enableMaterialEvent(){
		/*材质点击事件*/
		$(document).on("click","#showLayer_material .textures-con a",function () {
            if ($(this).hasClass("hover")) {
                //设置当前选中值
                $(this).removeClass("hover");
            } else {
                //设置当前选中值
                $(this).addClass("hover");
				if ($("#showLayer_material .textures-con a").length === 1) {
					$("#showLayer_material button.confirm-btn").click();
				}
            }
            return false;
        });
		
		//点击材质选择下的确认按钮
		$(document).on("click","#showLayer_material button.confirm-btn",function () {
			var val = "",uuid="";
			$("#showLayer_material .textures-con a.hover").each(function (i, e) {
				val += $(e).text();
				if (i < $("#showLayer_material .textures-con a.hover").length - 1) {
					val += "/";
				}
				uuid += $(e).attr("uuid");
				if (i < $("#showLayer_material .textures-con a.hover").length - 1) {
					uuid += ",";
				}
			});
			$("#material").val(val);
			$("#material-uuid").val(uuid)
			$("#material").blur();
			$("#showLayer_material").hide();
			cache.search_last_click_item = "material";
			cache.search_last_show_layer = $("#showLayer_material").html();
			//setSpecDetail();
			//loadFactory(cache.temp_search_param.nsortID);
			
			$(".form-item").removeClass("focus");
			//getFactory();
			if($("#material-uuid").val() != "" && cache.nsort_trigger_flag){
				$("#showLayer_factory").show();
			}
			if($("#ck_categoryuuid").val() == ""){
				$("#factory").closest(".form-item").addClass("focus");
			}
			if(isInit){
				flag = true;
			}
			if(cache.temp_search_param.material != $("#material-uuid").val()){
				cache.temp_search_param.spec1 = "";
				cache.temp_search_param.spec2 = "";
				cache.temp_search_param.spec3 = "";
				$('#inputspec1').val("");
				$('#inputspec2').val("");
				$('#inputspec3').val("");
				if(cache.temp_search_param.material != null && cache.temp_search_param.material != ""){
					$('#ck_spec1').val("");
					$('#ck_spec2').val("");
					$('#ck_spec3').val("");
					flag = true;
				}
			}
			
			setSpecDetail();

			//$("#inputspec1").click();
			return false;
		});
		
		//点击材质选择下的清除按钮
		$(document).on("click","#showLayer_material button.clear-btn",function () {
			$("#showLayer_material .textures-con a").removeClass("hover");
			$("#material").val("");
			$("#material").blur();
			return false;
		});
	}
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：设置规格1的事件。
      *@param: spec 规格代码 "spec2","spec3", 用户拼接一些组件的id如 "showLayer_spec2"
      *@return：null
      *
      */
    function enableSpec1Event(){
		var spec = "spec1";
		//规格tab点击事件，显示当前tab内容
		$(document).on("mouseover","#showLayer_" + spec + " .standard-t-li",function () {
            $(this).siblings().removeClass("hover");
            $(this).addClass("hover");
            $(this).parents(".show-layer").find("div[type='specDetail']").hide();
            $(this).parents(".show-layer").find("div[type='specDetail']:eq(" + $(this).index() + ")").show();
			return false;
        });
		
        //小分类点击事件
		$(document).on("click","#showLayer_" + spec + " .textures-list-first",function () {
            if ($(this).attr("checked")) {
                $(this).removeAttr("checked");
                $(this).parent().find("a").removeClass("hover");
            } else {
                $(this).attr("checked", "checked");
                $(this).parent().find("a").addClass("hover");
            }
			return false;
        });
        //规格明细点击事件
		$(document).on("click","#showLayer_" + spec + " div[type='specDetail'] a",function () {
            if ($(this).hasClass("hover")) {
                $(this).removeClass("hover");
            } else {
                $(this).addClass("hover");
				if ($("#showLayer_" + spec + " div[type='specDetail'] a").length === 1) {
					$("#" + spec + " button.confirm-btn").click();
				}
            }
			return false;
        });
        //规格1确认按钮事件
		$(document).on("click","#" + spec + " button.confirm-btn",function () {
            var value = "";
            $("#" + spec + " div[type='specDetail'] a.hover").each(function (i, e) {
                value += $(e).text();
                if (i < $("#" + spec + " div[type='specDetail'] a.hover").length - 1) {
                    value +=  "/" ;
                }
            });
            $("#input" + spec).val(value);
            $("#input" + spec).blur();
            $(this).parents(".show-layer").hide();
			
			cache.search_last_click_item = "inputspec1";
			cache.search_last_show_layer = $("#showLayer_spec1").html();
			$("#inputspec1").closest(".form-item").removeClass("focus");
			/*if($("#showLayer_spec2").length>0){
				$("#inputspec2").closest(".form-item").addClass("focus");
				$("#showLayer_spec2").show();
			}*/
			
			//showSearchResult();
            return false;
        });
        //规格1清除按钮事件
		$(document).on("click","#" + spec + " button.clear-btn",function () {
            $("#" + spec + " div[type='specDetail'] a").removeClass("hover");
			$("#input" + spec).val("");
			$("#input" + spec).blur();
            return false;
        });

	}
	
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：设置规格2,3的事件。
      *@param: spec 规格代码 "spec2","spec3", 用户拼接一些组件的id如 "showLayer_spec2"
      *@return：null
      *
      */
    function enableSpec2Spec3Event(spec,spec1){
		//规格明细点击事件
		$(document).on("click","#showLayer_" + spec + " a",function () {
            $(this).parents(".textures-con").find("a").removeClass("hover");
            $(this).addClass("hover");
            $("#input" + spec).val($(this).text());
			$(this).closest(".show-layer").find(".btn-bar input").val("");
            $("#input" + spec).blur();
            $(this).parents(".show-layer").hide();
			cache.search_last_click_item = "input"+spec;
			cache.search_last_show_layer = $("#showLayer_"+spec).html();
			
			$("#input"+spec).closest(".form-item").removeClass("focus");
			if($("#showLayer_"+spec1).length>0){
				$("#input"+spec1).closest(".form-item").addClass("focus");
				$("#showLayer_"+spec1).show();
			}
			
			
            return false;
        });

        //规格2,3确认按钮事件
		$(document).on("click","#showLayer_" + spec + " button.confirm-btn",function () {
            var from = $("#from_" + spec), to = $("#to_" + spec);
            if (prcsteel.checkInput(from, to)) {
                if ($.trim($(from).val()) === "" && $.trim($(to).val()) === "") {
                    //$("#input" + spec).val("");
					$("#showLayer_" + spec).hide();
                } else {
                    var minVal , maxVal;
                    if ($.trim($(from).val()) !== "" && $.trim($(to).val()) !== "") {//起始结束都不为空
                        minVal = $(from).val();
                        maxVal = $(to).val();
                    } else if ($.trim($(from).val()) === "") {// 起始值为空
                        minVal = $("#showLayer_" + spec + " .textures-con a:first").text();
                        maxVal = $(to).val();
                    } else {
                        minVal = $(from).val();
                        maxVal = $("#showLayer_" + spec + " .textures-con a:last").text();
                    }
                    $("#input" + spec).val(minVal + "-" + maxVal);
                    $("#showLayer_" + spec + " a").removeClass("hover");
                    $("#showLayer_" + spec + " a").each(function () {
                        var val = $(this).text() * 1.0;
                        if (val >= minVal && val <= maxVal) {
                            $(this).addClass("hover");
                        }
                    });
					
                }

                $("#input" + spec).blur();
                $(this).parents(".show-layer").hide();
				cache.search_last_click_item = "input"+spec;
				cache.search_last_show_layer = $("#showLayer_"+spec).formhtml();
				$("#input"+spec).closest(".form-item").removeClass("focus");
				if($("#showLayer_"+spec1).length>0){
					$("#input"+spec1).closest(".form-item").addClass("focus");
					$("#showLayer_"+spec1).show();
				}
            }
            return false;
        });
        //规格2,3清除按钮事件
		$(document).on("click","#showLayer_" + spec + " button.clear-btn",function () {
            $("#showLayer_" + spec + " a").removeClass("hover");
            $("#" + spec + " input").val("");
			$("#input" + spec).blur();
            return false;
        });
	}
	
    /**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：选择品名后，显示该品名下的规格列表，如长宽高，但是不包括具体规格下的参数列表
      *@param: 品名ID
      *@return：null
      *
      */
    function showSpecList(nsortId) {
        $(".product-ipt[type='spec']").remove();
        var isReturn = false;
        $(cache.search_sorts).each(function (i, e) {
            $(e.classInfo).each(function (j, f) {
                $(f.nsort).each(function (k, g) {
                    if (g.nsortID == nsortId) {
                        var specNames = g.specName,i=0;
                        $.each(specNames, function (key, value) {
                        	i++;
                            if ($.trim(value) !== "" && value !== null) {
                                var content = '<div class="product-ipt" type="spec" id="' + key + '">'
                                            + ((i == 1 ) ? '<span class="in-block f-fl">' + '规格' + '：</span>' : "")
                                            + '	   <div class="form-item form-item-width">'
                                            + '                <label class="f-label" style="color: rgb(153, 153, 153);">' + (key === 'spec1' ? '可多选' : '单选') + '</label>'
                                            + '                <input type="text" readonly="readonly"  id="input' + key + '" class="f-text sm" value="">'
                                            + '            <span class="icon down-arr-icon"></span>'
                                            + '		</div>'
                                            + '</div>';
                                $(".m-search-bar").append(content);

                                $("#input" + key).inputFocus();
                            }
                        });
                        isReturn = true;
                        return false;
                    }
                });
                if (isReturn) {
                    return false;
                }
            });
            if (isReturn) {
                return false;
            }
        });

        $(".product-ipt[type='spec'] .form-item").click(function (e) {
            $(".show-layer").hide();
            $('.form-item').removeClass("focus");
            if ($.trim($("#nsortId").val()) === "" || $.trim($("#nsortId").val()) === "-9999") {
                $("#nsortName").focus().closest(".form-item").addClass("focus").find(".show-layer").show();
            } else {
               // setSpecDetail();
                $(this).parent().addClass("focus").find('.show-layer').show();
            }
        });
    }
    /**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：根据选择的品名和材质，查询对应的规格列表明细数据，并加载到对应规格，当点击规格框的时候，弹出相应数据层
      *@param: 输入参数直接从页面品名和材质输入框获取，不传递参数
      *@return：null
      *
      */
    function setSpecDetail() {
        var nsortId = $("#nsortId").val(), material = $("#material-uuid").val();
        //查询条件未更改，不查询
//      if (cache.temp_nsort_for_showspec == nsortId && cache.temp_search_param.material == material) {
//          return false;
//      }
        util.post("/api/market/getspec",{nosrtUUID:nsortId,materialUUID:material},loadSpec)
        //记录下查询条件
        cache.temp_search_param.material = material;
        cache.temp_nsort_for_showspec = nsortId;
    }
    //加载规格数据
	function loadSpec(data) {
        if (data.code == "13003") {
        	cache.temp_spec = {"spec1List":[],"spec2List":[],"spec3List":[]};
        	if(data.data != null){
        		cache.temp_spec = data.data;
        	}
			$(".product-ipt[type='spec']").each(function (i, e) {
				var spec = $(this).attr("id"),
					specList = cache.temp_spec[spec + 'List'],
					rowSize = 6,
					colSize = 3,
					pageSize = rowSize * colSize,
					page = 0;
				if (specList.length === 0) {
					page = 1;
				} else {
					page = Math.floor((specList.length - 1) / pageSize) + 1;
				}
				//spec1 多选处理
				if (i === 0) {
					setSpec1(specList, spec, page, pageSize, rowSize);
				} else {
					//spec2,3 单选，区间输入处理
					setSpec2Spec3(specList, rowSize, spec, i);
				}
			});
//			if($("#ck_categoryuuid").val() == ""){
//				$("#showLayer_spec1").show();
//			}
			if($("#ck_categoryuuid").val() != "" && isInit){
				//规格1
				var spec1 = $("#ck_spec1").val();
				$("#showLayer_spec1 .textures-con a").removeClass("hover");
				$("#inputspec1").val("");
				if (spec1 !== undefined && spec1 !== "") {
					var arr = spec1.split(",");
					$(arr).each(function(i, e){
						$("#showLayer_spec1 .textures-con a").each(function(j, f){
							if($(f).text() * 1.0 === e * 1.0){
								$(f).click();
							}
						});
					});
					$("#showLayer_spec1 button.confirm-btn").click();
					$("#inputspec1").val(arr.join("/"));
				}
				$("#inputspec1").blur();
				$(".form-item").removeClass("focus");
				$(".show-layer").hide();
				
				
				//规格2
				
				var spec2 = $("#ck_spec2").val(),minSpec2="",maxSpec2="";
				$("#showLayer_spec2 .textures-con a").removeClass("hover");
				$("#inputspec2").val("");
				$("#from_spec2").val("");
				$("#to_spec2").val("");
				if(spec2.indexOf("-")>0){
					var temp = spec2.split("-");
					minSpec2 = temp[0];
					maxSpec2 = temp[1];
					spec2 = "";
				}
				if (spec2 !== undefined && spec2 !== "") {
					var arr = spec2.split(",");
					$(arr).each(function(i, e){
						$("#showLayer_spec2 .textures-con a").each(function(j, f){
							if ($(f).text() * 1.0 === e * 1.0) {
								$(f).click();
							}
						});
					});
					$("#inputspec2").val(spec2);
					$("#inputspec2").keydown();
					$(".show-layer").hide();
				} else {
					if (minSpec2 !== undefined && minSpec2 !== "") {
						$("#from_spec2").val(minSpec2);
					}
					if(maxSpec2 !== undefined && maxSpec2 !== ""){
						$("#to_spec2").val(maxSpec2);
					}
					$("#showLayer_spec2 button.confirm-btn").click();
					
				}
				$(".form-item").removeClass("focus");
				
					//规格3
				var spec3 = $("#ck_spec3").val(),minSpec3="",maxSpec3="";
				$("#showLayer_spec3 .textures-con a").removeClass("hover");
				$("#inputspec3").val("");
				$("#from_spec3").val("");
				$("#to_spec3").val("");
				if(spec3.indexOf("-")>0){
					var temp = spec3.split("-");
					minSpec3 = temp[0];
					maxSpec3 = temp[1];
					spec2 = "";
				}
				if (spec3 !== undefined&&spec3 !== "") {
					var arr = spec3.split(",");
					$(arr).each(function(i, e){
						$("#showLayer_spec3 .textures-con a").each(function(j, f){
							if($(f).text()*1.0 === e*1.0){
								$(f).click();
							}
						});
					});
					$("#inputspec3").val(spec3);
					$("#inputspec3").keydown();
					$(".show-layer").hide();
				} else {
					if (minSpec3 !== undefined&&minSpec3 !== "") {
						$("#from_spec3").val(minSpec3);
					}
					if (maxSpec3 !== undefined&&maxSpec3 !== "") {
						$("#to_spec3").val(maxSpec3);
					}
					$("#showLayer_spec3 button.confirm-btn").click();
				}
				$(".form-item").removeClass("focus");
				
				
			}
			if(($("#ck_factory").val() == "") || cache.temp_search_param.material != null && cache.temp_search_param.material != "" && flag){
				if(isInit){
					isInit = false;
				}else{
					if(cache.nsort_trigger_flag){
						$("#showLayer_factory").show();
					}
					cache.nsort_trigger_flag = true;
					flag = false;
					isInit = false;
				}
				
			}
        } else {
			cache.temp_spec = null;
            alert("加载规格发生错误");
		}
   };
    /**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：设置规格1的参数列表，之所以不同规格要分开设置是因为规格1和2,3的处理方式是不一样的。
      *@param: specList:该规格下的明细数据，spec：规格弹层的名称参数，page：分几页（tab）显示, pageSize：一页显示多少数据，rowSize：一行显示多少数据 
      *@return：null
      *
      */
    function setSpec1(specList, spec, page, pageSize, rowSize) {
        var content = '<!--规格数据层  S-->'
                + '<div class="show-layer wid445 textures-bar standard-bar none" id="showLayer_' + spec + '">'
                + '<div class="standard-t f-clr-r">'
                + '   <ul class="standard-t-ul">',
            specDetail = "";
		if(specList.length>0){
			var p;
			for (p = 0; p < page; p++) {
				var from = p * pageSize,
					to = (from + pageSize) > specList.length ? specList.length - 1 : (from + pageSize - 1);
				content += '<li class="standard-t-li ' + (p === 0 ? 'hover' : '') + '"><a href="javascript:;">' + specList[from].spec + '-' + specList[to].spec + '</a>'
						 + '    <em class="icon down-redArr-icon"></em>'
						 + '</li>';
				specDetail += '<div class="textures-con f-clrfix" type="specDetail">';
				var q;
				for (q = from; q <= to; q++) {

					if (q % rowSize === 0) {
						specDetail += ' <div class="textures-con-bar-list bder-b-dashed">';
						if (q + rowSize < to) {
							specDetail += '<span class="textures-list-first bold">' + specList[q].spec + '-' + specList[q + rowSize - 1].spec + '</span>';
						} else {
							specDetail += '<span class="textures-list-first bold">' + specList[q].spec + '-' + specList[to].spec + '</span>';
						}
					}
					specDetail += '<span><a href="javascript:;">' + specList[q].spec + '</a></span>';
					if ((q + 1) % rowSize === 0 || (q + 1) === specList.length) {

						specDetail += '</div>';
					}
				}
				specDetail += ' <div class="f-clr-l"></div>';
				specDetail += '</div>';
			}
		} else {
			specDetail = "你所选择的品类材质组合下没有资源";
		}
        
        content += '    </ul>'
                + '    <a class="layer-del f-fr"></a>'
                + '</div>';
        content += specDetail;
        content += '<div class="btn-bar">'
                + '    <button class="clear-btn">清除</button>'
                + '    <button class="confirm-btn">确认</button>'
                + '</div>'
                + '<!--规格数据层  E--></div>';
        $("#showLayer_" + spec).remove();
        $("#" + spec + " .form-item").append(content);
        //只显示第一个规格tab，其他tab隐藏
        $("#" + spec + " .form-item div[type='specDetail']:gt(0)").hide();

    }
	
	
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：设置规格2,3的参数列表，之所以不同规格要分开设置是因为规格1和2,3的处理方式是不一样的。
      *@param: specList:该规格下的明细数据，spec：规格弹层的名称参数，rowSize：一行显示多少数据
      *@return：null
      *
      */
    function setSpec2Spec3(specList, rowSize, spec) {
        var classes = "";
        if (spec === "spec3") {
            classes = "show-layer breadth-bar extent-bar  none";
        } else {
            classes = "show-layer textures-bar breadth-bar standard-bar none";
        }
        var content = '<div class="' + classes + '" id="showLayer_' + spec + '">'
                   + '<div class="textures-con f-clrfix ">';
		if(specList.length > 0){
			$(specList).each(function (j, e) {
				if (j % rowSize === 0) {
					content += ' <div class="textures-con-bar-list bder-b-dashed">';
				}
				content += '<span><a href="javascript:;">' + specList[j].spec + '</a></span>';
				if ((j + 1) % rowSize === 0 || (j + 1) === specList.length) {
					content += '</div>';
				}
			});
		} else {
			content += "你所选择的品类材质组合下没有资源";
		}
        
        content += '<div class="f-clr-l"></div>'
               + ' </div>'
               + ' <div class="btn-bar t-l">'
               + '     <span class="s-gray">范围：</span>'
               + '     <input class="range-ipt" type="text" value="" id="from_' + spec + '" /><em class="dash-line">—</em><input class="range-ipt" type="text" value="" id="to_' + spec + '" />'
               + '     <button class="clear-btn">清除</button>'
               + '     <button class="confirm-btn">确认</button>'
               + ' </div>'
               + '</div>';
        $("#showLayer_" + spec).remove();
        $("#" + spec + " .form-item").append(content);
    }
    /**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：加载大类品名数据, 缓存到本地js变量中
      *@param: null
      *@return：null
      *
      */
    function loadSorts() {
    	util.post("/api/market/getsortandnsort",{},loadSortName);
   	};
	function loadSortName(data){
		if (data.code === "13001") {
				parseSortData(data)
				$("div[name='nsort']:gt(0)").hide();
				if($("#ck_categoryname").val()!=""){
					$("#nsortName").val($("#ck_categoryname").val());
				}
				$("#nsortName").focus().blur();
				if($("#ck_categoryuuid").val() !=""){
					initSortParam();
				}
				
            } else {
				cache.search_sorts = null;
				$(".m-s").hide();
            }
	};
	function parseSortData(data){
		cache.search_sorts = data.data;
		$(cache.search_sorts).each(function (i, e) {
			//卷板，型材 横向列表
			var li = '<li class="product-t-li ' + (i === 0 ? 'hover' : '') + '"><a href="javascript:;" sortId="' + e.sortID + '">' + e.sortName + '</a>'
					+ '<em class="icon down-redArr-icon"></em></li>';
			$("#showLayer_nsortName .product-t .product-t-ul").append(li);//'<a href="javascript:void(0)" sortId="'+e.sortID+'"><font>'+e.sortName+'</font></a>');
			//以下：品名明细，包括 热轧+品名
			//最大的包裹div
			var classDiv = $('<div name="nsort"/>');
			$(e.classInfo).each(function (j, f) {
				var nsort = $('<div class="product-con f-clrfix bder-b-dashed"/>');
				//热轧，冷轧
				var className = $('<div class="product-con-sort f-fl"><span class="bold">' + (f.className === "" ? "" : f.className + ":") + '</span></div>');
				//过滤掉被设置成不显示的品名
				$(f.nsort).each(function (k, g) {
					var arr = [];
					if(g.nsortIsEcShow !== 'true'){
						arr.push(k);
					}
					while(arr.length != 0){
						f.nsort.splice(arr.pop(), 1);
					}
				});
				if (f.nsort.length > 0) {
					//品名列表最外层div
					var nsortDiv = $('<div class="product-con-bar f-fl"/>'),
						rowSize = 5,
						nsortPkg = "";
					$(f.nsort).each(function (k, g) {
						//每N行换行div
						if (k % rowSize === 0) {
							nsortPkg += '<div class="product-con-bar-list">';
						}
						nsortPkg += '<span><a nsortId="' + g.nsortID + '" href="javascript:;">' + g.nsortName + '</a></span>';
						if ((k + 1) % rowSize === 0 || (k + 1) === f.nsort.length) {
							nsortPkg += '</div>';
						}
					});
					$(nsortDiv).append($(nsortPkg));
					$(nsort).append(className);
					$(nsort).append(nsortDiv);
				}
				$(classDiv).append(nsort);
			});
			$("#showLayer_nsortName").append(classDiv);
			
		});
	};
    
	
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：根据品名加载对应厂家
      *@param: null，参数从cache.temp_search_param.nsortID取，为选择了的品名
      *@return：null
      *
      */
    function loadFactory(nsortId) {
		util.post("/api/market/getfactory",{nosrtUUID:nsortId},loadFactoryCallback)
   };
    function loadFactoryCallback(data){
    	//"13004"
    	if(data.code == "13004"){
    		cache.factory_cache = data.data;
    		getFactory();
    	}
    };
    //获取厂家列表UI展示
    function getFactory(){
    	if(cache.factory_cache != null){
    		var data = formatData(cache.factory_cache);
    		$("#showLayer_factory .product-b").empty();
    		$("#showLayer_factory .product-t .product-t-ul").empty();
    		$(data).each(function (i, e) {
			//厂家首字母
			var li = '<li class="product-f-li sm ' + (i === 0 ? 'hover' : '') + '"><a href="javascript:;" >' + e.sortName + '</a>'
					+ '<em class="icon down-redArr-icon"></em></li>';
			$("#showLayer_factory .product-t .product-t-ul").append(li);//'<a href="javascript:void(0)" sortId="'+e.sortID+'"><font>'+e.sortName+'</font></a>');
			//以下：厂家列表明细
			//最大的包裹div
			var classDiv = $('<div name="fsort"/>');
			$(e.sortInfo).each(function (j, f) {
					var nsort = $('<div class="product-con f-clrfix bder-b-dashed"/>');
					//热轧，冷轧
					var className = $('<div class="product-con-sort f-fl"><span class="bold">' + (f.className === "" ? "" : f.className + ":") + '</span></div>');
	
					if (f.list.length > 0) {
						//厂家列表最外层div
						var nsortDiv = $('<div class="product-con-bar f-fl"/>'),
							rowSize = 5,
							nsortPkg = "";
						$(f.list).each(function (k, g) {
							//每N行换行div
							if (k % rowSize === 0) {
								nsortPkg += '<div class="factory-con-bar-list">';
							}
							nsortPkg += '<span><a uuid="' + g.uuid + '" href="javascript:;">' + g.name + '</a></span>';
							if ((k + 1) % rowSize === 0 || (k + 1) === f.list.length) {
								nsortPkg += '</div>';
							}
						});
						$(nsortDiv).append($(nsortPkg));
						$(nsort).append(className);
						$(nsort).append(nsortDiv);
					}
					$(classDiv).append(nsort);
				});
				$("#showLayer_factory .product-b").append(classDiv);
				
			});
			
			$("div[name='fsort']:gt(0)").hide();
			$("div[name='fsort']:eq(0)").show();
//			if($("#ck_categoryuuid").val() == ""){
//				$("#showLayer_factory").show();
//			}
			if($("#ck_categoryuuid").val() !=""){//只有当页面搜索后才会触发，默认不会触发
				//厂家
				var factorys = $("#ck_factory").val();
				var tempArr = factorys.split(",");
//				$("#showLayer_material .textures-con a").removeClass("hover");
//				$("#material").val("");
				if (factorys !== undefined&&factorys !== "") {
					var arr = factorys.split(",");
					$(arr).each(function(i, e){
						$("#showLayer_factory .factory-con-bar-list a").each(function(j, f){
							if ($(f).attr("uuid") === e) {
								$(f).click();
							}
						});
						//var sort = $("#showLayer_factory .product-t-ul li a[sortID*='" + $(f) + "']");
						//$(sort).parent().trigger('mouseenter');
						
					});
					$("#showLayer_factory button.confirm-btn").click();
					//$("#material").val(arr.join("/"));
					var index = $("#showLayer_factory .factory-con-bar-list a[uuid='"+arr[0]+"']").closest("div[name='fsort']").index();
					$("#showLayer_factory .product-t-ul li a").eq(index).parent().trigger('mouseenter');
					$(".form-item").removeClass("focus");
				}
				
				
				
				$("#factory").blur();
				$(".show-layer").hide();
				
			}
		
    	}
    };
    function formatData(data){
    	var k ,index=0,j=0,rtn= [],keyArr=[],valArr = [],i;
    	for(k in data){
    		index++;
    		keyArr.push(k);
    		valArr.push(data[k]);
    	}
    	
    	var kl = keyArr.length,vl = valArr.length ;
    	var obj = {},rtnLen=Math.floor(kl/4),lastlen = kl%4;
    	for(i = 0;i<rtnLen;i++){
    		var sortName = "",sortInfo=[],tempObj = {};
    		for(var m=4*i;m<4*(i+1);m++){
    			sortName+=keyArr[m];
    			tempObj.className =  keyArr[m];
    			tempObj.list = valArr[m];
    			sortInfo.push(tempObj);
    			tempObj = {};
    			if(lastlen == 1 && m == rtnLen*4-1){
    				sortName+=keyArr[m+1];
	    			tempObj.className =  keyArr[m+1];
	    			tempObj.list = valArr[m+1];
	    			sortInfo.push(tempObj);
    			}
    			tempObj = {};
    		}
    		obj.sortName = sortName;
    		sortName = "";
    		obj.sortInfo = sortInfo;
    		sortInfo = []
    		rtn.push(obj);
    		obj = {};
    	}
    	
    	if(lastlen>1 || (rtnLen == 0 && lastlen == 1)){
    		var sortName = "",sortInfo=[],tempObj = {};
    		for(i = rtnLen*4;i<lastlen+rtnLen*4;i++){
    			sortName+=keyArr[i];
    			tempObj.className =  keyArr[i];
    			tempObj.list = valArr[i];
    			sortInfo.push(tempObj);
    			tempObj = {};
    		}
    		obj.sortName = sortName;
    		sortName = "";
    		obj.sortInfo = sortInfo;
    		sortInfo = []
    		rtn.push(obj);
    		obj = {};
    	}
    	return rtn;
    };
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：绑定区域模块事件
      *@param: null
      *@return：null
      *
      */
	function enableAreaButton() {
		//区域Tab点击事件
		$(document).on("click",".area-bar-com",function () {
			$('.form-item').removeClass("focus");
			$(".show-layer").hide();
			$(".area-layer").show();
		});
		//区域城市点击事件
		$(document).on("click",".area-layer .textures-con a",function () {
			if ($(this).is(".area-layer a:last")) {
				$(".area-bar-com a").text("全国");
				$(".area-layer a").removeClass("hover");
				$(this).addClass("hover");
				//$(".show-layer").hide();
				//cache.search_last_click_item = "area";
				//cache.search_last_show_layer = $(".area-layer").html();
				//showSearchResult();
			} else {
				$(".area-layer a:last").removeClass("hover");
				if ($(this).hasClass("hover")) {
					$(this).removeClass("hover");
				} else {
					$(this).addClass("hover");
				}
			}
			//标记区域更改
			cache.search_area_changed_flag = true;
			return false;
		});
		//区域内确定按钮点击事件
		$(document).on("click",".area-layer button.confirm-btn",function () {
			cache.search_last_click_item = "area";
			cache.search_last_show_layer = $(".area-layer").html();
			//区域
			var cityIdList = "",
	            isSelected2 = "",
	            other = "",
	            provinceIdList = [];
				
			$(".area-layer .product-con-bar:not(:last) a.hover").each(function(i, e) {
	            cityIdList += $(this).attr("cityId") + ",";
				var provinceId = $(this).attr("provinceId");
				if ($.inArray(provinceId, provinceIdList) === -1) {
					provinceIdList.push(provinceId);
				}
				if ($(this).attr("isSelected") === "2") {
					isSelected2 += $(this).text() +  "/";
				} else {
					other += $(this).text() +  "/";
				}
			});
			if (cityIdList !== "") {
				cityIdList = cityIdList.slice(0, -1);
			}
			
			var cityNameList = isSelected2 + other;
			if (cityNameList === "") {
				cityNameList = "全国";
			} else {
				cityNameList = cityNameList.slice(0, -1);
			}
			$(".area-bar-com a").text(cityNameList);
			$(".area-bar-com a").attr("title",cityNameList);
			if(!validateForm()){
				$(".area-layer").hide();
				return ;
			}
			showSearchResult();
			return false;
		});
		//区域内清除按钮点击事件
		$(document).on("click",".area-layer button.clear-btn",function () {
			$(".area-layer a").removeClass("hover");
			cache.temp_search_param.cityID = "";
			return false;
		});
		//小分类点击事件
		$(document).on("click",".area-bar .product-con-sort",function () {
			if ($(this).is(".area-bar .product-con-sort:last")) {
				
			} else {
				$(".area-bar .product-con-bar a:last").removeClass("hover");
				if ($(this).attr("checked")) {
					$(this).removeAttr("checked");
					$(this).parent().find("a").removeClass("hover");
				} else {
					$(this).attr("checked", "checked");
					$(this).parent().find("a").addClass("hover");
				}
			}
			return false;
        });
		//关闭按钮
		$(document).on("click",".layer-del",function () {
			cancelSelection(true);
			return false;
		});
	}
	
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：绑定厂家模块事件
      *@param: null
      *@return：null
      *
      */
	function enableFactoryButton() {
		var t;
		$(document).on("mouseover",".product-f-li" ,
			function () {
				var tab = this;
				t = setTimeout(function () {
					$(tab).parent().find('li').removeClass('hover');
					$(tab).addClass('hover');
					$('div[name="fsort"]').hide();
					$('div[name="fsort"]:eq(' + ($(tab).index()) + ')').show();
					return false;
				}, 200);
			}
		);
		$(document).on("mouseout",".product-f-li" ,
			function () {
				clearTimeout(t);
			}
		);
		//厂家Tab点击事件
		$(document).on("click",".factory-bar-com",function () {
			$('.form-item').removeClass("focus");
			$(".show-layer").hide();
			$(".factory-layer").show();
			
		});
		//厂家点击事件
		$(document).on("click",".factory-con-bar-list a",function () {
			if ($(this).hasClass("hover")) {
				$(this).removeClass("hover");
			} else {
				$(this).addClass("hover");
			}
			return false;
		});
		//厂家内确定按钮点击事件
		$(document).on("click",".factory-layer button.confirm-btn",function () {
			var val = "",uuid="";
			$("#showLayer_factory .factory-con-bar-list a.hover").each(function (i, e) {
				val += $(e).text();
				if (i < $("#showLayer_factory .factory-con-bar-list a.hover").length - 1) {
					val += "/";
				}
				uuid += $(e).attr("uuid");
				if (i < $("#showLayer_factory .factory-con-bar-list a.hover").length - 1) {
					uuid += ",";
				}
			});
			$("#factory").val(val);
			$("#factory-uuid").val(uuid);
			$("#factory").blur();
			$("#showLayer_factory").hide();
			cache.search_last_click_item = "factory";
			cache.search_last_show_layer = $("#showLayer_factory").html();
			//setSpecDetail();
			$("#factory").closest(".form-item").removeClass("focus");
			if(cache.nsort_trigger_flag){
				$("#inputspec1").closest(".form-item").addClass("focus");
				$("#showLayer_spec1").show();
			}
			cache.nsort_trigger_flag = true;
			//showSearchResult();
			
			return false;
		});
		
		//厂家内清除按钮点击事件
		$(document).on("click",".factory-layer button.clear-btn",function () {
			$(".factory-layer a").removeClass("hover");
			$("#factory").val("");
			$("#factory").blur();
			//cache.temp_search_param.factory = "";
			return false;
		});
	}
	
	//鼠标点击弹窗外，弹窗消失
	$(document).click(function (event) {
		var target = event.target;
		var type = "";
		if($(target).closest(".form-item").length === 1) {
			target = $(target).closest(".form-item").find("input:eq(0)");
			type = "formItem";
		}
		if($(target).attr("id") === "material"){
			if(cache.search_temp_isNsortChanged){
				validatNsort();
				cache.search_temp_isNsortChanged = false;
			}
			
			type = "material";
		} else if ($(target).attr("id") === "inputspec1"){
			if(cache.search_temp_isNsortChanged){
				validatNsort();
				cache.search_temp_isNsortChanged = false;
			}
			type = "inputspec1";
		} else if ($(target).attr("id") === "inputspec2"){
			if(cache.search_temp_isNsortChanged){
				validatNsort();
				cache.search_temp_isNsortChanged = false;
			}
			type = "inputspec2";
		} else if ($(target).attr("id") === "inputspec3"){
			if(cache.search_temp_isNsortChanged){
				validatNsort();
				cache.search_temp_isNsortChanged = false;
			}
			type = "inputspec3";
		} else if ($(target).closest(".area-bar-com").length === 1){
			type = "area";
		} else if ($(target).closest(".factory-bar-com").length === 1){
			type = "factory";
		} else if ($(target).closest(".order-bar-com").length === 1){
			type = "order";
		} else if ($(target).closest(".show-layer").length === 1){
			type = "showLayer";
		}
		
		if (type !== "") {
			//点在弹出层空白区域
			if(type === "showLayer" || type === "order") {
				return false;
			}
			if(cache.search_last_click_item){
				var isSameClick = (type === "material" && cache.search_last_click_item === "material")||
								  (type === "inputspec1" && cache.search_last_click_item === "inputspec1")||
								  (type === "inputspec2" && cache.search_last_click_item === "inputspec2")||
								  (type === "inputspec3" && cache.search_last_click_item === "inputspec3")||
								  (type === "area" && cache.search_last_click_item === "area")||
								  (type === "factory" && cache.search_last_click_item === "factory");
				if(isSameClick){
					return false;
				}else{
					cancelSelection();
				}
			}
			
			if(type === "material"){
				cache.search_last_click_item = "material";
				cache.search_last_show_layer = $("#showLayer_material").html();
				return false;
			}else if(type === "inputspec1"){
				cache.search_last_click_item = "inputspec1";
				cache.search_last_show_layer = $("#showLayer_spec1").html();
				return false;
			}else if(type === "inputspec2"){
				cache.search_last_click_item = "inputspec2";
				cache.search_last_show_layer = $("#showLayer_spec2").html();
				return false;
			}else if(type === "inputspec3"){
				cache.search_last_click_item = "inputspec3";
				cache.search_last_show_layer = $("#showLayer_spec3").html();
				return false;
			}else if(type === "area"){
				cache.search_last_click_item = "area";
				cache.search_last_show_layer = $(".area-layer").html();
				return false;
			}else if(type === "factory"){
				cache.search_last_click_item = "factory";
				cache.search_last_show_layer = $(".factory-layer").html();
				return false;
			}
		}else{
			if($(".show-layer:visible").length > 0){
				cancelSelection(true);
			}
		}
    });
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：取消之前的弹层选择操作，如点击了某些选项但是没有确定。
      *@param: hideLayer, 是否关闭弹窗
      *@return：null
      *
      */
	function cancelSelection(hideLayer){
		if (cache.search_last_click_item === "material") {
			$("#showLayer_material").html(cache.search_last_show_layer);
		} else if (cache.search_last_click_item === "inputspec1") {
			$("#showLayer_spec1").html(cache.search_last_show_layer);
		} else if (cache.search_last_click_item === "inputspec2") {
			$("#showLayer_spec2").html(cache.search_last_show_layer);
		} else if (cache.search_last_click_item === "inputspec3") {
			$("#showLayer_spec3").html(cache.search_last_show_layer);
		} else if (cache.search_last_click_item === "area") {
			$(".area-layer").html(cache.search_last_show_layer);
			
		} else if (cache.search_last_click_item === "factory") {
			$(".factory-layer").html(cache.search_last_show_layer);
		}
		if(hideLayer){
			$(".show-layer").hide();
			$("#proComp").hide();
			$(".form-item").removeClass("focus");
		}
	}
    /**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：加载指定品名下的材质数据
      *@param: nsortId：品名ID
      *@return：null
      *
      */
    function setMertial(nsortId) {
        if (cache.temp_search_param.nsortID === nsortId) {
            return false;
        }
        $("#material").empty();
        
        util.post("/api/market/getmaterial",{nosrtUUID:nsortId},loadMaterial);
		//记住上次查询的品名,以免重复发送ajax请求
		cache.temp_search_param.nsortID = nsortId;
    };
    //加载材质数据
    function loadMaterial(data) {
        if (data.code === "13002") {
            cache.temp_material = data.data;
			$("#showLayer_material .textures-con").empty();
			var rowSize = 5, content = "";
			if (cache.temp_material === null || cache.temp_material.length === 0) {
				$("#showLayer_material .textures-con").text("你所选择的品类下没有资源");
			}
			$(cache.temp_material).each(function (i, e) {
				if (i % rowSize === 0) {
					content += '<div class="textures-con-bar-list bder-b-dashed">';
				}
				content += '<span><a href="javascript:;" uuid='+e.material.uuid+'>' + e.material.name + '</a></span>';
				if ((i + 1) % rowSize === 0 || (i + 1) === cache.temp_material.length) {
					content += '</div>';
				}
			});
			$("#showLayer_material .textures-con").append($(content));
			if($("#ck_categoryuuid").val() !=""){//只有当页面搜索后才会触发，默认不会触发
				//材质
				var material = $("#ck_materialuuid").val()
				$("#showLayer_material .textures-con a").removeClass("hover");
				$("#material").val("");
				if (material !== undefined&&material !== "") {
					var arr = material.split(",");
					$(arr).each(function(i, e){
						$("#showLayer_material .textures-con a").each(function(j, f){
							if ($(f).attr("uuid") === e) {
								$(f).click();
							}
						});
					});
					$("#showLayer_material button.confirm-btn").click();
					//$("#material").val(arr.join("/"));
					///获取规格信息
					//setSpecDetail();
				}
				$("#material").blur();
				$(".show-layer").hide();
			}
			if($("#ck_categoryuuid").val() =="" && cache.nsort_trigger_flag){
				$("#showLayer_material").show();
			}
			
        } else {
			cache.temp_material = null;
            alert("加载材质发生错误");
		}
		cache.nsort_trigger_flag = true;
        cache.search_last_click_item = "material";
		cache.search_last_show_layer = $("#showLayer_material").html();
     };
	
	function inNsortList(sortName){
		var nsortId = -9999;
		$(cache.search_sorts).each(function (i, e) {
            $(e.classInfo).each(function (j, f) {
                $(f.nsort).each(function (k, g) {
                    if ($.trim(sortName) == g.nsortName) {
                        nsortId = g.nsortID;
                    }
					if (nsortId!=-9999) {
						return false;
					}
                });
				if (nsortId!=-9999) {
					return false;
				}
            });
			if (nsortId!=-9999) {
				return false;
			}
        });
		return nsortId;
	}
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：在本地记录查询参数
      *@param: null
      *@return：null
      *
      */
    function setParam() {
		var nsortName = $.trim($("#nsortName").val());
		if (nsortName !== "" && inNsortList(nsortName)==-9999){
			$("#sortId").val("-9999");
			$("#sortName").val("无此品名");
			$("#nsortId").val("-9999");
		}else if(nsortName === "" && !cache.search_temp_isInit){
			$("#sortId").val("");
			$("#sortName").val("");
		}

		var sortName = $("#sortName").val(),
			sortId = $("#sortId").val(),
            nsortId = $("#nsortId").val(),
            //可多选，用“，”隔开
            material = $("#material-uuid").val(),
            //spec1//可多选，用“，”隔开
            spec1 = "",
            spec1Input = $("#inputspec1").val();
        if (spec1Input) {
           // spec1 = spec1Input.replaceAll("/", ",");
           spec1 = spec1Input.replaceAll("/", ",")
        }
      
        var spec2 = "", 
            spec2Input = $("#inputspec2").val();
        spec2 = spec2Input;

		var spec3 = "", 
            spec3Input = $("#inputspec3").val();
            spec3 = spec3Input;

		//区域
		var cityIdList = "",
            isSelected2 = "",
            other = "",
            provinceIdList = [];
			
		$(".area-layer .product-con-bar:not(:last) a.hover").each(function(i, e) {
            cityIdList += $(this).attr("cityId") + ",";
			var provinceId = $(this).attr("provinceId");
			if ($.inArray(provinceId, provinceIdList) === -1) {
				provinceIdList.push(provinceId);
			}
			if ($(this).attr("isSelected") === "2") {
				isSelected2 += $(this).text() +  "/";
			} else {
				other += $(this).text() +  "/";
			}
		});
		if (cityIdList !== "") {
			cityIdList = cityIdList.slice(0, -1);
		}
		
		var cityNameList = isSelected2 + other;
		if (cityNameList === "") {
			cityNameList = "全国";
		} else {
			cityNameList = cityNameList.slice(0, -1);
		}
		$(".area-bar-com a").text(cityNameList);
		$(".area-bar-com a").attr("title",cityNameList);
		//厂家
		var factoryList = "";
		var factoryListShow = "";
		$(".factory-layer .textures-con a.hover").each(function (i, e) {
			//factoryList += $(this).text();
			factoryListShow += $(this).text();
			if (i !== $(".factory-layer .textures-con a.hover").length - 1) {
				//factoryList += ",";
				factoryListShow += "/";
			}
		});
		factoryList = $("#factory-uuid").val();
		if (factoryListShow === "") {
			factoryListShow = "厂家";
		}
		var showFactoryListShow = factoryListShow.split("/").length > 1 ? factoryListShow.split("/")[0] + "..." : factoryListShow.split("/")[0];
		$(".factory-bar-com a").text(showFactoryListShow);
		//分页
		var pageIndex = $(".page-num-ipt").val();
		pageIndex =  parseInt(pageIndex,10);
		if(pageIndex == NaN){
			pageIndex = 1;
		}
		//保存查询参数,查询时直接传递cache.temp_search_param作为参数
		cache.temp_search_param.sortID = sortId;
		cache.temp_search_param.sortName = sortName;
		cache.temp_search_param.nsortID = nsortId;
		cache.temp_search_param.nsortName = nsortName;
		cache.temp_search_param.material = material;
		cache.temp_search_param.spec1 = spec1;
		cache.temp_search_param.spec2 = spec2;
		cache.temp_search_param.spec3 = spec3;
		cache.temp_search_param.cityID = cityIdList;
		//cache.temp_search_param.cityName = cityNameList === "全国" ? "" : cityNameList;
		cache.temp_search_param.provinceID = provinceIdList.join(",");
		cache.temp_search_param.factory = factoryList;
		cache.temp_search_param.pageIndex = pageIndex;
//		var str = $.getSearchUrlVar('str')
//		if(cache.search_temp_isInit && str !== undefined){
//			str = decodeURIComponent(str);
//			cache.temp_search_param.str = str;
//		}else{
//			cache.temp_search_param.str = "";
//		}
		
	}
	
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：处理登陆用户的最近搜索和猜你喜欢数据
      *@param: 
      *@return：null
      *
      */
	function searchHistory(){
		util.post("/api/search/getRecentSearchList",{},historyCallback);
	};
	function preferCallback(data){
		if(data.code == "10005"){
			cache.search_search_history.commonSearches = data.data;
			//showCommonSearchList(transform(data.data));.
			showCommonSearchList(data.data);
		}
	};
	function historyCallback(data){
		$(".prefer-resoult span").remove();
		if(data.code == "10002"){
			cache.search_search_history.recentSearches = data.data;
			//showRecentSearchList(transform(data.data));
			showRecentSearchList(data.data);
			util.post("/api/search/getSearchPreferenceList",{},preferCallback);		
		}
	};
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：显示从服务器拉取的登陆用户"猜你喜欢"数据
      *@param: com ,猜你喜欢搜索条件列表
      *@return：null
      *
      */
	function showCommonSearchList(com){
		if (com === undefined || com === null || com.length === 0) {
			$(".prefer-resoult dl:eq(1)").hide();
			$("#pre-common").html("");
			return false;
		}
		var content = "";
		if (com.length > 0) {
			$(".prefer-resoult dl:eq(1)").show().parent().show();
		}
        $(com).each(function(i, e) {
			if (!e) {
				return false;
			}
			var item = "";
			item += e.categoryName;
			if (e.materialName !== null && e.materialName !== "") {
				if(item!="") item += "/" ;
				 item+= e.materialName;
			}
			if (e.spec1 !== null && e.spec1 !== "" && e.spec1 !== "null") {
				if(item!="") item += "/" ;
				item +=  e.spec1;
			}
			if (e.spec2 !== null && e.spec2 !== "" && e.spec2 !== "-" && e.spec2 !== "null"){
				if(item!="") item += "/" ;
				item += e.spec2;
			}
//			if (e.minSpec2 !== null && e.minSpec2 !== ""){
//				if(item!="") item += "/" ;
//				item += e.minSpec2 + "~" + e.maxSpec2;
//			}
			if (e.spec3 !== null &&e.spec3 !== "" && e.spec3 !== "-" && e.spec3 !== "null") {
				if(item!="") item += "/" ;
				item += e.spec3;
			}
//			if (e.minSpec3 !== null && e.minSpec3!=="") {
//				if(item!="") item += "/" ;
//				item += e.minSpec3 + "~" + e.maxSpec3;
//			}
			if (e.cityName !== null && e.cityName!=="") {
				if(item!="") item += "/" ;
				item += e.cityName;
				
			}
			if(e.factoryName !== null && e.factoryName !== ""){
				if(item!="") item += "/" ;
				item += e.factoryName;
			}
			content += '<span><a type="show" href="javascript:;" title="' + item + '">' + item + '</a><a class="del" href="javascript:;" title="删除"></a></span>';
		});
        $("#pre-common").html(content);
		
	};
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：显示从服务器拉取的登陆用户"最近搜索"数据
      *@param: com ,最近搜索条件列表
      *@return：null
      *
      */
	function showRecentSearchList(com) {
		if (com === undefined || com === null || com.length === 0) {
			return false;
		}
		var content = "";
		if (com.length>0) {
			$(".prefer-resoult dl:eq(0)").show().parent().show();
		}
        $(com).each(function(i, e) {
			if (!e) {
				return false;
			}
			var item = "";
			item += e.categoryName;
			if (e.materialName !== null && e.materialName !== "") {
				if(item!="") item += "/" ;
				 item+= e.materialName;
			}
			if (e.spec1 !== null && e.spec1 !== "") {
				if(item!="") item += "/" ;
				item +=  e.spec1;
			}
			if (e.spec2 !== null && e.spec2 !== "" && e.spec2 !== "-"){
				if(item!="") item += "/" ;
				item += e.spec2;
			}
//			if (e.minSpec2 !== null && e.minSpec2 !== ""){
//				if(item!="") item += "/" ;
//				item += e.minSpec2 + "~" + e.maxSpec2;
//			}
			if (e.spec3 !== null &&e.spec3 !== "" && e.spec3 !== "-") {
				if(item!="") item += "/" ;
				item += e.spec3;
			}
//			if (e.minSpec3 !== null && e.minSpec3!=="") {
//				if(item!="") item += "/" ;
//				item += e.minSpec3 + "~" + e.maxSpec3;
//			}
			if (e.cityName !== null && e.cityName!=="") {
				if(item!="") item += "/" ;
				item += e.cityName;
				
			}
			if(e.factoryName !== null && e.factoryName !== ""){
				if(item!="") item += "/" ;
				item += e.factoryName;
			}
			content += '<span><a type="show" href="javascript:;" title="' + item + '">' + item + '</a><a class="del" href="javascript:;" title="删除"></a></span>';
		});
        $("#pre-lately").html(content);
		
		
	}
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：将从服务器拉取的搜索记录数据转化成本地格式，服务器上会把minSpec2=111,maxSpec2=222变成spec2=111-222。
      *@param: com, 服务器上的格式
      *@return：转化后的格式
      *
      */
	function transform(com){
		$(com).each(function(i, e){
			var spec2 = e.spec2;
			if (spec2.indexOf("-") > -1) {
				var min = spec2.split("-")[0];
				var max = spec2.split("-")[1];
				e["minSpec2"] = min;
				e["maxSpec2"] = max;
				e.spec2 = "";
			}else{
				e["minSpec2"] = "";
				e["maxSpec2"] = "";
			}
			var spec3 = e.spec3;
			if (spec3.indexOf("-") > -1) {
				var min = spec3.split("-")[0];
				var max = spec3.split("-")[1];
				e["minSpec3"] = min;
				e["maxSpec3"] = max;
				e.spec3 = ""
			} else{
				e["minSpec3"] = "";
				e["maxSpec3"] = "";
			}
		});
		return com;
	};
	
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：根据URL参数初始化选择项
      *@param: ${nsortID, material, spec1, spec2, minSpec2, maxSpec2, spec3, minSpec3, maxSpec3, cityID, factory, priceOrder}
      *@return：null
      *
      */
	function initParam(sortID, nsortName, nsortID, material, spec1, spec2, spec3, cityID, factory, priceOrder) {
		var url = "";
    	
    	if(nsortID!= undefined && nsortID!=null && nsortID !=""){
    		url +="categoryuuid_"+nsortID;
    	}
    	if(nsortName!= undefined && nsortName!=null && nsortName !=""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="categoryname_"+encodeURIComponent(encodeURIComponent(nsortName));
    	}
//  	if(sortID!= undefined && sortID!=null && sortID != ""){
//  		if(url.length>0){
//  			url +="_";
//  		}
//  		url +="sortid_"+sortID;
//  	}
    	if(material!= undefined && material!=null && material != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="material_"+material ;
    	}
    	if(factory!= undefined && factory!=null && factory != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="factory_"+factory ;
    	}
    	if(spec1!= undefined && spec1!=null && spec1 != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="spec1_"+spec1 ;
    	}
    	if(spec2!= undefined && spec2!=null && spec2 != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="spec2_"+spec2 ;
    	}
    	if(spec3!= undefined && spec3!=null && spec3 != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="spec3_"+spec3 ;
    	}
    	if(priceOrder!= undefined && priceOrder!=null && priceOrder != ""){//0 asc ;1 desc
    		if(url.length>0){
    			url +="_";
    		}
    		url +="orderway_"+priceOrder ;
    	}
    	if(cityID!= undefined && cityID!=null && cityID != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="city_"+cityID ;
    	}
    	window.location.href = cache.base_url+"/market/"+url;
	}
	
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：根据参数查询资源列表数据，并显示在界面上
      *@param: 查询参数从缓存变量cache.temp_search_param中取
      *@return：null
      *
      */
    function showSearchResult() {
		
		$(".form-item").blur();
		
		//$(".show-layer").hide();
		//$("#m-page").hide();
		$(".search_notice").hide();
		
	    setParam();
	    getURL();

   };
   //获取拼接URL地址
    function getURL(){
    	var url = "";
    	
    	if(cache.temp_search_param.nsortID !=""){
    		url +="categoryuuid_"+cache.temp_search_param.nsortID;
    	}
    	if(cache.temp_search_param.nsortName !=""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="categoryname_" + encodeURIComponent(encodeURIComponent(cache.temp_search_param.nsortName));;
    	}
//  	if(cache.temp_search_param.sortID != ""){
//  		if(url.length>0){
//  			url +="_";
//  		}
//  		url +="sortid_"+cache.temp_search_param.sortID;
//  	}
    	if(cache.temp_search_param.material != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="material_"+cache.temp_search_param.material ;
    	}
    	if(cache.temp_search_param.factory != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="factory_"+cache.temp_search_param.factory ;
    	}
    	if(cache.temp_search_param.spec1 != undefined && cache.temp_search_param.spec1 != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="spec1_"+cache.temp_search_param.spec1 ;
    	}
    	if(cache.temp_search_param.spec2 != undefined && cache.temp_search_param.spec2 != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="spec2_"+cache.temp_search_param.spec2 ;
    	}
    	if(cache.temp_search_param.spec3 != undefined && cache.temp_search_param.spec3 != ""){
    		if(url.length>0){
    			url +="_";
    		}
    		url +="spec3_"+cache.temp_search_param.spec3 ;
    	}
    	if(cache.temp_search_param.priceOrder != undefined && cache.temp_search_param.priceOrder != ""){//0 asc ;1 desc
    		if(url.length>0){
    			url +="_";
    		}
    		url +="orderway_"+cache.temp_search_param.priceOrder ;
    	}
		if(cache.temp_search_param.cityID != undefined){
			if(cache.temp_search_param.cityID == ""){
				cache.temp_search_param.cityID ="0";
			}
    		if(url.length>0){
    			url +="_";
    		}
    		url +="city_"+cache.temp_search_param.cityID ;
    	}
    	if(cache.temp_search_param.pageIndex != undefined && cache.temp_search_param.pageIndex != "" && cache.temp_search_param.pageIndex > 1){//0 asc ;1 desc
    		if(url.length>0){
    			url +="/";
    		}
    		url +="list_"+cache.temp_search_param.pageIndex ;
    	}
    	window.location.href = cache.base_url+"/market/"+url;
    };
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：首页数据加载初始化
      *@param: null
      *@return：null
      *
      */
	function init(){
		if($("#ck_categoryuuid").val()!="" && $("#ck_materialuuid").val()!="" ){
			isInit = true;
		}
		//启用初始化事件
		enableInitEvents();
		enableEvent();
		/*加载初始化数据*/
		loadSorts();
		var v = $("#ck_orderway").val() == "ASC" ? 0 : 1;	
		$(".order-bar-com a").text($(".bder-b-dashed[v='"+v+"']").text());
		var citys = $("#ck_city").val(),text="";
		if(citys!="" && citys !="0"){
			$(".area-layer a").removeClass("hover");
			var tempArr = citys.split(",");
			$.each(tempArr, function(i,e) {
				$(".area-layer a[cityid='"+e+"']").addClass("hover");
			});	
			$.each(tempArr, function(i,e) {
				
				text +=$(".area-layer a[cityid='"+e+"']").text()+"/";
			});
			text = text.substring(0,text.length-1);
			$(".area-bar-com a").text(text);
		}
		if(window.location.href.indexOf("categoryuuid")>0){
			$(window).scrollTop(239);
		}
		
		//initParam(sortID, "", nsortID, material, spec1, spec2, spec3,  cityID, factory, priceOrder);
			
	};
	function enableEvent(){
		$(".page-num-ipt").keydown(function(e){
//			if(e.keyCode == "13"){
//				if($(this).val() == ""){
//					return ;
//				}
//				showSearchResult();
//			}
		});
		$(".page-num-btn").bind("click",function(){
			if($(".page-num-ipt").val() == ""){
				return ;
			}
			var url = window.location.href;
			//if(url)
			if(url.indexOf("categoryuuid")>0){
				showSearchResult();
			}else{
				var pageIndex = $(".page-num-ipt").val();
				pageIndex =  parseInt(pageIndex,10);
				if(pageIndex == NaN){
					pageIndex = 1;
				}
				if(url.indexOf("list_")>0){
					url = url.split("/list_")[0];
				}
				window.location.href = url+"/list_"+pageIndex;
			}
			
		})
	};
	function initMaterialSearchParam (material){
		var categoryuuid = $("#ck_categoryuuid").val();
		setMertial(categoryuuid);
	};
	//初始化搜索条件
	function initSortParam(){
		var categoryuuid = $("#ck_categoryuuid").val(),
			categoryname = $("#ck_categoryname").val(),
			materialuuid = $("#ck_materialuuid").val(),
			factory = $("#ck_factory").val();
			
		var href= window.location.href;
		var sordId =href.split("sortid_").length >1 ?  href.split("sortid_")[1].split("_material")[0] :"" ;
		//首先根据sortid处理品名初始化
		if(categoryuuid !== undefined && categoryuuid !== ""){
//			var sort = $("#showLayer_nsortName .product-t-ul li a[sortID='" + sordId + "']");
//			$("#sortId").val(sordId);
//			$("#sortName").val($(sort).text());
//			$(sort).parent().trigger('mouseenter');
			var index = ($("#showLayer_nsortName span a[nsortId='"+categoryuuid+"']").closest("div[name='nsort']").index()-1);
			var sort = $("#showLayer_nsortName .product-t-ul li :eq("+index+")");
			var sortId = sort.children("a").attr("sortid");
			$("#sortId").val(sordId);
			$("#sortName").val(sort.text());
			$(sort).parent().trigger('mouseenter');
		}
		if (categoryuuid !== undefined && categoryuuid !== "" ) {
			if(categoryuuid != -9999){
				autoClick = true;
				$("#showLayer_nsortName span a[nsortId='"+categoryuuid+"']").click();
				$("#showLayer_nsortName div[name='nsort']").css("display","none");
				$("#showLayer_nsortName span a[nsortId='"+categoryuuid+"']").closest("div[name='nsort']").css("display","block");
				$(".form-item").removeClass("focus");
				//isRefresh = false;
			}else{
				$("#nsortId").val(categoryuuid);
				$("#nsortName").val(categoryname);
				$("#showLayer_nsortName .product-t-ul li").removeClass("hover");
				$("#showLayer_nsortName .product-con-bar-list a").removeClass("hover");
			}
			$(".show-layer").hide();
		} else {
			if(!cache.search_temp_isInit){
				$("#sortId").val("");
				$("#sortName").val("");
				
				$("#nsortId").val("");
				$("#nsortName").val("");
			}
			
			
		}
		initMaterialSearchParam(materialuuid);
		
	};
	function validateForm(){
		if(cache.temp_search_param.sortID == "" || $("#nsortName").val() ==""){
			setTimeout(function(){
				$(".err-c").children(".info").text("请选择需购买的品名").parent().show();
				$("#nsortName").closest(".form-item").addClass("focus");
			},100)
			return false;
		}
//		if($("#material-uuid").val() == "" || $("#material").val() ==""){
//			setTimeout(function(){
//				$(".err-c").children(".info").text("请选择需购买的材质").parent().show();
//				$("#material").closest(".form-item").addClass("focus");
//			},100)
//			return false;
//		}
//		if($("#factory-uuid").val() == "" || $("#factory").val() ==""){
//			setTimeout(function(){
//				$(".err-c").children(".info").text("请选择需购买的厂家").parent().show();
//				$("#factory").closest(".form-item").addClass("focus");
//			},100)
//			return false;
//		}
		return  true;
	};
	
	function enableInitEvents(){
		//输入框获取失去焦点事件,不能统一写，因为label值会合并导致显示错误
		$('#nsortName').inputFocus();
		$('#material').inputFocus();
		$('#factory').inputFocus();
		$('#inputspec1').inputFocus();
		$('#inputspec2').inputFocus();
		$('#inputspec3').inputFocus();


		//厂家模糊匹配
		$('#factory').bind('input propertychange', function () {
			var matchStr = $("#factory").val();
			if ($.trim(matchStr) === "") {
				$("#showLayer_factory li").removeClass("hover");
				$("#showLayer_factory a").removeClass("hover");
				$("#factoryMatch").hide();

				$(".factory-layer .textures-con").empty();
				$(".factory-bar").hide();

				$(".show-layer").hide();
				$("#showLayer_factory").show();
				return false;
			}

			showFactoryMatchList();
		});
		//键盘选择模糊匹配结果
		$('#factory').keydown(function (event) {
			if ($("#factoryMatch ul li").length > 0) {
				var index;
				if (event.keyCode === 38) {
					index = $("#factoryMatch ul li").has("a.hover").index();
					$("#factoryMatch ul li:eq(" + index + ") a").removeClass("hover");
					if (index === -1 || index === 0) {
						index = $("#factoryMatch ul li").length - 1;
					} else {
						index--;
					}
					$("#factoryMatch ul li:eq(" + index + ") a").addClass("hover");
				} else if (event.keyCode === 40) {
					index = $("#factoryMatch ul li").has("a.hover").index();
					$("#factoryMatch ul li:eq(" + index + ") a").removeClass("hover");
					if (index === -1 || index === $("#factoryMatch ul li").length - 1) {
						index = 0;
					} else {
						index++;
					}
					$("#factoryMatch ul li:eq(" + index + ") a").addClass("hover");
				} else if (event.keyCode === 13) {
					index = $("#factoryMatch ul li").has("a.hover").index();
					if (index > -1) {
						var uuid = $("#factoryMatch ul li").has("a.hover").attr("nsortid");
						var a = $("#showLayer_factory div[name='fsort'] a[uuid='" + uuid + "']");
						cache.nsort_trigger_flag = false;
						$(a).click();
						$("#showLayer_factory button.confirm-btn").click();
						$("#factoryMatch .product-complete-ul").empty();
						$("#factoryMatch").hide();
						return false;
					}
				}
			}
			// if (event.keyCode === 9) {
			// 	validatNsort();
			// }
		});
		//材质模糊匹配
		$('#material').bind('input propertychange', function () {
			var matchStr = $("#material").val();
			if ($.trim(matchStr) === "") {
				$("#showLayer_material li").removeClass("hover");
				$("#showLayer_material a").removeClass("hover");
				$("#materialsHeadMatch").hide();

				$(".factory-layer .textures-con").empty();
				$(".factory-bar").hide();

				$(".show-layer").hide();
				$("#showLayer_material").show();
				return false;
			}

			showMaterialMatchList();
		});
		//键盘选择模糊匹配结果
		$('#material').keydown(function (event) {
			if ($("#materialsHeadMatch ul li").length > 0) {
				var index;
				if (event.keyCode === 38) {
					index = $("#materialsHeadMatch ul li").has("a.hover").index();
					$("#materialsHeadMatch ul li:eq(" + index + ") a").removeClass("hover");
					if (index === -1 || index === 0) {
						index = $("#materialsHeadMatch ul li").length - 1;
					} else {
						index--;
					}
					$("#materialsHeadMatch ul li:eq(" + index + ") a").addClass("hover");
				} else if (event.keyCode === 40) {
					index = $("#materialsHeadMatch ul li").has("a.hover").index();
					$("#materialsHeadMatch ul li:eq(" + index + ") a").removeClass("hover");
					if (index === -1 || index === $("#materialsHeadMatch ul li").length - 1) {
						index = 0;
					} else {
						index++;
					}
					$("#materialsHeadMatch ul li:eq(" + index + ") a").addClass("hover");
				} else if (event.keyCode === 13) {
					index = $("#materialsHeadMatch ul li").has("a.hover").index();
					if (index > -1) {
						var uuid = $("#materialsHeadMatch ul li").has("a.hover").attr("nsortId");
						cache.nsort_trigger_flag = false;
						$("#showLayer_material .textures-con a[uuid='" + uuid + "']").click();
						$("#showLayer_material button.confirm-btn").click();
						$("#materialsHeadMatch .product-complete-ul").empty();
						$("#materialsHeadMatch").hide();
						return false;
					}
				}
			}
			// if (event.keyCode === 9) {
			// 	validatNsort();
			// }
		});
		//模糊匹配
		$('#nsortName').bind('input propertychange', function () {
			var matchStr = $("#nsortName").val();
			if ($.trim(matchStr) === "") {
				$(".m-search-bar input:gt(0)").val("").blur();
				$("#showLayer_nsortName li").removeClass("hover");
				$("#showLayer_nsortName a").removeClass("hover");
				$("#proComp").hide();
				cache.temp_search_param.nsortID = "";
				cache.temp_search_param.nsortName = "";
				cache.temp_search_param.sortID = "";
				cache.temp_search_param.sortName = "";
				$("#showLayer_material .textures-con").text("");
				$("#showLayer_spec1 .textures-con").text("");
				$("#showLayer_spec2 .textures-con").text("");
				$("#showLayer_spec3 .textures-con").text("");
				$(".factory-layer .textures-con").empty();
				$(".factory-bar").hide();
				
				$(".show-layer").hide();
				$("#showLayer_nsortName").show();
				return false;
			}
			
			showPYMatchList();
		});
		//键盘选择模糊匹配结果
		$('#nsortName').keydown(function (event) {
			if ($("#proComp ul li").length > 0) {
				var index;
				if (event.keyCode === 38) {
					index = $("#proComp ul li").has("a.hover").index();
					$("#proComp ul li:eq(" + index + ") a").removeClass("hover");
					if (index === -1 || index === 0) {
						index = $("#proComp ul li").length - 1;
					} else {
						index--;
					}
					$("#proComp ul li:eq(" + index + ") a").addClass("hover");
				} else if (event.keyCode === 40) {
					index = $("#proComp ul li").has("a.hover").index();
					$("#proComp ul li:eq(" + index + ") a").removeClass("hover");
					if (index === -1 || index === $("#proComp ul li").length - 1) {
						index = 0;
					} else {
						index++;
					}
					$("#proComp ul li:eq(" + index + ") a").addClass("hover");
				} else if (event.keyCode === 13) {
					index = $("#proComp ul li").has("a.hover").index();
					if (index > -1) {
						var nsortId = $("#proComp ul li").has("a.hover").attr("nsortId");
						cache.nsort_trigger_flag = false;
						$("#showLayer_nsortName div[name='nsort'] a[nsortId='" + nsortId + "']").click();
						$("#proComp .product-complete-ul").empty();
						$("#proComp").hide();
						return false;
					}
				}
			}
			if (event.keyCode === 9) {
				validatNsort();
			}
		});
		//搜索按钮
		$("#searchBtn").click(function () {
			if(!validateForm()){
				return ;
			}
			$(".err-c").hide();
			validatNsort();
			showSearchResult();
		});
		
		//鼠标滑过大类，显示大类下的小类
		var t;
		$(document).on("mouseover",".product-t-li" ,
			function () {
				var tab = this;
				t = setTimeout(function () {
					$(tab).parent().find('li').removeClass('hover');
					$(tab).addClass('hover');
					$('div[name="nsort"]').hide();
					$('div[name="nsort"]:eq(' + ($(tab).index()) + ')').show();
					return false;
				}, 200);
			}
		);
		$(document).on("mouseout",".product-t-li" ,
			function () {
				clearTimeout(t);
			}
		);
		
		//点击品名
		$(document).on("click",".product-con-bar-list a",function () {
			
			if(!autoClick){
				$("#ck_categoryuuid").val("");
			}
			if(autoClick){
				autoClick =false;
			}
			//设置品名id值，隐藏域
			var nsortId = $(this).attr('nsortid');
			//设置品名name值
			var nsortName = $(this).text();
			$("#nsortName").val(nsortName);
			//手动触发事件
			$("#nsortName").keydown();
			//是否和上次品名一致，一致不要重复发请求
			if(nsortId !== cache.temp_search_param.nsortID){
				
				//新一轮品名选择，清空其下选择（材质规格）
				$(".m-search-bar input[id!='nsortName']").val("");
				
				
				//$("#nsortName").inputFocus();
				//设置大类id值，隐藏域
				var index = $(this).closest("div[name='nsort']").index() - 1;
				var a = $("#showLayer_nsortName .product-t-ul li:eq(" + index + ") a");
				var sortId = $(a).attr("sortId");
				
				$("#sortId").val(sortId);
				//设置大类Name值，隐藏域
				var sortName = $(a).text();
				$("#sortName").val(sortName);
				
				$("#nsortId").val(nsortId);
				//品名id放到材质加载后去设置，因为涉及到材质的二次加载
				cache.temp_search_param.sortID = sortId;
				cache.temp_search_param.sortName = sortName;
				cache.temp_search_param.nsortName = nsortName;
				
				//去掉之前的选择
				$("#showLayer_nsortName span a").removeClass("hover");
				//设置当前选中值
				$(this).addClass("hover");
				//加载厂家
				loadFactory(nsortId);
				//加载材质数据
				setMertial(nsortId);
				//加载规格列表（非规格明细）
				showSpecList(nsortId);
				
				$("#material").val("");
				$("#material").inputFocus();
			}
			
			//隐藏品名弹出层
			$("#showLayer_nsortName").hide();
			$("#proComp").hide();
			//材质获取焦点
			$(".form-item").removeClass("focus");
			$("#material").closest(".form-item").addClass("focus");
			//显示材质弹出层
			if(cache.nsort_trigger_flag) {
				$("#showLayer_material").show();
			}

			return false;
		});
		
		/*input点击事件*/
		$("#m-search").on("click",".form-item",function () {
			if ($("#proComp").is(":hidden")) {
				$(".show-layer").hide();
				$('.form-item').removeClass("focus");
				if ($.trim($("#nsortId").val()) === "" || $.trim($("#nsortId").val()) === "-9999") {
					$("#nsortName").focus().closest(".form-item").addClass("focus").find(".show-layer").show();
				} else {
					$(this).addClass("focus").find(".show-layer").show();
					if($("#ck_categoryuuid").val()!=""){
						$("div[name='nsort']").hide();
						var el = $("#showLayer_nsortName span a[nsortId='"+$("#ck_categoryuuid").val()+"']").closest("div[name='nsort']");
						el.css("display","block");
						$("#showLayer_nsortName li").removeClass("hover");
						$("#showLayer_nsortName li:eq("+(el.index()-1)+")").addClass("hover");
					}
//					if($("#ck_factory").val()!=""){
//						var uuid = $("#ck_factory").val().split(",")[0];
//						$("div[name='fsort']").hide();
//						var el = $("#showLayer_factory .product-b a[uuid='"+uuid+"']").closest("div[name='fsort']");
//						el.show();
//						$("#showLayer_factory .product-t-ul li").removeClass("hover");
//						$("#showLayer_factory .product-t-ul li a").eq(el.index()).parent().addClass("hover");
//						
//						//$("#showLayer_factory .product-t-ul li a").eq(el.index()).parent().trigger('mouseenter');
//						//$("#showLayer_factory button.confirm-btn").click();
//						
//					}
					
				}
			}
		});
		
		//价格排序框点击事件
		$(".order-bar-com").click(function () {
			$(".show-layer").hide();
			$(".order-bar-ul").show();
		});
		
		$(".order-bar-ul li").click(function () {
			if(!validateForm()){
				$(".order-bar-ul").hide();
				return ;
			}
			//价格排序 True：从高到低  False：从低到高
			cache.temp_search_param.priceOrder = $(this).attr("v");
			$(".order-bar-com a").text($(this).find("a").text());
			$(".order-bar-ul").hide();
			showSearchResult();
		});
		
		$(document).on("click","#pre-lately a[type='show']",function() {
			var param = [];
			
			param = cache.search_search_history.recentSearches[$(this).parent().index()];
			cache.search_click_history = true;
			initParam("", param.categoryName, param.categoryUuid, param.materialUuid, param.spec1, param.spec2,param.spec3,  param.cityId, param.factoryId, 0);
			
		});
		//删除一个最近搜索
		$(document).on("click","#pre-lately a.del",function() {
			var el = $(this);
			var index = el.parent().index();
			
			var id = cache.search_search_history.recentSearches[index].id;
			if(id !== undefined){//如果有记录号，则发送请求给服务端（逻辑）删除该记录
				util.post("/api/search/delById",{id:id},function(data){
					if(data.code == "10003"){
						cache.search_search_history.recentSearches.splice(index, 1);
						el.parent().remove();
						//没有最近搜索就不显示
						if($("#pre-lately span").length === 0){
							$("#pre-lately").parent().hide();
							if ($("#pre-common span").length === 0) {
								$(".prefer-resoult").hide();
							}
						}
					}
					
				})
			}
			
		});
		$(document).on("click","#pre-common a[type='show']",function() {
			cache.search_click_history = true;
			var param = cache.search_search_history.commonSearches[$(this).parent().index()];
			
			initParam("", param.categoryName, param.categoryUuid, param.materialUuid, param.spec1, param.spec2,param.spec3,  param.cityId, param.factoryId, 0);
			
		})
		//删除一个猜你喜欢
		$(document).on("click","#pre-common a.del",function() {
			var el = $(this);
			var index = el.parent().index();
			//var com = cache.search_search_history.commonSearches;
			var id = cache.search_search_history.commonSearches[index].id;
			if (id !== undefined) {//如果有记录号，则发送请求给服务端（逻辑）删除该记录
				util.post("/api/search/delSearchPreference",{id:id},function(data){
					if(data.code == "10006"){
//						el.parent().remove();
//						//没有猜你喜欢就不显示
//						if($("#pre-common span").length === 0){
//							$("#pre-common").parent().hide();
//							if($("#pre-lately span").length === 0){
//								$(".prefer-resoult").hide();
//							}
//						}
						util.post("/api/search/getSearchPreferenceList",{},preferCallback);		
					}
					
				})
			}
			
			//com.splice(index, 1);
			
			
		});
		enableMaterialEvent();
		enableSpec1Event();
		enableSpec2Spec3Event("spec2","spec3");
		enableSpec2Spec3Event("spec3");
		enableAreaButton();
		enableFactoryButton();
		
	}
	
	$("#nsortName").change(function(){
		cache.search_temp_isNsortChanged = true;
	});
	
	function validatNsort() {		
		var nsortName = $("#nsortName").val();
		if($.trim(nsortName)==""){
			$("#sortId").val("");
			$("#sortName").val("");
			$("#nsortId").val("");
			return false;
		}
		var nsortId = inNsortList(nsortName);
		if(nsortId == -9999){
			$(".m-search-bar input:gt(0)").val("").blur();
			$("#showLayer_nsortName li").removeClass("hover");
			$("#showLayer_nsortName a").removeClass("hover");
			$("#proComp").hide();
			cache.temp_search_param.nsortID = -9999;
			$("#sortId").val("-9999");
			$("#sortName").val("无此品名");
			$("#nsortId").val("-9999");
		
			$("#showLayer_material .textures-con").text("");
			$("#showLayer_spec1 .textures-con").text("");
			$("#showLayer_spec2 .textures-con").text("");
			$("#showLayer_spec3 .textures-con").text("");
			$(".factory-layer .textures-con").empty();
			$(".factory-bar").hide();
			
			$(".show-layer").hide();
			$("#showLayer_nsortName").show();
			return false;
		}else{
			$(".show-layer").hide();
			var a = $("#showLayer_nsortName div[name='nsort'] a[nsortId='" + nsortId + "']");
			$(a).click();
			//$("#material").closest(".form-item").removeClass("focus");
			var index = $(a).closest("div[name='nsort']").index() - 1;
			$("#showLayer_nsortName .product-t-ul li:eq(" + index + ")").trigger('mouseenter');
		}
	};
	/**
      *@date:2016-05-28
      *@Modify-lastdate :2016-06-02
      *@author :Chengf
      *@describe：设置登陆用户信息
      *@param: null 
      *@return：null
      *
      */
	
	//暴露对外方法
    module.exports = {
		init : init
    };
	
	$(document).ready(function(){
		//加载最近搜索和猜你喜欢,之所以在最后才加载是因为如果之前点击退出刷新本页的话，退出需要一定时间来缓冲
//		if ($("#nsortId").val() === "") {
//			setTimeout(function(){searchHistory()},1500);
//			//searchHistory();
//		}
		searchHistory();
	});
});
