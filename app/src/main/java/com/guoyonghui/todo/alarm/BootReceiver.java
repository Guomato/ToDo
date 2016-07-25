package com.guoyonghui.todo.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.guoyonghui.todo.R;
import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.data.source.TasksRepository;
import com.guoyonghui.todo.data.source.local.TasksLocalDataSource;
import com.guoyonghui.todo.taskdetail.TaskDetailActivity;
import com.guoyonghui.todo.tasks.TasksActivity;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            notify(context);

            TasksRepository tasksRepository = TasksRepository.getInstance(TasksLocalDataSource.getInstance(context));
            List<Task> tasks = tasksRepository.loadTasks();
            for (Task task : tasks) {
                if (task.isActive()) {
                    AlarmReceiver.setAlarm(context, task);
                }
            }
        }
    }

    private void notify(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        Intent intent = new Intent(context, TasksActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setContentTitle("收到启动广播")
                .setContentText("嗨呀")
                .setAutoCancel(true)
                .build();
        notification.defaults = Notification.DEFAULT_SOUND;

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }
}
