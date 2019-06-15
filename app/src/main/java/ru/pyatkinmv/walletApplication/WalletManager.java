package ru.pyatkinmv.walletApplication;

import android.content.Context;
import android.util.Log;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class WalletManager {
    private final static String TAG = "WalletManager";

    private Set<ProgressUpdater> updaters;

    private Wallet wallet;

    private static WalletManager INSTANCE;

    public static WalletManager getInstance() {
        return INSTANCE;
    }

    public static void init(Context context) {
        INSTANCE = new WalletManager(context);
    }

    private WalletManager(Context context) {
        Log.d(TAG, "INIT WALLET KIT");

        updaters = new HashSet<>();

        NetworkParameters params = TestNet3Params.get();

        WalletAppKit kit = new WalletAppKit(params, new File(String.valueOf(context.getFilesDir())), "forwarding-service-testnet") {
            @Override
            protected void onSetupCompleted() {
                if (wallet().getKeyChainGroupSize() < 1)
                    wallet().importKey(new ECKey());

                wallet = wallet();

                Log.d(TAG, "WALLET KIT IS INITED");
            }
        };

        kit.setDownloadListener(new DownloadProgressTracker() {
            @Override
            protected void doneDownload() {
                super.doneDownload();
                for (ProgressUpdater updater : updaters) {
                    updater.onFinish();
                }
            }

            @Override
            protected void progress(double pct, int blocksSoFar, Date date) {
                super.progress(pct, blocksSoFar, date);
                for (ProgressUpdater updater : updaters) {
                    updater.postProgress(pct, blocksSoFar, date);
                }
            }
        }).setBlockingStartup(false);

        kit.startAsync();
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

    public void register(ProgressUpdater listener) {
        updaters.add(listener);
    }

    public void remove(ProgressUpdater listener) {
        updaters.remove(listener);
    }
}