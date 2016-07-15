package com.guoyonghui.todo.statistics;

import com.guoyonghui.todo.BasePresenter;
import com.guoyonghui.todo.BaseView;

public interface StatisticsContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void showStatistics(int completed, int active);

    }

}
