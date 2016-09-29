/*jslint sloppy: false, vars: false*/
/**
 *
 * This plugin depends on store.js now.
 * https://github.com/marcuswestin/store.js
 *
 */
define(function(require, exports, module) {
    var em, hasEnoughWeight = true, timeLong=60,
        $ = require("jquery"),
        util = require("util"),
        constant = require("constant"),
        cache = require("cache"),
        gwfinddialog = require("gwfind.dialog"),
        prcsteel = require("prcsteel"),
        cas = require("cas"),
        tips = require("checkformtips");
        require("json");
        ajaxFileUpload = require("ajaxfileupload");
        window.cas = cas;
    var FORMTEXT = "请输入想要钢材的品名";
    var imgUrlstr = [];
    var html = '<div class="find-box-bg">'+
        '<form  name="gwfindform" method="post" id="gwfindform" enctype="multipart/form-data">'+
        '<h2>填写采购需求</h2>'+
        '<textarea class="textarea" id="textarea" maxlength="100" name="request">请输入想要钢材的品名</textarea>'+
        '</div>'+
        '<div class="find-box-bg">'+
        '<h2>上传采购需求</h2>'+
        '<div class="markbtnbox">'+
        '<div class="markbtn" style="margin-bottom:7px">'+
        '<a href="javascript:;" class="a-upload"><input type="file" upload="file" name="uploadFile" id="uploadFile3" /><em class="upload-icon"></em>上传文件</a>'+
        '</div>'+
       
        '</div>'+
        '<div class="filelist">' +
        '<div id="file_upload-queue" class="uploadify-queue">'+
        '</div>'+
        '</div>'+
        '<div class="upload_btn"><a href="javascript:;" class="a-upload"><input type="file" class="filerowbtn" upload="file" name="uploadFile" id="uploadFile4" /><em class="upload-icon"></em>上传文件</a></div>'+
        '<input type="hidden" name="fileUrl" id="fileUrl">'+
        '<p>注：可上传2M之内的word、excel、文本和图片文件</p>'+
        '</div>'+
       '<span class="file-err-msg-span" style="color:#c81623;display:inline-block;margin-left:20px;"></span>'+
        '<input type="button" class="helpme-btn" value="钢为购">'+
        '</form>'+
        '<p class="remind">我们将用<span>0571-8971&nbsp;8799</span>给您回电，请保持手机畅通</p>';

    $(".find-content").append(html);

    var helpfind = {
        init:function(){
            //点击表单清空文本
            helpfind.xqEmptyFormText($("#textarea"));

            if($.support.style){
                    $(document).on("change","#uploadFile3,#uploadFile4",function(){
                        
                        var filetypeTrue = helpfind.fileChange($(this)),
                            btnID = $(this).attr("id"),
                            isSizeTrue = helpfind.getFileSize($(this));
                        if(!isSizeTrue){
                            $(".file-err-msg-span").show().html("文件超过2M");
                            return false;
                        };
                        if(!filetypeTrue){
                            $(".file-err-msg-span").show().html("文件格式有误");
                            return false;
                        };
                        if(imgUrlstr.length >=5){
                            $(".file-err-msg-span").show().html("最多上传5个文件");
                            return false;
                        };

                        $(".err-msg-span").hide();
                        if(btnID == "uploadFile3"){ //判断file
                            $("#uploadFile4").attr("name","");
                            $("#uploadFile3").attr("name","uploadFile");        
                            helpfind.uploadFile("uploadFile3");
                        }else{
                            $("#uploadFile3").attr("name","");
                            $("#uploadFile4").attr("name","uploadFile");
                            helpfind.uploadFile("uploadFile4");
                        }
                        $(".markbtnbox").hide();
                    })
                }else{
                    $("#uploadFile3,#uploadFile4").bind("change",function(){
                        
                        var filetypeTrue = helpfind.fileChange($(this)),
                            btnID = $(this).attr("id"),
                            isSizeTrue = helpfind.getFileSize($(this));
                        if(!filetypeTrue){
                            $(".file-err-msg-span").show().html("文件格式有误");
                            return false;
                        };
                        if(imgUrlstr.length >=5){
                            $(".file-err-msg-span").show().html("最多上传5个文件");
                            return false;
                        };
                        if(!isSizeTrue){
                            $(".file-err-msg-span").show().html("文件超过2M");
                            return false;
                        };
                        $(".file-err-msg-span").html("");
                        if(btnID == "uploadFile3"){ //判断file
                            $("#uploadFile4").attr("name","");
                            $("#uploadFile3").attr("name","uploadFile");        
                            helpfind.uploadFile("uploadFile3");
                        }else{
                            $("#uploadFile3").attr("name","");
                            $("#uploadFile4").attr("name","uploadFile");
                            helpfind.uploadFile("uploadFile4");
                        };
                        $(".markbtnbox").hide();
                    })
                }  
                helpfind.closeli(); //删除列表文件

            //提交表单
            $(".helpme-btn").click(function(){
                var textareaVal =$("#textarea").val();
                if(textareaVal == "请输入想要钢材的品名"){
                    $("#textarea").val("");
                };
                helpfind.submitdata();
            });

            //验证手机
            $(document).on("change","#userPhonetellogin",function(){
                var isTrue = helpfind.checkPhone($(this));
                if(!isTrue){
                    return;
                }
            });

            $(document).on('click', ".cart-login-submit", function () {
              var $ = require("jquery"),v = helpfind.checkPhone($("#userPhonetellogin")) &&　util.checkverifyCode($("#smsCodeNumber"));
                if (!v) {
                    return;
                }
	    	 	var phone = $("#userPhonetellogin").val(),
                code = $("#smsCodeNumber").val(),
                rtn = cas.remoteValid($("#userPhonetellogin").val());
                if(rtn == "3012"){
	    	 		return ;
	    	 	}else if(rtn == "1004"){
					var config = {
	                    url : "/api/user/registnopwd",
	                    data:{"mobile":phone,"code":code},
	                    success:function(data){
	                        helpfind.checkUserRegCallback(data);
	                    }
	                }
	                util.ajax({config:config});
	    	 		return ;
	    	 	}
                
                $(".cart-login-submit").prop("disabled","disabled").addClass("not-allowed");
	            cas.setCallback(function(){
	            	helpfind.submitdata(1);
	            });
				cas.casLogin(phone,code);
//              var config = {
//                  url : "/api/user/phone/check",
//                  data:{"phone":phone},
//                  success:function(data){
//                      helpfind.checkUserCallback(data);
//                  }
//              }
//              util.ajax({config:config});
            });
            $(document).on('click', ".login-valid-btn-find", function () {
                var $ = require("jquery"),v = helpfind.checkPhone($("#userPhonetellogin"));
                if (!v || (timeLong > 0 && timeLong != 60) || $(this).hasClass("not-allowed")) {
                    return;
                }
                $.post(cache.base_url + "/api/code/send/registerorlogin", {phone: $("#userPhonetellogin").val()}, function (response, status) {
                    //if (response.code == "2000") {
                    //    $(".err-msg-span").show().html("验证码发送成功");
                    //} else if (response.code == "2001") {
                    //    $(".err-msg-span").show().html("验证码发送失败");
                    //} else if (response.code == "2003") {
                    //    $(".err-msg-span").show().html("验证码发送太频繁");
                    //}else if (response.code == "3012") {
                    //    $(".err-msg-span").show().html("用户账号已被锁定!");
                    //}
                    $(".err-msg-span").show().html(constant.getCodeMsg(response.code));
                });
                helpfind.countDown();
                $(this).attr("disabled", "disabled").addClass("not-allowed");
            });
        },
        //检查文件大小
        getFileSize:function(eleId) {
            try {
                var size = 0;
                if ($.support.style) {//非IE
                    size = eleId[0].files[0].size;//byte
                    size = size / 1024;//kb
                    size = size / 1024;//mb
                } else {//IE
                    var fileMgr = new ActiveXObject("Scripting.FileSystemObject");
                    var filePath = eleId[0].value;
                    var fileObj = fileMgr.getFile(filePath);
                    size = fileObj.size; //byte
                    size = size / 1024;//kb
                    size = size / 1024;//mb
                }
                if(size >2){
                    return false;
                }else{
                    return true;
                }
            } catch (e) {
                return true;
            } 
        },
        //验证手机格式与长度
        checkPhone:function(elmid){
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
        },
		
        //文件上传
        uploadFile : function(elm){
            $ = require("jquery1.9");
            $.ajaxFileUpload({
                url: cache.base_url+"/common/uploadfile",
                secureuri:false,
                dataType: "json",
                fileElementId:elm,
                success: function(data){
                    if(data.code!="6000"){
                        $(".file-err-msg-span").show().html(constant.getCodeMsg(data.code));
                        if(imgUrlstr.length <1){
                            $(".markbtnbox").show();
                        }
                        return false;
                    }
                    $(".file-err-msg-span").html("");
                   
                   var fileUrlarr = data.data,
                        fileUrlstrs= new Array(),
                        imgtype = "",
                        filename = "",
                        icon = "";
                        fileUrlstrs=fileUrlarr.replace(/\\/g,"/").split("/");       
                        imgtype =fileUrlstrs[fileUrlstrs.length-1].split(".");
                        filename = fileUrlstrs[fileUrlstrs.length-1];
                        
                    if(imgtype[imgtype.length-1] == "png" || imgtype[imgtype.length-1] == "jpg" || imgtype[imgtype.length-1] == "gif" || imgtype[imgtype.length-1] == "bmp"){
                        icon = "image-icon";
                    }else{
                        icon = "file-icon";
                    }
                    //文件列表
                    $("#file_upload-queue").append('<div class="uploadify-queue-item" data-string="'+fileUrlarr+'"><span class='+icon+'></span><span class="filenametxt">'+filename+'</span><em></em></div>')
                    //隐藏表单赋值
                    imgUrlstr.push(data.data);
                    $("#fileUrl").val(imgUrlstr);
                }
            });
        },
        
        //验证上传文件
        fileChange:function (target) {
            var filepath=target.val(),
                extStart=filepath.lastIndexOf("."),
                ext=filepath.substring(extStart,filepath.length),
                filetypes =[".jpg",".png",".gif",".txt",".jped",".bmp",".wps",".doc",".ppt",".xls",".docx",".xlsx",".et"];
            for(var i =0; i<filetypes.length; i++){
                if(filetypes[i] == ext){
                    return true;
                }
            }
            return false;
        },
        
        
        closeli : function(){
            $(document).on('click', ".uploadify-queue-item em", function(e) {
                util.stopF(e); //阻止冒泡
               var item = $(this).closest(".uploadify-queue-item");
               item.remove();
               if(imgUrlstr.length <=1){
                    $(".markbtnbox").show();
               }
               var replaceText = item.attr("data-string");
               var fileUrlstring = $("#fileUrl").val();
               $(".a-upload input").val("");
               var arry = util.remove(imgUrlstr,replaceText);
               $("#fileUrl").val(arry.join(','));
               $(".file-err-msg-span").show().html("");
               
            })
        },
        //提交表单
       submitdata : function(getreload){
        var isreload = getreload ==undefined || getreload ==null || getreload =="" ? 0 : getreload;
            $.post(cache.base_url+"/api/requirement/submit", { request:$("#textarea").val(),fileUrl:$("#fileUrl").val()}, function (data, status) {
                if(data.code=="7000"){
                    gwfinddialog.successDialog(isreload, data.data, data.status);
                    $("#file_upload-queue").html("");
                    imgUrlstr = [];
                    $("#textarea").val("请输入想要钢材的品名").css("color","#999");
                    $("#fileUrl").val("");
                    $(".markbtnbox").show();
                }else if(data.code == "0001"){
                    gwfinddialog.loginDialog();
                }else{
                    $(".file-err-msg-span").show().html(constant.getCodeMsg(data.code));
                    $(".err-msg-span").show().html(constant.getCodeMsg(data.code));
                    return false;
                }
     
        });

        },
        //需求表单，指定默认内容
        xqEmptyFormText : function(elm){
            //兼容IEtextarea控制长度
            elm.keyup(function(){
                var area=$(this);
                var max=parseInt(area.attr("maxlength"),10);
                if(max>0){
                    if(area.val().length>max){ 
                        area.val(area.val().substr(0,max));
                    }
                }
            });
            elm.focus(function(){
                if($(this).val() != FORMTEXT){
                    return true;
                    $(this).css("color","#333");
                }else{
                    $(this).val("");
                    $(this).css("color","#999");
                }
            });
            elm.blur(function(){
                //复制的字符处理问题
                var area=$(this);
                var max=parseInt(area.attr("maxlength"),10);
                if(max>0){
                    if(area.val().length>max){
                        area.val(area.val().substr(0,max));
                    }
                }
                if($(this).val() == ""){
                    $(this).val(FORMTEXT);
                    $(this).css("color","#999");
                }else{
                    $(this).css("color","#333");
                    return true;
        
                }
            });

        },

        countDown : function(){
            var btn = $(".login-valid-btn-find");
            var intervalEvent = setInterval(function(){
                if(timeLong>=0){
                    btn.text(timeLong--+"秒");
                }else{
                    btn.text ("重新获取");
                    timeLong = 60;
                    btn.removeClass("not-allowed");
                    clearInterval(intervalEvent);
                }

            },1000);
        },
        //验证用户是否存在      
        checkUserCallback:function(data){
            var phone = $("#userPhonetellogin").val(),
            code = $("#smsCodeNumber").val();
            if(data.code == "1002"){ //已存在用户直接登录
                login(phone,code);
            }else if(data.code == "1004"){ //未注册用户
                var config = {
                    url : "/api/user/registnopwd",
                    data:{"mobile":phone,"code":code},
                    success:function(data){
                        helpfind.checkUserRegCallback(data);
                    }
                }
                util.ajax({config:config});
            }else if(data.code == "3012"){ //用户锁定
                helpfind.checkUserRegCallback(data);
            }
        },
        checkUserRegCallback:function(data){
            var phone = $("#userPhonetellogin").val(), code = $("#smsCodeNumber").val(),obj,
                style = 2;
                if(data.code !="3005"){
                    obj = {
                        type : style
                    }
                    tips.checkFormTips(obj,constant.getCodeMsg(data.code));
                    $(".cart-login-submit").removeAttr("disabled").removeClass("not-allowed");;
                    return false;
                }
                cas.setCallback(loginAfter);
				cas.casLogin(phone,code);
                //login(phone,data.data);
        }
    }

    function login(phone,pwd){
    	var config = {
        	type : "POST",
			url : "/api/passport/verify",
			data : {
                username:phone,
                password:pwd
            },
			dataType: "json",
            success : function(res, status, xhr) {
            	loginCallback(res)

            }
		}
		util.ajax({config:config});
    };
    function verify(url){
		$.ajax({
            type : "GET",
            url : url,
            success : function(text, textStatus, xhr) {
                fetchAccount();
               
            }
        });
    };
    function loginAfter(){
        var config = {
			url : "/api/user/login/after",
			asnyc:false,
            success : function(data) {
            	helpfind.submitdata(1);
            }
		}
		util.ajax({config:config});
    };
    function fetchAccount(){
    	var config = {
    		type : "GET",
			url : "/api/passport/verify",
            dataType: "text",
            success : function(res, status, xhr) {
               	 loginAfter();
            }
		}
		util.ajax({config:config});
    };
    function loginCallback(data,logintab,style){
    	var obj,style = 2;
    	if(data.code !="5002"){
			obj = {
				type : style
			}
			tips.checkFormTips(obj,constant.getCodeMsg(data.code =="5004" ? "2004": data.code));
			$(".cart-login-submit").removeAttr("disabled").removeClass("not-allowed");
			return false;
		}
		verify(data.data);
    };
    
    helpfind.init();

})