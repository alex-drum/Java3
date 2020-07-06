
import auth.DatabaseAuthService;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;
    private String password;
    private String logFile = "";

    public String getName() {
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        this.myServer = myServer;
        this.socket = socket;
        this.name = "";
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(myHandlerThread);
        } catch (IOException ex) {
            throw new RuntimeException("Client creation error");
        }
    }

    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        myServer.unsubscribe(this);
        myServer.broadcast("User " + name + " left", true);
    }

    private void readMessages() throws IOException {
        while (true) {
            if (in.available()>0) {
                String message = in.readUTF();
                System.out.println("From " + name + ":" + message);
                if (message.equals("/end")) {
                    closeConnection();
                    return;
                }
                if (message.startsWith("/ch ")) {
                    String[] parts = message.split("\\s");
                    String newNick = parts[1];
                   String updatedNick = myServer.getAuthService().changeNick(name, newNick);

                  // TODO допилить код, чтоб при смене ника в GUI производились соответствующие замены.
                }
                if (message.startsWith("/w ")) {
                    String[] parts = message.split("\\s");
                    String realMessage = message.substring(message.indexOf(" ", message.indexOf(" ") + 1));
                    myServer.sendDirect(parts[1],name + ": "+ realMessage);
                    String sToLog = message + "\n";
                    Files.write(Paths.get(logFile), sToLog.getBytes(), StandardOpenOption.APPEND);
                } else {
                    myServer.broadcast(name + ": " + message, true);
                    String sToLog = message + "\n";
                    Files.write(Paths.get(logFile), sToLog.getBytes(), StandardOpenOption.APPEND);
                }
            }
        }
    }

    private void authenticate() throws IOException {
        while(true) {
            if (in.available()>0){
                String str = in.readUTF();
                if (str.startsWith("/auth")) {
                    String[] parts = str.split("\\s");
                    password = parts[2];
                    String nick = myServer.getAuthService().getNickByLoginAndPwd(parts[1], parts[2]);
                    if (nick != null) {
                        if (!myServer.isNickLogged(nick)) {
                            System.out.println(nick + " logged into chat");
                            name = nick;
                            sendMsg("/authOk " + nick);
                            myServer.broadcast(nick + " is in chat", true);
                            myServer.subscribe(this);
                            logFile = "C:\\Users\\Public\\" + name + "_logs.txt";
                            if (!Files.exists(Paths.get(logFile))) {
                                FileOutputStream fos = new FileOutputStream(logFile);
                                fos.close();
                            }
                            String msg = LocalDateTime.now() + ": " + nick + " is in chat\n";
                            Files.write(Paths.get(logFile), msg.getBytes(), StandardOpenOption.APPEND);
                            return;
                        } else {
                            System.out.println("User " + nick + " tried to re-enter");
                            sendMsg("User already logged in");
                        }
                    } else {
                        System.out.println("Wrong login/password");
                        sendMsg("Incorrect login attempted");
                    }
                }
            }

        }
    }

    public void sendMsg(String s) {
        try {
            out.writeUTF(s);
            String sToLog = s + "\n";
            Files.write(Paths.get(logFile), sToLog.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Thread myHandlerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                authenticate();
                readMessages();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    });
}
