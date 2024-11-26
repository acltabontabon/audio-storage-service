package com.acltabontabon.audiostorage.service;

import com.acltabontabon.audiostorage.exception.AudioAlreadyExistException;
import com.acltabontabon.audiostorage.exception.FileConversionException;
import com.acltabontabon.audiostorage.exception.PhraseNotFoundException;
import com.acltabontabon.audiostorage.exception.UnsupportedFileExtensionException;
import com.acltabontabon.audiostorage.exception.UserNotFoundException;
import com.acltabontabon.audiostorage.model.AudioFile;
import com.acltabontabon.audiostorage.model.UserPhraseAudio;
import com.acltabontabon.audiostorage.model.UserPhraseAudioId;
import com.acltabontabon.audiostorage.repository.AudioFileRepository;
import com.acltabontabon.audiostorage.repository.PhraseRepository;
import com.acltabontabon.audiostorage.repository.UserPhraseAudioRepository;
import com.acltabontabon.audiostorage.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService implements AudioStorageService {

    private final UserRepository userRepository;
    private final PhraseRepository phraseRepository;
    private final AudioFileRepository audioFileRepository;
    private final UserPhraseAudioRepository userPhraseAudioRepository;

    private final AudioConverterService audioConverterService;

    @Override
    @Transactional
    public void uploadAudio(MultipartFile audioFile, Long userId, Long phraseId) {
        if (!hasValidExtension(audioFile.getOriginalFilename())) {
            throw new UnsupportedFileExtensionException("Invalid audio file extension! Supported: " + ALLOWED_EXTENSIONS);
        }

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        if (!phraseRepository.existsById(phraseId)) {
            throw new PhraseNotFoundException("Phrase with id " + phraseId + " not found");
        }

        if (userPhraseAudioRepository.existsById(new UserPhraseAudioId(userId, phraseId))) {
            throw new AudioAlreadyExistException("Audio already exists for this user and phrase");
        }

        try {
            AudioFile persistedAudioFile = saveAudioDetailsToDB(userId, phraseId);

            // TODO: make this non-blocking!
            audioConverterService.saveAsWav(audioFile, persistedAudioFile.getStoragePath());
        } catch (Exception e) {
            log.error("Failed to convert audio file", e);
            // rollback transaction
            throw new FileConversionException("Failed to convert audio file");
        }
    }

    @Override
    public void downloadAudio() {

    }

    private AudioFile saveAudioDetailsToDB(Long userId, Long phraseId) {
        String filename = generateFilename(getTargetAudioFormat());
        AudioFile persistedAudioFile = audioFileRepository.save(AudioFile.builder()
            .filename(filename)
            .storagePath("audio_files/" + filename)
            .build());

        userPhraseAudioRepository.save(UserPhraseAudio.builder()
            .userId(userId)
            .phraseId(phraseId)
            .audioFile(persistedAudioFile)
            .build());

        return persistedAudioFile;
    }
}
