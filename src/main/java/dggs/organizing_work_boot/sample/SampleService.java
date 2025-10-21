package dggs.organizing_work_boot.sample;

import java.util.List;
import java.util.Optional;

public interface SampleService {
    //디폴트 public
    Long join(Sample sample);
    List<Sample> findSampleTable();
    Optional<Sample> findOne(Long pk);
    List<Sample> findById(String pk); //안씀
    void deleteById(Sample sampleTable);
}
