package edu.scu.gsgapp.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.R;
import edu.scu.gsgapp.activity.GsgBaseActivity;

/**
 * Created by chuanxu on 4/16/16.
 */
public class DashboardActivity extends GsgBaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        getData();
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
