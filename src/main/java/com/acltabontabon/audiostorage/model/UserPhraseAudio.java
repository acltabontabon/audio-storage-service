package com.acltabontabon.audiostorage.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserPhraseAudioId.class)
public class UserPhraseAudio {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "phrase_id")
    private Long phraseId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "phrase_id", insertable = false, updatable = false)
    private Phrase phrase;

    @ManyToOne
    @JoinColumn(name = "filename", nullable = false)
    private AudioFile audioFile;
}
