package dggs.organizing_work_boot.business;

import dggs.organizing_work_boot.business.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    //jpa 기본

    //SELECT * FROM Business a LEFT JOIN business_management b on a.business_pk  = b.business_fk
    // sql 아님
    @Query("SELECT DISTINCT b FROM Business b LEFT JOIN FETCH b.businessManagementList")
    List<Business> findAllWithManagement();

    //단, 이 경우 Business 엔티티만 반환하고,
    //businessManagementList는 자동 매핑되지 않음 → DTO로 처리 필요
    @Query(value = "SELECT * FROM business a LEFT JOIN business_management b ON a.business_pk = b.business_fk", nativeQuery = true)
    List<Business> findAllWithManagementNative();

    // 자식 엔티티의 필드 조건으로 조회
    List<Business> findByBusinessManagementList_BusinessManagementTeam(String team);

    // 이름으로 전체 리스트 가져오기 (조건 없이)
    List<Business> findAllByBusinessManagementListIsNotNull();
}