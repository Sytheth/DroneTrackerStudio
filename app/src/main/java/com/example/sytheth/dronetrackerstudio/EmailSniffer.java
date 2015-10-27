/*

		import java.util.*;
		import javax.activation.DataHandler;
		import java.io.FileWriter;
		import java.io.IOException;
		import javax.mail.*;
		import javax.mail.Flags.Flag;
		import javax.mail.search.FlagTerm;

//import org.apache.commons.lang3.StringUtils;
		import java.io.InputStream;
		import java.io.FileOutputStream;
		import java.io.BufferedWriter;
		import java.io.File;


*/
/**
 *
 *
 * @author Stephen Cronin
 *         Things to implement next:
 *         -Writing to file
 *         -Loading all unread messages & marking as unread - maybe send to a gmail folder
 *         -Some sort of way to filter out random other emails
 *//*

public class EmailSniffer {
	static double[] gps;


	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		try {
			Session session = Session.getInstance(props, null);
			Store store = session.getStore();
			store.connect("imap.gmail.com", "croninstephen347@gmail.com", "Stephen55!");


import java.util.*;
import javax.activation.DataHandler;
import java.io.FileWriter;
import java.io.IOException;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;
import org.apache.commons.lang3.StringUtils;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.File;



/**
 *
 *
 * @author Stephen Cronin

/**
public class EmailSniffer {
	/**
	 * @param gps GPS coordinates to be pulled from the .jpg.
	 *//**
	static double[] gps;
	*/
	/**
	 * Receives all unread messages, filters them, and parses and stores the relevant data.
	 *
	 *//**
	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		try {
			Session session = Session.getInstance(props, null);
			Store store = session.getStore();
			store.connect("imap.gmail.com", "croninstephen347@gmail.com", "Stephen55!");
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));//Looks for all unread emails
			for (int z = 0; z < messages.length; z++) { //Loop through all emails
				Message msg = messages[z];
				Address[] in = msg.getFrom();

				//Check to see the message came from a relevant address
				Boolean sender = false;
				for (Address address : in) {
					if (address.toString().contains("gdcerau@gmail.com") || address.toString().contains("devin@isovirtual.com") || address.toString().contains("DroneyTracker@Droney.com")) {
						System.out.println("FROM:" + address.toString());
						sender = true;
					}
				}

				if (sender) {//Sent from a source we care about
					//Prepare database write
					FileWriter fileWritter = new FileWriter("Database.txt", true);
					BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
					String writeString = "";

					//Pull data from message
					Multipart mp = (Multipart) msg.getContent();
					BodyPart bp = mp.getBodyPart(0);
					//System.out.println("SENT DATE:" + msg.getSentDate());
					//System.out.println("SUBJECT:" + msg.getSubject());

					writeString = writeString + msg.getSubject() + "\t";

					//System.out.println("CONTENT:" + bp.getContent());
					//System.out.println("-------------------------");


					//Get the picture from the email
					List<File> attachments = new ArrayList<File>();
					Multipart multipart = (Multipart) msg.getContent();
					for (int i = 0; i < multipart.getCount(); i++) {
						BodyPart bodyPart = multipart.getBodyPart(i);
						if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
								!StringUtils.isNotBlank(bodyPart.getFileName())) {
							continue; // dealing with attachments only
						}
						InputStream is = bodyPart.getInputStream();
						File f = new File("E:/Documents/Workspace/SE 300 Database/" + bodyPart.getFileName());
						try {
							//Pull GPS data from image
							javaxt.io.Image image = new javaxt.io.Image(f);
							double[] gps = image.getGPSCoordinate();
							//writeString = writeString + "N: " + gps[1] + "\t" + "W: " + gps[0] + "\t";
						} catch (Exception e) {//No coordinate data
							//writeString = writeString + "Coordinate data not available.\t";
						}
						//Write image file and save it
						FileOutputStream fos = new FileOutputStream(f);
						byte[] buf = new byte[4096];
						int bytesRead;
						while ((bytesRead = is.read(buf)) != -1) {
							fos.write(buf, 0, bytesRead);
						}
						fos.close();
						attachments.add(f);
						writeString = writeString + f.getPath();//Last entry in database
					}

					bufferWritter.write(writeString);//Write entry to database
					bufferWritter.newLine();//Next entry goes on next line
					bufferWritter.close();
					System.out.println("-------------------" + "\n");

				}//End if

				msg.setFlag(Flags.Flag.SEEN, true); //Marks the email as read
			}//End for

		} catch (Exception mex) {
			mex.printStackTrace();
		}
	}
}


Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));//Looks for all unread emails
			for (int z = 0; z < messages.length; z++) { //Loop through all emails
				Message msg = messages[z];
				Address[] in = msg.getFrom();

				//Check to see the message came from a relevant address
				Boolean sender = false;
				for (Address address : in) {
					if (address.toString().contains("gdcerau@gmail.com") || address.toString().contains("devin@isovirtual.com") || address.toString().contains("DroneyTracker@Droney.com")) {
						System.out.println("FROM:" + address.toString());
						sender = true;
					}
				}

				if (sender) {//Sent from a source we care about
					//Prepare database write
					FileWriter fileWritter = new FileWriter("Database.txt", true);
					BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
					String writeString = "";

					//Pull data from message
					Multipart mp = (Multipart) msg.getContent();
					BodyPart bp = mp.getBodyPart(0);
					//System.out.println("SENT DATE:" + msg.getSentDate());
					//System.out.println("SUBJECT:" + msg.getSubject());

					writeString = writeString + msg.getSubject() + "\t";

					//System.out.println("CONTENT:" + bp.getContent());
					//System.out.println("-------------------------");


					//Get the picture from the email
					List<File> attachments = new ArrayList<File>();
					Multipart multipart = (Multipart) msg.getContent();
					for (int i = 0; i < multipart.getCount(); i++) {
						BodyPart bodyPart = multipart.getBodyPart(i);
						if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
								!StringUtils.isNotBlank(bodyPart.getFileName())) {
							continue; // dealing with attachments only
						}
						InputStream is = bodyPart.getInputStream();
						File f = new File("E:/Documents/Workspace/SE 300 Database/" + bodyPart.getFileName());
						try {
							//Pull GPS data from image
							javaxt.io.Image image = new javaxt.io.Image(f);
							double[] gps = image.getGPSCoordinate();
							//writeString = writeString + "N: " + gps[1] + "\t" + "W: " + gps[0] + "\t";
						} catch (Exception e) {//No coordinate data
							//writeString = writeString + "Coordinate data not available.\t";
						}
						//Write image file and save it
						FileOutputStream fos = new FileOutputStream(f);
						byte[] buf = new byte[4096];
						int bytesRead;
						while ((bytesRead = is.read(buf)) != -1) {
							fos.write(buf, 0, bytesRead);
						}
						fos.close();
						attachments.add(f);
						writeString = writeString + f.getPath();//Last entry in database
					}

					bufferWritter.write(writeString);//Write entry to database
					bufferWritter.newLine();//Next entry goes on next line
					bufferWritter.close();
					System.out.println("-------------------" + "\n");

				}//End if

				msg.setFlag(Flags.Flag.SEEN, true); //Marks the email as read
			}//End for

		} catch (Exception mex) {
			mex.printStackTrace();
		}
	}
}
*/

