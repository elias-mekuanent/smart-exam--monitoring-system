package com.senpare.audioprocessingservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/audio-processing-service")
@Slf4j
public class AudioProcessorController {


    @PostMapping(value = "/measure-noise", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Double> measureNoise(@RequestPart MultipartFile multipartFile) throws IOException, UnsupportedAudioFileException {
        InputStream inputStream = multipartFile.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);

        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        int frameSize = format.getFrameSize();

        byte[] data = new byte[(int) frames * frameSize];
        audioInputStream.read(data);

        // Calculate the RMS amplitude of the audio signal
        double rms = 0;
        for (int i = 0; i < data.length; i += 2) {
            short sample = (short) ((data[i] & 0xff) | (data[i + 1] << 8));
            rms += sample * sample;
        }
        rms = Math.sqrt(rms / (data.length / 2));

        log.debug("Audio file received");
        return ResponseEntity.ok().body(rms);
    }
}
