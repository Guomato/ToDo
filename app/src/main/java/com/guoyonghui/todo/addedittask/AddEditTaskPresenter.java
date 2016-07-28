package com.guoyonghui.todo.addedittask;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TaskLoader;
import com.guoyonghui.todo.data.source.TasksRepository;

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter, LoaderManager.LoaderCallbacks<Task> {

    private static final int TASK_QUERY = 1;

    private String mTaskId;

    private TasksRepository mTasksRepository;

    private AddEditTaskContract.View mAddEditTaskView;

    private LoaderManager mLoaderManager;

    private TaskLoader mTaskLoader;

    public AddEditTaskPresenter(String taskId, TasksRepository tasksRepository, AddEditTaskContract.View addEditTaskView, LoaderManager loaderManager, TaskLoader taskLoader) {
        mTaskId = taskId;
        mTasksRepository = tasksRepository;
        mAddEditTaskView = addEditTaskView;
        mLoaderManager = loaderManager;
        mTaskLoader = taskLoader;

        mAddEditTaskView.setPresenter(this);
    }

    @Override
    public Loader<Task> onCreateLoader(int id, Bundle args) {
        return mTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<Task> loader, Task data) {
        if (data != null) {
            showTask(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Task> loader) {

    }

    @Override
    public void start() {
        if (!isNewTask()) {
            mLoaderManager.initLoader(TASK_QUERY, null, this);
        }
    }

    @Override
    public void finishAddEditTask(String title, String description) {
        Task task = new Task(title, description);
        if (task.isEmpty()) {
            mAddEditTaskView.showEmptyTaskError();
            return;
        }

        if (isNewTask()) {
            createTask(task);
        } else {
            updateTask(task);
        }

        mAddEditTaskView.showTasksListUI();
    }

    private void updateTask(Task task) {
        task.setID(mTaskId);
        mTasksRepository.updateTask(task);
    }

    private void createTask(Task task) {
        mTasksRepository.saveTask(task);
    }

    private boolean isNewTask() {
        return mTaskId == null || mTaskId.isEmpty();
    }


    private void showTask(Task task) {
        mAddEditTaskView.showTitle(task.getTitle());
        mAddEditTaskView.showDescription(task.getDescription());
    }
}
