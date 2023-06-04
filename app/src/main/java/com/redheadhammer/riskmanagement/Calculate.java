package com.redheadhammer.riskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.redheadhammer.riskmanagement.databinding.ActivityCalculateBinding;

public class Calculate extends AppCompatActivity {

    private ActivityCalculateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setup();
    }

    private void setup() {
        // data = [entry, stoploss, quantity]
        int[] data = {0, 0, 1};
        String prefix = binding.expectedLoss.getText().toString();
        binding.entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence price, int i, int i1, int i2) {
                try {
                    data[0] = Integer.parseInt(price.toString());
                } catch (NumberFormatException e) {
                    data[0] = 1;
                }

                setLoss(prefix, data);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.stoploss.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence price, int i, int i1, int i2) {
                try {
                    data[1] = Integer.parseInt(price.toString());
                } catch (NumberFormatException e) {
                    data[1] = 1;
                }

                setLoss(prefix, data);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });

        binding.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence price, int i, int i1, int i2) {
                try {
                    data[2] = Integer.parseInt(price.toString());
                } catch (NumberFormatException e) {
                    data[2] = 1;
                }

                setLoss(prefix, data);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });
    }

    private void setLoss(String prefix, int[] data) {
        int realLoss = loss(data[0], data[1], data[2]);
        binding.expectedLoss.setText(String.format("%s %d", prefix, realLoss));
        int color = R.color.red;
        if (realLoss > 0) color = R.color.green;
        binding.expectedLoss.setTextColor(getResources().getColor(color, getTheme()));
    }

    private int calculateZerodha(double orderValue, boolean selling) {
        double brokerage = Math.min(0.0003 * orderValue, 20);
        double STT = 0.00025 * orderValue;
        double transactionCharges = 0.000032 * orderValue;
        double GST = 0.18 * (brokerage + STT + transactionCharges);
        double SEBI = (10 * orderValue) / 10000000;
        double stamp = 0.00003 * orderValue;

        if (selling) return (int) (brokerage + STT + transactionCharges + GST + SEBI);
        return  (int) (brokerage + transactionCharges + GST + SEBI + stamp);
    }

    private int loss(double enter, double sl, int quantity) {
        double buyValue = (enter * quantity);
        int buyBrokerage = calculateZerodha(buyValue, false);

        double sellValue = (sl * quantity);
        int sellBrokerage = calculateZerodha(sellValue, true);

        int diff = (int)(sellValue - buyValue);
        return diff - buyBrokerage - sellBrokerage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.additem, menu);

        return super.onCreateOptionsMenu(menu);
    }

}