/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.opensmartgridplatform.adapter.ws.smartmetering.application.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.opensmartgridplatform.adapter.ws.schema.smartmetering.common.ObisCodeValues;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.GetPowerQualityProfileRequest;

public class GetPowerQualityProfileRequestMappingTest {

    private static final String DEVICE_NAME = "TEST10240000001";

    private static final Date DATE = new Date();
    private final MonitoringMapper mapper = new MonitoringMapper();

    @Test
    public void convertGetPowerQualityProfileRequestData() {
        final org.opensmartgridplatform.adapter.ws.schema.smartmetering.monitoring.GetPowerQualityProfileRequest source = this.makeRequest();
        final Object result = this.mapper.map(source, GetPowerQualityProfileRequest.class);

        assertThat(result).as("mapping GetPowerQualityProfileRequest should not return null").isNotNull();
        assertThat(result).as("mapping GetPowerQualityProfileRequest  should return correct type").isInstanceOf(
                GetPowerQualityProfileRequest.class);

        final GetPowerQualityProfileRequest target = (GetPowerQualityProfileRequest) result;

        assertThat(target.getDeviceIdentification()).isEqualTo(source.getDeviceIdentification());
        assertThat(target.getProfileType()).isEqualTo(source.getProfileType());
        final DateTime targetEndDate = new DateTime(target.getEndDate());
        assertThat(targetEndDate.getYear()).isEqualTo(source.getBeginDate().getYear());
    }

    private XMLGregorianCalendar makeGregorianCalendar() {
        final GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(DATE);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (final DatatypeConfigurationException e) {
            throw new RuntimeException("error creating XMLGregorianCalendar");
        }
    }

    private ObisCodeValues makeObisCodeValues() {
        final ObisCodeValues result = new ObisCodeValues();
        result.setA((short) 1);
        result.setB((short) 1);
        result.setC((short) 1);
        result.setD((short) 1);
        result.setE((short) 1);
        result.setF((short) 1);
        return result;
    }

    private org.opensmartgridplatform.adapter.ws.schema.smartmetering.monitoring.GetPowerQualityProfileRequest makeRequest() {
        final org.opensmartgridplatform.adapter.ws.schema.smartmetering.monitoring.GetPowerQualityProfileRequest result = new org.opensmartgridplatform.adapter.ws.schema.smartmetering.monitoring.GetPowerQualityProfileRequest();
        result.setProfileType("PRIVATE");
        result.setDeviceIdentification(DEVICE_NAME);
        result.setBeginDate(this.makeGregorianCalendar());
        result.setEndDate(this.makeGregorianCalendar());
        return result;
    }
}
