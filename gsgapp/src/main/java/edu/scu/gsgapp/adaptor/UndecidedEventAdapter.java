package edu.scu.gsgapp.adaptor;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import edu.scu.core.AppAction;
import edu.scu.gsgapp.R;
import edu.scu.gsgapp.databinding.UndecidedEventRowBinding;
import edu.scu.model.EventUndecided;

/**
 * Created by Blood on 2016/5/22.
 */
public class UndecidedEventAdapter extends RecyclerView.Adapter<UndecidedEventObjectHolder> {

    private Context context;
    private AppAction appAction;
    private List<EventUndecided> undecidedEventList;
    private Button acceptButton;
    private Button declineButton;

    public UndecidedEventAdapter(List<EventUndecided> undecidedEventList) {
        this.undecidedEventList = undecidedEventList;
    }

    /*
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
                        Toast.makeText(context, "failed",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "failed",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        return row;
    }
*/

    @Override
    public UndecidedEventObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UndecidedEventRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.undecided_event_row, parent, false);
        return new UndecidedEventObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(UndecidedEventObjectHolder holder, int position) {
        holder.bindConnection(this.undecidedEventList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.undecidedEventList.size();
    }

}
