/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.smartmetering.application.mapping;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemObisCode;

public class CosemObisCodeConverter extends CustomConverter<CosemObisCode, byte[]> {
    @Override
    public byte[] convert(final CosemObisCode arg0, final Type<? extends byte[]> arg1) {
        return arg0.toByteArray();
    }
}
