package com.guoyonghui.todo.tasks;

import android.app.Activity;

import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TasksDataSource;
import com.guoyonghui.todo.data.source.TasksRepository;

import java.util.ArrayList;
import java.util.List;

public class TasksPresenter implements TasksContract.Presenter {

    private TasksDataSource mTasksRepository;

    private TasksContract.View mTasksView;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    public TasksPresenter(TasksRepository tasksRepository, TasksContract.View tasksView) {
        mTasksRepository = tasksRepository;
        mTasksView = tasksView;

        mTasksView.setPresenter(this);
    }

    @Override
    public void loadTasks() {
        mTasksRepository.loadTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                System.out.println(tasks.size());

                List<Task> tasksToShow = new ArrayList<>();

                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;

                        default:
                            break;
                    }
                }

                mTasksView.showTasks(tasksToShow);
                showFilteringLabel();
            }

            @Override
            public void onDataNotAvailable() {
                mTasksView.showNoTasks();
                showFilteringLabel();
            }
        });
    }

    @Override
    public void completeTask(Task task) {
        mTasksRepository.completeTask(task);
        mTasksView.showTaskMarkedCompleted();
        loadTasks();
    }

    @Override
    public void activeTask(Task task) {
        mTasksRepository.activeTask(task);
        mTasksView.showTaskMarkedActive();
        loadTasks();
    }

    @Override
    public void deleteCompletedTasks() {
        mTasksRepository.deleteCompletedTasks();
        loadTasks();
    }

    @Override
    public void deleteAllTasks() {
        mTasksRepository.deleteAllTasks();
        loadTasks();
    }

    @Override
    public void openAddTaskUI() {
        mTasksView.showAddTaskUI();
    }

    @Override
    public void start() {
        loadTasks();
    }

    @Override
    public TasksFilterType getFilteringLabel() {
        return mCurrentFiltering;
    }

    @Override
    public void setFilteringLabel(TasksFilterType filterType) {
        mCurrentFiltering = filterType;
    }

    @Override
    public void openTaskDetailUI(Task task) {
        mTasksView.showTaskDetailUI(task.getID());
    }

    @Override
    public void openFilteringPopupMenu() {
        mTasksView.showFilteringPopupMenu();
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if(requestCode == TasksFragment.REQUEST_ADD_TASK) {
            if(resultCode == Activity.RESULT_OK) {
                mTasksView.showSuccessfullyAddTask();
            }
        }
    }

    private void showFilteringLabel() {
        switch (mCurrentFiltering) {
            case ALL_TASKS:
                mTasksView.showFilteringLabelAll();
                break;
            case COMPLETED_TASKS:
                mTasksView.showFilteringLabelCompleted();
                break;
            case ACTIVE_TASKS:
                mTasksView.showFilteringLabelActive();
                break;

            default:
                break;
        }
    }
}
