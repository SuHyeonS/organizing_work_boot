package dggs.organizing_work_boot.work;

import dggs.organizing_work_boot.work.entity.Work;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
public class AipWorkController {

    //행위	HTTP 메서드	URL 예시	의미
    //조회	GET	/api/work/1	id가 1인 Work 조회
    //등록	POST	/api/work	새로운 Work 생성
    //수정	PUT / PATCH	/api/work/1	id가 1인 Work 수정
    //삭제	DELETE	/api/work/1	id가 1인 Work 삭제

    String className = "work";

    @Autowired
    private final WorkService workService; // Lombok이 생성자 자동 생성

    //select
    @GetMapping("/list")
    public List<Work> list (){
        log.info(className+" list..");
        //List<Work> list = workService.findTableList(); //전체
        List<Work> list = workService.findByParentIsNull(); //부모만
        
        return list;
    }

    //select
    @GetMapping("/subList")
    public List<Work> subList (Model model, @ModelAttribute Work key){
        log.info(className+" subList..key : "+key.getWorkPk());
        List<Work> list = workService.findByParent_WorkPk(key.getWorkPk());
        return list;
    }

    @GetMapping(value="/one")
    public Optional<Work> one (Model model, @ModelAttribute Work business){
        log.info(className+" one.. getBusinessPk : "+business.getWorkPk());

        Optional<Work> resultValue = Optional.empty();

        if(business.getWorkPk() != null && !"".equals(business.getWorkPk())) {
            log.info(className+" insert or update {}", business.getWorkPk());
            resultValue = workService.findOne(business.getWorkPk());
        }else{
            log.info(className+" not value");
        }
        //log.info("test : "+business.getBusinessManagementList().get(0).getBusinessManagementPk());

        return resultValue;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Work work) {
        log.info("insert.. {}", work);
        Long id = workService.join(work);
        return ResponseEntity.ok("등록 완료: " + id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Work work) {
        log.info("update.. {}", id);
        work.setWorkPk(id);
        workService.update(work);
        return ResponseEntity.ok("수정 완료");
    }


    //전체 저장
    @PostMapping("/saveAll")
    public ResponseEntity<?> saveAll(@RequestBody Work request) {
        Long parentPk = request.getParent() != null ? request.getParent().getWorkPk() : null;
        if(null != parentPk){
            log.info(className+" saveAll.. 부모키 : "+parentPk);
        }else{
            log.info(className+" saveAll.. 부모키 NULL ");
        }

        List<Work> updatedList = request.getUpdatedList();
        List<Work> newList = request.getNewList();
        /*
        // ✅ updatedList 처리
        if (updatedList == null || updatedList.isEmpty()) {
            log.info("업데이트할 항목이 없습니다.");
        } else {
            for (Work w : updatedList) {
                if (w.getParent() == null) {
                    w.setParent(new Work());
                }
                w.getParent().setWorkPk(key);
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
                w.getParent().setWorkPk(key);
            }
        }
        */
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
        log.info(className + " delete.. id=" + id);

        Work data = new Work();
        data.setWorkPk(id);
        workService.deleteById(data);
        return ResponseEntity.ok("삭제되었습니다.");
    }

}
