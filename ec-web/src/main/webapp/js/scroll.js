$(function(){
    $('#fullpage').fullpage({
    	//anchors: ['firstPage', 'secondPage', 'thirdPage', 'fourthPage'],
    	navigation: true,
        verticalCentered: false,
        scrollOverflow: true,
        onLeave: function (index, nextIndex, direction) {
	       if (nextIndex == 4) {
	            positionAdjust();
	        }
	    }
    })
    $(".download-r span,.download-r img").bind("click",function(){
    	var type= $(this).attr("data-attr");
    	$(".d-mask").show();
    	$(".alert-dialog").show();
    	$(".alert-dialog img").attr("src",Context.PATH+"/css/default/images/zgapp.png");
    })
    $(".closeLabel").bind("click",function(){
    	$(".d-mask").hide();
    	$(".alert-dialog").hide();
    })
})
var positionAdjust = function(){
	var qrcodecHeight = $(".qrcode-c").height(),
	QrCode = 260;
	$(".ios-c,.android-c").css("marginTop",(qrcodecHeight-QrCode)/2);
	$(".cartoon").css("marginTop",(qrcodecHeight-360));
}
