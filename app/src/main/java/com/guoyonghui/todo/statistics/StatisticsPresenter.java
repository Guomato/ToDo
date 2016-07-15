package com.guoyonghui.todo.statistics;

import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TasksDataSource;

import java.util.Date;
import java.util.List;

public class StatisticsPresenter implements StatisticsContract.Presenter {

    private TasksDataSource mTasksRepository;

    private StatisticsContract.View mStatisticsView;

    public StatisticsPresenter(TasksDataSource tasksRepository, StatisticsContract.View statisticsView) {
        mTasksRepository = tasksRepository;
        mStatisticsView = statisticsView;

        mStatisticsView.setPresenter(this);
    }

    @Override
    public void start() {
        mTasksRepository.loadTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                int completed = 0;
                int active = 0;

                for(Task task: tasks) {
                    if(task.isCompleted()) {
                        completed++;
                    } else {
                        active++;
                    }
                }

                showStatistics(completed, active);
            }

            @Override
            public void onDataNotAvailable() {
                showStatistics(0, 0);
            }
        });
    }

    private void showStatistics(int completed, int active) {
        mStatisticsView.showStatistics(completed, active);
    }
}
