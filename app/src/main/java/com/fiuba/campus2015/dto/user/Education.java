package com.fiuba.campus2015.dto.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gonzalovelasco on 12/4/15.
 */
public class Education {

    public List<Career> careers;

    public Education(){
        this.careers = new ArrayList<Career>();
    }

    public void addCareer(String title, String branch, String initdate){
        this.careers.add(new Career(title,branch,initdate));
    }

    public class Career {
        public String title;
        public String branch;
        public String initdate;

        public Career(String title, String branch, String initdate){

            this.branch = branch;
            this.initdate = initdate;
            this.title = title;
        }

    }
}
