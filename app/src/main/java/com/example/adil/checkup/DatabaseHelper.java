package com.example.adil.checkup;

/**
 * Created by adil on 5/13/17.
 */


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.adil.checkup.models.Medicine;
import com.example.adil.checkup.models.Tag;
import com.example.adil.checkup.models.Todo;
import com.example.adil.checkup.models.meds;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Table Names
    private static final String TABLE_TODO = "meds";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_TODO_TAG = "meds_tags";

    private static final String TABLE_MEDICATION="Medication";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // NOTES Table - column nmaes
    private static final String KEY_TODO = "todo";
    private static final String KEY_STATUS = "status";

    // TAGS Table - column names
    private static final String KEY_TAG_NAME = "tag_name";

    // NOTE_TAGS Table - column names
    private static final String KEY_TODO_ID = "todo_id";
    private static final String KEY_TAG_ID = "tag_id";
    //MEDICAL table Column Names
    private static final String KEY_NAME = "name";
    private static final String KEY_DOSAGE = "dosage";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_WHATFOR = "whatfor";


    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_TODO_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_MEDICATION = "CREATE TABLE "
            + TABLE_MEDICATION + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            +KEY_NAME + " STRING,"
            + KEY_DESCRIPTION + " STRING," + KEY_DOSAGE + " STRING,"
            +KEY_WHATFOR+" STRING,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_TODO_TAG);
        db.execSQL(CREATE_TABLE_MEDICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TAG);

        // create new tables
        onCreate(db);
    }
    //"medication table methods"
//    creating a Medicine


    public void addMeds(String name, String dosage, String description, String whatfor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_DESCRIPTION, description); // descriptipn
        values.put(KEY_DOSAGE, dosage); // Email
        values.put(KEY_WHATFOR, whatfor); // Created At

        // Inserting Row
        long id = db.insert(TABLE_MEDICATION, null, values);
        db.close(); // Closing database connection


        Log.d("db", "New medication inserted into sqlite: " + id);
    }
    public HashMap<String, String> getMedDetails(long id) {
        HashMap<String, String> medication =  new HashMap<String, String>();

        String selectQuery = "SELECT  * FROM " + TABLE_MEDICATION +" WHERE ID ="+id;
        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

            // Move to first row
            c.moveToFirst();
            if (c.getCount() > 0) {
               medication.put("description",c.getString((c.getColumnIndex(KEY_DESCRIPTION))));
               medication.put("dosage", c.getString((c.getColumnIndex(KEY_DOSAGE))));
               medication.put("whatfor", c.getString((c.getColumnIndex(KEY_WHATFOR))));
               medication.put("name",c.getString((c.getColumnIndex(KEY_NAME))));
            }
            c.close();
            db.close();



  return  medication;
    }



    public List<meds> getAllMeds() {
        List<meds> medicines = new ArrayList<meds>();
        String selectQuery = "SELECT  * FROM " + TABLE_MEDICATION;

        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                meds Med=new meds();
                Med.setDescription(c.getString((c.getColumnIndex(KEY_DESCRIPTION))));
                Med.setDosage(c.getString((c.getColumnIndex(KEY_DOSAGE))));
                Med.setWhatfor(c.getString((c.getColumnIndex(KEY_WHATFOR))));
                Med.setName(c.getString((c.getColumnIndex(KEY_NAME))));
                Med.setId(c.getInt((c.getColumnIndex(KEY_ID))));


                // adding to todo list
                medicines.add(Med);
            } while (c.moveToNext());
        }

        return medicines;
    }


    // ------------------------ "todos" table methods ----------------//

    /**
     * Creating a todo
     */
    public long createToDo(Todo todo, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getNote());
        values.put(KEY_STATUS, todo.getStatus());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long todo_id = db.insert(TABLE_TODO, null, values);

        // insert tag_ids
        for (long tag_id : tag_ids) {
            createTodoTag(todo_id, tag_id);
        }

        return todo_id;
    }

    /**
     * get single todo
     */
    public meds getMed(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MEDICATION + " WHERE "
                + KEY_ID + " = " + todo_id;

        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        meds td = new meds();
        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        td.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        td.setDosage(c.getString(c.getColumnIndex(KEY_DOSAGE)));
        td.setWhatfor(c.getString(c.getColumnIndex(KEY_WHATFOR)));

        return td;
    }

    /**
     * getting all todos
     * */
    public List<Todo> getAllToDos() {
        List<Todo> todos = new ArrayList<Todo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Todo td = new Todo();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    /**
     * getting all todos under single tag
     * */
    public List<Todo> getAllToDosByTag(String tag_name) {
        List<Todo> todos = new ArrayList<Todo>();

        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_TODO_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Todo td = new Todo();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    /**
     * getting todo count
     */
    public int getToDoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a todo
     */
    public int updateToDo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getNote());
        values.put(KEY_STATUS, todo.getStatus());

        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });
    }

    /**
     * Deleting a todo
     */
    public void deleteToDo(long tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }

    // ------------------------ "tags" table methods ----------------//

    /**
     * Creating tag
     */
    public long createTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long tag_id = db.insert(TABLE_TAG, null, values);

        return tag_id;
    }

    /**
     * getting all tags
     * */
    public ArrayList<Tag> getAllTags() {
        ArrayList<Tag> tags = new ArrayList<Tag>();
        String selectQuery = "SELECT  * FROM " + TABLE_TAG;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Tag t = new Tag();
                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setTagName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));

                // adding to tags list
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }

    /**
     * Updating a tag
     */
    public int updateTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());

        // updating row
        return db.update(TABLE_TAG, values, KEY_ID + " = ?",
                new String[] { String.valueOf(tag.getId()) });
    }

    /**
     * Deleting a tag
     */
    public void deleteTag(Tag tag, boolean should_delete_all_tag_todos) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting tag
        // check if todos under this tag should also be deleted
        if (should_delete_all_tag_todos) {
            // get all todos under this tag
            List<Todo> allTagToDos = getAllToDosByTag(tag.getTagName());

            // delete all todos
            for (Todo todo : allTagToDos) {
                // delete todo
                deleteToDo(todo.getId());
            }
        }

        // now delete the tag
        db.delete(TABLE_TAG, KEY_ID + " = ?",
                new String[] { String.valueOf(tag.getId()) });
    }

    // ------------------------ "todo_tags" table methods ----------------//

    /**
     * Creating todo_tag
     */
    public long createTodoTag(long todo_id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_ID, todo_id);
        values.put(KEY_TAG_ID, tag_id);
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_TODO_TAG, null, values);

        return id;
    }

    /**
     * Updating a todo tag
     */
    public int updateNoteTag(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_ID, tag_id);

        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Deleting a todo tag
     */
    public void deleteToDoTag(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}