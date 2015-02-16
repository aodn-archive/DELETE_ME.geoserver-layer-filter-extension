/*
 * Copyright 2014 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */

package au.org.emii.geoserver.extensions.filters.layer.data.io;

import au.org.emii.geoserver.extensions.filters.layer.data.Filter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.visitor.UniqueVisitor;
import org.opengis.feature.Feature;

import java.io.IOException;
import java.util.*;

import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.type.FeatureType;
import java.lang.reflect.Method;
import org.geotools.data.simple.SimpleFeatureSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Type;

public class PossibleValuesReader2 {

	private static final String TYPE_NAME_PREFIX = "class ";
 
	public static String getClassName(Type type) {
		if (type==null) {
			return "";
		}
		String className = type.toString();
		if (className.startsWith(TYPE_NAME_PREFIX)) {
			className = className.substring(TYPE_NAME_PREFIX.length());
		}
		return className;
	}

    public Set<String> read(DataStoreInfo dataStoreInfo, LayerInfo layerInfo, String propertyName )
        throws IOException, NoSuchMethodException, SQLException, IllegalAccessException, InvocationTargetException
  {

        JDBCDataStore store = (JDBCDataStore)dataStoreInfo.getDataStore(null);

        String layerName = layerInfo.getName();

        Query query = new Query( null, null, new String[] { } );

        UniqueVisitor visitor = new UniqueVisitor( propertyName);

        Connection conn = store.getDataSource().getConnection();

        SimpleFeatureSource source = store.getFeatureSource( layerName );

        FeatureType schema = source.getSchema();

        Method storeGetAggregateValueMethod = store.getClass().getDeclaredMethod("getAggregateValue",
            org.opengis.feature.FeatureVisitor.class,
            org.opengis.feature.simple.SimpleFeatureType.class,
            org.geotools.data.Query.class,
            java.sql.Connection.class
        );

        storeGetAggregateValueMethod.setAccessible(true);

        storeGetAggregateValueMethod.invoke(store, visitor, schema, query, conn );

        Set result = visitor.getUnique();

        // we need to map to string form here, which means that we need the actual type ...
        for( Object item : result ) 
        {
			// String name1 = item.class.name ; 
			//String name2 = item.getClass().getSimpleName(); 

			String name2 = item.getClass().toString(); 
	
			//*/String x = String.class; 


			Object value = item; 
		
			if (value.getClass() == Integer.class) {
				System.out.println("This is an Integer");
			} 
			else if (value.getClass() == String.class) {
				System.out.println("This is a String");
			}
			else if (value.getClass() == Float.class) {
				System.out.println("This is a Float");
			}	

/*
			if (item instanceof String.class) {
				System.out.println( "it's a string " );
			}
			if (item instanceof Long.class) {
				System.out.println( "it's a long" );
			}
*/



			/* 
			//String name2 = item.getClass().getName(); 
			no string switch support...
			switch( name ){
				 case "A":	
					System.out.println( "hi" );
			};
			// CLAZZ z = CLAZZ.valueOf( item.getClass().getSimpleName());
			*/

			if (name2.equals(String.class)) {
				System.out.println( "hi" );
			} 
			if (name2.equals(Long.class)) {
				System.out.println( "hi" );
			} 



        } 

        Set<String> result2 = new TreeSet<String>( result );

        return result2;
    }
}
