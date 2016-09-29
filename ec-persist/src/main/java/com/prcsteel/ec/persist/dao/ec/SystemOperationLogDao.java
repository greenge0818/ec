package com.prcsteel.ec.persist.dao.ec;


import com.prcsteel.ec.model.domain.ec.SystemOperationLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author peanut
 * @description 操作日志Dao
 * @date 2016/4/29 11:01
 */
@Repository
public interface SystemOperationLogDao {

    /**
     * 添加操作日志
     *
     * @param log
     * @return
     */
    int insert(SystemOperationLog log);

    /**
     * 根据操作人guid,操作key获取日志对象---默认以创建日期倒序
     *
     * @param oprtGuid 操作人guid
     * @param oprtKey  操作key，例如用户登陆：USER_LOGIN
     * @return 日志对象
     * @author peanut
     * @date 2016/05/09
     */
    List<SystemOperationLog> getLogByOprtGuidAndOprtKey(@Param("oprtGuid") String oprtGuid, @Param("oprtKey") String oprtKey);
}
