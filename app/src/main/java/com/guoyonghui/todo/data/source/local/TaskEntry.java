package com.guoyonghui.todo.data.source.local;

import android.provider.BaseColumns;

public final class TaskEntry implements BaseColumns {

    public static final String TABLE_NAME = "task";
    public static final String COLUMN_NAME_TASK_ENTRY = "task_entry";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_COMPLETED = "completed";
    public static final String COLUMN_NAME_ALARM = "alarm";

}
