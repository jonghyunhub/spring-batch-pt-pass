package com.fastcampus.pass.job.pass;

import com.fastcampus.pass.repository.pass.*;
import com.fastcampus.pass.repository.user.UserGroupMappingEntity;
import com.fastcampus.pass.repository.user.UserGroupMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AddPassesTaskletTest {
    @Mock
    private StepContribution stepContribution;

    @Mock
    private ChunkContext chunkContext;

    @Mock
    private PassRepository passRepository;

    @Mock
    private BulkPassRepository bulkPassRepository;

    @Mock
    private UserGroupMappingRepository userGroupMappingRepository;

    @InjectMocks
    private AddPassesTasklet addPassesTasklet;

    @Test
    void test_execute() throws Exception {
        // given
        final String userGroupId = "GROUP";
        final String userId = "A1000000";
        final Integer packageSeq = 1;
        final Integer count = 10;
        final LocalDateTime now = LocalDateTime.now();

        final BulkPassEntity bulkPassEntity = BulkPassEntity.builder()
                .packageSeq(packageSeq)
                .userGroupId(userGroupId)
                .status(BulkPassStatus.READY)
                .count(count)
                .startedAt(now)
                .endedAt(now.plusDays(60))
                .build();

        final UserGroupMappingEntity userGroupMappingEntity = UserGroupMappingEntity
                .builder()
                .userGroupId(userGroupId)
                .userId(userId)
                .build();

        given(bulkPassRepository
                .findByStatusAndStartedAtGreaterThan(eq(BulkPassStatus.READY), any())
        ).willReturn(List.of(bulkPassEntity));

        given(userGroupMappingRepository.findByUserGroupId(eq(userGroupId)))
                .willReturn(List.of(userGroupMappingEntity));

        // when
        RepeatStatus repeatStatus = addPassesTasklet.execute(stepContribution, chunkContext);

        // then
        // execute의 return 값인 RepeatStatus 값을 확인
        assertEquals(RepeatStatus.FINISHED, repeatStatus);

        // 추가된 PassEntity 값을 확인
        ArgumentCaptor<List> passEntitiesCapture = ArgumentCaptor.forClass(List.class);
        verify(passRepository, times(1)).saveAll(passEntitiesCapture.capture());
        final List<PassEntity> passEntities = passEntitiesCapture.getValue();

        assertEquals(1, passEntities.size());

        final PassEntity passEntity = passEntities.get(0);
        assertThat(passEntity).extracting(
                "packageSeq",
                "userId",
                "status",
                "remainingCount"
        ).containsExactly(
                packageSeq,
                userId,
                PassStatus.READY,
                count
        );
    }

}