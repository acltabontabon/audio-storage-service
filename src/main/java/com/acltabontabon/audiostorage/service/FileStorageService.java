package com.acltabontabon.audiostorage.service;

import com.acltabontabon.audiostorage.exception.AudioAlreadyExistException;
import com.acltabontabon.audiostorage.exception.AudioNotFoundException;
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
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        if (!isFileSupported(audioFile.getOriginalFilename())) {
            throw new UnsupportedFileExtensionException("Invalid audio file extension! Supported: " + ALLOWED_EXTENSIONS);
        }

        if (userPhraseAudioRepository.existsById(new UserPhraseAudioId(userId, phraseId))) {
            throw new AudioAlreadyExistException("Audio already exists for this user and phrase");
        }

        validate(userId, phraseId);

        try {
            AudioFile persistedAudioFile = saveAudioDetails(userId, phraseId);

            // TODO: make this non-blocking?
            audioConverterService.toWAV(audioFile, persistedAudioFile.getStoragePath());
        } catch (Exception e) {
            log.error("Failed to convert audio file", e);
            // rollback transaction
            throw new FileConversionException("Failed to convert audio file");
        }
    }

    @Override
    public File downloadAudio(Long userId, Long phraseId, String audioFormat) {
        if (!isFileTypeSupported(audioFormat)) {
            throw new UnsupportedFileExtensionException("Invalid audio file extension! Supported: " + ALLOWED_EXTENSIONS);
        }

        validate(userId, phraseId);

        UserPhraseAudio userPhraseAudio = userPhraseAudioRepository
            .findById(new UserPhraseAudioId(userId, phraseId))
            .orElseThrow(() -> new AudioNotFoundException("Audio does not exist for userId: " + userId + ", phraseId: " + phraseId));

        try {
            // Convert the audio file to the desired format
            return audioConverterService.toM4A(new File(userPhraseAudio.getAudioFile().getStoragePath()));
        } catch (Exception e) {
            log.error("Failed to convert audio file for userId: {}, phraseId: {}", userId, phraseId, e);
            throw new FileConversionException("Failed to convert audio file");
        }
    }

    /**
     * Validates if the user and phrase exists and if the audio already exists for the user
     * and phrase.
     *
     * @param userId -
     * @param phraseId -
     */
    private void validate(Long userId, Long phraseId) {
        if (!userRepository.existsById(userId)) {
            log.error("User with id " + userId + " not found");
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        if (!phraseRepository.existsById(phraseId)) {
            log.error("Phrase with id " + phraseId + " not found");
            throw new PhraseNotFoundException("Phrase with id " + phraseId + " not found");
        }
    }

    /**
     * Saves the audio details to the database.
     *
     * @param userId -
     * @param phraseId -
     * @return audio file
     */
    private AudioFile saveAudioDetails(Long userId, Long phraseId) {
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
