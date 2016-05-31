package edu.scu.gsgapp.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.SubscriptionOptions;

import java.util.List;

import edu.scu.core.ActionCallbackListener;
import edu.scu.core.callback.DefaultChannelMessageResponder;
import edu.scu.core.callback.EventChannelMessageLeaderResponder;
import edu.scu.core.callback.EventChannelMessageMemberResponder;
import edu.scu.gsgapp.R;
import edu.scu.gsgapp.fragment.DashboardCalendarFragment;
import edu.scu.gsgapp.fragment.DashboardEventFragment;
import edu.scu.gsgapp.fragment.DashboardMeFragment;
import edu.scu.model.Event;
import edu.scu.util.lib.GoogleProjectSettings;

/**
 * Created by chuanxu on 4/16/16.
 */
public class DashboardActivity extends GsgBaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static boolean isSyncFinish = false;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private Button calendarButton;
    private Button eventsButton;
    private Button meButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initWidgetable();
        initListener();
        initFragment();

        getData();
        startDefaultChannelSubscriptionAsycTask();
    }

    private void startDefaultChannelSubscriptionAsycTask() {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean hasSubscribed = false;

                while (!hasSubscribed) {
                    try {
                        Thread.sleep(100);

                        if (isSyncFinish) {
                            isSyncFinish = false;
                            hasSubscribed = true;
                            String hostPersonObjectId = appAction.getHostPerson().getObjectId();

                            resubscribeAllChannels(hostPersonObjectId);
                            break;
                        }
                    } catch (BackendlessException | InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (hasSubscribed) {
                            break;
                        }
                    }

                }

                Thread.yield();
            }
        }).start();
//                return null;
//            }
//        }.execute();
    }

    private void resubscribeAllChannels(String hostPersonObjectId) {

        SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
        String selector = "receiverId" + hostPersonObjectId.replace("-", "") + " = 'true'";
        subscriptionOptions.setSelector(selector);

        // Resubscribe default channel
        DefaultChannelMessageResponder defaultChannelMsgResponder = new DefaultChannelMessageResponder(appAction.getUndecidedEventList());
        resubscribeChannel(GoogleProjectSettings.DEFAULT_CHANNEL, defaultChannelMsgResponder, subscriptionOptions);

        // Resubscribe all event channels as leader
        for (Event event : appAction.getHostPerson().getEventsAsLeader()) {
            EventChannelMessageLeaderResponder channelMessageLeaderResponder = new EventChannelMessageLeaderResponder(appAction.getHostPerson());
            resubscribeChannel(event.getObjectId(), channelMessageLeaderResponder, subscriptionOptions);
        }

        // Resubscribe all event channels as member
        for (Event event : appAction.getHostPerson().getEventsAsMember()) {
            EventChannelMessageMemberResponder channelMessageMemberResponder = new EventChannelMessageMemberResponder();
            resubscribeChannel(event.getObjectId(), channelMessageMemberResponder, subscriptionOptions);
        }
    }

    private void resubscribeChannel(final String channelName, final AsyncCallback<List<Message>> channelMessageResponder, SubscriptionOptions subscriptionOptions) {
        Backendless.Messaging.subscribe(channelName, channelMessageResponder, subscriptionOptions, new AsyncCallback<Subscription>() {

            @Override
            public void handleResponse(Subscription subscription) {
                appAction.addToChannelMap(channelName, channelMessageResponder, subscription);
                Toast.makeText(context, "Resubscribe to channel success: " + channelName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(context, "Resubscribe to channel fail: " + backendlessFault.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        // TODO: clear old data


        //getData();
    }

    private void initFragment() {
        DashboardCalendarFragment dashboardCalendarFragment = new DashboardCalendarFragment();
        getFragmentManager().beginTransaction().replace(R.id.dashboard_container, dashboardCalendarFragment).commit();
    }

    private void initWidgetable() {
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(this);
//        listView = (ListView) findViewById(R.id.list_view);
//        listAdapter = new CouponListAdapter(this);
//        listView.setAdapter(listAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar_dashboard);
        //setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                final ProgressDialog progressDialog = ProgressDialog.show( DashboardActivity.this, "", "Connecting...", true );

                Intent intent = null;
                switch(item.getItemId()) {
                    case R.id.menu_dashboard_toolbar_add_event:
                        //intent = new Intent(DashboardActivity.this, ProposeEventActivity.class);
                        //startActivity(intent);
                        appAction.proposeEvent(new ActionCallbackListener<Event>() {
                            @Override
                            public void onSuccess(Event data) {
                                progressDialog.cancel();
                                Intent intent = new Intent(DashboardActivity.this, ProposeEventActivity.class);
                                intent.putExtra("eventId", data.getObjectId());
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(String message) {
                                progressDialog.cancel();
                                Toast.makeText(context, "Failed: " + message,Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.menu_dashboard_toolbar_test:
                        intent = new Intent(DashboardActivity.this, TestEventActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        assert false;
                }
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.menu_dashboard_toolbar);

        this.calendarButton = (Button) findViewById(R.id.dashboard_button_calendar);
        this.eventsButton = (Button) findViewById(R.id.dashboard_button_events);
        this.meButton = (Button) findViewById(R.id.dashboard_button_me);
    }

    private void initListener() {

        this.calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment calendarFragment = getFragmentManager().findFragmentByTag("CALENDAR");
                if (calendarFragment != null && calendarFragment.isVisible()) {
                    return;
                }

                switchFragment("calendar");
            }
        });

        this.eventsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment eventsFragment = getFragmentManager().findFragmentByTag("EVENTS");
                if (eventsFragment != null && eventsFragment.isVisible()) {
                    return;
                }

                switchFragment("events");
            }
        });

        this.meButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment meFragment = getFragmentManager().findFragmentByTag("ME");
                if (meFragment != null && meFragment.isVisible()) {
                    return;
                }

                switchFragment("me");
            }
        });

    }

    private void switchFragment(String fragmentName) {

        switch (fragmentName) {
            case "calendar":
                DashboardCalendarFragment dashboardCalendarFragment = new DashboardCalendarFragment();
                getFragmentManager().beginTransaction().replace(R.id.dashboard_container, dashboardCalendarFragment, "CALENDAR").commit();
                break;
            case "events":
                DashboardEventFragment dashboardEventFragment = new DashboardEventFragment();
                getFragmentManager().beginTransaction().replace(R.id.dashboard_container, dashboardEventFragment, "EVENTS").commit();
                break;
            case "me":
                DashboardMeFragment dashboardMeFragment = new DashboardMeFragment();
                getFragmentManager().beginTransaction().replace(R.id.dashboard_container, dashboardMeFragment, "ME").commit();
                break;
            default:
                assert false;
        }

    }

    private void validateLogin() {
        super.appAction.validateLogin(new ActionCallbackListener<Void>() {

            @Override
            public void onSuccess(Void data) {
                Toast.makeText(context, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String message) {
                // Do nothing
            }

        });
    }

    private void getData() {
        syncHostInformation();
    }

    private void syncHostInformation() {

        final ProgressDialog progressDialog = ProgressDialog.show( DashboardActivity.this, "", "Sync with server...", true );

        ActionCallbackListener<Void> callBackListener = new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                progressDialog.cancel();
                Toast.makeText(context, "Sync with server success", Toast.LENGTH_SHORT).show();
                isSyncFinish = true;
            }

            @Override
            public void onFailure(String message) {
                progressDialog.cancel();
                Toast.makeText(context, "Sync with server fail", Toast.LENGTH_SHORT).show();
            }
        };
        getAppAction().syncHostInformation(getAppAction().getHostUserId(), callBackListener);
    }

}
