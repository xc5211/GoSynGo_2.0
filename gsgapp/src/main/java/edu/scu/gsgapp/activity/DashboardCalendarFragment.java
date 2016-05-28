package edu.scu.gsgapp.activity;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import edu.scu.gsgapp.GsgApplication;
import edu.scu.gsgapp.R;
import edu.scu.gsgapp.adaptor.CalendarAdapter;
import edu.scu.gsgapp.adaptor.Utility;
import edu.scu.gsgapp.adaptor.UndecidedEventAdapter;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventUndecided;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/14/16.
 */
public class DashboardCalendarFragment extends Fragment {

    private View view;
    private GsgApplication gsgApplication;

    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker
    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;

    //sichao for grid view
    private ListView undecidedEventListView;
    private UndecidedEventAdapter undecidedEventAdapter;
    private List<Event> undecidedEventList = new ArrayList<Event>();
    public final static int LIST_LENGTH = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard_calendar, container, false);

        this.gsgApplication = (GsgApplication) getActivity().getApplication();

        //CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendarview_calendar_monthly);
        //ListView listView = (ListView) view.findViewById(R.id.listview_calendar_event);

        //Person hostPerson = (Person) getArguments().getSerializable(Person.SERIALIZE_KEY);

        // TODO: setup calendarView & listView


        RecyclerView undecidedEventRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_dashboard_calenar_undecided_event);
        List<EventUndecided> undecidedEventList = gsgApplication.getAppAction().getUndecidedEventList();

        UndecidedEventAdapter undecidedEventAdapter = new UndecidedEventAdapter(undecidedEventList);
        undecidedEventRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        undecidedEventRecyclerView.setAdapter(undecidedEventAdapter);




        Locale.setDefault(Locale.US);

        rLayout = (LinearLayout) view.findViewById(R.id.text);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<>();

        adapter = new CalendarAdapter(view.getContext(), month);

        GridView gridview = (GridView) view.findViewById(R.id.gridview_calendar_monthly);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        LinearLayout previous = (LinearLayout) view.findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        LinearLayout next = (LinearLayout) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO[?]: Removed calendar grid view listener
//                // removing the previous view if added
//                if (((LinearLayout) rLayout).getChildCount() > 0) {
//                    ((LinearLayout) rLayout).removeAllViews();
//                }
//                desc = new ArrayList<>();
//                date = new ArrayList<>();
//                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
//                String selectedGridDate = CalendarAdapter.dayString.get(position);
//                String[] separatedTime = selectedGridDate.split("-");
//                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");// taking last part of date. ie; 2 from 2012-12-02.
//                int gridvalue = Integer.parseInt(gridvalueString);
//                // navigate to next or previous month on clicking offdays.
//                if ((gridvalue > 10) && (position < 8)) {
//                    setPreviousMonth();
//                    refreshCalendar();
//                } else if ((gridvalue < 7) && (position > 28)) {
//                    setNextMonth();
//                    refreshCalendar();
//                }
//                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
//
//                for (int i = 0; i < Utility.startDates.size(); i++) {
//                    if (Utility.startDates.get(i).equals(selectedGridDate)) {
//                        desc.add(Utility.nameOfEvent.get(i));
//                    }
//                }
//
//                if (desc.size() > 0) {
//                    for (int i = 0; i < desc.size(); i++) {
//                        TextView rowTextView = new TextView(view.getContext());
//
//                        // set some properties of rowTextView or something
//                        rowTextView.setText("Event:" + desc.get(i));
//                        rowTextView.setTextColor(Color.BLACK);
//
//                        // add the textview to the linearlayout
//                        rLayout.addView(rowTextView);
//                    }
//                }
//
//                desc = null;
            }

        });

        //sichao
        //initEventList();
        initUndecidedEventList();
        return view;
    }


    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    protected void showToast(String string) {
        Toast.makeText(view.getContext(), string, Toast.LENGTH_SHORT).show();
    }

    public void refreshCalendar() {
        TextView title = (TextView) view.findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String itemvalue;
            event = Utility.readCalendarEvent(view.getContext());
            Log.d("=====Event====", event.toString());
            Log.d("=====Date ARRAY====", Utility.startDates.toString());

            for (int i = 0; i < Utility.startDates.size(); i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(GregorianCalendar.DATE, 1);
                items.add(Utility.startDates.get(i).toString());
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };

    private void initUndecidedEventList() {
//        undecidedEventAdapter = new UndecidedEventAdapter(view.getContext(),
//                R.layout.undecided_event_row, ((GsgApplication)getActivity().getApplication()).getAppAction().getUndecidedEventList(), ((GsgApplication)getActivity().getApplication()).getAppAction());
//        undecidedEventListView = (ListView) view.findViewById(R.id.listview_calendar_event);
//        undecidedEventListView.setAdapter(undecidedEventAdapter);
    }

    //sichao for test
    private void initEventList() {
        for(int i = 0; i < LIST_LENGTH; i++) {
            Event event = new Event();
            EventLeaderDetail eventLeaderDetail = new EventLeaderDetail();
            Person leader = new Person();
            leader.setFirstName("sichao");
            eventLeaderDetail.setLeader(leader);
            event.setEventLeaderDetail(eventLeaderDetail);
            event.setTitle("hot show baby!");
            undecidedEventList.add(event);
        }
    }

}
