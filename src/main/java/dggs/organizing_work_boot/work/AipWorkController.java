package dggs.organizing_work_boot.work;

import dggs.organizing_work_boot.work.entity.Work;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequiredArgsConstructor
@Slf4j
@RestController //@Controller + @ResponseBody
@RequestMapping("/api/work")
public class AipWorkController {

    String className = "work";
    @Autowired
    private final WorkService workService; // Lombok이 생성자 자동 생성

    //select
    @GetMapping("/list")
    public List<Work> list (Model model){
        log.info(className+" list..");
        List<Work> list = workService.findTableList();
        return list;
    }

    @RequestMapping(value="/one", method= RequestMethod.GET)
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

    //insert, update
    @RequestMapping(value="/new")
    public String new_ (Model model , @ModelAttribute Work business){
        log.info(className+" new..");
        if(business != null ){
            log.info("insert");
        }else{
            log.info("update" + String.valueOf(business.getWorkPk()));
        }
        Long resultVlaue = workService.join(business);
        return "등록 및 수정 되었습니다 key : "+resultVlaue;
    }

    //delete
    @GetMapping("/delete")
    public String delete (Model model, @ModelAttribute Work business){
        log.info(className+" delete.."+ business.getWorkPk());
        workService.deleteById(business);
        return "삭제되었습니다.";
    }

}
