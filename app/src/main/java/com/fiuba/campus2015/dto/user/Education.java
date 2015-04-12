package com.fiuba.campus2015.dto.user;

import java.util.Date;
import java.util.List;

/**
 * Created by gonzalovelasco on 12/4/15.
 */
public class Education {

    public List<Carrera> carreras;

    public class Carrera {
        public String nombre;
        public String orientacion;
        public Date inicio;


    }
}
