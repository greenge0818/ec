package com.prcsteel.ec.persist.dao.ec;

import com.prcsteel.ec.model.domain.ec.Cart;
import com.prcsteel.ec.model.dto.ResourceDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CartDao {
    int insert(Cart record);

    int insertSelective(Cart record);

    /**
     * 通过cookieId和客户guid获取购物车信息
     *
     * @param userGuid
     * @param cookieId
     * @return
     */
    List<ResourceDto> selectByUserGuidAndCookieId(@Param("userGuid") String userGuid, @Param("cookieId") String cookieId);

    /**
     * 把购物车信息归属从cookieId改为客户guid（客户登陆时调用）
     *
     * @param userGuid
     * @param cookieId
     */
    int updateCartUserGuidByCookieId(@Param("userGuid") String userGuid, @Param("cookieId") String cookieId);

    /**
     * 批量修改购物车重量和选中标记
     *
     * @param ids
     * @param weight
     * @param isChecked
     */
    int batchUpdateWeightAndChecked(@Param("ids") String ids, @Param("weight") BigDecimal weight, @Param("isChecked") String isChecked);

    /**
     * 批量删除购物车
     *
     * @param ids
     * @return
     */
    int batchDeleteByIds(@Param("ids") String ids);

    /**
     * 按userGuid做筛选
     *
     * @param userGuid
     * @return
     */
    List<Cart> selectCheckedByUserGuid(@Param("userGuid") String userGuid);

    /**
     * 批量添加购物车
     *
     * @param cartList 购物车列表
     * @return  影响行数
     * @author peanut
     * @date 2016/05/09
     */
    int batchInsert(List<Cart> cartList);

    /**
     * 按条件做筛选(卖家/品名/材质/规格/厂家/仓库/城市/计重方式)
     *
     * @param cart
     * @return
     */
    Long selectByParam(Cart cart);

    /**
     * 更新购物车价格和重量
     *
     * @param id
     * @param weight
     * @param price
     */
    int updateWeightAndPrice(@Param("id") Long id, @Param("weight") BigDecimal weight, @Param("price") BigDecimal price);
}