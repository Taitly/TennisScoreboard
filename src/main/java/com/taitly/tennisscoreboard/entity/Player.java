package com.taitly.tennisscoreboard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Transient
    private final UUID tempId = UUID.randomUUID();

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (id != null && player.id != null) {
            return id.equals(player.id);
        }

        return tempId.equals(player.tempId);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : tempId.hashCode();
    }
}