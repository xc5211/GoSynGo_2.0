package edu.scu.gsgapp.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.scu.gsgapp.R;

/**
 * Created by chuanxu on 5/14/16.
 */
public class DashboardEventFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_event, container, false);

        ListView eventAsLeaderView = (ListView) view.findViewById(R.id.listview_event_asleader);
        ListView eventAsMemberView = (ListView) view.findViewById(R.id.listview_event_asmember);

        // TODO: setup vieventAsLeaderView & eventAsMemberView



        return view;
    }
}
