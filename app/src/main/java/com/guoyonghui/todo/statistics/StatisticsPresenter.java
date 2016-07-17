package com.guoyonghui.todo.statistics;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TasksDataSource;
import com.guoyonghui.todo.data.source.TasksLoader;

import java.util.Date;
import java.util.List;

public class StatisticsPresenter implements StatisticsContract.Presenter, LoaderManager.LoaderCallbacks<List<Task>> {

    private static final int TASKS_QUERY = 3;

    private TasksDataSource mTasksRepository;

    private StatisticsContract.View mStatisticsView;

    private LoaderManager mLoaderManager;

    private TasksLoader mTasksLoader;


    public StatisticsPresenter(TasksDataSource tasksRepository, StatisticsContract.View statisticsView, LoaderManager loaderManager, TasksLoader tasksLoader) {
        mTasksRepository = tasksRepository;
        mStatisticsView = statisticsView;
        mLoaderManager = loaderManager;
        mTasksLoader = tasksLoader;

        mStatisticsView.setPresenter(this);
    }

    @Override
    public Loader<List<Task>> onCreateLoader(int id, Bundle args) {
        mStatisticsView.showLoadingIndicator(true);

        return mTasksLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Task>> loader, List<Task> data) {
        mStatisticsView.showLoadingIndicator(false);

        int completed = 0;
        int active = 0;
        for(Task task: data) {
            if(task.isCompleted()) {
                completed++;
            } else {
                active++;
            }
        }

        showStatistics(completed, active);
    }

    @Override
    public void onLoaderReset(Loader<List<Task>> loader) {

    }

    @Override
    public void start() {
        mLoaderManager.initLoader(TASKS_QUERY, null, this);
    }

    private void showStatistics(int completed, int active) {
        mStatisticsView.showStatistics(completed, active);
    }
}
