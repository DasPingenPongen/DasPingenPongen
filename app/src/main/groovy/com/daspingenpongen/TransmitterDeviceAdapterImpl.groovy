package com.daspingenpongen

import groovy.transform.CompileStatic
import io.relayr.model.TransmitterDevice

@CompileStatic
class TransmitterDeviceAdapterImpl implements TransmitterDeviceAdapter{

    @Delegate
    private TransmitterDevice transmitterDevice

    TransmitterDeviceAdapterImpl(TransmitterDevice transmitterDevice) {
        this.transmitterDevice = transmitterDevice
    }
}
