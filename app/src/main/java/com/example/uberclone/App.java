package com.example.uberclone;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {
    public void onCreate() {

        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("CaxEHEI0WWjya5x3uf7JKMNEorCkCpZV8G1ydoPc")
                // if defined
                .clientKey("Wxb7wljsTxpXGMl5vlAoWVM7SrD4gEFXMKNTj21s")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
