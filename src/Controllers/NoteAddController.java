package Controllers;

import Classes.DatabaseOperationsImpl;
import Classes.Note;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.stage.Stage;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class NoteAddController
{
    @FXML
    private Button noteAddButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField dateField;

    @FXML
    private TextArea noteArea;

    private Note note;

    private DatabaseOperationsImpl databaseOperations;

    @FXML
    public void actionClose(ActionEvent event)
    {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        noteArea.setText("");
        stage.hide();
    }

    @FXML
    public void actionAdd(ActionEvent event)
    {

        //note.setDate(curTimeString());

        note.setNoteText(noteArea.getText());
        if(noteAddButton.getText().equals("Edit note"))
        {
            editThread();
        }
        else
        {
            addThread();
        }
        actionClose(event);
    }

    private void addThread()
    {
        Thread noteAddThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                databaseOperations.insertNoteToDB(note);
            }
        });
        noteAddThread.setName("Thread for note adding");
        noteAddThread.start();
        try {
            noteAddThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void editThread()
    {
        Thread noteEdiThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                databaseOperations.updateNoteInDB(note);
            }
        });
        noteEdiThread.setName("Thread for note adding");
        noteEdiThread.start();
        try {
            noteEdiThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private String curTimeString()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringDate = dateFormat.format(Calendar.getInstance().getTime());

        return stringDate;
    }

    public void setDatabaseOperations(DatabaseOperationsImpl databaseOperations)
    {
        this.databaseOperations = databaseOperations;
    }

    public void setNote(Note noteForSet)
    {
        if(noteForSet == null)
        {
            return;
        }
        note = noteForSet;
        String noteText = note.getNoteText();
        String noteDate = note.getDate();

        if(!noteText.equals("")&&!noteDate.equals(""))
        {
            noteAddButton.setText("Edit note");
        }
        noteArea.setText(noteText);
        dateField.setText(noteDate);

    }



    public Note getNote()
    {
        return note;
    }
}
