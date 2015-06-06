package com.daspingenpongen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import groovy.transform.CompileStatic
import io.relayr.RelayrSdk
import io.relayr.model.Transmitter
import io.relayr.model.TransmitterDevice
import io.relayr.model.User
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

@CompileStatic
public class MainActivity extends AppCompatActivity {

    Subscription subscription
    DeviceSubscriber leftDeviceSubscriber
    DeviceSubscriber rightDeviceSubscriber

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
        leftDeviceSubscriber?.unsubscribe()
        rightDeviceSubscriber?.unsubscribe()
    }

    private void subscribeOnLogin(Observable<User> logInObservable) {
        subscription = logInObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.&onLoggedIn, this.&onLoggedInError)
    }

    private void onLoggedIn(User user) {
        new DeviceSubscriber(new TransmitterDevice('dd66be35-d8fe-441f-be2a-5017cf30f5d8', 'cI_xGtONFU0.sWClApAFfQdxPLWatH02', '', '', '')).subscribe(this.&onLeftAddPoint)
        new DeviceSubscriber(new TransmitterDevice('31f3e93f-642b-43d9-9432-1f67c3b310de', 'cI_xGtONFU0.sWClApAFfQdxPLWatH02', '', '', '')).subscribe(this.&onRightAddPoint)
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
