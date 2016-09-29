package com.prcsteel.ec.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Rolyer on 2016/5/23.
 */
public class DemoJob extends BaseJob {
    private final static Logger LOGGER = LoggerFactory.getLogger(BaseJob.class);

    @Override
    public void execute() {
        LOGGER.debug("execute job: demo");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
