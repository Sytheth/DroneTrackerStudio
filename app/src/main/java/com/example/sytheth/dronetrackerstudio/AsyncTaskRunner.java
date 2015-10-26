package com.example.sytheth.dronetrackerstudio;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;

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
public class AsyncTaskRunner extends AsyncTask<Object, Void, Result>{
    @Override
    protected Result doInBackground(Object... params) {
        Email email = (Email)params[0];
        File file = (File)params[1];
        try {
            email.addAttachment(file.getPath());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            if(email.send()){
                System.out.println("YES");
            }
            else{
                System.out.println("No");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
