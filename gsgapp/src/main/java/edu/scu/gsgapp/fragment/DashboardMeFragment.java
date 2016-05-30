package edu.scu.gsgapp.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.GsgApplication;
import edu.scu.gsgapp.R;
import edu.scu.gsgapp.activity.LoginActivity;

/**
 * Created by chuanxu on 5/15/16.
 */
public class DashboardMeFragment extends Fragment {

    private GsgApplication gsgApplication;
    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private Button settingButton;
    private Button logoutButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_me, container, false);

        this.gsgApplication = (GsgApplication) getActivity().getApplication();

        this.profileImageView = (ImageView) view.findViewById(R.id.imageView_dashboard_me_profile);
        //TO DO: click image to upload and initiate camera

        this.logoutButton = (Button) view.findViewById(R.id.button_logout);
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        this.usernameTextView = (TextView) view.findViewById(R.id.text_view_dashboard_me_username);
        String username = gsgApplication.getAppAction().getHostPerson().getName();
        usernameTextView.setText(username);

        this.emailTextView = (TextView) view.findViewById(R.id.textView_dashboard_me_email);
        String email = gsgApplication.getAppAction().getHostPerson().getEmail();
        emailTextView.setText(email);

        this.settingButton = (Button) view.findViewById(R.id.button_dashboard_me_setting);
        //To Do:

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
