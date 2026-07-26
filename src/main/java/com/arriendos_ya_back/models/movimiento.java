package com.arriendos_ya_back.models;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "movimientos")
public class movimiento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoMovimiento tipo;
	private String concepto;
	private double monto;
	private ZonedDateTime fecha;

	@Column(name = "url_comprobante", nullable = true)
	private String urlComprobante;

	@ManyToOne(optional = false)
	@JoinColumn(name = "propiedad_id", nullable = false)
	private propiedad propiedad;

	// --- GETTERS Y SETTERS ---
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public TipoMovimiento getTipo() { return tipo; }
	public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

	public String getConcepto() { return concepto; }
	public void setConcepto(String concepto) { this.concepto = concepto; }

	public double getMonto() { return monto; }
	public void setMonto(double monto) { this.monto = monto; }

	public ZonedDateTime getFecha() { return fecha; }
	public void setFecha(ZonedDateTime fecha) { this.fecha = fecha; }

	public String getUrlComprobante() { return urlComprobante; }
	public void setUrlComprobante(String urlComprobante) { this.urlComprobante = urlComprobante; }

	public propiedad getPropiedad() { return propiedad; }
	public void setPropiedad(propiedad propiedad) { this.propiedad = propiedad; }
}
