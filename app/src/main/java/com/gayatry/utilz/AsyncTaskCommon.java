package com.gayatry.utilz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gayatry.rest.JsonParser;

public class AsyncTaskCommon extends AsyncTask<String, Void, String> {

    private AsyncTaskCompleteListener callback;
    private Context context;

    public AsyncTaskCommon(Context context, AsyncTaskCompleteListener cb) {
        this.context = context;
        this.callback = cb;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            JsonParser jsonParser = new JsonParser();
            return jsonParser.getResponse(params[0]);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    protected void onPostExecute(String result) {
       callback.onTaskComplete(result);
   }

    public interface AsyncTaskCompleteListener {
        void onTaskComplete(String result);
    }
}