package com.estudio.springboot.backend.apirest.springbootbackendapirest.models.services;

import com.estudio.springboot.backend.apirest.springbootbackendapirest.models.dao.IClienteDao;
import com.estudio.springboot.backend.apirest.springbootbackendapirest.models.entity.Cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service()//componente de servicio de spring
public class ClienteServiceImpl implements IClienteService{
    //inyectamos la interfaz con los metodos de crud repository
    @Autowired
    private IClienteDao clienteDao;

    @Transactional(readOnly = true)//no es necesario porque ya está integrado
    @Override
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();//como retorna un iterable toca hacerle un cast
    }

    @Transactional(readOnly = true)
    @Override
    public Cliente findById(Long id) {
        return clienteDao.findById(id).orElse(null);//retorna un optional
    }

    @Transactional
    @Override
    public Cliente save(Cliente cliente) {
        return clienteDao.save(cliente);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);

    }
}
