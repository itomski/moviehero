module de.lubowiecki {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires json.simple;

    opens de.lubowiecki to javafx.fxml;
    exports de.lubowiecki;
}
