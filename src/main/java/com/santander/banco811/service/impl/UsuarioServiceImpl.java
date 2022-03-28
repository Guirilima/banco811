package com.santander.banco811.service.impl;

import com.santander.banco811.dto.UsuarioRequest;
import com.santander.banco811.dto.UsuarioResponse;
import com.santander.banco811.model.Usuario;
import com.santander.banco811.repository.UsuarioRepository;
import com.santander.banco811.service.UsuarioService;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Random

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ContaRepository contaRepository;

    @Override
    public List<Usuario> getAll(String nome) {

        if (nome != null) {
            return usuarioRepository.findByNome(nome);
        } else {
            return usuarioRepository.findAll();
        }
    }

    @Override
    public UsuarioResponse create(UsuarioRequest usuarioRequest) {
        Usuario usuario = new Usuario(usuarioRequest);
        usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario);
    }

    @Override
    public Usuario getById(Integer id) {
        return usuarioRepository.findById(id).orElseThrow();
    }

    @Override
    public Usuario update(UsuarioRequest usuarioRequest, Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();

        usuario.setNome(usuarioRequest.getNome());
        usuario.setCpf(usuarioRequest.getCpf());
        usuario.setSenha(usuarioRequest.getSenha());

        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Integer id) {
        var usuario = usuarioRepository.findById(id).orElseThrow();

        usuarioRepository.delete(usuario);
    }
    
    @Override
    public void createNewAccount (AccountRequest accountRequest, Integer id) {
        var usuario = this.getById(id); // Validação se existe.
        
        //Criação da conta
        Conta conta = new Conta();
        conta.setUsuario(usuario); //Associação
        
        //Já que estamos recebendo CPF ...
        conta.setTipoConta(TipoConta.PF);
        conta.setSaldo(new BigDecimal("0"));//Conta zerada
        
        //Asociação pelo request
        conta.setAgencia(accountRequest.getAgencia());
        
        //Lógica de última hora,
        // Número aleatorio 0 á 678 + os três últimos número do CPF.
        conta.setNumero(random.nextInt(678) + Integer.parseInt(StringUtils.right(usuario.getCpf(), 3)))
        
        contaRepository.save(conta);
        //Sucesso.
        //Retorno 200 ou poderia retorna o 201 + id no Path.        
    }
}
