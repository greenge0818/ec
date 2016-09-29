/*jslint devel: true, plusplus: false, vars: false*/
/*utf-8*/
define(function (require, exports, module) {
    "use strict";
    var $ = require("jquery"),util=require("util"), cache = require("cache"), prcsteel = require("prcsteel");

    if (typeof window.JSON === 'undefined') {
        require("json2");
    }
	function validateForm(){
		if($("#nsortName").val() ==""){
			setTimeout(function(){
				$(".err-c").children(".info").text("请输入需购买的钢材").parent().show();
				//$("#nsortName").closest(".form-item").addClass("focus");
			},100)
			return false;
		}
		return  true;
	};
    /**
     *@date:2015-05-05
     *@Modify-lastdate :2015-05-13
     *@author :Green.Ge
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
            if (prcsteel.filter(e.categoryName+" "+ e.materialsName, matchStr) || prcsteel.filter(e.materialsName+" "+ e.categoryName, matchStr)) {
                $("#proComp .product-complete-ul").append('<li style="white-space: nowrap;" sortId="' + e.categoryUuid + '" materialsUuid="'+e.materialsUuid+'"><a href="javascript:;">' + e.categoryName +' '+ e.materialsName+'</a></li>');
                count++;
            }
            if (count === 5) {
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
            var v=$(this).find("a").html()
            $("#nsortName").val(v);
            $("#proComp").hide();

            //保存查询参数,查询时直接传递cache.temp_search_param作为参数
            cache.temp_search_param.sortID = $(this).attr("sortId");
            cache.temp_search_param.sortName = v.split(" ")[0];
            cache.temp_search_param.material = $(this).attr("materialsuuid");
            getURL();
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
        $.get(cache.base_url+"/api/market/getallcategorymaterials",{},function(data){
            cache.search_sorts = data;
        });
    }

    $("#nsortName").on("focus",function(){
        $(".err-c").hide();
    });

    //鼠标点击弹窗外，弹窗消失
    $(document).click(function (event) {
        var target = event.target;
        if($(target).attr("id") != "proComp"){
            $("#proComp").hide();
        }
    });

    function inNsortList(v){
        if(!v){
            return false;
        }
        var array=v.split(" ");
        if(array.length < 1){
            return false;
        }
        //品名名称
        var categoryName = array[0];
        //材质名称
        var materialsName = array[1];
        if(cache.search_sorts){
            $(cache.search_sorts).each(function (i, e) {
                if(categoryName == e.categoryName ){
                    cache.temp_search_param.sortID=e.categoryUuid;
                    cache.temp_search_param.sortName = categoryName;
                }if(materialsName == e.materialsName){
                    cache.temp_search_param.material = e.materialsUuid;
                }
            });
        }
    }

    function getURL(){
        var url = "";
        if(cache.temp_search_param.sortID != ""){
            url +="categoryuuid_"+cache.temp_search_param.sortID;
        }
        if(cache.temp_search_param.sortName != ""){
            url +="_categoryname_"+encodeURIComponent(encodeURIComponent(cache.temp_search_param.sortName));
        }
        if(cache.temp_search_param.material != ""){
            url +="_material_"+cache.temp_search_param.material ;
        }
        window.open(cache.base_url+"/market/"+url);
    };

    /**
     *@date:2015-05-05
     *@Modify-lastdate :2015-05-13
     *@author :Green.Ge
     *@describe：首页数据加载初始化
     *@param: null
     *@return：null
     *
     */
    function init(){

        loadAllCategoryMaterials();
        //启用初始化事件
        enableInitEvents();
        /*加载初始化数据*/
        //loadSorts();

    }

    function enableInitEvents(){
        //模糊匹配
        $('#nsortName').bind('input propertychange', function () {
            var matchStr = $("#nsortName").val();
            if ($.trim(matchStr) === "") {
                $(".i-search-bar input:gt(0)").val("").blur();
                $("#showLayer_nsortName li").removeClass("hover");
                $("#showLayer_nsortName a").removeClass("hover");
                $("#proComp").hide();
                cache.temp_search_param.nsortID = "";
                cache.temp_search_param.nsortName = "";
                cache.temp_search_param.sortID = "";
                cache.temp_search_param.sortName = "";
                $("#showLayer_material .textures-con").text("");
                $(".factory-layer .textures-con").empty();
                $(".factory-bar").hide();

                $(".show-layer").hide();
                //$("#showLayer_nsortName").show();
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
                        var li=$("#proComp ul li").has("a.hover");

                        cache.temp_search_param.sortID = li.attr("sortId");
                        cache.temp_search_param.material= li.attr("materialsuuid");
                        var v=li.find("a").html();
                        var array= v.split(" ");
                        if(array && array.length==2){
                            cache.temp_search_param.sortName=array[0];
                        }
                        $("#nsortName").val(v);

                        $("#proComp .product-complete-ul").empty();
                        $("#proComp").hide();

                        getURL();
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
			$("#proComp").hide();

            cache.temp_search_param.material = "";
            cache.temp_search_param.sortID = "";
            cache.temp_search_param.sortName = "";
            if(validatNsort()) {
                getURL();
            }
        });

        /*input点击事件*/
        $("#i-search").on("click",".form-item",function () {
            if ($("#proComp").is(":hidden")) {
                $(".show-layer").hide();
                $('.form-item').removeClass("focus");
                if ($.trim($("#nsortId").val()) === "" || $.trim($("#nsortId").val()) === "-9999") {
                   // $("#nsortName").focus().closest(".form-item").addClass("focus").find(".show-layer").show();
                } else {
                    $(this).addClass("focus").find(".show-layer").show();
                }
            }
        });
    }

    function validatNsort() {
        var v = $("#nsortName").val();
        if(v){
            inNsortList(v);
            return true;
        }
    };

    //暴露对外方法
    module.exports = {
        init : init
    };
});