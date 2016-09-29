package com.prcsteel.ec.controller.api;

import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.model.dto.CategoryMaterials;
import com.prcsteel.ec.model.dto.SmartVo;
import com.prcsteel.ec.service.MarketService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Rabbit on 2015/11/17.
 */
@Controller
@RequestMapping("/api/market/")
public class MarketRestController {

    private Logger logger= LoggerFactory.getLogger(MarketRestController.class);

    @Resource
    private MarketService marketService;

    //init
    @RequestMapping(value = "getsortandnsort", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("获取所有品名")
    public SmartVo getSortAndNsort(){
        try{
            return marketService.getSortAndNsort();
        }catch (BusinessException e) {
            return new SmartVo(e.getCode(), null);
        }
    }

    //after select nsort
    @RequestMapping(value = "getmaterial", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("根据品名uuid获取材质")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nosrtUUID", value = "品名uuid", dataType = "String", paramType = "query")
    })
    public SmartVo getMaterial(String nosrtUUID){
        try{
            return marketService.getMaterial(nosrtUUID);
        }catch (BusinessException e) {
            return new SmartVo(e.getCode(), null);
        }
    }

    @RequestMapping(value = "getfactory", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("根据品名uuid获取钢厂")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nosrtUUID", value = "品名uuid", dataType = "String", paramType = "query")
    })
    public SmartVo getFactory(String nosrtUUID){
        try{
            return marketService.getFactory(nosrtUUID);
        }catch (BusinessException e) {
            return new SmartVo(e.getCode(), null);
        }
    }

    //after select material
    @RequestMapping(value = "getspec", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("根据品名uuid和材质uuid获取规格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nosrtUUID", value = "品名uuid", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "materialUUID", value = "材质uuid", dataType = "String", paramType = "query")
    })
    public SmartVo getSpec(String nosrtUUID, String materialUUID) {
        try{
            return marketService.getSpec(nosrtUUID, materialUUID);
        }catch (BusinessException e) {
            return new SmartVo(e.getCode(), null);
        }
    }

    /**
     * get all category materials info, contains name and uuid
     *
     * @return
     * @author peanut
     * @date 2016/08/18
     */
    @RequestMapping(value = "getallcategorymaterials", method = RequestMethod.GET)
    @ResponseBody
    public List<CategoryMaterials> getAllCategoryMaterials() {
        try {
            return marketService.getAllCategoryMaterials();
        } catch (Exception e) {
            logger.error("get all category materials info (contain name and uuid) error ,{}",e.getMessage());
            return null;
        }
    }


}
