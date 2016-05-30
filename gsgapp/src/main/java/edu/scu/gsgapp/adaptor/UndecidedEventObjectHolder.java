package edu.scu.gsgapp.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import edu.scu.gsgapp.databinding.UndecidedEventRowBinding;
import edu.scu.model.EventUndecided;

/**
 * Created by chuanxu on 5/27/16.
 */
public class UndecidedEventObjectHolder extends RecyclerView.ViewHolder {

    private UndecidedEventRowBinding binding;

    public UndecidedEventObjectHolder(UndecidedEventRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindConnection(EventUndecided undecidedEvent, final View.OnClickListener acceptOnClickListener, final View.OnClickListener declineOnClickListener) {
        binding.setUndecidedEvent(undecidedEvent);
        binding.executePendingBindings();

        binding.buttonDecline.setOnClickListener(declineOnClickListener);
        binding.buttonAccept.setOnClickListener(acceptOnClickListener);
    }

}
