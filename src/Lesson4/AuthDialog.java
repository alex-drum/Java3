
import multiscene.ChatSceneApp;
import multiscene.SceneFlow;
import multiscene.Stageable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AuthDialog implements Stageable {
    private Stage stage;
    public Socket socket;

    @FXML
    AnchorPane rootPane;

    @FXML
    TextField userName;

    @FXML
    PasswordField userPassword;

    public void submitUserPassword(ActionEvent actionEvent) {
        try {
            socket = ChatSceneApp.getScenes().get(SceneFlow.CHAT).getSocket();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String authMessage = "/auth " + userName.getText() + " " + userPassword.getText();
            out.writeUTF(authMessage);
            while (true) {
                if(in.available()>0) {
                    String strFromServer = in.readUTF();
                    if (strFromServer.startsWith("/authOk")) {
                        System.out.println("Authorized on server");
                        ChatSceneApp.getScenes().get(SceneFlow.CHAT).setNick(strFromServer.split("\\s")[1]);
                        break;
                    }
                }
            }
            stage.setTitle("Chat as " + ChatSceneApp.getScenes().get(SceneFlow.CHAT).getNick());
            stage.setScene(ChatSceneApp.getScenes().get(SceneFlow.CHAT).getScene());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void exit(ActionEvent actionEvent) {
        ((Stage)rootPane.getScene().getWindow()).close();
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
