package dggs.organizing_work_boot.work.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

//@Entity
//@Getter
//@Setter
//@Table(name = "work_unit")
public class workUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="work_unit_pk")
    private Long workUnitPk;

    @Column(name="work_unit_title")
    private String workUnitTitle; //업무 제목

    @Column(name="work_unit_request_date")
    private LocalDate workUnitRequestDate; //요청일

    @Column(name="work_unit_expected_date")
    private LocalDate workUnitExpectedDate; //예정일

    @Column(name="work_unit_progress_date")
    private LocalDate workUnitProgressDate; //진행일

    @Column(name="work_unit_situation")
    private String workUnitSituation; // 진행여부

    @Column(name="work_unit_etc")
    private String workUnitEtc; //비고


    // ✅ FK 설정 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_unit_fk", nullable = false, foreignKey = @ForeignKey(name = "fk_work_unit_to_work"))
    @JsonBackReference //무한루프 방지
    private Work workUnitFk;
}
