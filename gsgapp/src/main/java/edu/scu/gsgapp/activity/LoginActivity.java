package edu.scu.gsgapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.R;

public class LoginActivity extends GsgBaseActivity {

    private EditText emailEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initListeners();

        /*
        Toolbar toolBar = (Toolbar) findViewById(R.id.button_register);
        setSupportActionBar(toolBar);

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
        */
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.button_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    private void initViews() {
        this.emailEdit = (EditText) findViewById(R.id.edit_email);
        this.passwordEdit = (EditText) findViewById(R.id.edit_password);
        this.loginButton = (Button) findViewById(R.id.button_login);
        this.registerButton = (Button) findViewById(R.id.button_register);
    }

    private void initListeners() {
        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });
    }

    public void login(View view) {
        String email = this.emailEdit.getText().toString();
        String password = this.passwordEdit.getText().toString();
        this.loginButton.setEnabled(false);

        boolean stayLoggedIn = true;
        super.appAction.login(email, password, stayLoggedIn, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Toast.makeText(context, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DashboardActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                loginButton.setEnabled(true);
            }
        });
    }

}

