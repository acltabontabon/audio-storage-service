package com.acltabontabon.audiostorage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Phrase {

    @Id
    @Column(name = "phrase_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phraseId;

    @Column(name = "content", nullable = false)
    private String content;

}