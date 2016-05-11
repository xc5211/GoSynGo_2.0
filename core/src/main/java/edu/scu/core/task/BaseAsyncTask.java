package edu.scu.core.task;

import android.os.AsyncTask;
import android.os.Handler;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/5/16.
 */
public abstract class BaseAsyncTask extends AsyncTask<Object, Object, ApiResponse> {

    protected Api api;
    protected ActionCallbackListener listener;
    protected Handler handler;

    public BaseAsyncTask(final Api api, final ActionCallbackListener listener, final Handler handler) {
        this.api = api;
        this.listener = listener;
        this.handler = handler;
    }

    @Override
    protected abstract ApiResponse doInBackground(Object... params);

//    @Override
//    protected abstract void onProgressUpdate(Object... obj);

    @Override
    protected abstract void onPostExecute(ApiResponse response);

}
