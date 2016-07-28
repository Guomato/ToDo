package com.guoyonghui.todo.addedittask;

import com.guoyonghui.todo.base.BasePresenter;
import com.guoyonghui.todo.base.BaseView;
import com.guoyonghui.todo.data.Task;

public interface AddEditTaskContract {

    interface Presenter extends BasePresenter {

        void finishAddEditTask(String title, String description);

    }

    interface View extends BaseView<Presenter> {

        void showTitle(String title);

        void showDescription(String description);

        void showNoTask();

        void showEmptyTaskError();

        void showTasksListUI();

        boolean isActive();

    }

}
