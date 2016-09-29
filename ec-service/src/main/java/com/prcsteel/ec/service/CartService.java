package com.prcsteel.ec.service;

import com.prcsteel.ec.model.domain.ec.Cart;
import com.prcsteel.ec.model.dto.CartDto;
import com.prcsteel.ec.model.model.Market2PickRequirement;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    /**
     * 添加购物车
     * @param cart
     */
    void addCart(Cart cart, String cookieId);

    /**
     * 修改购物车信息
     * @param ids
     * @param weight
     * @param isChecked
     */
    void updateCart(String ids, BigDecimal weight, String isChecked);

    /**
     * 删除购物车信息
     * @param ids
     */
    void delCartByIds(String ids);

    /**
     * 通过cookieId和客户guid获取购物车信息
     * @param cookieId
     * @return
     */
    List<CartDto> selectByUserGuidAndCookieId(String cookieId);

    /**
     * 把购物车信息归属从cookieId改为客户guid（客户登陆时调用）
     * @param cookieId
     */
    void updateCartUserGuidByCookieId(String cookieId);

    /**
     * 提交购物车
     * @return 返回生成发订单code，用于订单监测
     */
    String insertRequirement();
}
