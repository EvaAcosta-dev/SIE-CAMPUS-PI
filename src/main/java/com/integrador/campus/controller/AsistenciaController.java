package com.integrador.campus.controller;

import com.integrador.campus.dao.AsistenciaDao;
import com.integrador.campus.model.Asistencia;

import java.util.List;

public class AsistenciaController {

    private final AsistenciaDao dao = new AsistenciaDao();

    public boolean registrarAsistencia(Asistencia a) {
        return dao.registrarAsistencia(a);
    }

    public boolean actualizarAsistencia(Asistencia a) {
        return dao.actualizarAsistencia(a);
    }

    public List<Asistencia> listarAsistencias(int idEstudiante, int idMateria, int idCurso) {
        return dao.listarAsistencias(idEstudiante, idMateria, idCurso);
    }

    public boolean eliminarAsistencia(int id) {
        return dao.eliminarAsistencia(id);
    }
}