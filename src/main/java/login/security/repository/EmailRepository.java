package login.security.repository;

import login.security.Entity.EmailVerified;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailVerified,String> {

    EmailVerified findByUsername(String username);

    boolean existsByUsername(String username);
}
