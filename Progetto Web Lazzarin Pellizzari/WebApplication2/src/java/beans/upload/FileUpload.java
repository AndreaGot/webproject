package beans.upload;

import beans.Posts;
import beans.User;
import database.DBPost;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class FileUpload {

    public static final String filePathSave = "/home/informatico/NetBeansProjects/WebApplication2/web/forum/files/";
    public static final String filePathFind = "/WebApplication2/faces/forum/files/";
    private LinkedList<MyFile> files = new LinkedList();
    private String realpath;
    private String uri;
    private String messageError;
    private String outputMessageError;

    public void listener(UploadEvent event) throws Exception {
        FacesContext fc = FacesContext.getCurrentInstance();
        User utente = (User) (fc.getApplication().createValueBinding("#{user}").getValue(fc));
        Posts post = (Posts) (fc.getApplication().createValueBinding("#{posts}").getValue(fc));
        //prelevo le informazioni del file appena caricato
        UploadItem item = event.getUploadItem();

        DBPost db = new DBPost();

        MyFile file = new MyFile();

        file.setName(item.getFileName());

        file.setUri("/WebApplication2/files" + file.getName());
        file.setIduser(utente.getId());

        File dir = new File(filePathSave + post.getCurrentIdGroup() + "/");
        //creo la cartella di destinazione per il gruppo, nel caso in cui si tratti del primo caricamento
        dir.mkdirs();

        File f;
        if (file.getName().length() > 45) {
            int index_extension = file.getName().lastIndexOf(".");
            if (index_extension != -1) {
                String newFileName = file.getName().substring(0, 45 - file.getName().substring(index_extension).length());
                newFileName += file.getName().substring(index_extension);
                f = new File(filePathSave + post.getCurrentIdGroup() + "/" + newFileName);
            } else {
                f = new File(filePathSave + post.getCurrentIdGroup() + "/" + file.getName().substring(0, 45));
            }
        } else {
            f = new File(filePathSave + post.getCurrentIdGroup() + "/" + file.getName());
        }
        if (db.addAllegato(f.getName(), utente.getId(), post.getCurrentIdGroup(), file.getUri())) {
            transfer(item.getFile(), f);
        } else {
            //errore file presente
            //setOutputMessageError("<h3><font color=\"red\">"+file.getName()+":"+getMessageError()+"</font></h3>");
        }
        post.viewPosts();
        db.close();
    }

    private void transfer(File source, File dest) {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            long pos = 0;
            long max = in.size();
            while (pos < max) {
                pos += in.transferTo(pos, max - pos, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
