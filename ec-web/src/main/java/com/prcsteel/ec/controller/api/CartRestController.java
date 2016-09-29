package com.prcsteel.ec.controller.api;

import com.prcsteel.ec.controller.BaseController;
import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.enums.ResultMsgType;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.util.CookieUtil;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.model.domain.ec.Cart;
import com.prcsteel.ec.service.ActiveMQService;
import com.prcsteel.ec.service.CartService;
import com.prcsteel.ec.service.CommonService;
import com.prcsteel.ec.validator.CartValidator;
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
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Controller
@RequestMapping("/api/cart/")
public class CartRestController extends BaseController {
    @Resource
    CartService cartService;

    @Resource
    CommonService commonService;

    @Value("${ut.switch}")
    private String ut;

    /**
     * 添加购物车
     *
     * @param cart
     */
    @ResponseBody
    @RequestMapping(value = "addcart", method = RequestMethod.POST)
    @ApiOperation("购物车新增资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryUuid", value = "品名uuid", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "categoryName", value = "品名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "factoryId", value = "厂家id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "factoryName", value = "厂家", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "materialUuid", value = "材质uuid", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "materialName", value = "材质", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "单价", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sellerId", value = "卖家id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sellerName", value = "卖家名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "cityId", value = "城市id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "cityName", value = "城市名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "warehouseId", value = "仓库id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "warehouseName", value = "仓库名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "spec1", value = "品规1", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "spec2", value = "品规2", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "spec3", value = "品规3", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "weight", value = "重量", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "weightConcept", value = "计重方式", dataType = "string", paramType = "query")
    })
    public Result addCart(Cart cart, BindingResult br, HttpServletRequest request) {
        Result result = new Result(ResultMsgType.BUSINESS);
        //run Spring validator manually
        new CartValidator().validate(cart, br);
        if (!br.hasErrors()) {
            try {
                cartService.addCart(cart, CookieUtil.getCookieId(request));
                result.setCode(MessageTemplate.CART_ADD_SUCCESS.getCode());
            } catch (BusinessException e) {
                return new Result(e.getCode());
            }
        } else {
            result.setCode(br.getAllErrors().get(0).getDefaultMessage());
        }
        return result;
    }

    /**
     * 修改购物车信息
     *
     * @param ids
     * @param weight
     * @param isChecked
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updatecart", method = RequestMethod.POST)
    @ApiOperation("修改购物车资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "weight", value = "weight", dataType = "Double", paramType = "query"),
            @ApiImplicitParam(name = "isChecked", value = "isChecked", dataType = "String", paramType = "query")
    })
    public Result updateCart(String ids, Double weight, String isChecked) {
        Result result = new Result();
        try {
            cartService.updateCart(ids, weight != null ? BigDecimal.valueOf(weight) : null, isChecked);
            result.setCode(MessageTemplate.CART_UPDATE_SUCCESS.getCode());
        } catch (BusinessException e) {
            return new Result(e.getCode());
        }
        return result;
    }

    /**
     * 删除购物车信息
     *
     * @param ids
     */
    @ResponseBody
    @RequestMapping(value = "delcartbyids", method = RequestMethod.POST)
    @ApiOperation("删除购物车资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids", dataType = "String", paramType = "query")
    })
    public Result delCartById(String ids) {
        try {
            cartService.delCartByIds(ids);
            return new Result(MessageTemplate.CART_DELETE_SUCCESS.getCode());
        } catch (BusinessException e) {
            return new Result(e.getCode());
        }
    }

    /**
     * 通过cookieId和客户guid获取购物车信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getcartinfo", method = RequestMethod.POST)
    @ApiOperation("获取购物车信息")
    public Result getCartInfo(HttpServletRequest request) {
        return new Result(MessageTemplate.CART_GET_SUCCESS.getCode(),
                cartService.selectByUserGuidAndCookieId(CookieUtil.getCookieId(request)), ResultMsgType.BUSINESS);
    }

    /**
     * 提交购物车到询价单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insertrequirement", method = RequestMethod.POST)
    @ApiOperation("提交购物车到询价单")
    public Result insertRequirement() {
        Result result = new Result();
        try {
            String code = cartService.insertRequirement();
            result.setCode(MessageTemplate.CART_SUBMIT_SUCCESS.getCode());
            result.setStatus(commonService.getHoliday());
            if (Boolean.parseBoolean(ut)) {
                result.setData(code);
            }
        } catch (BusinessException e) {
            result.setCode(e.getCode());
        }
        return result;
    }
}
