package com.acltabontabon.audiostorage.repository;

import com.acltabontabon.audiostorage.model.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFile, String> {

}
