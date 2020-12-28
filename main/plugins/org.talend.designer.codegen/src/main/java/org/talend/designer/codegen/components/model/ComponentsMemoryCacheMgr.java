// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.codegen.components.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.component_cache.ComponentInfo;
import org.talend.core.model.component_cache.ComponentsCache;
import org.talend.core.model.components.ComponentManager;

/*
 * Created by bhe on Dec 28, 2020
 */
public class ComponentsMemoryCacheMgr {

    private static Logger LOGGER = Logger.getLogger(ComponentsMemoryCacheMgr.class);

    private static final ComponentsMemoryCacheMgr INSTANCE = new ComponentsMemoryCacheMgr();

    private Map<String, List<ComponentInfo>> cacheMap = new HashMap<String, List<ComponentInfo>>();

    private ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();

    private ComponentsMemoryCacheMgr() {
    }

    public static ComponentsMemoryCacheMgr getInstance() {
        return INSTANCE;
    }

    public void add(String key, ComponentInfo info) {
        cacheLock.writeLock().lock();
        try {
            List<ComponentInfo> infos = cacheMap.get(key);
            if (infos == null) {
                infos = new ArrayList<ComponentInfo>();
                cacheMap.put(key, infos);
            }

            infos.add(info);

        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    public void addEntry(String key, List<ComponentInfo> infos) {
        cacheLock.writeLock().lock();
        try {
            List<ComponentInfo> cis = cacheMap.get(key);
            if (cis == null) {
                cis = new ArrayList<ComponentInfo>();
                cacheMap.put(key, cis);
            }

            cis.addAll(infos);

        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    public Map<String, List<ComponentInfo>> getCachedMap() {
        cacheLock.readLock().lock();
        try {
            return Collections.unmodifiableMap(cacheMap);
        } finally {
            cacheLock.readLock().unlock();
        }
    }

    public boolean containsKey(String key) {
        cacheLock.readLock().lock();
        try {
            return cacheMap.containsKey(key);
        } finally {
            cacheLock.readLock().unlock();
        }
    }

    public List<ComponentInfo> get(String key) {
        cacheLock.readLock().lock();
        try {
            return cacheMap.get(key);
        } finally {
            cacheLock.readLock().unlock();
        }
    }

    public void persist() {
        Map<String, List<ComponentInfo>> cachedData = this.getCachedMap();
        cachedData.forEach((k, v) -> {
            BasicEList<ComponentInfo> list = new BasicEList<ComponentInfo>(v);
            ComponentManager.getComponentCache().getComponentEntryMap().put(k, list);
        });
        ComponentManager.setModified(true);
        ComponentManager.saveResource();
    }

    public void clear() {
        cacheLock.writeLock().lock();
        try {
            this.cacheMap.clear();
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    public void loadFromCacheFile(String installLocation) {
        ComponentsCache cacheFromFile = null;
        try {
            cacheFromFile = ComponentManager.loadComponentCacheFile(installLocation);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

        cacheLock.writeLock().lock();
        try {
            Set<Entry<String, EList<ComponentInfo>>> entries = cacheFromFile.getComponentEntryMap()
                    .entrySet();
            for (Entry<String, EList<ComponentInfo>> entry : entries) {
                List<ComponentInfo> list = new ArrayList<ComponentInfo>(entry.getValue());
                this.cacheMap.put(entry.getKey(), list);
            }

        } finally {
            cacheLock.writeLock().unlock();
        }
    }
}
