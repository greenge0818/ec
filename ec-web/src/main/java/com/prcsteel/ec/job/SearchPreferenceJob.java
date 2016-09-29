package com.prcsteel.ec.job;

import com.prcsteel.ec.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by Rabbit on 2016/5/23.
 */
public class SearchPreferenceJob extends BaseJob {
    @Resource
    private SearchService searchService;

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseJob.class);

    @Override
    public void execute() {
        LOGGER.debug("execute job: searchPreference");
        searchService.searchPreferenceJob();
        LOGGER.debug("end job: searchPreference");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
