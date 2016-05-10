package edu.scu.gsgapp.activity;

import android.app.ProgressDialog;
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
        validateLogin();

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
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        validateLogin();
    }

    private void validateLogin() {
        super.appAction.validateLogin(new ActionCallbackListener<Void>() {

            @Override
            public void onSuccess(Void data) {
                Toast.makeText(context, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String message) {
                // Do nothing
            }

        });
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
            }
        });
    }

    public void login(View view) {
        String email = this.emailEdit.getText().toString();
        String password = this.passwordEdit.getText().toString();
        this.loginButton.setEnabled(false);

        final ProgressDialog progressDialog = ProgressDialog.show( LoginActivity.this, "", "Logging in...", true );
        boolean stayLoggedIn = true;
        super.appAction.login(email, password, stayLoggedIn, new ActionCallbackListener<String>() {

            @Override
            public void onSuccess(String data) {
                progressDialog.cancel();
                Toast.makeText(context, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                loginButton.setEnabled(true);
            }

        });
    }

}
