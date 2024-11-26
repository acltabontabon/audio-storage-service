package com.acltabontabon.audiostorage.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.acltabontabon.audiostorage.service.AudioStorageService;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AudioStorageController.class)
class AudioStorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AudioStorageService audioStorageService; // Use @MockBean from Spring

    @Test
    void successUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "audio_file", "test.m4a", MediaType.MULTIPART_FORM_DATA_VALUE, "audio content".getBytes()
        );

        doNothing()
            .when(audioStorageService)
            .uploadAudio(any(), anyLong(), anyLong());

        mockMvc.perform(multipart("/audio/user/{userId}/phrase/{phraseId}", 1L, 1L)
                .file(file))
            .andExpect(status().isCreated());
    }

    @Test
    void successfulDownload() throws Exception {
        File mockFile = new File("test.m4a");
        mockFile.createNewFile();
        mockFile.deleteOnExit();

        when(audioStorageService.downloadAudio(1L, 1L, "m4a"))
            .thenReturn(mockFile);

        mockMvc.perform(get("/audio/user/1/phrase/1/m4a"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", "audio/mp4"))
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.m4a\""));
    }
}