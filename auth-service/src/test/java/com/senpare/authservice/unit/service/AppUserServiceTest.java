//package com.senpare.authservice.unit.service;
//
//import com.senpare.authservice.BaseTest;
//import com.senpare.authservice.exception.BadRequestException;
//import com.senpare.authservice.exception.ResourceAlreadyExistsException;
//import com.senpare.authservice.exception.ResourceNotFoundException;
//import com.senpare.authservice.model.AppUser;
//import com.senpare.authservice.model.Role;
//import com.senpare.authservice.repository.AppUserRepository;
//import com.senpare.authservice.service.AppUserService;
//import com.senpare.authservice.service.RoleService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//class AppUserServiceTest extends BaseTest<AppUser> {
//
//
//    @Mock
//    private AppUserRepository appUserRepository;
//    @Mock
//    private RoleService roleService;
//    @Mock
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    @Captor
//    private ArgumentCaptor<String> emailArgumentCaptor;
//    private AppUserService underTest;
//    private AppUser appUser;
//
//
//    @BeforeEach
//    void setUp() {
//        underTest = new AppUserService(appUserRepository, roleService,  bCryptPasswordEncoder);
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
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        Role role = new Role()
//                .setRoleName("ROLE_USER");
//
//        // when
//        when(roleService.getRoleByName(anyString())).thenReturn(role);
//        underTest.create(appUser, true);
//
//        // then
//        then(appUserRepository).should().saveAndFlush(modelArgumentCaptor.capture());
//        AppUser capture = modelArgumentCaptor.getValue();
//        assertThat(capture).isEqualTo(appUser);
//    }
//
//    @Test
//    public void shouldNotCreateNullAppUser() {
//        assertThatThrownBy(() -> underTest.create(appUser, true))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("AppUser can't be null");
//
//        // finally
//        then(appUserRepository).should(never()).saveAndFlush(any());
//    }
//
//    @Test
//    public void shouldNotCreateAppUserWhenFirstNameIsNull() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(appUser, true))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Name can't be null or empty");
//
//        // finally
//        then(appUserRepository).should(never()).saveAndFlush(any());
//    }
//
//    @Test
//    public void shouldNotCreateAppUserWhenLastNameIsNull() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(appUser, true))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Name can't be null or empty");
//
//        // finally
//        then(appUserRepository).should(never()).saveAndFlush(any());
//    }
//
//    @Test
//    public void shouldNotCreateAppUserWhenEmailIsNull() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(appUser, true))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Email can't be null or empty");
//
//        // finally
//        then(appUserRepository).should(never()).saveAndFlush(any());
//    }
//
//    @Test
//    public void shouldNotCreateAppUserWhenPasswordIsNull() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(appUser, true))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Password can't be null or empty");
//
//        // finally
//        then(appUserRepository).should(never()).saveAndFlush(any());
//    }
//
//    @Test
//    public void shouldThrowWhenEmailIsTaken() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword1")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        AppUser appUser2 = new AppUser()
//                .setUuid(2L)
//                .setFirstName("Test2")
//                .setLastName("User2")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword2")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.of(appUser));
//
//        // then
//        assertThatThrownBy(() -> underTest.create(appUser2, true))
//                .isInstanceOf(ResourceAlreadyExistsException.class)
//                .hasMessageContaining("Email already taken");
//
//        // finally
//        then(appUserRepository).should(never()).save(any(AppUser.class));
//    }
//
//    @Test
//    public void shouldGetAppUserByEmail() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.of(appUser));
//        AppUser newAppUser = underTest.getAppUserEmail(appUser.getEmail());
//
//        // then
//        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
//        then(appUserRepository).should().findByEmail(emailCaptor.capture());
//        assertThat(emailCaptor.getValue()).isEqualTo(appUser.getEmail());
//        assertThat(newAppUser).isEqualTo(appUser);
//    }
//
//    @Test
//    public void shouldThrowIfAppUserNotFoundByEmail() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.empty());
//        assertThatThrownBy(() -> underTest.getAppUserEmail(appUser.getEmail()))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("User can't be found by email");
//
//        // then
//        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
//        then(appUserRepository).should().findByEmail(emailCaptor.capture());
//        assertThat(emailCaptor.getValue()).isEqualTo(appUser.getEmail());
//    }
//
//    @Test
//    public void shouldGetAppUserById() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(appUserRepository.findById(appUser.getUuid())).thenReturn(Optional.of(appUser));
//        AppUser actualAppUser = underTest.getAppUser(appUser.getUuid());
//
//        // then
//        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
//        then(appUserRepository).should().findById(idCaptor.capture());
//        assertThat(idCaptor.getValue()).isEqualTo(appUser.getUuid());
//        assertThat(actualAppUser).isEqualTo(appUser);
//
//    }
//
//    @Test
//    public void shouldThrowIfAppUserNotFoundById() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(appUserRepository.findById(appUser.getUuid())).thenReturn(Optional.empty());
//        assertThatThrownBy(() -> underTest.getAppUser(appUser.getUuid()))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("User can't be found by id");
//
//        // then
//        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
//        then(appUserRepository).should().findById(idCaptor.capture());
//        assertThat(idCaptor.getValue()).isEqualTo(appUser.getUuid());
//    }
//
//    @Test
//    public void shouldThrowWhenGettingAppUserWithNullId() {
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.getAppUser(null))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Id can't be null");
//
//
//        // finally
//        then(appUserRepository).should(never()).findById(anyLong());
//    }
//
//    @Test
//    public void shouldGetAppUsers() {
//        // given
//        List<AppUser> appUsers = List.of(
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
//        );
//
//        // when
//        when(appUserRepository.findAll()).thenReturn(appUsers);
//        List<AppUser> actualAppUsers = underTest.getAppUsers();
//
//        // then
//        then(appUserRepository).should().findAll();
//        assertThat(actualAppUsers).isEqualTo(appUsers);
//    }
//
//    @Test
//    public void shouldGetAppUsers_With_AppUsers() {
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
//        when(appUserRepository.findAll(any(Pageable.class))).thenReturn(appUsers);
//        Page<AppUser> actualAppUsers = underTest.getAppUsers(1, 2, "firstName");
//
//        // then
//        then(appUserRepository).should().findAll(any(Pageable.class));
//        assertThat(actualAppUsers).isEqualTo(appUsers);
//    }
//
//    @Test
//    void shouldUpdateAppUser() {
//        // given
//        appUser = new AppUser()
//                .setFirstName("Test1")
//                .setLastName("User1")
//                .setEmail("testuser1@test.com");
//        AppUser updatesAppUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test2")
//                .setLastName("User2")
//                .setEmail("testuser2@test.com")
//                .setPassword("testPassword2")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//
//        // when
//        when(appUserRepository.findById(anyLong())).thenReturn(Optional.of(updatesAppUser));
//        when(appUserRepository.save(any(AppUser.class))).thenReturn(updatesAppUser);
//        AppUser actual = underTest.update(1L, appUser);
//
//        // then
//        then(appUserRepository).should().findById(idArgumentCaptor.capture());
//        then(appUserRepository).should().save(modelArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(actual).isEqualTo(updatesAppUser);
//    }
//
//    @Test
//    void shouldThrowWhenUpdatingAppUserWithNullId() {
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.update(null, appUser))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Id can't be null");
//        then(appUserRepository).should(never()).findById(anyLong());
//        then(appUserRepository).should(never()).save(any(AppUser.class));
//    }
//
//    @Test
//    void shouldThrowWhenUpdatingAppUserWithNullAppUser() {
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.update(1L, null))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("AppUser can't be null");
//        then(appUserRepository).should(never()).findById(anyLong());
//        then(appUserRepository).should(never()).save(any(AppUser.class));
//    }
//
//    @Test
//    void shouldThrowWhenUpdatingAppUserWithTakenEmail() {
//        // given
//        appUser = new AppUser()
//                .setFirstName("Test1")
//                .setLastName("User1")
//                .setEmail("testuser@test.com");
//        AppUser updatesAppUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test1")
//                .setLastName("User1")
//                .setEmail("testuser2@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        AppUserService mockAppUserService = spy(underTest);
//
//        // when
//        doReturn(true).when(mockAppUserService).isEmailTaken(anyString());
//        when(appUserRepository.findById(anyLong())).thenReturn(Optional.of(updatesAppUser));
//
//        // then
//        assertThatThrownBy(() -> mockAppUserService.update(1L, appUser))
//                .isInstanceOf(ResourceAlreadyExistsException.class)
//                .hasMessageContaining("Email already taken");
//
//        //finally
//        then(appUserRepository).should(never()).save(any(AppUser.class));
//    }
//
//    // TODO: test updating individual fields (like firstName, lastName)
//
//    @Test
//    public void shouldDeleteAppUser() {
//        // given
//        Long id = 1L;
//        appUser = new AppUser()
//                .setUuid(id)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//
//        // when
//        when(appUserRepository.findById(id)).thenReturn(Optional.of(appUser));
//        underTest.delete(id);
//
//        //then
//        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
//        then(appUserRepository).should().findById(idCaptor.capture());
//        then(appUserRepository).should().deleteById(idCaptor.capture());
//
//        assertThat(idCaptor.getAllValues().get(0)).isEqualTo(id);
//        assertThat(idCaptor.getAllValues().get(1)).isEqualTo(id);
//    }
//
//    @Test
//    public void shouldThrowWhenDeletingAppUserThatDoesNotExist() {
//        // given
//        Long id = 1L;
//
//        // when
//        when(appUserRepository.findById(id)).thenReturn(Optional.empty());
//
//        //then
//        assertThatThrownBy(() -> underTest.delete(id))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("User can't be found by id");
//        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
//        then(appUserRepository).should().findById(idCaptor.capture());
//        assertThat(idCaptor.getValue()).isEqualTo(id);
//
//        // finally
//        then(appUserRepository).should(never()).deleteById(anyLong());
//    }
//
//    @Test
//    public void shouldThrowWhenDeletingAppUserWithNullId() {
//        // when
//        //then
//        assertThatThrownBy(() -> underTest.delete(null))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Id can't be null");
//
//
//        // finally
//        then(appUserRepository).should(never()).findById(anyLong());
//        then(appUserRepository).should(never()).deleteById(anyLong());
//    }
//
//    @Test
//    public void shouldReturnTrueForExistingEmail() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser));
//        boolean actual = underTest.isEmailTaken(appUser.getEmail());
//
//        // then
//        then(appUserRepository).should().findByEmail(emailArgumentCaptor.capture());
//        assertThat(emailArgumentCaptor.getValue()).isEqualTo(appUser.getEmail());
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldReturnFalseForExistingEmail() {
//        // given
//        appUser = new AppUser()
//                .setUuid(1L)
//                .setFirstName("Test")
//                .setLastName("User")
//                .setEmail("testuser1@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        boolean actual = underTest.isEmailTaken(appUser.getEmail());
//
//        // then
//        then(appUserRepository).should().findByEmail(emailArgumentCaptor.capture());
//        assertThat(emailArgumentCaptor.getValue()).isEqualTo(appUser.getEmail());
//        assertThat(actual).isFalse();
//    }
//
//    // TODO: test changePassword method
//}