package com.daspingenpongen

import android.app.Application
import groovy.transform.CompileStatic
import io.relayr.RelayrSdk

@CompileStatic
final class MyFirstApp extends Application {

    @Override
    void onCreate() {
        super.onCreate()
        new RelayrSdk.Builder(this).inMockMode(false).build()
    }

}