package com.acltabontabon.audiostorage.repository;

import com.acltabontabon.audiostorage.model.UserPhraseAudio;
import com.acltabontabon.audiostorage.model.UserPhraseAudioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhraseAudioRepository extends JpaRepository<UserPhraseAudio, UserPhraseAudioId> {

}
