package Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Persistencia.Dron;
import Persistencia.Portadron;
import Persistencia.daos.DAOPortadron;
import Persistencia.daos.DAODron;

@RestController
@RequestMapping("/api/portadrones")

public class PortadronController {

	@Autowired  
    private DAOPortadron portadrones;

	@Autowired
    private DAODron Ddrones;

	@GetMapping("/{id}/{tipo}")
	public Portadron obtenerPortadron(
	        @PathVariable String id,
	        @PathVariable String tipo) {

	    Portadron p = portadrones.find(id, tipo);

	    if(p != null){
	    	List<Dron> drones = Ddrones.ListarDrones(id, tipo);
	        p.setDrones(drones);
	    }

	    return p;
	}
}
