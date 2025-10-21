package dggs.organizing_work_boot.business;

import dggs.organizing_work_boot.business.entity.Business;
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
public class BusinessServicelmpl implements BusinessService {
    //@Autowired
    private BusinessRepository businessRepository;

    @Autowired
    public BusinessServicelmpl(BusinessRepository repository) {
        log.info("BusinessServicelmpl.."+ repository );
        assert repository != null;
        this.businessRepository = repository;
    }

    //조인 목록
    @Transactional(readOnly = true)
    public List<Business> findJoinTableList(){
        // fetch join으로 businessManagementList 같이 가져오기
        return businessRepository.findAllWithManagement();
    }

    //목록
    @Override
    @Transactional(readOnly = true) //읽기용
    public List<Business> findTableList() {
        log.info("BusinessServicelmpl findTableList..");
        return businessRepository.findAll();
    }

    //하나만
    @Override
    @Transactional(readOnly = true) //읽기용
    public Optional<Business> findOne(Long pk) {
        log.info("BusinessServicelmpl findOne.."+ pk );
        return businessRepository.findById(pk);
    }

    //등록 및 수정
    @Override
    public Long join(Business sample) {
        log.info("BusinessServicelmpl join.."+ sample);
        businessRepository.save(sample);
        return sample.getBusinessPk();
    }

    //삭제
    @Override
    public void deleteById(Business sampleTable) {
        log.info("BusinessServicelmpl deleteById.."+ sampleTable.toString() );
        businessRepository.deleteById(sampleTable.getBusinessPk());
    }
}
