package com.example.unithon.domain.club.dto.res;

import com.example.unithon.domain.club.entity.Club;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClubCreateResDto {
    private final Long id;
    private final String name;

    @Builder
    private ClubCreateResDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ClubCreateResDto from(Club club) {
        return ClubCreateResDto.builder()
                .id(club.getId())
                .name(club.getName())
                .build();
    }
}