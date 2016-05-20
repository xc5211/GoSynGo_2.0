package edu.scu.gsgapp.activity;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import edu.scu.model.Event;


/**
 * Created by Hairong on 5/19/16.
 */
public class DashboardEventPagerAdapter extends PagerAdapter {
    private Activity activity;
    private List<Event>[] events;


    public DashboardEventPagerAdapter(Activity activity, List<Event> eventsAsLeader, List<Event> eventsAsMember) {
        this.activity = activity;
        events = new List[2];
        events[0] = eventsAsLeader;
        events[1] = eventsAsMember;
    }

    @Override
    public int getCount() {
        return events.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return events;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}