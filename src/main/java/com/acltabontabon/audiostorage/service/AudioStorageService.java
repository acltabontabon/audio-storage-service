package com.acltabontabon.audiostorage.service;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AudioStorageService {

    // TODO: Make this configurable
    List<String> ALLOWED_EXTENSIONS = Arrays.asList("m4a");

    void uploadAudio(MultipartFile audioFile, Long userId, Long phraseId);

    File downloadAudio(Long userId, Long phraseId, String audioFormat);

    default String generateFilename(String ext) {
        return String.format("AudioFile_%s_%d.%s", LocalDate.now(), System.currentTimeMillis(), ext);
    }

    default boolean isFormatSupported(String filename) {
        if (filename == null || !filename.contains(".")) {
            return false;
        }

        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    default String getTargetAudioFormat() {
        return "wav";
    }
}
