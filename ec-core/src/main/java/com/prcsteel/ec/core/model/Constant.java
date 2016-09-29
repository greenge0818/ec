package com.prcsteel.ec.core.model;


import java.io.File;
import java.text.SimpleDateFormat;

/**
 * @ClassName: Constant
 * @Description: 常量类
 * @Author Green.Ge
 * @Date 2016年4月27日
 */

public class Constant {
    public static final String IMAGE_SUFFIX = "[jpg][gif][png][jpeg][bmp]";
    public static final String FILE_SUFFIX = "[doc][docx][xls][xlsx][wps][et][txt]";
    public static final Long MAX_IMG_SIZE = 2L;//2M
    public static final Long M_SIZE = 1024L * 1024L;//1M的size

    public static final Integer MEMCACHEREQUIREMENTIDTIMEOUT = 24 * 60 * 60;
    public static final Integer MEMCACHE_TODO_LIST_TIMEOUT = 5 * 60;
    public static final String IDS_REGEX = "^(\\d+,)*\\d+$";   //ids正则验证(1,2,3)
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";  //时间格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";  //时间格式
    public static final Integer PAGE_SIZE = 5;   //分页记录数
    public static final String CACHE_TODO_LIST = "TODO_LIST_";  //待办事项缓存key头部

    public static final String COOKIE_KEY = "userCookieId";   //客户端cookie的key
    public static final Integer COOKIE_MAX_AGE = 7 * 24 * 60 * 60;   //cookie时效是7天

    // 文件临时保存目录
    public static final String FILESAVETEMPPATH = "temp" + File.separator;

    /**
     * 短信验证码发送间隔时间:1分钟（毫秒为单位） add by peanut
     */
    public static final Long CODE_LIMITED_TIME = 1 * 60 * 1000L;

    /**
     * 短信验证码失效时间:30分钟（毫秒为单位） add by peanut
     */
    public static final Long CODE_EXPIRE_TIME = 30 * 60 * 1000L;

    /**
     * 超市游客操作时默认系统用户
     */
    public static final String SYS_USER = "SYS_USER";

    /**
     * 超市游客操作时默认系统用户GUID
     */
    public static final String SYS_USER_GUID = "GUID_9999999999";

    /**
     * 默认公司id add by peanut on 2015/05/09
     */
    public static final Long DEFAULT_SELLER_ID = 110l;

    /**
     * 默认公司名称 add by peanut on 2015/05/09
     */
    public static final String DEFAULT_SELLER_NAME = "杭州钢为网络科技有限公司";

    /**
     * 金额小数位数  add by peanut on 2015/05/09
     */
    public static final Integer MONEY_DIGIT = 2;

    /**
     * 采购需求长度
     */
    public static final Integer SIZE = 100;

    /**
     * 超市首页热门资源默认条数 add by Tiny on 2015/05/19
     */
    public static final Integer HOT_RESOURCE_SIZE = 10;

    /**
     * 超市首页热门行情资讯默认条数 add by Tiny on 2015/05/27
     */
    public static final Integer HOT_MARKET_SIZE = 12;

    /**
     * 超市列表默认每页面条数 add by peanut on 2015/05/19
     */
    public static final int MARKET_PAGE_SIZE = 15;

    /**
     * 超市列表默认页码 add by peanut on 2015/05/19
     */
    public static final int DEFAULT_PAGE_INDEX = 1;

    /**
     * 超市列表默认排序方式 add by peanut on 2015/05/19
     */
    public static final String DEFAULT_ORDER_WAY = "ASC";

    /**
     * 超市列表默认根据价格排序 add by peanut on 2015/05/26
     */
    public static final String DEFAULT_ORDERBY = "price";

    /**
     * 用户状态
     */
    public static final String USER_STATUS_ENABLE = "1";

    /**
     * IP获取城市的获取超时时间,单位：毫秒
     */
    public static final Integer GET_CITY_TIMEOUT = 1000;

    /**
     * 默认缓存失效时间 24小时 add by peanut on 2015/05/30
     */
    public static final Integer DEFAULT_CACHE_EXPIRED = 24 * 60 * 60;

    /**
     * 地区城市缓存key add by peanut on 2015/05/30
     */
    public static final String AREA_CACHE_KEY = "citys";

    /**
     * 品名缓存key  add by peanut on 2015/05/30
     */
    public static final String CATEGORY_CACHE_KEY = "allCategory";

    /**
     * 材质缓存key前缀  add by peanut on 2015/05/30
     */
    public static final String MATERIAL_CACHE_KEY_PRXFIX = "material_";

    /**
     * 钢厂缓存key前缀  add by peanut on 2015/05/30
     */
    public static final String FACTORY_CACHE_KEY_PRXFIX = "factory_";

    /**
     * 友情链接缓存key add by Tiny on 2015/06/08
     */
    public static final String FRIENDSHIP_LINK_CACHE_KEY = "FriendshipLink";

    /**
     * 手机长度
     */
    public static final Integer MOBILE_SIZE = 11;

    /**
     * 钢铁超市默认标题 add by Tiny on 2015/06/08
     */
    public static final String MARKET_TITLE = "钢铁超市_钢材价格_钢铁价格_钢材现货交易_钢为网";

    /**
     * 钢铁超市自定义标题后缀 add by Tiny on 2015/06/08
     */
    public static final String MARKET_TITLE_SUFFIX = "批发采购_钢为网";

    /**
     * 钢铁超市自定义标题后缀1 add by Tiny on 2015/06/14
     */
    public static final String MARKET_TITLE_SUFFIX1 = "批发采购_第#page页_钢为网";

    /**
     * 逗号 add by Tiny on 2015/06/08
     */
    public static final String COMMA = ",";

    /**
     * 下划线 add by Tiny on 2015/06/08
     */
    public static final String UNDERLINE = "_";

    /**
     * 钢铁超市自定义标题部分内容 add by Tiny on 2015/06/08
     */
    public static final String MARKET_PRICE = "价格";

    /**
     * 钢铁超市自定义标题部分内容 add by Tiny on 2015/07/20
     */
    public static final String MARKET_FACTORY = "厂家_";

    /**
     * 长度1 add by Tiny on 2015/06/08
     */
    public static final int SIZE_ONE = 1;

    /**
     * 默认无页码 add by Tiny on 2015/06/14
     */
    public static final int NO_PAGE_SIZE = 0;

    /**
     * 下载文件路径前缀 add by Tiny on 2015/06/16
     */
    public static final String FILE_URL_PREFIX = "common/getfile?key=";

    /**
     * 分检推送给超市的需求，返回给分检需求明细source='PHONE' add by Tiny on 2015/06/21
     */
    public static final String SOURCE_TO_PICK = "PHONE";

    public static final String SEND_SMS_SUCCESS = "短信发送成功";

    public static final String FROM = "EC";

    public static final String DATEFORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd_HH:mm:ss";
    public static final String SECRET = "f3b6d77cceea9adc612a124c14ca508b";//系统间通信密钥

    /**
     * 友情链接默认条数 add by Tiny on 2015/07/04
     */
    public static final Integer FRIENDSHIP_LINK_SIZE = 9999;

    /**
     * 友情链接类型 add by Tiny on 2015/07/04
     */
    public static final Integer FRIENDSHIP_LINK_TYPE = 0;

    public static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_TIME_FORMAT);

    /**
     * 掌柜指数缓存key前缀  add by Tiny on 2015/07/07
     */
    public static final String PRICE_INDEXS_LINK_CACHE_KEY_PRXFIX = "priceIndexs_";

    /**
     * 热门资源缓存key前缀  add by peanut on 2015/07/26
     */
    public static final String HOT_RESOURCE_CACHE_KEY_PRXFIX = "hot_resource_";

    /**
     * 掌柜指数长度  add by Tiny on 2015/07/07
     */
    public static final Integer PRICE_INDEXS_SIZE = 6;

    /**
     * 广告缓存key  add by Tiny on 2015/07/07
     */
    public static final String MARKET_AD_CACHE_KEY = "marketAd";

    /**
     * category materials info (contains name and uuid) cache key  add by peanut on 2015/08/18
     */
    public static final String CATEGORY_MATERIALS_CACHE_KEY = "category_materials_cache_key";

    /**
     * 悬浮广告缓存key  add by Rabbit on 2016/7/29
     */
    public static final String MARKET_FLOAT_AD_CACHE_KEY = "marketFloatAd";

    /**
     * 会员活动缓存ke  add by Tiny on 2015/07/07
     */
    public static final String ACTIVITIES_CACHE_KEY = "activities";

    /**
     * 会员活动长度  add by Tiny on 2015/07/07
     */
    public static final Integer ACTIVITIES_SIZE = 3;

    /**
     * 行情中心数据缓存失效时间 20分钟 add by Tiny on 2016/07/13
     */
    public static final Integer HOME_PAGE_CACHE_EXPIRED = 20 * 60;

    /**
     * 热门行情资讯缓存key  add by Tiny on 2015/07/13
     */
    public static final String HOT_MARKET_CACHE_KEY = "hotMarket";

    /**
     * 钢架汇总统计缓存key  add by Tiny on 2015/07/13
     */
    public static final String STEEL_STATISTICS_CACHE_KEY = "steelStatistics";

    /**
     * 钢铁超市默认description add by Tiny on 2015/07/15
     */
    public static final String MARKET_DEFAULT_DESCRIPTION = "钢为网钢铁超市为国内钢厂、钢材贸易商、钢材终端用户提供国内钢铁,钢材,螺纹钢,热轧卷板,冷轧卷板,中厚板,热轧带钢线上钢铁市场,钢材交易电商平台。";

    /**
     * 钢铁超市自定义description add by Tiny on 2015/07/15
     */
    public static final String MARKET_CUSTOM_DESCRIPTION = "钢为网钢铁超市为国内钢厂、钢材贸易商、钢材终端用户提供#factory#category批发采购，#category市场价格行情，#city#category生产厂家的线上钢铁市场,钢材交易电商平台。";

    /**
     * 钢铁超市默认Keywords add by Tiny on 2015/07/15
     */
    public static final String MARKET_DEFAULT_KEYWORDS = "钢铁,钢材,螺纹钢,热轧卷板,冷轧卷板,中厚板,热轧带钢,钢铁市场,钢材交易";

    /**
     * 钢铁超市自定义Keywords add by Tiny on 2015/07/15
     */
    public static final String MARKET_CUSTOM_KEYWORDS = "#category,#factory,批发采购,钢材价格";

    /**
     * 悬浮广告posId add by Rabbit on 2016/7/29
     */
    public static final Integer FLOAT_AD_POSID = 204;

    /**
     * 超市广告 add by Tiny on 2015/07/29
     */
    public static final Integer MARKET_AD_POSID = 203;

    /*
     * 提供给app的特权用户 add by Rabbit on 2016/8/1
     */
    public static final String APP_SUPER_USER = "13467310896";

    /**
     * 图片验证码KEY前缀
     */
    public static final String VALIDATE_CODE_CACHE_KEY = "vcode_";

    /**
     * 注册发送短信验证码限制时间:10分钟（毫秒为单位） add by Tiny
     */
    public static final Long REGISTER_SMS_LIMITED_TIME = 10 * 60 * 1000L;

    /**
     * 注册发送短信验证码锁定时间:4小时（毫秒为单位） add by Tiny
     */
    public static final Long REGISTER_SMS_LOCKED_TIME = 4 * 60 * 60 * 1000L;

    /**
     * 注册发送短信验证码限制次数:3次 add by Tiny
     */
    public static final Integer REGISTER_SMS_LIMITED_THREE_TIMES = 3;

	/**
     * APP消息推送标题
     */
    public static final String APP_NOTIFICATION_TITLE = "钢为掌柜";

    /**
     * APP消息推送正文
     */
    public static final String APP_NOTIFICATION_CONTENT = "您有一条新的消息，点击查看";

    /**
     * land2 缓存key  add by peanut on 2015/09/12
     */
    public static final String LAND2_CACHE_KEY = "land2";

    /**
     * land3 缓存key  add by peanut on 2015/09/12
     */
    public static final String LAND3_CACHE_KEY = "land3";

    /**
     * land4 缓存key  add by peanut on 2015/09/12
     */
    public static final String LAND4_CACHE_KEY = "land4";

    /**
     * land2,land3,land4 每页长度
     */
    public static final Integer LAND_SIZE = 20;
}
