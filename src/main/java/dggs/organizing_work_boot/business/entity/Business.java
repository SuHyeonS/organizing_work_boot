package dggs.organizing_work_boot.business.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "business")
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="business_pk")
    private Long businessPk;

    @Column(name="business_area")
    private String businessArea; //지역

    @Column(name="business_name")
    private String businessName; //사업명

    @Column(name="business_type")
    private String businessType; //사업구분 (신규 / 유지보수)


    //단순하게 부모 → 자식만 JSON에 필요하면 → @JsonManagedReference / @JsonBackReference
    //양쪽 정보 모두 필요하고 순환 구조가 깊으면 → @JsonIdentityInfo

    // (선택사항) 역방향 매핑 — Business 기준으로 Management 목록 접근 가능 > 참조만 가능
    //cascade = CascadeType.ALL 부모를 조작할 때 자식도 함께 처리해라 의미로 단점은 부모 삭제시 자식도 삭제됨 다대다 관계에서는 사용 지향하는게 좋음.
    //orphanRemoval = true 부모 컬렉션에서 제거된 자식을 DB에서 자동 삭제
    //mappedBy > class @ManyToOne의 필드 명칭
    @OneToMany(mappedBy = "businessFk", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference //무한루프 방지
    private List<BusinessManagement> businessManagementList;

}
