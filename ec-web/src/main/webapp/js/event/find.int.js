define(function(require, exports, module) {
    var em, hasEnoughWeight = true, timeLong=60,imgUrlstr = [],
    $ = require("jquery"),
    constant = require("constant"),
    util=require("util");
    exports.uploadfileFch =function(){
        $(window).bind("scroll",function(){
          var navH = $(".find-content").offset().top;
          var scroH = $(window).scrollTop();
          if(scroH >= 171){
             $(".find-content").css({"position" : "fixed", "top" :20,"z-index":20});
          }else{
             $(".find-content").css({"position":"static", "top" :171,"z-index":20});
          }
        });

        //点击表单清空文本
        $("#textarea").focus(function(){
            if($(this).val() != "请输入想要钢材的品名"){
                $(this).css("color","#333");
            }else{
                $(this).val("");
                $(this).css("color","#999");
            }
        });
        $("#textarea").blur(function(){
            if($(this).val() == ""){
                $(this).val("请输入想要钢材的品名");
                $(this).css("color","#999");
            }else{
                $(this).css("color","#333");
            }
        });

        //提交表单
        $(".helpme-btn").click(function(){
            var textareaVal =$("#textarea").val();
                if(textareaVal == "请输入想要钢材的品名"){
                $("#textarea").val("")
            }
            submitdata();
        });
        closeli();

    };
    //检查文件大小
   var getFileSize = function (eleId) {
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
        };
    var uploadFile = function (elmid){
            $ = require("jquery1.9");
            $.ajaxFileUpload({
                url: Context.PATH+"/common/uploadfile",
                secureuri:false,
                dataType: "json",
                fileElementId:elmid,
                success: function(data){
                    if(data.code!="6000"){
                    	if(imgUrlstr.length <1){
		        			$(".markbtnbox").show();
	   					}
                    	$(".err-msg-span").show().html("").html(constant.getCodeMsg(data.code));
                        return false;
                    }
                    $(".err-msg-span").html("");
                    var fileUrlarr = data.data,
                        fileUrlstrs= new Array(),
                        imgtype = "",
                        filename = "",
                        icon = "";
                        fileUrlstrs=fileUrlarr.replace(/\\/g,"/").split("/"); 
                        imgtype =fileUrlstrs[fileUrlstrs.length-1].split(".");
                        filename = fileUrlstrs[fileUrlstrs.length-1];
                    if(imgtype[1] == "png" || imgtype[1] == "jpg" || imgtype[1] == "gif" || imgtype[1] == "bmp"){
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
        };

    //删除上传文件条目    
    var closeli = function(){
        $(document).on('click', ".uploadify-queue-item em", function() {
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
           $(".err-msg-span").show().html("")
        })
    };

     //验证上传文件
   var fileChange =  function (target) {
        var filepath=target.val(),
            extStart=filepath.lastIndexOf("."),
            ext=filepath.substring(extStart,filepath.length),
            filetypes =[".jpg",".png",".gif",".txt",".jped",".bmp",".wps",".doc",".ppt",".xls",".docx",".xlsx",".et"],
            isNext = false;
        for(var i =0; i<filetypes.length; i++){
            if(filetypes[i] == ext){
                isNext = true;
            }
        }
        if(!isNext){
             $(".err-msg-span").show().html("").html("文件格式有误");
             return false;
        }
        return true;
    };
    var init = function(){
    	 if($.support.style){
			$(document).on("change","#uploadFile1,#uploadFile2",function(){
	
		        var filetypeTrue = fileChange($(this));
				var btnID = $(this).attr("id"),isSizeTrue = getFileSize($(this));
		        if(!filetypeTrue){
		            return false;
		        };
		        if(imgUrlstr.length >=5){
		            $(".err-msg-span").show().html("").html("最多上传5个文件");
		            return false;
		        };
	            if(!isSizeTrue){
	                $(".err-msg-span").show().html("").html("文件超过2M");
	                return false;
	            };            
		        $(".err-msg-span").html("");
		        if(btnID == "uploadFile1"){ //判断file
			        $("#uploadFile2").attr("name","");
			        $("#uploadFile1").attr("name","uploadFile");        
			        uploadFile("uploadFile1");
		        }else{
			        $("#uploadFile1").attr("name","");
			        $("#uploadFile2").attr("name","uploadFile");
			        uploadFile("uploadFile2");
		        }
		        $(".markbtnbox").hide();
			})
		}else{
			$("#uploadFile1,#uploadFile2").bind("change",function(){
				var filetypeTrue = fileChange($(this));
				var btnID = $(this).attr("id"),isSizeTrue = getFileSize($(this));
		        if(!filetypeTrue){
		            return false;
		        };
		        if(imgUrlstr.length >=5){
		            $(".err-msg-span").show().html("").html("最多上传5个文件");
		            return false;
		        };
	            if(!isSizeTrue){
	                $(".err-msg-span").show().html("").html("文件超过2M");
	                $(this).val("");
	                return false;
	            };
		        $(".err-msg-span").html("");
		        if(btnID == "uploadFile1"){ //判断file
			        $("#uploadFile2").attr("name","");
			        $("#uploadFile1").attr("name","uploadFile");        
			        uploadFile("uploadFile1");
		        }else{
			        $("#uploadFile1").attr("name","");
			        $("#uploadFile2").attr("name","uploadFile");
			        uploadFile("uploadFile2");
		        }
		        $(".markbtnbox").hide();
			})
		};
    }

    //提交表单
    var submitdata = function(){
        $.post(Context.PATH+"/api/requirement/submit", { request:$("#textarea").val(),fileUrl:$("#fileUrl").val()}, function (data, status) {
           	if(data.code !="7000"){
           		$(".err-msg-span").show().html("").html(constant.getCodeMsg(data.code));
           		return false;
           	}
       	    successDialog(data.status);
            $("#textarea").val("请输入想要钢材的品名").css("color","#999");
            imgUrlstr = [];
            $("#fileUrl").val("");
            $(".err-msg-span").show().html("");
            $("#file_upload-queue").html("");
            $(".markbtnbox").show();
        });
    };

   var successDialog = function (status) {
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
        el += '<a href="javascript:;" class="dia_continue">我知道了</a>&nbsp;';
        el += '</div>';
        el += '</div>';
        el += '</div>';
        util.getDialog(false, el);
        $(".dia_continue").click(function(){
        	$(".err-msg-span").html("");
            util.closeDialog();
        })
    }; 
	init();
})