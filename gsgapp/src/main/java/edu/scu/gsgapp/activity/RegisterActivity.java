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
import edu.scu.model.Person;

/**
 * Created by chuanxu on 4/16/16.
 */
public class RegisterActivity extends GsgBaseActivity {
    private EditText firstnameEdit;
    private EditText lastnameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initListeners();
    }

    //validate if two passwords are the same

    protected void initViews() {
        this.firstnameEdit = (EditText) findViewById(R.id.firstname_editText);
        this.lastnameEdit = (EditText) findViewById((R.id.lastname_editText));
        this.emailEdit = (EditText) findViewById(R.id.email_editText);
        this.passwordEdit = (EditText) findViewById(R.id.password_editText);
        this.submitButton = (Button) findViewById(R.id.submit_button);

    }

    protected void initListeners() {
        this.submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    private void register() {
        String password = this.passwordEdit.getText().toString();
        String firstname = this.firstnameEdit.getText().toString();
        String lastname = this.lastnameEdit.getText().toString();
        String userEmail = this.emailEdit.getText().toString();
        //this.submitButton.setEnabled(false);

        final ProgressDialog progressDialog = ProgressDialog.show( RegisterActivity.this, "", "Regisrering...", true );

        getAppAction().register( userEmail,password,firstname,lastname,new ActionCallbackListener<Person>() {

            public void onSuccess(Person data) {
                Toast.makeText(context, "Register successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                progressDialog.cancel();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                //submitButton.setEnabled(true);
                progressDialog.cancel();

            }
        });
    }
}
