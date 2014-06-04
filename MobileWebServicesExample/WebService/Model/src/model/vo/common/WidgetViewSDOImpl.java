package model.vo.common;

import org.eclipse.persistence.sdo.SDODataObject;

public class WidgetViewSDOImpl extends SDODataObject implements WidgetViewSDO {

   public static final int START_PROPERTY_INDEX = 0;

   public static final int END_PROPERTY_INDEX = START_PROPERTY_INDEX + 2;

   public WidgetViewSDOImpl() {}

   public java.math.BigDecimal getWidgetId() {
      return getBigDecimal(START_PROPERTY_INDEX + 0);
   }

   public void setWidgetId(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 0 , value);
   }

   public java.lang.String getWidgetName() {
      return getString(START_PROPERTY_INDEX + 1);
   }

   public void setWidgetName(java.lang.String value) {
      set(START_PROPERTY_INDEX + 1 , value);
   }

   public java.lang.String getWidgetColor() {
      return getString(START_PROPERTY_INDEX + 2);
   }

   public void setWidgetColor(java.lang.String value) {
      set(START_PROPERTY_INDEX + 2 , value);
   }


}

