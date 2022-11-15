package com.lukinhasssss.admin.catalogo.domain.exception

import com.lukinhasssss.admin.catalogo.domain.validation.handler.Notification

class NotificationException(
    message: String = "",
    notification: Notification
) : DomainException(message, notification.getErrors())
