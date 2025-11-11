package dggs.organizing_work_boot.users.entity;

import dggs.organizing_work_boot.work.entity.Work;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    //네이밍 클래스_이름_타입(화면 표현용)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_pk")
    @Comment("_유저일련번호")
    private Long usersPk;

    @Column(name = "users_department")
    @Comment("1_부서")  // ✅ DB 컬럼 주석으로 들어감
    private String usersDepartment;

    @Column(name = "users_team")
    @Comment("2_팀")  // ✅ DB 컬럼 주석으로 들어감
    private String usersTeam;

    @Column(name = "users_name")
    @Comment("3_유저명")  // ✅ DB 컬럼 주석으로 들어감
    private String usersName;

    @Column(name = "users_id")
    @Comment("4_유저아이디")  // ✅ DB 컬럼 주석으로 들어감
    private String usersId;

    @Column(name = "users_pw")
    @Comment("5_유저비밀번호")  // ✅ DB 컬럼 주석으로 들어감
    private String usersPw;

    //work
    @OneToMany(mappedBy = "usersFk") //맵핑할 필드값
    @Comment("_업무키")  // ✅ DB 컬럼 주석으로 들어감
    private List<Work> workFk = new ArrayList<>();

}
