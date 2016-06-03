package edu.scu.gsgapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.scu.gsgapp.GsgApplication;
import edu.scu.gsgapp.R;
import edu.scu.gsgapp.activity.EventDetailActivity;
import edu.scu.gsgapp.adapter.dashboard.events.DashboardEventPagerAdapter;
import edu.scu.gsgapp.adapter.dashboard.events.EventListViewAdapter;
import edu.scu.model.Event;
import edu.scu.model.enumeration.StatusEvent;

/**
 * Created by chuanxu on 5/14/16.
 */
public class DashboardEventFragment extends Fragment {

    private GsgApplication gsgApplication;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewPager view = (ViewPager) inflater.inflate(R.layout.fragment_dashboard_event_viewpager, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_dashboard_event);

        this.gsgApplication = (GsgApplication) getActivity().getApplication();
        List<Event>[] pagesEvents = getPagesEvents();
        List<Event> eventsReady = pagesEvents[0];
        List<Event> eventsNotReady = pagesEvents[1];

        Map<Event, Boolean> eventReadyLeaderMap = getEventLeaderMap(eventsReady);
        Map<Event, Boolean> eventNotReadyLeaderMap = getEventLeaderMap(eventsNotReady);

        ListView eventsReadyListView = (ListView) inflater.inflate(R.layout.fragment_event_listing, null).findViewById(R.id.list_view_fragment_event);
        ListView eventsNotReadyListView = (ListView) inflater.inflate(R.layout.fragment_event_listing, null).findViewById(R.id.list_view_fragment_event);

        eventsReadyListView.setAdapter(new EventListViewAdapter(container.getContext(), R.layout.fragment_event_ready_view_pager_custom_row, true, eventsReady, eventReadyLeaderMap));
        eventsReadyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(parent.getContext(), EventDetailActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                Event event = (Event) parent.getAdapter().getItem(position);
                bundle.putString("eventId", event.getObjectId());
                bundle.putString("eventState", "Ready");
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
            }
        });

        eventsNotReadyListView.setAdapter(new EventListViewAdapter(container.getContext(), R.layout.fragment_event_not_ready_view_pager_custom_row, false, eventsNotReady, eventNotReadyLeaderMap));
        eventsNotReadyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(parent.getContext(), EventDetailActivity.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                Event event = (Event) parent.getAdapter().getItem(position);
                bundle.putString("eventId", event.getObjectId());
                bundle.putString("eventState", "NotReady");
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
            }
        });

        ListView[] pagesView = new ListView[2];
        pagesView[0] = eventsReadyListView;
        pagesView[1] = eventsNotReadyListView;

        viewPager.setAdapter(new DashboardEventPagerAdapter(pagesView));
        return view;
    }

    private Map<Event, Boolean> getEventLeaderMap(List<Event> events) {
        Map<Event, Boolean> eventLeaderMap = new HashMap<>();
        for (Event event : events) {
            if (event.getEventMemberDetail(gsgApplication.getAppAction().getHostPerson().getObjectId()) == null) {
                eventLeaderMap.put(event, true);
            } else {
                eventLeaderMap.put(event, false);
            }
        }
        return eventLeaderMap;
    }

    private List<Event>[] getPagesEvents() {
        List<Event>[] pagesEvents = new ArrayList[2];
        List<Event> eventsReady = new ArrayList<>();
        List<Event> eventsNotReady = new ArrayList<>();

        Collection<Event> allEvents = new ArrayList<>();
        allEvents.addAll(gsgApplication.getAppAction().getHostPerson().getEventsAsLeader());
        allEvents.addAll(gsgApplication.getAppAction().getHostPerson().getEventsAsMember());

        for (Event event : allEvents) {
            if (event.getStatusEvent().equals(StatusEvent.Ready.getStatus())) {
                eventsReady.add(event);
            } else if (event.getStatusEvent().equals(StatusEvent.Pending.getStatus()) ||
                    event.getStatusEvent().equals(StatusEvent.Tentative.getStatus())) {
                eventsNotReady.add(event);
            }
        }

        pagesEvents[0] = eventsReady;
        pagesEvents[1] = eventsNotReady;
        return pagesEvents;
    }

}
