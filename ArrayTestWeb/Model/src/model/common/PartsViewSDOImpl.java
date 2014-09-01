package model.common;

import org.eclipse.persistence.sdo.SDODataObject;

public class PartsViewSDOImpl extends SDODataObject implements PartsViewSDO {

   public static final int START_PROPERTY_INDEX = 0;

   public static final int END_PROPERTY_INDEX = START_PROPERTY_INDEX + 12;

   public PartsViewSDOImpl() {}

   public java.math.BigInteger getPspecId() {
      return getBigInteger(START_PROPERTY_INDEX + 0);
   }

   public void setPspecId(java.math.BigInteger value) {
      set(START_PROPERTY_INDEX + 0 , value);
   }

   public java.lang.String getPart() {
      return getString(START_PROPERTY_INDEX + 1);
   }

   public void setPart(java.lang.String value) {
      set(START_PROPERTY_INDEX + 1 , value);
   }

   public java.lang.String getKeyword() {
      return getString(START_PROPERTY_INDEX + 2);
   }

   public void setKeyword(java.lang.String value) {
      set(START_PROPERTY_INDEX + 2 , value);
   }

   public java.lang.String getDescription() {
      return getString(START_PROPERTY_INDEX + 3);
   }

   public void setDescription(java.lang.String value) {
      set(START_PROPERTY_INDEX + 3 , value);
   }

   public java.lang.String getFsc() {
      return getString(START_PROPERTY_INDEX + 4);
   }

   public void setFsc(java.lang.String value) {
      set(START_PROPERTY_INDEX + 4 , value);
   }

   public java.math.BigDecimal getInventoryCount() {
      return getBigDecimal(START_PROPERTY_INDEX + 5);
   }

   public void setInventoryCount(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 5 , value);
   }

   public java.math.BigDecimal getBacklogCount() {
      return getBigDecimal(START_PROPERTY_INDEX + 6);
   }

   public void setBacklogCount(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 6 , value);
   }

   public java.lang.String getOem() {
      return getString(START_PROPERTY_INDEX + 7);
   }

   public void setOem(java.lang.String value) {
      set(START_PROPERTY_INDEX + 7 , value);
   }

   public java.math.BigDecimal getListPrice() {
      return getBigDecimal(START_PROPERTY_INDEX + 8);
   }

   public void setListPrice(java.math.BigDecimal value) {
      set(START_PROPERTY_INDEX + 8 , value);
   }

   public java.lang.String getListPriceCurrency() {
      return getString(START_PROPERTY_INDEX + 9);
   }

   public void setListPriceCurrency(java.lang.String value) {
      set(START_PROPERTY_INDEX + 9 , value);
   }

   public java.sql.Timestamp getListPriceEffective() {
      return (java.sql.Timestamp)get(START_PROPERTY_INDEX + 10);
   }

   public void setListPriceEffective(java.sql.Timestamp value) {
      set(START_PROPERTY_INDEX + 10 , value);
   }

   public java.lang.String getExportLicenseRequired() {
      return getString(START_PROPERTY_INDEX + 11);
   }

   public void setExportLicenseRequired(java.lang.String value) {
      set(START_PROPERTY_INDEX + 11 , value);
   }

   public java.lang.String getPotentiallyHazardous() {
      return getString(START_PROPERTY_INDEX + 12);
   }

   public void setPotentiallyHazardous(java.lang.String value) {
      set(START_PROPERTY_INDEX + 12 , value);
   }


}

