package dto;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class User_DTO implements Serializable {

    @Expose
    private String fisrt_name;
    @Expose
    private String last_name;
    @Expose
    private String email;

    @Expose (serialize = false,deserialize = true )
    private String password;
    public User_DTO() {
    }

    /**
     * @return the fisrt_name
     */
    public String getFisrt_name() {
        return fisrt_name;
    }

    /**
     * @param fisrt_name the fisrt_name to set
     */
    public void setFisrt_name(String fisrt_name) {
        this.fisrt_name = fisrt_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
