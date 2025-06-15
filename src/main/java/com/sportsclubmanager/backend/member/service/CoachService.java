package com.sportsclubmanager.backend.member.service;

import com.sportsclubmanager.backend.member.model.Coach;
import com.sportsclubmanager.backend.member.repository.CoachRepository;
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
public class CoachService implements UserService<Coach> {

    private final CoachRepository coachRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public CoachService(
            CoachRepository coachRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.coachRepository = coachRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Coach save(Coach coach) {
        coach.setRoles(RoleAuthorityUtils.getRoles(coach, roleRepository));
        coach.setPassword(passwordEncoder.encode(coach.getPassword()));
        return coachRepository.save(coach);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Coach> findById(Long id) {
        return coachRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Coach> findByUsername(String username) {
        return coachRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coach> findAll() {
        return coachRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Coach> findAll(Pageable pageable) {
        return coachRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Optional<Coach> update(
            Long id,
            UserUpdateRequest userUpdateRequest) {
        Optional<Coach> coachOptional = coachRepository.findById(id);

        if (coachOptional.isPresent()) {
            Coach coachUpdated = coachOptional.orElseThrow();

            coachUpdated.setName(userUpdateRequest.getName());
            coachUpdated.setLastName(userUpdateRequest.getLastName());
            coachUpdated.setPhoneNumber(userUpdateRequest.getPhoneNumber());
            coachUpdated.setEmail(userUpdateRequest.getEmail());
            coachUpdated.setUsername(userUpdateRequest.getUsername());
            coachUpdated.setRoles(
                    RoleAuthorityUtils.getRoles(coachUpdated, roleRepository));

            return Optional.of(coachRepository.save(coachUpdated));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        coachRepository.deleteById(id);
    }

    @Override
    public boolean updateAffiliationStatus(
            Long id,
            AffiliationStatus affiliationStatus) {
        Optional<Coach> coachOptional = coachRepository.findById(id);

        if (coachOptional.isPresent()) {
            Coach coach = coachOptional.orElseThrow();
            coach.setAffiliationStatus(affiliationStatus);
            coachRepository.save(coach);
            return true;
        }
        return false;
    }
}
