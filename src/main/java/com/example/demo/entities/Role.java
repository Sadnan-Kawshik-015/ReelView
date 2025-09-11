package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.javers.core.metamodel.annotation.ShallowReference;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @NotNull
    @Id
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(128)")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @DiffIgnore
    @ShallowReference
    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    public Set<User> users = new HashSet<>();
}
