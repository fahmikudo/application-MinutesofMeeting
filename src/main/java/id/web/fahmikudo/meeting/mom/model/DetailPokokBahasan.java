package id.web.fahmikudo.meeting.mom.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "detail_pokok_bahasan")
@Getter
@Setter
public class DetailPokokBahasan implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_detail_pokok_bahasan")
    private String id;

    @Column(nullable = false)
    private int nomor;

    @Column(nullable = false)
    @Type(type = "text")
    private String detailPokokBahasan;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pokok_bahasan", nullable = false)
    private PokokBahasan pokokBahasan;

    public DetailPokokBahasan() {
    }
}
