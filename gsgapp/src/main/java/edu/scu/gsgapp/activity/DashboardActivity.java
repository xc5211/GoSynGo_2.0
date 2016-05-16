package edu.scu.gsgapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.R;

/**
 * Created by chuanxu on 4/16/16.
 */
public class DashboardActivity extends GsgBaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private Button calendarButton;
    private Button eventsButton;
    private Button meButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        initListener();
        getData();

        // prepare the argument
        Bundle bundle = new Bundle();
        // TODO: pass host person object to fragment
        //bundle.putSerializable(Person.SERIALIZE_KEY, appAction.getHostPerson());
        DashboardCalendarFragment dashboardCalendarFragment = new DashboardCalendarFragment();
        dashboardCalendarFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.dashboard_container, dashboardCalendarFragment)
                .commit();
    }

    @Override
    public void onRefresh() {
        // TODO: clear old data


        // get new data
        getData();
    }

    private void initViews() {
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
                switch(item.getItemId()) {
                    case R.id.menu_dashboard_toolbar_add_event:
                        Intent intent = new Intent(DashboardActivity.this, ProposeEventActivity.class);
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
                switchFragment("calendar");
            }
        });

        this.eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment("events");
            }
        });

        this.meButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment("me");
            }
        });

    }

    private void switchFragment(String fragmentName) {
        Bundle bundle = new Bundle();

        switch (fragmentName) {
            case "calendar":
                // prepare the argument
                // TODO: pass args to pass
                //bundle.putSerializable(Person.SERIALIZE_KEY, appAction.getHostPerson());
                DashboardCalendarFragment dashboardCalendarFragment = new DashboardCalendarFragment();
                dashboardCalendarFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.dashboard_container, dashboardCalendarFragment)
                        .commit();
                break;
            case "events":
                // prepare the argument
                // TODO: pass args to pass
                //bundle.putSerializable(Person.SERIALIZE_KEY, appAction.getHostPerson());
                DashboardEventFragment dashboardEventFragment = new DashboardEventFragment();
                dashboardEventFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.dashboard_container, dashboardEventFragment)
                        .commit();
                break;
            case "me":
                // prepare the argument
                // TODO: pass args to pass
                //bundle.putSerializable(Person.SERIALIZE_KEY, appAction.getHostPerson());
                DashboardMeFragment dashboardMeFragment = new DashboardMeFragment();
                dashboardMeFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.dashboard_container, dashboardMeFragment)
                        .commit();
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
    }

}
