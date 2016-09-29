package com.prcsteel.ec.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

/**
 * Represents sample route builder.
 *
 * Created by Rolyer on 2016/5/23.
 */
public final class ListenerRouteBuilder extends RouteBuilder {

    @Value("${amq.assRequirement}")
    private String assRequirement;

    @Value("${amq.assRequirementStatus}")
    private String assRequirementStatus;

    @Value("${amq.smRequirementStatus}")
    private String smRequirementStatus;

    @Value("${amq.cbmsRequirementStatus}")
    private String cbmsRequirementStatus;

    @Value("${amq.addMarketUser}")
    private String addMarketUser;

    @Value("${amq.updateMarketUser}")
    private String updateMarketUser;

    @Value("${amq.changeContactStatus}")
    private String changeContactStatus;

    @Override
    public void configure() throws Exception {
        from("activemq:" + assRequirement).to("activemqHandler");
        from("activemq:" + assRequirementStatus).to("assRequirementStatusHandler");
        from("activemq:" + smRequirementStatus).to("smRequirementStatusHandler");
        from("activemq:" + cbmsRequirementStatus).to("cbmsRequirementStatusHandler");
        from("activemq:" + addMarketUser).to("addMarketUserHandler");
        from("activemq:" + updateMarketUser).to("updateMarketUserHandler");
        from("activemq:" + changeContactStatus).to("changeContactStatusHandler");
    }

}
