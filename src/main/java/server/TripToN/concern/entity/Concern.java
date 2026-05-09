package server.TripToN.concern.entity;

import org.hibernate.annotations.SQLRestriction;
import server.TripToN.AiResponse.entity.AiResponse;
import server.TripToN.comment.entity.Comment;
import server.TripToN.concern.dto.ConcernUpdateRequestDto;
import server.TripToN.global.util.BaseEntity;
import server.TripToN.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@Table(name = "CONCERNS")
@SQLRestriction("deleted_at IS NULL")
public class Concern extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concern_id")
    private Long concernId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "concern", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "concern", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AiResponse aiResponse;

    @Column(nullable = false, length = 255)
    private String concernTitle;

    @Column(columnDefinition = "TEXT")
    private String concernContent;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LuggageType luggageType;

    public void updateConcern(ConcernUpdateRequestDto dto) {
        this.concernContent = dto.getConcernContent();
        this.concernTitle = dto.getConcernTitle();
    }
}
