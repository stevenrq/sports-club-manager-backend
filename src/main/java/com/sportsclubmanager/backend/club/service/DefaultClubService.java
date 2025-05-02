package com.sportsclubmanager.backend.club.service;

import java.util.List;
import java.util.Optional;

import com.sportsclubmanager.backend.exception.ClubDeletingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sportsclubmanager.backend.club.dto.ClubUpdateRequest;
import com.sportsclubmanager.backend.club.model.Club;
import com.sportsclubmanager.backend.club.repository.ClubRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultClubService implements BaseClubService {

    private final ClubRepository clubRepository;

    public DefaultClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public Club save(Club club) {
        return clubRepository.save(club);
    }

    @Override
    public Optional<Club> findById(Long id) {
        return clubRepository.findById(id);
    }

    @Override
    public Optional<Club> findByName(String name) {
        return clubRepository.findByName(name);
    }

    @Override
    public List<Club> findAll() {
        return clubRepository.findAll();
    }

    @Override
    public Page<Club> findAll(Pageable pageable) {
        return clubRepository.findAll(pageable);
    }

    @Override
    public Optional<Club> update(Long id, ClubUpdateRequest clubUpdateRequest) {
        Optional<Club> clubOptional = clubRepository.findById(id);

        if (clubOptional.isPresent()) {
            Club clubUpdated = clubOptional.orElseThrow();

            clubUpdated.setName(clubUpdateRequest.getName());
            clubUpdated.setAddress(clubUpdateRequest.getAddress());
            clubUpdated.setPhoneNumber(clubUpdateRequest.getPhoneNumber());
            return Optional.of(clubRepository.save(clubUpdated));
        }
        return Optional.empty();
    }

    /**
     * Elimina un club por su ID.
     *
     * @param id El ID del club a eliminar.
     * @throws ClubDeletingException Si ocurre un error al desvincular las relaciones del club.
     * @apiNote Se debe tener en cuenta que el id del club en las tablas relacionadas (miembros) serán nulos.
     */
    @Override
    public void deleteById(Long id) {
        Optional<Club> clubOptional = clubRepository.findById(id);
        if (clubOptional.isPresent()) {
            Club clubToDelete = clubOptional.orElseThrow();
            try {
                clubToDelete.getClubAdministrator().setClub(null);
                clubToDelete.getCoaches().forEach(coach -> coach.setClub(null));
                clubToDelete.getPlayers().forEach(player -> player.setClub(null));
            } catch (Exception e) {
                throw new ClubDeletingException("Error while deleting club", e);
            }
            clubRepository.deleteById(id);
        }
    }
}
