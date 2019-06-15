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
    private ProgressUpdater listener;

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

        listener = new ProgressUpdater() {
            public void done() {
                doneDownload();
                startBalanceActivity();
            }

            public void update(double pct, int blocksSoFar, Date date) {
                progress(pct, blocksSoFar, date);

                postProgress((int) pct);
                Log.d(TAG, pct + " PERCENT");
            }

        };

        WalletManager.getInstance().register(listener);

        handler = new Handler(getMainLooper());

        postProgress(progress);
    }

    public void postProgress(int newProgress) {
        this.progress = newProgress;

        textView.setText(progress + "%");

        progressBar.setProgress(progress);
        progressBar.setSecondaryProgress(progress + 5);

    }

    public void startBalanceActivity() {
        handler.postDelayed(() -> startActivity(new Intent(this, BalanceActivity.class)), BALANCE_DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WalletManager.getInstance().remove(listener);
    }
}
