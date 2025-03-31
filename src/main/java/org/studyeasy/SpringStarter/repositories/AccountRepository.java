package org.studyeasy.SpringStarter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studyeasy.SpringStarter.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findOneByEmailIgnoreCase(String email);



}
