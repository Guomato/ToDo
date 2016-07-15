package com.guoyonghui.todo.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.guoyonghui.todo.R;
import com.guoyonghui.todo.addedittask.AddEditTaskActivity;
import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.taskdetail.TaskDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment implements TasksContract.View {

    public static final int REQUEST_ADD_TASK = 1;

    private TasksContract.Presenter mPresenter;

    private TextView mFilteringLabelTextView;

    private TasksAdapter mTasksAdapter;

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mTasksAdapter = new TasksAdapter(new ArrayList<Task>(), mTaskItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        ListView tasksListView = (ListView) rootView.findViewById(R.id.tasks_list);
        tasksListView.setAdapter(mTasksAdapter);

        mFilteringLabelTextView = (TextView) rootView.findViewById(R.id.filtering_label);

        FloatingActionButton addTaskButton = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openAddTaskUI();
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
        inflater.inflate(R.menu.menu_tasks, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filtering:
                mPresenter.openFilteringPopupMenu();
                return true;
            case R.id.action_delete_all:
                mPresenter.deleteAllTasks();
                return true;
            case R.id.action_delete_completed:
                mPresenter.deleteCompletedTasks();
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
    public void showTasks(List<Task> tasks) {
        mTasksAdapter.replaceData(tasks);
    }

    @Override
    public void showNoTasks() {
        showMessage(R.string.tip_no_task);
    }

    @Override
    public void showFilteringLabelAll() {
        mFilteringLabelTextView.setText(R.string.action_filtering_all);
    }

    @Override
    public void showFilteringLabelCompleted() {
        mFilteringLabelTextView.setText(R.string.action_filtering_completed);
    }

    @Override
    public void showFilteringLabelActive() {
        mFilteringLabelTextView.setText(R.string.action_filtering_active);
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
    public void showAddTaskUI() {
        Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
        startActivityForResult(intent, REQUEST_ADD_TASK);
    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showFilteringPopupMenu() {
        final PopupMenu popupMenu = new PopupMenu(getActivity(), getActivity().findViewById(R.id.action_filtering));
        popupMenu.inflate(R.menu.menu_filtering);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_filtering_all:
                        mPresenter.setFilteringLabel(TasksFilterType.ALL_TASKS);
                        break;
                    case R.id.action_filtering_completed:
                        mPresenter.setFilteringLabel(TasksFilterType.COMPLETED_TASKS);
                        break;
                    case R.id.action_filtering_active:
                        mPresenter.setFilteringLabel(TasksFilterType.ACTIVE_TASKS);
                        break;

                    default:
                        break;
                }

                mPresenter.loadTasks();
                return true;
            }
        });

        popupMenu.show();
    }

    @Override
    public void showTaskDetailUI(String taskId) {
        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    @Override
    public void showSuccessfullyAddTask() {
        showMessage(R.string.tip_task_add_success);
    }

    private void showMessage(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showMessage(int msgResId) {
        Snackbar.make(getView(), msgResId, Snackbar.LENGTH_SHORT).show();
    }

    private TaskItemListener mTaskItemListener = new TaskItemListener() {
        @Override
        public void onTaskClick(Task task) {
            mPresenter.openTaskDetailUI(task);
        }

        @Override
        public void onCompleteTaskClick(Task task) {
            mPresenter.completeTask(task);
        }

        @Override
        public void onActiveTaskClick(Task task) {
            mPresenter.activeTask(task);
        }
    };

    private class TasksAdapter extends BaseAdapter {

        private List<Task> mTasks;

        private TaskItemListener mTaskItemListener;

        public TasksAdapter(List<Task> tasks, TaskItemListener taskItemListener) {
            setData(tasks);
            mTaskItemListener = taskItemListener;
        }

        public void setData(List<Task> tasks) {
            mTasks = tasks;
        }

        public void replaceData(List<Task> tasks) {
            setData(tasks);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public Object getItem(int i) {
            return mTasks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_item, viewGroup, false);
            }

            final Task task = (Task) getItem(i);

            TextView titleTextView = (TextView) view.findViewById(R.id.title);
            titleTextView.setText(task.getTitleForList());

            CheckBox completeCheckBox = (CheckBox) view.findViewById(R.id.complete);
            completeCheckBox.setChecked(task.isCompleted());

            if (task.isCompleted()) {
                view.setBackgroundResource(R.drawable.list_completed_touch_feedback);
            } else {
                view.setBackgroundResource(R.drawable.touch_feedback);
            }

            completeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (task.isCompleted()) {
                        mTaskItemListener.onActiveTaskClick(task);
                    } else {
                        mTaskItemListener.onCompleteTaskClick(task);
                    }
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTaskItemListener.onTaskClick(task);
                }
            });

            return view;
        }
    }

    private interface TaskItemListener {

        void onTaskClick(Task task);

        void onCompleteTaskClick(Task task);

        void onActiveTaskClick(Task task);

    }
}
