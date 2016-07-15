package com.guoyonghui.todo.taskdetail;

import android.app.Activity;

import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TasksDataSource;

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private String mTaskId;

    private TasksDataSource mTasksRepository;

    private TaskDetailContract.View mTaskDetailView;

    public TaskDetailPresenter(String taskId, TasksDataSource tasksRepository, TaskDetailContract.View taskDetailView) {
        mTaskId = taskId;
        mTasksRepository = tasksRepository;
        mTaskDetailView = taskDetailView;

        mTaskDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        getTask();
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
        if(requestCode == TaskDetailFragment.REQUEST_EDIT_TASK) {
            if(resultCode == Activity.RESULT_OK) {
                mTaskDetailView.showSuccessfullyEditTask();
            }
        }
    }

    private void getTask() {
        if(mTaskId == null || mTaskId.isEmpty()) {
            mTaskDetailView.showNoTask();
            return;
        }

        mTasksRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if(!mTaskDetailView.isActive()) {
                    return;
                }

                showTask(task);
            }

            @Override
            public void onDataNotAvailable() {
                if(!mTaskDetailView.isActive()) {
                    return;
                }
                mTaskDetailView.showNoTask();
            }
        });
    }

    private void showTask(Task task) {
        String title = task.getTitle();
        String description = task.getDescription();

        if(title == null || title.isEmpty()) {
            mTaskDetailView.hideTitle();
        } else {
            mTaskDetailView.showTitle(title);
        }

        if (description == null || description.isEmpty()) {
            mTaskDetailView.hideDescription();
        } else {
            mTaskDetailView.showDescription(description);
        }

        mTaskDetailView.showCompleteStatus(task.isCompleted());
    }
}
