package entelect.training.incubator.spring.security.repository;

import entelect.training.incubator.spring.security.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
