package com.integrador.campus.controller;

import com.integrador.campus.dao.CalificacionDao;
import com.integrador.campus.model.Calificacion;

import java.util.List;

public class CalificacionController {

    private final CalificacionDao dao = new CalificacionDao();

    public boolean registrarCalificacion(Calificacion c) {
        return dao.registrarCalificacion(c);
    }

    public boolean actualizarCalificacion(Calificacion c) {
        return dao.actualizarCalificacion(c);
    }

    public List<Calificacion> listarCalificaciones(int idEstudiante, int anioLectivo) {
        return dao.listarCalificaciones(idEstudiante, anioLectivo);
    }

    public List<Calificacion> listarCalificacionesPorMateria(int idMateria, int idCurso, int anioLectivo) {
        return dao.listarCalificacionesPorMateria(idMateria, idCurso, anioLectivo);
    }

    public boolean eliminarCalificacion(int id) {
        return dao.eliminarCalificacion(id);
    }
}