package com.secure_bank.bank.UI.Activity;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.secure_bank.bank.Listener.ActionCompleteListener;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.Auth.PasswordRecovery.PasswordRecoveryApiCall;

import java.util.Objects;

public class PasswordRecoverActivity extends AppCompatActivity {

    EditText et_username;
    TextView l_login;


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
        setContentView(R.layout.activity_password_recover);

        Global.context = this;

        Objects.requireNonNull(this.getSupportActionBar()).setTitle(R.string.password_recovery_label);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        et_username = findViewById(R.id.et_username);
        l_login = findViewById(R.id.l_login);

        TextPaint paint = l_login.getPaint();
        float width = paint.measureText(getString(R.string.loginLabel));

        Shader textShader = new LinearGradient(0, 0, width, l_login.getTextSize(),
                new int[]{
                        ContextCompat.getColor(Global.context, R.color.logoStart),
                        ContextCompat.getColor(Global.context, R.color.logoEnd)
                }, null, Shader.TileMode.CLAMP);
        l_login.getPaint().setShader(textShader);

    }

    public void onRecoverClick(View view) {
        PasswordRecoveryApiCall.Execute(et_username.getText().toString(), new ActionCompleteListener() {
            @Override
            public void actionCompleted() {
                Toast.makeText(Global.context, "Password recovery successful", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void actionFailed(String error) {
                Toast.makeText(Global.context, "Password recovery failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
