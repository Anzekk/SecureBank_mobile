package com.secure_bank.bank.UI.Activity;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.secure_bank.bank.Listener.ActionCompleteListener;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.Auth.Register.AuthRegisterApiCall;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    TextView et_username;
    TextView et_password;
    TextView et_passwordRepeat;

    TextView l_login;

    CheckBox termsAndConditions;


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
        setContentView(R.layout.activity_register);

        Global.context = this;

        Objects.requireNonNull(this.getSupportActionBar()).setTitle(R.string.register);
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        et_username = findViewById(R.id.reg_et_username);
        et_password = findViewById(R.id.reg_et_password1);
        et_passwordRepeat = findViewById(R.id.reg_et_password2);
        termsAndConditions = findViewById(R.id.termsAndConditions);
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

    public void onRegisterClick(View view) {
        if (termsAndConditions.isChecked()) {
            if (isValidEmail(et_username.getText().toString())) {
                if (et_password.getText().toString().matches(et_passwordRepeat.getText().toString())) {
                    if (et_password.getText().length() > 4) {
                        AuthRegisterApiCall.Execute(et_username.getText().toString(), et_password.getText().toString(), new ActionCompleteListener() {
                            @Override
                            public void actionCompleted() {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Global.SCHEME + Global.HOST + ":1080"));
                                startActivity(browserIntent);
                                finish();
                            }

                            @Override
                            public void actionFailed(String error) {
                                Log.e(Global.TAG, "Error: " + error);
                                Toast.makeText(Global.context, "An error has occurred: " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, "Password is to short", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Email address is invalid", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You need to accept terms and conditions", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
