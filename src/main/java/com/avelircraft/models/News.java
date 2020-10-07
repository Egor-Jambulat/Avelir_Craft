package com.avelircraft.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "header")
    //@NotNull
    private String header;

    @Column(name = "description")
    private String description;

    @Column(name = "message")
    private String message;

    @Column(name = "date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "img_id")
    private Long img_id;

    @OneToOne(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Image image;

    @Column(name = "views")
    private Long views;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments;

    public News incrementViews() {
        views++;
        return this;
    }

    public News(){
        header = "Пустой заголовок";
        message = "Пустая новость";
        description = "Пустое описание";
        views = 0l;
        id = 0;
        //date =  new Date();
    };

    public News(String header, String description, String message){
        this.header = header;
        this.description = description;
        this.message = message;
    }

    public News(String header, String description, String message, Long img_id, Image image) {
        this.header = header;
        this.description = description;
        this.message = message;
        this.img_id = img_id;
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getImg_id() {
        return img_id;
    }

    public void setImg_id(Long img_id) {
        this.img_id = img_id;
    }

    public Image getImage() { return image; }

    public void setImage(Image image) { this.image = image; }

    public Long getViews() {
        return views;
    }

    public Integer getId() {
        return id;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", header='" + header + '\'' +
                ", description='" + description + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }

}