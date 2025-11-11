package dggs.organizing_work_boot.users;

import dggs.organizing_work_boot.users.entity.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    //jpa 기본

    Optional<Users> findByUsersIdAndUsersPw(String usersId, String usersPw);
}
