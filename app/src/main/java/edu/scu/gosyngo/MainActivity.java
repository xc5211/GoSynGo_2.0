package edu.scu.gosyngo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;

import edu.scu.api.ApiResponse;
import edu.scu.util.lib.BackendlessSettings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        String userEmail1 = "huangsichao2015@gmail.com";
        String password1 = "test";
        String name1 = "Sichao";

        String userEmail2 = "whairong2011@gmail.com";
        String password2 = "test";
        String name2 = "Hairong";
        Backendless.initApp(this, BackendlessSettings.APP_ID, BackendlessSettings.SECRET_KEY_ANDROID, BackendlessSettings.APP_VERSION);


        ApiResponse<String> response = null;
        BackendlessUser user = new BackendlessUser();
        user.setEmail(userEmail1);
        user.setPassword(password1);
        user.setProperty("name", name1);
        user.setProperty("isGoogleCalendarImported", false);

        try {
            user = Backendless.UserService.register(user);
        } catch(BackendlessException exception) {
            Log.i(">>>>>>>>>>>>>>>>>>>>>>", exception.getCode());
        }

        assert response.getObj() != null;
        assert response.getObj() instanceof String;
        assert response.getObjLists() == null;
        assert response.getEvent() == "0";
        assert response.getMsg() == "Registration is successful";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
