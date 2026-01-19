package com.bookverse.repository;

import com.bookverse.entity.User;
import com.bookverse.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.isActive = true")
    List<User> findAllActiveUsers();

    // Admin queries - Search và filter users
    @Query(
            value = """
    SELECT *
    FROM users u
    WHERE (
        :keyword IS NULL OR
        u.username ILIKE CONCAT('%', CAST(:keyword AS TEXT), '%') OR
        u.email ILIKE CONCAT('%', CAST(:keyword AS TEXT), '%') OR
        u.full_name ILIKE CONCAT('%', CAST(:keyword AS TEXT), '%')
    )
    AND (:role IS NULL OR u.role = :role)
    AND (:isActive IS NULL OR u.is_active = :isActive)
    AND (:isDeleted IS NULL OR u.is_deleted = :isDeleted)
    ORDER BY u.createdDate DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM users u
    WHERE (
        :keyword IS NULL OR
        u.username ILIKE CONCAT('%', CAST(:keyword AS TEXT), '%') OR
        u.email ILIKE CONCAT('%', CAST(:keyword AS TEXT), '%') OR
        u.full_name ILIKE CONCAT('%', CAST(:keyword AS TEXT), '%')
    )
    AND (:role IS NULL OR u.role = :role)
    AND (:isActive IS NULL OR u.is_active = :isActive)
    AND (:isDeleted IS NULL OR u.is_deleted = :isDeleted)
    """,
            nativeQuery = true
    )
    Page<User> searchUsers(
            @Param("keyword") Object keyword,
            @Param("role") String role,
            @Param("isActive") Boolean isActive,
            @Param("isDeleted") Boolean isDeleted,
            Pageable pageable
    );

    // Statistics queries
    @Query("SELECT COUNT(u) FROM User u WHERE u.isDeleted = false")
    Long countTotalUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true AND u.isDeleted = false")
    Long countActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = false AND u.isDeleted = false")
    Long countBlockedUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isDeleted = true")
    Long countDeletedUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isDeleted = false")
    Long countByRole(@Param("role") Role role);
}
