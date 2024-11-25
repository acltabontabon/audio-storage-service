package com.acltabontabon.audiostorage.repository;

import com.acltabontabon.audiostorage.model.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {

}
