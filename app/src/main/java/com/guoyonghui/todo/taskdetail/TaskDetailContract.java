package com.guoyonghui.todo.taskdetail;

import com.guoyonghui.todo.BasePresenter;
import com.guoyonghui.todo.BaseView;
import com.guoyonghui.todo.data.Task;

public interface TaskDetailContract {

    interface Presenter extends BasePresenter {

        void completeTask(String taskId);

        void activeTask(String taskId);

        void deleteTask(String taskId);

        void openEditTaskUI(String taskId);

        void result(int requestCode, int resultCode);

    }

    interface View extends BaseView<Presenter> {

        void showNoTask();

        void showTitle(String title);

        void hideTitle();

        void showAlarm(String alarm);

        void hideAlarm();

        void showDescription(String description);

        void hideDescription();

        void showCompleteStatus(boolean completed);

        void showTaskMarkedCompleted();

        void showTaskMarkedActive();

        void showTaskDeleted();

        void showEditTaskUI(String taskId);

        void showSuccessfullyEditTask();

        boolean isActive();

    }

}
