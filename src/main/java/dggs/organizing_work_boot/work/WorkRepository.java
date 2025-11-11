package dggs.organizing_work_boot.work;

import dggs.organizing_work_boot.work.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    //jpa 기본

    @Query("SELECT w FROM Work w LEFT JOIN FETCH w.children WHERE w.parent IS NULL")
    List<Work> findAllRootWorks();

    @Query("SELECT w FROM Work w LEFT JOIN FETCH w.children WHERE w.id = :id")
    Optional<Work> findWorkTreeById(@Param("id") Long id);

    List<Work> findByParent_WorkPk(Long parentId);
    List<Work> findByParentIsNull();
    /*
    @Query(value = """
        SELECT
            c.table_schema,
            c.table_name,
            c.column_name,
            c.data_type,
            col_description(pg_class.oid, c.ordinal_position) AS column_comment
        FROM information_schema.columns c
        JOIN pg_class ON pg_class.relname = c.table_name
        WHERE c.table_name = :tableName
          AND c.table_schema = :schemaName
        ORDER BY c.ordinal_position
        """, nativeQuery = true)
    List<Object[]> findTableInfo(
            @Param("tableName") String tableName,
            @Param("schemaName") String schemaName);
     */

    @Query(value = """
            SELECT
                c.table_schema,
                c.table_name,
                c.column_name,
                c.data_type,
                -- 숫자_ 형식 제거한 주석
                REGEXP_REPLACE(col_description(pg_class.oid, c.ordinal_position), '^[0-9]+_', '') AS column_comment
            FROM information_schema.columns c
            JOIN pg_class
              ON pg_class.relname = c.table_name
            WHERE c.table_name = :tableName
              AND c.table_schema = :schemaName
              -- 주석이 있고, _로 시작하지 않으며 숫자_ 형식이 있거나 일반 문자열인 경우
              AND col_description(pg_class.oid, c.ordinal_position) IS NOT NULL
              AND col_description(pg_class.oid, c.ordinal_position) NOT LIKE '\\_%'
            ORDER BY
              -- 숫자_ 앞부분 추출하여 숫자 순 정렬 (없으면 999로 처리)
              COALESCE(NULLIF(REGEXP_REPLACE(col_description(pg_class.oid, c.ordinal_position), '^([0-9]+)_.*', '\\1'), ''), '999')::int
        """, nativeQuery = true)
    List<Object[]> findTableInfo(
            @Param("tableName") String tableName,
            @Param("schemaName") String schemaName);

    @Query("SELECT w FROM Work w LEFT JOIN FETCH w.parent WHERE w.workPk = :pk")
    Optional<Work> findByIdWithParent(@Param("pk") Long pk);

    //옵션2 가져오기
    //요청자
    @Query("SELECT w.workRequesterSituation FROM Work w GROUP BY w.workRequesterSituation")
    List<String> findDistinctRequesterSituations();
    //수행자
    @Query("SELECT w.workPerformerSituation FROM Work w GROUP BY w.workPerformerSituation")
    List<String> findDistinctPerformerSituations();

}