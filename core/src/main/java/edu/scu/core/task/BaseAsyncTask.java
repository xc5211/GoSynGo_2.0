package edu.scu.core.task;

import android.os.AsyncTask;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;

/**
 * Created by chuanxu on 5/5/16.
 */
public class BaseAsyncTask<T> implements Executable {

    protected AsyncTask<Void, Void, ApiResponse> asyncTask;

    public BaseAsyncTask(final Api api, final ActionCallbackListener<T> listener) {

    }

    @Override
    public void execute() {
        asyncTask.execute();
    }

}
