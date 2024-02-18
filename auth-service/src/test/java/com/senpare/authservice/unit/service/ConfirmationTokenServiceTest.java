//package com.senpare.authservice.unit.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.when;
//
//import java.util.UUID;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.senpare.authservice.BaseTest;
//import com.senpare.authservice.exception.BadRequestException;
//import com.senpare.authservice.exception.ResourceAlreadyExistsException;
//import com.senpare.authservice.model.AppUser;
//import com.senpare.authservice.model.ConfirmationToken;
//import com.senpare.authservice.repository.ConfirmationTokenRepository;
//import com.senpare.authservice.service.ConfirmationTokenService;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//public class ConfirmationTokenServiceTest extends BaseTest<ConfirmationToken> {
//
//    @Mock
//    private ConfirmationTokenRepository confirmationTokenRepository;
//    private ConfirmationTokenService underTest;
//    private final String token = UUID.randomUUID().toString();
//
//    @BeforeEach
//    public void setUp() {
//        underTest = new ConfirmationTokenService(confirmationTokenRepository, fixedClock);
//    }
//
//    private final AppUser appUser = new AppUser()
//            .setUuid(1L)
//            .setFirstName("Test")
//            .setLastName("User")
//            .setEmail("testuser1@test.com")
//            .setPassword("testPassword")
//            .setCreatedOn(now)
//            .setModifiedOn(now);
//
//    private final ConfirmationToken inputConfirmationToken;
//
//    {
//        inputConfirmationToken = new ConfirmationToken()
//                .setId(1L)
//                .setToken(token)
//                .setAppUser(appUser)
//                .setExpiresAt(now)
//                .setCreatedOn(now)
//                .setModifiedOn(now)
//        ;
//    }
//
//
//    @Test
//    public void shouldCreateConfirmationToken() {
//        // given
//        ConfirmationTokenService mockedConfirmationTokenService = spy(underTest);
//        ConfirmationToken expectedConfirmationToken = new ConfirmationToken()
//                .setId(1L)
//                .setToken(token)
//                .setAppUser(appUser)
//                .setExpiresAt(now)
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        doReturn(false).when(mockedConfirmationTokenService).isTokenExist(anyString());
//        when(confirmationTokenRepository.save(any(ConfirmationToken.class))).thenReturn(inputConfirmationToken);
//        ConfirmationToken actual = mockedConfirmationTokenService.create(inputConfirmationToken);
//
//        // then
//        then(mockedConfirmationTokenService).should().isTokenExist(eq(expectedConfirmationToken.getToken()));
//        then(confirmationTokenRepository).should().save(eq(inputConfirmationToken));
//        assertThat(actual.getConfirmedAt()).isEqualTo(expectedConfirmationToken.getConfirmedAt());
//        assertThat(actual.getToken()).isEqualTo(expectedConfirmationToken.getToken());
//    }
//
//    @Test
//    public void shouldNotCreateConfirmationTokenIsNull() {
//        // given
//        ConfirmationToken confirmationToken = new ConfirmationToken()
//                .setId(1L)
//                .setAppUser(appUser)
//                .setExpiresAt(now)
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(confirmationToken))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Token can't be null or empty");
//
//        // finally
//        then(confirmationTokenRepository).should(never()).save(eq(inputConfirmationToken));
//    }
//
//    @Test
//    public void shouldNotCreateConfirmationTokenIsEmpty() {
//        // given
//        ConfirmationToken confirmationToken = new ConfirmationToken()
//                .setId(1L)
//                .setToken("")
//                .setAppUser(appUser)
//                .setExpiresAt(now)
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(confirmationToken))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Token can't be null or empty");
//
//        // finally
//        then(confirmationTokenRepository).should(never()).save(eq(inputConfirmationToken));
//    }
//
//    @Test
//    public void shouldNotCreateConfirmationExpiredAtIsNull() {
//        // given
//        ConfirmationToken confirmationToken = new ConfirmationToken()
//                .setId(1L)
//                .setToken(token)
//                .setAppUser(appUser)
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(confirmationToken))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Expired at field can't be null");
//
//        // finally
//        then(confirmationTokenRepository).should(never()).save(eq(inputConfirmationToken));
//    }
//
//    @Test
//    public void shouldNotCreateConfirmationIfTokenIsAlreadyConfirmed() {
//        // given
//        ConfirmationToken confirmationToken = new ConfirmationToken()
//                .setId(1L)
//                .setToken(token)
//                .setAppUser(appUser)
//                .setExpiresAt(now)
//                .setConfirmedAt(now)
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(confirmationToken))
//                .isInstanceOf(ResourceAlreadyExistsException.class)
//                .hasMessageContaining("Token is already confirmed");
//
//        // finally
//        then(confirmationTokenRepository).should(never()).save(eq(inputConfirmationToken));
//    }
//
//    @Test
//    public void shouldNotCreateConfirmationTokenIfTokenAlreadyExists() {
//        // given
//        ConfirmationTokenService mockedConfirmationTokenService = spy(underTest);
//        ConfirmationToken expectedConfirmationToken = new ConfirmationToken()
//                .setId(1L)
//                .setToken(token)
//                .setAppUser(appUser)
//                .setExpiresAt(now)
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        // then
//        doReturn(true).when(mockedConfirmationTokenService).isTokenExist(anyString());
//        assertThatThrownBy(() -> mockedConfirmationTokenService.create(inputConfirmationToken))
//                .isInstanceOf(ResourceAlreadyExistsException.class)
//                .hasMessageContaining("Token already exists");
//        then(mockedConfirmationTokenService).should().isTokenExist(eq(expectedConfirmationToken.getToken()));
//
//        // finally
//        then(confirmationTokenRepository).should(never()).save(any(ConfirmationToken.class));
//    }
//
//}
