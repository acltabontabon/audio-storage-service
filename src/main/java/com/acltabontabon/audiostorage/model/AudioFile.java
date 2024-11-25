package com.acltabontabon.audiostorage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AudioFile {

    @Id
    @Column(name = "filename")
    private String filename;

    @Column(name = "storage_path", nullable = false)
    private String storagePath;

}
