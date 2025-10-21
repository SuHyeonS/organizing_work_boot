package dggs.organizing_work_boot.work.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private Long workPk;

    @Column(name = "work_title")
    private String workTitle; // 업무 제목

    @Column(name = "work_requester")
    private String workRequester; // 요청자

    @Column(name = "work_request_date")
    private LocalDate workRequestDate; // 요청일

    @Column(name = "work_completion_date")
    private LocalDate workCompletionDate; // 완료일

    @Column(name = "work_contents")
    private String workContents; // 내용

    @Column(name = "work_etc")
    private String workEtc; // 비고


    //하위 목록시 작성필요
    @Column(name = "work_expected_start_date")
    private LocalDate workExpectedStartDate; //예정일 시작

    @Column(name = "work_expected_end_date")
    private LocalDate workExpectedEndDate; //예정일 종료

    @Column(name = "work_progress_start_date")
    private LocalDate workProgressStartDate; //진행일 시작

    @Column(name = "work_progress_end_date")
    private LocalDate workProgressEndDate; //진행일 종료

    @Column(name = "work_situation")
    private String workSituation; // 진행 상태

    /** 자기참조 관계 설정 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_parent_id")
    @JsonBackReference
    private Work parent;

    /** 하위 업무 리스트 */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Work> children = new ArrayList<>();

    public void addChild(Work child) {
        children.add(child);
        child.setParent(this);
    }
}
