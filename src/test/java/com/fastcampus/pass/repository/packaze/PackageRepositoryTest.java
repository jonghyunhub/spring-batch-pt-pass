package com.fastcampus.pass.repository.packaze;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class PackageRepositoryTest {
    @Autowired
    private PackageRepository packageRepository;

    @Test
    void save_test(){
        // given
        PackageEntity packageEntity = PackageEntity.builder()
                .packageName("바디 챌린지 PT 12주")
                .period(84)
                .build();

        // when
        packageRepository.save(packageEntity);

        // then
        assertNotNull(packageEntity.getPackageSeq());
    }

    @Test
    void test_findByCreatedAtAfter(){
        // given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity0 = PackageEntity.builder()
                .packageName("학생 전용 3개월")
                .period(90)
                .build();
        packageRepository.save(packageEntity0);

        PackageEntity packageEntity1 = PackageEntity.builder()
                .packageName("학생 전용 6개월")
                .period(180)
                .build();
        packageRepository.save(packageEntity1);

        // when
        final List<PackageEntity> packageEntities = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

        // then
        assertEquals(1, packageEntities.size());
        assertEquals(packageEntity1.getPackageSeq(), packageEntities.get(0).getPackageSeq());
    }

    @Test
    void test_updateCountAndPeriod(){
        // given
        PackageEntity packageEntity = PackageEntity.builder()
                .packageName("바디프로필 이벤트 4개월")
                .period(90)
                .build();
        packageRepository.save(packageEntity);

        // when
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        PackageEntity updatedPackageEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        // then
        assertEquals(1, updatedCount);
        assertEquals(30, updatedPackageEntity.getCount());
        assertEquals(120, updatedPackageEntity.getPeriod());

    }

    @Test
    void test_delete(){
        // given
        PackageEntity packageEntity = PackageEntity.builder()
                .packageName("제거할 이용권")
                .count(1)
                .build();
        PackageEntity newPackageEntity = packageRepository.save(packageEntity);

        // when
        packageRepository.deleteById(newPackageEntity.getPackageSeq());

        // then
        assertTrue(packageRepository.findById(newPackageEntity.getPackageSeq()).isEmpty());

    }
}