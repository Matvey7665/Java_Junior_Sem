package ru.gb;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.gb.hw4.MyUser;
import ru.gb.hw4.Post;
import ru.gb.hw4.PostComment;

import java.lang.module.Configuration;
import java.util.List;

public class Main {

  // Jetbrains Intellij IDEA

  // .zip

  public static void main(String[] args) {
      Configuration configuration = new Configuration();
      configuration.configure();
    try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
      // Инициализация таблиц (не удалять!)
      // initTables(sessionFactory);

      // Использования методов CRUD
      long user_id = 3L;
      withSessionCreateUser(sessionFactory, user_id);
      System.out.println("\nCreated user#" + user_id + ": " + withSessionReadUser(sessionFactory, user_id) + "\n");
      withSessionUpdateUser(sessionFactory, user_id);
      System.out.println("\nUpdated user#" + user_id + ": " + withSessionReadUser(sessionFactory, user_id) + "\n");
      withSessionDeleteUser(sessionFactory, user_id);
      System.out.println("\nDeleted user#" + user_id + ": " + withSessionReadUser(sessionFactory, user_id) + "\n");

      // Посты пользователя
      withSessionGetPosts(sessionFactory, 1L);

      // Комментарии к посту
      withSessionGetPostsComments(sessionFactory, 1L);

      // Все комментарии пользователя
      withSessionGetALLPostsCommentsUser(sessionFactory, 2L);

      // Пользователи, которых комментировал пользователь
      withSessionGetUsersThatUserCommentedOn(sessionFactory, 2L);

    }

  }

  private static void withSessionCreateUser(SessionFactory sessionFactory, Long user_id) {
    // create
    try (Session session = sessionFactory.openSession()) {
      Transaction tx = session.beginTransaction();
      MyUser user = new MyUser();
      user.setId(user_id);
      user.setName("User #" + user_id);

      session.persist(user); // insert
      tx.commit();
    }
  }

  private static void withSessionUpdateUser(SessionFactory sessionFactory, Long user_id) {
    // update
    try (Session session = sessionFactory.openSession()) {
      MyUser toUpdate = session.find(MyUser.class, user_id);
      session.detach(toUpdate);
      toUpdate.setName("UPDATED");

      Transaction tx = session.beginTransaction();
      session.merge(toUpdate); // update
      tx.commit();
    }
  }

  private static MyUser withSessionReadUser(SessionFactory sessionFactory, Long user_id) {
    // read
    try (Session session = sessionFactory.openSession()) {
      MyUser user = session.find(MyUser.class, user_id);
      return user;
    }
  }

  private static void withSessionDeleteUser(SessionFactory sessionFactory, Long user_id) {
    // delete
    try (Session session = sessionFactory.openSession()) {
      MyUser toDelete = session.find(MyUser.class, user_id);

      Transaction tx = session.beginTransaction();
      session.remove(toDelete); // delete
      tx.commit();
    }
  }

  private static void withSessionGetPosts(SessionFactory sessionFactory, long user_id) {
    try (Session session = sessionFactory.openSession()) {
      MyUser user = session.find(MyUser.class, user_id);
      System.out.println("\nПосты пользователя " + user.getName() + ":");
      user.getPosts().forEach(System.out::println);
    }
  }

  private static void withSessionGetPostsComments(SessionFactory sessionFactory, long post_id) {
    try (Session session = sessionFactory.openSession()) {
      Post post = session.find(Post.class, post_id);
      System.out.println("\nКомментарии к посту " + post.getMyUser().getName() + "/" + post + ":");
      ;
      post.getPostComments().forEach(System.out::println);
    }
  }

  private static void withSessionGetALLPostsCommentsUser(SessionFactory sessionFactory, long user_id) {
    try (Session session = sessionFactory.openSession()) {
      MyUser user = session.find(MyUser.class, user_id);
      System.out.println("\nВсе комментариипользователя " + user.getName() + ":");
      ;
      user.getPostComments().forEach(System.out::println);
    }
  }

  private static List<MyUser> withSessionGetUsersThatUserCommentedOn(SessionFactory sessionFactory, long user_id) {

    try (Session session = sessionFactory.openSession()) {
      MyUser user = session.find(MyUser.class, user_id);
      Query<MyUser> query = session.createQuery("""
              select post.myUser
              from PostComment as post_comment
                  join post_comment.post as post
                  join post.myUser as myUser
              where post_comment.myUser = :user""");

      query.setParameter("user", user);

      List<MyUser> users = query.list();

      System.out.println("\nСписок пользователей, кого комментировал " + user);
      ;
      System.out.println(users);
      return users;
    }
  }


  private static void initTables(SessionFactory sessionFactory) {
    try (Session session = sessionFactory.openSession()) {
      MyUser myUser1 = new MyUser();
      myUser1.setId(1L);
      myUser1.setName("Bob");

      MyUser myUser2 = new MyUser();
      myUser2.setId(2L);
      myUser2.setName("Peter");

      Post post1 = new Post();
      post1.setId(1L);
      post1.setTitle("Title #1");
      post1.setMyUser(myUser1);

      Post post2 = new Post();
      post2.setId(2L);
      post2.setTitle("Title #2");
      post2.setMyUser(myUser2);

      PostComment postComment1 = new PostComment();
      postComment1.setId(1L);
      postComment1.setComment("Comment #1");
      postComment1.setPost(post1);
      postComment1.setMyUser(myUser2);

      PostComment postComment2 = new PostComment();
      postComment2.setId(2L);
      postComment2.setComment("Comment #2");
      postComment2.setPost(post1);
      postComment2.setMyUser(myUser2);

      Transaction tx = session.beginTransaction();
      session.persist(myUser1);
      session.persist(myUser2);
      session.persist(post1);
      session.persist(post2);
      session.persist(postComment1);
      session.persist(postComment2);
      tx.commit();
    }
  }
}