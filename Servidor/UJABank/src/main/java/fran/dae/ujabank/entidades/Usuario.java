/** **********************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 *********************************************************** */
package fran.dae.ujabank.entidades;

import fran.dae.ujabank.dto.UsuarioDTO;
import fran.dae.ujabank.excepciones.formato.DNIoNIFIncorrecto;
import fran.dae.ujabank.excepciones.formato.EmailIncorrecto;
import fran.dae.ujabank.excepciones.formato.FechaNacimientoIncorrecto;
import fran.dae.ujabank.excepciones.formato.TelefonoIncorrecto;
import fran.dae.ujabank.util.Formato;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author fran
 */
@Entity
public class Usuario implements Serializable {

    private String nombre;

    @Id
    private String DNI;
    private String direccion;
    private String numeroTelefono;
    private String eMail;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;

    @OneToMany(mappedBy = "usuarioAsociado", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Map<String, CuentaBancaria> cuentasBancarias;
    
    private String password;
    //private String token;
    
//    @OneToMany(mappedBy = "tarjetaAsociada", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    private Map<String, TarjetaDeCredito> tarjetasDeCredito;
    
    /**
     * @brief Constructor por defecto
     */
    public Usuario() {
    }

    /**
     * @param _password
     * @brief Constructor parametrizado
     * @param _nombre nombre del usuario
     * @param _DNI DNI del usuario
     * @param _direccion direccion del usuario
     * @param _numeroTelefono Numero de telefono del usuario
     * @param _eMail E-Mail del usuario
     * @param _fechaNacimiento Fecha de nacimiento del usuario
     */
    public Usuario(String _nombre, String _DNI, String _direccion, String _numeroTelefono, String _eMail, Date _fechaNacimiento, String _password) {

        if (!Formato.patronDNI.matcher(_DNI).matches() && !Formato.patronNIF.matcher(_DNI).matches()) {
            throw new DNIoNIFIncorrecto();
        }
        if (!Formato.patronEMAIL.matcher(_eMail).matches()) {
            throw new EmailIncorrecto();
        }
        if (!Formato.patronTLF.matcher(_numeroTelefono).matches()) {
            throw new TelefonoIncorrecto();
        }
        if (!Formato.comprobarFechas(_fechaNacimiento)) {
            throw new FechaNacimientoIncorrecto();
        }

        nombre = _nombre;
        DNI = _DNI;
        direccion = _direccion;
        numeroTelefono = _numeroTelefono;
        eMail = _eMail;
        fechaNacimiento = _fechaNacimiento;
        cuentasBancarias = new TreeMap<>();
//        tarjetasDeCredito = new TreeMap<>();
        
        password = _password;
        
        //token = DigestUtils.md5DigestAsHex(DNI.getBytes());
        
    }

    //   ___ ___ _____ _____ ___ ___  ___ 
    //  / __| __|_   _|_   _| __| _ \/ __|
    // | (_ | _|  | |   | | | _||   /\__ \
    //  \___|___| |_|   |_| |___|_|_\|___/
    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the DNI
     */
    public String getDNI() {
        return DNI;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @return the numeroTelefono
     */
    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    /**
     * @return the eMail
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * @return the fechaNacimiento
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * @return the cuentasBancarias
     */
    public Map<String, CuentaBancaria> getCuentasBancarias() {
        return cuentasBancarias;
    }

    /**
     * @brief Metodo que te devuelve la cuenta por id de un usuario
     * @param idCuenta ID de la cuenta
     * @return Cuenta bancaria que se manda
     */
    public CuentaBancaria getCuentaBancaria(String idCuenta) {
        return cuentasBancarias.get(idCuenta);
    }
    
//    /**
//     * @return the tarjetasDeCredito
//     */
//    public Map<String, TarjetaDeCredito> getTarjetasDeCredito() {
//        return tarjetasDeCredito;
//    }
//    
//    /**
//     * @brief Metodo que devuelve la tarjeta por ID
//     * @param idTarjeta ID de la tarjeta
//     * @return the tarjeta
//     */
//    public TarjetaDeCredito getTarjetaCredito(String idTarjeta) {
//        return tarjetasDeCredito.get(idTarjeta);
//    }
    
    /**
     * @brief Metodo que devuelve el token
     * @return token
     */
//    public String getToken() {
//        return token;
//    }
    
    /**
     * @return the Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @brief Metodo para crear una cuenta bancaria con id único y saldo 0
     * @return Cuenta bancaria creada
     */
    public CuentaBancaria crearCuentaBancaria() {

        String idOficina = "ES9632";
        String idCuenta;
        do {
            idCuenta = RandomStringUtils.randomNumeric(14);
        } while (cuentasBancarias.containsKey(idOficina + idCuenta));

        idCuenta = idOficina + idCuenta;
        CuentaBancaria cuentaBancaria = new CuentaBancaria(idCuenta, 0, this);

        return cuentaBancaria;
    }

    public void anadirCuentaBancaria(CuentaBancaria cuentaBancaria) {
        cuentasBancarias.put(cuentaBancaria.getId(), cuentaBancaria);
    }

    /**
     * @brief DTO a Usuario
     * @param dto DTO de usuario
     * @return Nuevo usuario
     */
    public static Usuario fromDTO(UsuarioDTO dto) {
        
        Date dt = new Date(dto.getFechaNacimiento());
        if (!Formato.patronDNI.matcher(dto.getDNI()).matches() && !Formato.patronNIF.matcher(dto.getDNI()).matches()) {
            throw new DNIoNIFIncorrecto();
        }
        if (!Formato.patronEMAIL.matcher(dto.geteMail()).matches()) {
            throw new EmailIncorrecto();
        }
        if (!Formato.patronTLF.matcher(dto.getNumeroTelefono()).matches()) {
            throw new TelefonoIncorrecto();
        }
        if (!Formato.comprobarFechas(dt)) {
            throw new FechaNacimientoIncorrecto();
        }
        Usuario usuario = new Usuario(dto.getNombre(), dto.getDNI(), dto.getDireccion(),
                dto.getNumeroTelefono(), dto.geteMail(), dt, dto.getPassword());

        return usuario;
    }

    /**
     * @brief Usuario a DTO
     * @return DTO de usuario
     */
    public UsuarioDTO toDTO() {
        String fechaN = Formato.sdf.format(fechaNacimiento);
        UsuarioDTO dto = new UsuarioDTO(nombre, DNI, direccion, numeroTelefono, eMail, fechaN, password);

        return dto;
    }
    
    
}
