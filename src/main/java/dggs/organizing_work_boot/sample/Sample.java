package dggs.organizing_work_boot.sample;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sample")
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="sample_pk")
    private Long samplePk;

    @Column(name="sample_id")
    private String sampleId;

    @Column(name="sample_name")
    private String sampleName;

    @Column(name="sample_pw")
    private String samplePw;

}
