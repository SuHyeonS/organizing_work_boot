package dggs.organizing_work_boot.work;

import dggs.organizing_work_boot.work.entity.Work;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
//@Transactional(readOnly = true) //읽기용 (죄회용으로 사용하면 성능 좋아짐)
@RequiredArgsConstructor
public class WorkServicelmpl implements WorkService {


    String className = "WorkServicelmpl ";

    //@Autowired
    private WorkRepository workRepository;

    @Autowired
    public WorkServicelmpl(WorkRepository repository) {
        log.info(className+"생성자.. "+repository);
        assert repository != null;
        this.workRepository = repository;
    }

    //전체목록
    @Override
    @Transactional(readOnly = true) //읽기용
    public List<Work> findTableList() {
        log.info(className+"findTableList.. ");
        return workRepository.findAll();
    }

    //부모목록
    @Override
    @Transactional(readOnly = true) //읽기용
    public List<Work> findByParentIsNull() {
        log.info(className+"findByParentIsNull.. ");
        return workRepository.findByParentIsNull();
    }


    //자식목록
    @Override
    @Transactional(readOnly = true) //읽기용
    public List<Work> findByParent_WorkPk(Long parentId) {
        log.info(className+"findByParent_WorkPk.. parentId : "+parentId);
        return workRepository.findByParent_WorkPk(parentId);
    }
    
    //전체 업데이트
    @Override
    public void updateWorks(List<Work> updatedList) {
        if (updatedList == null || updatedList.isEmpty()) {
            log.info(className+"updateWorks.. not  updatedList");
            return; // 아무것도 없으면 종료
        }

        for (Work work : updatedList) {
            if (work.getWorkPk() != null && workRepository.existsById(work.getWorkPk())) {
                workRepository.save(work); // JPA save는 PK 있으면 update
            }
        }
    }
    
    //전체 저장
    @Override
    public void insertWorks(List<Work> newList) {
        if (newList == null || newList.isEmpty()) {
            log.info(className+"insertWorks.. not  updatedList");
            return; // 아무것도 없으면 종료
        }

        for (Work work : newList) {
            work.setWorkPk(null); // PK 제거 (신규)
            workRepository.save(work);
        }
    }


    //하나만
    @Override
    @Transactional(readOnly = true) //읽기용
    public Optional<Work> findOne(Long pk) {
        log.info(className+"findOne.. "+pk);
        return workRepository.findById(pk);
    }

    //등록 및 수정
    @Override
    public Long join(Work work) {
        log.info(className+"join.. "+work);

        //부모키가 있다면 가져와서 담음.
        if (work.getParent() != null) {
            Work parent = workRepository.findById(work.getParent().getWorkPk())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            work.setParent(parent); // parent는 findById로 불러온 영속 상태의 엔티티여야 함 (jpa 설정때문에 필요함)
        }

        Work result = workRepository.save(work);
        return result.getWorkPk();
    }

    @Override
    public Long update(Work work) {
        log.info(className+"update.. "+work);

        //부모키가 있다면 가져와서 담음.
        if (work.getParent() != null) {
            Work parent = workRepository.findById(work.getParent().getWorkPk())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            work.setParent(parent); // parent는 findById로 불러온 영속 상태의 엔티티여야 함 (jpa 설정때문에 필요함)
        }

        Work result = workRepository.save(work);
        return result.getWorkPk();
    }


    //삭제
    @Override
    public void deleteById(Work sampleTable) {
        log.info("BusinessServicelmpl deleteById.."+ sampleTable.toString() );
        workRepository.deleteById(sampleTable.getWorkPk());
    }



}
