module di.blackjack {
    requires javafx.controls;
    requires javafx.fxml;
    requires di.componentesblackjack;

    opens di.blackjack to javafx.fxml;
    exports di.blackjack;
}