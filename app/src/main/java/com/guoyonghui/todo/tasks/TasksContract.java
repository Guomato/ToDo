package com.guoyonghui.todo.tasks;

import com.guoyonghui.todo.base.BasePresenter;
import com.guoyonghui.todo.base.BaseView;
import com.guoyonghui.todo.data.Task;

import java.util.List;

public interface TasksContract {

    interface Presenter extends BasePresenter {

        void loadTasks();

        void completeTask(Task task);

        void activeTask(Task task);

        void deleteCompletedTasks();

        void deleteAllTasks();

        void openAddTaskUI();

        TasksFilterType getFilteringLabel();

        void setFilteringLabel(TasksFilterType filterType);

        void openTaskDetailUI(Task task);

        void openFilteringPopupMenu();

        void result(int requestCode, int resultCode);

    }

    interface View extends BaseView<Presenter> {

        void showTasks(List<Task> tasks);

        void showNoTasks();

        void showFilteringLabelAll();

        void showFilteringLabelCompleted();

        void showFilteringLabelActive();

        void showTaskMarkedCompleted();

        void showTaskMarkedActive();

        void showTaskDetailUI(String taskId);

        void showAddTaskUI();

        void showFilteringPopupMenu();

        void showSuccessfullyAddTask();

        void showLoadingIndicator(boolean show);

    }

}
