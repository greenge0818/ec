package com.prcsteel.ec.service;


import com.prcsteel.ec.model.domain.ec.Requirement;
import com.prcsteel.ec.model.domain.ec.RequirementItem;
import com.prcsteel.ec.model.domain.ec.User;
import com.prcsteel.ec.model.dto.RequirementDto;
import com.prcsteel.ec.model.dto.RequirementForPickDto;
import com.prcsteel.ec.model.model.AddCbmsContact;
import com.prcsteel.ec.model.model.CreateRequirementFromPick;
import com.prcsteel.ec.model.model.Market2PickRequirement;
import com.prcsteel.ec.model.query.RequirementQuery;
import com.prcsteel.ec.model.query.RequirementStatusChangerQuery;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: RequirementService
 * @Description: 需求单服务
 * @Author Green.Ge
 * @Date 2016年4月27日
 */
public interface RequirementService {
    /**
     * @Author: Green.Ge
     * @Description: 获取需求单号
     * @Date: 2016年4月27日
     */
    String genCode();

    /**
     * @Author: Green.Ge
     * @Description: 获取某天单号的最大编号
     * @Date: 2016年4月27日
     */
    Integer getMaxCode(String date);

    /**
     * @Author: Rabbit
     * @Description: 提交app再来一单需求
     * @Date: 2016年6月2日
     */
    void submitAppOnceMore(String req, String token);

    /**
     * @Author: Tiny
     * edit by Rabbit 增加token参数兼容app接口调用
     * @Description: 提交需求
     * @Date: 2016年4月28日
     */
    void submitRequirment(Requirement req, String token, List<RequirementItem> requirementItems);

    /**
     * @Author: Tiny
     * @Description: 获取需求单
     * @Date: 2016年05月05日
     */
    List<RequirementDto> getRequirement(RequirementQuery requirementQuery);

    /**
     * @Author: Tiny
     * @Description: 获取需求详情
     * @Date: 2016年05月05日
     */
    List<RequirementDto> viewDetail(String requirementCode);

    /**
     * 按需求单code筛选
     *
     * @param codes
     * @return
     */
    List<RequirementDto> selectRequirementsByCodes(String codes);

    /**
     * 获取需求总数
     *
     * @Date: 2016年05月09日
     */
    Map<String, Integer> totalRequirement(User user);

    /**
     * 根据状态获取需求总数
     *
     * @param requirementQuery
     * @Date: 2016年05月09日
     */
    int requirementCountByStatus(RequirementQuery requirementQuery);

    /**
     * @Author: Tiny
     * @Description: 分拣系统/找货系统 更新需求单状态
     * @Date: 2016年05月23日
     */
    void updateRequirementByRemote(RequirementStatusChangerQuery changer);

    /**
     * @Author: Tiny
     * @Description: 分拣系统根据单号获取需求单明细信息
     * @Date: 2016年05月26日
     */
    RequirementForPickDto getDetailsByCode(String code);

    /**
     * @Author: Tiny
     * @Description: 分拣系统推送新增需求单给超市
     * @Date: 2016年05月26日
     */
    void createRequirementPick(CreateRequirementFromPick create);
}
