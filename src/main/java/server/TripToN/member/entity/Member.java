package server.TripToN.member.entity;

import server.TripToN.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(
        name = "MEMBERS",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_MEMBER_EMAIL", columnNames = "member_email"),
                @UniqueConstraint(name = "UK_MEMBER_NICKNAME", columnNames = "member_nickname")
        }
)

public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, length = 255)
    private String memberLoginPassword;

    @Column(nullable = false, length = 100)
    private String memberEmail;

    @Column(nullable = false)
    private String memberNickname;

}
