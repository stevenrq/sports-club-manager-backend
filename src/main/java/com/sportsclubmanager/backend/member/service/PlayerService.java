package com.sportsclubmanager.backend.member.service;

import com.sportsclubmanager.backend.event.model.Tournament;
import com.sportsclubmanager.backend.event.model.Training;
import com.sportsclubmanager.backend.event.repository.TournamentRepository;
import com.sportsclubmanager.backend.event.repository.TrainingRepository;
import com.sportsclubmanager.backend.member.exception.MaximumParticipantsException;
import com.sportsclubmanager.backend.member.exception.PlayerAlreadyHasTournamentEventException;
import com.sportsclubmanager.backend.member.exception.PlayerAlreadyHasTrainingEventException;
import com.sportsclubmanager.backend.member.model.Player;
import com.sportsclubmanager.backend.member.repository.PlayerRepository;
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
public class PlayerService implements UserService<Player> {

    private final PlayerRepository playerRepository;
    private final RoleRepository roleRepository;
    private final TournamentRepository tournamentRepository;
    private final TrainingRepository trainingRepository;

    private final PasswordEncoder passwordEncoder;

    public PlayerService(
            PlayerRepository playerRepository,
            RoleRepository roleRepository,
            TournamentRepository tournamentRepository,
            TrainingRepository trainingRepository,
            PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.roleRepository = roleRepository;
        this.tournamentRepository = tournamentRepository;
        this.trainingRepository = trainingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Player save(Player player) {
        player.setRoles(RoleAuthorityUtils.getRoles(player, roleRepository));
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        return playerRepository.save(player);
    }

    @Override
    public Optional<Player> findById(Long id) {
        return playerRepository.findById(id);
    }

    @Override
    public Optional<Player> findByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public Page<Player> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Override
    public Optional<Player> update(
            Long id,
            UserUpdateRequest userUpdateRequest) {
        Optional<Player> playerOptional = playerRepository.findById(id);

        if (playerOptional.isPresent()) {
            Player playerUpdated = playerOptional.orElseThrow();

            playerUpdated.setName(userUpdateRequest.getName());
            playerUpdated.setLastName(userUpdateRequest.getLastName());
            playerUpdated.setPhoneNumber(userUpdateRequest.getPhoneNumber());
            playerUpdated.setEmail(userUpdateRequest.getEmail());
            playerUpdated.setUsername(userUpdateRequest.getUsername());
            playerUpdated.setRoles(
                    RoleAuthorityUtils.getRoles(playerUpdated, roleRepository));

            return Optional.of(playerRepository.save(playerUpdated));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public boolean updateAffiliationStatus(
            Long id,
            AffiliationStatus affiliationStatus) {
        Optional<Player> playerOptional = playerRepository.findById(id);

        if (playerOptional.isPresent()) {
            Player player = playerOptional.orElseThrow();
            player.setAffiliationStatus(affiliationStatus);
            playerRepository.save(player);
            return true;
        }
        return false;
    }

    /**
     * Registra a un jugador en un evento de torneo.
     *
     * @param playerId          El ID del jugador a registrar
     * @param tournamentEventId El ID del evento de torneo
     * @return true si el registro fue exitoso
     * @throws IllegalArgumentException                 si el jugador o torneo no
     *                                                  existe
     * @throws PlayerAlreadyHasTournamentEventException si el jugador ya está
     *                                                  registrado en el torneo
     * @throws MaximumParticipantsException             si el torneo alcanzó el
     *                                                  máximo de participantes
     */
    public boolean registerInTournamentEvent(
            Long playerId,
            Long tournamentEventId) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(
                tournamentEventId);

        Player player;
        Tournament tournament;

        if (playerOptional.isEmpty()) {
            throw new IllegalArgumentException(
                    "Jugador con ID " + playerId + " no encontrado");
        } else {
            player = playerOptional.orElseThrow();
        }

        if (tournamentOptional.isEmpty()) {
            throw new IllegalArgumentException(
                    "Evento de torneo con ID " +
                            tournamentEventId +
                            " no encontrado");
        } else {
            tournament = tournamentOptional.orElseThrow();
        }

        if (player.getEvents().contains(tournament)) {
            throw new PlayerAlreadyHasTournamentEventException(
                    "Jugador con ID: " +
                            playerId +
                            " ya tiene evento de torneo con ID: " +
                            tournament.getId() +
                            " asociado");
        }
        if (tournament.getPlayers().size() >= tournament.getMaximumParticipants()) {
            throw new MaximumParticipantsException(
                    "Los participantes no deben ser mayores que " +
                            tournament.getMaximumParticipants());
        }

        player.getEvents().add(tournament);
        tournament.getPlayers().add(player);
        playerRepository.save(player);

        return true;
    }

    /**
     * Registra a un jugador en un evento de entrenamiento.
     *
     * @param playerId        El ID del jugador a registrar
     * @param trainingEventId El ID del evento de entrenamiento
     * @return true si el registro fue exitoso
     * @throws IllegalArgumentException               si el jugador o
     *                                                entrenamiento no existe
     * @throws PlayerAlreadyHasTrainingEventException si el jugador ya está
     *                                                registrado en el
     *                                                entrenamiento
     * @throws MaximumParticipantsException           si el entrenamiento alcanzó
     *                                                el máximo de participantes
     */
    public boolean registerInTrainingEvent(
            Long playerId,
            Long trainingEventId) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);
        Optional<Training> trainingOptional = trainingRepository.findById(
                trainingEventId);

        Player player;
        Training training;

        if (playerOptional.isEmpty()) {
            throw new IllegalArgumentException(
                    "Jugador con ID " + playerId + " no encontrado");
        } else {
            player = playerOptional.orElseThrow();
        }

        if (trainingOptional.isEmpty()) {
            throw new IllegalArgumentException(
                    "Evento de entrenamiento con ID " +
                            trainingEventId +
                            " no encontrado");
        } else {
            training = trainingOptional.orElseThrow();
        }

        if (player.getEvents().contains(training)) {
            throw new PlayerAlreadyHasTrainingEventException(
                    "Jugador con ID: " +
                            playerId +
                            " ya tiene evento de entrenamiento con ID: " +
                            training.getId() +
                            " asociado");
        }

        if (training.getPlayers().size() >= training.getMaximumParticipants()) {
            throw new MaximumParticipantsException(
                    "Los participantes no deben ser mayores que " +
                            training.getMaximumParticipants());
        }

        player.getEvents().add(training);
        training.getPlayers().add(player);
        playerRepository.save(player);

        return true;
    }
}
