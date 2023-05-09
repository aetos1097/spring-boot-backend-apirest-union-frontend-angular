package com.estudio.springboot.backend.apirest.springbootbackendapirest.controllers;

import com.estudio.springboot.backend.apirest.springbootbackendapirest.models.entity.Cliente;
import com.estudio.springboot.backend.apirest.springbootbackendapirest.models.services.ClienteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200","*"})//forma para conectar el back con el front en angular, le dejamos todos los permisos
@RestController
@RequestMapping("/api")
public class ClienteRestController {
    @Autowired
    private ClienteServiceImpl clienteService;

    @GetMapping("/clientes")
    public List<Cliente> index(){
        return clienteService.findAll();

    }
    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Cliente cliente = null;
        Map<String,Object> response = new HashMap<>();
        try{//se intenta la consulta
            cliente = clienteService.findById(id);
        }catch(DataAccessException e){//si hay un error en el servidor capturaras el error
            response.put("mensaje","Error al realizar la consulta en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));//captura el error de forma mas detalla
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(cliente == null){//si el cliente es nulo se pasa el mensaje
            response.put("mensaje","El Cliente con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
    }

    //guardar
    @PostMapping("/clientes")
    //@ResponseStatus(HttpStatus.CREATED)//el estado de la respuesta se cambia
    public ResponseEntity<?> create(@RequestBody Cliente cliente){
        Cliente clienteNuevo = null;
        Map<String,Object> response = new HashMap<>();// lugar donde ira lso mensajes de exito o error
        //capturamos si hay errores
        try{
            clienteNuevo = clienteService.save(cliente);

        }catch(DataAccessException e){
            response.put("mensaje","Error al realizar insert en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El cliente ha sido creado");
        response.put("cliente",clienteNuevo);
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }

    //update
    @PutMapping("/clientes/{id}")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id){
        //obtenemos el cliente de la db por su id
        Cliente clienteActual = clienteService.findById(id);
        Cliente clienteActualizado = null;
        Map<String,Object> response = new HashMap<>();// lugar donde ira lso mensajes de exito o error, tambien nuestra respuesta
        //si el id del cliente es nullo
        if( clienteActual == null){
            response.put("mensaje","Error: no se pudo editar, el cliente Id: ".concat(id.toString()).concat(" no existe en la abse de datos"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try{
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setCreateAt(cliente.getCreateAt());

            clienteActualizado = clienteService.save(clienteActual);

        }catch(DataAccessException e){
            response.put("mensaje","Error al actualizar el cliente en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);

        }
        response.put("mensaje","El Cliente ha sido actualizado");
        response.put("cliente", clienteActualizado);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //delete
    @DeleteMapping("/clientes/{id}")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id){
        //todo: verificar porque el catch no se esta tomando
        //Map<String, Object> response = new HashMap<>();

        Map<String,Object> response = new HashMap<>();

        try{

            clienteService.delete(id);

        }catch(DataAccessException e){
            response.put("mensaje","Error al eliminar el cliente en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido eliminado");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
