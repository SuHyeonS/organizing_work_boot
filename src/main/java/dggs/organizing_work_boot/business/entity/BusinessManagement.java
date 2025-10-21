package dggs.organizing_work_boot.business.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "businessManagement")
public class BusinessManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="businessManagement_pk")
    private Long businessManagementPk;

    @Column(name="businessManagement_start_date")
    private LocalDate businessManagementStartDate; //시작년도

    @Column(name="businessManagement_and_date")
    private LocalDate businessManagementAndDate; //종료년도

    @Column(name="businessManagement_contract_status")
    private String businessManagementContractStatus; //계약현황 (계약, 비계약)

    @Column(name="businessManagement_team")
    private String businessManagementTeam; //담당팀

    // ✅ FK 설정 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_fk", nullable = false, foreignKey = @ForeignKey(name = "fk_business_management_to_business"))
    @JsonBackReference //무한루프 방지
    private Business businessFk;
}
