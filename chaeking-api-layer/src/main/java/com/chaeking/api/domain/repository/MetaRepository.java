package com.chaeking.api.domain.repository;

import com.chaeking.api.domain.entity.Meta;
import com.chaeking.api.value.enumerate.MetaType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetaRepository extends JpaRepository<Meta, Long> {
    Optional<Meta> findByType(MetaType type);
}
