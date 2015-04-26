package com.fiuba.campus2015.dto.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gonzalovelasco on 12/4/15.
 */
public class Job {

    public List<Company> companies;

    public Job(){
        this.companies = new ArrayList<Company>();
    }

    public void addCompany(String place, String initdate, String enddate){
        this.companies.add(new Company(place,initdate,enddate));
    }

    public class Company {
        public String place;
        public String initdate;
        public String enddate;

        public Company(String place, String initdate, String enddate){

            this.place = place;
            this.initdate = initdate;
            this.enddate = enddate;
        }
    }
}
