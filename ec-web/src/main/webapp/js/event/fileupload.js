define(function(require,exports,module){
	var $ = require("jquery1.9"),gwfinddialog = require("gwfind.dialog"),util=require("util"),constant = require("constant");

var imgUrlstr = [];
var init = {
    //文件上传
    uploadFile : function(elm){
        $.ajaxFileUpload({
            url: Context.PATH+"/common/uploadfile",
            secureuri:false,
            dataType: "json",
            fileElementId:elm,
            success: function(data){
	        	if(data.code!="6000"){
	            	$(".file-err-msg-span").show().html("").html(constant.getCodeMsg(data.code));
	                if(imgUrlstr.length <1){
	        			$(".markbtnbox").show();
					}
	                return false;
	            }
	            $(".file-err-msg-span").hide();
               
               var fileUrlarr = data.data,
                    fileUrlstrs= new Array(),
                    imgtype = "",
                    filename = "",
                    icon = "";
                    fileUrlstrs=fileUrlarr.replace(/\\/g,"/").split("/");                
                    imgtype =fileUrlstrs[fileUrlstrs.length-1].split(".");
                    filename = fileUrlstrs[fileUrlstrs.length-1];
                    
                if(imgtype[imgtype.length] == "png" || imgtype[imgtype.length] == "jpg" || imgtype[imgtype.length] == "gif" || imgtype[imgtype.length] == "bmp"){
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
            filetypes =[".jpg",".png",".gif",".txt",".jped",".bmp",".wps",".doc",".ppt",".xls",".docx",".xlsx",".et"],
            isNext = false;
        for(var i =0; i<filetypes.length; i++){
            if(filetypes[i] == ext){
                isNext = true;
            }
        }
        if(!isNext){
             $(".file-err-msg-span").show().html("").html("文件格式有误");
             return false;
        }
        return true;
    },
    
    
    closefilelist : function(){
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
	       $(".file-err-msg-span").show().html("");
	       
	    })
    },
    //提交表单
   submitdata : function(){
        $.post(Context.PATH+"/api/requirement/submit", { request:$("#textarea").val(),fileUrl:$("#fileUrl").val()}, function (data, status) {
            if(data.code=="7000"){
		        gwfinddialog.successDialog(1,null,data.status);
		        $("#file_upload-queue").html("");
		        imgUrlstr = [];
		        $("#textarea").val("请输入想要钢材的品名").css("color","#999");
		        $("#fileUrl").val("");
		        $(".markbtnbox").show();
            }else if(data.code == "0001"){
            	gwfinddialog.loginDialog();
            }else{
            	$(".file-err-msg-span").show().html("").html(constant.getCodeMsg(data.code));
                $(".err-msg-span").show().html("").html(constant.getCodeMsg(data.code));
                return false;
            }
 
    });

    }

    
}	   
   module.exports = {
		submitdata: init.submitdata,
        fileChange: init.fileChange,
        closefilelist: init.closefilelist,
        uploadFile: init.uploadFile

	}
    
})