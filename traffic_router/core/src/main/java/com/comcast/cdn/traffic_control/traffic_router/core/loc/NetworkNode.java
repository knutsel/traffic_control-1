/*
 * Copyright 2015 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.comcast.cdn.traffic_control.traffic_router.core.loc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.comcast.cdn.traffic_control.traffic_router.core.util.CidrAddress;
import com.comcast.cdn.traffic_control.traffic_router.geolocation.Geolocation;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.ajax.json.JSONTokener;

import com.comcast.cdn.traffic_control.traffic_router.core.cache.Cache;
import com.comcast.cdn.traffic_control.traffic_router.core.cache.CacheLocation;
import com.comcast.cdn.traffic_control.traffic_router.core.cache.CacheRegister;

public class NetworkNode implements Comparable<NetworkNode> {
    private static final Logger LOGGER = Logger.getLogger(NetworkNode.class);
    private static final String DEFAULT_SUB_STR = "0.0.0.0/0";

    private static NetworkNode instance;
	private static NetworkNode deepInstance;
    private static CacheRegister cacheRegister;

    private CidrAddress cidrAddress;
    private String loc;
    private CacheLocation cacheLocation = null;
    private Geolocation geolocation = null;
    protected Map<NetworkNode,NetworkNode> children;

    public static NetworkNode getInstance() {
        if (instance != null) {
            return instance;
        }

        try {
            instance = new NetworkNode(DEFAULT_SUB_STR);
        } catch (NetworkNodeException e) {
            LOGGER.warn(e);
        }

        return instance;
    }

    public static NetworkNode getDeepInstance() {
        if (deepInstance != null) {
            return deepInstance;
        }

        try {
            deepInstance = new NetworkNode(DEFAULT_SUB_STR);
        } catch (NetworkNodeException e) {
            LOGGER.warn(e);
        }

        return deepInstance;
    }

	public static void setCacheRegister(final CacheRegister cr) {
	        cacheRegister = cr;
	}

    public static NetworkNode generateTree(final File f, final boolean verifyOnly, final boolean useDeep) throws NetworkNodeException, FileNotFoundException, JSONException  {
			LOGGER.info("DDC: Starting generateTree1, useDeep: " + useDeep);
        return generateTree(new JSONObject(new JSONTokener(new FileReader(f))), verifyOnly, useDeep);
    }

    public static NetworkNode generateTree(final File f, final boolean verifyOnly) throws NetworkNodeException, FileNotFoundException, JSONException  {
			LOGGER.info("DDC: Starting generateTree2, useDeep: " + false);
        return generateTree(new JSONObject(new JSONTokener(new FileReader(f))), verifyOnly, false);
    }

    public static NetworkNode generateTree(final JSONObject json, final boolean verifyOnly) {
			LOGGER.info("DDC: Starting generateTree3, useDeep: " + false);
        return generateTree(json, verifyOnly, false);
	}

	// both PMD.CyclomaticComplexity PMD.NPathComplexit will be flagged :( TODO JvD
    @SuppressWarnings("PMD")
    public static NetworkNode generateTree(final JSONObject json, final boolean verifyOnly, final boolean useDeep) {
        try {
			LOGGER.info("DDC: Starting generateTree, useDeep: " + useDeep);
            final JSONObject coverageZones = json.getJSONObject("coverageZones");

            final SuperNode root = new SuperNode();

            for (final String loc : JSONObject.getNames(coverageZones)) {
				LOGGER.info("LOOP loc");
                final JSONObject locData = coverageZones.getJSONObject(loc);
                final JSONObject coordinates = locData.optJSONObject("coordinates");
                Geolocation geolocation = null;

                if (coordinates != null && coordinates.has("latitude") && coordinates.has("longitude")) {
                    final double latitude = coordinates.optDouble("latitude");
                    final double longitude = coordinates.optDouble("longitude");
                    geolocation = new Geolocation(latitude, longitude);
                }

                try {
                    final JSONArray network6 = locData.getJSONArray("network6");

                    for (int i = 0; i < network6.length(); i++) {
						LOGGER.info("LOOP n6");
                        final String ip = network6.getString(i);

                        try {
                            root.add6(new NetworkNode(ip, loc, geolocation));
                        } catch (NetworkNodeException ex) {
                            LOGGER.error(ex, ex);
                            return null;
                        }
                    }
                } catch (JSONException ex) {
                    LOGGER.warn("An exception was caught while accessing the network6 key of " + loc + " in the incoming coverage zone file: " + ex.getMessage());
                }

                try {
                    final JSONArray network = locData.getJSONArray("network");

                    for (int i = 0; i < network.length(); i++) {
						LOGGER.info("loop n");
                        final String ip = network.getString(i);

                        try {
                            root.add(new NetworkNode(ip, loc, geolocation));
                        } catch (NetworkNodeException ex) {
                            LOGGER.error(ex, ex);
                            return null;
                        }
                    }
                } catch (JSONException ex) {
                    LOGGER.warn("An exception was caught while accessing the network key of " + loc + " in the incoming coverage zone file: " + ex.getMessage());
                }
				if (useDeep) {
				    try {
					    final JSONArray caches = locData.getJSONArray("caches");
					    CacheLocation deepLoc = null;
					    for (int i = 0; i < caches.length(); i++) {
							LOGGER.info("loop caches");
						    if (deepLoc == null) {
							    deepLoc = new CacheLocation( "deep." + caches.getString(i), new Geolocation(0.0, 0.0));  // TODO JvD 
						    }
							if (deepLoc == null ) {
								LOGGER.info("deepLoc == null");
							} else {
								LOGGER.info("deepLoc = cool");
							}
						    // get the cache from the cacheregister here.
							if (cacheRegister == null) {
								LOGGER.info("cacheRegister is cool");
							} else {
								LOGGER.info("cacheRegister is not cool");
							}
							LOGGER.info("GETTING " + caches.getString(i));
						    final Cache cache = cacheRegister.getCacheMap().get(caches.getString(i));
							if (cache == null) {
								LOGGER.info("cache is cool");
							} else {
								LOGGER.info("Cache is not cool");
							}
						    LOGGER.info("DDC: Adding " + caches.getString(i) + " to " + deepLoc.getId() + ".");
						    deepLoc.addCache(cache);
					    }
                    } catch (JSONException ex) {
                        LOGGER.warn("An exception was caught while accessing the caches key of " + loc + " in the incoming coverage zone file: " + ex.getMessage());
                    }
                }
			}

            if (!verifyOnly) {
				if (useDeep) {
                	deepInstance = root;
				}
				else {
					instance = root;
				}
            }

            return root;
        } catch (JSONException e) {
            LOGGER.warn(e,e);
        } catch (NetworkNodeException ex) {
            LOGGER.fatal(ex, ex);
        }

        return null;
    }
    public NetworkNode(final String str) throws NetworkNodeException {
        this(str, null);
    }

    public NetworkNode(final String str, final String loc) throws NetworkNodeException {
        this(str, loc, null);
    }

    public NetworkNode(final String str, final String loc, final Geolocation geolocation) throws NetworkNodeException {
        this.loc = loc;
        this.geolocation = geolocation;
        cidrAddress = CidrAddress.fromString(str);
    }

    public NetworkNode getNetwork(final String ip) throws NetworkNodeException {
        return getNetwork(new NetworkNode(ip));
    }

    public NetworkNode getNetwork(final NetworkNode ipnn) {
        if (this.compareTo(ipnn) != 0) {
            return null;
        }

        if (children == null) {
            return this;
        }

        final NetworkNode c = children.get(ipnn);

        if (c == null) {
            return this;
        }

        return c.getNetwork(ipnn);
    }

    public Boolean add(final NetworkNode nn) {
        synchronized(this) {
            if (children == null) {
                children = new TreeMap<NetworkNode,NetworkNode>();
            }

            return add(children, nn);
        }
    }

    protected Boolean add(final Map<NetworkNode,NetworkNode> children, final NetworkNode networkNode) {
        if (compareTo(networkNode) != 0) {
            return false;
        }

        for (final NetworkNode child : children.values()) {
            if (child.cidrAddress.equals(networkNode.cidrAddress)) {
                return false;
            }
        }

        final List<NetworkNode> movedChildren = new ArrayList<NetworkNode>();

        for (final NetworkNode child : children.values()) {
            if (networkNode.cidrAddress.includesAddress(child.cidrAddress)) {
                movedChildren.add(child);
                networkNode.add(child);
            }
        }

        for (final NetworkNode movedChild : movedChildren) {
            children.remove(movedChild);
        }

        for (final NetworkNode child : children.values()) {
            if (child.cidrAddress.includesAddress(networkNode.cidrAddress)) {
                return child.add(networkNode);
            }
        }

        children.put(networkNode, networkNode);
        return true;
    }

    public String getLoc() {
        return loc;
    }

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public CacheLocation getCacheLocation() {
        return cacheLocation;
    }

    public void setCacheLocation(final CacheLocation cacheLocation) {
        this.cacheLocation = cacheLocation;
    }

    public int size() {
        if (children == null) {
            return 1;
        }

        int size = 1;

        for (final NetworkNode child : children.keySet()) {
            size += child.size();
        }

        return size;
    }

    public void clearCacheLocations() {
        synchronized(this) {
            cacheLocation = null;

            if (this instanceof SuperNode) {
                final SuperNode superNode = (SuperNode) this;

                if (superNode.children6 != null) {
                    for (final NetworkNode child : superNode.children6.keySet()) {
                        child.clearCacheLocations();
                    }
                }
            }

            if (children != null) {
                for (final NetworkNode child : children.keySet()) {
                    child.clearCacheLocations();
                }
            }
        }
    }

    public static class SuperNode extends NetworkNode {
        private Map<NetworkNode, NetworkNode> children6;

        public SuperNode() throws NetworkNodeException {
            super(DEFAULT_SUB_STR);
        }

        public Boolean add6(final NetworkNode nn) {
            if(children6 == null) {
                children6 = new TreeMap<NetworkNode,NetworkNode>();
            }
            return add(children6, nn);
        }

        public NetworkNode getNetwork(final String ip) throws NetworkNodeException {
            final NetworkNode nn = new NetworkNode(ip);
            if (nn.cidrAddress.isIpV6()) {
                return getNetwork6(nn);
            }
            return getNetwork(nn);
        }

        public NetworkNode getNetwork6(final NetworkNode networkNode) {
            if (children6 == null) {
                return this;
            }

            final NetworkNode c = children6.get(networkNode);

            if (c == null) {
                return this;
            }

            return c.getNetwork(networkNode);
        }
    }

    @Override
    public int compareTo(final NetworkNode other) {
        return cidrAddress.compareTo(other.cidrAddress);
    }

    public String toString() {
        String str = "";
        try {
            str = InetAddress.getByAddress(cidrAddress.getHostBytes()).toString().replace("/", "");
        } catch (UnknownHostException e) {
            LOGGER.warn(e,e);
        }

        return "[" + str + "/" + cidrAddress.getNetmaskLength() + "] - location:" + this.getLoc();
    }
}
