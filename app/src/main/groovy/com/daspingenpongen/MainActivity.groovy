package com.daspingenpongen

import android.content.Intent
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
    private final GameEndChecker gameEndChecker = new GameEndChecker()

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
        int score = getLeftScore() + 1
        setLeftScore(score)
        if (isGameEnd()) {
            displayGameOver(getString(R.string.left_user_name))
        }
    }

    private void onRightAddPoint() {
        int score = getRightScore() + 1
        setRightScore(score)
        if (isGameEnd()) {
            displayGameOver(getString(R.string.left_user_name))
        }
    }

    boolean isGameEnd() {
        int leftScore = getLeftScore()
        int rightScore = getRightScore()
        return gameEndChecker.isGameEnd(leftScore, rightScore)
    }

    void displayGameOver(String playerName) {
        Intent intent = new Intent(this, GameOverActivity)
        intent.putExtra('PLAYER_NAME', playerName)
        startActivity(intent)
    }

    void setLeftScore(int score) {
        TextView left = (TextView) findViewById(R.id.left_score)
        left.text = score.toString()
    }

    int getLeftScore() {
        TextView left = (TextView) findViewById(R.id.left_score)
        return Integer.parseInt(left.text.toString())
    }

    void setRightScore(int score) {
        TextView right = (TextView) findViewById(R.id.right_score)
        right.text = score.toString()
    }

    int getRightScore() {
        TextView right = (TextView) findViewById(R.id.right_score)
        return Integer.parseInt(right.text.toString())
    }

    private void onLoggedInError(Throwable throwable) {
        Log.e('Login error', throwable.message, throwable)
    }
}
