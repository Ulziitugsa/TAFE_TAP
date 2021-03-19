package com.amplifyframework.datastore.generated.model;


import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Student type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Students", authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", operations = { ModelOperation.CREATE, ModelOperation.DELETE, ModelOperation.UPDATE })
})
public final class Student implements Model {
  public static final QueryField ID = field("Student", "id");
  public static final QueryField STUDENT_ID = field("Student", "student_id");
  public static final QueryField GIVEN_NAME = field("Student", "given_name");
  public static final QueryField FAMILY_NAME = field("Student", "family_name");
  public static final QueryField EMAIL = field("Student", "email");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String student_id;
  private final @ModelField(targetType="String", isRequired = true) String given_name;
  private final @ModelField(targetType="String") String family_name;
  private final @ModelField(targetType="String") String email;
  public String getId() {
      return id;
  }
  
  public String getStudentId() {
      return student_id;
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
  
  private Student(String id, String student_id, String given_name, String family_name, String email) {
    this.id = id;
    this.student_id = student_id;
    this.given_name = given_name;
    this.family_name = family_name;
    this.email = email;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Student student = (Student) obj;
      return ObjectsCompat.equals(getId(), student.getId()) &&
              ObjectsCompat.equals(getStudentId(), student.getStudentId()) &&
              ObjectsCompat.equals(getGivenName(), student.getGivenName()) &&
              ObjectsCompat.equals(getFamilyName(), student.getFamilyName()) &&
              ObjectsCompat.equals(getEmail(), student.getEmail());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getStudentId())
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
      .append("student_id=" + String.valueOf(getStudentId()) + ", ")
      .append("given_name=" + String.valueOf(getGivenName()) + ", ")
      .append("family_name=" + String.valueOf(getFamilyName()) + ", ")
      .append("email=" + String.valueOf(getEmail()))
      .append("}")
      .toString();
  }
  
  public static StudentIdStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Student justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Student(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      student_id,
      given_name,
      family_name,
      email);
  }
  public interface StudentIdStep {
    GivenNameStep studentId(String studentId);
  }
  

  public interface GivenNameStep {
    BuildStep givenName(String givenName);
  }
  

  public interface BuildStep {
    Student build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep familyName(String familyName);
    BuildStep email(String email);
  }
  

  public static class Builder implements StudentIdStep, GivenNameStep, BuildStep {
    private String id;
    private String student_id;
    private String given_name;
    private String family_name;
    private String email;
    @Override
     public Student build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Student(
          id,
          student_id,
          given_name,
          family_name,
          email);
    }
    
    @Override
     public GivenNameStep studentId(String studentId) {
        Objects.requireNonNull(studentId);
        this.student_id = studentId;
        return this;
    }
    
    @Override
     public BuildStep givenName(String givenName) {
        Objects.requireNonNull(givenName);
        this.given_name = givenName;
        return this;
    }
    
    @Override
     public BuildStep familyName(String familyName) {
        this.family_name = familyName;
        return this;
    }
    
    @Override
     public BuildStep email(String email) {
        this.email = email;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String studentId, String givenName, String familyName, String email) {
      super.id(id);
      super.studentId(studentId)
        .givenName(givenName)
        .familyName(familyName)
        .email(email);
    }
    
    @Override
     public CopyOfBuilder studentId(String studentId) {
      return (CopyOfBuilder) super.studentId(studentId);
    }
    
    @Override
     public CopyOfBuilder givenName(String givenName) {
      return (CopyOfBuilder) super.givenName(givenName);
    }
    
    @Override
     public CopyOfBuilder familyName(String familyName) {
      return (CopyOfBuilder) super.familyName(familyName);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
  }
  
}
