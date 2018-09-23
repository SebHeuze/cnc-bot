package org.cnc.cncbot.stats.dao;


import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.cnc.cncbot.stats.entities.StatsAlliance;
import org.cnc.cncbot.stats.entities.StatsList;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

/**
 * DAO Class for StatsList
 * @author heuze
 *
 */
@Slf4j
@Repository
public class StatsProcessingDAO {


	/**
	 * Id alliance to replace in query.
	 */
	private final String VAR_ID_ALLIANCE = ":id_alliance";

	@Autowired
	EntityManager em;

	/**
	 * 
	 * @param query
	 * @return
	 */
	public JsonArray excecuteStat(StatsList stat, StatsAlliance alliance) {

		String sqlQuery = alliance != null ? stat.getRequest().replace(VAR_ID_ALLIANCE, String.valueOf(alliance.getId())) : stat.getRequest();
		
		Query query =  em.createNativeQuery(sqlQuery);
		NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
		nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String,Object>> results = query.getResultList();

		final List<JsonObject> jsonList = new ArrayList<>();
		results.forEach(result -> 
		{
			try {
				this.mapRow(result);
			} catch (SQLException se) {
				log.error("Error during excecuteGlobalStat stat " + stat.getName(), se);
			}
		}
				);

		JsonArray jsonArray = new JsonArray();

		for (JsonObject jsonObject : jsonList){
			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}


	/**
	 * Convert Java Object to JsonObject
	 * @param rs
	 * @param rowNum
	 * @return
	 * @throws SQLException
	 */
	public JsonObject mapRow(Map<String,Object> result) throws SQLException {
		JsonObject objectNode =  new JsonObject();
		for(Entry<String,Object> entry : result.entrySet()){
			String column = entry.getKey();
			Object value = entry.getValue();
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
