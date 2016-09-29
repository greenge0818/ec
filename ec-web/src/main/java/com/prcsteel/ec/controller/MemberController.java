package com.prcsteel.ec.controller;

import com.prcsteel.ec.core.enums.ConsignOrderStatus;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.core.util.UserUtils;
import com.prcsteel.ec.model.domain.ec.User;
import com.prcsteel.ec.model.dto.CartDto;
import com.prcsteel.ec.model.dto.ConsignTabsDto;
import com.prcsteel.ec.service.CartService;
import com.prcsteel.ec.service.MemberService;
import com.prcsteel.ec.service.RequirementService;
import com.prcsteel.ec.service.UserService;
import com.prcsteel.ec.service.api.RestCbmsService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private CartService cartService;

    @Resource
    private RequirementService requirementService;

    @Resource
    private MemberService memberService;

    @Resource
    private RestCbmsService restCbmsService;

    @Value("${marketCenterDomain}")
    private String marketCenterDomain;

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @RequestMapping("")
    public String index(ModelMap out) {
        memberInfo(out);
        cartInfo(out);
        return "/member/index";
    }

    /**
     * 会员中心中转至修改手机号
     *
     * @return
     * @author peanut
     * @date 2016/05/05
     */
    @RequestMapping(value = "/resetphone", method = RequestMethod.GET)
    @ApiOperation("会员中心中转至修改手机号")
    public String resetPhone(ModelMap out) {
        String userPhone = UserUtils.getPrincipal();
        out.put("userPhone", (StringUtils.isNotBlank(userPhone) && StringUtil.isPhoneNumberCheck(userPhone)) ?
                StringUtil.truncatePhone(userPhone) : null);
        return "/member/resetphone";
    }

    /**
     * 会员中心中转至修改密码
     *
     * @return
     * @author peanut
     * @date 2016/05/16
     */
    @RequestMapping(value = "/modifypassword", method = RequestMethod.GET)
    @ApiOperation("会员中心中转至修改密码")
    public String modifyPassword(ModelMap out) {
        String userPhone = UserUtils.getPrincipal();
        out.put("userPhone", (StringUtils.isNotBlank(userPhone) && StringUtil.isPhoneNumberCheck(userPhone)) ?
                StringUtil.truncatePhone(userPhone) : null);
        return "/member/modifypassword";
    }

    /**
     * 采购管理页面
     *
     * @param out
     */
    @RequestMapping("/consignmanage")
    public String consignManage(ModelMap out) {
        cartInfo(out);
        //tab页标签
        try {
            out.put("tabs", memberService.getConsignTabs());
        } catch (BusinessException e) {
            out.put("tabs", consignResult());
            logger.error("采购管理页面获取失败：" + e.getMsg());
        }
        return "/member/consignmanage";
    }

    /**
     * 采购管理页面报错时的返回结果
     * @return
     */
    private List<ConsignTabsDto> consignResult(){
        List<ConsignTabsDto> re = new LinkedList<>();
        for (ConsignOrderStatus status : ConsignOrderStatus.values()) {
            ConsignTabsDto tab = new ConsignTabsDto();
            tab.setCode(status.getCode());
            tab.setMsg(status.getMsg());
            tab.setCount(0);
            re.add(tab);
        }
        return re;
    }

    /**
     * 询价管理页面
     */
    @RequestMapping(value = "/requirement", method = RequestMethod.GET)
    @ApiOperation("询价管理中转")
    public String requirement(ModelMap out) {
        User user = commonService.getCurrentUser();

        if (user != null) {
            //获取需求总数
            Map<String, Integer> map = requirementService.totalRequirement(user);
            out.put("all", map.get("allCount"));
            out.put("newCount", map.get("newCount"));
            out.put("pickedCount", map.get("pickedCount"));
            out.put("quotedCount", map.get("quotedCount"));
            out.put("finishedCount", map.get("finishedCount"));
            out.put("closedCount", map.get("closedCount"));

        }
        cartInfo(out);
        return "/requirement/index";
    }

    /**
     * 购物车信息
     *
     * @param out
     * @return
     */
    private void cartInfo(ModelMap out) {
        User user = commonService.getCurrentUser();
        int size = 0;
        if (user != null) {
            //用户不同公司的购物车资源总数量
            List<CartDto> list = cartService.selectByUserGuidAndCookieId(null);
            if (list != null) {
                size = list.stream().mapToInt(e -> e.getResourceList().size()).sum();
            }
            out.put("myCartCount", size);
            out.put("marketCenterDomain", marketCenterDomain);
        }
    }

    /**
     * 会员信息
     *
     * @param out
     * @return
     */
    private void memberInfo(ModelMap out) {
        User user = commonService.getCurrentUser();
        if (user != null) {

            //公司信息
            out.put("company", userService.getUserCompany(user.getMobile().toString()));

            //上次登陆时间
            out.put("lastLoginTime", userService.getUserLastLoginTime(user.getMobile()));

            //用户积分
            out.put("score", userService.getUserScore(user.getMobile()));
        }
    }

    /**
     * 获取积分
     *
     * @param out
     * @return
     */
    @RequestMapping("/getpoint")
    public String getpoint(ModelMap out) {
        memberInfo(out);
        cartInfo(out);
        return "/member/getpoint";
    }
}
