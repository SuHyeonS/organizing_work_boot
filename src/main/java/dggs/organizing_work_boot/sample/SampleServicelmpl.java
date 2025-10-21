package dggs.organizing_work_boot.sample;

import dggs.organizing_work_boot.secretData.AES128;
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
public class SampleServicelmpl implements SampleService {
    //@Autowired
    private SampleRepository STRepository;

    @Autowired
    public SampleServicelmpl(SampleRepository STRepository) {
        log.info("SampleServicelmpl.."+ STRepository );
        assert STRepository != null;
        this.STRepository = STRepository;
    }

    /* 회원가입 */
    /*
    //기본형
    @Override
    public Long join(Sample sample) {
        log.info("SampleServicelmpl join.."+ sample);
        STRepository.save(sample);
        return sample.getSamplePk();
    }
    */
    @Override
    public Long join(Sample sample){
        log.info("SampleServiceImpl join.."+ sample);

        //암호화 대칭키
        AES128 aes128 = new AES128("1234567891011120"); //16자리만 됨
        String enc = null;
        String dec = null;
        try {
            enc = aes128.encrypt(sample.getSamplePw()); //암호화
            dec = aes128.decrypt(enc); //복호화
        }catch (Exception e){
            e.printStackTrace();
        }

        log.info("join 암호화 : "+enc);
        log.info("join 복호화 : "+dec);

        if(enc == null && dec == null){
            return  -1L;
        }

        // ✅ 암호화된 비밀번호를 엔티티에 반영
        sample.setSamplePw(enc);

        STRepository.save(sample);
        return sample.getSamplePk();
    }

    /* 전체 회원 조회 */
    @Override
    @Transactional(readOnly = true) //읽기용
    public List<Sample> findSampleTable() {
        log.info("SampleServicelmpl findSampleTable..");
        return STRepository.findAll();
    }

    /* 한명 */
    @Override
    public Optional<Sample> findOne(Long pk) {
        log.info("SampleServicelmpl findOne.."+ pk );
        return STRepository.findById(pk);
    }

    @Override
    public List<Sample> findById(String pk) {
        log.info("SampleServicelmpl findById.."+ pk );
        return null; //안씀
    }

    /* 삭제 */
    @Override
    public void deleteById(Sample sampleTable) {
        log.info("SampleServicelmpl deleteById.."+ sampleTable.toString() );
        STRepository.deleteById(sampleTable.getSamplePk());
    }
}
