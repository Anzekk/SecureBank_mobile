package com.secure_bank.bank.WebServices.Store.BuyProduct;


import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionCompleteListener;
import com.secure_bank.bank.Model.EmptyResult;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

import org.json.JSONObject;

public class StoreBuyProductApiCall extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionCompleteListener Listener;
    private EmptyResult response;
    private int id;
    private double price;
    private int quantity;

    private StoreBuyProductApiCall(int id, double price, int quantity, ActionCompleteListener listener) {
        this.Listener = listener;
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    public static void Execute(int id, double price, int quantity, ActionCompleteListener listener) {
        if (running) {
            listener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        StoreBuyProductApiCall loginAsync = new StoreBuyProductApiCall(id, price, quantity, listener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink() {
        String PATH = "/api/Store/BuyProduct";

        return Global.SCHEME + Global.HOST + PATH;
    }

    public static EmptyResult call(int id, double price, int quantity) {
        EmptyResult emptyResponse = new EmptyResult();
        String link = getLink();
        Log.e(Global.TAG, link);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
            jsonBody.put("price", price);
            jsonBody.put("quantity", quantity);

            final String requestBody = jsonBody.toString();

            RestClient restClient = new RestClient(link);
            restClient.AddHeader("Method", "POST");
            restClient.AddHeader("Accept-Encoding", "gzip");
            restClient.AddHeader("Accept", "application/json");
            restClient.AddHeader("Content-type", "application/json");
            restClient.AddHeader("Cookie", "SessionId=" + Global.person.Cookie);
            restClient.SetBody(requestBody);

            restClient.Execute(RestClient.RequestMethod.POST);
            String response = restClient.getResponse();
            if (response.length() <= 3) {
                return emptyResponse;
            }

            emptyResponse = EmptyResult.convertStringJsonToEmptyResult(response);

            return emptyResponse;

        } catch (Exception e) {
            e.printStackTrace();
            emptyResponse.ErrorCode = Global.Error.ERROR_UNKNOWN;
            emptyResponse.Error = e.toString();
            return emptyResponse;
        }
    }

    protected void onPreExecute() {
        running = true;
        this.response = new EmptyResult();
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            if (Network.hasConnection()) {
                this.response = call(id, price, quantity);
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
            this.Listener.actionCompleted();
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
