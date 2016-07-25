package com.guoyonghui.todo.addedittask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.guoyonghui.BaseActivity;
import com.guoyonghui.todo.R;
import com.guoyonghui.todo.data.source.TaskLoader;
import com.guoyonghui.todo.data.source.TasksRepository;

public class AddEditTaskActivity extends BaseActivity {

    public static final String EXTRA_TASK_ID = "com.guoyonghui.todo.addedittask.EXTRA_TASK_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        String taskId = null;
        if (getIntent().hasExtra(EXTRA_TASK_ID)) {
            taskId = getIntent().getStringExtra(EXTRA_TASK_ID);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_edit_task_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(taskId == null ? R.string.title_new_todo : R.string.title_edit_todo);
        }


        AddEditTaskFragment addEditTaskFragment = (AddEditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.add_edit_task_fragment_container);
        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditTaskFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.add_edit_task_fragment_container, addEditTaskFragment)
                    .commit();
        }

        TasksRepository tasksRepository = getTasksRepository();
        TaskLoader taskLoader = new TaskLoader(this, tasksRepository, taskId);
        new AddEditTaskPresenter(taskId, tasksRepository, addEditTaskFragment, getSupportLoaderManager(), taskLoader);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
