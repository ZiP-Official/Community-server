package com.zip.community.platform.adapter.out.mongo.estate;

import com.zip.community.platform.domain.estate.Estate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

@Document(collection = "db")  // "db" 컬렉션을 사용하도록 설정
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class EstateDocument {

    @Field("_id")
    private String id;

    private String kaptCode;
    private String kaptAddr;
    private String kaptName;

    public static EstateDocument from(Estate estate) {


        return EstateDocument.builder()
                .kaptCode(estate.getKaptCode())
                .kaptAddr(estate.getKaptAddr())
                .kaptName(estate.getKaptName())
                .build();
    }

    public Estate toDomain() {

        return Estate.builder()
                .id(this.id)
                .kaptCode(this.kaptCode)
                .kaptAddr(this.kaptAddr)
                .kaptName(this.kaptName)
                .build();
    }

}
