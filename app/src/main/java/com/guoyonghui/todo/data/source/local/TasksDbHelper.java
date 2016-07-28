package com.guoyonghui.todo.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.guoyonghui.todo.data.source.local.TaskEntry;

public class TasksDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " text";

    private static final String BOOL_TYPE = " int";

    private static final String SEPERATOR = ", ";

    private static final String SQL_CREATE =
            "create table if not exists " + TaskEntry.TABLE_NAME + "(" +
                    TaskEntry.COLUMN_NAME_TASK_ENTRY + TEXT_TYPE + " primary key" + SEPERATOR +
                    TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + SEPERATOR +
                    TaskEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + SEPERATOR +
                    TaskEntry.COLUMN_NAME_DATE + TEXT_TYPE + SEPERATOR +
                    TaskEntry.COLUMN_NAME_COMPLETED + BOOL_TYPE + ")";

    public TasksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
