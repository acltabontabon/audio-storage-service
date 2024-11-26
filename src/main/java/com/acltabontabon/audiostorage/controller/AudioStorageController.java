package com.acltabontabon.audiostorage.controller;

import com.acltabontabon.audiostorage.dto.ApiResponse;
import com.acltabontabon.audiostorage.service.AudioStorageService;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/audio/user")
public class AudioStorageController {

    private final AudioStorageService audioStorageService;

    @PostMapping("/{userId}/phrase/{phraseId}")
    public ResponseEntity<ApiResponse> upload(@PathVariable Long userId,
                                              @PathVariable Long phraseId,
                                              @RequestParam("audio_file") MultipartFile audioFile) {

        if (audioFile == null || audioFile.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Audio file is empty"));
        }

        log.info("Received audio file: {}", audioFile.getOriginalFilename());
        audioStorageService.uploadAudio(audioFile, userId, phraseId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Audio file uploaded successfully"));
    }

    @GetMapping("/{userId}/phrase/{phraseId}/{audioFormat}")
    public ResponseEntity<Resource> download(@PathVariable Long userId,
                         @PathVariable Long phraseId,
                         @PathVariable String audioFormat) {

        log.info("Downloading audio file for user: {}, phrase: {}", userId, phraseId);
        File audioFile = audioStorageService.downloadAudio(userId, phraseId, audioFormat);
        Resource resource = new FileSystemResource(audioFile);

        // Return the file as a response
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("audio/mp4"))
            .header("Content-Disposition", "attachment; filename=\"" + audioFile.getName() + "\"")
            .body(resource);
    }
}
