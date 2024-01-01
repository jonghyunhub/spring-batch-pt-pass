package com.fastcampus.pass.job.notification;

import com.fastcampus.pass.repository.notification.NotificationEntity;
import com.fastcampus.pass.repository.notification.NotificationRepository;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SendNotificationItemWriter implements ItemWriter<NotificationEntity> {

    private final NotificationRepository notificationRepository;
    private final
    @Override
    public void write(List<? extends NotificationEntity> list) throws Exception {

    }
}
