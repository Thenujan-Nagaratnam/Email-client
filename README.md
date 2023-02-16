# Email-client
An email client for sending mails using commandline: Commandline based java email client to send emails, to send birthday wishes automatically and storing the email details.

The email client has two types of recipients, official and personal. Some official recipients are close friends.

Details of the recipient list is stored in a text file.  An official recipient’s record in the text file has the following format: official: <name>, <email>,<designation>. A sample record for official recipients in the text file looks as follows:

Official: nimal,nimal@gmail.com,ceo

A sample record for official friends in the text file looks as follows (last value is the recipient's birthday):

Office_friend: kamal,kamal@gmail.com,clerk,2000/12/12

A sample record for personal recipients in the text file looks as follows (last value is the recipient's birthday):

Personal: sunil,<nick-name>,sunil@gmail.com,2000/10/10

The user is given the option to update this text file, i.e. the user should be able to add a new recipient through command-line, and these details are added to the text file.

When the email client is running, an object for each email recipient is maintained in the application. For this, the client will load the recipient details from the text file into the application. For each recipient having a birthday, a birthday greeting is sent on the correct day. Official friends and personal recipients are sent different messages (e.g. Wish you a Happy Birthday. <your name> for an office friend, and hugs and love on your birthday. <your name> for personal recipients). But all personal recipients receive the same message, and all office friends receive the same message.  A list of recipients to whom a birthday greeting should be sent is maintained in the application, when it is running. When the email client is started, it should traverse this list, and send a greeting email to anyone having their birthday on that day.

The system is able to keep a count of the recipient objects. Used static members to keep this count.

All the emails sent out by the email client is saved into the hard disk, in the form of objects – object serialization is used for this. The user will be able to retrieve information of all the mails sent on a particular day by using a command-line option.

Command-line options are available for:

1. Adding a new recipient
2. Sending an email
3. Printing out all the names of recipients who have their birthday set to current date
4. Printing out details (subject and recipient) of all the emails sent on a date specified by user input
5. Printing out the number of recipient objects in the application

In the given code, note that it imports the javax.mail package. This package is included in the javax.mail.jar.

Save the recipient data into clientList.txt.
