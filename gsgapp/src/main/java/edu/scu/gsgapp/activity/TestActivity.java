package edu.scu.gsgapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.scu.gsgapp.R;
import edu.scu.model.Event;
import edu.scu.model.Person;

public class TestActivity extends AppCompatActivity {
    Button testButton;
    Person person = new Person();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initialPerson();
        test();
    }

    public void test() {
        testButton = (Button) findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePerson();
            }
        });
    }

    public void initialPerson() {
        person.setName("sichao");
        List<Event> eventList = new ArrayList<>();
        Event event = new Event();
        event.setTitle("event");
        event.setLocation("lucas hall");
        eventList.add(event);
        person.setEventsAsLeader(eventList);
        person.setEmail("sichao@gmail.com");
        person.setUserId("abc123");
    }

    public void savePerson() {
        String filename = "test";
        ObjectOutput out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(),"")+File.separator+filename));
            out.writeObject(person);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
