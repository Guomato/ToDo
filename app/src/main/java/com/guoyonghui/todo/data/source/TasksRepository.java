package com.guoyonghui.todo.data.source;

import com.guoyonghui.todo.data.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private TasksDataSource mTasksLocalDataSource;

    private Map<String, Task> mCachedTasks;

    private Set<TasksRepositoryObserver> mObservers = new HashSet<>();

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
    public List<Task> loadTasks() {
        List<Task> tasks;

        if(cachedTasksAvailable()) {
            return getCachedTasks();
        }

        tasks = mTasksLocalDataSource.loadTasks();
        refreshCache(tasks);

        return tasks;
    }

    @Override
    public Task getTask(String taskId) {
        Task cachedTask = mCachedTasks.get(taskId);
        if(cachedTask != null){
            return cachedTask;
        }

        return mTasksLocalDataSource.getTask(taskId);
    }

    @Override
    public void saveTask(Task task) {
        mTasksLocalDataSource.saveTask(task);

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getID(), task);

        notifyObservers();
    }

    @Override
    public void updateTask(Task task) {
        mTasksLocalDataSource.updateTask(task);

        mCachedTasks.put(task.getID(), task);

        notifyObservers();
    }

    @Override
    public void completeTask(Task task) {
        mTasksLocalDataSource.completeTask(task);

        task.setCompleted(true);
        mCachedTasks.put(task.getID(), task);

        notifyObservers();
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

        notifyObservers();
    }

    @Override
    public void activeTask(String taskId) {
        activeTask(getTaskWithId(taskId));
    }

    @Override
    public void deleteTask(String taskId) {
        mTasksLocalDataSource.deleteTask(taskId);

        mCachedTasks.remove(taskId);

        notifyObservers();
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

        notifyObservers();
    }

    @Override
    public void deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();

        notifyObservers();
    }

    public boolean cachedTasksAvailable() {
        return mCachedTasks != null;
    }

    public List<Task> getCachedTasks() {
        return new ArrayList<>(mCachedTasks.values());
    }

    public Task getCachedTask(String taskId) {
        return mCachedTasks.get(taskId);
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

    interface TasksRepositoryObserver {

        void onDataChanged();

    }

    public void registerTasksRepositoryObserver(TasksRepositoryObserver observer) {
        mObservers.add(observer);
    }

    public void unregisterTasksRepositoryObserver(TasksRepositoryObserver observer) {
        if(mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyObservers() {
        for(TasksRepositoryObserver observer: mObservers) {
            observer.onDataChanged();
        }
    }

}
