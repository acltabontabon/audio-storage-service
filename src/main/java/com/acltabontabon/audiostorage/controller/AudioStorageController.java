package com.acltabontabon.audiostorage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audio/user")
public class AudioStorageController {

    @PostMapping("/{userId}/phrase/{phraseId}")
    public void upload(@PathVariable String userId, @PathVariable String phraseId) {

    }

    @GetMapping("/{userId}/phrase/{phraseId}/{audioFormat}")
    public void download(@PathVariable String userId, @PathVariable String phraseId, @PathVariable String audioFormat) {

    }
}
