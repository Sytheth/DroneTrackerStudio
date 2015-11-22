package com.example.sytheth.dronetrackerstudio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by Sytheth on 11/21/2015.
 */
public class Signin extends DialogFragment {
    public Hasher infoHasher;
    public Location location;
    public File file;
    public Signin(Hasher infoHasher2, Location location2, File file2) {
        infoHasher = infoHasher2;
        location = location2;
        file = file2;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.login, null))
                // Add action buttons
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        // sign in the user ...
                        try {
                            EditText edit1 = (EditText)f.findViewById(R.id.username);
                            EditText edit2 = (EditText)f.findViewById(R.id.password);
                            String user = edit1.getText().toString();
                            String password = edit2.getText().toString();
                            infoHasher.getInfo(user, password);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        }
                        Email email = null;
                        try {
                            email = new Email(infoHasher.getStream());
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String[] toArr = {"croninstephen347@gmail.com"};
                        email.setTo(toArr);
                        email.setFrom("DroneyTracker@Droney.com");

                        // Collect informaiton from GUI
                        EditText editText = (EditText) getActivity().findViewById(R.id.editText1);
                        String description = editText.getText().toString();
                        Calendar c = Calendar.getInstance();
                        String dateTime = c.getTime().toString();

                        // If location was found, add it to the subject line
                        if (!location.getProvider().contentEquals("Test")) {
                            email.setSubject(description + "|" + dateTime + "|" + "Lat: " + location.getLatitude() + "|" + "Long: " + location.getLongitude());
                        } else {
                            email.setSubject(description + "|" + dateTime + "|" + " Location Unavailable");
                        }
                        email.setBody("");

                        // Send the email
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        Object[] obarray = {email, file};
                        runner.execute(obarray);

                        // Reset camera *** to be added later
                        Toast.makeText(getActivity(), "Email Sent!", Toast.LENGTH_SHORT).show();
                        Signin.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Signin.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


}
