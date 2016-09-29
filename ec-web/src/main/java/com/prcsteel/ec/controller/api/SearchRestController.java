package com.prcsteel.ec.controller.api;

import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.enums.ResultMsgType;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.util.CookieUtil;
import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.service.SearchService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by myh on 2016/5/16.
 */
@Controller
@RequestMapping("/api/search/")
public class SearchRestController {
    @Resource
    SearchService searchService;

    /**
     * 获取最近搜索数据
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getRecentSearchList", method = RequestMethod.POST)
    @ApiOperation("获取最近搜索数据")
    public Result getRecentSearchList(HttpServletRequest request) {
        return new Result(MessageTemplate.SEARCH_HISTORY_GET_SUCCESS.getCode(),
                searchService.getRecentSearchList(CookieUtil.getCookieId(request)), ResultMsgType.BUSINESS);
    }

    /**
     * 按id（软）删除最近搜索记录
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "delById", method = RequestMethod.POST)
    @ApiOperation("按id（软）删除最近搜索记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Long", paramType = "query")
    })
    public Result delById(Long id, HttpServletRequest request) {
        try {
            searchService.delById(id, CookieUtil.getCookieId(request));
            return new Result(MessageTemplate.SEARCH_HISTORY_DELETE_SUCCESS.getCode());
        } catch (BusinessException e) {
            return new Result(e.getCode());
        }
    }

    /**
     * 获取猜你喜欢数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getSearchPreferenceList", method = RequestMethod.POST)
    @ApiOperation("获取猜你喜欢数据")
    public Result getSearchPreferenceList() {
        try {
            return new Result(MessageTemplate.SEARCH_PREFERENCE_GET_SUCCESS.getCode(),
                    searchService.getSearchPreferenceList(), ResultMsgType.BUSINESS);
        } catch (BusinessException e) {
            return new Result(e.getCode());
        }
    }

    /**
     * 按id（软）删除猜你喜欢记录
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "delSearchPreference", method = RequestMethod.POST)
    @ApiOperation("按id（软）删除猜你喜欢记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Long", paramType = "query")
    })
    public Result delSearchPreference(Long id) {
        try {
            searchService.delSearchPreference(id);
            return new Result(MessageTemplate.SEARCH_PREFERENCE_DELETE_SUCCESS.getCode());
        } catch (BusinessException e) {
            return new Result(e.getCode());
        }
    }

    /**
     * @Author: Tiny
     * @Description: 获取热门资源
     * @Date: 2016年05月18日
     */
    @ResponseBody
    @RequestMapping(value = "getHotResourceList", method = RequestMethod.POST)
    @ApiOperation("获取热门资源")
    public Result getHotResourceList(HttpServletRequest request) {
        try {
            return new Result(MessageTemplate.HOT_RESOURCE_GET_SUCCESS.getCode(),
                    searchService.getHotResourceList(StringUtil.getCity(request)), ResultMsgType.BUSINESS);
        } catch (BusinessException e) {
            return new Result(e.getCode());
        }
    }
}
