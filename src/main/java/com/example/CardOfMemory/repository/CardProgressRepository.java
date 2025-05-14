package com.example.CardOfMemory.repository;


import com.example.CardOfMemory.entity.CardProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardProgressRepository extends JpaRepository<CardProgress, UUID> {
}
