package com.daspingenpongen

import android.util.Log
import groovy.transform.CompileStatic
import io.relayr.model.Reading
import io.relayr.model.TransmitterDevice
import rx.Subscription

@CompileStatic
final class DeviceSubscriber {

    private TransmitterDevice transmitterDevice
    private Subscription subscription
    private OnAddPointListener onAddPointListener

    DeviceSubscriber(TransmitterDevice transmitterDevice) {
        this.transmitterDevice = transmitterDevice
    }

    void subscribe(OnAddPointListener onAddPointListener) {
        this.onAddPointListener = onAddPointListener
        subscription = transmitterDevice.subscribeToCloudReadings()
                .filter(this.&filterProximityOnly)
                .map(this.&mapToStatus)
                .distinctUntilChanged()
                .subscribe(this.&onTouch, this.&onError)
    }


    boolean filterProximityOnly(Reading reading) {
        return reading.meaning == 'proximity'
    }

    Boolean mapToStatus(Reading reading) {
        Log.e('Raw value', transmitterDevice.id + '\t' + reading.value.toString())
        return Double.parseDouble(reading.value.toString()) > 1000d
    }

    void onTouch(Boolean status) {
        if (status) {
            Log.e('Point', 'this event should add one point!')
            onAddPointListener?.onAddPoint()
        }
    }

    private void onError(Throwable throwable) {
        Log.e('I dont know what to do with this error', throwable.message, throwable)
    }

    void unsubscribe() {
        subscription?.unsubscribe()
    }
}