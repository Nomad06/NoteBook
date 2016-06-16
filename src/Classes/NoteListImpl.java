package Classes;

import Interfaces.NoteList;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class NoteListImpl implements NoteList
{
    private ObservableList<Note> observableList = FXCollections.observableArrayList();

    @Override
    public void addNote(Note note)
    {
        observableList.add(note);
    }

    @Override
    public void deleteNote(Note note)
    {
        observableList.remove(note);
    }

    @Override
    public void updateNote(Note note)
    {

    }

    public void writeDataToObservableList(ArrayList<Note> listFromDB)
    {
        observableList.addAll(listFromDB);
        System.out.println(Thread.currentThread() + " absorbing arrayList by observableList");
    }

    public ObservableList<Note> getObservableList()
    {
        return this.observableList;
    }
}
