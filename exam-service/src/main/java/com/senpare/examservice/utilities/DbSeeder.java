package com.senpare.examservice.utilities;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
@Profile({"dev", "prod"})
public class DbSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private static boolean alreadySetup = false;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }



        log.info("Database successfully seeded");
        alreadySetup = true;
    }

}