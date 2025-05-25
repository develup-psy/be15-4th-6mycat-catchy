package com.sixmycat.catchy.feature.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String social;

    private String contactNumber;

    private String profileImage;

    private String nickname;

    private String statusMessage;

    @Column(insertable = false)
    private Date createdAt;

    @Column(insertable = false)
    private Date updatedAt;

    private Date deletedAt;

    public void updateDeletedAt() {
        this.deletedAt = new Date();
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cat> cats = new ArrayList<>();

    public Member(String nickname, String statusMessage, String profileImage) {
        this.nickname = nickname;
        this.statusMessage = statusMessage;
        this.profileImage = profileImage;
    }

    public void updateProfile(String nickname, String statusMessage, String profileImage) {
        if (nickname != null) this.nickname = nickname;
        if (statusMessage != null) this.statusMessage = statusMessage;
        if (profileImage != null) this.profileImage = profileImage;
    }

    public void addCat(Cat cat) {
        cats.add(cat);
        cat.assignTo(this);
    }
}
