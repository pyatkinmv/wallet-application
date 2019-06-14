package ru.pyatkinmv.walletApplication;

import android.content.Context;
import android.util.Log;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;

import java.io.File;

public class WalletManager {
    private final static String TAG = "WalletManager";

    private Wallet wallet;
    private boolean isReady;

    private static WalletManager INSTANCE;

    public static WalletManager getInstance() {
        return INSTANCE;
    }

    public static void init(Context context) {
        INSTANCE = new WalletManager(context);
    }

    private WalletManager(Context context) {
        Log.d(TAG, "INIT WALLET KIT");

        NetworkParameters params = TestNet3Params.get();

        WalletAppKit kit = new WalletAppKit(params, new File(String.valueOf(context.getFilesDir())),
                "forwarding-service-testnet") {
            @Override
            protected void onSetupCompleted() {
                // This is called in a background thread after startAndWait is called, as setting up various objects
                // can do disk and network IO that may cause UI jank/stuttering in wallet apps if it were to be done
                // on the main thread.
                if (wallet().getKeyChainGroupSize() < 1)
                    wallet().importKey(new ECKey());

                wallet = wallet();
                isReady = true;

                Log.d(TAG, "WALLET KIT IS INITED");
                Log.d(TAG, wallet().getBalance().toFriendlyString());
            }
        };

        if (params == RegTestParams.get()) {
            kit.connectToLocalHost();
        }

        kit.startAsync();
    }

    public boolean isReady() {
        return isReady;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Coin getBalance() {
        return getWallet().getBalance();
    }

    public Address generateAddress() {
        return wallet.freshReceiveAddress();
    }
}
