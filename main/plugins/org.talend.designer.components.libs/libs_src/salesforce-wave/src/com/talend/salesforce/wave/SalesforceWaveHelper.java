/*
 * @author wluo@talend.com
 */
package com.talend.salesforce.wave;

import java.util.List;

public class SalesforceWaveHelper{
    public static String generateJsonMetadata(java.io.File jsonFile,List<MetadataColumnRuntime> imatadataColumnList,java.util.Map<String,Object> customedConfig){
      	String jsonMetadataStr = null;
        StringBuilder strBuilder = new StringBuilder("{");//The left brace
            strBuilder.append("\"fileFormat\":{");
            strBuilder.append("\"charsetName\":");                       strBuilder.append("\"").append(customedConfig.get("charsetName")).append("\"");
            strBuilder.append(",\"fieldsDelimitedBy\":");                strBuilder.append("\"").append(customedConfig.get("fieldsDelimitedBy")).append("\"");
            strBuilder.append(",\"fieldsEnclosedBy\":");                 strBuilder.append("\"").append(customedConfig.get("fieldsEnclosedBy")).append("\"");
            strBuilder.append(",\"linesTerminatedBy\":");                strBuilder.append("\"").append(customedConfig.get("linesTerminatedBy")).append("\"");

            strBuilder.append(customedConfig.get("numberOfLinesToIgnore"));
            strBuilder.append("},");//File Format Section done.

            strBuilder.append("\"objects\":[{");
            strBuilder.append("\"name\":");                               strBuilder.append("\"").append(customedConfig.get("UniqueApiName")).append("\"");
            strBuilder.append(",\"fullyQualifiedName\":");                strBuilder.append("\"").append(customedConfig.get("fullyQualifiedName")).append("\"");
            strBuilder.append(",\"connector\":");                         strBuilder.append("\"").append("Talend_tSalesforceWaveBulkExec").append("\"");
            strBuilder.append(",\"description\":");                       strBuilder.append("\"").append("").append("\"");
            strBuilder.append(",\"label\":");                             strBuilder.append("\"").append(customedConfig.get("label")).append("\"");
            //Objects Section done
            /*
            ** The Mapping between Talend Schema and Salesforce Wave JSON Schema Fields Section
            */
            strBuilder.append(",\"fields\":[");
            int counter = 0;
            for(MetadataColumnRuntime imetadataColumn : imatadataColumnList){
               String defaultValue = imetadataColumn.getDefault();
               if(counter != 0){
                 strBuilder.append(",");
               }
                strBuilder.append("{");
                strBuilder.append("\"name\":");                           strBuilder.append("\"").append(imetadataColumn.getLabel()).append("\"");
                strBuilder.append(",\"fullyQualifiedName\":");            strBuilder.append("\"").append(customedConfig.get("UniqueApiName").replace("\"",""));
                                                                          strBuilder.append(".");
                                                                          strBuilder.append(imetadataColumn.getLabel()).append("\"");
                String description = imetadataColumn.getComment();
                if(description != null && !"".equals(description)){
                strBuilder.append(",\"description\":");                   strBuilder.append("\"").append(description).append("\"");
                }
                strBuilder.append(",\"label\":");                         strBuilder.append("\"").append(imetadataColumn.getLabel()).append("\"");
                strBuilder.append(",\"isUniqueId\":");                    strBuilder.append(imetadataColumn.isPrimaryKey());

                String salesforceWaveType = SalesforceWaveHelper.convertTalendType2SalesforceWaveType(imetadataColumn);
                strBuilder.append(",\"type\":");                          strBuilder.append("\"").append(salesforceWaveType).append("\"");
            if("Numeric".equalsIgnoreCase(salesforceWaveType)){
                strBuilder.append(",\"precision\":");                     strBuilder.append("\"").append(imetadataColumn.getLength()).append("\"");
                strBuilder.append(",\"scale\":");                         strBuilder.append("\"").append(imetadataColumn.getPrecision()).append("\"");
                    if(defaultValue != null && !"".equals(defaultValue)){
                strBuilder.append(",\"defaultValue\":");                  strBuilder.append("\"").append(defaultValue).append("\"");
                    }
              }

            if("Date".equalsIgnoreCase(salesforceWaveType)){
               strBuilder.append(",\"format\":");                         strBuilder.append("\"").append(imetadataColumn.getPattern()).append("\"");
            }

            if("Numeric".equalsIgnoreCase(salesforceWaveType)){
                strBuilder.append(",\"format\":");                        strBuilder.append("\"").append(customedConfig.get("numericFormat")).append("\"");
            }

            //Use the "default" column as fiscalMonthOffset
            if("Date".equalsIgnoreCase(salesforceWaveType)){
                strBuilder.append(",\"fiscalMonthOffset\":");            strBuilder.append(0);
            }

             strBuilder.append("}");
             counter ++;
          }

             strBuilder.append("]");//match with "fields" : [
             strBuilder.append("}]");//math with "objects" : [{
             strBuilder.append("}");//The right brace
             
             jsonMetadataStr = strBuilder.toString();

             if(jsonFile != null){
                 java.io.BufferedWriter buffWriter = null;
                 try{
                    buffWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(jsonFile),"UTF-8"));
                    buffWriter.write(jsonMetadataStr);
                 }catch(java.io.FileNotFoundException e){
                    e.printStackTrace();
                 }catch(java.io.IOException e){
                    e.printStackTrace();
                 }catch(java.lang.Exception e){
                    e.printStackTrace();
                 }finally{
                    if(buffWriter != null){
                        try{
                            buffWriter.close();
                        }catch(java.io.IOException e){
                            e.printStackTrace();
                        }
                    }
                 }
             }
             return jsonMetadataStr;
        }

        public static String convertTalendType2SalesforceWaveType(MetadataColumnRuntime imetadataColumn){
            String talendType = imetadataColumn.getTalendType();
            java.util.List<String> numericList = java.util.Arrays.asList("id_Byte","id_Short","id_Integer","id_Long","id_Double","id_Float","id_BigDecimal");

            if("id_Date".equals(talendType)){
                return "Date";
            }else if(numericList.contains(talendType)){
                return "Numeric";
            }else{
                return "Text";
            }
        }
    }
