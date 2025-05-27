package com.example.DiscountBackend.entities;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrackingData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idproduct;
    private String pagename;
    private Long timespent;
    private Integer clicks;
    private Long idLink;

    @Temporal(TemporalType.TIMESTAMP)
    private Date entryTimeFormatted;

    private Double latitude;
    private Double longitude;
    private String sessionId;

    @PrePersist
    protected void onCreate() {
        entryTimeFormatted = new Date();
    }
}
