//package com.senpare.authservice.unit.controller;
//
//import com.senpare.authservice.BaseTest;
//import com.senpare.authservice.controller.AppUserController;
//import com.senpare.authservice.exception.BadRequestException;
//import com.senpare.authservice.model.AppUser;
//import com.senpare.authservice.service.AppUserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//class AppUserControllerTest extends BaseTest<AppUser> {
//
//    // TODO implement negative test cases
//
//    @Mock
//    private AppUserService appUserService;
//
//    private AppUserController underTest;
//
//    private AppUser appUser;
//
//    @BeforeEach
//    public void setUp() {
//        underTest = new AppUserController(appUserService);
//    }
//
//    @Test
//    public void shouldCreateAppUser() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        // when
//        when(appUserService.create(appUser, true)).thenReturn(appUser);
//        ResponseEntity<AppUser> response = underTest.createAppUser(appUser);
//
//        // then
//        then(appUserService).should().create(modelArgumentCaptor.capture(), eq(true));
//        assertThat(modelArgumentCaptor.getValue()).isEqualTo(appUser);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(response.getBody()).isEqualTo(appUser);
//    }
//
//    @Test
//    public void shouldNotCreateNullAppUser() {
//        // when
//        when(appUserService.create(null, true)).thenThrow(BadRequestException.class);
//
//        // then
//        assertThatThrownBy(() -> underTest.createAppUser(null)).isInstanceOf(BadRequestException.class);
//        then(appUserService).should().create(modelArgumentCaptor.capture(), eq(true));
//        assertThat(modelArgumentCaptor.getValue()).isEqualTo(null);
//    }
//
//
//    @Test
//    public void shouldGetAppUsers() {
//        // given
//        Page<AppUser> appUsers = new PageImpl<>(List.of(
//                new AppUser()
//                        .setUuid(1L)
//                        .setFirstName("Test")
//                        .setLastName("User")
//                        .setEmail("testuser1@test.com")
//                        .setPassword("testPassword")
//                        .setCreatedOn(now)
//                        .setModifiedOn(now),
//                new AppUser()
//                        .setUuid(2L)
//                        .setFirstName("Test2")
//                        .setLastName("User2")
//                        .setEmail("testuser2@test.com")
//                        .setCreatedOn(now)
//                        .setModifiedOn(now)
//        ));
//
//        // when
//        when(appUserService.getAppUsers(anyInt(), anyInt(), anyString())).thenReturn(appUsers);
//        ResponseEntity<List<AppUser>> response = underTest.getAppUsers(1, 2, "firstName"); //TODO: this is temporary considering changing values
//
//        // then
//        then(appUserService).should().getAppUsers(1, 2, "firstName");
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo(appUsers.getContent());
//    }
//
//    @Test
//    public void shouldGetAppUser() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        // when
//        when(appUserService.getAppUser(anyLong())).thenReturn(appUser);
//        ResponseEntity<AppUser> response = underTest.getAppUser(1L);
//
//        // then
//        then(appUserService).should().getAppUser(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo(appUser);
//    }
//
//    @Test
//    public void shouldUpdateAppUser() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        AppUser updatedAppUser = new AppUser()
//                .setUuid(2L)
//                .setFirstName("Test1")
//                .setLastName("User2")
//                .setEmail("testuser2@test.com")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(appUserService.update(anyLong(), any(AppUser.class))).thenReturn(updatedAppUser);
//        ResponseEntity<AppUser> response = underTest.updateAppUser(1L, appUser);
//
//        // then
//        then(appUserService).should().update(idArgumentCaptor.capture(), modelArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(modelArgumentCaptor.getValue()).isEqualTo(appUser);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo(updatedAppUser);
//    }
//
//    @Test
//    public void shouldDeleteAppUser() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        // when
//        when(appUserService.delete(anyLong())).thenReturn(appUser);
//        ResponseEntity response = underTest.deleteAppUser(1L);
//
//        // then
//        then(appUserService).should().delete(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        assertThat(response.getBody()).isEqualTo(null);
//    }
//}