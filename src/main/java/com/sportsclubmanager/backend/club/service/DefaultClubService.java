package com.sportsclubmanager.backend.club.service;

import java.util.List;
import java.util.Optional;

import com.sportsclubmanager.backend.exception.ClubDeletingException;
import com.sportsclubmanager.backend.member.model.ClubAdministrator;
import com.sportsclubmanager.backend.member.service.ClubAdministratorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sportsclubmanager.backend.club.dto.ClubUpdateRequest;
import com.sportsclubmanager.backend.club.model.Club;
import com.sportsclubmanager.backend.club.repository.ClubRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultClubService implements BaseClubService {

    private final ClubAdministratorService clubAdministratorService;

    private final ClubRepository clubRepository;

    public DefaultClubService(ClubRepository clubRepository, ClubAdministratorService clubAdministratorService) {
        this.clubRepository = clubRepository;
        this.clubAdministratorService = clubAdministratorService;
    }

    /**
     * Guarda un club en la base de datos y lo asocia con un administrador de club.
     *
     * @param club        El objeto `Club` que se desea guardar.
     * @param clubAdminId El ID del administrador del club que se asociará con el club.
     * @return El objeto `Club` guardado en la base de datos.
     * @throws IllegalArgumentException Si el ID del administrador del club es nulo,
     *                                  si no se encuentra un administrador con el ID proporcionado,
     *                                  o si el administrador ya tiene un club asignado.
     */
    @Override
    public Club save(Club club, Long clubAdminId) {
        if (clubAdminId == null) {
            throw new IllegalArgumentException("Club Administrator ID must not be null");
        }

        Optional<ClubAdministrator> clubAdminOptional = clubAdministratorService.findById(clubAdminId);
        if (clubAdminOptional.isEmpty()) {
            throw new IllegalArgumentException("Club Administrator with ID " + clubAdminId + " not found");
        }

        ClubAdministrator clubAdmin = clubAdminOptional.orElseThrow();
        if (clubAdmin.getClub() != null) {
            throw new IllegalArgumentException("Club Administrator already has a club assigned with ID " + clubAdmin.getClub().getId());
        }

        club.setClubAdministrator(clubAdmin);
        clubAdmin.setClub(club);

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
