// JavaScript Document
define(function (require, exports, module) {
    //初始化

   
    var $ = require("jquery"),
    shoppingcart = require("cart");
    $(function(){
        // init shopping cart
        $(".cart_items").shoppingcart();
    })
});
