package com.redheadhammer.riskmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.redheadhammer.riskmanagement.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setup();
    }

    public void setup() {
        binding.intraday.setOnClickListener(view -> {
            Intent intent = new Intent(this, Calculate.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.additem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {
            addPL();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addPL() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add P&L");
        builder.setMessage("Add Today's Profit/Loss with considering taxes and brokerages");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(input);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String value = String.valueOf(input.getText());
            int pl = 0;
            try {
                pl = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.wrong_values, Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }
}