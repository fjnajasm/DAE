/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.entidades;

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
public class TarjetaDeCredito implements Serializable{
    
    @Id
    private final String numeroTarjeta;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCaducidad;
    
    private final String CVV;
    private final String nombreTitular;
    private final String banco;
    
//    @ManyToOne
//    @JoinColumn(name = "tarjetaAsociada")
//    private Usuario usuarioAsociado;
    

    /**
     * @brief Constructor por defecto
     */
    public TarjetaDeCredito() {
        this.numeroTarjeta = null;
        this.fechaCaducidad = null;
        this.CVV = null;
        this.nombreTitular = null;
        this.banco = null;
//        this.usuarioAsociado = null;
    }

    /**
     * @brief Constructor parametrizado
     * @param numeroTarjeta ID de la tarjeta
     * @param fechaCaducidad Fecha de caducidad de la tarjeta
     * @param CVV Codigo de seguridad de la tarjeta
     * @param nombreTitular Titular de la tarjeta
     * @param banco Entidad bancaria
     */
    public TarjetaDeCredito(String numeroTarjeta, Date fechaCaducidad, String CVV, String nombreTitular, String banco) {
        this.numeroTarjeta = numeroTarjeta;
        this.fechaCaducidad = fechaCaducidad;
        this.CVV = CVV;
        this.nombreTitular = nombreTitular;
        this.banco = banco;
//        this.usuarioAsociado = usuarioAsociado;
    }

    //   ___ ___ _____ _____ ___ ___  ___ 
    //  / __| __|_   _|_   _| __| _ \/ __|
    // | (_ | _|  | |   | | | _||   /\__ \
    //  \___|___| |_|   |_| |___|_|_\|___/
                     
    /**
     * @return the numeroTarjeta
     */
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    /**
     * @return the fechaCaducidad
     */
    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    /**
     * @return the CVV
     */
    public String getCVV() {
        return CVV;
    }

    /**
     * @return the nombreTitular
     */
    public String getNombreTitular() {
        return nombreTitular;
    }

    /**
     * @return the banco
     */
    public String getBanco() {
        return banco;
    }
    
    /**
     * @return the usuarioAsociado
     */
//    public Usuario getUsuarioAsociado() {
//        return usuarioAsociado;
//    }
//    
//    public String getDNIUsuarioAsociado() {
//        return usuarioAsociado.getDNI();
//    }
//    
//    public String getTokenUsuarioAsociado() {
//        return usuarioAsociado.getToken();
//    }
    //  ___ ___ _____ _____ ___ ___  ___ 
    // / __| __|_   _|_   _| __| _ \/ __|
    // \__ \ _|  | |   | | | _||   /\__ \
    // |___/___| |_|   |_| |___|_|_\|___/
                                   
    /**
     * @param fechaCaducidad the fechaCaducidad to set
     */
    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }
}
