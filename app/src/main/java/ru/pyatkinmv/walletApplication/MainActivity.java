package ru.pyatkinmv.walletApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int BALANCE_DELAY = 1000;
    public static final int CHECK_READY_PERIOD = 200;
    private TextView textView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textView.setText(R.string.loading);

        handler = new Handler(getMainLooper());

        checkReady();
    }

    private void checkReady() {
        if (WalletManager.getInstance().isReady()) {
            textView.setText(R.string.loaded);
            handler.postDelayed(()->{
                startActivity(new Intent(this, BalanceActivity.class));
            }, BALANCE_DELAY);
        } else {
            handler.postDelayed(this::checkReady, CHECK_READY_PERIOD);
        }
    }
}
