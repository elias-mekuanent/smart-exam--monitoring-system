//package com.senpare.authservice.unit.repository;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.senpare.authservice.model.AppUser;
//import com.senpare.authservice.repository.AppUserRepository;
//
//@SpringBootTest
//@AutoConfigureDataJpa
//class AppUserRepositoryTest {
//
//    private final AppUserRepository underTest;
//    private AppUser appUser;
//
//    @Autowired
//    public AppUserRepositoryTest(AppUserRepository underTest) {
//        this.underTest = underTest;
//    }
//
//    @Test
//    public void shouldFindByEmail() {
//        // given
//        appUser = new AppUser()
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword");
//
//        // when
//        underTest.save(appUser);
//
//        // then
//        assertTrue(underTest.findByEmail(appUser.getEmail()).isPresent());
//    }
//
//    @Test
//    public void shouldNotFindAppUserForWrongEmail() {
//        // given
//        appUser = new AppUser()
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser2@test.com")
//                .setPassword("testPassword");
//        // when
//        underTest.save(appUser);
//
//        // then
//        assertFalse(underTest.findByEmail("wrongemail@test.com").isPresent());
//    }
//}