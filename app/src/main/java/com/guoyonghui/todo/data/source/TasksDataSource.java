package com.guoyonghui.todo.data.source;

import com.guoyonghui.todo.data.Task;

import java.util.List;

public interface TasksDataSource {

    interface LoadTasksCallback {

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();

    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();

    }

    void loadTasks(LoadTasksCallback callback);

    void getTask(String taskId, GetTaskCallback callback);

    void saveTask(Task task);

    void updateTask(Task task);

    void completeTask(Task task);

    void completeTask(String taskId);

    void activeTask(Task task);

    void activeTask(String taskId);

    void deleteTask(String taskId);

    void deleteCompletedTasks();

    void deleteAllTasks();

}
