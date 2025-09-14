package com.example.demo.controller;

import com.example.demo.dtos.input.LoginRequestDTO;
import com.example.demo.dtos.output.LoginResponseDTO;
import com.example.demo.dtos.output.ResponseModelDTO;
import com.example.demo.dtos.output.UserResponseDTO;
import com.example.demo.service.AdminUserService;
import com.example.demo.utils.constants.CommonMessage;
import com.example.demo.utils.enums.Status;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json;charset=UTF-8")
@Tag(name = "auth management api")
@Validated
@RequiredArgsConstructor
public class AdminUserController extends BaseController {
    private final AdminUserService adminUserService;


    @Tags(value = @Tag(name = "RV005"))
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponseDTO> response = adminUserService.getAllUsers();
            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(Status.SUCCESS.getValue())
                            .message(CommonMessage.USER_FETCHED_SUCCESSFULLY)
                            .data(response)
                            .build()
            );
            
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    @Tags(value = @Tag(name = "RV006"))
    @GetMapping("/users/{role_id}")
    public ResponseEntity<?> getUserByRoleID(@PathVariable("role_id") String roleId) {
        try {
            List<UserResponseDTO> response = adminUserService.getUsersByRole(roleId);
            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(Status.SUCCESS.getValue())
                            .message(CommonMessage.USER_FETCHED_SUCCESSFULLY)
                            .data(response)
                            .build()
            );

        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    @Tags(value = @Tag(name = "RV006"))
    @GetMapping("/users/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable("user_id") String userId) {
        try {
            UserResponseDTO response = adminUserService.getUserById(userId);
            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(Status.SUCCESS.getValue())
                            .message(CommonMessage.USER_FETCHED_SUCCESSFULLY)
                            .data(response)
                            .build()
            );

        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }

    @Tags(value = @Tag(name = "RV007"))
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("user_id") String userId) {
        try {
            adminUserService.deleteUserById(userId);
            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(Status.SUCCESS.getValue())
                            .message("Users deleted successfully")
                            .data(null)
                            .build()
            );

        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    
}
