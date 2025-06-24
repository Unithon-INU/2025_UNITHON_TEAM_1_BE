package com.example.unithon.domain.club.entity;

import com.example.unithon.domain.club.Division;
import com.example.unithon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "clubs")
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Division division;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Builder
    private Club(Division division, String name, String description) {
        this.division = division;
        this.name = name;
        this.description = description;
    }

    public void updateClub(Division division, String name, String description) {
        this.division = division;
        this.name = name;
        this.description = description;
    }
}