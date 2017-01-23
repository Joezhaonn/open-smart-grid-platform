/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.steps.ws.core.configurationmanagement;

import static com.alliander.osgp.platform.cucumber.core.Helpers.getString;
import static com.alliander.osgp.platform.cucumber.core.Helpers.saveCorrelationUidInScenarioContext;

import java.util.Map;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.alliander.osgp.adapter.ws.schema.core.common.AsyncRequest;
import com.alliander.osgp.adapter.ws.schema.core.common.OsgpResultType;
import com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.GetConfigurationAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.GetConfigurationAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.GetConfigurationRequest;
import com.alliander.osgp.adapter.ws.schema.core.configurationmanagement.GetConfigurationResponse;
import com.alliander.osgp.platform.cucumber.config.CoreDeviceConfiguration;
import com.alliander.osgp.platform.cucumber.core.ScenarioContext;
import com.alliander.osgp.platform.cucumber.steps.Defaults;
import com.alliander.osgp.platform.cucumber.steps.Keys;
import com.alliander.osgp.platform.cucumber.steps.ws.core.firmwaremanagement.GetFirmwareVersionSteps;
import com.alliander.osgp.platform.cucumber.support.ws.core.CoreConfigurationManagementClient;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * Class with all the firmware requests steps
 */
public class GetConfigurationSteps {
    @Autowired
    private CoreDeviceConfiguration configuration;

    @Autowired
    private CoreConfigurationManagementClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(GetFirmwareVersionSteps.class);

    /**
     * Sends a Get Configuration request to the platform for a given device
     * identification.
     *
     * @param requestParameters
     *            The table with the request parameters.
     * @throws Throwable
     */
    @Given("^receiving a get configuration request$")
    public void receivingAGetConfigurationRequest(final Map<String, String> requestParameters) throws Throwable {
        final GetConfigurationRequest request = new GetConfigurationRequest();
        request.setDeviceIdentification(
                getString(requestParameters, Keys.KEY_DEVICE_IDENTIFICATION, Defaults.DEFAULT_DEVICE_IDENTIFICATION));

        try {
            ScenarioContext.Current().put(Keys.RESPONSE, this.client.getConfiguration(request));
        } catch (final SoapFaultClientException ex) {
            ScenarioContext.Current().put(Keys.RESPONSE, ex);
        }
    }

    /**
     * The check for the response from the Platform.
     *
     * @param expectedResponseData
     *            The table with the expected fields in the response.
     * @note The response will contain the correlation uid, so store that in the
     *       current scenario context for later use.
     * @throws Throwable
     */
    @Then("^the get configuration async response contains$")
    public void theGetConfigurationResponseContains(final Map<String, String> expectedResponseData) throws Throwable {
        final GetConfigurationAsyncResponse response = (GetConfigurationAsyncResponse) ScenarioContext.Current()
                .get(Keys.RESPONSE);

        Assert.assertNotNull(response.getAsyncResponse().getCorrelationUid());
        Assert.assertEquals(getString(expectedResponseData, Keys.KEY_DEVICE_IDENTIFICATION),
                response.getAsyncResponse().getDeviceId());

        // Save the returned CorrelationUid in the Scenario related context for
        // further use.
        saveCorrelationUidInScenarioContext(response.getAsyncResponse().getCorrelationUid(),
                getString(expectedResponseData, Keys.KEY_ORGANIZATION_IDENTIFICATION,
                        Defaults.DEFAULT_ORGANIZATION_IDENTIFICATION));

        LOGGER.info("Got CorrelationUid: [" + ScenarioContext.Current().get(Keys.KEY_CORRELATION_UID) + "]");
    }

    @Then("^the platform buffers a get configuration response message for device \"([^\"]*)\"$")
    public void thePlatformBufferesAGetConfigurationResponseMessage(final String deviceIdentification,
            final Map<String, String> expectedResponseData) throws Throwable {
        final GetConfigurationAsyncRequest request = new GetConfigurationAsyncRequest();
        final AsyncRequest asyncRequest = new AsyncRequest();
        asyncRequest.setDeviceId(deviceIdentification);
        asyncRequest.setCorrelationUid((String) ScenarioContext.Current().get(Keys.KEY_CORRELATION_UID));
        request.setAsyncRequest(asyncRequest);

        boolean success = false;
        int count = 0;
        while (!success) {
            if (count > this.configuration.defaultTimeout) {
                Assert.fail("Timeout");
            }

            count++;
            Thread.sleep(1000);

            try {
                final GetConfigurationResponse response = this.client.getGetConfiguration(request);

                Assert.assertEquals(Enum.valueOf(OsgpResultType.class, expectedResponseData.get(Keys.KEY_RESULT)),
                        response.getResult());

                success = true;
            } catch (final Exception ex) {
                LOGGER.debug(ex.getMessage());
            }
        }
    }
}