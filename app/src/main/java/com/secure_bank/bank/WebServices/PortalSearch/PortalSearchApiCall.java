package com.secure_bank.bank.WebServices.PortalSearch;

import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionHTMLCompleteListener;
import com.secure_bank.bank.Model.PortalSearchResp;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

public class PortalSearchApiCall extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionHTMLCompleteListener Listener;
    private PortalSearchResp response;
    private String text;

    private PortalSearchApiCall(String text, ActionHTMLCompleteListener actionCompleteListener) {
        this.Listener = actionCompleteListener;
        this.text = text;
    }

    public static void Execute(String text, ActionHTMLCompleteListener actionCompleteListener) {
        if (running) {
            actionCompleteListener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        PortalSearchApiCall loginAsync = new PortalSearchApiCall(text, actionCompleteListener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink(String text) {
        String PATH = "/api/PortalSearch/Index?";
        String defaultParam = "searchString=%s";

        return Global.SCHEME + Global.HOST + PATH +
                String.format(defaultParam, text);
    }

    public static PortalSearchResp call(String text) {

        PortalSearchResp portalSearchResp = new PortalSearchResp();
        String link = getLink(text);
        Log.e(Global.TAG, link);
        try {
            RestClient restClient = new RestClient(link);
            restClient.AddHeader("Method", "GET");
            restClient.AddHeader("Accept-Encoding", "gzip");
            restClient.AddHeader("Accept", "application/json");
            restClient.AddHeader("Content-type", "application/json");
            restClient.AddHeader("Cookie", "SessionId=" + Global.person.Cookie);

            restClient.Execute(RestClient.RequestMethod.GET);
            String response = restClient.getResponse();

            if (response.length() <= 3) {
                return portalSearchResp;
            }

            portalSearchResp = PortalSearchResp.convertStringJsonToPortalSearchResp(response);

            return portalSearchResp;

        } catch (Exception e) {
            e.printStackTrace();
            portalSearchResp.ErrorCode = Global.Error.ERROR_UNKNOWN;
            portalSearchResp.Error = e.toString();
            return portalSearchResp;
        }
    }

    protected void onPreExecute() {
        running = true;
        this.response = new PortalSearchResp();
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            if (Network.hasConnection()) {
                this.response = call(text);
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
            this.Listener.actionCompleted(response.Data);
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
