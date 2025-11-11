package dggs.organizing_work_boot.users;

import dggs.organizing_work_boot.users.entity.Users;
import dggs.organizing_work_boot.work.entity.Work;

import java.util.Optional;

public interface UsersService {
    Optional<Users> findOne(Long pk);

    Optional<Users> login(Users login);
}
