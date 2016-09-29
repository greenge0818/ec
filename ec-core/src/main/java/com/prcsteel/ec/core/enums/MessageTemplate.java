package com.prcsteel.ec.core.enums;

import com.prcsteel.ec.core.model.Constant;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * @author peanut
 * @description 返回前台信息模板
 * @date 2016/4/28 10:42
 */
public enum MessageTemplate {
    UNKNOW_EXCEPTION("0000", "未知错误！"),

    USER_NOT_SIGN_IN("0001", "用户未登录"),

    PHONE_EMPTY("1000", "手机号码为空！"),
    PHONE_EXISTS("1001", "手机号码已存在！"),
    PHONE_AVAILABLE("1002", "手机号码可用！"),
    PHONE_ERROR("1003", "手机号码不正确!"),
    PHONE_UNREGISTER("1004", "手机号码未注册!"),
    PHONE_RESET_SUCCESS("1005", "手机号修改成功!"),
    PHONE_RESET_ERROR("1006", "手机号修改失败!"),
    PHONE_RESET_EXCEPTION("1007", "手机号修改异常!"),

    SMS_SEND_SUCCESS("2000", "验证码发送成功!"),
    SMS_SEND_ERROR("2001", "验证码发送失败!"),
    SMS_SEND_TIMEOUT("2002", "验证码已失效!"),
    SMS_SEND_LIMITED("2003", "验证码发送太频繁!"),
    SMS_CHECK_ERROR("2004", "验证码错误!"),
    SMS_CHECK_DUPLICATE("2005", "验证码重复，请重新发送!"),
    SMS_MODULE_EMPTY("2006", "验证码模块为空!"),
    SMS_CODE_AVAILABLE("2007", "验证码可用!"),
    SMS_CODE_UNAVAILABLE("2008", "请先发送验证码!"),
    PWD_SEND_ERROR("2009", "密码发送失败!"),
    SMS_RECORD_INSERT_ERROR("2010", "短信记录生成失败!"),
    SMS_GEN_TOKEN_FAIL("2011", "发送短信时生成token失败"),
    SMS_CONTENT_EMPTY("2012", "短信内容为空"),
    SMS_REGISTER_SEND_LIMITED("2013", "注册验证码发送受限!"),

    USER_EMPTY("3000", "用户为空!"),
    USER_EXISTS("3001", "手机号已注册!"),
    USER_MOBILE_EMPTY("3002", "用户手机号为空!"),
    USER_PWD_EMPTY("3003", "用户密码为空!"),
    USER_CODE_EMPTY("3004", "验证码为空!"),
    USER_REGISTER_SUCCESS("3005", "用户注册成功!"),
    USER_REGISTER_ERROR("3006", "用户注册失败!"),
    USER_PWD_RESET_SUCCESS("3007", "用户密码重置成功!"),
    USER_PWD_RESET_ERROR("3008", "用户密码重置失败!"),
    USER_PWD_RESET_EXCEPTION("3009", "用户密码重置操作异常!"),
    USER_PHONE_NOT_MATCH("3010", "被操作的手机号与当前用户的手机号不一致，请刷新后再试!"),
    USER_GET_APPUSER_ERROR("3011", "获取APP用户失败"),
    USER_ACCOUNT_LOCKED("3012", "用户账号已被锁定!"),

    CART_ID_ERROR("4001", "购物车id非法!"),
    CART_WEIGHT_ERROR("4002", "无法批量修改购物车重量!"),
    CART_WEIGHT_CHECK_NULL_ERROR("4003", "重量和选中至少需要修改一个!"),
    CART_WEIGHT_CHECK_UPDATE_SAMETIME_ERROR("4004", "无法同时修改重量和选中!"),
    CART_SUBMIT_ERROR("4005", " 购物车提交失败!"),
    CART_SUBMIT_SUCCESS("4006", " 购物车提交成功!"),
    CART_ADD_ERROR("4007", " 购物车添加失败!"),
    CART_ADD_SUCCESS("4008", " 购物车添加成功!"),
    CART_UPDATE_ERROR("4009", " 购物车更新失败!"),
    CART_UPDATE_SUCCESS("4010", " 购物车更新成功!"),
    CART_DELETE_ERROR("4011", " 购物车删除失败!"),
    CART_DELETE_SUCCESS("4012", " 购物车删除成功!"),
    CART_GET_SUCCESS("4013", " 购物车获取成功!"),
    CART_EMPTY_ERROR("4014", " 购物车为空,提交失败!"),
    CART_RESOURCE_EMPTY("4015", "无资源可加入购物车!"),

    LOGIN_TYPE_EMPTY("5000", "用户登陆类型为空!"),
    LOGIN_PWD_EMPTY("5001", "登陆密码为空!"),
    LOGIN_SUCCESS("5002", "登陆成功!"),
    LOGIN_ERROR("5003", "登陆失败!"),
    LOGIN_PWD_ERROR("5004", "密码错误!"),

    UPLOAD_FILE_SUCCESS("6000", "上传文件成功!"),
    UPLOAD_FILE_ERROR("6001", "上传文件失败!"),
    UPLOAD_FILE_INCORRECT_FORMAT("6002", "文件格式不正确!"),
    UPLOAD_FILE_INCORRECT_SIZE("6003", "文件超过" + Constant.MAX_IMG_SIZE + "M!"),
    UPLOAD_FILE_NOT_FOUND("6004", "没有找到上传的文件!"),
    UPLOAD_FILE_ILLEGAL("6005", "非法上传文件!"),

    REQUIREMENT_SUBMIT_SUCCESS("7000", "需求提交成功!"),
    REQUIREMENT_SUBMIT_ERROR("7001", "需求提交失败!"),
    REQUIREMENT_SIZE_ERROR("7002", "采购需求过长!"),
    REQUIREMENT_SUBMIT_LIMITED("7003", "采购需求提交太频繁!"),
    REQUIREMENT_EMPTY("7004", "需求提交传入参数不能为空!"),
    REQUIREMENT_IMGLIST_EMPTY("7005", "图片列表不能为空!"),

    MEMBER_GET_TODO_SUCCESS("8000", "获取待办事项成功!"),
    MEMBER_GET_TODO_ERROR("8001", "获取待办事项失败!"),
    MEMBER_DATE_FORMAT_ERROR("8002", "时间格式错误!"),
    MEMBER_GET_CONSIGN_ORDER_SUCCESS("8003", "采购信息获取成功！"),

    INQUIRY_GET_SUCCESS("9000", "询价单获取成功！"),
    INQUIRY_GET_DETAIL_SUCCESS("9001", "询价单详情获取成功！"),

    SEARCH_HISTORY_INSERT_FAIL("10001", "添加搜索记录失败！"),
    SEARCH_HISTORY_GET_SUCCESS("10002", "搜索记录获取成功！"),
    SEARCH_HISTORY_DELETE_SUCCESS("10003", "搜索记录删除成功！"),
    SEARCH_HISTORY_DELETE_FAIL("10004", "搜索记录删除失败！"),
    SEARCH_PREFERENCE_GET_SUCCESS("10005", "搜索记录获取成功！"),
    SEARCH_PREFERENCE_DELETE_SUCCESS("10006", "偏好记录删除成功！"),
    SEARCH_PREFERENCE_DELETE_FAIL("10007", "偏好记录删除失败！"),

    HOT_RESOURCE_GET_SUCCESS("11001", "热门资源获取成功！"),
    HOT_RESOURCE_GET_FAIL("11002", "热门资源获取失败！"),

    RESOURCE_CATEGORY_GET_SUCCESS("13001", "品名获取成功！"),
    RESOURCE_MATERIAL_GET_SUCCESS("13002", "材质获取成功！"),
    RESOURCE_SPEC_GET_SUCCESS("13003", "规格获取成功！"),
    RESOURCE_FACTORY_GET_SUCCESS("13004", "厂家获取成功！"),

    CBMS_SERVER_ERROR("14001", "CBMS系统出错"),
    SM_SERVER_ERROR("14002", "找货系统出错"),
    PICK_SERVER_ERROR("14003", "分拣系统出错"),
    POINT_SERVER_ERROR("14004", "积分系统出错"),
    MC_SERVER_ERROR("14005", "行情系统出错"),
    ESB_SEND_SMS_ERROR("14006", "ESB发送短信出错"),
    ESB_GET_AD_ERROR("14007", "ESB获取广告出错"),
    JSON_PARSE_ERROR("14008", "JSON格式化出错"),
    MEM_OPT_ERROR("14009", "缓存操作失败"),

    VCODE_EMPTY_ERROR("15001", "图型验证码不能为空"),
    VCODE_INCORRECT_ERROR("15002", "图型验证码错误"),

    /**
     * 返回给外部系统信息
     */
    OPERATE_SUCCESS("0", "成功！"),
    REQUIREMENT_CODE_EMPTY("-1", "需求单号不能为空！"),
    REMOTE_STATUS_TO_EMPTY("-2", "设置成状态不能为空！"),
    REMOTE_CODE_EMPTY("-3", "外部单号不能为空！"),
    REMOTE_CREATED_EMPTY("-4", "外部单号生成时间不能为空！"),
    REQUIREMENT_CLOSED_DATE("-5", "关闭时间不能为空！"),
    REQUIREMENT_CLOSED_REASON_EMPTY("-6", "关闭理由不能为空！"),
    REMOTE_STATUS_TO_ERROR("-7", "需求单更新状态错误！"),
    REQUIREMENT_CODE_NOT_EXIST("-8", "指定需求单不存在！"),
    REMOTE_CREATED_DATE_ERROR("-9", "外部单号生成时间非法 ！"),
    REMOTE_CLOSED_DATE_ERROR("-10", "关闭时间非法！"),
    REQUIREMENT_UPDATE_FAIL("-11", "需求单更新失败！"),
    REQUIREMENT_PARAM_EMPTY("-12", "传入参数为空！"),
    REQUIREMENT_CREATED_EMPTY("-13", "需求单生成时间不能为空！"),
    REMOTE_USER_MOBILE_EMPTY("-14", "客户手机号不能为空！"),
    REMOTE_USER_NAME_EMPTY("-15", "客户姓名不能为空！"),
    REMOTE_USER_ACCOUNT_EMPTY("-16", "客户所属公司不能为空！"),
    REQUIREMENT_CREATED_DATE_ERROR("-17", "需求创建时间非法！"),
    REQUIREMENT_INSERT_FAIL("-18", "需求单新增失败！"),
    REMOTE_USER_INSERT_FAIL("-19", "客户新增失败！"),
    REMOTE_USER_MOBILE_FORMAT_ERROR("-20", "客户手机号格式不正确！"),
    REMOTE_USER_MOBILE_EXIST("-21", "客户手机号已存在！"),
    REMOTE_NEW_MOBILE_EMPTY("-22", "新手机号不能为空！"),
    REMOTE_NEW_MOBILE_FORMAT_ERROR("-23", "新手机号格式不正确！"),
    REMOTE_NEW_MOBILE_EXIST("-24", "新手机号已存在！"),
    REMOTE_USER_UPDATE_FAIL("-25", "客户更新失败！"),
    ONCE_MORE_JSON_PARSE_ERROR("-26", "APP再来一单JSON格式错误！"),
    CATEGORY_UUID_ERROR("-27", "品名UUID非法！"),
    MATERIAL_UUID_ERROR("-28", "材质UUID非法！"),
    FACTORY_ID_ERROR("-29", "厂家ID非法！"),
    CATEGORY_UUID_EMPTY("-30", "品名UUID不能为空！"),
    MATERIAL_UUID_EMPTY("-31", "材质UUID不能为空！"),
    FACTORY_ID_EMPTY("-32", "厂家ID不能为空！"),
    REMOTE_PWD_SEND_ERROR("-33", "动态密码发送失败！"),

    LOGIN_TOKEN_BLANK("-34", "token不能为空"),
    REMOTE_USER_ID_EMPTY("-35", "客户ID不能为空！"),
    REMOTE_ORDER_CODE_ERROR("-36", "外部单号错误！"),
    REQUIREMENT_CODE_FINISHED("-37", "需求单已完成，不能进行其他操作！"),
    PICK_OPERATE_ERROR("-38", "分检系统只能操作状态为NEW的需求单！"),
    SMART_OPERATE_ERROR("-39", "找货系统只能操作状态为PICKED、QUOTED、CLOSED、ACTIVATED的需求单！"),
    CBMS_OPERATE_ERROR("-40", "CBMS只能处理状态为QUOTED的需求单!"),
    REQUIREMENT_STAGESTATUS_ERROR("-41", "除报价可以重复报价外，其他状态不能重复！"),
    REMOTE_USER_STATUS_EMPTY("-42", "客户更新状态不能为空（1启用0禁用）！"),
    REMOTE_USER_NOT_EXIST("-43", "指定用户不存在！"),
    TIME_EMPTY("-44", "时间不能为空！"),
    TIME_ERROR("-45", "时间格式错误！"),
    PICK_REQUIREMENT_CODE_EXIST("-46", "分检新增的需求单单号已存在！"),
    VERSION_GET_FAIL("-47", "APP版本号获取失败！"),
    REQUIREMENT_CODE_CLOSED("-48", "需求单已关闭，只能进行激活操作！"),
    REQUIREMENT_IS_OPEN("-49", "需求单未关闭，不需要进行激活操作！"),
    DEVICE_NO_NULL_ERROR("-50", "设备号不能为空"),
    DEVICE_TYPE_NULL_ERROR("-51", "设备类型不能为空"),

    PHP_SUCCESS("200", "操作成功");
    private String code;
    private String msg;

    MessageTemplate(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据code获取信息
     *
     * @param code
     * @return
     */
    public static String getMsgByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return Arrays.asList(MessageTemplate.values()).stream().filter(e -> code.equals(e.getCode())).findFirst().get().getMsg();
    }
}
