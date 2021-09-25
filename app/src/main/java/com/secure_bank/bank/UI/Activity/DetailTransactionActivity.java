package com.secure_bank.bank.UI.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.secure_bank.bank.Listener.ActionGetTransactionCompleteListener;
import com.secure_bank.bank.Model.TransactionResp;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.DateTimeHelper;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.Transaction.Get.TransactionGetApiCall;

import java.util.Objects;

public class DetailTransactionActivity extends AppCompatActivity {

    TextView detailTransaction_sender;
    TextView detailTransaction_receiver;
    TextView detailTransaction_date;
    TextView detailTransaction_reason;
    TextView detailTransaction_amount;
    TextView detailTransaction_reference;


    private TransactionResp record;

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
        setContentView(R.layout.activity_detail_transaction);
        Global.context = this;

        this.setTitle(this.getString(R.string.details_of_transaction_label));
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        detailTransaction_sender = findViewById(R.id.detailTransaction_sender);
        detailTransaction_receiver = findViewById(R.id.detailTransaction_receiver);
        detailTransaction_date = findViewById(R.id.detailTransaction_date);
        detailTransaction_reason = findViewById(R.id.detailTransaction_reason);
        detailTransaction_amount = findViewById(R.id.detailTransaction_amount);
        detailTransaction_reference = findViewById(R.id.detailTransaction_reference);


        TransactionGetApiCall.Execute(getIntent().getIntExtra("TRANSACTION_ID", 0), new ActionGetTransactionCompleteListener() {
            @Override
            public void actionCompleted(TransactionResp record) {
                DetailTransactionActivity.this.record = record;
                setData();
            }

            @Override
            public void actionFailed(String error) {
                Log.e(Global.TAG, "Error: " + error);
            }
        });

    }

    private void setData() {
        String amount = String.format("%.2f €", (float) record.amount);

        detailTransaction_sender.setText(record.senderId);
        detailTransaction_receiver.setText(record.receiverId);
        detailTransaction_date.setText(DateTimeHelper.convertStringToNewFormatString(record.dateTime, DateTimeHelper.FORMAT_WEBAPI_CREATE_SPECIAL, DateTimeHelper.FORMAT_DISPLAY));
        detailTransaction_reason.setText(record.reason);
        detailTransaction_amount.setText(amount);
        detailTransaction_reference.setText(record.reference);
    }
}
