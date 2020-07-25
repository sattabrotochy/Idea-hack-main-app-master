package teamx.ideahacks.Models;

public class UserModel {

    String username,university_id,email,userid,status;
    long create_at;
    int giveVote,submit;

    public UserModel() {
    }


    public UserModel(String username, String university_id, String email, String userid, String status, long create_at, int giveVote, int submit) {
        this.username = username;
        this.university_id = university_id;
        this.email = email;
        this.userid = userid;
        this.status = status;
        this.create_at = create_at;
        this.giveVote = giveVote;
        this.submit = submit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUniversity_id() {
        return university_id;
    }

    public void setUniversity_id(String university_id) {
        this.university_id = university_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public int getGiveVote() {
        return giveVote;
    }

    public void setGiveVote(int giveVote) {
        this.giveVote = giveVote;
    }

    public int getSubmit() {
        return submit;
    }

    public void setSubmit(int submit) {
        this.submit = submit;
    }
}
