package multiscene;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatSceneApp extends Application {
    private static final String LOGIN_FXML = "../AuthDialog.fxml";
    private static final String CHAT_FXML = "../ChatClient.fxml";

    private static Map<SceneFlow, FxmlInfo> scenes = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    public static void updateScene(SceneFlow sceneFlow, FxmlInfo info) {
        scenes.put(sceneFlow, info);
    }

    public static Map<SceneFlow, FxmlInfo> getScenes() {
        return scenes;
    }

    @Override
    public void start(Stage primaryStage) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8189);
        } catch (IOException e) {
            e.printStackTrace();
        }
        scenes.put(SceneFlow.LOGIN, new FxmlInfo(LOGIN_FXML, SceneFlow.LOGIN, primaryStage, socket));
        scenes.put(SceneFlow.CHAT, new FxmlInfo(CHAT_FXML, SceneFlow.CHAT, primaryStage, socket));
        primaryStage.setScene(scenes.get(SceneFlow.LOGIN).getScene());
        primaryStage.setTitle("Login");
        primaryStage.show();

        //        String logSrc = "C:\\Users\\Public\\nick1 logs.txt";
//        File logFile = new File(logSrc);
//        System.out.println(logFile.length());;
//        FileReader fr = new FileReader(logFile);
//        BufferedReader br = new BufferedReader(fr);
//        String msg = br.readLine();
//        System.out.println(msg);
//        messageArea.getEngine().loadContent(msg);


//        while (msg != null) {
//            msg = br.readLine();
//            messageArea.getEngine().loadContent(msg);
//            System.out.println(msg);
//        }

    }
}
