package com.avelircraft.models;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "authme")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String realname;
    private String password;
    private String ip;
    private Long lastlogin;
    private Double x;
    private Double y;
    private Double z;
    private String world;
    private Long regdate;
    private String regip;
    private Float yaw;
    private Float pitch;
    private String email;
    private Short isLogged;
    private Short hasSession;
    private String totp;
    private boolean profileicon;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) //LAZY
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;

    public User(){};

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRealname() {
        return realname;
    }

    public String getPassword() {
        return password;
    }

    public String getIp() {
        return ip;
    }

    public Long getLastlogin() {
        return lastlogin;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public Long getRegdate() {
        return regdate;
    }

    public String getRegip() {
        return regip;
    }

    public Float getYaw() {
        return yaw;
    }

    public Float getPitch() {
        return pitch;
    }

    public String getEmail() {
        return email;
    }

    public Short getIsLogged() {
        return isLogged;
    }

    public Short getHasSession() {
        return hasSession;
    }

    public String getTotp() {
        return totp;
    }

    public boolean isProfileicon() { return profileicon; }

    public void setProfileicon(boolean profileicon) { this.profileicon = profileicon; }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", password='" + password + '\'' +
                ", ip='" + ip + '\'' +
                ", lastlogin=" + lastlogin +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", world='" + world + '\'' +
                ", regdate=" + regdate +
                ", regip='" + regip + '\'' +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", email='" + email + '\'' +
                ", isLogged=" + isLogged +
                ", hasSession=" + hasSession +
                ", totp='" + totp + '\'' +
                '}';
    }

//    @Transient
//    public List<Role> getRoles() {
//        if (roles != null) return roles;
//        else return getFreshDbRoles();
//    }

//    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setRole(Role role) {
        roles.add(role);
    }

    @Transactional
    public List<Comment> getComments() { return comments; }

    public void setComments(List<Comment> comments) { this.comments = comments; }

}
