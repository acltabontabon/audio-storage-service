package com.acltabontabon.audiostorage.service;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AudioConverterService {

    public void saveAsWav(MultipartFile audioFile, String outputPath) throws IOException {
        File audioTmpFile = File.createTempFile("audio_", ".m4a");
        audioFile.transferTo(audioTmpFile);

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();

        try {
            FFmpeg.atPath()
                .addInput(UrlInput.fromPath(audioTmpFile.toPath()))
                .addOutput(UrlOutput.toPath(outputFile.toPath()))
                .execute();
        } finally {
            audioTmpFile.delete();
        }
    }
}
