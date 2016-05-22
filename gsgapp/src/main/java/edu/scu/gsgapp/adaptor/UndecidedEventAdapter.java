package edu.scu.gsgapp.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.scu.core.ActionCallbackListener;
import edu.scu.core.AppAction;
import edu.scu.gsgapp.R;
import edu.scu.model.Event;

import java.util.List;

/**
 * Created by Blood on 2016/5/22.
 */
public class UndecidedEventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private AppAction appAction;
    private List<Event> undecidedEventList;
    //private TextView eventTime;
    private TextView eventTitle;
    private TextView eventLeader;
    private Button acceptButton;
    private Button declineButton;

    public UndecidedEventAdapter(Context context, int resource, List<Event> undecidedEventList, AppAction appAction) {
        super(context, resource, undecidedEventList);
        this.undecidedEventList = undecidedEventList;
        this.context = context;
        this.appAction = appAction;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int i = 0;
        View row = convertView;
        if (convertView == null) {
            row = inflater.inflate(R.layout.undecided_event_row, parent, false);
        }


        //eventTime = (TextView) row.findViewById(R.id.textView_event_time);
        //eventTime.setText(undecidedEventList.get(position).getTimestamp().toString());

        eventTitle = (TextView) row.findViewById(R.id.textView_event_title);
        eventTitle.setText(undecidedEventList.get(position).getTitle());

        eventLeader = (TextView) row.findViewById(R.id.textView_event_leader);
        eventLeader.setText("Leader: "+ undecidedEventList.get(position).getEventLeaderDetail().getLeader().getFirstName());

        final int staticPosition = position;
        acceptButton = (Button) row.findViewById(R.id.button_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Event Number " + staticPosition  + "accpeted",Toast.LENGTH_SHORT).show();
                appAction.acceptEvent(undecidedEventList.get(position),
                        undecidedEventList.get(position).getEventLeaderDetail().getLeader().getObjectId(),
                        new ActionCallbackListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        undecidedEventList.remove(position);
                        UndecidedEventAdapter.this.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
            }
        });

        declineButton = (Button) row.findViewById(R.id.button_decline);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Event Number " + staticPosition  + "declined",Toast.LENGTH_SHORT).show();
                appAction.declineEvent(undecidedEventList.get(position),
                        undecidedEventList.get(position).getEventLeaderDetail().getLeader().getObjectId(),
                        new ActionCallbackListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        undecidedEventList.remove(position);
                        UndecidedEventAdapter.this.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });

            }
        });
        return row;
    }

}
