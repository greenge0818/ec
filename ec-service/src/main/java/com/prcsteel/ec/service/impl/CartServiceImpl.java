package com.prcsteel.ec.service.impl;

import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.enums.RemoteDataSource;
import com.prcsteel.ec.core.enums.RequirementStatus;
import com.prcsteel.ec.core.enums.RequirementType;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.model.domain.ec.Cart;
import com.prcsteel.ec.model.domain.ec.Requirement;
import com.prcsteel.ec.model.domain.ec.RequirementItem;
import com.prcsteel.ec.model.domain.ec.User;
import com.prcsteel.ec.model.dto.CartDto;
import com.prcsteel.ec.model.dto.ResourceDto;
import com.prcsteel.ec.model.model.Market2PickRequirement;
import com.prcsteel.ec.persist.dao.ec.CartDao;
import com.prcsteel.ec.persist.dao.ec.RequirementItemDao;
import com.prcsteel.ec.service.CartService;
import com.prcsteel.ec.service.CommonService;
import com.prcsteel.ec.service.GlobalIdService;
import com.prcsteel.ec.service.RequirementService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Transactional
@Service("cartService")
public class CartServiceImpl extends GenericDaoServiceImpl implements CartService {
    @Resource
    private CartDao cartDao;
    @Resource
    GlobalIdService globalIdService;
    @Resource
    RequirementService requirementService;
    @Resource
    RequirementItemDao requirementItemDao;
    @Resource
    CommonService commonService;

    /**
     * 添加购物车
     *
     * @param cart
     */
    @Override
    public void addCart(Cart cart, String cookieId) {
        if (StringUtils.isBlank(cookieId)) {
            throw new BusinessException(MessageTemplate.CART_ADD_ERROR.getCode(), MessageTemplate.CART_ADD_ERROR.getMsg());
        }
        cart.preInsert(globalIdService.getId());
        User user = commonService.getCurrentUser();
        if (user == null) {
            cart.setCookieId(cookieId);
            cart.setCreatedBy(cookieId);
            cart.setLastUpdatedBy(cookieId);
        } else {
            cart.setUserGuid(user.getGuid());
        }
        cart.setWeight(cart.getWeight().setScale(3, BigDecimal.ROUND_HALF_UP));
        if (cart.getPrice() != null) {
            cart.setPrice(cart.getPrice().setScale(3, BigDecimal.ROUND_HALF_UP));
            cart.setAmount(cart.getPrice().multiply(cart.getWeight()).setScale(3, BigDecimal.ROUND_HALF_UP));
        }


        //添加钢为默认卖家
        cart.setSellerId(Constant.DEFAULT_SELLER_ID);
        cart.setSellerName(Constant.DEFAULT_SELLER_NAME);
        Long oldId = cartDao.selectByParam(cart);      //check有没有相同资源
        if (oldId == null && cartDao.insertSelective(cart) != 1) {   //没有的话直接插入
            throw new BusinessException(MessageTemplate.CART_ADD_ERROR.getCode(), MessageTemplate.CART_ADD_ERROR.getMsg());
        }else if(oldId != null && cartDao.updateWeightAndPrice(oldId, cart.getWeight(), cart.getPrice()) != 1){  //否则，对原资源做更新
            throw new BusinessException(MessageTemplate.CART_ADD_ERROR.getCode(), MessageTemplate.CART_ADD_ERROR.getMsg());
        }
    }

    /**
     * 批量修改购物车
     *
     * @param ids
     * @param weight
     * @param isChecked
     */
    @Override
    public void updateCart(String ids, BigDecimal weight, String isChecked) {
        if (weight == null && isChecked == null) {  //参数不能都为null
            throw new BusinessException(MessageTemplate.CART_WEIGHT_CHECK_NULL_ERROR.getCode(), MessageTemplate.CART_WEIGHT_CHECK_NULL_ERROR.getMsg());
        }
        if (weight != null && isChecked != null) {  //不能同时修改重量和选中
            throw new BusinessException(MessageTemplate.CART_WEIGHT_CHECK_UPDATE_SAMETIME_ERROR.getCode(), MessageTemplate.CART_WEIGHT_CHECK_UPDATE_SAMETIME_ERROR.getMsg());
        }
        if (ids != null && Pattern.matches(Constant.IDS_REGEX, ids)) {
            String[] id = ids.split(",");
            if (id.length > 1 && weight != null) {
                throw new BusinessException(MessageTemplate.CART_WEIGHT_ERROR.getCode(), MessageTemplate.CART_WEIGHT_ERROR.getMsg());
            }
            if (cartDao.batchUpdateWeightAndChecked(ids, weight, isChecked) != id.length) {
                throw new BusinessException(MessageTemplate.CART_UPDATE_ERROR.getCode(), MessageTemplate.CART_UPDATE_ERROR.getMsg());
            }
        } else {
            throw new BusinessException(MessageTemplate.CART_ID_ERROR.getCode(), MessageTemplate.CART_ID_ERROR.getMsg());
        }
    }

    /**
     * 删除购物车信息
     *
     * @param ids
     */
    @Override
    public void delCartByIds(String ids) {
        if (ids != null && Pattern.matches(Constant.IDS_REGEX, ids)) {
            if (cartDao.batchDeleteByIds(ids) != ids.split(",").length) {
                throw new BusinessException(MessageTemplate.CART_DELETE_ERROR.getCode(), MessageTemplate.CART_DELETE_ERROR.getMsg());
            }
        } else {
            throw new BusinessException(MessageTemplate.CART_ID_ERROR.getCode(), MessageTemplate.CART_ID_ERROR.getMsg());
        }
    }

    /**
     * 通过cookieId和客户guid获取购物车信息
     *
     * @param cookieId
     * @return
     */
    @Override
    public List<CartDto> selectByUserGuidAndCookieId(String cookieId) {
        User user = commonService.getCurrentUser();
        List<CartDto> list = new LinkedList<>();
        List<ResourceDto> carts = new LinkedList<>();
        if (user != null && StringUtils.isNotBlank(user.getGuid())) {
            carts = cartDao.selectByUserGuidAndCookieId(user.getGuid(), null);  //如果为已登录客户，就不用cookieId去取了
        } else if (StringUtils.isNotBlank(cookieId)) {
            carts = cartDao.selectByUserGuidAndCookieId(null, cookieId);
        }
        Map<Long, List<ResourceDto>> resourceGroupList = carts.stream().collect(Collectors.groupingBy(ResourceDto::getSellerId, Collectors.toList()));
        for (Long sellerId : resourceGroupList.keySet()) {
            CartDto dto = new CartDto();
            dto.setSellerId(sellerId);
            dto.setSellerName(resourceGroupList.get(sellerId).get(0).getSellerName());
            dto.setResourceList(resourceGroupList.get(sellerId));
            list.add(dto);
        }
        return list;
    }

    /**
     * 把购物车信息归属从cookieId改为客户guid（客户登陆时调用）
     *
     * @param cookieId
     */
    @Override
    public void updateCartUserGuidByCookieId(String cookieId) {
        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        cartDao.updateCartUserGuidByCookieId(user.getGuid(), cookieId);
    }

    /**
     * 提交购物车
     */
    @Override
    public String insertRequirement() {
        User user = commonService.getCurrentUser();
        if (user == null) {
            throw new BusinessException(MessageTemplate.USER_NOT_SIGN_IN.getCode(), MessageTemplate.USER_NOT_SIGN_IN.getMsg());
        }
        List<Cart> carts = cartDao.selectCheckedByUserGuid(user.getGuid());
        if (carts.isEmpty()) {
            throw new BusinessException(MessageTemplate.CART_EMPTY_ERROR.getCode(), MessageTemplate.CART_EMPTY_ERROR.getMsg());
        }
        Requirement requirement = new Requirement();
        requirement.setUserGuid(user.getGuid());
        requirement.setSource(RemoteDataSource.WEB.getCode());
        requirement.setType(RequirementType.CART.getCode());
        requirement.setStageStatus(RequirementStatus.NEW.getCode());

        //插入详情
        List<RequirementItem> requirementItems = new LinkedList<>();
        for (Cart cart : carts) {
            RequirementItem requirementItem = new RequirementItem(cart);
            requirementItem.preInsert(globalIdService.getId());
            if (requirementItem.getCreatedBy() == null) {
                requirementItem.setCreatedBy(user.getGuid());
            }
            if (requirementItem.getLastUpdatedBy() == null) {
                requirementItem.setLastUpdatedBy(user.getGuid());
            }
            requirementItems.add(requirementItem);

        }
        requirementService.submitRequirment(requirement, null, requirementItems);
        delCartByIds(StringUtils.join(Arrays.asList(carts.stream().map(Cart::getId).distinct().toArray(Long[]::new)), ','));
        //向APP推送消息
        commonService.sendNotification(user.getMobile());
        return requirement.getCode();
    }
}
