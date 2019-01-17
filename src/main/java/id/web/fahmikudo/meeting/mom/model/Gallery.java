package id.web.fahmikudo.meeting.mom.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "gallery")
@Data
public class Gallery implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_gallery")
    private String id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    @Lob
    private byte[] data;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_meeting", nullable = false)
    private Meeting meeting;

    public Gallery(String fileName, String contentType, byte[] bytes) {
    }
}
