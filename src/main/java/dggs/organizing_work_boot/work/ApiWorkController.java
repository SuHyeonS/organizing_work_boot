package dggs.organizing_work_boot.work;

import dggs.organizing_work_boot.work.entity.Work;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
* http://localhost:8080/api/business/list
* http://localhost:8080/api/business/one?businessPk=1
*
* http://localhost:8080/api/business/delete?businessPk=1
*
* /api/business/new?businessArea=서울&businessName=도로공사
* */

@RequiredArgsConstructor
@Slf4j
@RestController //@Controller + @ResponseBody
@RequestMapping("/api/work")
public class ApiWorkController {

    //행위	HTTP 메서드	URL 예시	의미
    //조회	GET	/api/work/1	id가 1인 Work 조회
    //등록	POST	/api/work	새로운 Work 생성
    //수정	PUT / PATCH	/api/work/1	id가 1인 Work 수정
    //삭제	DELETE	/api/work/1	id가 1인 Work 삭제

    String className = "work";

    @Autowired
    private final WorkService workService; // Lombok이 생성자 자동 생성

    //데이터 DDL
    @GetMapping("/meta")
    //@GetMapping("/{tableName}")
    public List<Map<String, Object>> getTableInfo (@RequestParam String schemaName, @RequestParam String tableName){
        log.info(className+" getTableInfo..schemaName={}, tableName={}",schemaName,tableName);

        List<Map<String, Object>> findTableInfo;
        findTableInfo = workService.findTableInfo(tableName,schemaName);
        log.info("getTableInfo={}",findTableInfo.toString());
        return findTableInfo;
    }

    //부모목록
    @GetMapping // 호출 axios.get("/api/work");
    //@GetMapping("/List")
    public List<Work> getParentDetail (@ModelAttribute Work work){
        log.info(className+" getParentDetail..");
        //List<Work> list = workService.findTableList(); //전체
        List<Work> list = workService.findByParentIsNull(); //부모만

        log.info("getParentDetail={}",list.toString());

        return list;
    }

    //자식목록
    @GetMapping("/{id}/children")
    //@GetMapping("/subList")
    public List<Work> getSubList (@PathVariable Long id){//(Model model, @ModelAttribute Work key){
        log.info(className+" getSubList..parentId = {}", id);
        List<Work> list = workService.findByParent_WorkPk(id);
        log.info("getSubList={}",list.toString());
        return list;
    }

    //단건 조회 @PathVariable — “URL 경로에서 값을 받는 경우”
    @GetMapping("/{id}")
    //@GetMapping(value="/one")
    public ResponseEntity<Work> getOne (@PathVariable Long id) {
        log.info("{} getOne.. id={}", className, id);

        Work work = workService.findOne(id)
                .orElseThrow(() -> new RuntimeException("해당 Work를 찾을 수 없습니다."));
        log.info("getOne={}",work.toString());
        return ResponseEntity.ok(work);
    }

    //단건 조회 부모키 가져오기 @PathVariable — “URL 경로에서 값을 받는 경우”
    @GetMapping("/{id}/parentId")
    //@GetMapping(value="/one")
    public ResponseEntity<Work> getParentId (@PathVariable Long id) {
        log.info("{} getParentId.. id={}", className, id);

        Optional<Work> workOpt = workService.findByIdWithParent(id);
                //.orElseThrow(() -> new RuntimeException("해당 Work를 찾을 수 없습니다."));

        //key 가 없을경우
        if (workOpt.isEmpty()) {
            log.warn("⚠ 해당 Work({})를 찾을 수 없습니다.", id);
            throw new RuntimeException("해당 Work를 찾을 수 없습니다.");
        }

        Work work = workOpt.get(); // Optional에서 실제 객체 꺼내기
        log.info("{} getParentId.. work={}", className, work.getWorkPk());

        // 부모 Work 가져오기
        Work parent = work.getParent();

        //부모가 없을경우
        // ✅ null 체크를 log 전에 수행해야 함
        if (parent == null) {
            log.info("{} getParentId.. 부모 없음(id={})", className, id);
            // 204 No Content
            return ResponseEntity.noContent().build();
        }

        // Lazy 강제 초기화
        parent.getWorkPk();   // Hibernate 프록시 초기화
        parent.setChildren(null); // 자식 정보 제외 (순환참조 방지)

        log.info("{} getParentId.. parent={}", className, parent.getWorkPk());
        if (parent == null) {
            // 부모가 없는 경우 204 No Content 또는 null 반환
            return ResponseEntity.noContent().build();
        }

        // Lazy 강제 초기화
        parent.getWorkPk();  // 부모 PK 접근 → Hibernate 초기화
        parent.setChildren(null); // 자식은 제외하고 보내기
        log.info("getParentId={}",parent.toString());
        return ResponseEntity.ok(parent);
    }

    //단건 저장
    @PostMapping
    public ResponseEntity<String> create(@RequestBody Work work) {
        log.info("insert.. {}", work);
        Long id = workService.join(work);
        return ResponseEntity.ok("등록 완료: " + id);
    }

    //수정
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Work work) {
        log.info("update.. {}", id);
        work.setWorkPk(id);
        workService.update(work);
        return ResponseEntity.ok("수정 완료");
    }

    //하위목록으로 이관
    @PutMapping("/{id}/moveChildren")
    public ResponseEntity<String> moveChildren(@PathVariable Long id, @RequestBody Work work) {
        log.info("moveSublist.. {} and subList : {}", id, work);
        work.setWorkPk(id);
        workService.moveChildren(id, work.getSubList());
        return ResponseEntity.ok("하위목록으로 이관 되었습니다");
    }

    //상위목록으로 이관
    @PutMapping("/{id}/moveParent")
    public ResponseEntity<String> moveParent(@PathVariable Long id, @RequestBody Work work) {
        log.info("moveParent.. {} and moveParent : {}", id, work);
        work.setWorkPk(id);

        String mge="";
        try {
            mge = workService.moveParent(id, work.getSubList());
            return ResponseEntity.ok(mge);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mge);
        }


    }


    //전체 저장
    @PostMapping("/saveAll")
    public ResponseEntity<?> saveAll(@RequestBody Work Work) {
        log.info("{} saveAll.. getWorkPk = {}", className, Work.getWorkPk());

        List<Work> updatedList = Work.getUpdatedList();
        List<Work> newList = Work.getNewList();

        if (Work.getWorkPk() != null) {

            Work request = workService.findOne(Work.getWorkPk())
                    .orElseThrow(() -> new RuntimeException("부모 없음"));

            // ✅ updatedList 처리
            if (updatedList == null || updatedList.isEmpty()) {
                log.info("업데이트할 항목이 없습니다.");
            } else {
                for (Work w : updatedList) {
                    if (w.getParent() == null) {
                        w.setParent(new Work());
                    }
                    w.setParent(request);
                }
            }

            // ✅ newList 처리
            if (newList == null || newList.isEmpty()) {
                log.info("신규 업데이트할 항목이 없습니다.");
            } else {
                for (Work w : newList) {
                    if (w.getParent() == null) {
                        w.setParent(new Work());
                    }
                    w.setParent(request);
                }
            }
        }


        log.info("updatedList >>>"+updatedList);
        log.info("newList >>>"+newList);

        if (updatedList != null && !updatedList.isEmpty()) {
            log.info("updatedList if >>>"+updatedList);
            workService.updateWorks(updatedList);
        }

        if (newList != null && !newList.isEmpty()) {
            log.info("newList if >>>"+newList);
            workService.insertWorks(newList);
        }

        return ResponseEntity.ok("저장 성공");
    }


    //delete
    //ResponseEntity<T>	응답 본문 + HTTP 상태코드 제어	ResponseEntity.ok("삭제되었습니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        log.info(className + " delete.. id = {}", id);

        Work data = new Work();
        data.setWorkPk(id);
        workService.deleteById(data);
        return ResponseEntity.ok("삭제되었습니다.");
    }

}
