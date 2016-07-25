package com.guoyonghui.todo.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.guoyonghui.todo.base.BaseActivity;
import com.guoyonghui.todo.R;
import com.guoyonghui.todo.data.source.TasksLoader;
import com.guoyonghui.todo.data.source.TasksRepository;
import com.guoyonghui.todo.statistics.StatisticsActivity;

public class TasksActivity extends BaseActivity {

    private static final String CURRENT_FILTERING_KEY = "com.guoyonghui.todo.tasks.CURRENT_FILTERING_KEY";

    private DrawerLayout mDrawerLayout;

    private TasksContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tasks_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.tasks_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.tasks_nav_view);
        if (navigationView != null) {
            setupNavigationView(navigationView);
        }

        TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.tasks_fragment_container);
        if (tasksFragment == null) {
            tasksFragment = TasksFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tasks_fragment_container, tasksFragment)
                    .commit();
        }

        TasksRepository tasksRepository = getTasksRepository();
        TasksLoader tasksLoader = new TasksLoader(this, tasksRepository);

        mPresenter = new TasksPresenter(tasksRepository, tasksFragment, getSupportLoaderManager(), tasksLoader);

        if (savedInstanceState != null) {
            TasksFilterType filterType = (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mPresenter.setFilteringLabel(filterType);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mPresenter.getFilteringLabel());

        super.onSaveInstanceState(outState);
    }

    private void setupNavigationView(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.nav_item_todo);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_item_todo:
                        break;
                    case R.id.nav_item_statistics:
                        Intent intent = new Intent(TasksActivity.this, StatisticsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }

                item.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

}
