package ru.pyatkinmv.walletApplication;

import android.app.Application;
import android.util.Log;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;

import java.io.File;

public class WalletApplication extends Application {
    private final static String TAG = "Wallet Application";

    @Override
    public void onCreate() {
        super.onCreate();
        initWalletKit();
    }

    private void initWalletKit() {
        Log.d(TAG, "INIT WALLET KIT");


        // First we configure the network we want to use.
        // The available options are:
        // - MainNetParams
        // - TestNet3Params
        // - RegTestParams
        // While developing your application you probably want to use the Regtest mode and run your local bitcoin network. Run bitcoind with the -regtest flag
        // To test you app with a real network you can use the testnet. The testnet is an alternative bitcoin network that follows the same rules as main network. Coins are worth nothing and you can get coins for example from http://faucet.xeno-genesis.com/
        //
        // For more information have a look at: https://bitcoinj.github.io/testing and https://bitcoin.org/en/developer-examples#testing-applications
        NetworkParameters params = TestNet3Params.get();

        // Now we initialize a new WalletAppKit. The kit handles all the boilerplate for us and is the easiest way to get everything up and running.
        // Have a look at the WalletAppKit documentation and its source to understand what's happening behind the scenes: https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/kits/WalletAppKit.java
        WalletAppKit kit = new WalletAppKit(params, new File("."), "walletApplication") {
            @Override
            protected void onSetupCompleted() {
                super.onSetupCompleted();
                Log.d(TAG, "WALLET KIT WAS INITED");
            }
        };

//        kit.connectToLocalHost();

        // In case you want to connect with your local bitcoind tell the kit to connect to localhost.
        // You must do that in reg test mode.
        //kit.connectToLocalHost();

        // Now we start the kit and sync the blockchain.
        // bitcoinj is working a lot with the Google Guava libraries. The WalletAppKit extends the AbstractIdleService. Have a look at the introduction to Guava services: https://github.com/google/guava/wiki/ServiceExplained
        kit.startAsync();
//        kit.awaitRunning();


        // Ready to run. The kit syncs the blockchain and our wallet event listener gets notified when something happens.
        // To test everything we create and print a fresh receiving address. Send some coins to that address and see if everything works.
//        System.out.println("send money to: " + kit.wallet().freshReceiveAddress().toString());

        // Make sure to properly shut down all the running services when you manually want to stop the kit. The WalletAppKit registers a runtime ShutdownHook so we actually do not need to worry about that when our application is stopping.
        //System.out.println("shutting down again");
        //kit.stopAsync();
        //kit.awaitTerminated();
    }
}
