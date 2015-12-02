/*



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
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;



*/
/**
 *
 *
 * @author Stephen Cronin
 *//*


public class EmailSniffer {
	static double[] gps;

	*/
/**
	 * Receives all unread messages, filters them, and parses and stores the relevant data.
	 *
	 *//*

	*/
/**
	 * Run on object instantiation.
	 * @param args Arguments sent on initialization.
	 *//*

	public static void main(String[] args) {
		query();
	}
	*/
/**
	 * Pulls down new emails from server, parses the data, and then stores it.
	 *//*

	public static void query(){
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

				Boolean sender = true;
				if (sender) {//Sent from a source we care about


					//Prepare database write
					FileWriter fileWritter = new FileWriter("Database.txt", true);
					BufferedWriter bufferWritter = new BufferedWriter(fileWritter);

					Boolean endStuff = false;
					String writeString = "";
					try{
						//Pull data from message
						Multipart mp = (Multipart) msg.getContent();
						BodyPart bp = mp.getBodyPart(0);

						writeString = writeString + msg.getSubject() + "|";
						if(writeString.split("|").length < 3){
							endStuff = true;
						}
					}catch(Exception f){

					}


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
						File currentDirectory = new File(new File("").getAbsolutePath());
						File f = new File(currentDirectory.getCanonicalPath() + "\\" + bodyPart.getFileName());
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
					if(endStuff){
						bufferWritter.write(writeString);//Write entry to database
						bufferWritter.newLine();//Next entry goes on next line
						bufferWritter.close();
					}
				}//End if

				msg.setFlag(Flags.Flag.SEEN, true); //Marks the email as read
			}//End for
		} catch (Exception mex) {
			mex.printStackTrace();
		}
		sort();
	}
	*/
/**
	 * Sorts the database entries by date where the oldest entry is first.
	 *//*

	public static void sort(){
		String initialItem = "";
		ArrayList<String[]> a = new ArrayList<String[]>();
		try{//Load all rows into an array here
			BufferedReader br = new BufferedReader(new FileReader("Database.txt"));
			while ((initialItem = br.readLine()) != null){
				String[] items = initialItem.split("\\|");
				a.add(items);
			}
			br.close();
		}catch(Exception e){
		}

		ArrayList<String> temp = new ArrayList<String>();
		for(int i = 0; i < a.size(); i++){
			String[] b = a.get(i)[1].split(" ");
			String[] d = b[3].split(":");
			String c = b[5] + m2d(b[1]) + b[2] + d[0] + d[1] + d[2];
			temp.add(c);
		}

		Collections.sort(temp.subList(0, temp.size()));

		ArrayList<String[]> t = new ArrayList<String[]>();

		for(int j = 0; j < a.size(); j++){
			for(int i = 0; i < a.size(); i++){

				String s = temp.get(j);
				String[] b = a.get(i)[1].split(" ");
				String[] d = b[3].split(":");
				String c = b[5] + m2d(b[1]) + b[2] + d[0] + d[1] + d[2];

				if(s.contains(c)){
					t.add(a.get(i));
					break;
				}
			}
		}

		try{
			FileWriter fileWritter = new FileWriter("Database.txt", false);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			for(int i = 0; i < temp.size(); i++){
				String w = t.get(i)[0] + "|" + t.get(i)[1] + "|" + t.get(i)[2] + "|" + t.get(i)[3] + "|" + t.get(i)[4];
				bufferWritter.write(w);
				bufferWritter.newLine();
			}
			bufferWritter.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}//End sort
	*/
/**
	 * Converts month code into a string of the integer equivalent.
	 * @param month Month in shortened character form.
	 * @return
	 *//*

	public static String m2d(String month){
		if(month == "Jan"){
			return "1";
		}else if(month.contains("Feb")){
			return "2";
		}else if(month.contains("Mar")){
			return "3";
		}else if(month.contains("Apr")){
			return "4";
		}else if(month.contains("May")){
			return "5";
		}else if(month.contains("June")){
			return "6";
		}else if(month.contains("July")){
			return "7";
		}else if(month.contains("Aug")){
			return "8";
		}else if(month.contains("Sep")){
			return "9";
		}else if(month.contains("Oct")){
			return "10";
		}else if(month.contains("Nov")){
			return "11";
		}else if(month.contains("Dec")){
			return "12";
		}else{
			return "99";
		}
	}
}


*/
