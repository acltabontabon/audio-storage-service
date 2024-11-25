package com.acltabontabon.audiostorage.service;

import jakarta.transaction.Transactional;

public interface StorageService {

    @Transactional
    void uploadAudio();

    void downloadAudio();

    void convertAudio();

}
