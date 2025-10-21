package dggs.organizing_work_boot.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


// /main/... 으로 주소 정해주세요
@RequiredArgsConstructor
@Slf4j

@RestController //@Controller + @ResponseBody
@RequestMapping("/api/sample")
public class ApiSampleController {

    @Autowired
    private final SampleService sampleService; // Lombok이 생성자 자동 생성

    //메인 form
    @RequestMapping(value="", method= RequestMethod.GET)
    public Optional<Sample> SampleMain(Model model, @ModelAttribute Sample sampleVo){
        log.info("SampleMain : "+(sampleVo == null)+" : "+(sampleVo.getSamplePk() != null && !"".equals(sampleVo.getSamplePk())));
        //model.addAttribute("adminVo", new Admin());

        Optional<Sample> resultValue = Optional.empty();

        if(sampleVo.getSamplePk() == null || "".equals(sampleVo.getSamplePk())) {
            log.info("home controller insert form");
            //resultValue = "저장된 값이 없습니다";

        }else { //수정
            //log.info("home controller update form " + +adminVo.getSamplePk());
            log.info("update form {}", sampleVo.getSamplePk());
            resultValue = sampleService.findOne(sampleVo.getSamplePk());
        }
        return resultValue;
    }

    //select
    @GetMapping("/list")
    public List<Sample> SampleList (Model model ){
        log.info("SampleList..");
        List<Sample> sampleTable = sampleService.findSampleTable();
        return sampleTable;
    }

    //insert, update
    @RequestMapping(value="/new")
    public String SampleNew (Model model , @ModelAttribute Sample sampleTable){
        log.info("SampleNew.." + sampleTable.toString());
        if(sampleTable != null ){
            log.info("SampleNew insert");
        }else{
            log.info("SampleNew update" + String.valueOf(sampleTable.getSamplePk()));
        }
        Long resultVlaue = sampleService.join(sampleTable);
        return "등록 및 수정 되었습니다 key : "+resultVlaue;
    }

    //delete
    @GetMapping("/delete")
    public String SampleDelete (Model model, @ModelAttribute Sample sampleTable){
        log.info("SampleDelete.." + sampleTable.getSamplePk());
        sampleService.deleteById(sampleTable);
        return "삭제되었습니다.";
    }

}
