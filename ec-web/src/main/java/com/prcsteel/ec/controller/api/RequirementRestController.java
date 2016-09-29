package com.prcsteel.ec.controller.api;

import com.prcsteel.ec.core.enums.*;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.oplog.OpLog;
import com.prcsteel.ec.core.oplog.OpParam;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.model.domain.ec.Requirement;
import com.prcsteel.ec.service.CommonService;
import com.prcsteel.ec.service.RequirementService;
import com.prcsteel.ec.validator.RequirementValidator;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @ClassName: RequirementRestController
 * @Description: 采购需求
 * @Author Tiny
 * @Date 2016年4月28日
 */
@Controller
@RequestMapping("/api/requirement/")
public class RequirementRestController {
    @Resource
    private RequirementService requirementService;
    @Resource
    private CommonService commonService;
    @Value("${ut.switch}")
    private String ut;

    /**
     * @Author: Tiny
     * @Description: 提交需求
     * @Date: 2016年4月28日
     */
    @OpLog(OpType.ADD_REQUIREMENT)
    @OpParam("requirement")
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ApiOperation(value = "添加需求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "request", value = "我的要求", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "fileUrl", value = "文件地址(逗号隔开)", dataType = "string", paramType = "query")
    })
    public
    @ResponseBody
    Result add(@Valid Requirement requirement, BindingResult br) {
        Result result = new Result();
        new RequirementValidator().validate(requirement, br);

        if (!br.hasErrors()) {
            try {
                requirement.setSource(RemoteDataSource.WEB.getCode());
                requirement.setType(RequirementType.HELP.getCode());
                requirement.setStageStatus(RequirementStatus.NEW.getCode());
                requirementService.submitRequirment(requirement, null, null);
                result.setCode(MessageTemplate.REQUIREMENT_SUBMIT_SUCCESS.getCode());
                result.setStatus(commonService.getHoliday());
                if (Boolean.parseBoolean(ut)) {
                    result.setData(requirement.getCode());
                }
            } catch (BusinessException be) {
                result.setCode(be.getCode());
            }
        } else {
            result.setCode(br.getAllErrors().get(0).getDefaultMessage());
        }
        return result;
    }

    /**
     * @Author: Green.Ge
     * @Description: 提供给分拣系统的需求单信息
     * @Date: 2016年05月06日
     */
    @RequestMapping(value = "pickup/getDetailsByCode", method = RequestMethod.GET)
    @ApiOperation(value = "分拣系统获取需求单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "需求单号", dataType = "string", paramType = "query", required = true)
    })
    @ResponseBody
    public Result getDetailsByCode(String code) {
        Result result = new Result();
        try {
            result.setData(requirementService.getDetailsByCode(code));
            result.setCode(MessageTemplate.OPERATE_SUCCESS.getCode());
        } catch (BusinessException be) {
            result.setCode(be.getCode());
        }
        return result;
    }

    /**
     * @Author: Green.Ge
     * @Description: 提供给外部系统的需求单号
     * @Date: 2016年05月13日
     */
    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    @ApiOperation(value = "外部系统获取需求单号")
    @ResponseBody
    public Result getDetailsByCode() {
        //code不存在
        Result result = new Result();
        try {
            result.setCode(MessageTemplate.OPERATE_SUCCESS.getCode());
            result.setStatus(MessageTemplate.OPERATE_SUCCESS.getCode());
            result.setData(requirementService.genCode());
        } catch (BusinessException be) {

        }
        return result;
    }
}
