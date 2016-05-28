package edu.scu.gsgapp.adaptor;

import android.support.v7.widget.RecyclerView;

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

    public void bindConnection(EventUndecided undecidedEvent) {
        binding.setUndecidedEvent(undecidedEvent);
        binding.executePendingBindings();
    }

}
