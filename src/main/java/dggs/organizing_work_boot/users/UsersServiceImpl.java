package dggs.organizing_work_boot.users;

import dggs.organizing_work_boot.users.entity.Users;
import dggs.organizing_work_boot.work.entity.Work;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
//@Transactional(readOnly = true) //읽기용 (죄회용으로 사용하면 성능 좋아짐)
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;
    String className = "UsersServiceImpl ";

    @Autowired
    public UsersServiceImpl(UsersRepository repository) {
        log.info(className+"생성자.. "+repository);
        assert repository != null;
        this.usersRepository = repository;
    }

    //하나만
    @Override
    @Transactional(readOnly = true) //읽기용
    public Optional<Users> findOne(Long pk) {
        log.info(className+"findOne.. "+pk);
        return usersRepository.findById(pk);
    }

    @Override
    @Transactional(readOnly = true) //읽기용
    public Optional<Users> login(Users login) {
        log.info(className+"login.. usersId={}, usersPw={}",login.getUsersId(),login.getUsersPw());
        return usersRepository.findByUsersIdAndUsersPw(login.getUsersId(),login.getUsersPw());
    }

}
