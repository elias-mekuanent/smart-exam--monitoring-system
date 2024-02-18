//package com.senpare.authservice.unit.service;//package com.senpare.authservice.unit.service;
////
////import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
////import static org.mockito.ArgumentMatchers.any;
////import static org.mockito.ArgumentMatchers.anyString;
////import static org.mockito.BDDMockito.then;
////import static org.mockito.Mockito.doReturn;
////import static org.mockito.Mockito.spy;
////import static org.mockito.Mockito.when;
////
////import java.time.LocalDateTime;
////import java.util.UUID;
////
////import org.junit.jupiter.api.BeforeEach;
////import org.junit.jupiter.api.Test;
////import org.mockito.ArgumentCaptor;
////import org.mockito.Mock;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.boot.test.context.SpringBootTest;
////
////import com.senpare.authservice.BaseTest;
////import com.senpare.authservice.dto.RegistrationDTO;
////import com.senpare.authservice.model.AppUser;
////import com.senpare.authservice.model.ConfirmationToken;
////import com.senpare.authservice.service.AppUserService;
////import com.senpare.authservice.service.ConfirmationTokenService;
////import com.senpare.authservice.service.RegistrationService;
////import com.senpare.authservice.utilities.EmailTemplate;
////
////@SpringBootTest
////public class RegistrationServiceTest extends BaseTest<AppUser> {
////
////    @Mock
////    private AppUserService appUserService;
////    @Mock
////    private EmailValidator emailValidator;
////    @Mock
////    private EmailService emailService;
////    @Mock
////    private EmailTemplate emailTemplate;
////    @Mock
////    private ConfirmationTokenService confirmationTokenService;
////    @Value(value = "${app.base-url}")
////    String baseUrl;
////    @Value(value = "${app.setToken-expiration-delay}")
////    int tokenExpirationDelay;
////    private RegistrationService underTest;
////
////
////    @BeforeEach
////    public void setUp() {
////        underTest = new RegistrationService(appUserService,
////                emailValidator,
////                emailService,
////                emailTemplate,
////                confirmationTokenService,
////                baseUrl,
////                tokenExpirationDelay,
////                fixedClock);
////    }
////
////    @Test
////    public void shouldRegister() {
////        // given
////        RegistrationService mockedRegistrationService = spy(underTest);
////        RegistrationDTO registrationDTO = new RegistrationDTO();
////        registrationDTO.setFirstName("Test");
////        registrationDTO.setLastName("User");
////        registrationDTO.setEmail("testuser@example.com");
////        registrationDTO.setPassword("testPassword");
////        AppUser appUser = new AppUser()
////                .setFirstName(registrationDTO.getFirstName())
////                .setLastName(registrationDTO.getLastName())
////                .setEmail(registrationDTO.getEmail())
////                .setPassword(registrationDTO.getPassword())
////                .enabled(false)
////                .build();
////        ConfirmationToken confirmationToken = new ConfirmationToken()
////                        .setToken(UUID.randomUUID().toString())
////                        .setAppUser(appUser)
////                        .expiresAt(LocalDateTime.now(fixedClock).plusMinutes(tokenExpirationDelay))
////                        .build();
////
////        // when
////        when(emailValidator.test(anyString())).thenReturn(true);
////        when(confirmationTokenService.create(any(ConfirmationToken.class))).thenReturn(confirmationToken);
////        doReturn(confirmationToken.getToken()).when(mockedRegistrationService).createConfirmationToken(any(AppUser.class));
////        AppUser actual = mockedRegistrationService.register(registrationDTO);
////
////        // then
////        ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
////        then(emailValidator).should().test(emailArgumentCaptor.capture());
////        assertThat(emailArgumentCaptor.getValue()).isEqualTo(registrationDTO.getEmail());
////        assertThat(actual).isEqualTo(appUser);
////    }
////}
