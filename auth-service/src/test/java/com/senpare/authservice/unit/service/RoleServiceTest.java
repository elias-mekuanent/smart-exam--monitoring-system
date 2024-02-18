//package com.senpare.authservice.unit.service;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import com.senpare.authservice.BaseTest;
//import com.senpare.authservice.exception.BadRequestException;
//import com.senpare.authservice.exception.ResourceAlreadyExistsException;
//import com.senpare.authservice.exception.ResourceNotFoundException;
//import com.senpare.authservice.model.Role;
//import com.senpare.authservice.repository.RoleRepository;
//import com.senpare.authservice.service.RoleService;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//public class RoleServiceTest extends BaseTest<Role> {
//
//    @Mock
//    private RoleRepository roleRepository;
//
//    private RoleService underTest;
//
//    private Role role;
//
//    @BeforeEach
//    void setUp() {
//        underTest = new RoleService(roleRepository);
//    }
//
//    @Test
//    public void shouldCreateRole() {
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleName("testRole")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(roleRepository.save(any(Role.class))).thenReturn(role);
//        Role actual = underTest.create(role);
//
//        // then
//        then(roleRepository).should().save(modelArgumentCaptor.capture());
//        assertThat(modelArgumentCaptor.getValue()).isEqualTo(role);
//        assertThat(actual).isEqualTo(role);
//    }
//
//    @Test
//    public void shouldThrowWhenCreatingNullRole() {
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(null))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Role can't be null");
//
//        // finally
//        then(roleRepository).should(never()).save(any(Role.class));
//    }
//
//    @Test
//    public void shouldThrowWhenCreatingRoleWhenWithNullRoleName() {
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(role))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Role name can't be null or empty");
//
//        // finally
//        then(roleRepository).should(never()).save(any(Role.class));
//    }
//
//    @Test
//    public void shouldThrowWhenCreatingRoleWithTakenName() {
//        RoleService mockedRoleService = spy(underTest);
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleName("testRole")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        doReturn(true).when(mockedRoleService).isRoleNameTaken(anyString());
//
//        // then
//        assertThatThrownBy(() -> mockedRoleService.create(role))
//                .isInstanceOf(ResourceAlreadyExistsException.class)
//                .hasMessageContaining("Role name already taken");
//
//        // finally
//        then(roleRepository).should(never()).save(any(Role.class));
//    }
//
//    @Test
//    public void shouldGetRoles() {
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleName("testRole")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        List<Role> roles = List.of(
//                role
//                , new Role()
//                        .setId(2L)
//                        .setRoleName("tesRole2")
//                        .setRoleDescription("testRoleDescription2")
//                        .setPrivileges(Set.of())
//                        .setCreatedOn(now)
//                        .setModifiedOn(now)
//        );
//
//        // when
//        when(roleRepository.findAll()).thenReturn(roles);
//        List<Role> actual = underTest.getRoles();
//
//        // then
//        then(roleRepository).should().findAll();
//        assertThat(actual).isEqualTo(roles);
//    }
//
//    @Test
//    public void shouldGetRoles_With_Pagination() {
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleName("testRole")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        Page<Role> roles = new PageImpl<>(List.of(role,
//                new Role()
//                .setId(2L)
//                .setRoleName("tesRole2")
//                .setRoleDescription("testRoleDescription2")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now)
//        ));
//
//        // when
//        when(roleRepository.findAll(any(Pageable.class))).thenReturn(roles);
//        Page<Role> actual = underTest.getRoles(1, 2, "roleName");
//
//        // then
//        then(roleRepository).should().findAll(any(Pageable.class));
//        assertThat(actual).isEqualTo(roles);
//    }
//
//    @Test
//    public void shouldGetRole() {
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleName("testRole")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
//        Role actual = underTest.getRole(1L);
//
//        // then
//        then(roleRepository).should().findById(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(actual).isEqualTo(role);
//    }
//
//    @Test
//    public void shouldThrowWhenGettingRoleWithNullId() {
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.getRole(null))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Id can't be null");
//
//        // finally
//        then(roleRepository).should(never()).findById(anyLong());
//    }
//
//    @Test
//    public void shouldThrowWhenGettingRoleThatDoesNotExist() {
//        // when
//        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
//        // then
//        assertThatThrownBy(() -> underTest.getRole(1L))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("Role can't be found by id");
//    }
//
//    @Test
//    public void shouldUpdateRole() {
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleName("testRole")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        Role updatedRole = new Role()
//                .setId(1L)
//                .setRoleName("testRole2")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
//        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);
//        Role actual = underTest.update(1L, role);
//
//        // then
//        then(roleRepository).should().findById(idArgumentCaptor.capture());
//        then(roleRepository).should().save(modelArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(modelArgumentCaptor.getValue()).isEqualTo(role);
//        assertThat(actual).isEqualTo(updatedRole);
//    }
//
//    @Test
//    public void shouldThrowWhenUpdatingRoleWithNullId() {
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleName("testRole")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.update(null, role))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Id can't be null");
//
//        // finally
//        then(roleRepository).should(never()).findById(anyLong());
//        then(roleRepository).should(never()).save(any(Role.class));
//    }
//
//    @Test
//    public void shouldThrowWhenUpdatingNullRole() {
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.update(1L, null))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Role can't be null");
//
//        // finally
//        then(roleRepository).should(never()).findById(anyLong());
//        then(roleRepository).should(never()).save(any(Role.class));
//    }
//
//    @Test
//    public void shouldThrowWhenUpdatingRoleWithTakenName() {
//        RoleService mockedRoleService = spy(underTest);
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleName("testRole")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
//        doReturn(true).when(mockedRoleService).isRoleNameTaken(anyString());
//
//        // then
//        assertThatThrownBy(() -> mockedRoleService.update(1L, role))
//                .isInstanceOf(ResourceAlreadyExistsException.class)
//                .hasMessageContaining("Role name already taken");
//        then(roleRepository).should().findById(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//
//        // finally
//        then(roleRepository).should(never()).save(any(Role.class));
//    }
//
//    @Test
//    public void shouldDeleteRole() {
//        RoleService mockedRoleService = spy(underTest);
//        // given
//        role = new Role()
//                .setId(1L)
//                .setRoleName("testRole")
//                .setRoleDescription("testRoleDescription")
//                .setPrivileges(Set.of())
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        doReturn(role).when(mockedRoleService).getRole(anyLong());
//        Role actual = mockedRoleService.delete(1L);
//
//        // then
//        then(roleRepository).should().deleteById(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(actual).isEqualTo(role);
//    }
//
//    @Test
//    public void shouldThrowWhenDeletingRoleWithNull() {
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.delete(1L))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("Role can't be found by id");
//
//        // finally
//        then(roleRepository).should(never()).deleteById(anyLong());
//    }
//
//}
