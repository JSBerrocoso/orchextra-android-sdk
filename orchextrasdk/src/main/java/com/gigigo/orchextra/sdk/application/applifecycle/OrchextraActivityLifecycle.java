/*
 * Created by Orchextra
 *
 * Copyright (C) 2016 Gigigo Mobile Services SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gigigo.orchextra.sdk.application.applifecycle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.gigigo.ggglib.utils.ConsistencyUtils;
import com.gigigo.orchextra.device.notifications.AndroidNotificationBuilder;
import com.gigigo.orchextra.device.notifications.NotificationReceiver;
import com.gigigo.orchextra.device.notifications.dtos.AndroidBasicAction;
import com.gigigo.orchextra.device.notifications.dtos.AndroidNotification;
import com.gigigo.orchextra.domain.abstractions.device.OrchextraLogger;
import com.gigigo.orchextra.domain.abstractions.lifecycle.AppStatusEventsListener;
import com.gigigo.orchextra.domain.abstractions.lifecycle.LifeCycleAccessor;
import com.gigigo.orchextra.domain.model.actions.ActionType;
import java.util.Iterator;
import java.util.Stack;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) public class OrchextraActivityLifecycle
    implements Application.ActivityLifecycleCallbacks, LifeCycleAccessor {

  private final AppStatusEventsListener appStatusEventsListener;
  private final OrchextraLogger orchextraLogger;
  private final String notificationActivityClass;

  private Stack<ActivityLifecyleWrapper> activityStack = new Stack<>();

  public OrchextraActivityLifecycle(AppStatusEventsListener listener,
      OrchextraLogger orchextraLogger, String notificationActivityClass) {
    this.appStatusEventsListener = listener;
    this.orchextraLogger = orchextraLogger;
    this.notificationActivityClass = notificationActivityClass;
  }

  @Override public boolean isInBackground() {
    return activityStack.isEmpty();
  }

  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
  }

  @Override public void onActivityStarted(Activity activity) {

    boolean wasInBackground = activityStack.empty();
    if (wasInBackground) {
      appStatusEventsListener.onBackgroundEnd();
    }

    this.activityStack.push(new ActivityLifecyleWrapper(activity, true, false));

    if (wasInBackground) {
      startForegroundMode();
    }
    //EVALUATE INTENT WHEN ACTIVITY "HOME" ARE REACHED, NOT BEFORE
    if (notificationActivityClass.equals(activity.getClass().toString())
        && NotificationReceiver.mIntent != null
        && AndroidNotificationBuilder.NOTIFICATION_ACTION_OX.equals(
        NotificationReceiver.mIntent.getAction())) {
      AndroidBasicAction androidBasicAction = getBasicActionChangeShownFromIntent();

      if (!androidBasicAction.getAction().equals(ActionType.NOTIFICATION_PUSH.getStringValue())) {
        generateIntentWhenNotificationActivityOpened(activity, androidBasicAction);
      }
    }
  }

  private void generateIntentWhenNotificationActivityOpened(Activity activity,
      AndroidBasicAction androidBasicAction) {
    Intent intent = new Intent(activity, NotificationReceiver.class).setAction(
        NotificationReceiver.ACTION_NOTIFICATION_BROADCAST_RECEIVER)
        .putExtra(NotificationReceiver.NOTIFICATION_BROADCAST_RECEIVER,
            NotificationReceiver.NOTIFICATION_BROADCAST_RECEIVER)
        .putExtra(AndroidNotificationBuilder.EXTRA_NOTIFICATION_ACTION, androidBasicAction)
        .putExtra(AndroidNotificationBuilder.HAVE_ACTIVITY_NOTIFICATION_OX, true)
        .setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

    activity.sendBroadcast(intent);
  }

  private AndroidBasicAction getBasicActionChangeShownFromIntent() {
    AndroidBasicAction androidBasicAction = new AndroidBasicAction();
    try {
      androidBasicAction = (AndroidBasicAction) NotificationReceiver.mIntent.getParcelableExtra(
          AndroidNotificationBuilder.EXTRA_NOTIFICATION_ACTION);
      if (androidBasicAction != null && androidBasicAction.getNotification() != null) {
        AndroidNotification androidNotification = androidBasicAction.getNotification();
        if (androidNotification != null
            && androidNotification.getBody() != ""
            && androidNotification.getTitle() != "") {
          androidNotification.setShown(
              false);//we will to show the notification again when the activity home of app will be reached
          androidBasicAction.setNotification(androidNotification);
        }
      }
    } catch (Throwable tr) {
    }
    return androidBasicAction;
  }

  @Override public void onActivityResumed(Activity activity) {
    if (!ConsistencyUtils.isObjectNull(activityStack)) {
      activityStack.peek().setIsPaused(false);
    }
  }

  @Override public void onActivityPaused(Activity activity) {
    if (!ConsistencyUtils.isObjectNull(activityStack)) {
      activityStack.peek().setIsPaused(true);
    }
  }

  @Override public void onActivityStopped(Activity activity) {
    if (!ConsistencyUtils.isObjectNull(activityStack)) {

      if (activityStack.size() == 1) {
        appStatusEventsListener.onForegroundEnd();
      }
      removeActivityFromStack(activity);
      setBackgroundModeIfNeeded();
    }
  }

  @Override public void onActivityDestroyed(Activity activity) {
  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
  }

  public boolean isActivityContextAvailable() {
    return (getCurrentActivity() != null);
  }

  public AppStatusEventsListener getAppStatusEventsListener() {
    return appStatusEventsListener;
  }

  public Activity getCurrentActivity() {

    for (ActivityLifecyleWrapper activityLifecyleWrapper : activityStack) {
      if (!activityLifecyleWrapper.isPaused()) {
        return activityLifecyleWrapper.getActivity();
      }
    }

    for (ActivityLifecyleWrapper activityLifecyleWrapper : activityStack) {
      if (!activityLifecyleWrapper.isStopped()) {
        return activityLifecyleWrapper.getActivity();
      }
    }

    return null;
  }

  private void removeActivityFromStack(Activity activity) {
    Iterator<ActivityLifecyleWrapper> iter = activityStack.iterator();
    while (iter.hasNext()) {
      ActivityLifecyleWrapper activityLifecyleWrapper = iter.next();
      if (activityLifecyleWrapper.getActivity().equals(activity)) {
        //activityStack.remove(activityLifecyleWrapper);
        iter.remove();
      }
    }
  }

  private void setBackgroundModeIfNeeded() {
    if (activityStack.empty()) {
      appStatusEventsListener.onBackgroundStart();
    }
  }

  private void startForegroundMode() {
    appStatusEventsListener.onForegroundStart();
  }

  //todo notcomplete
  private void prinStatusOfStack(String s) {
    orchextraLogger.log("STACK Status :: " + s);
    orchextraLogger.log("STACK Status :: Elements in Stack :" + activityStack.size());
    for (int i = 0; i < activityStack.size(); i++) {
      orchextraLogger.log("STACK Status :: Activity "
          + i
          + " Stopped: "
          + activityStack.get(i).isStopped()
          + " Paused: "
          + activityStack.get(i).isPaused()
          + " Has Context: "
          + (activityStack.get(i).getActivity() != null)
          + " Activity Context: "
          + activityStack.get(i).getActivity());
    }

    orchextraLogger.log("STACK Status :: Lifecycle, Is app in Background: " + isInBackground());

    orchextraLogger.log(
        "STACK Status :: isActivityContextAvailable: " + isActivityContextAvailable());

    orchextraLogger.log("STACK Status :: getCurrentActivity: " + getCurrentActivity());

    orchextraLogger.log("------------- STACK Status ---------------");
  }
}