package com.guoyonghui.todo.addedittask;

import com.guoyonghui.todo.BasePresenter;
import com.guoyonghui.todo.BaseView;

public interface AddEditTaskContract {

    interface Presenter extends BasePresenter {

        void finishAddEditTask(String title, String description, String alarm);

    }

    interface View extends BaseView<Presenter> {

        void showTitle(String title);

        void showDescription(String description);

        void showAlarm(String alarm);

        void showNoTask();

        void showAlarmIllegalError();

        void showEmptyTaskError();

        void showTasksListUI();

        boolean isActive();

    }

}
