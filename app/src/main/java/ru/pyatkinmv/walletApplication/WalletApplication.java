package ru.pyatkinmv.walletApplication;

import android.app.Application;

public class WalletApplication extends Application {
    private final static String TAG = "WalletApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        WalletManager.init(this);
    }

}
