package dggs.organizing_work_boot.business;

import dggs.organizing_work_boot.business.entity.Business;

import java.util.List;
import java.util.Optional;

public interface BusinessService {
    //디폴트 public
    Long join(Business sample); //등록 및 수정
    List<Business> findTableList(); //목록
    Optional<Business> findOne(Long pk); //상세
    void deleteById(Business sampleTable); //삭제

    List<Business> findJoinTableList();
}
