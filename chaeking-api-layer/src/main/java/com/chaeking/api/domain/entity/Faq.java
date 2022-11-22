package com.chaeking.api.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "faq")
@Entity
@Where(clause = "active = 1")
public class Faq extends BaseBoard {

}
