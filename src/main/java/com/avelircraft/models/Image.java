package com.avelircraft.models;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "images")
public class Image {

    @Id
    private Long id;
    private String type;
    private byte[] img;

    @OneToOne
    @JoinColumn(name = "news_id")
    private News news;

    Image(){}

    public Image(Long id, String type, byte[] img) {
        this.id = id;
        this.type = type;
        this.img = img;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long name) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public News getNews() { return news; }

    // public void setNews(News news) { this.news = news; }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
