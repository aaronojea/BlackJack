package di.blackjack;

import di.componentesblackjack.carta.Carta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorJuego implements Initializable {

    private Partida partida = new Partida();

    @FXML
    private AnchorPane cMaquina, cJugador;

    @FXML
    private Label cPuntos, puntosMaquina, puntosJugador;

    @FXML
    private Carta carta2;

    @FXML
    void plantarse() {
        desvelarCartas();

    }

    @FXML
    void pedirCarta() {

    }

    @FXML
    void retirarse() {

    }

    public void desvelarCartas() {
        this.partida.maquina.forEach(carta -> {
            this.mostrarCarta(cMaquina, carta, true);
        });
    }

    public void jugarIA() {
        if(this.partida.puntos(this.partida.jugador) < this.partida.puntos(this.partida.maquina)) {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.carta2.setImagen("Acarta");

        this.partida.iniciarPartida();

        this.mostrarCarta(cJugador, this.partida.cartaJugador(), true);
        this.mostrarCarta(cJugador, this.partida.cartaJugador(), true);
        ponerPuntos(cJugador);
        this.mostrarCarta(cMaquina, this.partida.cartaMaquina(), true);
        this.mostrarCarta(cMaquina, this.partida.cartaMaquina(), false);
    }

    public void mostrarCarta(AnchorPane zona, ModeloCarta carta, boolean volteada) {
        Carta c = new Carta();
        int x=0;

        if (zona==cMaquina){
            x=50*partida.maquina.size();
        } else {
            x=50*partida.jugador.size();
        }

        if (volteada) {
            c.setImagen(carta.getImagen());
        } else {
            c.setImagen("Acarta");
        }
        c.setLayoutX(x);
        c.setLayoutY(120);
        zona.getChildren().add(c);
    }

    public void ponerPuntos(AnchorPane zona) {

        if (zona==cMaquina){
            int puntos = this.partida.puntos(this.partida.maquina);
            this.puntosMaquina.setText(String.valueOf(puntos));
        } else {
            int punt = this.partida.puntos(this.partida.jugador);
            this.puntosJugador.setText(String.valueOf(punt));
        }
    }
}