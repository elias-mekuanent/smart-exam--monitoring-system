//package com.senpare.authservice.controller;
//
//import com.senpare.authservice.model.Privilege;
//import com.senpare.authservice.service.PrivilegeService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/v1/auth-service/privileges")
//@Tag(name = "Privilege endpoint", description = "CRUD operation on system role privileges")
//public class PrivilegeController {
//
//    private final PrivilegeService privilegeService;
//
//    @GetMapping
//    @Operation(summary = "Get all privileges")
//    @PreAuthorize("hasAuthority('PRIVILEGE-LIST')")
//    public ResponseEntity<List<Privilege>> getPrivileges(
//            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
//            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
//            @RequestParam(value = "sort", required = false, defaultValue = "privilegeName") String sort) {
//        List<Privilege> privileges = privilegeService.getPrivileges(page, size, sort).getContent();
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        responseHeaders.setAllow(Set.of(HttpMethod.GET));
//        return new ResponseEntity<>(privileges, responseHeaders, HttpStatus.OK);
//    }
//
//    @GetMapping("{uuid}")
//    @Operation(summary = "Get privilege by uuid")
//    @PreAuthorize("hasAuthority('PRIVILEGE-DETAIL')")
//    public ResponseEntity<Privilege> getPrivilege(@PathVariable("uuid") UUID uuid) {
//        Privilege privilege = privilegeService.getPrivilege(uuid);
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        responseHeaders.setAllow(Set.of(HttpMethod.GET));
//        return new ResponseEntity<>(privilege, responseHeaders, HttpStatus.OK);
//    }
//}
