//200647R


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;







/*
 * interface for the personal and official friend recipients(can send birthday wishes)
 */
interface Greetable {
	public void sendGreeting(String email,String date);
}


//**************************************************************************************************************************************


/*
 * class for the recipients, we don't have to instantiate the class, so it's declared as abstract
 */
abstract class Recipient{
	
	private String name;
	private String email;
	protected static Hashtable<String, Recipient> hashRecipients = new Hashtable<String, Recipient>();   //hash table to keep the recipient objects
	private static int no = 0;
	static LocalDateTime todayDate = LocalDateTime.now();
    static DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
    static String date = todayDate.format(myFormatDate);
	
	//constructor for Recipient objects
	public Recipient(String name, String email) {
		this.name = name;
		this.email = email;
		hashRecipients.put(email, this);    //adding the recipient objects to hash table
		no++;             //static member to keep the recipients count
	}

	/*
	 * Static method for getting the total number of recipients in the application
	 */
	public static int getNoOfRecipients() {
		return no;
	}

	public String getName() {
		return name;
	}


	public String getEmail() {
		return email;
	}

	public static Hashtable<String, Recipient> getHashRecipients(){
		return hashRecipients;
	}

    
		
	

	
}


//**************************************************************************************************************************************

/*
 * class for official recipients
 */
class OfficialRecipient extends Recipient {

	private String designation;

	
	public OfficialRecipient(String name, String email, String designation) {
		super(name,email);
		this.designation = designation;
	}


	public String getDesignation() {
		return designation;
	}

	
}


//**************************************************************************************************************************************


/*
 * class for official friend clients
 */
class CloseFriendOfficial extends OfficialRecipient implements Greetable{

	private String birthday;
	
	public CloseFriendOfficial(String name, String email, String designation, String birthday) {
		super(name, email, designation);
		this.birthday = birthday;
	}
	
	public String getBirthday() {
		return birthday;
	}

	
	/*
	 * method for sending birthday wish who have birthdays
	 */
	public void sendGreeting(String email, String date) {
		EmailObject emailObj = new EmailObject(email,"Birthday wish","Wish you a Happy Birthday. Thenujan",date);
		JavaSendMail.sendMail(emailObj);
		
	}
	
	
	
}


//**************************************************************************************************************************************


/*
 * class for personal clients
 */
class PersonalRecipient extends Recipient implements Greetable{
	
	private String nickName;
	private String birthday;
	
	public PersonalRecipient(String name, String nickName, String email, String birthday) {
		super(name, email);
		this.nickName = nickName;
		this.birthday = birthday;
	}

	public String getBirthday() {
		return birthday;
	}


	public String getNickName() {
		return nickName;
	}

	/*
	 * method for sending birthday wish who have birthdays
	 */
	public void sendGreeting(String email,String date) {
		EmailObject emailObj = new EmailObject(email,"Birthday wish","Hugs and love on your birthday. Thenujan",date);
		JavaSendMail.sendMail(emailObj);
		
	}

	
}



//**************************************************************************************************************************************



class EmailObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String email;
	private String subject;
	private String content;
	private String date;
	
	public EmailObject(String email, String subject, String content, String date) {
		super();
		this.email = email;
		this.subject = subject;
		this.content = content;
		this.date = date;
	}

	public String getEmail() {
		return email;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public String getDate() {
		return date;
	}
	
	
	
}


//**************************************************************************************************************************************


/*
 * class for serialize and deserialize email objects
 */
class EmailSerialization {
	 
    private static File myFile = new File("emailList.txt");           //Getting the file
 
    /*
     * method for deserialize email objects, and print the mails which sent in the given dates
     */
    public static void deserialize(String date){
 
        try {
 
        	myFile.createNewFile();         //create a new file if the mentioned file doesn't exist   
        }
 
        catch (Exception e) {
        }
 
        if (myFile.length() != 0) {
 
            try {
 
                FileInputStream fistream = new FileInputStream("emailList.txt");
 
                ObjectInputStream oiStream = new ObjectInputStream(fistream);
 
                EmailObject emailObj = null;
                boolean found = false;
 
                while (fistream.available() != 0) {              //Reading every objects in the file
                	emailObj = (EmailObject)oiStream.readObject();
                    String mailDate = emailObj.getDate();
 
                    if(date.equals(mailDate)) {         //printing the mail details in the given date
                    	String emailAddress = emailObj.getEmail();
                    	System.out.println("EmailAddress: "+ emailAddress +", Subject: "+emailObj.getSubject());
                    	found = true;
                    }
                }
                
                if (!found) {
                	System.out.println("No mails sent on "+date);
                }
 
                oiStream.close();
                fistream.close();
 
            }
 
            catch (Exception e) {
 
                System.out.println("Error Occurred" + e);
 
                e.printStackTrace();
            }
        }
        return ;
    }
 
    
    /*
     * method for serialize email objects. email objects are passed as parameters.
     */
    public static void serialize(EmailObject emailObj)
    {
       
 
        if (emailObj != null) {
            try {
 
                FileOutputStream fos = null;
 
                fos = new FileOutputStream("emailList.txt", true);
 
                if (myFile.length() == 0) {         //if file has nothing in it write the object as usual
                    ObjectOutputStream ooStream = new ObjectOutputStream(fos);
                    ooStream.writeObject(emailObj);
                    ooStream.close();
                }
 
                else {         //if file already have byte stream written in it, appending using the created class SubObjectOutputStream.
 
                	SubObjectOutputStream ooStream = null;
                    ooStream = new SubObjectOutputStream(fos);
                    ooStream.writeObject(emailObj);
 
              
                    ooStream.close();
                }
 
              
                fos.close();
            }
 
            catch (Exception e) {
 
                System.out.println("Error Occurred" + e);
            }
 
        }
 
        return ;
    }
}



//**************************************************************************************************************************************


/*
 * class to avoid the header to written more than once which causes StreamCorruptedException.
 */
class SubObjectOutputStream extends ObjectOutputStream {
	 
	SubObjectOutputStream() throws IOException
    {
 
        super();
    }
 
	SubObjectOutputStream(OutputStream o) throws IOException
    {
        super(o);
    }
 
	/*
	 * do nothing to avoid to create another header written.
	 */
	public void writeStreamHeader() throws IOException
    {
        return;
    }
}



//**************************************************************************************************************************************


/*
 * class for sending emails
 */
class JavaSendMail {
	
    
    /*
     * method to send emails
     */
	public static void sendMail(EmailObject emailObj) {
		
		
		
		Properties properties = new Properties();
		
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		
		String username = "thenujanthenu22@gmail.com";
		String password = "";
		

		Session session = Session.getInstance(properties,new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		Message message = prepareMessage(session,username,emailObj);
		
		try {
			Transport.send(message);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		EmailSerialization.serialize(emailObj);
		
	}
	

	private static Message prepareMessage(Session session,String username,EmailObject emailobject) {
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(username));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailobject.getEmail()));
			message.setSubject(emailobject.getSubject());
			message.setText(emailobject.getContent());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return message;
	}
	
	
}

/*
 
class SentBirthdayList{
	String date;
	ArrayList<String> sentList = new ArrayList<>();
	
	public SentBirthdayList(String date, ArrayList<String> sentList) {
		this.date = date;
		this.sentList = sentList;
	}
	
	
}

*/

//**************************************************************************************************************************************
//**************************************************************************************************************************************





public class EmailClient {

public static void main(String[] args) throws IOException, ClassNotFoundException {
		
	
		LocalDateTime todayDate = LocalDateTime.now();
		DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
	    String formattedDate = todayDate.format(myFormatDate);
	    String date = formattedDate.substring(5);
	    ArrayList<Greetable> birthdayRecipients = new ArrayList<>();       // A list of recipients to whom a birthday greeting should be sent
	    
	    
	    try {                             
			File myObj = new File("clientList.txt");
			try {
				  
				   myObj.createNewFile();         //create a new file if the mentioned file doesn't exist   
		    }catch (Exception e) {}
			
			
			Scanner myReader = new Scanner(myObj);
			
			while (myReader.hasNextLine()) {
					String data = myReader.next();
					String data1 = myReader.nextLine();
					String[] details = data1.strip().split(",");             
				    createRecipientObjects(data,details,formattedDate,birthdayRecipients);         //creating objects for every recipient in the clientList

					
			}
			myReader.close();
		} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
		}		
		
	    
	    checkBirthdayWish(birthdayRecipients,formattedDate);      //checking whether we already sent the birthday wishes for the current day and if not send the wishes
	    
	    
	    
		try (Scanner scanner0 = new Scanner(System.in)) {
		    	
			while(true) {
			    
				System.out.println("Enter option type: \n"
									+ "1 - Adding a new recipient\n"
									+ "2 - Sending an email\n"
									+ "3 - Printing out all the recipients who have birthdays\n"
									+ "4 - Printing out details of all the emails sent\n"
									+ "5 - Printing out the number of recipient objects in the application");
				
					int option = scanner0.nextInt();
					
					if(option == -1) {
						System.out.println("Application closed!");
						break;
					}
						
		  
					switch(option){
						case 1:
							System.out.println("input format\n- Official: name,email,designation\n"
												+"- Office_friend: name,email,designation,birthday(yyyy/MM/dd)\n"
												+"- Personal: name,nickName,email,birthday(yyyy/MM/dd)");
							Scanner scanner1 = new Scanner(System.in);
							String input1= scanner1.nextLine();
							String[] recipientType = input1.split(" ");
							String data = recipientType[0];
							String[] details = recipientType[1].strip().split(",");
							
							
							FileWriter fileWriter = new FileWriter("clientList.txt",true);
							BufferedWriter buffWriter = new BufferedWriter(fileWriter); 
							PrintWriter printWriter = new PrintWriter(buffWriter);
							
							if(data.equals("Personal:")) {
								String emailAddress = details[2];
								if (Recipient.getHashRecipients().containsKey(emailAddress)) {     //avoiding the recipients added to the client list more than once
									System.out.println("Recipient details is already in the client list personal");
									break;
								}else {
									printWriter.println(input1);             //writing the details of the recipient to the file
									PersonalRecipient personal = new PersonalRecipient(details[0], details[1], details[2], details[3]); 
									String birthdate = details[3].substring(5);
									if(birthdate.equals(date)) {         //checking whether the Personal recipient has birthday on this day
										birthdayRecipients.add(personal);
										personal.sendGreeting(details[2], formattedDate);

									}
								}
							}else if (data.equals("Official:")){
								String emailAddress = details[1];

								if (Recipient.getHashRecipients().containsKey(emailAddress)) {
									System.out.println("Recipient details is already in the client list official");
									break;
								}else {
									printWriter.println(input1);             //writing the details of the recipient to the file
									@SuppressWarnings("unused")
									OfficialRecipient official = new OfficialRecipient(details[0], details[1], details[2]);
								}
							} else if(data.equals("Office_friend:")){
								String emailAddress = details[1];
								if (Recipient.getHashRecipients().containsKey(emailAddress)) {
									System.out.println("Recipient details is already in the client list");
									break;
								}else {
									printWriter.println(input1);             //writing the details of the recipient to the file
									CloseFriendOfficial office_friend = new CloseFriendOfficial(details[0], details[1], details[2], details[3]);
									String birthdate = details[3].substring(5);
									if(birthdate.equals(date)) {        //checking whether the Personal recipient has birthday on this day
										birthdayRecipients.add(office_friend);
										office_friend.sendGreeting(details[1], formattedDate);

									}
								}
							}else {	
								System.out.println("invalid input\n");
							}

					
							printWriter.close();
							buffWriter.close();
							fileWriter.close();
							break;
							
						case 2:
							System.out.println("input format - email,subject,content\n");     // input format - email,subject,content
							Scanner scanner2 = new Scanner(System.in);
							String[] input2= scanner2.nextLine().split(",");
							EmailObject emailObj = new EmailObject(input2[0], input2[1], input2[2], formattedDate);      //creating email object
							JavaSendMail.sendMail(emailObj);             //sending email
							break;
							
						case 3:
							printBirthdayDetails(birthdayRecipients, date);			//calling the method to print the details of the recipients who have birthday on current date		
							System.out.print("\n");
							break;
							
						case 4:
							System.out.println("input format - yyyy/MM/dd (ex: 2018/09/17)\n");  // input format - yyyy/MM/dd (ex: 2018/09/17)// input format - yyyy/MM/dd (ex: 2018/09/17)
							Scanner scanner3 = new Scanner(System.in);
							String date1 = scanner3.nextLine();           // code to print the details of all the emails sent on the input date
							EmailSerialization.deserialize(date1);
							System.out.print("\n");
							break;
							
						case 5:
							System.out.println(Recipient.getNoOfRecipients());       //print the no of recipients in the application
							break;
				
						}
						
						} 
			}catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
 }


		/*
		 * method to create the recipient objects
		 */
		public static void createRecipientObjects(String data,String[] details, String formattedDate,ArrayList<Greetable> birthdayRecipients) {	
			String date = formattedDate.substring(5);
				if (data.equals("Official:")) {         //checking the type of the recipient
					@SuppressWarnings("unused")
					OfficialRecipient official = new OfficialRecipient(details[0], details[1], details[2]);
				}
				else if (data.equals("Office_friend:")) {                //checking the type of the recipient
					CloseFriendOfficial office_friend = new CloseFriendOfficial(details[0], details[1], details[2], details[3]);
					String birthdate = details[3].substring(5);
					if(birthdate.equals(date)) {        //checking whether the Personal recipient has birthday on this day
						birthdayRecipients.add(office_friend);
					}
				}
				else if (data.equals("Personal:")){                          //checking the type of the recipient
					PersonalRecipient personal = new PersonalRecipient(details[0], details[1], details[2], details[3]); 
					String birthdate = details[3].substring(5);
					if(birthdate.equals(date)) {         //checking whether the Personal recipient has birthday on this day
						birthdayRecipients.add(personal);
					}
				}
		}

		/*
		 * method to check whether we already sent the birthday wishes for the current day
		 */
		public static void checkBirthdayWish(ArrayList<Greetable> birthdayRecipients,String formattedDate) throws IOException {
			File myObj = new File("sentList.txt");
			try {
			  
			   myObj.createNewFile();         //create a new file if the mentioned file doesn't exist   
	        }
	 
	        catch (Exception e) {
	        }
			
			Scanner myReader = new Scanner(myObj);
			
			if(!myReader.hasNextLine()) {         //if the file is empty
				FileWriter fileWriter = new FileWriter("sentList.txt",true);         //open the file in append mode
				fileWriter.write(formattedDate);
				fileWriter.close();
				SendBirthdayWish(birthdayRecipients, formattedDate);    //sending wishes
			}else if(myReader.hasNextLine()) {         //if file has something written on it
				String data = myReader.nextLine();
				if(!data.equals(formattedDate)) {         //if the date writtrn in the file is not today's date
					FileWriter fileWriter1 = new FileWriter("sentList.txt");         //open the file in the write mode to over write the file
					fileWriter1.write(formattedDate);     //writing the todays date
					fileWriter1.close();
					SendBirthdayWish(birthdayRecipients, formattedDate);          //method to send wishes
				}
			}
			
			myReader.close();
		}


		
		/*
		 * method to send birthday wishes
		 */
		public static void SendBirthdayWish(ArrayList<Greetable> birthdayRecipients,String formattedDate) {
			for (int i = 0; i< birthdayRecipients.size(); i++) {           //traverse the birthday list, and send a greeting email to anyone having their birthday on current day.
		    	
		    	if (birthdayRecipients.get(i) instanceof PersonalRecipient) {      //checking the type of the recipient
		    		PersonalRecipient obj = (PersonalRecipient) birthdayRecipients.get(i);        //casting the greetable object to personal recipient object
		    		String emailAddress = obj.getEmail();
		    		obj.sendGreeting(emailAddress,formattedDate);                          //send birthday greeting 
		    	}else if (birthdayRecipients.get(i) instanceof CloseFriendOfficial) {
		    		CloseFriendOfficial obj = (CloseFriendOfficial) birthdayRecipients.get(i);
		    		String emailAddress = obj.getEmail();
		    		obj.sendGreeting(emailAddress,formattedDate);
		    	}
		    	
		    	
		    }
		}
		

		
		/*
		 * method to print the recipients who have birthday on current date	
		 */
		private static void printBirthdayDetails(ArrayList<Greetable> readBirthday, String dayMonth) {
			
			if (readBirthday.size() == 0) {              //checking the size of the list of the recipient who have birthday
				System.out.println("No one has birthday today");
				return;
			}
			for (int i = 0; i<readBirthday.size();i++) {
				Recipient obj = (Recipient) readBirthday.get(i);
				System.out.println("Name: "+obj.getName());          //printing the name of the recipients who have birthday on current day
			}
		}
		
}

