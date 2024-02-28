package di.blackjack;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ControladorInicial {

    @FXML
    private AnchorPane panel;

    @FXML
    void cargarVistaJuego() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("vistaJuego.fxml"));

            panel.getChildren().clear();

            panel.getChildren().add(fxmlLoader.load());

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
