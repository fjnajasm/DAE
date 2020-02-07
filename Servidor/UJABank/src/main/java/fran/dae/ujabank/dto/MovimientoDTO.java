/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.dto;

import java.util.Date;

/**
 *
 * @author fran
 */
public class MovimientoDTO {
    
    private String id;
    private String tipo;
    private String concepto;
    private float importe;
    private Date fecha;
    private String cuentaEmisora;
    private String entidadReceptora;

    public MovimientoDTO() {
    }
    
    

    public MovimientoDTO(String id, String tipo, String concepto, float importe, Date fecha, String cuentaEmisora, String entidadReceptora) {
        this.id = id.substring(20, id.length());
        this.tipo = tipo;
        this.concepto = concepto;
        this.importe = importe;
        this.fecha = fecha;
        this.cuentaEmisora = cuentaEmisora;
        this.entidadReceptora = entidadReceptora;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return the concepto
     */
    public String getConcepto() {
        return concepto;
    }
    
    /**
     * @return the importe
     */
    public float getImporte() {
        return importe;
    }
    
    
    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @return the cuentaEmisora
     */
    public String getCuentaEmisora() {
        return cuentaEmisora;
    }

    /**
     * @return the entidadReceptora
     */
    public String getEntidadReceptora() {
        return entidadReceptora;
    }

    

    
    
}
