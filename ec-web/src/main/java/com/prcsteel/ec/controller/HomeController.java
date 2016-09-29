package com.prcsteel.ec.controller;

import com.prcsteel.ec.core.enums.MessageTemplate;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.service.CacheService;
import com.prcsteel.ec.core.util.CookieUtil;
import com.prcsteel.ec.core.util.NumberUtil;
import com.prcsteel.ec.core.util.StringUtil;
import com.prcsteel.ec.dto.Result;
import com.prcsteel.ec.model.dto.CategoryCacheDto;
import com.prcsteel.ec.model.dto.RequirementItemDto;
import com.prcsteel.ec.model.dto.Smart2MarketResourceDto;
import com.prcsteel.ec.model.query.Market2SmartResourceQuery;
import com.prcsteel.ec.service.MarketService;
import com.prcsteel.ec.service.SearchService;
import com.prcsteel.ec.service.api.RestMarketCenterService;
import com.prcsteel.ec.util.VerifyCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("")
public class HomeController extends BaseController {

    @Value("${logoutUrl}")
    private String logoutUrl;

    @Value("${marketCenterDomain}")
    private String marketCenterDomain;

    @Resource
    SearchService searchService;

    @Resource
    MarketService marketService;

    @Resource
    RestMarketCenterService restMarketCenterService;

    @Resource
    private CacheService cacheService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("")
    public String index(ModelMap out) {
        try {
            out.put("hotMarket", commonService.getHotMarket());                                      //获取热门行情资讯
        } catch (BusinessException e) {
            logger.error("热门行情资讯加载错误：", e.getMsg());
        }
        try {
            out.put("steelStatistics", commonService.getSteelStatistics());                          //获取钢价汇总统计
        } catch (BusinessException e) {
            logger.error("钢价汇总统计加载错误：", e.getMsg());
        }
        try {
            out.put("friendshipLink", commonService.getFriendshipLink());               //获取友情链接列表
        } catch (BusinessException e) {
            logger.error("友情链接列表加载错误：", e.getMsg());
        }
        out.put("marketCenterDomain", marketCenterDomain);
        return "index";
    }

    @RequestMapping("/warning")
    public void warning() {
    }

    @RequestMapping("/fail")
    public void fail() {
    }

    @RequestMapping("/unauth")
    public void unauth() {
    }

    @RequestMapping("/remote-login")
    public void remoteLogin() {
    }

    @RequestMapping("/login")
    public void login(String tel) {
        commonService.login(tel);
    }

    @RequestMapping("/caslogin")
    public String caslogin() {
        return "caslogin";
    }

    @RequestMapping("/casloginDiagram")
    public String casloginDiagram() {
        return "casloginDiagram";
    }

    @RequestMapping("/logout")
    public String logout(String to) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        if (StringUtils.isNoneBlank(to)) {
            if ("login".equals(to)) {
                return "redirect:" + casServer + "/logout?service=" + loginUrl;
            }
        }
        return "redirect:" + logoutUrl;
    }

    /**
     * ajax 登出
     *
     * @return
     */
    @RequestMapping("/ajaxlogout")
    @ResponseBody
    public Result ajaxLogout() {

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        Result result = new Result();
        result.setCode(MessageTemplate.OPERATE_SUCCESS.getCode());
        return result;
    }

    /**
     * 钢铁超市首页----例：/market/
     *
     * @param market2SmartResourceQuery 资源查询对象
     * @return
     * @author peanut
     * @date 2016/05/18
     * @see Market2SmartResourceQuery
     */
    @RequestMapping("/market")
    public String marketIndex(ModelMap out, Market2SmartResourceQuery market2SmartResourceQuery, HttpServletRequest request) {
        try {
            market2SmartResourceQuery.setCookieId(CookieUtil.getCookieId(request));
            /**  没有采购城市，默认选择当前ip所在的中心城市 **/
            if (StringUtils.isBlank(market2SmartResourceQuery.getPurchaseCityIds())) {
                market2SmartResourceQuery.setPurchaseCityIds(marketService.getCenterCitysByIp(StringUtil.getIpAddr(request)));
            }
            return marketCommon(out, searchService.search(market2SmartResourceQuery), market2SmartResourceQuery, "market/list_", Constant.MARKET_TITLE, Constant.NO_PAGE_SIZE);
        } catch (BusinessException e) {
            return marketCommon(out, null, market2SmartResourceQuery, "market/list_", Constant.MARKET_TITLE, Constant.NO_PAGE_SIZE);
        }

    }

    /**
     * 钢铁超市首页-----通过list页面方式访问，例：market/list_5.html
     *
     * @param pageIndex 页码
     * @return
     * @author peanut
     * @date 2016/05/26
     */
    @RequestMapping("/market/list_{pageIndex}")
    public String marketIndex(ModelMap out, @PathVariable("pageIndex") int pageIndex, HttpServletRequest request) {
        Market2SmartResourceQuery market2SmartResourceQuery = new Market2SmartResourceQuery();
        try {
            market2SmartResourceQuery.setPageIndex(pageIndex);
            market2SmartResourceQuery.setCookieId(CookieUtil.getCookieId(request));

            /**  没有采购城市，默认选择当前ip所在的中心城市 **/
            if (StringUtils.isBlank(market2SmartResourceQuery.getPurchaseCityIds())) {
                market2SmartResourceQuery.setPurchaseCityIds(marketService.getCenterCitysByIp(StringUtil.getIpAddr(request)));
            }
            /**分页不进搜索历史 **/
            market2SmartResourceQuery.setIsSearchHistory(false);
            return marketCommon(out, searchService.search(market2SmartResourceQuery), market2SmartResourceQuery, "market/list_", Constant.MARKET_TITLE, Constant.PAGE_SIZE);
        } catch (BusinessException e) {
            return marketCommon(out, null, market2SmartResourceQuery, "market/list_", Constant.MARKET_TITLE, Constant.PAGE_SIZE);
        }
    }

    /**
     * 钢铁超市首页-----通过品名uuid方式或一系列查询条件方式访问，例：market/4a9io9d.... (4a9io9d... 为品名uuid)或 market/categoryuuid_ssss
     *
     * @param content 品名uuid
     * @return
     * @author peanut
     * @date 2016/05/26
     */
    @RequestMapping("/market/{content:.*}")
    public String marketIndex(ModelMap out, @PathVariable("content") String content, HttpServletRequest request) {
        Market2SmartResourceQuery market2SmartResourceQuery = new Market2SmartResourceQuery();
        try {
            market2SmartResourceQuery.setCookieId(CookieUtil.getCookieId(request));
            String categoryName;
            //以品名uuid存在即为一系列查询条件方式
            if (content.indexOf("categoryuuid_") >= 0) {
                parseQuery(market2SmartResourceQuery, content);
            } else {
                //以单品名方式查询
                market2SmartResourceQuery.setCategoryUuid(content);
            }
            categoryName = marketService.getCategoryName(market2SmartResourceQuery.getCategoryUuid());
            if (StringUtils.isNotBlank(categoryName)) {
                market2SmartResourceQuery.setCategoryName(categoryName);
            }

            if ("1".equals(market2SmartResourceQuery.getOrderWay())) {
                market2SmartResourceQuery.setOrderWay("DESC");
            } else {
                market2SmartResourceQuery.setOrderWay("ASC");
            }
            /**  没有采购城市，默认选择当前ip所在的中心城市 **/
            if (StringUtils.isBlank(market2SmartResourceQuery.getPurchaseCityIds())) {
                market2SmartResourceQuery.setPurchaseCityIds(marketService.getCenterCitysByIp(StringUtil.getIpAddr(request)));
            }
            /** 资源列表 **/
            Smart2MarketResourceDto dto = searchService.search(market2SmartResourceQuery);

            /** 查询对象添加品名名称 **/
            if (StringUtils.isNotBlank(categoryName)) {
                market2SmartResourceQuery.setCategoryName(categoryName);
            }

            return marketCommon(out, dto, market2SmartResourceQuery, "market/" + encodeName(content) + "/list_", null, Constant.NO_PAGE_SIZE);

        } catch (BusinessException e) {
            return marketCommon(out, null, market2SmartResourceQuery, "market/" + encodeName(content) + "/list_", null, Constant.NO_PAGE_SIZE);
        }
    }

    /**
     * 钢铁超市首页-----通过品名uuid方式及分页方式访问，例：market/4a9io9d....html (4a9io9d... 为品名uuid)
     *
     * @param content 品名uuid
     * @return
     * @author peanut
     * @date 2016/05/26
     */
    @RequestMapping("/market/{content:.*}/list_{pageIndex}")
    public String marketIndex(ModelMap out, @PathVariable("content") String content, @PathVariable("pageIndex") int pageIndex, HttpServletRequest request) {
        Market2SmartResourceQuery market2SmartResourceQuery = new Market2SmartResourceQuery();
        try {
            market2SmartResourceQuery.setCookieId(CookieUtil.getCookieId(request));
            String categoryName;
            //以品名uuid打头即为一系列查询条件方式
            if (content.indexOf("categoryuuid_") >= 0) {
                parseQuery(market2SmartResourceQuery, content);
            } else {
                //以单品名方式查询
                market2SmartResourceQuery.setCategoryUuid(content);
            }
            categoryName = marketService.getCategoryName(market2SmartResourceQuery.getCategoryUuid());
            if (StringUtils.isNotBlank(categoryName)) {
                market2SmartResourceQuery.setCategoryName(categoryName);
            }
            market2SmartResourceQuery.setPageIndex(pageIndex);

            if ("1".equals(market2SmartResourceQuery.getOrderWay())) {
                market2SmartResourceQuery.setOrderWay("DESC");
            } else {
                market2SmartResourceQuery.setOrderWay("ASC");
            }

            /**  没有采购城市，默认选择当前ip所在的中心城市 **/
            if (StringUtils.isBlank(market2SmartResourceQuery.getPurchaseCityIds())) {
                market2SmartResourceQuery.setPurchaseCityIds(marketService.getCenterCitysByIp(StringUtil.getIpAddr(request)));
            }

            /**分页不进搜索历史 **/
            market2SmartResourceQuery.setIsSearchHistory(false);
            /** 资源列表 **/
            Smart2MarketResourceDto dto = searchService.search(market2SmartResourceQuery);

            /** 查询对象添加品名名称 **/
            if (StringUtils.isNotBlank(categoryName)) {
                market2SmartResourceQuery.setCategoryName(categoryName);
            }

            return marketCommon(out, dto, market2SmartResourceQuery, "market/" + encodeName(content) + "/list_", null, pageIndex);
        } catch (BusinessException e) {
            return marketCommon(out, null, market2SmartResourceQuery, "market/" + encodeName(content) + "/list_", null, pageIndex);
        }
    }

    /**
     * 超市资源首页共用返回方法
     *
     * @param out
     * @param dto                       资源列表对象
     * @param market2SmartResourceQuery 查询对象
     * @param url                       分页地址
     * @return
     */
    private String marketCommon(ModelMap out, Smart2MarketResourceDto dto, Market2SmartResourceQuery market2SmartResourceQuery, String url, String title, int pageIndex) {
        String description = Constant.MARKET_DEFAULT_DESCRIPTION;
        String keywords = Constant.MARKET_DEFAULT_KEYWORDS;
        /** 资源列表 **/
        out.put("resourceDto", dto);

        /** 查询对象 **/
        out.put("query", market2SmartResourceQuery);

        /** 基础分页地址 **/
        out.put("url", url);

        try {
            /** 全国城市 **/
            out.put("citys", marketService.getAllCitys());
        } catch (BusinessException e) {
            logger.error("全国城市加载错误：", e.getMsg());
        }

        /** 标题, Description,Keywords*/
        if (title == null) {
            title = getTitle(market2SmartResourceQuery, pageIndex);
            String catetoryName = market2SmartResourceQuery.getCategoryName();
            String factoryName = market2SmartResourceQuery.getFactoryNames();
            String cityName = market2SmartResourceQuery.getTdkCityNames();
            description = Constant.MARKET_CUSTOM_DESCRIPTION.replaceAll("#category", catetoryName == null ? "" : catetoryName).replaceAll(
                    "#factory", factoryName == null ? "" : factoryName).replaceAll("#city", cityName == null ? "" : cityName);
            keywords = Constant.MARKET_CUSTOM_KEYWORDS.replace("#category", catetoryName == null ? "" : catetoryName).replace("#factory", factoryName == null ? "" : factoryName);
        }
        out.put("title", title);
        out.put("description", description);
        out.put("keywords", keywords);
        out.put("marketCenterDomain", marketCenterDomain);

        return "market/index";
    }

    /**
     * @ClassName: getTitle
     * @Description: 获取超市标题
     * @Author Tiny
     * @Date 2016年06月08日
     */
    private String getTitle(Market2SmartResourceQuery market2SmartResourceQuery, int pageIndex) {
        String title = "";
        String titleSuffix = pageIndex > 0 ? Constant.MARKET_TITLE_SUFFIX1.replace("#page", Integer.toString(pageIndex)) : Constant.MARKET_TITLE_SUFFIX;
        String factorys = market2SmartResourceQuery.getFactoryNames();
        String citys = market2SmartResourceQuery.getTdkCityNames();
        if (factorys == null && citys == null) {
            title = market2SmartResourceQuery.getCategoryName() + Constant.MARKET_PRICE + Constant.UNDERLINE + market2SmartResourceQuery.getCategoryName() + titleSuffix;
        }
        if (factorys == null && citys != null) {
            title = citys + market2SmartResourceQuery.getCategoryName() + Constant.UNDERLINE + market2SmartResourceQuery.getCategoryName() + titleSuffix;
        }
        if (citys == null && factorys != null) {
            title = factorys + market2SmartResourceQuery.getCategoryName() + Constant.MARKET_FACTORY + market2SmartResourceQuery.getCategoryName() + titleSuffix;
        }
        if (citys != null && factorys != null) {
            title = citys + market2SmartResourceQuery.getCategoryName() + Constant.MARKET_FACTORY + factorys + market2SmartResourceQuery.getCategoryName() + titleSuffix;
        }
        return title;
    }

    /**
     * 解析查询内容
     *
     * @param content 查询内容 例：categoryuuid_5ty_categoryname_中(转义)_material_asys,ssyss_factory_1,2,3_spec1_1,2,3,4_spec2_22-33_spec3_12_city_1,2,3,4_orderby_price_orderway_asc_page_2_pagesize_20
     * @return
     */
    private void parseQuery(Market2SmartResourceQuery market2SmartResourceQuery, String content) {
        List<String> list = Arrays.asList(content.split("_"));
        market2SmartResourceQuery.setCategoryUuid(getValue(list, "categoryuuid"));
        try {
            String cname = getValue(list, "categoryname");
            if (StringUtils.isNotBlank(cname)) {
                market2SmartResourceQuery.setCategoryName(URLDecoder.decode(cname, "utf-8")); //解码
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding failed!", e);
        }
        market2SmartResourceQuery.setMaterialUuids(getValue(list, "material"));
        market2SmartResourceQuery.setMaterialNames(marketService.getMaterialNames(market2SmartResourceQuery.getMaterialUuids(), market2SmartResourceQuery.getCategoryUuid()));
        market2SmartResourceQuery.setFactoryIds(getValue(list, "factory"));
        market2SmartResourceQuery.setFactoryNames(marketService.getFactoryNames(market2SmartResourceQuery.getFactoryIds(), market2SmartResourceQuery.getCategoryUuid()));
        market2SmartResourceQuery.setSpec1(getValue(list, "spec1"));
        market2SmartResourceQuery.setSpec2(getValue(list, "spec2"));
        market2SmartResourceQuery.setSpec3(getValue(list, "spec3"));
        market2SmartResourceQuery.setPurchaseCityIds(getValue(list, "city"));
        market2SmartResourceQuery.setOrderBy(StringUtils.isNotBlank(getValue(list, "orderby")) ? getValue(list, "orderby") : Constant.DEFAULT_ORDERBY);
        market2SmartResourceQuery.setOrderWay(StringUtils.isNotBlank(getValue(list, "orderway")) ? getValue(list, "orderway") : Constant.DEFAULT_ORDER_WAY);
        market2SmartResourceQuery.setPageIndex(StringUtils.isNumeric(getValue(list, "page")) ? Integer.parseInt(getValue(list, "page")) : Constant.DEFAULT_PAGE_INDEX);
        market2SmartResourceQuery.setPageSize(StringUtils.isNumeric(getValue(list, "pagesize")) ? Integer.parseInt(getValue(list, "pagesize")) : Constant.MARKET_PAGE_SIZE);
    }

    /**
     * 循环取得对应key的值
     *
     * @param list 列表
     * @param key  关键字
     * @return
     */
    private String getValue(List<String> list, String key) {
        for (int k = 0; k < list.size(); k++) {
            if (key.equals(list.get(k)) && (k + 1) <= list.size() - 1) {
                return list.get(k + 1);
            }
        }
        return null;
    }

    /**
     * 地址内容字符编码
     *
     * @param content
     * @return
     */
    private String encodeName(String content) {
        String cname = getValue(Arrays.asList(content.split("_")), "categoryname");
        try {
            if (StringUtils.isNotBlank(cname)) {
                content = content.replace(cname, URLEncoder.encode(cname, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding failed!", e);
        }
        return content;
    }

    /**
     * 跳转帮我找单独页
     *
     * @return
     */
    @RequestMapping("/helpfind")
    public String helpfind(ModelMap out) {
        out.put("marketCenterDomain", marketCenterDomain);
        return "/helpfind/index";
    }

    /**
     * 跳转app微信下载页
     *
     * @return
     */
    @RequestMapping("/appdownload")
    public String appdownload(HttpServletRequest request, ModelMap out) {
        String userAgent = request.getHeader("user-agent");
        if (userAgent.contains("MicroMessenger")) {//微信扫出来的，显示微信相关信息
            out.put("showWechat", true);
        }
        return "/appintro/download";
    }

    /**
     * app下载真正动作
     */
    @RequestMapping("/downloadapp")
    public String downloadapp(HttpServletRequest request) {
//        string[] keywords = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser" };
        String userAgent = request.getHeader("user-agent");
        String redirectUrl = "";
        if (StringUtils.isNotBlank(userAgent)) {
            if (userAgent.contains("MicroMessenger")) {//微信扫出来的，直接去微信下载页
                redirectUrl = "appdownload";
            } else {
                if (userAgent.contains("Android")) {
                    redirectUrl = "common/getfile?key=app/gw_market.apk";
                } else if (userAgent.contains("iPhone") || userAgent.contains("iPod") || userAgent.contains("iPad")) {
//                app = "gw_market.ipa";
                    redirectUrl = "https://itunes.apple.com/cn/app/id1139744712";
                } else if (userAgent.contains("Windows Phone")) {
                    redirectUrl = "common/getfile?key=app/gw_market.xap";
                } else {
                    //其他途径默认安卓
                    redirectUrl = "common/getfile?key=app/gw_market.apk";
                }
            }

        }

        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = "404";
        }

        return "redirect:" + redirectUrl;
    }

    /**
     * 跳转app介绍页
     *
     * @return
     */
    @RequestMapping("/appintro")
    public String appintro(ModelMap out) {
        out.put("marketCenterDomain", marketCenterDomain);
        return "/appintro/index";
    }

    /**
     * 跳转关于我们页
     *
     * @return
     */
    @RequestMapping("/aboutus")
    public String aboutUs(ModelMap out) {
        out.put("marketCenterDomain", marketCenterDomain);
        return "aboutus";
    }

    /**
     * 跳转广告页
     *
     * @return
     */
    @RequestMapping("/adintro")
    public String adintro(ModelMap out) {
        out.put("marketCenterDomain", marketCenterDomain);
        return "adintro";
    }

    /**
     * 404统一返回页面
     *
     * @return
     * @author peanut
     * @date 2016/06/03
     */
    @RequestMapping("/404")
    public String errorPageFor404() {
        return "404";
    }

    /**
     * 免责声明
     *
     * @return
     * @author Green
     * @date 2016/07/26
     */
    @RequestMapping("/disclaimer")
    public String disclaimer(ModelMap out) {
        out.put("marketCenterDomain", marketCenterDomain);
        return "disclaimer";
    }

    /**
     * 500统一返回页面
     *
     * @return
     * @author peanut
     * @date 2016/06/03
     */
    @RequestMapping("/500")
    public String errorPageFor500() {
        return "500";
    }

    private static final String RES_CODE = "code";
    private static final String RES_MSG = "msg";
    private static final String DEFAULT_CODE = "0";

    /**
     * CAS SSO Login
     *
     * @param out
     * @param request
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/sso")
    public String sso(ModelMap out, HttpServletRequest request) throws UnsupportedEncodingException {
        String code = request.getParameter(RES_CODE);
        String msg = new String(request.getParameter(RES_MSG).getBytes("iso-8859-1"), "utf-8");

        if (StringUtils.isBlank(code)) {
            code = DEFAULT_CODE;
        }

        out.put(RES_CODE, code);
        out.put(RES_MSG, msg);

        return "sso";
    }

    /**
     * 跳转着陆页单独页
     *
     * @return
     */
    @RequestMapping("/land")
    public String land() {
        return "/land/index";
    }

    /**
     * 给搜索引擎使用--品名列表(无分页)；默认第一页
     *
     * @return
     */
    @RequestMapping("/guide/name.html")
    public String name(ModelMap out) {
        List<CategoryCacheDto.CategoryClass.Nsort> list = commonService.land2();
        commonLand(out, 1, list);
        return "/land/land2";
    }

    /**
     * 给搜索引擎使用--品名列表(有分页)
     *
     * @return
     */
    @RequestMapping("/guide/name_{pageIndex}.html")
    public String name(ModelMap out, @PathVariable("pageIndex") int pageIndex) {
        List<CategoryCacheDto.CategoryClass.Nsort> list = commonService.land2();
        commonLand(out, pageIndex, list);
        return "/land/land2";
    }

    /**
     * 给搜索引擎使用--品名加单个钢厂列表(无分页)；默认第一页
     *
     * @return
     */
    @RequestMapping("/guide/factory.html")
    public String factory(ModelMap out) {
        List<RequirementItemDto> list = commonService.land3();
        commonLand(out, 1, list);
        return "/land/land3";
    }

    /**
     * 给搜索引擎使用--品名加单个钢厂列表(有分页)
     *
     * @return
     */
    @RequestMapping("/guide/factory_{pageIndex}.html")
    public String factory(ModelMap out, @PathVariable("pageIndex") int pageIndex) {
        List<RequirementItemDto> list = commonService.land3();
        commonLand(out, pageIndex, list);
        return "/land/land3";
    }

    /**
     * 给搜索引擎使用--品名加单个城市列表(无分页)；默认第一页
     *
     * @return
     */
    @RequestMapping("/guide/city.html")
    public String city(ModelMap out) {
        List<RequirementItemDto> list = commonService.land4();
        commonLand(out, 1, list);
        return "/land/land4";
    }

    /**
     * 给搜索引擎使用--品名加单个城市列表(有分页)
     *
     * @return
     */
    @RequestMapping("/guide/city_{pageIndex}.html")
    public String city(ModelMap out, @PathVariable("pageIndex") int pageIndex) {
        List<RequirementItemDto> list = commonService.land4();
        commonLand(out, pageIndex, list);
        return "/land/land4";
    }

    private void commonLand(ModelMap out, int pageIndex, List list) {
        int pageNum = list != null && !list.isEmpty() ? NumberUtil.pageCount(list.size(), Constant.LAND_SIZE) : 0;
        if (pageNum == 0) {
            out.put("resultList", new ArrayList<>());
            out.put("pageIndex", 1);
        }else if (pageIndex >= pageNum) {
            out.put("pageIndex", pageNum);
            out.put("resultList", list.subList((pageNum - 1) * Constant.LAND_SIZE, list.size()));
        } else if (pageIndex < 1) {
            out.put("pageIndex", 1);
            out.put("resultList", list.subList(0, Constant.LAND_SIZE));
        } else {
            out.put("pageIndex", pageIndex);
            out.put("resultList", list.subList((pageIndex - 1) * Constant.LAND_SIZE, pageIndex * Constant.LAND_SIZE));
        }
        out.put("total", list != null && !list.isEmpty() ? list.size() : 0);
        out.put("pageSize", Constant.LAND_SIZE);
        out.put("hotMarket", commonService.getHotMarket());                     //获取热门行情资讯
        out.put("steelStatistics", commonService.getSteelStatistics());         //获取钢价汇总统计
        out.put("marketCenterDomain", marketCenterDomain);
    }

    /**
     * 跳转网站导航页单独页
     *
     * @return
     */
    @RequestMapping("/guide")
    public String guide(ModelMap out) {
        out.put("marketCenterDomain", marketCenterDomain);
        return "/guide/index";
    }

    @RequestMapping("/validateCode")
    public void getValidateCode(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        //存入会话session
        cacheService.set(Constant.VALIDATE_CODE_CACHE_KEY + session.getId(), 30 * 1000, verifyCode.toLowerCase());
        //生成图片
        int w = 200, h = 80;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        response.getOutputStream().flush();
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
