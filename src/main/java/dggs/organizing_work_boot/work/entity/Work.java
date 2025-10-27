package dggs.organizing_work_boot.work.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "work")
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_pk")
    @Comment("업무일련번호")
    private Long workPk;

    //메인
    @Column(name = "work_title")
    @Comment("업무 제목")  // ✅ DB 컬럼 주석으로 들어감
    private String workTitle;

    @Column(name = "work_request_date")
    @Comment("요청일")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workRequestDate;

    @Column(name = "work_requester")
    @Comment("요청자")  // ✅ DB 컬럼 주석으로 들어감
    private String workRequester; // 요청자

    @Column(name = "work_performer")
    @Comment("수행자")  // ✅ DB 컬럼 주석으로 들어감
    private String workPerformer; // 수행자

    @Column(name = "work_completion_date")
    @Comment("완료일")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workCompletionDate; // 완료일

    @Column(name = "work_contents")
    @Comment("내용")  // ✅ DB 컬럼 주석으로 들어감
    private String workContents; // 내용

    @Column(name = "work_situation")
    @Comment("진행 상태")  // ✅ DB 컬럼 주석으로 들어감
    private String workSituation; // 진행 상태[예정, 진행중, 완료]

    @Column(name = "work_type")
    @Comment("업무구분")  // ✅ DB 컬럼 주석으로 들어감
    private String workType; //업무구분[내업, (기본)외업]

    @Column(name = "work_assortment")
    @Comment("업무종류")  // ✅ DB 컬럼 주석으로 들어감
    private String workAssortment; //업무종류[개발, 문서, 데이터, 업데이트]

    @Column(name = "work_etc")
    @Comment("비고")  // ✅ DB 컬럼 주석으로 들어감
    private String workEtc; // 비고



    //하위 목록시 작성필요
    @Column(name = "work_expected_start_date")
    @Comment("예정일 시작")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workExpectedStartDate; //예정일 시작

    @Column(name = "work_expected_end_date")
    @Comment("예정일 종료")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workExpectedEndDate; //예정일 종료

    @Column(name = "work_progress_start_date")
    @Comment("진행일 시작")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workProgressStartDate; //진행일 시작

    @Column(name = "work_progress_end_date")
    @Comment("진행일 종료")  // ✅ DB 컬럼 주석으로 들어감
    private LocalDate workProgressEndDate; //진행일 종료



    /** 자기참조 관계 설정 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_parent_id")
    @JsonBackReference
    @Comment("_부모일련번호")
    private Work parent;

    /** 하위 업무 리스트 */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
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

    @Transient
    private String tableName = "work";   //기본 테이블명
    @Transient
    private String schemaName = "public"; //기본스키마



}
