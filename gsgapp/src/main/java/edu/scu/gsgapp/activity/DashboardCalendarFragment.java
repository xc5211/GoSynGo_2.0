package edu.scu.gsgapp.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import edu.scu.gsgapp.R;

/**
 * Created by chuanxu on 5/14/16.
 */
public class DashboardCalendarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_calendar_monthly, container, false);

        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendarview_calendar_monthly);
        ListView listView = (ListView) view.findViewById(R.id.listview_calendar_event);

        //Person hostPerson = (Person) getArguments().getSerializable(Person.SERIALIZE_KEY);

        // TODO: setup calendarView & listView



        return view;
    }

}
