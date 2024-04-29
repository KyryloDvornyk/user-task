package com.user.task.repository;

import com.user.task.model.LocalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalAddressRepository extends JpaRepository<LocalAddress, Long> {
}
