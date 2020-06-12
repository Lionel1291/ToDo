package ch.bztf;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * ViewController
 */
public class ViewController implements Initializable {
    @FXML
    private TextField tf_Name_Add;

    @FXML
    private TextField tf_WithWho_Add;

    @FXML
    private TextField tf_Where_Add;

    @FXML
    private TextField tf_Hours_Add;

    @FXML
    private TextField tf_Minutes_Add;

    @FXML
    private TextField tf_DeleteToDoNr;

    @FXML
    private Text t_State;

    @FXML
    private DatePicker dp_Add;

    @FXML
    private Button button_Add;

    @FXML
    private Button butto_Delete;

    @FXML
    private TableView<ToDo> tv_Show;

    @FXML
    private TableColumn<ToDo, Integer> tc_Show_ToDoNumber;

    @FXML
    private TableColumn<ToDo, String> tc_Show_ToDoName;

    @FXML
    private TableColumn<ToDo, String> tc_Show_WithWho;

    @FXML
    private TableColumn<ToDo, String> tc_Show_Where;

    @FXML
    private TableColumn<ToDo, Date> tc_Show_Date;

    @FXML
    private TableColumn<ToDo, String> tc_Show_Time;

    private ObservableList<ToDo> ol;

    private String sqlSelect = "SELECT * FROM Auftraege";
    private String sqlInsert = "INSERT INTO Auftraege(AuftragNr,AuftragTitel,AuftragMitWem,AuftragWo,AuftragDatum,AuftragZeit) VALUES (?,?,?,?,?,?)";
    private String sqlDelete = "DELETE FROM Auftraege where AuftragNr = ?";

    public static String deleteNr;

    /**
     * Hier werden alle Daten geladen sobald der ViewController ins spiel kommt
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        try {
            loadAll();
            t_State.setText("Daten geladen");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * In dieser Methode weden die Daten aus den Textfeldern geholt und ueberprueft oder in andere Methoden übergeben wo sie geprueft werden.
     * Geprueft wird ob in den erfordelichen Feldern etwas enthalten ist und ob die Datums und Zeitangaben stimmen koennen.
     * Am ende werden die Felder geleert.
     * Wenn etwas nicht stimmen wuerde enthält die Variable absichtlich den wert null oder es wird einfach im Status angezigt was falsch ist.
     * 
     * @param event : Wenn mit der Maus auf den Knopf gedrückt wird
     * @throws SQLException
     */
    @FXML
    protected void addData(MouseEvent event) throws SQLException {
        String toDoName = tf_Name_Add.getText();
        String withWho = tf_WithWho_Add.getText();
        String minutes = tf_Minutes_Add.getText();
        String hours = tf_Hours_Add.getText();
        String where = tf_Where_Add.getText();

        if ((dp_Add.getValue() != null) || ((!toDoName.equals("")) && (!toDoName.equals(null)))) {
            if (dp_Add.getValue() != null) {
                LocalDate localDate = dp_Add.getValue();
                java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
                Boolean tempBoolean = false;
                if (actuallDateInsert(localDate, tempBoolean)) {
                    sqlDate = null;
                }
                String time = timeCheck(hours, minutes);

                if ((!toDoName.equals("")) && (!toDoName.equals(null))) {
                    if (time != null) {
                        if (sqlDate != null) {
                            try {
                                insertAll(toDoName, withWho, where, sqlDate, time);
                            } catch (SQLException exception) {
                                System.out.println(exception.getMessage());
                            }
                            loadAll();
                            tf_Name_Add.clear();
                            tf_WithWho_Add.clear();
                            tf_Minutes_Add.clear();
                            tf_Hours_Add.clear();
                            tf_Where_Add.clear();
                            dp_Add.setValue(null);
                            t_State.setText("Daten geladen und hinzugefügt");
                        }
                    } else {
                        t_State.setText("Ungülltige Zeitangabe");
                    }
                } else {
                    t_State.setText("Wo ist der Name des ToDos?");
                }
            } else {
                t_State.setText("Wo ist das Datum?");
            }
        } else {
            t_State.setText("Das Datum und der Name fehlt.");
        }
    }

    /**
     * Diese Methode wird verwendet um aus einem Feld eine Zahl heruaszulesen. Wenn
     * dieses Feld nicht leer ist wird auf das FXML File delete.fxml gewechselt.
     * 
     * @param event : Wenn mit der Maus auf den Knopf gedrückt wird
     * @throws IOException
     */
    @FXML
    protected void deleteData(MouseEvent event) throws IOException {
        deleteNr = tf_DeleteToDoNr.getText();
        if (!deleteNr.equals("")) {
            App.setRoot("delete");
        }else {
            t_State.setText("Bitte ToDo Nummer eingeben.");
        }
    }

    /**
     * In dieser Methode werden die Daten aus der Datenbank in die Tabelle geladen.
     * Wenn bei der Datumsprüfung true herauskommt wird der Datensatz gelöscht.
     * Wenn false herauskommt wird der Datensatz in eine Observable List eingetragen 
     * und anschliessend in die Tabelle übergeben.
     * 
     * @throws SQLException
     */
    private void loadAll() throws SQLException {
        try {
            Connection dbConnection = SQLiteDBToDo.sQLiteDBToDoConnection();
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            ol = FXCollections.observableArrayList();

            while (resultSet.next()) {
                LocalDate localSQLDate = resultSet.getDate(5).toLocalDate();
                int tempInt = resultSet.getInt(1);
                String tempSring = Integer.toString(tempInt);
                PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlDelete);
                Boolean sdBoolean = true;
                if (actuallDateSelect(sdBoolean, localSQLDate)) {
                    preparedStatement.setString(1, tempSring);
                } else {
                    ol.add(new ToDo(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                            resultSet.getString(4), resultSet.getDate(5), resultSet.getString(6)));
                }
                preparedStatement.execute();
            }
            resultSet.close();
            dbConnection.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        tc_Show_ToDoNumber.setCellValueFactory(new PropertyValueFactory<ToDo, Integer>("number"));

        tc_Show_ToDoName.setCellValueFactory(new PropertyValueFactory<ToDo, String>("toDoName"));

        tc_Show_WithWho.setCellValueFactory(new PropertyValueFactory<ToDo, String>("withWho"));

        tc_Show_Where.setCellValueFactory(new PropertyValueFactory<ToDo, String>("where"));

        tc_Show_Date.setCellValueFactory(new PropertyValueFactory<ToDo, Date>("date"));

        tc_Show_Time.setCellValueFactory(new PropertyValueFactory<ToDo, String>("time"));

        tv_Show.setItems(null);
        tv_Show.setItems(ol);
    }

    /**
     * In dieser Methode werden die Daten in die Datenbank eingetragen
     * 
     * @param toDoName  : Name des ToDos
     * @param withWho   : Mit Wem der ToDo ist
     * @param where     : Wo die ToDos stattfinden
     * @param sqlDate   : Das Datum des ToDos
     * @param time      : Die Zeit des ToDos
     * @throws SQLException
     */
    private void insertAll(String toDoName, String withWho, String where, java.sql.Date sqlDate, String time) throws SQLException {
        Connection dbConnection = SQLiteDBToDo.sQLiteDBToDoConnection();
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlInsert);
        preparedStatement.setString(2, toDoName);
        preparedStatement.setString(3, withWho);
        preparedStatement.setString(4, where);
        preparedStatement.setDate(5, sqlDate);
        preparedStatement.setString(6, time);
        preparedStatement.addBatch();
        preparedStatement.execute();
        dbConnection.close();
    }

    /**
     * In dieser Methode wird geprüft ob das Datum aelter als heute ist.
     * Wenn das Datum aelter ist wird true zurueckgegeben und andernfalls false
     * 
     * @param localDate : Das Datum aus dem DatePicker
     * @param idBoolean : Wird zur pruefung des Datums verwendet
     * @return : Boolean als Rueckgabewert
     */
    private Boolean actuallDateInsert(LocalDate localDate, Boolean idBoolean) {
        LocalDate tempDate = LocalDate.now();
        if (localDate.getYear() == tempDate.getYear()) {
            if (localDate.getMonthValue() == tempDate.getMonthValue()) {
                if (localDate.getDayOfMonth() < tempDate.getDayOfMonth()) {
                    idBoolean = true;
                    t_State.setText("Datum ist von Gestern :)");
                } else {
                    idBoolean = false;
                }
            } else if (localDate.getMonthValue() < tempDate.getMonthValue()) {
                idBoolean = true;
                t_State.setText("Datum ist von Gestern :)");
            }
        } else if (localDate.getYear() < tempDate.getYear()) {
            idBoolean = true;
            t_State.setText("Datum ist von Gestern :)");
        }
        return idBoolean;
    }

    /**
     * Hier wird geprüft ob die Zeitangabe sinnvoll ist.
     * Wenn sie nicht sinnvoll ist wird sie auf absichtlich auf null gesetzt.
     * Wenn keine angabe vorhanden ist oder nur stunden und minuten wird das fehlende mit 00 ersetzt.
     * 
     * @param hours     : Stunden des ToDo
     * @param minutes   : Minuten des ToDo
     * @return : String als Rueckgabewert
     */
    private String timeCheck(String hours, String minutes) {
        String time = new String("00:00");
        if ((!hours.equals("")) || (!minutes.equals(""))) {
            if (minutes.equals("")) {
                minutes = "00";
            }
            if (hours.equals("")) {
                hours = "00";
            }
            int hoursInt = Integer.parseInt(hours);
            int minutesInt = Integer.parseInt(minutes);

            if ((minutesInt <= 59) && (minutesInt >= 0) && (hoursInt >= 0) && (hoursInt <= 23)) {
                String hoursTemp = Integer.toString(hoursInt);
                String minutesTemp = Integer.toString(minutesInt);

                if (hoursTemp.length() == 1) {
                    hoursTemp = "0" + hoursTemp;
                }
                if (minutesTemp.length() == 1) {
                    minutesTemp = "0" + minutesTemp;
                }
                time = hoursTemp + ":" + minutesTemp;
            } else {
                time = null;
                t_State.setText("Ungülltige Zeitangabe");
            }
        }
        return time;
    }

    /**
     * Hier wird geprüft ob das Datum aus der Datenbank noch einigermassen aktuell ist.
     * Wenn es nicht mehr aktuell ist wird true zurueckgegeben und andernfalls false.
     * 
     * @param sdBoolean     : Wird zur pruefung des Datums verwendet
     * @param localSQLDate  : Datum aus SQLite Datenbank
     * @return : Boolean als Rueckgabewert
     */
    private Boolean actuallDateSelect(Boolean sdBoolean, LocalDate localSQLDate) {
        LocalDate local = LocalDate.now();
        if (local.getYear() == localSQLDate.getYear()) {
            if (local.getMonthValue() == localSQLDate.getMonthValue()) {
                if ((local.getDayOfMonth() - 7) >= localSQLDate.getDayOfMonth()) {
                    sdBoolean = true;
                } else {
                    sdBoolean = false;
                }
            } else if (local.getMonthValue() >= localSQLDate.getMonthValue()) {
                sdBoolean = true;
            } else {
                sdBoolean = false;
            }
        } else if (local.getYear() >= localSQLDate.getYear()) {
            sdBoolean = true;
        } else {
            sdBoolean = false;
        }
        return sdBoolean;
    }
}