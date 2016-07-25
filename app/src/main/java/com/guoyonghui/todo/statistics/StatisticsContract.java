package com.guoyonghui.todo.statistics;

import com.guoyonghui.todo.base.BasePresenter;
import com.guoyonghui.todo.base.BaseView;

public interface StatisticsContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void showStatistics(int completed, int active);

        void showLoadingIndicator(boolean show);

    }

}
