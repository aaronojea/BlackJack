package di.blackjack;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorPrincipal {

    public ControladorInicial controladorInicial;

    @FXML
    private AnchorPane contenido;

    @FXML
    public void cargarVistaJuego() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("vistaJuego.fxml"));

            contenido.getChildren().clear();

            contenido.getChildren().add(fxmlLoader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
