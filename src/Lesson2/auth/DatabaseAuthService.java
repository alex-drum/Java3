package auth;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAuthService implements AuthenticationService {
    Connection conn = null;

    private class User {
        private String login;
        private String passwd;
        private String nick;

        public User(String login, String passwd, String nick) {
            this.login = login;
            this.passwd = passwd;
            this.nick = nick;
        }
    }

    private List<User> userList;

    public DatabaseAuthService() {
        userList = new ArrayList<>();

        try {
            conn = DriverManager.
                    getConnection("jdbc:h2:~/test", "sa", "");
            Statement statement = conn.createStatement();
            statement.execute("drop table if exists users;");
            statement.execute("create table if not exists users(" +
                    "id integer primary key auto_increment, " +
                    "login varchar(10), " +
                    "password varchar(10), " +
                    "nick varchar(10));");
            System.out.println("Database created!");

            statement.execute("delete from users");

            statement.execute("insert into users(login, password, nick) values('login1', 'pass1', 'nick1')");
            System.out.println("User1 added!");
            statement.execute("insert into users(login, password, nick) values('login2', 'pass2', 'nick2')");
            System.out.println("User2 added!");
            statement.execute("insert into users(login, password, nick) values('login3', 'pass3', 'nick3')");
            System.out.println("User3 added!");


            ResultSet rs = statement.executeQuery("SELECT * FROM USERS");
            while (rs.next()) {
                Integer id = rs.getInt("ID");
                String login = rs.getString("LOGIN");
                String passwd = rs.getString("PASSWORD");
                String nick = rs.getString("NICK");
                System.out.println("ID " + id + ": " + login + ", " + passwd + ", " + nick);
                userList.add(new User(login, passwd, nick));
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Authentication service started");
    }

    public void stop() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Authentication service stopped");
    }

    public synchronized String getNickByLoginAndPwd(String login, String passwd) {
        for(User user: userList) {
            if (user.login.equals(login) && user.passwd.equals(passwd)) {
                return user.nick;
            }
        }
        return null;
    }

    public synchronized String changeNick (String oldNick, String newNick) {
        String newNickUpdated = null;
        try {
            conn = DriverManager.
                    getConnection("jdbc:h2:~/test", "sa", "");
            Statement statement = conn.createStatement();
            statement.execute("update users set nick = 'newNick' where id = 1");

            ResultSet rs = statement.executeQuery("select * from users where login = 'login1'");
            System.out.println(rs);
            while (rs.next()) {
                newNickUpdated = rs.getString(4);
                System.out.println("Your new nick: " + newNickUpdated);
                System.out.println("Nick changed!");
            }
            return newNickUpdated;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
