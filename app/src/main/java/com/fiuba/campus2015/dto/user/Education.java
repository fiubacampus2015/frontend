package com.fiuba.campus2015.dto.user;

import java.util.Date;
import java.util.List;

/**
 * Created by gonzalovelasco on 12/4/15.
 */
public class Education {

    public List<Carrer> careers;

    public class Carrer {
        public String title;
        public String branch;
        public Date initdate;


    }
}
