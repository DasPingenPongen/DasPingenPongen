package com.daspingenpongen

import android.util.Log
import groovy.transform.CompileStatic
import io.relayr.model.Reading
import rx.Subscription

@CompileStatic
final class DeviceSubscriber {

    private TransmitterDeviceAdapter transmitterDevice
    private Subscription subscription
    private OnAddPointListener onAddPointListener

    DeviceSubscriber(TransmitterDeviceAdapter transmitterDevice) {
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


    private boolean filterProximityOnly(Reading reading) {
        return reading.meaning == 'proximity'
    }

    private Boolean mapToStatus(Reading reading) {
        return Double.parseDouble(reading.value.toString()) > 1000d
    }

    private void onTouch(Boolean status) {
        if (status) {
            onAddPointListener?.onAddPoint()
            Log.e('Point', 'this event should add one point!')
        }
    }

    private void onError(Throwable throwable) {
        Log.e('I dont know what to do with this error', throwable.message, throwable)
        unsubscribe()
        subscribe(onAddPointListener)
    }

    void unsubscribe() {
        subscription?.unsubscribe()
    }
}