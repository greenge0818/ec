package com.prcsteel.ec.service;

import com.prcsteel.ec.core.enums.OpType;
import com.prcsteel.ec.model.domain.ec.SystemOperationLog;

import java.util.List;

/**
 * @author peanut
 * @description 系统操作日志Service
 * @date 2016/4/29 10:54
 */
public interface SystemOperationLogService {
    /**
     * 插入操作日志
     *
     * @param sysOpLog
     * @return
     * @author peanut
     * @date 2016/04/29
     */
    int insert(SystemOperationLog sysOpLog);

    /**
     * 根据操作人guid,操作key获取日志对象---默认以创建日期倒序
     *
     * @param oprtGuid 操作人guid
     * @param oprtKey  操作key，例如用户登陆：USER_LOGIN
     * @return 日志对象集
     * @author peanut
     * @date 2016/05/09
     */
    List<SystemOperationLog> getLogByOprtGuidAndOprtKey(String oprtGuid, String oprtKey);

    /**
     * 添加登陆日志
     *
     * @param username 账号
     * @param password 密码或验证码
     * @return 影响行数
     * @author peanut
     * @date 2016/05/13
     */
    int insertLoginLog(String username, String password);

    /**
     * 添加日志模板
     *
     * @param opType     操作类型
     * @param parameters 提交的参数
     * @return 影响行数
     * @author Tiny
     * @date 2016/06/01
     */
    int insertModel(OpType opType, String parameters);
}
