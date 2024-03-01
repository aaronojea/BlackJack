package di.blackjack;

import di.componentesblackjack.carta.Carta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private Label puntosMaquina, puntosJugador, cPuntuacion;

    @FXML
    private Carta carta2;

    @FXML
    void plantarse() {
        System.out.println("plantarse");
        desvelarCartas();
        ponerPuntos(cMaquina);
        jugarIA();
        ponerPuntos(cMaquina);
    }

    @FXML
    void pedirCarta() {
        this.mostrarCarta(cJugador, this.partida.cartaJugador(), true);
        this.ponerPuntos(cJugador);
        if(this.partida.puntos(this.partida.jugador) > 21) {
            System.out.println("HAS PERDIDO");
        }
    }

    @FXML
    void retirarse() {
        //vistaInformacion.fxml
    }

    public void desvelarCartas() {
        this.partida.maquina.forEach(carta -> {
            this.mostrarCarta(cMaquina, carta, true);
        });
    }

    public void jugarIA() {

        while (this.partida.puntos(this.partida.maquina) < this.partida.puntos(this.partida.jugador)) {
            this.mostrarCarta(cMaquina, this.partida.cartaMaquina(), false);
        }
        comprobarResultado();
        desvelarCartas();
    }

    public void comprobarResultado() {

        if(this.partida.puntos(this.partida.maquina) > 21) {
            if(this.partida.puntos(this.partida.jugador) == 21) {
                System.out.println("NORABOA BLACK JACK, MAIS 2 PUNTOS");
                puntuacionJugador(3, "sumar");
            }else {
                System.out.println("Noraboa, mais 1 punto");
                puntuacionJugador(2, "sumar");
            }
        }else if (this.partida.puntos(this.partida.maquina) == 21) {
            if(this.partida.puntos(this.partida.jugador) == 21) {
                System.out.println("EMPATE");
                puntuacionJugador(1, "sumar");
            }else {
                System.out.println("HAS PERDIDO");
            }
        }else {
            if(this.partida.puntos(this.partida.maquina) >= this.partida.puntos(this.partida.jugador)) {
                System.out.println("HAS PERDIDO");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.puntuacionJugador(1, "restar");

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

    public void puntuacionJugador(int puntos, String operacion) {

        int puntosJugador = Integer.parseInt(cPuntuacion.getText());

        if(operacion == "sumar") {
            puntosJugador += puntos;
        }else {
            puntosJugador -= puntos;
        }

        this.cPuntuacion.setText(String.valueOf(puntosJugador));
    }
}