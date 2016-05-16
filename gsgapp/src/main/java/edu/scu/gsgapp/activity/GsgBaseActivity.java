package edu.scu.gsgapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;

import edu.scu.core.AppAction;
import edu.scu.gsgapp.GsgApplication;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 4/16/16.
 */
public abstract class GsgBaseActivity extends AppCompatActivity {

    protected Context context;
    protected GsgApplication gsgApplication;
    protected AppAction appAction;
    protected Person hostPerson;
    public String fileUri = "PersonMap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        gsgApplication = (GsgApplication) this.getApplication();
        appAction = gsgApplication.getAppAction();
        hostPerson = appAction.getHostPerson();
    }

    protected AppAction getAppAction() {
        return this.appAction;
    }

    public void saveHostPerson() {
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(),"") + File.separator + fileUri));

            // TODO: check person
            if(appAction.getHostPerson() != null) {
                out.writeObject(new HashMap<String, Person>().put(appAction.getHostPerson().getUserId(), appAction.getHostPerson()));
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHostPerson(String userID) {
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(new File(new File(getFilesDir(),"") + File.separator + fileUri)));
            HashMap<String, Person> buffedPersonMap = (HashMap<String, Person>) input.readObject();

            // TODO: check person
            if(buffedPersonMap.get(userID) != null) {
                this.appAction.setHostPerson(buffedPersonMap.get(userID));
            }

            input.close();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
