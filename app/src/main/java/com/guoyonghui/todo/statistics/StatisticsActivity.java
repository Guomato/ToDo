package com.guoyonghui.todo.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.guoyonghui.BaseActivity;
import com.guoyonghui.todo.R;
import com.guoyonghui.todo.data.source.TasksLoader;
import com.guoyonghui.todo.data.source.TasksRepository;
import com.guoyonghui.todo.tasks.TasksActivity;

public class StatisticsActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.statistics_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setTitle(R.string.nav_item_statistics);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.statistics_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.statistics_nav_view);
        if (navigationView != null) {
            setupNavigationView(navigationView);
        }

        StatisticsFragment statisticsFragment = (StatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.statistics_fragment_container);
        if (statisticsFragment == null) {
            statisticsFragment = StatisticsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.statistics_fragment_container, statisticsFragment)
                    .commit();
        }

        TasksRepository tasksRepository = getTasksRepository();
        TasksLoader tasksLoader = new TasksLoader(this, tasksRepository);
        new StatisticsPresenter(tasksRepository, statisticsFragment, getSupportLoaderManager(), tasksLoader);
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

    private void setupNavigationView(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.nav_item_statistics);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_item_todo:
                        Intent intent = new Intent(StatisticsActivity.this, TasksActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case R.id.nav_item_statistics:
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
