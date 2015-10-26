package com.example.sytheth.dronetrackerstudio;

import android.os.AsyncTask;
import android.widget.Toast;

import javax.xml.transform.Result;

/**
 * Created by Sytheth on 10/7/2015.
 * awalts first comment for SE300 on 10/11/2015
 * Test push by stephen for the last time
 *
 *
 *
 *
 */
public class AsyncTaskRunner extends AsyncTask<Email, Void, Result>{
    @Override
    protected Result doInBackground(Email... params) {
        Email email = params[0];
        /*
        try {
            email.addAttachment(file.getPath());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        try {
            email.send();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
