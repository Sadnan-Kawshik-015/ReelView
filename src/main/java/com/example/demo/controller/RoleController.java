package com.example.demo.controller;

import com.example.demo.dtos.output.ResponseModelDTO;
import com.example.demo.dtos.output.RoleDTO;
import com.example.demo.service.RoleService;
import com.example.demo.utils.constants.CommonMessage;
import com.example.demo.utils.constants.HeaderName;
import com.example.demo.utils.enums.Status;
import com.example.demo.utils.enums.TokenType;
import com.sun.net.httpserver.Authenticator;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json;charset=UTF-8")
@Tag(name = "token management api")
@Validated
@RequiredArgsConstructor
public class RoleController extends BaseController {
    private final RoleService roleService;


    @Tags(value = @Tag(name = "RV008"))
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles() {
        try {
            List<RoleDTO> response = roleService.getAllRoles();

            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(Status.SUCCESS.getValue())
                            .message("Role fetched successfully")
                            .data(response)
                            .build()
            );
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    @Tags(value = @Tag(name = "RS009"))
    @GetMapping("/roles/{role_id}")
    public ResponseEntity<?> getRoleById(@PathVariable("role_id") String roleId) {
        try {
            RoleDTO response = roleService.getRoleById(roleId);
            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(Status.SUCCESS.getValue())
                            .message("Role fetched successfully")
                            .data(response)
                            .build()
            );
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    @Tags(value = @Tag(name = "RV010"))
    @DeleteMapping("/roles/{role_id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable("role_id") String roleId) {
        try {
            roleService.deleteRoleById(roleId);

            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(Status.SUCCESS.getValue())
                            .message("Role deleted successfully")
                            .data(null)
                            .build()
            );
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
}
