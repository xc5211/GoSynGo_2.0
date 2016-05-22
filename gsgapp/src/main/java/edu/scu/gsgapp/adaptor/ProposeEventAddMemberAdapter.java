package edu.scu.gsgapp.adaptor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Blood on 2016/5/20.
 */
public class ProposeEventAddMemberAdapter extends BaseAdapter {
    private Context context;
    List<String> personNames;

    public ProposeEventAddMemberAdapter(Context context, List<String> personNames) {
        this.context = context;
        this.personNames = personNames;
    }

    @Override
    public int getCount() {
        return personNames.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if(convertView == null) {
            textView = new TextView(context);
            textView.setText(personNames.get(position));
        } else {
            textView = (TextView) convertView;
        }

        return textView;
    }

    @Override
    public Object getItem(int position) {
        return personNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
