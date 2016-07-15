package com.guoyonghui.todo.taskdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.guoyonghui.todo.R;
import com.guoyonghui.todo.BaseApplication;

public class TaskDetailActivity extends AppCompatActivity {

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

        new TaskDetailPresenter(taskId, ((BaseApplication) getApplication()).getTasksRepository(), taskDetailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
