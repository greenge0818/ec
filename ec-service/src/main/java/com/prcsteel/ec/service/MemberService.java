package com.prcsteel.ec.service;

import com.prcsteel.ec.model.domain.ec.APPOnceMore;
import com.prcsteel.ec.model.domain.ec.Cart;
import com.prcsteel.ec.model.domain.ec.ConsignOrderDto;
import com.prcsteel.ec.model.domain.ec.RequirementStatusChangeRecord;
import com.prcsteel.ec.model.dto.ConsignTabsDto;
import com.prcsteel.ec.model.dto.RequirementDto;
import com.prcsteel.ec.model.query.ConsignOrderQuery;

import java.util.List;

public interface MemberService {
    /**
     * 获取待办事项列表
     *
     * @param maxId     行情中心最大id
     * @param lastTime  最后时间,获取该时间之前的数据
     * @param pageIndex 页码
     * @return
     */
    List<RequirementDto> getTodoList(Integer maxId, Long lastTime, Integer pageIndex);

    /**
     * 获取待办事项列表（APP）
     *
     * @param lastTime   最后时间,获取该时间之后的所有数据
     * @param token      登录凭证
     * @param isOnceMore 是否是再来一单
     */
    List<RequirementDto> getTodoListAPP(String lastTime, String token, Boolean isOnceMore);

    /**
     * 再来一单，资源加入购物车
     *
     * @param cartList 购物车列表
     * @return 购物车资源数量
     * @author peanut
     * @date 2016/05/09
     */
    int onceMoreOrder(List<Cart> cartList);

    /**
     * 获取采购单数据
     */
    List<ConsignOrderDto> getConsignInfo(ConsignOrderQuery query);

    /**
     * 获取采购单tab信息（状态枚举和数量角标）
     */
    List<ConsignTabsDto> getConsignTabs();

    /**
     * 获取某个时间点之后的再来一单列表(APP)
     *
     * @param token
     * @param lastDateTime
     * @return
     */
    List<APPOnceMore> getAppOncemorelist(String lastDateTime, String token);

    /**
     * 获取需求单详情
     *
     * @param source  数据来源
     * @param records 状态变化表数据
     * @return
     */
    List<RequirementDto> getRequirement(String source, List<RequirementStatusChangeRecord> records);
}
