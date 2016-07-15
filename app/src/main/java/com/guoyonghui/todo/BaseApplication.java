package com.guoyonghui.todo;

import android.app.Application;
import android.content.SharedPreferences;

import com.guoyonghui.todo.data.source.TasksRepository;
import com.guoyonghui.todo.data.source.local.TasksLocalDataSource;

public class BaseApplication extends Application {

    private static final String PREFERENCE_FIRST_LOAD = "com.guoyonghui.todo.PREFERENCE_FIRST_LOAD";

    public TasksRepository getTasksRepository() {
        return TasksRepository.getInstance(TasksLocalDataSource.getInstance(getApplicationContext()));
    }

    public boolean isFirstLoad() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("APP_FIRST_LOAD", 0);
        boolean isFirstLoad = preferences.getBoolean(PREFERENCE_FIRST_LOAD, true);
        if(isFirstLoad) {
            preferences.edit().putBoolean(PREFERENCE_FIRST_LOAD, false).apply();
            return true;
        } else {
            return false;
        }
    }
}
