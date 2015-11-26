package com.example.sytheth.dronetrackerstudio;

import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Hasher
{
    protected static File outfile;
    protected static SecretKey skey;
    protected byte[] decodedKey;
    protected FileInputStream encfis;
    protected static CipherInputStream cis;

    protected Hasher(String path)
    {
        outfile = new File(path);
        decodedKey = Base64.decode("xhksO0ZGMNmH57EI4T0G/w==", Base64.DEFAULT);
        skey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

    }

    protected boolean exist() throws IOException
    {
        if(outfile.exists()) {
            //encfis = new FileInputStream(outfile);
            return true;
        }
        else
        {
            //outfile.createNewFile();
            return false;
        }
    }

    protected void getInfo(String username, String password)  throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException
    {
        String data = username + password;
        InputStream is = new ByteArrayInputStream(data.getBytes());
        encrypt(is);
    }

    protected CipherInputStream getStream()
    {
        return cis;
    }

    protected static void encrypt(InputStream f) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException
    {
        FileOutputStream fos = new FileOutputStream(outfile);
        Cipher encipher = Cipher.getInstance("AES");
        cis = new CipherInputStream(f, encipher);

        encipher.init(Cipher.ENCRYPT_MODE, skey);

        int read;

        while((read = cis.read())!=-1)
        {
            fos.write((char)read);
            fos.flush();
        }

        fos.close();
        cis.close();
    }

    protected static String decrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException
    {
        int read;
        FileInputStream ef = new FileInputStream(outfile);
        OutputStream os = new ByteArrayOutputStream();
        Cipher decipher = Cipher.getInstance("AES");
        decipher.init(Cipher.DECRYPT_MODE, skey);

        CipherOutputStream cos = new CipherOutputStream(os,decipher);

        while((read=ef.read())!=-1)
        {
            cos.write(read);
            cos.flush();
        }

        cos.close();
        ef.close();

        return os.toString();
    }

}
