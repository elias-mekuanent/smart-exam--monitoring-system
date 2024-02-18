//package com.senpare.authservice.integration;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import java.util.Set;
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
//import com.senpare.authservice.model.Privilege;
//import com.senpare.authservice.model.Role;
//import com.senpare.authservice.service.RoleService;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@WithMockUser(authorities = {"ROLE_SUPER_ADMIN", "ROLE-CREATE", "ROLE-UPDATE", "ROLE-DELETE"})
//public class RoleControllerIntTest extends BaseIntTest<Role> {
//
//    private static final String SUB_URL = BASE_URL + "/user-service/roles";
//    private static final Set<Privilege> privileges = Set.of(new Privilege()
//            .setPrivilegeName("testPrivilege")
//            .setPrivilegeDescription("testPrivilegeDescription")
//            .setCreatedOn(now)
//            .setModifiedOn(now));
//    private static final Role role = new Role()
//            .setId(1L)
//            .setRoleName("testRole")
//            .setRoleDescription("testRoleDescription")
//            .setPrivileges(privileges)
//            .setCreatedOn(now)
//            .setModifiedOn(now);
//
//    @MockBean
//    private RoleService roleService;
//
//    public RoleControllerIntTest() {
//        super(role);
//    }
//
//    @Test
//    public void shouldCreateRole() throws Throwable {
//        // when
//        when(roleService.create(any(Role.class))).thenReturn(role);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post(SUB_URL)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonMapper.writeValueAsString(role));
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//
//
//        // then
//        then(roleService).should().create(modelArgumentCaptor.capture());
//        JSONAssert.assertEquals(expectedModel, jsonMapper.writeValueAsString(modelArgumentCaptor.getValue()), false);
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
//        JSONAssert.assertEquals(expectedModel, response.getContentAsString(), true);
//    }
//
//    @Test
//    public void shouldGetAppUser() throws Throwable {
//        // when
//        when(roleService.getRole(anyLong())).thenReturn(role);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get(SUB_URL + "/" + 1)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//
//        // then
//        then(roleService).should().getRole(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1);
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        JSONAssert.assertEquals(expectedModel, response.getContentAsString(), true);
//    }
//
//    @Test
//    public void shouldGetRoles() throws Exception {
//        // given
//        Role role2 = new Role()
//                .setId(1L)
//                .setRoleName("tesRole2")
//                .setRoleDescription("testRoleDescription2")
//                .setPrivileges(privileges)
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        Page<Role> roles = new PageImpl<>(List.of(role, role2));
//
//        String expectedRoles = jsonMapper.writeValueAsString(roles.getContent());
//
//        // when
//        when(roleService.getRoles(anyInt(), anyInt(), anyString())).thenReturn(roles);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get(SUB_URL)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//
//        // then
//        then(roleService).should().getRoles(anyInt(), anyInt(), anyString());
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        JSONAssert.assertEquals(expectedRoles, response.getContentAsString(), true);
//    }
//
//    @Test
//    public void shouldUpdateRole() throws Throwable {
//        // given
//        Role updatedRole = new Role()
//                .setId(1L)
//                .setRoleName("tesRole2")
//                .setRoleDescription("testRoleDescription2")
//                .setPrivileges(privileges)
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        String expectedUpdatedRole = jsonMapper.writeValueAsString(updatedRole);
//
//        // when
//        when(roleService.update(anyLong(), any(Role.class))).thenReturn(updatedRole);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .put(SUB_URL + "/" + 1)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(expectedModel);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//
//        // then
//        then(roleService).should().update(idArgumentCaptor.capture(), modelArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(jsonMapper.writeValueAsString(modelArgumentCaptor.getValue())).isEqualTo(expectedModel);
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        JSONAssert.assertEquals(expectedUpdatedRole, response.getContentAsString(), true);
//    }
//
//    @Test
//    public void shouldDeleteRole() throws Throwable {
//        // when
//        when(roleService.delete(anyLong())).thenReturn(role);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .delete(SUB_URL + "/" + 1)
//                .accept(MediaType.APPLICATION_JSON);
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//
//        // then
//        then(roleService).should().delete(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        assertThat(response.getContentAsString()).isEqualTo("");
//    }
//}
