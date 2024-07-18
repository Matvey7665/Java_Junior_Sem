package ru.gb.hw4;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name="timestamp")
    private LocalDate timestamp;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private MyUser myUser;

    @OneToMany(mappedBy = "post")
    private List<PostComment> postComments;


    public Post() {
        timestamp = now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public List<PostComment> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<PostComment> postComments) {
        this.postComments = postComments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", title='" + title + '\'' +
//                ", myUser=" + myUser +
                '}';
    }
}
