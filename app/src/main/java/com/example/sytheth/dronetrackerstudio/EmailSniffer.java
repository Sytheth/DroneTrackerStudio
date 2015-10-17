package com.example.sytheth.dronetrackerstudio;

import java.util.*;
import javax.activation.DataHandler;
import java.io.FileWriter;
import java.io.IOException;
import javax.mail.*;
import org.apache.commons.lang3.StringUtils;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;


/**
 * 
 * @author Stephen Cronin
 * Things to implement next:
 * 		-Writing to file
 * 		-Loading all unread messages & marking as unread - maybe send to a gmail folder
 * 		-Some sort of way to filter out random other emails
 */
public class EmailSniffer {
	static double[] gps;


	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		try {
			Session session = Session.getInstance(props, null);
			Store store = session.getStore();
			store.connect("imap.gmail.com", "croninstephen347@gmail.com", "Stephen55!");
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			Message msg = inbox.getMessage(inbox.getMessageCount());
			Address[] in = msg.getFrom();
			for (Address address : in) {
				System.out.println("FROM:" + address.toString());
			}

			Multipart mp = (Multipart) msg.getContent();
			BodyPart bp = mp.getBodyPart(0);
			System.out.println("SENT DATE:" + msg.getSentDate());
			System.out.println("SUBJECT:" + msg.getSubject());
			//System.out.println("CONTENT:" + bp.getContent());

			System.out.println("-------------------------");


			List<File> attachments = new ArrayList<File>();
			//for (Message message : msg) {
			Multipart multipart = (Multipart) msg.getContent();
			//Multipart multipart = (Multipart) message.getContent();
			// System.out.println(multipart.getCount());

			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
						!StringUtils.isNotBlank(bodyPart.getFileName())) {
					continue; // dealing with attachments only
				} 
				InputStream is = bodyPart.getInputStream();
				File f = new File("E:/Workspace/SE 300/" + bodyPart.getFileName());

				try{
					javaxt.io.Image image = new javaxt.io.Image(f);
					double[] gps = image.getGPSCoordinate();
					System.out.println("N: " + gps[1]); //N
					System.out.println("W: " + gps[0]); //W
				} catch(Exception e){//No coodinate data
					System.out.println(e);
				}

				FileOutputStream fos = new FileOutputStream(f);
				byte[] buf = new byte[4096];
				int bytesRead;
				while((bytesRead = is.read(buf))!=-1) {
					fos.write(buf, 0, bytesRead);
				}
				fos.close();
				attachments.add(f);
			}
			//}
		
		} catch (Exception mex) {
			mex.printStackTrace();
		}
	}

	public static void fileLogger(){

	}
}