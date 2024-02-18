//package com.senpare.authservice.integration;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyBoolean;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import java.util.UUID;
//
//import org.junit.jupiter.api.Test;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.senpare.authservice.dto.ChangePasswordDTO;
//import com.senpare.authservice.model.AppUser;
//import com.senpare.authservice.service.AppUserService;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@WithMockUser(roles = "SUPER_ADMIN")
//public class AppUserControllerIntTest extends BaseIntTest<AppUser> {
//
//    private static final String SUB_URL = BASE_URL + "/user-service/users";
//    private static final AppUser appUser = new AppUser()
//            .setUuid(UUID.randomUUID())
//            .setFirstName("Test")
//            .setLastName("User")
//            .setEmail("testuser1@test.com")
//            .setPassword("testPassword")
//            .setEnabled(true)
//            .setCreatedOn(now)
//            .setModifiedOn(now);
//    @MockBean
//    private AppUserService appUserService;
//
//    public AppUserControllerIntTest() {
//        super(appUser);
//    }
//
//    @Test
//    public void shouldGetAppUser() throws Exception {
//        // when
//        when(appUserService.getAppUser(any(UUID.class))).thenReturn(appUser);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get(SUB_URL + "/" + 1)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//        // then
//        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(result.getResponse().getContentAsString()).isEqualTo(expectedModel);
//    }
//
//    @Test
//    public void shouldGetAppUsers() throws Exception {
//        // given
//        Page<AppUser> appUsers = new PageImpl<>(List.of(
//                appUser,
//                new AppUser()
//                        .setUuid(2L)
//                        .setFirstName("Test2")
//                        .setLastName("User2")
//                        .setEmail("testuser2@test.com")
//                        .setPassword("testPassword")
//                        .setCreatedOn(now)
//                        .setModifiedOn(now)
//        ));
//        String expected = jsonMapper.writeValueAsString(appUsers.getContent());
//
//        // when
//        when(appUserService.getAppUsers(anyInt(), anyInt(), anyString())).thenReturn(appUsers);
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(SUB_URL)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(request).andReturn();
//
//        // then
//        then(appUserService).should().getAppUsers(anyInt(), anyInt(), anyString());
//        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(result.getResponse().getContentAsString()).isEqualTo(expected);
//    }
//
//    @Test
//    public void shouldCreateAppUser() throws Exception {
//        // when
//        when(appUserService.create(any(AppUser.class), anyBoolean())).thenReturn(appUser);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SUB_URL)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(expectedModel);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//
//        // then
//        then(appUserService).should().create(modelArgumentCaptor.capture(),  eq(true));
//        JSONAssert.assertEquals(jsonMapper.writeValueAsString(modelArgumentCaptor.getValue()), expectedModel, true);
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
//        JSONAssert.assertEquals(response.getContentAsString(), expectedModel, true);
//    }
//
//    @Test
//    public void shouldUpdateAppUser() throws Exception {
//        // given
//        AppUser candidateAppUser = new AppUser()
//                .setUuid(UUID.randomUUID())
//                .setFirstName("UpdateTest")
//                .setLastName("User")
//                .setEmail("updatedtest@test.com")
//                .setPassword("testPassword")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(appUserService.update(any(UUID.class), any(AppUser.class))).thenReturn(candidateAppUser);
//        RequestBuilder request = MockMvcRequestBuilders
//                .put(SUB_URL + "/" + 1)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(expectedModel);
//        MvcResult result = mockMvc.perform(request).andReturn();
//
//        // then
//        then(appUserService).should().update(idArgumentCaptor.capture(), modelArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1);
//        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
//        JSONAssert.assertEquals(expectedModel, jsonMapper.writeValueAsString(modelArgumentCaptor.getValue()), true);
//    }
//
//    @Test
//    public void shouldDeleteAppUser() throws Exception {
//        // when
//        RequestBuilder request = MockMvcRequestBuilders
//                .delete(SUB_URL + "/" + 1)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(request).andReturn();
//
//        // then
//        then(appUserService).should().delete(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }
//
//    @Test
//    public void shouldResetPassword() throws Exception {
//        // given
//        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
//        changePasswordDTO.setOldPassword("testPassword");
//        changePasswordDTO.setNewPassword("testPassword1");
//
//        // when
//        when(appUserService.changePassword(anyLong(), anyString(), anyString())).thenReturn(appUser);
//        RequestBuilder request = MockMvcRequestBuilders
//                .put(SUB_URL + "/" + 1 + "/reset-password")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(jsonMapper.writeValueAsString(changePasswordDTO));
//        MvcResult result = mockMvc.perform(request).andReturn();
//
//        // then
//        then(appUserService).should().changePassword(idArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1);
//        assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo("testPassword");
//        assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo("testPassword1");
//        assertThat(result.getResponse().getStatus()).isEqualTo(204);
//    }
//
//}
