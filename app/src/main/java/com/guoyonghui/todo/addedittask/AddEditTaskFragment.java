package com.guoyonghui.todo.addedittask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.guoyonghui.todo.R;
import com.guoyonghui.todo.base.BaseFragment;
import com.guoyonghui.todo.base.BasePresenter;

public class AddEditTaskFragment extends BaseFragment implements AddEditTaskContract.View {

    private AddEditTaskContract.Presenter mPresenter;

    private String mTaskId;

    private EditText mTitleEditText;

    private EditText mDescriptionEditText;

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_edit_task, container, false);

        mTitleEditText = (EditText) rootView.findViewById(R.id.add_edit_task_title);

        mDescriptionEditText = (EditText) rootView.findViewById(R.id.add_edit_task_description);

        FloatingActionButton doneAddEditButton = (FloatingActionButton) getActivity().findViewById(R.id.done_fab);
        doneAddEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitleEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                mPresenter.finishAddEditTask(title, description);
            }
        });

        return rootView;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(AddEditTaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTitle(String title) {
        mTitleEditText.setText(title);
    }

    @Override
    public void showDescription(String description) {
        mDescriptionEditText.setText(description);
    }

    @Override
    public void showNoTask() {
        showMessage(R.string.tip_no_task);
    }

    @Override
    public void showEmptyTaskError() {
        showMessage(R.string.tip_empty_task_error);
    }

    @Override
    public void showTasksListUI() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

}
