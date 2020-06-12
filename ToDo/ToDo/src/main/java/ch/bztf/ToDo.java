package ch.bztf;

import java.util.Date;

public class ToDo {
    private int number;
    private String toDoName;
    private String withWho;
    private String where;
    private Date date;
    private String time;

    /**
     * Konstruktor fuer die Klasse
     * 
     * @param newNumber   : Die Nummer der ToDos
     * @param newToDoName : Name des ToDos
     * @param newWithWho  : Mit Wem der ToDo ist
     * @param newWhere    : Wo die ToDos stattfinden
     * @param newDate     : Das Datum des ToDos
     * @param newTime     : Die Zeit des ToDos
     */
    public ToDo(int newNumber, String newToDoName, String newWithWho, String newWhere, Date newDate, String newTime) {
        number = newNumber;
        toDoName = newToDoName;
        withWho = newWithWho;
        where = newWhere;
        date = newDate;
        time = newTime;
    }

    /**
     * @param newNumber
     */
    public void setNumber(int newNumber) {
        number = newNumber;
    }

    /**
     * Gibt die Variable number zurueck
     * 
     * @return : int als Rueckgabewert
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param newToDoName
     */
    public void setToDoName(String newToDoName) {
        toDoName = newToDoName;
    }

    /**
     * Gibt die Variable toDoName zuruek
     * 
     * @return : String als Rueckgabewert
     */
    public String getToDoName() {
        return toDoName;
    }

    /**
     * @param newWithWho
     */
    public void setWithWho(String newWithWho) {
        withWho = newWithWho;
    }

    /**
     * Gibt die Variable withWho zuruek
     * 
     * @return : String als Rueckgabewert
     */
    public String getWithWho() {
        return withWho;
    }

    /**
     * @param newWhere
     */
    public void setWhere(String newWhere) {
        where = newWhere;
    }

    /**
     * Gibt die Variable where zurueck
     * 
     * @return : String als Rueckgabewert
     */
    public String getWhere() {
        return where;
    }

    /**
     * @param newDate
     */
    public void setDate(Date newDate) {
        date = newDate;
    }

    /**
     * Gibt die Variable date zuruek
     * 
     * @return : String als Rueckgabewert
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param newTime
     */
    public void setTime(String newTime) {
        time = newTime;
    }

    /**
     * Gibt die Variable time zurueck
     * 
     * @return : String als Rueckgabewert
     */
    public String getTime() {
        return time;
    }
}