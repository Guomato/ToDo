package com.guoyonghui.todo.addedittask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.guoyonghui.todo.R;
import com.guoyonghui.todo.alarm.AlarmReceiver;
import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.util.CalendarFormatHelper;

import java.util.Calendar;
import java.util.Date;

public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private AddEditTaskContract.Presenter mPresenter;

    private String mTaskId;

    private EditText mTitleEditText;

    private EditText mDescriptionEditText;

    private Button mAlarmButton;

    private Calendar mCurrentCalendar;

    private Calendar mAlarmCalender;

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentCalendar = Calendar.getInstance();
        mAlarmCalender = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_edit_task, container, false);

        mTitleEditText = (EditText) rootView.findViewById(R.id.add_edit_task_title);

        mDescriptionEditText = (EditText) rootView.findViewById(R.id.add_edit_task_description);

        mAlarmButton = (Button) rootView.findViewById(R.id.alarm_button);
        mAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentCalendar.setTime(new Date());
                showDatePicker();
            }
        });

        FloatingActionButton doneAddEditButton = (FloatingActionButton) getActivity().findViewById(R.id.done_fab);
        doneAddEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitleEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                String alarm = mAlarmButton.getText().toString();
                mPresenter.finishAddEditTask(title, description, alarm);
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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mAlarmCalender.set(Calendar.YEAR, year);
        mAlarmCalender.set(Calendar.MONTH, monthOfYear);
        mAlarmCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        showTimePicker();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mAlarmCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mAlarmCalender.set(Calendar.MINUTE, minute);

        mAlarmButton.setText(CalendarFormatHelper.format(mAlarmCalender));
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
    public void showAlarm(String alarm) {
        if (!(alarm == null || alarm.isEmpty())) {
            mAlarmButton.setText(alarm);
        }
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
    public void showAlarmIllegalError() {
        showMessage(R.string.tip_alarm_illegal_error);
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
    public void setAlarm(Task task) {
        AlarmReceiver.setAlarm(getActivity(), task);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, mCurrentCalendar.get(Calendar.YEAR), mCurrentCalendar.get(Calendar.MONTH), mCurrentCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, mCurrentCalendar.get(Calendar.HOUR_OF_DAY), mCurrentCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void showMessage(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showMessage(int msgResId) {
        Snackbar.make(getView(), msgResId, Snackbar.LENGTH_SHORT).show();
    }

}
