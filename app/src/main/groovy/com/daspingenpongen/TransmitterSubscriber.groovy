package com.daspingenpongen

import android.util.Log
import groovy.transform.CompileStatic
import io.relayr.model.Reading
import io.relayr.model.Transmitter
import io.relayr.model.TransmitterDevice
import rx.Subscription

@CompileStatic
final class TransmitterSubscriber {

    private Transmitter transmitter
    private List<Subscription> subscriptions

    TransmitterSubscriber(Transmitter transmitter) {
        this.transmitter = transmitter
    }

    void subscribe() {
        transmitter.devices.subscribe(this.&onDevices)
    }

    void unSubscribe() {
        subscriptions?.each {
            it?.unsubscribe()
        }
    }

    void onDevices(List<TransmitterDevice> devices) {
        Log.e('on transmitter devices', devices.toString())
        subscriptions = devices.collect {
            Log.e('Device id:', it.id)
            it.subscribeToCloudReadings()
                    .filter(this.&filterProximityOnly)
                    .map(this.&mapToStatus)
                    .buffer(2)
                    .filter(this.&changeToTrue)
                    .subscribe(this.&onTouch)
        }
    }

    boolean filterProximityOnly(Reading reading) {
        return reading.meaning == 'proximity'
    }

    Boolean mapToStatus(Reading reading) {
        return Double.parseDouble(reading.value.toString()) > 1000d
    }

    boolean changeToTrue(List<Boolean> status) {
        return !status[0] && status[1]
    }

    void onTouch(List<Boolean> aBoolean) {
        Log.e('Point', 'this event should add one point!')
    }
}