/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/ade/sitemap/shared/rpc/Attic/I_CmsSitemapService.java,v $
 * Date   : $Date: 2010/06/08 07:12:45 $
 * Version: $Revision: 1.15 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (C) 2002 - 2009 Alkacon Software (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.ade.sitemap.shared.rpc;

import org.opencms.ade.sitemap.shared.CmsClientSitemapEntry;
import org.opencms.ade.sitemap.shared.CmsSitemapData;
import org.opencms.ade.sitemap.shared.CmsSubSitemapInfo;
import org.opencms.gwt.CmsRpcException;
import org.opencms.xml.sitemap.I_CmsSitemapChange;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.SynchronizedRpcRequest;

/**
 * Handles all RPC services related to the sitemap.<p>
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.15 $ 
 * 
 * @since 8.0.0
 * 
 * @see org.opencms.ade.sitemap.CmsSitemapService
 * @see org.opencms.ade.sitemap.shared.rpc.I_CmsSitemapService
 * @see org.opencms.ade.sitemap.shared.rpc.I_CmsSitemapServiceAsync
 */
@RemoteServiceRelativePath("org.opencms.ade.sitemap.CmsSitemapService.gwt")
public interface I_CmsSitemapService extends RemoteService {

    /**
     * Creates a new sub-sitemap resource from the given sitemap and path.<p>
     * 
     * @param sitemapUri the super sitemap URI
     * @param path the starting path
     * 
     * @return the new created sub-sitemap URI
     * 
     * @throws CmsRpcException if something goes wrong 
     */
    CmsSubSitemapInfo createSubsitemap(String sitemapUri, String path) throws CmsRpcException;

    /**
     * Executed when leaving the page.<p>
     * 
     * @param recentList the modified recent list, or <code>null</code> if it has not been modified
     * 
     * @throws CmsRpcException if something goes wrong 
     */
    void exit(List<CmsClientSitemapEntry> recentList) throws CmsRpcException;

    /**
     * Returns the sitemap children for the given path.<p>
     * 
     * @param sitemapUri the URI of the sitemap 
     * @param root the site relative root
     *  
     * @return the sitemap children
     * 
     * @throws CmsRpcException if something goes wrong 
     */
    List<CmsClientSitemapEntry> getChildren(String sitemapUri, String root) throws CmsRpcException;

    /**
     * Returns the sitemap entry for the given path.<p>
     * 
     * @param sitemapUri the URI of the sitemap 
     * @param root the site relative root
     *  
     * @return the sitemap entry
     * 
     * @throws CmsRpcException if something goes wrong 
     */
    CmsClientSitemapEntry getEntry(String sitemapUri, String root) throws CmsRpcException;

    /**
     * Merges the given super sitemap with the sub-sitemap at the given path.<p>
     * 
     * @param sitemapUri the super sitemap URI
     * @param path the starting path
     * 
     * @throws CmsRpcException if something goes wrong 
     */
    void mergeSubsitemap(String sitemapUri, String path) throws CmsRpcException;

    /**
     * Returns the initialization data for the given sitemap.<p>
     * 
     * @param sitemapUri the site relative path
     *  
     * @return the initialization data
     * 
     * @throws CmsRpcException if something goes wrong 
     */
    CmsSitemapData prefetch(String sitemapUri) throws CmsRpcException;

    /**
     * Saves the changes to the given sitemap.<p>
     * 
     * @param sitemapUri the sitemap URI 
     * @param changes the changes to save
     * @param unlockAfterSave if true, unlocks the sitemap after saving it 
     * 
     * @return the new timestamp
     * 
     * @throws CmsRpcException if something goes wrong 
     */
    long save(String sitemapUri, List<I_CmsSitemapChange> changes, boolean unlockAfterSave) throws CmsRpcException;

    /**
     * Saves the changes to the given sitemap.<p>
     * 
     * @param sitemapUri the sitemap URI 
     * @param changes the changes to save
     * 
     * @return the new timestamp
     * 
     * @throws CmsRpcException if something goes wrong 
     */
    @SynchronizedRpcRequest
    long saveSync(String sitemapUri, List<I_CmsSitemapChange> changes) throws CmsRpcException;
}
