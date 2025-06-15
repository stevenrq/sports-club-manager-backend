package com.sportsclubmanager.backend.member.service;

import com.sportsclubmanager.backend.club.model.Club;
import com.sportsclubmanager.backend.club.repository.ClubRepository;
import com.sportsclubmanager.backend.member.exception.ClubAlreadyHasPlayerException;
import com.sportsclubmanager.backend.member.exception.PlayerAlreadyHasClubException;
import com.sportsclubmanager.backend.member.model.ClubAdministrator;
import com.sportsclubmanager.backend.member.model.Player;
import com.sportsclubmanager.backend.member.repository.ClubAdministratorRepository;
import com.sportsclubmanager.backend.member.repository.PlayerRepository;
import com.sportsclubmanager.backend.shared.exception.ResourceNotFoundException;
import com.sportsclubmanager.backend.shared.util.RoleAuthorityUtils;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.model.AffiliationStatus;
import com.sportsclubmanager.backend.user.repository.RoleRepository;
import com.sportsclubmanager.backend.user.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClubAdministratorService
        implements UserService<ClubAdministrator> {

    private final ClubAdministratorRepository clubAdministratorRepository;
    private final RoleRepository roleRepository;
    private final ClubRepository clubRepository;
    private final PlayerRepository playerRepository;

    private final PasswordEncoder passwordEncoder;

    public ClubAdministratorService(
            ClubAdministratorRepository clubAdministratorRepository,
            RoleRepository roleRepository,
            ClubRepository clubRepository,
            PlayerRepository playerRepository,
            PasswordEncoder passwordEncoder) {
        this.clubAdministratorRepository = clubAdministratorRepository;
        this.roleRepository = roleRepository;
        this.clubRepository = clubRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public ClubAdministrator save(ClubAdministrator clubAdministrator) {
        clubAdministrator.setRoles(
                RoleAuthorityUtils.getRoles(clubAdministrator, roleRepository));
        clubAdministrator.setPassword(
                passwordEncoder.encode(clubAdministrator.getPassword()));
        return clubAdministratorRepository.save(clubAdministrator);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClubAdministrator> findById(Long id) {
        return clubAdministratorRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClubAdministrator> findByUsername(String username) {
        return clubAdministratorRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubAdministrator> findAll() {
        return clubAdministratorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClubAdministrator> findAll(Pageable pageable) {
        return clubAdministratorRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Optional<ClubAdministrator> update(
            Long id,
            UserUpdateRequest userUpdateRequest) {
        Optional<ClubAdministrator> clubAdminOptional = clubAdministratorRepository.findById(id);

        if (clubAdminOptional.isPresent()) {
            ClubAdministrator clubAdminUpdated = clubAdminOptional.orElseThrow();

            clubAdminUpdated.setName(userUpdateRequest.getName());
            clubAdminUpdated.setLastName(userUpdateRequest.getLastName());
            clubAdminUpdated.setPhoneNumber(userUpdateRequest.getPhoneNumber());
            clubAdminUpdated.setEmail(userUpdateRequest.getEmail());
            clubAdminUpdated.setUsername(userUpdateRequest.getUsername());
            clubAdminUpdated.setRoles(
                    RoleAuthorityUtils.getRoles(clubAdminUpdated, roleRepository));

            return Optional.of(
                    clubAdministratorRepository.save(clubAdminUpdated));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        clubAdministratorRepository.deleteById(id);
    }

    @Override
    public boolean updateAffiliationStatus(
            Long id,
            AffiliationStatus affiliationStatus) {
        Optional<ClubAdministrator> clubAdminOptional = clubAdministratorRepository.findById(id);

        if (clubAdminOptional.isPresent()) {
            ClubAdministrator clubAdmin = clubAdminOptional.orElseThrow();
            clubAdmin.setAffiliationStatus(affiliationStatus);
            clubAdministratorRepository.save(clubAdmin);
            return true;
        }
        return false;
    }

    /**
     * Vincula un jugador a un club
     *
     * @param clubId   club al que el jugador se vinculará
     * @param playerId jugador a vincular
     * @throws ResourceNotFoundException     si el club o el jugador no se
     *                                       encuentran
     * @throws ClubAlreadyHasPlayerException si el club ya tiene al jugador
     *                                       especificado asociado
     * @throws PlayerAlreadyHasClubException si el jugador ya tiene algún club
     *                                       asociado
     */
    public void linkPlayerToClub(Long clubId, Long playerId) {
        Club club = clubRepository
                .findById(clubId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Club no encontrado con ID: " + clubId));

        Player player = playerRepository
                .findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Jugador no encontrado con ID: " + playerId));

        if (club.getPlayers().contains(player)) {
            throw new ClubAlreadyHasPlayerException(
                    "Club con ID: " +
                            clubId +
                            " ya tiene al jugador con ID: " +
                            playerId +
                            " asociado");
        } else if (player.getClub() != null) {
            throw new PlayerAlreadyHasClubException(
                    "Jugador con ID: " + playerId + " ya tiene un club asociado");
        }

        club.getPlayers().add(player);
        player.setClub(club);
        clubRepository.save(club);
    }
}
