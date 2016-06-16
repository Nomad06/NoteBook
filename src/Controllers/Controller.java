package Controllers;

import Classes.DatabaseOperationsImpl;
import Classes.Note;
import Classes.NoteListImpl;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.Window;

import javafx.event.ActionEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Controller
{

    private NoteListImpl noteList = new NoteListImpl();

    private Stage mainStage;
    private Stage noteAddStage;
    private Parent noteAdd;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private NoteAddController noteAddController;

    private DatabaseOperationsImpl dbOperations = new DatabaseOperationsImpl();

    //table
    @FXML
    private TableView<Note> tableNotes;

    @FXML
    private TableColumn<Note, Date> dateColumn;

    @FXML
    TableColumn<Note, String> noteColumn;

    //Buttons
    @FXML
    Button addButton;

    @FXML
    Button deleteButton;

    @FXML
    Button editButton;



    public void setMainStage(Stage stageFromMain)
    {
        this.mainStage = stageFromMain;
    }

    @FXML
    private void initialize() {

        System.out.println(Thread.currentThread());
        dbInitInTread();
        initData();
        tableFilling();
        initLoader();
        initListeners();
    }



    private void tableFilling()
    {
        // устанавливаем тип и значение которое должно хранится в колонке

        noteColumn.setCellValueFactory(new PropertyValueFactory<Note, String>("noteText"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Note, Date>("date"));

        // заполняем таблицу данными
        tableNotes.setItems(noteList.getObservableList());
    }

    private void dbInitInTread()
    {
        Thread threadDB = new Thread(dbOperations);
        try {
            threadDB.setName("DBWorker");
            threadDB.start();
            threadDB.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread());
    }

    private void initLoader() {
        try
        {
            fxmlLoader.setLocation(getClass().getResource("../fxml/noteAdd.fxml"));
            noteAdd = fxmlLoader.load();
            noteAddController = fxmlLoader.getController();
            noteAddController.setDatabaseOperations(dbOperations);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void initData()
    {
        ArrayList<Note> noteArrayList;
        noteArrayList = dbOperations.getNoteList();

        System.out.println("Внезапно оказались в инициировании данных");
        noteList.writeDataToObservableList(noteArrayList);
    }


    private void initListeners()
    {
        noteList.getObservableList().addListener(new ListChangeListener<Note>() {
            @Override
            public void onChanged(Change<? extends Note> c) {

            }
        });


        tableNotes.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getClickCount() == 2) {
                    editDialogCall();
                }
            }
        });

    }

    private void editDialogCall() {
        noteAddController.setNote((Note)tableNotes.getSelectionModel().getSelectedItem());
        showDialog();
    }

    public void enterKeyPress(KeyEvent event)
    {
        KeyCode code = event.getCode();

        if(code.equals(KeyCode.ENTER))
        {
            editDialogCall();
        }
        else if(code.equals(KeyCode.DELETE))
        {
            noteDeleteActions();
        }
    }

    public void addButtonAction(ActionEvent actionEvent)
    {
        Object source = actionEvent.getSource();

        if(!(source instanceof Button))
        {
            return;
        }

        Button clickedButton = (Button)source;

        Window parent = ((Node)actionEvent.getSource()).getScene().getWindow();


        switch (clickedButton.getId())
        {
            case "addButton":
                noteAddController.setNote(new Note(curTimeString(), ""));

                showDialog();
                noteList.addNote(noteAddController.getNote());

                break;

            case "deleteButton":
                noteDeleteActions();

                break;

            case "editButton":

                editDialogCall();
                break;
        }

    }

    private void noteDeleteActions() {
        Note noteToDelete = (Note) tableNotes.getSelectionModel().getSelectedItem();
        dbOperations.deleteNoteFromDB(noteToDelete);
        noteList.deleteNote(noteToDelete);
    }

    private String curTimeString()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stringDate = dateFormat.format(Calendar.getInstance().getTime());

        return stringDate;
    }

    @FXML
    private void showDialog()
    {
        if(noteAddStage == null)
        {
            noteAddStage = new Stage();
            noteAddStage.setTitle("Note adding");
            noteAddStage.setMinHeight(500);
            noteAddStage.setMinWidth(500);
            noteAddStage.setResizable(false);
            noteAddStage.setScene(new Scene(noteAdd));
            noteAddStage.initModality(Modality.WINDOW_MODAL);
            noteAddStage.initOwner(mainStage);
        }
		noteAddStage.showAndWait();

    }

    public void contextMenuRequest(Event event)
    {
        System.out.println("Зафиксировано несанкционированное нажатие правой кнопки мыши!");

    }
}
