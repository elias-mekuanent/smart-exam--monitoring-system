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
//import com.senpare.authservice.model.Privilege;
//import com.senpare.authservice.repository.PrivilegeRepository;
//import com.senpare.authservice.service.PrivilegeService;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//public class PrivilegeServiceTest extends BaseTest<Privilege> {
//
//    @Mock
//    private PrivilegeRepository privilegeRepository;
//
//    private PrivilegeService underTest;
//
//    private Privilege privilege;
//
//    @BeforeEach
//    void setUp() {
//        underTest = new PrivilegeService(privilegeRepository);
//    }
//
//    @Test
//    public void shouldCreatePrivilege() {
//        // given
//        privilege = new Privilege()
//                .setId(1L)
//                .setPrivilegeName("testPrivilege")
//                .setPrivilegeDescription("testPrivilegeDescription")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(privilegeRepository.save(any(Privilege.class))).thenReturn(privilege);
//        Privilege actual = underTest.create(privilege);
//
//        // then
//        then(privilegeRepository).should().save(modelArgumentCaptor.capture());
//        assertThat(modelArgumentCaptor.getValue()).isEqualTo(privilege);
//        assertThat(actual).isEqualTo(privilege);
//    }
//
//    @Test
//    public void shouldThrowWhenCreatingNullPrivilege() {
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.create(null))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Privilege can't be null");
//
//        // finally
//        then(privilegeRepository).should(never()).save(any(Privilege.class));
//    }
//
//    @Test
//    public void shouldThrowCreatingPrivilegeWithTakenPrivilegeName() {
//        PrivilegeService mockedPrivilegeService = spy(underTest);
//        // given
//        privilege = new Privilege()
//                .setId(1L)
//                .setPrivilegeName("testPrivilege")
//                .setPrivilegeDescription("testPrivilegeDescription")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//        // when
//        doReturn(true).when(mockedPrivilegeService).isPrivilegeNameTaken(anyString());
//
//        // then
//        assertThatThrownBy(() -> mockedPrivilegeService.create(privilege))
//                .isInstanceOf(ResourceAlreadyExistsException.class)
//                .hasMessageContaining("Privilege name already taken");
//
//        // finally
//        then(privilegeRepository).should(never()).save(any(Privilege.class));
//    }
//
//    @Test
//    public void shouldGetPrivileges() {
//        // given
//        List<Privilege> privileges = List.of(
//                new Privilege()
//                        .setId(1L)
//                        .setPrivilegeName("testPrivilege")
//                        .setPrivilegeDescription("testPrivilegeDescription")
//                        .setCreatedOn(now)
//                        .setModifiedOn(now),
//                new Privilege()
//                        .setId(2L)
//                        .setPrivilegeName("testPrivilege2")
//                        .setPrivilegeDescription("testPrivilegeDescription2")
//                        .setCreatedOn(now)
//                        .setModifiedOn(now)
//        );
//
//        // when
//        when(privilegeRepository.findAll()).thenReturn(privileges);
//        List<Privilege> actual = underTest.getPrivileges();
//
//        // then
//        then(privilegeRepository).should().findAll();
//        assertThat(actual).isEqualTo(privileges);
//    }
//
//    @Test
//    public void shouldGetPrivileges_With_Pagination() {
//        // given
//        Page<Privilege> privileges = new PageImpl<>(List.of(
//                new Privilege()
//                        .setId(1L)
//                        .setPrivilegeName("testPrivilege")
//                        .setPrivilegeDescription("testPrivilegeDescription")
//                        .setCreatedOn(now)
//                        .setModifiedOn(now),
//                new Privilege()
//                        .setId(2L)
//                        .setPrivilegeName("testPrivilege2")
//                        .setPrivilegeDescription("testPrivilegeDescription2")
//                        .setCreatedOn(now)
//                        .setModifiedOn(now)
//        ));
//
//        // when
//        when(privilegeRepository.findAll(any(Pageable.class))).thenReturn(privileges);
//        Page<Privilege> actual = underTest.getPrivileges(1, 2, "privilegeName");
//
//        // then
//        then(privilegeRepository).should().findAll(any(Pageable.class));
//        assertThat(actual).isEqualTo(privileges);
//    }
//
//    @Test
//    public void shouldGetPrivilege() {
//        // given
//        privilege = new Privilege()
//                .setId(1L)
//                .setPrivilegeName("testPrivilege")
//                .setPrivilegeDescription("testPrivilegeDescription")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(privilegeRepository.findById(anyLong())).thenReturn(Optional.of(privilege));
//        Privilege actual = underTest.getPrivilege(1L);
//
//        // then
//        then(privilegeRepository).should().findById(idArgumentCaptor.capture());
//        assertThat(idArgumentCaptor.getValue()).isEqualTo(1L);
//        assertThat(actual).isEqualTo(privilege);
//    }
//
//    @Test
//    public void shouldThrowWhenGettingPrivilegeWithNullId() {
//        // when
//        // then
//        assertThatThrownBy(() -> underTest.getPrivilege(null))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Privilege id can't be null");
//
//        // finally
//        then(privilegeRepository).should(never()).findById(anyLong());
//    }
//
//    @Test
//    public void shouldThrowWhenGettingPrivilegeThatDoesNotExist() {
//        //given
//        privilege = new Privilege()
//                .setId(1L)
//                .setPrivilegeName("testPrivilege")
//                .setPrivilegeDescription("testPrivilegeDescription")
//                .setCreatedOn(now)
//                .setModifiedOn(now);
//
//        // when
//        when(privilegeRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        // then
//        assertThatThrownBy(() -> underTest.getPrivilege(1L))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("Privilege can't be found by id");
//    }
//
//    // TODO add test case for IsPrivilegeNameTaken
//}
