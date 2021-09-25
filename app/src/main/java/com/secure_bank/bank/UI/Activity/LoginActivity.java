package com.secure_bank.bank.UI.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.secure_bank.bank.Listener.ActionLoginCompleteListener;
import com.secure_bank.bank.Model.UserModel;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.AppChecker;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.Auth.Login.AuthLoginApiCall;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText et_username;
    EditText et_password;
    TextView l_login;
    AppCompatImageButton btn_login_settings;

    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        l_login = findViewById(R.id.l_login);
        btn_login_settings = findViewById(R.id.btn_login_settings);

        Global.context = this;

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        SharedPreferences sharedpreferences = getSharedPreferences(Global.MYPREFS, Context.MODE_PRIVATE);
        Global.HOST = sharedpreferences.getString(Global.IP, Global.HOST);
        Global.SCHEME = sharedpreferences.getBoolean(Global.PROTOCOL, true) ? "https://" : "http://";

        TextPaint paint = l_login.getPaint();
        float width = paint.measureText(getString(R.string.loginLabel));

        Shader textShader = new LinearGradient(0, 0, width, l_login.getTextSize(),
                new int[]{
                        ContextCompat.getColor(Global.context, R.color.logoStart),
                        ContextCompat.getColor(Global.context, R.color.logoEnd)
                }, null, Shader.TileMode.CLAMP);
        l_login.getPaint().setShader(textShader);

        btn_login_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Global.context, SettingsActivity.class);
                startActivity(intent);
            }
        });


        if (ContextCompat.checkSelfPermission(Global.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }

        et_username.setText(sharedpreferences.getString(Global.USERNAME, ""));
        et_password.setText(sharedpreferences.getString(Global.PASSWORD, ""));
        if (et_username.getText() != null && et_username.getText().length() > 0 && et_password.getText() != null && et_password.getText().length() > 0)
            onLoginClick(null);
    }


    public void onLoginClick(View view) {
        Global.rootUser = AppChecker.IsRoot(this);
        AuthLoginApiCall.Execute(this, et_username.getText().toString(), et_password.getText().toString(), new ActionLoginCompleteListener() {
            @Override
            public void actionCompleted(UserModel user) {
                SharedPreferences sharedPreferences = getSharedPreferences(Global.MYPREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor SPEditor = sharedPreferences.edit();
                SPEditor.putString(Global.USERNAME, et_username.getText().toString());
                SPEditor.putString(Global.PASSWORD, et_password.getText().toString());
                SPEditor.putString(Global.SESSION, user.Cookie);

                SPEditor.apply();
                Global.person = user;
                LoginActivity.this.openMainActivity();
            }

            @Override
            public void actionFailed(String error) {
                Log.e(Global.TAG, "Error: " + error);
                Toast.makeText(Global.context, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onForgotYourPasswordClick(View view) {
        openPasswordRecoveryActivity();
    }

    private void openPasswordRecoveryActivity() {
        Intent intent = new Intent(this, PasswordRecoverActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainNavigationDrawer.class);
        startActivity(intent);
        finish();
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to save profile image")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) Global.context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onNewUserClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
