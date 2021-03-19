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

/** This is an auto generated class representing the Lecturer type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Lecturers", authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", operations = { ModelOperation.CREATE, ModelOperation.DELETE, ModelOperation.UPDATE })
})
public final class Lecturer implements Model {
  public static final QueryField ID = field("Lecturer", "id");
  public static final QueryField LECTURER_ID = field("Lecturer", "lecturer_id");
  public static final QueryField GIVEN_NAME = field("Lecturer", "given_name");
  public static final QueryField FAMILY_NAME = field("Lecturer", "family_name");
  public static final QueryField EMAIL = field("Lecturer", "email");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String lecturer_id;
  private final @ModelField(targetType="String", isRequired = true) String given_name;
  private final @ModelField(targetType="String") String family_name;
  private final @ModelField(targetType="String") String email;
  public String getId() {
      return id;
  }
  
  public String getLecturerId() {
      return lecturer_id;
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
  
  private Lecturer(String id, String lecturer_id, String given_name, String family_name, String email) {
    this.id = id;
    this.lecturer_id = lecturer_id;
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
      Lecturer lecturer = (Lecturer) obj;
      return ObjectsCompat.equals(getId(), lecturer.getId()) &&
              ObjectsCompat.equals(getLecturerId(), lecturer.getLecturerId()) &&
              ObjectsCompat.equals(getGivenName(), lecturer.getGivenName()) &&
              ObjectsCompat.equals(getFamilyName(), lecturer.getFamilyName()) &&
              ObjectsCompat.equals(getEmail(), lecturer.getEmail());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getLecturerId())
      .append(getGivenName())
      .append(getFamilyName())
      .append(getEmail())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Lecturer {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("lecturer_id=" + String.valueOf(getLecturerId()) + ", ")
      .append("given_name=" + String.valueOf(getGivenName()) + ", ")
      .append("family_name=" + String.valueOf(getFamilyName()) + ", ")
      .append("email=" + String.valueOf(getEmail()))
      .append("}")
      .toString();
  }
  
  public static LecturerIdStep builder() {
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
  public static Lecturer justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Lecturer(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      lecturer_id,
      given_name,
      family_name,
      email);
  }
  public interface LecturerIdStep {
    GivenNameStep lecturerId(String lecturerId);
  }
  

  public interface GivenNameStep {
    BuildStep givenName(String givenName);
  }
  

  public interface BuildStep {
    Lecturer build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep familyName(String familyName);
    BuildStep email(String email);
  }
  

  public static class Builder implements LecturerIdStep, GivenNameStep, BuildStep {
    private String id;
    private String lecturer_id;
    private String given_name;
    private String family_name;
    private String email;
    @Override
     public Lecturer build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Lecturer(
          id,
          lecturer_id,
          given_name,
          family_name,
          email);
    }
    
    @Override
     public GivenNameStep lecturerId(String lecturerId) {
        Objects.requireNonNull(lecturerId);
        this.lecturer_id = lecturerId;
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
    private CopyOfBuilder(String id, String lecturerId, String givenName, String familyName, String email) {
      super.id(id);
      super.lecturerId(lecturerId)
        .givenName(givenName)
        .familyName(familyName)
        .email(email);
    }
    
    @Override
     public CopyOfBuilder lecturerId(String lecturerId) {
      return (CopyOfBuilder) super.lecturerId(lecturerId);
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
