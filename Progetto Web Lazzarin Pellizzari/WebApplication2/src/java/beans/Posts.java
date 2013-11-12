package beans;

import beans.upload.MyFile;
import database.DBPost;
import java.util.ArrayList;
import javax.faces.event.ActionEvent;

public class Posts {

    private int currentIdGroup = -1;
    private int currentIdUser;
    private String groupName;
    private ArrayList<Post> posts;
    private ArrayList<MyFile> files;
    private String textnewpost;
    private int num_of_files = 0;

    public void setAttributeGroupId(ActionEvent event) {
        this.currentIdGroup = ((Integer) event.getComponent().getAttributes().get("id_gruppo")).intValue();
    }

    public void setAttributeForAdd(ActionEvent event) {
        this.currentIdUser = ((Integer) event.getComponent().getAttributes().get("identuser")).intValue();
    }

    public int getNum_of_files() {
        return num_of_files;
    }

    public ArrayList<MyFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<MyFile> files) {
        num_of_files = files.size();
        this.files = files;
    }

    public int getCurrentIdGroup() {
        return currentIdGroup;
    }

    public void setCurrentIdGroup(int currentIdGroup) {
        this.currentIdGroup = currentIdGroup;
    }

    public int getCurrentIdUser() {
        return currentIdUser;
    }

    public void setCurrentIdUser(int currentIdUser) {
        this.currentIdUser = currentIdUser;
    }

    public int getPostId() {
        return currentIdGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTextnewpost() {
        return textnewpost;
    }

    public void setTextnewpost(String textnewpost) {
        this.textnewpost = textnewpost;
    }

    public ArrayList<Post> getPosts() {
        DBPost db = new DBPost();
        db.setPosts(this);
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public String viewPosts() {
        DBPost db = new DBPost();
        if (db.setPosts(this)) {
            return "view_post";
        } else {
            return "failure";
        }

    }

    public String addPost() {
        DBPost db = new DBPost();
        if (db.addPost(textnewpost, currentIdUser, currentIdGroup)) {
            db.setPosts(this);
            textnewpost = "";
            return "post_added";
        } else {
            return "failure";
        }
    }
}
