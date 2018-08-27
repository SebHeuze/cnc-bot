package org.cnc.cncbot.stats.dao.rowmapper;
//convenient Spring JDBC RowMapper for when you want the flexibility of Jackson's TreeModel API
//Note: Jackson can also serialize standard Java Collections (Maps and Lists) to JSON: if you don't need JsonNode,
//it's simpler and more portable to have Spring JDBC simply return a Map or List<Map>.

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.google.gson.JsonObject;

public class JsonNodeRowMapper implements RowMapper<JsonObject> {


 @Override
 public JsonObject mapRow(ResultSet rs, int rowNum) throws SQLException {
   JsonObject objectNode =  new JsonObject();
     ResultSetMetaData rsmd = rs.getMetaData();
     int columnCount = rsmd.getColumnCount();
     for (int index = 1; index <= columnCount; index++) {
         String column = JdbcUtils.lookupColumnName(rsmd, index);
         Object value = rs.getObject(column);
         if (value == null) {
             objectNode.addProperty(column, "");
         } else if (value instanceof Integer) {
             objectNode.addProperty(column, (Integer) value);
         } else if (value instanceof String) {
             objectNode.addProperty(column, (String) value);                
         } else if (value instanceof Boolean) {
             objectNode.addProperty(column, (Boolean) value);           
         } else if (value instanceof Date) {
             objectNode.addProperty(column, ((Date) value).getTime());                
         } else if (value instanceof Long) {
             objectNode.addProperty(column, (Long) value);                
         } else if (value instanceof Double) {
             objectNode.addProperty(column, (Double) value);                
         } else if (value instanceof Float) {
             objectNode.addProperty(column, (Float) value);                
         } else if (value instanceof BigDecimal) {
             objectNode.addProperty(column, (BigDecimal) value);
         } else if (value instanceof Byte) {
             objectNode.addProperty(column, (Byte) value);
         } else if (value instanceof byte[]) {
             objectNode.addProperty(column, new String((byte[]) value));                
         } else {
             throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
         }
     }
     return objectNode;
 }

}