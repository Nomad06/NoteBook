package Classes;

import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class Note
{
    private SimpleStringProperty date = new SimpleStringProperty("");
    private SimpleStringProperty noteText = new SimpleStringProperty("");


    public Note(String d, String text)
    {
        this.date = new SimpleStringProperty(d);
        this.noteText = new SimpleStringProperty(text);
    }

    public Note()
    {}

    //setters
    public void setDate(String d)
    {
        this.date.set(d);
    }

    public void setNoteText(String text)
    {
        this.noteText.set(text);
    }

    //getters
    public String getNoteText()
    {
        return noteText.get();
    }

    public String getDate()
    {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public SimpleStringProperty noteTextProperty() {
        return noteText;
    }
}
