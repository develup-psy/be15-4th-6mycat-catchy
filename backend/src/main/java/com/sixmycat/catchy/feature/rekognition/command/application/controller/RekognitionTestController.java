package com.sixmycat.catchy.feature.rekognition.command.application.controller;

import com.sixmycat.catchy.feature.rekognition.command.application.service.ImageModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.rekognition.model.Label;

import java.util.List;

@RestController
@RequestMapping("/rekognition")
@RequiredArgsConstructor
public class RekognitionTestController {

    private final ImageModerationService moderationService;

    @GetMapping("/labels")
    public List<Label> detect(@RequestParam String bucket, @RequestParam String key) {
        return moderationService.detectLabels(bucket, key);
    }
}