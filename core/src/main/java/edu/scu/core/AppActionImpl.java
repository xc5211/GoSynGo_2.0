package edu.scu.core;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.Date;
import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiImpl;
import edu.scu.api.ApiResponse;

/**
 * Created by chuanxu on 4/14/16.
 */
public class AppActionImpl implements AppAction {

    private Context context;
    private Api api;

    public AppActionImpl (Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public void register(final String userEmail, final String password, final String name, final ActionCallbackListener<String> listener) {

        // check userEmail
        if (TextUtils.isEmpty(userEmail)) {
            if (listener != null) {
                listener.onFailure("Email is empty");
            }
        }

        // check password
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure("Password is empty");
            }
        }

        // TODO: check name

        // TODO: validate parameters...


        AsyncTask<Void, Void, ApiResponse<String>> asyncTask = new AsyncTask<Void, Void, ApiResponse<String>>() {

            @Override
            protected ApiResponse<String> doInBackground(Void... params) {
                return api.register(userEmail, password, name);
            }

            @Override
            protected void onPostExecute(ApiResponse<String> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response.getObj());
                    } else {
                        listener.onFailure(response.getMsg());
                    }
                }
            }

        };
        asyncTask.execute();

    }

    @Override
    public void login(final String userEmail, final String password, final boolean stayLoggedIn, final ActionCallbackListener<Void> listener) {

        // check userEmail
        if (TextUtils.isEmpty(userEmail)) {
            if (listener != null) {
                // TODO:
                //listener.onFailure(ErrorEvent.PARAM_NULL, "User email is empty");
            }
        }

        // check password
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                // TODO:
                //listener.onFailure(ErrorEvent.PARAM_NULL, "Password is empty");
            }
        }

        // TODO: validate password



    }

    @Override
    public void getMonthlyScheduledDates(ActionCallbackListener<List<Date>> actionCallbackListener) {

    }
}
