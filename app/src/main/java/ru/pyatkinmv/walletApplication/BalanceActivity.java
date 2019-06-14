package ru.pyatkinmv.walletApplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.String.format;

public class BalanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        Button button = findViewById(R.id.button);
        button.setText(R.string.addr_gen_activity);

        TextView textView = findViewById(R.id.balanceText);
        textView.setText(format("Balance: %s", WalletManager.getInstance().getBalance().toFriendlyString()));

        button.setOnClickListener(view -> {
            if (view.getId() == R.id.button) {
                startActivity(new Intent(this, AddressGeneratorActivity.class));
            }
        });
    }

}
