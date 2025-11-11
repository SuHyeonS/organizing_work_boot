package dggs.organizing_work_boot.users;

import dggs.organizing_work_boot.users.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController //@Controller + @ResponseBody
@RequestMapping("/api/users")
public class ApiUsersController {
    //RESTful 표준
    //행위	HTTP 메서드	URL 예시	의미
    //조회	GET	/api/work/1	id가 1인 Work 조회
    //등록	POST	/api/work	새로운 Work 생성
    //수정	PUT / PATCH	/api/work/1	id가 1인 Work 수정
    //삭제	DELETE	/api/work/1	id가 1인 Work 삭제

    //RequestParam 단일 파라미터
    //RequestBody vo 객체

    @Autowired
    private final UsersService usersService; // Lombok이 생성자 자동 생성
    String className = "users";

    @PostMapping("/login")
    public ResponseEntity<Users> login (@RequestBody Users login){
        log.info(className+" login..getUsersId={}, getUsersPw={}",login.getUsersId(),login.getUsersPw());

        Optional<Users> data = usersService.login(login);
        //Users user = data.get(); // Optional에서 실제 객체 꺼내기

        if (data.isPresent()) { //isPresent 값이 존재하는지 여부를 boolean으로 확인하는 메서드
            return ResponseEntity.ok(data.get());
        } else {
            log.error("로그인 실패: 아이디 또는 비밀번호 불일치");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //HTTP 상태 코드 401 (Unauthorized) & 본문(body) 없음
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 아이디 또는 비밀번호 불일치"); //return type 안맞음
        }


    }

}
