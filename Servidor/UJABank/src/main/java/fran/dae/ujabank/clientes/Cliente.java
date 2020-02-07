/** **********************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 *********************************************************** */
package fran.dae.ujabank.clientes;

import fran.dae.ujabank.clientes.menu.ConsoleUtils;
import fran.dae.ujabank.clientes.menu.Menu;
import fran.dae.ujabank.clientes.menu.MenuItem;
import fran.dae.ujabank.dto.CuentaBancariaDTO;
import fran.dae.ujabank.dto.MovimientoDTO;
import fran.dae.ujabank.dto.UsuarioDTO;
import fran.dae.ujabank.excepciones.CuentaBancariaInexistente;
import fran.dae.ujabank.excepciones.CuentasIguales;
import fran.dae.ujabank.excepciones.ImporteIncorrecto;
import fran.dae.ujabank.excepciones.SaldoInsuficiente;
import fran.dae.ujabank.excepciones.UsuarioNoRegistrado;
import fran.dae.ujabank.excepciones.UsuarioYaRegistrado;
import fran.dae.ujabank.excepciones.formato.DNIoNIFIncorrecto;
import fran.dae.ujabank.excepciones.formato.EmailIncorrecto;
import fran.dae.ujabank.excepciones.formato.FechaNacimientoIncorrecto;
import fran.dae.ujabank.excepciones.formato.TelefonoIncorrecto;
import fran.dae.ujabank.interfaces.UJABankInterface;
import java.util.Date;
import java.util.List;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author fran
 */
public class Cliente {

    UJABankInterface servicios;

    public Cliente() {
    }

    public Cliente(ApplicationContext context) {
        servicios = context.getBean(UJABankInterface.class);
    }

    public void registrarUsuario() {

        String DNI = ConsoleUtils.getStringField("Introducir NIF/NIE");
        String nombre = ConsoleUtils.getStringField("Introducir nombre y apellidos");
        String direccion = ConsoleUtils.getStringField("Introducir dirección");
        String tlf = ConsoleUtils.getStringField("Introducir nº de teléfono");
        String eMail = ConsoleUtils.getStringField("Introducir E-Mail");
        Date fechaNacimiento = ConsoleUtils.getDateField("Introducir fecha de nacimiento");

        //UsuarioDTO usu = new UsuarioDTO(nombre, DNI, direccion, tlf, eMail, fechaNacimiento, "123");

        try {
            //servicios.registrarUsuario(usu);
            //System.out.println(usu.getDNI() + " registrado con éxito");

        } catch (UsuarioYaRegistrado u) {
            System.err.println("Usuario ya registrado en la app");
        } catch (FechaNacimientoIncorrecto n) {
            System.err.println("Fecha nacimiento incorrecta");
        } catch (DNIoNIFIncorrecto d) {
            System.err.println("DNI o NIF incorrecto");
        } catch (EmailIncorrecto dd) {
            System.err.println(" E-Mail incorrecto");
        } catch (TelefonoIncorrecto t) {
            System.err.println("Telefono incorrecto");
        }
    }

    public void crearCuentaBancaria() {
        try {

            String DNIUsuario = ConsoleUtils.getStringField("Introduzca el DNI del usuario");

            CuentaBancariaDTO cDTO = servicios.crearCuentaNueva(DNIUsuario);

            ConsoleUtils.println("Cuenta Bancaria con id: " + cDTO.getId() + " registrada con éxito");

        } catch (UsuarioNoRegistrado u) {
            System.err.println("Usuario no registrado");
        }
    }

    public void ingresarDinero() {

        try {

            String DNI = ConsoleUtils.getStringField("Introduzca el DNI");
            String idCuenta = ConsoleUtils.getStringField("Introduzca el id de cuenta bancaria");
            Float importe = ConsoleUtils.getFloatField("Introduzca el importe");
            String concepto = ConsoleUtils.getStringField("Introduzca el concepto");

            MovimientoDTO mDTO = servicios.ingresoDinero(DNI, importe, idCuenta, concepto);

            ConsoleUtils.println("Movimiento: " + mDTO.getId() + " de tipo " + mDTO.getTipo() + " realizado con éxito");

        } catch (CuentaBancariaInexistente cbi) {
            System.err.println("Cuenta Bancaria inexistente");
        } catch (UsuarioNoRegistrado unr) {
            System.err.println("Usuario no registrado");
        } catch (ImporteIncorrecto ii) {
            System.err.println("Importe introducido incorrecto");
        }
    }

    public void retirarUJACoin() {

        try {

            String dniUsuario = ConsoleUtils.getStringField("Introduzca el DNI del usuario");
            String idCuenta = ConsoleUtils.getStringField("Introduzca el id de cuenta bancaria");
            String tj = ConsoleUtils.getStringField("Introduzca el Numero de tarjeta");
            Float importe = ConsoleUtils.getFloatField("Introduzca el importe que quiere retirar");
            String concepto = ConsoleUtils.getStringField("Introduzca el concepto");

            MovimientoDTO mDTO = servicios.retirarUJACoin(dniUsuario, idCuenta, importe, tj, concepto);

            ConsoleUtils.println("Movimiento: " + mDTO.getId() + " de tipo " + mDTO.getTipo() + " realizado con éxito");

        } catch (UsuarioNoRegistrado unr) {
            System.err.println("Usuario no registrado");
        } catch (CuentaBancariaInexistente cbi) {
            System.err.println("Cuenta bancaria inexistente");
        } catch (SaldoInsuficiente si) {
            System.err.println("Saldo en la cuenta bancaria insuficiente");
        } catch (ImporteIncorrecto ii) {
            System.err.println("Importe no puede ser igual o menor que 0");
        }
    }

    public void transferirUJACoins() {

        try {
            String cbe = ConsoleUtils.getStringField("Introduzca la cuenta bancaria emisora");
            String cbr = ConsoleUtils.getStringField("Introduzca la cuenta bancaria receptora");
            Float importe = ConsoleUtils.getFloatField("Introduzca el importe a transferir");
            String concepto = ConsoleUtils.getStringField("Introduzca el concepto");

            List<MovimientoDTO> res = servicios.transferirUJACoin("21025923J", cbe, cbr, importe, concepto);

            for (MovimientoDTO m : res) {
                ConsoleUtils.println("Movimiento " + m.getId() + " de tipo " + m.getTipo() + " realizado con éxito");
            }

        } catch (UsuarioNoRegistrado unr) {
            System.err.println("Usuario no registrado");
        } catch (CuentaBancariaInexistente cbi) {
            System.err.println("Cuenta bancaria inexistente");
        } catch (SaldoInsuficiente si) {
            System.err.println("Saldo en la cuenta bancaria insuficiente");
        } catch (CuentasIguales ci) {
            System.err.println("Cuentas no pueden ser iguales");
        }
    }

    public void consultarMovimientosUsuario() {

        try {

            String dni = ConsoleUtils.getStringField("Introduzca el DNI");
            List<MovimientoDTO> movimientos = servicios.consultarMovimientosUsuario(dni);

            if (movimientos.isEmpty()) {
                System.out.println("Usuario sin movimientos");
            }

            for (MovimientoDTO m : movimientos) {
                ConsoleUtils.println("Movimiento con ID: " + m.getId());
                ConsoleUtils.println("Concepto: " + m.getConcepto());
                ConsoleUtils.println("Importe: " + m.getImporte());
                ConsoleUtils.println("Tipo: " + m.getTipo());
                ConsoleUtils.println("Entidad emisora: " + m.getCuentaEmisora());

                if (m.getEntidadReceptora() != "") {
                    ConsoleUtils.println("Entidad receptora: " + m.getEntidadReceptora());
                }
            }

        } catch (UsuarioNoRegistrado unr) {
            System.err.println("Usuario no registrado");
        } catch (CuentaBancariaInexistente cbi) {
            System.err.println("Usuario sin cuenta bancaria");
        }
    }

    public void consultarMovimientosCuenta() {

        try {

            String cuentaBancaria = ConsoleUtils.getStringField("Introduzca la cuenta bancaria");
            List<MovimientoDTO> movimientos = servicios.consultarMovimientosCuenta("21025923J", cuentaBancaria);

            if (movimientos.isEmpty()) {
                ConsoleUtils.println("Cuenta bancaria sin movimientos");
            }

            for (MovimientoDTO m : movimientos) {
                ConsoleUtils.println("Movimiento con ID: " + m.getId());
                ConsoleUtils.println("Concepto: " + m.getConcepto());
                ConsoleUtils.println("Importe: " + m.getImporte());
                ConsoleUtils.println("Tipo: " + m.getTipo());
                ConsoleUtils.println("Entidad emisora: " + m.getCuentaEmisora());

                if (m.getEntidadReceptora() != "") {
                    ConsoleUtils.println("Entidad receptora: " + m.getEntidadReceptora());
                }
            }

        } catch (CuentaBancariaInexistente cbi) {
            System.err.println("Cuenta bancaria inexistente");
        }
    }

    public void ingresarDineroPorUsuario() {

        try {

            String dni = ConsoleUtils.getStringField("Introduzca el DNI");
            List<CuentaBancariaDTO> cuentas = servicios.consultarCuentasUsuario(dni);

            if (cuentas.isEmpty()) {
                ConsoleUtils.println("Este usuario no tiene cuenta bancarias");
            } else {

                Float importe = ConsoleUtils.getFloatField("Introduzca el importe");
                String concepto = ConsoleUtils.getStringField("Introduzca el concepto");

                int flag = 1;

                for (CuentaBancariaDTO c : cuentas) {
                    ConsoleUtils.println(flag + ". " + "Cuenta bancaria: " + c.getId());
                    flag++;
                }

                flag = ConsoleUtils.getIntField("Seleccione cuenta") - 1;

                MovimientoDTO mDTO = servicios.ingresoDinero(dni, importe, cuentas.get(flag).getId(), concepto);

                ConsoleUtils.println("Movimiento: " + mDTO.getId() + " de tipo " + mDTO.getTipo() + " realizado con éxito");
            }

        } catch (UsuarioNoRegistrado unr) {
            System.err.println("Usuario no registrado");
        }
    }

    public void operar() {
        Menu menuPrincipal = new Menu("Salir", "Salir");

        menuPrincipal.setTitle("Cliente UJABank");

        menuPrincipal.addItem(new MenuItem("Registrar usuario: ", this::registrarUsuario));
        menuPrincipal.addItem(new MenuItem("Crear cuenta bancaria: ", this::crearCuentaBancaria));
        menuPrincipal.addItem(new MenuItem("Ingresar dinero eligiendo cuenta", this::ingresarDineroPorUsuario));
        menuPrincipal.addItem(new MenuItem("Ingresar directo en cuenta: ", this::ingresarDinero));
        menuPrincipal.addItem(new MenuItem("Retirar UJACoins: ", this::retirarUJACoin));
        menuPrincipal.addItem(new MenuItem("Transferir UJACoins: ", this::transferirUJACoins));
        menuPrincipal.addItem(new MenuItem("Lista de movimientos: ", this::consultarMovimientosUsuario));
        menuPrincipal.addItem(new MenuItem("Lista de movimientos por cuenta: ", this::consultarMovimientosCuenta));

        menuPrincipal.execute();
    }

}
