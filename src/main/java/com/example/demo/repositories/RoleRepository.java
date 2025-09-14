package com.example.demo.repositories;

import com.example.demo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Optional<Role> findById(String id);
    @Query(value = "SELECT distinct r FROM Role r " +
            "LEFT JOIN fetch  r.users users " +
            "WHERE r.id =:role_id ")
    Optional<Role> getRoleById(String role_id);
}
