package com.secure_bank.bank.WebServices.Auth.Login;


import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionLoginCompleteListener;
import com.secure_bank.bank.Model.UserModel;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

public class AuthGetTestUser extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionLoginCompleteListener Listener;
    private UserModel response;

    private AuthGetTestUser(ActionLoginCompleteListener listener) {
        this.Listener = listener;
    }

    public static void Execute(ActionLoginCompleteListener listener) {
        if (running) {
            listener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        AuthGetTestUser loginAsync = new AuthGetTestUser(listener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink() {
        String PATH = "/Auth/GetTestUser";

        return Global.SCHEME + Global.HOST + PATH;
    }

    public static UserModel call() {
        UserModel loginResponseRecord = new UserModel();
        String link = getLink();
        Log.e(Global.TAG, link);
        try {
            RestClient restClient = new RestClient(link);
            restClient.AddHeader("Method", "GET");
            restClient.AddHeader("Accept-Encoding", "gzip");
            restClient.AddHeader("Accept", "application/json");
            restClient.AddHeader("Content-type", "application/json");

            restClient.Execute(RestClient.RequestMethod.GET);
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
        this.response = new UserModel();
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            if (Network.hasConnection()) {
                this.response = call();
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
        running = false;

        if (this.response.ErrorCode >= 0 && (this.response.Error != null && this.response.Error.isEmpty())) {
            this.Listener.actionCompleted(response);
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
