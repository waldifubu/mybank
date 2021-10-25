package entity;

import javax.persistence.*;
import java.sql.Date;

@Table(name = "users")
@Entity
public class User {
    @Id
    @Column(name = "u_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "u_nick")
    private String username;
    @Column(name = "u_pass")
    private String password;

    @Column(name = "u_fullname")
    private String fullname;

    @Column(name = "u_regged")
    private String regged;

    @Column(name = "u_last")
    private String lastLogin;

    @Column(name = "u_arbeit")
    private Date workingDay;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRegged() {
        return regged;
    }

    public void setRegged(String regged) {
        this.regged = regged;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(Date workingDay) {
        this.workingDay = workingDay;
    }
}