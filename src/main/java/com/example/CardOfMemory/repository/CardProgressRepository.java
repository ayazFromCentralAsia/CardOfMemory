package com.example.CardOfMemory.repository;


import com.example.CardOfMemory.entity.Card;
import com.example.CardOfMemory.entity.CardProgress;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardProgressRepository extends JpaRepository<CardProgress, UUID> {
    
    @Query("SELECT cp.card FROM CardProgress cp WHERE cp.user.id = :userId")
    List<Card> findCardsByUserId(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CardProgress cp WHERE cp.card.id = :cardId AND cp.user.id = :userId")
    void deleteByCardIdAndUserId(@Param("cardId") UUID cardId, @Param("userId") UUID userId);
}
