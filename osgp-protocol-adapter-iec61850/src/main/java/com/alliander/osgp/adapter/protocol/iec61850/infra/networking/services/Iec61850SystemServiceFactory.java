/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.SystemService;
import com.alliander.osgp.dto.valueobjects.microgrids.SystemFilterDto;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;

@Component
public class Iec61850SystemServiceFactory {

    private Map<String, SystemService> systemServices;

    public SystemService getSystemService(final SystemFilterDto systemFilter) throws OsgpException {
        return this.getSystemService(systemFilter.getId(), systemFilter.getSystemType());
    }

    public SystemService getSystemService(final int systemId, final String systemType) throws OsgpException {
        final String key = systemType.toUpperCase(Locale.ENGLISH) + systemId;
        if (this.getSystemServices().containsKey(key)) {
            return this.getSystemServices().get(key);
        }

        throw new OsgpException(ComponentType.PROTOCOL_IEC61850, "Invalid System Type in System Filter: [" + key + "]");
    }

    private Map<String, SystemService> getSystemServices() {
        if (this.systemServices == null) {
            this.systemServices = new HashMap<>();

            // Refactor to read logical devices from device, database, or file
            final int numberOfPVs = 3;
            final int numberOfBatteries = 2;
            final int numberOfEngines = 3;

            this.systemServices.put("RTU1", new Iec61850RtuSystemService(1));
            for (int i = 1; i <= numberOfPVs; i++) {
                this.systemServices.put("PV" + i, new Iec61850PvSystemService(i));
            }
            for (int i = 1; i <= numberOfBatteries; i++) {
                this.systemServices.put("BATTERY" + i, new Iec61850BatterySystemService(i));
            }
            for (int i = 1; i <= numberOfEngines; i++) {
                this.systemServices.put("ENGINE" + i, new Iec61850EngineSystemService(i));
            }
            this.systemServices.put("LOAD1", new Iec61850LoadSystemService(1));
            this.systemServices.put("HEAT_BUFFER1", new Iec61850HeatBufferSystemService(1));
            this.systemServices.put("CHP1", new Iec61850ChpSystemService(1));
        }
        return this.systemServices;
    }
}
