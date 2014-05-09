package view;

import com.sun.mail.smtp.SMTPTransport;

import java.util.Date;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import javax.mail.Message;
import javax.mail.Session;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import javax.naming.InitialContext;

public class BasePageBean {
    private RichInputText emailIdInputText;
    private RichInputText emailSubjectInputText;
    private RichInputText mailText;
    
    String emailID = null;
    String emailSubject = null;
    String emailText = null;

    public BasePageBean() {
    }

    public void setEmailIdInputText(RichInputText emailIdInputText) {
        this.emailIdInputText = emailIdInputText;
    }

    public RichInputText getEmailIdInputText() {
        return emailIdInputText;
    }

    public void setEmailSubjectInputText(RichInputText emailSubjectInputText) {
        this.emailSubjectInputText = emailSubjectInputText;
    }

    public RichInputText getEmailSubjectInputText() {
        return emailSubjectInputText;
    }

    public void sendMail(ActionEvent actionEvent) throws Exception{
        // Add event code here...
        String emailID = getEmailID();
        String subject = getEmailSubject();
        String text = getEmailText();
        
        InitialContext ic = new InitialContext();
        Session session = (Session) ic.lookup("mail/NewMailSession");

        Properties props = session.getProperties();
        System.out.println("LKAPOOR: PROPERTIES LIST: ");
        props.list(System.out);

        String  to = emailID;
        if(to==null || to.equals(""))
            to = "stahljer@gmail.com";
        
        if(subject==null || subject.equals(""))
            subject = "Test Mail";

        String mailhost = props.getProperty("mail.smtp.host");
        System.out.println("LKAPOOR:: mailhost = " + mailhost);
        String user = props.getProperty("mail.smtp.user");
        System.out.println("LKAPOOR:: user = " + user);
        String password = props.getProperty("mail.smtp.password");
        System.out.println("LKAPOOR:: password = " + password);
        String protocol = props.getProperty("mail.transport.protocol");
        System.out.println("LKAPOOR:: protocol = " + protocol);
        
        String authorization = props.getProperty("mail.smtps.auth");
        String mailDisabled = props.getProperty("mail.disable");
        String verboseProp = props.getProperty("mail.verbose");
        String debugProp = props.getProperty("mail.debug");
        
        boolean sentDisabled = false;
        if(mailDisabled.equals("true"))
            sentDisabled = true;
        
        if(!sentDisabled){
            
            boolean auth = false;
            if(authorization.equals("true"))
                auth = true;
            
            boolean verbose = false;
            if(verboseProp.equals("true"))
                verbose = true;
    
            String mailer = "smtpsend";
            
            if(debugProp.equals("true"))
                session.setDebug(true);
            else
                session.setDebug(false);
    
            System.out.println("LKAPOOR: session initialized.");
                  
            Message msg = new MimeMessage(session);
            
            msg.setFrom();       
            
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            
            System.out.println("LKAPOOR: recipient set.");
            
            msg.setSubject(subject);
            msg.setText(text);
    
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());
    
            System.out.println("LKAPOOR: Metadata set.");
    
            SMTPTransport t = (SMTPTransport)session.getTransport(protocol);
    
            System.out.println("LKAPOOR: Gettting Transport.");
    
            try {
                System.out.println("LKAPOOR: Before connect via authorization.");
                t.connect(mailhost, user, password);
                t.sendMessage(msg, msg.getAllRecipients());
                System.out.println("LKAPOOR: message sent.");
            } finally {
                    if (verbose)
                        System.out.println("Response: " + t.getLastServerResponse());
                    t.close();
            }
            
            System.out.println("\nMail was sent successfully.");
        }else{
            System.out.println("Mail Sending is disabled.");
        }
        
    }

    public void setMailText(RichInputText mailText) {
        this.mailText = mailText;
    }

    public RichInputText getMailText() {
        return mailText;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

    public String getEmailText() {
        return emailText;
    }
}