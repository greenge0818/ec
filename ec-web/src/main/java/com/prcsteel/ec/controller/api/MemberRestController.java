package com.prcsteel.ec.controller.api;

import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.enums.ResultMsgType;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.dto.PageResult;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.model.domain.ec.Cart;
import com.prcsteel.ec.model.domain.ec.ConsignOrderDto;
import com.prcsteel.ec.model.dto.RequirementDto;
import com.prcsteel.ec.model.dto.SmartVo;
import com.prcsteel.ec.model.query.ConsignOrderQuery;
import com.prcsteel.ec.model.query.RequirementQuery;
import com.prcsteel.ec.persist.dao.ec.UserDao;
import com.prcsteel.ec.service.CartService;
import com.prcsteel.ec.service.MemberService;
import com.prcsteel.ec.service.RequirementService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/member/")
public class MemberRestController {
    @Resource
    MemberService memberService;

    @Resource
    CartService cartService;

    @Resource
    private RequirementService requirementService;

    /**
     * 获取客户待办事项
     *
     * @param maxId     行情中心最大id
     * @param lastTime  最后时间,获取该时间之前的数据
     * @param pageIndex 页码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "todolist", method = RequestMethod.POST)
    @ApiOperation("获取客户待办事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "maxId", value = "行情中心最大id", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "lastTime", value = "最后时间,获取该时间之前的数据", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "Integer", paramType = "query")
    })
    public Result getTodoList(Integer maxId, Long lastTime, Integer pageIndex) {
        try {
            return new Result(MessageTemplate.MEMBER_GET_TODO_SUCCESS.getCode(),
                    memberService.getTodoList(maxId, lastTime, pageIndex), ResultMsgType.BUSINESS);
        } catch (BusinessException e) {
            return new Result(e.getCode());
        }
    }


    /**
     * 获取采购单数据
     */
    @ResponseBody
    @RequestMapping(value = "getconsigninfo", method = RequestMethod.POST)
    @ApiOperation("获取采购单数据")
    public PageResult getConsignInfo(ConsignOrderQuery query) {
        PageResult result = new PageResult();
        try {
            List<ConsignOrderDto> orders = memberService.getConsignInfo(query);
            result.setData(orders.subList(0, orders.size() - 1));   //最后一条是计数用的，应该去掉
            result.setRecordsFiltered(orders.size() - 1);
            result.setRecordsTotal(orders.get(orders.size() - 1).getTotal());
            result.setCode(MessageTemplate.MEMBER_GET_CONSIGN_ORDER_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * 再来一单，多条资源加入购物车
     *
     * @return
     * @author peanut
     * @date 2015/05/09
     */
    @ResponseBody
    @RequestMapping(value = "oncemore", method = RequestMethod.POST)
    public Result onceMoreOrder(@RequestBody List<Cart> cartList) {
        Result result = new Result();
        try {
            result.setData(memberService.onceMoreOrder(cartList));
            result.setCode(MessageTemplate.CART_ADD_SUCCESS.getCode());
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 查看询价单
     * @Date: 2016年05月05日
     */
    @RequestMapping(value = "searchrequirement", method = RequestMethod.POST)
    @ApiOperation(value = "查看询价单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stageStatus", value = "需求单状态", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "搜索开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "搜索结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "页码", dataType = "Integer", paramType = "query")
    })
    public
    @ResponseBody
    PageResult searchRequirement(@Valid RequirementQuery requirementQuery) {
        PageResult result = new PageResult();
        try {
            List<RequirementDto> list = requirementService.getRequirement(requirementQuery);
            return new PageResult(MessageTemplate.INQUIRY_GET_SUCCESS.getCode(), list == null ? 0 : list.size(),
                    requirementService.requirementCountByStatus(requirementQuery), list);
        } catch (BusinessException be) {
            result.setCode(be.getCode());
        }
        return result;
    }

    /**
     * @Author: Tiny
     * @Description: 查看详情
     * @Date: 2016年05月05日
     */
    @RequestMapping(value = "viewdetails", method = RequestMethod.POST)
    @ApiOperation(value = "查看详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requirementCode", value = "询价单号", dataType = "String", paramType = "query")
    })
    public
    @ResponseBody
    Result viewDetails(@Valid String requirementCode) {
        try {
            return new Result(MessageTemplate.INQUIRY_GET_DETAIL_SUCCESS.getCode(),
                    requirementService.viewDetail(requirementCode), ResultMsgType.BUSINESS);
        } catch (BusinessException be) {
            return new Result(be.getCode());
        }
    }

}
