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
//        RelayrSdk.logOut()
    }

    private void subscribeOnLogin(Observable<User> logInObservable) {
        subscription = logInObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.&onLoggedIn, this.&onLoggedInError)
    }

    private void onLoggedIn(User user) {
        user.transmitters
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.&onTransmitters)
    }

    private void onTransmitters(List<Transmitter> transmitters) {
        Log.e('on transmitters', transmitters.toString())
        transmitters.each {
            Log.e('Transmitter id:', it.id)
            leftTransmitterSubscriber = new TransmitterSubscriber(it)
            leftTransmitterSubscriber.subscribe(this.&onLeftAddPoint)
        }
    }

    private void onLeftAddPoint() {
        TextView leftScore = (TextView) findViewById(R.id.left_score)
        leftScore.text = (Integer.parseInt(leftScore.text.toString()) + 1).toString()
    }

    private void onLoggedInError(Throwable throwable) {
        Log.e('Login error', throwable.message, throwable)
    }
}
