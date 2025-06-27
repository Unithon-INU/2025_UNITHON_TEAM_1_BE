package com.example.unithon.domain.club.dto.res;

import com.example.unithon.domain.club.enums.Division;
import com.example.unithon.domain.club.entity.Club;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClubGetResDto {
    private final Long id;
    private final Division division;
    private final String name;
    private final String summary;
    private final String description;

    @Builder
    private ClubGetResDto(Long id, Division division, String name, String summary, String description) {
        this.id = id;
        this.division = division;
        this.name = name;
        this.summary = summary;
        this.description = description;
    }

    public static ClubGetResDto from(Club club) {
        return ClubGetResDto.builder()
                .id(club.getId())
                .division(club.getDivision())
                .name(club.getName())
                .summary(club.getSummary())
                .description(club.getDescription())
                .build();
    }
}