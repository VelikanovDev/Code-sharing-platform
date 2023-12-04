package com.velikanovdev.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "snippets")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Snippet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String code;

    @Column
    private LocalDateTime date;

    @Column
    private LocalDateTime editDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User author;

    @Transient
    @JsonIgnore
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public String getDate() {
        return date.format(formatter);
    }

    public String getEditDate() {
        if (this.editDate == null) return null;
        return editDate.format(formatter);
    }

}
