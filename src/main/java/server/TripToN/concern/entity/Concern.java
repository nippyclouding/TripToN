package server.TripToN.concern.entity;

import org.apache.catalina.connector.Response;
import server.TripToN.AiResponse.entity.AiResponse;
import server.TripToN.comment.entity.Comment;
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

    private String concernTitle;

    @Column(columnDefinition = "TEXT")
    private String concernContent;

    private boolean isLocked;

    @Enumerated(EnumType.STRING)
    private LuggageType luggageType;
}
