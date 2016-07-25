package com.guoyonghui.todo.base;

import android.support.v7.app.AppCompatActivity;

import com.guoyonghui.todo.data.source.TasksRepository;
import com.guoyonghui.todo.data.source.local.TasksLocalDataSource;

public abstract class BaseActivity extends AppCompatActivity {

    public TasksRepository getTasksRepository() {
        return TasksRepository.getInstance(TasksLocalDataSource.getInstance(getApplicationContext()));
    }

}
