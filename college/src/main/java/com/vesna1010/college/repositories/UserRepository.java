package com.vesna1010.college.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.vesna1010.college.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	@Modifying
	@Query("update User u set u.enabled=false where u.id=?1")
	void disableById(@Param("1") Long id);

	boolean existsByEmail(String email);

}
