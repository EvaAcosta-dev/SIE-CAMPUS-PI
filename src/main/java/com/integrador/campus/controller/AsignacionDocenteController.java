package com.integrador.campus.controller;

import com.integrador.campus.dao.AsignacionDocenteDao;
import com.integrador.campus.model.AsignacionDocente;

import java.util.List;

public class AsignacionDocenteController {

    private final AsignacionDocenteDao dao = new AsignacionDocenteDao();

    public boolean registrarAsignacion(AsignacionDocente a) {
        return dao.registrarAsignacion(a);
    }

    public boolean actualizarAsignacion(AsignacionDocente a) {
        return dao.actualizarAsignacion(a);
    }

    public List<AsignacionDocente> listarAsignaciones(int idDocente, int anioLectivo) {
        return dao.listarAsignaciones(idDocente, anioLectivo);
    }

    public boolean eliminarAsignacion(int id) {
        return dao.eliminarAsignacion(id);
    }
}