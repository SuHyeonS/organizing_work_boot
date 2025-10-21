package dggs.organizing_work_boot.sample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {
    //jpa 기본
}