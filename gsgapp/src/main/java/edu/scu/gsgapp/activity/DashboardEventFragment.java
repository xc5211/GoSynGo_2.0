package edu.scu.gsgapp.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import edu.scu.gsgapp.GsgApplication;
import edu.scu.gsgapp.R;
import edu.scu.model.Event;

/**
 * Created by chuanxu on 5/14/16.
 */
public class DashboardEventFragment extends Fragment {

    private GsgApplication gsgApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_event_viewpager, container, false);

        this.gsgApplication = (GsgApplication) getActivity().getApplication();

        List<Event> eventsAsLeader = gsgApplication.getAppAction().getHostPerson().getEventsAsLeader();
        List<Event> eventsAsMember = gsgApplication.getAppAction().getHostPerson().getEventsAsMember();

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_dashboard_event);
        viewPager.setAdapter(new DashboardEventPagerAdapter(getActivity(), eventsAsLeader, eventsAsMember));


        return view;
    }
}
