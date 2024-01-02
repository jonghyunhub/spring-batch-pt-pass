package com.fastcampus.pass.repository.pass;

import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-02T16:08:20+0900",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
public class PassModelMapperImpl implements PassModelMapper {

    @Override
    public PassEntity toPassEntity(BulkPassEntity bulkPassEntity, String userId) {
        if ( bulkPassEntity == null && userId == null ) {
            return null;
        }

        PassEntity.PassEntityBuilder passEntity = PassEntity.builder();

        if ( bulkPassEntity != null ) {
            passEntity.status( status( bulkPassEntity.getStatus() ) );
            passEntity.remainingCount( bulkPassEntity.getCount() );
            passEntity.packageSeq( bulkPassEntity.getPackageSeq() );
            passEntity.startedAt( bulkPassEntity.getStartedAt() );
            passEntity.endedAt( bulkPassEntity.getEndedAt() );
        }
        passEntity.userId( userId );

        return passEntity.build();
    }
}
