
import multiscene.ChatSceneApp;
import multiscene.SceneFlow;
import multiscene.Stageable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatController implements Stageable {
    private ObservableList<String> nickListItems;
    private String myNick;
    private Stage stage;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread readerThread;
    private String chatText = "<body>";

    @FXML
    WebView messageArea;

    @FXML
    TextField newMessage;

    @FXML
    Button sendButton;

    @FXML
    ListView nickList;

    public void initialize() throws IOException {
        readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        if (in.available()>0) {
                            String strFromServer = in.readUTF();
                            System.out.println("From server: " + strFromServer);
                            if (strFromServer.equalsIgnoreCase("/end")) {
                                terminateClient();
                                break;
                            }
                            if(!strFromServer.startsWith("/")) {
                                if (!strFromServer.startsWith(myNick+":")) {
                                    if(strFromServer.startsWith("(direct)")) {
                                        chatText += "<p style='background-color:powderblue;'>" +
                                                strFromServer.substring(strFromServer.indexOf(")") + 2) + "</p>";
                                    } else {
                                        chatText += "<p>" + strFromServer + "</p>";
                                    }
                                    Platform.runLater(()->{
                                        messageArea.getEngine().loadContent(chatText);
                                    });
                                }
                            } else if(strFromServer.startsWith("/clients ")) {
                                updateClientsList(strFromServer);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        readerThread.start();
        nickListItems = FXCollections.observableArrayList();
        nickListItems.add("All");
        socket = ChatSceneApp.getScenes().get(SceneFlow.CHAT).getSocket();
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        myNick = ChatSceneApp.getScenes().get(SceneFlow.CHAT).getNick();
        while (true) {
            if(in.available()>0) {
                String strFromServer = in.readUTF();
                if (strFromServer.startsWith("/clients ")) {
                    updateClientsList(strFromServer);
                    break;
                }
            }
        }
        nickList.setItems(nickListItems);
        nickList.getSelectionModel().select(0);
    }

    private void terminateClient() {
        readerThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.exit();
    }

    private void updateClientsList(String strFromServer) {
        String[] parts = strFromServer.split("\\s");
        Platform.runLater(()->{
            for(int i=1; i<parts.length; i++) {
                if (!parts[i].equals(myNick)) nickListItems.add(parts[i]);
            }
            nickList.setItems(nickListItems);
        });
    }

    public void sendMessageTypeAction(ActionEvent actionEvent) {
        int selectedIndex = (Integer) nickList.getSelectionModel().getSelectedIndices().get(0);
        String messageText = newMessage.getText().trim();
        if(!messageText.isEmpty()) {
            chatText += "<p align='right'>" + messageText + "</p>";
            messageArea.getEngine().loadContent(chatText);
            if(selectedIndex!=0) {
                messageText = "/w " + nickList.getSelectionModel().getSelectedItems().get(0) + " " +messageText;
                System.out.println("message sent: " + messageText);
            }
            try {
                out.writeUTF(messageText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            newMessage.clear();
        }
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event->{
            try {
                out.writeUTF("/end");
                readerThread.interrupt();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.exit();
        });
    }
}
