package com.gigigo.orchextra.domain.abstractions.notifications;

import com.gigigo.orchextra.domain.abstractions.actions.ActionDispatcherListener;
import com.gigigo.orchextra.domain.model.actions.strategy.BasicAction;
import com.gigigo.orchextra.domain.model.actions.strategy.Notification;

public interface NotificationBehavior {
    void dispatchNotificationAction(BasicAction action, Notification notification);

    void setActionDispatcherListener(
        ActionDispatcherListener actionDispatcherListener);
}