package com.daspingenpongen

import android.util.Log
import groovy.transform.CompileStatic
import io.relayr.model.Transmitter
import io.relayr.model.TransmitterDevice

@CompileStatic
final class TransmitterSubscriber {

    private Transmitter transmitter
    private List<DeviceSubscriber> subscriptions
    private OnAddPointListener onAddPointListener

    TransmitterSubscriber(Transmitter transmitter) {
        this.transmitter = transmitter
    }

    void subscribe(OnAddPointListener onAddPointListener) {
        this.onAddPointListener = onAddPointListener
        transmitter.devices.subscribe(this.&onDevices)
    }

    void onDevices(List<TransmitterDevice> devices) {
        Log.e('on transmitter devices', devices.toString())
        subscriptions = devices.collect(this.&subscribeToCloudReadings)
    }

    DeviceSubscriber subscribeToCloudReadings(TransmitterDevice transmitterDevice) {
        DeviceSubscriber deviceSubscriber = new DeviceSubscriber(transmitterDevice)
        deviceSubscriber.subscribe(onAddPointListener)
        return deviceSubscriber
    }

    void unSubscribe() {
        subscriptions?.each {
            it?.unsubscribe()
        }
    }

}