package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column (name = "first_name" , length = 45, nullable = false)
    private String fisrt_name;
    
    @Column (name = "last_name" , length = 45, nullable = false)
    private String last_name;
    
    @Column (name = "email" , length = 45, nullable = false)
    private String email;
    
    @Column (name = "password" , length = 15, nullable = false)
    private String password;  
    
    @Column (name = "verification" , length = 45, nullable = false)
    private String verification;
    
    @Column (name = "subscription" , length = 10, nullable = false)
    private int subscription;

    public  User(){}
   
    
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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

    /**
     * @return the verification
     */
    public String getVerification() {
        return verification;
    }

    /**
     * @param verification the verification to set
     */
    public void setVerification(String verification) {
        this.verification = verification;
    }
    
     public int getSubscription() {
        return subscription;
    }

    /**
     * @param verification the verification to set
     */
    public void setSubscription(int subscription) {
        this.subscription = subscription;
    }


}
