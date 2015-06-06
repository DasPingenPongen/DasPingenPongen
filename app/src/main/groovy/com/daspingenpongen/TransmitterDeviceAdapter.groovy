package com.daspingenpongen

import groovy.transform.CompileStatic
import io.relayr.model.Reading
import rx.Observable

@CompileStatic
interface TransmitterDeviceAdapter {

    Observable<Reading> subscribeToCloudReadings()

}
