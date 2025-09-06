package com.choongang.todolist;

import com.choongang.todolist.dao.UserDao;
import com.choongang.todolist.domain.User;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class TodolistApplication {

    private final UserDao userDao;

    public TodolistApplication(UserDao userDao) {
        this.userDao = userDao;
    }

    public static void main(String[] args) {
        SpringApplication.run(TodolistApplication.class, args);
    }

    /***
     * 애플리케이션이 시작될 때, 초기 데이터를 설정할거에요.
     * 1. User 객체를 생성할거에요.
     * 2. UserDao를 사용해서, User 객체를 저장할거에요.
     3. User 객체가 저장되었다는 메시지를 출력할거에요.
     4. @PostConstruct 어노테이션을 사용해서, 애플리케이션이 시작될 때, init() 메서드가 실행되도록 할거에요.
     */
    @PostConstruct
    public void init() {
        User user = new User(1L, "username", "password",
                "test@example.com", LocalDateTime.now(), LocalDateTime.now());
        System.out.println("User created: " + user.getUsername());
        userDao.saveUser(user);
    }
}
