
/*utf-8*/
//(function () {"use strict"; return this === undefined;}());

define(function (require, exports, module) {
    "use strict";
	var $ = require("jquery"),cache = require("cache");
	require("jquery-page");
	module.exports = {
		 //获取视口高度
         getTotalHeight: function () {
 
             if ($.support.leadingWhitespace) {
                 return document.compatMode == 'CSS1Compat'? document.documentElement.clientHeight : document.body.clientHeight;
             } else {
                 if(self.innerHeight) {
                      return self.innerHeight;
                 } else {
                     return document.documentElement.clientHeight;
                 }
             }
         },
		 setMask: function () {
            var createMask, loadImg;
            createMask = document.createElement("div");
            loadImg = document.createElement("img");

            createMask.className = "u-mask";
            createMask.setAttribute("id", "u-mask");

            loadImg.className = "u-load";
            loadImg.setAttribute("src", cache.base_url+"/css/default/images/loading.gif");
            loadImg.setAttribute("id", "u-load");
            document.body.appendChild(loadImg);
            document.body.appendChild(createMask);
        },
        delMask: function () {
            $("#u-mask, .u-load").remove();
        },
        sAjax :function(cfg,mask){
        	this.ajax({config:(cfg ? cfg : {}),mask:(mask ? mask : "0")});
        },
       	ajax:function(options){
       		var cfg = {
       			type:"post",
       			async:true,
       			dataType:"json"
       		}
       		var opts = {};
			for(var k in options.config){
				opts[k] = options.config[k];
			}
       		 var  mk = options.mask ? options.mask : "0";
       		 if(mk == "1"){
       		 	this.setMask();
       		 	var self;
				if(this.delMask){
					self=this;
				}else{
					self=this.parent;
				}
       		 	if(opts.success && typeof opts.success == "function"){
					var fun = opts.success;
       		 		var temp = {
       		 			success:function(data, textStatus, jqXHR){
       		 				fun(data);
	       		 			if (mk === "1") {
								self.delMask();
							}
       		 			}
       		 		}
       		 		$.extend(opts,temp);
       		 		
       		 	}
       		 	if(opts.error && typeof opts.error == "function"){
       		 		var fun = opts.error;
       		 		var temp = {
       		 			error:function(XMLHttpRequest, textStatus, errorThrown){
       		 				fun(XMLHttpRequest, textStatus, errorThrown);
	       		 			if (mk === "1") {
								self.delMask();
							}
       		 			}
       		 		}
       		 		$.extend(opts,temp);
       		 	}
       		 }
       		if(opts){
       			opts.url = cache.base_url+opts.url;
       			$.extend(cfg,opts);
       		}
       		$.ajax(cfg);
       	},
       	esbajax:function(options){
       		var cfg = {
       			type:"post",
       			async:true,
       			dataType:"json"
       		}
       		var opts = {};
			for(var k in options.config){
				opts[k] = options.config[k];
			}
       		 var  mk = options.mask ? options.mask : "0";
       		 if(mk == "1"){
       		 	this.setMask();
       		 	var self;
				if(this.delMask){
					self=this;
				}else{
					self=this.parent;
				}
       		 	if(opts.success && typeof opts.success == "function"){
					var fun = opts.success;
       		 		var temp = {
       		 			success:function(data, textStatus, jqXHR){
       		 				fun(data);
	       		 			if (mk === "1") {
								self.delMask();
							}
       		 			}
       		 		}
       		 		$.extend(opts,temp);
       		 		
       		 	}
       		 	if(opts.error && typeof opts.error == "function"){
       		 		var fun = opts.error;
       		 		var temp = {
       		 			error:function(XMLHttpRequest, textStatus, errorThrown){
       		 				fun(XMLHttpRequest, textStatus, errorThrown);
	       		 			if (mk === "1") {
								self.delMask();
							}
       		 			}
       		 		}
       		 		$.extend(opts,temp);
       		 	}
       		 }
       		if(opts){
       			$.extend(cfg,opts);
       		}
       		$.ajax(cfg);
       	},
       	/*
       	 * 清除浏览器冒泡行为
       	 */
       	stopF : function(event) {
       		var e = event ? event : window.event;
			if (e == window.event) {// IE
				e.cancelBubble = true;
			} else {
				e.stopPropagation();
			}
		},
		/*
		 * 删除数组内部值
		 */
	    remove:function(array, val) {
	    	if (!Array.prototype.indexOf) //如果发现数组没有indexOf方法，会添加上这个方法。
			{
			  Array.prototype.indexOf = function(elt)
			  {
			    var len = this.length >>> 0;
			    var from = Number(arguments[1]) || 0;
			    from = (from < 0)
			         ? Math.ceil(from)
			         : Math.floor(from);
			    if (from < 0)
			      from += len;
			    for (; from < len; from++)
			    {
			      if (from in this &&
			          this[from] === elt)
			        return from;
			    }
			    return -1;
			  };
			}
	        var index = array.indexOf(val);
	        if (index > -1) {
	            array.splice(index, 1);
	        }
	        return array;
	    },
	    
       	post:function(url,data,callback,mask){
       		 var ifLoad = arguments[4];
            if (ifLoad == 1){
                $(".list-loading").show();
            }
            var  mk = mask !== "1" ? "0" : "1";
			var self;
			if(this.delMask){
				self=this;
			}else{
				self=this.parent;
			}
            if (mk === "1") {
                this.setMask();
            }
       		url = cache.base_url+url;
       		$.post(url,data,function(response,status){
       			callback(response,status);
   				if (mk === "1") {
					self.delMask();
				}
       			
       		})
       	},
       	 /**
          *@date:2015-05-05
          *@Modify-lastdate :2015-05-05
          *@describe：分页器,
          *@param:data数据json类型, pageBar存放页码位置, pageCallback：回调函数处理内容
          *返回值：（返回信息，注意冒号于参数对齐，并描述返回值类型）
          *
          */
        initPage : function (total,callback) {//分页
            var total = total, pageBar = "m-page-num";//var total总条数
            $("#" + pageBar).pagination(total, {
                num_edge_entries: 1, //边缘页数
                num_display_entries: 5, //主体页数
                callback: callback,//发送ajax获取分页内容
                items_per_page: cache.pageSize, //每页显示1条
                prev_text: "<&nbsp;上一页",
                next_text: "下一页 &nbsp;>",
				parent: this
            });
        },
        /**
         * @title:        弹框标题
         * @con:          弹框内容容器obj或url:http://xxx 或文本内容
         * @id:           弹框唯一标识
         * @fun:          回调函数
         * @isafter:      回调函数执行的地方,默认在代码末尾执行{1:末尾执行;0:开头执行}
         *
        **/
        dialog:"",
        getDialog : function (title, con, fixed,top,id, fun, isafter) {
            require("artdialog.min");
            if (id === undefined || id === null) {
                id = 'globalDialog';
            }
            var url = con.toLowerCase().substr(0, 6);
            var conBox = con.toLowerCase().substr(0, 1), autoClose = false;

            if (typeof (isafter) === 'undefined' || isafter === '' || isafter === null) { isafter = 1; }
            if (typeof (fun) === 'undefined' || fun === '' || fun === null) {fun = ''; }
			if(typeof(fixed) === 'undefined' || fixed ===null ){
				fixed = true;
			}
            var dialog = art.dialog({
                id: id,
                padding: 0,
                title: title,
                lock: true,
                fixed:fixed
            });
            this.dialog = dialog;
            dialog.hidden();
            if ((url === 'http:/' || url === 'https:') && ( con.toLowerCase().indexOf(".html") > 0 || con.toLowerCase().indexOf(".php") > 0)) {
                $('#dialogContBox').load(con + '?_=' + Math.random(), function (data) {
                    if (isafter !== 1) {
                        if (typeof (fun) === 'function') {
                            fun(data);
                        }
                    }
                    dialog._reset();
                    dialog.visible();
                    setTimeout(function(){
	                	 $(".d-outer").parent("div").css("top",(top ? top : "30%"))
	                },20)
                    if (isafter === 1) {
                        if (typeof (fun) === 'function') {
                            fun(data);
                        }
                    }
                });
            }
            else
            {
                var _con = con;
                if(conBox == '#' && $(con).length > 0) {
                    _con = $(con).html();
                }

                if(isafter !== 1) {
                    if (typeof(fun) === 'function') {
                        fun(_con);
                    }
                }
                if(_con.toLowerCase().indexOf("div") < 0 ){
                    _con = '<div style="padding:10px 30px;">' + _con + '</div>';
                    dialog.content(_con);
                    //autoClose = true;
                }else{
                    dialog.content(_con);
                }

                dialog._reset();
                dialog.visible();
                setTimeout(function(){
                	 $(".d-outer").parent("div").css("top",(top ? top : "30%"));
                },20)
               
//              $(dialog.dom.outer).parents().css("top","30%");
                if (isafter === 1) {if(typeof(fun) === 'function') {fun(_con); } }

                if(autoClose){
                    setTimeout(this.setTmClose, 3000)
                }
            }
        },
        //关闭弹出层窗口
        setTmClose:function(){
           document.getElementById("dialogClose").click();
        },
        closeDialog:function(){
            this.dialog.close();
        },
		/**
		 * 
		 * @param {Object} price
		 * @param {Object} isWeight
		 * 设置显示格式。如1,000.000
		 */
		setFormat :function(price,isWeight,clear){
			if(price == undefined){
				return "0.00";
			}
			var arr = price.toString().split(".");
			var int_part = arr[0],float_part = arr[1];
			var yushu = int_part.length%3,zhengshu = Math.ceil(int_part.length/3),int_part_n="",float_part_n="";
			var oneplus = 0;
			if(zhengshu==1){
				int_part_n=int_part;
			}else{
				if(yushu>0){
					 int_part=(yushu == 1 ? "00":"0")+int_part;
				}
				for(var i=0;i<zhengshu;i++){
					if(i==(zhengshu-1)){
						int_part_n+=int_part.substring(i*3,(i+1)*3);
					}else{
						int_part_n+=int_part.substring(i*3,(i+1)*3)+",";	
					}
					
				}
				if(yushu>0){
					int_part_n = int_part_n.substr(yushu == 1 ? 2 : 1)
				}
				
			}
			
			
			if(float_part){
				var ismore3 = float_part.length>3;
				if(ismore3){
					var val = float_part.substr(3,1);
					if((val-0)>=5){
						float_part_n = float_part.substr(0,3)-0+1 == 1000 ? "000" :(float_part.substr(0,3)-0 == 0 ? "001" : (float_part.substr(0,3)-0+1+""));
						oneplus = float_part.substr(0,3)-0+1 == 1000 ? 1 :0;
						int_part_n = this.clearFormat(int_part_n)-0+oneplus+"";
					}else{
						float_part_n = float_part.substr(0,3)
					}
				}else{
					if(float_part.length<3 ){
						if(isWeight){
							float_part_n = (float_part.length == 1 ? (float_part+"00"):(float_part+"0"));
						}else{
							float_part_n = (float_part.length == 1 ? (float_part+"0"):(float_part));
						}
					}else{
						float_part_n = float_part;
					}
					
					
				}
				if(!isWeight){
					if((float_part-0)== 0){
						float_part ="";
					}
				}
			}else{
				if(isWeight){
					float_part_n ="000";
				}else{
					float_part_n ="";
				}
				
			}
			
			if(clear){
				int_part_n = this.clearFormat(int_part_n);
			}
			return ( int_part_n+(float_part_n.length>0 ? ("."+float_part_n) : ("")));
		},
		/**
		 *
		 * @param {Object} price
		 * 清除显示格式
		 * 
		 */
		clearFormat:function(price){
			price = price+"";
			var arr = price.split(",");
			return arr.join("");
		},
		
		/**
		 *
		 * @param
		 *  客服qq
		 */
		setqq:function(){
			$("#entQQ").click();
			var inter = setInterval(function(){
				if($(".WPA3-SELECT-PANEL").length>0){
					$(".WPA3-SELECT-PANEL").prev("div").remove();
					$(".WPA3-SELECT-PANEL").remove();
					clearInterval(inter);
				}
			},10)
			
			//window.open("http://wpa.qq.com/msgrd?v=3&uin="+cache.kefuqq+"&site=qq&menu=yes","_blank");
		},
    isNotBlank:function(str){
      if(str != null && str !="null" && $.trim(str) != ""){
        return true;
      }
      return false;
    },

    /**
     *
     * @param string name
     *  获得指定URL参数值
     */
    getUrlValue:function(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
      var r = window.location.search.substr(1).match(reg);
      if (r != null){
        return decodeURI(r[2]);
      }  
      return null;
    },
	checkverifyCode:function(elmid){
		var val = elmid.val();
		if(val == ""){
			$(".err-msg-span").show().html("").html("验证码错误");
			elmid.focus();
			return false;
		}else if(!(/^\d{4}$/.test(val))) {
			$(".err-msg-span").show().html("").html("验证码错误");
			elmid.focus();
			return false;
		}
		$(".err-msg-span").show().html("");
		return true;
	}

	};
});

