package edu.scu.gsgapp.fragment;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import edu.scu.gsgapp.GsgApplication;
import edu.scu.gsgapp.R;
import edu.scu.model.Event;
import edu.scu.model.enumeration.StatusEvent;
import edu.scu.weekviewlib.DateTimeInterpreter;
import edu.scu.weekviewlib.WeekView;
import edu.scu.weekviewlib.WeekViewEvent;
import edu.scu.weekviewlib.MonthLoader;

/**
 * Created by Blood on 2016/5/30.
 */
public class BaseWeekViewFragment extends Fragment implements
        MonthLoader.MonthChangeListener,
        WeekView.EventClickListener,
        WeekView.EventLongPressListener,
        WeekView.EmptyViewLongPressListener {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private static final int TYPE_FIVE_DAY_VIEW = 5;
    private int mWeekViewType = TYPE_FIVE_DAY_VIEW;
    private WeekView mWeekView;

    List<WeekViewEvent> weekViewEvents = new ArrayList<WeekViewEvent>();
    List<Event> allReadyEvents;
    List<Date> selectedDates = new ArrayList<>();

    private FragmentDateCommunicator fragmentDateCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_event_detail_not_ready_weekview, container, false);
        mWeekView = (WeekView) view.findViewById(R.id.event_detail_fragment_weekView);
        setDayDisplayType(TYPE_FIVE_DAY_VIEW);

        allReadyEvents = getAllReadyEvents();
        if(getArguments() != null) {
            selectedDates = decodeDate(getArguments().getStringArrayList("encodedDates"));
        }

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

        try {
            fragmentDateCommunicator = (FragmentDateCommunicator) getActivity();
        } catch (ClassCastException cce) {
            cce.printStackTrace();
        }

        return view;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        for(int i = 0; i < allReadyEvents.size(); i++) {

            Event readyEvent = allReadyEvents.get(i);
            Date timestamp = readyEvent.getTimestamp();
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(timestamp);
            startTime.add(Calendar.HOUR, -3);

            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.MINUTE, readyEvent.getDurationInMin());
            WeekViewEvent weekViewEvent = new WeekViewEvent(i, readyEvent.getTitle(), startTime, endTime);
            weekViewEvent.setColor(getResources().getColor(R.color.event_color_02));
            weekViewEvents.add(weekViewEvent);
        }

        for(int i = 0; i < selectedDates.size(); i++) {

            Date timestamp = selectedDates.get(i);
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(timestamp);
            startTime.add(Calendar.HOUR, 0);

            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.MINUTE, 90);
            WeekViewEvent weekViewEvent = new WeekViewEvent(i, "", startTime, endTime);
            weekViewEvent.setColor(getResources().getColor(R.color.event_color_03));
            weekViewEvents.add(weekViewEvent);
        }

        return weekViewEvents;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        //Toast.makeText(getContext(), "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
        //weekViewEvents.remove(event);
    }

    @Override
    public void onEventLongPress(WeekViewEvent weekViewEvent, RectF eventRect) {
        //Toast.makeText(getContext(), "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
        selectedDates.remove(weekViewEvent.getStartTime().getTime());
        fragmentDateCommunicator.updateSelectedDates(selectedDates);
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

        int tapMinute = time.get(Calendar.MINUTE);

        if(tapMinute < 15) {
            time.set(Calendar.MINUTE, 0);
        } else if (tapMinute >= 15 && tapMinute < 45) {
            time.set(Calendar.MINUTE, 30);
        } else {
            time.set(Calendar.MINUTE, 0);
            time.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY) + 1);
        }

        //Toast.makeText(getContext(),  getEventTitle(time), Toast.LENGTH_SHORT).show();
        Date date = time.getTime();
        selectedDates.add(date);
        fragmentDateCommunicator.updateSelectedDates(selectedDates);
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    private void setDayDisplayType(int mWeekViewType) {

        mWeekView.setNumberOfVisibleDays(mWeekViewType);

        // Lets change some dimensions to best fit the view.
        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
    }

    private List<Event> getAllReadyEvents() {

        List<Event> allReadyEvents = new ArrayList<>();

        Collection<Event> allEvents = new ArrayList<>();
        allEvents.addAll(((GsgApplication)(getActivity().getApplication())).getAppAction().getHostPerson().getEventsAsLeader());
        allEvents.addAll(((GsgApplication)(getActivity().getApplication())).getAppAction().getHostPerson().getEventsAsMember());
        for (Event event : allEvents) {
            if (event.getStatusEvent().equals(StatusEvent.Ready.getStatus())) {
                allReadyEvents.add(event);
            }
        }

        return allReadyEvents;
    }

    private List<Date> decodeDate(ArrayList<String> encodedDates) {
        List<Date> decodedDates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(String s: encodedDates) {
           try{
               decodedDates.add(sdf.parse(s));
           } catch (ParseException e) {
               e.printStackTrace();
           }
        }
        return decodedDates;
    }
}
