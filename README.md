### 2016/07/25

重启后接收开机广播BOOT_COMPLETED的问题。

> Starting with Android 3.1 all applications, upon installation, are placed in a "stopped" state.(This is the same state that the application ends up in after the user force-stops the app from the Settings application.)
> While in "stopped" state, the application will not run for any reason, except by a manual launch of an activity. (Meaning no BroadcastRecevier(ACTION_PACKAGE_INSTALLED, BOOT_COMPLETED etc. will be invoked, regardless of the event for which they have registered, until the user runs the app manually.)

按照上述规则进行测试，在Samsung S5(Android 5.0)上仍无法受到开机广播，而在Nexus 5(Android 5.0)上得到了较好的测试效果，目前还没有发现原因所在。