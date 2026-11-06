package com.integrador.campus.controller;

import com.integrador.campus.dao.MatriculaDao;
import com.integrador.campus.model.Matricula;

import java.util.List;

public class MatriculaController {

    private final MatriculaDao dao = new MatriculaDao();

    public boolean registrarMatricula(Matricula m) {
        return dao.registrarMatricula(m);
    }

    public boolean actualizarMatricula(Matricula m) {
        return dao.actualizarMatricula(m);
    }

    public List<Matricula> listarMatriculas() {
        return dao.listarMatriculas();
    }

    public List<Matricula> buscarMatricula(String valor) {
        return dao.buscarMatricula(valor);
    }

    public boolean eliminarMatricula(int id) {
        return dao.eliminarMatricula(id);
    }
}