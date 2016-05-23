package edu.scu.gsgapp.activity;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import edu.scu.gsgapp.R;
import edu.scu.model.Event;


/**
 * Created by Hairong on 5/19/16.
 */
public class DashboardEventPagerAdapter extends PagerAdapter {

    private ListView[] eventViews;

    public DashboardEventPagerAdapter(ListView[] pagesView) {
        eventViews = pagesView;
        eventViews[0] = pagesView[0];
        eventViews[1] = pagesView[1];
    }

    @Override
    public int getItemPosition (Object object) {
        int index = (eventViews[0] == object) ? 0 : 1;
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public int getCount() {
        return eventViews.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ListView listView = eventViews[position];
        container.addView(listView);
        return listView;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        container.removeView(eventViews[position]);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
