package di.blackjack;

import di.componentesblackjack.carta.Carta;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
    private Button bLogin;

    private AnchorPane cMaquina, cJugador;

    private Label puntosMaquina, puntosJugador, coins;

    @FXML
    AnchorPane contenido;

    private Carta carta2;

    private ImageView imagenView;

    private Button bAceptar, bCerrar;

    private Label cTexto;

    private TextField cUsuario;

    private Partida partida = new Partida();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ventanaUsuario();
    }

    private void ventanaUsuario() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ventanaUsuario.fxml"));

            contenido.getChildren().clear();
            contenido.getChildren().add(loader.load());

            cUsuario = (TextField) contenido.lookup("#cUsuario");

            bLogin = (Button) contenido.lookup("#bLogin");
            bLogin.setOnAction(e -> guardarUsuario());


            this.mostrarMenu();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void guardarUsuario() {

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
            bHit.setOnAction(e -> {
                pedirCarta();
            });

            bExit = (Button) contenido.lookup("#bExit");
            bExit.setOnAction(e -> retirarse());

            bStand = (Button) contenido.lookup("#bStand");
            bStand.setOnAction(e -> {
                try {
                    plantarse();
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });

            this.partida.iniciarPartida();

            int creditos = this.calcularCreditos(1, "restar");
            if(creditos > 0) {
                this.ponerCreditos(creditos);
            }else {
                this.ponerCreditos(creditos);

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NO COINS");
                alert.setHeaderText("WARNING");
                alert.setContentText("You are out of coins, come back later");
                alert.showAndWait();
                Platform.exit();
            }

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

    private void mostrarPuntuaciones() {

    }

    void pedirCarta() {
        this.mostrarCarta(cJugador, this.partida.cartaJugador(), true);
        this.ponerPuntos(cJugador);
        if(this.partida.puntos(this.partida.jugador) > 21) {
            System.out.println("HAS PERDIDO");
            desvelarCartas();
            abrirVentana("OH, you lost", "gameover");
        }
    }

    void retirarse() {
        mostrarMenu();
    }

    void plantarse() throws IOException, InterruptedException {
        desvelarCartas();
        jugarIA();
        ponerPuntos(cMaquina);
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

    public void desvelarCartas() {
        Carta c= (Carta) this.cMaquina.getChildren().get(this.cMaquina.getChildren().size()-1);
        ModeloCarta carta = this.partida.maquina.get(this.partida.maquina.size() - 1);
        c.setImagen(carta.getImagen());
    }

    public void jugarIA() throws IOException, InterruptedException {

        while (this.partida.puntos(this.partida.maquina) < this.partida.puntos(this.partida.jugador)) {
            this.mostrarCarta(cMaquina, this.partida.cartaMaquina(), true);
        }

        comprobarResultado();
    }

    public void comprobarResultado() throws IOException, InterruptedException {
        String mensaje = "";
        String imagen = "";

        if(this.partida.puntos(this.partida.maquina) > 21) {
            if(this.partida.puntos(this.partida.jugador) == 21) {
                System.out.println("NORABOA BLACK JACK, MAIS 2 PUNTOS");
                mensaje = "Black jack, you have won three coins";
                imagen = "youwin";
                ponerCreditos(calcularCreditos(3, "sumar"));
            }else {
                System.out.println("Noraboa, mais 1 punto");
                mensaje = "You have won two coins";
                imagen = "youwin";
                ponerCreditos(calcularCreditos(2, "sumar"));
            }
        }else if (this.partida.puntos(this.partida.maquina) == 21) {
            if(this.partida.puntos(this.partida.jugador) == 21) {
                System.out.println("EMPATE");
                mensaje = "OH, you tied";
                imagen = "tied";
                ponerCreditos(calcularCreditos(1, "sumar"));
            }else {
                System.out.println("HAS PERDIDO");
                mensaje = "OH, you lost";
                imagen = "gameover";
            }
        }else {
            if(this.partida.puntos(this.partida.maquina) >= this.partida.puntos(this.partida.jugador)) {
                System.out.println("HAS PERDIDO");
                mensaje = "OH, you lost";
                imagen = "gameover";
            }
        }
        Timeline timeline=new Timeline(
                new KeyFrame(Duration.millis(2000))
        );
        String finalMensaje = mensaje;
        String finalImagen = imagen;
        timeline.setOnFinished(e-> abrirVentana(finalMensaje, finalImagen));
        timeline.playFromStart();

    }

    public void abrirVentana(String mensaje, String imagen) {

        try {

            Stage stage = new Stage();
            FXMLLoader loader= new FXMLLoader(getClass().getResource("ventanaInfo.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Ventana Modal");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
            bAceptar = (Button) root.lookup("#bAceptar");
            bAceptar.setOnAction(event -> {
                nuevaPartida();
                stage.close();
            });
            imagenView = (ImageView) root.lookup("#imagenView");
            Image img = new Image(getClass().getResourceAsStream("img/"+imagen+".png"));
            imagenView.setImage(img);
            cTexto = (Label) root.lookup("#cTexto");
            cTexto.setText(mensaje);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int calcularCreditos(int puntos, String operacion) {

        int puntosJugador = 0;

        if(operacion == "sumar") {
            puntosJugador = this.partida.getCreditos() + puntos;
        }else {
            puntosJugador = this.partida.getCreditos() - puntos;
        }

        this.partida.setCreditos(puntosJugador);

        return puntosJugador;
    }

    public void ponerCreditos(int creditos) {
        this.coins.setText(String.valueOf(creditos));
    }
}