package com.prcsteel.ec.service.impl;

import com.google.gson.Gson;
import com.prcsteel.ec.core.enums.OpType;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.model.domain.ec.SystemOperationLog;
import com.prcsteel.ec.model.domain.ec.User;
import com.prcsteel.ec.persist.dao.ec.SystemOperationLogDao;
import com.prcsteel.ec.persist.dao.ec.UserDao;
import com.prcsteel.ec.service.GlobalIdService;
import com.prcsteel.ec.service.SystemOperationLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author peanut
 * @description 系统操作日志Service
 * @date 2016/4/29 10:54
 */
@Service
public class SystemOperationLogServiceImpl implements SystemOperationLogService {

    @Resource
    private SystemOperationLogDao systemOperationLogDao;

    @Resource
    private UserDao userDao;

    @Resource
    private GlobalIdService globalIdService;

    @Override
    public int insert(SystemOperationLog sysOpLog) {
        return systemOperationLogDao.insert(sysOpLog);
    }


    /**
     * 根据操作人guid,操作key获取日志对象---默认以创建日期倒序
     *
     * @param oprtGuid 操作人guid
     * @param oprtKey  操作key，例如用户登陆：USER_LOGIN
     * @return 日志对象集
     * @author peanut
     * @date 2016/05/09
     */
    @Override
    public List<SystemOperationLog> getLogByOprtGuidAndOprtKey(String oprtGuid, String oprtKey) {

        if (StringUtils.isBlank(oprtGuid) || StringUtils.isBlank(oprtKey)) return null;

        return systemOperationLogDao.getLogByOprtGuidAndOprtKey(oprtGuid, oprtKey);
    }

    /**
     * 添加登陆日志
     *
     * @param username 账号
     * @param password 密码或验证码
     * @return 影响行数
     * @author peanut
     * @date 2016/05/13
     */
    @Override
    public int insertLoginLog(String username, String password) {
        if (StringUtils.isBlank(username)) {
            return 0;
        }
        User user = userDao.selectByPhone(username);
        if (user != null) {
            SystemOperationLog sysOpLog = new SystemOperationLog();
            sysOpLog.preInsert(globalIdService.getId());
            sysOpLog.setOperationKey(OpType.USER_LOGIN);
            sysOpLog.setOperationLevel(OpType.USER_LOGIN.getLevel());
            sysOpLog.setOperationLevelValue(OpType.USER_LOGIN.getLevel().getLevel());
            sysOpLog.setOperationName(OpType.USER_LOGIN.getDescription());
            sysOpLog.setParameters(new Gson().toJson(Arrays.asList(new String[]{username, password})));
            sysOpLog.setCreatedBy(user.getMobile());
            sysOpLog.setLastUpdatedBy(user.getMobile());
            sysOpLog.setOperatorGuid(user.getGuid());
            sysOpLog.setOperatorName(user.getMobile());

            return systemOperationLogDao.insert(sysOpLog);
        }
        return 0;
    }

    /**
     * 添加日志模板
     *
     * @param opType     操作类型
     * @param parameters 提交的参数
     * @return 影响行数
     * @author Tiny
     * @date 2016/06/01
     */
    @Override
    public int insertModel(OpType opType, String parameters) {
        if ((opType == null) || StringUtils.isBlank(parameters)) return 0;
        SystemOperationLog sysOpLog = new SystemOperationLog(Constant.SYS_USER_GUID, Constant.SYS_USER, opType,
                opType.getDescription(), opType.getLevel(), opType.getLevel().getLevel(), parameters,
                Constant.SYS_USER, Constant.SYS_USER);
        sysOpLog.preInsert(globalIdService.getId());
        return systemOperationLogDao.insert(sysOpLog);
    }
}
