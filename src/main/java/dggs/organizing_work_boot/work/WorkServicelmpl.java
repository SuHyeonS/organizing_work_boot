package dggs.organizing_work_boot.work;

import dggs.organizing_work_boot.util.PropertyUtil;
import dggs.organizing_work_boot.work.entity.Work;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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

    //부모객체
    @Override
    @Transactional(readOnly = true) //읽기용
    public Optional<Work> findByIdWithParent(Long pk) {
        log.info(className+"findByIdWithParent.. ");
        return workRepository.findByIdWithParent(pk);
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
        log.info("{} deleteById.. {}", className, sampleTable.toString() );
        workRepository.deleteById(sampleTable.getWorkPk());
    }


    // util 함수
    private String toCamelCase(String s) {
        String[] parts = s.split("_");
        StringBuilder camelCaseString = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(parts[i].substring(0, 1).toUpperCase());
            camelCaseString.append(parts[i].substring(1));
        }
        return camelCaseString.toString();
    }

    //DB DDL 정보
    @Override
    public List<Map<String, Object>> findTableInfo(String tableName, String schemaName) {
        log.info("BusinessServicelmpl findTableInfo..tableName : "+tableName+", schemaName : "+schemaName);
        log.info("{} findTableInfo... tableName : {}, schemaName : {}", className, tableName, schemaName);

        // VO 값 / 기본값 처리
        Work work = new Work();
        if(tableName == null || tableName.isEmpty()) {
            tableName = work.getTableName();
        }
        if(schemaName == null || schemaName.isEmpty()) {
            schemaName = work.getSchemaName();
        }

        List<Object[]> rows = workRepository.findTableInfo(tableName, schemaName);

        List<Map<String, Object>> fields = new ArrayList<>();

        for (Object row[] : rows) {
            String columnName = (String) row[2];   // column_name
            String dataType   = (String) row[3];   // data_type
            String comment    = (String) row[4];   // column_comment

            String type = "text";
            if (dataType.contains("date") || columnName.toLowerCase().contains("date")) {
                type = "date";
            } else if (columnName.toLowerCase().contains("status")) {
                type = "select";
            } else if (columnName.toLowerCase().contains("situation")) {
                type = "selInput";
            }

            Map<String, Object> field = new HashMap<>();
            field.put("key", toCamelCase(columnName)); // ← 여기서 DB 컬럼명을 camelCase로 변환
            field.put("label", comment != null ? comment : columnName);
            field.put("type", type);

            //옵션 넣기 > 프로퍼티에서 목록가져옴
            String optionValues = PropertyUtil.getProperty("work.options."+toCamelCase(columnName));
            //log.info("work.options."+toCamelCase(columnName)+"={}",optionValues);
            if(optionValues != null && !optionValues.isEmpty()){
                field.put("options", Arrays.asList(optionValues.split(",")));
            }

            //옵션2 데이터 리스트 DB에서 group by 함
            //요청자
            if(toCamelCase(columnName).equals("workRequesterSituation")){
                List<String> data = workRepository.findDistinctRequesterSituations();
                field.put("options", data);
            }
            //수행자
            if(toCamelCase(columnName).equals("workPerformerSituation")){
                List<String> data = workRepository.findDistinctPerformerSituations();
                field.put("options", data);
            }

            fields.add(field);
        }

        return fields;
    }

    //하위목록으로 이관
    public void moveChildren(Long parentId, List<Work> subList) {
        // 부모 Work 조회
        Work parentWork = workRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("부모 work를 찾을 수 없습니다: " + parentId));

        // subList 각각 업데이트
        for (Work sub : subList) {
            Work child = workRepository.findById(sub.getWorkPk())
                    .orElseThrow(() -> new RuntimeException("하위 work를 찾을 수 없습니다: " + sub.getWorkPk()));
            child.setParent(parentWork); // ✅ 부모 설정
            workRepository.save(child);
        }
    }

    //상위목록으로 이관
    public String moveParent(Long parentId, List<Work> subList) {
        // 현재 부모
        Work parentWork = workRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("부모 work를 찾을 수 없습니다: " + parentId));

        //현재부모의 부모
        Work overParent = new Work();
        if (parentWork.getParent() != null) {
            overParent = workRepository.findById(parentWork.getParent().getWorkPk())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            //work.setParent(parent); // parent는 findById로 불러온 영속 상태의 엔티티여야 함 (jpa 설정때문에 필요함)
        }else{
            //최상위 이동
            log.info("{} moveParent.. not parent key ",className);
            overParent = null;
            //return "상위 목록이 없습니다";
        }

        // subList 각각 업데이트
        for (Work sub : subList) {
            Work child = workRepository.findById(sub.getWorkPk())
                    .orElseThrow(() -> new RuntimeException("하위 work를 찾을 수 없습니다: " + sub.getWorkPk()));
            child.setParent(overParent); // ✅ 부모 설정
            workRepository.save(child);
        }
        return "이관되었습니다";
    }

}
