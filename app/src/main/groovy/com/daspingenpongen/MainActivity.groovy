package com.daspingenpongen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import groovy.transform.CompileStatic
import io.relayr.RelayrSdk
import io.relayr.model.Transmitter
import io.relayr.model.User
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

@CompileStatic
public class MainActivity extends AppCompatActivity {

    Subscription subscription
    TransmitterSubscriber leftTransmitterSubscriber
    TransmitterSubscriber rightTransmitterSubscriber

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume()
        subscribeOnLogin(RelayrSdk.isUserLoggedIn() ? RelayrSdk.relayrApi.userInfo : RelayrSdk.logIn(this))
    }

    @Override
    protected void onPause() {
        super.onPause()
        subscription?.unsubscribe()
        leftTransmitterSubscriber?.unSubscribe()
        rightTransmitterSubscriber?.unSubscribe()
//        RelayrSdk.logOut()
    }

    private void subscribeOnLogin(Observable<User> logInObservable) {
        subscription = logInObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.&onLoggedIn, this.&onLoggedInError)
    }

    private void onLoggedIn(User user) {
        subscription = user.transmitters
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.&onTransmitters)
    }

    private void onTransmitters(List<Transmitter> transmitters) {
        Log.e('on transmitters', transmitters.toString())
        transmitters.each {
            Log.e('Transmitter id:', it.id)
            if (it.id == '6a4c5117-7f8a-41ed-a170-1cf90a9eb73e') {
                leftTransmitterSubscriber = new TransmitterSubscriber(it)
                leftTransmitterSubscriber.subscribe(this.&onLeftAddPoint)
            } else {//d30cc9c2-290b-4fdd-af6f-23eaf2ba0550
                rightTransmitterSubscriber = new TransmitterSubscriber(it)
                rightTransmitterSubscriber.subscribe(this.&onRightAddPoint)
            }
        }
    }

    private void onLeftAddPoint() {
        TextView leftScore = (TextView) findViewById(R.id.left_score)
        leftScore.text = (Integer.parseInt(leftScore.text.toString()) + 1).toString()
    }

    private void onRightAddPoint() {
        TextView leftScore = (TextView) findViewById(R.id.right_score)
        leftScore.text = (Integer.parseInt(leftScore.text.toString()) + 1).toString()
    }

    private void onLoggedInError(Throwable throwable) {
        Log.e('Login error', throwable.message, throwable)
    }
}
