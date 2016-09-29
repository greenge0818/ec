/*utf-8*/
//(function () {"use strict"; return this === undefined;}());
define(function(require, exports, module) {
	"use strict";
	module.exports = {
        base_url : Context.PATH,
		cas_url	:	Context.CAS_PATH,
        pageSize:5,

		search_sorts : null,
		search_areas : null,
		search_factories : null,
		search_brand_filter: null,
		temp_material : null,
		temp_spec : [],
		ajaxList:[],
		search_result : null,
		factory_cache:null,
		factory_cache_arr:null,
		targetBtn:".cart-login-submit",
		temp_search_param : {
			"sortID" : "",
			"sortName" : "",
			"nsortID" : "",
			"nsortName" : "",
			"material" : "",
			"spec1" : "",
			"spec2" : "",
			"minSpec2" : "",
			"maxSpec2" : "",
			"spec3" : "",
			"minSpec3" : "",
			"maxSpec3" : "",
			"priceOrder" : "",
			"cityID" : "",
			"cityName" : "",
			"provinceID" : "",
			"factory" : "",
			"uploaded" : [],
			"PageSize" : 30,
			"str": "",
			"pageIndex":1
		},
		temp_nsort_for_showspec:null,
		search_result_total: 0,
		search_search_history: [],
		search_last_show_layer :null,
		search_area_changed_flag: null,
		global_islogon : false,
		global_userinfo : [],
		search_temp_isInit: true,
		search_temp_isNsortChanged: false,
		search_click_history: false,
		search_recent_cookie_name_notlogin : "recent_tourist",
		search_global_cookie_expire_day : 365 * 10,
		search_saveMax : 5,
		search_last_click_item: null,
		search_autoToAll: false,
        max_weight : 100,
        mix_weight : 1,
        messages : { 
            cart : {
                special:"不锈",
				invalidWeightNotEnough: "提示：采购重量不能小于1吨",
				invalidWeightOverweight: "提示：采购重量不能大于X吨",
				invalidWeightDecimal: "提示：小数点后数字不能超过2位",
                invalidWeightValue :  "提示：采购重量不低于X吨，不超过100吨",
                invalidWeightFormat :  "提示：请输入正确的数字"
            }
        },
        kefuqq : 2561709944,
		nsort_trigger_flag : true //品名输入方式 true为鼠标点击弹层，false为autocomplete选择。
		
	};
});


















































