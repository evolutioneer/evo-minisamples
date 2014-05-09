package view;


import com.sun.mail.smtp.SMTPTransport;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.Date;
import java.util.Properties;

import javax.faces.event.ActionEvent;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.naming.InitialContext;

import oracle.adf.view.rich.component.rich.RichDocument;
import oracle.adf.view.rich.component.rich.RichForm;
import oracle.adf.view.rich.component.rich.fragment.RichPageTemplate;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;


public class BasePageBean {

    private RichPageTemplate pt1;
    private RichForm f1;
    private RichDocument d1;
    private RichPanelGroupLayout pgl1;
    
    private String _emailID = null;
    private String _emailSubject = null;
    private String _emailText = null;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private RichCommandButton cb1;

    public BasePageBean() {
    }


    public void sendMail(ActionEvent actionEvent) throws Exception {
        
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
        
        String authorization = props.getProperty("mail.smtp.auth");
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


    public void setPt1(RichPageTemplate pt1) {
        this.pt1 = pt1;
    }

    public RichPageTemplate getPt1() {
        return pt1;
    }

    public void setF1(RichForm f1) {
        this.f1 = f1;
    }

    public RichForm getF1() {
        return f1;
    }

    public void setD1(RichDocument d1) {
        this.d1 = d1;
    }

    public RichDocument getD1() {
        return d1;
    }

    public void setPgl1(RichPanelGroupLayout pgl1) {
        this.pgl1 = pgl1;
    }

    public RichPanelGroupLayout getPgl1() {
        return pgl1;
    }

    public void setEmailID(String _emailID) {
        String oldEmailID = this._emailID;
        this._emailID = _emailID;
        propertyChangeSupport.firePropertyChange("EmailID", oldEmailID, _emailID);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public String getEmailID() {
        return _emailID;
    }

    public void setEmailSubject(String _emailSubject) {
        String oldEmailSubject = this._emailSubject;
        this._emailSubject = _emailSubject;
        propertyChangeSupport.firePropertyChange("EmailSubject", oldEmailSubject, _emailSubject);
    }

    public String getEmailSubject() {
        return _emailSubject;
    }

    public void setEmailText(String _emailText) {
        String oldEmailText = this._emailText;
        this._emailText = _emailText;
        propertyChangeSupport.firePropertyChange("EmailText", oldEmailText, _emailText);
    }

    public String getEmailText() {
        return _emailText;
    }

    public void setCb1(RichCommandButton cb1) {
        this.cb1 = cb1;
    }

    public RichCommandButton getCb1() {
        return cb1;
    }
}
