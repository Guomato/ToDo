package com.guoyonghui.todo.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.guoyonghui.todo.data.Task;

public class TaskLoader extends AsyncTaskLoader<Task> implements TasksRepository.TasksRepositoryObserver{

    private TasksRepository mTasksRepository;

    private String mTaskId;

    public TaskLoader(Context context, TasksRepository tasksRepository, String taskId) {
        super(context);
        mTasksRepository = tasksRepository;
        mTaskId = taskId;
    }

    @Override
    public void deliverResult(Task data) {
        if(isReset()) {
            return;
        }

        if(isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if(mTasksRepository.cachedTasksAvailable()) {
            deliverResult(mTasksRepository.getCachedTask(mTaskId));
        }

        mTasksRepository.registerTasksRepositoryObserver(this);

        if(takeContentChanged() || !mTasksRepository.cachedTasksAvailable()) {
            forceLoad();
        }
    }

    @Override
    public Task loadInBackground() {
        return mTasksRepository.getTask(mTaskId);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mTasksRepository.unregisterTasksRepositoryObserver(this);
    }

    @Override
    public void onDataChanged() {
        if(isStarted()) {
            forceLoad();
        }
    }
}
