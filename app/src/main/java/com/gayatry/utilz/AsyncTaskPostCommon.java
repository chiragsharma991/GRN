package com.gayatry.utilz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gayatry.rest.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class AsyncTaskPostCommon extends AsyncTask<Object, Void, String> {

    private AsyncTaskCompleteListener callback;
    private Context context;

    public AsyncTaskPostCommon(Context context, AsyncTaskCompleteListener cb) {
        this.context = context;
        this.callback = cb;
    }

    @Override
    protected String doInBackground(Object... params) {
        try{
            String url = (String) params[0];
            String jsonObject = (String) params[1];
            Log.e("param", ""+jsonObject.toString());
            JsonParser jsonParser = new JsonParser();
            return jsonParser.postToResponse(url, jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    protected void onPostExecute(String result) {
        try {
            callback.onTaskComplete(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface AsyncTaskCompleteListener {
        void onTaskComplete(String result) throws JSONException;
    }
}