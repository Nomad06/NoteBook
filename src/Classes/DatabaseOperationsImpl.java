package Classes;

import Interfaces.DatabaseOperations;
import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DatabaseOperationsImpl implements DatabaseOperations, Runnable
{
    private static final String URL = "jdbc:mysql://localhost:3306/notes";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "000000";

    private static final String noteID = "note";
    private static final String dateID = "noteDate";

    private Connection connection;
    private ArrayList<Note> noteList;
    private PreparedStatement prepStatement;


    public DatabaseOperationsImpl()
    {
        noteList = new ArrayList<Note>();

        prepStatement = null;
    }

    @Override
    public void setConnect()
    {
        System.out.println(Thread.currentThread());
        try
        {
            System.out.println("Пробуем подключиться!");
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if(!connection.isClosed())
            {
                System.out.println("Success");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Соединение не получено почему-то");
            e.printStackTrace();
        }
    }

    private void readFromDB()
    {

        System.out.println(Thread.currentThread() + " стартовал метод для чтения из базы");
        String query = "select * from notes";

        try
        {
            Statement statement = connection.createStatement();
            System.out.println(Thread.currentThread() + " получил стетхема " + statement.isClosed());

            ResultSet resultSet = statement.executeQuery(query);
            System.out.println(Thread.currentThread() + " выполнил запрос " + resultSet.next());

            while (resultSet.next())
            {
                String stringDate = resultSet.getDate(dateID) + " " + resultSet.getTime(dateID);
                noteList.add(new Note(stringDate, resultSet.getString(noteID)));
                System.out.println(Thread.currentThread() + " read another row");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteNoteFromDB(Note note)
    {
        System.out.println(Thread.currentThread() + " стартовал метод записи в базу");

        String query = "delete from notes where noteDate =?;";
        try
        {
            prepStatement = connection.prepareStatement(query);
            System.out.println(Thread.currentThread() + " получили подготовленного стетхема");
            prepStatement.setString(1, note.getDate());
            prepStatement.execute();
            System.out.println(Thread.currentThread() + " выполнили стетхема");

        }
        catch (SQLException e)
        {
            System.out.println(Thread.currentThread() + " какие-то проблемы со стетхемом");
            e.printStackTrace();
        }
    }

    @Override
    public void updateNoteInDB(Note note)
    {
        System.out.println(Thread.currentThread() + " стартовал метод записи в базу");
        String query = "update notes set note = ? where noteDate=?;";
        try
        {
            prepStatement = connection.prepareStatement(query);
            System.out.println(Thread.currentThread() + " получили подготовленного стетхема");
            prepStatement.setString(1, note.getNoteText());
            prepStatement.setString(2, note.getDate());
            prepStatement.execute();
            System.out.println(Thread.currentThread() + " выполнили стетхема");

        }
        catch (SQLException e)
        {
            System.out.println(Thread.currentThread() + " какие-то проблемы со стетхемом");
            e.printStackTrace();
        }
    }

    @Override
    public void insertNoteToDB(Note note)
    {
        System.out.println(Thread.currentThread() + " стартовал метод записи в базу");
        String query = "insert into notes (note, noteDate) values (?, ?);";

        try
        {
            prepStatement = connection.prepareStatement(query);
            System.out.println(Thread.currentThread() + " получили подготовленного стетхема");
            prepStatement.setString(1, note.getNoteText());
            prepStatement.setString(2, note.getDate());
            prepStatement.execute();
            System.out.println(Thread.currentThread() + " выполнили стетхема");

        }
        catch (SQLException e)
        {
            System.out.println(Thread.currentThread() + " какие-то проблемы со стетхемом");
            e.printStackTrace();
        }
    }



    public ArrayList<Note> getNoteList()
    {
        return noteList;
    }

    public Connection getConnection()
    {
        return connection;
    }

    private void workWithDB()
    {
        System.out.println("Подошли к методу установки соединения");
        setConnect();
        System.out.println("Подошли к методу чтения данных из базы");
        readFromDB();
    }

    @Override
    public void run()
    {
        workWithDB();
    }
}
