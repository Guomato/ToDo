package com.guoyonghui.todo.data.source;

import com.guoyonghui.todo.data.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private TasksDataSource mTasksLocalDataSource;

    private Map<String, Task> mCachedTasks;

    private TasksRepository(TasksDataSource tasksLocalDataSource) {
        mTasksLocalDataSource = tasksLocalDataSource;
    }

    public static TasksRepository getInstance(TasksDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void loadTasks(final LoadTasksCallback callback) {
        if (mCachedTasks != null) {
            callback.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            return;
        }

        mTasksLocalDataSource.loadTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                callback.onTasksLoaded(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getTask(String taskId, GetTaskCallback callback) {
        if (mCachedTasks != null) {
            callback.onTaskLoaded(mCachedTasks.get(taskId));
            return;
        }

        mTasksLocalDataSource.getTask(taskId, callback);
    }

    @Override
    public void saveTask(Task task) {
        mTasksLocalDataSource.saveTask(task);

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getID(), task);
    }

    @Override
    public void updateTask(Task task) {
        mTasksLocalDataSource.updateTask(task);

        mCachedTasks.put(task.getID(), task);
    }

    @Override
    public void completeTask(Task task) {
        mTasksLocalDataSource.completeTask(task);

        task.setCompleted(true);
        mCachedTasks.put(task.getID(), task);
    }

    @Override
    public void completeTask(String taskId) {
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activeTask(Task task) {
        mTasksLocalDataSource.activeTask(task);

        task.setCompleted(false);
        mCachedTasks.put(task.getID(), task);
    }

    @Override
    public void activeTask(String taskId) {
        activeTask(getTaskWithId(taskId));
    }

    @Override
    public void deleteTask(String taskId) {
        mTasksLocalDataSource.deleteTask(taskId);

        mCachedTasks.remove(taskId);
    }

    @Override
    public void deleteCompletedTasks() {
        mTasksLocalDataSource.deleteCompletedTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Task>> iterator = mCachedTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Task> entry = iterator.next();
            if (entry.getValue().isCompleted()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }

        mCachedTasks.clear();
        for (Task task : tasks) {
            mCachedTasks.put(task.getID(), task);
        }
    }

    private Task getTaskWithId(String taskId) {
        return (mCachedTasks == null) ? null : mCachedTasks.get(taskId);
    }
}
