/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.entidades;

import fran.dae.ujabank.dto.MovimientoDTO;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author fran
 */


@Entity
public class Movimiento implements Serializable {
    
    @Id
    private String id;
    private String tipo;
    private String concepto;
    private float importe;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    private String cuentaEmisora;
    private String entidadReceptora;
    
    /**
     * @brief Constructor por defecto
     */
    public Movimiento() {
        
    }
    
    /**
     * @brief Constructor parametrizado
     * @param _id ID de movimiento
     * @param _tipo Tipo de movimiento (viene de un enum)
     * @param _concepto Concepto del movimiento
     * @param _importe Importe de la operación
     * @param _fecha Fecha de la operacion
     * @param _cuentaEmisora ID de la cuenta emisora
     * @param _entidadReceptora ID de la cuenta receptora
     */
    public Movimiento(String _id, String _tipo, String _concepto, float _importe, Date _fecha, String _cuentaEmisora, String _entidadReceptora) {
        id = _id;
        tipo = _tipo;
        concepto = _concepto;
        importe = _importe;
        fecha = _fecha;
        cuentaEmisora = _cuentaEmisora;
        entidadReceptora = _entidadReceptora;
    }

    //   ___ ___ _____ _____ ___ ___  ___ 
    //  / __| __|_   _|_   _| __| _ \/ __|
    // | (_ | _|  | |   | | | _||   /\__ \
    //  \___|___| |_|   |_| |___|_|_\|___/
                                   
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
    public String getCuentaReceptora() {
        return entidadReceptora;
    }
    
    
    /**
     * @brief DTO a Movimiento
     * @param dto DTO que se le pasa
     * @return Movimiento que se crea
     */
    public static Movimiento fromDTO(MovimientoDTO dto) {
        
        Movimiento movimiento = new Movimiento(dto.getId(), dto.getTipo(), dto.getConcepto(), dto.getImporte(),
                                               dto.getFecha(), dto.getCuentaEmisora(), dto.getEntidadReceptora());
        
        return movimiento;
    }
    
    /**
     * @brief Movimiento a DTO
     * @return DTO que se crea
     */
    public MovimientoDTO toDTO() {
        
        MovimientoDTO dto = new MovimientoDTO(id, tipo, concepto, importe, fecha, cuentaEmisora, entidadReceptora);
        
        return dto;
    }
    
    
}
