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

    @Column
    private String fileName;

    @Column
    private String originalName;

    @Column
    private Long fileSize;

    @Builder
    public Photo(Post post, String fileName, String originalName, Long fileSize) {
        this.post = post;
        this.fileName = fileName;
        this.originalName = originalName;
        this.fileSize = fileSize;
    }
}
