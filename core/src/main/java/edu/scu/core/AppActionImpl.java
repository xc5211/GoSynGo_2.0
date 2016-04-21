package edu.scu.core;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;

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
    public void validateLogin(final ActionCallbackListener<Void> listener) {
        // Check local userToken. If available, it means the user has already logged in.
        String userToken = UserTokenStorageFactory.instance().getStorage().get();
        if(userToken != null && !userToken.equals("")) {
            String currentUserObjectId = UserIdStorageFactory.instance().getStorage().get();
            listener.onSuccess(null);
        } else {
            listener.onFailure(null);
        }
    }

    @Override
    public void login(final String userEmail, final String password, final boolean stayLoggedIn, final ActionCallbackListener<String> listener) {

        // check userEmail
        if (TextUtils.isEmpty(userEmail)) {
            if (listener != null) {
                listener.onFailure("User email is empty");
            }
        }

        // check password
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure("Password is empty");
            }
        }

        // TODO: validate password


        AsyncTask<Void, Void, ApiResponse<String>> asyncTask = new AsyncTask<Void, Void, ApiResponse<String>>() {

            @Override
            protected ApiResponse<String> doInBackground(Void... params) {
                return api.login(userEmail, password, stayLoggedIn);
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
    public void logout(final ActionCallbackListener<Void> listener) {

        AsyncTask<Void, Void, ApiResponse<Void>> asyncTask = new AsyncTask<Void, Void, ApiResponse<Void>>() {

            @Override
            protected ApiResponse<Void> doInBackground(Void... params) {
                return api.logout();
            }

            @Override
            protected void onPostExecute(ApiResponse<Void> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(null);
                    } else {
                        listener.onFailure(response.getMsg());
                    }
                }
            }

        };
        asyncTask.execute();
    }

    @Override
    public void getMonthlyScheduledDates(ActionCallbackListener<List<Date>> actionCallbackListener) {

    }

    @Override
    public void proposeEvent(final String leaderId, final ActionCallbackListener<String> listener) {
        AsyncTask<Void, Void, ApiResponse<String>> asyncTask = new AsyncTask<Void, Void, ApiResponse<String>>() {

            @Override
            protected ApiResponse<String> doInBackground(Void... params) {
                return api.proposeEvent(leaderId);
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
    public void addEventMember(String leaderId, String eventId, String memberEmail,ActionCallbackListener<String> listener) {

    }

}
