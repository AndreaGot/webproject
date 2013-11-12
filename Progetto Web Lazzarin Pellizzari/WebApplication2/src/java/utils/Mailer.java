package utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mailer {

    public static String sendMail(String destinatario, String ip) {
        String hash = null;
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "out.alice.it");

            //hash da usare per l'invito
            hash = Functions.SHA1(Calendar.getInstance().getTimeInMillis() + "");

            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);

            Message msg = new MimeMessage(session);

            Multipart mp = new MimeMultipart("alternative");

            BodyPart textPart = new MimeBodyPart();
            textPart.setText("Sei stato invitato ad un gruppo. \nIl programma che stai utilizzando non supporta i messagg HTML,\nquindi, se desideri essere parte del gruppo, ti chiediamo di copiare ed incollare il seguente indirizzo nel tuo browser: \n\nhttp://" + ip + "/WebApplication2/faces/accept_invite?invite=" + hash);

            BodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent("Sei stato invitato al gruppo ...<br>\nSe desideri partecipare, clicca <a href=\"http://" + ip + "/WebApplication2/faces/accept_invite?invite=" + hash + "\">qui</a>, altrimenti ignora questo messaggio.", "text/html");

            mp.addBodyPart(textPart);
            mp.addBodyPart(htmlPart);

            msg.setSubject("Conferma registrazione Forum Cooperativo");

            msg.setContent(mp);

            InternetAddress from = new InternetAddress("noreply@forum.it");
            msg.setFrom(from);

            InternetAddress to = new InternetAddress(destinatario);
            msg.setRecipient(Message.RecipientType.TO, to);

            Transport.send(msg);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
            hash = null;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
            hash = null;
        } catch (AddressException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
            hash = null;
        } catch (MessagingException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
            hash = null;
        } finally {
            return hash;
        }



    }
}
