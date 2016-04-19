package edu.scu.gsgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.R;

/**
 * Created by chuanxu on 4/16/16.
 */
public class DashboardActivity extends GsgBaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private Button logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        getData();
        // TODO: Explain when to use initListener() vs "onclick" behavior defined in xml
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

        this.logoutButton = (Button) findViewById(R.id.button_logout);
    }

    public void logout(View view) {
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

    private void getData() {
        super.appAction.getMonthlyScheduledDates(new ActionCallbackListener<List<Date>>() {
            @Override
            public void onSuccess(List<Date> data) {
                if (!data.isEmpty()) {
                    // TODO: fill data in calendar view
                }
                swipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setEnabled(false);
            }
        });
    }

}
