package dggs.organizing_work_boot.work.entity;

import com.fasterxml.jackson.annotation.*;
import dggs.organizing_work_boot.business.entity.Business;
import dggs.organizing_work_boot.users.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"parent", "children"}) // 🔹 순환참조 방지
@Entity
@Getter
@Setter
@Table(name = "work")
/*
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "workPk"
)
*/
public class Work {

    //네이밍 클래스_이름_타입(화면 표현용)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_pk")
    @Comment("_업무일련번호")
    private Long workPk;

    //메인
    @Column(name = "work_title")
    @Comment("1_업무 제목")  // ✅ DB 컬럼 주석으로 들어감
    private String workTitle;

    @Column(name = "work_request_date")
    @Comment("2_요청일")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workRequestDate;

    @Column(name = "work_requester_situation")
    @Comment("3_요청자")  // ✅ DB 컬럼 주석으로 들어감
    private String workRequesterSituation; // 요청자

    @Column(name = "work_performer_situation")
    @Comment("4_수행자")  // ✅ DB 컬럼 주석으로 들어감
    private String workPerformerSituation; // 수행자

    @Column(name = "work_completion_date")
    @Comment("5_완료일")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workCompletionDate; // 완료일

    @Column(name = "work_contents")
    @Comment("6_내용")  // ✅ DB 컬럼 주석으로 들어감
    private String workContents; // 내용

    @Column(name = "work_situation_status")
    @Comment("7_진행 상태")  // ✅ DB 컬럼 주석으로 들어감
    private String workSituationStatus; // 진행 상태[예정, 진행중, 완료, 보류]

    @Column(name = "work_type_status")
    @Comment("8_업무구분")  // ✅ DB 컬럼 주석으로 들어감
    private String workTypeStatus; //업무구분[내업, (기본)외업]

    @Column(name = "work_assortment_status")
    @Comment("9_업무종류")  // ✅ DB 컬럼 주석으로 들어감
    private String workAssortmentStatus; //업무종류[개발, 문서, 데이터, 업데이트]

    @Column(name = "work_etc")
    @Comment("10_비고")  // ✅ DB 컬럼 주석으로 들어감
    private String workEtc; // 비고



    //하위 목록시 작성필요
    @Column(name = "work_expected_start_date")
    @Comment("11_예정일 시작")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workExpectedStartDate; //예정일 시작

    @Column(name = "work_expected_end_date")
    @Comment("12_예정일 종료")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workExpectedEndDate; //예정일 종료

    @Column(name = "work_progress_start_date")
    @Comment("13_진행일 시작")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workProgressStartDate; //진행일 시작

    @Column(name = "work_progress_end_date")
    @Comment("14_진행일 종료")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workProgressEndDate; //진행일 종료


    //양방향 참조만 필드명
    //business
    @ManyToOne @JoinColumn(name = "business_fk", foreignKey = @ForeignKey(name = "work_business_fk"))
    @Comment("_사업키")
    private Business businessFk;

    //user
    @ManyToOne @JoinColumn(name = "users_fk", foreignKey = @ForeignKey(name = "work_users_fk"))
    @Comment("_유저키")
    private Users usersFk;



    /** 자기참조 관계 설정 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_parent_id")
    @JsonBackReference //순환참조 직렬화 제외
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "children"})
    @Comment("_부모일련번호")
    private Work parent;

    /** 하위 업무 리스트 */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference //순환참조 직렬화 포함
    private List<Work> children = new ArrayList<>();


    public void addChild(Work child) {
        children.add(child);
        child.setParent(this);
    }

    //데이터 받기용 (비 컬럼)
    @Transient
    private List<Work> updatedList;
    @Transient
    private List<Work> newList;

    //하위목록으로 이관
    @Transient
    private List<Work> subList;

    @Transient
    private String tableName = "work";   //기본 테이블명
    @Transient
    private String schemaName = "public"; //기본스키마

    @Transient
    private String check; //상세페이지 전후 찾기


}
