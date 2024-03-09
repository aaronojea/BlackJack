package di.blackjack;

/*
 * Clase: ControladorVistaPruebas
 * Autor: Aarón Ojea Olmos
 * Fecha de creación: 2024
 * Descripción-Enunciado: Clase controlador de la interfaz gráfica de las vistas del juego. Aquí se implementa como se verán
 *                        las cartas, se coloquen los puntos en sus marcadores y etc...
 */

import di.componentesblackjack.carta.Carta;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ControladorJuego implements Initializable {

    //Componentes de la vista vistaBase
    @FXML
    private AnchorPane contenido;

    //Componentes de la vista ventanaUsuario
    private Button bLogin;
    private TextField cUsuario;

    //Componentes de la vista vistaMenu
    private Button bEnter;
    private Button bExitGame;

    //Componentes de la vista vistaJuego
    private Button bExit;
    private Button bHit;
    private Button bStand;
    private AnchorPane cMaquina, cJugador;
    private Label puntosMaquina, puntosJugador, coins;

    //Componentes de la vista vistaInfo
    private ImageView imagenView;
    private Button bAceptar;
    private Label cTexto;

    //Componente reutilizable echo en otro proyecto
    private Carta carta2;

    private Partida partida = new Partida();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ventanaUsuario();
    }

    //Método que carga en la vistaBase la vista de ventanaUsuario. Cogiendo así de ella sus componentes para poder
    //utilizarlos en otros métodos y añadirles funcionalidad.
    private void ventanaUsuario() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ventanaUsuario.fxml"));

            contenido.getChildren().clear();
            contenido.getChildren().add(loader.load());

            cUsuario = (TextField) contenido.lookup("#cUsuario");

            bLogin = (Button) contenido.lookup("#bLogin");
            bLogin.setOnAction(e -> guardarUsuario(cUsuario.getText()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Método que llama el boton aceptar de la vista de ventanaUsuario, en el guarda en el atributo usuario de la clase
    //partida el texto que será el nombre de un usuario introducido en el campo de texto de la ventanaUsuario. Luego llama
    //al método mostrarMenu
    private void guardarUsuario(String usuario) {
        this.partida.setUsuario(usuario);
        this.mostrarMenu();
    }

    //Método que carga en la vistaBase la vista de vistaMenu. Cogiendo así de ella sus componentes para poder
    //utilizarlos en otros métodos y añadirles funcionalidad.
    private void mostrarMenu() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("vistaMenu.fxml"));

            contenido.getChildren().clear();
            contenido.getChildren().add(loader.load());

            bEnter = (Button) contenido.lookup("#bEnter");
            bEnter.setOnAction(e -> nuevaPartida());

            bExitGame = (Button) contenido.lookup("#bExitGame");
            bExitGame.setOnAction(e -> Platform.exit());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Este método es llamado por el boton de Enter que hay en la vistaMenu. Carga en la vistaBase la vista de vistaJuego.
    //Cogiendo así de ella sus componentes para poder utilizarlos en otros métodos y añadirles funcionalidad.
    //Tambien utilizando la clase partida inicia la lógica del juego.
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

            //Método de la clase Partida
            this.partida.iniciarPartida();

            //Aquí al iniciar el juego le resta un crédito que significa la apuesta del jugador. En cada partida apuesta
            //1 la máquina y uno el jugador. Luego comprueba si tiene créditos o no para poder jugar.
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

    //Este método es llamado por el boton Hit. Coloca una carta en el AncchorPane del jugador, le suma su valor en sus puntos.
    //Luego comprueba si se ha pasado de 21, si es así el jugador ya ha perdido la partida.
    void pedirCarta() {
        this.mostrarCarta(cJugador, this.partida.cartaJugador(), true);
        this.ponerPuntos(cJugador);
        if(this.partida.puntos(this.partida.jugador) > 21) {
            System.out.println("HAS PERDIDO");
            desvelarCartas();
            abrirVentana("OH, you lost", "gameover");
        }
    }

    //Este método es llamado por el botón Exit. Sale del juego y vuelve a la vista del menu. Primero le devuelve el crédito que
    //apuesta al iniciar la partida porque esta no la juega se retira.
    void retirarse() {
        this.ponerCreditos(calcularCreditos(1, "sumar"));
        this.rankingUsuarios();
        this.mostrarMenu();
    }

    //Este método es llamado por el boton Stand. Cuando el jugador no quiere más cartas le da a este boton. Se muestran las
    //cartas de la máquina y empieza a jugar la máquina, luego se ponen los puntos que tenga.
    void plantarse() throws IOException, InterruptedException {
        desvelarCartas();
        jugarIA();
        ponerPuntos(cMaquina);
    }

    //Este método coloca el componente carta correspondiente al modelo en el AnchorPane indicado, el del jugador
    //o el de la máquina
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

    //En este método se le indica a quien hay que poner los puntos si al jugador o a la máquina y con el método puntos
    //de la clase Partida suma los valores de las cartas y los pone en el marcador indicado.
    public void ponerPuntos(AnchorPane zona) {

        if (zona==cMaquina){
            int puntos = this.partida.puntos(this.partida.maquina);
            this.puntosMaquina.setText(String.valueOf(puntos));
        } else {
            int punt = this.partida.puntos(this.partida.jugador);
            this.puntosJugador.setText(String.valueOf(punt));
        }
    }

    //Este método gira las cartas de la máquina que están volteadas para poder verlas.
    public void desvelarCartas() {
        Carta c= (Carta) this.cMaquina.getChildren().get(this.cMaquina.getChildren().size()-1);
        ModeloCarta carta = this.partida.maquina.get(this.partida.maquina.size() - 1);
        c.setImagen(carta.getImagen());
    }

    //Juego de la máquina. Pedirá cartas hasta superar en puntos al jugador.
    public void jugarIA() throws IOException, InterruptedException {

        while (this.partida.puntos(this.partida.maquina) < this.partida.puntos(this.partida.jugador)) {
            this.mostrarCarta(cMaquina, this.partida.cartaMaquina(), true);
        }

        comprobarResultado();
    }

    //Una vez para de pedir cartas la máquina se comprueba el resultado. Si gana la máquina, el jugador, hay empate o BlackJack.
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

    //Este método abre una ventana que carga la vista de ventanaInfo. Esta ventana se muestra una vez comprobado el resultado.
    //Mostrando si el jugador ha perdido o ganado.
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

    //Este método resta o suma los créditos del jugador segun gane o pierda
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

    //Este método pone en el marcador de créditos del jugador los créditos que tiene en todo momento.
    public void ponerCreditos(int creditos) {
        this.coins.setText(String.valueOf(creditos));
    }

    //Este método añade en el HashMap el nombre y los créditos con los que acaba un jugador, luego lo pasa a un TreeMap
    //para poder ordenarlos por los nombres. Luego los guarda en un archivo de texto.
    private void rankingUsuarios() {

        cargarDatos(this.partida.ranking);

        this.partida.ranking.put(this.partida.getUsuario(), this.partida.getCreditos());

        Map<String, Integer> treeMap = new TreeMap<>(this.partida.ranking);

        guardarDatos(treeMap);
    }

    //Este método carga en el HashMap los registros ya guardados en el archivo, para que al guardar un nuevo registro
    //aparezcan los que ya hay.
    private void cargarDatos(HashMap<String, Integer> ranking) {

        try (BufferedReader br = new BufferedReader(new FileReader("ranking.txt"))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                ranking.put(datos[0], Integer.valueOf(datos[1]));
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Este método guarda los valores del TreeMap en un archivo de texto.
    private void guardarDatos(Map<String, Integer> treeMap) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ranking.txt"))){

            for(Map.Entry<String, Integer> e: treeMap.entrySet()) {

                bw.write(e.getKey()+","+e.getValue());

                bw.newLine();
            }

            bw.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}