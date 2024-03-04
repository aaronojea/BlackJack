package di.blackjack;

import di.componentesblackjack.carta.Carta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorJuego implements Initializable {

    private Button bEnter;
    private Button bExitGame;
    private Button bCoins;
    private Button bExit;
    private Button bHit;
    private Button bStand;

    private Partida partida = new Partida();

    private AnchorPane cMaquina, cJugador;

    private Label puntosMaquina, puntosJugador, coins;

    @FXML
    AnchorPane contenido;

    private Carta carta2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.mostrarMenu();
    }

    private void mostrarMenu() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("vistaMenu.fxml"));

            contenido.getChildren().clear();
            contenido.getChildren().add(loader.load());

            bEnter = (Button) contenido.lookup("#bEnter");
            bEnter.setOnAction(e -> nuevaPartida());

            bExitGame = (Button) contenido.lookup("#bExitGame");
            bExitGame.setOnAction(e -> Platform.exit());

            bCoins = (Button) contenido.lookup("#bCoins");
            bCoins.setOnAction(e -> mostrarPuntuaciones());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void nuevaPartida() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("vistaJuego.fxml"));

            contenido.getChildren().clear();
            contenido.getChildren().add(loader.load());

            //paneles
            cMaquina = (AnchorPane) contenido.lookup("#cMaquina");
            cJugador = (AnchorPane) contenido.lookup("#cJugador");
            puntosMaquina = (Label) contenido.lookup("#puntosMaquina");
            puntosJugador = (Label) contenido.lookup("#puntosJugador");
            coins = (Label) contenido.lookup("#coins");
            carta2 = (Carta) contenido.lookup("#carta2");

            bHit = (Button) contenido.lookup("#bHit");
            bHit.setOnAction(e -> pedirCarta());

            bExit = (Button) contenido.lookup("#bExit");
            bExit.setOnAction(e -> retirarse());

            bStand = (Button) contenido.lookup("#bStand");
            bStand.setOnAction(e -> plantarse());
            this.partida.iniciarPartida();
            this.puntuacionJugador(1, "restar");
            this.carta2.setImagen("Acarta");


            this.mostrarCarta(cJugador, this.partida.cartaJugador(), true);
            this.mostrarCarta(cJugador, this.partida.cartaJugador(), true);
            ponerPuntos(cJugador);
            this.mostrarCarta(cMaquina, this.partida.cartaMaquina(), true);
            this.mostrarCarta(cMaquina, this.partida.cartaMaquina(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void plantarse() {
        System.out.println("plantarse");
        desvelarCartas();
        ponerPuntos(cMaquina);
        jugarIA();
        ponerPuntos(cMaquina);
    }

    public void desvelarCartas() {
        this.partida.maquina.forEach(carta -> {
            this.mostrarCarta(cMaquina, carta, true);
        });
    }

    void pedirCarta() {
        this.mostrarCarta(cJugador, this.partida.cartaJugador(), true);
        this.ponerPuntos(cJugador);
        if(this.partida.puntos(this.partida.jugador) > 21) {
            System.out.println("HAS PERDIDO");
        }
    }

    void retirarse() {
        mostrarMenu();
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
    private void mostrarPuntuaciones() {

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

        int puntosJugador = Integer.parseInt(coins.getText());

        if(operacion == "sumar") {
            puntosJugador += puntos;
        }else {
            puntosJugador -= puntos;
        }

        this.coins.setText(String.valueOf(puntosJugador));
    }
}