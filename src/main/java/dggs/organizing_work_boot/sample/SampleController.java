package dggs.organizing_work_boot.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;



//@RestController //@Controller + @ResponseBody
//@RequestMapping("/api/admin")

// /main/... 으로 주소 정해주세요
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sample")
public class SampleController {

    /*
    @GetMapping("/")
    public String hello() {
        return "Hello, Spring Boot!";
    }
    */

    @Autowired
    private final SampleService sampleService; // Lombok이 생성자 자동 생성

    //메인 form
    @RequestMapping(value="", method= RequestMethod.GET)
    public String SampleMain(Model model, @ModelAttribute Sample sampleVo){
        log.info("SampleMain : "+(sampleVo == null)+" : "+(sampleVo.getSamplePk() != null && !"".equals(sampleVo.getSamplePk())));
        //model.addAttribute("adminVo", new Admin());

        if(sampleVo.getSamplePk() == null || "".equals(sampleVo.getSamplePk())) {
            log.info("home controller insert form");
            model.addAttribute("sampleVo", new Sample());

        }else { //수정
            //log.info("home controller update form " + +adminVo.getSamplePk());
            log.info("update form {}", sampleVo.getSamplePk());
            model.addAttribute("sampleVo", sampleService.findOne(sampleVo.getSamplePk()));
        }

        return "sample/home";
    }

    //select
    @GetMapping("/list")
    public String SampleList (Model model ){
        log.info("SampleList..");
        List<Sample> sampleTable = sampleService.findSampleTable();
        model.addAttribute("sampleTable",sampleTable);
        return "sample/list";
    }

    //insert, update
    @RequestMapping(value="/new")
    public String SampleNew (Model model , @ModelAttribute Sample sampleTable){
        log.info("SampleNew.." + sampleTable.toString());
        if(sampleTable != null ){
            log.info("SampleNew insert");
            //log.info(sampleTable.getSampleName());
            //log.info(sampleTable.getSampleId());

        }else{
            log.info("SampleNew update" + String.valueOf(sampleTable.getSamplePk()));
        }
        sampleService.join(sampleTable);
        return "redirect:/sample";
    }

    //delete
    @GetMapping("/delete")
    public String SampleDelete (Model model, @ModelAttribute Sample sampleTable){
        log.info("SampleDelete.." + sampleTable.getSamplePk());
        sampleService.deleteById(sampleTable);
        return "redirect:/sample";
    }

    /*
    //api page controller와 같이 사용시
    //@GetMapping("/{pk}")
    @GetMapping("/{pk:\\d+}")
    @ResponseBody
    public Sample getAdmin(@PathVariable Long pk) {
        // 예시: DB 조회 대신 샘플 데이터 반환
        Sample admin = new Sample();
        admin.setSamplePk(pk);
        admin.setSampleName("홍길동");
        return admin;
    }

    // 관리자 리스트 조회
    @GetMapping("/api/admin/list")
    @ResponseBody
    public List<Sample> getAdminList() {
        log.info("Admin 리스트 API 호출");

        // 실제 DB에서 가져오려면 adminService.findSampleTable() 사용
        List<Sample> sampleTable = sampleService.findSampleTable();
        return sampleTable;
    }
    */

}
