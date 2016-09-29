package com.prcsteel.ec.service;

import com.google.gson.Gson;
import com.prcsteel.ec.core.enums.OpType;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.oplog.OpLog;
import com.prcsteel.ec.core.oplog.OpParam;
import com.prcsteel.ec.core.util.ObjectUtil;
import com.prcsteel.ec.core.util.UserUtils;
import com.prcsteel.ec.model.domain.ec.SystemOperationLog;
import com.prcsteel.ec.model.domain.ec.User;
import com.prcsteel.ec.persist.dao.ec.UserDao;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 切面
 *
 * @author peanut
 */
@Aspect
@Component
public class OpLogAspect {

    @Resource
    private UserDao userDao;

    @Resource
    private SystemOperationLogService systemOperationLogService;

    @Resource
    private GlobalIdService globalIdService;

    private static final Logger logger = LoggerFactory.getLogger(OpLogAspect.class);

    private static final int PARAMS_MAX_LENGTH = 1000;

    public void addOpLog(JoinPoint point, OpLog opLog) {
        try {
            // 查找参数配置，将参数的值提取出来
            Map<String, String> paramsDtoList = getParamsValue(point);
            // 保存行为日志
            saveOpLog(opLog, paramsDtoList);
        } catch (Exception e) {
            logger.error("Add operation log failed!", e);
        }
    }

    private Map<String, String> getParamsValue(JoinPoint point) {
        Map<String, String> paramsMap = new HashMap<>();
        Object[] args = point.getArgs();
        MethodSignature ms = (MethodSignature) point.getSignature();
        OpParam[] opParams = ms.getMethod().getAnnotationsByType(OpParam.class);
        String[] parametersNames = ms.getParameterNames();
        // 查找参数，填充参数的值
        for (OpParam p : opParams) {
            if (p.index() >= args.length) {
                throw new IndexOutOfBoundsException("OpLog OpParams index out of bounds");
            }
            String paramValue = p.defaultValue();
            Object obj;
            if (p.index() > 0) {
                if (args[p.index()] != null) {
                    obj = args[p.index()];
                    paramValue = getParamString(obj);
                }
            } else {
                String pname = p.name();
                if (StringUtils.isBlank(pname)) {
                    pname = p.value();
                }
                for (int i = 0; i < parametersNames.length; i++) {
                    if (parametersNames[i].equals(pname)) {
                        paramValue = getParamString(args[i]);
                        break;
                    }
                }
                if (!StringUtils.isBlank(p.name()) && StringUtils.isEmpty(paramValue)) {  //直接指定value值
                    paramValue = p.value();
                }
            }
            paramsMap.put(StringUtils.isEmpty(p.name()) ? p.value() : p.name(), paramValue);//未指定name，则使用方法参数名作为key
        }
        return paramsMap;
    }

    private void saveOpLog(OpLog opLog, Map<String, String> paramsDtoList) {
        OpType opType = opLog.value();

        String phone = UserUtils.getPrincipal();
        User user  = null;
        if(StringUtils.isNotBlank(phone)){
            user = userDao.selectByPhone(phone);
        }
        SystemOperationLog sysOpLog = new SystemOperationLog();
        sysOpLog.preInsert(globalIdService.getId());
        sysOpLog.setOperationKey(opType);
        sysOpLog.setOperationLevel(opType.getLevel());
        sysOpLog.setOperationLevelValue(opType.getLevel().getLevel());
        sysOpLog.setOperationName(opType.getDescription());
        sysOpLog.setParameters(new Gson().toJson(paramsDtoList));

        String userName = Constant.SYS_USER;
        String userGuid = Constant.SYS_USER_GUID;
        if (user != null) {
            userName = user.getMobile();
            userGuid = user.getGuid();
        }

        sysOpLog.setCreatedBy(userName);
        sysOpLog.setLastUpdatedBy(userName);
        sysOpLog.setOperatorGuid(userGuid);
        sysOpLog.setOperatorName(userName);


        if (sysOpLog.getParameters().length() > PARAMS_MAX_LENGTH) {
            sysOpLog.setParameters(sysOpLog.getParameters().substring(0, PARAMS_MAX_LENGTH));
        }
        systemOperationLogService.insert(sysOpLog);
    }

    private String getParamString(Object obj) {
        if (obj == null) {
            return "";
        }
        String str;
        if (ObjectUtil.isBaseDataType(obj)) {
            str = obj.toString();
        } else {
            str = new Gson().toJson(obj);
        }
        return str;
    }
}
