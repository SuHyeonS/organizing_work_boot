package dggs.organizing_work_boot.work;

import dggs.organizing_work_boot.work.entity.Work;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkService {
    //디폴트 public
    Long join(Work sample); //등록 및 수정
    Long update(Work sample); //등록 및 수정
    List<Work> findTableList(); //목록
    Optional<Work> findOne(Long pk); //상세
    void deleteById(Work sampleTable); //삭제


    void updateWorks(List<Work> updatedList); //저장
    void insertWorks(List<Work> newList); //저장

    List<Work> findByParent_WorkPk(Long parentId);
    List<Work> findByParentIsNull();
    List<Map<String, Object>> findTableInfo(String tableName, String schemaName);
}
