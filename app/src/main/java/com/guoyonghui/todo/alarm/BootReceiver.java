package com.guoyonghui.todo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TasksRepository;
import com.guoyonghui.todo.data.source.local.TasksLocalDataSource;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            TasksRepository tasksRepository = TasksRepository.getInstance(TasksLocalDataSource.getInstance(context));
            List<Task> tasks = tasksRepository.loadTasks();
            for (Task task : tasks) {
                if (task.isActive()) {
                    AlarmReceiver.setAlarm(context, task);
                }
            }
        }
    }
}