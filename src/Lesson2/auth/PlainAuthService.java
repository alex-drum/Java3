//package com.geekbrains.java2.lesson8.auth;
// package auth;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PlainAuthService implements AuthenticationService {
//    Connection conn = null;
//
//    private class User {
//        private String login;
//        private String passwd;
//        private String nick;
//
//        public User(String login, String passwd, String nick) {
//            this.login = login;
//            this.passwd = passwd;
//            this.nick = nick;
//        }
//    }
//
//    private List<User> userList;
//
//    public PlainAuthService() {
//        userList = new ArrayList<>();
//        userList.add(new User("login1", "pass1", "nick1"));
//        userList.add(new User("login2", "pass2", "nick2"));
//        userList.add(new User("login3", "pass3", "nick3"));
//    }
//
//    public void start() {
//        System.out.println("Authentication service started");
//    }
//
//    public void stop() {
//        System.out.println("Authentication service stopped");
//    }
//
//    public String getNickByLoginAndPwd(String login, String passwd) {
//        for(User user: userList) {
//            if (user.login.equals(login) && user.passwd.equals(passwd)) {
//                return user.nick;
//            }
//        }
//        return null;
//    }
//}
