package com.guoyonghui.todo.taskdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.guoyonghui.todo.base.BaseActivity;
import com.guoyonghui.todo.R;
import com.guoyonghui.todo.data.source.TaskLoader;
import com.guoyonghui.todo.data.source.TasksRepository;

public class TaskDetailActivity extends BaseActivity {

    public static final String EXTRA_TASK_ID = "com.guoyonghui.todo.taskdetail.EXTRA_TASK_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.task_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.task_detail_fragment_container);
        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.task_detail_fragment_container, taskDetailFragment)
                    .commit();
        }

        TasksRepository tasksRepository = getTasksRepository();
        TaskLoader taskLoader = new TaskLoader(this, tasksRepository, taskId);
        new TaskDetailPresenter(taskId, tasksRepository, taskDetailFragment, getSupportLoaderManager(), taskLoader);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
