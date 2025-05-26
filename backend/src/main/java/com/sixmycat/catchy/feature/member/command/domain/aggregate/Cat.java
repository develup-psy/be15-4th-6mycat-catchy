package com.sixmycat.catchy.feature.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    private String gender;

    private String breed;

    private LocalDate birthDate;

    private Integer age;

    @Column(insertable = false)
    private Date createdAt;

    @Column(insertable = false)
    private Date updatedAt;

    private Date deletedAt;

    public Cat(String name, String gender, String breed, LocalDate birthDay, Integer age, Member member) {
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.birthDate = birthDay;
        this.age = age;
        this.member = member;
    }

    public void updateCatInfo(String name, String gender, String breed, LocalDate birthDay, Integer age) {
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.birthDate = birthDay;
        this.age = age;
    }

    public void assignTo(Member member) {
        this.member = member;
    }

    public void delete() {
        this.deletedAt = new Date(); // soft delete (DB에서는 남기고 숨김 처리)
    }
}
