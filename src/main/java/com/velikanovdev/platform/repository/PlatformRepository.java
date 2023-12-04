package com.velikanovdev.platform.repository;

import com.velikanovdev.platform.entity.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformRepository extends JpaRepository<Snippet, Integer> {
    List<Snippet> findAllByOrderByDateDesc();
}
