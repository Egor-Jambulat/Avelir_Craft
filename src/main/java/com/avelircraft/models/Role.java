package com.avelircraft.models;

import javax.persistence.*;

@Entity
@Table(name = "luckperms_players")
public class Role {

    @Id
    @Column(name = "uuid")
    private String uuid;

//    @Column(name = "username")
//    private String username;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName="username")
    private User user;

    @Column(name = "primary_group")
    private String role;

    public Role() {
    }

    public Role(User user) {
        this.user = user;
        uuid = System.currentTimeMillis() + "default";
        role = "default";
    }

    public Role(String uuid, User user, String role) {
        this.uuid = uuid;
        this.user = user;
        this.role = role;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
