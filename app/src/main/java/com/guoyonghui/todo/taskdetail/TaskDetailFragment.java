package com.guoyonghui.todo.taskdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.guoyonghui.todo.R;
import com.guoyonghui.todo.addedittask.AddEditTaskActivity;

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View {

    private static final String ARGUMENT_EXTRA_ID = "com.guoyonghui.todo.taskdetail.ARGUMENT_EXTRA_ID";

    public static final int REQUEST_EDIT_TASK = 1;

    private TaskDetailContract.Presenter mPresenter;

    private String mTaskId;

    private CheckBox mCompleteCheckBox;

    private TextView mTitleTextView;

    private TextView mAlarmTextView;

    private TextView mDescriptionTextView;

    public static TaskDetailFragment newInstance(String taskId) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_EXTRA_ID, taskId);

        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mTaskId = getArguments().getString(ARGUMENT_EXTRA_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_detail, container, false);

        mCompleteCheckBox = (CheckBox) rootView.findViewById(R.id.task_detail_complete);

        mTitleTextView = (TextView) rootView.findViewById(R.id.task_detail_title);

        mAlarmTextView = (TextView) rootView.findViewById(R.id.task_detail_alarm);

        mDescriptionTextView = (TextView) rootView.findViewById(R.id.task_detail_description);

        FloatingActionButton editTaskButton = (FloatingActionButton) getActivity().findViewById(R.id.edit_fab);
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openEditTaskUI(mTaskId);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_task:
                mPresenter.deleteTask(mTaskId);
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNoTask() {
        showMessage(R.string.tip_no_task);
    }

    @Override
    public void showTitle(String title) {
        mTitleTextView.setVisibility(View.VISIBLE);
        mTitleTextView.setText(title);
    }

    @Override
    public void hideTitle() {
        mTitleTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAlarm(String alarm) {
        mAlarmTextView.setVisibility(View.VISIBLE);
        mAlarmTextView.setText(alarm);
    }

    @Override
    public void hideAlarm() {
        mAlarmTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showDescription(String description) {
        mDescriptionTextView.setVisibility(View.VISIBLE);
        mDescriptionTextView.setText(description);
    }

    @Override
    public void hideDescription() {
        mDescriptionTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showCompleteStatus(boolean completed) {
        mCompleteCheckBox.setChecked(completed);
        mCompleteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCompleteCheckBox.isChecked()) {
                    mPresenter.completeTask(mTaskId);
                } else {
                    mPresenter.activeTask(mTaskId);
                }
            }
        });
//        mCompleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b) {
//                    mPresenter.completeTask(mTaskId);
//                } else {
//                    mPresenter.activeTask(mTaskId);
//                }
//            }
//        });
    }

    @Override
    public void showTaskMarkedCompleted() {
        showMessage(R.string.tip_task_marked_completed);
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(R.string.tip_task_marked_active);
    }

    @Override
    public void showTaskDeleted() {
        getActivity().finish();
    }

    @Override
    public void showEditTaskUI(String taskId) {
        Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskActivity.EXTRA_TASK_ID, mTaskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

    @Override
    public void showSuccessfullyEditTask() {
        showMessage(R.string.tip_task_edit_success);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void showMessage(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showMessage(int msgResId) {
        Snackbar.make(getView(), msgResId, Snackbar.LENGTH_SHORT).show();
    }
}
