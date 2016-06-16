package Interfaces;

import Classes.Note;

public interface DatabaseOperations
{
    void setConnect();

    void deleteNoteFromDB(Note note);

    void updateNoteInDB(Note note);

    void insertNoteToDB(Note note);
}
