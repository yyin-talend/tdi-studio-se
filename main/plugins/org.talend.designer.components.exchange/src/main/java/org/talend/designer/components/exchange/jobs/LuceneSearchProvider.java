// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.exchange.jobs;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * DOC hcyi class global comment. Detailled comment
 */
public class LuceneSearchProvider<T> implements ISearchProvider<T> {

    private static final String KEY_FIELD = "key"; //$NON-NLS-1$

    private static final String FIELD_PREFIX = "field"; //$NON-NLS-1$

    private static final Version LUCENE_VER = Version.LUCENE_30;

    // Construct a RAMDirectory to hold the in-memory representation
    // of the index.
    private RAMDirectory fIndex;

    private IndexWriterConfig fWriterConfig;

    private IndexWriter fWriter;

    private Map<String, T> fRecords;

    private int fFieldNum = 0;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.components.exchange.jobs.ISearchProvider#init()
     */
    @Override
    public void init() throws IOException {
        try {
            if (fIndex != null) {
                fIndex.close();
            }
        } catch (RuntimeException e) {

        }
        fRecords = new HashMap<String, T>();

        fIndex = new RAMDirectory();
        // FIXME version is 3.5??

        fWriterConfig = new IndexWriterConfig(LUCENE_VER, new StandardAnalyzer(LUCENE_VER));
        // Make an writer to create the index
        fWriter = new IndexWriter(fIndex, fWriterConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.components.exchange.jobs.ISearchProvider#addRecord(T, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void addRecord(T bean, String key, String... fields) throws IOException {
        Document doc = new Document();

        if (fields.length > fFieldNum) {
            fFieldNum = fields.length;
        }

        for (int i = 0; i < fields.length; i++) {
            doc.add(new Field(FIELD_PREFIX + i, new StringReader(fields[i])));
        }
        doc.add(new Field(KEY_FIELD, key, Field.Store.YES, Field.Index.NO));
        fRecords.put(key, bean);
        // add record to index
        if (fWriter == null) { // FIXME why need?
            // fWriter = new IndexWriter(fIndex, new StandardAnalyzer(), true);
        }
        fWriter.addDocument(doc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.components.exchange.jobs.ISearchProvider#search(java.lang.String)
     */
    @Override
    public List<T> search(String queryString) throws ParseException, IOException {
        if (fWriter != null) {
            // Optimize and close the writer to finish building the index
            fWriter.optimize();
            fWriter.close();
        }
        // Build an IndexSearcher using the in-memory index
        IndexReader reader = IndexReader.open(fIndex, true);
        IndexSearcher searcher = new IndexSearcher(reader); // FIXME, closeReader=true, before.

        Set<String> keys = new HashSet<String>();

        try {

            for (int f = 0; f < fFieldNum; f++) {
                String field = FIELD_PREFIX + f;

                // Build a Query object
                Query query = new QueryParser(LUCENE_VER, field, new StandardAnalyzer(LUCENE_VER)).parse(queryString);

                // Search for the query
                TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE); // means search all?
                for (ScoreDoc sDoc : topDocs.scoreDocs) {
                    Document doc = searcher.doc(sDoc.doc);
                    String value = doc.get(KEY_FIELD);
                    if (value != null) {
                        keys.add(value);
                    }
                }
            }

            List<T> result = new ArrayList<T>();
            for (String key : keys) {
                result.add(fRecords.get(key));
            }
            return result;
        } finally {
            searcher.close();
            reader.close();
        }

    }
}
