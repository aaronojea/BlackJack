package di.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Partida {

    boolean iniciado; //controlar estado partida
    List<ModeloCarta> baraja;
    List<ModeloCarta> maquina;
    List<ModeloCarta> jugador;

    public Partida() {}

    public void iniciarPartida() {
        this.iniciado = true;
        this.baraja = new ArrayList<>();
        this.maquina = new ArrayList<>();
        this.jugador = new ArrayList<>();
        this.crearBaraja();
    }

    //Método para crear una baraja
    public void crearBaraja() {
        char[] palos = {'C', 'T', 'P', 'D'};
        String[] nombres = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        int valor;

        for (char palo : palos) {
            for (int i = 0; i < 13; i++) {
                valor = (i >= 10) ? 10 : i + 1;
                ModeloCarta carta = new ModeloCarta(palo, nombres[i], valor);
                this.baraja.add(carta);
            }
        }
        //System.out.println(baraja.toString());
    }

    //Método para sacar una carta de la baraja que aún no esté fuera
    public ModeloCarta sacarCarta(){
        ModeloCarta carta=null;
        Random aleatorio = new Random(System.currentTimeMillis());
        boolean control=true;
        while(control) {
            carta = this.baraja.get(aleatorio.nextInt(52));
            if (!carta.isRepartida()) {
                carta.setRepartida(true);
                control=false;
            }
        }
        return carta;
    }

    //Método para repartir una carta al jugador
    public ModeloCarta cartaJugador() {
        ModeloCarta c = this.sacarCarta();
        this.jugador.add(c);
        return c;
    }

    //Método para repartir una carta al ordenador
    public ModeloCarta cartaMaquina() {
        ModeloCarta c = this.sacarCarta();
        this.maquina.add(c);
        return c;
    }

    //Getters & Setters
    public boolean isIniciado() {
        return iniciado;
    }

    public void setIniciado(boolean iniciado) {
        this.iniciado = iniciado;
    }

    public List<ModeloCarta> getBaraja() {
        return baraja;
    }

    public void setBaraja(List<ModeloCarta> baraja) {
        this.baraja = baraja;
    }

    public List<ModeloCarta> getMaquina() {
        return maquina;
    }

    public void setMaquina(List<ModeloCarta> maquina) {
        this.maquina = maquina;
    }

    public List<ModeloCarta> getJugador() {
        return jugador;
    }

    public void setJugador(List<ModeloCarta> jugador) {
        this.jugador = jugador;
    }

    public int puntos(List<ModeloCarta> cartas ){
        int suma=0;

        for (ModeloCarta carta : cartas) {
            suma += carta.getValor();
        }

        return  suma;
    }
}
