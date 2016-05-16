package edu.scu.gsgapp.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.GsgApplication;
import edu.scu.gsgapp.R;

/**
 * Created by chuanxu on 5/15/16.
 */
public class DashboardMeFragment extends Fragment {

    private GsgApplication gsgApplication;
    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_me, container, false);

        this.gsgApplication = (GsgApplication) getActivity().getApplication();

        this.logoutButton = (Button) view.findViewById(R.id.button_logout);
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    public void logout() {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "Logging out...", true );

        gsgApplication.getAppAction().logout(new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                Toast.makeText(getActivity(), R.string.toast_logout_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                progressDialog.cancel();
            }

            @Override
            public void onFailure(String message) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
