package com.secure_bank.bank.WebServices;

import android.util.Log;

import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.TrustUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class RestClient {

    private HttpURLConnection connection;
    private String response;
    private String body;

    public RestClient(String link) throws IOException {
        URL url = new URL(link);
        TrustUtil.allowAllSSL();
        this.connection = (HttpURLConnection) url.openConnection();
    }

    public String getResponse() {
        return response;
    }

    public void AddHeader(String name, String value) throws Exception {
        if (connection == null) {
            throw new Exception("Connection is null");
        }
        Log.d(Global.TAG, "Name: " + name + ", value:" + value);
        connection.setRequestProperty(name, value);
    }

    public void SetBody(String body) {
        this.body = body;
    }

    public void Execute(RequestMethod method) throws Exception {
        if (connection == null) {
            throw new Exception("Connection is null");
        }
        try {
            switch (method) {
                case POST: {
                    executeRequestPOST();
                    break;
                }
                case GET: {
                    executeRequestGET();
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private void executeRequestPOST() throws Exception {
        try {
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(Global.CONNECTIONTIMEOUT);
            connection.setReadTimeout(Global.TIMEOUT);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            if (body != null) {
                OutputStream os = connection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(body);
                writer.flush();
                writer.close();
                os.close();
            }

            connection.connect();

            if (connection.getResponseCode() != Global.HTTP_OK_RESPONSE) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + connection.getResponseCode());
            }

            InputStream inputStream;

            if ("gzip".equals(connection.getContentEncoding())) {
                inputStream = new GZIPInputStream(connection.getInputStream());
            } else {
                inputStream = (connection.getInputStream());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                stringBuilder.append(output);
            }

            response = URLDecoder.decode(stringBuilder.toString(), "UTF-8");

            connection.disconnect();

        } catch (Exception ex) {
            ex.printStackTrace();
            connection.disconnect();
            throw ex;
        }
    }

    private void executeRequestGET() throws Exception {
        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(Global.CONNECTIONTIMEOUT);
            connection.setReadTimeout(Global.TIMEOUT);
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream;

            if ("gzip".equals(connection.getContentEncoding())) {
                inputStream = new GZIPInputStream(connection.getInputStream());
            } else {
                inputStream = (connection.getInputStream());
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == Global.HTTP_OK_RESPONSE) {
                StringBuilder stringBuilder = new StringBuilder();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";

                while ((line = rd.readLine()) != null) {
                    stringBuilder.append(line);
                }

                response = stringBuilder.toString();
                connection.disconnect();
            } else {
                throw new Exception("Responce code: " + responseCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            connection.disconnect();
            throw ex;
        }
    }

    public enum RequestMethod {
        POST, GET
    }
}