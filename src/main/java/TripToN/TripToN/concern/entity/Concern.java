package TripToN.TripToN.concern.entity;

import TripToN.TripToN.global.util.BaseEntity;
import TripToN.TripToN.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@Table(name = "CONCERNS")
public class Concern extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concern_id")
    private Long concernId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String concernTitle;

    @Column(columnDefinition = "TEXT")
    private String concernContent;

    private boolean isLocked;

    @Enumerated(EnumType.STRING)
    private LuggageType luggageType;
}
