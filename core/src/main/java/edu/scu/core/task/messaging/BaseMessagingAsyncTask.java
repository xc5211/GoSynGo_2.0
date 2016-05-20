package edu.scu.core.task.messaging;

import android.os.AsyncTask;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;

/**
 * Created by chuanxu on 5/20/16.
 */
// TODO[later]
public abstract class BaseMessagingAsyncTask extends AsyncTask<Void, Void, ApiResponse> {

    protected Api api;
    protected AsyncCallback<List<Message>> channelMsgResponder;

    public BaseMessagingAsyncTask(final Api api, final AsyncCallback<List<Message>> channelMsgResponder) {
        this.api = api;
        this.channelMsgResponder = channelMsgResponder;
    }

    @Override
    protected abstract ApiResponse doInBackground(Void... params);

//    @Override
//    protected abstract void onProgressUpdate(Object... obj);

//    @Override
//    protected abstract void onPostExecute(ApiResponse response);

}
