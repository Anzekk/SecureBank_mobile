package com.secure_bank.bank.UI.Fragments.Transactions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.secure_bank.bank.Listener.ActionCompleteListener;
import com.secure_bank.bank.Model.TransactionDBModel;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.DateTimeHelper;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.Transaction.Create.TransactionCreateApiCall;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewTransactionsFragment extends Fragment {

    TextView createNewTransaction_sender;
    EditText createNewTransaction_receiver;
    EditText createNewTransaction_date;
    EditText createNewTransaction_reason;
    EditText createNewTransaction_reference;
    EditText createNewTransaction_amount;
    AppCompatButton createNewTransaction_submit;

    TransactionDBModel transaction = new TransactionDBModel();

    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateText();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_transaction, container, false);


        createNewTransaction_sender = view.findViewById(R.id.createNewTransaction_sender);
        createNewTransaction_receiver = view.findViewById(R.id.createNewTransaction_receiver);
        createNewTransaction_date = view.findViewById(R.id.createNewTransaction_date);
        createNewTransaction_reason = view.findViewById(R.id.createNewTransaction_reason);
        createNewTransaction_reference = view.findViewById(R.id.createNewTransaction_reference);
        createNewTransaction_amount = view.findViewById(R.id.createNewTransaction_amount);
        createNewTransaction_submit = view.findViewById(R.id.createNewTransaction_submit);

        createNewTransaction_sender.setText(Global.person.UserName);

        createNewTransaction_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1 && s.toString().equals("0"))
                    createNewTransaction_amount.setText("");
            }
        });

        createNewTransaction_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Global.context, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        createNewTransaction_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (createNewTransaction_receiver.getText().toString().length() > 0 &&
                            createNewTransaction_date.getText().toString().length() > 0 &&
                            createNewTransaction_amount.getText().toString().length() > 0 &&
                            createNewTransaction_reason.getText().toString().length() > 0 &&
                            createNewTransaction_sender.getText().toString().length() > 0) {
                        transaction = new TransactionDBModel(
                                createNewTransaction_sender.getText().toString(),
                                createNewTransaction_receiver.getText().toString(),
                                DateTimeHelper.convertDateToFormatString(calendar, DateTimeHelper.FORMAT_WEBAPI_CREATE_SPECIAL),
                                createNewTransaction_reason.getText().toString(),
                                createNewTransaction_reference.getText().toString(),
                                Integer.parseInt(createNewTransaction_amount.getText().toString()));
                        TransactionCreateApiCall.Execute(transaction, new ActionCompleteListener() {
                            @Override
                            public void actionCompleted() {
                                TransactionsFragment.refresh(true);
                                createNewTransaction_receiver.setText("");
                                createNewTransaction_date.setText("");
                                createNewTransaction_reason.setText("");
                                createNewTransaction_reference.setText("");
                                createNewTransaction_amount.setText("");
                                Toast.makeText(Global.context, "Transaction successful!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void actionFailed(String error) {
                                Log.e(Global.TAG, "Error: " + error);
                            }
                        });
                    } else {
                        Toast.makeText(Global.context, "Input data missing!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Log.e(Global.TAG, "Error: " + ex.toString());
                }
            }
        });

        return view;
    }

    private void updateText() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        createNewTransaction_date.setText(simpleDateFormat.format(calendar.getTime()));
    }
}