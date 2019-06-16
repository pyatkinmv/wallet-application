package ru.pyatkinmv.walletApplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "Main Activity";
    private final static int BALANCE_DELAY = 100;

    private ProgressUpdater updater;

    private int progress;
    private Handler handler;
    private TextView textView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.text);

        updater = new ProgressUpdater() {
            public void onFinish() {
                doneDownload();
                startBalanceActivity();
            }

            public void postProgress(double pct, int blocksSoFar, Date date) {
                progress(pct, blocksSoFar, date);
                updateProgress((int) pct);
            }
        };

        WalletManager.getInstance().register(updater);

        handler = new Handler(getMainLooper());

        updateProgress(progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WalletManager.getInstance().remove(updater);
    }

    private void updateProgress(int newProgress) {
        this.progress = newProgress;

        StringBuilder strProgress = new StringBuilder();
        strProgress.append(progress);
        strProgress.append("%");

        textView.setText(strProgress);
        progressBar.setProgress(progress);
        progressBar.setSecondaryProgress(progress + 5);

        StringBuilder downloaded = strProgress.append(getResources().getString(R.string.downloaded));
        Log.d(TAG, downloaded.toString());
    }

    private void startBalanceActivity() {
        handler.postDelayed(() -> startActivity(new Intent(this, BalanceActivity.class)), BALANCE_DELAY);
    }
}
