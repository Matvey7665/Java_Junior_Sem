package ru.gb.hw4;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "myUser")
public class MyUser {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;


//    Можно так, или как ниже
//    @OneToMany
//    @JoinTable(
//            name="post",
//            joinColumns = @JoinColumn(name="id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )

    @OneToMany(mappedBy = "myUser")
    private List<Post> posts;

    @OneToMany(mappedBy = "myUser")
    private List<PostComment> postComments;

    public MyUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<PostComment> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<PostComment> postComments) {
        this.postComments = postComments;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}