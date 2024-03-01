package di.blackjack;

import di.componentesblackjack.carta.Carta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorInicial implements Initializable {

    @FXML
    private AnchorPane panel;

    @FXML
    private Carta carta1;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //this.carta1.setImagen("Acarta");
        this.carta1.setImagen("Acarta");


    }
}
