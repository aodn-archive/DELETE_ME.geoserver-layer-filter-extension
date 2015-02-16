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

import java.util.ArrayList; 
import java.util.List; 

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


    public List<String> read(DataStoreInfo dataStoreInfo, LayerInfo layerInfo, String propertyName )
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

        // order to underlying comparator
        result = new TreeSet( result );   

        // all elts are guaranteed to be the same type
        Class clazz = result.iterator().next().getClass();

        // list stuff should probably be done near the document formatter, since it's an output type.

        List<String> result2 = new ArrayList<String>();

        if (clazz == Integer.class) {
            for(Object value : result) {
                result2.add(Integer.toString((Integer)value)); 
            }
        } 
        else if (clazz == Long.class) {
            for(Object value : result) {
                result2.add(Long.toString((Long)value)); 
            }
        } 
        else if (clazz == String.class) {
            for(Object value : result) {
                result2.add((String)value); 
            }
        }
        else {
           throw new RuntimeException("Unrecognized values type" );  
        }


        return result2;
    }
}
