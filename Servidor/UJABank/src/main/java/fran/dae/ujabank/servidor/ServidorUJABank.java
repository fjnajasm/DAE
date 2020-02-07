/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.servidor;

import fran.dae.ujabank.clientes.Cliente;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author fran
 */

@SpringBootApplication(scanBasePackages = {"fran.dae.ujabank.beans", "fran.dae.ujabank.dao", "fran.dae.ujabank.restcontrollers", "fran.dae.ujabank.seguridad"})
@EntityScan(basePackages="fran.dae.ujabank.entidades")
public class ServidorUJABank {
    
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ServidorUJABank.class);
        ApplicationContext appContext = app.run(args);
        
        Cliente cliente = new Cliente(appContext);
        cliente.operar();
      
    }
}
