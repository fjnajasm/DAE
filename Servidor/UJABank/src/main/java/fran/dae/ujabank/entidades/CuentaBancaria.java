/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.entidades;

import es.dae.ujaen.euroujacoinrate.EuroUJACoinRate;
import fran.dae.ujabank.dto.CuentaBancariaDTO;
import fran.dae.ujabank.dto.UsuarioDTO;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author fran
 */
@Entity
public class CuentaBancaria implements Serializable {

    private enum TIPO_TRANSACCION {
        INGRESO, RETIRADA, TRANSFERENCIA, TRASNFERENCIA_A_FAVOR
    }

    @Id
    private String id;
    private float saldo;

    @OneToMany(fetch = FetchType.LAZY)
    private Map<String, Movimiento> movimientosCuenta;

    @ManyToOne
    @JoinColumn(name = "usuarioAsociado")
    private Usuario usuarioAsociado;

    /**
     * @brief Constructor por defecto
     */
    public CuentaBancaria() {

    }

    /**
     * @brief Constructor parametrizado
     * @param _id ID de cuenta bancaria
     * @param _saldo Saldo de la cuenta bancaria
     * @param _usuarioAsociado Usuario asociado a la cuenta bancaria
     */
    public CuentaBancaria(String _id, float _saldo, Usuario _usuarioAsociado) {
        id = _id;
        saldo = _saldo;
        usuarioAsociado = _usuarioAsociado;

        movimientosCuenta = new TreeMap<>();
    }

    //   ___ ___ _____ _____ ___ ___  ___ 
    //  / __| __|_   _|_   _| __| _ \/ __|
    // | (_ | _|  | |   | | | _||   /\__ \
    //  \___|___| |_|   |_| |___|_|_\|___/
    
    public String getId() {
        return id;
    }

    public float getSaldo() {
        return saldo;
    }

    public Map<String, Movimiento> getMovimientosCuenta() {
        return movimientosCuenta;
    }

    public Usuario getUsuarioAsociado() {
        return usuarioAsociado;
    }
    
    public String getDNIUsuarioAsociado() {
        return usuarioAsociado.getDNI();
    }
    
////    public String getTokenUsuarioAsociado() {
////        return usuarioAsociado.getToken();
////    }

    
    /**
     * @brief Metodo para ingresar dinero en la cuenta bancaria actual
     * @param importe Cantidad a ingresar en €
     * @param concepto Concepto que le pasa el usuario que hace el ingreso
     * @return Movimiento creado en la BD
     */
    public Movimiento ingresarDinero(Float importe, String concepto) {

        EuroUJACoinRate eu = new EuroUJACoinRate();

        importe = importe * eu.euroToUJACoinToday();

        this.anadirSaldo(importe);

        Movimiento mov = new Movimiento(this.id + movimientosCuenta.size(), TIPO_TRANSACCION.INGRESO.name(),
                concepto, importe, Date.from(Instant.now()), this.id, "");

        return mov;
    }

    /**
     * @brief Metodo para retirar dinero en una entidad 
     *        bancaria ajena al banco (Tarjetas de credito normalmente)
     * @param importe Cantidad a retirar en UJACoins
     * @param concepto Concepto que le pasa el usuario que hace la retirada
     * @param idEntidadBancaria ID de la entidad bancaria
     * @return Movimiento creado en la BD
     */
    public Movimiento retirarDinero(Float importe, String concepto, String idEntidadBancaria) {

        this.retirarSaldo(importe);

        Movimiento mov = new Movimiento(this.id + movimientosCuenta.size(), TIPO_TRANSACCION.RETIRADA.name(),
                                        concepto, importe, Date.from(Instant.now()), this.id, idEntidadBancaria);

        return mov;
    }

    /**
     * @brief Metodo para transferir UJACoins de una cuenta bancaria a otra
     * @param importe Cantidad a transferir en UJACoins
     * @param concepto Concepto que le pasa el usuario que hace la transferencia
     * @param cuentaBancariaRec Cuenta bancaria receptora
     * @return Listado de movimientos que se crean tanto en la cuenta emisora (de tipo TRANSFERENCIA)
     *         como en la cuenta emisora (de tipo TRANSFERENCIA_A_FAVOR)
     */
    public List<Movimiento> transferenciaBancaria(Float importe, String concepto, CuentaBancaria cuentaBancariaRec) {

        this.retirarSaldo(importe);
        cuentaBancariaRec.anadirSaldo(importe);

        Movimiento movE = new Movimiento(this.id + movimientosCuenta.size(), TIPO_TRANSACCION.TRANSFERENCIA.name(),
                concepto, importe, Date.from(Instant.now()), this.id, cuentaBancariaRec.getId());

        Movimiento movR = new Movimiento(cuentaBancariaRec.getId() + cuentaBancariaRec.movimientosCuenta.size(),
                TIPO_TRANSACCION.TRASNFERENCIA_A_FAVOR.name(), concepto, importe,
                Date.from(Instant.now()), this.id, cuentaBancariaRec.getId());

        List<Movimiento> result = new ArrayList<>();
        result.add(movE);
        result.add(movR);

        return result;
    }

    /**
     * @brief Método para añadir saldo
     * @param importe Importe a añadir
     */
    public void anadirSaldo(Float importe) {
        saldo += importe;
    }

    /**
     * @brief Metodo para retirar saldo
     * @param importe Importe a retirar
     */
    public void retirarSaldo(Float importe) {
        saldo -= importe;
    }

    /**
     * @brief Metodo para añadir el movimiento a la cuenta bancaria
     * @param mov Metodo que hay que añadir
     */
    public void anadirMovimiento(Movimiento mov) {
        this.movimientosCuenta.put(mov.getId(), mov);
    }

    /**
     * @brief CuentaBancariaDTO a CuentaBancaria
     * @param dto DTO de Cuenta Bancaria que hay que transformar
     * @param usuario DTO de usuario para crear la cuenta bancaria
     * @return Cuenta bancaria ya transformada
     */
    public static CuentaBancaria fromDTO(CuentaBancariaDTO dto, UsuarioDTO usuario) {
        
        CuentaBancaria cuentaBancaria = new CuentaBancaria(dto.getId(), dto.getSaldo(), Usuario.fromDTO(usuario));

        return cuentaBancaria;
    }

    /**
     * @brief CuentaBancaria a CuentaBancariaDTO
     * @return DTO de CuentaBancaria
     */
    public CuentaBancariaDTO toDTO() {
        CuentaBancariaDTO dto = new CuentaBancariaDTO(id, saldo, usuarioAsociado.getDNI());

        return dto;
    }

    
    private static Float obtainUJARate() throws Exception{
        final String europeanBankInfo = "https://api.exchangeratesapi.io/latest";
        ResponseEntity<JSONObject> response = new RestTemplate().getForEntity(europeanBankInfo, JSONObject.class);
        JSONObject jsonObject = new JSONObject((Map) response.getBody());
        Object jsonEuropeanRates = jsonObject.get("rates");
        LinkedHashMap mapEuropeanRates = (LinkedHashMap) jsonEuropeanRates;
        Float exchangeRate = (Float) mapEuropeanRates.get("KRW");
        return exchangeRate;
    }
}
