package com.sportsclubmanager.backend.member.service;

import java.util.List;
import java.util.Optional;

import com.sportsclubmanager.backend.user.model.AffiliationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportsclubmanager.backend.member.model.ClubAdministrator;
import com.sportsclubmanager.backend.member.repository.ClubAdministratorRepository;
import com.sportsclubmanager.backend.shared.util.RoleAuthorityUtils;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.repository.RoleRepository;
import com.sportsclubmanager.backend.user.service.UserService;

@Service
public class ClubAdministratorService implements UserService<ClubAdministrator> {

    private final ClubAdministratorRepository clubAdministratorRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public ClubAdministratorService(ClubAdministratorRepository clubAdministratorRepository,
                                    RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.clubAdministratorRepository = clubAdministratorRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public ClubAdministrator save(ClubAdministrator clubAdministrator) {
        clubAdministrator.setRoles(RoleAuthorityUtils.getRoles(clubAdministrator, roleRepository));
        clubAdministrator.setPassword(passwordEncoder.encode(clubAdministrator.getPassword()));
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
    public Optional<ClubAdministrator> update(Long id, UserUpdateRequest userUpdateRequest) {
        Optional<ClubAdministrator> clubAdminOptional = clubAdministratorRepository.findById(id);

        if (clubAdminOptional.isPresent()) {
            ClubAdministrator clubAdminUpdated = clubAdminOptional.orElseThrow();

            clubAdminUpdated.setName(userUpdateRequest.getName());
            clubAdminUpdated.setLastName(userUpdateRequest.getLastName());
            clubAdminUpdated.setPhoneNumber(userUpdateRequest.getPhoneNumber());
            clubAdminUpdated.setEmail(userUpdateRequest.getEmail());
            clubAdminUpdated.setUsername(userUpdateRequest.getUsername());
            clubAdminUpdated.setRoles(RoleAuthorityUtils.getRolesFromUpdateRequest(userUpdateRequest, roleRepository));

            return Optional.of(clubAdministratorRepository.save(clubAdminUpdated));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        clubAdministratorRepository.deleteById(id);
    }

    @Override
    public boolean updateAffiliationStatus(Long id, AffiliationStatus affiliationStatus) {
        Optional<ClubAdministrator> clubAdminOptional = clubAdministratorRepository.findById(id);

        if (clubAdminOptional.isPresent()) {
            ClubAdministrator clubAdmin = clubAdminOptional.orElseThrow();
            clubAdmin.setAffiliationStatus(affiliationStatus);
            clubAdministratorRepository.save(clubAdmin);
            return true;
        }
        return false;
    }
}
