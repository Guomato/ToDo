package com.guoyonghui.todo.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TasksDataSource;

import java.util.ArrayList;
import java.util.List;

public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE = null;

    private TasksDbHelper mDbHelper;

    private TasksLocalDataSource(Context context) {
        mDbHelper = new TasksDbHelper(context);
    }

    public static TasksLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public List<Task> loadTasks() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        List<Task> tasks = new ArrayList<>();

        String[] columns = {
                TaskEntry.COLUMN_NAME_TASK_ENTRY,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_ALARM,
                TaskEntry.COLUMN_NAME_COMPLETED
        };

        Cursor c = db.query(TaskEntry.TABLE_NAME, columns, null, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String taskId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TASK_ENTRY));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                String alarm = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ALARM));
                boolean completed = c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;

                Task task = new Task(taskId, title, description, alarm, completed);
                tasks.add(task);
            }
        }
        if (c != null) {
            c.close();
        }
        db.close();

        return tasks;
    }

    @Override
    public Task getTask(String taskId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Task task = null;

        String[] columns = {
                TaskEntry.COLUMN_NAME_TASK_ENTRY,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_ALARM,
                TaskEntry.COLUMN_NAME_COMPLETED
        };
        String selection = TaskEntry.COLUMN_NAME_TASK_ENTRY + " like ?";
        String[] selectionArgs = {taskId};

        Cursor c = db.query(TaskEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
            String description = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
            String alarm = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ALARM));
            boolean completed = c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;

            task = new Task(taskId, title, description, alarm, completed);
        }
        if (c != null) {
            c.close();
        }
        db.close();

        return task;
    }

    @Override
    public void saveTask(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TASK_ENTRY, task.getID());
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_ALARM, task.getAlarm());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());

        db.insert(TaskEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void updateTask(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_ALARM, task.getAlarm());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());

        String whereClause = TaskEntry.COLUMN_NAME_TASK_ENTRY + " like ?";
        String[] whereArgs = {task.getID()};

        db.update(TaskEntry.TABLE_NAME, values, whereClause, whereArgs);

        db.close();
    }

    @Override
    public void completeTask(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

        String whereClause = TaskEntry.COLUMN_NAME_TASK_ENTRY + " like ?";
        String[] whereArgs = {task.getID()};

        db.update(TaskEntry.TABLE_NAME, values, whereClause, whereArgs);

        db.close();
    }

    @Override
    public void completeTask(String taskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

        String whereClause = TaskEntry.COLUMN_NAME_TASK_ENTRY + " like ?";
        String[] whereArgs = {taskId};

        db.update(TaskEntry.TABLE_NAME, values, whereClause, whereArgs);

        db.close();
    }

    @Override
    public void activeTask(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

        String whereClause = TaskEntry.COLUMN_NAME_TASK_ENTRY + " like ?";
        String[] whereArgs = {task.getID()};

        db.update(TaskEntry.TABLE_NAME, values, whereClause, whereArgs);

        db.close();
    }

    @Override
    public void activeTask(String taskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

        String whereClause = TaskEntry.COLUMN_NAME_TASK_ENTRY + " like ?";
        String[] whereArgs = {taskId};

        db.update(TaskEntry.TABLE_NAME, values, whereClause, whereArgs);

        db.close();
    }

    @Override
    public void deleteTask(String taskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String whereClause = TaskEntry.COLUMN_NAME_TASK_ENTRY + " like ?";
        String[] whereArgs = {taskId};

        db.delete(TaskEntry.TABLE_NAME, whereClause, whereArgs);

        db.close();
    }

    @Override
    public void deleteCompletedTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String whereClause = TaskEntry.COLUMN_NAME_COMPLETED + " like ?";
        String[] whereArgs = {"1"};

        db.delete(TaskEntry.TABLE_NAME, whereClause, whereArgs);

        db.close();
    }

    @Override
    public void deleteAllTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(TaskEntry.TABLE_NAME, null, null);

        db.close();
    }
}
