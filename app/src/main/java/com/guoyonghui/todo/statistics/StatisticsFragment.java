package com.guoyonghui.todo.statistics;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoyonghui.todo.R;
import com.guoyonghui.todo.view.StatisticsView;

public class StatisticsFragment extends Fragment implements StatisticsContract.View {

    private StatisticsContract.Presenter mPresenter;

    private StatisticsView mStatisticsView;

    private ProgressDialog mProgressDialog;

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.loading_indicator));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        mStatisticsView = (StatisticsView) rootView.findViewById(R.id.statistics_view);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void setPresenter(StatisticsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        if (show) {
            mProgressDialog.show();
        } else {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showStatistics(int completed, int active) {
        StatisticsView.Category category = new StatisticsView.Category(R.color.column_color_completed, R.color.column_color_active);
        category.add(getString(R.string.info_statistics_completed), completed);
        category.add(getString(R.string.info_statistics_active), active);

        mStatisticsView.setCategory(category);
    }
}
