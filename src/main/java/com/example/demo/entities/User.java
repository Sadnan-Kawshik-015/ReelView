package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Builder
public class User implements IMask, Cloneable {
    @Id
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(128)")
    private String id;

    @NotNull
    @Email
    @Size(max = 50)
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Size(max = 128)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 128)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(max = 128)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "password_reset_token", unique = true)
    private String passwordResetToken;

    @Column(name = "password_reset_token_expire_on")
    private LocalDateTime passwordResetTokenExpireOn;

    @Column(name = "failed_login_attempt")
    @NotNull
    private int failedLoginAttempt;

    @Column(name = "is_locked", columnDefinition = "BIT")
    @NotNull
    private boolean isLocked;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expire_on")
    private LocalDateTime otpExpireOn;

    @NotNull
    @Column(name = "is_active", columnDefinition = "BIT", nullable = false)
    private boolean isActive;

    @Column(name = "is_email_verified", columnDefinition = "BIT", nullable = false)
    private boolean isEmailVerified;

    @Size(max = 128)
    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "email_verification_token_expire_on")
    private LocalDateTime emailVerificationTokenExpireOn;

    @Column(name = "is_mobile_verified", columnDefinition = "BIT", nullable = false)
    private boolean isMobileVerified;

    @Size(max = 128)
    @Column(name = "mobile_verification_token")
    private String mobileVerificationToken;

    @Column(name = "mobile_verification_token_expire_on")
    private LocalDateTime mobileVerificationTokenExpireOn;

    @Column(name = "password_changed_on")
    private LocalDateTime passwordChangedOn;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String name, String email) {
        this.firstName = name;
        this.email = email;
    }

    @Override
    public Object getMasked(String maskValue) {
        this.password = maskValue;
        return this;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public String getFullName() {
        if (StringUtils.hasText(this.firstName) && StringUtils.hasText(this.lastName)) {
            String var10000 = this.firstName.trim();
            return var10000 + " " + this.lastName.trim();
        } else if (StringUtils.hasText(this.firstName)) {
            return this.firstName.trim();
        } else {
            return StringUtils.hasText(this.lastName) ? this.lastName.trim() : "";
        }
    }

}
