package com.fastcampus.pass.repository.pass;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PassModelMapperTest {

    @Test
    void test_toPassEntity(){
        // given
        final LocalDateTime now = LocalDateTime.now();
        final String userId = "A1000000";

        BulkPassEntity bulkPassEntity = BulkPassEntity.builder()
                .packageSeq(1)
                .userGroupId("GROUP")
                .status(BulkPassStatus.COMPLETED)
                .count(10)
                .startedAt(now.minusDays(60))
                .endedAt(now)
                .build();

        // when
        final PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPassEntity, userId);

        // then
        assertThat(passEntity)
                .extracting(
                        "packageSeq",
                        "status",
                        "remainingCount",
                        "startedAt",
                        "endedAt",
                        "userId")
                .containsExactly(
                        1,
                        PassStatus.READY,
                        10,
                        now.minusDays(60),
                        now,
                        userId
                );

    }

}