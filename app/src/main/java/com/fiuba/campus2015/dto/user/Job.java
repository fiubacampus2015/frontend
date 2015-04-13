package com.fiuba.campus2015.dto.user;

import java.util.Date;
import java.util.List;

/**
 * Created by gonzalovelasco on 12/4/15.
 */
public class Job {

    public List<Company> companies;

    public class Company {
        public String place;
        public Date initdate;
        public Date enddate;
    }
}
