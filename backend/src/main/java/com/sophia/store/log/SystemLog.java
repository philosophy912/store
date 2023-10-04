package com.sophia.store.log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "SYSTEMLOG")
@JsonIgnoreProperties(value = {"handler"})
public class SystemLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "operation", nullable = false)
    private String operation;
    @Column(name = "method", nullable = false)
    private String method;
    @Column(name = "params", nullable = false)
    private String params;
    @Column(name = "ipAddress", nullable = false)
    private String ipAddress;
    @Column(name = "time", nullable = false)
    private Long time;
    @Column(name = "createDate", nullable = false)
    private Date createDate;
}
