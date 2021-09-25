package com.secure_bank.bank.WebServices.User.ProfileImage;

import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionCompleteListener;
import com.secure_bank.bank.Model.ProfileImageResponseRecord;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

public class ProfileImageFundsApiCall extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionCompleteListener Listener;
    private ProfileImageResponseRecord response;
    private String username;

    private ProfileImageFundsApiCall(String username, ActionCompleteListener actionCompleteListener) {
        this.Listener = actionCompleteListener;
        this.username = username;
    }

    public static void Execute(String username, ActionCompleteListener actionCompleteListener) {
        if (running) {
            actionCompleteListener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        ProfileImageFundsApiCall loginAsync = new ProfileImageFundsApiCall(username, actionCompleteListener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink(String username) {
        String PATH = "/api/User/ProfileImage?";
        String defaultParam = "user=%s";

        return Global.SCHEME + Global.HOST + PATH +
                String.format(defaultParam, username);
    }

    public static ProfileImageResponseRecord call(String username) {

        ProfileImageResponseRecord profileImageResponseRecord = new ProfileImageResponseRecord();
        String link = getLink(username);
        Log.e(Global.TAG, link);
        try {
            RestClient restClient = new RestClient(link);
            restClient.AddHeader("Method", "GET");
            restClient.AddHeader("Accept-Encoding", "gzip");
            restClient.AddHeader("Accept", "image/jpg");
            restClient.AddHeader("Content-type", "application/json");

            restClient.Execute(RestClient.RequestMethod.GET);
            String response = restClient.getResponse();

            if (response.length() <= 3) {
                return profileImageResponseRecord;
            }

            ProfileImageResponseRecord.SaveImage(response);

            return profileImageResponseRecord;

        } catch (Exception e) {
            e.printStackTrace();
            profileImageResponseRecord.ErrorCode = Global.Error.ERROR_UNKNOWN;
            profileImageResponseRecord.Error = e.toString();
            return profileImageResponseRecord;
        }
    }

    protected void onPreExecute() {
        running = true;
        this.response = new ProfileImageResponseRecord();
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            if (Network.hasConnection()) {
                this.response = call(username);
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

        if (this.response.ErrorCode >= 0 && (this.response.Error != null && !this.response.Error.isEmpty())) {
            this.Listener.actionCompleted();
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
