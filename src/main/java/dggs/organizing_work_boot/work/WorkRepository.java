package dggs.organizing_work_boot.work;

import dggs.organizing_work_boot.work.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    //jpa 기본

    @Query("SELECT w FROM Work w LEFT JOIN FETCH w.children WHERE w.parent IS NULL")
    List<Work> findAllRootWorks();

    @Query("SELECT w FROM Work w LEFT JOIN FETCH w.children WHERE w.id = :id")
    Optional<Work> findWorkTreeById(@Param("id") Long id);
}