package com.secure_bank.bank.WebServices.Auth.Login;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionLoginCompleteListener;
import com.secure_bank.bank.Model.UserModel;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

import org.json.JSONObject;

public class AuthLoginApiCall extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionLoginCompleteListener Listener;
    private UserModel response;
    private Context context;
    private static ProgressDialog dialog;
    private String username;
    private String password;

    private AuthLoginApiCall(Context context, String username, String password, ActionLoginCompleteListener listener) {
        this.Listener = listener;
        this.context = context;
        this.username = username;
        this.password = password;
    }

    public static void Execute(Context context, String username, String password, ActionLoginCompleteListener listener) {
        if (running) {
            listener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        AuthLoginApiCall loginAsync = new AuthLoginApiCall(context, username, password, listener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink() {
        String PATH = "/api/Auth/Login";

        return Global.SCHEME + Global.HOST + PATH;
    }

    public static UserModel call(String username, String password) {
        UserModel loginResponseRecord = new UserModel();
        String link = getLink();
        Log.e(Global.TAG, link);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("userName", username);
            jsonBody.put("password", password);
            jsonBody.put("userRight", 0);

            final String requestBody = jsonBody.toString();

            Log.d(Global.TAG, requestBody);

            RestClient restClient = new RestClient(link);
            restClient.AddHeader("Method", "POST");
            restClient.AddHeader("Accept-Encoding", "gzip");
            restClient.AddHeader("Accept", "application/json");
            restClient.AddHeader("Content-type", "application/json");
            restClient.SetBody(requestBody);

            restClient.Execute(RestClient.RequestMethod.POST);
            String response = restClient.getResponse();
            if (response.length() <= 3) {
                return loginResponseRecord;
            }

            loginResponseRecord = UserModel.convertStringJsonToUserModel(response);

            return loginResponseRecord;

        } catch (Exception e) {
            e.printStackTrace();
            loginResponseRecord.ErrorCode = Global.Error.ERROR_UNKNOWN;
            loginResponseRecord.Error = e.toString();
            return loginResponseRecord;
        }
    }

    protected void onPreExecute() {
        running = true;

        dialog = ProgressDialog.show(context, "",
                "Logging in. Please wait...", true);

        this.response = new UserModel();
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            if (Network.hasConnection()) {
                this.response = call(username, password);
            } else {
                this.response.Error = Global.Error.NO_INTERNET_TEXT;
                this.response.ErrorCode = Global.Error.NO_INTERNET;
            }
        } catch (Exception e) {
            this.response.Error = e.toString();
            this.response.ErrorCode = Global.Error.ERROR_UNKNOWN;
        }
        return null;
    }

    protected void onPostExecute(Void voidR) {
        dialog.dismiss();

        running = false;


        if (this.response.ErrorCode >= 0 && (this.response.Error != null && this.response.Error.isEmpty())) {
            this.Listener.actionCompleted(response);
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
