package auth;


public interface AuthenticationService {
     void start();
     void stop();
    String getNickByLoginAndPwd(String login, String password);
    String changeNick(String oldNick, String newNick);
}
