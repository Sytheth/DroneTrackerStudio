package com.example.sytheth.dronetrackerstudio;

import android.util.Log;

import junit.framework.TestCase;

/**
 * Created by Sytheth on 11/29/2015.
 */
public class Tester extends TestCase {

    MainActivity a = new MainActivity();
    protected void setUp() {

    }

    public void testDecConversion() {

        String b = a.dec2DMS(-105.9876543);
        assertTrue(b.equals("105/1,59/1,15555/1000"));
    }
    public void testHasher(){
        Hasher h = new Hasher("data/data/com.example.sytheth.dronetrackerstudio/files/hast2.txt");

        try {
            h.getInfo("Hello", "Hi");
            String original = h.decrypt();
            assertEquals(original, "HelloHi");
        }catch(Exception e){
            fail();
        }
    }
    public void testEmail(){
        try{
        Hasher h = new Hasher("data/data/com.example.sytheth.dronetrackerstudio/files/hast.txt");
        Email email = new Email(null);
        String[] toArr = {"croninstephen347@gmail.com"};
        email.setTo(toArr);
        email.setFrom("DroneyTracker@Droney.com");

        // If location was found, add it to the subject line

            email.setSubject("hello");


        email.setBody("asdf");

    assertTrue(email.send());
}catch(Exception e){
    fail();
}
    }
}
