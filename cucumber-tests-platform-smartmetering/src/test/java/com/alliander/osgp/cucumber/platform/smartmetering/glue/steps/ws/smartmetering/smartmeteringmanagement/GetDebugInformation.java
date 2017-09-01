/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.smartmetering.glue.steps.ws.smartmetering.smartmeteringmanagement;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.adapter.ws.schema.smartmetering.management.FindMessageLogsAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.management.FindMessageLogsAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.smartmetering.management.FindMessageLogsRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.management.FindMessageLogsResponse;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.cucumber.platform.smartmetering.PlatformSmartmeteringKeys;
import com.alliander.osgp.cucumber.platform.smartmetering.builders.logging.DeviceLogItemBuilder;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.management.FindMessageLogsRequestFactory;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.management.SmartMeteringManagementRequestClient;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.management.SmartMeteringManagementResponseClient;
import com.alliander.osgp.logging.domain.entities.DeviceLogItem;
import com.alliander.osgp.logging.domain.repositories.DeviceLogItemRepository;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GetDebugInformation {

    @Autowired
    private DeviceLogItemRepository logItemRepository;

    @Autowired
    private DeviceLogItemBuilder deviceLogItemBuilder;

    @Autowired
    private SmartMeteringManagementRequestClient<FindMessageLogsAsyncResponse, FindMessageLogsRequest> smartMeteringManagementRequestClient;

    @Autowired
    private SmartMeteringManagementResponseClient<FindMessageLogsResponse, FindMessageLogsAsyncRequest> smartMeteringManagementResponseClient;

    @Given("^there is debug information logged for the device$")
    public void thereIsDebugInformationLoggedForTheDevice() throws Throwable {
        final DeviceLogItem deviceLogItem = this.deviceLogItemBuilder.withDeviceIdentification(
                ScenarioContext.current().get(PlatformKeys.KEY_DEVICE_IDENTIFICATION).toString()).build();

        this.logItemRepository.save(deviceLogItem);
    }

    @When("^the get debug information request is received$")
    public void theGetDebugInformationRequestIsReceived(final Map<String, String> requestData) throws Throwable {
        final FindMessageLogsRequest findMessageLogsRequest = FindMessageLogsRequestFactory
                .fromParameterMap(requestData);
        final FindMessageLogsAsyncResponse findMessageLogsAsyncResponse = this.smartMeteringManagementRequestClient
                .doRequest(findMessageLogsRequest);

        assertNotNull("AsyncResponse should not be null", findMessageLogsAsyncResponse);
        ScenarioContext.current().put(PlatformSmartmeteringKeys.KEY_CORRELATION_UID,
                findMessageLogsAsyncResponse.getCorrelationUid());
    }

    @Then("^the device debug information should be in the response message$")
    public void theDeviceDebugInformationShouldBeInTheResponseMessage() throws Throwable {
        final FindMessageLogsAsyncRequest findMessageLogsAsyncRequest = FindMessageLogsRequestFactory
                .fromScenarioContext();
        final FindMessageLogsResponse findMessageLogsResponse = this.smartMeteringManagementResponseClient
                .getResponse(findMessageLogsAsyncRequest);

        assertNotNull("FindMessageLogsRequestResponse should not be null", findMessageLogsResponse);
        assertNotNull("Expected logs", findMessageLogsResponse.getMessageLogPage());
    }
}
