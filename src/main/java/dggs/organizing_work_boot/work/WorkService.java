package dggs.organizing_work_boot.work;

import dggs.organizing_work_boot.work.entity.Work;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface WorkService {
    //디폴트 public
    Long join(Work sample); //등록 및 수정
    List<Work> findTableList(); //목록
    Optional<Work> findOne(Long pk); //상세
    void deleteById(Work sampleTable); //삭제

}
