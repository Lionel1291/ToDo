package ch.bztf;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * DeleteController
 */
public class DeleteController {
    @FXML
    private Button button_No;

    @FXML
    private Button button_Yes;

    private String deleteNr;

    private String sqlDelete = "DELETE FROM Auftraege where AuftragNr = ?";

    /**
     * Diese Methode prueft wird dazu verwendet um Daten zu loeschen indem man auf
     * Ja Klickt. Anschliessend wird man zurueck zur view.fxml geleitet
     * 
     * @param event : Wenn mit der Maus auf den Knopf gedrueckt wird
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    protected void deleteData(MouseEvent event) throws SQLException, IOException {
        deleteNr = ViewController.deleteNr;
        try {
            Connection dbcConnection = SQLiteDBToDo.sQLiteDBToDoConnection();
            PreparedStatement preparedStatement = dbcConnection.prepareStatement(sqlDelete);
            preparedStatement.setString(1, deleteNr);
            preparedStatement.execute();
            dbcConnection.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        App.setRoot("view");
    }

    /**
     * Diese Methode wird verwendet um zum view.fxml zurueck zu kehren
     * 
     * @param event : Wenn mit der Maus auf den Knopf gedrückt wird
     * @throws IOException
     */
    @FXML
    protected void backToMain(MouseEvent event) throws IOException {
        App.setRoot("view");
    }
}