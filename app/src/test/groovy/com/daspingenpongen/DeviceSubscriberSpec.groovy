package com.daspingenpongen

import io.relayr.model.Reading
import rx.Observable
import spock.lang.Specification

class DeviceSubscriberSpec extends Specification {

    def "DeviceSubscriber should call onAddPointListener when value change and its bigger then 1000"() {
        given:
        DeviceSubscriber subscriber = new DeviceSubscriber({ Observable.just(
                new Reading(0, 0, 'proximity', null, '1001')
        ) })
        OnAddPointListener onAddPointListener = Mock(OnAddPointListener)

        when:
        subscriber.subscribe(onAddPointListener)

        then:
        1 * onAddPointListener.onAddPoint()
    }

    def "DeviceSubscriber should not call onAddPointListener when value change and its lower then 1001"() {
        given:
        DeviceSubscriber subscriber = new DeviceSubscriber({ Observable.just(
                new Reading(0, 0, 'proximity', null, '1000')
        ) })
        OnAddPointListener onAddPointListener = Mock(OnAddPointListener)

        when:
        subscriber.subscribe(onAddPointListener)

        then:
        0 * onAddPointListener.onAddPoint()
    }

    def "DeviceSubscriber should call onAddPointListener once when two next values are bigger then 1001"() {
        given:
        DeviceSubscriber subscriber = new DeviceSubscriber({ Observable.from(
                [new Reading(0, 0, 'proximity', null, '1001'),new Reading(0, 0, 'proximity', null, '1001')]
        ) })
        OnAddPointListener onAddPointListener = Mock(OnAddPointListener)

        when:
        subscriber.subscribe(onAddPointListener)

        then:
        1 * onAddPointListener.onAddPoint()
    }

}
