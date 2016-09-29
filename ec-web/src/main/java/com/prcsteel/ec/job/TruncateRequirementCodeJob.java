package com.prcsteel.ec.job;

import com.prcsteel.ec.persist.dao.ec.RequirementCodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 清空需求单号表
 * Created by peanut on 2016/07/22
 */
public class TruncateRequirementCodeJob extends BaseJob {
    @Resource
    private RequirementCodeDao requirementCodeDao;

    private final static Logger logger = LoggerFactory.getLogger(TruncateRequirementCodeJob.class);

    @Override
    public void execute() {
        logger.debug("execute job: truncate the table global_requirement_code begin ");
        requirementCodeDao.truncate();
        logger.debug("end job: truncate the table global_requirement_code end ");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
