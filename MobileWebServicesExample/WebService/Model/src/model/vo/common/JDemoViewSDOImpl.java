package model.vo.common;

import org.eclipse.persistence.sdo.SDODataObject;

public class JDemoViewSDOImpl extends SDODataObject implements JDemoViewSDO {

   public static final int START_PROPERTY_INDEX = 0;

   public static final int END_PROPERTY_INDEX = START_PROPERTY_INDEX + 1;

   public JDemoViewSDOImpl() {}

   public java.math.BigDecimal getJdemoId() {
      return getBigDecimal(START_PROPERTY_INDEX + 0);
   }

   public void setJdemoId(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 0 , value);
   }

   public java.lang.String getJdemoName() {
      return getString(START_PROPERTY_INDEX + 1);
   }

   public void setJdemoName(java.lang.String value) {
      set(START_PROPERTY_INDEX + 1 , value);
   }


}

