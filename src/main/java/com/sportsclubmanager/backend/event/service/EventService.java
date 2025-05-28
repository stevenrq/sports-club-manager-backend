package com.sportsclubmanager.backend.event.service;

import com.sportsclubmanager.backend.event.dto.EventUpdateRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz de servicio para gestionar eventos.
 *
 * @param <T> tipo de evento
 */
public interface EventService<T> {
    /**
     * Guarda un nuevo evento.
     *
     * @param t evento a guardar
     * @return evento guardado
     */
    T save(T t);

    /**
     * Busca un evento por su ID.
     *
     * @param id identificador del evento
     * @return evento encontrado envuelto en Optional
     */
    Optional<T> findById(Long id);

    /**
     * Obtiene todos los eventos.
     *
     * @return lista de eventos
     */
    List<T> findAll();

    /**
     * Obtiene eventos de forma paginada.
     *
     * @param pageable información de paginación
     * @return página de eventos
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Actualiza un evento existente.
     *
     * @param id                 identificador del evento a actualizar
     * @param eventUpdateRequest datos de actualización
     * @return evento actualizado envuelto en Optional
     */
    Optional<T> update(Long id, EventUpdateRequest eventUpdateRequest);

    /**
     * Elimina un evento por su ID.
     *
     * @param id identificador del evento a eliminar
     */
    void deleteById(Long id);
}
