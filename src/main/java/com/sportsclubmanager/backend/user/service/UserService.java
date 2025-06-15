package com.sportsclubmanager.backend.user.service;

import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.model.AffiliationStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * UserService define las operaciones que se pueden realizar sobre las entidades
 * de usuario. Es una interfaz genérica que puede ser implementada para un tipo
 * específico de usuario.
 *
 * @param <T> el tipo de la entidad de usuario
 */
public interface UserService<T> {
    T save(T t);

    Optional<T> findById(Long id);

    Optional<T> findByUsername(String username);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    Optional<T> update(Long id, UserUpdateRequest userUpdateRequest);

    void deleteById(Long id);

    boolean updateAffiliationStatus(
            Long id,
            AffiliationStatus affiliationStatus);
}
