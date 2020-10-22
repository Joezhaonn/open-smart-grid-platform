/**
 * Copyright 2020 Alliander N.V.
 * Copyright 2012-20 Fraunhofer ISE
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * This file was originally part of jDLMS, where it was part of a group of classes residing in packages
 * org.openmuc.jdlms.interfaceclass, org.openmuc.jdlms.interfaceclass.attribute and org.openmuc.jdlms.interfaceclass.method
 * that have been deprecated for jDLMS since version 1.5.1.
 *
 * It has been copied to the GXF code base under the Apache License, Version 2.0 with the permission of Fraunhofer ISE.
 * For more information about jDLMS visit
 *
 * http://www.openmuc.org
 */
package org.opensmartgridplatform.dlms.interfaceclass.method;

import org.opensmartgridplatform.dlms.interfaceclass.InterfaceClass;

/**
 * This class contains the methods defined for IC Clock.
 */
public enum ClockMethod implements MethodClass {

    ADJUST_TO_QUARTER(1, false),
    ADJUST_TO_MEASURING_PERIOD(2, false),
    ADJUST_TO_MINUTE(3, false),
    ADJUST_TO_PRESET_TIME(4, false),
    PRESET_ADJUSTING_TIME(5, false),
    SHIFT_TIME(6, false);

    static final InterfaceClass INTERFACE_CLASS = InterfaceClass.CLOCK;

    private final int methodId;
    private final boolean mandatory;

    private ClockMethod(final int methodId, final boolean mandatory) {
        this.methodId = methodId;
        this.mandatory = mandatory;
    }

    @Override
    public boolean isMandatory() {
        return this.mandatory;
    }

    @Override
    public int getMethodId() {
        return this.methodId;
    }

    @Override
    public InterfaceClass getInterfaceClass() {
        return INTERFACE_CLASS;
    }

    @Override
    public String getMethodName() {
        return this.name();
    }
}