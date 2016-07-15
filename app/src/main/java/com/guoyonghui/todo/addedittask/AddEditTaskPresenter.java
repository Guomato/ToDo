package com.guoyonghui.todo.addedittask;

import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TasksDataSource;
import com.guoyonghui.todo.data.source.TasksRepository;

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter {

    private String mTaskId;

    private TasksRepository mTasksRepository;

    private AddEditTaskContract.View mAddEditTaskView;

    public AddEditTaskPresenter(String taskId, TasksRepository tasksRepository, AddEditTaskContract.View addEditTaskView) {
        mTaskId = taskId;
        mTasksRepository = tasksRepository;
        mAddEditTaskView = addEditTaskView;

        mAddEditTaskView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewTask()) {
            getTask();
        }
    }

    @Override
    public void finishAddEditTask(String title, String description) {
        Task task = new Task(title, description);
        if(task.isEmpty()) {
            mAddEditTaskView.showEmptyTaskError();
            return;
        }

        if(isNewTask()) {
            createTask(task);
        } else {
            updateTask(task);
        }
    }

    private void updateTask(Task task) {
        task.setID(mTaskId);
        mTasksRepository.updateTask(task);
        mAddEditTaskView.showTasksListUI();
    }

    private void createTask(Task task) {
        mTasksRepository.saveTask(task);
        mAddEditTaskView.showTasksListUI();
    }

    private boolean isNewTask() {
        return mTaskId == null || mTaskId.isEmpty();
    }
    
    private void getTask() {
        if(isNewTask()) {
            mAddEditTaskView.showNoTask();
            return;
        }

        mTasksRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if(!mAddEditTaskView.isActive()) {
                    return;
                }

                showTask(task);
            }

            @Override
            public void onDataNotAvailable() {
                if(!mAddEditTaskView.isActive()) {
                    return;
                }
                mAddEditTaskView.showNoTask();
            }
        });
    }

    private void showTask(Task task) {
        mAddEditTaskView.showTitle(task.getTitle());
        mAddEditTaskView.showDescription(task.getDescription());
    }
}
