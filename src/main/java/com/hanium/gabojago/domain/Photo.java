package com.hanium.gabojago.domain;

import lombok.*;

import javax.persistence.*;

@ToString(exclude = {"post"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "file")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String fileName;

    private String originalName;

    private Long fileSize;

    @Builder
    public Photo(Post post, String fileName, String originalName, Long fileSize) {
        this.post = post;
        this.fileName = fileName;
        this.originalName = originalName;
        this.fileSize = fileSize;
    }
}
