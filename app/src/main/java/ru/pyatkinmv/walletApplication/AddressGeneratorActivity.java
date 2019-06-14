package ru.pyatkinmv.walletApplication;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddressGeneratorActivity extends AppCompatActivity {
    private TextView addressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_generator);

        addressView = findViewById(R.id.address);
        addressView.setVisibility(View.GONE);

        Button generateBtn = findViewById(R.id.genAddrBtn);
        generateBtn.setText(R.string.gen_new_addr);
        generateBtn.setOnClickListener(view -> {
            ViewGroup parentView = (ViewGroup) view.getParent();
            parentView.removeView(view);
            addressView.setVisibility(View.VISIBLE);
            addressView.setText(WalletManager.getInstance().generateAddress().toString());
        });
    }
}
