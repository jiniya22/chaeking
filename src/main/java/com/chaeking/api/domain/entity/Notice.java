package com.chaeking.api.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "notice")
@Entity
@Where(clause = "active = 1")
public class Notice extends BaseBoard {

}
