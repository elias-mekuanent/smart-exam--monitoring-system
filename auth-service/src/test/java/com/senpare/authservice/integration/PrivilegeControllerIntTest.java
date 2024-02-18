//package com.senpare.authservice.integration;
//
//
//import com.senpare.authservice.model.Privilege;
//import com.senpare.authservice.service.PrivilegeService;
//import org.junit.jupiter.api.Test;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@WithMockUser(authorities = {"ROLE_SUPER_ADMIN", "ROLE-CREATE", "ROLE-UPDATE", "ROLE-DELETE"})
//public class PrivilegeControllerIntTest extends  BaseIntTest<Privilege> {
//
//    private static final String SUB_URL = BASE_URL + "/user-service/privileges";
//
//    @MockBean
//    private PrivilegeService privilegeService;
//    private static final Privilege privilege = new Privilege()
//            .setPrivilegeName("testPrivilege")
//            .setPrivilegeDescription("testPrivilegeDescription")
//            .setCreatedOn(now)
//            .setModifiedOn(now);
//
//    public PrivilegeControllerIntTest() {
//        super(privilege);
//    }
//
//    @Test
//    public void shouldGetAllPrivileges() throws Exception {
//        // given
//        Page<Privilege> privileges = new PageImpl<Privilege>(List.of(
//                privilege,
//                new Privilege().setPrivilegeName("testPrivilege2")
//                        .setCreatedOn(now)
//                        .setModifiedOn(now)
//        ));
//        String expectedPrivileges = jsonMapper.writeValueAsString(privileges.getContent());
//
//        // when
//        when(privilegeService.getPrivileges(anyInt(), anyInt(), anyString())).thenReturn(privileges);
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(SUB_URL)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(request).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//
//        // then
//        then(privilegeService).should().getPrivileges(intArgumentCaptor.capture(), intArgumentCaptor.capture(), stringArgumentCaptor.capture());
//        assertThat(intArgumentCaptor.getAllValues().get(0)).isEqualTo(1);
//        assertThat(intArgumentCaptor.getAllValues().get(1)).isEqualTo(5);
//        assertThat(stringArgumentCaptor.getValue()).isEqualTo("privilegeName");
//        assertThat(response.getStatus()).isEqualTo(200);
//        JSONAssert.assertEquals(expectedPrivileges, response.getContentAsString(), true);
//    }
//
//    @Test
//    public void shouldGetPrivilege() throws Exception {
//        // given
//        String expectedPrivileges = jsonMapper.writeValueAsString(privilege);
//
//        // when
//        when(privilegeService.getPrivilege(anyLong())).thenReturn(privilege);
//        RequestBuilder request = MockMvcRequestBuilders
//                .get(SUB_URL + "/"+ 1)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(request).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//
//        // then
//        then(privilegeService).should().getPrivilege(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1);
//        assertThat(response.getStatus()).isEqualTo(200);
//        JSONAssert.assertEquals(expectedPrivileges, response.getContentAsString(), true);
//    }
//}
