/**
 * Copyright 2020 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.iec60870.application.services;

import org.opensmartgridplatform.adapter.protocol.iec60870.domain.services.LightMeasurementService;
import org.opensmartgridplatform.adapter.protocol.iec60870.domain.valueobjects.ResponseMetadata;
import org.opensmartgridplatform.adapter.protocol.iec60870.infra.messaging.DeviceResponseMessageSender;
import org.opensmartgridplatform.dto.valueobjects.LightSensorStatusDto;
import org.opensmartgridplatform.shared.infra.jms.DeviceMessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.MessageType;
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Iec60870LightMeasurementService implements LightMeasurementService {

    private static final MessageType MESSAGE_TYPE = MessageType.GET_LIGHT_SENSOR_STATUS;

    @Autowired
    DeviceResponseMessageSender deviceResponseMessageSender;

    @Override
    public void send(final LightSensorStatusDto lightSensorSatusDto, final ResponseMetadata responseMetadata) {
        final DeviceMessageMetadata deviceMessageMetadata = DeviceMessageMetadata.newBuilder()
                .withBypassRetry(true)
                .withCorrelationUid(responseMetadata.getCorrelationUid())
                .withDeviceIdentification(responseMetadata.getDeviceIdentification())
                .withMessageType(MESSAGE_TYPE.name())
                .withOrganisationIdentification(responseMetadata.getOrganisationIdentification())
                .build();
        final ProtocolResponseMessage responseMessage = ProtocolResponseMessage.newBuilder()
                .deviceMessageMetadata(deviceMessageMetadata)
                .domain(responseMetadata.getDomainInfo().getDomain())
                .domainVersion(responseMetadata.getDomainInfo().getDomainVersion())
                .dataObject(lightSensorSatusDto)
                .result(ResponseMessageResultType.OK)
                .build();
        this.deviceResponseMessageSender.send(responseMessage);
    }
}