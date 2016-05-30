package edu.scu.gsgapp.adaptor;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import edu.scu.core.ActionCallbackListener;
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

    public UndecidedEventAdapter(Context context, AppAction appAction, List<EventUndecided> undecidedEventList) {
        this.context = context;
        this.appAction = appAction;
        this.undecidedEventList = undecidedEventList;
    }

    @Override
    public UndecidedEventObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UndecidedEventRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.undecided_event_row, parent, false);
        return new UndecidedEventObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(UndecidedEventObjectHolder holder, final int position) {

        EventUndecided undecidedEvent = this.undecidedEventList.get(position);
        final String title = undecidedEvent.title;
        final String leaderName = undecidedEvent.leaderName;
        final String eventId = undecidedEvent.eventId;
        final String leaderId = undecidedEvent.leaderId;

        View.OnClickListener acceptOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appAction.acceptEvent(eventId, leaderId, new ActionCallbackListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        Toast.makeText(context, "Accepted event success", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, "Accepted event fail", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };

        View.OnClickListener declineOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appAction.declineEvent(eventId, leaderId, new ActionCallbackListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        Toast.makeText(context, "Declined event success", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, "Declined event fail", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };

        holder.bindConnection(this.undecidedEventList.get(position), acceptOnClickListener, declineOnClickListener);
    }

    @Override
    public int getItemCount() {
        return this.undecidedEventList.size();
    }

}
