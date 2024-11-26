package com.acltabontabon.audiostorage.service;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class AudioConverterService {

    public void toWAV(MultipartFile audioFile, String outputPath) throws IOException {
        log.debug("Converting audio file to WAV: {}", audioFile.getOriginalFilename());
        File srcTempFile = File.createTempFile("audio_", ".m4a");
        audioFile.transferTo(srcTempFile);

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();

        try {
            FFmpeg.atPath()
                .addInput(UrlInput.fromPath(srcTempFile.toPath()))
                .addOutput(UrlOutput.toPath(outputFile.toPath()))
                .execute();
        } finally {
            srcTempFile.delete();
        }
    }

    public File toM4A(File srcAudioFile) {
        log.debug("Converting audio file to M4A: {}", srcAudioFile.getName());
        File outputFile = new File("audio_download_cache", fileNameWithoutExtension(srcAudioFile.getName()) + ".m4a");

        // skip conversion and use cached file if it exists
        if (outputFile.exists()) {
            log.info("Using cached audio file: {}", outputFile);
            return outputFile;
        }

        outputFile.deleteOnExit();
        outputFile.getParentFile().mkdirs();

        FFmpeg.atPath()
            .addInput(UrlInput.fromPath(srcAudioFile.toPath()))
            .addOutput(UrlOutput.toPath(outputFile.toPath())
                .setFormat("mp4")
                .addArguments("-c:a", "aac"))
            .execute();
        return outputFile;
    }

    private String fileNameWithoutExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
