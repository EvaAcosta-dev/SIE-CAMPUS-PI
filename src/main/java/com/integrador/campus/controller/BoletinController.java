package com.integrador.campus.controller;

import com.integrador.campus.dao.BoletinDao;
import com.integrador.campus.model.Boletin;

import java.util.List;

public class BoletinController {

    private final BoletinDao dao = new BoletinDao();

    public boolean generarBoletin(Boletin b) {
        return dao.generarBoletin(b);
    }

    public List<Boletin> listarBoletines(int idEstudiante) {
        return dao.listarBoletines(idEstudiante);
    }

    public boolean actualizarEstadoBoletin(int idBoletin, String estado) {
        return dao.actualizarEstadoBoletin(idBoletin, estado);
    }

    public boolean eliminarBoletin(int id) {
        return dao.eliminarBoletin(id);
    }
}