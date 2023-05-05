package com.estudio.springboot.backend.apirest.springbootbackendapirest.controllers;

import com.estudio.springboot.backend.apirest.springbootbackendapirest.models.entity.Cliente;
import com.estudio.springboot.backend.apirest.springbootbackendapirest.models.services.ClienteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    public Cliente show(@PathVariable Long id){
        return clienteService.findById(id);
    }

    //guardar
    @PostMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)//el estado de la respuesta se cambia
    public Cliente create(@RequestBody Cliente cliente){
        return clienteService.save(cliente);
    }

    //update
    @PutMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente update(@RequestBody Cliente cliente, @PathVariable Long id){
        //obtenemos el cliente de la db por su id
        Cliente clienteActual = clienteService.findById(id);

        clienteActual.setNombre(cliente.getNombre());
        clienteActual.setApellido(cliente.getApellido());
        clienteActual.setEmail(cliente.getEmail());

        return clienteService.save(clienteActual);// se pone el cliente actual porque es el que contiene los datos a modificar
    }

    //delete
    @DeleteMapping("/clientes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        clienteService.delete(id);
    }
}
