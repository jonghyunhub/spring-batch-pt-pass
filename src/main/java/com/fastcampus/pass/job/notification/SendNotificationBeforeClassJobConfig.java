package com.fastcampus.pass.job.notification;

import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.notification.NotificationEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class SendNotificationBeforeClassJobConfig {
    private final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final SendNotificationItemWriter sendNotificationItemWriter;

    public SendNotificationBeforeClassJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory, SendNotificationItemWriter sendNotificationItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.sendNotificationItemWriter = sendNotificationItemWriter;
    }

    @Bean
    public Job sendNotificationBeforeClassJob() {
        return this.jobBuilderFactory.get("sendNotificationBeforeClassJob")
                .start(addNotificationStep())
                .next(sendNotificationStep())
                .build();
    }

    /**
     * 첫번째 스텝 : notification 데이터를 채워줌
     */
    @Bean
    public Step addNotificationStep() {
        return this.stepBuilderFactory.get("addNotificationStep")
                .<BookingEntity, NotificationEntity>chunk(CHUNK_SIZE)
    }

    /**
     * 두번째 스텝 : notification 데이터를 기반으로 사용자들에게 알람을 보냄
     */
    @Bean
    public Step sendNotificationStep() {

    }
}
