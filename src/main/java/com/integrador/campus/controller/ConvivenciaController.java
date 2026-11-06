package com.integrador.campus.controller;

import com.integrador.campus.dao.ConvivenciaDao;
import com.integrador.campus.model.Convivencia;

import java.util.List;

public class ConvivenciaController {

    private final ConvivenciaDao dao = new ConvivenciaDao();

    public boolean registrarConvivencia(Convivencia c) {
        return dao.registrarConvivencia(c);
    }

    public boolean actualizarConvivencia(Convivencia c) {
        return dao.actualizarConvivencia(c);
    }

    public List<Convivencia> listarConvivencias(int idEstudiante) {
        return dao.listarConvivencias(idEstudiante);
    }

    public boolean eliminarConvivencia(int id) {
        return dao.eliminarConvivencia(id);
    }
}