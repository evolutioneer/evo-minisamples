package model.vo.common;

import org.eclipse.persistence.sdo.SDODataObject;

public class CandyViewSDOImpl extends SDODataObject implements CandyViewSDO {

   public static final int START_PROPERTY_INDEX = 0;

   public static final int END_PROPERTY_INDEX = START_PROPERTY_INDEX + 3;

   public CandyViewSDOImpl() {}

   public java.math.BigDecimal getCandyId() {
      return getBigDecimal(START_PROPERTY_INDEX + 0);
   }

   public void setCandyId(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 0 , value);
   }

   public java.lang.String getCandyName() {
      return getString(START_PROPERTY_INDEX + 1);
   }

   public void setCandyName(java.lang.String value) {
      set(START_PROPERTY_INDEX + 1 , value);
   }

   public java.math.BigDecimal getCandyQuality() {
      return getBigDecimal(START_PROPERTY_INDEX + 2);
   }

   public void setCandyQuality(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 2 , value);
   }

   public java.math.BigDecimal getCandyChocolate() {
      return getBigDecimal(START_PROPERTY_INDEX + 3);
   }

   public void setCandyChocolate(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 3 , value);
   }


}

