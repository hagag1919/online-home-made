package com.example.user.repo;

import com.example.user.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Account entity that provides CRUD operations
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    /**
     * Find an account by its username
     * 
     * @param username the username to search for
     * @return Optional containing the account if found
     */
    Optional<Account> findByUsername(String username);

    List<Account> findAll();

    Optional<Account> findById(Long id);
    
    /**
     * Check if an account with the given username exists
     * 
     * @param username the username to check
     * @return true if an account with the username exists, false otherwise
     */
    boolean existsByUsername(String username);

    Double getUserBalance(Long id);

    @Modifying
    @Transactional
    @Query(name = "Account.updateUserBalance")
    void updateUserBalance(@Param("id") Long id, @Param("balance") Double balance);
}
