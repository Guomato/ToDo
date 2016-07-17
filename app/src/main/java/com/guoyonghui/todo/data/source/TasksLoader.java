package com.guoyonghui.todo.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.guoyonghui.todo.data.Task;

import java.util.List;

public class TasksLoader extends AsyncTaskLoader<List<Task>> implements TasksRepository.TasksRepositoryObserver {

    public static final String TAG = "TasksLoader";

    private TasksRepository mTasksRepository;

    public TasksLoader(Context context, TasksRepository tasksRepository) {
        super(context);
        mTasksRepository = tasksRepository;
    }

    @Override
    public void deliverResult(List<Task> data) {
        Log.d(TAG, "deliverResult");

        if(isReset()) {
            return;
        }

        if(isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, Thread.currentThread().getName());

        Log.d(TAG, "cache available: " + mTasksRepository.cachedTasksAvailable());

        Log.d(TAG, "onStartLoading");

        if(mTasksRepository.cachedTasksAvailable()) {
            deliverResult(mTasksRepository.getCachedTasks());
        }

        mTasksRepository.registerTasksRepositoryObserver(this);

        if(takeContentChanged() || !mTasksRepository.cachedTasksAvailable()) {
            forceLoad();
        }
    }

    @Override
    public List<Task> loadInBackground() {
        Log.d(TAG, "loadInBackground");

        return mTasksRepository.loadTasks();
    }

    @Override
    protected void onStopLoading() {
        Log.d(TAG, "onStopLoading");

        cancelLoad();
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset");

        mTasksRepository.unregisterTasksRepositoryObserver(this);
    }

    @Override
    public void onDataChanged() {
        Log.d(TAG, "onDataChanged");

        if(isStarted()) {
            forceLoad();
        }
    }
}
