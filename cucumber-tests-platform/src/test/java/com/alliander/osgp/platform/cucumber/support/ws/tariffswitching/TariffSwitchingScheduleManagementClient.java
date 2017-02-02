/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.support.ws.tariffswitching;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleRequest;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.SetScheduleResponse;
import com.alliander.osgp.platform.cucumber.support.ws.BaseClient;
import com.alliander.osgp.shared.exceptionhandling.WebServiceSecurityException;
import com.alliander.osgp.shared.infra.ws.DefaultWebServiceTemplateFactory;

@Component
public class TariffSwitchingScheduleManagementClient extends BaseClient {

    @Autowired
    private DefaultWebServiceTemplateFactory tariffSwitchingScheduleManagementWstf;

    public SetScheduleAsyncResponse setSchedule(final SetScheduleRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.tariffSwitchingScheduleManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());

        return (SetScheduleAsyncResponse) webServiceTemplate.marshalSendAndReceive(request);
    }

    public SetScheduleResponse getSetSchedule(final SetScheduleAsyncRequest request)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final WebServiceTemplate webServiceTemplate = this.tariffSwitchingScheduleManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName());

        return (SetScheduleResponse) webServiceTemplate.marshalSendAndReceive(request);
    }
}
