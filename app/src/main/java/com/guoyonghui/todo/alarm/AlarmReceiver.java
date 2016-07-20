package com.guoyonghui.todo.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;

import com.guoyonghui.todo.R;
import com.guoyonghui.todo.data.Task;
import com.guoyonghui.todo.taskdetail.TaskDetailActivity;
import com.guoyonghui.todo.util.CalendarFormatHelper;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_TASK_ALARM = "com.guoyonghui.todo.alarm.ACTION_TASK_ALARM";

    public static final String EXTRA_TASK = "com.guoyonghui.todo.alarm.EXTRA_TASK";

    private static final int NOTIFICATION_TASK = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (ACTION_TASK_ALARM.equals(action)) {
            Task task = (Task) intent.getSerializableExtra(EXTRA_TASK);

            notify(context, task);
        }
    }

    private void notify(Context context, Task task) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, task.getID());
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setContentTitle(task.getTitle())
                .setContentText(task.getDescription())
                .setAutoCancel(true)
                .build();
        notification.defaults = Notification.DEFAULT_SOUND;

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_TASK, notification);
    }

    public static void setAlarm(Context context, Task task) {
        Calendar alarmCalendar = CalendarFormatHelper.parse(task.getAlarm());

        if (alarmCalendar == null) {
            return;
        }

        long triggerAtMills = alarmCalendar.getTimeInMillis();
        if (triggerAtMills <= System.currentTimeMillis()) {
            return;
        }

        Intent intent = new Intent(ACTION_TASK_ALARM);
        intent.putExtra(EXTRA_TASK, task);

        PendingIntent pi = PendingIntent.getBroadcast(context.getApplicationContext(), task.hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, triggerAtMills, pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, triggerAtMills, pi);
        }
    }

    public static void cancelAlarm(Context context, Task task) {
        Intent intent = new Intent(ACTION_TASK_ALARM);
        intent.putExtra(EXTRA_TASK, task);

        PendingIntent pi = PendingIntent.getBroadcast(context.getApplicationContext(), task.hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

}
