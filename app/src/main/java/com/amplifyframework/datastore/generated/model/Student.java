package com.amplifyframework.datastore.generated.model;

import androidx.core.util.ObjectsCompat;

public class Student {
    public String id;
    public String email;
    public String given_name;
    public String family_name;

    public Student(String id, String given_name, String family_name, String email) {
        this.id = id;
        this.given_name = given_name;
        this.family_name = family_name;
        this.email = email;
    }

    public Student() {
        this.id = null;
        this.given_name = null;
        this.family_name = null;
        this.email = null;
    }
    public String getId() {
        return id;
    }

    public String getGivenName() {
        return given_name;
    }

    public String getFamilyName() {
        return family_name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGivenName(String GivenName) {
        this.given_name = GivenName;
    }

    public void setFamilyName(String FamilyName) {
        this.family_name = FamilyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if(obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            Student student = (Student) obj;
            return ObjectsCompat.equals(getId(), student.getId()) &&
                    ObjectsCompat.equals(getGivenName(), student.getGivenName()) &&
                    ObjectsCompat.equals(getFamilyName(), student.getFamilyName()) &&
                    ObjectsCompat.equals(getEmail(), student.getEmail());
        }
    }

    @Override
    public int hashCode() {
        return new StringBuilder()
                .append(getId())
                .append(getGivenName())
                .append(getFamilyName())
                .append(getEmail())
                .toString()
                .hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Student {")
                .append("id=" + String.valueOf(getId()) + ", ")
                .append("given_name=" + String.valueOf(getGivenName()) + ", ")
                .append("family_name=" + String.valueOf(getFamilyName()) + ", ")
                .append("email=" + String.valueOf(getEmail()))
                .append("}")
                .toString();
    }


}
