package com.prcsteel.ec.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
	/**
	 * 侧边栏页面
	 */
	@RequestMapping(value = "/slide", method = RequestMethod.GET)
	@ApiOperation("侧边栏中转")
	public String slide(){
		return "/cart/slide";
	}

	/**
	 * 我的购物车页面
	 */
	@RequestMapping(value = "/mycart", method = RequestMethod.GET)
	@ApiOperation("我的购物车中转")
	public String myCart(){
		return "/cart/mycart";
	}
}
