package com.example.adil.checkup.models;

/**
 * Created by adil on 5/13/17.
 */
public class meds {

    int id;
    String description;
    String dosage;
    String whatfor;
    String created_at;
    String name;

    // constructors
    public meds() {
    }

    public meds(String name,String description, String dosage,String whatfor) {
        this.description = description;
        this.dosage = dosage;
        this.whatfor=whatfor;
    }

    public meds(int id,String name, String description, String dosage,String whatfor) {
        this.id = id;
        this.name=name;
        this.description =description;
        this.dosage= dosage;
        this.whatfor=whatfor;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setWhatfor(String whatfor)
    {
        this.whatfor=whatfor;
    }
    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    // getters
    public long getId() {
        return this.id;
    }
   public String getName() {return this.name; }
    public String getDescription() {
        return this.description;
    }

    public String getDosage() {
        return this.dosage;
    }
    public String getWhatfor()
    {
        return  this.whatfor;
    }
}