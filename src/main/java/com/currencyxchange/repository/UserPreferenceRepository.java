package com.currencyxchange.repository;

import com.currencyxchange.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    Optional<UserPreference> findByIpAddress(String ipAddress);
}
