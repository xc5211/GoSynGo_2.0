package edu.scu.gsgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
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
    private Toolbar toolbarTop;
    private Toolbar toolbarBottom;
    private Button logoutButton;

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

    // TODO: init views
    private void initViews() {
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(this);
//        listView = (ListView) findViewById(R.id.list_view);
//        listAdapter = new CouponListAdapter(this);
//        listView.setAdapter(listAdapter);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbarTop);
        toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        setSupportActionBar(toolbarBottom);

        this.logoutButton = (Button) findViewById(R.id.button_logout);
    }

    private void initListener() {
        this.logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void logout() {
        super.appAction.logout(new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                Toast.makeText(context, R.string.toast_logout_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
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
