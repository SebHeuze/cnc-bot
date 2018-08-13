package org.cnc.cncbot.stats.entities;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


/**
 * The persistent class for the poi_hist database table.
 * 
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="poi_hist")
public class PoiHist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="id_hist")
	private Integer idHist;

	private Integer id;
	
	@Column(name="coord_x")
	private Integer coordX;

	@Column(name="coord_y")
	private Integer coordY;

	@Column(name="id_alliance")
	private Integer idAlliance;

	private Integer level;

	private Integer type;

	@Temporal(TemporalType.DATE)
	private Date date;
}