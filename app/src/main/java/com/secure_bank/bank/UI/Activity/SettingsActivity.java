package com.secure_bank.bank.UI.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.Global;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    AppCompatEditText acet_ip;
    ToggleButton acet_protocol;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Global.context = this;

        Objects.requireNonNull(this.getSupportActionBar()).setTitle(R.string.settings_label);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        acet_ip = findViewById(R.id.acet_ip);
        acet_protocol = findViewById(R.id.acet_protocol);

        String localProtocol = Global.SCHEME.split(":")[0];

        acet_ip.setText(Global.HOST);
        acet_protocol.setChecked(localProtocol.equals("https"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedpreferences = getSharedPreferences(Global.MYPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Global.IP, Objects.requireNonNull(acet_ip.getText()).toString());
        editor.putBoolean(Global.PROTOCOL, Objects.requireNonNull(acet_protocol.isChecked()));
        editor.apply();
        Global.HOST = Objects.requireNonNull(acet_ip.getText()).toString();
        Global.SCHEME = acet_protocol.isChecked() ? "https://" : "http://";
    }
}
