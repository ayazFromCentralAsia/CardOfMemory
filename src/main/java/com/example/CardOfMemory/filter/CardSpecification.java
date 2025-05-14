package com.example.CardOfMemory.filter;

import com.example.CardOfMemory.entity.Card;
import com.example.CardOfMemory.entity.CardProgress;
import com.example.CardOfMemory.enums.Topic;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CardSpecification {

    public static Specification<Card> hasTopic(Topic topic) {
        return (root, query, cb) -> topic == null ? null : cb.equal(root.get("topic"), topic);
    }

    public static Specification<Card> hasDifficulty(Integer difficulty) {
        return (root, query, cb) -> difficulty == null ? null : cb.equal(root.get("difficulty"), difficulty);
    }

    public static Specification<Card> hasOwner(UUID ownerId) {
        return (root, query, cb) -> ownerId == null ? null : cb.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Card> hasTextLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) return null;
            String like = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("question")), like),
                    cb.like(cb.lower(root.get("answer")), like)
            );
        };
    }

    public static Specification<Card> knownByUser(Boolean known, UUID userId) {
        return (root, query, cb) -> {
            if (known == null || userId == null) return null;
            Join<Card, CardProgress> progressJoin = root.join("progressList", JoinType.LEFT);
            return cb.and(
                    cb.equal(progressJoin.get("user").get("id"), userId),
                    cb.equal(progressJoin.get("known"), known)
            );
        };
    }
}
