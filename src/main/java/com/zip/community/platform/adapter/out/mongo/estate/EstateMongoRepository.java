package com.zip.community.platform.adapter.out.mongo.estate;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EstateMongoRepository extends MongoRepository<EstateDocument, String> {

    Optional<EstateDocument> findByKaptCode(String kaptCode);

    Optional<EstateDocument> findByKaptName(String kaptName);

    // 특정 동이나 특정 지구를 포함하는 주소 검색 (예: "분당구", "야탑동")
    @Query("{'kaptAddr': { $regex: ?0, $options: 'i' }}")
    Page<EstateDocument> findByKaptAddrContaining(String address, Pageable pageable);

    // 좌표 주변의 특정 반경이상 주소 검색
    @Query(value = "{ 'location': { $geoWithin: { $centerSphere: [ [ ?0, ?1 ], ?2 ] } } }")
    List<EstateDocument> findByLocation(double longitude, double latitude, double radiusInRadians);


}
