package server.TripToN.comment.entity;

import org.hibernate.annotations.Where;
import server.TripToN.comment.dto.CommentResponseDto;
import server.TripToN.concern.entity.Concern;
import server.TripToN.global.util.BaseEntity;
import server.TripToN.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@Table(name = "COMMENTS")
@Where(clause = "deleted_at IS NULL") // Comments DB 에서 조회 시 항상 해당 조건 적용
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concern_id", nullable = false)
    private Concern concern;

    @Column(columnDefinition = "TEXT")
    private String commentContent;

    public CommentResponseDto toDto() {
        return CommentResponseDto.builder()
                .commentId(this.commentId)
                .memberId(this.member.getMemberId())
                .commentContent(this.commentContent)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .commentMemberNickname(this.member.getMemberNickName())
                .build();
    }

    public void updateContent(String updateContext) {
        this.commentContent = updateContext;
    }
}
