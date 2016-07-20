package com.guoyonghui.todo.taskdetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TaskLoader;
import com.guoyonghui.todo.data.source.TasksDataSource;

public class TaskDetailPresenter implements TaskDetailContract.Presenter, LoaderManager.LoaderCallbacks<Task> {

    private static final int TASK_QUERY = 2;

    private String mTaskId;

    private TasksDataSource mTasksRepository;

    private TaskDetailContract.View mTaskDetailView;

    private LoaderManager mLoaderManager;

    private TaskLoader mTaskLoader;

    public TaskDetailPresenter(String taskId, TasksDataSource tasksRepository, TaskDetailContract.View taskDetailView, LoaderManager loaderManager, TaskLoader taskLoader) {
        mTaskId = taskId;
        mTasksRepository = tasksRepository;
        mTaskDetailView = taskDetailView;
        mLoaderManager = loaderManager;
        mTaskLoader = taskLoader;

        mTaskDetailView.setPresenter(this);
    }

    @Override
    public Loader<Task> onCreateLoader(int id, Bundle args) {
        return mTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<Task> loader, Task data) {
        if (data != null) {
            showTask(data);
        } else {
            mTaskDetailView.showNoTask();
        }
    }

    @Override
    public void onLoaderReset(Loader<Task> loader) {

    }

    @Override
    public void start() {
        mLoaderManager.initLoader(TASK_QUERY, null, this);
    }

    @Override
    public void completeTask(String taskId) {
        mTasksRepository.completeTask(taskId);
        mTaskDetailView.showTaskMarkedCompleted();
    }

    @Override
    public void activeTask(String taskId) {
        mTasksRepository.activeTask(taskId);
        mTaskDetailView.showTaskMarkedActive();
    }

    @Override
    public void deleteTask(String taskId) {
        mTasksRepository.deleteTask(taskId);
        mTaskDetailView.showTaskDeleted();
    }

    @Override
    public void openEditTaskUI(String taskId) {
        mTaskDetailView.showEditTaskUI(taskId);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (requestCode == TaskDetailFragment.REQUEST_EDIT_TASK) {
            if (resultCode == Activity.RESULT_OK) {
                mTaskDetailView.showSuccessfullyEditTask();
            }
        }
    }

    private void showTask(Task task) {
        String title = task.getTitle();
        String description = task.getDescription();
        String alarm = task.getAlarm();

        if (title == null || title.isEmpty()) {
            mTaskDetailView.hideTitle();
        } else {
            mTaskDetailView.showTitle(title);
        }

        if (alarm == null || alarm.isEmpty()) {
            mTaskDetailView.hideAlarm();
        } else {
            mTaskDetailView.showAlarm(alarm);
        }

        if (description == null || description.isEmpty()) {
            mTaskDetailView.hideDescription();
        } else {
            mTaskDetailView.showDescription(description);
        }

        mTaskDetailView.showCompleteStatus(task.isCompleted());
    }
}
